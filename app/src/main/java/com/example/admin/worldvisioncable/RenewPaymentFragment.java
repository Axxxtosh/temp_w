package com.example.admin.worldvisioncable;


import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.atom.mobilepaymentsdk.PayActivity;
import com.example.admin.worldvisioncable.Models.UsedObject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import dmax.dialog.SpotsDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class RenewPaymentFragment extends AppCompatActivity {
    Button paymentCallAction;
    String vVenderURL;
    private SpotsDialog dialog;
    View v;

    TextView username,accountid,billno;
    TextView renewpackname,renewvalidity,renewduedate;
    TextView previous_due,current_invoice,totalpayment;

    private static String TAG="Payment";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_renew_payment);

        username= findViewById(R.id.username);
        accountid= findViewById(R.id.accountid);
        billno= findViewById(R.id.billno);
        renewpackname= findViewById(R.id.renewplanname);
        renewvalidity= findViewById(R.id.renewvalidity);
        renewduedate= findViewById(R.id.renewDuedate);
        previous_due= findViewById(R.id.previous_due_amount);
        current_invoice= findViewById(R.id.current_due_amount);
        totalpayment= findViewById(R.id.total_due_amount);

        username.setText(UsedObject.getUserUID());
        accountid.setText(UsedObject.getUserUID());

        Toolbar toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Renew Plan");
        toolbar.setTitleTextColor(getResources().getColor(R.color.grey));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        renewpackname.setText("Plan Name :"+ UsedObject.getCurBBPlanName());
        renewvalidity.setText("Validity :"+ UsedObject.getCurBBPlanValidity());
        renewduedate.setText("Due Date: "+ UsedObject.getCurBBPlanDueDate());
        current_invoice.setText("Rs."+ UsedObject.getCurBBPlanPrice());
        totalpayment.setText("Rs."+ UsedObject.getCurBBPlanPrice());
        previous_due.setText("Rs. 0");

        long time= System.currentTimeMillis();
        billno.setText("WVC"+time);


        dialog = new SpotsDialog(this, R.style.Custom);

        dialog.getWindow().setBackgroundDrawableResource(
                R.color.transparent);

        paymentCallAction= findViewById(R.id.paymentCallAction);

        paymentCallAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                //launchactivity();

                new StartPayment().execute();


            }
        });
        //return v;
    }

   /* private void launchactivity() {
        String TotalPrice="51";
        long time= System.currentTimeMillis();
        String TaxId="WVC"+String.valueOf(time);
        String username= UsedObject.getUserName();
        String email= UsedObject.getUserEmail();
        String mobile= UsedObject.getUserMobile();
        String userid= "2635";

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String CurrDateTime = sdf.format(new Date()).toString();
        //
        String  URL="https://payment.atomtech.in/mobilesdk?login=14334&pass=WORLD@123&ttype=NBFundTransfer&prodid=WORLD&amt=51&txncurr=INR&txnscamt=0&ru=https://www.worldvisioncable.in/payment-success&clientcode=lisas00940&txnid="+TaxId+"&date="+CurrDateTime+"&udf1="+username+"&udf2="+email+"&udf3="+mobile+"&udf4=Bangalore&custacc="+userid+"&udf9=user_id:3119;package_type:1;package_id:11";

        Intent newPayIntent = new Intent(RenewPaymentFragment.this, PayActivity.class);
        newPayIntent.putExtra("merchantId", "14334");
        newPayIntent.putExtra("txnscamt", "0"); //Fixed. Must be 0
        newPayIntent.putExtra("loginid", "14334");
        newPayIntent.putExtra("password", "WORLD@123");
        newPayIntent.putExtra("prodid", "WORLD");
        newPayIntent.putExtra("txncurr", "INR"); //Fixed. Must be INR
        newPayIntent.putExtra("clientcode", "lisas00940");
        newPayIntent.putExtra("custacc", "123456789");
        newPayIntent.putExtra("channelid", "INT");
        newPayIntent.putExtra("amt", "100");//Should be 3 decimal number i.e 100.000
        newPayIntent.putExtra("txnid", "wv_123456789");
        newPayIntent.putExtra("date", "30/12/2015 18:31:00");//Should be in same format
        newPayIntent.putExtra("cardtype", "DC");// CC or DC ONLY (value should be same as commented)
        newPayIntent.putExtra("cardAssociate", "MASTER");// for VISA and MASTER. MAESTRO ONLY (value should be same as commented)
        newPayIntent.putExtra("surcharge", "NO");// Should be passed YES if surcharge is applicable else pass NO
//use below Production url only with Production "Library-MobilePaymentSDK", Located inside PROD folder
//newPayIntent.putExtra("ru","https://payment.atomtech.in/mobilesdk/param"); //ru FOR Production
//use below UAT url only with UAT "Library-MobilePaymentSDK", Located inside UAT folder
        newPayIntent.putExtra("ru",URL);
        startActivityForResult(newPayIntent, 1);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        dialog.dismiss();

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            if (data != null)
            {
                String message = data.getStringExtra("status");
                String[] resKey = data.getStringArrayExtra("responseKeyArray");
                String[] resValue = data.getStringArrayExtra("responseValueArray");
                if(resKey!=null && resValue!=null)
                {
                    for(int i=0; i<resKey.length; i++)
                        System.out.println(" "+i+" resKey : "+resKey[i]+" resValue : "+resValue[i]);
                }
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private class StartPayment extends AsyncTask<String, Void, String> {
        String Atom2Request;

        @Override
        protected String doInBackground(String... params) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String CurrDateTime = sdf.format(new Date()).toString();
           // vVenderURL = "https://paynetzuat.atomtech.in/paynetz/epi/fts?login=mt=1&14334&pass=WORLD@123&ttype=NBFundTransfer&prodid=WORLD&atxncurr=INR&txnscamt=0&clientcode=bGlzYXMwMDk0MA==&txnid=wv_123456789&date=27/11/2017&custacc=123456789&udf1=Customer&udf2=rajtufan@gmail.com&udf3=8485835654&udf4=pune&ru=http://example.webservice/response.aspx?";

           // String TotalPrice=UsedObject.getCurBBPlanPrice();
            String TotalPrice="51";
            long time= System.currentTimeMillis();
            String TaxId="WVC"+String.valueOf(time);
            String username= UsedObject.getUserName();
            String email= UsedObject.getUserEmail();
            String mobile= UsedObject.getUserMobile();
            String userid= UsedObject.getId();
            String packageid=UsedObject.getCurBBPlanId();

            Log.d(TAG+"userid",userid);


            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

            //vVenderURL = "https://payment.atomtech.in/paynetz/epi/fts?login=14334&pass=WORLD@123&ttype=NBFundTransfer&prodid=WORLD&amt="+TotalPrice+"&txncurr=INR&txnscamt=0&ru=https://www.worldvisioncable.in/payment-success&clientcode=lisas00940&txnid="+TaxId+"&date="+CurrDateTime+"&udf1="+username+"&udf2="+email+"&udf3="+mobile+"&udf4=Bangalore&custacc="+userid+"";

            //vVenderURL = "https://payment.atomtech.in/paynetz/epi/fts?login=14334&pass=WORLD@123&ttype=NBFundTransfer&prodid=WORLD&amt=51&txncurr=INR&txnscamt=0&ru=https://www.worldvisioncable.in/payment-success&clientcode=lisas00940&txnid="+TaxId+"&date="+CurrDateTime+"&udf1="+username+"&udf2="+email+"&udf3="+mobile+"&udf4=Bangalore&custacc="+userid+"&udf9={\"+user_id\":\"+3119\",\"package_type\":\"1\",\"package_id\":\"11\"}";
            vVenderURL="https://payment.atomtech.in/paynetz/epi/fts?login=14334&pass=WORLD@123&ttype=NBFundTransfer&prodid=WORLD&amt=51&txncurr=INR&txnscamt=0&ru=https://www.worldvisioncable.in/payment-success&clientcode=lisas00940&txnid="+TaxId+"&date="+CurrDateTime+"&udf1="+username+"&udf2="+email+"&udf3="+mobile+"&udf4=Bangalore&custacc="+userid+"&udf9=user_id:3119;package_type:1;package_id:"+packageid;


            Log.d("Vvendor URL", vVenderURL);


            //URL vVenderURL = new URL(params[0]);'

            XMLParser parser = new XMLParser();
            String xml = parser.getXmlFromUrl(vVenderURL);     // getting XML
            Document doc = parser.getDomElement(xml);         // getting DOM element
            NodeList nList = doc.getElementsByTagName("RESPONSE");
            String xmlURL = "";
            String xmlttype = "";
            String xmltoken="";
            String xmltxnStage="";
            String xmltempTxnId="";
            for (int tempN = 0; tempN < nList.getLength(); tempN++) {
                Node nNode = nList.item(tempN);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    System.out.println("URL : " + eElement.getElementsByTagName("url").item(0).getChildNodes().item(0).getNodeValue());
                    xmlURL = eElement.getElementsByTagName("url").item(0).getChildNodes().item(0).getNodeValue();

                    NodeList aList = eElement.getElementsByTagName("param");
                    String vParamName;
                    for (int atrCnt = 0; atrCnt < aList.getLength(); atrCnt++) {
                        vParamName = aList.item(atrCnt).getAttributes().getNamedItem("name").getNodeValue();
                        System.out.println("<br>paramName : : " + vParamName);

                        if (vParamName.equals("ttype")) {
                            xmlttype = aList.item(atrCnt).getChildNodes().item(0).getNodeValue();
                        } else if (vParamName.equals("tempTxnId")) {
                            xmltempTxnId = aList.item(atrCnt).getChildNodes().item(0).getNodeValue();
                        } else if (vParamName.equals("token")) {
                            xmltoken = aList.item(atrCnt).getChildNodes().item(0).getNodeValue();
                        } else if (vParamName.equals("txnStage")) {
                            xmltxnStage = aList.item(atrCnt).getChildNodes().item(0).getNodeValue();
                        }
                    }
                    Log.d("XML URL", xmlURL);
                    Log.d("XML TRANS TYPE", xmlttype);
                    Log.d("tempTxnId : ", xmltempTxnId);
                    Log.d("param :token     :", xmltoken);
                    Log.d("param :txnStage  : ", xmltxnStage);
                }
            }

            Atom2Request = xmlURL + "?ttype=" + xmlttype + "&tempTxnId=" + xmltempTxnId + "&token=" + xmltoken + "&txnStage=" + xmltxnStage;
            Atom2Request = Atom2Request.replace(" ", "%20");
            Log.d(TAG+"ATOM 2nd Request URl", Atom2Request);
            return Atom2Request;
        }

        @Override
        protected void onPostExecute(String result) {
            if (dialog != null) {
                dialog.dismiss();
                Intent intent = new Intent(RenewPaymentFragment.this, WebContent.class);
                intent.putExtra("Keyatomrequest", result);
                startActivityForResult(intent, 1);
            }

        }



        @Override
        protected void onPreExecute() {
            // dialog.setMessage("Processing Request...");
            // dialog.setIndeterminate(false);
            //dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 1){
        if(resultCode == RESULT_OK){

            Bundle extras = data.getExtras();
            String response=extras.getString("Result");

            Log.d("Payment",response);
            //Response From Payment gateway
            String[] parts = response.split(",");

            Log.d("Payment", Arrays.toString(parts));

           /* Intent intent = new Intent(RenewPaymentFragment.this,PaymentStatus.class);
            intent.putExtra("transaction_number",parts[0]);
            intent.putExtra("transaction_id",parts[1]);
            intent.putExtra("amount",parts[2]);
            intent.putExtra("date_time",parts[4]);
            intent.putExtra("package_name",parts[13]);

            startActivity(intent);
            finish();*/




            Toast.makeText(this, ""+data, Toast.LENGTH_SHORT).show();
        }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            }

            /*
            * Transaction number-44271173,
            * Transaction id-WVC1513769980709,
            * Amount 51.00,
            * WORLD,
            * Date Wed Dec 20 17:11:13 IST 2017,
            * 735430065179,
            * F,
            * ?+?M=?M=,
            * ATOM PG,
            * ,
            * 44271173,
            * 14334,
            * null,
            * user_id:3119;package_type:1;package_id:8,
            * DC,
            * 0.0,
            * 536799XXXXXX7757,
            * Prabhakar,
            * krishna@worldvisioncable.com,
            * 9900249945,
            * Bangalore,,
            *
            *
            *
            * */

    }}

}
