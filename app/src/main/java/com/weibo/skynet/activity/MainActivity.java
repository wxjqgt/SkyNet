package com.weibo.skynet.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;
import com.weibo.skynet.R;
import com.weibo.skynet.base.BaseActivity;
import com.weibo.skynet.fragment.Fragment_ImageList;
import com.weibo.skynet.fragment.Fragment_ImageViewPager;
import com.weibo.skynet.fragment.Fragment_Music;
import com.weibo.skynet.fragment.Fragment_Video;
import com.weibo.skynet.util.App_mine;
import com.weibo.skynet.util.Custant;
import com.weibo.skynet.util.DLog;
import com.weibo.skynet.util.ImageLoader;
import com.weibo.skynet.util.MainService;
import com.weibo.skynet.util.Storage_Utils;

import java.util.Random;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.VitamioLicense;

public class MainActivity extends BaseActivity implements BoomMenuButton.OnSubButtonClickListener {

    public FragmentManager fm;
    private Fragment lastFragment;
    private String lastTag;
    private boolean isViewPager = false, isInit = false;
    private int currentPosition = 0;

    private BoomMenuButton boomMenuButton;
    private TextView textView;

    @Override
    protected void findView() {
        super.findView();
        boomMenuButton = (BoomMenuButton) findViewById(R.id.boom);
        textView = (TextView) findViewById(R.id.text);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!isInit) {
            initBoom();
        }
        isInit = true;
    }

    @Override
    public void onClick(int buttonIndex) {
        String title = Custant.STRINGS[buttonIndex];
        switch (title) {
            case "image":
                showImageListFragment();
                break;
            case "music":
                showMusicFragment();
                break;
            case "video":
                showVideoFragment();
                break;
            case "exit":
                exit();
                break;
            case "main":
                hide();
                break;
            default:
                break;
        }
    }

    public void showImageListFragment() {
        ms.loadImage();
        if (isViewPager) {
            showFragment(Custant.TYPE_VIEWPAGER, currentPosition);
        } else {
            showFragment(Custant.TYPE_IMAGE, -1);
        }
    }

    public void showViewPagerImage(int position) {
        showFragment(Custant.TYPE_VIEWPAGER, position);
        isViewPager = true;
        currentPosition = position;
        Fragment_ImageViewPager sf = (Fragment_ImageViewPager) fm.findFragmentByTag(Custant.TYPE_VIEWPAGER);
        if (sf != null) {
            sf.setImageCurrentPosition(currentPosition);
        }
    }

    public void showMusicFragment() {
        showFragment(Custant.TYPE_MUSIC, -1);
    }

    public void showVideoFragment() {
        showFragment(Custant.TYPE_VIDEO, -1);
    }

    private void initBoom() {
        int number = 5;
        Drawable[] drawables = new Drawable[number];
        int[] drawablesResource = new int[]{
                R.drawable.mark,
                R.drawable.refresh,
                R.drawable.copy,
                R.drawable.heart,
                R.drawable.info,
                R.drawable.like,
                R.drawable.record,
                R.drawable.search,
                R.drawable.settings
        };
        for (int i = 0; i < number; i++) {
            drawables[i] = ContextCompat.getDrawable(App_mine.context_app, drawablesResource[i]);
        }
        String[] strings = new String[number];
        for (int i = 0; i < number; i++) {
            strings[i] = Custant.STRINGS[i];
        }
        int[][] colors = new int[number][2];
        for (int i = 0; i < number; i++) {
            colors[i][1] = GetRandomColor();
            colors[i][0] = Util.getInstance().getPressedColor(colors[i][1]);
        }

        ButtonType buttonType = ButtonType.CIRCLE;
        // Now with Builder, you can init BMB more convenient
        new BoomMenuButton.Builder()
                .subButtons(drawables, colors, strings)
                .button(buttonType)
                .boom(BoomType.PARABOLA_2)
                .place(PlaceType.CIRCLE_5_3)
                .boomButtonShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
                .subButtonsShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
                .onSubButtonClick(this)
                .init(boomMenuButton);
        boomMenuButton.setDuration(500);
    }

    public int GetRandomColor() {
        Random random = new Random();
        int p = random.nextInt(Custant.Colors.length);
        return Color.parseColor(Custant.Colors[p]);
    }

    private void showFragment(String tag, int position) {
        int visibility = textView.getVisibility();
        if (!(visibility == View.GONE)) {
            textView.setVisibility(View.GONE);
        }
        hide(lastFragment);
        Fragment fi = fm.findFragmentByTag(tag);
        if (fi == null) {
            if (tag.equals(Custant.TYPE_IMAGE)) {
                fi = Fragment_ImageList.newInstance();
            } else if (tag.equals(Custant.TYPE_VIEWPAGER)) {
                fi = Fragment_ImageViewPager.newInstance(position);
            } else if (tag.equals(Custant.TYPE_VIDEO)) {
                fi = Fragment_Video.newInstance();
            } else if (tag.equals(Custant.TYPE_MUSIC)) {
                fi = Fragment_Music.newInstance();
            }
            fm.beginTransaction().add(R.id.show_fragment, fi, tag).commit();
        } else {
            show(fi);
        }
        lastFragment = fi;
        lastTag = tag;
    }

    private void hide(Fragment fragment) {
        if (fragment != null) {
            fm.beginTransaction().hide(fragment).commit();
        }
    }

    public void hide() {
        if (lastFragment != null) {
            fm.beginTransaction().hide(lastFragment).commit();
        }
        int visibility = textView.getVisibility();
        if (!(visibility == View.VISIBLE)) {
            textView.setVisibility(View.VISIBLE);
        }
    }

    private void show(Fragment fragment) {
        if (fragment != null) {
            fm.beginTransaction().show(fragment).commit();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Storage_Utils.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindMainService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        UnbindMainService();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!LibsChecker.checkVitamioLibs(this))
            return;

        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fm = getSupportFragmentManager();
        startService(new Intent(this, MainService.class));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (boomMenuButton != null && !boomMenuButton.isClosed()) {
            boomMenuButton.dismiss();
        }
        if (ms != null) {
            ms.stopService(new Intent(this, MainService.class));
        }
        ImageLoader.clearCache(this);
    }

    @Override
    public void onBackPressed() {
        hide(lastFragment);
        if (lastTag.equals(Custant.TYPE_VIEWPAGER)){
            isViewPager = false;
            showImageListFragment();
        }
    }

}

