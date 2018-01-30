package com.uriah.admin.worldvisioncable;



import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.app.AlertDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.uriah.admin.worldvisioncable.Models.UsedObject;
import com.uriah.admin.worldvisioncable.libraries.SampleFragment;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;

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


    private int mSeries1Index;
    LinearLayout dataLayout, mainLayout, loading;

    TextView dataUsage, error;
    Button btnRenewBroadband,changePlan;
    int wheel=0;

    private static final String TAG ="Active Cable Fragment";

    TextView txtPackageName,txtPrice,txtProvider,txtValidity,txtDue_date,daysRemaining;

    String PackageId, due_date, Package_name,
            Provider_name,
            Tarrif, Validity;


    public ActiveCableFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_new, container, false);
        //Loading

        loading = v.findViewById(R.id.loading);

        txtDue_date= v.findViewById(R.id.due_date);
        txtPackageName= v.findViewById(R.id.planname);
        txtPrice= v.findViewById(R.id.price);
        txtProvider= v.findViewById(R.id.provider);
        txtValidity= v.findViewById(R.id.validity);
        changePlan= v.findViewById(R.id.recommendedPlans);
        daysRemaining= v.findViewById(R.id.daysRemaining);
        btnRenewBroadband= v.findViewById(R.id.renewBroadbandplan);
        error = v.findViewById(R.id.error);

        btnRenewBroadband.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UsedObject.setCurBBPlanDueDate(due_date);
                UsedObject.setCurCTPlanDueDate(due_date);
                UsedObject.setCurBBPlanName(Package_name);
                UsedObject.setCurBBPlanPrice(Tarrif);
                UsedObject.setCurBBPlanValidity(Validity);
                UsedObject.setCurBBPlanProvider(Provider_name);
                UsedObject.setCurBBPlanId("2");

                Log.d("Renew Pack name","S"+ UsedObject.getCurBBPlanName());


                Log.d("Renew", "Fragment");
                Intent i=new Intent(getActivity(),RenewPaymentFragment.class);
                startActivity(i);


            }
        });


        dataLayout= v.findViewById(R.id.dataLayout);
        dataUsage= v.findViewById(R.id.dataUsed);
        mainLayout = v.findViewById(R.id.main_layout);

        mainLayout.setVisibility(View.GONE);

        //  dataLayout.setVisibility(View.GONE);


        Log.d("User Id","S"+ UsedObject.getId());
        new ActiveBroadBandTask().execute(UsedObject.getId());


        changePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getActivity());
                }

                builder.setTitle("To Change Plan")
                        .setMessage("To change existing plan, Please call us at 080-25534744 or Mail us at info@worldvision.com")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })

                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
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
        decoView.configureAngles(280, -10);

        final float seriesMax = 80f;
        //final float seriesMin = 50f;
        decoView.addSeries(new SeriesItem.Builder(Color.argb(255, 64, 255, 64), Color.argb(255, 255, 0, 0))
                .setRange(0, seriesMax, seriesMax)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(12f))
                .build());

        decoView.addSeries(new SeriesItem.Builder(Color.argb(255, 0, 0, 0))
                .setRange(0, seriesMax, seriesMax)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(12f))
                .build());

        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 64, 255, 64), Color.argb(255, 255, 0, 0))
                .setRange(0, seriesMax, 0)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(12f))
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
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {

            String res=activePlan(params);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject jsonObject = new JSONObject(result);
                String response = jsonObject.getString("response");
                Log.d(TAG,result);
                if (response.equalsIgnoreCase("202")) {
                    loading.setVisibility(View.GONE);

                    error.setVisibility(View.VISIBLE);
                }
                if(response.equalsIgnoreCase("200"))

                {

                    loading.setVisibility(View.GONE);

                    PackageId = jsonObject.getString("packageId");
                    due_date = jsonObject.getString("due_date");
                    Package_name = jsonObject.getString("Package_name");
                    Provider_name = jsonObject.getString("Provider_name");
                    Tarrif = jsonObject.getString("price");
                    Validity = jsonObject.getString("Validity");
                    txtPackageName.setText(Package_name);
                    txtDue_date.setText("Due Date :"+due_date);
                    txtPrice.setText("Rs."+Tarrif);
                    txtValidity.setText(Validity);
                    txtProvider.setText(Provider_name);
                    final String DATE_FORMAT = "yyyy-MM-dd";
                    Calendar c = Calendar.getInstance();
                    System.out.println("Current time => " + c.getTime());
                    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
                    Date startDate, endDate;
                    Calendar calc = Calendar.getInstance();
                    System.out.println("Current time => " + calc.getTime());
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = df.format(c.getTime());
                    try {
                        startDate = dateFormat.parse(due_date);
                        endDate = dateFormat.parse(formattedDate);

                        int days = daysBetween(endDate,startDate);

                        Log.d("Days Remaining","D"+days);



                        if (days < 0 || days > 30) {
                            daysRemaining.setText("Your plan has been expired");
                            dataUsage.setText("Expired");
                            days = 30;
                            // dataLayout.setVisibility(View.VISIBLE);


                        } else {

                            daysRemaining.setText(days + " Days Remaining for Next Bill");
                            dataUsage.setText(String.valueOf(30 - days) + "\nDays");
                            // dataLayout.setVisibility(View.VISIBLE);
                        }


                        wheel=days;
                        calculateAngle(wheel);
                        setupEvents();

                        mainLayout.setVisibility(View.VISIBLE);


                    } catch (ParseException e) {
                        e.printStackTrace();

                        mainLayout.setVisibility(View.VISIBLE);
                    }


                }
            }
            catch(JSONException e){
                e.printStackTrace();
                loading.setVisibility(View.GONE);

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

        } catch (Exception exception) {

            Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
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


}
