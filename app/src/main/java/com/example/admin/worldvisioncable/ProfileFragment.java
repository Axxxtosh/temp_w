package com.example.admin.worldvisioncable;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.admin.worldvisioncable.Session.UserSessionManager;

import org.w3c.dom.Text;

import static android.content.Context.MODE_PRIVATE;
import static com.example.admin.worldvisioncable.Session.UserSessionManager.KEY_ADDRESS;
import static com.example.admin.worldvisioncable.Session.UserSessionManager.KEY_CARDNO;
import static com.example.admin.worldvisioncable.Session.UserSessionManager.KEY_CITYNAME;
import static com.example.admin.worldvisioncable.Session.UserSessionManager.KEY_COUNTRYNAME;
import static com.example.admin.worldvisioncable.Session.UserSessionManager.KEY_EMAIL;
import static com.example.admin.worldvisioncable.Session.UserSessionManager.KEY_ID;
import static com.example.admin.worldvisioncable.Session.UserSessionManager.KEY_MOBILE;
import static com.example.admin.worldvisioncable.Session.UserSessionManager.KEY_USERID;
import static com.example.admin.worldvisioncable.Session.UserSessionManager.KEY_USERNAME;



/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private String userName,userMobile,userCardNo,userAddress,userID,userMail;

    TextView userImage;
    private static final String PREFER_NAME = "UserSession";
    View v;
    Button btn_user_edit;
    LinearLayout logout;
    UserSessionManager sessionManager;




    public ProfileFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_profile, container, false);
        TextView tv_userName = (TextView) v.findViewById(R.id.user_name);
        TextView tv_userMail = (TextView) v.findViewById(R.id.user_mail);
        TextView tv_userMobile = (TextView) v.findViewById(R.id.user_mobile);
        TextView tv_userID = (TextView) v.findViewById(R.id.user_id);
        TextView tv_cardNo= (TextView) v.findViewById(R.id.user_card_no);
        TextView tv_userAddress = (TextView) v.findViewById(R.id.user_address);
        logout=(LinearLayout)v.findViewById(R.id.prof_logout);
        userImage=(TextView) v.findViewById(R.id.user_image);
        btn_user_edit=(Button)v.findViewById(R.id.btn_user_edit);

        sessionManager = new UserSessionManager(getActivity());

        btn_user_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),EditUserProfile.class);
                startActivity(i);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                sessionManager.logoutUser();
            }
        });

        //Getting values from Preference

        getUserPreference();
        String img=userName.substring(0,1);
        userImage.setText(userName.substring(0,1));
        tv_userName.setText(userName);
        tv_userMail.setText(userMail);
        tv_userMobile.setText(userMobile);
        tv_userID.setText(userID);
        tv_cardNo.setText(userCardNo);
        tv_userAddress.setText(userAddress);


        return v;






    }

    private void getUserPreference() {

        SharedPreferences prefs = getActivity().getSharedPreferences(PREFER_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        userName=prefs.getString(KEY_USERNAME,"Sample");
        userMail=prefs.getString(KEY_EMAIL,"");
        userCardNo=prefs.getString(KEY_CARDNO,"");
        userMobile=prefs.getString(KEY_MOBILE,"");
        userID=prefs.getString(KEY_USERID,"");
        String add=prefs.getString(KEY_ADDRESS,"");
        String[] data = add.split(",");

        userAddress=data[0]+data[1]+",\n"+data[2]+",\n"+prefs.getString(KEY_CITYNAME,"")+","+
        prefs.getString(KEY_COUNTRYNAME,"");
    }

}
