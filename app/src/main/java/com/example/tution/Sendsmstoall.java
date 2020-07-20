package com.example.tution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.HashMap;
import java.util.Map;

public class Sendsmstoall extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS=0;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    String msg;

    RadioGroup rdg;
    EditText nfdays;

    Map<String,String> alldata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendsmstoall);

        rdg=(RadioGroup)findViewById(R.id.msgtype);
        nfdays=(EditText)findViewById(R.id.noofdays);
        alldata=new HashMap<String, String>();

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Students Profile");

        databaseReference.addValueEventListener(new com.google.firebase.database.ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                GenericTypeIndicator<Map<String,Map<String, Map<String,student_details>>>> gti= new GenericTypeIndicator<Map<String, Map<String, Map<String,student_details>>>>() {
                };
                Map<String,Map<String,Map<String,student_details>>> schoolnames=dataSnapshot.getValue(gti);
                try {
                    for (Map.Entry<String, Map<String, Map<String, student_details>>> entry : schoolnames.entrySet()) {
                        for (Map.Entry<String, Map<String, student_details>> cls : entry.getValue().entrySet()) {
                            for (Map.Entry<String, student_details> stunames : cls.getValue().entrySet()) {
                                student_details sd = stunames.getValue();
                                alldata.put(stunames.getKey(), sd.stuphno);

                            }

                        }
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(Sendsmstoall.this,"Students Not available Please Add Students",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Sendsmstoall.this,MainPage.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.SEND_SMS))
            {
                //if granted
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }

        String g = "";

        int s = rdg.getCheckedRadioButtonId();
        RadioButton select = (RadioButton) findViewById(s);
        if(s!=-1)
        {
            g=select.getText().toString();
        }
        if(g.equals("Fees"))
        {
            msg="Dear Sir/Madam\n\tKindly requesting to pay your ward tution fee";
        }
        else
        {
            nfdays.setVisibility(View.VISIBLE);
            msg="Dear Sir/Madam\n\tKindly informing no tution for"+nfdays.getText().toString().trim()+"days";
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onRequestPermissionsResult(int requestCode,String Permissions[],int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_SEND_SMS:
            {
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this,"Thanks for permission",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this,"you Regected permission",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void sendsms()
    {
        SmsManager smsManager=SmsManager.getDefault();
        for(Map.Entry<String,String> studata:alldata.entrySet())
        {
            smsManager.sendTextMessage(studata.getValue(),null,msg,null,null);
        }
        Toast.makeText(this,"Job Done!",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,MainPage.class));
    }

    public void sendtoall(View view) {


        sendsms();
    }
}
