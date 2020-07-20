package com.example.tution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class StudentProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    private AppBarConfiguration mAppBarConfiguration;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    DrawerLayout drawer;
    NavigationView navigationView;

    String scl,cl,nam;


    TextView txtnavnam;
    ImageView imgnavpic;

    public static student_details sd= new student_details();

    ImageView imv;
    TextView name,fname,mname,classname,schoolname,phonenumber,feesstu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();


        Toolbar toolbar = findViewById(R.id.stutoolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_stu_layout);
        navigationView = findViewById(R.id.nav_stu_view);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        View head=navigationView.getHeaderView(0);

        txtnavnam=head.findViewById(R.id.stunavname);
        imgnavpic=head.findViewById(R.id.stunavimageView);

        name=(TextView)findViewById(R.id.viestuname);
        fname=(TextView)findViewById(R.id.viestufathername);
        mname=(TextView)findViewById(R.id.viestumothername);
        schoolname=(TextView)findViewById(R.id.viestuschool);
        classname=(TextView)findViewById(R.id.viestuclass);
        phonenumber=(TextView)findViewById(R.id.viestuphno);
        imv=(ImageView)findViewById(R.id.viestupro);
        feesstu=(TextView)findViewById(R.id.viestufee);

        scl=school.getschselected();
        cl=Classeslist.clssselected();
        nam=StudentsList.getsectstu();

        databaseReference=firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Students Profile").child(scl).child(cl).child(nam);
        databaseReference.addValueEventListener(new com.google.firebase.database.ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sd=(student_details)dataSnapshot.getValue(student_details.class);
                name.setText(sd.stuname);
                fname.setText(sd.stufather);
                mname.setText(sd.stumother);
                schoolname.setText(sd.stuschool);
                classname.setText(sd.stuclass);
                phonenumber.setText(sd.stuphno);
                feesstu.setText(sd.fees+"Rs");

                txtnavnam.setText(sd.stuname);
                storageReference=firebaseStorage.getReference("Users").child(firebaseAuth.getUid()).child("images").child("Students Profile").child(scl).child(cl).child(nam);
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {


                        Picasso.get().load(uri).into(imv);
                        Picasso.get().load(uri).into(imgnavpic);

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static student_details getstudet()
    {
        return sd;
    }


    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.nav_stu_edit:
                startActivity(new Intent(StudentProfile.this,Editstudents.class));
                break;
            case R.id.nav_stu_add_marks:
                startActivity(new Intent(StudentProfile.this,TestLists.class));
                break;
            case R.id.nav_stu_home:
                startActivity(new Intent(StudentProfile.this,MainPage.class));
                break;
            case R.id.nav_stu_view_progress:
                startActivity(new Intent(StudentProfile.this,Studentprogress.class));
                break;
            case R.id.nav_stu_send:
                startActivity(new Intent(StudentProfile.this,sendsmstoone.class));
                break;
            case R.id.fee_status:
                startActivity(new Intent(StudentProfile.this,FeesStatus.class));
                break;
            case R.id.remove_student:
                removedata();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void removedata() {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to delete ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                databaseReference=firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Students Profile").child(scl).child(cl).child(nam);
                databaseReference.removeValue();
                storageReference=firebaseStorage.getReference("Users").child(firebaseAuth.getUid()).child("images").child("Students Profile").child(scl).child(cl).child(nam);
                databaseReference=firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Students Fee").child(scl).child(cl).child(nam);
                databaseReference.removeValue();
                databaseReference=firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Students Marks").child(scl).child(cl).child(nam);
                databaseReference.removeValue();
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(StudentProfile.this,"Data Deleted Succesfully",Toast.LENGTH_SHORT).show();

                    }
                });
                databaseReference= firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Students Names").child(nam);
                databaseReference.removeValue();
                databaseReference = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("absentes List").child(nam);
                databaseReference.removeValue();
                startActivity(new Intent(StudentProfile.this,MainPage.class));

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(StudentProfile.this,"Data Not deleted",Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}
