package com.ddancn.keepaccount.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ddancn.keepaccount.R;
import com.ddancn.keepaccount.fragment.AddFragment;
import com.ddancn.keepaccount.fragment.RecordFragment;
import com.ddancn.keepaccount.fragment.SumFragment;
import com.ddancn.lib.base.BaseActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ddan.zhuang
 */
public class MainActivity extends BaseActivity {

    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavView;

    @Override
    protected int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean hasHeader() {
        return false;
    }

    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.viewpager);
        mBottomNavView = findViewById(R.id.navigation);
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AddFragment());
        adapter.addFragment(new RecordFragment());
        adapter.addFragment(new SumFragment());
        mViewPager.setAdapter(adapter);
    }

    @Override
    protected void bindListener() {
        mBottomNavView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_add:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_list:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_chart:
                    mViewPager.setCurrentItem(2);
                    return true;
                default:
            }
            return false;
        });
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mBottomNavView.getMenu().getItem(position).setChecked(true);
            }
        });
    }

    public class MainPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();

        public MainPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        @NonNull
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment) {
            fragments.add(fragment);
        }
    }
}
