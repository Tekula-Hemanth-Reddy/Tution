package com.example.tution;

import android.content.Context;
import android.content.SharedPreferences;

public class preferencemanager {

    private Context context;
    private SharedPreferences sharedPreferences;

    public preferencemanager(Context context)
    {
        this.context=context;
        getSharedPreferences();

    }

    private void getSharedPreferences()
    {
        sharedPreferences=context.getSharedPreferences(context.getString(R.string.my_preference),context.MODE_PRIVATE);
    }
    public void writePreference()
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.my_preferencekey),"INIT_OK");
        editor.commit();
    }

    public boolean checkPreference()
    {
        boolean status = false;
        if(sharedPreferences.getString(context.getString(R.string.my_preferencekey),"null").equals("null"))
        {
            status=false;
        }
        else
        {
            status=true;

        }
        return status;
    }
    public void clearPrefrerence()
    {
        sharedPreferences.edit().clear().commit();
    }
}
