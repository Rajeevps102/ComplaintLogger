package complaintloggger.wiztelapp.com.complaint_logger;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import android.os.AsyncTask;
import android.util.Log;

public class Servicehandler  {
	static InputStream is = null;
	static String response = null;
	public final static int GET = 1;
	public final static int POST = 2;
    HttpURLConnection urlConnection;
	public Servicehandler() {

	}



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
            String line = null;
            Log.d("inside make","handler11111111111111111111111111111111111111111");
            while ((line = reader.readLine()) != null) {

                sb.append(line + "\n");
            }

                is.close();

              response=sb.toString();


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
