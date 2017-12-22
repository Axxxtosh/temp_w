package com.example.admin.worldvisioncable.Models;

/**
 * Created by root on 16/10/17.
 */

public class SliderDataModel {

    String sliderImage,sliderName,sliderID;

    public SliderDataModel(String sliderImage, String sliderName, String sliderID) {
        this.sliderImage = sliderImage;
        this.sliderName = sliderName;
        this.sliderID = sliderID;
    }

    public String getSliderImage() {
        return sliderImage;
    }

    public void setSliderImage(String sliderImage) {
        this.sliderImage = sliderImage;
    }

    public String getSliderName() {
        return sliderName;
    }

    public void setSliderName(String sliderName) {
        this.sliderName = sliderName;
    }

    public String getSliderID() {
        return sliderID;
    }

    public void setSliderID(String sliderID) {
        this.sliderID = sliderID;
    }
}
