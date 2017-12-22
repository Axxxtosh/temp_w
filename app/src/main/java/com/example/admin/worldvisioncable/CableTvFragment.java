package com.example.admin.worldvisioncable;



import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.worldvisioncable.Adapters.SnapChannelAdapter;
import com.example.admin.worldvisioncable.Models.CablePacksModel;
import com.example.admin.worldvisioncable.Models.ChannelModel;
import com.example.admin.worldvisioncable.Models.SnapChannelModel;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class CableTvFragment extends Fragment {

    Context context;
    List<ChannelModel> primechannelList;
    List<ChannelModel> royalchannelList;
    List<CablePacksModel> primechannelTitleList;
    List<CablePacksModel> royalchannelTitleList;
    TextView primecablePlanName,primecablePlanPrice,primecablePlanchannelCount;
    //  SubCategories_AdsAdapter adsAdapter;
    String API_URL="https://www.worldvisioncable.in/api/cable_packs.php";
    String id,image;
    SimpleAdapter madapter;
    private RecyclerView.LayoutManager layoutManager;
    SnapChannelAdapter snapAdapter;
    private RecyclerView recyclerView;
    View v;
    int channelcount;

    SpotsDialog dialog;
    public CableTvFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_cable_tv, container, false);

        primecablePlanName=(TextView)v.findViewById(R.id.primecablePlan);
        primecablePlanPrice=(TextView)v.findViewById(R.id.primecablePrice);
        //primecablePlanchannelCount=(TextView)v.findViewById(R.id.primecableChannelList);

        primechannelList = new ArrayList<>();
        royalchannelList = new ArrayList<>();

        royalchannelTitleList = new ArrayList<>();

        dialog = new SpotsDialog(getActivity(), R.style.Custom);
        dialog.getWindow().setBackgroundDrawableResource(
                R.color.transparent);
        dialog.show();
        getData();
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView_cabletv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        snapAdapter = new SnapChannelAdapter();

//        adapter1 = new CableTVActivity.Adapter(primechannelList,this);
//
//        recyclerView.setAdapter(adapter1);
        recyclerView.setAdapter(snapAdapter);
        return v;

    }

    private void getData(){
        //Showing a progress dialog
       // final ProgressDialog loading = ProgressDialog.show(getActivity(),"Loading Data", "Please wait...",false,false);



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
                dialog.dismiss();
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });

        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(jsonObjectRequest);


    }


    private void parseData(JSONObject array){


        try {
            JSONObject array1 = array.getJSONObject("Prime");

            Log.d("Prime Array:",array1.length()+"");

            primecablePlanName.setText("Prime");
            String validity=array1.getString("Validity");
            String price=array1.getString("price");

            primecablePlanPrice.setText("Rs."+price+" / Month");

           /* String count=String.valueOf(array1.length()-2);
            primecablePlanchannelCount.setText(count+ "Channels Available");*/


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

                snapAdapter.addSnap(new SnapChannelModel(Gravity.START, channelTitle, primechannelList,getActivity()));
            }
            snapAdapter.notifyDataSetChanged();

            /*String count=String.valueOf(channelcount);
            Log.d("Channel count",""+count);*/


            //adapter1.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
