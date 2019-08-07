package com.ads.readmessage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class ReadMessageReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SmsBroadcastReceiver";
    private String phoneNo;
    private String msg;
    @Override
    public void onReceive(Context context, Intent intent) {
        //retries the general action to be performed and displays in log
        Log.i(TAG,"Intent Received: "+intent.getAction());
        if (intent.getAction() == SMS_RECEIVED){
            //retires a map of extended data from intent
            Bundle dataBundle = intent.getExtras();
            if (dataBundle != null){
                //creates protocol data unit object which is used for transferring message
                Object[] mypdu = (Object[])dataBundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[mypdu.length];

                for (int i=0; i<mypdu.length; i++){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        String format = dataBundle.getString("format");
                        messages[i] = SmsMessage.createFromPdu((byte[])mypdu[i],format);
                    } else {
                        messages[i] = SmsMessage.createFromPdu((byte[])mypdu[i]);
                    }
                    msg = messages[i].getDisplayMessageBody();
                    phoneNo = messages[i].getOriginatingAddress();
                }
                Toast.makeText(context, "Message: "+msg+"\nNumber: "+phoneNo, Toast.LENGTH_LONG).show();
            }
        }
    }
}
