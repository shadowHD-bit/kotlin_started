package com.example.lab7_krivikov

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.provider.Settings
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    private val MY_PERMISSIONS_REQUEST_LOCATION = 1
    private var locationManager: LocationManager? = null
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            showInfo(location)
        }

        override fun onProviderDisabled(provider: String) {
            showInfo()
        }

        override fun onProviderEnabled(provider: String) {
            showInfo()
        }

        override fun onStatusChanged(
            provider: String, status: Int,
            extras: Bundle
        ) {
            showInfo()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        // Проверяем есть ли разрешение
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            // Разрешения нет. Нужно ли показать пользователю пояснения?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Показываем пояснения
            } else {
                // Пояснений не требуется, запрашиваем разрешение
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        } else {
            // Разрешение есть, выполняем требуемое действие
        }
    }

    fun startTracking() {
        // Проверяем есть ли разрешение
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            // Здесь код работы с разрешениями...
        } else {
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000, 10f, locationListener
            )
            locationManager!!.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000, 10f, locationListener
            )
            showInfo()
        }
    }

    override fun onResume() {
        super.onResume()
        startTracking()
    }

    override fun onPause() {
        super.onPause()
        stopTracking()
    }

    private fun showInfo(location: Location? = null) {
        val isGpsOn = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkOn = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        findViewById<TextView>(R.id.gps_status).text =
            if (isGpsOn) "GPS ON" else "GPS OFF"
        findViewById<TextView>(R.id.network_status).text =
            if (isNetworkOn) "Network ON" else "Network OFF"
        if (location != null) {
            if (location.provider == LocationManager.GPS_PROVIDER) {
                this.currentLongitudeGPS = location.longitude
                this.currentLatitudeGPS = location.latitude
                findViewById<TextView>(R.id.gps_coords).text =
                    "GPS: широта = " + location.latitude.toString() +
                            ", долгота = " + location.longitude.toString()
            }
            if (location.provider == LocationManager.NETWORK_PROVIDER) {
                this.currentLongitudeNetwork = location.longitude
                this.currentLatitudeNetwork = location.latitude
                findViewById<TextView>(R.id.network_coords).text =
                    "Network: широта = " + location.latitude.toString() +
                            ", долгота = " + location.longitude.toString()
            }
        }
    }

    private var userLatitude = 0.0
    private var userLongitude = 0.0

    private var currentLongitudeGPS = 0.0;
    private var currentLatitudeGPS = 0.0;
    private var currentLongitudeNetwork = 0.0;
    private var currentLatitudeNetwork = 0.0;
    private var resultsGpsLocation = FloatArray(1)
    private var resultsNetworkLocation = FloatArray(1)


    fun setLocationUser(view: View) {

        this.userLatitude = findViewById<EditText>(R.id.latitudeUser).text.toString().toDouble();
        this.userLongitude = findViewById<EditText>(R.id.longitudeUser).text.toString().toDouble()

        Location.distanceBetween(
            this.currentLongitudeGPS,
            this.currentLatitudeGPS,
            this.userLongitude,
            this.userLatitude,
            this.resultsGpsLocation
        );
        Location.distanceBetween(
            this.currentLatitudeNetwork,
            this.currentLongitudeNetwork,
            this.userLongitude,
            this.userLatitude,
            this.resultsNetworkLocation
        );
        var check100meterstToGps = resultsGpsLocation[0]
        var check100meterstToNetwork = resultsNetworkLocation[0]

        if (check100meterstToNetwork <= 100 || check100meterstToGps <= 100) {
            findViewById<TextView>(R.id.textPoint).text =
                "Полученные данные: широта = " + userLatitude.toString() +
                        ", долгота = " + userLongitude.toString() + ". Данная позиция в зоне 100 метров около вас!"

    }
    else
    {
        findViewById<TextView>(R.id.textPoint).text =
            "Полученные данные: широта = " + userLatitude.toString() +
                    ", долгота = " + userLongitude.toString()
    }
}

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            // Разрешение есть, заново выполняем требуемое действие
        }
        else {
            // Разрешения нет...
        }
    }

    fun stopTracking() {
        locationManager!!.removeUpdates(locationListener)
    }

    fun buttonOpenSettings(view: View) {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:$packageName"))
        startActivity(intent)
    }

    fun setLocationButtonClick(view: View){
        setLocationUser(view)
    }

}