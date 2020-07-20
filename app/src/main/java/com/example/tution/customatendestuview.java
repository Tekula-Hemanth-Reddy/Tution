package com.example.tution;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class customatendestuview extends ArrayAdapter {

    Context context;
    int resource;
    List<atendeview> attendestulist;
    ArrayList<atendeview> namesofstudentspresent=new ArrayList<atendeview>();

    public customatendestuview(@NonNull Context context, int resource, List<atendeview> attendestulist) {
        super(context, resource, attendestulist);

        this.context=context;
        this.resource=resource;
        this.attendestulist=attendestulist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(resource,null);

        TextView textstuname=(TextView)view.findViewById(R.id.attendstunam);
        ImageView imagestuatnd=(ImageView)view.findViewById(R.id.attendstuphoto);
        Switch butnstuatnd=(Switch)view.findViewById(R.id.attendstupresent);

        final atendeview stu=attendestulist.get(position);

        textstuname.setText(stu.atendename);
        butnstuatnd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {

                    namesofstudentspresent.add(stu);
                }
                else
                {
                    namesofstudentspresent.remove(stu);
                }
            }
        });

        return view;
    }

    public ArrayList<atendeview> getpresenteslist() {
       return namesofstudentspresent;
    }
}
