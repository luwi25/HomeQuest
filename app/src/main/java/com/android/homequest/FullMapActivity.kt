package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class FullMapActivity : FragmentActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_map)

        val button_back = findViewById<ImageButton>(R.id.button_back)
        button_back.setOnClickListener {
            startActivity(
                Intent(this, ParentDashboardActivity::class.java)
            )
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.full_map_fragment) as SupportMapFragment

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Optional: Zoom into a specific location
        val location = LatLng(-33.852, 151.211) // Sydney as example
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14f))
        map.addMarker(MarkerOptions().position(location).title("Marker in Sydney"))
    }
}