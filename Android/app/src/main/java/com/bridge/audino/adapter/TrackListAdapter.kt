package com.bridge.audino.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bridge.audino.R
import com.bridge.audino.databinding.TrackItemBinding
import com.bridge.audino.model.Track
import com.bridge.audino.utils.ArtistPopupMenuBuilder
import com.bridge.audino.utils.CircleTransform
import com.bridge.audino.utils.TrackUtil
import com.bridge.audino.utils.listener.ArtistClickListener
import com.bridge.audino.utils.listener.FavoriteClickListener
import com.bridge.audino.utils.listener.TrackClickListener
import com.squareup.picasso.Picasso

abstract class TrackListAdapter(
    val trackClickListener: TrackClickListener,
    val favoriteClickListener: FavoriteClickListener,
    val artistClickListener: ArtistClickListener,
    var mList: List<Track> = listOf()
) :
    RecyclerView.Adapter<TrackListAdapter.ViewHolder>() {

    abstract fun setTrackList(list: List<Track>)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TrackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList[position]
        holder.mBinding.title = item.name

        holder.mBinding.artist = item.artists.joinToString { it.name }

        Picasso.get()
            .load(item.album.imageUrl)
            .transform(CircleTransform())
            .into(holder.mBinding.trackIcon)

        holder.mBinding.clickable = TrackUtil.isTrackEnabled(item)
        if (holder.mBinding.clickable!!) {
            holder.mBinding.itemBackground.setOnClickListener {
                trackClickListener.onTrackClicked(item)
            }

            holder.mBinding.trackFavoriteButton.setOnClickListener {
                changeFavorite(
                    holder.mBinding.trackFavoriteButton,
                    holder.mBinding.trackFavoriteButtonProgressBar,
                    item
                )
            }

            if (item.favorite) checkFavorite(holder.mBinding.trackFavoriteButton) else uncheckFavorite(
                holder.mBinding.trackFavoriteButton
            )
        } else {
            disableFavorite(holder.mBinding.trackFavoriteButton)
        }

        holder.mBinding.trackFavoriteButtonProgressBar.visibility = View.INVISIBLE
        holder.mBinding.trackFavoriteButton.visibility = View.VISIBLE

        holder.mBinding.itemMoreOptions.setOnClickListener {
            showPopup(holder.mBinding, item)
        }
    }

    private fun showPopup(binding: TrackItemBinding, track: Track) {
        ArtistPopupMenuBuilder().build(binding.root.context, binding.itemMoreOptions)
            .inflate(R.menu.more_options_menu)
            .addAll(track)
            .setOnMenuItemClickListener {
                artistClickListener.onArtistClickedListener(track.artists[it].id)
            }.show()
    }

    private fun changeFavorite(imgButton: ImageButton, progressBar: ProgressBar, track: Track) {
        showFavoriteProgressBar(imgButton, progressBar)
        favoriteClickListener.onFavoriteClicked(track.id)
    }

    private fun checkFavorite(imgButton: ImageButton) {
        imgButton.setImageResource(R.drawable.ic_favorite_filled)
        imgButton.imageTintList =
            ContextCompat.getColorStateList(imgButton.context, R.color.blue)
    }

    private fun uncheckFavorite(imgButton: ImageButton) {
        imgButton.setImageResource(R.drawable.ic_favorite_outline)
        imgButton.imageTintList =
            ContextCompat.getColorStateList(imgButton.context, R.color.off_white)
    }

    private fun disableFavorite(imgButton: ImageButton) {
        imgButton.setImageResource(R.drawable.ic_favorite_outline)
        imgButton.imageTintList =
            ContextCompat.getColorStateList(imgButton.context, R.color.track_item_disabled)
    }

    private fun showFavoriteProgressBar(imgButton: ImageButton, progressBar: ProgressBar) {
        imgButton.isClickable = false
        imgButton.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
    }

    class ViewHolder(val mBinding: TrackItemBinding) : RecyclerView.ViewHolder(mBinding.root)
}