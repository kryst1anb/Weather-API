package com.example.weatherapi

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity() {

    private var disposable: Disposable? = null

    private val OpenWeatherMapApiService by lazy {
        WeatherAPIService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchCityButton.setOnClickListener {
            if (searchCity.text.toString().isNotEmpty()) {

                searchWeather(searchCity.text.toString())
                val inputMethod =
                        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethod.hideSoftInputFromWindow(searchCityButton.windowToken, 0)
            } else {
                Toast.makeText(this, "Field CITY cannot be empty", Toast.LENGTH_LONG).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun searchWeather(searchString: String) {
        disposable = OpenWeatherMapApiService.query(
                        searchString,//JEŻELI CHCĘ SIĘ TYLKO MIASTA POLSKI DOPISAC >   +",pl",   <
                        "b314601205f4c241d94fa31f7c339a88",
                        "metric",
                        "en"
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { response ->
                            if (response.cod == "200") {

                                val time =
                                        "${takeTime((response.dt.toLong() + response.timezone.toLong()) * 1000L)}"
                                val sunriseTime =
                                        "${takeTime((response.sys.sunrise.toLong() + response.timezone.toLong()) * 1000L)}"
                                val sunsetTime =
                                        "${takeTime((response.sys.sunset.toLong() + response.timezone.toLong()) * 1000L)}"

                                val temperature = response.main.temp.toFloat()
                                val temperature_min = response.main.temp_min.toFloat()
                                val temperature_max = response.main.temp_max.toFloat()

                                city.text = "${response.name}, ${response.sys.country}"
                                temp.text = "%.1f".format(temperature) + " °C"
                                arrowDownDesc.text = "%.1f".format(temperature_min) + " °C"
                                arrowUPDesc.text = "%.1f".format(temperature_max) + " °C"
                                pressure.text = "${response.main.pressure} hPa"
                                description.text = response.weather[0].description

                                humidity.text = "${response.main.humidity}%"
                                date.text = "${takeDate(
                                        (response.dt.toLong() + response.timezone.toLong() - 3600) * 1000L,
                                        "dd-MM-yyyy HH:mm"
                                )}"

                                sunrise.text = sunriseTime
                                sunset.text = sunsetTime

//  *************ABY POBIERANIE IKON DZIALALO TRZEBA ZROBIC JAKO ASYNC TASK***********************************//
//                        val url = URL("http://openweathermap.org/img/w/${result.weather[0].icon}.png")      //
//                        val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())      //
//                        icon.setImageBitmap(bitmap)                                                         //
//  **********************************************************************************************************//
                                // DEBUG CZASU
                                // Toast.makeText(this,time, Toast.LENGTH_LONG).show()
                                if (response.weather[0].main == "Clear" && (time < sunriseTime || time > sunsetTime)) {
                                    icon.setBackgroundResource(R.drawable.ic_night)
                                } else if (response.weather[0].main == "Clear") {
                                    icon.setBackgroundResource(R.drawable.ic_day)
                                } else if (response.weather[0].main == "Clouds" && (time < sunriseTime || time > sunsetTime)) {
                                    icon.setBackgroundResource(R.drawable.ic_night_cloudy)
                                } else if (response.weather[0].main == "Clouds" && (response.clouds.all > "25")) {
                                    icon.setBackgroundResource(R.drawable.ic_cloudy)
                                } else if (response.weather[0].main == "Clouds") {
                                    icon.setBackgroundResource(R.drawable.ic_day_cloudy)
                                } else if (response.weather[0].main == "Snow" && (time < sunriseTime || time > sunsetTime) && response.weather[0].description.contains(
                                                "light"
                                        )
                                ) {
                                    icon.setBackgroundResource(R.drawable.ic_night_snow)
                                } else if (response.weather[0].main == "Snow" && response.weather[0].description.contains(
                                                "light"
                                        )
                                ) {
                                    icon.setBackgroundResource(R.drawable.ic_day_snow)
                                } else if (response.weather[0].main == "Snow") {
                                    icon.setBackgroundResource(R.drawable.ic_snow)
                                } else if (response.weather[0].main == "Thunderstorm" && (time < sunriseTime || time > sunsetTime) && response.weather[0].description.contains(
                                                "light"
                                        )
                                ) {
                                    icon.setBackgroundResource(R.drawable.ic_night_storm)
                                } else if (response.weather[0].main == "Thunderstorm" && response.weather[0].description.contains(
                                                "light"
                                        )
                                ) {
                                    icon.setBackgroundResource(R.drawable.ic_day_storm)
                                } else if (response.weather[0].main == "Thunderstorm") {
                                    icon.setBackgroundResource(R.drawable.ic_storm)
                                } else if (response.weather[0].main == "Drizzle" && (time < sunriseTime || time > sunsetTime) && response.weather[0].description.contains(
                                                "light"
                                        )
                                ) {
                                    icon.setBackgroundResource(R.drawable.ic_night_drizzle)
                                } else if (response.weather[0].main == "Drizzle" && response.weather[0].description.contains(
                                                "light"
                                        )
                                ) {
                                    icon.setBackgroundResource(R.drawable.ic_day_drizzle)
                                } else if (response.weather[0].main == "Drizzle") {
                                    icon.setBackgroundResource(R.drawable.ic_drizzle)
                                } else if (response.weather[0].main == "Rain" && (time < sunriseTime || time > sunsetTime) && response.weather[0].description.contains(
                                                "light"
                                        )
                                ) {
                                    icon.setBackgroundResource(R.drawable.ic_night_rain)
                                } else if (response.weather[0].main == "Rain" && response.weather[0].description.contains(
                                                "light"
                                        )
                                ) {
                                    icon.setBackgroundResource(R.drawable.ic_day_rain)
                                } else if (response.weather[0].main == "Rain") {
                                    icon.setBackgroundResource(R.drawable.ic_rain)
                                } else if (response.weather[0].main == "Mist" || response.weather[0].main == "Smoke" || response.weather[0].main == "Haze" || response.weather[0].main == "Dust" || response.weather[0].main == "Fog" || response.weather[0].main == "Sand" || response.weather[0].main == "Dust" || response.weather[0].main == "Ash" || response.weather[0].main == "Squall" || response.weather[0].main == "Tornado" && (time < sunriseTime || time > sunsetTime)) {
                                    icon.setBackgroundResource(R.drawable.ic_night_fog)
                                } else if (response.weather[0].main == "Mist" && response.weather[0].main == "Smoke" && response.weather[0].main == "Haze" && response.weather[0].main == "Dust" || response.weather[0].main == "Fog" || response.weather[0].main == "Sand" || response.weather[0].main == "Dust" || response.weather[0].main == "Ash" || response.weather[0].main == "Squall" || response.weather[0].main == "Tornado") {
                                    icon.setBackgroundResource(R.drawable.ic_day_fog)
                                }

                                arrowDown.setBackgroundResource(R.drawable.ic_arrow_down)
                                arrowUP.setBackgroundResource(R.drawable.ic_arrow_up)
                                humidityIcon.setBackgroundResource(R.drawable.ic_drop)
                                sunriseIcon.setBackgroundResource(R.drawable.ic_sunrise)
                                sunsetIcon.setBackgroundResource(R.drawable.ic_sunset)

                                if (time > sunriseTime && time < sunsetTime) {
                                    background.setBackgroundResource(R.drawable.day)
                                } else {
                                    background.setBackgroundResource(R.drawable.night)
                                }
                                // DEBUG
                                // Toast.makeText(this, "City ${result.name}", Toast.LENGTH_SHORT).show()
                            }
                        }, { error ->
                    if (error.message!!.contains("404")) {
                        Toast.makeText(
                                this,
                                "City \'" + searchCity.text.toString() + "\' not found",
                                Toast.LENGTH_LONG
                        ).show()
                    } else if (error.message!!.contains("Unable to resolve host")) {
                        Toast.makeText(this, "Check internet connection", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
                    }
                }
                )
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    fun takeTime(milliSec: Long): String? {
        val time = String.format(
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSec) % 24,
                TimeUnit.MILLISECONDS.toMinutes(milliSec) % 60
        )
        return time
    }

    fun takeDate(milliSec: Long, dateFormat: String?): String? {
        val format = SimpleDateFormat(dateFormat)
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(milliSec)
        return format.format(calendar.getTime())
    }
}
