package complaintloggger.wiztelapp.com.complaint_logger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Raju on 04-05-2015.
 */
public class Home extends ActionBarActivity{

    Servicehandler servicehandler = new Servicehandler();
    Spinner organizationListSpinner;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String selctedcountryname;
    static String url = "http://10.0.0.127/complaintlogger/fetchorglist.php";
    /*The home class is loaded on successful user verification at the login process. the Home class
     displays options to
      1. choose an organization by fetching from the database
      2. write down the complaint heading
      3. write a complaint about a service or product in the organization
      4. attach a photo as an evidence to the complaint by either
           a. clicking a photo or
           b. attaching photo from the external storage
      5. submit button*/
    ArrayList<String> organization_list = new ArrayList<String>(); //**** list to populate the spinner****//
    Fetchorganization fetchorg= new Fetchorganization();

    public  Home()
   {

   }

    EditText home_complaintHeadET,home_complaintET;  // edit text that receive complaint subject and complaint
    String ComplaintHeadString, ComplaintString,OrganizationString; //string that store complaint subject  complaint, and organization name that is selected from spinner




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        home_complaintHeadET=(EditText)findViewById(R.id.home_complaintHeadET);
        home_complaintET=(EditText)findViewById(R.id.home_complaintET);
        sp = getSharedPreferences("isonetime", Context.MODE_PRIVATE);
        editor = sp.edit();
        selctedcountryname = sp.getString("selectedcountryname", "default");

        organizationListSpinner = (Spinner) findViewById(R.id.organizationListSpinner);

        fetchorg.execute(selctedcountryname);

    }






    //////////////////////////////////////////////////////////////////

    public class Fetchorganization extends AsyncTask<String, Void, Void> {

        Fetchorganization() {

        }

        @Override
        protected Void doInBackground(String... strings) {
            Log.d("jobin", "selected country in the fetch organization class is " + strings[0]);
            String result = servicehandler.makeServiceCall(url, strings[0]);
            Log.d("jobin", "result given from the makeservicecall is: " + result);

            try {
                JSONArray jsonArray = null;
                JSONObject j = null;
                JSONObject jsonObject = new JSONObject(result);
                jsonArray = jsonObject.getJSONArray("organization");

                for (Integer i = 0; i < jsonArray.length(); i++) {
                    j = jsonArray.getJSONObject(i);
                    String list = j.getString("organization_name");
                    Log.d("jobin", "list is " + list);
                    organization_list.add(list);
                    Log.d("jobin", "" + j.getString("organization_name"));


                }
                //  Intent k = new Intent(Splash_screen.this, Login.class);
                //   startActivity(k);


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item,organization_list);
            organizationListSpinner.setAdapter(adapter);
            organizationListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    OrganizationString=organization_list.get(position).toString();
                    Toast.makeText(getApplicationContext(),OrganizationString,Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }


///////////////////////////////////////////////////////////////////////////////////////



}
