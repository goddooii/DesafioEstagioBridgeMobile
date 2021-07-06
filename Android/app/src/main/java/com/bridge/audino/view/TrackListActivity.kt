package com.bridge.audino.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.bridge.audino.utils.listener.ArtistClickListener
import com.bridge.audino.utils.listener.TrackClickListener
import com.bridge.audino.viewmodel.ArtistViewModel

abstract class TrackListActivity : AppCompatActivity(), ArtistClickListener, TrackClickListener {
    override fun onArtistClickedListener(id: String) {
        val intent = Intent(this, ArtistActivity::class.java).apply {
            putExtra(ArtistViewModel.ARTIST_ID, id)
        }
        startActivity(intent)
    }
}