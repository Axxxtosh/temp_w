package com.uriah.admin.worldvisioncable;

import android.content.Context;
import android.graphics.Color;

import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.support.v4.view.ViewPager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;


public class NewHomeFragment extends Fragment  implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener{

    View v;

    private TabHost mTabHost;
    private ViewPager mViewPager;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();
    private Pager mPagerAdapter;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_new_home, container, false);

        // Initialise the TabHost
        initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }
        // Intialise ViewPager
        intialiseViewPager();


        for (int i = 0; i < this.mTabHost.getTabWidget().getChildCount(); i++) {
            //this.mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#20A0E1")); // unselected
            TextView tv = this.mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            tv.setTextColor(Color.parseColor("#bdbdbd"));
            tv.setTextSize(12);
            tv.setGravity(Gravity.CENTER);
        }

        TextView tv = this.mTabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title); //Unselected Tabs
        tv.setTextColor(Color.parseColor("#f44336"));
        tv.setTextSize(12);
        tv.setGravity(Gravity.CENTER);
        return v;
    }

    /**
     * Initialise ViewPager
     */
    private void intialiseViewPager() {

        List<Fragment> fragments = new Vector<Fragment>();


        //*/

        // fragments.add(Fragment.instantiate(this, HathwayNetworkFragment.class.getName()));
        fragments.add(Fragment.instantiate(getActivity(), ActiveCableFragment.class.getName()));
        fragments.add(Fragment.instantiate(getActivity(), ActiveBroadbandFragment.class.getName()));


        this.mPagerAdapter  = new Pager(super.getActivity().getSupportFragmentManager(), fragments);
        //
        this.mViewPager = v.findViewById(R.id.viewpager);
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.setOnPageChangeListener(this);
    }

    /**
     * Initialise the Tab Host
     */
    private void initialiseTabHost(Bundle args) {
        mTabHost = v.findViewById(android.R.id.tabhost);
        mTabHost.setup();
        TabInfo tabInfo = null;

        /*InternetPacksActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Hathway Network").setIndicator("Hathway Network"), ( tabInfo = new TabInfo("Hathway Network", HathwayNetworkFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);*/

        NewHomeFragment.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Active Cable TV Plan").setIndicator("Active Cable TV Plan"), (tabInfo = new TabInfo("Active Cable TV Plan", ActiveCableFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        NewHomeFragment.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Active Broadband Plan").setIndicator("Active Broadband Plan"), ( tabInfo = new TabInfo("Active Broadband Plan", ActiveBroadbandFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);



        // Default to first tab
        //this.onTabChanged("Tab1");
        //
        mTabHost.setOnTabChangedListener(this);
    }


    private static void AddTab(NewHomeFragment activity, TabHost tabHost, TabHost.TabSpec tabSpec,TabInfo tabInfo) {
        // Attach a Tab view factory to the spec
        tabSpec.setContent(activity.new TabFactory(activity.getActivity()));
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
            TextView tv = this.mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#bdbdbd"));
            tv.setTextSize(12);
            tv.setGravity(Gravity.CENTER);
        }

        // this.mTabHost.getTabWidget().getChildAt(this.mTabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#20A0E1")); // selected
        TextView tv = this.mTabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
        tv.setTextColor(Color.parseColor("#f44336"));
        tv.setTextSize(12);
        tv.setGravity(Gravity.CENTER);


    }


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
