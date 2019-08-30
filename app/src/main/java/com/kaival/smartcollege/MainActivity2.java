package com.kaival.smartcollege;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

public class MainActivity2 extends AppCompatActivity {
static ExpandableListView explv;
    List<String> batchlist=new ArrayList<>();
    Map<String,ArrayList<String>> studlist=new HashMap<>();
    static ArrayList<String> b1=new ArrayList<String>();
    static ArrayList<String> b2=new ArrayList<String>();
    static ArrayList<String> b3=new ArrayList<String>();
    static ExpandableListAdapter listAdapter;
    static FloatingActionButton addbtn;
    static FloatingActionButton deletebtn;
    static Button add;
    static EditText enroll;
    EditText name;
    static ProgressBar pb;
    String getbatch;
    ArrayList<String> SelectedItems;
    public static Button submit;
    String ignorednamedatabase;
    String ignorednamecomp;
    static Spinner batchspinner;
    String []spinnervals;
    public static ArrayList<String> vals =new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        pb=findViewById(R.id.pb);
        pb.setVisibility(View.VISIBLE);
        submit=(Button)findViewById(R.id.submitbtn);
        addbtn=findViewById(R.id.addstd);
        deletebtn=findViewById(R.id.deletestd);
        enroll=findViewById(R.id.enroll);
        name=findViewById(R.id.name);
        add=findViewById(R.id.add);
        batchspinner=findViewById(R.id.batchspin);
        String div=getAttendance.division.getSelectedItem().toString();
        spinnervals= new String[]{"Select",div+"1 BATCH", div+"2 BATCH", div+"3 BATCH"};
        final ArrayAdapter spinvals=new ArrayAdapter(this,android.R.layout.simple_list_item_1,spinnervals);
        //spinvals.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        batchspinner.setAdapter(spinvals);
        batchspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"U selected:"+spinnervals[position],Toast.LENGTH_SHORT).show();
                getbatch=spinnervals[position];
                View v= batchspinner.getSelectedView();
                ((TextView)v).setTextSize(13);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                getbatch=null;
            }
        });

            //b1 Batch
        String sem=getAttendance.sem.getSelectedItem().toString();
        String dept=getAttendance.dept.getSelectedItem().toString();
        System.out.println(""+sem+""+dept+""+div);
            FirebaseDatabase.getInstance().getReference().child("Students Data"). child(getAttendance.dept.getSelectedItem().toString()+getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString()+getAttendance.division.getSelectedItem().toString()).child(div+"1"+" BATCH").child("LIST").addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    b1.removeAll(b1);
                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        b1.add("" + child.getKey().toString() + ":" + child.getValue().toString());
                        vals.add("" + child.getKey().toString() + ":" + child.getValue().toString());

                    }

                }

                public void onCancelled(DatabaseError databaseError) {

                }
            });

            //b2 batch

            FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString()+getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString()+getAttendance.division.getSelectedItem().toString()).child(div+"2"+" BATCH").child("LIST").addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    b2.removeAll(b2);
                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        b2.add("" + child.getKey().toString() + ":" + child.getValue().toString());
                        vals.add("" + child.getKey().toString() + ":" + child.getValue().toString());

                    }

                }

                public void onCancelled(DatabaseError databaseError) {

                }
            });

            //b3 batch

            FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString()+getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString()+getAttendance.division.getSelectedItem().toString()).child(div+"3"+" BATCH").child("LIST").addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    b3.removeAll(b3);
                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        b3.add("" + child.getKey().toString() + ":" + child.getValue().toString());
                        vals.add("" + child.getKey().toString() + ":" + child.getValue().toString());
                    }
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            });

        pb.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);
        filldata();
        explv=(ExpandableListView)findViewById(R.id.expanded_lv);
        listAdapter=new MyAdapter(this,batchlist,studlist,submit);
        explv.setAdapter(listAdapter);
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
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onVerify();
            }
        });

    }




    public void filldata()
    {
        String div=getAttendance.division.getSelectedItem().toString();
        batchlist.add(div+"1 Batch");
        batchlist.add(div+"2 Batch");
        batchlist.add(div+"3 Batch");


        studlist.put(batchlist.get(0),b1);
        studlist.put(batchlist.get(1),b2);
        studlist.put(batchlist.get(2),b3);
        pb.setVisibility(View.GONE);
    }
    public static void b2add()
    {String sem=getAttendance.sem.getSelectedItem().toString();
        String dept=getAttendance.dept.getSelectedItem().toString();
        String div=getAttendance.division.getSelectedItem().toString();
        System.out.println(""+sem+""+dept+""+div);
        FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString()+getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString()+getAttendance.division.getSelectedItem().toString()).child(div+"2"+" BATCH").child("LIST").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                b2.removeAll(b2);
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    b2.add("" + child.getKey().toString() + ":" + child.getValue().toString());
                    vals.add("" + child.getKey().toString() + ":" + child.getValue().toString());

                }

            }

            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public static void b1add()
    {
        String sem=getAttendance.sem.getSelectedItem().toString();
        String dept=getAttendance.dept.getSelectedItem().toString();
        String div=getAttendance.division.getSelectedItem().toString();
        System.out.println(""+sem+""+dept+""+div);
        FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString()+getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString()+getAttendance.division.getSelectedItem().toString()).child(div+"1"+" BATCH").child("LIST").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                b1.removeAll(b1);
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    b1.add("" + child.getKey().toString() + ":" + child.getValue().toString());
                    vals.add("" + child.getKey().toString() + ":" + child.getValue().toString());

                }

            }

            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public static void b3add()
    {

        String sem=getAttendance.sem.getSelectedItem().toString();
        String dept=getAttendance.dept.getSelectedItem().toString();
        String div=getAttendance.division.getSelectedItem().toString();
        System.out.println(""+sem+""+dept+""+div);
                FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString()+getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString()+getAttendance.division.getSelectedItem().toString()).child(div+"3"+" BATCH").child("LIST").addValueEventListener(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        b3.removeAll(b3);
                        for (DataSnapshot child : dataSnapshot.getChildren()) {

                            b3.add("" + child.getKey().toString() + ":" + child.getValue().toString());
                            vals.add("" + child.getKey().toString() + ":" + child.getValue().toString());
                        }
                    }

                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public void onVerify() {
        pb.setVisibility(View.VISIBLE);
        if (enroll.getVisibility()==View.GONE &&name.getVisibility()==View.GONE&&add.getVisibility()==View.GONE&&batchspinner.getVisibility()==View.GONE ) {
            enroll.setVisibility(View.VISIBLE);
            name.setVisibility(View.VISIBLE);
            add.setVisibility(View.VISIBLE);
            batchspinner.setVisibility(View.VISIBLE);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (enroll.getText().toString().isEmpty()) {
                        pb.setVisibility(View.GONE);
                        enroll.setError("Enter the Enrollment");
                        return;
                    }
                    if (name.getText().toString().isEmpty()) {
                        pb.setVisibility(View.GONE);
                        name.setError("Enter the Name");
                        return;
                    }
                    if(getbatch==null || getbatch=="Select")
                    {
                        pb.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Select the Batch!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for (int i = 0; i < vals.size(); i++) {
                        ignorednamedatabase = vals.get(i).toLowerCase();
                        System.out.println("Values ignored:" + ignorednamedatabase);

                        ignorednamecomp = name.getText().toString().toLowerCase();
                        if (ignorednamedatabase.equals(enroll.getText().toString() + ":" + ignorednamecomp)) {
                            pb.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), enroll.getText().toString() + " Already Exists", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    String sem=getAttendance.sem.getSelectedItem().toString();
                    String dept=getAttendance.dept.getSelectedItem().toString();
                    String div=getAttendance.division.getSelectedItem().toString();
                    System.out.println(""+sem+""+dept+""+div);
                    FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString()+getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString()+getAttendance.division.getSelectedItem().toString()).child(getbatch).child("LIST").child(enroll.getText().toString()).setValue(name.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            pb.setVisibility(View.GONE);
                            Toast.makeText(MainActivity2.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                            enroll.setText("");
                            name.setText("");
                            System.out.println("GET BATCH="+getbatch);
                            b1.removeAll(b1);
                            b2.removeAll(b2);
                            b3.removeAll(b3);
                            vals.removeAll(vals);
                            ignorednamedatabase="";
                            b1add();
                            b2add();
                            b3add();
                            explv.setAdapter(listAdapter);
                        }
                    });


                }
            });
        }
        else
        {
            pb.setVisibility(View.GONE);
            enroll.setVisibility(View.GONE);
            name.setVisibility(View.GONE);
            add.setVisibility(View.GONE);
            batchspinner.setVisibility(View.GONE);
        }
    }


}