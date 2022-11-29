package com.example.womensafetyapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button Login;
    TextView register;
    EditText password;
    String password_1="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Login=(Button)findViewById(R.id.login);
        Login.setOnClickListener(new Loginclick ());

        password=(EditText)findViewById(R.id.password);
        register=(TextView)findViewById(R.id.register);
        register.setOnClickListener(new CreateAccount());

    }

    class CreateAccount implements View.OnClickListener
    {
       @Override
        public void onClick(View v)
        {

            Intent i=new Intent(MainActivity.this,NewAccount.class);
            startActivity(i);
        }
    }

    class Loginclick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            password_1=password.getText().toString();
            if(password_1.length()==0)
            {
                Toast.makeText(MainActivity.this,"Invalid Password",Toast.LENGTH_LONG).show();
                return;
            }
           new UserLogin().execute();
        }
    }

    class UserLogin extends AsyncTask<String, String, String>
    {
      private ProgressDialog pDialog = new ProgressDialog(MainActivity.this);
        private static final String TAG_SUCCESS = "success";
        int success;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog.setMessage("searching....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {
           try {
               List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("password",password_1));
                JSONObject json = new JSONParser().makeHttpRequest(StaticData.server+"userlogin.php", "GET", params);
              Log.d("Create Response", json.toString());
             try {

                    success = json.getInt(TAG_SUCCESS);
                    if(success==1)
                    {
                        StaticData.phoneno=json.getString("phoneno");
                        StaticData.name=json.getString("name");
                        StaticData.firstname=json.getString("firstname");
                        StaticData.lastname=json.getString("lastname");
                        StaticData.email=json.getString("email");
                        StaticData.age=json.getString("age");
                        StaticData.gender=json.getString("gender");
                        StaticData.city=json.getString("city");
                        StaticData.address=json.getString("address");
                        StaticData.password=json.getString("password");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {

                //Toast.makeText(MainActivity.this, "Error:"+e.toString(), Toast.LENGTH_LONG).show();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {

            pDialog.dismiss();

            if(success==1)
            {
                Intent i=new Intent(MainActivity.this,HomeMenuActivity.class);
                startActivity(i);
            }
            else
            {
                Toast.makeText(MainActivity.this, "Incorrect Password", Toast.LENGTH_LONG).show();
            }

        }


    }
    /////////////


}