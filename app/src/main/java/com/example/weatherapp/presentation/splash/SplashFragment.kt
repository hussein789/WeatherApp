package com.example.weatherapp.presentation.splash

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.WeatherApp
import com.example.weatherapp.databinding.SplashFragmentBinding
import javax.inject.Inject

class SplashFragment : Fragment() {

    private lateinit var viewModel: SplashViewModel
    lateinit var binding:SplashFragmentBinding

    @Inject
    lateinit var factory: SplashViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WeatherApp.appComponent.inject(this)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SplashFragmentBinding.inflate(
            inflater,container,false
        )

        viewModel = ViewModelProvider(this,factory).get(SplashViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.showLoadingLD.observe(viewLifecycleOwner, Observer { show ->
            show?.let { handleProgress(show) }
        })
        viewModel.navigateToWeatherScreenLD.observe(viewLifecycleOwner, Observer { navigate ->
            navigate?.let { navigateToWeatherScreen(navigate) }
        })
    }

    private fun navigateToWeatherScreen(navigate: Boolean) {
        if(navigate)
            findNavController().navigate(R.id.action_splashFragment_to_weatherHomeFragment)
    }

    private fun handleProgress(show: Boolean) {
        binding.splashProgress.visibility = if(show) View.VISIBLE else View.GONE
    }
}