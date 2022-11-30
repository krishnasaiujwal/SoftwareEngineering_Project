package com.example.womensafetyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

import java.util.List;

import org.apache.http.NameValuePair;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.*;
public class AddFeedbackActivity extends AppCompatActivity {

    Button addbtn;
    Spinner tophoneno;
    EditText feedback;
    ListView listview1;
    ArrayList<String> destlst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feedback);
        listview1=(ListView)findViewById(R.id.listview1);
        tophoneno=(Spinner)findViewById(R.id.tophoneno);
        feedback=(EditText)findViewById(R.id.feedback);
        addbtn=(Button)findViewById(R.id.addbtn);
        addbtn.setOnClickListener(new AddButtonClick());
        new LoadContacts().execute();
        new ShowFeedbacks().execute();
    }

    class AddButtonClick implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            if(feedback.getText().toString().length()==0)
            {
                Toast.makeText(getApplicationContext(),"Fill  feedback",Toast.LENGTH_LONG).show();
                return;
            }

            new AddFeedback().execute();
        }
    }
    class AddFeedback extends AsyncTask<String, String, String>
    {
        private ProgressDialog pDialog = new ProgressDialog(AddFeedbackActivity.this);
        private  String TAG_SUCCESS = "success";
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
                params.add(new BasicNameValuePair("fromphoneno",StaticData.phoneno));
                params.add(new BasicNameValuePair("tophoneno",tophoneno.getSelectedItem().toString()));
                params.add(new BasicNameValuePair("feedback",feedback.getText().toString()));

                JSONObject json = new JSONParser().makeHttpRequest(StaticData.server+"AddFeedbackActivity.php", "GET", params);


               success = json.getInt(TAG_SUCCESS);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                Toast.makeText(AddFeedbackActivity.this, "Error"+e.toString(), Toast.LENGTH_LONG).show();
            }
            return null;
        }

            protected void onPostExecute(String file_url) {
            pDialog.dismiss();


            if (success == 1)
            {
                Toast.makeText(AddFeedbackActivity.this,"Feedback Added Successfully.",Toast.LENGTH_LONG).show();
                new ShowFeedbacks().execute();
            }
            else
            {
                Toast.makeText(AddFeedbackActivity.this,"Feedback Not Added.  Try Again!",Toast.LENGTH_LONG).show();
            }
        }
    }

    ///////////////
    class LoadContacts extends AsyncTask<String, String, String>
    {

        private ProgressDialog pDialog = new ProgressDialog(AddFeedbackActivity.this);
        private  String TAG_SUCCESS = "success";
        int success=0;
        String data="";

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
                JSONObject json = new JSONParser().makeHttpRequest(StaticData.server+"ViewContactsAcitivity1.php", "GET", params);
                Log.d("Create Response", json.toString());

                success = json.getInt(TAG_SUCCESS);
                if(success==1)
                {
                    data=json.getString("recs");
                }
            }
            catch (JSONException e)
            {
                Toast.makeText(AddFeedbackActivity.this, "Error"+e.toString(), Toast.LENGTH_LONG).show();
            }
            return null;
        }


        protected void onPostExecute(String file_url)
        {

            pDialog.dismiss();


            if (success == 1)
            {
                ArrayList<String> al=new ArrayList<String>();
                boolean found=false;
                try
                {
                    String rows[]=data.split("@@");
                    for(String row: rows)
                    {
                        if(row.length()>1)
                        {
                            String s[]=row.split("~");
                            String str=s[1]+"-"+s[2];
                            al.add(str);

                            found=true;

                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddFeedbackActivity.this, android.R.layout.simple_spinner_item, al);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    tophoneno.setAdapter(adapter);


                }
                catch(Exception eee)
                {
                    Toast.makeText(AddFeedbackActivity.this,"Error : "+eee,Toast.LENGTH_LONG).show();
                }

            }
            else
            {
                Toast.makeText(AddFeedbackActivity.this,"Contacts Not Found1",Toast.LENGTH_LONG).show();
            }
        }
    }

//////////////
class ShowFeedbacks extends AsyncTask<String, String, String>
{

    private ProgressDialog pDialog = new ProgressDialog(AddFeedbackActivity.this);
    private static final String TAG_SUCCESS = "success";
    int success=0;
    String recs="";

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
            JSONObject json = new JSONParser().makeHttpRequest(StaticData.server+"ShowFeedBacks.php", "GET", params);

            Log.d("Create Response", json.toString());

            success = json.getInt(TAG_SUCCESS);
            if(success==1)
            {
                recs=json.getString("recs");
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            Toast.makeText(AddFeedbackActivity.this, "Error"+e.toString(), Toast.LENGTH_LONG).show();
        }
        return null;
    }


    protected void onPostExecute(String file_url)
    {

        pDialog.dismiss();


        if (success == 1)
        {
            ArrayList<String> al=new ArrayList<String>();

            boolean found=false;
            try
            {
                String rows[]=recs.split("@@");
                for(String row: rows)
                {
                    if(row.length()>1)
                    {
                        String s[]=row.split("~");
                        String str="Date : "+s[3]+"\nFrom Phoneno: "+s[0]+"\nTo Phoneno: "+s[1]+"\nFeedback: "+s[2];
                        al.add(str);

                    }
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddFeedbackActivity.this,android.R.layout.simple_list_item_1, al);
                listview1.setAdapter(arrayAdapter);

            }
            catch(Exception eee)
            {
                Toast.makeText(AddFeedbackActivity.this,"Error : "+eee,Toast.LENGTH_LONG).show();
            }

        }
        else
        {
            Toast.makeText(AddFeedbackActivity.this,"Feedbacks Not Found",Toast.LENGTH_LONG).show();
        }
    }
}






}