package com.example.onlinecomplain;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public class activitySignIn extends AppCompatActivity {
    private final activitySignIn activity= activitySignIn.this;
    private TextInputLayout lyEmail;
    private NestedScrollView nestedScrollView;
    private TextInputLayout lyPass;
    private EditText elEmail;
    private EditText elPass;
    private AppCompatButton signin;
    private AppCompatTextView cAccount;
    private InputValidation inputValidation;
    private databaseFile databaseFile;
    private user user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        signin=(AppCompatButton)findViewById(R.id.login);
        cAccount=(AppCompatTextView)findViewById(R.id.create);
        elEmail=findViewById(R.id.leemail);
        elPass=findViewById(R.id.lepass);
        lyEmail=findViewById(R.id.lEmail);
        lyPass=findViewById(R.id.lpass);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        verifyFromSq();


                }


        });
        cAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), activitySignup.class));
            }
        });

initObject();
    }


    private void initObject(){
       inputValidation=new InputValidation(activity);
       databaseFile =new databaseFile(activity);
        user =new user();
    }


    private void verifyFromSq(){
        if(!inputValidation.isInputEditText(elEmail,lyEmail,getString(R.string.errormessageemail))){
            return;
        }
        if(!inputValidation.isInputEditTextEmail(elEmail,lyEmail,getString(R.string.errormessageemail))){
            return;
        }
        if(!inputValidation.isInputEditText(elPass,lyPass,getString(R.string.errormessagepass))){
            return;
        }
        if(databaseFile.checkUser(elEmail.getText().toString().trim(),elPass.getText().toString().trim())){
    Intent i=new Intent(activity,HomeActivity.class);
    i.putExtra("Email",elEmail.getText().toString().trim());
    emptyInputEditText();
    startActivity(i);
        } else{
            Snackbar.make(nestedScrollView,"Please Enter Valid Email And Password!",Snackbar.LENGTH_LONG).show();
        }
    }
    private void emptyInputEditText(){

        elEmail.setText(null);
        elPass.setText(null);


    }
}