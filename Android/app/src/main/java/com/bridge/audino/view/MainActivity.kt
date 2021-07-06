package com.bridge.audino.view

import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bridge.audino.R
import com.bridge.audino.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : FragmentActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mBinding.pager.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> HomeFragment()
                    1 -> SearchFragment()
                    else -> FavoriteFragment()
                }
            }

            override fun getItemCount(): Int {
                return 3
            }
        }
        TabLayoutMediator(mBinding.tabLayout, mBinding.pager) { tab, position ->
            when (position) {
                0 -> tab.icon = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_home, null
                )
                1 -> tab.icon = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_more_24px, null
                )
                else -> tab.icon =
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_favorite_filled, null
                    )
            }
        }.attach()
    }
}
