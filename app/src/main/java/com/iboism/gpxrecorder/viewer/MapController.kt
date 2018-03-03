package com.iboism.gpxrecorder.viewer

import android.content.Context
import android.graphics.Color.MAGENTA
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.JointType.ROUND
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.iboism.gpxrecorder.model.GpxContent
import com.iboism.gpxrecorder.model.Track
import com.iboism.gpxrecorder.model.Waypoint
import com.iboism.gpxrecorder.util.DateTimeFormatHelper

/**
 * Created by Brad on 2/26/2018.
 */

class MapController(val context: Context, val gpxId: Long): OnMapReadyCallback {

    override fun onMapReady(map: GoogleMap?) {
        if (map == null) return // take no action if a map was not created

        MapsInitializer.initialize(context)
        map.uiSettings.isMyLocationButtonEnabled = false
        map.mapType = GoogleMap.MAP_TYPE_HYBRID
        GpxContent.withId(gpxId)?.let { map.drawContent(it) }
    }

    private fun startPoint(gpx: GpxContent): LatLng? {
        return gpx.trackList.firstOrNull()?.segments?.firstOrNull()?.points?.firstOrNull()?.let {
            LatLng(it.lat, it.lon)
        } ?:  gpx.waypointList.firstOrNull()?.let {
            LatLng(it.lat, it.lon)
        }
    }

    private fun GoogleMap.drawContent(gpx: GpxContent) {
        this.drawTracks(gpx.trackList.toList())
        this.drawWaypoints(gpx.waypointList.toList())

        startPoint(gpx)?.let {
            this.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 10f))
        }
    }

    private fun GoogleMap.drawTracks(tracks: List<Track>) {
        // draw track lines
        tracks.forEach {
            this.addPolyline(
                    PolylineOptions()
                            .color(MAGENTA)
                            .jointType(ROUND)
                            .width(6f)
                            .addAll(it.segments.flatMap { it.getLatLngPoints() }))
            }
        // draw marker at start
        tracks.firstOrNull()?.segments?.firstOrNull()?.points?.firstOrNull()?.let {
            this.addMarker(MarkerOptions().position(LatLng(it.lat, it.lon))
                    .title("Start")
                    .snippet(DateTimeFormatHelper.toReadableString(it.time)))
        }
        // draw marker at end
        tracks.lastOrNull()?.segments?.lastOrNull()?.points?.lastOrNull()?.let {
            this.addMarker(MarkerOptions().position(LatLng(it.lat, it.lon))
                    .title("End")
                    .snippet(DateTimeFormatHelper.toReadableString(it.time)))
        }
    }

    private fun GoogleMap.drawWaypoints(waypoints: List<Waypoint>) {
        waypoints.forEach {
            this.addMarker(MarkerOptions().position(LatLng(it.lat,it.lon))
                    .flat(false)
                    .title(it.title)
                    .snippet(it.desc))
        }
    }
}