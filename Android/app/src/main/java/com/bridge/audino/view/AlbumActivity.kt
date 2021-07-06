package com.bridge.audino.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bridge.audino.R
import com.bridge.audino.databinding.ActivityAlbumBinding
import com.bridge.audino.viewmodel.AlbumViewModel

class AlbumActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityAlbumBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_album)
        val artistId = if (intent.hasExtra(AlbumViewModel.ARTIST_ID)) intent.getStringExtra(AlbumViewModel.ARTIST_ID) else null
        val albumId = if (intent.hasExtra(AlbumViewModel.ALBUM_ID)) intent.getStringExtra(AlbumViewModel.ALBUM_ID) else null
    }
}
