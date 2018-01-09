package com.uriah.admin.worldvisioncable.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.uriah.admin.worldvisioncable.Models.SnapChannelModel;
import com.uriah.admin.worldvisioncable.R;
import com.github.rubensousa.gravitysnaphelper.GravityPagerSnapHelper;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;

/**
 * Created by admin on 02/12/2017.
 */

public class SnapChannelAdapter extends RecyclerView.Adapter<SnapChannelAdapter.ViewHolder> implements GravitySnapHelper.SnapListener{
    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    Context mContext;

    private ArrayList<SnapChannelModel> mSnaps;
    // Disable touch detection for parent recyclerView if we use vertical nested recyclerViews
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        }
    };

    public SnapChannelAdapter() {
        mSnaps = new ArrayList<>();
    }

    public void addSnap(SnapChannelModel snap) {
        mSnaps.add(snap);
    }

    @Override
    public int getItemViewType(int position) {
        SnapChannelModel snap = mSnaps.get(position);
        switch (snap.getGravity()) {
            case Gravity.CENTER_VERTICAL:
                return VERTICAL;
            case Gravity.CENTER_HORIZONTAL:
                return HORIZONTAL;
            case Gravity.START:
                return HORIZONTAL;
            case Gravity.TOP:
                return VERTICAL;
            case Gravity.END:
                return HORIZONTAL;
            case Gravity.BOTTOM:
                return VERTICAL;
        }
        return HORIZONTAL;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.channellist_row, parent, false);
        SnapChannelAdapter.ViewHolder viewHolder = new SnapChannelAdapter.ViewHolder(v);
        //sub = new ENETFragment();
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SnapChannelModel snap = mSnaps.get(position);
        holder.snapTextView.setText(snap.getText());

        if (snap.getGravity() == Gravity.START || snap.getGravity() == Gravity.END) {
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder
                    .recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            new GravitySnapHelper(snap.getGravity(), false, this).attachToRecyclerView(holder.recyclerView);
        } else if (snap.getGravity() == Gravity.CENTER_HORIZONTAL ||
                snap.getGravity() == Gravity.CENTER_VERTICAL) {
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder
                    .recyclerView.getContext(), snap.getGravity() == Gravity.CENTER_HORIZONTAL ?
                    LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
            new LinearSnapHelper().attachToRecyclerView(holder.recyclerView);
        } else if (snap.getGravity() == Gravity.CENTER) { // Pager snap
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder
                    .recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            new GravityPagerSnapHelper(Gravity.START).attachToRecyclerView(holder.recyclerView);
        } else { // Top / Bottom
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder
                    .recyclerView.getContext()));
            new GravitySnapHelper(snap.getGravity()).attachToRecyclerView(holder.recyclerView);
        }


        holder.recyclerView.setAdapter(new VerticalAdapter(snap.getGravity() == Gravity.START
                || snap.getGravity() == Gravity.END
                || snap.getGravity() == Gravity.CENTER_HORIZONTAL,
                snap.getGravity() == Gravity.CENTER, snap.getChannelModels(),snap.getmContext()));
    }

    @Override
    public int getItemCount() {
        return mSnaps.size();
    }

    @Override
    public void onSnap(int position) {
        Log.d("Snapped: ", position + "");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView snapTextView;
        public Button moreBtn;
        public RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            snapTextView = (TextView) itemView.findViewById(R.id.titleChannel);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.horizontal_list);
            moreBtn=(Button)itemView.findViewById(R.id.morebtn);
        }

    }
}
