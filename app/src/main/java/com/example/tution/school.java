package com.example.tution;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.Map;

public class school extends AppCompatActivity {

    ListView listView;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference studentsdata;
    ArrayAdapter<String> adapter;
    ArrayList<String> schnames = new ArrayList<String>();

    static String shlselected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school);

        listView=(ListView)findViewById(R.id.schoolslist);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        studentsdata=firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Students Profile");
        studentsdata.addValueEventListener(new com.google.firebase.database.ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                GenericTypeIndicator<Map<String,Map<String,Map<String,student_details>>>> gti= new GenericTypeIndicator<Map<String, Map<String, Map<String,student_details>>>>() {
                };
                Map<String,Map<String,Map<String,student_details>>> schoolnames=dataSnapshot.getValue(gti);
                try {
                    for (Map.Entry<String, Map<String, Map<String, student_details>>> entry : schoolnames.entrySet()) {

                        schnames.add(entry.getKey());

                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(school.this,"Students not available please Add Students",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(school.this,MainPage.class));
                }
             adapter= new ArrayAdapter<>(school.this, android.R.layout.simple_list_item_1, schnames);
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
                shlselected =(String) parent.getItemAtPosition(position);
                startActivity(new Intent(school.this,Classeslist.class));
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

    public static String getschselected()
    {
        return shlselected;
    }
}
