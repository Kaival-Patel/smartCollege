package com.kaival.smartcollege;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.kaival.smartcollege.getAttendance.dept;
import static com.kaival.smartcollege.getAttendance.division;
import static com.kaival.smartcollege.getAttendance.sem;
import static com.kaival.smartcollege.getbatch.explv;
import static com.kaival.smartcollege.getbatch.genbatch;
import static com.kaival.smartcollege.MainActivity2.listAdapter;
import static com.kaival.smartcollege.MainActivity2.vals;
import static com.kaival.smartcollege.getbatch.genbatch;
import static com.kaival.smartcollege.getbatch.genbatchadapter;
import static com.kaival.smartcollege.getbatch.genbatchvals;
import static com.kaival.smartcollege.getbatch.getbatchadd;
import static com.kaival.smartcollege.getbatch.pb;

public class BatchAdapter extends BaseExpandableListAdapter {
    Context context;
    String studlist,child2;
    int lecmonth,labmonth;
    int lecyear,labyear;
    List<String> batchlist;
    List<String>values=new ArrayList<String>();
    List<String>batches=new ArrayList<String>();
    Map<String,ArrayList<String>> studList;
    Button submit;
    String getbatch,flagcheck,labmonthfinal,lecmonthfinal;
    ArrayList<String> SelectedItems;
    public BatchAdapter(Context context, List<String> batchlist, Map<String, ArrayList<String>> studlist,Button submit) {
        this.context = context;
        this.batchlist = batchlist;
        this.studList = studlist;
        this.submit=submit;
    }
    @Override
    public int getGroupCount() {
        return batchlist.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return studList.get(batchlist.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return batchlist.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return studList.get(batchlist.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String batchlist=(String)getGroup(groupPosition);
        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.parentview,null);
        }
        TextView txtparent=(TextView)convertView.findViewById(R.id.parenttext);
        Typeface productsans = Typeface.createFromAsset(context.getAssets(), "Product Sans Regular.ttf");
        Typeface oxygen= Typeface.createFromAsset(context.getAssets(), "Oxygen.otf");
        txtparent.setTypeface(oxygen);
        txtparent.setText(batchlist);


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        studlist = (String) getChild(groupPosition, childPosition);
        child2=studlist;
        com.kaival.smartcollege.getbatch.pb.setVisibility(View.VISIBLE);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.childview, null);
        }
        final CheckBox txtchild=(CheckBox)convertView.findViewById(R.id.childtext);
        Typeface productsans = Typeface.createFromAsset(context.getAssets(), "Product Sans Regular.ttf");
        Typeface oxygen= Typeface.createFromAsset(context.getAssets(), "Oxygen.otf");
        txtchild.setTypeface(oxygen);
        txtchild.setText(studlist);
        com.kaival.smartcollege.getbatch.pb.setVisibility(View.GONE);


        txtchild.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    values.add(txtchild.getText().toString());
                   com.kaival.smartcollege.getbatch.deletebtn.setVisibility(View.VISIBLE);
                }
                if(!isChecked)
                {
                    values.remove(txtchild.getText().toString());

                }
            }
        });
        com.kaival.smartcollege.getbatch.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (values.size() > 0) {
                    AlertDialog ad=new AlertDialog.Builder(context).setCancelable(false).setMessage("Are you sure you want to Delete "+values+"?").setTitle("Delete Record!").setNegativeButton("No",null).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    for (int i = 0; i < genbatch.size(); i++) {
                                        for (int j = 0; j < values.size(); j++) {
                                            if (genbatch.get(i).equals(values.get(j))) {
                                                System.out.println("At genbatch's:" + genbatch.get(i) + " is " + values.get(j));
                                                //delete Logic
                                                String sem = getAttendance.sem.getSelectedItem().toString();
                                                String dept = getAttendance.dept.getSelectedItem().toString();
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                System.out.println("" + sem + "" + dept + "" + div);
                                                FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString()+getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString()+getAttendance.division.getSelectedItem().toString()).child(""+getAttendance.batch.getSelectedItem().toString()+" BATCH").child("LIST").child(values.get(j).toString().replaceAll("[^0-9]", "")).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            Toast.makeText(context,"Deleted Successfully!",Toast.LENGTH_LONG).show();
                                                            genbatch.removeAll(genbatch);
                                                            genbatchvals.removeAll(genbatchvals);
                                                            explv.setAdapter(genbatchadapter);
                                                            getbatchadd();
                                                            com.kaival.smartcollege.getbatch.pb.setVisibility(View.GONE);
                                                        }
                                                        if(!task.isSuccessful())
                                                        {

                                                            Toast.makeText(context, "Deletion Failure!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                }
                            }}}}).show();

                }
                else
                {
                    com.kaival.smartcollege.getbatch.pb.setVisibility(View.GONE);
                    Toast.makeText(context,"You didnt selected any Students!",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        com.kaival.smartcollege.getbatch.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Please Wait!",Toast.LENGTH_SHORT).show();
                AlertDialog ad=new AlertDialog.Builder(context).setTitle("Attendance").setMessage("Present Entries:\n"+values+"\n"+"\nThis cannot be undone Later!").setNegativeButton("Change",null).setPositiveButton("Mark", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(getAttendance.slot.getSelectedItem().toString().equals("LAB 1"))
                        {
                          DatabaseReference lec1= FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString()+getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString()+getAttendance.division.getSelectedItem().toString()).child(getAttendance.batch.getSelectedItem().toString()+ " BATCH").child("Attendance").child(getAttendance.dateandday.getText().toString()).child("L1");
                          DatabaseReference lec2= FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString()+getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString()+getAttendance.division.getSelectedItem().toString()).child(getAttendance.batch.getSelectedItem().toString()+ " BATCH").child("Attendance").child(getAttendance.dateandday.getText().toString()).child("L2");
                            for (int i = 0; i < genbatch.size(); i++) {
                                for (int j = 0; j < values.size(); j++) {
                                    if (genbatch.get(i).equals(values.get(j))) {
                                        System.out.println("At 's:"+getAttendance.batch.getSelectedItem().toString()+""+ genbatch.get(i) + " is " + values.get(j));
                                        getAttendance.pb.setVisibility(View.VISIBLE);
                                        lec1.child(values.get(j).toString().replaceAll("[^0-9]", "")).setValue("P").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    getAttendance.pb.setVisibility(View.GONE);
                                                    submit.setEnabled(true);
                                                    String div = getAttendance.division.getSelectedItem().toString();
                                                    Toast.makeText(context, getAttendance.batch.getSelectedItem().toString() + " Submitted Successfully", Toast.LENGTH_SHORT).show();
                                                }
                                                if (!task.isSuccessful()) {
                                                    getAttendance.pb.setVisibility(View.GONE);
                                                    submit.setEnabled(true);
                                                    String div = getAttendance.division.getSelectedItem().toString();
                                                    Toast.makeText(context, getAttendance.batch.getSelectedItem().toString() + " Submission Failure!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        lec2.child(values.get(j).toString().replaceAll("[^0-9]", "")).setValue("P").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    getAttendance.pb.setVisibility(View.GONE);
                                                    submit.setEnabled(true);
                                                    String div = getAttendance.division.getSelectedItem().toString();
                                                    Toast.makeText(context, getAttendance.batch.getSelectedItem().toString() + " Submitted Successfully", Toast.LENGTH_SHORT).show();
                                                }
                                                if (!task.isSuccessful()) {
                                                    getAttendance.pb.setVisibility(View.GONE);
                                                    submit.setEnabled(true);
                                                    String div = getAttendance.division.getSelectedItem().toString();
                                                    Toast.makeText(context, getAttendance.batch.getSelectedItem().toString() + " Submission Failure!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                                    }
                                }
                            }
                        }
                        if(getAttendance.slot.getSelectedItem().toString().equals("LAB 2"))
                        {
                            DatabaseReference lec3= FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString()+getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString()+getAttendance.division.getSelectedItem().toString()).child(getAttendance.batch.getSelectedItem().toString()+ " BATCH").child("Attendance").child(getAttendance.dateandday.getText().toString()).child("L3");
                            DatabaseReference lec4= FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString()+getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString()+getAttendance.division.getSelectedItem().toString()).child(getAttendance.batch.getSelectedItem().toString()+ " BATCH").child("Attendance").child(getAttendance.dateandday.getText().toString()).child("L4");
                            for (int i = 0; i < genbatch.size(); i++) {
                                for (int j = 0; j < values.size(); j++) {
                                    if (genbatch.get(i).equals(values.get(j))) {
                                        System.out.println("At 's:"+getAttendance.batch.getSelectedItem().toString()+""+ genbatch.get(i) + " is " + values.get(j));
                                        getAttendance.pb.setVisibility(View.VISIBLE);
                                        lec3.child(values.get(j).toString().replaceAll("[^0-9]", "")).setValue("P").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    getAttendance.pb.setVisibility(View.GONE);
                                                    submit.setEnabled(true);
                                                    String div = getAttendance.division.getSelectedItem().toString();
                                                    Toast.makeText(context, getAttendance.batch.getSelectedItem().toString() + " Submitted Successfully", Toast.LENGTH_SHORT).show();
                                                }
                                                if (!task.isSuccessful()) {
                                                    getAttendance.pb.setVisibility(View.GONE);
                                                    submit.setEnabled(true);
                                                    String div = getAttendance.division.getSelectedItem().toString();
                                                    Toast.makeText(context, getAttendance.batch.getSelectedItem().toString() + " Submission Failure!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        lec4.child(values.get(j).toString().replaceAll("[^0-9]", "")).setValue("P").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    getAttendance.pb.setVisibility(View.GONE);
                                                    submit.setEnabled(true);
                                                    String div = getAttendance.division.getSelectedItem().toString();
                                                    Toast.makeText(context, getAttendance.batch.getSelectedItem().toString() + " Submitted Successfully", Toast.LENGTH_SHORT).show();
                                                }
                                                if (!task.isSuccessful()) {
                                                    getAttendance.pb.setVisibility(View.GONE);
                                                    submit.setEnabled(true);
                                                    String div = getAttendance.division.getSelectedItem().toString();
                                                    Toast.makeText(context, getAttendance.batch.getSelectedItem().toString() + " Submission Failure!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                                    }
                                }
                            }
                        }
                        if(getAttendance.slot.getSelectedItem().toString().equals("LAB 3"))
                        {
                            DatabaseReference lec5= FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString()+getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString()+getAttendance.division.getSelectedItem().toString()).child(getAttendance.batch.getSelectedItem().toString()+ " BATCH").child("Attendance").child(getAttendance.dateandday.getText().toString()).child("L5");
                            DatabaseReference lec6= FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString()+getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString()+getAttendance.division.getSelectedItem().toString()).child(getAttendance.batch.getSelectedItem().toString()+ " BATCH").child("Attendance").child(getAttendance.dateandday.getText().toString()).child("L6");
                            for (int i = 0; i < genbatch.size(); i++) {
                                for (int j = 0; j < values.size(); j++) {
                                    if (genbatch.get(i).equals(values.get(j))) {
                                        System.out.println("At 's:"+getAttendance.batch.getSelectedItem().toString()+""+ genbatch.get(i) + " is " + values.get(j));
                                        getAttendance.pb.setVisibility(View.VISIBLE);
                                        lec5.child(values.get(j).toString().replaceAll("[^0-9]", "")).setValue("P").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    getAttendance.pb.setVisibility(View.GONE);
                                                    submit.setEnabled(true);
                                                    String div = getAttendance.division.getSelectedItem().toString();
                                                    Toast.makeText(context, getAttendance.batch.getSelectedItem().toString() + " Submitted Successfully", Toast.LENGTH_SHORT).show();
                                                }
                                                if (!task.isSuccessful()) {
                                                    getAttendance.pb.setVisibility(View.GONE);
                                                    submit.setEnabled(true);
                                                    String div = getAttendance.division.getSelectedItem().toString();
                                                    Toast.makeText(context, getAttendance.batch.getSelectedItem().toString() + " Submission Failure!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        lec6.child(values.get(j).toString().replaceAll("[^0-9]", "")).setValue("P").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    getAttendance.pb.setVisibility(View.GONE);
                                                    submit.setEnabled(true);
                                                    String div = getAttendance.division.getSelectedItem().toString();
                                                    Toast.makeText(context, getAttendance.batch.getSelectedItem().toString() + " Submitted Successfully", Toast.LENGTH_SHORT).show();
                                                }
                                                if (!task.isSuccessful()) {
                                                    getAttendance.pb.setVisibility(View.GONE);
                                                    submit.setEnabled(true);
                                                    String div = getAttendance.division.getSelectedItem().toString();
                                                    Toast.makeText(context, getAttendance.batch.getSelectedItem().toString() + " Submission Failure!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                                    }
                                }
                            }
                        }
                    }
                }).show();
            }
        });
        return convertView;
    }








    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
