package com.example.admin.worldvisioncable;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.worldvisioncable.Models.PaymentHistoryModel;
import com.example.admin.worldvisioncable.Models.UsedObject;

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

import dmax.dialog.SpotsDialog;


public class AccountStatement extends Fragment {

    View v;
    private SpotsDialog home_dialog;

    TextView txtLatestActivePlan,txtLatestBillGeneratedOn,txtLatestTransactionId,txtLatestPaymentType,txtLatestBillFor,txtDescription;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_account_statement, container, false);

        home_dialog = new SpotsDialog(getActivity(), R.style.Custom);
        home_dialog.getWindow().setBackgroundDrawableResource(
                R.color.transparent);
        home_dialog.show();

        txtLatestActivePlan=(TextView)v.findViewById(R.id.latestactivePlan);
        txtLatestBillGeneratedOn=(TextView)v.findViewById(R.id.latestbillgenerateOn);
        txtLatestTransactionId=(TextView)v.findViewById(R.id.latestTransactionId);
        txtLatestPaymentType=(TextView)v.findViewById(R.id.latestPaymentType);
        txtLatestBillFor=(TextView)v.findViewById(R.id.billFor);
        txtDescription=(TextView)v.findViewById(R.id.billDesc);

        new AccountStatementTask().execute(UsedObject.getId());

        return v;
    }

    public class AccountStatementTask extends AsyncTask<String,Void,String>
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
                Log.d("Bills Log",result);
                JSONArray arr=jsonObject.getJSONArray("items");


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
                    txtLatestBillFor.setText("Rs."+complaints.getString("Amount"));
                    txtDescription.setText(complaints.getString("desc"));



                home_dialog.dismiss();
            }
            catch(JSONException e){
                e.printStackTrace();
                home_dialog.dismiss();
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
            home_dialog.dismiss();

        }
    }

    public String activePlan(String[] valuse) {
        String s="";
        try
        {
            HttpClient httpClient=new DefaultHttpClient();
            // HttpPost httpPost=new HttpPost("http://www.42estate.in/api/register-api.php");

            HttpPost httpPost=new HttpPost("https://www.worldvisioncable.in/api/account/view_bills.php");
            List<NameValuePair> list=new ArrayList<NameValuePair>();

            list.add(new BasicNameValuePair("userid",valuse[0]));
            Log.d("userid",valuse[0]);


            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=  httpClient.execute(httpPost);

            HttpEntity httpEntity=httpResponse.getEntity();
            s= readResponseLogin(httpResponse);

        }
        catch(Exception exception)  {
            Log.d("activeplan","inactiveplan");
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
