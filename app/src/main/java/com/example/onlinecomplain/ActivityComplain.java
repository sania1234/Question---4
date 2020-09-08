package com.example.onlinecomplain;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static android.os.Environment.getExternalStoragePublicDirectory;


public class ActivityComplain extends Activity
        implements LocationListener {
    public double latitude;
    public double longitude;
    public LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;
EditText c,s,d;
    TextView tv,show,userD,tvv;
    String pathfile;
    Button ca,save,sqmaisave,showC;
    ImageView im,ok;
    private databaseFile databaseFile;
    private user user;
    Uri uri;
    FirebaseStorage storage;
    StorageReference storageReference;
    Firebase ur;
    String userkaemail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);

       storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Firebase.setAndroidContext(ActivityComplain.this);
        ur = new Firebase("https://memonapp-fc936.firebaseio.com/");
      userD=(TextView) findViewById(R.id.user);
        tvv=findViewById(R.id.loci);
        tv=findViewById(R.id.loc);
        ca=findViewById(R.id.done);
        im=findViewById(R.id.ivm);
        c = (EditText) findViewById(R.id.cate);
        s = (EditText) findViewById(R.id.sev);
        d = (EditText) findViewById(R.id.des);
        showC = (Button) findViewById(R.id.shawC);
        save = (Button) findViewById(R.id.savee);
        sqmaisave = (Button) findViewById(R.id.sq);
        userkaemail=getIntent().getStringExtra("E");
        userD.setText(userkaemail);
showC.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent i=new Intent(ActivityComplain.this, activityReport.class);
        startActivity(i);
    }
});

        if(Build.VERSION.SDK_INT >=23){
            requestPermissions(new String[] {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},2);

        }
        user = new user();
        databaseFile = new databaseFile(ActivityComplain.this);


        ca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchPic();


            }
        });
        getLocation();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
      final Bitmap bitmap = BitmapFactory.decodeFile(pathfile);
            im.setImageBitmap(bitmap);

            if (isLocationEnabled(ActivityComplain.this)) {
                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                criteria = new Criteria();
                bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                }
                Location location = locationManager.getLastKnownLocation(bestProvider);
                if (location != null) {
                    Log.e("TAG", "GPS is on");
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Toast.makeText(ActivityComplain.this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
                    tv.setText("LONGITUDE: " + longitude);
                    tvv.setText("LATITUDE: " + latitude);

                   save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                             uploadImage();
                           Firebase firebase=ur.child("User Email");
                           firebase.setValue(userkaemail);
                            firebase = ur.child("Category");
                            firebase.setValue(c.getText().toString().trim());
                            firebase = ur.child("Severity");
                            firebase.setValue(s.getText().toString().trim());
                            firebase = ur.child("Description");
                            firebase.setValue(d.getText().toString().trim());
                            firebase = ur.child("Lattitdue");
                            firebase.setValue(latitude);
                            firebase = ur.child("Longitude");
                            firebase.setValue(longitude);
                            Toast.makeText(ActivityComplain.this,"Data Send Successfuly To Server Firebase!",Toast.LENGTH_LONG).show();

                            /*Toast.makeText(JGPSCameraActivity.this, "Your Complain has been  Send Successfully to Server!", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(JGPSCameraActivity.this, ComplainShowActivity.class);
                            i.putExtra("cat", c.getText().toString().trim());
                            i.putExtra("sever", s.getText().toString().trim());
                            i.putExtra("description", d.getText().toString().trim());
                            String la = Double.toString(latitude);
                            String lo = Double.toString(longitude);
                            i.putExtra("lat", la);
                            i.putExtra("lon", lo);
                            startActivity(i);*/
                        }
                    });
                    sqmaisave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            user.setCat(c.getText().toString().trim());
                            user.setSev(s.getText().toString().trim());
                            user.setDes(d.getText().toString().trim());
                            user.setLat(latitude);
                            user.setLon(longitude);
                            user.setIm(String.valueOf(uri));
                            user.setUserEmail(userkaemail);
                            databaseFile.insertData(user);

                            Toast.makeText(ActivityComplain.this,"Your Complain has been Saved!",Toast.LENGTH_LONG).show();
                        }
                    });


                }
                else{
                    //This is what you need:
                    locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
                }
            }
            else
            {
                Toast.makeText(ActivityComplain.this, "Enable Location", Toast.LENGTH_SHORT).show();
            }





        } else if (resultCode == RESULT_CANCELED) {
            // user cancelled Image capture
            Toast.makeText(getApplicationContext(),
                    "Cancelled", Toast.LENGTH_SHORT)
                    .show();
        } else {
            // failed to capture image
            Toast.makeText(getApplicationContext(),
                    "Error!", Toast.LENGTH_SHORT)
                    .show();
        }
    }













   /* protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Bitmap bitmap=BitmapFactory.decodeFile(pathfile);
            im.setImageBitmap(bitmap);
        }
    }*/

    private void dispatchPic() {
        Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(i.resolveActivity(getPackageManager()) !=null){
            File f =null;
            f=createPhotoFile();
            if(f != null) {
                pathfile = f.getAbsolutePath();
                uri= FileProvider.getUriForFile(ActivityComplain.this,"com.example.onlinecomplain.fileprovider",f);
                i.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                startActivityForResult(i,1);
            }


        }
    }

    private File createPhotoFile() {
        String name=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageD=getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File img=null;
        try {
            img=File.createTempFile(name,".jpg",storageD);
        } catch (IOException e) {
            Log.d("mylog","Excep:" + e.toString());

        }
        return img;



    }
    public static boolean isLocationEnabled(Context context) {
        //...............
        return true;
    }

    public Boolean getLocation() {
        if (isLocationEnabled(ActivityComplain.this)) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

            //You can still do this if you like, you might get lucky:
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return true;
            }
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                Log.e("TAG", "GPS is on");
                latitude = location.getLatitude();
                longitude = location.getLongitude();
               // Toast.makeText(GPSActivity.this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();

            }
            else{
                //This is what you need:
                locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
            }
        }
        else
        {
            //prompt user to enable location....
            //.................
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);

    }

    @Override
    public void onLocationChanged(Location location) {
        //Hey, a non null location! Sweet!

        //remove location callback:
        locationManager.removeUpdates(this);

        //open the map:
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Toast.makeText(ActivityComplain.this, "Jaaaanlatitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
            tv.setText("Lon: " + longitude + "\n" + "Lattitude:" + latitude );
        }




    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void searchNearestPlace(String v2txt) {
        //.....
    }



    private void uploadImage() {

        if(uri!= null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(ActivityComplain.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ActivityComplain.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }
}
