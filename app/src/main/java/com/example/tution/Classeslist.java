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
import java.util.List;
import java.util.Map;

public class Classeslist extends AppCompatActivity {

    ListView clclis;
    static String clcselected;
    String Schoolselected;

    List<String> clsnames;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classeslist);
        clclis=(ListView)findViewById(R.id.listclasses);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();



        ArrayList<String> al=new ArrayList<String>();
        al.add("Nursery");
        al.add("LKG");
        al.add("UKG");
        for(int i=1;i<11;i++)
        {
            al.add(""+i);
        }
        ArrayAdapter ad=new ArrayAdapter(Classeslist.this,android.R.layout.simple_list_item_1,al);
        clclis.setAdapter(ad);
        clsnames= new ArrayList<>();
        Schoolselected=school.getschselected();
        clclis.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clcselected =(String) parent.getItemAtPosition(position);
                if(containstudents())
                {
                    startActivity(new Intent(Classeslist.this,StudentsList.class));
                }

            }
        });



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private boolean containstudents() {

        databaseReference=firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Students Profile").child(Schoolselected);
        databaseReference.addValueEventListener( new com.google.firebase.database.ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String,Map<String, student_details>>> gti= new GenericTypeIndicator<Map<String, Map<String,student_details>>>() {
                };
                Map<String,Map<String,student_details>> schoolnames=dataSnapshot.getValue(gti);
                for(Map.Entry<String,Map<String,student_details>> entry:schoolnames.entrySet())
                {
                    clsnames.add(entry.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(clsnames.contains(clcselected))
        {
            return true;

        }
        else
        {
            Toast.makeText(this,"No students in this class if so click again",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public static String clssselected()
    {
        return clcselected;
    }

}
