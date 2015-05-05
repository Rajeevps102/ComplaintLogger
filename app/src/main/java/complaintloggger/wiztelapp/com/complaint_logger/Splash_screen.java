package complaintloggger.wiztelapp.com.complaint_logger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class Splash_screen extends Activity {

    Boolean chk;  //******Variable to check the first time login****//
    private static int SPLASH_TIME_OUT = 3000;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;

            //*** for initializing the chk value to true for first run ***//
            sp = getSharedPreferences("isonetime", Context.MODE_PRIVATE);
            editor = sp.edit();
            chk = sp.getBoolean("isonetime", true);

            //*** Thread for creating the delay for splash screen***//

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    if (chk) {

                        //	editor.putBoolean("isonetime",false);
                        //	editor.commit();
                        Log.d("tag", "boolean" + chk);

                        Login l = new Login(sp, editor);
                        Intent i = new Intent(Splash_screen.this, Login.class);
                        startActivity(i);
                        finish();
                    } else {

                        Intent j = new Intent(Splash_screen.this, Home.class);
                        startActivity(j);
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

}
