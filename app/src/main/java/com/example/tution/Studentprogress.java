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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.Map;

public class Studentprogress extends AppCompatActivity {

    ListView listView;
    static String stdatptst;

    String clcset;
    String sclset;
    String stdset;



    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ArrayAdapter<String> adapter;
    ArrayList<String> tstatp = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentprogress);

        listView=(ListView)findViewById(R.id.atemptedtest);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();

        clcset=Classeslist.clssselected();
        sclset=school.getschselected();
        stdset=StudentsList.getsectstu();

        databaseReference=firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Students Marks").child(sclset).child(clcset).child(stdset);
        databaseReference.addValueEventListener(new com.google.firebase.database.ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String,Marks>> gti= new GenericTypeIndicator<Map<String,Marks>>() {
                };
                Map<String,Marks> testatemptedbystu=dataSnapshot.getValue(gti);
                try{
                for(Map.Entry<String,Marks> entry:testatemptedbystu.entrySet())
                {

                    tstatp.add(entry.getKey());

                }
                }
                catch (Exception e)
                {
                    Toast.makeText(Studentprogress.this,"Marks Not entered, Come back after entering marks",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Studentprogress.this,StudentProfile.class));
                }
                adapter= new ArrayAdapter<>(Studentprogress.this, android.R.layout.simple_list_item_1, tstatp);
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
                stdatptst =(String) parent.getItemAtPosition(position);
                startActivity(new Intent(Studentprogress.this,viewstudentsmarks.class));
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
    public static String gettestsected()
    {
        return stdatptst;
    }

}
