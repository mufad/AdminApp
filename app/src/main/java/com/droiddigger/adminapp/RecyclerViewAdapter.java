package com.droiddigger.adminapp;

/**
 * Created by mufad on 10/29/2016.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Post> dataList;
    Context context;
    RecyclerViewAdapter(ArrayList<Post> list){
        this.dataList=list;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item,parent,false);
        ImageViewHolder imageViewHolder=new ImageViewHolder(view);
        context=view.getContext();
        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ImageViewHolder) holder).title.setText(dataList.get(position).getTitle());
        ((ImageViewHolder) holder).user.setText(dataList.get(position).getUserName());
        ((ImageViewHolder)holder).date.setText(dataList.get(position).getTimestamp());
        ((ImageViewHolder)holder).daystogo.setText(dataList.get(position).getEtDay());
        String url=dataList.get(position).getImageUrl();
        ImageView circleImageView=(((ImageViewHolder) holder).imageView);
        Glide.with(context).load(url).into(circleImageView);
        int [] color={R.color.bg1, R.color.bg2, R.color.bg3};
        Random r = new Random();
        int n=r.nextInt(3);
        ((ImageViewHolder)holder).container.setBackgroundColor(context.getResources().getColor(color[n]));


    }

    @Override
    public int getItemCount() {

        return dataList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, user, date, daystogo;
        LinearLayout container;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_image);
            title = (TextView) itemView.findViewById(R.id.titleText);
            user=(TextView)itemView.findViewById(R.id.postedByText);
            date=(TextView)itemView.findViewById(R.id.dateText);
            daystogo=(TextView)itemView.findViewById(R.id.daysToSolve);
            container=(LinearLayout)itemView.findViewById(R.id.container);
        }
    }

    public ArrayList<Post> getDataSet(){
        notifyDataSetChanged();
        Log.e("mufsize",getItemCount()+"");
        return dataList;
    }

}

