package com.kaival.smartcollege;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static android.view.View.GONE;

public class profileactivity extends AppCompatActivity {
    private static final int CHOOSE_IMG = 101;
    EditText profilename;
    LinearLayout mainlinearlayout;
    String profileurl;
    ImageView mydp;
    Button upload,editbtn;
    TextView textView;
    ArrayList<String> batches=new ArrayList<>();
    Button savebtn;
    String admissionyearval=null;
    String deptval=null;
    String divval=null;
    String batchval=null;
    Uri uriprofile;
    String fbatch,fdept,fdiv,fyear,fenrollment;
    boolean profilenameset=false,profilephotoset=false;
    TextView profiletext,emailtext;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    ProgressBar pb,bottombar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileactivity);
        this.getSupportActionBar().setTitle("Profile");
        profilename=findViewById(R.id.profilename);
        pb=findViewById(R.id.pbar);
        mydp=findViewById(R.id.profileimage);
        upload=findViewById(R.id.updatedp);
        emailtext=findViewById(R.id.emailverifytext);
        mainlinearlayout=findViewById(R.id.mainLinear);
        bottombar=findViewById(R.id.progbar);
        String type=getIntent().getStringExtra("type");
        if(type.equals("student"))
        {
            bottombar.setVisibility(View.VISIBLE);
            setdata();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setViews();
                }
            },2000);

        }
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mydp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagechooser();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadmydetails();
            }
        });
        loaduserinfo();
    }

    private void setdata() {
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        FirebaseAuth mauth=FirebaseAuth.getInstance();
        FirebaseUser user=mauth.getCurrentUser();
        db.getReference().child("Students").child(""+user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    if(ds.getKey().equals("Batch"))
                    {
                        fbatch=ds.getValue().toString();
                        System.out.println("FBATCH:"+fbatch);
                    }
                    else if(ds.getKey().equals("Department"))
                    {
                        fdept=ds.getValue().toString();
                        System.out.println("FDEPT"+fdept);
                    }
                    else if(ds.getKey().equals("Division"))
                    {
                        fdiv=ds.getValue().toString();
                        System.out.println("FDIV:"+fdiv);
                    }
                    else if(ds.getKey().equals("Year"))
                    {
                        fyear=ds.getValue().toString();
                        System.out.println("FYEAR:"+fyear);
                    }
                    else if(ds.getKey().equals("senroll"))
                    {
                        fenrollment=ds.getValue().toString();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        bottombar.setVisibility(GONE);




    }
    private void setViews()
    {
        bottombar.setVisibility(View.VISIBLE);
        HashMap<String,String> arr=new HashMap<>();
        arr.put("Enrollment",fenrollment);
        arr.put("Batch",fbatch);
        arr.put("Department",fdept+fdiv);
        arr.put("Admission Year",fyear);
        textView=new TextView(this);
        textView.setTextSize(30);
        textView.setText("Student DETAILS");
        textView.setTextColor(Color.parseColor("#22517a"));
        mainlinearlayout.addView(textView);

        textView=new TextView(this);
        textView.setTextSize(20);
        textView.setText(arr.get("Enrollment"));
        textView.setTextColor(Color.parseColor("#3e0f66"));
        mainlinearlayout.addView(textView);

        textView=new TextView(this);
        textView.setTextSize(17);
        textView.setText(arr.get("Batch"));
        textView.setTextColor(Color.parseColor("#a685c1"));
        mainlinearlayout.addView(textView);

        textView=new TextView(this);
        textView.setTextSize(17);
        textView.setText(arr.get("Department"));
        textView.setTextColor(Color.parseColor("#a685c1"));
        mainlinearlayout.addView(textView);

        textView=new TextView(this);
        textView.setTextSize(17);
        textView.setText(arr.get("Admission Year"));
        textView.setTextColor(Color.parseColor("#a685c1"));
        mainlinearlayout.addView(textView);

        editbtn=new Button(this);
        mainlinearlayout.addView(editbtn);
        editbtn.setText("Edit My Data");
        editbtn.setTextSize(20);
        editbtn.setTextColor(Color.parseColor("#FFFFFF"));
        editbtn.setBackgroundColor(Color.parseColor("#8807f2"));
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
        bottombar.setVisibility(GONE);
    }

    public void showAlertDialog()
    {
        Spinner admissionyear;
        Spinner deptspinner;
        final Spinner batchspinner;
        Spinner divspinner;
        AlertDialog.Builder ad=new AlertDialog.Builder(profileactivity.this);
        View v= LayoutInflater.from(profileactivity.this).inflate(R.layout.studentloggedincustom_alert,null);
        ad.setMessage("Fill Info").setView(v).setCancelable(false);
        final AlertDialog dialog=ad.create();
        dialog.setView(v);
        batchspinner=v.findViewById(R.id.batchpicker);
        batchspinner.setEnabled(false);
        ArrayList<String> years=new ArrayList<>();
        years.add("SELECT");
        for(int i=2015;i<2030;i++)
        {
            years.add(""+i);
        }
        ArrayList<String> depts=new ArrayList<>();
        depts.add("SELECT");
        depts.add("IT");
        depts.add("COMPUTER");
        depts.add("MECHANICAL");
        depts.add("CIVIL");
        batches.add("SELECT");
        ArrayList<String> divs=new ArrayList<>();
        divs.add("SELECT");
        for(char ch='A';ch<'H';ch++)
        {
            divs.add(""+ch);
        }



        ArrayAdapter<String> spinneryearadapter =new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,years);
        ArrayAdapter<String> deptadapter =new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,depts);
        ArrayAdapter<String> batchadapater=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,batches);
        final ArrayAdapter<String> divadapater=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,divs);




        admissionyear=v.findViewById(R.id.yearpickerbtn);
        admissionyear.setAdapter(spinneryearadapter);
        deptspinner=v.findViewById(R.id.departmentpicker);
        deptspinner.setAdapter(deptadapter);
        batchspinner.setAdapter(batchadapater);
        savebtn=v.findViewById(R.id.savebtn);
        divspinner=v.findViewById(R.id.divpicker);
        divspinner.setAdapter(divadapater);

        admissionyear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                admissionyearval=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        deptspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                deptval=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        divspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                divval=parent.getItemAtPosition(position).toString();
                if(divval!=null && !divval.equals("SELECT"))
                {
                    batchspinner.setEnabled(true);
                    for(int i=1;i<4;i++)
                    {
                        batches.add(""+divval+""+i+" BATCH");
                    }
                }
                else
                {
                    batchspinner.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        batchspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                batchval=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(divval.equals("SELECT") || divval==null)
                {
                    Toast.makeText(getApplicationContext(),"Enter Division!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(admissionyearval.equals("SELECT") || admissionyearval==null)
                {
                    Toast.makeText(getApplicationContext(),"Enter Year!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(batchval.equals("SELECT") || batchval==null)
                {
                    Toast.makeText(getApplicationContext(),"Enter Batch!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(deptval.equals("SELECT") || deptval==null)
                {
                    Toast.makeText(getApplicationContext(),"Enter Department!",Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                FirebaseAuth mauth=FirebaseAuth.getInstance();
                FirebaseUser user=mauth.getCurrentUser();
                firebaseDatabase.getReference().child("Students").child(""+user.getUid().toString()).child("Division").setValue(divval);
                firebaseDatabase.getReference().child("Students").child(""+user.getUid().toString()).child("Year").setValue(admissionyearval);
                firebaseDatabase.getReference().child("Students").child(""+user.getUid().toString()).child("Department").setValue(deptval);
                firebaseDatabase.getReference().child("Students").child(""+user.getUid().toString()).child("Batch").setValue(batchval);
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Data Saved!Tap Again to Close", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()==null)
        {
            this.finish();
            startActivity(new Intent(this,whichactivity.class));
        }
    }
    private void loaduserinfo() {
        final FirebaseUser user=mAuth.getCurrentUser();
        pb.setVisibility(View.VISIBLE);
        if(user!=null) {
            if (user.getPhotoUrl() != null) {
                pb.setVisibility(View.GONE);
                Glide.with(this).load(user.getPhotoUrl().toString()).into(mydp);
                profilephotoset=true;
            }
            else if(user.getPhotoUrl()==null)
            {
                pb.setVisibility(GONE);
                Toast.makeText(getApplicationContext(),"Profile Photo Not Found!",Toast.LENGTH_SHORT).show();
            }
            if (user.getDisplayName() != null) {
                pb.setVisibility(View.GONE);
                String displayName= user.getDisplayName();
                profilename.setText(displayName);
                profilenameset=true;
            }
            if(user.isEmailVerified())
            {
                emailtext.setText("Email Verified");
                emailtext.setTextColor(Color.parseColor("#3dbc2f"));
            }
            if(!user.isEmailVerified())
            {
                emailtext.setText("Email Not Verified(Click Here to verify)");
                emailtext.setTextColor(Color.parseColor("#236cff"));
                emailtext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(getApplicationContext(),"You will receive Email in Few hours!",Toast.LENGTH_SHORT).show();
                                    emailtext.setVisibility(GONE);
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Verification Request Failed!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        }
        pb.setVisibility(View.GONE);

    }

    private void uploadmydetails() {
        if(profilename.getText().toString().isEmpty())
        {
            profilename.requestFocus();
            profilename.setError("Enter the Profile Name!");
            profilename.setBackgroundResource(R.drawable.errorred);
            return;
        }
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null)
        {
            pb.setVisibility(View.VISIBLE);
            UserProfileChangeRequest profile=new UserProfileChangeRequest.Builder().setDisplayName(profilename.getText().toString()).setPhotoUri(Uri.parse(profileurl)).build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(),"Profile Updated!",Toast.LENGTH_SHORT).show();
                        pb.setVisibility(GONE);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Profile Updation Failure!",Toast.LENGTH_SHORT).show();
                        pb.setVisibility(GONE);
                    }
                }
            });
        }
        if(user!=null && profilenameset==true && profilephotoset==false)
        {
            pb.setVisibility(View.VISIBLE);
            UserProfileChangeRequest profile=new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse((profileurl))).build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        pb.setVisibility(GONE);
                        Toast.makeText(getApplicationContext(),"Profile Updated!",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        pb.setVisibility(GONE);
                        Toast.makeText(getApplicationContext(),"Profile Updation Failure!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(user!=null && profilephotoset==true && profilenameset==false) {
            if (profilename != null) {
                pb.setVisibility(View.VISIBLE);
                UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(profilename.getText().toString()).build();
                user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            pb.setVisibility(GONE);
                            Toast.makeText(getApplicationContext(), "Profile Updated!", Toast.LENGTH_SHORT).show();
                        } else {
                            pb.setVisibility(GONE);
                            Toast.makeText(getApplicationContext(), "Profile Updation Failure!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

    }
    private void imagechooser() {
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select Profile Photo!"),CHOOSE_IMG);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CHOOSE_IMG && resultCode==RESULT_OK && data!=null&&data.getData()!=null)
        {
            uriprofile=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),uriprofile);
                mydp.setImageBitmap(bitmap);
                uploadimagetofirebase();
            } catch (IOException e)
            {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadimagetofirebase() {
        final StorageReference profileimageref = FirebaseStorage.getInstance().getReference().child("profilepics/" + System.currentTimeMillis() + ".jpg");
        if (uriprofile != null) {
            pb.setVisibility(View.VISIBLE);
            profileimageref.putFile(uriprofile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    pb.setVisibility(View.GONE);
                    profileurl = taskSnapshot.getDownloadUrl().toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pb.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
