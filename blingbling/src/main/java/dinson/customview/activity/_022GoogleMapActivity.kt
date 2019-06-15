package dinson.customview.activity

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.tbruyelle.rxpermissions2.RxPermissions
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.kotlin.logi
import io.reactivex.rxkotlin.addTo


class _022GoogleMapActivity : BaseActivity(),
    OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    GoogleMap.OnMarkerClickListener,
    LocationListener {


    private lateinit var mMap: GoogleMap
    private val mGoogleApiClient by lazy {
        GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
    }
    private var mLastLocation: Location? = null

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__022_google_map)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    override fun onStart() {
        super.onStart()
        mGoogleApiClient.connect()
    }

    override fun onStop() {
        super.onStop()
        if (mGoogleApiClient.isConnected) mGoogleApiClient.disconnect()
    }

    /**
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * Device must installed Google Play services.
     */

    override fun onMapReady(googleMap: GoogleMap) {

        logi("onMapReady...")

        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        // Add a marker in Sydney and move the camera
        /*val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/
        val myPlace = LatLng(40.73, -73.99)  // this is New York
        mMap.addMarker(MarkerOptions().position(myPlace).title("My Favorite City"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPlace, 12f))
    }

    /**
     * GoogleMap连接暂停
     */
    override fun onConnectionSuspended(p0: Int) {
        logi("onConnectionSuspended...")
    }

    /**
     * GoogleMap连接失败
     */
    override fun onConnectionFailed(p0: ConnectionResult) {
        logi("onConnectionFailed...")
    }

    /**
     * GoogleMap连接完毕
     */
    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        logi("onConnected...")


        RxPermissions(this).request(ACCESS_FINE_LOCATION)
            .subscribe {
                if (!it) return@subscribe
                /*  val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                var myLocation: Location? = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                 if (myLocation == null) {
                     val criteria = Criteria()
                     criteria.accuracy = Criteria.ACCURACY_COARSE
                     val provider = lm.getBestProvider(criteria, true)
                     myLocation = lm.getLastKnownLocation(provider)
                 }

                 info(myLocation.toString() + "-----")*/

                mMap.isMyLocationEnabled = true
               /* val locationAvailability =
                    LocationServices.getFusedLocationProviderClient(this).locationAvailability
                //LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient)
                if (locationAvailability.isSuccessful) {
                    // 3
                    LocationServices.getFusedLocationProviderClient(this).lastLocation.let {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.result.latitude, it.result.longitude), 12f))
                    }
                }*/
                // 2


                logi("------1")


                val locationManager = getSystemService (Context.LOCATION_SERVICE) as LocationManager
               /* locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, 0f, )*/
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, 0f,object :android.location.LocationListener{
                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                    }

                    override fun onProviderEnabled(provider: String?) {
                    }

                    override fun onProviderDisabled(provider: String?) {
                    }

                    override fun onLocationChanged(location: Location?) {


                    }

                })



                val locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient)
                if (null != locationAvailability && locationAvailability.isLocationAvailable) {
                    // 3
                    logi("------2")
                    val  mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
                    // 4
                    if (mLastLocation != null) {
                        logi("------3")
                        val currentLocation = LatLng(mLastLocation.latitude, mLastLocation
                            .longitude)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12f))
                    }
                }
            }.addToManaged()
    }

    /**
     * GoogleMarkerClick点击
     */
    override fun onMarkerClick(p0: Marker?): Boolean {
        logi("onMarkerClick...")
        return true
    }

    /**
     * 定位回调
     */
    override fun onLocationChanged(p0: Location?) {
        logi("onLocationChanged...")

    }
}
