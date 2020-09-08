package com.example.onlinecomplain;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class HomeActivity extends AppCompatActivity {
TextView st,eid;
AppCompatButton bt,n;
    static String emailFromIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        st=findViewById(R.id.ts);
        eid=findViewById(R.id.te);
        bt=findViewById(R.id.com);
      n=findViewById(R.id.h);
    emailFromIntent=getIntent().getStringExtra("Email");
        eid.setText(emailFromIntent);



       bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(HomeActivity.this, ActivityComplain.class);
              i.putExtra("E",emailFromIntent);
                startActivity(i);
            }
        });
        n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(HomeActivity.this, activityReport.class);
                startActivity(i);
            }
        });



    }
}