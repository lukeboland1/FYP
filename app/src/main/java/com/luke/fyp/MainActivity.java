package com.luke.fyp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button enterMeal;
    private Button viewMeals;
    private Button storeMealRecord;
    private Button writeFile;
    private DatabaseAccess db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseAccess(this);
        new doesUserExist().execute();
        new setUser().execute();

        setContentView(R.layout.activity_main);
        enterMeal = (Button)findViewById(R.id.enterMeal);
        viewMeals = (Button)findViewById(R.id.storedMeals);
        storeMealRecord = (Button)findViewById(R.id.storeMealentry);
        writeFile = (Button)findViewById(R.id.writeFile);
        enterMeal.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick (View view){
                    Intent intent = new Intent(getApplicationContext(), addMealActivity.class);
                    startActivity(intent);

                }
          }

        );


        viewMeals.setOnClickListener(new OnClickListener() {
                @Override
                  public void onClick (View view){
                    Intent intent = new Intent(getApplicationContext(), viewMealsActivity.class);
                    startActivity(intent);

                }
               }

        );

        storeMealRecord.setOnClickListener(new OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   Intent intent = new Intent(getApplicationContext(), storeMealRecordActivity.class);
                                                   startActivity(intent);

                                               }
                                           }

        );

        writeFile.setOnClickListener(new OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   Intent intent = new Intent(getApplicationContext(), writeFileActivity.class);
                                                   startActivity(intent);

                                               }
                                           }

        );

        }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
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

    private class doesUserExist extends AsyncTask<Integer, Object, Boolean> {

        @Override
        protected Boolean doInBackground(Integer... params) {
            if(!db.doesUserExist())
            {
                Intent intent = new Intent(getApplicationContext(), firstWelcomeActivity.class);
                startActivity(intent);
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

        }
    }


    private class setUser extends AsyncTask<Integer, Object, Boolean> {

        @Override
        protected Boolean doInBackground(Integer... params) {
            ((MyApp) MainActivity.this.getApplication()).setUser(db.getUser());
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

        }
    }
}