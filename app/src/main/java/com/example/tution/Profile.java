package com.example.tution;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {

    ImageView imvprof;
    TextView usn,use,usph,usc,usp;
    Button usedt;

    UserProfile up;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imvprof=(ImageView)findViewById(R.id.usrprofilepic);
        usn=(TextView)findViewById(R.id.usrname);
        use=(TextView)findViewById(R.id.usrmail);
        usph=(TextView)findViewById(R.id.usrphno);
        usp=(TextView)findViewById(R.id.usrplace);
        usc=(TextView)findViewById(R.id.usrpos);
        usedt=(Button)findViewById(R.id.usredt);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();

        databaseReference=firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Users Profile");
        databaseReference.addValueEventListener(new com.google.firebase.database.ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                up=(UserProfile)dataSnapshot.getValue(UserProfile.class);
                usn.setText(up.name);
                use.setText(up.mail);
                usph.setText(up.phno);
                usp.setText(up.place);
                usc.setText(up.CP);
//
//                if(up.imp.equals("set"))
//                {
                    storageReference=firebaseStorage.getReference("Users").child(firebaseAuth.getUid()).child("Users Profile").child("images").child(" profile Pic");
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {


                            Picasso.get().load(uri).into(imvprof);

                        }
                    });
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Profile.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });



        usedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this,EditUserdetails.class));
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
