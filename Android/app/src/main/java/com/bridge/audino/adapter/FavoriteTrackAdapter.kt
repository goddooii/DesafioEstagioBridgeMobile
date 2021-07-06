package com.bridge.audino.adapter

import com.bridge.audino.model.Track
import com.bridge.audino.utils.listener.ArtistClickListener
import com.bridge.audino.utils.listener.FavoriteClickListener
import com.bridge.audino.utils.listener.TrackClickListener

class FavoriteTrackAdapter(
    trackClickListener: TrackClickListener,
    favoriteClickListener: FavoriteClickListener,
    artistClickListener: ArtistClickListener,
    mList: List<Track> = listOf()
) : TrackListAdapter(trackClickListener, favoriteClickListener, artistClickListener, mList) {

    override fun setTrackList(list: List<Track>) {
        if (mList.isNullOrEmpty()) {
            mList = list
            notifyDataSetChanged()
        } else {
            val oldList = mList.toList()
            mList = list
            for (index in (oldList.size - 1) downTo 0) {
                if (!list.contains(oldList[index])) {
                    notifyItemRemoved(index)
                }
            }
            list.forEachIndexed { index, track ->
                if (!oldList.contains(track)) {
                    notifyItemInserted(index)
                }
            }
        }
    }
}