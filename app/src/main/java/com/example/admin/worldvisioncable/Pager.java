package com.example.admin.worldvisioncable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by Belal on 2/3/2016.
 */
//Extending FragmentStatePagerAdapter
public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
   // int tabCount;

    private List<Fragment> fragments;

    //Constructor to the class
    public Pager(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        //Initializing tab count
       //this.tabCount= tabCount;
        this.fragments = fragments;
    }

    //Overriding method getItem

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    /* (non-Javadoc)
     * @see android.support.v4.view.PagerAdapter#getCount()
     */
    @Override
    public int getCount() {
        return this.fragments.size();
    }

   /* @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                ENETFragment tab1 = new ENETFragment();
                return tab1;
            case 1:
                TTNNetFragment tab2 = new TTNNetFragment();
                return tab2;
            case 2:
                MetroNetFragment tab3 = new MetroNetFragment();
                return tab3;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }*/
}