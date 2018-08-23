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
    public ApolloClient apolloClient;
    Spinner spinner;

    EditText editText;

    TextView username;

    Integer pwallet;

    String Phonenumber1;

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

        username = findViewById(R.id.username);
        spinner = (Spinner) findViewById(R.id.spinnercountries);
        editText = (EditText) findViewById(R.id.no);

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
//            final String Phonenumber1 =  number;
            //callQuery(Phonenumber1,un);

            Intent intent = new Intent(this,NavActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }


//    void callQuery(final String mobno,final String user) {
//
////        final String phonenumber1 = getIntent().getStringExtra("phonenumber1");
////
////        final String username = getIntent().getStringExtra("username");
//
//        File file = new File(this.getCacheDir().toURI());
//        //Size in bytes of the cache
//        int size = 1024*1024;
//
//        //Create the http response cache store
//        DiskLruHttpCacheStore cacheStore = new DiskLruHttpCacheStore(file, size);
//
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .build();
//
//        apolloClient = ApolloClient.builder()
//                .serverUrl("https://digicashserver.herokuapp.com/graphql")
//                .httpCache(new ApolloHttpCache(cacheStore))
//                .okHttpClient(okHttpClient)
//                .build();
//
//
//
////        callQuery();
//
//        // setUpClient("https://digicashserver.herokuapp.com/graphql");
//
////        PersondetailsQuery persondetailsQuery = PersondetailsQuery.builder()
////                .build();
//
//
//        apolloClient
//                .query(PersondetailsQuery.builder().mobileno(mobno).build())
//                .httpCachePolicy(HttpCachePolicy.NETWORK_ONLY)
//                .enqueue(new ApolloCall.Callback<PersondetailsQuery.Data>() {
//                    @Override
//                    public void onResponse(@Nonnull Response<PersondetailsQuery.Data> response) {
//
//
//                        PersondetailsQuery.Data data = response.data();
//
//                        if(data!=null){
//                            Log.d("msg","kkkkkkkkkkkkkkasdddddddddddddddddddddddddddddd");
//                        }
//
//                        try {
//                            pwallet = Integer.parseInt(data.person().get(0).wallet.toString());
//
//                            Log.d("datas",Integer.toString(pwallet));
//
//
//
//                        }catch (Exception e){
//                            Log.d("catch", "errrrrrrrrrrrrrrrrrrrrrrrr");
////                            postMutation(wallet,mobno,user);
//
//                            // Log.d("pp",mobno +user+wallet);
//
//                        }
//
//
//                    }
//
//                    @Override
//                    public void onFailure(@Nonnull ApolloException e) {
//
//                        Log.e("Fail", "onFailure: ",e );
//
//                    }
//                });
//
//
//
//
//    }
}
