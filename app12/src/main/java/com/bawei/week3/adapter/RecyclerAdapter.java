package com.bawei.week3.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bawei.week3.Bean.ProductsListBean;
import com.bawei.week3.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ProductsListBean.Data> mList = new ArrayList<>();

    private boolean mIsLinear;

    public RecyclerAdapter(Context context, boolean isLinear) {
        mContext = context;
        mIsLinear = isLinear;
    }

    public List<ProductsListBean.Data> getData(){
        //TODO:完成剩余布局，完成javaBean剩余参数，完成数据展示，我不写了
        return  mList;
    }

    public void setData(List<ProductsListBean.Data> list) {
        if (list != null) {
            mList = list;
        }

        notifyDataSetChanged();
    }

    public void addData(List<ProductsListBean.Data> list) {
        if (list != null) {
            mList.addAll(list);
        }

        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (mIsLinear) {
            View v = View.inflate(mContext, R.layout.item_recycle_linear, null);
            holder = new ViewHolderLinear(v);
        }else{
            View v = View.inflate(mContext, R.layout.item_recycle_grid, null);
            holder = new ViewHolderGrid(v);
        }

        return holder;

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        String img = mList.get(position).getImages();
        String[] imgs = img.split("\\|");

        if(mIsLinear) {
            ViewHolderLinear holderLinear = (ViewHolderLinear) holder;
            Glide.with(mContext).load(imgs[0]).into(holderLinear.img);
            holderLinear.textView.setText(mList.get(position).getTitle());
            holderLinear.ll_item_recycle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mClickListener != null){
                        mClickListener.onItemClick(position);
                    }
                }
            });
        }else{
            ViewHolderGrid holderGrid = (ViewHolderGrid) holder;
            Glide.with(mContext).load(imgs[0]).into(holderGrid.img);
            holderGrid.textView.setText(mList.get(position).getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class ViewHolderLinear extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView textView;
        public LinearLayout ll_item_recycle;

        public ViewHolderLinear(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_item_recycle);
            img = itemView.findViewById(R.id.iv_item_recycle);
            ll_item_recycle = itemView.findViewById(R.id.ll_item_recycle);
        }
    }

    class ViewHolderGrid extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView textView;

        public ViewHolderGrid(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_item_grid);
            img = itemView.findViewById(R.id.iv_item_grid);
        }
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnLongItemClickListener {
        void onItemLongClick(int position);
    }

    private OnItemClickListener mClickListener;
    private OnLongItemClickListener mLongItemClickListener;

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public void setOnLongItemClickListener(OnLongItemClickListener longItemClickListener) {
        this.mLongItemClickListener = longItemClickListener;
    }

}
