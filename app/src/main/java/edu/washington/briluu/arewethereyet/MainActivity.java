package edu.washington.briluu.arewethereyet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String INTENT_MESSAGE = "MESSAGE";
    private String INTENT_NUMBER = "NUMBER";
    private String INTENT_INTERVAL = "INTERVAL";
    private Boolean started = false;
    private EditText messageET;
    private EditText numberET;
    private EditText intervalET;
    private Button button;
    AlarmManager manager;
    PendingIntent pIntent;
    private static String message;
    private static String number;
    private static int interval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Wire up buttons
        Button start = (Button) findViewById(R.id.start);
        messageET = (EditText) findViewById(R.id.message);
        numberET = (EditText) findViewById(R.id.phone_number);
        intervalET = (EditText) findViewById(R.id.interval);
        button = (Button) findViewById(R.id.start);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!started) {
                    if (isFormValid()) {
                        manager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                        Intent intent = new Intent(MainActivity.this, MessageReceiver.class);
                        String fullMessage = number + ": " + message;
                        intent.putExtra(INTENT_MESSAGE, fullMessage);
                        intent.putExtra(INTENT_NUMBER, number);

                        pIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

                        // send the message in specified minute intervals given by the user
                        manager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                                SystemClock.elapsedRealtime() + 60 * 1000 * interval,
                                1000 * 60 * interval,
                                pIntent);
                        started = true;
                        button.setText(R.string.stop);
                    }
                } else {
                    stop();
                    button.setText(R.string.start);
                    Toast.makeText(getApplicationContext(), "Messages stopped!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Validate form inputs
    public boolean isFormValid() {
        message = messageET.getText().toString();
        number = numberET.getText().toString();
        String intervalStr = intervalET.getText().toString();
        if (intervalStr.length() != 0) {
            interval = Integer.parseInt(intervalET.getText().toString());
        } else {
            intervalET.setError("Need a value for the interval between messages!");
            return false;
        }
        Boolean validMessage = message != null && !message.isEmpty();
        if (!validMessage) {
            messageET.setError("Need a message of at least 1 character!");
            return false;
        }
        Boolean validNumber = number != null && !number.isEmpty();
        if (!validNumber) {
            numberET.setError("Need a phone number of at least 1 digit!");
            return false;
        }
        Log.i("MainActivity", "Message is: \"" + number + ": " + message + "\"");
        return true;
    }

    // stops the alarm
    public void stop() {
        if (started && manager != null) {
            started = false;
            manager.cancel(pIntent);
            pIntent.cancel();
        }
    }

}
