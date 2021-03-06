package com.uriah.admin.worldvisioncable;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;


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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;


public class NewConnectionActivity extends AppCompatActivity {

    Button sendRequest, back;
    LinearLayout loading;
    ArrayList<String> services,city;
    Spinner selectService,selectCity;
    String sendservices,sendcity;
    EditText edt_fullname,edt_mobileno,edt_address,edt_message,edt_pincode,edt_emailid;

    HashMap<String, String> hm = new HashMap<String, String>();


    Pattern email_pattern = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_connection);

        loading = findViewById(R.id.loading);




        back = (Button) findViewById(R.id.backAction);
        selectService=(Spinner)findViewById(R.id.spn_chooseServices);
        selectCity=(Spinner)findViewById(R.id.spn_chooseCity);
        edt_fullname=(EditText)findViewById(R.id.newConnection_fullname);
        edt_pincode=(EditText)findViewById(R.id.newConnection_pincode);
        edt_emailid=(EditText)findViewById(R.id.newConnection_emailid);
        edt_address=(EditText)findViewById(R.id.newConnection_address);
        edt_mobileno=(EditText)findViewById(R.id.newConnection_mobileno);
        edt_message=(EditText)findViewById(R.id.newConnection_message);

        sendRequest=(Button)findViewById(R.id.sendRequest);


        services=new ArrayList<>();
        city=new ArrayList<>();

        sendservices="Select Services";
        sendcity="Select City";

        services.add("Select Services");
        services.add("Broadband");
        services.add("Cable TV");

        city.add("Select City");
        city.add("Bangalore");
        city.add("Others");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

       selectService.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,services));

       selectCity.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,city));

        selectService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sendservices=services.get(position);

                Log.d("Services",sendservices);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        selectCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sendcity=city.get(position);

                Log.d("City",sendcity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String Email = edt_emailid.getText().toString().trim();
                Matcher m = email_pattern.matcher(Email);

                if (edt_address.getText().toString().trim().equals("")) {
                    edt_address.setError("Enter Your Address");
                } if (edt_emailid.getText().toString().trim().equals("")) {
                    edt_emailid.setError("Enter EmailId");
                }
                if (!m.find()) {
                    edt_emailid.setError("Enter Valid Email");
                }
                if (edt_pincode.getText().toString().equals("")) {
                    edt_pincode.setError("Please Enter PinCode");
                } if (edt_message.getText().toString().trim().equals("")) {
                    edt_message.setError("Enter Your Message");
                } if (edt_fullname.getText().toString().trim().equals("")) {
                    edt_fullname.setError("Pleaser Enter Your Name");
                } if (edt_mobileno.getText().toString().trim().equals("")) {
                    edt_mobileno.setError("Pleaser Enter Your Mobile No.");
                } if (sendservices.equalsIgnoreCase("Select Services")) {
                    Toast.makeText(getApplicationContext(), "Please Select Services", Toast.LENGTH_LONG).show();
                } if (sendcity.equalsIgnoreCase("Select City")) {
                    Toast.makeText(getApplicationContext(), "Please Select City", Toast.LENGTH_LONG).show();
                }
                else {
                    if ((!edt_message.getText().toString().trim().equals(""))&&(!edt_fullname.getText().toString().trim().equals(""))&&
                            (!edt_mobileno.getText().toString().trim().equals(""))&&
                            (!edt_pincode.getText().toString().equals(""))&&(!edt_emailid.getText().toString().trim().equals(""))&&
                            (!edt_address.getText().toString().trim().equals(""))&&(!sendservices.equalsIgnoreCase("Select Services")) && (!sendcity.equalsIgnoreCase("Select City"))) {


                        String fullname = edt_fullname.getText().toString();
                        String email = edt_emailid.getText().toString();
                        String Mobile = edt_mobileno.getText().toString();
                        ;
                        String Pincode = edt_pincode.getText().toString();
                        String Address = edt_address.getText().toString();
                        String Message = edt_message.getText().toString();


                        new RegisterExecuteTask().execute(fullname, email, Mobile, sendservices, Address, sendcity, Pincode, Message);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Please Fill the Required Details", Toast.LENGTH_LONG).show();
                    }
                }

                /*else {



                }*/
            }
        });
        }


    class RegisterExecuteTask extends AsyncTask<String, Integer, String>
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

                       AlertDialog.Builder builder = new AlertDialog.Builder(NewConnectionActivity.this);
                        builder.setMessage("Your Request Sent, Provider will contact you soon.")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                        finish();
                                    }
                                });
                        AlertDialog alert = builder.create();
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
    /*HttpPost httpPost=new HttpPost("https://www.worldvisioncable.in/api/new_connection_api.php?");
            List<NameValuePair> list=new ArrayList<NameValuePair>();

            list.add(new BasicNameValuePair("name",valuse[0]));
            list.add(new BasicNameValuePair("email",valuse[1]));
            list.add(new BasicNameValuePair("phone",valuse[2]));
            list.add(new BasicNameValuePair("service_type",valuse[3]));
            list.add(new BasicNameValuePair("address",valuse[4]));
            list.add(new BasicNameValuePair("city",valuse[5]));
            list.add(new BasicNameValuePair("pincode",valuse[6]));
            list.add(new BasicNameValuePair("message",valuse[7]));*/


    public String newConnection(String[] valuse) {

        hm.put("name", valuse[0]);
        hm.put("email", valuse[1]);
        hm.put("phone", valuse[2]);
        hm.put("service_type", valuse[3]);
        hm.put("address", valuse[4]);
        hm.put("city", valuse[5]);
        hm.put("pincode", valuse[6]);
        hm.put("message", valuse[7]);
        String s = "";
        URL url;
        String response = "";
        try {
            url = new URL("https://www.worldvisioncable.in/api/new_connection_api.php?");

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
