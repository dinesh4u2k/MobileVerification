package com.example.kavi.mobileverification;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.io.File;

import javax.annotation.Nonnull;

import okhttp3.OkHttpClient;


public class MainActivity extends AppCompatActivity implements Home1.OnFragmentInteractionListener, Myaccount.OnFragmentInteractionListener {


    public ApolloClient apolloClient;



    Integer pwallet;

    boolean mpress = false;


    final Integer wallet =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);



        final String phonenumber1 = sp.getString("mobile",null);

        final String username = sp.getString("username",null);





        callQuery(phonenumber1,username);


        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Home"));
        tabLayout.addTab(tabLayout.newTab().setText("My Account"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplication(), "User registered Successfully", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(MainActivity.this, pmobileno2+pwallet2+pusername2, Toast.LENGTH_SHORT).show();

                    }
                });
            }
            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("Fail", "onFailure: ",e );
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        if (mpress) {
            super.onBackPressed();
            finishAffinity();
        }
        this.mpress=true;
        Toast.makeText(this, "Press back again to Exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mpress=false;

            }
        },2000);
    }
}
