package com.bridge.audino.view

import android.content.Intent
import androidx.fragment.app.Fragment
import com.bridge.audino.utils.listener.ArtistClickListener
import com.bridge.audino.utils.listener.TrackClickListener
import com.bridge.audino.viewmodel.ArtistViewModel

abstract class TrackListFragment : Fragment(), ArtistClickListener, TrackClickListener {
    override fun onArtistClickedListener(id: String) {
        val intent = Intent(context, ArtistActivity::class.java).apply {
            putExtra(ArtistViewModel.ARTIST_ID, id)
        }
        startActivity(intent)
    }
}