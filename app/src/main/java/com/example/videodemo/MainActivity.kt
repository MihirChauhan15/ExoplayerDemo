package com.example.videodemo


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.Util


class MainActivity : AppCompatActivity() {
    var currentWindow: Int=0
    var playbackPosition: Long = 0
    var playWhenReady: Boolean = true
    lateinit var playerView: PlayerView
    private var player: SimpleExoPlayer?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerView =findViewById(R.id.playerView)
    }
    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT < 24 || player == null) {
            initializePlayer()
        }
    }
    private fun initializePlayer(){
        player = SimpleExoPlayer.Builder(this).build().apply {
            setHandleAudioBecomingNoisy(true)
        }
        playerView.player = player

        val mediaItem= MediaItem.fromUri(getString(R.string.audio))
        player!!.setMediaItem(mediaItem)
        player!!.addMediaItem(mediaItem)
        player!!.setPlayWhenReady(playWhenReady);
        player!!.seekTo(currentWindow, playbackPosition);
        player!!.prepare();

    }
    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }
    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player!!.playWhenReady
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            player!!.release()
            player = null
        }
    }

}
