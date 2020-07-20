package com.example.tution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class viewstudentsmarks extends AppCompatActivity {
    TextView testname;
    TextView t,h,e,m,p,b,s,c,g;

    Marks stumarks=new Marks();

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewstudentsmarks);

        testname=(TextView)findViewById(R.id.tviewselect);
        t=(TextView)findViewById(R.id.viewtelugu);
        h=(TextView)findViewById(R.id.viewhindi);
        e=(TextView)findViewById(R.id.viewenglish);
        m=(TextView)findViewById(R.id.viewmaths);
        p=(TextView)findViewById(R.id.viewphysics);
        b=(TextView)findViewById(R.id.viewbiology);
        s=(TextView)findViewById(R.id.viewsocial);
        c=(TextView)findViewById(R.id.viewcomputer);
        g=(TextView)findViewById(R.id.viewgk);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();

        testname.setText(Studentprogress.gettestsected());

        databaseReference=firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Students Marks").child(school.getschselected()).child(Classeslist.clssselected()).child(StudentsList.getsectstu()).child(Studentprogress.gettestsected());
        databaseReference.addValueEventListener(new com.google.firebase.database.ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stumarks=(Marks)dataSnapshot.getValue(Marks.class);
                t.setText(stumarks.tel);
                h.setText(stumarks.hin);
                e.setText(stumarks.eng);
                m.setText(stumarks.mat);
                p.setText(stumarks.phy);
                b.setText(stumarks.bio);
                s.setText(stumarks.soc);
                c.setText(stumarks.cmp);
                g.setText(stumarks.generalk);
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
