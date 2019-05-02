package com.iboism.gpxrecorder.recording

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import com.iboism.gpxrecorder.R
import kotlinx.android.synthetic.main.current_recording_view.view.*

class CurrentRecordingView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private val rootView = LayoutInflater.from(context).inflate(R.layout.current_recording_view, this) as ConstraintLayout
    val routeTitle = route_title_tv
    val addWaypointButton = add_wpt_view as Button
    val playPauseButton = playpause_view as Button
    val stopButton = stop_view as Button

    fun setPaused(isPaused: Boolean) {
        playPauseButton.text = if (isPaused) context.getText(R.string.resume_recording) else context.getText(R.string.pause_recording)
    }
}