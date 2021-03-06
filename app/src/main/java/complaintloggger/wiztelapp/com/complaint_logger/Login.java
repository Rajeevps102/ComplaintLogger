package complaintloggger.wiztelapp.com.complaint_logger;

import android.app.Activity;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Raju on 04-05-2015.
 */
public class Login extends Activity implements View.OnClickListener {
//*******************************//
    /* id's for edittext and button
       edittext username
       edittext mail
       edittext mobile
       Button button
     */

    Button register;
    EditText uname;
    EditText mob;
    EditText email;
    Spinner spinner;
    Integer id;
    // local variables to save username and mobile number and email are defined below//

    String user_name;
    String mobile_number;
    String mail_id;
    String countryString;

Boolean server_timeout=true;
  String mobile_number_verification;
    static String sender_number;
    String number_format="+91";

    SharedPreferences splash;
    SharedPreferences.Editor editor;
    Servicehandler servicehandler=new Servicehandler();
    Fetchspinner spn=new Fetchspinner();
    static String url="http://220.227.57.26/complaint_logger/addcomplaint.php";
    ArrayList<String> spinner_list=new ArrayList<String>(); //**** list to populate the spinner****//

    SmsManager smsManager = SmsManager.getDefault();

    // otp class


    // to check mobile number and email pattern//

    String MobilePattern = "[0-9]{10}";
    String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    // default constructor //
    public Login() {
        // TODO Auto-generated constructor stub
    }

    // constructor to initialize one time check in splash screen//

