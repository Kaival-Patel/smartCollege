package com.kaival.smartcollege;


import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.HashMap;

import javax.microedition.khronos.egl.EGLDisplay;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_upload extends Fragment{
Spinner semspinner,typespinner;
String semlist[]={"SOCET:IT:5_IT_A","SOCET:IT:5_IT_B"};
String type[]={"MID-1-Marks","Mid-2-Marks","Attendance Till Date"};
ProgressBar pb;
EditText enroll,sp,ada,cg,oopj,attendance;
Button update;
TextView tv;
boolean datafound=false;
String semtypevalue,semspinnervalue;

    public fragment_upload() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_fragment_upload, container, false);
        semspinner=v.findViewById(R.id.spinner_sem);
        typespinner=v.findViewById(R.id.spinner_type);
        ArrayAdapter<String> semadapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, Arrays.asList(semlist));
        ArrayAdapter<String> typeadapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, Arrays.asList(type));
        typespinner.setAdapter(typeadapter);
        semspinner.setAdapter(semadapter);
        update=(Button)v.findViewById(R.id.update_btn);
        sp=(EditText)v.findViewById(R.id.spmarks);
        ada=(EditText)v.findViewById(R.id.adamarks);
        oopj=(EditText)v.findViewById(R.id.oopjmarks);
        cg=(EditText)v.findViewById(R.id.cgmarks);
        enroll=(EditText)v.findViewById(R.id.enrollment);
        attendance=(EditText)v.findViewById(R.id.attendance);
        tv=(TextView)v.findViewById(R.id.entermarks);
        pb=(ProgressBar)v.findViewById(R.id.pb);
        semspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                semspinnervalue=semlist[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        typespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  semtypevalue=type[position];
                  if(semtypevalue.equals("Attendance Till Date"))
                  {
                      attendance.setVisibility(View.VISIBLE);
                      ada.setVisibility(View.GONE);
                      sp.setVisibility(View.GONE);
                      oopj.setVisibility(View.GONE);
                      tv.setVisibility(View.GONE);
                      cg.setVisibility(View.GONE);
                      update.setText("Update Attendance");
                      update.setPadding(10,10,10,10);
                  }
                  else
                  {
                      attendance.setVisibility(View.GONE);
                      ada.setVisibility(View.VISIBLE);
                      sp.setVisibility(View.VISIBLE);
                      oopj.setVisibility(View.VISIBLE);
                      tv.setVisibility(View.VISIBLE);
                      cg.setVisibility(View.VISIBLE);
                      update.setText("Update Marks");
                      update.setPadding(10,10,10,10);
                  }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                for(int i=0;i<100;i++)
                {
                    pb.setProgress(i);

                }
                boolean connected=false;
                ConnectivityManager connectivityManager = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED){
                    connected=true;
                }
                if(connected==true)
                {
                    //Do stuff when connection is available
                    DatabaseReference dr;
                    if(semtypevalue.equals("Attendance Till Date")==false) {
                        //if get type isnt attendance
                        if(enroll.getText().toString().length()==12==true) {
                            //check valid enrollment
                            int cgmarks=Integer.parseInt(cg.getText().toString());
                            int oopjmarks=Integer.parseInt(oopj.getText().toString());
                            int adamarks=Integer.parseInt(ada.getText().toString());
                            int spmarks=Integer.parseInt(sp.getText().toString());


                            if(cgmarks<41 && oopjmarks<41 && adamarks<41 && spmarks<41) {
                                //check valid marks!!
                                dr = FirebaseDatabase.getInstance().getReference().child("Students").child(enroll.getText().toString()).child("Data").child(semtypevalue);
                                dr.child("CG").setValue(cg.getText().toString());
                                dr.child("SP").setValue(sp.getText().toString());
                                dr.child("ADA").setValue(ada.getText().toString());
                                dr.child("OOPJ").setValue(oopj.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        pb.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getContext(), "Details Uploaded Successfully", Toast.LENGTH_LONG).show();
                                        enroll.setText("");
                                        sp.setText("");
                                        cg.setText("");
                                        oopj.setText("");
                                        ada.setText("");
                                    }
                                });
                                //check valid marks!!
                            }
                            else
                            {
                                //not valid marks!
                                pb.setVisibility(View.INVISIBLE);
                                Toast.makeText(getContext(),"Marks Entered aren't Valid",Toast.LENGTH_SHORT).show();
                                sp.setBackgroundResource(R.drawable.errorred);
                                cg.setBackgroundResource(R.drawable.errorred);
                                ada.setBackgroundResource(R.drawable.errorred);
                                oopj.setBackgroundResource(R.drawable.errorred);
                            }
                            //check valid enrollment
                        }
                        if(enroll.getText().toString().length()==12==false)
                        {
                            //enrollment error
                            pb.setVisibility(View.INVISIBLE);
                            Toast.makeText(getContext(),"Enter Valid Enrollment Number!",Toast.LENGTH_SHORT).show();
                            enroll.setBackgroundResource(R.drawable.errorred);
                        }
                        else
                        {
                            //nothing to do when we have both conditions for enrollment check
                        }
                        //if get type isnt attendance
                    }
                    else {
                        if (enroll.getText().toString().length() == 12 == false) {
                            //enrollment error
                            pb.setVisibility(View.INVISIBLE);
                            Toast.makeText(getContext(), "Enter Valid Enrollment Number!", Toast.LENGTH_SHORT).show();
                            enroll.setBackgroundResource(R.drawable.errorred);
                        }
                        if (enroll.getText().toString().length() == 12 == true) {
                            dr = FirebaseDatabase.getInstance().getReference().child("Students").child("" + enroll.getText().toString()).child("Data").child("Attendance");
                            dr.setValue(attendance.getText().toString().concat("%")).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    pb.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getContext(), "ATTENDANCE SUBMITTED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                                    attendance.setText("");
                                }
                            });
                        }
                    }
                    //Do stuff when connection is available
                }
                else
                {
                    pb.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "Please Connect To Internet and try Again!", Toast.LENGTH_SHORT).show();

                }
            }
        });


        return v;
    }


}
