package complaintloggger.wiztelapp.com.complaint_logger;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.EditText;

/**
 * Created by Raju on 04-05-2015.
 */
public class Home extends ActionBarActivity{
    /*The home class is loaded on successful user verification at the login process. the Home class
     displays options to
      1. choose an organization by fetching from the database
      2. write down the complaint heading
      3. write a complaint about a service or product in the organization
      4. attach a photo as an evidence to the complaint by either
           a. clicking a photo or
           b. attaching photo from the external storage
      5. submit button*/

    EditText home_complaintHeadET,home_complaintET;  // edit text that receive complaint subject and complaint
    String ComplaintHeadString, ComplaintString; //string that store complaint subject and complaint
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        home_complaintHeadET=(EditText)findViewById(R.id.home_complaintHeadET);
        home_complaintET=(EditText)findViewById(R.id.home_complaintET);

    }



}
