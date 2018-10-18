package edu.temple.threadapplication;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView countdown;
    boolean pause = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // we want a countdown timer

        countdown = findViewById(R.id.countdownTextView);

        final Thread t = new Thread() {
            @Override
            public void run () {
                for (int i = 100; i > 0; i--) {

                    /* ONE WAY TO DO IT
                    //illegal because workers can't manipulate views
                    //countdown.setText(String.valueOf(i));   //'i' would seem like an id to the thing
                    Message msg = Message.obtain(); // message.obtain recycles a message object for you
                    msg.what = i; //put in the value you want to store
                    //simple int values: store in arg1 or arg2
                    //simple int values: store in what attribute
                    //more complex data, put it in the obj reference
                    timerHandler.sendMessage(msg);  // threads accessing variables, not view objects
                    //now the main thread has it
                    */
                    //Log.i("Countdown", String.valueOf(i));

                    timerHandler.sendEmptyMessage(i);   // does the stuff we did above
                    while (pause);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        t.start();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "You clicked the button!", Toast.LENGTH_SHORT).show();

                pause = !pause;
            }
        });
    }

    // we want the android.os one
    // it's the proper handler to use
    // helps us handle memory leaks
    Handler timerHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //now the main thread has the message object
            countdown.setText(String.valueOf(msg.what));    //we get the what value from our message
            return false;
        }
    });
}
