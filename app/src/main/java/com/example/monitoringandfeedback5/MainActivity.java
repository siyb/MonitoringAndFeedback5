package com.example.monitoringandfeedback5;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private PrimeReceiver primeReceiver;
    private TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.display);
        final EditText et = findViewById(R.id.input);

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PrimeService.class);
                String input = et.getText().toString();
                if (input.equals("")) input = "1000";
                i.putExtra(PrimeService.COUNT_TO, Integer.parseInt(input));
                startForegroundService(i);
            }
        });
        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PrimeService.class);
                stopService(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(primeReceiver = new PrimeReceiver(), new IntentFilter(PrimeService.ACTION_PRIME_RESULT));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(primeReceiver);
    }

    public class PrimeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int primeNumber = intent.getIntExtra(PrimeService.PRIME, 2);
            tv.setText(String.valueOf(primeNumber));
        }
    }
}
