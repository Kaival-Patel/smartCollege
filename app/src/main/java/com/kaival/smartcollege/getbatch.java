package com.kaival.smartcollege;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class getbatch extends AppCompatActivity {
    static ExpandableListView explv;
    List<String> batchlist=new ArrayList<>();
    Map<String,ArrayList<String>> studlist=new HashMap<>();
    static ArrayList<String> genbatch=new ArrayList<String>();
    static ExpandableListAdapter genbatchadapter;
    static FloatingActionButton addbtn;
    static FloatingActionButton deletebtn;
    static Button add;
    static EditText enroll;
    EditText name;
    static ProgressBar pb;
    String getbatch;
    ArrayList<String> SelectedItems;
    public static Button submit;
    boolean batchfound=false;
    String ignorednamedatabase;
    String ignorednamecomp;
    static Spinner batchspinner;
    String []spinnergenbatchvals;
    public static ArrayList<String> genbatchvals =new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getbatch);
        pb=findViewById(R.id.pb);
        pb.setVisibility(View.VISIBLE);
        submit=(Button)findViewById(R.id.submitbtn);
        addbtn=findViewById(R.id.addstd);
        deletebtn=findViewById(R.id.deletestd);
        enroll=findViewById(R.id.enroll);
        name=findViewById(R.id.name);
        add=findViewById(R.id.add);
        batchspinner=findViewById(R.id.batchspin);
        spinnergenbatchvals= new String[]{"Select",""+getAttendance.batch.getSelectedItem().toString()};
        final ArrayAdapter spingenbatchvals=new ArrayAdapter(this,android.R.layout.simple_list_item_1,spinnergenbatchvals);
        //spingenbatchvals.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        batchspinner.setAdapter(spingenbatchvals);
        batchspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getbatch=spinnergenbatchvals[position];
                View v= batchspinner.getSelectedView();
                ((TextView)v).setTextSize(13);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                getbatch=null;
            }
        });
        //fetch the batch
        String sem=getAttendance.sem.getSelectedItem().toString();
        String dept=getAttendance.dept.getSelectedItem().toString();
        String div=getAttendance.division.getSelectedItem().toString();
        System.out.println(""+sem+""+dept+""+div);
        FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString()+getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString()+getAttendance.division.getSelectedItem().toString()).child(""+getAttendance.batch.getSelectedItem().toString()+" BATCH").child("LIST").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                pb.setVisibility(View.VISIBLE);
                genbatch.removeAll(genbatch);
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    genbatch.add("" + child.getKey().toString() + ":" + child.getValue().toString());
                    genbatchvals.add("" + child.getKey().toString() + ":" + child.getValue().toString());
                    batchfound=true;
                }
                if(batchfound==false)
                {
                    pb.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"No Batch Data Found in the Database!",Toast.LENGTH_SHORT).show();
                }
                if(batchfound==true)
                {
                    pb.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Loading! Please Wait!",Toast.LENGTH_SHORT).show();
                }
            }

            public void onCancelled(DatabaseError databaseError) {

            }
        });
        System.out.println("Batch List:"+genbatch);
        filldata();
        explv=(ExpandableListView)findViewById(R.id.expanded_lv);
        genbatchadapter=new BatchAdapter(this,batchlist,studlist,submit);
        explv.setAdapter(genbatchadapter);
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

    private void filldata() {

        pb.setVisibility(View.VISIBLE);
        batchlist.add(""+getAttendance.batch.getSelectedItem().toString()+" Batch");
        studlist.put(batchlist.get(0),genbatch);
        pb.setVisibility(View.GONE);
    }
    public static void getbatchadd()
    {
        FirebaseDatabase.getInstance().getReference().child("Students Data").child(""+getAttendance.sem.getSelectedItem().toString()+""+getAttendance.dept.getSelectedItem().toString()+""+getAttendance.division.getSelectedItem().toString()).child(""+getAttendance.batch.getSelectedItem().toString()+" BATCH").child("LIST").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                pb.setVisibility(View.VISIBLE);
                genbatch.removeAll(genbatch);
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    genbatch.add("" + child.getKey().toString() + ":" + child.getValue().toString());
                    genbatchvals.add("" + child.getKey().toString() + ":" + child.getValue().toString());

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
                    for (int i = 0; i < genbatchvals.size(); i++) {
                        ignorednamedatabase = genbatchvals.get(i).toLowerCase();
                        System.out.println("Values ignored:" + ignorednamedatabase);

                        ignorednamecomp = name.getText().toString().toLowerCase();
                        if (ignorednamedatabase.equals(enroll.getText().toString() + ":" + ignorednamecomp)) {
                            pb.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), enroll.getText().toString() + " Already Exists", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }




                    FirebaseDatabase.getInstance().getReference().child("Students Data").child(""+getAttendance.sem.getSelectedItem().toString()+""+getAttendance.dept.getSelectedItem().toString()+""+getAttendance.division.getSelectedItem().toString()).child(""+getAttendance.batch.getSelectedItem().toString()+" BATCH").child("LIST").child(enroll.getText().toString()).setValue(name.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            pb.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
                            enroll.setText("");
                            name.setText("");
                            System.out.println("GET BATCH="+getbatch);
                            genbatch.removeAll(genbatch);
                            genbatchvals.removeAll(genbatchvals);
                            ignorednamedatabase="";
                            getbatchadd();
                            explv.setAdapter(genbatchadapter);
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
