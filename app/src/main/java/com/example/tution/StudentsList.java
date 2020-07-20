package com.example.tution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.Map;

public class StudentsList extends AppCompatActivity {

    ListView listView;
    static String stdsect;

    String clcset;
    String sclset;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ArrayAdapter<String> adapter;
    ArrayList<String> stunames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView=(ListView)findViewById(R.id.stsslect);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();

        clcset=Classeslist.clssselected();
        sclset=school.getschselected();

        databaseReference=firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Students Profile").child(sclset).child(clcset);
        databaseReference.addValueEventListener(new com.google.firebase.database.ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String,student_details>> gti= new GenericTypeIndicator<Map<String,student_details>>() {
                };
                Map<String,student_details> addedstudentsnames=dataSnapshot.getValue(gti);
                for(Map.Entry<String,student_details> entry:addedstudentsnames.entrySet())
                {

                    stunames.add(entry.getKey());

                }
                adapter= new ArrayAdapter<>(StudentsList.this, android.R.layout.simple_list_item_1, stunames);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                stdsect =(String) parent.getItemAtPosition(position);
                startActivity(new Intent(StudentsList.this,StudentProfile.class));
            }
        });

    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public static String getsectstu()
    {
        return stdsect;
    }
}
