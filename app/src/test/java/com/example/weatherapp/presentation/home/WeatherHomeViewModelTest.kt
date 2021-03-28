package com.example.weatherapp.presentation.home

import BaseTestClass
import android.content.Context
import android.location.Location
import captureValues
import com.example.weatherapp.domain.GetWeatherInfoUseCase
import com.example.weatherapp.domain.IsAnyProviderAvailableUseCase
import com.example.weatherapp.domain.IsLocationServiceEnabledUseCase
import com.example.weatherapp.domain.MyLocationUseCase
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import getValueForTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.mockito.Mockito.mock
import java.lang.RuntimeException

class WeatherHomeViewModelTest : BaseTestClass(){

    lateinit var viewModel: WeatherHomeViewModel

    private val context = mock(Context::class.java)
    private val locationUseCase = mock(MyLocationUseCase::class.java)
    private val getWeatherInfoUseCase = mock(GetWeatherInfoUseCase::class.java)
    private val isAnyProviderAvailableUseCase = mock(IsAnyProviderAvailableUseCase::class.java)
    private val isLocationServiceEnabledUseCase = mock(IsLocationServiceEnabledUseCase::class.java)
    private val weatherUIModel = mock(WeatherUIModel::class.java)
    private val location = mock(Location::class.java)

    private val validCityName = "London"
    private val invalidCityName = "Lon"

    @Test
    fun getWeatherByCityName_emptyCityName_launchEmptyState(){
        mockSuccessCase()

        viewModel.getWeatherByCityName("")

        val result = viewModel.showEmptyStateLD.getValueForTest()

        assertEquals(result, true)
    }

    @Test
    fun getWeatherByCityName_citName_callApi() = runBlockingTest {
        mockSuccessCase()


        viewModel.getWeatherByCityName(validCityName)

        verify(getWeatherInfoUseCase,times(1)).execute(validCityName)

    }

    @Test
    fun getWeatherByCityName_validCitName_showContent() = runBlockingTest {
        mockSuccessCase()

        viewModel.getWeatherByCityName(validCityName)

        val result = viewModel.showContentLD.getValueForTest()

        assertEquals(result,true)
    }

    @Test
    fun getWeatherByCityName_validCityName_emitDataFromUseCase(){
        mockSuccessCase()

        viewModel.getWeatherByCityName(validCityName)

        val result = viewModel.weatherLD.getValueForTest()

        assertEquals(result,weatherUIModel)
    }

    @Test
    fun getWeatherByCityName_cityName_showLoadingWhenStartAndHideAfterFinish(){
        mockSuccessCase()

        val cityName = "London"

        viewModel.loadingLD.captureValues {
            viewModel.getWeatherByCityName(cityName)

            assertEquals(values.first(),true)
            assertEquals(values.last(),false)
        }
    }

    @Test
    fun getWeatherByCityName_invalidCityName_showErrorText(){
        mockFailureCase()

        viewModel.getWeatherByCityName(invalidCityName)

        val result = viewModel.errorLD.getValueForTest()

        assertEquals(result,true)
    }

    @Test
    fun getWeatherForCurrentLocation_locatoinServiceEnabledAndThereIsProvider_invokeLocationService() = runBlockingTest{

        whenever(isLocationServiceEnabledUseCase.execute()).thenReturn(true)
        whenever(isAnyProviderAvailableUseCase.execute()).thenReturn(true)
        viewModel = WeatherHomeViewModel(getWeatherInfoUseCase,locationUseCase,context,isLocationServiceEnabledUseCase,isAnyProviderAvailableUseCase)

        viewModel.getWeatherForCurrentLocation()

        verify(locationUseCase, times(1)).execute()
    }

    @Test
    fun getWeatherForCurrentLocation_locationServiceDisabledAndThereIsProvider_notReturnLocation() = runBlockingTest{

        whenever(isLocationServiceEnabledUseCase.execute()).thenReturn(false)
        whenever(isAnyProviderAvailableUseCase.execute()).thenReturn(true)
        viewModel = WeatherHomeViewModel(getWeatherInfoUseCase,locationUseCase,context,isLocationServiceEnabledUseCase,isAnyProviderAvailableUseCase)

        viewModel.getWeatherForCurrentLocation()

        verify(locationUseCase, times(0)).execute()
    }

    @Test
    fun getWeatherForCurrentLocation_locationServiceEnabledAndThereIsNoProvider_notReturnLocation() = runBlockingTest{

        whenever(isLocationServiceEnabledUseCase.execute()).thenReturn(true)
        whenever(isAnyProviderAvailableUseCase.execute()).thenReturn(false)
        viewModel = WeatherHomeViewModel(getWeatherInfoUseCase,locationUseCase,context,isLocationServiceEnabledUseCase,isAnyProviderAvailableUseCase)

        viewModel.getWeatherForCurrentLocation()

        verify(locationUseCase, times(0)).execute()
    }

    @Test
    fun onSearchTextChanged_updateWeatherWithNewCity() = runBlockingTest{
        mockSuccessCase()

        viewModel.onSearchTextChanged("New City")

        verify(getWeatherInfoUseCase, times(1)).execute("New City")
    }

    @Test
    fun setOnClearTextIconClicked_setEditTextToEmpty(){
        mockSuccessCase()

        viewModel.setOnClearTextIconClicked()

        val result = viewModel.searchedCityNameLD.getValueForTest()

        assertEquals(result,"")
    }

    private fun mockSuccessCase() = runBlockingTest{
        whenever(getWeatherInfoUseCase.execute(validCityName)).thenReturn(
            flow{
                emit(Result.success(weatherUIModel))
            }
        )
        viewModel = WeatherHomeViewModel(getWeatherInfoUseCase,locationUseCase,context,
        isLocationServiceEnabledUseCase,isAnyProviderAvailableUseCase)
    }

    private fun mockFailureCase() = runBlockingTest{
        whenever(getWeatherInfoUseCase.execute(invalidCityName)).thenThrow(RuntimeException(""))
        viewModel = WeatherHomeViewModel(getWeatherInfoUseCase,locationUseCase,context,
        isLocationServiceEnabledUseCase,isAnyProviderAvailableUseCase)
    }


}