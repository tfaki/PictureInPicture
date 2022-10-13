package com.android.pictureinpicture

import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Rational
import android.view.View
import android.widget.ImageButton
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class VideoActivity : AppCompatActivity() {

    private val TAG:String = "PIP_TAG"

    private var videoUri: Uri? = null

    private var pictureInPictureParamsBuilder:PictureInPictureParams.Builder? = null

    private lateinit var videoView: VideoView
    private lateinit var pipButton: ImageButton

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        videoView = findViewById(R.id.videoView)
        pipButton = findViewById(R.id.pipBtn)
        //get intent with url and pass in function to play video
        setVideoView()

        //init PictureInPictureParams, requires Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            pictureInPictureParamsBuilder = PictureInPictureParams.Builder()
        }

        //handle click, enter PIP
        pipButton.setOnClickListener {
            pictureInPictureMode()
        }

    }

    private fun setVideoView() {
        val videoURL = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        Log.d(TAG, "setVideoView: $videoURL")

        //MediaController for video controls
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)

        videoUri = Uri.parse(videoURL)

        //set media controller to video view
        videoView.setMediaController(mediaController)
        //set video uri to video view
        videoView.setVideoURI(videoUri)

        //add video prepare listenrer
        videoView.setOnPreparedListener {mp ->
            //video is prepared, play
            Log.d(TAG, "setVideoView: Video Prepared, playing...")
            mp.start()
        }
    }

    private fun pictureInPictureMode(){
        //Requires Android O and higher
        Log.d(TAG, "pictureInPictureMode: Try to enter in PIP mode")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Log.d(TAG, "pictureInPictureMode: Supports PIP")
            //setup PIP height width
            val aspectRatio = Rational(videoView.width, videoView.height)
            pictureInPictureParamsBuilder!!.setAspectRatio(aspectRatio).build()
            enterPictureInPictureMode(pictureInPictureParamsBuilder!!.build())
        }
        else{
            Log.d(TAG, "pictureInPictureMode: Doesn't supports PIP")
            Toast.makeText(this, "Your device doesn't supports PIP", Toast.LENGTH_LONG).show()
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        //when user presses home button, if not in PIP mode, enter in PIP, requires Android N and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            Log.d(TAG, "onUserLeaveHint: was not in PIP")
            pictureInPictureMode()
        }
        else{
            Log.d(TAG, "onUserLeaveHint: Already in PIP")
        }
    }

    override fun onBackPressed() {
        onUserLeaveHint()
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration?
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        if (isInPictureInPictureMode){
            Log.d(TAG, "onPictureInPictureModeChanged: Entered PIP")
            pipButton.visibility = View.GONE
        }
        else{
            Log.d(TAG, "onPictureInPictureModeChanged: Exited PIP")
            pipButton.visibility = View.VISIBLE
        }
    }

    override fun onStop() {
        super.onStop()
        if (videoView.isPlaying){
            videoView.stopPlayback()
        }
    }
}