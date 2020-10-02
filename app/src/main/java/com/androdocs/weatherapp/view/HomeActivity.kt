package com.androdocs.weatherapp.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.androdocs.weatherapp.R
import com.androdocs.weatherapp.Model.SharedPreferences
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var preferenceModel: SharedPreferences
    var context = this


    var CITY: String = "Udupi,India"
    val API: String = "5086ee31682cb35c05cb1f0d9bf0c676"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferenceModel =
            SharedPreferences(context)
        toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        setSupportActionBar(toolbar)
        supportActionBar?.setLogo(R.drawable.logo)
        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation(tv_location)
        } else {
            ActivityCompat.requestPermissions(
                this as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )

            Log.d("Loc2", CITY)

        }
        Log.d("Loc", CITY)


        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mitem1 -> startActivity(
                    Intent(
                        this,
                        HomeActivity::class.java
                    )
                )
                R.id.mitem2 -> startActivity(
                    Intent(
                        this,
                        FavouriteActivity::class.java
                    )
                )
                R.id.mitem3 -> startActivity(Intent(this, SearchActivity::class.java))
            }
            true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.homesearchbar -> {
                val intent = PlaceAutocomplete.IntentBuilder()
                    .accessToken("pk.eyJ1Ijoic3VzaG1pdGhhc2hldDEyMyIsImEiOiJja2ZjYjNndHkxZTZjMnpxc2k4bndnaTJ2In0.YtNvTh7-VtuL_0lJ1X-5Kg")
                    .placeOptions(PlaceOptions.builder().build(PlaceOptions.MODE_CARDS))
                    .build(this)

                startActivityForResult(intent, 1)
            }
        }
        return true
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            var feature = PlaceAutocomplete.getPlace(data)

            var searchValue = feature.text()!!
            CITY = searchValue
            preferenceModel.saveToRecentSearch(CITY)
            weatherTask().execute()
            Log.d("LogReport", "" + searchValue)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    @SuppressLint("StaticFieldLeak")
    inner class weatherTask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            /* Showing the ProgressBar, Making the main design GONE */
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            // findViewById<RelativeLayout>(R.id.layout).visibility = View.GONE
            findViewById<TextView>(R.id.errorText).visibility = View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            var response: String?
            try {
                response =
                    URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY &units=metric&appid=$API").readText(
                        Charsets.UTF_8
                    )
            } catch (e: Exception) {
                response = null
            }
            return response
        }


        @SuppressLint("ResourceAsColor")
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                /* Extracting JSON returns from the API */
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt: Long = jsonObj.getLong("dt")

                val textView: TextView = findViewById(R.id.tv_datetime)
                val simpleDateFormat = SimpleDateFormat("E, dd MMM yyy hh:mm a")
                val currentDateAndTime: String = simpleDateFormat.format(Date())
                textView.text = currentDateAndTime

                var temp = main.getString("temp") + "°"
                var tempMinMax =
                    "" + main.getString("temp_min") + "°" + "-" + main.getString("temp_max") + "°"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity") + "km/h"
                val visiblility = jsonObj.getString("visibility") + "km"
                val windSpeed = wind.getString("speed") + "%"
                val weatherDescription = weather.getString("description")


                val address = jsonObj.getString("name") + ", " + sys.getString("country")
                val id = weather.getString("id").toInt()
                if (id < 300) {
                    iv_weathericon.setBackgroundResource(R.drawable.icon_thunderstorm_big)


                } else if (id < 500) {
                    iv_weathericon.setBackgroundResource(R.drawable.icon_rain_big)


                } else if (id < 600) {
                    iv_weathericon.setBackgroundResource(R.drawable.icon_rain_big)


                } else if (id < 700) {
                    iv_weathericon.setBackgroundResource(R.drawable.icon_partially_cloudy_big)


                } else if (id < 800) {
                    iv_weathericon.setBackgroundResource(R.drawable.icon_mostly_sunny)


                } else if (id == 800) {
                    iv_weathericon.setBackgroundResource(R.drawable.icon_mostly_sunny)


                } else {
                    iv_weathericon.setBackgroundResource(R.drawable.icon_mostly_cloudy_big)

                }


                /* Populating extracted data into our views */
                findViewById<TextView>(R.id.tv_location).text = address

                findViewById<TextView>(R.id.tv_status).text = weatherDescription.capitalize()
                findViewById<TextView>(R.id.tv_temp).text = temp
                findViewById<TextView>(R.id.tv_minmax).text = tempMinMax

                findViewById<TextView>(R.id.tv_wind).text = windSpeed

                findViewById<TextView>(R.id.tv_humidity).text = humidity

                findViewById<TextView>(R.id.tv_visibility).text = visiblility
                var flag = 0
                for (i in preferenceModel.loadFavouriteList()) {
                    Log.d("Ciyt", i.cityName)
                    if (i.cityName == CITY) {
                        flag = 1
                        break

                    }

                }
                if (flag == 1) {
                    findViewById<CheckBox>(R.id.checkBox).isChecked = true
                } else {
                    findViewById<CheckBox>(R.id.checkBox).isChecked = false
                }

                radioCelsius.setOnClickListener() {
                    radioCelsius.let {
                        it.setBackgroundResource(R.color.white)
                        it.setTextColor(Color.parseColor("#E32843"))

                    }
                    radioFahrenheit.let {
                        it.setBackgroundResource(R.color.transparent)
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                    }

                    var temp = ((main.getString("temp").toDouble()) - 273.15).toInt()
                    findViewById<TextView>(R.id.tv_temp).text = temp.toString() + "°"
                    var tempmin = (((main.getString("temp_min").toDouble()) - 273.15)).toInt()
                        .toString() + "°"
                    var tempmax = (((main.getString("temp_max").toDouble()) - 273.15)).toInt()
                        .toString() + "°"
                    var tempMinMax = tempmin + "-" + tempmax
                    findViewById<TextView>(R.id.tv_minmax).text = tempMinMax

                }


                radioFahrenheit.setOnClickListener() {
                    radioFahrenheit.let {
                        it.setBackgroundResource(R.color.white)
                        it.setTextColor(Color.parseColor("#E32843"))
                    }
                    radioCelsius.let {
                        it.setBackgroundResource(R.color.transparent)
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                    var temp =
                        ((((main.getString("temp").toDouble()) - 273.15) * 9 / 5) + 32).toInt()
                    findViewById<TextView>(R.id.tv_temp).text = temp.toString() + "°"
                    var tempmin = (((((main.getString("temp_min")
                        .toDouble()) - 273.15) * 9 / 5) + 32)).toInt().toString() + "°"
                    var tempmax = (((((main.getString("temp_max")
                        .toDouble()) - 273.15) * 9 / 5) + 32)).toInt().toString() + "°"
                    var tempMinMax = tempmin + "-" + tempmax
                    findViewById<TextView>(R.id.tv_minmax).text = tempMinMax


                }
                checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        AddTofav.text = "Added to favourites"
                        AddTofav.setTextColor(
                            ContextCompat.getColor(
                                this@HomeActivity,
                                R.color.yello
                            )
                        )
                        preferenceModel.saveToFavouritesList(CITY)
                        Log.d("check", isChecked.toString())
                    } else {
                        AddTofav.text = "Add to favourites"
                        AddTofav.setTextColor(
                            ContextCompat.getColor(
                                this@HomeActivity,
                                R.color.fav
                            )
                        )
                        preferenceModel.removeSingleItem(CITY)

                    }
                }


            } catch (e: Exception) {
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE

            }

        }

    }

    fun getCurrentLocation(tv_location: TextView) {
        val fusedLocationProviderClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            return
        }
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {
            val location = it.getResult()
            val geocoder: Geocoder = Geocoder(this)
            var address: List<Address> =
                geocoder.getFromLocation(location.latitude, location.longitude, 1)
            tv_location.text = "${address.get(0).subAdminArea}"
            tv_location.text = "${address.get(0).subAdminArea}, ${address.get(0).adminArea}"
            //tv_location.text= address[0].locality  + ", " + address[0].subAdminArea + ", " + address[0].adminArea

            CITY = tv_location.text.toString()
            weatherTask().execute()
            //Log.d("loc1",address.get(0).subAdminArea)
        }
    }

   // fun String.replaceAndCapitalize() = this.replace("\"", "").split(" ").joinToString(" ") { it.capitalize() }.trimEnd()


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                getCurrentLocation(tv_location)
            }
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
}









