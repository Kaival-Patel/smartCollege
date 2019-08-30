package com.kaival.smartcollege;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.kaival.smartcollege.MainActivity2.b1;
import static com.kaival.smartcollege.MainActivity2.b2;
import static com.kaival.smartcollege.MainActivity2.b3;
import static com.kaival.smartcollege.MainActivity2.listAdapter;
import static com.kaival.smartcollege.MainActivity2.vals;

public class MyAdapter extends BaseExpandableListAdapter {
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
    String div=getAttendance.division.getSelectedItem().toString();

    public MyAdapter(Context context, List<String> batchlist, Map<String, ArrayList<String>> studlist,Button submit) {
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
        txtparent.setTypeface(productsans);
        txtparent.setText(batchlist);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        studlist = (String) getChild(groupPosition, childPosition);
        child2=studlist;
        MainActivity2.pb.setVisibility(View.VISIBLE);
         final Toast makepresent=Toast.makeText(context,"",Toast.LENGTH_SHORT);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.childview, null);
        }
        final CheckBox txtchild=(CheckBox)convertView.findViewById(R.id.childtext);
        System.out.println("TXTXCHILD:"+txtchild.getText().toString());
        System.out.println("SXSXSTUDLIST:"+studlist);
        Typeface productsans = Typeface.createFromAsset(context.getAssets(), "Product Sans Regular.ttf");
        Typeface oxygen= Typeface.createFromAsset(context.getAssets(), "Oxygen.otf");
        txtchild.setTypeface(oxygen);
        txtchild.setText(studlist);
        MainActivity2.pb.setVisibility(View.GONE);
        txtchild.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    values.add(txtchild.getText().toString());
                    MainActivity2.deletebtn.setVisibility(View.VISIBLE);
                }
                if(!isChecked)
                {
                    values.remove(txtchild.getText().toString());

                }
            }
        });
        MainActivity2.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //b1 batch
                if (values.size()>0) {
                    AlertDialog ad=new AlertDialog.Builder(context).setCancelable(false).setMessage("Are you sure you want to Delete Records?").setTitle("Delete Record!").setNegativeButton("No",null).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int i = 0; i < b1.size(); i++) {
                        for (int j = 0; j < values.size(); j++) {
                            if (b1.get(i).equals(values.get(j))) {
                                System.out.println("At b1's:" + b1.get(i) + " is " + values.get(j));
                                //delete Logic
                                String sem=getAttendance.sem.getSelectedItem().toString();
                                String dept=getAttendance.dept.getSelectedItem().toString();
                                String div=getAttendance.division.getSelectedItem().toString();
                                System.out.println(""+sem+""+dept+""+div);
                                FirebaseDatabase.getInstance().getReference().child("Students Data").child(""+sem+""+dept+""+div).child(div+"1"+" BATCH").child("LIST").child(values.get(j).toString().replaceAll("[^0-9]", "")).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {

                                            Toast.makeText(context,"Deleted Successfully!",Toast.LENGTH_LONG).show();
                                            b1.removeAll(b1);
                                            vals.removeAll(vals);
                                            MainActivity2.explv.setAdapter(listAdapter);
                                            MainActivity2.b1add();
                                        }
                                        if(!task.isSuccessful())
                                        {
                                            Toast.makeText(context, "Deletion Failure!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                            }
                        }
                    }

                //b2 batch
                for (int i = 0; i < b2.size(); i++) {
                    for (int j = 0; j < values.size(); j++) {
                        if (b2.get(i).equals(values.get(j))) {
                            System.out.println("At b2's:" + b2.get(i) + " is " + values.get(j));
                            //delete logic
                            String sem=getAttendance.sem.getSelectedItem().toString();
                            String dept=getAttendance.dept.getSelectedItem().toString();
                            String div=getAttendance.division.getSelectedItem().toString();
                            System.out.println(""+sem+""+dept+""+div);
                            MainActivity2.pb.setVisibility(View.VISIBLE);
                            FirebaseDatabase.getInstance().getReference().child("Students Data").child(""+sem+""+dept+""+div).child(div+"2"+" BATCH").child("LIST").child(values.get(j).replaceAll("[^0-9]", "")).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(context,"Deleted Successfully!",Toast.LENGTH_LONG).show();
                                        b2.removeAll(b2);
                                        vals.removeAll(vals);
                                        MainActivity2.explv.setAdapter(listAdapter);
                                        MainActivity2.b2add();
                                        MainActivity2.pb.setVisibility(View.GONE);
                                    }
                                    if(!task.isSuccessful())
                                    {
                                        MainActivity2.pb.setVisibility(View.GONE);
                                        Toast.makeText(context, "Deletion Failure!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }
                //b3 batch

                for (int i = 0; i < b3.size(); i++) {
                    for (int j = 0; j < values.size(); j++) {
                        if (b3.get(i).equals(values.get(j))) {
                            System.out.println("At b3's:" + b3.get(i) + " is " + values.get(j));
                            //delete logic
                            String sem=getAttendance.sem.getSelectedItem().toString();
                            String dept=getAttendance.dept.getSelectedItem().toString();
                            String div=getAttendance.division.getSelectedItem().toString();
                            System.out.println(""+sem+""+dept+""+div);
                            FirebaseDatabase.getInstance().getReference().child("Students Data").child(""+sem+""+dept+""+div).child(div+"3"+" BATCH").child("LIST").child(values.get(j).toString().replaceAll("[^0-9]", "")).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(context,"Deleted Successfully!",Toast.LENGTH_LONG).show();
                                        b3.removeAll(b3);
                                        vals.removeAll(vals);
                                        MainActivity2.explv.setAdapter(listAdapter);
                                        MainActivity2.b3add();
                                        MainActivity2.pb.setVisibility(View.GONE);
                                    }
                                    if(!task.isSuccessful())
                                    {

                                        Toast.makeText(context, "Deletion Failure!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }
                        }
                    }).show();
            }
            else
                {
                    MainActivity2.pb.setVisibility(View.GONE);
                    Toast.makeText(context,"U didn't Choosed any students!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        final View finalConvertView = convertView;
        MainActivity2.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    //b1batch
                    submit.setEnabled(false);
                    if (getAttendance.slot.getSelectedItem().toString().equals("LEC 1")) {
                        //datareference to slot l1,l2;
                        String sem = getAttendance.sem.getSelectedItem().toString();
                        String dept = getAttendance.dept.getSelectedItem().toString();
                        String div = getAttendance.division.getSelectedItem().toString();
                        DatabaseReference l1b1 = FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString() + getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString() + getAttendance.division.getSelectedItem().toString()).child(div + "1" + " BATCH").child("Attendance").child(getAttendance.dateandday.getText().toString()).child("L1");
                        DatabaseReference l1b2 = FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString() + getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString() + getAttendance.division.getSelectedItem().toString()).child(div + "2" + " BATCH").child("Attendance").child(getAttendance.dateandday.getText().toString()).child("L1");
                        DatabaseReference l1b3 = FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString() + getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString() + getAttendance.division.getSelectedItem().toString()).child(div + "3" + " BATCH").child("Attendance").child(getAttendance.dateandday.getText().toString()).child("L1");
                        for (int i = 0; i < b1.size(); i++) {
                            for (int j = 0; j < values.size(); j++) {
                                if (b1.get(i).equals(values.get(j))) {
                                    System.out.println("At b1's:" + b1.get(i) + " is " + values.get(j));
                                    MainActivity2.pb.setVisibility(View.VISIBLE);
                                    l1b1.child(values.get(j).toString().replaceAll("[^0-9]", "")).setValue("P").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "1 Submitted Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                            if (!task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "1 Submission Failure!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }
                            }
                        }
                        //b2 batch
                        for (int i = 0; i < b2.size(); i++) {
                            for (int j = 0; j < values.size(); j++) {
                                if (b2.get(i).equals(values.get(j))) {
                                    System.out.println("At b2's:" + b2.get(i) + " is " + values.get(j));
                                    MainActivity2.pb.setVisibility(View.VISIBLE);
                                    l1b2.child(values.get(j).toString().replaceAll("[^0-9]", "")).setValue("P").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "2 Submitted Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                            if (!task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "2 Submission Failure!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }
                            }
                        }

                        //b3 batch
                        for (int i = 0; i < b3.size(); i++) {
                            for (int j = 0; j < values.size(); j++) {
                                if (b3.get(i).equals(values.get(j))) {
                                    System.out.println("At b3's:" + b3.get(i) + " is " + values.get(j));
                                    submit.setEnabled(false);
                                    MainActivity2.pb.setVisibility(View.VISIBLE);
                                    l1b3.child(values.get(j).toString().replaceAll("[^0-9]", "")).setValue("P").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "3 Submitted Successfully", Toast.LENGTH_SHORT).show();
                                                submit.setEnabled(true);
                                            }
                                            if (!task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "3 Submission Failure!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }
                            }
                        }

                    }
                    if (getAttendance.slot.getSelectedItem().toString().equals("LEC 2")) {
                        String sem = getAttendance.sem.getSelectedItem().toString();
                        String dept = getAttendance.dept.getSelectedItem().toString();
                        String div = getAttendance.division.getSelectedItem().toString();
                        DatabaseReference l2b1 = FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString() + getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString() + getAttendance.division.getSelectedItem().toString()).child(div + "1" + " BATCH").child("Attendance").child(getAttendance.dateandday.getText().toString()).child("L2");
                        DatabaseReference l2b2 = FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString() + getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString() + getAttendance.division.getSelectedItem().toString()).child(div + "2" + " BATCH").child("Attendance").child(getAttendance.dateandday.getText().toString()).child("L2");
                        DatabaseReference l2b3 = FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString() + getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString() + getAttendance.division.getSelectedItem().toString()).child(div + "3" + " BATCH").child("Attendance").child(getAttendance.dateandday.getText().toString()).child("L2");
                        for (int i = 0; i < b1.size(); i++) {
                            for (int j = 0; j < values.size(); j++) {
                                if (b1.get(i).equals(values.get(j))) {
                                    System.out.println("At b1's:" + b1.get(i) + " is " + values.get(j));
                                    MainActivity2.pb.setVisibility(View.VISIBLE);
                                    l2b1.child(values.get(j).toString().replaceAll("[^0-9]", "")).setValue("P").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "1 Submitted Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                            if (!task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "1 Submission Failure!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }
                            }
                        }
                        //b2 batch
                        for (int i = 0; i < b2.size(); i++) {
                            for (int j = 0; j < values.size(); j++) {
                                if (b2.get(i).equals(values.get(j))) {
                                    System.out.println("At b2's:" + b2.get(i) + " is " + values.get(j));
                                    MainActivity2.pb.setVisibility(View.VISIBLE);
                                    l2b2.child(values.get(j).toString().replaceAll("[^0-9]", "")).setValue("P").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "2 Submitted Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                            if (!task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "2 Submission Failure!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }
                            }
                        }

                        //b3 batch
                        for (int i = 0; i < b3.size(); i++) {
                            for (int j = 0; j < values.size(); j++) {
                                if (b3.get(i).equals(values.get(j))) {
                                    System.out.println("At b3's:" + b3.get(i) + " is " + values.get(j));
                                    submit.setEnabled(false);
                                    MainActivity2.pb.setVisibility(View.VISIBLE);
                                    l2b3.child(values.get(j).toString().replaceAll("[^0-9]", "")).setValue("P").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "3 Submitted Successfully", Toast.LENGTH_SHORT).show();
                                                submit.setEnabled(true);
                                            }
                                            if (!task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "3 Submission Failure!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }
                            }
                        }

                    }
                    if (getAttendance.slot.getSelectedItem().toString().equals("LEC 3")) {
                        String sem = getAttendance.sem.getSelectedItem().toString();
                        String dept = getAttendance.dept.getSelectedItem().toString();
                        String div = getAttendance.division.getSelectedItem().toString();
                        DatabaseReference l3b1 = FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString() + getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString() + getAttendance.division.getSelectedItem().toString()).child(div + "1" + " BATCH").child("Attendance").child(getAttendance.dateandday.getText().toString()).child("L3");
                        DatabaseReference l3b2 = FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString() + getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString() + getAttendance.division.getSelectedItem().toString()).child(div + "2" + " BATCH").child("Attendance").child(getAttendance.dateandday.getText().toString()).child("L3");
                        DatabaseReference l3b3 = FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString() + getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString() + getAttendance.division.getSelectedItem().toString()).child(div + "3" + " BATCH").child("Attendance").child(getAttendance.dateandday.getText().toString()).child("L3");
                        for (int i = 0; i < b1.size(); i++) {
                            for (int j = 0; j < values.size(); j++) {
                                if (b1.get(i).equals(values.get(j))) {
                                    System.out.println("At b1's:" + b1.get(i) + " is " + values.get(j));
                                    MainActivity2.pb.setVisibility(View.VISIBLE);
                                    l3b1.child(values.get(j).toString().replaceAll("[^0-9]", "")).setValue("P").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "1 Submitted Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                            if (!task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "1 Submission Failure!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }
                            }
                        }
                        //b2 batch
                        for (int i = 0; i < b2.size(); i++) {
                            for (int j = 0; j < values.size(); j++) {
                                if (b2.get(i).equals(values.get(j))) {
                                    System.out.println("At b2's:" + b2.get(i) + " is " + values.get(j));
                                    MainActivity2.pb.setVisibility(View.VISIBLE);
                                    l3b2.child(values.get(j).toString().replaceAll("[^0-9]", "")).setValue("P").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "2 Submitted Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                            if (!task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "2 Submission Failure!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }
                            }
                        }

                        //b3 batch
                        for (int i = 0; i < b3.size(); i++) {
                            for (int j = 0; j < values.size(); j++) {
                                if (b3.get(i).equals(values.get(j))) {
                                    System.out.println("At b3's:" + b3.get(i) + " is " + values.get(j));
                                    submit.setEnabled(false);
                                    MainActivity2.pb.setVisibility(View.VISIBLE);
                                    l3b3.child(values.get(j).toString().replaceAll("[^0-9]", "")).setValue("P").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "3 Submitted Successfully", Toast.LENGTH_SHORT).show();
                                                submit.setEnabled(true);
                                            }
                                            if (!task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "3 Submission Failure!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }
                            }
                        }

                    }
                    if (getAttendance.slot.getSelectedItem().toString().equals("LEC 4")) {
                        String sem = getAttendance.sem.getSelectedItem().toString();
                        String dept = getAttendance.dept.getSelectedItem().toString();
                        String div = getAttendance.division.getSelectedItem().toString();
                        DatabaseReference l4b1 = FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString() + getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString() + getAttendance.division.getSelectedItem().toString()).child(div + "1" + " BATCH").child("Attendance").child(getAttendance.dateandday.getText().toString()).child("L4");
                        DatabaseReference l4b2 = FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString() + getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString() + getAttendance.division.getSelectedItem().toString()).child(div + "2" + " BATCH").child("Attendance").child(getAttendance.dateandday.getText().toString()).child("L4");
                        DatabaseReference l4b3 = FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString() + getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString() + getAttendance.division.getSelectedItem().toString()).child(div + "3" + " BATCH").child("Attendance").child(getAttendance.dateandday.getText().toString()).child("L4");
                        for (int i = 0; i < b1.size(); i++) {
                            for (int j = 0; j < values.size(); j++) {
                                if (b1.get(i).equals(values.get(j))) {
                                    System.out.println("At b1's:" + b1.get(i) + " is " + values.get(j));
                                    MainActivity2.pb.setVisibility(View.VISIBLE);
                                    l4b1.child(values.get(j).toString().replaceAll("[^0-9]", "")).setValue("P").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "1 Submitted Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                            if (!task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "1 Submission Failure!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }
                            }
                        }
                        //b2 batch
                        for (int i = 0; i < b2.size(); i++) {
                            for (int j = 0; j < values.size(); j++) {
                                if (b2.get(i).equals(values.get(j))) {
                                    System.out.println("At b2's:" + b2.get(i) + " is " + values.get(j));
                                    MainActivity2.pb.setVisibility(View.VISIBLE);
                                    l4b2.child(values.get(j).toString().replaceAll("[^0-9]", "")).setValue("P").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "2 Submitted Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                            if (!task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "2 Submission Failure!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }
                            }
                        }

                        //b3 batch
                        for (int i = 0; i < b3.size(); i++) {
                            for (int j = 0; j < values.size(); j++) {
                                if (b3.get(i).equals(values.get(j))) {
                                    System.out.println("At b3's:" + b3.get(i) + " is " + values.get(j));
                                    submit.setEnabled(false);
                                    MainActivity2.pb.setVisibility(View.VISIBLE);
                                    l4b3.child(values.get(j).toString().replaceAll("[^0-9]", "")).setValue("P").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "3 Submitted Successfully", Toast.LENGTH_SHORT).show();
                                                submit.setEnabled(true);
                                            }
                                            if (!task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "3 Submission Failure!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }
                            }
                        }

                    }
                    if (getAttendance.slot.getSelectedItem().toString().equals("LEC 5")) {
                        String sem = getAttendance.sem.getSelectedItem().toString();
                        String dept = getAttendance.dept.getSelectedItem().toString();
                        String div = getAttendance.division.getSelectedItem().toString();
                        DatabaseReference l5b1 = FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString() + getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString() + getAttendance.division.getSelectedItem().toString()).child(div + "1" + " BATCH").child("Attendance").child(getAttendance.dateandday.getText().toString()).child("L5");
                        DatabaseReference l5b2 = FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString() + getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString() + getAttendance.division.getSelectedItem().toString()).child(div + "2" + " BATCH").child("Attendance").child(getAttendance.dateandday.getText().toString()).child("L5");
                        DatabaseReference l5b3 = FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString() + getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString() + getAttendance.division.getSelectedItem().toString()).child(div + "3" + " BATCH").child("Attendance").child(getAttendance.dateandday.getText().toString()).child("L5");
                        for (int i = 0; i < b1.size(); i++) {
                            for (int j = 0; j < values.size(); j++) {
                                if (b1.get(i).equals(values.get(j))) {
                                    System.out.println("At b1's:" + b1.get(i) + " is " + values.get(j));
                                    MainActivity2.pb.setVisibility(View.VISIBLE);
                                    l5b1.child(values.get(j).toString().replaceAll("[^0-9]", "")).setValue("P").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "1 Submitted Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                            if (!task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "1 Submission Failure!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }
                            }
                        }
                        //b2 batch
                        for (int i = 0; i < b2.size(); i++) {
                            for (int j = 0; j < values.size(); j++) {
                                if (b2.get(i).equals(values.get(j))) {
                                    System.out.println("At b2's:" + b2.get(i) + " is " + values.get(j));
                                    MainActivity2.pb.setVisibility(View.VISIBLE);
                                    l5b2.child(values.get(j).toString().replaceAll("[^0-9]", "")).setValue("P").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "2 Submitted Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                            if (!task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "2 Submission Failure!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }
                            }
                        }

                        //b3 batch
                        for (int i = 0; i < b3.size(); i++) {
                            for (int j = 0; j < values.size(); j++) {
                                if (b3.get(i).equals(values.get(j))) {
                                    System.out.println("At b3's:" + b3.get(i) + " is " + values.get(j));
                                    submit.setEnabled(false);
                                    MainActivity2.pb.setVisibility(View.VISIBLE);
                                    l5b3.child(values.get(j).toString().replaceAll("[^0-9]", "")).setValue("P").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "3 Submitted Successfully", Toast.LENGTH_SHORT).show();
                                                submit.setEnabled(true);
                                            }
                                            if (!task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "3 Submission Failure!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }
                            }
                        }

                    }
                    if (getAttendance.slot.getSelectedItem().toString().equals("LEC 6")) {
                        String sem = getAttendance.sem.getSelectedItem().toString();
                        String dept = getAttendance.dept.getSelectedItem().toString();
                        String div = getAttendance.division.getSelectedItem().toString();
                        DatabaseReference l6b1 = FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString() + getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString() + getAttendance.division.getSelectedItem().toString()).child(div + "1" + " BATCH").child("Attendance").child(getAttendance.dateandday.getText().toString()).child("L6");
                        DatabaseReference l6b2 = FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString() + getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString() + getAttendance.division.getSelectedItem().toString()).child(div + "2" + " BATCH").child("Attendance").child(getAttendance.dateandday.getText().toString()).child("L6");
                        DatabaseReference l6b3 = FirebaseDatabase.getInstance().getReference().child("Students Data").child(getAttendance.dept.getSelectedItem().toString() + getAttendance.styear.getSelectedItem().toString()).child(getAttendance.dept.getSelectedItem().toString() + getAttendance.division.getSelectedItem().toString()).child(div + "3" + " BATCH").child("Attendance").child(getAttendance.dateandday.getText().toString()).child("L6");
                        for (int i = 0; i < b1.size(); i++) {
                            for (int j = 0; j < values.size(); j++) {
                                if (b1.get(i).equals(values.get(j))) {
                                    System.out.println("At b1's:" + b1.get(i) + " is " + values.get(j));
                                    MainActivity2.pb.setVisibility(View.VISIBLE);
                                    l6b1.child(values.get(j).toString().replaceAll("[^0-9]", "")).setValue("P").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "1 Submitted Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                            if (!task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "1 Submission Failure!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }
                            }
                        }
                        //b2 batch
                        for (int i = 0; i < b2.size(); i++) {
                            for (int j = 0; j < values.size(); j++) {
                                if (b2.get(i).equals(values.get(j))) {
                                    System.out.println("At b2's:" + b2.get(i) + " is " + values.get(j));
                                    MainActivity2.pb.setVisibility(View.VISIBLE);
                                    l6b2.child(values.get(j).toString().replaceAll("[^0-9]", "")).setValue("P").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "2 Submitted Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                            if (!task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "2 Submission Failure!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }
                            }
                        }

                        //b3 batch
                        for (int i = 0; i < b3.size(); i++) {
                            for (int j = 0; j < values.size(); j++) {
                                if (b3.get(i).equals(values.get(j))) {
                                    System.out.println("At b3's:" + b3.get(i) + " is " + values.get(j));
                                    submit.setEnabled(false);
                                    MainActivity2.pb.setVisibility(View.VISIBLE);
                                    l6b3.child(values.get(j).toString().replaceAll("[^0-9]", "")).setValue("P").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "3 Submitted Successfully", Toast.LENGTH_SHORT).show();
                                                submit.setEnabled(true);
                                            }
                                            if (!task.isSuccessful()) {
                                                MainActivity2.pb.setVisibility(View.GONE);
                                                submit.setEnabled(true);
                                                String div = getAttendance.division.getSelectedItem().toString();
                                                Toast.makeText(context, div + "3 Submission Failure!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }
                            }
                        }

                    }
                }
            }});
                        
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
