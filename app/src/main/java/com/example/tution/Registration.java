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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

public class Registration extends AppCompatActivity {

    EditText name;
    EditText mail;
    EditText phno;
    EditText place;
    EditText cp;
    EditText pass;
    EditText repass;
    Button register;
    TextView already;
    ImageView profile;

    static int Pic_image=123;
    Uri imagepath=null;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        allign();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(Registration.this,MainActivity.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String nam = name.getText().toString().trim();
                String mai = mail.getText().toString().trim();
                String pas = pass.getText().toString().trim();
                if(isfilled())
                {
                    firebaseAuth.createUserWithEmailAndPassword(mai,pas).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(Registration.this,"Registration Succesfull",Toast.LENGTH_SHORT).show();
                                createprofile();
                                sendemailverification();
                            }
                            else
                            {
                                Toast.makeText(Registration.this,"Registration Failed",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(Registration.this,MainActivity.class));
                            }

                        }
                    });

                }
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.setType("image/*");//application/pdf-doc-*//audio/*
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"select image"), Pic_image);
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

    private boolean isfilled() {
        String nam = name.getText().toString().trim();
        String mai = mail.getText().toString().trim();
        String pas = pass.getText().toString().trim();
        String repas = repass.getText().toString().trim();
        String pla = place.getText().toString().trim();
        String c = cp.getText().toString().trim();
        String ph = phno.getText().toString().trim();
        if(nam.isEmpty() || mai.isEmpty() || pas.isEmpty() || repas.isEmpty() || pla.isEmpty() || c.isEmpty() || ph.isEmpty())
        {
            Toast.makeText(Registration.this,"Please Fill All Details",Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            if(ph.length()==10 && ph.matches("^[0-9]*$"))
            {
                //do nothing just for check
            }
            else
            {
                Toast.makeText(Registration.this,"Enter Valid Phone number ",Toast.LENGTH_LONG).show();
                return false;
            }
            if(pas.equals(repas))
            {
                if(pas.length()<6)
                {
                    Toast.makeText(Registration.this,"Password must contain 6 letters One capital, one digit ",Toast.LENGTH_LONG).show();
                    return false;
                }
                else
                {
                    if(pas.matches("^[0-9a-zA-z]*$")) {
                        return true; }
                    else
                    {
                        Toast.makeText(Registration.this,"Password must contain 6 letters One capital, one digit ",Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
            }
            else
            {
                Toast.makeText(Registration.this,"Please Check Re-enter Password",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

    private void createprofile() {

        String nam = name.getText().toString().trim();
        String mai = mail.getText().toString().trim();
        String pl = place.getText().toString().trim();
        String p = phno.getText().toString().trim();
        String c = cp.getText().toString().trim();

        String imp;

        StorageReference imageref = firebaseStorage.getReference("Users").child(firebaseAuth.getUid()).child("Users Profile").child("images").child(" profile Pic");

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
                    Toast.makeText(Registration.this, "Upload failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Registration.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                }
            });
        }
        DatabaseReference mydata = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Users Profile");


        UserProfile userProfile = new UserProfile(nam,mai,pl,p,c,imp);
        mydata.setValue(userProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Registration.this,"Profile set",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendemailverification() {
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(Registration.this,"verify your email",Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(Registration.this,MainActivity.class));
                    }
                    else
                    {
                        Toast.makeText(Registration.this,"oops something went wrong",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void allign() {
        name = (EditText) findViewById(R.id.rtname);
        mail = (EditText) findViewById(R.id.rtmail);
        phno = (EditText) findViewById(R.id.rtphno);
        place = (EditText) findViewById(R.id.rtplace);
        cp = (EditText) findViewById(R.id.rtcp);
        pass = (EditText) findViewById(R.id.rtpass);
        repass = (EditText) findViewById(R.id.rtrepass);

        register = (Button) findViewById(R.id.rtre);

        already = (TextView) findViewById(R.id.rtalready);

        profile = (ImageView) findViewById(R.id.rtprofile);

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == Pic_image && resultCode == RESULT_OK && data.getData()!=null)
        {
            imagepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);
                profile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
//    public static String sendmailid()
//    {
//        return mail.getText().toString().trim();
//    }
}
