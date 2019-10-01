package com.example.androidclientsecond;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {


    /*Client*/
    private EditText input;
    private Button btnSend;
    private TextView text ;
    private Handler handler = new Handler();
    DataOutputStream outputStream;
    BufferedReader inputStream;
    Socket socket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.txt);
        input = findViewById(R.id.edtinput);
        btnSend = findViewById(R.id.btnsend);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
        try {
            log("Connecting !!!");
            socket = new Socket("192.168.1.33",9000);
            log("Connected !!!");

            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            log("Buffers initialized !!!");
//            outputStream.write("Hello from client :))\n".getBytes());
            while (true){
                String message = inputStream.readLine();
                log(message);
            }

        }
        catch (UnknownHostException e) {
            log("Error: Host Exeption !!!");
            e.printStackTrace();
        }
        catch (IOException e) {
            log("Error: IO Exeption !!!");
            e.printStackTrace();
        }
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(outputStream == null){
                    return;
                }
                try {
                    String message = input.getText().toString()+"\n";
                    outputStream.write(message.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }
    private void log(final String message){
        Long timeStamp = System.currentTimeMillis();
        final Long time = timeStamp %1000000;
        handler.post(new Runnable() {
            @Override
            public void run() {
                text.setText(text.getText()+"\n@"+time+":"+message);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
