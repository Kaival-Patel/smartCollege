package com.kaival.smartcollege;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_view extends Fragment {
    CardView mid1card, mid2card, attendance, tt;
    ImageView ttimg;
    TextView attxt, mid1txt, mid2txt;
    int days = 0, count = 0;
    int percent=0,totallecs;
    boolean counted=false;
    ProgressBar pb;
    String enrollment;
    final int counter = 0;
    DatabaseReference dr1 = FirebaseDatabase.getInstance().getReference().child("Students Data");
    List<String> values = new ArrayList<String>();
    List<String> presententries = new ArrayList<String>();
    List<String> dates = new ArrayList<String>();
    List<String> presentkey = new ArrayList<String>();



    public fragment_view() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_view, container, false);
        mid1card = (CardView) v.findViewById(R.id.mid1card);
        mid2card = (CardView) v.findViewById(R.id.mid2card);
        attendance = (CardView) v.findViewById(R.id.attendancecard);
        tt = (CardView) v.findViewById(R.id.ttcard);
        ttimg = (ImageView) v.findViewById(R.id.imgtimetable);
        attxt = (TextView) v.findViewById(R.id.atttxt);
        mid1txt = (TextView) v.findViewById(R.id.mid1txt);
        mid2txt = (TextView) v.findViewById(R.id.mid2txt);
        pb=(ProgressBar)v.findViewById(R.id.pb);
        Intent intent=getActivity().getIntent();
        enrollment=intent.getStringExtra("Username");
        matchmyenroll();
        return v;
    }

    private void matchmyenroll() {

        pb.setVisibility(View.VISIBLE);
        System.out.println("ENROLLMENT:" + enrollment);
        synchronized (this) {
            dr1.child("5ITB").child("B1 BATCH").child("LIST").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    presentkey.removeAll(presentkey);
                    for (DataSnapshot chid : dataSnapshot.getChildren()) {
                        values.add(chid.getKey());
                    }
                    for (String str : values) {
                        if (enrollment.equals(str)) {
                            System.out.println("Matched to B1:");
                            final String batch = "B1 BATCH";
                            dr1.child("5ITB").child(batch).child("Attendance").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        dates.add(child.getKey());
                                        days = dates.size();

                                    }
                                    for (final String date : dates) {
                                        dr1.child("5ITB").child(batch).child("Attendance").child(date).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                for (DataSnapshot lecnumbers : dataSnapshot.getChildren()) {
                                                    dr1.child("5ITB").child(batch).child("Attendance").child(date).child(lecnumbers.getKey()).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                            for (DataSnapshot prnumbers : dataSnapshot.getChildren()) {
                                                                if (enrollment.equals(prnumbers.getKey())) {
                                                                    System.out.println("DETAILS ATT MATCHED");

                                                                        count++;

                                                                }
                                                            }
                                                            pb.setVisibility(View.GONE);
                                                            System.out.println("COUNT outside:" + count);
                                                            totallecs = (days) * 6;

                                                                percent = ((count) * 100) / totallecs;
                                                                attxt.setText(percent + "%");

                                                            System.out.println("Percent:" + percent + "%");
                                                            counted=true;


                                                        }


                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                    System.out.println("For date:" + date + " there are " + lecnumbers.getKey());


                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            break;
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });








        }

    }
}


