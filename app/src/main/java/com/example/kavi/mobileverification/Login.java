package com.example.kavi.mobileverification;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    Spinner spinner;

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        spinner = (Spinner) findViewById(R.id.spinnercountries);
        editText = (EditText) findViewById(R.id.no);

        spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, CountryData.countryareacodes));


        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = CountryData.countryareacodes[spinner.getSelectedItemPosition()];

                String number = editText.getText().toString().trim();

                if (number.isEmpty() || number.length() <10){
                    editText .setError("Valid Number is Required");
                    editText.requestFocus();
                    return;
                }

                String Phonenumber = "+" + code + number;

                Intent intent = new Intent(Login.this, VerifyPhoneActivity.class);
                intent.putExtra("phonenumber", Phonenumber);
                startActivity(intent);


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null){

            Intent intent = new Intent(this,NavActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }
}
