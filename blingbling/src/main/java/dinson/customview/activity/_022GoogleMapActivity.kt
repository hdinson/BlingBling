package dinson.customview.activity

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.location.Location
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
        mGoogleApiClient.context
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
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        /*val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/


        RxPermissions(this).request(ACCESS_COARSE_LOCATION)
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
                val locationAvailability =
                    LocationServices.getFusedLocationProviderClient(this).locationAvailability
                //LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient)
                if (locationAvailability.isSuccessful) {
                    // 3
                    LocationServices.getFusedLocationProviderClient(this).lastLocation.let {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.result.latitude, it.result.longitude), 12f))
                    }
                }
            }

    }

    /**
     * GoogleMap连接暂停
     */
    override fun onConnectionSuspended(p0: Int) {

    }

    /**
     * GoogleMap连接失败
     */
    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    /**
     * GoogleMap连接完毕
     */
    override fun onConnected(p0: Bundle?) {

    }

    /**
     * GoogleMarkerClick点击
     */
    override fun onMarkerClick(p0: Marker?): Boolean {
        return true
    }

    /**
     * 定位回调
     */
    override fun onLocationChanged(p0: Location?) {


    }
}
