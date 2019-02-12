package com.ddancn.keepaccount.activity;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ddancn.keepaccount.R;
import com.ddancn.keepaccount.fragment.AddFragment;
import com.ddancn.keepaccount.fragment.RecordFragment;
import com.ddancn.keepaccount.fragment.SumFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavView;
    private MainPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = findViewById(R.id.viewpager);
        mBottomNavView = findViewById(R.id.navigation);
        adapter = new MainPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AddFragment());
        adapter.addFragment(new RecordFragment());
        adapter.addFragment(new SumFragment());
        mViewPager.setAdapter(adapter);

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
            }
            return false;
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mBottomNavView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public class MainPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
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
