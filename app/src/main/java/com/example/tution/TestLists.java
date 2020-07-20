package com.example.tution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class TestLists extends AppCompatActivity {

    ListView tstlis;
    static String tstselected;

    List<String> tstnames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_lists);

        tstlis=(ListView)findViewById(R.id.listtest);
        tstnames=new ArrayList<String>();
        tstnames.add("First Unit Test");
        tstnames.add("Second Unit Test");
        tstnames.add("Third Unit Test");
        tstnames.add("Fourth Unit Test");
        tstnames.add("Ouatearly Exams");
        tstnames.add("Halfyearly Exams");
        tstnames.add("Anual Exams");

        ArrayAdapter ad=new ArrayAdapter(TestLists.this,android.R.layout.simple_list_item_1,tstnames);
        tstlis.setAdapter(ad);
        tstlis.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tstselected =(String) parent.getItemAtPosition(position);

                    startActivity(new Intent(TestLists.this,EnterMarks.class));

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

    public static String getTstselected()
    {
        return tstselected;
    }

}
