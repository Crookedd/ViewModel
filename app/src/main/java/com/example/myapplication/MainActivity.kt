package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import viewmodel.WeatherViewModel

class MainActivity : AppCompatActivity() {
    lateinit var editText : EditText
    lateinit var buttonWeather : Button
    lateinit var textViewCity: TextView
    lateinit var textViewTemperature: TextView
    lateinit var textViewDescription: TextView
    lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textViewCity = findViewById(R.id.textView)
        textViewTemperature = findViewById(R.id.textView2)
        textViewDescription = findViewById(R.id.textView3)
        editText = findViewById(R.id.editTextText)
        buttonWeather = findViewById(R.id.button)
        image = findViewById(R.id.imageView)
        val weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        buttonWeather.setOnClickListener() {
            if (editText.text.toString() != "") {
                weatherViewModel.getCurrentWeather(editText.text.toString())
            }
        }
        weatherViewModel.weatherData.observe(this, Observer { response ->
            val weather = response.weather[0]
            val main = response.main
            val iconc = response.weather[0]


            textViewCity.text = "Город: ${response.name}"
            textViewTemperature.text = "Температура: ${main.temp}°C"
            textViewDescription.text = "Описание: ${weather.description}"
            Picasso.get().load("https://openweathermap.org/img/wn/${iconc.icon}.png").into(image)
        })

    }
}
interface RetrofitServices {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") cityName: String?,
        @Query("appid") apiKey: String?,
        @Query("units") units: String,
        @Query("lang") lang: String
    ):WeatherData
}

object RetrofitClient {
    private var retrofit: Retrofit? = null

    fun getClient(baseUrl: String): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}
data class Weather(
    val description: String,
    val icon: String,
    val temp: Double
)
data class WeatherData(
    val weather: List<Weather>,
    val name: String,
    val main: Main
)
data class Main(
    val temp: Double,
)

