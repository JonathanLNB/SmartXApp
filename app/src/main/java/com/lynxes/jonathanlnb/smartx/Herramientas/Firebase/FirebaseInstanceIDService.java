package com.lynxes.jonathanlnb.smartx.Herramientas.Firebase;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.google.android.gms.identity.intents.AddressConstants;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.lynxes.jonathanlnb.smartx.R;

/**
 * Created by user on 11/10/2017.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    private SharedPreferences sharedPreferences;

    @Override
    public void onTokenRefresh() {
        sharedPreferences = getSharedPreferences(getString(R.string.shareKey), MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.println(Log.ASSERT, "Llave", ""+token);
        ed.putString(getString(R.string.shareKey), token);
        ed.commit();
    }
}
