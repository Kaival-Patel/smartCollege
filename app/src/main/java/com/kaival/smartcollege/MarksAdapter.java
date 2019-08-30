package com.kaival.smartcollege;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MarksAdapter extends BaseExpandableListAdapter {
    List<String> batchlist;
    Context context;
    Button submit;
    String studlist,child2;
    static List<String>values=new ArrayList<String>();
    List<String>numericvalues;
    Map<String,ArrayList<String>> studList;
    public MarksAdapter(Context context, List<String> batchlist, Map<String, ArrayList<String>> studlist, Button submit)
    {
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
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String batchlist=(String)getGroup(groupPosition);
        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.parentview,null);
        }
        TextView txtparent=(TextView)convertView.findViewById(R.id.parenttext);
        txtparent.setText(batchlist);


        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        studlist = (String) getChild(groupPosition, childPosition);
        child2=studlist;
        final Toast makepresent=Toast.makeText(context,"",Toast.LENGTH_SHORT);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.childview, null);
        }
        final CheckBox txtchild=(CheckBox)convertView.findViewById(R.id.childtext);
        txtchild.setText(studlist);
        values.removeAll(values);
        txtchild.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    values.add(txtchild.getText().toString());
                }
                if(!isChecked)
                {
                    values.remove(txtchild.getText().toString());

                }
            }
        });
        MainActivity3.fillmarksbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MarksAdapter.values.size()>0) {
                    for (int i=0;i<MarksAdapter.values.size();i++)
                    {
                        System.out.println(MarksAdapter.values.get(i).replaceAll("[^0-9]", ""));

                    }
                    AlertDialog alertDialog=new AlertDialog.Builder(context).setTitle("Marksfillup Students List").setMessage("Are You Sure You want to Proceed With this students only:\n"+""+MarksAdapter.values).setCancelable(false).setNegativeButton("NO",null)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(context,"Loading Modules Please Wait!",Toast.LENGTH_SHORT).show();
                                    Intent i=new Intent(context,MarksEntry.class);
                                    context.startActivity(i);
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
