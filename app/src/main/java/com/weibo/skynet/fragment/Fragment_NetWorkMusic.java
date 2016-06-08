package com.weibo.skynet.fragment;

import com.weibo.skynet.R;
import com.weibo.skynet.base.BaseFragment;

public class Fragment_NetWorkMusic extends BaseFragment {

    @Override
    protected void findView() {
        super.findView();
        view = inflater.inflate(R.layout.networksongs, container, false);
    }

    public Fragment_NetWorkMusic() {}

    public static Fragment_NetWorkMusic newInstance() {
        Fragment_NetWorkMusic fragment = new Fragment_NetWorkMusic();
        return fragment;
    }

}
