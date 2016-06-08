package com.weibo.skynet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.weibo.skynet.R;
import com.weibo.skynet.model.Mp3Info;
import com.weibo.skynet.util.ImageLoader;
import com.weibo.skynet.util.MediaUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/7.
 */
public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.Viewholder> {

    private List<Mp3Info> mp3Infos;
    private Context context;

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public MusicListAdapter(Context context) {
        this.context = context;
        this.mp3Infos = new ArrayList<>();
    }

    public void setData(List<Mp3Info> is) {
        this.mp3Infos.addAll(is);
    }


    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(context).inflate(R.layout.listview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final Viewholder holder, final int position) {
        Mp3Info mp3Info = mp3Infos.get(position);
        holder.im_bum.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoader.load(context, MediaUtils.getArtWorkUri(mp3Info.getId(),mp3Info.getAlbumId()),R.mipmap.ic_custom,holder.im_bum);
        holder.tv_singer.setText(mp3Info.getArtist());
        holder.songname.setText(mp3Info.getTitle());
        holder.tv_duration.setText(MediaUtils.formattime(mp3Info.getDuration()));

        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                /**
                 * @param v
                 */
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mp3Infos.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {

        public ImageView im_bum;
        private TextView tv_singer,songname,tv_duration;

        public Viewholder(View itemView) {
            super(itemView);
            tv_duration = (TextView) itemView.findViewById(R.id.duration);
            tv_singer = (TextView) itemView.findViewById(R.id.singer);
            songname = (TextView) itemView.findViewById(R.id.songname);
            im_bum = (ImageView) itemView.findViewById(R.id.album);
        }
    }

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

}
