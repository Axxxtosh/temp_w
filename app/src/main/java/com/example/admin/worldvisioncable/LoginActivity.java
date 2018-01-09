package com.example.admin.worldvisioncable;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.worldvisioncable.Models.UsedObject;
import com.example.admin.worldvisioncable.Session.UserSessionManager;

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
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin,btnLoginClose,btnForgetPassword;
    EditText edtUsername,edtPassword;
    UserSessionManager sessionManager;
    String userName,emailId,MobileNo,Address;
    String CardNo,CityName,StateName,CountryName,id,userid;
    private SpotsDialog loaddialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loaddialog=new SpotsDialog(this);

        loaddialog.getWindow().setBackgroundDrawableResource(
                R.color.transparent);

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

                    loaddialog.show();
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

        btnForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

    }
    class LoginAsync extends AsyncTask<String, Integer, String>
    {
        @Override
        protected String doInBackground(String... params) {

            String res=login(params);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            //Toast.makeText(getApplicationContext(), "Result:"+result, Toast.LENGTH_SHORT).show();

            try {
                JSONObject jsonObject = new JSONObject(result);
                String response = jsonObject.getString("response");
                Log.d("LogLogin",result);

                switch (response) {

                    case "200" :
                        Log.d("Response",response);

                        loaddialog.dismiss();
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
                        loaddialog.dismiss();
                        break;
                    case "204":
                        Toast.makeText(getApplicationContext(), "Invalid Data", Toast.LENGTH_LONG).show();
                        loaddialog.dismiss();
                        break;

                    default:
                        Toast.makeText(getApplicationContext(), "Some Problem Occur with Server", Toast.LENGTH_LONG).show();
                        loaddialog.dismiss();
                        break;

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String login(String[] valuse) {
            String s="";
            try
            {
                HttpClient httpClient=new DefaultHttpClient();
                // HttpPost httpPost=new HttpPost("http://www.42estate.in/api/register-api.php");

                HttpPost httpPost=new HttpPost("https://www.worldvisioncable.in/api/login_api.php");
                List<NameValuePair> list=new ArrayList<NameValuePair>();

                list.add(new BasicNameValuePair("username",valuse[0]));
                list.add(new BasicNameValuePair("password",valuse[1]));

                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse=  httpClient.execute(httpPost);

                HttpEntity httpEntity=httpResponse.getEntity();
                s= readResponseLogin(httpResponse);

            }
            catch(Exception exception)  {}
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
