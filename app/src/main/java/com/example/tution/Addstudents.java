package com.example.tution;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;

public class Addstudents extends AppCompatActivity {

    EditText name,addstuschool,clas,father,mother,phno,fe;
    Button done;
    ImageView photo;



    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    FirebaseAuth firebaseAuth;

    DatabaseReference databaseReference;

    static int Pic_image=123;
    Uri imagepath=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstudents);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name=(EditText)findViewById(R.id.addname);
        addstuschool=(EditText)findViewById(R.id.addschool);
        clas=(EditText)findViewById(R.id.addclass);
        father=(EditText)findViewById(R.id.addfathername);
        mother=(EditText)findViewById(R.id.addmothername);
        phno=(EditText)findViewById(R.id.addphno);
        done=(Button)findViewById(R.id.adddetails);
        fe=(EditText)findViewById(R.id.addfee);
        photo=(ImageView)findViewById(R.id.addstuphoto);

        firebaseAuth =FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addinformation();
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.setType("image/*");//application/pdf-doc-*//audio/*
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"select image"), Pic_image);
            }
        });


    }

    private void addinformation() {
        String nam=name.getText().toString().trim();
        String scl=addstuschool.getText().toString().trim();
        String cl=clas.getText().toString().trim();
        String fnam=father.getText().toString().trim();
        String mnam=mother.getText().toString().trim();
        String ph=phno.getText().toString().trim();
        String f=fe.getText().toString().trim();
        String imp;
        if (Validate(nam,scl,cl,fnam,mnam,ph,f))
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
                        Toast.makeText(Addstudents.this, "Upload failed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(Addstudents.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            Calendar c=Calendar.getInstance();
            int timeofday=c.get(Calendar.YEAR);
            String year= ""+timeofday;




            final student_details sd=new student_details(nam,scl,cl,fnam,mnam,ph,year,imp,f);
            databaseReference=firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Students Profile").child(scl).child(cl).child(nam);

               databaseReference.setValue(sd).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful())
                       {
                           Toast.makeText(Addstudents.this,"Succesfully added",Toast.LENGTH_SHORT).show();
                           addfeelist();
                           startActivity(new Intent(Addstudents.this,MainPage.class));
                       }
                       else
                       {
                           Toast.makeText(Addstudents.this,"Something went wrong try again",Toast.LENGTH_LONG).show();
                       }
                   }
               });

        }
    }

    private boolean Validate(String nam, String scl, String cl, String fnam, String mnam, String ph,String f) {
        if(nam.isEmpty() || scl.isEmpty() || cl.isEmpty() || fnam.isEmpty() || mnam.isEmpty() || ph.isEmpty() || f.isEmpty())
        {
            Toast.makeText(this,"Please fill all details",Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            if(Integer.parseInt(cl)>10)
            {
                Toast.makeText(this,"Only up to 10th ",Toast.LENGTH_LONG).show();
                return false;
            }
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

    public void addfeelist()
    {
        final String nam=name.getText().toString().trim();
        String scl=addstuschool.getText().toString().trim();
        String cl=clas.getText().toString().trim();
        feemonths fe =new feemonths();

        fe.April="No";
        fe.August="No";
        fe.December="No";
        fe.February="No";
        fe.January="No";
        fe.July="No";
        fe.June="No";
        fe.March="No";
        fe.May="No";
        fe.November="No";
        fe.October="No";
        fe.September="No";



        DatabaseReference mydata = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Students Fee").child(scl).child(cl).child(nam);
        mydata.setValue(fe).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {

                }
            }
        });

        atendeview sta=new atendeview(nam);

        DatabaseReference stddataref=firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Students Names").child(nam);
        stddataref.setValue(sta).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    ////
                }
                else
                {
                    Toast.makeText(Addstudents.this,"Error",Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}
