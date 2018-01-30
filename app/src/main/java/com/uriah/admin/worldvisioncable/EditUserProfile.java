package com.uriah.admin.worldvisioncable;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.uriah.admin.worldvisioncable.Models.UsedObject;

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

import static com.uriah.admin.worldvisioncable.Session.UserSessionManager.KEY_ADDRESS;

public class EditUserProfile extends AppCompatActivity {
    EditText et_user_edit_name,et_user_edit_mobile,et_user_edit_email,et_user_edit_address;
    Button btn_user_edit_save,editProfile_changePassword;
    LinearLayout loading;
    android.support.v7.app.AlertDialog dialog_register;
    EditText currentPassword,newPassword,newConfirmPassword;
    Button ChangePassword,Cancel;
    private static final String PREFER_NAME = "UserSession";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        loading = findViewById(R.id.loading);




        et_user_edit_name=(EditText)findViewById(R.id.et_user_edit_name);
        et_user_edit_mobile=(EditText)findViewById(R.id.et_user_edit_mobile);
        et_user_edit_email=(EditText)findViewById(R.id.et_user_edit_email);
        et_user_edit_address=(EditText)findViewById(R.id.et_user_edit_address);
        btn_user_edit_save=(Button)findViewById(R.id.btn_user_edit_save);

