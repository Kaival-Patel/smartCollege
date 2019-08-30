package com.kaival.smartcollege;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubjectSelector extends AppCompatActivity {
    static ExpandableListView explv;
    static ExpandableListAdapter adapter;
    static Button submit,submitsubs;
    ArrayList<String> SelectedItems;
    List<String> Subjecttitle=new ArrayList<>();
    static ArrayList<String> subjectlist=new ArrayList<String>();
    Map<String,ArrayList<String>> sublist=new HashMap<>();
    static FloatingActionButton addsub,deletesub;
    static EditText subname,subcode;
    String lowernamedatabase,lowernamecomp;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_selector);
        submit=findViewById(R.id.selectstudentsbtn);
        addsub=findViewById(R.id.addsubjectbtn);
        deletesub=findViewById(R.id.deletesubjectbtn);
        subname=findViewById(R.id.subjectname);
        subcode=findViewById(R.id.subjectcode);
        submitsubs=findViewById(R.id.submitsubbtn);
        explv=findViewById(R.id.expanded_lv);
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setTitle("Select the Subject");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        FirebaseDatabase.getInstance().getReference().child("Students Data").child("SUBJECTS").child(Midmarkssetup.departmentspin.getSelectedItem().toString()).child(Midmarkssetup.semesterspin.getSelectedItem().toString())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        subjectlist.removeAll(subjectlist);
                        for(DataSnapshot child:dataSnapshot.getChildren())
                        {
                            subjectlist.add(""+child.getKey().toString()+":"+child.getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        System.out.println("Subject List:"+subjectlist);
        Subjecttitle.add("Subjects List");
        sublist.put(Subjecttitle.get(0),subjectlist);
        System.out.println("Sublist:"+sublist);
        adapter=new MidMarksAdapter(this,Subjecttitle,sublist);
        explv.setAdapter(adapter);
        explv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });
        explv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = ((TextView)view).getText().toString();
                if(SelectedItems.contains(selectedItem))
                    SelectedItems.remove(selectedItem); //remove deselected item from the list of selected items
                else
                    SelectedItems.add(selectedItem); //add selected item to the list of selected items
            }
        });
        submitsubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SubjectSelector.subname.getText().toString().equals(""))
                    {
                        SubjectSelector.subname.setError("Enter The Subject Name!");
                        return;
                    }
                    if(SubjectSelector.subcode.getText().toString().equals(""))
                    {
                        SubjectSelector.subcode.setError("Enter The Subject Code!");
                        return;
                    }
                    if(subjectlist.size()>0) {
                        for (int i = 0; i < subjectlist.size(); i++) {
                            lowernamedatabase = subjectlist.get(i).toLowerCase();
                            lowernamecomp = subname.getText().toString().toLowerCase();
                            if (lowernamedatabase.equals(lowernamecomp + ":" + subcode.getText().toString())) {
                                Toast.makeText(getApplicationContext(), lowernamecomp + " Already Exists", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                    FirebaseDatabase.getInstance().getReference().child("Students Data").child("SUBJECTS").child(Midmarkssetup.departmentspin.getSelectedItem().toString())
                            .child(Midmarkssetup.semesterspin.getSelectedItem().toString()).child(SubjectSelector.subname.getText().toString().replaceAll("\\s+",""))
                            .setValue(SubjectSelector.subcode.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(SubjectSelector.this,"Subject Addition Success!",Toast.LENGTH_SHORT).show();
                                explv.setAdapter(adapter);
                            }
                        }
                    });
                }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
