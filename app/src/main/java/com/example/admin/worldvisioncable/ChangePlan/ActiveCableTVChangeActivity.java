package com.example.admin.worldvisioncable.ChangePlan;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.worldvisioncable.R;

import java.util.ArrayList;
import java.util.List;

public class ActiveCableTVChangeActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_cable_tvchange);
        //Cards
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_changeCablePlan);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<String>names=new ArrayList<>();
        names.add("Prime");
        names.add("Royal");

        adapter = new ActiveCableTVChangeActivity.Adapter(names,this);
        recyclerView.setAdapter(adapter);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change Broadband Plans");
        toolbar.setTitleTextColor(getResources().getColor(R.color.grey));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
    public class Adapter extends RecyclerView.Adapter<ActiveCableTVChangeActivity.Adapter.ViewHolder> {

        private Context context;
        private List<String>names;

        public Adapter(List<String>names,Context context) {
            super();
            this.names=names;
            this.context = context;
        }

        @Override
        public ActiveCableTVChangeActivity.Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cable_tv_plan_change_item_row, parent, false);
            ActiveCableTVChangeActivity.Adapter.ViewHolder viewHolder = new ActiveCableTVChangeActivity.Adapter.ViewHolder(v);

            return viewHolder;
        }


        @Override
        public void onBindViewHolder(ActiveCableTVChangeActivity.Adapter.ViewHolder holder, final int position) {

            String name=
            names.get(position);
            if(name.equals("Royal")){
                holder.imageView.setVisibility(View.INVISIBLE);}
            holder.txtPlanname.setText(name);


        }


        @Override
        public int getItemCount() {
            return names.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public TextView txtPlanname;
            public ImageView  imageView;
            public View view;



            public ViewHolder(View itemView) {
                super(itemView);

                txtPlanname = (TextView) itemView.findViewById(R.id.planname);
                imageView=(ImageView)itemView.findViewById(R.id.check);
                view = itemView;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {

                        //Dialog for cable plans
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(context);
                        }
                        builder.setTitle("To Change Plan")
                                .setMessage("To change existing plan, Please call us at 080-25534744 or Mail us at info@worldvision.com")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                    }
                                })

                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();



                        //

                    }
                });



            }
        }
    }
}
