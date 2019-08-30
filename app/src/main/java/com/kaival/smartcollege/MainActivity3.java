package com.kaival.smartcollege;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity3 extends AppCompatActivity {
ExpandableListView expandableListView;
static Button fillmarksbtn;
static ProgressBar pb;
ExpandableListAdapter expandableListAdapter;
List<String> batchlist=new ArrayList<>();
Map<String,ArrayList<String>> studlist=new HashMap<>();
static ArrayList<String> b1=new ArrayList<String>();
public static ArrayList<String> vals =new ArrayList<String>();
ArrayList<String> SelectedItems;
static ArrayList<String> b2=new ArrayList<String>();
static ArrayList<String> b3=new ArrayList<String>();
String sem,dept,div;
TextView heading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        expandableListView=findViewById(R.id.expanded_lv);
        fillmarksbtn=findViewById(R.id.fillmarksbtn);
        pb=findViewById(R.id.pb);
        heading=findViewById(R.id.headingtv);

        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setTitle("Choose the Students");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        Typeface productsans = Typeface.createFromAsset(getAssets(), "Product Sans Bold.ttf");
        heading.setTypeface(productsans);
        sem=Midmarkssetup.semesterspin.getSelectedItem().toString();
        dept=Midmarkssetup.departmentspin.getSelectedItem().toString();
        div=Midmarkssetup.divisionspin.getSelectedItem().toString();
        FirebaseDatabase.getInstance().getReference().child("Students Data").child(Midmarkssetup.departmentspin.getSelectedItem().toString()+Midmarkssetup.yearspin.getSelectedItem().toString()).child(Midmarkssetup.departmentspin.getSelectedItem().toString()+Midmarkssetup.divisionspin.getSelectedItem().toString()).child(div+"1"+" BATCH").child("LIST").addValueEventListener(new ValueEventListener() {
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

        FirebaseDatabase.getInstance().getReference().child("Students Data").child(Midmarkssetup.departmentspin.getSelectedItem().toString()+Midmarkssetup.yearspin.getSelectedItem().toString()).child(Midmarkssetup.departmentspin.getSelectedItem().toString()+Midmarkssetup.divisionspin.getSelectedItem().toString()).child(div+"2"+" BATCH").child("LIST").addValueEventListener(new ValueEventListener() {
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

        FirebaseDatabase.getInstance().getReference().child("Students Data").child(Midmarkssetup.departmentspin.getSelectedItem().toString()+Midmarkssetup.yearspin.getSelectedItem().toString()).child(Midmarkssetup.departmentspin.getSelectedItem().toString()+Midmarkssetup.divisionspin.getSelectedItem().toString()).child(div+"3"+" BATCH").child("LIST").addValueEventListener(new ValueEventListener() {
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
        System.out.println("Values Fetched:"+vals);
        filldata();
        expandableListAdapter=new MarksAdapter(this,batchlist,studlist,fillmarksbtn);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });
        expandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = ((TextView)view).getText().toString();
                if(SelectedItems.contains(selectedItem))
                    SelectedItems.remove(selectedItem); //remove deselected item from the list of selected items
                else
                    SelectedItems.add(selectedItem); //add selected item to the list of selected items
            }
        });
    }

    private void filldata() {
        String div=Midmarkssetup.divisionspin.getSelectedItem().toString();
        batchlist.add(div+"1 Batch");
        batchlist.add(div+"2 Batch");
        batchlist.add(div+"3 Batch");

        studlist.put(batchlist.get(0),b1);
        studlist.put(batchlist.get(1),b2);
        studlist.put(batchlist.get(2),b3);
        pb.setVisibility(View.GONE);
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
