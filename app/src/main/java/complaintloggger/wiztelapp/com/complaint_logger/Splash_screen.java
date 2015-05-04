package complaintloggger.wiztelapp.com.complaint_logger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class Splash_screen extends Activity {

    Boolean chk;  //******Variable to check the first time login****//
    private static int SPLASH_TIME_OUT = 3000;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

    //*** for initializing the chk value to true for first run ***//
        sp=getSharedPreferences("isonetime", Context.MODE_PRIVATE);
        editor = sp.edit();
        chk=sp.getBoolean("isonetime", true);

        //*** Thread for creating the delay for splash screen***//

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if(chk){

                    //	editor.putBoolean("isonetime",false);
                    //	editor.commit();
                    Log.d("tag", "boolean" + chk);

                    Login l=new Login(sp,editor);
                    Intent i=new Intent(Splash_screen.this,Login.class);
                    startActivity(i);
                    finish();
                }
                else{

                    Intent j=new Intent(Splash_screen.this,Home.class);
                    startActivity(j);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
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
