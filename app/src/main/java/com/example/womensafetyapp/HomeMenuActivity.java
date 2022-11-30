package com.example.womensafetyapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeMenuActivity extends AppCompatActivity implements LocationListener {
    ImageButton AddContact,profile,emergencyalarm;
    ImageButton ViewContacts;
    ImageButton FakeCall;
    ImageButton SecureMe;
    ImageButton Feedback;
    ImageButton Quit;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0;

    String lat = "0.0", lon = "0.0";
    LocationManager locationManager;
    String provider;

    Location location = null;
    boolean flag;
    int nn=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);
        AddContact=(ImageButton)findViewById(R.id.AddContact);
        AddContact.setOnClickListener(new AddContactclick ());
        ViewContacts=(ImageButton)findViewById(R.id.ViewContacts);
        ViewContacts.setOnClickListener(new ViewContactsclick ());

        profile=(ImageButton)findViewById(R.id.pofile);
        profile.setOnClickListener(new Profileclick ());
        emergencyalarm=(ImageButton)findViewById(R.id.emergencyalarm);
        emergencyalarm.setOnClickListener(new EmergencyAlarmclick ());


        FakeCall=(ImageButton)findViewById(R.id.FakeCall);
        FakeCall.setOnClickListener(new FakeCallclick ());
        SecureMe=(ImageButton)findViewById(R.id.SecureMe);
        SecureMe.setOnClickListener(new SecureMeclick ());
        Feedback=(ImageButton)findViewById(R.id.Feedback);
        Feedback.setOnClickListener(new Feedbackclick ());
        Quit=(ImageButton)findViewById(R.id.Quit);
        Quit.setOnClickListener(new Quitclick ());


        if (ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        try {
            if (provider != null && !provider.equals("")) {
                location = locationManager.getLastKnownLocation(provider);
                locationManager.requestLocationUpdates(provider, 3000, 1, HomeMenuActivity.this);

                if(location!=null)
                    onLocationChanged(location);
                else
                    Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
            }

        }catch(Exception eee)
        {

        }

    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d("Latitude", "changing location");

        lat=""+location.getLatitude();
        lon=""+location.getLongitude();

        StaticData.lat=lat;
        StaticData.lon=lon;


        flag=true;

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }


    class AddContactclick implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {

            Intent i=new Intent(HomeMenuActivity.this,AddContactActivity.class);
            startActivity(i);
        }
    }

    class ViewContactsclick implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {

            Intent i=new Intent(HomeMenuActivity.this,ViewContactsActivity.class);
            startActivity(i);
        }
    }


    class Profileclick implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {

            Intent i=new Intent(HomeMenuActivity.this,ProfileActivity.class);
            startActivity(i);
        }
    }


    class EmergencyAlarmclick implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            nn=1; //emergency alarm clicked
            new LoadContacts().execute();

        }
    }
    class FakeCallclick implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            Intent i=new Intent(HomeMenuActivity.this,FakeCallActivity.class);
            startActivity(i);
        }
    }

    class SecureMeclick implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            nn=2; //secureme clicked
            new LoadContacts().execute();

        }
    }

    class Feedbackclick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            Intent i=new Intent(HomeMenuActivity.this,AddFeedbackActivity.class);
            startActivity(i);
        }
    }

    class Quitclick implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {

            Intent i=new Intent(HomeMenuActivity.this,MainActivity.class);
            startActivity(i);
            finishAffinity();
        }
    }

    class LoadContacts extends AsyncTask<String, String, String>
    {

        private ProgressDialog pDialog = new ProgressDialog(HomeMenuActivity.this);
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

                //Toast.makeText(ViewContactsActivity.this, "Error"+e.toString(), Toast.LENGTH_LONG).show();
            }
            return null;
        }


        protected void onPostExecute(String file_url)
        {
            //dismiss the dialog once done
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
                            String str="contact_person : "+s[1]+"\n"+"contact_phoneno : "+s[2];
                            al.add(str);

                            found=true;
                        if(nn==1) {
                            android.telephony.SmsManager sm = android.telephony.SmsManager.getDefault();

                            String phoneNumber = "+1" + s[2];
                            sm.sendTextMessage(phoneNumber, null, "Please help me", null, null);
                        }
                        else if(nn==2){
                            android.telephony.SmsManager sm = android.telephony.SmsManager.getDefault();

                            String phoneNumber = "+1" + s[2];

                            String add="http://maps.google.com/?daddr="+StaticData.lat+","+StaticData.lon;

                            sm.sendTextMessage(phoneNumber, null, "Track me for help: " +add, null, null);
                        }
                        }
                    }
                    if(found==true){
                        Toast.makeText(HomeMenuActivity.this,"Messages sent to your contacts",Toast.LENGTH_LONG).show();
                    }

                }
                catch(Exception eee)
                {
                    Toast.makeText(HomeMenuActivity.this,"Error : "+eee,Toast.LENGTH_LONG).show();
                }

            }
            else
            {
                Toast.makeText(HomeMenuActivity.this,"Contacts Not Found1",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                HomeMenuActivity.this);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HomeMenuActivity.this.finishAffinity();
                Intent i=new Intent(HomeMenuActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        alertDialog.setNegativeButton("No", null);

        alertDialog.setMessage("Do you want to exit?");
        alertDialog.setTitle("Alert");
        alertDialog.show();
    }


}