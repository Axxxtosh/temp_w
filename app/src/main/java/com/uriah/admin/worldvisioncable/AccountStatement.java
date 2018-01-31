package com.uriah.admin.worldvisioncable;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uriah.admin.worldvisioncable.Models.PaymentHistoryModel;
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


public class AccountStatement extends Fragment {

    View v;

    CardView main;
    LinearLayout loading;
    HashMap<String, String> hm = new HashMap<String, String>();

    TextView txtLatestActivePlan,txtLatestBillGeneratedOn,txtLatestTransactionId,txtLatestPaymentType,txtLatestBillFor,txtDescription;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_account_statement, container, false);

        loading = v.findViewById(R.id.loading);



        main = (CardView) v.findViewById(R.id.main);
        txtLatestActivePlan=(TextView)v.findViewById(R.id.latestactivePlan);
        txtLatestBillGeneratedOn=(TextView)v.findViewById(R.id.latestbillgenerateOn);
        txtLatestTransactionId=(TextView)v.findViewById(R.id.latestTransactionId);
        txtLatestPaymentType=(TextView)v.findViewById(R.id.latestPaymentType);
        txtLatestBillFor=(TextView)v.findViewById(R.id.billFor);
        txtDescription=(TextView)v.findViewById(R.id.billDesc);
        main.setVisibility(View.GONE);

        new AccountStatementTask().execute(UsedObject.getId());

        return v;
    }

    public class AccountStatementTask extends AsyncTask<String,Void,String>
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


                Log.d("Bills Log", result);
                JSONArray arr = jsonObject.getJSONArray("items");

                    JSONObject complaints = arr.getJSONObject(0);
                    String id = complaints.getString("id");
                    PaymentHistoryModel data = new PaymentHistoryModel();
                    data.setPackageid(complaints.getString("id"));
                    data.setTransactionId(complaints.getString("transaction_id"));
                    data.setMmp_txn(complaints.getString("mmp_txn"));
                    data.setType(complaints.getString("Type"));
                    data.setAmount(complaints.getString("Amount"));
                    data.setPain_on(complaints.getString("Paid_on"));
                    data.setDesc(complaints.getString("desc"));

                    txtLatestActivePlan.setText(complaints.getString("Package_name"));
                    txtLatestBillGeneratedOn.setText(complaints.getString("Paid_on"));
                    txtLatestTransactionId.setText(complaints.getString("transaction_id"));
                    txtLatestPaymentType.setText(complaints.getString("Type"));
                txtLatestBillFor.setText("Rs." + complaints.getString("Amount"));
                    txtDescription.setText(complaints.getString("desc"));



                main.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }


            catch(JSONException e){
                e.printStackTrace();
                loading.setVisibility(View.GONE);

                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();

            }


        }

    }

    public String activePlan(String[] valuse) {
        String s="";
        //https://www.worldvisioncable.in/api/account/view_bills.php userid
        hm.put("userid", valuse[0]);


        URL url;
        String response = "";
        try {
            url = new URL("https://www.worldvisioncable.in/api/account/view_bills.php");

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
