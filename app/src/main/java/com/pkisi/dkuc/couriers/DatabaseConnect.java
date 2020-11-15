package com.pkisi.dkuc.couriers;

import android.os.AsyncTask;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class DatabaseConnect extends AsyncTask {
    private String result;
    private String form = "";
    public DatabaseConnect() {
    }

    public DatabaseConnect(String form) {
        this.form = form;
    }



    @Override
    protected Object doInBackground(Object[] objects) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url
                ("http://192.168.8.100:8080/rest/parcels")
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
                        /*Headers responseHeaders = response.headers();
                        for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                            System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                        }
                        System.out.println("jestem tu");*/
            System.out.println("jestem w doinb: ");
            result = response.body().string();
            //return response.body().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }

    public ArrayList<Package> getAllPackages(String a){
        ArrayList<Package> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(a);
            for (int i = 0; i < array.length(); i++) {
                Package p = new Package();
                p.setId(Integer.parseInt(array.getJSONObject(i).getString("id")));
                p.setNr_paczki(array.getJSONObject(i).getString("nr_paczki"));
                p.setKoszt(Float.parseFloat(array.getJSONObject(i).getString("koszt")));
                p.setMiasto(array.getJSONObject(i).getString("miasto"));
                p.setUlica(array.getJSONObject(i).getString("ulica"));
                p.setNr_domu(array.getJSONObject(i).getString("nr_domu"));
                p.setNr_lokalu(array.getJSONObject(i).getString("nr_lokalu"));
                /*test = array.getJSONObject(i).getString("nr") +" "+ array.getJSONObject(i)
                        .getString("stan");*/
                list.add(p);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return list;
    }
    public ArrayList<String> getPackage(String a,String b){
        ArrayList<String> list = new ArrayList<>();
        String st = "";
        try{
            JSONArray array = new JSONArray(a);
            for (int i = 0; i < array.length(); i++) {
                if(array.getJSONObject(i).getString("nr").equals(b)){
                    st = array.getJSONObject(i).getString("koszt")+" "+array.getJSONObject(i)
                            .getString("waga");
                    list.add(st);
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return list;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    public String getResult() {
        return result;
    }
}


/* @Override
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
            *//*BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream, "iso-8859-1"));
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
            }*//*
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
    }*/