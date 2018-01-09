package com.uriah.admin.worldvisioncable;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;

import android.view.View;

import android.widget.Button;

import android.widget.SimpleAdapter;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.uriah.admin.worldvisioncable.Adapters.SnapChannelAdapter;
import com.uriah.admin.worldvisioncable.Models.CablePacksModel;
import com.uriah.admin.worldvisioncable.Models.ChannelModel;

import com.uriah.admin.worldvisioncable.Models.SnapChannelModel;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class CableTVActivity extends AppCompatActivity {

    Context context;
    List<ChannelModel> primechannelList;
    List<ChannelModel> royalchannelList;
    List<CablePacksModel> primechannelTitleList;
    List<CablePacksModel> royalchannelTitleList;
    //  SubCategories_AdsAdapter adsAdapter;
    String API_URL="https://www.worldvisioncable.in/api/cable_packs.php";
    String id,image;
    SimpleAdapter madapter;
    private RecyclerView.LayoutManager layoutManager;
    SnapChannelAdapter snapAdapter;
    private RecyclerView recyclerView;
    Button login;

    SpotsDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cable_tv);

        primechannelList = new ArrayList<>();
        royalchannelList = new ArrayList<>();

        login=(Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Login","Clicked");

                Intent i=new Intent(CableTVActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        dialog = new SpotsDialog(this, R.style.Custom);
        dialog.getWindow().setBackgroundDrawableResource(
                R.color.transparent);
        dialog.show();

        royalchannelTitleList = new ArrayList<>();

        getData();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView_cabletv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        snapAdapter = new SnapChannelAdapter();

//        adapter1 = new CableTVActivity.Adapter(primechannelList,this);
//
//        recyclerView.setAdapter(adapter1);
        recyclerView.setAdapter(snapAdapter);

    }

    private void getData(){
        //Showing a progress dialog
        //final ProgressDialog loading = ProgressDialog.show(this,"Loading Data", "Please wait...",false,false);



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(API_URL,null,new Response.Listener<JSONObject>(){


            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();

                Log.d("Log",response+"");
                parseData(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(jsonObjectRequest);
    }




    private void parseData(JSONObject array){


        try {
            JSONObject array1 = array.getJSONObject("Prime");

            Log.d("Prime Array:",array1.length()+"");

            for(int i = 2; i<array1.length(); i++) {


                primechannelList = new ArrayList<>();

                Log.d("Channel Name",""+array1.names().get(i));
                String channelTitle=String.valueOf(array1.names().get(i));
               // primechannelTitleList.add(new CablePacksModel(channelTitle));

                JSONObject channelArray = array1.getJSONObject(channelTitle);

                Log.d("channelArray Length",channelArray.length()+"");


                for(int k=0;k<channelArray.length()-2;k++)
                {
                    String index=String.valueOf(k);
                    JSONObject channels=channelArray.getJSONObject(index);
                    Log.d("Channels",channels.getString("channel_name"));
                    ChannelModel data = new ChannelModel();
                    data.setId(channels.getString("id"));
                    data.setChannel_id(channels.getString("channel_id"));
                    data.setChannel_image(channels.getString("channel_image"));
                    data.setChannel_name(channels.getString("channel_name"));
                    data.setCp_id(channels.getString("cp_id"));
                    data.setProvider_id(channels.getString("Provider_id"));
                    primechannelList.add(data);
                }

                snapAdapter.addSnap(new SnapChannelModel(Gravity.START, channelTitle, primechannelList,this));
            }
            snapAdapter.notifyDataSetChanged();
            dialog.dismiss();

            //adapter1.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
