package com.example.kavi.mobileverification;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Myaccount.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Myaccount#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Myaccount extends Fragment implements Refer.OnFragmentInteractionListener, History.OnFragmentInteractionListener {

    private TextView amount;

    private LinearLayout his;

    private LinearLayout shar;

    private LinearLayout log;

    public String mobile;

    private Refer refer;
    private History history1;

    public ApolloClient apolloClient;

    public String pwallet;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Myaccount() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Myaccount.
     */
    // TODO: Rename and change types and number of parameters
    public static Myaccount newInstance(String param1, String param2) {
        Myaccount fragment = new Myaccount();
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
        // Inflate the layout for this fragment


        View rootView=inflater.inflate(R.layout.fragment_myaccount, container, false);

        amount = rootView.findViewById(R.id.cash);

        his = rootView.findViewById(R.id.history);
        shar = rootView.findViewById(R.id.share);
        log = rootView.findViewById(R.id.logout);

        new AccountAsync().execute();



        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getContext(),Login.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        });

        refer = new Refer();
        history1 = new History();

        shar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Here is the share content body";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));


            }
        });



        return rootView;
    }

    private class AccountAsync extends AsyncTask<String,Integer,String>{

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            this.progressDialog.setTitle("Processing");
            this.progressDialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {

            try {


            SharedPreferences sp = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);

            mobile = sp.getString("mobile", null);

            File file = new File(getActivity().getCacheDir().toURI());
            //Size in bytes of the cache
            int size = 1024 * 1024;

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
                    .query(PersondetailsQuery.builder().mobileno(mobile).build())
                    .httpCachePolicy(HttpCachePolicy.NETWORK_FIRST)
                    .enqueue(new ApolloCall.Callback<PersondetailsQuery.Data>() {
                        @Override
                        public void onResponse(@Nonnull Response<PersondetailsQuery.Data> response) {
                            try {


                                PersondetailsQuery.Data data = response.data();


                                if (data.person != null) {
                                    pwallet = data.person.get(0).wallet.toString();
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

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                        @Override
                        public void onFailure(@Nonnull ApolloException e) {

                            Log.e("Fail", "onFailure: ", e);

                        }
                    });

        }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public OnFragmentInteractionListener getmListener() {
        return mListener;
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

    @Override
    public void onFragmentInteraction(Uri uri) {

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
