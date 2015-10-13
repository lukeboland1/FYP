package com.luke.fyp;

import android.os.AsyncTask;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class addMealActivity extends AppCompatActivity {
    private DatabaseAccess db;
    private EditText mEdit;
    private EditText fat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);
        db = new DatabaseAccess(this);
        mEdit   = (EditText)findViewById(R.id.mealName);
        fat = (EditText)findViewById(R.id.fatContent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_meal, menu);
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

    public void saveMeal(View v)
    {
        new AddNewMeal().execute(mEdit.getText().toString(), fat.getText().toString());
        /*if(db.insertMeal(mEdit.getText().toString(), Integer.parseInt(fat.getText().toString())));
        {
            Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
        }
        mEdit.setText("");
        fat.setText("");*/
    }

    private class AddNewMeal extends AsyncTask<String, Object, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            boolean tof = db.insertMeal(params[0], Integer.parseInt(params[1]));
            db.close();
            return tof;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
            mEdit.setText("");
            fat.setText("");

        }
    }




}
