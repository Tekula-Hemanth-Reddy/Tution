package com.example.tution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.Map;

public class DailyAttendanceview extends AppCompatActivity {

    ListView list;

    ArrayList alist=new ArrayList<String>();

    ArrayAdapter<String> adapter;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    DatabaseReference mydata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_attendanceview);


        list=(ListView)findViewById(R.id.abststulist);



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();


        mydata=firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("absentes List");
        mydata.addValueEventListener(new com.google.firebase.database.ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                GenericTypeIndicator<Map<String,atendeview>> gti=new GenericTypeIndicator<Map<String, atendeview>>() {
                };
                Map<String,atendeview> abstmaplist=dataSnapshot.getValue(gti);
                try{

                    for(Map.Entry<String,atendeview> maplist:abstmaplist.entrySet()) {

                        alist.add(maplist.getKey());

                    }
                    adapter = new ArrayAdapter<String>(DailyAttendanceview.this, android.R.layout.simple_list_item_1,alist);
                    list.setAdapter(adapter);

                }
                catch (Exception e)
                {

                    Toast.makeText(DailyAttendanceview.this,"First Take Attendance if taken No Absentes",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DailyAttendanceview.this,MainPage.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
}
