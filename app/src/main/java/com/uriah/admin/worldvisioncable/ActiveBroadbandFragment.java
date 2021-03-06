package com.uriah.admin.worldvisioncable;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.TextView;


import com.uriah.admin.worldvisioncable.Models.UsedObject;
import com.uriah.admin.worldvisioncable.libraries.SampleFragment;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


public class ActiveBroadbandFragment extends SampleFragment {

    View v;

    private int mSeries1Index;
    LinearLayout dataLayout, mainFragment, loading;
    TextView dataUsage, error;
    Button btnRenewBroadband,changePlan;
    int wheel=0;

    TextView txtPackageName,txtPrice,txtProvider,txtValidity,txtDue_date,daysRemaining;

    String PackageId, due_date, Package_name,
            Network_id, Provider_name, Speed, Data_transfer,
            After_FUp,Tarrif,GST,Total,Validity;

    HashMap<String, String> hm = new HashMap<String, String>();


    public ActiveBroadbandFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_active_broadband, container, false);
        //Loading
        loading = v.findViewById(R.id.loading);

        error = v.findViewById(R.id.error);
        txtDue_date= v.findViewById(R.id.due_date);
        txtPackageName= v.findViewById(R.id.planname);
        txtPrice= v.findViewById(R.id.price);
        txtProvider= v.findViewById(R.id.provider);
        txtValidity= v.findViewById(R.id.validity);
        changePlan= v.findViewById(R.id.recommendedPlans);
        daysRemaining= v.findViewById(R.id.daysRemaining);
        btnRenewBroadband= v.findViewById(R.id.renewBroadbandplan);

        mainFragment = v.findViewById(R.id.main_layout);
        mainFragment.setVisibility(View.GONE);



        btnRenewBroadband.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UsedObject.setCurBBPlanDueDate(due_date);
                UsedObject.setCurBBPlanName(Package_name);
                UsedObject.setCurBBPlanPrice(Total);
                UsedObject.setCurBBPlanValidity(Validity);
                UsedObject.setCurBBPlanProvider(Provider_name);
                UsedObject.setCurBBPlanId("1");

                Log.d("Renew Pack name","S"+ UsedObject.getCurBBPlanName());


                Log.d("Renew", "Fragment");
                Intent i=new Intent(getActivity(),RenewPaymentFragment.class);
                startActivity(i);

            }
        });


        dataLayout= v.findViewById(R.id.dataLayout);
        dataUsage= v.findViewById(R.id.dataUsed);


        // dataLayout.setVisibility(View.GONE);




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
    public void onResume() {
        super.onResume();

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
                .setLineWidth(getDimension(15f))
                .build());

        decoView.addSeries(new SeriesItem.Builder(Color.argb(255, 0, 0, 0))
                .setRange(0, seriesMax, seriesMax)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(15f))
                .build());

        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 64, 255, 64), Color.argb(255, 255, 0, 0))
                .setRange(0, seriesMax, 0)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(15f))
                .setCapRounded(true)
                .setShowPointWhenEmpty(true)
                .build();

        mSeries1Index = decoView.addSeries(seriesItem1);


    }

    @Override
    protected void setupEvents() {
        final DecoView decoView = getDecoView();
        if (decoView == null || decoView.isEmpty()) {
           // throw new IllegalStateException("Unable to add events to empty DecoView");
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

            //Toast.makeText(getApplicationContext(), "Result:"+result, Toast.LENGTH_SHORT).show();

            try {
                JSONObject jsonObject = new JSONObject(result);
                String response = jsonObject.getString("response");
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
                    Network_id = jsonObject.getString("Network_id");
                    Provider_name = jsonObject.getString("Provider_name");
                    Speed = jsonObject.getString("Speed");
                    Data_transfer = jsonObject.getString("Data_Transfer");
                    After_FUp = jsonObject.getString("After_Fup");
                    Tarrif = jsonObject.getString("Traiff");
                    GST = jsonObject.getString("GST");
                    Total = jsonObject.getString("Total");
                    Validity = jsonObject.getString("Validity");

                    txtPackageName.setText(Package_name);
                    txtDue_date.setText("Due Date :"+due_date);
                    txtPrice.setText("Rs."+Total);
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



                        if (days < 0) {
                            daysRemaining.setText("Your plan has been expired");
                            dataUsage.setText("Expired");


                        } else {

                            daysRemaining.setText(days + "Days Remaining for Next Bill");
                            dataUsage.setText(String.valueOf(30 - days) + "\nDays");

                        }

                        wheel=days;
                        calculateAngle(wheel);
                        setupEvents();
                        mainFragment.setVisibility(View.VISIBLE);


                    } catch (ParseException e) {
                        e.printStackTrace();


                    }


                }

            }
             catch(JSONException e){
                 loading.setVisibility(View.GONE);
                    e.printStackTrace();


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
        //https://www.worldvisioncable.in/api/account/active_plan_cable.php userid valuse[0]
        hm.put("userid", valuse[0]);


        URL url;
        String response = "";
        try {
            url = new URL("https://www.worldvisioncable.in/api/account/active_plan_broadband.php");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(hm));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }




}
