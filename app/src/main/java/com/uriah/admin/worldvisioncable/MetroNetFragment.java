package com.uriah.admin.worldvisioncable;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.uriah.admin.worldvisioncable.Models.InternetPacksModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class MetroNetFragment extends Fragment {

    View v;
    List<InternetPacksModel> internetPacksList;
    //  SubCategories_AdsAdapter adsAdapter;
    String API_URL="https://www.worldvisioncable.in/api/internet_packs.php";
    String id,image;
    SimpleAdapter madapter;


    private RecyclerView.Adapter adapter1;
    private RecyclerView recyclerView;
    String finalUrl;
    private RecyclerView.LayoutManager layoutManager;

    SpotsDialog dialog;

    public MetroNetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_metro_net, container, false);

        internetPacksList = new ArrayList<>();

        dialog = new SpotsDialog(getActivity(), R.style.Custom);
        dialog.getWindow().setBackgroundDrawableResource(
                R.color.transparent);
        dialog.show();


        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_subCategory);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        adapter1 = new MetroNetFragment.Adapter(internetPacksList,getContext());
        getData();
        recyclerView.setAdapter(adapter1);

        return v;
    }

    private void getData(){
        //Showing a progress dialog
      //  final ProgressDialog loading = ProgressDialog.show(getActivity(),"Loading Data", "Please wait...",false,false);



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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(jsonObjectRequest);
    }




    private void parseData(JSONObject array){


        try {
            JSONArray array1 = array.getJSONArray("Metro Net Network");

            Log.d("Length",array1.length()+"");

            for(int i = 0; i<array1.length(); i++) {

                JSONObject json = array1.getJSONObject(i);
                InternetPacksModel data = new InternetPacksModel();
                data.setId(json.getString("id"));
                data.setSpeed(json.getString("Speed"));
                data.setData_transfer(json.getString("Data_Transfer"));
                data.setAfter_Fup(json.getString("After_Fup"));
                data.setTraiff(json.getString("Traiff"));
                data.setGST(json.getString("GST"));
                data.setTotal(json.getString("Total"));
                data.setValidity(json.getString("Validity"));


                internetPacksList.add(data);
                dialog.dismiss();

            }
            adapter1.notifyDataSetChanged();
            dialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public class Adapter extends RecyclerView.Adapter<MetroNetFragment.Adapter.ViewHolder> {

        private ImageLoader imageLoader;
        private Context context;
        MetroNetFragment sub;

        //List of superHeroes
        List<InternetPacksModel> model;

        public Adapter(List<InternetPacksModel> model, Context context) {
            super();
            //Getting all the superheroes
            this.model = model;
            this.context = context;
        }

        @Override
        public MetroNetFragment.Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.internetpack_row, parent, false);
            MetroNetFragment.Adapter.ViewHolder viewHolder = new MetroNetFragment.Adapter.ViewHolder(v);
            sub = new MetroNetFragment();
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(MetroNetFragment.Adapter.ViewHolder holder, final int position) {

            final InternetPacksModel internetPacksModel = model.get(position);

            String PlanName=internetPacksModel.getSpeed()+" "+internetPacksModel.getData_transfer()+" FUP "+internetPacksModel.getAfter_Fup()+" "+"("+internetPacksModel.getValidity()+")";
            holder.txtPlanname.setText(PlanName);
            holder.txtSpeed.setText("Speed         :   "+internetPacksModel.getSpeed());
            holder.txtValidity.setText("Validity              :   "+internetPacksModel.getValidity());
            holder.txtDataTransfertxt.setText("Data Transfer   :   "+internetPacksModel.getData_transfer());
            holder.txtFUP.setText("After FUP   :   "+internetPacksModel.getAfter_Fup());
            holder.txtTarrif.setText("Rs."+internetPacksModel.getTraiff());
            holder.txtGST.setText("Rs. "+internetPacksModel.getGST());
            holder.txtTarrif.setText("Rs. "+internetPacksModel.getTotal());



            /*holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Toast.makeText(getActivity(),""+ model.get(position).getId(),Toast.LENGTH_LONG).show();
                    SingleTonModel.setAdsId(model.get(position).getId());

                    CategoryAdsDetailFragment detailFragment = new CategoryAdsDetailFragment();
                    FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.setCustomAnimations(R.anim.enter_left, R.anim.exit_right, R.anim.enter_right, R.anim.exit_left);
                    transaction.replace(R.id.content_main, detailFragment);
                    transaction.commit();
                }
            });*/
           /* holder.btn_recharge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(context,"Helo",Toast.LENGTH_LONG).show();
                    *//*Log.d("Call", model.get(position).getMobile());
                    Uri number = Uri.parse("tel:" + model.get(position).getMobile());

                    Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                    context.startActivity(callIntent);*//*
                }
            });*/


        }

        @Override
        public int getItemCount() {
            return model.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public TextView txtPlanname, txtSpeed,txtValidity,txtFUP,txtDataTransfertxt,txtTarrif,txtGST,txtTotal;

            Button btn_recharge;


            public ViewHolder(View itemView) {
                super(itemView);

                txtPlanname = (TextView) itemView.findViewById(R.id.planname);
                txtSpeed = (TextView) itemView.findViewById(R.id.txtSpeed);
                txtValidity = (TextView) itemView.findViewById(R.id.txtValidity);
                txtFUP = (TextView) itemView.findViewById(R.id.txtFUP);
                txtDataTransfertxt = (TextView) itemView.findViewById(R.id.txtDataTransfer);
                txtTarrif = (TextView) itemView.findViewById(R.id.txtTarrifAmt);
                txtGST = (TextView) itemView.findViewById(R.id.txtGSTAmt);
                txtTotal = (TextView) itemView.findViewById(R.id.txtTotal);

              //  btn_recharge = (Button) itemView.findViewById(R.id.btnRecharge);
            }
        }
    }

}
