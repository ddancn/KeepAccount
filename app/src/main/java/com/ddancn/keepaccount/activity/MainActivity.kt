package com.ddancn.keepaccount.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ddancn.keepaccount.R
import com.ddancn.keepaccount.fragment.AddFragment
import com.ddancn.keepaccount.fragment.RecordFragment
import com.ddancn.keepaccount.fragment.SumFragment
import com.ddancn.lib.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author ddan.zhuang
 */
class MainActivity : BaseActivity() {

    override fun bindLayout(): Int {
        return R.layout.activity_main
    }

    override fun hasHeader(): Boolean {
        return false
    }

    override fun initView() {
        val adapter = MainPagerAdapter(this)
        adapter.addFragment(AddFragment())
        adapter.addFragment(RecordFragment())
        adapter.addFragment(SumFragment())
        viewpager.adapter = adapter
        viewpager.offscreenPageLimit = 2
    }

    override fun bindListener() {
        navigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_add -> viewpager.currentItem = 0
                R.id.navigation_list -> viewpager.currentItem = 1
                R.id.navigation_chart -> viewpager.currentItem = 2
            }
            false
        }
        viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                navigation.menu.getItem(position).isChecked = true
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
