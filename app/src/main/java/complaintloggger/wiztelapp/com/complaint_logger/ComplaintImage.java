package complaintloggger.wiztelapp.com.complaint_logger;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ImageView;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Raju on 25-05-2015.
 */
public class ComplaintImage extends ActionBarActivity {

    Intent intent=new Intent();
    Integer id;
    FetchImage fetchImage=new FetchImage();
    Displaypic displaypic=new Displaypic();
    HttpURLConnection urlConnection;
     String response;
    ImageView imageView1,imageView2,imageView3;
    ArrayList<String>pic_path=new ArrayList<>();
    Bitmap bitmap;
    Integer count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaint_image);
        imageView1=(ImageView)findViewById(R.id.compalint_Img1);
        imageView2=(ImageView)findViewById(R.id.compalint_Img2);
        imageView3=(ImageView)findViewById(R.id.compalint_Img3);
 intent=getIntent();
        id=intent.getIntExtra("complaint_id", 0);
        Log.d("raju",""+id);
        fetchImage.execute(id);
    }



    public class FetchImage extends AsyncTask<Integer, Void, Void>{

        public FetchImage() {

        }

        @Override
        protected Void doInBackground(Integer... integers) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("complaint_id", integers[0]);
            }
            catch (JSONException e){
                e.printStackTrace();
            }
            try {
              //

                String url="http://10.0.0.130/complaintlogger/getImage.php";
                URL Url = new URL(url);
                urlConnection = (HttpURLConnection) Url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                urlConnection.setRequestProperty("Accept", "application/json");

                urlConnection.setRequestMethod("POST");
                urlConnection.connect();
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(jsonObject.toString());
                wr.flush();
                wr.close();
                int HttpResult =urlConnection.getResponseCode();
                if(HttpResult ==HttpURLConnection.HTTP_OK) {
                    InputStream in = urlConnection.getInputStream();
                    Log.d("jobin", "inputstream recieved" + urlConnection.getInputStream());
                    // InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    // BufferedReader reader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    response = reader.readLine();
                    Log.d("jobin", "picture from server:  " + response);

                    JSONObject j=new JSONObject((response.toString()));
                    JSONArray jsonArray=null;
                    JSONObject js=null;
                    jsonArray=j.getJSONArray("url");
                    for(Integer i=0;i<jsonArray.length();i++) {
                        js=jsonArray.getJSONObject(i);
                        String pic=js.getString("photo_url");
                        pic=pic.replace("E:/wamp/www","http://10.0.0.130:80");
                        Log.d("trimstring0",""+pic);
                         pic_path.add(pic);



                    }

in.close();

                }

            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
                Log.e("Buffer Error", "Error: " + e.toString());
            }
            finally {
                urlConnection.disconnect();
            }

            return null;


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            if(pic_path.size()!=0){
                for(Integer i=0;i<pic_path.size();i++){
                    count=count+1;
                    displaypic.execute(pic_path.get(i));
                }
            }
        }
    }


    public class Displaypic extends AsyncTask<String, Void, Void>{
        public Displaypic() {
        }

        @Override
        protected Void doInBackground(String... strings) {
            Log.d("url","1111111111111111111111111111111111111111"+strings[0]);
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(strings[0]).getContent());
                Log.d("rajeev",""+bitmap);
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
           // BitmapFactory.Options opt = new BitmapFactory.Options();
           // opt.inSampleSize = 5;
          //  Bitmap b = BitmapFactory.decodeFile(pic, opt);
            if(count==1) {
                imageView1.setImageBitmap(bitmap);
               
            }
            else if(count==2){
                imageView2.setImageBitmap(bitmap);
            }

        }







    }

}
