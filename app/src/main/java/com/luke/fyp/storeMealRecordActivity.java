package com.luke.fyp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class storeMealRecordActivity extends AppCompatActivity {
    private DatabaseAccess db;
    private EditText creonTaken;
    private ArrayList<Meal> meals;
    private EditText notes;
    private String mealName;
    private TextView suggestedCreon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_meal_record);
        final User u = ((MyApp) this.getApplication()).getUser();
        db = new DatabaseAccess(this);
        creonTaken   = (EditText)findViewById(R.id.creonTaken1);
        notes   = (EditText)findViewById(R.id.notes1);
        suggestedCreon   = (TextView)findViewById(R.id.suggestedCreon);
        /*meals = db.getAllMeals();
        String[] items = new String[meals.size()];
        for(int i = 0; i < meals.size(); i++)
        {
            items[i] = (meals.get(i).getName());
        }
        Spinner dropdown = (Spinner)findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                String item =(String) arg0.getItemAtPosition(arg2);
                mealName = item;
                boolean found = false;
                int fat = 0;
                for(int i = 0; i < meals.size() && !found; i++)
                {
                    if(meals.get(i).getName().equals(mealName))
                    {
                        fat = meals.get(i).getFatContent();
                        found = true;
                    }
                }
                int creon = fat / u.getFatPerCreon();
                suggestedCreon.setText("Suggested Creon = " + creon);



            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {


            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_store_meal_record, menu);
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

    public boolean storeMealRecord(View v)
    {
        new storeMealRecord().execute(mealName, creonTaken.getText().toString(), notes.getText().toString());
        return true;
    }

    private class storeMealRecord extends AsyncTask<String, Object, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            Calendar calendar = Calendar.getInstance();
            long datetime = calendar.getTimeInMillis();
            //boolean tof = db.storeMealRecord(params[0], Integer.parseInt(params[1]), params[2], datetime);
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
            creonTaken.setText("");
            notes.setText("");
            suggestedCreon.setText("");

        }
    }

    private class getAllMeals extends AsyncTask<String, Object, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            //meals = db.getAllMeals();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);


        }
    }
}
