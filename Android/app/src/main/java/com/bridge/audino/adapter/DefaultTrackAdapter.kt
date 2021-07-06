package com.bridge.audino.adapter

import com.bridge.audino.model.Track
import com.bridge.audino.utils.listener.ArtistClickListener
import com.bridge.audino.utils.listener.FavoriteClickListener
import com.bridge.audino.utils.listener.TrackClickListener

class DefaultTrackAdapter(
    trackClickListener: TrackClickListener,
    favoriteClickListener: FavoriteClickListener,
    artistClickListener: ArtistClickListener,
    mList: List<Track> = listOf()
) : TrackListAdapter(trackClickListener, favoriteClickListener, artistClickListener, mList) {

    override fun setTrackList(list: List<Track>) {
        if (mList.isEmpty()) {
            mList = list
            notifyDataSetChanged()
        } else {
            val oldList = mList.toList()
            mList = list
            oldList.forEachIndexed { index, track ->
                if (list.contains(track) && track.favorite != list[index].favorite) notifyItemChanged(index)
            }
        }
    }
}