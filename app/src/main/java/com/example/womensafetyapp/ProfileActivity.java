package com.example.womensafetyapp;

import androidx.appcompat.app.AppCompatActivity;

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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileActivity extends AppCompatActivity {
    Button accountbtn;
    EditText phoneno;
    EditText firstname;
    EditText lastname;
    EditText age;
    Spinner gender;
    EditText email;
    EditText city;
    EditText address,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        phoneno=(EditText)findViewById(R.id.phoneno);
        firstname=(EditText)findViewById(R.id.firstname);
        lastname=(EditText)findViewById(R.id.lastname);
        age=(EditText)findViewById(R.id.age);
        gender=(Spinner) findViewById(R.id.gender);
        email=(EditText)findViewById(R.id.email);
        city=(EditText)findViewById(R.id.city);
        address=(EditText)findViewById(R.id.address);
        password=(EditText)findViewById(R.id.password);

        phoneno.setText(StaticData.phoneno);
        firstname.setText(StaticData.firstname);
        lastname.setText(StaticData.lastname);
        age.setText(StaticData.age);
        if(StaticData.gender.equalsIgnoreCase("male")){
            gender.setSelection(0);
        }
        else{
            gender.setSelection(1);
        }

        email.setText(StaticData.email);
        city.setText(StaticData.city);
        address.setText(StaticData.address);
        password.setText(StaticData.password);


        accountbtn=(Button)findViewById(R.id.accountbtn);
        accountbtn.setOnClickListener(new AccountButtonClick());
    }

    class AccountButtonClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            if(phoneno.getText().toString().length()==0) {
                Toast.makeText(ProfileActivity.this,"Fill phoneno",Toast.LENGTH_LONG).show();
                return;
            }
            if(firstname.getText().toString().length()==0) {
                Toast.makeText(ProfileActivity.this,"Fill firstname",Toast.LENGTH_LONG).show();
                return;
            }
            if(lastname.getText().toString().length()==0){
                Toast.makeText(ProfileActivity.this,"Fill lastname",Toast.LENGTH_LONG).show();
                return;
            }
            if(age.getText().toString().length()==0) {
                Toast.makeText(ProfileActivity.this,"Fill age",Toast.LENGTH_LONG).show();
                return;
            }
            if(gender.getSelectedItem().toString().length()==0)
            {
                Toast.makeText(ProfileActivity.this,"Fill gender",Toast.LENGTH_LONG).show();
                return;
            }

            if(email.getText().toString().length()==0)
            {
                Toast.makeText(ProfileActivity.this,"Fill email",Toast.LENGTH_LONG).show();
                return;
            }
            if(validateEmail(email.getText().toString())==false)
            {
                Toast.makeText(ProfileActivity.this,"Invalid email",Toast.LENGTH_LONG).show();
                return;
            }
            if(city.getText().toString().length()==0)
            {
                Toast.makeText(ProfileActivity.this,"Fill city",Toast.LENGTH_LONG).show();
                return;
            }

            if(address.getText().toString().length()==0)
            {
                Toast.makeText(ProfileActivity.this,"Fill address",Toast.LENGTH_LONG).show();
                return;
            }
            if(password.getText().toString().length()==0)
            {
                Toast.makeText(ProfileActivity.this,"Fill password",Toast.LENGTH_LONG).show();
                return;
            }


            new UpdateProfile().execute();

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


    class UpdateProfile extends AsyncTask<String, String, String>
    {
        private ProgressDialog pDialog = new ProgressDialog(ProfileActivity.this);
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
                params.add(new BasicNameValuePair("phoneno",StaticData.phoneno));
                params.add(new BasicNameValuePair("firstname",firstname.getText().toString()));
                params.add(new BasicNameValuePair("lastname",lastname.getText().toString()));
                params.add(new BasicNameValuePair("age",age.getText().toString()));
                params.add(new BasicNameValuePair("gender",gender.getSelectedItem().toString()));
                params.add(new BasicNameValuePair("email",email.getText().toString()));

                params.add(new BasicNameValuePair("city",city.getText().toString()));
                params.add(new BasicNameValuePair("address",address.getText().toString()));
                params.add(new BasicNameValuePair("password",password.getText().toString()));
                JSONObject json = new JSONParser().makeHttpRequest(StaticData.server+"update_profile.php", "GET", params);
                Log.d("Create Response", json.toString());
                try {
                    success = json.getInt(TAG_SUCCESS);
                    if(success==1)
                    {

                        StaticData.firstname=json.getString("firstname");
                        StaticData.lastname=json.getString("lastname");
                        StaticData.name = StaticData.firstname +" " + StaticData.lastname;
                        StaticData.email=json.getString("email");
                        StaticData.age=json.getString("age");
                        StaticData.gender=json.getString("gender");
                        StaticData.city=json.getString("city");
                        StaticData.address=json.getString("address");
                        StaticData.password=json.getString("password");

                    }



                } catch (JSONException e) {

                }
            } catch (Exception e) {

                Toast.makeText(ProfileActivity.this, "Error:"+e.toString(), Toast.LENGTH_LONG).show();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(success==1)
            {
                Toast.makeText(ProfileActivity.this, "Account updated successfully", Toast.LENGTH_LONG).show();
                Intent i=new Intent(ProfileActivity.this,HomeMenuActivity.class);
                startActivity(i);
            }
            else
            {
                Toast.makeText(ProfileActivity.this, "Account not updated successfully", Toast.LENGTH_LONG).show();
            }

        }
    }

}