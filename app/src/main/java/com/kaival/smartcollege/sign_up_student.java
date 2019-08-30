package com.kaival.smartcollege;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link sign_up_student#newInstance} factory method to
 * create an instance of this fragment.
 */
public class sign_up_student extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button submit;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    public EditText email,enroll,password,confirmpassword1;
    ProgressBar pb;
    boolean loop=false;
    ScrollView sclayout;
    AnimationDrawable animationDrawable;
    View view;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public sign_up_student() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static sign_up_student newInstance(String param1, String param2) {
        sign_up_student fragment = new sign_up_student();
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
        view=inflater.inflate(R.layout.fragment_sign_up_student, container, false);
        email=view.findViewById(R.id.email);
        password=view.findViewById(R.id.password);
        sclayout=view.findViewById(R.id.sclayout);
        animationDrawable=(AnimationDrawable)sclayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();
        confirmpassword1=view.findViewById(R.id.confirmpassword1);
        pb=view.findViewById(R.id.pb);
        enroll=view.findViewById(R.id.enroll);
        submit=view.findViewById(R.id.signupbuttonfrag);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupstudent();
            }
        });
              
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null)
        {

        }
    }

    public void signupstudent() {
        final String semail=email.getText().toString();
        final String senroll=enroll.getText().toString();
        ConnectivityManager connectivityManager=(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED &&
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
            Toast.makeText(getContext(),"Please Connect To Internet!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(enroll.getText().toString().isEmpty())
        {
            enroll.setError("Enrollment-ID cannot be Empty");
            enroll.requestFocus();
            enroll.setBackgroundResource(R.drawable.errorred);
            return;
        }
        if(enroll.getText().toString().length()==12==false)
        {
            enroll.setError("Invalid Enrollment-ID");
            enroll.requestFocus();
            enroll.setBackgroundResource(R.drawable.errorred);
            return;
        }

        if(email.getText().toString().isEmpty())
        {
            email.setError("Email cannot be Empty");
            email.requestFocus();
            email.setBackgroundResource(R.drawable.errorred);
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
        {
            email.setError("Invalid Email");
            email.requestFocus();
            email.setBackgroundResource(R.drawable.errorred);
            return;
        }
        if(password.getText().toString().isEmpty())
        {
            password.setError("Password Cannot be Empty");
            password.requestFocus();
            password.setBackgroundResource(R.drawable.errorred);
            return;

        }
        if(!password.getText().toString().equals(confirmpassword1.getText().toString()))
        {
            confirmpassword1.setError("Confirm the Password Correctly");
            confirmpassword1.requestFocus();
            confirmpassword1.setBackgroundResource(R.drawable.errorred);

            return;

        }
        if(password.getText().toString().length()<6)
        {
            password.setError("Password Too short");
            password.requestFocus();
            return;

        }
        pb.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                pb.setVisibility(View.GONE);
                student_user user =new student_user(semail,senroll);
                System.out.println("Users Are:"+user);
                FirebaseDatabase.getInstance().getReference().child("Students").child(FirebaseAuth.getInstance().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Registration Failed!Try Again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                if(!task.isSuccessful())
                {
                    Toast.makeText(getContext(), task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }




            }
        });






    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
