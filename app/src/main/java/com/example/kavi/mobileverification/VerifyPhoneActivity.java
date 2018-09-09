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

        sendverificationcode(phonenumber);
        findViewById(R.id.resend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = phnnumber.getText().toString().trim();
                resendVerificationCode(number);

            }
        });

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

                            String phonenumber1 = getIntent().getStringExtra("phonenumber1");

                           String username = getIntent().getStringExtra("username");

                            Intent intent = new Intent(VerifyPhoneActivity.this,MainActivity.class);

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
    private void resendVerificationCode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mcallBack         // OnVerificationStateChangedCallbacks
        );             // ForceResendingToken from callbacks
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
