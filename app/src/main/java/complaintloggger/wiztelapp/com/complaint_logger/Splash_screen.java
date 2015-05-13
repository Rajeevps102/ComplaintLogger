package complaintloggger.wiztelapp.com.complaint_logger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
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

    static String url = "http://10.0.0.127/complaintlogger/fetchorglist.php";
    ArrayList<String> oganization_list = new ArrayList<String>(); //**** list to populate the spinner****//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        boolean connected = false;


//the following code checks for internet connectivity of the device and redirects to the home page if network is available and login form is filled
        ConnectivityManager connect = null;
        connect = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);

        if (connect != null) {
            NetworkInfo result = connect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (result != null && result.isConnectedOrConnecting())
            {
                Toast.makeText(getApplicationContext(), "network  available", Toast.LENGTH_LONG).show();
            }
            else {
                result = connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (result != null && result.isConnectedOrConnecting()) {
                    Toast.makeText(getApplicationContext(), "network  available", Toast.LENGTH_LONG).show();


                    //for initializing the chk value to true for first run
                    sp = getSharedPreferences("isonetime", Context.MODE_PRIVATE);
                    editor = sp.edit();
                    chk = sp.getBoolean("firstlogin", true);


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
                                Intent i = new Intent(Splash_screen.this, Home.class); // has to corrected as intent to home.class pn completion of test
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
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            System.exit(0);
                        }
                    }, SPLASH_TIME_OUT);
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "network not available", Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    System.exit(0);
                }
            }, SPLASH_TIME_OUT);
        }


    }



}
