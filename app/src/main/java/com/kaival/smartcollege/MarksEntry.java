package com.kaival.smartcollege;

        import android.accessibilityservice.AccessibilityService;
        import android.annotation.SuppressLint;
        import android.app.ActionBar;
        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.graphics.Typeface;
        import android.media.tv.TvContract;
        import android.speech.tts.TextToSpeech;
        import android.support.annotation.NonNull;
        import android.support.v4.view.ViewPager;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.text.Editable;
        import android.text.Layout;
        import android.text.TextWatcher;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.Button;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.ListView;
        import android.widget.ProgressBar;
        import android.widget.RelativeLayout;
        import android.widget.TableLayout;
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
        import java.util.Set;

public class MarksEntry extends AppCompatActivity {
    TextView enrollment,outoftv,entertv;
    String getEnroll,presentstudent;
    List<String> vals=MarksAdapter.values;
    Button skip;
    Button updateoutof;
     Button submit;
    ListView subjectlistview;
    EditText outof;
    ImageView next,prev;
    boolean allgood=false;
    ProgressBar pb;
    HashMap result=new HashMap();
    static List<String> subsmarks=new ArrayList<>();
    ViewGroup vg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marks_entry);
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setTitle("Marks Entry");
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Typeface productsansreg = Typeface.createFromAsset(getAssets(), "Product Sans Regular.ttf");
        Typeface productsansbold = Typeface.createFromAsset(getAssets(), "Product Sans Bold.ttf");
        enrollment=findViewById(R.id.enrollmenttv);
        outoftv=findViewById(R.id.outoftv);
        skip=findViewById(R.id.skipbtn);
        enrollment.setTypeface(productsansbold);
        outoftv.setTypeface(productsansreg);
        pb=findViewById(R.id.pb);
        next=findViewById(R.id.nextenroll);
        prev=findViewById(R.id.prevenroll);
        submit=findViewById(R.id.submitbtn);
        updateoutof=findViewById(R.id.updateoutofbtn);
        subjectlistview=findViewById(R.id.subjectlistview);
        entertv=findViewById(R.id.entermarkstv);
        entertv.setTypeface(productsansreg);
        subjectAdapter adapter=new subjectAdapter(this);
        subjectlistview.setAdapter(adapter);
        outof=findViewById(R.id.editoutof);
        getEnroll=MarksAdapter.values.get(0);
        presentstudent=vals.get(0);
        getEnroll.replaceAll("[^0-9]", "");
        enrollment.setText(getEnroll);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatenextstudent();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatenextstudent();
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateprevstudent();
            }
        });
        updateoutof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateoutofmarks();
            }
        });
        FirebaseDatabase.getInstance().getReference().child("Students Data").child(Midmarkssetup.semesterspin.getSelectedItem().toString()+Midmarkssetup.departmentspin.getSelectedItem()+Midmarkssetup.divisionspin.getSelectedItem()).
                child(Midmarkssetup.examtype).child("outof").setValue(""+outof.getHint().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    pb.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Out of Marks Updated Successfully!",Toast.LENGTH_LONG).show();
                }
                else
                {
                    pb.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Out of Marks Updation Failure!",Toast.LENGTH_LONG).show();
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView;
                result.remove(result);
                int editmarks;
                int outofmarks;
                EditText editText;
                for(int i=0;i<subjectlistview.getChildCount();i++)
                {
                  View view=subjectlistview.getChildAt(i);
                  editText=(EditText)view.findViewById(R.id.subjedittext);
                    textView=(TextView)view.findViewById(R.id.subjecttvpar);
                    try {
                        editmarks = Integer.parseInt(editText.getText().toString());
                        outofmarks = Integer.parseInt(editText.getHint().toString());
                        if(editmarks>outofmarks || editmarks<0 || editText.getText().toString().equals(""))
                        {
                            editText.setError("Invalid Marks");
                            allgood=false;
                            return;
                        }
                    }
                    catch(Exception e)
                    {
                        editText.setError("Invalid Marks");
                        allgood=false;
                        return;
                    }

                    allgood=true;
                    System.out.println("Edittext:"+editText.getText().toString());
                    result.put(textView.getText().toString(),editText.getText().toString());

                    }
                if(allgood==true)
                {for(int i=0;i<subjectlistview.getChildCount();i++)
                {
                    View view=subjectlistview.getChildAt(i);
                    editText=(EditText)view.findViewById(R.id.subjedittext);
                    textView=(TextView)view.findViewById(R.id.subjecttvpar);
                    editmarks=Integer.parseInt(editText.getText().toString());
                    outofmarks=Integer.parseInt(editText.getHint().toString());
                    final EditText finalEditText = editText;
                    pb.setVisibility(View.VISIBLE);
                    submit.setEnabled(false);
                    FirebaseDatabase.getInstance().getReference().child("Students Data").child(Midmarkssetup.departmentspin.getSelectedItem().toString()+Midmarkssetup.yearspin.getSelectedItem().toString()).child(Midmarkssetup.departmentspin.getSelectedItem().toString()+Midmarkssetup.divisionspin.getSelectedItem().toString()).
                            child(Midmarkssetup.examtype).child(enrollment.getText().toString().replaceAll("[^0-9]", ""))
                            .child(textView.getText().toString()).setValue(editmarks).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                pb.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Marks of "+enrollment.getText().toString().replaceAll("[^0-9]", "")+" Uploaded Successfully",Toast.LENGTH_LONG).show();
                                submit.setEnabled(true);
                                finalEditText.setText("");
                            }
                            else
                            {
                                pb.setVisibility(View.GONE);
                                submit.setEnabled(true);
                                Toast.makeText(getApplicationContext(),"Marks of "+enrollment.getText().toString().replaceAll("[^0-9]", "")+" Upload Failure",Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    }

                }
                generatenextstudent();
            }
        });

    }




    private void updateoutofmarks() {

        if (outof.getText().toString().equals("") == true) {
            Toast.makeText(this, "Enter the Out of Marks!", Toast.LENGTH_SHORT).show();
            outof.setError("Enter the Out of Marks!");
            outof.requestFocus();
            return;
        }
        AlertDialog ad=new AlertDialog.Builder(MarksEntry.this).setTitle("Update the Out Of Marks").setMessage("Updating the Out of Marks will be changed for all the student's marks you have entered till now for "+Midmarkssetup.examtype)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < subjectlistview.getChildCount(); i++) {
                            View view = subjectlistview.getChildAt(i);
                            EditText editText = (EditText) view.findViewById(R.id.subjedittext);
                            editText.setHint(outof.getText().toString());
                        }
                        outof.setHint(""+outof.getText().toString());
                        pb.setVisibility(View.VISIBLE);
                        FirebaseDatabase.getInstance().getReference().child("Students Data").child(Midmarkssetup.departmentspin.getSelectedItem().toString()+Midmarkssetup.yearspin.getSelectedItem().toString()).child(Midmarkssetup.departmentspin.getSelectedItem().toString()+Midmarkssetup.divisionspin.getSelectedItem().toString()).
                                child(Midmarkssetup.examtype).child("outof").setValue(""+outof.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    pb.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(),"Out of Marks Updated Successfully!",Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    pb.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(),"Out of Marks Updation Failure!",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                }).setNegativeButton("NO",null).show();



    }
    private void generateprevstudent() {
        System.out.println("VALUES ARE:");
        for (int i=0;i<vals.size();i++) {
            if(presentstudent.equals(vals.get(i)))
            {
                try {
                    System.out.println("previous Student is :" + vals.get(i - 1));
                    presentstudent = vals.get(i - 1);
                    break;
                }
                catch (IndexOutOfBoundsException e)
                {
                    Toast.makeText(this,"First Student (No previous Student available)!",Toast.LENGTH_SHORT).show();
                }
            }

        }
        enrollment.setText(presentstudent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void generatenextstudent()
    {
        System.out.println("VALUES ARE:");
        for (int i = 0; i < vals.size()-1; i++) {
                if(presentstudent.equals(vals.get(i)))
                {
                    System.out.println("Next Student is :"+vals.get(i+1));
                    presentstudent=vals.get(i+1);
                    break;
                }

        }
        enrollment.setText(presentstudent);
    }

    public static class subjectAdapter extends BaseAdapter {
        public List<String> sublist=new ArrayList<>();
        String subs;
        TextView subjects;
        Context context;
        CheckBox checkBox;
        static EditText subjectedit;
        public subjectAdapter(Context context)
        {
            this.context=context;
            for(int i=0;i<MidMarksAdapter.subvalues.size();i++) {
                sublist.add(MidMarksAdapter.subvalues.get(i));
            }
        }
        @Override
        public int getCount() {
            return MidMarksAdapter.subvalues.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            subs=(String)sublist.get(position);
            if(convertView==null)
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.parent_marksentry_lv, null);
            }
            Typeface productsansreg = Typeface.createFromAsset(context.getAssets(), "Product Sans Regular.ttf");
            subjects=convertView.findViewById(R.id.subjecttvpar);
            subjectedit=convertView.findViewById(R.id.subjedittext);
            subjects.setText(subs);
            subjects.setTypeface(productsansreg);




            return convertView;
        }
    }
}
