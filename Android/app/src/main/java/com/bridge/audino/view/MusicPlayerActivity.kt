package com.bridge.audino.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bridge.audino.R
import com.bridge.audino.databinding.ActivityMusicPlayerBinding
import com.bridge.audino.model.Track
import com.bridge.audino.utils.listener.MusicPlayerFavoriteListener
import com.bridge.audino.utils.listener.OnBackPressedListener
import com.bridge.audino.viewmodel.MusicPlayerViewModel
import com.bridge.audino.viewmodel.MusicPlayerViewModel.Companion.REPEAT_OFF
import com.bridge.audino.viewmodel.MusicPlayerViewModel.Companion.REPEAT_ON
import com.bridge.audino.viewmodel.MusicPlayerViewModel.Companion.REPEAT_ONE
import com.squareup.picasso.Picasso

class MusicPlayerActivity : AppCompatActivity(), OnBackPressedListener, MusicPlayerFavoriteListener {

    private lateinit var mBinding: ActivityMusicPlayerBinding
    private val mMusicPlayerViewModel: MusicPlayerViewModel by lazy {
        ViewModelProvider(this).get(
            MusicPlayerViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_music_player)

        mBinding.onBackPressedListener = this
        mBinding.favoriteListener = this
        mBinding.listenerPlayer = mMusicPlayerViewModel
        mBinding.musicProgressSb.setOnSeekBarChangeListener(mMusicPlayerViewModel)
        registerObservations()

        val trackId = if (intent.hasExtra(MusicPlayerViewModel.TRACK_ID)) intent.getStringExtra(MusicPlayerViewModel.TRACK_ID) else null
        val artistId = if (intent.hasExtra(MusicPlayerViewModel.ARTIST_ID)) intent.getStringExtra(MusicPlayerViewModel.ARTIST_ID) else null
        val albumId = if (intent.hasExtra(MusicPlayerViewModel.ALBUM_ID)) intent.getStringExtra(MusicPlayerViewModel.ALBUM_ID) else null
        val queryText = if (intent.hasExtra(MusicPlayerViewModel.QUERY_TEXT)) intent.getStringExtra(MusicPlayerViewModel.QUERY_TEXT) else null
        val fromId = intent.getIntExtra(MusicPlayerViewModel.FROM_ID, -1)
        mMusicPlayerViewModel.init(trackId = trackId, artistId = artistId, textQuery = queryText, albumId = albumId, from = fromId)
    }

    override fun onBackPressed() {
        mMusicPlayerViewModel.releaseMusicPlayer()
        super.onBackPressed()
    }

    override fun onDestroy() {
        mMusicPlayerViewModel.onDestroy()
        super.onDestroy()
    }

    override fun onFavoriteClicked() {
        mBinding.trackFavoriteButton.visibility = View.INVISIBLE
        mBinding.trackFavoriteButtonProgressBar.visibility = View.VISIBLE
        mMusicPlayerViewModel.onFavoriteClicked()
    }

    private fun registerObservations() {
        mMusicPlayerViewModel.isLoading().observe(this, Observer<Boolean> {
            if (it != null) {
                updateLoading(it)
            }
        })
        mMusicPlayerViewModel.getCurrentTrack().observe(this, Observer<Track> {
            if (it != null) {
                updateTrack(it)
            }
        })
        mMusicPlayerViewModel.isMusicPlaying().observe(this, Observer<Boolean> {
            if (it != null) {
                updatePlayingStatus(it)
            }
        })
        mMusicPlayerViewModel.getRepeatStatus().observe(this, Observer<Int> {
            if (it != null) {
                updateRepeatStatus(it)
            }
        })
        mMusicPlayerViewModel.getTrackDuration().observe(this, Observer<Int> {
            if (it != null) {
                initializeSeekBar(it)
            }
        })
        mMusicPlayerViewModel.getPlayerCurrentPosition().observe(this, Observer<Int> {
            if (it != null) {
                updatePlayerCurrentPosition(it)
            }
        })
        mMusicPlayerViewModel.isFavorite().observe(this, Observer<Boolean> {
            if (it != null) {
                updateFavoriteStatus(it)
            }
        })
    }

    private fun updateLoading(isLoading: Boolean) {
        if (isLoading) {
            mBinding.mediaplayerContent.visibility = View.GONE
            mBinding.mediaplayerProgressbar.visibility = View.VISIBLE
        } else {
            mBinding.mediaplayerProgressbar.visibility = View.GONE
            mBinding.mediaplayerContent.visibility = View.VISIBLE
        }
    }

    private fun updateTrack(track: Track) {
        mBinding.songNameTv.text = track.name
        mBinding.artistNameTv.text = track.artists[0].name
        updateFavoriteStatus(track.favorite)
        Picasso.get()
            .load(track.album.imageUrl)
            .into(mBinding.songImage)
    }

    private fun updatePlayingStatus(isPlaying: Boolean) {
        val icon = if (isPlaying) R.drawable.ic_pause_circle else R.drawable.ic_play_circle
        mBinding.playPauseIb.setImageResource(icon)
    }

    private fun updateRepeatStatus(status: Int) {

    }

    private fun initializeSeekBar(duration: Int) {
        mBinding.musicProgressSb.max = duration
    }

    private fun updatePlayerCurrentPosition(position: Int) {
        mBinding.musicProgressSb.progress = position
    }

    private fun updateFavoriteStatus(favorite: Boolean) {
        val favoriteIcon = if (favorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_outline
        val favoriteColor = if (favorite) R.color.blue else R.color.off_white
        mBinding.trackFavoriteButton.let {
            it.setImageResource(favoriteIcon)
            it.imageTintList = ContextCompat.getColorStateList(this, favoriteColor)
        }
        mBinding.trackFavoriteButtonProgressBar.visibility = View.INVISIBLE
        mBinding.trackFavoriteButton.visibility = View.VISIBLE
    }
}
