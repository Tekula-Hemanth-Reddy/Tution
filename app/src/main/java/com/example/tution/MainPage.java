package com.example.tution;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class MainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    FrameLayout frameLayout;

    private AppBarConfiguration mAppBarConfiguration;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    DatabaseReference mydata;
    DrawerLayout drawer;
    NavigationView navigationView;



    ImageView imv;
    TextView nam,wis;



    TextView txtnamenav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("Users").child(firebaseAuth.getUid()).child("Users Profile").child("images").child(" profile Pic");
        mydata=firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Users Profile");

        setContentView(R.layout.activity_main_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        View header=navigationView.getHeaderView(0);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_add, R.id.nav_show,R.id.nav_send)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        imv = header.findViewById(R.id.imageView);
        txtnamenav=header.findViewById(R.id.name);

        nam=findViewById(R.id.wishname);
        wis=findViewById(R.id.wish);

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {


                Picasso.get().load(uri).into(imv);


            }
        });

        mydata.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile up=dataSnapshot.getValue(UserProfile.class);
                nam.setText(up.name);
                txtnamenav.setText(up.name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        frameLayout=(FrameLayout)findViewById(R.id.fragment_container);
        Calendar c=Calendar.getInstance();
        int timeofday=c.get(Calendar.HOUR_OF_DAY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (timeofday >= 0 && timeofday < 12) {
                wis.setText("GOOD MORNING");

                frameLayout.setBackground(getDrawable(R.drawable.gm));

            } else if (timeofday >= 12 && timeofday < 16) {

                wis.setText("GOOD AFTERNOON");
                frameLayout.setBackground(getDrawable(R.drawable.gm));
            } else if (timeofday >= 16 && timeofday < 21) {

                wis.setText("GOOD EVENING");
                frameLayout.setBackground(getDrawable(R.drawable.gm));
            } else if (timeofday >= 21 && timeofday < 24) {

                wis.setText("GOOD NIGHT");
                frameLayout.setBackground(getDrawable(R.drawable.gn));
            }
        }
        else
        {
            wis.setText("hello");
        }






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
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
            case R.id.nav_add:
                startActivity(new Intent(this,Addstudents.class));
                break;
            case R.id.nav_show:
                startActivity(new Intent(MainPage.this,school.class));
                break;
            case R.id.nav_home:
                //startActivity(new Intent(this,information.class));
                break;
            case R.id.nav_send:
                startActivity(new Intent(MainPage.this,Sendsmstoall.class));
                break;
            case R.id.take_attendence:
                startActivity(new Intent(MainPage.this,DailyAttendance.class));
                break;
            case R.id.nav_view_attendence:
                startActivity(new Intent(MainPage.this,DailyAttendanceview.class));
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_Logout:{
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(MainPage.this,MainActivity.class));
                Toast.makeText(MainPage.this,"LogOut Successful",Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.action_settings:{
                startActivity(new Intent(MainPage.this,lgnact.class));
                break;
            }
            case R.id.action_Profile:{
                startActivity(new Intent(MainPage.this,Profile.class));
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
