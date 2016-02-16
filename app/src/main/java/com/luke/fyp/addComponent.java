package com.luke.fyp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class addComponent extends AppCompatActivity {
    private DatabaseAccess db;
    private EditText mEdit;
    private EditText fat;
    String servingType;
    boolean canStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_component);
        db = new DatabaseAccess(this);
        mEdit   = (EditText)findViewById(R.id.componentName);
        fat = (EditText)findViewById(R.id.fatPerServing);
        String[] items = {"Millilitres", "Grams"};
        Spinner dropdown = (Spinner)findViewById(R.id.spinnerServingType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                String item =(String) arg0.getItemAtPosition(arg2);
                servingType = item;
                canStore = true;
                if(servingType.equals("Grams"))
                {
                    fat.setHint("Fat per 100 grams");
                }
                else if(servingType.equals("Millilitres"))
                {
                    fat.setHint("Fat per 100 millilitres");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {


            }
        });
    }



    public void saveComponent(View v)
    {
        if(!fat.getText().equals("")) {
            new AddNewComponent().execute(mEdit.getText().toString(), fat.getText().toString());
        }
        else
        {
            Toast.makeText(addComponent.this, "Please enter fat per serving", Toast.LENGTH_SHORT).show();
        }
    }

    private class AddNewComponent extends AsyncTask<String, Object, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            boolean tof = db.insertComponent(params[0], Integer.parseInt(params[1]), servingType);
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











    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_component, menu);
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
