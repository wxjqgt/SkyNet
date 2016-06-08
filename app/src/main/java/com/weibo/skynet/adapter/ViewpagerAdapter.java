package com.weibo.skynet.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/11.
 */
public class ViewpagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;

    public ViewpagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
    }

    public void addFragments(List<Fragment> fragments){
        this.fragments.addAll(fragments);
        this.notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
