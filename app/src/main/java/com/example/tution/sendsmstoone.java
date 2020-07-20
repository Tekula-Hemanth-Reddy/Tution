package com.example.tution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

public class sendsmstoone extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS=0;


    EditText txtmsg;
    TextView snd;

    student_details stud=StudentProfile.getstudet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendsmstoone);

        txtmsg=(EditText)findViewById(R.id.onetypemsg);
        snd=(TextView)findViewById(R.id.sendtomsg);
        snd.setText("SendTo : "+stud.stuphno);
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

    public void sendtoall(View view) {
        SmsManager smsManager=SmsManager.getDefault();
        smsManager.sendTextMessage(stud.stuphno,null,txtmsg.getText().toString().trim(),null,null);

//        Intent intent=new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse("smsto:"+stud.stuphno));
//        intent.putExtra("sms_body",txtmsg.getText().toString());
//        startActivity(intent);
        Toast.makeText(this,"Job Done!",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,StudentProfile.class));
    }
}
