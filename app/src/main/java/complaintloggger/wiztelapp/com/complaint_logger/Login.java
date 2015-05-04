package complaintloggger.wiztelapp.com.complaint_logger;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by Raju on 04-05-2015.
 */
public class Login extends Activity {

    Button register;
    EditText uname;
    EditText mob;
    EditText email;
    Spinner spinner;
    SharedPreferences splash;
    SharedPreferences.Editor editor;
    String MobilePattern = "[0-9]{10}";
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
        uname=(EditText)findViewById(R.id.editText);
        email=(EditText)findViewById(R.id.editText2);
        mob=(EditText)findViewById(R.id.editText3);
        spinner=(Spinner)findViewById(R.id.spinner);
        ArrayList<String>al=new ArrayList<String>();
        al.add("Select Country");
        al.add("India");
        ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,al);
        spinner.setAdapter(adapter);
    }

}
