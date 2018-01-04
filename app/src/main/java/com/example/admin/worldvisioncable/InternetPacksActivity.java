package com.example.admin.worldvisioncable;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class InternetPacksActivity extends AppCompatActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener{

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    private TabHost mTabHost;
    private ViewPager mViewPager;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, InternetPacksActivity.TabInfo>();
    private Pager mPagerAdapter;
    Button login;


    private class TabInfo {
        private String tag;
        private Class<?> clss;
        private Bundle args;
        private Fragment fragment;
        TabInfo(String tag, Class<?> clazz, Bundle args) {
            this.tag = tag;
            this.clss = clazz;
            this.args = args;
        }

    }

    class TabFactory implements TabHost.TabContentFactory {

        private final Context mContext;

        /**
         * @param context
         */
        public TabFactory(Context context) {
            mContext = context;
        }

        /** (non-Javadoc)
         * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
         */
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_packs);



// Initialise the TabHost
        this.initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }
        // Intialise ViewPager
        this.intialiseViewPager();

        for (int i = 0; i < this.mTabHost.getTabWidget().getChildCount(); i++) {
            //this.mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#20A0E1")); // unselected
            TextView tv = this.mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            tv.setTextColor(Color.parseColor("#bdbdbd"));
            tv.setGravity(Gravity.CENTER);
        }

        TextView tv = this.mTabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title); //Unselected Tabs
        tv.setTextColor(Color.parseColor("#f44336"));
        tv.setGravity(Gravity.CENTER);


    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
    }

    /**
     * Initialise ViewPager
     */
    private void intialiseViewPager() {

        List<Fragment> fragments = new Vector<Fragment>();
       // fragments.add(Fragment.instantiate(this, HathwayNetworkFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, ENETFragment.class.getName()));
        /*fragments.add(Fragment.instantiate(this, MetroNetFragment.class.getName()));*/
        fragments.add(Fragment.instantiate(this, TTNNetFragment.class.getName()));
        this.mPagerAdapter  = new Pager(super.getSupportFragmentManager(), fragments);
        //
        this.mViewPager = (ViewPager)super.findViewById(R.id.viewpager);
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.setOnPageChangeListener(this);
    }

    /**
     * Initialise the Tab Host
     */
    private void initialiseTabHost(Bundle args) {
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();
        TabInfo tabInfo = null;

        /*InternetPacksActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Hathway Network").setIndicator("Hathway Network"), ( tabInfo = new TabInfo("Hathway Network", HathwayNetworkFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);*/

        InternetPacksActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("ENET Network").setIndicator("ENET Network"), ( tabInfo = new TabInfo("ENET Network", ENETFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        /*InternetPacksActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Metro Net Network").setIndicator("Metro Net Network"), ( tabInfo = new TabInfo("Metro Net Network", MetroNetFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);*/

        InternetPacksActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("TTN Network").setIndicator("TTN Network"), ( tabInfo = new TabInfo("TTN Network", TTNNetFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        // Default to first tab
        //this.onTabChanged("Tab1");
        //
        mTabHost.setOnTabChangedListener(this);
    }


    private static void AddTab(InternetPacksActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        // Attach a Tab view factory to the spec
        tabSpec.setContent(activity.new TabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    /** (non-Javadoc)
     * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
     */
    public void onTabChanged(String tag) {
        //TabInfo newTab = this.mapTabInfo.get(tag);
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);

        for (int i = 0; i < this.mTabHost.getTabWidget().getChildCount(); i++) {
            //this.mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#20A0E1")); // unselected
            TextView tv = (TextView) this.mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            tv.setTextColor(Color.parseColor("#bdbdbd"));
        }

       // this.mTabHost.getTabWidget().getChildAt(this.mTabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#20A0E1")); // selected
        TextView tv = (TextView) this.mTabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
        tv.setTextColor(Color.parseColor("#f44336"));


    }

    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)
     */
    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected(int)
     */
    @Override
    public void onPageSelected(int position) {
        // TODO Auto-generated method stub
        this.mTabHost.setCurrentTab(position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // TODO Auto-generated method stub

    }


}
