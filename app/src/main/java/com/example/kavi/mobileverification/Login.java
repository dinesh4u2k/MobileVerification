package com.example.kavi.mobileverification;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.api.cache.http.HttpCachePolicy;
import com.apollographql.apollo.cache.http.ApolloHttpCache;
import com.apollographql.apollo.cache.http.DiskLruHttpCacheStore;
import com.apollographql.apollo.exception.ApolloException;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

import javax.annotation.Nonnull;

import okhttp3.OkHttpClient;

public class Login extends AppCompatActivity {

    Spinner spinner;

    EditText editText;

    EditText username;

    String Phonenumber1;
    CheckBox terms;

    String un;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        if (ContextCompat.checkSelfPermission(Login.this,
                android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Login.this,
                    android.Manifest.permission.READ_PHONE_STATE)){
                ActivityCompat.requestPermissions(Login.this, new String[]{
                        android.Manifest.permission.READ_PHONE_STATE}, 1);

            } else {

                ActivityCompat.requestPermissions(Login.this, new String[]{
                        android.Manifest.permission.READ_PHONE_STATE}, 1);

            }
        } else {

        }

        if (ContextCompat.checkSelfPermission(Login.this,
                Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Login.this,
                    Manifest.permission.WAKE_LOCK)){
                ActivityCompat.requestPermissions(Login.this, new String[]{
                        Manifest.permission.WAKE_LOCK}, 1);

            } else {

                ActivityCompat.requestPermissions(Login.this, new String[]{
                        Manifest.permission.WAKE_LOCK}, 1);

            }
        } else {

        }


        //username = findViewById(R.id.username);
        spinner = (Spinner) findViewById(R.id.spinnercountries);
        editText = (EditText) findViewById(R.id.no);
        terms = (CheckBox) findViewById(R.id.checkbox);
        username = findViewById(R.id.username);



        spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, CountryData.countryareacodes));


        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String code = CountryData.countryareacodes[spinner.getSelectedItemPosition()];

                String number = editText.getText().toString().trim();

                un = username.getText().toString().trim();

                if (number.isEmpty() || number.length() <10){
                    editText .setError("Valid Number is Required");
                    editText.requestFocus();
                    return;
                }
                if (!terms.isChecked()){
                    terms .setError("Agree to the Terms");
                    terms.requestFocus();
                    return;
                }
                if (un.isEmpty()){
                    editText .setError("Valid Name is Required");
                    editText.requestFocus();
                }


                String Phonenumber =  code + number;
                Phonenumber1 =  number;


                SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);

                final SharedPreferences.Editor editor = sp.edit();

                editor.putString("mobile",Phonenumber1);
                editor.putString("username",un);
                editor.apply();

                Intent intent = new Intent(Login.this, VerifyPhoneActivity.class);
                intent.putExtra("phonenumber", Phonenumber);
                intent.putExtra("phonenumber1", Phonenumber1);
                intent.putExtra("username",un);
                startActivity(intent);


            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(Login.this,
                            Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "permission Granted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "No permission Granted", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            ;

            Intent intent = new Intent(this,MainActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }

}
