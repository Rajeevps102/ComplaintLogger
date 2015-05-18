package complaintloggger.wiztelapp.com.complaint_logger;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
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
public class StatusViewer extends ActionBarActivity {

    ListView status_listview;
    ArrayList<String>complaint_head=new ArrayList<String>();
    ArrayList<Integer>complaint_id=new ArrayList<Integer>();
    ArrayList<String>complaint_status=new ArrayList<String>();
    Context con=this;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
public  Integer userid;
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
      final   AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getApplicationContext());
        alertDialogBuilder.setMessage("rajeev");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                        Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_LONG).show();
                    }
                });
        alertDialogBuilder.setNegativeButton("raju",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_LONG).show();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

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

public Complaint_details(){

}

    @Override
    protected String doInBackground(Integer... integers) {
       return servicehandler.makeServiceCall(integers[0]);

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("rajeev","inside post");
        Log.d("rajeev",""+s);
        JSONArray result=null;
        JSONObject jobj=null;
        try {
            JSONObject jsonObject = new JSONObject(s);
            result=jsonObject.getJSONArray("result");
            for(Integer i=0;i<result.length();i++){
                jobj=result.getJSONObject(i);
                complaint_head.add(jobj.getString("header"));
                complaint_id.add(jobj.getInt("complaint_id"));
                complaint_status.add(jobj.getString("description"));
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        status_listview.setAdapter(new Base(con, complaint_id, complaint_head, complaint_status));
    }
}
}
