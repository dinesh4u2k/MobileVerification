package com.example.kavi.adkoin;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

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

//        addAutoStartup();

        for (Intent intent : AUTO_START_INTENTS){
            if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                startActivity(intent);
                break;
            }
        }

//        if (Build.MANUFACTURER.equalsIgnoreCase("oppo")) {
//            try {
//                Intent intent = new Intent();
//                intent.setClassName("com.coloros.safecenter",
//                        "com.coloros.safecenter.permission.startup.StartupAppListActivity");
//                startActivity(intent);
//            } catch (Exception e) {
//                try {
//                    Intent intent = new Intent();
//                    intent.setClassName("com.oppo.safe",
//                            "com.oppo.safe.permission.startup.StartupAppListActivity");
//                    startActivity(intent);
//
//                } catch (Exception ex) {
//                    try {
//                        Intent intent = new Intent();
//                        intent.setClassName("com.coloros.safecenter",
//                                "com.coloros.safecenter.startupapp.StartupAppListActivity");
//                        startActivity(intent);
//                    } catch (Exception exx) {
//
//                    }
//                }
//            }
//        }



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
        }

//        if (ContextCompat.checkSelfPermission(Login.this,
//                Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(Login.this,
//                    Manifest.permission.WAKE_LOCK)){
//                ActivityCompat.requestPermissions(Login.this, new String[]{
//                        Manifest.permission.WAKE_LOCK}, 1);
//
//            } else {
//
//                ActivityCompat.requestPermissions(Login.this, new String[]{
//                        Manifest.permission.WAKE_LOCK}, 1);
//
//            }
//        }


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

    private static final Intent[] AUTO_START_INTENTS = {
            new Intent().setComponent(new ComponentName("com.samsung.android.lool",
                    "com.samsung.android.sm.ui.battery.BatteryActivity")),
            new Intent("miui.intent.action.OP_AUTO_START").addCategory(Intent.CATEGORY_DEFAULT),
            new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
            new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
            new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.entry.FunctionActivity")).setData(
                    Uri.parse("mobilemanager://function/entry/AutoStart"))
    };

//    public void addAutoStartup() {
//
//        try {
//
//            Intent intent = new Intent();
//            String manufacturer = android.os.Build.MANUFACTURER;
//            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
//                Toast.makeText(this, "Enable autostart to earn money", Toast.LENGTH_LONG).show();
//                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
//            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
//                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
//            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
//                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
//            } else if ("Letv".equalsIgnoreCase(manufacturer)) {
//                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
//            } else if ("Honor".equalsIgnoreCase(manufacturer)) {
//                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
//            }
//
//            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//            if  (list.size() > 0) {
//                startActivity(intent);
//            }
//        } catch (Exception e) {
//            Log.e("exc" , String.valueOf(e));
//        }
//
//    }


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


            Intent intent = new Intent(this,MainActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }

}