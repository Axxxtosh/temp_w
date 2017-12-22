package com.example.admin.worldvisioncable;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import dmax.dialog.SpotsDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class EnquiryFragment extends Fragment {
    Button enquiry_submit;
    /*private SpotsDialog loaddialog;
    EditText enquiry_name,enquiry_emailid,enquiry_mobileno,enquiry_description;*/

    View v;
    public EnquiryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_enquiry, container, false);

        /*loaddialog=new SpotsDialog(getActivity());

        loaddialog.getWindow().setBackgroundDrawableResource(
                R.color.transparent);

        enquiry_submit=(Button)v.findViewById(R.id.enquiry_submit);
        enquiry_name=(EditText)v.findViewById(R.id.enquiry_name);
        enquiry_emailid=(EditText)v.findViewById(R.id.enquiry_emailid);
        enquiry_mobileno=(EditText)v.findViewById(R.id.enquiry_mobileno);
        enquiry_description=(EditText)v.findViewById(R.id.enquiry_description);

        enquiry_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (enquiry_mobileno.getText().toString().trim().equals("")) {
                    enquiry_mobileno.setError("Enter Your Mobile No");
                } if (enquiry_description.getText().toString().trim().equals("")) {
                    enquiry_description.setError("Enter Description");
                }
                else {

                    loaddialog.show();

                    String name=enquiry_name.getText().toString();
                    String email=enquiry_emailid.getText().toString();
                    String Mobile=enquiry_mobileno.getText().toString();;
                    String description=enquiry_description.getText().toString();

                }
            }
        });*/

        return v;
    }

}
