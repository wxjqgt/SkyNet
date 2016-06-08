package com.weibo.skynet.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.weibo.skynet.R;
import com.weibo.skynet.util.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/23.
 */
public abstract class CommonRecyclerViewAdapter<T> extends RecyclerView.Adapter<CommonRecyclerViewAdapter.ViewHolder>{

    protected Context context;
    protected List<T> data;
    protected int layout;
    protected List<Integer> mHeights;

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public void addData(List<T> value){
        int position = data.size() - 1;
        this.data.addAll(value);
        this.notifyItemInserted(position);
    }

    public CommonRecyclerViewAdapter(Context context, int layout,List<T> data){
        this.context = context;
        this.data = new ArrayList<>();
        this.data.addAll(data);
        this.layout = layout;

        mHeights = new ArrayList<>();
        int size = data.size();
        for (int i = 0; i < size; i++) {
            mHeights.add((int) (100 + Math.random() * 300));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layout,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        convert(holder,data.get(position),position);
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
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
                    return true;
                }
            });
        }
    }

    public abstract void convert(ViewHolder holder, T t,int position);

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private SparseArray<View> views;
        private Context context;

        public ViewHolder(View itemView,Context context) {
            super(itemView);
            this.views = new SparseArray<>();
            this.context = context;
        }

        public void setImageUrl(int id,String url){
            ImageView imageView = (ImageView) views.get(id);
            if (imageView == null){
                imageView = (ImageView) itemView.findViewById(id);
                views.put(id,imageView);
            }
            ImageLoader.load(context,url, R.mipmap.ic_custom,imageView);
        }

        public void setText(int id,String text){
            TextView textView = (TextView) views.get(id);
            if (textView == null){
                textView = (TextView) itemView.findViewById(id);
                views.put(id,textView);
            }
            textView.setText(text);
        }

        public <T extends Object> T getView(int id){
            View view = views.get(id);
            if (view == null){
                view = itemView.findViewById(id);
                views.put(id,view);
            }
            return (T)view;
        }

    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
}
