package com.uriah.admin.worldvisioncable;



import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;


import com.uriah.admin.worldvisioncable.Models.ComplaintsModel;

import com.uriah.admin.worldvisioncable.Models.UsedObject;


import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisteredComplaints extends Fragment {

    List<ComplaintsModel> complaintsModelList;


    //  SubCategories_AdsAdapter adsAdapter;
   // String API_URL="https://www.worldvisioncable.in/api/account/view_complaints.php?userid=2635";
    String id,image;
    SimpleAdapter madapter;
    View v;

    LinearLayout loading;

    private RecyclerView.Adapter adapter1;
    private RecyclerView recyclerView;
    String finalUrl;
    HashMap<String, String> hm = new HashMap<String, String>();



    private RecyclerView.LayoutManager layoutManager;

    public RegisteredComplaints() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_registered_complaints, container, false);
        loading = v.findViewById(R.id.loading);

        complaintsModelList = new ArrayList<>();


        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_subCategory);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter1 = new Adapter(complaintsModelList,getContext());



        new ViewComplaintsAsync().execute(UsedObject.getId());

        recyclerView.setAdapter(adapter1);

        return v;
    }

    public class ViewComplaintsAsync extends AsyncTask<String, Integer, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {


            String res=login(params);


            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            //Toast.makeText(getApplicationContext(), "Result:"+result, Toast.LENGTH_SHORT).show();

            try {
                loading.setVisibility(View.GONE);
                JSONObject jsonObject = new JSONObject(result);
                String response = jsonObject.getString("response");
                Log.d("Complaints Log",result);
                JSONArray arr=jsonObject.getJSONArray("items");

                for(int i=0;i<arr.length();i++)
                {
                    JSONObject complaints=arr.getJSONObject(i);
                    String id=complaints.getString("id");
                    ComplaintsModel data = new ComplaintsModel();
                    data.setId(complaints.getString("id"));
                    data.setName(complaints.getString("name"));
                    data.setEmail(complaints.getString("email"));
                    data.setPhone(complaints.getString("phone"));
                    data.setNature_of_complaint(complaints.getString("nature_of_complaint"));
                    data.setDescription(complaints.getString("description"));
                    data.setStatus(complaints.getString("status"));
                    data.setCreated_on(complaints.getString("created_on"));

                    complaintsModelList.add(data);

                    Log.d("ID","S"+id);
                }
                adapter1.notifyDataSetChanged();
                /*String message=jsonObject.getString("message");
                Log.d("Json Count","S"+jsonObject.length());
                if(message.equalsIgnoreCase("Data Available"))
                {
                    for(int i=0;i<jsonObject.length()-3;i++)
                    {
                        String index=String.valueOf(i);
                        JSONObject complaints=jsonObject.getJSONObject(index);

                        String id=complaints.getString("id");
                        Log.d("Complaints Id","C"+id);
                    }

                   // JSONObject obj=jsonObject.getJSONObject()
                }*/
                //Log.d("Complaints Length",)




            } catch (JSONException e) {
                e.printStackTrace();
                loading.setVisibility(View.GONE);
            }
        }

        public String login(String[] valuse) {
            String s="";
            //https://www.worldvisioncable.in/api/account/active_plan_cable.php userid valuse[0]
            hm.put("userid", valuse[0]);


            URL url;
            String response = "";
            try {
                url = new URL("https://www.worldvisioncable.in/api/account/view_complaints.php");

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
    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private ImageLoader imageLoader;
        private Context context;
        RegisteredComplaints sub;

        //List of superHeroes
        List<ComplaintsModel> model;

        public Adapter(List<ComplaintsModel> model, Context context) {
            super();
            //Getting all the superheroes
            this.model = model;
            this.context = context;
        }

        @Override
        public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.registered_complaints_row, parent, false);
            Adapter.ViewHolder viewHolder = new Adapter.ViewHolder(v);
            sub = new RegisteredComplaints();
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(Adapter.ViewHolder holder, final int position) {

            final ComplaintsModel complaintsModel = model.get(position);

            holder.nature_of_complaints.setText(complaintsModel.getNature_of_complaint());
            holder.description_complaints.setText(complaintsModel.getDescription());
            holder.status_complaints.setText(complaintsModel.getStatus());
            holder.raised_on.setText(complaintsModel.getCreated_on());

        }

        @Override
        public int getItemCount() {
            return model.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public TextView nature_of_complaints,description_complaints,status_complaints,raised_on;

            Button btn_recharge;


            public ViewHolder(View itemView) {
                super(itemView);

                nature_of_complaints = (TextView) itemView.findViewById(R.id.nature_of_complaints);
                description_complaints = (TextView) itemView.findViewById(R.id.description_complaints);
                status_complaints = (TextView) itemView.findViewById(R.id.status_complaints);
                raised_on = (TextView) itemView.findViewById(R.id.raised_on);
                //  btn_recharge = (Button) itemView.findViewById(R.id.btnRecharge);
            }
        }
    }
}
