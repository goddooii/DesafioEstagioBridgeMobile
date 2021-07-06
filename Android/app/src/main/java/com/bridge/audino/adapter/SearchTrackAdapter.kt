package com.bridge.audino.adapter

import com.bridge.audino.model.Track
import com.bridge.audino.utils.listener.ArtistClickListener
import com.bridge.audino.utils.listener.FavoriteClickListener
import com.bridge.audino.utils.listener.TrackClickListener

class SearchTrackAdapter(
    trackClickListener: TrackClickListener,
    favoriteClickListener: FavoriteClickListener,
    artistClickListener: ArtistClickListener,
    mList: List<Track> = listOf()
) : TrackListAdapter(trackClickListener, favoriteClickListener, artistClickListener, mList) {

    override fun setTrackList(list: List<Track>) {
        val oldList = mList.toList()
        mList = list
        if (oldList.isEmpty() || oldList.size != list.size) {
            notifyDataSetChanged()
        } else {
            list.forEachIndexed { _, track ->
                if (!oldList.contains(track)) {
                    notifyDataSetChanged()
                    return
                }
            }
            oldList.forEachIndexed { index, track ->
                if (track.favorite != list[index].favorite) notifyItemChanged(index)
            }
        }
    }
}