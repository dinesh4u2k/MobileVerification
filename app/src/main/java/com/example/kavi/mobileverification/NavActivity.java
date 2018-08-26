package com.example.kavi.mobileverification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
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
import java.util.List;

import javax.annotation.Nonnull;

import okhttp3.OkHttpClient;

public class NavActivity extends AppCompatActivity
        implements Home.OnFragmentInteractionListener, Refer.OnFragmentInteractionListener, History.OnFragmentInteractionListener {

    private Button logoutbtn;
    public ApolloClient apolloClient;

    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;

    private Cashout cashfragment;

    private Banners banfragment;

    private Refer refer;
    private History history;
    private Home home;

    boolean mpressedonce = false;


    Integer pwallet;


    final Integer wallet =0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);



        final String phonenumber1 = sp.getString("mobile",null);

        final String username = sp.getString("username",null);





        callQuery(phonenumber1,username);


        logoutbtn = (Button) findViewById(R.id.logout);

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(NavActivity.this,Login.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        });

        mMainFrame = (FrameLayout) findViewById(R.id.main_frame);
        mMainNav = (BottomNavigationView) findViewById(R.id.navbot);

        cashfragment = new Cashout();
        // balfragment = new Balance();
        banfragment = new Banners();
        //   bottomsheet = new Bottomsheet();
        refer = new Refer();
        history = new History();
        home= new Home();

        setFragment(cashfragment);

        BottomNavigationhelper.removeShiftMode(mMainNav);


        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_home:
                        setFragment(home);
                        return true;

                    case R.id.nav_cash:
                        setFragment(cashfragment);
                        return true;

                    case R.id.nav_ban:
                        setFragment(banfragment);
                        return true;

                    case R.id.nav_refer:
                        //   setFragment(refer);
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = "Here is the share content body";
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));

                        return true;
                    case R.id.nav_history:
                        setFragment(history);
                        return  true;



                    default:return false;

                }

            }
        });

    }


    void callQuery(final String mobno,final String user) {


        File file = new File(this.getCacheDir().toURI());
        //Size in bytes of the cache
        int size = 1024*1024;

        //Create the http response cache store
        DiskLruHttpCacheStore cacheStore = new DiskLruHttpCacheStore(file, size);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        apolloClient = ApolloClient.builder()
                .serverUrl("https://digicashserver.herokuapp.com/graphql")
                .httpCache(new ApolloHttpCache(cacheStore))
                .okHttpClient(okHttpClient)
                .build();


        apolloClient
                .query(PersondetailsQuery.builder().mobileno(mobno).build())
                .httpCachePolicy(HttpCachePolicy.NETWORK_ONLY)
                .enqueue(new ApolloCall.Callback<PersondetailsQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<PersondetailsQuery.Data> response) {


                        PersondetailsQuery.Data data = response.data();

                        if(data!=null){
                            Log.d("msg","kkkkkkkkkkkkkkasdddddddddddddddddddddddddddddd");
                        }

                        try {
                            pwallet = Integer.parseInt(data.person().get(0).wallet.toString());

                            Log.d("datas",Integer.toString(pwallet));

                            SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);

                            final SharedPreferences.Editor editor = sp.edit();

                            editor.putString("mobile",mobno);
                            editor.apply();


                        }catch (Exception e){
                            Log.d("catch", "errrrrrrrrrrrrrrrrrrrrrrrr");
                            postMutation(wallet,mobno,user);

                            SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);

                            final SharedPreferences.Editor editor = sp.edit();

                            editor.putString("mobile",mobno);
                            editor.apply();


                        }


                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                        Log.e("Fail", "onFailure: ",e );

                    }
                });




    }


    void postMutation(final int pwallet2, final String pmobileno2, final String pusername2){
        File file = new File(this.getCacheDir().toURI());
        //Size in bytes of the cache
        int size = 1024*1024;

        //Create the http response cache store
        DiskLruHttpCacheStore cacheStore = new DiskLruHttpCacheStore(file, size);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        apolloClient = ApolloClient.builder()
                .serverUrl("https://digicashserver.herokuapp.com/graphql")
                .httpCache(new ApolloHttpCache(cacheStore))
                .okHttpClient(okHttpClient)
                .build();
        AddPersonMutation addPersonMutation = AddPersonMutation.builder()
                .username(pusername2)
                .mobileno(pmobileno2)
                .wallet(pwallet2)
                .build();
        ApolloCall<AddPersonMutation.Data> call = apolloClient.mutate(addPersonMutation);
        call.enqueue(new ApolloCall.Callback<AddPersonMutation.Data>() {
            @Override
            public void onResponse(@Nonnull Response<AddPersonMutation.Data> response) {
                AddPersonMutation.Data res = response.data();

                NavActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplication(), "User registered Successfully", Toast.LENGTH_SHORT).show();
                        Toast.makeText(NavActivity.this, pmobileno2+pwallet2+pusername2, Toast.LENGTH_SHORT).show();

                    }
                });
            }
            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("Fail", "onFailure: ",e );
            }
        });
    }




    private void setFragment(android.support.v4.app.Fragment fragment) {

        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {

        if (mpressedonce) {
            super.onBackPressed();
            finishAffinity();

        }
        this.mpressedonce = true;
        Toast.makeText(this,"Press Back again to exit",Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mpressedonce = false;
            }
        }, 2000);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
