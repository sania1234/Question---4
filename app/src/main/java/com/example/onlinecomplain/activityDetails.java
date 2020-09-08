package com.example.onlinecomplain;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class activityDetails extends AppCompatActivity {
    databaseFile helpher;
    List<user> dbList;
    int position;
    TextView umail,cate,sevir,desir,lat,lon;
    ImageView im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);

        getSupportActionBar().hide();


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        // 5. get status value from bundle
        position = bundle.getInt("position");
         im=(ImageView) findViewById(R.id.cimage);
        umail=(TextView)findViewById(R.id.usermail);
        cate =(TextView)findViewById(R.id.categori);
        sevir =(TextView)findViewById(R.id.sevi);
        desir=(TextView)findViewById(R.id.desi);
        lat =(TextView)findViewById(R.id.longg);
        lon =(TextView)findViewById(R.id.lattitud);
        helpher = new databaseFile(this);
        dbList= new ArrayList<user>();
        dbList = helpher.getDataFromDB();

        if(dbList.size()>0){
            String userem= dbList.get(position).getUserEmail();
            String cati=dbList.get(position).getCat();
            String sevi=dbList.get(position).getSev();
            String desi=dbList.get(position).getDes();
            Double lati=dbList.get(position).getLat();
            Double lonn=dbList.get(position).getLon();
           String imag=(dbList.get(position).getIm());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(imag));
                im.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            umail.setText(userem);
            cate.setText(cati);
            sevir.setText(sevi);
            desir.setText(desi);
            lat.setText(String.valueOf(lati));
            lon.setText(String.valueOf(lonn));



        }

        Toast.makeText(activityDetails.this, dbList.toString(), Toast.LENGTH_LONG);
    }


   /* public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }*/



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
