package com.example.womensafetyapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewAccount extends AppCompatActivity {
    Button accountbtn;
    EditText phoneno;
    EditText firstname;
    EditText lastname;
    EditText age;
    Spinner gender;
    EditText email;
    EditText city;
    EditText address,password,cpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        phoneno=(EditText)findViewById(R.id.phoneno);
        firstname=(EditText)findViewById(R.id.firstname);
        lastname=(EditText)findViewById(R.id.lastname);
        age=(EditText)findViewById(R.id.age);
        gender=(Spinner) findViewById(R.id.gender);
        email=(EditText)findViewById(R.id.email);
        city=(EditText)findViewById(R.id.city);
        address=(EditText)findViewById(R.id.address);
        password=(EditText)findViewById(R.id.password);
        cpassword=(EditText)findViewById(R.id.cpassword);
        accountbtn=(Button)findViewById(R.id.accountbtn);
        accountbtn.setOnClickListener(new AccountButtonClick());
    }

    class AccountButtonClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
        if(phoneno.getText().toString().length()==0) {
                Toast.makeText(NewAccount.this,"Fill phoneno",Toast.LENGTH_LONG).show();
                return;
            }
            if(firstname.getText().toString().length()==0) {
                Toast.makeText(NewAccount.this,"Fill firstname",Toast.LENGTH_LONG).show();
                return;
            }
            if(lastname.getText().toString().length()==0){
                Toast.makeText(NewAccount.this,"Fill lastname",Toast.LENGTH_LONG).show();
                return;
            }
            if(age.getText().toString().length()==0) {
                Toast.makeText(NewAccount.this,"Fill age",Toast.LENGTH_LONG).show();
                return;
            }
            if(gender.getSelectedItem().toString().length()==0)
            {
                Toast.makeText(NewAccount.this,"Fill gender",Toast.LENGTH_LONG).show();
                return;
            }

            if(email.getText().toString().length()==0)
            {
                Toast.makeText(NewAccount.this,"Fill email",Toast.LENGTH_LONG).show();
                return;
            }
            if(validateEmail(email.getText().toString())==false)
            {
                Toast.makeText(NewAccount.this,"Invalid email",Toast.LENGTH_LONG).show();
                return;
            }
          if(city.getText().toString().length()==0)
            {
                Toast.makeText(NewAccount.this,"Fill city",Toast.LENGTH_LONG).show();
                return;
            }

            if(address.getText().toString().length()==0)
            {
                Toast.makeText(NewAccount.this,"Fill address",Toast.LENGTH_LONG).show();
                return;
            }
          if(password.getText().toString().length()==0)
            {
                Toast.makeText(NewAccount.this,"Fill password",Toast.LENGTH_LONG).show();
                return;
            }
            String password1=password.getText().toString();
            if(!password1.equals(cpassword.getText().toString()))
            {
                Toast.makeText(NewAccount.this,"passwords not match",Toast.LENGTH_LONG).show();
                return;
            }

            new UserRegistration().execute();

        }
    }

    public static boolean validateEmail(String email) {
        boolean valid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            valid = true;
        }
        return valid;
    }

    class UserRegistration extends AsyncTask<String, String, String>
    {
        private ProgressDialog pDialog = new ProgressDialog(NewAccount.this);
        private static final String TAG_SUCCESS = "success";
        int success;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           pDialog.setMessage("please wait....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
     protected String doInBackground(String... args) {
        try {
             List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("phoneno",phoneno.getText().toString()));
                params.add(new BasicNameValuePair("firstname",firstname.getText().toString()));
                params.add(new BasicNameValuePair("lastname",lastname.getText().toString()));
                params.add(new BasicNameValuePair("age",age.getText().toString()));
                params.add(new BasicNameValuePair("gender",gender.getSelectedItem().toString()));
                params.add(new BasicNameValuePair("email",email.getText().toString()));

                params.add(new BasicNameValuePair("city",city.getText().toString()));
                params.add(new BasicNameValuePair("address",address.getText().toString()));
                params.add(new BasicNameValuePair("password",password.getText().toString()));
                JSONObject json = new JSONParser().makeHttpRequest(StaticData.server+"user_reg.php", "GET", params);
               Log.d("Create Response", json.toString());
               try {
                  success = json.getInt(TAG_SUCCESS);
                } catch (JSONException e) {

                }
           } catch (Exception e) {

                Toast.makeText(NewAccount.this, "Error:"+e.toString(), Toast.LENGTH_LONG).show();
            }
         return null;
        }
       protected void onPostExecute(String file_url) {
          pDialog.dismiss();
             if(success==1)
            {
                Intent i=new Intent(NewAccount.this,MainActivity.class);
                startActivity(i);
            }
            else
            {
                Toast.makeText(NewAccount.this, "Account created successfully", Toast.LENGTH_LONG).show();
            }

        }
    }


}