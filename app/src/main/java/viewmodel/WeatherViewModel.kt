package viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.RetrofitClient
import com.example.myapplication.RetrofitServices
import com.example.myapplication.WeatherData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val retrofitService: RetrofitServices = RetrofitClient.getClient("https://api.openweathermap.org/data/2.5/").create(
        RetrofitServices::class.java)
    val weatherData: MutableLiveData<WeatherData> = MutableLiveData()

    fun getCurrentWeather(cityName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = retrofitService.getCurrentWeather(cityName, "ec9102a991f613fe3368ac64d021a84a", "metric", "ru")
                weatherData.postValue(response)
            }
            catch (ex : Exception) {}
        }
    }
}