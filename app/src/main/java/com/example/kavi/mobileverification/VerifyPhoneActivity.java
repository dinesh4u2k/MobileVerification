package com.example.kavi.mobileverification;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.api.cache.http.HttpCachePolicy;
import com.apollographql.apollo.cache.http.ApolloHttpCache;
import com.apollographql.apollo.cache.http.DiskLruHttpCacheStore;
import com.apollographql.apollo.exception.ApolloException;

import java.io.File;

import javax.annotation.Nonnull;



import okhttp3.OkHttpClient;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.File;
import java.util.concurrent.TimeUnit;



public class VerifyPhoneActivity extends AppCompatActivity {


    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText editText;
    private TextView phnnumber, changeno;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyphone);

        mAuth = FirebaseAuth.getInstance();

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        editText = (EditText) findViewById(R.id.editTextCode);

        phnnumber = (TextView) findViewById(R.id.phnnumber);

        changeno = (TextView) findViewById(R.id.change_no);

        changeno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VerifyPhoneActivity.this,Login.class);
                startActivity(intent);
            }
        });

        String phonenumber = getIntent().getStringExtra("phonenumber");

        phnnumber.setText(phonenumber);



//        Log.d("info", phonenumber1 + " " + username);

        sendverificationcode(phonenumber);

        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = editText.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6){

                    editText.setError("Enter Code....");
                    editText.requestFocus();
                    return;
                }

                verifycode(code);

            }
        });
    }

    private void verifycode(String code){

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        SigninwithCredential(credential);

    }

    private void SigninwithCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

//                            new android.os.Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    callQuery();
//                                }
//                            },2000);
                            String phonenumber1 = getIntent().getStringExtra("phonenumber1");

                            String username = getIntent().getStringExtra("username");

                            Intent intent = new Intent(VerifyPhoneActivity.this,NavActivity.class);

                            intent.putExtra("phonenumber1", phonenumber1);
                            intent.putExtra("username", username);

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);

                        } else {
                            Toast.makeText(VerifyPhoneActivity.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

//    private void callQuery() {
//
//        final String phonenumber1 = getIntent().getStringExtra("phonenumber1");
//        final String username = getIntent().getStringExtra("username");
//        final Integer wallet =0;
//
//        setupclient();
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
//                .query(PersondetailsQuery.builder().build())
//                .httpCachePolicy(HttpCachePolicy.NETWORK_ONLY)
//                .enqueue(new ApolloCall.Callback<PersondetailsQuery.Data>() {
//                    @Override
//                    public void onResponse(@Nonnull Response<PersondetailsQuery.Data> response) {
//
//
//                        PersondetailsQuery.Data data = response.data();
//
//                        Listsize = response.data().person().size();
//
////                        pwallet = Integer.parseInt(data.person().get(0).wallet.toString());
////                        puserid = data.person.get(0).userid.toString();
//
//                        for (int i=0;i<=Listsize;i++) {
//                            pmobileno = data.person.get(i).mobileno.toString();
//
//                            if (pmobileno.equals(phonenumber1)){
//
//                                pwallet = Integer.parseInt(data.person().get(i).wallet.toString());
//
//                                VerifyPhoneActivity.this.runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(VerifyPhoneActivity.this, "equalllllllllllllllllllssssssssssss", Toast.LENGTH_LONG).show();
//
//
//                                    }
//                                });
//
//
//
//                            }else if(i==Listsize){
//                                postMutation(wallet,phonenumber1,username);
//
//                                VerifyPhoneActivity.this.runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(VerifyPhoneActivity.this, "MUUUTAAATIIIIOOONNNNNNN", Toast.LENGTH_LONG).show();
//
//
//                                    }
//                                });
//
//                                //Toast.makeText(VerifyPhoneActivity.this, "mutatiiiiiooooooooonnnnnnnnnnnnnnn", Toast.LENGTH_SHORT).show();
//                            }
//
//                        }
//
//
//
//
//
//                       // Log.d("datas", puserid + "" + pmobileno + "" + pwallet );
//
//
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
//    }




//    void postMutation(int pwallet2,String pmobileno2,String puserid2){
//
//        setupclient();
//        AddPersonMutation addPersonMutation = AddPersonMutation.builder()
//                .userid(puserid2)
//                .mobileno(pmobileno2)
//                .wallet(pwallet2)
//                .build();
//        ApolloCall<AddPersonMutation.Data> call = apolloClient.mutate(addPersonMutation);
//        call.enqueue(new ApolloCall.Callback<AddPersonMutation.Data>() {
//            @Override
//            public void onResponse(@Nonnull Response<AddPersonMutation.Data> response) {
//                AddPersonMutation.Data res = response.data();
//
//                VerifyPhoneActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplication(), "Shelter registered Successfully", Toast.LENGTH_SHORT).show();
//
//
//                    }
//                });
//            }
//            @Override
//            public void onFailure(@Nonnull ApolloException e) {
//                Log.e("Fail", "onFailure: ",e );
//            }
//        });
//    }


//    private void setupclient(){
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
//    }



    private void sendverificationcode(String number){

        progressBar.setVisibility(View.VISIBLE);


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mcallBack
        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mcallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();

            if (code != null){

                editText.setText(code);
                verifycode(code);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

        }
    };
}
