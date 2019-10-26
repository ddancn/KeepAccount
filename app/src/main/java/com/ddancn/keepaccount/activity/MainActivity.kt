package com.ddancn.keepaccount.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ddancn.keepaccount.R
import com.ddancn.keepaccount.fragment.AddFragment
import com.ddancn.keepaccount.fragment.RecordFragment
import com.ddancn.keepaccount.fragment.SumFragment
import com.ddancn.lib.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

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
        val adapter = MainPagerAdapter(supportFragmentManager)
        adapter.addFragment(AddFragment())
        adapter.addFragment(RecordFragment())
        adapter.addFragment(SumFragment())
        viewpager.adapter = adapter
        viewpager.offscreenPageLimit = 2
    }

    override
    fun bindListener() {
        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_add -> viewpager.currentItem = 0
                R.id.navigation_list -> viewpager.currentItem = 1
                R.id.navigation_chart -> viewpager.currentItem = 2
            }
            false
        }
        viewpager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                navigation.menu.getItem(position).isChecked = true
            }
        })
    }

    class MainPagerAdapter(fm: FragmentManager)
        : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val fragments = ArrayList<Fragment>()

        override fun getItem(position: Int): Fragment {
            return fragments.get(position)
        }

        override fun getCount(): Int {
            return fragments.size
        }

        fun addFragment(fragment: Fragment) {
            fragments.add(fragment)
        }
    }
}
