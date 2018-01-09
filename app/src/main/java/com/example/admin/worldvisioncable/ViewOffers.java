package com.example.admin.worldvisioncable;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.MenuItem;


import android.view.View;
import android.widget.ImageView;

import com.example.admin.worldvisioncable.Models.SliderDataModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class ViewOffers extends AppCompatActivity {

    ArrayList<SliderDataModel> al_slider;

    private SpotsDialog home_dialog;
    CardView card1,card2,card3;
    ImageView slide1,slide2,slide3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_offers);
        card1= findViewById(R.id.card1);
        card2= findViewById(R.id.card2);
        card3= findViewById(R.id.card3);
        slide1= findViewById(R.id.slide1);
        slide2= findViewById(R.id.slide2);
        slide3= findViewById(R.id.slide3);
        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Offers");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        slide1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent i = new Intent(ViewOffers.this, LoginActivity.class);
                startActivity(i);
            }
        });
        slide2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent i = new Intent(ViewOffers.this, LoginActivity.class);
                startActivity(i);
            }
        });
        slide2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent i = new Intent(ViewOffers.this, LoginActivity.class);
                startActivity(i);
            }
        });





        home_dialog = new SpotsDialog(this, R.style.Custom);
        home_dialog.getWindow().setBackgroundDrawableResource(
                R.color.transparent);
        home_dialog.show();
        al_slider = new ArrayList<>();
        String SLIDER_URL = "https://www.worldvisioncable.in/api/slider-api.php";
        new SliderTask().execute(SLIDER_URL);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressLint("StaticFieldLeak")
    private class SliderTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... strings) {

            try {
                URL myURL = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) myURL.openConnection();
                connection.connect();

                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                StringBuilder sb = new StringBuilder();
                String str = br.readLine();

                while (str != null)
                {
                    sb.append(str);
                    str = br.readLine();
                }

                Log.d("SliderData",sb.toString());
                return sb.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            home_dialog.dismiss();

            try {
                JSONObject mainObject = new JSONObject(s);
                JSONArray mainArray= mainObject.getJSONArray("result");

                for (int i = 0; i < mainArray.length(); i++)
                {
                    // JSONObject childObject = mainArray.getJSONObject(i);
                    String sliderImage = mainArray.getString(i);
                   /* String sliderID = childObject.getString("id");
                    String sliderName = childObject.getString("name");*/


                    SliderDataModel sliderModel = new SliderDataModel(sliderImage,"","");

                    al_slider.add(sliderModel);

                    /*Picasso.with(getApplicationContext())
                            .load()
                            .placeholder(R.color.white) // optional
                            .error(R.color.white)         // optional
                            .into(imageView);*/


                }
                slide1.setScaleType(ImageView.ScaleType.FIT_XY);
                Picasso.with(getApplicationContext())
                        .load(al_slider.get(0).getSliderImage())
                        .resize(600, 200)
                        .placeholder(R.color.white) // optional
                        .error(R.color.white)         // optional
                        .into(slide1);
                slide2.setScaleType(ImageView.ScaleType.FIT_XY);
                Picasso.with(getApplicationContext())
                        .load(al_slider.get(1).getSliderImage())
                        .resize(600, 200)
                        .placeholder(R.color.white) // optional
                        .error(R.color.white)         // optional
                        .into(slide2);
                slide3.setScaleType(ImageView.ScaleType.FIT_XY);
                Picasso.with(getApplicationContext())
                        .load(al_slider.get(2).getSliderImage())
                        .resize(600, 200)
                        .placeholder(R.color.white) // optional
                        .error(R.color.white)         // optional
                        .into(slide3);




            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
   /* private class ImagePageAdapter extends PagerAdapter {


        //private final int[] mImages = new int[] {R.mipmap.slider_two_cloth,R.mipmap.slider_new_oneo,R.mipmap.shopping3,R.mipmap.slider_clothing,R.mipmap.slider_new_oneo};

        ArrayList<SliderDataModel> al_sliderImages;

        public ImagePageAdapter(ArrayList<SliderDataModel> al_sliderImages) {
            this.al_sliderImages = al_sliderImages;
        }

        @Override
        public int getCount() {
            return this.al_sliderImages.size();
        }


        @Override
        public void destroyItem(final ViewGroup container, final int position, final Object object) {
            View view = (View) object;
            container.removeView(view);
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            final Context context = container.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.echomepage_slider_items, container, false);
            //  final ImageView imageView = new ImageView(context);


            ImageView imageView =    itemView.findViewById(R.id.view_image_homepage);



         *//* Bitmap mbitmap = ((BitmapDrawable) getResources().getDrawable(Integer.parseInt(al_sliderImages.get(position).getSliderImage()))).getBitmap();

            Bitmap imageRounded = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), imageView.getConfig());

            Canvas canvas = new Canvas(imageRounded);
            Paint mpaint = new Paint();
            mpaint.setAntiAlias(true);
            mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 20, 20, mpaint);// Round Image Corner 100 100 100 100*//*
            final int padding = 15;
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//
            //imageView.setImageBitmap(imageRounded);

            Picasso.with(context.getApplicationContext())
                    .load(al_sliderImages.get(position).getSliderImage())
                    .placeholder(R.color.white) // optional
                    .error(R.color.white)         // optional
                    .into(imageView);
            //  imageView.setImageResource(this.mImages[position]);


            container.addView(itemView);

            return itemView;
        }

        @Override
        public boolean isViewFromObject(final View view, final Object object) {
            return view ==  object ;
        }


    }*/


}
