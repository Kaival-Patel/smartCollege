package com.kaival.smartcollege;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.spark.submitbutton.SubmitButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.util.Random;

public class Generate_Reports extends AppCompatActivity {
TableLayout tableLayout;
SubmitButton generate;
LinearLayout mainLayout;

OreoNotifications oreoNotifications;
NotificationManager notificationManager;
NotificationChannel notificationChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate__reports);
        generate=findViewById(R.id.generatepdf);
        tableLayout=findViewById(R.id.table);
        generate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               generatepdf();
        }
        });
        mainLayout=findViewById(R.id.mainlayout);




    }




    private void generatepdf() {
        Document doc=new Document();
        String outpath=Environment.getExternalStorageDirectory().getAbsolutePath();

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    //File write logic here
                    File folder=new File(outpath+"/SmartCollege");
                    if(!folder.exists())
                    {
                        folder.mkdir();
                        Snackbar.make(getCurrentFocus(),"Folder Created!",Snackbar.LENGTH_SHORT).show();
                    }
                    File file=new File(folder.getAbsolutePath()+"/mypdf"+System.currentTimeMillis()+".pdf");

                }
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                {
                    ActivityCompat.requestPermissions(Generate_Reports.this,new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                    Toast.makeText(Generate_Reports.this,"Please Grant us the Permission To Write!",Toast.LENGTH_SHORT).show();

                }

            }
            else
            {
                File folder=new File(getFilesDir()+"/SmartCollege");
                File file=new File(folder,"mypdf.pdf");
                if(!folder.exists())
                {
                    folder.mkdir();
                }
                Toast.makeText(getApplicationContext(),"PDF GENERATED SUCCESSFULLY!",Toast.LENGTH_SHORT).show();
            }

        }
        catch(Exception e)
        {

        }

    }


}
