package com.bridge.audino.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bridge.audino.adapter.SearchTrackAdapter
import com.bridge.audino.adapter.TrackListAdapter
import com.bridge.audino.databinding.SearchFragmentBinding
import com.bridge.audino.model.Track
import com.bridge.audino.utils.AfterTextChangedTextWatcher
import com.bridge.audino.utils.listener.TrackClickListener
import com.bridge.audino.viewmodel.MusicPlayerViewModel.Companion.FROM_ID
import com.bridge.audino.viewmodel.MusicPlayerViewModel.Companion.FROM_SEARCH
import com.bridge.audino.viewmodel.MusicPlayerViewModel.Companion.QUERY_TEXT
import com.bridge.audino.viewmodel.MusicPlayerViewModel.Companion.TRACK_ID
import com.bridge.audino.viewmodel.SearchTrackListViewModel

class SearchFragment : TrackListFragment() {
    private lateinit var mBinding: SearchFragmentBinding
    private val mSearchViewModel: SearchTrackListViewModel by lazy {
        ViewModelProvider(this).get(
            SearchTrackListViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = SearchFragmentBinding.inflate(LayoutInflater.from(context), container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()

        mBinding.searchTrackList.adapter = SearchTrackAdapter(this, mSearchViewModel, this)

        mSearchViewModel.getTrackList().observe(viewLifecycleOwner, Observer<List<Track>> {
            (mBinding.searchTrackList.adapter as TrackListAdapter).setTrackList(it)
            if (it.isEmpty()) {
                showEmptyLayout()
            } else {
                hideEmptyLayout()
            }
            showTrackList()
        })

        mBinding.pesquisarEt.addTextChangedListener(object : AfterTextChangedTextWatcher {
            override fun afterTextChanged(text: String) {
                hideEmptyLayout()
                showProgressbar()
                mSearchViewModel.onQueryChanged(text)
            }
        })
    }

    private fun hideEmptyLayout() {
        mBinding.emptySearchIcon.visibility = View.GONE
        mBinding.emptySearchLabel.visibility = View.GONE
    }

    private fun showEmptyLayout() {
        mBinding.emptySearchIcon.visibility = View.VISIBLE
        mBinding.emptySearchLabel.visibility = View.VISIBLE
    }

    private fun showProgressbar() {
        mBinding.searchTrackList.visibility = View.GONE
        mBinding.searchProgressbar.visibility = View.VISIBLE
    }

    private fun showTrackList() {
        mBinding.searchProgressbar.visibility = View.GONE
        mBinding.searchTrackList.visibility = View.VISIBLE
    }

    override fun onTrackClicked(track: Track) {
        val intent = Intent(context, MusicPlayerActivity::class.java).apply {
            putExtra(TRACK_ID, track.id)
            putExtra(FROM_ID, FROM_SEARCH)
            putExtra(QUERY_TEXT, mBinding.pesquisarEt.text.toString())
        }
        context?.startActivity(intent)
    }
}