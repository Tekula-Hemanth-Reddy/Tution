package com.example.tution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FeesStatus extends AppCompatActivity {


    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS=0;


    Switch[] aSwitch=new Switch[12];
    TextView[] textView=new TextView[12];



    feemonths fe = new feemonths();


    public static String mntnam;

    String sts;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fees_status);
        allign();

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



        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Students Fee").child(school.getschselected()).child(Classeslist.clssselected()).child(StudentsList.getsectstu());

        databaseReference.addValueEventListener(new com.google.firebase.database.ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fe=(feemonths)dataSnapshot.getValue(feemonths.class);
                setvalues();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        aSwitch[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    textView[0].setText("Paid");
                    fe.January="Paid";
                    mntnam="January";
                }
                else
                {
                    mntnam="";
                }
            }
        });


        aSwitch[1].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    textView[1].setText("Paid");
                    fe.February="Paid";
                    mntnam="February";
                }
                else
                {
                    mntnam="";
                }
            }
        });

        aSwitch[2].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    textView[2].setText("Paid");
                    fe.March="Paid";
                    mntnam="March";
                }
                else
                {
                    mntnam="";
                }
            }
        });

        aSwitch[3].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    textView[3].setText("Paid");
                    fe.April="Paid";
                    mntnam="April";
                }
                else
                {
                    mntnam="";
                }
            }
        });

        aSwitch[4].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    textView[4].setText("Paid");
                    fe.May="Paid";
                    mntnam="May";
                }
                else
                {
                    mntnam="";
                }
            }
        });

        aSwitch[5].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    textView[5].setText("Paid");
                    fe.June="Paid";
                    mntnam="June";
                }
                else
                {
                    mntnam="";
                }
            }
        });

        aSwitch[6].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    textView[6].setText("Paid");
                    fe.July="Paid";
                    mntnam="July";
                }
                else
                {
                    mntnam="";
                }
            }
        });

        aSwitch[7].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    textView[7].setText("Paid");
                    fe.August="Paid";
                    mntnam="August";
                }
                else
                {
                    mntnam="";
                }
            }
        });

        aSwitch[8].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    textView[8].setText("Paid");
                    fe.September="Paid";
                    mntnam="September";
                }
                else
                {
                    mntnam="";
                }
            }
        });

        aSwitch[9].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    textView[9].setText("Paid");
                    fe.October="Paid";
                    mntnam="October";
                }
                else
                {
                    mntnam="";
                }
            }
        });

        aSwitch[10].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    textView[10].setText("Paid");
                    fe.November="Paid";
                    mntnam="November";
                }
                else
                {
                    mntnam="";
                }
            }
        });

        aSwitch[11].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    textView[11].setText("Paid");
                    fe.December="Paid";
                    mntnam="December";
                }
                else
                {
                    mntnam="";
                }
            }
        });





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
    private void setvalues() {

        if(fe.January.equals("Paid"))
        {
            textView[0].setText(fe.January);
            aSwitch[0].setChecked(true);


        }
        if(fe.February.equals("Paid"))
        {
            textView[1].setText(fe.February);
            aSwitch[1].setChecked(true);

        }
        if(fe.March.equals("Paid"))
        {
            textView[2].setText(fe.March);
            aSwitch[2].setChecked(true);

        }
        if(fe.April.equals("Paid"))
        {
            textView[3].setText(fe.April);
            aSwitch[3].setChecked(true);

        }
        if(fe.May.equals("Paid"))
        {
            textView[4].setText(fe.May);
            aSwitch[4].setChecked(true);

        }
        if(fe.June.equals("Paid"))
        {
            textView[5].setText(fe.June);
            aSwitch[5].setChecked(true);

        }
        if(fe.July.equals("Paid"))
        {
            textView[6].setText(fe.July);
            aSwitch[6].setChecked(true);

        }
        if(fe.August.equals("Paid"))
        {
            textView[7].setText(fe.August);
            aSwitch[7].setChecked(true);

        }
        if(fe.September.equals("Paid"))
        {
            textView[8].setText(fe.September);
            aSwitch[8].setChecked(true);

        }
        if(fe.October.equals("Paid"))
        {
            textView[9].setText(fe.October);
            aSwitch[9].setChecked(true);

        }
        if(fe.November.equals("Paid"))
        {
            textView[10].setText(fe.November);
            aSwitch[10].setChecked(true);

        }
        if(fe.December.equals("Paid"))
        {
            textView[11].setText(fe.December);
            aSwitch[11].setChecked(true);

        }

    }

    private void allign() {

        aSwitch[0]=(Switch)findViewById(R.id.switchjan);
        aSwitch[1]=(Switch)findViewById(R.id.switchfeb);
        aSwitch[2]=(Switch)findViewById(R.id.switchmar);
        aSwitch[3]=(Switch)findViewById(R.id.switchapi);
        aSwitch[4]=(Switch)findViewById(R.id.switchmay);
        aSwitch[5]=(Switch)findViewById(R.id.switchjun);
        aSwitch[6]=(Switch)findViewById(R.id.switchjul);
        aSwitch[7]=(Switch)findViewById(R.id.switchaug);
        aSwitch[8]=(Switch)findViewById(R.id.switchsep);
        aSwitch[9]=(Switch)findViewById(R.id.switchoct);
        aSwitch[10]=(Switch)findViewById(R.id.switchnov);
        aSwitch[11]=(Switch)findViewById(R.id.switchdec);

        textView[0]=(TextView)findViewById(R.id.jansts);
        textView[1]=(TextView)findViewById(R.id.febsts);
        textView[2]=(TextView)findViewById(R.id.marsts);
        textView[3]=(TextView)findViewById(R.id.apists);
        textView[4]=(TextView)findViewById(R.id.maysts);
        textView[5]=(TextView)findViewById(R.id.junsts);
        textView[6]=(TextView)findViewById(R.id.julsts);
        textView[7]=(TextView)findViewById(R.id.augsts);
        textView[8]=(TextView)findViewById(R.id.sepsts);
        textView[9]=(TextView)findViewById(R.id.octsts);
        textView[10]=(TextView)findViewById(R.id.novsts);
        textView[11]=(TextView)findViewById(R.id.decsts);
    }


    public void savestatus(View view) {
        databaseReference=firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Students Fee").child(school.getschselected()).child(Classeslist.clssselected()).child(StudentsList.getsectstu());
        databaseReference.setValue(fe).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(FeesStatus.this,"Status Changed",Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(FeesStatus.this,StudentProfile.class));
                    sendtoall();
                }

            }
        });
    }

    public void onRequestPermissionsResult(int requestCode,String Permissions[],int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_SEND_SMS:
            {
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this,"Thanks for permission",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this,"you Regected permission",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void sendtoall() {

        student_details stud=StudentProfile.getstudet();
        SmsManager smsManager=SmsManager.getDefault();

        String msg="Dear Sir/Madam \n\tYour ward tution fee of "+mntnam+"is Paid";

        smsManager.sendTextMessage(stud.stuphno,null,msg,null,null);
        Toast.makeText(this,"Job Done!",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,StudentProfile.class));
    }
}
