package com.example.ben.delayedtexts;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.telephony.SmsManager;

/**
 * Created by Ben on 12/23/2017.
 */

public class sendSMS extends Activity {
    public void onCreate(){

    }

    public void sendSMS(String phoneNumber, String message){
        PendingIntent pi = PendingIntent.getActivity(this, 0,
                new Intent(this, sendSMS.class), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, pi, null);
    }
}
