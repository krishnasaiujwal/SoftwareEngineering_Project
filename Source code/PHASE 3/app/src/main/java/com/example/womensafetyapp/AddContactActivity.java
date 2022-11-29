package com.example.womensafetyapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddContactActivity extends AppCompatActivity {
    Button addbtn;
    EditText contact_person;
    EditText contact_phoneno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        contact_person=(EditText)findViewById(R.id.phoneno);
        contact_phoneno=(EditText)findViewById(R.id.contact_phoneno);
        addbtn=(Button)findViewById(R.id.submit);
        addbtn.setOnClickListener(new AddButtonlick());

    }
    class AddButtonlick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
          if(contact_person.getText().toString().length()==0)   {
                Toast.makeText(getApplicationContext(),"Fill contact person name",Toast.LENGTH_LONG).show();
                return;
          }
          if(contact_phoneno.getText().toString().length()==0) {
                Toast.makeText(getApplicationContext(),"Fill contact phoneno",Toast.LENGTH_LONG).show();
                return;
          }

            contact_person.setText("");
            contact_phoneno.setText("");
            new AddContact().execute();

        }
    }
    class AddContact extends AsyncTask<String, String, String>
    {
       private ProgressDialog pDialog = new ProgressDialog(AddContactActivity.this);
        private static final String TAG_SUCCESS = "success";
        int success=0;
       @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog.setMessage("wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
      protected String doInBackground(String... args) {
            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("phoneno",StaticData.phoneno));
                params.add(new BasicNameValuePair("contact_person",contact_person.getText().toString()));
                params.add(new BasicNameValuePair("contact_phoneno",contact_phoneno.getText().toString()));
                JSONObject json = new JSONParser().makeHttpRequest(StaticData.server+"addcontact.php", "GET", params);
         success = json.getInt(TAG_SUCCESS);
            }
            catch (JSONException e)
            {

                Toast.makeText(AddContactActivity.this, "Error:"+e.toString(), Toast.LENGTH_LONG).show();
            }
            return null;
        }


        protected void onPostExecute(String file_url) {

            pDialog.dismiss();


            if (success == 1)
            {
                Toast.makeText(AddContactActivity.this,"Contact Added Successfully.",Toast.LENGTH_LONG).show();

            }
            else
            {
                Toast.makeText(AddContactActivity.this,"Contact Not Added.  Try Again!",Toast.LENGTH_LONG).show();
            }
        }
    }
}