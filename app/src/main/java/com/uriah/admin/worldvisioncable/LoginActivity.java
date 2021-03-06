package com.uriah.admin.worldvisioncable;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.uriah.admin.worldvisioncable.Models.UsedObject;
import com.uriah.admin.worldvisioncable.Session.UserSessionManager;

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


public class LoginActivity extends AppCompatActivity {

    Button btnLogin,btnLoginClose,btnForgetPassword;
    EditText edtUsername,edtPassword;
    UserSessionManager sessionManager;
    String userName,emailId,MobileNo,Address;
    String CardNo,CityName,StateName,CountryName,id,userid;
    LinearLayout loading;

    HashMap<String, String> hm = new HashMap<String, String>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loading = findViewById(R.id.loading);


        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnLoginClose=(Button)findViewById(R.id.closeLogin);
        btnForgetPassword=(Button)findViewById(R.id.forgetPassword);

        sessionManager = new UserSessionManager(LoginActivity.this);


        edtUsername=(EditText)findViewById(R.id.edt_username);
        edtPassword=(EditText)findViewById(R.id.edt_password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("LogLogin", "Login");
                if (edtUsername.getText().toString().trim().equals("") && edtPassword.getText().toString().trim().equals("")) {
                    edtUsername.setError("Please Enter UserName");
                    edtPassword.setError("Please Enter Password");
                } else if (edtUsername.getText().toString().trim().equals("")) {
                    edtUsername.setError("Please Enter UserName");
                } else if (edtPassword.getText().toString().trim().equals("")) {
                    edtPassword.setError("Please Enter UserName");
                } else {
                    //login();
                    String username = edtUsername.getText().toString();
                    String password = edtPassword.getText().toString();
                    MainActivity.fa.finish();


                    //  Toast.makeText(getApplicationContext(),"Clicked correct",Toast.LENGTH_SHORT).show();
                    new LoginAsync().execute(username, password);

                }
            }
        });

        btnLoginClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }
    class LoginAsync extends AsyncTask<String, Integer, String>
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
                Log.d("LogLogin",result);

                switch (response) {

                    case "200" :
                        Log.d("Response",response);


                        userName = jsonObject.getString("full_name");
                        emailId = jsonObject.getString("email");
                        MobileNo = jsonObject.getString("phone");
                        CardNo = jsonObject.getString("card_no");
                        Address = jsonObject.getString("address_1");
                        CityName = jsonObject.getString("city_name");
                        StateName = jsonObject.getString("state_name");
                        CountryName = jsonObject.getString("country_name");
                        id = jsonObject.getString("id");
                        userid = jsonObject.getString("user_id");



                        Log.d("ID:","t"+id);
                        sessionManager.createUserLoginSession(userName,edtPassword.getText().toString() , emailId, MobileNo,
                                userid,Address,CardNo,CityName,StateName,CountryName,id);

                        UsedObject.setUserName(userName);
                        UsedObject.setUserMobile(MobileNo);
                        UsedObject.setUserEmail(emailId);
                        UsedObject.setUserUID(userid);
                        UsedObject.setCardNo(CardNo);
                        UsedObject.setAddress(Address);
                        UsedObject.setCityName(CityName);
                        UsedObject.setCountryName(CountryName);
                        UsedObject.setStateName(StateName);
                        UsedObject.setId(id);



                        Log.d("GEt User id","D"+UsedObject.getId());

                        Intent i=new Intent(LoginActivity.this,HomePageActivity.class);
                        startActivity(i);
                        finish();


                        break;
                    case "202":
                        Toast.makeText(getApplicationContext(), "Password not matched with the given Email", Toast.LENGTH_LONG).show();

                        break;
                    case "204":
                        Toast.makeText(getApplicationContext(), "Invalid Data", Toast.LENGTH_LONG).show();

                        break;

                    default:
                        Toast.makeText(getApplicationContext(), "Some Problem Occur with Server", Toast.LENGTH_LONG).show();

                        break;

                }

            } catch (JSONException e) {
                e.printStackTrace();
                loading.setVisibility(View.GONE);
            }
        }

        public String login(String[] valuse) {

            hm.put("username", valuse[0]);
            hm.put("password", valuse[1]);
            String s="";
            URL url;
            String response = "";
            try {
                url = new URL("https://www.worldvisioncable.in/api/login_api.php");

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
