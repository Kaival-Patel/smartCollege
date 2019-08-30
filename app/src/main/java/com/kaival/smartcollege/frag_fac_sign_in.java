package com.kaival.smartcollege;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link frag_fac_sign_in.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link frag_fac_sign_in#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frag_fac_sign_in extends Fragment {
    ProgressBar pb;
    Button signin;
    EditText email,password,enroll;
    TextView forget;
    CheckBox remember;
    ScrollView sclayout;
    AnimationDrawable animationDrawable;
    boolean loop=false;
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "FPrefsFile";
    FirebaseAuth mAuth=FirebaseAuth.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public frag_fac_sign_in() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment frag_fac_sign_in.
     */
    // TODO: Rename and change types and number of parameters
    public static frag_fac_sign_in newInstance(String param1, String param2) {
        frag_fac_sign_in fragment = new frag_fac_sign_in();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup containerId,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_frag_fac_sign_in,containerId, false);
        mPrefs=this.getActivity().getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        email=view.findViewById(R.id.email);
        remember=view.findViewById(R.id.rememberme);
        password=view.findViewById(R.id.password);
        pb=view.findViewById(R.id.pb);
        sclayout=view.findViewById(R.id.sclayout);
        animationDrawable=(AnimationDrawable)sclayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();
        enroll=view.findViewById(R.id.enroll);
        forget=view.findViewById(R.id.forget);
        signin=view.findViewById(R.id.signinbutton);
        getPreferencesData();
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Email:"+email.getText().toString());
                System.out.println("Password:"+password.getText().toString());
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


    private void getPreferencesData() {
        SharedPreferences sp=getActivity().getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        if(sp.contains("fpref_email"))
        {
            String em=sp.getString("fpref_email","not found.");
            email.setText(em.toString());
        }
        if(sp.contains("fpref_pass"))
        {
            String em=sp.getString("fpref_pass","not found.");
            password.setText(em.toString());
        }
        if(sp.contains("fpref_enroll"))
        {
            String em=sp.getString("fpref_enroll","not found.");
            enroll.setText(em.toString());
        }

        if(sp.contains("fpref_check"))
        {
            Boolean b=sp.getBoolean("fpref_check",false);
            remember.setChecked(b);
        }
    }

    public void signin()
    {
        String emailid=email.getText().toString();
        String passwordid=password.getText().toString();
        ConnectivityManager connectivityManager=(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED &&
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
            Toast.makeText(getContext(),"Please Connect To Internet!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(email.getText().toString().isEmpty())
        {
            email.setError("Enter the Email!");
            email.requestFocus();
            email.setBackgroundResource(R.drawable.errorred);
            return;
        }
        if(password.getText().toString().isEmpty())
        {
            password.setError("Enter the Password!");
            password.requestFocus();
            password.setBackgroundResource(R.drawable.errorred);
            return;
        }
        if(enroll.getText().toString().isEmpty())
        {
            enroll.setError("Enter the Faculty-ID");
            enroll.requestFocus();
            enroll.setBackgroundResource(R.drawable.errorred);
            return;
        }
        if(remember.isChecked())
        {
            Boolean boolisChecked=remember.isChecked();
            SharedPreferences.Editor editor=mPrefs.edit();
            editor.putString("fpref_email",email.getText().toString());
            editor.putString("fpref_pass",password.getText().toString());
            editor.putString("fpref_enroll",enroll.getText().toString());
            editor.putBoolean("fpref_check",boolisChecked);
            editor.apply();
        }
        if(!remember.isChecked())
        {
            mPrefs.edit().clear().apply();
        }
        pb.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(emailid,passwordid).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference().child("Faculty").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
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
                            FirebaseRemoteConfig remoteConfig=FirebaseRemoteConfig.getInstance();
                            System.out.println("Remote Config signed in account:"+remoteConfig.getString("Signed_in"));
                            if (loop == true) {
                                pb.setVisibility(View.GONE);
                                Toast.makeText(getContext(),"Login Successful",Toast.LENGTH_LONG).show();
                                System.out.println("Current User:"+mAuth.getCurrentUser().getUid());
                                Intent i=new Intent(getActivity(),Facultyloggedin.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);

                                email.getText().clear();
                                enroll.getText().clear();
                                password.getText().clear();
                            } else {
                                pb.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });




                }
                else if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                {
                    pb.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"Invalid Credentials",Toast.LENGTH_LONG).show();
                }
                else
                {
                    pb.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"Login Error!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri00) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri00);
        }
    }

  /*  @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

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
        void onFragmentInteraction(String uri00);
    }


}