        editProfile_changePassword=(Button)findViewById(R.id.editProfile_changePassword);
        editProfile_changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChangePasswordDialog();
            }
        });

        btn_user_edit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("ID", UsedObject.getId());
                Log.d("ID",et_user_edit_name.getText().toString());
                Log.d("ID",et_user_edit_email.getText().toString());
                Log.d("ID",et_user_edit_mobile.getText().toString());
                Log.d("ID",et_user_edit_address.getText().toString());

                new UpdateProfileAsynTask().execute(UsedObject.getId(),et_user_edit_name.getText().toString(),et_user_edit_email.getText().toString(),
                        et_user_edit_mobile.getText().toString(),et_user_edit_address.getText().toString());
            }
        });

        et_user_edit_name.setText(UsedObject.getUserName());
        et_user_edit_email.setText(UsedObject.getUserEmail());
        et_user_edit_mobile.setText(UsedObject.getUserMobile());

        SharedPreferences prefs = getSharedPreferences(PREFER_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String add=prefs.getString(KEY_ADDRESS,"");
        et_user_edit_address.setText(add);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Profile");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        final Drawable upArrow = this.getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    public void openChangePasswordDialog() {
        LayoutInflater inflater =this.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.change_password, null);

        currentPassword=(EditText)alertLayout.findViewById(R.id.edt_currentPassword);
        newPassword=(EditText)alertLayout.findViewById(R.id.edt_newPassword);
        newConfirmPassword=(EditText)alertLayout.findViewById(R.id.edt_confirmnewPassword);

        ChangePassword=(Button)alertLayout.findViewById(R.id.btn_change) ;
        ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid= UsedObject.getId();
                String currentPswd=currentPassword.getText().toString();
                String newPswd=newPassword.getText().toString();
                dialog_register.dismiss();
                new ChangePasswordAsync().execute(userid,currentPswd,newPswd);
            }
        });
        Cancel=(Button)alertLayout.findViewById(R.id.btn_cancel);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_register.dismiss();
            }
        });
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(EditUserProfile.this);
        builder.setView(alertLayout);
        builder.setCancelable(false);
        dialog_register = builder.create();

        dialog_register.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog_register.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog_register.show();

    }


    class ChangePasswordAsync extends AsyncTask<String, Integer, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String res=ChangePassword(params);


            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            //Toast.makeText(getApplicationContext(), "Result:"+result, Toast.LENGTH_SHORT).show();

            try {
                loading.setVisibility(View.GONE);
                JSONObject jsonObject = new JSONObject(result);
                String response = jsonObject.getString("response");

                switch (response) {

                    case "200" :


                        Toast.makeText(getApplicationContext(), "Password Changed Successfully", Toast.LENGTH_SHORT).show();

                        break;
                    case "202":
                        Toast.makeText(getApplicationContext(), "Password not matched with the given Email", Toast.LENGTH_LONG).show();
                        break;
                    case "204":
                        Toast.makeText(getApplicationContext(), "User is Inactive we sent a Activation  Email at your Registered Email", Toast.LENGTH_LONG).show();
                        break;
                    case "206":
                        Toast.makeText(getApplicationContext(), "Not Registered! Please Register First", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Some Problem Occur with Server", Toast.LENGTH_LONG).show();
                        break;


                }

            } catch (JSONException e) {
                e.printStackTrace();
                loading.setVisibility(View.GONE);


            }


            Log.d("LogCheck",result);
        }

    }


    public String ChangePassword(String[] valuse) {
        String s="";
        try
        {
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost("https://www.worldvisioncable.in/api/account/change_password_api.php");
            List<NameValuePair> list=new ArrayList<NameValuePair>();

            list.add(new BasicNameValuePair("userid",valuse[0]));
            list.add(new BasicNameValuePair("old_password",valuse[1]));
            list.add(new BasicNameValuePair("new_password",valuse[2]));



            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=  httpClient.execute(httpPost);

            HttpEntity httpEntity=httpResponse.getEntity();
            s= readResponseGmailRegistraion(httpResponse);

        } catch (Exception exception) {
            loading.setVisibility(View.GONE);
        }
        return s;


    }

    class UpdateProfileAsynTask extends AsyncTask<String, Integer, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);

        }
        @Override
        protected String doInBackground(String... params) {

            String res=newConnection(params);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            //Toast.makeText(getApplicationContext(), "Result:"+result, Toast.LENGTH_SHORT).show();

            try {
                loading.setVisibility(View.GONE);
                JSONObject jsonObject = new JSONObject(result);
                String response = jsonObject.getString("response");

                switch (response) {

                    case "200" :

                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(EditUserProfile.this);
                        builder.setMessage("Updated Successfully")
                            .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things

                                        /*ProfileFragment homeFragment = new ProfileFragment();
                                        FragmentTransaction ft =  getSupportFragmentManager().beginTransaction();
                                        // ft.addToBackStack(null);
                                        ft.setCustomAnimations(R.anim.enter_left, R.anim.exit_right, R.anim.enter_right, R.anim.exit_left);
                                        ft.replace(R.id.content_main, homeFragment, "HomeFragment");
                                        ft.commit();*/
                                        /*finish();
                                        Intent i = new Intent(EditUserProfile.this, HomePageActivity.class);
                                        startActivity(i);*/

                                    }
                                });
                        android.support.v7.app.AlertDialog alert = builder.create();
                        alert.show();
                        // Toast.makeText(getApplicationContext(), "Your Request Sent", Toast.LENGTH_LONG).show();


                        break;

                    default:
                       /* AlertDialog.Builder buildererr = new AlertDialog.Builder(getApplicationContext());
                        buildererr.setMessage("Some Problem Occur with Server, Please try again Later")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things

                                    }
                                });
                        AlertDialog alerterr = buildererr.create();
                        alerterr.show();
*/

                        Toast.makeText(EditUserProfile.this, "Some Problem Occur with Server", Toast.LENGTH_LONG).show();
                        break;


                }

            } catch (JSONException e) {
                e.printStackTrace();
                loading.setVisibility(View.GONE);

            }


            Log.d("LogCheck",result);

        }


    }

    public String newConnection(String[] valuse) {
        String s="";
        try
        {
            HttpClient httpClient=new DefaultHttpClient();
            // HttpPost httpPost=new HttpPost("http://www.42estate.in/api/register-api.php");
            //https://www.worldvisioncable.in/api/new_connection_api.php?userid=2635&name=Rajakumar A&email=rjkumar856@gmail.com&phone=9092310791&service_type=Cable&address=asdasdas&city=Bangalore&pincode=560100&message=Testing
            //
            HttpPost httpPost=new HttpPost("https://www.worldvisioncable.in/api/account/update_user_details.php");
            List<NameValuePair> list=new ArrayList<NameValuePair>();

            list.add(new BasicNameValuePair("userid",valuse[0]));
            list.add(new BasicNameValuePair("full_name",valuse[1]));
            list.add(new BasicNameValuePair("email",valuse[2]));
            list.add(new BasicNameValuePair("mobile",valuse[3]));
            list.add(new BasicNameValuePair("address",valuse[4]));



            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=  httpClient.execute(httpPost);

            HttpEntity httpEntity=httpResponse.getEntity();
            s= readResponseGmailRegistraion(httpResponse);

        } catch (Exception exception) {
            loading.setVisibility(View.GONE);

        }
        return s;


    }


    public String readResponseGmailRegistraion(HttpResponse res) {
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
            Intent i = new Intent(EditUserProfile.this, HomePageActivity.class);
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }
}
