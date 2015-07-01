package complaintloggger.wiztelapp.com.complaint_logger;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Splash_screen extends Activity {

    Boolean chk;  //******Variable to check the first time login****//
    private static int SPLASH_TIME_OUT = 3000;
    SharedPreferences sp;

    SharedPreferences.Editor editor;
    Servicehandler servicehandler = new Servicehandler();

  //  static String url = "http://10.0.0.128/complaintlogger/fetchorglist.php";
    ArrayList<String> oganization_list = new ArrayList<String>(); //**** list to populate the spinner****//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        boolean connected = false;
        //for initializing the chk value to true for first run
        sp = getSharedPreferences("isonetime", Context.MODE_PRIVATE);
        editor = sp.edit();
        chk = sp.getBoolean("firstlogin", true);


//the following code checks for internet connectivity of the device and redirects to the home page if network is available and login form is filled
        ConnectivityManager connect = null;
        connect = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        final boolean is3G = connect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isAvailable();
        final boolean isWifi = connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

        Log.d("rajeev",""+is3G+""+isWifi);

        if (connect != null) {
            NetworkInfo result = (connect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE));
            if (result != null && result.isConnectedOrConnecting()) {
             //   Toast.makeText(getApplicationContext(), "Mobile network  available", Toast.LENGTH_LONG).show();



                //Thread for creating the delay for splash screen

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (chk) {


                            //	editor.putBoolean("isonetime",false);
                            //	editor.commit();
                            Log.d("tag", "boolean" + chk);

                            Login l = new Login(sp, editor);
                            Intent i = new Intent(Splash_screen.this, App_intro.class); // has to corrected as intent to home.class pn completion of test
                            startActivity(i);
                            finish();
                        } else {

                            Intent i = new Intent(Splash_screen.this, Home.class); // has to corrected as intent to home.class pn completion of test
                            startActivity(i);
                            finish();


                        }
                    }
                }, SPLASH_TIME_OUT);


            }
            else {
                result = connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (result != null && result.isConnectedOrConnecting()) {
                  //  Toast.makeText(getApplicationContext(), "network  available", Toast.LENGTH_LONG).show();


                    //for initializing the chk value to true for first run
                /*    sp = getSharedPreferences("isonetime", Context.MODE_PRIVATE);
                    editor = sp.edit();
                    chk = sp.getBoolean("firstlogin", true); */


                    //Thread for creating the delay for splash screen

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            if (chk) {


                                //	editor.putBoolean("isonetime",false);
                                //	editor.commit();
                                Log.d("tag", "boolean" + chk);

                                Login l = new Login(sp, editor);
                                Intent i = new Intent(Splash_screen.this, App_intro.class); // has to corrected as intent to home.class pn completion of test
                                startActivity(i);
                                finish();
                            } else {

                                Intent i = new Intent(Splash_screen.this, Home.class); // has to corrected as intent to home.class pn completion of test
                                startActivity(i);
                                finish();


                            }
                        }
                    }, SPLASH_TIME_OUT);
                } else {
                    Toast.makeText(getApplicationContext(), "network not available", Toast.LENGTH_LONG).show();
                 /*   Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    startActivity(i);  */

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                         /*  Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.setClassName("com.android.phone", "com.android.phone.Settings");
                            startActivity(intent);  */

                           /* Intent intent = new Intent();
                            intent.setClassName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                            startActivity(intent);  */
                            if(is3G) {
                                Intent gpsOptionsIntent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                                startActivityForResult(gpsOptionsIntent, 0);
                                //  System.exit(0);
                            }
                            else{
                                Intent gpsOptionsIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                startActivityForResult(gpsOptionsIntent, 1);
                            }
                        }
                    }, SPLASH_TIME_OUT);
                }
            }
        }

        else {
          //  Toast.makeText(getApplicationContext(), "network not available", Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                 /*   Intent gpsOptionsIntent = new Intent(
                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(gpsOptionsIntent);  */


                    System.exit(0);
                }
            }, SPLASH_TIME_OUT);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(requestCode==0) {

           Toast.makeText(getApplicationContext(), "press back to quit", Toast.LENGTH_LONG).show();
           Intent intent = new Intent(Splash_screen.this, Splash_screen.class);

           startActivity(intent);
       }
       else if(requestCode==1) {
           Toast.makeText(getApplicationContext(), "press back to quit", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Splash_screen.this, Splash_screen.class);

            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);

    }
}
