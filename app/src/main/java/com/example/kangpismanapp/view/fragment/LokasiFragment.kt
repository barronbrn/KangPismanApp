package com.example.kangpismanapp.view.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kangpismanapp.R
import com.example.kangpismanapp.adapter.LokasiAdapter
import com.example.kangpismanapp.data.model.BankSampah
import com.example.kangpismanapp.viewmodel.LokasiViewModel
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@AndroidEntryPoint
class LokasiFragment : Fragment(R.layout.fragment_lokasi) {

    private val viewModel: LokasiViewModel by viewModels()
    private lateinit var mapView: MapView
    private lateinit var recyclerView: RecyclerView
    private lateinit var lokasiAdapter: LokasiAdapter

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) { getCurrentLocation() }
            else { Toast.makeText(requireContext(), "Izin lokasi dibutuhkan.", Toast.LENGTH_LONG).show() }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Configuration.getInstance().load(requireContext(), androidx.preference.PreferenceManager.getDefaultSharedPreferences(requireContext()))

        mapView = view.findViewById(R.id.map_view)
        recyclerView = view.findViewById(R.id.recycler_view_lokasi)

        setupRecyclerView()
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                getCurrentLocation()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) { return }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                observeViewModel(location)
            } else {
                Toast.makeText(requireContext(), "Gagal mendapatkan lokasi. Pastikan GPS aktif.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun observeViewModel(userLocation: Location) {
        if (!isAdded || view == null) {
            return
        }
        viewModel.bankSampahList.observe(viewLifecycleOwner) { bankList ->
            if (bankList.isNotEmpty()) {
                val sortedList = calculateDistancesAndSort(bankList, userLocation)
                updateUI(sortedList, userLocation)
            }
        }
    }

    private fun calculateDistancesAndSort(bankList: List<BankSampah>, userLocation: Location): List<BankSampah> {
        bankList.forEach { bank ->
            val bankLocation = Location("").apply {
                latitude = bank.lat
                longitude = bank.lng
            }
            bank.jarakInKm = userLocation.distanceTo(bankLocation) / 1000.0
        }
        return bankList.sortedBy { it.jarakInKm }
    }

    private fun updateUI(sortedList: List<BankSampah>, userLocation: Location) {
        lokasiAdapter.submitList(sortedList)
        setupMap(sortedList, userLocation)
    }

    private fun setupMap(sortedList: List<BankSampah>, userLocation: Location) {
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        val mapController = mapView.controller
        mapController.setZoom(13.0)

        val userGeoPoint = GeoPoint(userLocation.latitude, userLocation.longitude)
        mapController.setCenter(userGeoPoint)
        mapView.overlays.clear()

        // --- PENANDA UNTUK LOKASI PENGGUNA ---
        val userMarker = Marker(mapView)
        userMarker.position = userGeoPoint
        userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        userMarker.title = "Lokasi Anda"
        userMarker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.marker_user_location)
        mapView.overlays.add(userMarker)

        // --- PENANDA KUSTOM UNTUK BANK SAMPAH ---
        sortedList.forEach { bank ->
            val bankMarker = Marker(mapView)
            bankMarker.position = GeoPoint(bank.lat, bank.lng)
            bankMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            bankMarker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.marker_custom_bank)

            // Atur judul dan sub-deskripsi (jarak)
            bankMarker.title = bank.nama
            bankMarker.snippet = "%.1f km dari lokasi Anda".format(bank.jarakInKm)

            // Saat pin di peta di-klik, kita hanya pusatkan peta dan tampilkan info
            bankMarker.setOnMarkerClickListener { marker, _ ->
                mapView.controller.animateTo(marker.position)
                marker.showInfoWindow() // Tampilkan gelembung info saat di-klik
                true // Mengembalikan true menandakan klik sudah ditangani
            }
            mapView.overlays.add(bankMarker)
        }
        mapView.invalidate()
    }

    private fun setupRecyclerView() {
        lokasiAdapter = LokasiAdapter { bankSampah ->
            launchNavigation(bankSampah.lat, bankSampah.lng, bankSampah.nama)
        }
        recyclerView.adapter = lokasiAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun launchNavigation(lat: Double, lng: Double, label: String) {
        val gmmIntentUri = Uri.parse("google.navigation:q=$lat,$lng")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(mapIntent)
        } else {
           val webIntent = Intent (
               Intent.ACTION_VIEW,
               Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$label")
           )
           startActivity(webIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}