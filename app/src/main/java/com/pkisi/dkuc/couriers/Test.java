package com.pkisi.dkuc.couriers;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class Test extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            AsyncTask asyncTask = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder().url
                            ("http://192.168.8.100:8080/rest/workers")
                            .build();
                    Response response = null;
                    try {
                        response = client.newCall(request).execute();
                        /*Headers responseHeaders = response.headers();
                        for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                            System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                        }
                        System.out.println("jestem tu");*/
                        System.out.println(response.body().string());
                        return response.body().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                }
            }.execute();
        }
    }
}
