package com.bridge.audino.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bridge.audino.adapter.DefaultTrackAdapter
import com.bridge.audino.adapter.TrackListAdapter
import com.bridge.audino.databinding.HomeFragmentBinding
import com.bridge.audino.model.Track
import com.bridge.audino.utils.listener.TrackClickListener
import com.bridge.audino.viewmodel.MusicPlayerViewModel.Companion.FROM_HOME
import com.bridge.audino.viewmodel.MusicPlayerViewModel.Companion.FROM_ID
import com.bridge.audino.viewmodel.MusicPlayerViewModel.Companion.TRACK_ID
import com.bridge.audino.viewmodel.PopularTrackListViewModel

class HomeFragment : TrackListFragment() {
    private lateinit var mBinding: HomeFragmentBinding
    private val mHomeViewModel: PopularTrackListViewModel by lazy {
        ViewModelProvider(this).get(
            PopularTrackListViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = HomeFragmentBinding.inflate(LayoutInflater.from(context), container, false)
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        mHomeViewModel.init()
    }

    override fun onStart() {
        super.onStart()
        mBinding.homeList.adapter = DefaultTrackAdapter(this, mHomeViewModel, this)
        mHomeViewModel.getTrackList().observe(viewLifecycleOwner, Observer<List<Track>> {
            (mBinding.homeList.adapter as TrackListAdapter).setTrackList(it)
            mBinding.homeProgressbar.visibility = View.GONE
            mBinding.homeList.visibility = View.VISIBLE
        })
    }

    override fun onTrackClicked(track: Track) {
        val intent = Intent(context, MusicPlayerActivity::class.java).apply {
            putExtra(TRACK_ID, track.id)
            putExtra(FROM_ID, FROM_HOME)
        }
        context?.startActivity(intent)
    }
}