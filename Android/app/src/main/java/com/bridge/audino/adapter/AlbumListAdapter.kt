package com.bridge.audino.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bridge.audino.databinding.AlbumItemBinding
import com.bridge.audino.model.FullAlbum
import com.bridge.audino.utils.listener.AlbumClickListener
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

class AlbumListAdapter(val listener: AlbumClickListener, var mList: List<FullAlbum> = listOf()) : RecyclerView.Adapter<AlbumListAdapter.ViewHolder>() {

    fun setAlbumList(list: List<FullAlbum>) {
        mList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AlbumItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList[position]

        holder.mBinding.nome = item.name

        Picasso.get()
            .load(item.imageUrl)
            .transform(RoundedCornersTransformation(150, 0))
            .into(holder.mBinding.albumBackground)

        holder.mBinding.albumLayout.setOnClickListener {
            listener.onAlbumClicked(item)
        }
    }


    class ViewHolder(val mBinding: AlbumItemBinding) : RecyclerView.ViewHolder(mBinding.root)
}