package com.example.ben.delayedtexts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Ben on 12/24/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        //context.getSystemService("MainActivity");
        Intent newMain= new Intent(context, MainActivity.class);
        context.startActivity(newMain);
        //main.sendSMS("5555215554", "WELCOME to the jungle");

        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
        // TODO access the database for all the instances that need to be set at this time?
    }
}
