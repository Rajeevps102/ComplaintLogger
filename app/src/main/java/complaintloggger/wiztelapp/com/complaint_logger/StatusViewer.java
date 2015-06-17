package complaintloggger.wiztelapp.com.complaint_logger;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dep2 on 5/15/2015.
 */
public class StatusViewer extends ActionBarActivity implements AdapterView.OnItemClickListener {

    ListView status_listview;
    ArrayList<String>complaint_head=new ArrayList<String>();
    ArrayList<Integer>complaint_id=new ArrayList<Integer>();
    ArrayList<String>complaint_status=new ArrayList<String>();
    ArrayList<String>jsonObjectArrayList=new ArrayList<>();
    ArrayList<Integer>id=new ArrayList<Integer>();
    Context con=this;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
public  Integer userid;
    public  final Integer RESULT_CLOSE_ALL=1;
    Complaint_details complaint_details=new Complaint_details();
    Servicehandler servicehandler=new Servicehandler();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_viewer_page);
status_listview=(ListView)findViewById(R.id.listView);



//////////// shared pref///////////////////////////////////////////
        sp = getSharedPreferences("isonetime", Context.MODE_PRIVATE);
        editor = sp.edit();
        userid=sp.getInt("id",0);
        Log.d("country", "id" + userid);
complaint_details.execute(userid);
     /*   complaint_head.add("no current");
        complaint_id.add(1);
        complaint_status.add("processing");  */
      //  status_listview.setAdapter(new Base(con, complaint_id, complaint_head, complaint_status));
        status_listview.setOnItemClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(StatusViewer.this,Home.class);
            startActivity(i);
    }

// to implement base adapter to show the complaint status//////

    private class Viewholder{
        TextView id;
        TextView cmphd;
        TextView stas;
    }


    public class Base extends BaseAdapter {
        ArrayList<String>comp_head=new ArrayList<String>();
        ArrayList<Integer> comp_id=new ArrayList<Integer>();
        ArrayList<String> comp_sts=new ArrayList<String>();
        Context context;

        public Base(Context con, ArrayList<Integer> id, ArrayList<String> cmp,ArrayList<String> sts) {
            // TODO Auto-generated constructor stub
            this.context=con;
            this.comp_id=id;
            this.comp_head=cmp;
            this.comp_sts=sts;

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return comp_id.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return comp_id.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @SuppressWarnings("unused")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Viewholder holder=new Viewholder();

            //holder.tname.setText(act.get(position));

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.status_baseadapter, null);

                holder.cmphd=(TextView)convertView.findViewById(R.id.textView3);
                holder.id=(TextView)convertView.findViewById(R.id.textView2);
                holder.stas=(TextView)convertView.findViewById(R.id.textView4);
                convertView.setTag(holder);

            }

            holder=(Viewholder)convertView.getTag();

            Log.i("", "array:" + comp_id.size() + ":" + position + ":" + comp_id.get(position));

            holder.id.setText("" + comp_id.get(position));
            holder.cmphd.setText(comp_head.get(position));
            holder.stas.setText("" + comp_sts.get(position));

            return convertView;
        }

    }

    /* Async class to get the complaint details for the user */

public  class Complaint_details extends AsyncTask<Integer, Void, String> {

    ProgressDialog progressDialog; ////for showing progress

public Complaint_details(){

}

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(StatusViewer.this,R.style.MyTheme);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Fetching complaints");
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

        progressDialog.show();
    }

    @Override
    protected String doInBackground(Integer... integers) {
       return servicehandler.makeServiceCall(integers[0]);

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
progressDialog.dismiss();
        Log.d("rajeev","inside post");
        Log.d("rajeev",""+s);
        JSONArray result=null;
        JSONObject jobj=null;
        try {

            try {
                JSONObject jsonObject = new JSONObject(s);
                result = jsonObject.getJSONArray("result");
            }
            catch (NullPointerException e){

                e.printStackTrace();
            }
            catch(JSONException e){
                e.printStackTrace();
            }
            for(Integer i=0;i<result.length();i++){
                jobj=result.getJSONObject(i);
                jsonObjectArrayList.add(jobj.toString());
                complaint_head.add(jobj.getString("header"));
                complaint_id.add(jobj.getInt("complaint_id"));
                complaint_status.add(jobj.getString("description"));
                id.add(jobj.getInt("id"));
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        catch (NullPointerException e){

            e.printStackTrace();
        }
        if(result==null){
            Toast.makeText(getApplicationContext(),"No complaints to display",Toast.LENGTH_LONG).show();
        }
        else {
            status_listview.setAdapter(new Base(con, id, complaint_head, complaint_status));
        }
    }
}

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub

      //  Toast.makeText(getApplicationContext(), "You clicked on position : " + arg2 + " and id : " + arg3, Toast.LENGTH_LONG).show();
       Integer i=id.get(arg2);
     //   Toast.makeText(getApplicationContext(), "You clicked on position : " + i + " and id : " + i, Toast.LENGTH_LONG).show();

        final Intent intent=new Intent(StatusViewer.this,Status_details.class);
        intent.putExtra("complaint_id",i);
        intent.putStringArrayListExtra("jsonarray",jsonObjectArrayList);
        startActivity(intent);

        }





    }

