package com.uriah.admin.worldvisioncable.Models;

/**
 * Created by admin on 01/12/2017.
 */

public class ChannelModel {
    String id;
    String cp_id;
    String channel_id;
    String Provider_id;
    String channel_name;
    String channel_image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCp_id() {
        return cp_id;
    }

    public void setCp_id(String cp_id) {
        this.cp_id = cp_id;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getProvider_id() {
        return Provider_id;
    }

    public void setProvider_id(String provider_id) {
        Provider_id = provider_id;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getChannel_image() {
        return channel_image;
    }

    public void setChannel_image(String channel_image) {
        this.channel_image = channel_image;
    }
}
