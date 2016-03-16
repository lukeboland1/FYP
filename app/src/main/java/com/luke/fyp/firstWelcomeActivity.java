package com.luke.fyp;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class firstWelcomeActivity extends AppCompatActivity {
    private DatabaseAccess db;
    private EditText fatPerCreon;
    private String creonType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_welcome);
        db = new DatabaseAccess(this);
        fatPerCreon = (EditText)findViewById(R.id.fatPerCreon);
        Spinner dropdown = (Spinner)findViewById(R.id.spinner3);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"10000","25000","Both"});
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                creonType = (String) arg0.getItemAtPosition(arg2);
                if(creonType.equals("25000"))
                {
                    fatPerCreon.setHint("Fat per CREON 25,000 tablet");
                }
                else
                {
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
        getMenuInflater().inflate(R.menu.menu_first_welcome, menu);
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

    public boolean addUser(View v)
    {
        new AddNewUser().execute((creonType), fatPerCreon.getText().toString());
        this.finish();
        return true;

    }


    private class AddNewUser extends AsyncTask<String, Object, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            boolean tof = db.addUser((params[0]), Double.parseDouble(params[1]));
            ((MyApp) firstWelcomeActivity.this.getApplication()).setUser(db.getUser());
            return tof;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();

            fatPerCreon.setText("");


        }
    }
}
