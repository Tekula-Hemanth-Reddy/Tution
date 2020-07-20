package com.example.tution;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PassReset extends AppCompatActivity {

    EditText emailtxt;
    Button rest;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_reset);

        emailtxt = (EditText) findViewById(R.id.premailreset);
        rest = (Button) findViewById(R.id.prresetbt);
        firebaseAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String mail = emailtxt.getText().toString().trim();
                if (mail.isEmpty()) {

                    Toast.makeText(PassReset.this,"please enter mail",Toast.LENGTH_SHORT).show();

                } else {
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                Toast.makeText(PassReset.this,"password reset email sent!",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(PassReset.this,MainActivity.class));
                            }
                            else
                            {
                                Toast.makeText(PassReset.this,"password reset email failed!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

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
