package com.example.tariq.githubcommitapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.json.JSONException;
import java.io.IOException;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    public boolean isStringEmpty(String response){
        if(response.isEmpty()){
            return true;
        }else
            return false;
    }

    public void createTableRow(TableLayout tl, String[][] topNArrays){

        for (int i = 0; i < topNArrays.length; i++){
            String[] temp = new String[3];
            temp = topNArrays[i];

            String commitID = temp[0];
            String shorthandCommitID = commitID.substring(0,7);
            String name = temp[1];
            String date = temp[2];

            TableRow tr = new TableRow(this);

            TextView tv1 = new TextView(this);
            tv1.setBackgroundColor(getResources().getColor(R.color.white));
            tv1.setText(name);
            tv1.setPadding(1,1,1,1);

            TextView tv2 = new TextView(this);
            tv2.setBackgroundColor(getResources().getColor(R.color.white));
            tv2.setText(date);
            tv2.setPadding(0,1,160,1);

            TextView tv3 = new TextView(this);
            tv3.setBackgroundColor(getResources().getColor(R.color.white));
            tv3.setText(shorthandCommitID);
            tv3.setPadding(0,1,1,1);

            tr.addView(tv1);
            tr.addView(tv2);
            tr.addView(tv3);

            tl.addView(tr, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

        }

    }

    public String[][] callGithubAPI(String myResponse){

        boolean boolean_flag;

        boolean_flag = isStringEmpty(myResponse);

        if (!boolean_flag) {

            try {
                JSONArray jsonArray = new JSONArray(myResponse);
                int topNCommits = jsonArray.length();
                String[][] topArrays = new String[topNCommits][];

                for (int index = 0; index < jsonArray.length(); index++) {
                    String[] commitArray = new String[3];
                    String commitID = jsonArray.getJSONObject(index).getString("sha");
                    commitArray[0] = commitID;

                    JSONObject jsonobject = jsonArray.getJSONObject(index).getJSONObject("commit").getJSONObject("committer");
                    String name = jsonobject.getString("name");
                    commitArray[1] = name;
                    String date = jsonobject.getString("date");
                    commitArray[2] = date;
                    topArrays[index] = commitArray;
                    Log.i("top", Arrays.toString(commitArray));
                }
                return topArrays;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        final TableLayout tableLayout = findViewById(R.id.commitTable);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient client = new OkHttpClient();
                        okhttp3.Request request = new okhttp3.Request.Builder()
                                .url("https://api.github.com/repos/poynt/PoyntSamples/commits?per_page=10")
                                .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.isSuccessful()){
                            final String myresponse = response.body().string();
                            Log.i("myrespnse", myresponse);
                            final String[][] topNArrays = callGithubAPI(myresponse);

                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        tableLayout.setColumnCollapsed(0, false);
                                        tableLayout.setColumnCollapsed(1, false);
                                        tableLayout.setColumnCollapsed(2, false);
                                        createTableRow(tableLayout, topNArrays);

                                    }
                                });
                        }
                    }
                });
            }//onclick
        });//setOnClickListener
    }//onCreate




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
