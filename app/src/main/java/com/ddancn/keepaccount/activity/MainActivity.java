package com.ddancn.keepaccount.activity;

import android.database.sqlite.SQLiteDatabase;
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
import com.ddancn.keepaccount.util.ToastUtil;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int TYPE_OUT = -1;
    public static final int TYPE_IN = 1;

    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavView;
    private MainPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ToastUtil.init(getApplication());
        Iconify.with(new FontAwesomeModule());
        SQLiteDatabase db = LitePal.getDatabase();

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
