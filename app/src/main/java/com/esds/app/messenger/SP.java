package com.esds.app.messenger;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SP {
    SharedPreferences sp;
    SharedPreferences.Editor edit;

    public SP(Context context)
    {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        edit = sp.edit();
    }

    public int getId()
    {
        int id = sp.getInt("notification_id", 1);
        int newId = id + 1;
        edit.putInt("notification_id", newId).commit();
        return id;
    }
}
