package com.example.tution;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText mail,password;
    TextView newone,remains,fp;
    Button log;

    int i=5;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        allign();

        check_if_in();

        newone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Registration.class));
            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mail.getText().toString().isEmpty() || password.getText().toString().isEmpty())
                {
                    Toast.makeText(MainActivity.this,"Please enter all details",Toast.LENGTH_SHORT).show();
                    i=i-1;
                    remains.setText("No.of attempts remaining : "+i);
                    if(i==0)
                    {
                        Toast.makeText(MainActivity.this,"Try after some time",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else
                {
                    validate(mail.getText().toString().trim(),password.getText().toString().trim());
                }
            }
        });
        fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,PassReset.class));
            }
        });
    }

    public void check_if_in()
    {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            finish();
            startActivity(new Intent(MainActivity.this,MainPage.class));
        }
    }

    public void allign()
    {
        mail = (EditText) findViewById(R.id.etmemail);
        password = (EditText) findViewById(R.id.etmpass);
        newone = (TextView) findViewById(R.id.tvmnew);
        remains = (TextView) findViewById(R.id.tvmremain);
        fp = (TextView) findViewById(R.id.tvmfp);
        log = (Button) findViewById(R.id.btmlogin);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
    }

    private void validate(String email,String password) {

        progressDialog.setMessage("please wait");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    progressDialog.dismiss();
                    checkEmailVerification();
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,"Login Failed", Toast.LENGTH_SHORT).show();
                    i=i-1;
                    remains.setText("No.of attempts remaining : "+i);
                    if(i==0)
                    {
                        Toast.makeText(MainActivity.this,"Try after some time",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });
    }

    private void checkEmailVerification()
    {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();
        if(emailflag)
        {
            finish();
            startActivity(new Intent(MainActivity.this,MainPage.class));
        }
        else
        {
            Toast.makeText(this,"Verify your email",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }

}
