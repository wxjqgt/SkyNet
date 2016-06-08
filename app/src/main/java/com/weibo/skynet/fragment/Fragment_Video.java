package com.weibo.skynet.fragment;

import com.weibo.skynet.R;
import com.weibo.skynet.base.BaseFragment;

public class Fragment_Video extends BaseFragment {

    @Override
    protected void findView() {
        super.findView();
        view = inflater.inflate(R.layout.fragment_video, container, false);
    }

    public Fragment_Video() {}

    public static Fragment_Video newInstance() {
        Fragment_Video fragment = new Fragment_Video();
        return fragment;
    }

}
