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

import android.widget.LinearLayout;
import android.widget.TextView;


import com.android.volley.toolbox.ImageLoader;


import com.uriah.admin.worldvisioncable.Models.PaymentHistoryModel;
import com.uriah.admin.worldvisioncable.Models.UsedObject;

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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;




/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentHistory extends Fragment {

    List<PaymentHistoryModel> paymentHistoryList;

    String id,image;

    LinearLayout loading;

    View v;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter1;
    private RecyclerView recyclerView;



    public PaymentHistory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_payment_history, container, false);
        paymentHistoryList = new ArrayList<>();
        loading = v.findViewById(R.id.loading);


        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_Paymenthistory);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        adapter1 = new Adapter(paymentHistoryList,getContext());

        new ViewBillsAsync().execute(UsedObject.getId());
        recyclerView.setAdapter(adapter1);

        return v;
    }

    public class ViewBillsAsync extends AsyncTask<String, Integer, String>
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
                Log.d("Bills Log",result);
                JSONArray arr=jsonObject.getJSONArray("items");

                for(int i=0;i<arr.length();i++)
                {
                    JSONObject complaints=arr.getJSONObject(i);
                    String id=complaints.getString("id");
                    PaymentHistoryModel data = new PaymentHistoryModel();
                    data.setPackageid(complaints.getString("id"));
                    data.setTransactionId(complaints.getString("transaction_id"));
                    data.setMmp_txn(complaints.getString("mmp_txn"));
                    data.setType(complaints.getString("Type"));
                    data.setAmount(complaints.getString("Amount"));
                    data.setPain_on(complaints.getString("Paid_on"));
                    data.setDesc(complaints.getString("desc"));


                    paymentHistoryList.add(data);

                    Log.d("ID","S"+id);
                }



                adapter1.notifyDataSetChanged();


            } catch (JSONException e) {
                loading.setVisibility(View.GONE);
                e.printStackTrace();
            }
        }

        public String login(String[] valuse) {
            String s="";
            try
            {
                HttpClient httpClient=new DefaultHttpClient();
                // HttpPost httpPost=new HttpPost("http://www.42estate.in/api/register-api.php");

                HttpPost httpPost=new HttpPost("https://www.worldvisioncable.in/api/account/view_bills.php");
                List<NameValuePair> list=new ArrayList<NameValuePair>();

                list.add(new BasicNameValuePair("userid",valuse[0]));


                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse=  httpClient.execute(httpPost);

                HttpEntity httpEntity=httpResponse.getEntity();
                s= readResponseLogin(httpResponse);

            } catch (Exception exception) {
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


    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private ImageLoader imageLoader;
        private Context context;
        PaymentHistory sub;

        //List of superHeroes
        List<PaymentHistoryModel> model;

        public Adapter(List<PaymentHistoryModel> model, Context context) {
            super();
            //Getting all the superheroes
            this.model = model;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.paymenthistory_row, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            sub = new PaymentHistory();
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            final PaymentHistoryModel paymentHistoryModel = model.get(position);
            holder.bill_number.setText(paymentHistoryModel.getTransactionId());
            holder.amt_type.setText(paymentHistoryModel.getType());
            holder.amount_paid.setText(paymentHistoryModel.getAmount());
            holder.paid_date.setText(paymentHistoryModel.getPain_on());

        }

        @Override
        public int getItemCount() {
            return model.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public TextView bill_number,amt_type,amount_paid,paid_date;


            public ViewHolder(View itemView) {
                super(itemView);

                bill_number = (TextView) itemView.findViewById(R.id.bill_number);
                amt_type = (TextView) itemView.findViewById(R.id.amt_type);
                amount_paid = (TextView) itemView.findViewById(R.id.amount_paid);
                paid_date = (TextView) itemView.findViewById(R.id.paid_date);

            }
        }
    }

}