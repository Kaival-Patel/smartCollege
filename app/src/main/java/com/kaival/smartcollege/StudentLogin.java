package com.kaival.smartcollege;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLOutput;
import java.util.concurrent.TimeUnit;

public class StudentLogin extends AppCompatActivity {

    Button signup,signin;
    EditText username,password;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        signup=(Button)(findViewById(R.id.signup));
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(StudentLogin.this,welcome_page.class);
                startActivity(i);
            }
        });
        signin=(Button)(findViewById(R.id.stsignin));
        username=(EditText)findViewById(R.id.loginusername);
        pb=(ProgressBar)(findViewById(R.id.progressbar));
        password=(EditText)findViewById(R.id.loginpassword);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                for(int i=0;i<100;i++)
                {
                    pb.setProgress(i);

                }
                signin.setEnabled(false);
                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    connected = true;
                  DatabaseReference dr;
                  dr=FirebaseDatabase.getInstance().getReference().child("Students").child(username.getText().toString()).child("Password");
                  dr.addValueEventListener(new ValueEventListener() {
                      @Override
                      public void onDataChange(DataSnapshot dataSnapshot) {
                        String getfetched= (String) dataSnapshot.getValue();
                        String pass=password.getText().toString();
                          System.out.println("Username input:"+username.getText().toString());
                          System.out.println("Password Input:"+password.getText().toString());
                          System.out.println("FETCHED PASSWORD:"+getfetched);

                          if(pass.equals(getfetched)==false)
                        {
                            pb.setVisibility(View.INVISIBLE);

                            Toast.makeText(getApplicationContext(),"Wrong Login Credentials!",Toast.LENGTH_SHORT).show();
                            username.setBackgroundResource(R.drawable.errorred);
                            password.setBackgroundResource(R.drawable.errorred);
                            signin.setEnabled(true);
                        }

                        else
                        {
                            pb.setVisibility(View.INVISIBLE);

                            System.out.println("Successful LOGIN");
                            signin.setEnabled(true);
                            Toast.makeText(getApplicationContext(),"SUCCESSFUL LOGIN", Toast.LENGTH_SHORT).show();
                            Intent is=new Intent(StudentLogin.this,Facultyloggedin.class);
                            is.putExtra("Username",username.getText().toString());
                            is.putExtra("type","Students");
                            startActivity(is);
                        }
                      }

                      @Override
                      public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),"Error!Try AGAIN",Toast.LENGTH_SHORT).show();
                      }
                  });


                }
                else {
                    connected = false;
                    Toast.makeText(getApplicationContext(),"Please Connect to Internet!!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
