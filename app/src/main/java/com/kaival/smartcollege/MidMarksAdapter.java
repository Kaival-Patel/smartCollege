package com.kaival.smartcollege;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MidMarksAdapter extends BaseExpandableListAdapter {
    Context context;
    String subList,child2;

    static List<String>subvalues=new ArrayList<String>();
    Map<String,ArrayList<String>> sublist;
    List<String> Subjecttitle=new ArrayList<>();
    public MidMarksAdapter(Context context, List<String> Subjecttitle, Map<String, ArrayList<String>> sublist)
    {
        this.context = context;
        this.Subjecttitle=Subjecttitle;
        this.sublist = sublist;
    }
    @Override
    public int getGroupCount() {
        return Subjecttitle.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return sublist.get(Subjecttitle.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return Subjecttitle.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return sublist.get(Subjecttitle.get(groupPosition)).get(childPosition);
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
        String Subjecttitle=(String)getGroup(groupPosition);
        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.subjectparentview,null);
        }
        TextView txtparent=(TextView)convertView.findViewById(R.id.subtv);
        txtparent.setText(Subjecttitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        subList = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.subjectchildview, null);
        }
        final CheckBox txtchild=(CheckBox)convertView.findViewById(R.id.childtext1);
        txtchild.setText(subList);
        System.out.println("TXTCHILD:"+txtchild.getText());
        subvalues.removeAll(subvalues);
        txtchild.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    subvalues.add(txtchild.getText().toString());

                }
                if(!isChecked)
                {
                    subvalues.remove(txtchild.getText().toString());

                }
            }
        });
        SubjectSelector.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,MainActivity3.class);
                context.startActivity(i);

            }
        });
        SubjectSelector.addsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SubjectSelector.subcode.getVisibility()==View.GONE&&
                SubjectSelector.subname.getVisibility()==View.GONE&&
                SubjectSelector.submitsubs.getVisibility()==View.GONE) {
                    SubjectSelector.subcode.setVisibility(View.VISIBLE);
                    SubjectSelector.subname.setVisibility(View.VISIBLE);
                    SubjectSelector.submitsubs.setVisibility(View.VISIBLE);
                }
                else
                {
                    SubjectSelector.subcode.setVisibility(View.GONE);
                    SubjectSelector.subname.setVisibility(View.GONE);
                    SubjectSelector.submitsubs.setVisibility(View.GONE);
                }

            }
        });
        SubjectSelector.deletesub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(subvalues.size()>0)
                    {
                        AlertDialog ad=new AlertDialog.Builder(context).setTitle("Delete Subject").setMessage("Are you Sure you want to delete the Subjects?")
                                .setNegativeButton("No",null).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        for(int i=0;i<subvalues.size();i++)
                                        {
                                            System.out.println(""+subvalues.get(i).replaceAll("[^a-zA-Z]", ""));
                                            FirebaseDatabase.getInstance().getReference().child("Students Data").child("SUBJECTS").child(Midmarkssetup.departmentspin.getSelectedItem().toString())
                                                    .child(Midmarkssetup.semesterspin.getSelectedItem().toString()).child(""+subvalues.get(i).replaceAll("[^a-zA-Z]", "")).removeValue().
                                                    addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                          if(task.isSuccessful())
                                                          {
                                                              Toast.makeText(context,"Deleted Successfully!",Toast.LENGTH_SHORT).show();

                                                          }
                                                          else
                                                          {
                                                              Toast.makeText(context,"Deletion failure!",Toast.LENGTH_SHORT).show();
                                                              SubjectSelector.explv.setAdapter(SubjectSelector.adapter);
                                                          }
                                                        }
                                                    });
                                        }
                                        SubjectSelector.explv.setAdapter(SubjectSelector.adapter);
                                    }
                                }).show();
                    }
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
