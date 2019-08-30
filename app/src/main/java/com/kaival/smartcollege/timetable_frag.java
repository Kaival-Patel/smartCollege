package com.kaival.smartcollege;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class timetable_frag extends Fragment {
String [] daysp;
    String [] semsp;
    String [] deptsp;
    String []clgsp;
    String []slotsp;
    String []divisionsp;
    String []batchsp;
    EditText lecturername;
    Button lecturernamebtn;
    AdView madview;
    String selectedsem,selecteddept;
    Spinner day,college,division,sem,slot,subject,dept,batch;
    TextView name;
    Button submit,refreshsubs,editname;
    ProgressBar pb;
    boolean ttfound=false;

    public timetable_frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_timetable_frag, container, false);
        MobileAds.initialize(view.getContext(), "ca-app-pub-9758630723010439/7855444453");
        madview = (AdView) (view.findViewById(R.id.adView));
        AdRequest adRequest = new AdRequest.Builder().build();
        madview.loadAd(adRequest);
        day=(Spinner)view.findViewById(R.id.daychoosett);
        college=view.findViewById(R.id.clgchoosett);
        sem=view.findViewById(R.id.semchoosett);
        division=view.findViewById(R.id.divchoosett);
        slot=view.findViewById(R.id.slotchoosett);
        subject=view.findViewById(R.id.subjectchoosett);
        submit=view.findViewById(R.id.submitbtntt);
        dept=view.findViewById(R.id.deptchoosett);
        batch=view.findViewById(R.id.batchchoosett);
        pb=view.findViewById(R.id.pb);
        name=view.findViewById(R.id.facultynametxtview);
        lecturername=view.findViewById(R.id.lecturernameedittext);
        lecturernamebtn=view.findViewById(R.id.lectnamebtn);
        daysp= new String[]{"SELECT", "MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"};
        deptsp= new String[]{"SELECT", "MECHANICAL","CIVIL","COMPUTER","IT","CHEMICAL","AERONAUTICAL","E.C"};
        semsp= new String[]{"SELECT", "1","2","3","4","5","6","7","8"};
        clgsp= new String[]{"SELECT", "SOCET","ASOIT"};
        slotsp=new String[]{"SELECT","LEC 1","LAB 1","LEC 2","LAB 2","LEC 3","LAB 3","LEC 4","LEC 5","LEC 6"};
        divisionsp=new String[]{"A","B","C","D","E","F"};
        refreshsubs=view.findViewById(R.id.refreshsubs);
        editname=view.findViewById(R.id.editnamebtn);
        ArrayAdapter <String> dayadapter=new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_list_item_1,daysp);
        dayadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        ArrayAdapter deptadapter=new ArrayAdapter(this.getActivity(),android.R.layout.simple_list_item_1,deptsp);
        ArrayAdapter semadapter=new ArrayAdapter(this.getContext(),android.R.layout.simple_list_item_1,semsp);
        ArrayAdapter clgadapter=new ArrayAdapter(this.getContext(),android.R.layout.simple_list_item_1,clgsp);
        ArrayAdapter slotadapter=new ArrayAdapter(this.getContext(),android.R.layout.simple_list_item_1,slotsp);
        ArrayAdapter divisionadapter=new ArrayAdapter(this.getContext(),android.R.layout.simple_dropdown_item_1line,divisionsp);
        division.setAdapter(divisionadapter);
        day.setAdapter(dayadapter);
        college.setAdapter(clgadapter);
        dept.setAdapter(deptadapter);
        slot.setAdapter(slotadapter);
        sem.setAdapter(semadapter);
        editname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadprofile();

            }
        });
        loadprofilename();
        loaddeptandsem();
        loadbatch();
        refreshsubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadsubject();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadtimetable();
            }
        });
        lecturernamebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mauth=FirebaseAuth.getInstance();
                FirebaseUser user=mauth.getCurrentUser();
                if(user.getDisplayName()!=null)
                {
                    lecturername.setText(""+user.getDisplayName());
                }
                else
                {
                    Toast.makeText(getContext(),"Your Profile is not yet set so enter your name manually!",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        return view;
    }
    public void loadprofilename()
    {
        FirebaseAuth mauth=FirebaseAuth.getInstance();
        FirebaseUser user=mauth.getCurrentUser();
        if(user.getDisplayName()!=null)
        {
            name.setText("Hello "+user.getDisplayName());
        }
        else
        {
            name.setText("Hello Your Name");
        }
    }
    public void loaddeptandsem()
    {
        dept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selecteddept=deptsp[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedsem=semsp[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public void loadsubject()
    {
        System.out.println("Selected Sem:"+selectedsem);
        System.out.println("Selected DEPT:"+selecteddept);
        if(selecteddept=="IT")
        {
            if(selectedsem=="1")
            {
                String []it1={"SELECT","EG","EEE","EME","CALCULUS","CS"};
                ArrayAdapter<String> it1adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,it1);
                subject.setAdapter(it1adapter);
            }
            if(selectedsem=="2")
            {
                String []it2={"SELECT","CPD","EWS","OOPC","MATHS 2","BE"};
                ArrayAdapter<String> it2adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,it2);
                subject.setAdapter(it2adapter);
            }
            if(selectedsem=="3")
            {
                String []it3={"SELECT","DS","DBMS","AEM(MATHS-3)","DE"};
                ArrayAdapter<String> it3adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,it3);
                subject.setAdapter(it3adapter);
            }
            if(selectedsem=="5")
            {
                String []it5={"SELECT","OOPJ","ADA","CS","SP","DE"};
                ArrayAdapter<String> it5adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,it5);
                subject.setAdapter(it5adapter);
            }

        }
        else
        {
            String nothingstring[]={"Nothing Selected!"};
            ArrayAdapter nothingadapter=new ArrayAdapter(getContext(),android.R.layout.simple_dropdown_item_1line,nothingstring);
            subject.setAdapter(nothingadapter);
        }
    }
    public void loadprofile()
    {
        Toast.makeText(getContext(),"Go to Profile Section for Editing Name!",Toast.LENGTH_SHORT).show();
    }
    public void loadbatch()
    {
        division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                batchsp=new String[]{""+divisionsp[position]+"1",""+divisionsp[position]+"2",""+divisionsp[position]+"3","Lecture"};
                ArrayAdapter batchadapter=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,batchsp);
                batch.setAdapter(batchadapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        System.out.println("Batch Generated:"+batchsp);




    }
    public void uploadtimetable()
    {
       try {
           if (day.getSelectedItem().toString().equals("SELECT")) {
               Toast.makeText(getContext(), "Please Select the Day!", Toast.LENGTH_SHORT).show();
               return;
           }
           if (dept.getSelectedItem().toString().equals("SELECT")) {
               Toast.makeText(getContext(), "Please Select the Department!", Toast.LENGTH_SHORT).show();
               return;
           }
           if (sem.getSelectedItem().toString().equals("SELECT")) {
               Toast.makeText(getContext(), "Please Select the Semester!", Toast.LENGTH_SHORT).show();
               return;
           }
           if (college.getSelectedItem().toString().equals("SELECT")) {
               Toast.makeText(getContext(), "Please Select the College!", Toast.LENGTH_SHORT).show();
               return;
           }
           if (subject.getSelectedItem().toString().equals("SELECT")) {
               Toast.makeText(getContext(), "Please Select the Subject!", Toast.LENGTH_SHORT).show();
               return;
           }
           if (division.getSelectedItem().toString().equals("SELECT")) {
               Toast.makeText(getContext(), "Please Select the Division!", Toast.LENGTH_SHORT).show();
               return;
           }
           if (slot.getSelectedItem().toString().equals("SELECT")) {
               Toast.makeText(getContext(), "Please Select the Slot!", Toast.LENGTH_SHORT).show();
               return;
           }
           if (slot.getSelectedItem().toString().contains("LAB") == true && batch.getSelectedItem().toString().equals("Lecture") == true) {
               Toast.makeText(getContext(), "Batch must not be selected for lecture!", Toast.LENGTH_SHORT).show();
               return;
           }
           if (slot.getSelectedItem().toString().contains("LEC") == true && batch.getSelectedItem().toString().equals("Lecture") == false) {
               Toast.makeText(getContext(), "For Lecture Dont Select any Batches!", Toast.LENGTH_SHORT).show();
               return;

           }
           if (lecturername.getText().toString().equals("")) {
               Toast.makeText(getContext(), "Please enter Lecturer Name!", Toast.LENGTH_SHORT).show();
               lecturername.setError("Enter Lecturer Name!");
               return;
           }
           //Firebase Connection and upload
           final DatabaseReference fd;
           fd = FirebaseDatabase.getInstance().getReference();
           submit.setEnabled(false);
           pb.setVisibility(View.VISIBLE);
           fd.child("Faculty Data").child("TimeTable").child(college.getSelectedItem().toString()).child(dept.getSelectedItem().toString()).
                   child(sem.getSelectedItem().toString()).child("Class TT").child(day.getSelectedItem().toString()).child(slot.getSelectedItem().toString()).child(batch.getSelectedItem().toString())
           .addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   pb.setVisibility(View.GONE);
                   submit.setEnabled(true);
                   for(DataSnapshot child:dataSnapshot.getChildren())
                   {
                       System.out.println("Type of child:"+child.getValue().getClass().getName());
                       System.out.println("We get the data at address?:"+child.getValue().toString());
                       ttfound=true;
                   }
                   if(ttfound==false)
                   {
                       fd.child("Faculty Data").child("TimeTable").child(college.getSelectedItem().toString()).child(dept.getSelectedItem().toString()).
                               child(sem.getSelectedItem().toString()).child("Class TT").child(day.getSelectedItem().toString()).child(slot.getSelectedItem().toString()).
                               child(batch.getSelectedItem().toString()).child(subject.getSelectedItem().toString()).setValue(lecturername.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful()) {
                                   pb.setVisibility(View.GONE);
                                   submit.setEnabled(true);
                                   Toast.makeText(getContext(), "Timetable Changed/Uploaded Successfully!", Toast.LENGTH_LONG).show();
                               } else if (!task.isSuccessful()) {
                                   pb.setVisibility(View.GONE);
                                   submit.setEnabled(true);
                                   Toast.makeText(getContext(), "" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                   return;
                               }
                           }
                       });
                   }
                   else if(ttfound==true)
                   {
                       pb.setVisibility(View.GONE);
                       submit.setEnabled(false);
                       AlertDialog alertDialog=new AlertDialog.Builder(getContext()).setTitle("Timetable Found!").setMessage("Are you Sure You want to Change the Timetable.It is Already in our Database!").setNegativeButton("NO",null).
                               setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                       pb.setVisibility(View.VISIBLE);
                                       fd.child("Faculty Data").child("TimeTable").child(college.getSelectedItem().toString()).child(dept.getSelectedItem().toString()).
                                               child(sem.getSelectedItem().toString()).child("Class TT").child(day.getSelectedItem().toString()).child(slot.getSelectedItem().toString()).
                                               child(batch.getSelectedItem().toString()).child(subject.getSelectedItem().toString()).setValue(lecturername.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                               if (task.isSuccessful()) {
                                                   pb.setVisibility(View.GONE);
                                                   submit.setEnabled(true);
                                                   Toast.makeText(getContext(), "Timetable Changed/Uploaded Successfully!", Toast.LENGTH_LONG).show();
                                               } else if (!task.isSuccessful()) {
                                                   pb.setVisibility(View.GONE);
                                                   submit.setEnabled(true);
                                                   Toast.makeText(getContext(), "" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                   return;
                                               }
                                           }
                                       });
                                   }
                               }).setCancelable(false).show();
                   }
               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });
          /* */
       }
       catch (Exception n)
       {
           pb.setVisibility(View.GONE);
           submit.setEnabled(true);
           Toast.makeText(getContext(),""+n.getMessage(),Toast.LENGTH_LONG).show();
       }
    }




}
