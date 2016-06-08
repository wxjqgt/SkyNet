package com.weibo.skynet.fragment;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.weibo.skynet.R;
import com.weibo.skynet.activity.MainActivity;
import com.weibo.skynet.adapter.ViewpagerAdapter;
import com.weibo.skynet.base.BaseFragment;
import com.weibo.skynet.customView.NavigationView;
import com.weibo.skynet.model.Mp3Info;
import com.weibo.skynet.util.ClearUtil;
import com.weibo.skynet.util.EventType;
import com.weibo.skynet.util.ImageLoader;
import com.weibo.skynet.util.MainService;
import com.weibo.skynet.util.MediaUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Music extends BaseFragment implements
        NavigationView.OnSelecterLitener, View.OnClickListener,
        SeekBar.OnSeekBarChangeListener {

    private ImageView im_last, im_playorpause, im_next, im_album;
    private TextView tv_songname, tv_singername, tv_currntPosition, tv_duration;
    private ViewPager viewPager;
    private SeekBar seekBar_music;
    private LinearLayout navigationBar;

    private MainActivity mActivity;
    private MainService mainService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void findView() {
        super.findView();
        view = inflater.inflate(R.layout.fragment_music, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager_music);
        im_album = (ImageView) view.findViewById(R.id.imageView_albumId);
        im_last = (ImageView) view.findViewById(R.id.imageView_last);
        im_next = (ImageView) view.findViewById(R.id.next);
        im_playorpause = (ImageView) view.findViewById(R.id.playorpause);
        tv_singername = (TextView) view.findViewById(R.id.singer_name);
        tv_songname = (TextView) view.findViewById(R.id.song_name);
        tv_currntPosition = (TextView) view.findViewById(R.id.currentPosition);
        tv_duration = (TextView) view.findViewById(R.id.duration);
        seekBar_music = (SeekBar) view.findViewById(R.id.seekBar_song);
    }

    @Override
    protected void bindData() {
        super.bindData();
        mainService.loadMusic();
        mainService.getInstance();
        mainService.updateMusic();
        lodaMusicPlayData(view);
        mainService.resumeUpdateMusicUi();
    }

    private void lodaMusicPlayData(View view) {

        List<Fragment> fragments = new ArrayList<>();

        Fragment_LocalMusic fm_Local = Fragment_LocalMusic.newInstance(mainService.mp3InfoList);
        fragments.add(fm_Local);

        Fragment_NetWorkMusic fm_NetWork = Fragment_NetWorkMusic.newInstance();
        fragments.add(fm_NetWork);

        ViewpagerAdapter adapter = new ViewpagerAdapter(mActivity.getSupportFragmentManager());
        adapter.addFragments(fragments);
        ClearUtil.clearList(fragments);
        viewPager.setAdapter(adapter);

        navigationBar = (LinearLayout) view.findViewById(R.id.navigationbar);

        NavigationView nv_Local = new NavigationView(mActivity);
        nv_Local.setNavigationText("本地音乐");
        nv_Local.setOnSelecterLitener(this);
        nv_Local.setPosition(0);
        nv_Local.setPadding(10, 0, 10, 0);
        nv_Local.setNavigationItem();
        navigationBar.addView(nv_Local);

        NavigationView nv_NetWork = new NavigationView(mActivity);
        nv_NetWork.setNavigationText("网络酷歌");
        nv_NetWork.setOnSelecterLitener(this);
        nv_NetWork.setPosition(1);
        nv_Local.setPadding(10, 0, 10, 0);
        navigationBar.addView(nv_NetWork);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setNavigation(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        seekBar_music.setOnSeekBarChangeListener(this);
        im_playorpause.setImageResource(R.mipmap.btn_notification_player_play_normal);
        im_last.setImageResource(R.mipmap.btn_notification_player_prev_normal);
        im_next.setImageResource(R.mipmap.btn_notification_player_next_normal);

        im_album.setImageResource(R.mipmap.ic_custom);
        tv_songname.setText("无");
        tv_singername.setText("无");
        im_playorpause.setOnClickListener(this);
        im_next.setOnClickListener(this);
        im_last.setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdateProgress(EventType.MusicProgress progress) {
        im_playorpause.setImageResource(R.mipmap.btn_notification_player_stop_normal);
        seekBar_music.setProgress((int) progress.getMusicPlayUpdateProgress());
        tv_currntPosition.setText(progress.getMusicPlayUpdateProgressText());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdatePlayState(String state) {
        if (state.equals("resume")) {
            im_playorpause.setImageResource(R.mipmap.btn_notification_player_play_normal);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdatePosition(EventType.MusicPosition position) {

        List<Mp3Info> mp3Infos = MainService.mp3InfoList;
        if (mp3Infos.size() == 0 && mp3Infos == null) {
            mp3Infos = mainService.loadMusic();
        }

        Mp3Info mp3Info = mp3Infos.get(position.getMusicPlayUpdatePosition());

        int max = (int) mp3Info.getDuration();
        seekBar_music.setMax(max);

        Uri uri = MediaUtils.getArtWorkUri(mp3Info.getId(), mp3Info.getAlbumId());
        ImageLoader.load(this, uri, R.mipmap.ic_custom, im_album);

        tv_songname.setText(mp3Info.getTitle());
        tv_singername.setText(mp3Info.getArtist());
        tv_duration.setText(MediaUtils.formattime(mp3Info.getDuration()));

        im_playorpause.setImageResource(R.mipmap.btn_notification_player_play_normal);

        ClearUtil.clearObject(mp3Info);
    }

    private void setNavigation(int position) {
        for (int i = 0; i < navigationBar.getChildCount(); i++) {
            if (i == position) {
                continue;
            }
            NavigationView nav = (NavigationView) navigationBar.getChildAt(i);
            nav.setDefault();
        }
        NavigationView navigationView = (NavigationView) navigationBar.getChildAt(position);
        navigationView.setNavigationItem();
    }

    @Override
    public void OnSelecter(int position) {
        setNavigation(position);
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.playorpause:
                if (mainService.isPlaying()) {
                    mainService.pauseMusic();
                    im_playorpause.setImageResource(R.mipmap.btn_notification_player_play_normal);
                } else {
                    mainService.resumePlayMusic();
                    im_playorpause.setImageResource(R.mipmap.btn_notification_player_stop_normal);
                }
                break;
            case R.id.imageView_last:
                mainService.playLastMusic();
                break;
            case R.id.next:
                mainService.playNextMusic();
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mainService.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.bindMainService();
    }

    @Override
    public void onPause() {
        super.onPause();
        mActivity.UnbindMainService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
        mainService = mActivity.ms;
    }

    public Fragment_Music() {
    }

    public static Fragment_Music newInstance() {
        Fragment_Music fragment = new Fragment_Music();
        return fragment;
    }

}
