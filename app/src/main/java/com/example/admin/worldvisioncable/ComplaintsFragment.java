package com.example.admin.worldvisioncable;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admin.worldvisioncable.Models.UsedObject;

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

public class ComplaintsFragment extends Fragment {

    Button complaints_submit;
    private SpotsDialog loaddialog;
    ArrayList<String> natureofcomplaints;
    String sendcomplaints;
    Spinner selectcomplaint;
    EditText complaints_name,complaints_emailid,complaints_mobileno,complaints_technician,complaints_description;

    View v;
    public ComplaintsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_complaints, container, false);

        loaddialog=new SpotsDialog(getActivity());

        loaddialog.getWindow().setBackgroundDrawableResource(
                R.color.transparent);

        complaints_submit=(Button)v.findViewById(R.id.complaints_submit);
        selectcomplaint=(Spinner)v.findViewById(R.id.spn_natureofcomplaints);
        complaints_name=(EditText)v.findViewById(R.id.complaints_name);
        complaints_emailid=(EditText)v.findViewById(R.id.complaints_emailid);
        complaints_mobileno=(EditText)v.findViewById(R.id.complaints_mobileno);
        complaints_technician=(EditText)v.findViewById(R.id.complaints_technician);
        complaints_description=(EditText)v.findViewById(R.id.complaints_description);

        complaints_name.setText(UsedObject.getUserName());
        complaints_emailid.setText(UsedObject.getUserEmail());
        complaints_mobileno.setText(UsedObject.getUserMobile());

        natureofcomplaints=new ArrayList<>();

        sendcomplaints="Select Complaints";

        natureofcomplaints.add("Select Complaints");
        natureofcomplaints.add("Payment Issue");
        natureofcomplaints.add("Technical Issue");
        natureofcomplaints.add("Account Issue");
        natureofcomplaints.add("Sales Issue");
        natureofcomplaints.add("Not able to Use Internet");
        natureofcomplaints.add("Slow Internet Speed");
        natureofcomplaints.add("Router Configuration");
        natureofcomplaints.add("Online Payment Error");
        natureofcomplaints.add("Shifting Request");
        natureofcomplaints.add("Others");

        selectcomplaint.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,natureofcomplaints));

        selectcomplaint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sendcomplaints=natureofcomplaints.get(position);

                Log.d("Complaints",sendcomplaints);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        complaints_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (complaints_mobileno.getText().toString().trim().equals("")) {
                    complaints_mobileno.setError("Enter Your Mobile No");
                } if (complaints_description.getText().toString().trim().equals("")) {
                    complaints_description.setError("Enter Description");
                    if (sendcomplaints.equals("Select Complaints")) {
                        Toast.makeText(getActivity(), "Please Select Services", Toast.LENGTH_LONG).show();
                    }
                }
                else {

                    loaddialog.show();

                    String name=complaints_name.getText().toString();
                    String email=complaints_emailid.getText().toString();
                    String Mobile=complaints_mobileno.getText().toString();;
                    String technician=complaints_technician.getText().toString();
                    String description=complaints_description.getText().toString();

                    new RegisterComplaintsExecuteTask().execute(UsedObject.getId(), UsedObject.getUserName(), UsedObject.getUserEmail(),UsedObject.getUserMobile(),sendcomplaints,
                            technician,description);
                }
            }
        });

        return v;
    }


    class RegisterComplaintsExecuteTask extends AsyncTask<String, Integer, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loaddialog.show();
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
                JSONObject jsonObject = new JSONObject(result);
                String response = jsonObject.getString("response");

                switch (response) {

                    case "200" :

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Your Complaints Sent, Provider will contact you soon.")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                        NewHomeFragment homeFragment = new NewHomeFragment();
                                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                        // ft.addToBackStack(null);
                                        ft.setCustomAnimations(R.anim.enter_left, R.anim.exit_right, R.anim.enter_right, R.anim.exit_left);
                                        ft.replace(R.id.content_main, homeFragment, "HomeFragment");
                                        ft.commit();

                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        // Toast.makeText(getApplicationContext(), "Your Request Sent", Toast.LENGTH_LONG).show();
                        loaddialog.dismiss();


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
                        loaddialog.dismiss();
                        Toast.makeText(getActivity(), "Some Problem Occur with Server", Toast.LENGTH_LONG).show();
                        break;


                }

            } catch (JSONException e) {
                e.printStackTrace();
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
            HttpPost httpPost=new HttpPost("https://www.worldvisioncable.in/api/complaints_api.php");
            List<NameValuePair> list=new ArrayList<NameValuePair>();

            list.add(new BasicNameValuePair("userid",valuse[0]));
            list.add(new BasicNameValuePair("name",valuse[1]));
            list.add(new BasicNameValuePair("email",valuse[2]));
            list.add(new BasicNameValuePair("phone",valuse[3]));
            list.add(new BasicNameValuePair("nature_of_complaint",valuse[4]));
            list.add(new BasicNameValuePair("technician",valuse[5]));
            list.add(new BasicNameValuePair("description",valuse[6]));



            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=  httpClient.execute(httpPost);

            HttpEntity httpEntity=httpResponse.getEntity();
            s= readResponseGmailRegistraion(httpResponse);

        }
        catch(Exception exception)  {}
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




}
