package com.example.tution;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class EditUserdetails extends AppCompatActivity {

    ImageView edtusrpro;
    EditText edtusn,edtuse,edtusph,edtusc,edtusp;
    Button etds;
    UserProfile up;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    static int Pic_image=123;
    Uri imagepath=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_userdetails);

        edtusrpro=(ImageView)findViewById(R.id.etusrprofile);
        edtusn=(EditText)findViewById(R.id.etusrname);
        edtuse=(EditText)findViewById(R.id.etusrmail);
        edtusph=(EditText)findViewById(R.id.etusrphno);
        edtusp=(EditText)findViewById(R.id.etusrplace);
        edtusc=(EditText)findViewById(R.id.etusrcp);
        etds=(Button)findViewById(R.id.etsave);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();

        databaseReference=firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Users Profile");
        databaseReference.addValueEventListener(new com.google.firebase.database.ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                up=(UserProfile)dataSnapshot.getValue(UserProfile.class);
                edtusn.setText(up.name);
                edtuse.setText(up.mail);
                edtusph.setText(up.phno);
                edtusp.setText(up.place);
                edtusc.setText(up.CP);

//                if(up.imp.equals("set"))
//                {
                    storageReference=firebaseStorage.getReference("Users").child(firebaseAuth.getUid()).child("Users Profile/images/profile Pic");
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {


                            Picasso.get().load(uri).into(edtusrpro);

                        }
                    });
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditUserdetails.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });



        edtusrpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.setType("image/*");//application/pdf-doc-*//audio/*
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"select image"), Pic_image);
            }
        });

        etds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfile edup=new UserProfile();
                edup.name=edtusn.getText().toString().trim();
                edup.mail=edtuse.getText().toString().trim();
                edup.phno=edtusph.getText().toString().trim();
                edup.place=edtusp.getText().toString().trim();
                edup.CP=edtusc.getText().toString().trim();
                if(edup.phno.matches("^[0-9]*$") && edup.phno.length()==10)
                {
                    if(imagepath==null)
                    {
                        edup.imp="notset";
                        imagepath= Uri.parse("default");
                    }
                    else
                    {
                        edup.imp="set";
                        StorageReference storageReference=firebaseStorage.getReference("Users");
                        StorageReference imageref = storageReference.child(firebaseAuth.getUid()).child("Users Profile").child("images").child(" profile Pic");
                        UploadTask uploadTask = imageref.putFile(imagepath);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditUserdetails.this, "Upload failed", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(EditUserdetails.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    DatabaseReference mydata = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Users Profile");



                    mydata.setValue(edup).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(EditUserdetails.this,"Profile set",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

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

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == Pic_image && resultCode == RESULT_OK && data.getData()!=null)
        {
            imagepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);
                edtusrpro.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
