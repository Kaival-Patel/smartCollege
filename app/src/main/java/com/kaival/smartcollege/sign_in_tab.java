package com.kaival.smartcollege;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class sign_in_tab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View view;
    ProgressBar pb;
    Button signin;
    TextView forget;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    static EditText enroll;
    EditText email;
    EditText password;
    ScrollView sclayout;
    AnimationDrawable animationDrawable;
    CheckBox remember;
    private SharedPreferences mprefs;
    private static final String PREFS_NAME="PrefsFile";
    boolean loop=false;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public sign_in_tab() {
        // Required empty public constructor

    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment sign_in_tab.
     */
    // TODO: Rename and change types and number of parameters
    public static sign_in_tab newInstance(String param1, String param2) {
        sign_in_tab fragment = new sign_in_tab();
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
        mprefs=this.getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        view=inflater.inflate(R.layout.fragment_sign_in_tab, container, false);
        enroll=(EditText)view.findViewById(R.id.enroll);
        signin=(Button)view.findViewById(R.id.signinbutton);
        password=(EditText) view.findViewById(R.id.password);
        sclayout=view.findViewById(R.id.sclayout);
        animationDrawable=(AnimationDrawable)sclayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();
        remember=(CheckBox)view.findViewById(R.id.rememberme);
        pb=(ProgressBar)view.findViewById(R.id.pb);
        forget=(TextView)view.findViewById(R.id.forget);
        email=(EditText)view.findViewById(R.id.email);
        getstPreferencesData();
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),forgetpassword.class);
                startActivity(i);
            }
        });

        return view;
    }
    private void getstPreferencesData() {
    SharedPreferences sp=getActivity().getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
    if(sp.contains("pref_email"))
    {
        String em=sp.getString("pref_email","not found.");
        email.setText(em.toString());
    }
        if(sp.contains("pref_enroll"))
        {
            String em=sp.getString("pref_enroll","not found.");
            enroll.setText(em.toString());
        }
        if(sp.contains("pref_pass"))
        {
            String em=sp.getString("pref_pass","not found.");
            password.setText(em.toString());
        }
        if(sp.contains("pref_check"))
        {
            Boolean em=sp.getBoolean("pref_check",false);
            remember.setChecked(em);
        }
    }
    public void signin()
    {
        String passwordid=password.getText().toString();
        ConnectivityManager connectivityManager=(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED &&
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
            Toast.makeText(getContext(),"Please Connect To Internet!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(email.getText().toString().isEmpty())
        {
            email.setError("Enter the Email-ID");
            email.requestFocus();
            email.setBackgroundResource(R.drawable.errorred);
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
        {
            email.setError("Enter the Valid Email-ID");
            email.requestFocus();
            email.setBackgroundResource(R.drawable.errorred);
            return;
        }
        if(enroll.getText().toString().isEmpty())
        {
            enroll.setError("Enter the Enrollment-ID!");
            enroll.requestFocus();
            enroll.setBackgroundResource(R.drawable.errorred);
            return;
        }
        if(enroll.getText().toString().length()!=12)
        {
            enroll.setError("Enter the Valid Enrollment-ID!");
            enroll.requestFocus();
            enroll.setBackgroundResource(R.drawable.errorred);
            return;

        }
        if(password.getText().toString().isEmpty())
        {
            password.setError("Enter the Password!");
            password.requestFocus();
            return;
        }
        if(remember.isChecked())
        {
            Boolean boolchecked=remember.isChecked();
            SharedPreferences.Editor editor=mprefs.edit();
            editor.putString("pref_email",email.getText().toString());
            editor.putString("pref_enroll",enroll.getText().toString());
            editor.putString("pref_pass",password.getText().toString());
            editor.putBoolean("pref_check",boolchecked);
            editor.apply();
        }
        if(!remember.isChecked())
        {
            mprefs.edit().clear().apply();
        }

        pb.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference().child("Students").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            System.out.println("Outside the Loop:" + dataSnapshot.getValue());
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                String vals = (String) child.getValue();
                                System.out.println("StringS:" + vals);
                                if (vals.equals(enroll.getText().toString())) {
                                    loop = true;
                                }

                            }
                            System.out.println("LOOP:" + loop);
                            try {
                                if (loop == true) {
                                    pb.setVisibility(View.GONE);
                                    Intent i = new Intent(getActivity(), StudentLoggedin.class);
                                    i.putExtra("Username", enroll.getText().toString());
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    getActivity().finish();
                                    startActivity(i);
                                    email.getText().clear();
                                    enroll.getText().clear();
                                    password.getText().clear();
                                } else {
                                    pb.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                            catch(Exception e)
                            {
                                Toast.makeText(getContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
                else
                {
                    pb.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Wrong Login Credentials!Try Again", Toast.LENGTH_LONG).show();
                }
            }
        });
        }





    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri0) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri0);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri0);
    }
}
