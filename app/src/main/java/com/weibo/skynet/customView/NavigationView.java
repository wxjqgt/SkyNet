package com.weibo.skynet.customView;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weibo.skynet.R;


/**
 * Created by Administrator on 2016/4/16.
 */
public class NavigationView extends LinearLayout implements View.OnClickListener {

    private TextView navigationText;
    private ImageView underline_image;
    private OnSelecterLitener onSelecterLitener;
    private boolean isSelecter = false;
    private int position = 0;

    public NavigationView(Context context) {
        super(context);
        initView(context);
    }

    public NavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public NavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        inflate(context, R.layout.navigationlayout, this);
        navigationText = (TextView) findViewById(R.id.navigationtext);
        underline_image = (ImageView) findViewById(R.id.underline_image);
        underline_image.setVisibility(INVISIBLE);
    }

    public void setNavigationText(String text) {
        navigationText.setText(text);
    }

    public void setUnderline(int background) {
        underline_image.setBackgroundColor(background);
    }

    public void setOnSelecterLitener(OnSelecterLitener onSelecterLitener) {
        this.onSelecterLitener = onSelecterLitener;
        this.setOnClickListener(this);
    }

    public void setPosition(int position){
        this.position = position;
    }

    public void setDefault() {
        underline_image.setVisibility(INVISIBLE);
        navigationText.setTextColor(Color.BLACK);
        navigationText.setTextSize(17);
        isSelecter = false;
    }

    public void setNavigationItem() {
        if (!isSelecter) {
            navigationText.setTextColor(Color.parseColor("#ffaa66cc"));
            navigationText.setTextSize(19);
            underline_image.setVisibility(VISIBLE);
            isSelecter = true;
        }
    }

    @Override
    public void onClick(View v) {
        if (!isSelecter) {
            navigationText.setTextColor(Color.parseColor("#ffaa66cc"));
            navigationText.setTextSize(19);
            underline_image.setVisibility(VISIBLE);
            isSelecter = true;
        }
        onSelecterLitener.OnSelecter(position);
    }

    public interface OnSelecterLitener {
        void OnSelecter(int position);
    }

}
