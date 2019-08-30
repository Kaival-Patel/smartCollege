package com.kaival.smartcollege;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.ChildEventListener;
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
 * {@link frag_fac_sign_up.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link frag_fac_sign_up#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frag_fac_sign_up extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button submit;
    EditText email,enroll,password,confirmpassword1;
    boolean loop=false;
    ProgressBar pb;
    private FirebaseAuth mAuth;
    ScrollView sclayout;
    AnimationDrawable animationDrawable;
    String emailstring,pass,confirmpass;
    final boolean connected=false;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;

    public frag_fac_sign_up() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment frag_fac_sign_up.
     */
    // TODO: Rename and change types and number of parameters
    public static frag_fac_sign_up newInstance(String param1, String param2) {
        frag_fac_sign_up fragment = new frag_fac_sign_up();
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup containerID,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_frag_fac_sign_up, containerID, false);
        email=v.findViewById(R.id.email);
         emailstring=email.getText().toString();
        enroll=v.findViewById(R.id.enroll);
        password=v.findViewById(R.id.signuppassword);
         pass=password.getText().toString();
        confirmpassword1=v.findViewById(R.id.signupconfirmpassword);
        confirmpass=confirmpassword1.getText().toString();
        mAuth= FirebaseAuth.getInstance();
        submit=v.findViewById(R.id.signupbuttonfrag);
        pb=v.findViewById(R.id.pb);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reguser();
                System.out.println("EMAIL :"+email.getText().toString());
            }
        });
        sclayout=v.findViewById(R.id.sclayout);
        animationDrawable=(AnimationDrawable)sclayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();
        return v;
    }
    public void reguser()
    {
        ConnectivityManager connectivityManager=(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED &&
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
            Toast.makeText(getContext(),"Please Connect To Internet!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(email.getText().toString().isEmpty())
        {
            email.setError("Email cannot be Empty");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
        {
            email.setError("Invalid Email");
            email.requestFocus();
            return;
        }
        if(password.getText().toString().isEmpty())
        {
            password.setError("Password Cannot be Empty");
            password.requestFocus();
            return;

        }
        if(enroll.getText().toString().isEmpty())
        {
            enroll.setError("Enter the Faculty-ID");
            enroll.requestFocus();
            enroll.setBackgroundResource(R.drawable.errorred);
            return;
        }
        if(!password.getText().toString().equals(confirmpassword1.getText().toString()))
        {
            confirmpassword1.setError("Confirm the Password Correctly");
            confirmpassword1.requestFocus();

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
                if(task.isSuccessful())
                {
                    Faculty_user user =new Faculty_user(email.getText().toString(),enroll.getText().toString());
                    FirebaseDatabase.getInstance().getReference().child("Faculty").child(FirebaseAuth.getInstance().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(getContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                pb.setVisibility(View.GONE);
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Registration Failed!", Toast.LENGTH_SHORT).show();
                                pb.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                else
                {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException)
                    {
                        Toast.makeText(getContext(),"User Already Registered!Kindly Login to Continue",Toast.LENGTH_LONG).show();
                        pb.setVisibility(View.GONE);
                    }
                    else {
                        Toast.makeText(getContext(), "Error Occured!Try Again!", Toast.LENGTH_LONG).show();
                        pb.setVisibility(View.GONE);
                    }
                }
            }
        });




    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri01) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri01);
        }
    }

   /* @Override
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
        void onFragmentInteraction(String uri01);
    }
}
