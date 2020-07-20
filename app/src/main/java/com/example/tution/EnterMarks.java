package com.example.tution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EnterMarks extends AppCompatActivity {

    TextView testname;
    EditText t,h,e,m,p,b,s,c,g;

    Marks stumarks=new Marks();

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_marks);

        testname=(TextView)findViewById(R.id.tselect);
        t=(EditText)findViewById(R.id.telugu);
        h=(EditText)findViewById(R.id.hindi);
        e=(EditText)findViewById(R.id.english);
        m=(EditText)findViewById(R.id.maths);
        p=(EditText)findViewById(R.id.physics);
        b=(EditText)findViewById(R.id.biology);
        s=(EditText)findViewById(R.id.social);
        c=(EditText)findViewById(R.id.computer);
        g=(EditText)findViewById(R.id.gk);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();

        testname.setText(TestLists.getTstselected());
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

    public void savedetails(View view) {

        stumarks.tel=t.getText().toString().trim();
        stumarks.hin=h.getText().toString().trim();
        stumarks.eng=e.getText().toString().trim();
        stumarks.mat=m.getText().toString().trim();
        stumarks.phy=p.getText().toString().trim();
        stumarks.bio=b.getText().toString().trim();
        stumarks.soc=s.getText().toString().trim();
        stumarks.cmp=c.getText().toString().trim();
        stumarks.generalk=g.getText().toString().trim();

        databaseReference=firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Students Marks").child(school.getschselected()).child(Classeslist.clssselected()).child(StudentsList.getsectstu()).child(TestLists.getTstselected());
        databaseReference.setValue(stumarks).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(EnterMarks.this,"Succesfully added",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EnterMarks.this,StudentProfile.class));
                }
            }
        });

    }
}
