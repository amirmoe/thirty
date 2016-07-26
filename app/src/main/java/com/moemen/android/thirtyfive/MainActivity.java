package com.moemen.android.thirtyfive;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Activity for loading layout resources
 *
 * This activity is used to display a tabLayout and a viewPager..
 *
 * @author Amir Moemen
 *
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FELSÖKNING";

    /**
     *  Initializes the toolbar, tabLayout and Viewpager.
     * @param savedInstanceState Prior state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.game));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.scoretable));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter (getSupportFragmentManager(), tabLayout.getTabCount());
        adapter.setTabSelector(new TabSelector() {
            @Override
            public void onTabSwitch(int tabPosition) {
                viewPager.setCurrentItem(tabPosition);
            }
        });
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            /**
             * Switches the current view when new tab is selected
             * @param tab view that we want to change to.
             */
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }}