package com.example.admin.worldvisioncable.Models;

import android.content.Context;

import java.util.List;

/**
 * Created by admin on 02/12/2017.
 */

public class SnapChannelModel {
    private int mGravity;
    private String mText;
    private Context mContext;
    private List<ChannelModel> channelModels;

    public SnapChannelModel(int gravity, String text, List<ChannelModel> channels,Context ctx) {
        mGravity = gravity;
        mText = text;
        channelModels = channels;
        mContext=ctx;
    }

    public String getText(){
        return mText;
    }

    public int getGravity(){
        return mGravity;
    }

    public List<ChannelModel> getChannelModels() {
        return channelModels;
    }

    public Context getmContext() {
        return mContext;
    }
}
