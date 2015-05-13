package complaintloggger.wiztelapp.com.complaint_logger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    SharedPreferences splash;
    SharedPreferences.Editor editor;
    Servicehandler servicehandler=new Servicehandler();
    Fetchspinner spn=new Fetchspinner();
    static String url="http://10.0.0.127/complaintlogger/addcomplaint.php";
    ArrayList<String> spinner_list=new ArrayList<String>(); //**** list to populate the spinner****//

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

                if(countryString=="Select country"){


                }

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

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

        if(validate()&&checknumber()&&checkmail()){
          //  Toast.makeText(getApplicationContext(),"correct",Toast.LENGTH_SHORT).show();
            Log.d("country","33333333333330"+countryString);
            splash = getSharedPreferences("isonetime", Context.MODE_PRIVATE);
            editor = splash.edit();
            editor.putString("selectedcountryname",countryString);

            editor.commit();

            addingjsonvalues();
        }
           else{
            Toast.makeText(getApplicationContext(),"Incorrect",Toast.LENGTH_SHORT).show();
        }
    }

    // class to perform fetching data from server to populate spinner //

    public  class Fetchspinner extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            Log.d("raju",""+strings[0]);
          String result= servicehandler.makeServiceCall(strings[0]);
            Log.d("result","111111111111111111111111"+result);

            try {
                JSONArray jsonArray=null;
                JSONObject j=null;
                JSONObject jsonObject = new JSONObject(result);
                jsonArray=jsonObject.getJSONArray("country");

                for(Integer i=0;i<jsonArray.length();i++){
                    j=jsonArray.getJSONObject(i);
                    String list=j.getString("organization_country");

                    spinner_list.add(list);
                    Log.d("result","111111111111111111111111"+j.getString("organization_country"));
                }
            }

             catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }




             return  null;
        }


    }

    // to add json values like username mobile ...etc//

    public void addingjsonvalues(){

    user_name=uname.getText().toString();
    mobile_number=mob.getText().toString();
    mail_id=email.getText().toString();

        JSONObject jsonObject =new JSONObject();
        try{
            jsonObject.put("status","login");
            jsonObject.put("username",user_name);
            jsonObject.put("mobile",mobile_number);
            jsonObject.put("email",mail_id);
            jsonObject.put("country",countryString);
        }
        catch(JSONException e){
            e.printStackTrace();

        }
         String login_url="http://10.0.0.128/complaintlogger/login.php";
        servicehandler=new Servicehandler(Login.this,jsonObject,login_interface);

          servicehandler.execute(login_url);
    }


    // implementing the interface for json response for userdata//

Login_interface login_interface=new Login_interface() {
    @Override
    public void oncompletion(JSONObject json) {

        Log.d("final json from net", "111111111111" + json);
        try {
            id = json.getInt("id");
            Log.d("userid", "isssssssssss" + id);
        } catch (JSONException e) {
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
    }
};

}
