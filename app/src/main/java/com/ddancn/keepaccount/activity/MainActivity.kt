package com.ddancn.keepaccount.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ddancn.keepaccount.R
import com.ddancn.keepaccount.databinding.ActivityMainBinding
import com.ddancn.keepaccount.fragment.AddFragment
import com.ddancn.keepaccount.fragment.RecordFragment
import com.ddancn.keepaccount.fragment.SumFragment
import com.ddancn.lib.base.BaseActivity

/**
 * @author ddan.zhuang
 */
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun hasHeader(): Boolean {
        return false
    }

    override fun initView() {
        val adapter = MainPagerAdapter(this)
        adapter.addFragment(AddFragment())
        adapter.addFragment(RecordFragment())
        adapter.addFragment(SumFragment())
        vb.viewpager.adapter = adapter
        vb.viewpager.offscreenPageLimit = 2
    }

    override fun bindListener() {
        vb.navigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_add -> vb.viewpager.currentItem = 0
                R.id.navigation_list -> vb.viewpager.currentItem = 1
                R.id.navigation_chart -> vb.viewpager.currentItem = 2
            }
            false
        }
        vb.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                vb.navigation.menu.getItem(position).isChecked = true
            }
        })
    }

    class MainPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

        private val fragments = ArrayList<Fragment>()

        fun addFragment(fragment: Fragment) {
            fragments.add(fragment)
        }

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }
}
