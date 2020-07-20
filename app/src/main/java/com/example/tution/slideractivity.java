package com.example.tution;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

public class slideractivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mypage;

    private  int[] layouts={R.layout.intro1,R.layout.intro2,R.layout.intro3};

    private MpagerAdapter mpagerAdapter;

    private LinearLayout dotslayout;

    private ImageView[] dots;

    private Button next,skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(new preferencemanager(this).checkPreference())
        {
            loadhome();
        }

        if(Build.VERSION.SDK_INT>=19)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else
        {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_slideractivity);

        mypage = (ViewPager) findViewById(R.id.viewpager);

        dotslayout = (LinearLayout) findViewById(R.id.dotlayout);

        next = (Button) findViewById(R.id.btnext);
        skip = (Button) findViewById(R.id.btskip);

        skip.setOnClickListener(this);
        next.setOnClickListener(this);


        mpagerAdapter = new MpagerAdapter(layouts,this);
        mypage.setAdapter(mpagerAdapter);

        createdots(0);

        mypage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                createdots(position);
                if(position==layouts.length-1)
                {
                    next.setText("Start");
                    skip.setVisibility(View.INVISIBLE);
                }
                else
                {
                    next.setText("Next");
                    skip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void createdots(int current_position)
    {
        if(dotslayout!=null)
        {
            dotslayout.removeAllViews();

            dots = new ImageView[layouts.length];

            for(int i=0;i<layouts.length;i++)
            {
                dots[i]=new ImageView(this);
                if(i==current_position)
                {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.active_dots));
                }
                else
                {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.defaultdots));
                }

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

                params.setMargins(4,0,4,0);

                dotslayout.addView(dots[i],params);
            }
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnext:
                loadnext();
                break;

            case R.id.btskip:
                loadhome();
                new preferencemanager(this).writePreference();
                break;
        }

    }

    public void loadhome()
    {
        finish();
        startActivity(new Intent(slideractivity.this,MainActivity.class));
    }

    public void loadnext()
    {
        int ns=mypage.getCurrentItem()+1;

        if(ns<layouts.length)
        {
            mypage.setCurrentItem(ns);
        }
        else
        {
            loadhome();
            new preferencemanager(this).writePreference();
        }

    }
}
