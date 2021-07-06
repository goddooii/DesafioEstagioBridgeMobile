package com.bridge.audino.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bridge.audino.R
import com.bridge.audino.adapter.AlbumListAdapter
import com.bridge.audino.adapter.DefaultTrackAdapter
import com.bridge.audino.databinding.ActivityArtistBinding
import com.bridge.audino.model.FullAlbum
import com.bridge.audino.model.FullArtist
import com.bridge.audino.model.Track
import com.bridge.audino.utils.TrackUtil
import com.bridge.audino.utils.listener.AlbumClickListener
import com.bridge.audino.utils.listener.OnBackPressedListener
import com.bridge.audino.viewmodel.ArtistViewModel
import com.bridge.audino.viewmodel.MusicPlayerViewModel
import com.squareup.picasso.Picasso

class ArtistActivity : TrackListActivity(), OnBackPressedListener, AlbumClickListener {

    private lateinit var mBinding: ActivityArtistBinding
    private lateinit var idArtist: String
    private val mViewModel: ArtistViewModel by lazy { ViewModelProvider(this).get(ArtistViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_artist)

        val id = if (intent.hasExtra(ArtistViewModel.ARTIST_ID)) intent.getStringExtra(ArtistViewModel.ARTIST_ID) else null

        mBinding.onBackPressedListener = this
        mBinding.artistTrackList.adapter = DefaultTrackAdapter(this, mViewModel, this)
        val albumListAdapter = AlbumListAdapter(this)

        mViewModel.getFullArtist().observe(this, Observer<FullArtist> {
            fillInArtist(it)
        })

        mViewModel.getTrackList().observe(this, Observer<List<Track>> {
            (mBinding.artistTrackList.adapter as DefaultTrackAdapter).setTrackList(it)
            setArtistVisible()
        })

        mViewModel.init(id)
    }

    private fun fillInArtist(fullArtist: FullArtist) {
        idArtist = fullArtist.id
        mBinding.name = fullArtist.name
        Picasso.get()
                .load(fullArtist.imageUrl)
                .into(mBinding.artistBackground)

        mBinding.tocarEnabled = TrackUtil.isArtistEnabled(fullArtist)
        if (mBinding.tocarEnabled!!) {
            mBinding.artistPlayButton.setOnClickListener {
                onPlayClicked()
            }
        }
    }

    private fun setArtistVisible() {
        mBinding.artistaProgressbar.visibility = View.GONE
        mBinding.artistGroup.visibility = View.VISIBLE
    }

    override fun onAlbumClicked(album: FullAlbum) {
        
    }

    private fun onPlayClicked() {
        val intent = Intent(this, MusicPlayerActivity::class.java).apply {
            putExtra(MusicPlayerViewModel.FROM_ID, MusicPlayerViewModel.FROM_ARTIST)
            putExtra(MusicPlayerViewModel.ARTIST_ID, idArtist)
        }
        startActivity(intent)
    }

    override fun onTrackClicked(track: Track) {
        val intent = Intent(this, MusicPlayerActivity::class.java).apply {
            putExtra(MusicPlayerViewModel.TRACK_ID, track.id)
            putExtra(MusicPlayerViewModel.FROM_ID, MusicPlayerViewModel.FROM_ARTIST)
            putExtra(MusicPlayerViewModel.ARTIST_ID, idArtist)
        }
        startActivity(intent)
    }
}