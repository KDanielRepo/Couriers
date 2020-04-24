package com.pkisi.dkuc.couriers;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class DatabaseConnect extends AsyncTask<String,Void,String> {
    public DatabaseConnect(){}
    public DatabaseConnect(String form){
        this.form = form;
    }

    String result = "";
    String form = "";
    @Override
    protected String doInBackground(String... strings) {
        String login_url = strings[0];
        //String select = strings[1];
        //String from = strings[2];
        try {
            URL url = new URL(login_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            //OutputStream stream = connection.getOutputStream();
            /*BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream, "iso-8859-1"));
            if(form.equals("get")){
                String postData = URLEncoder.encode("select", "UTF-8") +"="+ URLEncoder.encode(select, "UTF-8") + "&" + URLEncoder.encode("from", "UTF-8") +"="+ URLEncoder.encode(from, "UTF-8");
                writer.write(postData);
                writer.flush();
                writer.close();
                stream.close();
            }else if(form.equals("send")){
                String postData = URLEncoder.encode("update", "UTF-8") +"="+ URLEncoder.encode
                        (select, "UTF-8") + "&" + URLEncoder.encode("set", "UTF-8") +"="+
                        URLEncoder.encode(from, "UTF-8");
                writer.write(postData);
                writer.flush();
                writer.close();
                stream.close();
            }*/
            //System.out.println(postData);
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"),8);
            String line = "";
            while ((line = reader.readLine()) != null) {
                result += line;
            }
            reader.close();
            inputStream.close();
            connection.disconnect();
            try{
                JSONArray array = new JSONArray(result);
                System.out.println(result);
                System.out.println(array);
                String test = "";
                for (int i = 0; i < array.length(); i++) {
                    test = array.getJSONObject(i).getString("imie_prac");
                }
                System.out.println(test);
            }catch (Exception e){
                e.printStackTrace();
            }

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getPhpResult(String a)throws JSONException {
        JSONArray array = new JSONArray(a);
        System.out.println(a);
        System.out.println(array);
        String test = "";
        for (int i = 0; i < array.length(); i++) {
            test = array.getJSONObject(i).getString("imie_prac");
        }
        return test;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(String aVoid) {
        System.out.println(aVoid);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
