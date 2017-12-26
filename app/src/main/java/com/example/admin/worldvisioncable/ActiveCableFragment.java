package com.example.admin.worldvisioncable;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.worldvisioncable.ChangePlan.ActiveCableTVChangeActivity;
import com.example.admin.worldvisioncable.ChangePlan.BroadbandPlanChangeActivity;
import com.example.admin.worldvisioncable.Models.SliderDataModel;
import com.example.admin.worldvisioncable.Models.UsedObject;
import com.example.admin.worldvisioncable.libraries.SampleFragment;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveCableFragment extends SampleFragment {

    View v;
    ViewPager view_pager_slider;
    ArrayList<SliderDataModel> al_slider;
    ImagePageAdapter sliderAdaper;
    private SpotsDialog home_dialog;
    private int mSeries1Index;
    LinearLayout dataLayout;
    TextView dataUsage;
    Button btnRenewBroadband,changePlan;
    int wheel=0;

    private static final String TAG ="Active Cable Fragment";

    TextView txtPackageName,txtPrice,txtProvider,txtValidity,txtDue_date,daysRemaining;

    String PlanName,PackageId,due_date,Package_name,
            Network_id,Provider_name,Location,Speed,Data_transfer,
            After_FUp,Tarrif,GST,Total,Validity;


    public ActiveCableFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_active_broadband, container, false);

        txtDue_date= v.findViewById(R.id.due_date);
        txtPackageName= v.findViewById(R.id.planname);
        txtPrice= v.findViewById(R.id.price);
        txtProvider= v.findViewById(R.id.provider);
        txtValidity= v.findViewById(R.id.validity);
        changePlan= v.findViewById(R.id.recommendedPlans);
        daysRemaining= v.findViewById(R.id.daysRemaining);
        btnRenewBroadband= v.findViewById(R.id.renewBroadbandplan);

        btnRenewBroadband.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UsedObject.setCurBBPlanDueDate(due_date);
                UsedObject.setCurBBPlanName(Package_name);
                UsedObject.setCurBBPlanPrice(Tarrif);
                UsedObject.setCurBBPlanValidity(Validity);
                UsedObject.setCurBBPlanProvider(Provider_name);
                UsedObject.setCurBBPlanId(PackageId);

                Log.d("Renew Pack name","S"+ UsedObject.getCurBBPlanName());


                Log.d("Renew", "Fragment");
                Intent i=new Intent(getActivity(),RenewPaymentFragment.class);
                startActivity(i);


            }
        });



        view_pager_slider = v.findViewById(R.id.view_pager_slider);

        dataLayout= v.findViewById(R.id.dataLayout);
        dataUsage= v.findViewById(R.id.dataUsed);

        dataLayout.setVisibility(View.GONE);

        home_dialog = new SpotsDialog(getActivity(), R.style.Custom);
        home_dialog.getWindow().setBackgroundDrawableResource(
                R.color.transparent);
        home_dialog.show();
        al_slider = new ArrayList<>();
        String SLIDER_URL = "https://www.worldvisioncable.in/api/slider-api.php";
        new SliderTask().execute(SLIDER_URL);

        Log.d("User Id","S"+ UsedObject.getId());
        //  String finalURL = "https://www.worldvisioncable.in/api/account/active_plan_broadband.php?userid="+ UsedObject.getId();
        //new ActiveBroadBandTask().execute("2635");

        new ActiveBroadBandTask().execute(UsedObject.getId());

        sliderAdaper = new ImagePageAdapter(al_slider);
        view_pager_slider.setClipToPadding(false);
        view_pager_slider.setPadding(40,0,40,0);
        view_pager_slider.setOffscreenPageLimit(15);
        view_pager_slider.setAdapter(sliderAdaper);

        changePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),ActiveCableTVChangeActivity.class);
                startActivity(i);
            }
        });

        return v;
    }

    @Override
    protected void createTracks() {
        //setDemoFinished(false);
        final DecoView decoView = getDecoView();
        final View view = getView();
        if (decoView == null || view == null) {
            return;
        }
        decoView.deleteAll();
        decoView.configureAngles( 280, 0);

        final float seriesMax = 80f;
        //final float seriesMin = 50f;
        decoView.addSeries(new SeriesItem.Builder(Color.argb(255, 64, 255, 64), Color.argb(255, 255, 0, 0))
                .setRange(0, seriesMax, seriesMax)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(10f))
                .build());

        decoView.addSeries(new SeriesItem.Builder(Color.argb(255, 0, 0, 0))
                .setRange(0, seriesMax, seriesMax)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(10f))
                .build());

        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 64, 255, 64), Color.argb(255, 255, 0, 0))
                .setRange(0, seriesMax, 0)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(10f))
                .setCapRounded(true)
                .setShowPointWhenEmpty(true)
                .build();

        mSeries1Index = decoView.addSeries(seriesItem1);


    }

    @Override
    protected void setupEvents() {
        final DecoView decoView = getDecoView();
        if (decoView == null || decoView.isEmpty()) {
            //  throw new IllegalStateException("Unable to add events to empty DecoView");
        }else {

            decoView.executeReset();
            decoView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                    .setDelay(200)
                    .setDuration(200)
                    .build());

            decoView.addEvent(new DecoEvent.Builder(wheel).setIndex(mSeries1Index).setDelay(100).build());
        }



    }


    public class ActiveBroadBandTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... params) {

            String res=activePlan(params);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            //Toast.makeText(getApplicationContext(), "Result:"+result, Toast.LENGTH_SHORT).show();

            try {
                JSONObject jsonObject = new JSONObject(result);
                String response = jsonObject.getString("response");
                Log.d(TAG,result);
                if(response.equalsIgnoreCase("200"))

                {

                    PackageId = jsonObject.getString("packageId");
                    due_date = jsonObject.getString("due_date");
                    Package_name = jsonObject.getString("Package_name");
                    //Network_id = jsonObject.getString("Network_id");
                    Provider_name = jsonObject.getString("Provider_name");
                    //Speed = jsonObject.getString("Speed");
                    //Data_transfer = jsonObject.getString("Data_Transfer");
                    //After_FUp = jsonObject.getString("After_Fup");
                    Tarrif = jsonObject.getString("price");
                    //GST = jsonObject.getString("GST");
                    //Total = jsonObject.getString("Total");
                    Validity = jsonObject.getString("Validity");

                    txtPackageName.setText(Package_name);
                    txtDue_date.setText("Due Date :"+due_date);
                    txtPrice.setText("Rs."+Tarrif);
                    txtValidity.setText(Validity);
                    txtProvider.setText(Provider_name);

                    final String DATE_FORMAT = "yyyy-MM-dd";  //or use "M/d/yyyy"


                    Calendar c = Calendar.getInstance();
                    System.out.println("Current time => " + c.getTime());




                    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
                    Date startDate, endDate;
                    Calendar calc = Calendar.getInstance();
                    System.out.println("Current time => " + calc.getTime());

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = df.format(c.getTime());
                    long numberOfDays = 0;
                    try {
                        startDate = dateFormat.parse(due_date);
                        endDate = dateFormat.parse(formattedDate);

                        int days = daysBetween(endDate,startDate);

                        Log.d("Days Remaining","D"+days);

                        daysRemaining.setText(days+ "Days Remaining for Next Bill");
                        dataLayout.setVisibility(View.VISIBLE);
                        dataUsage.setText(String.valueOf(30-days)+"\nDays");
                        wheel=days;
                        calculateAngle(wheel);
                        setupEvents();


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
            }
            catch(JSONException e){
                e.printStackTrace();
                Log.d("activecable","JSOn error");
            }

        }
    }

    private void calculateAngle(int angel) {

        float last;
        last=30-angel;

        last=(last*80);
        last=last/30;
        wheel=Math.round(last);

        setupEvents();



    }

    public int daysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    public String activePlan(String[] valuse) {
        String s="";
        try
        {
            HttpClient httpClient=new DefaultHttpClient();
            // HttpPost httpPost=new HttpPost("http://www.42estate.in/api/register-api.php");

            HttpPost httpPost=new HttpPost("https://www.worldvisioncable.in/api/account/active_plan_cable.php");
            List<NameValuePair> list=new ArrayList<NameValuePair>();

            list.add(new BasicNameValuePair("userid",valuse[0]));
            //   list.add(new BasicNameValuePair("password",valuse[1]));

            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=  httpClient.execute(httpPost);

            HttpEntity httpEntity=httpResponse.getEntity();
            s= readResponseLogin(httpResponse);

        }
        catch(Exception exception)  {}
        return s;
    }

    public String readResponseLogin(HttpResponse res) {
        InputStream is=null;
        String return_text="";
        try {
            is=res.getEntity().getContent();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
            String line="";
            StringBuffer sb=new StringBuffer();
            while ((line=bufferedReader.readLine())!=null)
            {
                sb.append(line);
            }
            return_text=sb.toString();
        } catch (Exception e)
        {

        }
        return return_text;

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
                    sliderAdaper.notifyDataSetChanged();

                }
                view_pager_slider.setAdapter(sliderAdaper);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class ImagePageAdapter extends PagerAdapter {


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
            final Context context = getActivity();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.echomepage_slider_items, container, false);
            //  final ImageView imageView = new ImageView(context);


            ImageView imageView =    itemView.findViewById(R.id.view_image_homepage);



         /* Bitmap mbitmap = ((BitmapDrawable) getResources().getDrawable(Integer.parseInt(al_sliderImages.get(position).getSliderImage()))).getBitmap();

            Bitmap imageRounded = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), imageView.getConfig());

            Canvas canvas = new Canvas(imageRounded);
            Paint mpaint = new Paint();
            mpaint.setAntiAlias(true);
            mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 20, 20, mpaint);// Round Image Corner 100 100 100 100*/
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
            home_dialog.dismiss();
            return itemView;
        }

        @Override
        public boolean isViewFromObject(final View view, final Object object) {
            return view ==  object ;
        }


    }

}