    public Login(SharedPreferences sp, SharedPreferences.Editor editor2) {
        // TODO Auto-generated constructor stub
        this.splash=sp;
        this.editor=editor2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_xml);
        initializeviews();
    }
 // To initialize the view in login page and creating a spinner //

    public void initializeviews(){
        register=(Button)findViewById(R.id.button);
        uname=(EditText)findViewById(R.id.username);
        email=(EditText)findViewById(R.id.mail);
        mob=(EditText)findViewById(R.id.mobile);

        int settings = EditorInfo.TYPE_CLASS_TEXT;
        uname.setInputType(settings);
        uname.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        mob.setInputType(settings);
        mob.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        email.setInputType(settings);
        email.setImeOptions(EditorInfo.IME_ACTION_DONE);


        register.setOnClickListener(this);



        /* spinner need to be populated from the locar server..
        select country is a coustom added data
         */
        spinner=(Spinner)findViewById(R.id.spinner);




        spinner_list.add("Select country");
      //  spinner.setPrompt("country");
// calling the web service to Fetchspinner class doinbackground //
        spn.execute(url);
        ArrayAdapter<String>adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item,spinner_list);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                countryString=spinner_list.get(position);

           /*     if(spinner_list.get(0)=="Select country"){

spinner_list.remove(0);
                }  */

               // Toast.makeText(getApplicationContext(),countryString,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    //*********** to validate the user entered details like username and mobile number************//

    public boolean validate() {


        Boolean valid;
        user_name = uname.getText().toString();



        if (user_name != null && user_name.length() > 0 ) {
            valid = true;
            Log.d("user","111111111111");
        } else {
            valid = false;
        }
        return valid;
    }


     //******* to validate the mobile number pattern ****** //

    public boolean checknumber(){
        Boolean valid;
        mobile_number = mob.getText().toString();
        Log.d("number","111111111111"+mobile_number);
        if(mobile_number.length()==10&&mobile_number.matches(MobilePattern)){
            Log.d("number","111111111111");
            valid=true;
        }
        else{
            valid= false;
        }
        return  valid;
    }

    //********** to validate the email ***********//

    public boolean checkmail(){
        mail_id=email.getText().toString().trim();
        if(mail_id.matches(emailPattern)){
            return true;
        }
        else{
                          //  email.setBackgroundResource(R.drawable.edit_red_line);
            return false;
        }
    }

    // function to verify the user entered mobile number ...wil use the smsreceiver class
    public void updateui(final String sender_number) {

mobile_number_verification=sender_number;
        getsendernumber(mobile_number_verification);

    }

    public static void getsendernumber(String number){

        sender_number=number;
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
       // Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim);






        if(validate()&&checknumber()&&checkmail()){
          //  Toast.makeText(getApplicationContext(),"correct",Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"verifying your mobile number",Toast.LENGTH_SHORT).show();
            Log.d("country","33333333333330"+countryString);
            splash = getSharedPreferences("isonetime", Context.MODE_PRIVATE);
            editor = splash.edit();
            editor.putString("selectedcountryname",countryString);

            editor.commit();
            smsManager.sendTextMessage(mobile_number, null, "Complaint Logger", null, null);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d("numberrrrrr", "33333333333330" + sender_number);
                        if (sender_number.toString().equals(number_format.concat(mobile_number))) {
                            addingjsonvalues();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"you have entered wrong mobile number",Toast.LENGTH_SHORT).show();
                        }
                    }catch (NullPointerException e){
                        Toast.makeText(getApplicationContext(),"you have entered wrong mobile number",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }



                }
            },10000);





        }
           else{
            Toast.makeText(getApplicationContext(),"enter valid data",Toast.LENGTH_SHORT).show();
        }
    }

    // class to perform fetching data from server to populate spinner //

    public  class Fetchspinner extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            Log.d("raju", "" + strings[0]);
            String result = servicehandler.makeServiceCall(strings[0]);
            Log.d("result", "111111111111111111111111" + result);
            if (result == null) {
server_timeout=false;

              //  Toast.makeText(getApplicationContext(), "Server timeout", Toast.LENGTH_LONG).show();

            } else {

                try {

                    JSONArray jsonArray = null;
                    JSONObject j = null;
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        jsonArray = jsonObject.getJSONArray("country");
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for (Integer i = 0; i < jsonArray.length(); i++) {
                        j = jsonArray.getJSONObject(i);
                        try {
                            String list = j.getString("organization_country");
                            spinner_list.add(list);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
                return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(!server_timeout){
                Toast.makeText(getApplicationContext(), "Connection timeout.....", Toast.LENGTH_LONG).show();
            }
        }
    }

    // to add json values like username mobile ...etc//

    public void addingjsonvalues(){

    user_name=uname.getText().toString();
    mobile_number=mob.getText().toString();
    mail_id=email.getText().toString();

if(countryString=="Select country"){
   Toast toast= Toast.makeText(getApplicationContext(),"server offline",Toast.LENGTH_LONG);
    toast.setGravity(Gravity.CENTER, 0, 0);
    toast.show();
    return;
}
        else {
    register.setVisibility(View.INVISIBLE);

    JSONObject jsonObject = new JSONObject();
    try {
        jsonObject.put("status", "login");
        jsonObject.put("username", user_name);
        jsonObject.put("mobile", mobile_number);
        jsonObject.put("email", mail_id);
        jsonObject.put("country", countryString);
    } catch (JSONException e) {
        e.printStackTrace();

    }
    String login_url = "http://220.227.57.26/complaint_logger/login.php";
    servicehandler = new Servicehandler(Login.this, jsonObject, login_interface);

    servicehandler.execute(login_url);
}
    }


    // implementing the interface for json response for userdata//

Login_interface login_interface=new Login_interface() {
    @Override
    public void oncompletion(JSONObject json) {

if(json==null){
    Toast toast=Toast.makeText(getApplicationContext(),"server offline",Toast.LENGTH_LONG);
    toast.setGravity(Gravity.CENTER, 0, 0);

    toast.show();

register.setVisibility(View.VISIBLE);
    return;

}

        Log.d("final json from net", "111111111111" + json);
        try {
            id = json.getInt("id");
            Log.d("userid", "isssssssssss" + id);

        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        splash = getSharedPreferences("isonetime", Context.MODE_PRIVATE);
        editor = splash.edit();
        editor.putInt("id",id);
        editor.putBoolean("firstlogin",false);
        editor.commit();
        Intent home=new Intent(Login.this,Home.class);
        startActivity(home);
        finish();
    }
};



}
