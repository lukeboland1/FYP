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
import android.widget.Toast;

public class settings extends AppCompatActivity {
    private DatabaseAccess db;
    private EditText fatPerCreon;
    private String creonType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        db = new DatabaseAccess(this);
        fatPerCreon = (EditText)findViewById(R.id.fatPerCreonSettings);
        Spinner dropdown = (Spinner)findViewById(R.id.spinnerSettings);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"10000","25000","Both"});
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                creonType = (String) arg0.getItemAtPosition(arg2);
                if (creonType.equals("25000")) {
                    fatPerCreon.setHint("Fat per CREON 25,000 tablet");
                } else {
                    fatPerCreon.setHint("Fat per CREON 10,000 tablet");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            super.onBackPressed();
            return true;
        }
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    public boolean addUser(View v)
    {
        if(fatPerCreon.getText().toString().equals(""))
        {
            Toast.makeText(settings.this, "Please enter fat per creon", Toast.LENGTH_SHORT).show();
        }

        else {
            new update().execute((creonType), fatPerCreon.getText().toString());
            this.finish();
        }

        return true;

    }

    private class update extends AsyncTask<String, Object, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            db.updateUser((params[0]), Double.parseDouble(params[1]));
            ((MyApp) settings.this.getApplication()).setUser(db.getUser());

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();

            fatPerCreon.setText("");


        }
    }
}
