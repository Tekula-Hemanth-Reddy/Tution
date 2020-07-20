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
import java.util.Calendar;

public class Editstudents extends AppCompatActivity {

    EditText name, addstuschool, clas, father, mother, phno,f;
    Button done;
    ImageView photo;

    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    FirebaseAuth firebaseAuth;

    DatabaseReference databaseReference;
    StorageReference storageReference;

    student_details sd=new student_details();

    String scl,cl,nam;

    static int Pic_image = 123;
    Uri imagepath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editstudents);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = (EditText) findViewById(R.id.edtstuname);
        addstuschool = (EditText) findViewById(R.id.edtstuschool);
        clas = (EditText) findViewById(R.id.edtstuclass);
        father = (EditText) findViewById(R.id.edtstufathername);
        mother = (EditText) findViewById(R.id.edtstumothername);
        phno = (EditText) findViewById(R.id.edtstuphno);
        done = (Button) findViewById(R.id.savestudetails);
        photo = (ImageView) findViewById(R.id.edtstuphoto);
        f=(EditText)findViewById(R.id.edtstufee);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        scl=school.getschselected();
        cl=Classeslist.clssselected();
        nam=StudentsList.getsectstu();

        databaseReference=firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Students Profile").child(scl).child(cl).child(nam);
        databaseReference.addValueEventListener(new com.google.firebase.database.ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sd=(student_details)dataSnapshot.getValue(student_details.class);
                name.setText(sd.stuname);
                father.setText(sd.stufather);
                mother.setText(sd.stumother);
                addstuschool.setText(sd.stuschool);
                clas.setText(sd.stuclass);
                phno.setText(sd.stuphno);
                f.setText(sd.fees+"Rs");
                storageReference=firebaseStorage.getReference("Users").child(firebaseAuth.getUid()).child("images").child("Students Profile").child(scl).child(cl).child(nam);
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {


                        Picasso.get().load(uri).into(photo);

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveinformation();
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");//application/pdf-doc-*//audio/*
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select image"), Pic_image);
            }
        });


    }

    private void saveinformation() {
        String nam=name.getText().toString().trim();
        String scl=addstuschool.getText().toString().trim();
        String cl=clas.getText().toString().trim();
        String fnam=father.getText().toString().trim();
        String mnam=mother.getText().toString().trim();
        String ph=phno.getText().toString().trim();
        String fe=f.getText().toString().trim();
        String imp;
        if (Validate(nam,scl,cl,fnam,mnam,ph,fe))
        {
            StorageReference storageReference=firebaseStorage.getReference("Users");
            StorageReference imageref = storageReference.child(firebaseAuth.getUid()).child("images").child("Students Profile").child(scl).child(cl).child(nam);

            if(imagepath==null)
            {
                imp="notset";
                imagepath= Uri.parse("default");
            }
            else {
                imp="set";
                UploadTask uploadTask = imageref.putFile(imagepath);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Editstudents.this, "Upload failed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(Editstudents.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            Calendar c=Calendar.getInstance();
            int timeofday=c.get(Calendar.YEAR);
            String year= ""+timeofday;




            final student_details sd=new student_details(nam,scl,cl,fnam,mnam,ph,year,imp,fe);
            databaseReference=firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Students Profile").child(scl).child(cl).child(nam);

            databaseReference.setValue(sd).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(Editstudents.this,"Succesfully added",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Editstudents.this,StudentProfile.class));
                    }
                    else
                    {
                        Toast.makeText(Editstudents.this,"Something went wrong try again",Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }
    private boolean Validate(String nam, String scl, String cl, String fnam, String mnam, String ph,String fe) {
        if(nam.isEmpty() || scl.isEmpty() || cl.isEmpty() || fnam.isEmpty() || mnam.isEmpty() || ph.isEmpty() || fe.isEmpty())
        {
            Toast.makeText(this,"Please fill all details",Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            if(ph.length()==10 && ph.matches("^[0-9]*$"))
            {
                return true;
            }
            else
            {
                Toast.makeText(this,"Enter Valid Phone number ",Toast.LENGTH_LONG).show();
                return false;
            }

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == Pic_image && resultCode == RESULT_OK && data.getData()!=null)
        {
            imagepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);
                photo.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
