package com.example.admin.worldvisioncable.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.worldvisioncable.Models.ChannelModel;
import com.example.admin.worldvisioncable.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by admin on 02/12/2017.
 */

public class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.ViewHolder> {

    private List<ChannelModel> mApps;
    Context mContext;
    private boolean mHorizontal;
    private boolean mPager;

    public VerticalAdapter(boolean horizontal, boolean pager, List<ChannelModel> apps,Context mContext) {
        mHorizontal = horizontal;
        mApps = apps;
        mPager = pager;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.channel_row, parent, false);
        VerticalAdapter.ViewHolder viewHolder = new VerticalAdapter.ViewHolder(v);
        //sub = new ENETFragment();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChannelModel app = mApps.get(position);
        holder.channelText.setText(app.getChannel_name());

        Picasso.with(mContext).load(app.getChannel_image()).into(holder.channelImg);
        /*holder.imageView.setImageResource(app.getDrawable());
        holder.nameTextView.setText(app.getName());
        holder.ratingTextView.setText(String.valueOf(app.getRating()));*/
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView channelImg;
        public TextView channelText;


        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            channelImg = (ImageView) itemView.findViewById(R.id.channel_img);

            channelText = (TextView) itemView.findViewById(R.id.channel_name);

        }

        @Override
        public void onClick(View v) {
            Log.d("App", mApps.get(getAdapterPosition()).getChannel_name());
        }
    }
}
