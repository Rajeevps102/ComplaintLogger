package complaintloggger.wiztelapp.com.complaint_logger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Raju on 21-05-2015.
 */
public class Status_details extends ActionBarActivity {


    public Status_details() {

    }

    TextView t1,t2,t3;
    Integer complaint_id;
    Intent intent=new Intent();
    ArrayList<String>jsonlist=new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_detail_page);
        t1=(TextView)findViewById(R.id.textView7);
        t2=(TextView)findViewById(R.id.textView8);
        t3=(TextView)findViewById(R.id.textView9);
intent=getIntent();
       complaint_id=intent.getIntExtra("complaint_id",0);
        jsonlist=intent.getStringArrayListExtra("jsonarray");
     //   t1.setText(complaint_id);
        Log.d("rajeev",""+complaint_id);
        Log.d("rajeev",""+jsonlist);
        for(Integer i=0;i<jsonlist.size();i++){
            try {
                JSONObject jsonObject = new JSONObject(jsonlist.get(i));
                if(jsonObject.getInt("complaint_id")==complaint_id) {
                    Log.d("rajeev", "" + jsonObject.getString("header"));
                    t1.setText(jsonObject.getString("header"));
                }
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        }

    }
}