package complaintloggger.wiztelapp.com.complaint_logger;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class Servicehandler extends  AsyncTask<String,Integer,JSONObject>  {
	static InputStream is = null;
	static String response = null;
    HttpURLConnection urlConnection;

    Context context;
    JSONObject jsonObject;
    Login_interface login_interface;
	public Servicehandler() {

	}

    public Servicehandler(Context context,JSONObject j,Login_interface listener) {
        // TODO Auto-generated constructor stub
        this.context=context;
        this.jsonObject=j;
        this.login_interface=listener;
    }

/*  makeServiceCall(String url1) is for getting the spinner value from the server
    Service handler class itself extends from async task to integrate webservice
    for login module
 */


    public String makeServiceCall(String url1) {
        try {

            Log.d("inside make","handler"+url1);
            URL url = new URL(url1);
            urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in =urlConnection.getInputStream();
            Log.d("inside make","111111111111111111111111111111111111"+urlConnection.getInputStream());
           // InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader=new BufferedReader(new InputStreamReader(in));
           // BufferedReader reader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            response = reader.readLine();
            Log.d("inside make","handler11111111111111111111111111111111111111111"+response);


                is.close();




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




        return response;
    }
/*
    public String makeServiceCall(String url1, String country) {
        try {

            Log.d("jobin","in make service call fetch organization the url recieved is "+url1+" and country selected is "+country);
            URL url = new URL(url1);
            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in =urlConnection.getInputStream();
            Log.d("jobin","inputstream recieved"+urlConnection.getInputStream());
            // InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader=new BufferedReader(new InputStreamReader(in));
            // BufferedReader reader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            response = reader.readLine();
            Log.d("jobin","response is:  "+response);


            is.close();




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




        return response;
    }

*/
    // function to call webservice for login in user information //

    public JSONObject login_webservice(String url1){

        try {
            Log.d("rajeev","inside service handler webservice");
            URL url = new URL(url1);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            urlConnection.setRequestProperty("Content-Type", "application/json");

            urlConnection.setRequestProperty("Accept", "application/json");

           urlConnection.setRequestMethod("POST");
            urlConnection.connect();
            OutputStreamWriter wr= new OutputStreamWriter(urlConnection.getOutputStream());
            Log.d("rajeev","sending json data"+jsonObject.toString());
            String eg=jsonObject.toString();
            wr.write(eg);

            wr.flush();
            wr.close();

            StringBuilder sb = new StringBuilder();

            int HttpResult =urlConnection.getResponseCode();

            if(HttpResult ==HttpURLConnection.HTTP_OK){

                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"utf-8"));

                String line = br.readLine();

                Log.d("rajeev","111111111111"+line);
                JSONObject jsonobj=new JSONObject(line);


                br.close();
                return  jsonobj;




            }


        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (Exception e) {

            e.printStackTrace();

        } finally {

            if(urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {

        Log.d("rajeev","inside service handler");
        return login_webservice(strings[0]);

    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        login_interface.oncompletion(jsonObject);
    }

    // for updating company name in home page spinner//

    public String makeServiceCall(String url1, String country) {
        try {

            JSONObject jsonObject1=new JSONObject();
            jsonObject1.put("country",country);
            Log.d("jobin","in make service call fetch organization the url recieved is "+url1+" and country selected is "+country);
            URL url = new URL(url1);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");

            urlConnection.setRequestProperty("Accept", "application/json");

            urlConnection.setRequestMethod("POST");
            urlConnection.connect();
            OutputStreamWriter wr= new OutputStreamWriter(urlConnection.getOutputStream());
            Log.d("rajeev","sending json data"+jsonObject1.toString());
           // String eg=jsonObject.toString();
            wr.write(jsonObject1.toString());

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
                Log.d("jobin", "response is:  " + response);


                is.close();


            }
        }
        catch(JSONException e){
            e.printStackTrace();

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




        return response;
    }
// to save the complaint//

    public String makeServiceCall(String url1, String json,Integer i) {

        try {


            Log.d("jobin", "in make service call fetch organization the url recieved is " + url1 + " and country selected is " + json);
            URL url = new URL(url1);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");

            urlConnection.setRequestProperty("Accept", "application/json");

            urlConnection.setRequestMethod("POST");
            urlConnection.connect();
            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
            wr.write(json);
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
                Log.d("jobin", "response is:  " + response);


                is.close();


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



        return response;

    }

    // for getting the complaint details//


    public String  makeServiceCall(Integer i){
        Log.d("rajeev","inside make"+i);
        String url="http://10.0.0.130/complaintlogger/viewstatus.php";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userid", i);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        try {



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
                Log.d("jobin", "response is:  " + response);


                is.close();


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

        return response;
    }
}
