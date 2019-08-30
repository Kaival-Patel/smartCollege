package com.kaival.smartcollege;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgetpassword extends AppCompatActivity {
    EditText email;
    Button reset;
    ProgressBar pb;
    FirebaseAuth mauth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        getSupportActionBar().setTitle("Forget Password");
        email=findViewById(R.id.forgetemail);
        reset=findViewById(R.id.resetpasswordbtn);
        pb=findViewById(R.id.pb);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetmypassword();
            }
        });

    }
    public void resetmypassword()
    {
        String forgetemail=email.getText().toString().trim();
        if(!Patterns.EMAIL_ADDRESS.matcher(forgetemail).matches()){
            Toast.makeText(this,"Enter Valid Email ID",Toast.LENGTH_SHORT).show();
        }
        else
        {
            reset.setEnabled(false);
            pb.setVisibility(View.VISIBLE);
            mauth.sendPasswordResetEmail(forgetemail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                   if(task.isSuccessful())
                   {
                       reset.setEnabled(false);
                       pb.setVisibility(View.GONE);
                       Toast.makeText(forgetpassword.this,"Password Reset Link sent!Check your Email",Toast.LENGTH_LONG).show();
                   }
                   else if(!task.isSuccessful())
                   {
                       reset.setEnabled(true);
                       pb.setVisibility(View.GONE);
                       Toast.makeText(forgetpassword.this,""+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                   }

                }
            });
        }
    }

}
