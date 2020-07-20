package com.example.tution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DailyAttendance extends AppCompatActivity {

    ArrayList<atendeview> stuatdpre=new ArrayList<atendeview>();
    ListView listView;
    customatendestuview ctmadapter;

    Button btnatdsave;

    ArrayList<String> stdalllist;
    ArrayList<atendeview> presenteslist=new ArrayList<atendeview>();
    public ArrayList<atendeview> allstudents=new ArrayList<atendeview>();

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_attendance);

        listView=(ListView)findViewById(R.id.stuatendlistview);

        btnatdsave=(Button)findViewById(R.id.saveatendance);

        btnatdsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenteslist=ctmadapter.getpresenteslist();
                allstudents.removeAll(presenteslist);

                databaseReference = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("absentes List");
                databaseReference.removeValue();

                for(atendeview atdv:allstudents) {

                    databaseReference = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("absentes List").child(atdv.atendename);
                    databaseReference.setValue(atdv).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //
                            }
                        }
                    });

                }
                Toast.makeText(DailyAttendance.this, "Absentes Updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DailyAttendance.this, MainPage.class));


            }
        });

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();

        databaseReference=firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Students Names");

        databaseReference.addValueEventListener(new com.google.firebase.database.ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String,atendeview>> gti=new GenericTypeIndicator<Map<String, atendeview>>() {
                };
                Map<String,atendeview> stuatdnames=dataSnapshot.getValue(gti);
                try {
                    for(Map.Entry<String,atendeview> atdstdntd:stuatdnames.entrySet())
                    {
                        stuatdpre.add(atdstdntd.getValue());
                    }
                    allstudents=stuatdpre;



                    ctmadapter=new customatendestuview(DailyAttendance.this,R.layout.list_item_stu_atd,stuatdpre);
                    listView.setAdapter(ctmadapter);

                }
                catch (Exception e)
                {
                    Toast.makeText(DailyAttendance.this,"Students Not available Please Add Students",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DailyAttendance.this,MainPage.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//        stuatdpre.add(new atendeview("hemanth"));
//        stuatdpre.add(new atendeview("ajith"));



//        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,allstudents);
//        listView.setAdapter(adapter);

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
