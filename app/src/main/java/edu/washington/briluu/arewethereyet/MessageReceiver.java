package edu.washington.briluu.arewethereyet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by briluu on 2/19/17.
 */

public class MessageReceiver extends BroadcastReceiver {

    private static String INTENT_MESSAGE = "MESSAGE";
    private static String INTENT_NUMBER = "NUMBER";

    public MessageReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("MessageReceiver", "onReceive for MessageReceiver called.");

        String message = intent.getStringExtra(INTENT_MESSAGE);
        String number = intent.getStringExtra(INTENT_NUMBER);

        Log.i("MessageReceiver", "Message is: " + message);

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, message, null, null);

        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
