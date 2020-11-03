package com.example.inventoryui.Controllers;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;

import com.example.inventoryui.R;
import com.here.oksse.OkSse;
import com.here.oksse.ServerSentEvent;

import okhttp3.Request;
import okhttp3.Response;

public class TestActivity extends AppCompatActivity {

    private Button start;
    private TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_sse);
        start = (Button) findViewById(R.id.start);
        output = (TextView) findViewById(R.id.output);
       // client = new OkHttpClient();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start();
                startOksse();
            }
        });

    }

    private void startOksse(){

        ServerSentEvent.Listener listener=new ServerSentEvent.Listener() {
            @Override
            public void onOpen(ServerSentEvent sse, Response response) {
                // When the channel is opened
            }

            @Override
            public void onMessage(ServerSentEvent sse, String id, String event, String message) {
                // When a message is received
                output(event);
            }

            @WorkerThread
            @Override
            public void onComment(ServerSentEvent sse, String comment) {
                // When a comment is received
            }

            @WorkerThread
            @Override
            public boolean onRetryTime(ServerSentEvent sse, long milliseconds) {
                return true; // True to use the new retry time received by SSE
            }

            @WorkerThread
            @Override
            public boolean onRetryError(ServerSentEvent sse, Throwable throwable, Response response) {
                return true; // True to retry, false otherwise
            }

            @WorkerThread
            @Override
            public void onClosed(ServerSentEvent sse) {
                // Channel closed
            }

            @Override
            public Request onPreRetry(ServerSentEvent sse, Request originalRequest) {
                return null;
            }
        };

        String path="http://192.168.1.2:8080/test";
            Request request = new Request.Builder().url(path).build();
            OkSse okSse = new OkSse();
            ServerSentEvent sse = okSse.newServerSentEvent(request, listener);

    }


    private void output(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                output.setText(output.getText().toString() + "\n\n" + txt);
            }
        });
    }
}