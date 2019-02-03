package com.example.kavi.adkoin;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.api.cache.http.HttpCachePolicy;
import com.apollographql.apollo.cache.http.ApolloHttpCache;
import com.apollographql.apollo.cache.http.DiskLruHttpCacheStore;
import com.apollographql.apollo.exception.ApolloException;
import com.viewpagerindicator.CirclePageIndicator;


import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Nonnull;

import okhttp3.OkHttpClient;


public class Home1 extends Fragment {
//    int images[]={
//            R.drawable.im1,
//    R.drawable.im2,
//    R.drawable.im3,
//    R.drawable.im4,
//    R.drawable.im5,
//
//};

    private int Listsize=0;

    private  ViewPager mPager;

    private CirclePageIndicator indicator;

    private TextView amount;

    public ApolloClient apolloClient;

    public String pwallet;
    public String pcount;

    public TextView callcount;

    public Integer callc;
    public String dd;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    private OnFragmentInteractionListener mListener;


    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    public final int wallet=0;
    private ArrayList<ImageModel> imageModelArrayList;

    private int[] myImageList = new int[70];

    public Home1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home1 newInstance(String param1, String param2) {
        Home1 fragment = new Home1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }





    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //oncreate
        // Inflate the layout for this fragment


        SharedPreferences sp = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);

        String mobile = sp.getString("mobile", null);

        View rootView = inflater.inflate(R.layout.fragment_home1, container, false);
        callcount = rootView.findViewById(R.id.call);
        amount = rootView.findViewById(R.id.cash_amo);






        //image slider code
//        imageModelArrayList = new ArrayList<>();
//        imageModelArrayList = populateList();

        init();

//        SharedPreferences spbroad = getActivity().getSharedPreferences("cc", Context.MODE_PRIVATE);
//
//        callc = spbroad.getInt("count", 0);

        dd = "0" + "/30";

        callcount.setText(dd);

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                //start your activity here
                callQuery1();
            }

        }, 3000);



        callQuery();
        return rootView;


    }




    void callQuery1()
    {

        SharedPreferences sp = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String mobile = sp.getString("mobile", null);

        Log.d("testtt",mobile);
        File file = new File(getActivity().getCacheDir().toURI());
        //Size in bytes of the cache
        int size = 1024 * 1024;

        //Create the http response cache store
        DiskLruHttpCacheStore cacheStore = new DiskLruHttpCacheStore(file, size);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        apolloClient = ApolloClient.builder()
                .serverUrl("https://adkoin-server.herokuapp.com/graphql")
                .httpCache(new ApolloHttpCache(cacheStore))
                .okHttpClient(okHttpClient)
                .build();


        apolloClient
                .query(PersondetailsQuery.builder().mobileno(mobile).build())
                .httpCachePolicy(HttpCachePolicy.NETWORK_FIRST)
                .enqueue(new ApolloCall.Callback<PersondetailsQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<PersondetailsQuery.Data> response) {


                        PersondetailsQuery.Data data = response.data();

                        if (data != null) {
                            Log.d("msg", "cash out");

                        }


                        if (data.person != null && data.person.get(0).wallet != null) {
                            pwallet = data.person.get(0).wallet.toString();
                            pcount = data.person.get(0).count.toString();
                        }

                        Log.d("datas", pwallet);
                        amount.post(new Runnable() {
                            @Override
                            public void run() {

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        amount.setText(pwallet);

                                    }
                                });

                            }
                        });

                        callcount.post(new Runnable() {
                            @Override
                            public void run() {

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        callcount.setText(pcount);

                                    }
                                });

                            }
                        });



                    }


                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                        Log.e("Fail", "onFailure: ", e);

                    }
                });

    }


    void callQuery()
    {


        File file = new File(getActivity().getCacheDir().toURI());
        //Size in bytes of the cache
        int size = 1024 * 1024;

        //Create the http response cache store
        DiskLruHttpCacheStore cacheStore = new DiskLruHttpCacheStore(file, size);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        apolloClient = ApolloClient.builder()
                .serverUrl("https://adkoin-server.herokuapp.com/graphql")
                .httpCache(new ApolloHttpCache(cacheStore))
                .okHttpClient(okHttpClient)
                .build();


        apolloClient
                .query(ImgurlQuery.builder().build())
                .httpCachePolicy(HttpCachePolicy.NETWORK_FIRST)
                .enqueue(new ApolloCall.Callback<ImgurlQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<ImgurlQuery.Data> response) {
                        final ArrayList<String> imagesFromURL = new ArrayList<String>();

//                        final String[] urll;

//                        SharedPreferences imgtopopup = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
//
//                        final SharedPreferences.Editor editorpop = imgtopopup.edit();
                        SharedPreferences imgtopopup = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);

                        final SharedPreferences.Editor editorpop = imgtopopup.edit();

                        ImgurlQuery.Data data = response.data();
                        Listsize = response.data().banner().size();
                        editorpop.putInt("listsize",Listsize);

                        for (int i = 0; i < Listsize; i++) {
//                            String var = String.valueOf(i);
//                            editorpop.putString(var,data.banner.get(i).imageurl.toString());
//                            urlll[i] = data.banner.get(i).imageurl.toString();
                            imagesFromURL.add(data.banner.get(i).imageurl.toString());
                            editorpop.putString("s"+i,imagesFromURL.get(i));
                            editorpop.apply();
                            Log.d("datas111", imagesFromURL.toString());

                        }


                        Home1.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {

//                                    Toast.makeText(getContext(), send, Toast.LENGTH_LONG).show();

                                    //mPager = new ViewPager(getActivity(), (AttributeSet) imagesFromURL);
                                    mPager = getActivity().findViewById(R.id.pager1);
                                    mPager.setAdapter(new SlidingImage_Adapter(getActivity(), imagesFromURL));
                                    //      mPager.setAdapter(new SlidingImage_Adapter(getContext(),imageModelArrayList));
                                    indicator = getActivity().findViewById(R.id.indicator);
                                    indicator.setViewPager(mPager);
                                    final float density = getResources().getDisplayMetrics().density;

                                    //Set circle indicator radius
                                    indicator.setRadius(5 * density);

                                    NUM_PAGES = imageModelArrayList.size();

                                    // Auto start of viewpager
                                    final Handler handler = new Handler();
                                    final Runnable Update = new Runnable() {
                                        public void run() {
                                            if (currentPage == NUM_PAGES) {
                                                currentPage = 0;
                                            }
                                            mPager.setCurrentItem(currentPage++, true);
                                        }
                                    };
                                    Timer swipeTimer = new Timer();
                                    swipeTimer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            handler.post(Update);
                                        }
                                    }, 3000, 3000);

                                    // Pager listener over indicator
                                    indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                        //
                                        @Override
                                        public void onPageSelected(int position) {
                                            currentPage = position;

                                        }

                                        @Override
                                        public void onPageScrolled(int pos, float arg1, int arg2) {

                                        }

                                        @Override
                                        public void onPageScrollStateChanged(int pos) {

                                        }
                                    });



                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        });


                    }


                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                        Log.e("Fail", "onFailure: ", e);

                    }
                });
    }



//    private ArrayList<ImageModel> populateList(){
//
//        ArrayList<ImageModel> list = new ArrayList<>();
//
//        for(int i = 0; i < 6; i++){
//            ImageModel imageModel = new ImageModel();
//            imageModel.setImage_drawable(myImageList[i]);
//            list.add(imageModel);
//        }
//
//        return list;
//    }

    private void init() {



    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

