package com.example.weatherapp.presentation.home

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.WeatherApp
import com.example.weatherapp.databinding.WeatherHomeFragmentLayoutBinding
import com.example.weatherapp.domain.Utils
import java.util.*
import javax.inject.Inject


class WeatherHomeFragment : Fragment() {

    lateinit var viewModel: WeatherHomeViewModel
    lateinit var binding: WeatherHomeFragmentLayoutBinding

    @Inject
    lateinit var factory: WeatherHomeViewModelFactory

    companion object {
        val MY_PERMISSIONS_REQUEST_LOCATION = 99
        val MY_PERMISSION_ALLOW_LOCATION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WeatherApp.appComponent.inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = WeatherHomeFragmentLayoutBinding.inflate(inflater, container, false)
        Utils.setLightStatusBar(binding.root, requireActivity())
        viewModel = ViewModelProvider(this, factory).get(WeatherHomeViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initClickListeners()
        checkLocationPermission()
        observeViewModel()
    }

    private fun initViews() {
        binding.searchCityNameEditText.addTextChangedListener(object : TextWatcher {

            private var timer: Timer = Timer()
            private val DELAY: Long = 1000

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                timer.cancel()
                timer = Timer()
                timer.schedule(
                        object : TimerTask() {
                            override fun run() {
                                viewModel.onSearchTextChanged(s?.toString())
                            }
                        },
                        DELAY
                )
            }

        })

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            viewModel.getWeatherByCityName(binding.searchCityNameEditText.text.toString())
        }
    }

    fun checkLocationPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
            )
            false
        } else {
            viewModel.getWeatherForCurrentLocation()
            true
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {

                    if (ContextCompat.checkSelfPermission(
                                    requireActivity(),
                                    Manifest.permission.ACCESS_FINE_LOCATION
                            )
                            == PackageManager.PERMISSION_GRANTED
                    ) {

                        //Request location updates:
                        viewModel.getWeatherForCurrentLocation()
                    }
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.errorLD.observe(viewLifecycleOwner, Observer { show ->
            show?.let {
                handleErrorMessage(
                        show
                )
            }
        })
        viewModel.loadingLD.observe(viewLifecycleOwner, Observer { show ->
            show?.let {
                handleLoading(
                        show
                )
            }
        })
        viewModel.showContentLD.observe(viewLifecycleOwner, Observer { show ->
            show?.let {
                handleWeatherInfoVisibility(
                        show
                )
            }
        })
        viewModel.weatherLD.observe(viewLifecycleOwner, Observer { weatherInfo ->
            weatherInfo?.let {
                handleWeatherData(
                        weatherInfo
                )
            }
        })
        viewModel.searchedCityNameLD.observe(viewLifecycleOwner, Observer { cityName ->
            cityName?.let { handleSearchedCityName(cityName) }
        })
        viewModel.showLocationPermissionLD.observe(viewLifecycleOwner, Observer { show ->
            show?.let { handleOpenLocationSettings() }
        })

        viewModel.showEmptyStateLD.observe(viewLifecycleOwner, Observer { show ->
            show?.let { handleEmptyState() }
        })
    }

    private fun handleEmptyState() {
        binding.emptyStateInclude.visibility = View.VISIBLE
        binding.errorText.visibility = View.GONE
        binding.weatherInfo.visibility = View.GONE
    }

    private fun handleSearchedCityName(cityName: String) {
        binding.searchCityNameEditText.setText(cityName)
    }

    private fun handleWeatherData(info: WeatherUIModel) {
        with(binding) {
            Glide.with(requireActivity())
                    .load("https://openweathermap.org/img/wn/${info.conditionIcon}.png")
                    .into(binding.conditionIcon)

            temp.text = getString(R.string.degree, info.temp.toString())
            tempMinText.text = getString(R.string.degree, info.minTemp.toString())
            tempMaxText.text = getString(R.string.degree, info.maxTemp.toString())
            condition.text = info.condition
            wind.text = getString(R.string.wind_speed, info.wind.toString())
            humidity.text = "${getString(R.string.humidity, info.humidity.toString())}%"
            clouds.text = "${getString(R.string.cloud_cover, info.clouds.toString())}%"
            pressure.text = getString(R.string.pressure, info.pressure.toString())
            sunrise.text = getString(R.string.sunrise, info.sunrise)
            sunset.text = getString(R.string.sunset, info.sunset)
            todayDate.text = info.todayDate
            todayTime.text = info.todayTime
            coordText.text = getString(R.string.lat_lng,info.lat,info.lng)
        }
    }

    private fun handleWeatherInfoVisibility(show: Boolean) {
        if (show) {
            binding.errorText.visibility = View.GONE
            binding.emptyStateInclude.visibility = View.GONE
            binding.weatherInfo.visibility = View.VISIBLE
        }
    }

    private fun handleLoading(show: Boolean) {
        binding.progress.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun handleErrorMessage(show: Boolean) {
        if (show) {
            binding.errorText.text = getString(R.string.no_results_found,binding.searchCityNameEditText.text.toString())
            binding.errorText.visibility = View.VISIBLE
            binding.emptyStateInclude.visibility = View.GONE
            binding.weatherInfo.visibility = View.GONE
        }
    }

    private fun initClickListeners() {
        binding.locateMeContainer.setOnClickListener {
            checkLocationPermission()
        }

        binding.clearTextIcon.setOnClickListener {
            viewModel.setOnClearTextIconClicked()
        }
    }

    private fun handleOpenLocationSettings() {
        val dialog = AlertDialog.Builder(requireActivity())
                .setTitle(getString(R.string.location_permission))
                .setMessage(getString(R.string.location_permission_message))
                .setPositiveButton(getString(R.string.settings)) { dialog, which ->
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivityForResult(intent, MY_PERMISSION_ALLOW_LOCATION)
                    dialog?.dismiss()
                }
                .setNegativeButton(getString(R.string.cancel)) { dialog, which -> dialog?.dismiss() }
                .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            MY_PERMISSION_ALLOW_LOCATION -> {
                checkLocationPermission()
            }
        }
    }

}