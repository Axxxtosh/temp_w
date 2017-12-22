package com.example.admin.worldvisioncable.Models;

/**
 * Created by admin on 01/12/2017.
 */

public class CablePacksModel {

    String ChannelName;

    public CablePacksModel(String ChannelName)
    {
        this.ChannelName=ChannelName;
    }

    public String getChannelName() {
        return ChannelName;
    }

    public void setChannelName(String channelName) {
        ChannelName = channelName;
    }
}
