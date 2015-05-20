package complaintloggger.wiztelapp.com.complaint_logger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Raju on 04-05-2015.
 */
public class Home extends ActionBarActivity implements View.OnClickListener {

    Servicehandler servicehandler = new Servicehandler();
    Spinner organizationListSpinner;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String selctedcountryname;
    Integer userid;
    public static String timeStamp;
    ImageView home_compalintImg3,home_compalintImg2,home_compalintImg1;
    String loc;
    String picturePath; // used in attachment
    Bitmap bitmap;
    public Integer attach_count=0;
    public Integer count=0;
    public Integer complaint_id;
    static String dirname = "ComplaintLogger";
    ImageView camera, attach;
    ArrayList<String>camera_image_path=new ArrayList<>();
ProgressBar pg;
    String json_string;//for json string
    Complaint_webservice complaint_webservice = new Complaint_webservice();
    static String url = "http://10.0.0.118/complaintlogger/fetchorg.php";
    static String complaint_url = "http://10.0.0.118/complaintlogger/complaints.php";
    private Uri fileUri;
    ArrayList<String> organization_list = new ArrayList<String>(); //**** list to populate the spinner****//
    Fetchorganization fetchorg = new Fetchorganization();
    EditText home_complaintHeadET, home_complaintET; // edit text that receive complaint subject and complaint
    Button submit;
    Toolbar toolbar;
    String ComplaintHeadString, ComplaintString, OrganizationString; //string that store complaint subject  complaint, and organization name that is selected from spinner

    /*The home class is loaded on successful user verification at the login process. the Home class
     displays options to
      1. choose an organization by fetching from the database
      2. write down the complaint heading
      3. write a complaint about a service or product in the organization
      4. attach a photo as an evidence to the complaint by either
           a. clicking a photo or
           b. attaching photo from the external storage
      5. submit button*/
    public Home() {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        home_complaintHeadET = (EditText) findViewById(R.id.home_complaintHeadET);
        home_complaintET = (EditText) findViewById(R.id.home_complaintET);
        initToolbars();

        submit = (Button) findViewById(R.id.home_submitBtn);
        pg=(ProgressBar)findViewById(R.id.pbHeaderProgress);
        pg.setVisibility(View.GONE);
        submit.setOnClickListener(this);
        sp = getSharedPreferences("isonetime", Context.MODE_PRIVATE);
        editor = sp.edit();
        selctedcountryname = sp.getString("selectedcountryname", "");
        userid=sp.getInt("id",0);
        Log.d("country", "id" + userid);

        organizationListSpinner = (Spinner) findViewById(R.id.organizationListSpinner);


        fetchorg.execute(selctedcountryname);
        home_compalintImg1=(ImageView)findViewById(R.id.home_compalintImg1);
        home_compalintImg2=(ImageView)findViewById(R.id.home_compalintImg2);
        home_compalintImg3=(ImageView)findViewById(R.id.home_compalintImg3);
        camera=(ImageView )findViewById(R.id.home_camera);
        attach=(ImageView)findViewById(R.id.home_attach);
        camera.setOnClickListener(this);
        attach.setOnClickListener(this);

    }

    private void initToolbars() {



        Toolbar toolbarBottom = (Toolbar) findViewById(R.id.toolbar);

        toolbarBottom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_settings:
                        Intent i=new Intent(Home.this,StatusViewer.class);
                        startActivity(i);
                        finish();

                        break;
                    // TODO: Other cases
                }
                return true;
            }
        });
        // Inflate a menu to be displayed in the toolbar
        toolbarBottom.inflateMenu(R.menu.menu_splash_screen);
    }





    @Override
    protected void onResume() {
        super.onResume();
       // home_complaintHeadET.setText("");
       // home_complaintET.setText("");
      //  camera_image_path.clear();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_submitBtn: addingjsonvalues() ;
                break;
            case R.id.home_camera:opencamera();
                break;
            case R.id.home_attach:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        attach_count=attach_count+1;
                count=count+1;
                startActivityForResult(galleryIntent,3);
                break;
        }
    }

    public void addingjsonvalues() {
        ComplaintHeadString = home_complaintHeadET.getText().toString();
        ComplaintString = home_complaintET.getText().toString();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("status", "complaint");
            jsonObject.put("header", ComplaintHeadString);
            jsonObject.put("complaint", ComplaintString);
            jsonObject.put("userid",userid);
            jsonObject.put("organization", OrganizationString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if ( ComplaintString!= null && ComplaintString.length() > 0 ) {
            complaint_webservice.execute(jsonObject.toString());
        }
        else{
            Toast.makeText(getApplicationContext(),"enter valid data",Toast.LENGTH_LONG).show();
        }
    }

////////////////////getting organization names from server//////////////////////////////////////////////

    public class Fetchorganization extends AsyncTask<String, Void, Void> {

        Fetchorganization() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pg.setVisibility(View.VISIBLE);

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

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, organization_list);
            organizationListSpinner.setAdapter(adapter);
            organizationListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    OrganizationString = organization_list.get(position).toString();

                    pg.setVisibility(View.GONE);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }


///////////////////////////////////////////////////////////////////////////////////////

    // to register a complaint to server//
    public class Complaint_webservice extends AsyncTask<String, Void, String> {

        Complaint_webservice() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d("json", "11111111" + strings[0]);
            String result = servicehandler.makeServiceCall(complaint_url, strings[0], 0);

            return result;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("rajeev", "111111111" + s);
            try {
                JSONObject j = new JSONObject(s);
                complaint_id=j.getInt("complaintid");
            }
            catch (JSONException e){
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(), "complaint submitted"+""+complaint_id, Toast.LENGTH_LONG).show();

            if(camera_image_path.size()!=0) {
                for (Integer i = 0; i < camera_image_path.size(); i++) {
                    RetrieveFeedTask obj = new RetrieveFeedTask();
                    obj.execute(camera_image_path.get(i));
                }
            }
            else{
                Intent i=new Intent(Home.this,StatusViewer.class);
                startActivity(i);
            }

        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////


    public void opencamera()
    {
        count=count+1;
        attach_count=attach_count+1;
        Log.d("jobin", "in the open camera function"+count);
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);// declare the intent for camera
      //  fileUri = getOutputMediaFileUri();// this function call will eventually return the name to be assigned for the photo
     //  Log.d("jobin", "fileuri recieved is "+fileUri);
     //   intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);// adds name as PutExtra feature
        startActivityForResult(intent, 1);// starts the intent to camera

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("jobin", "data recieved is " + data);


    if(resultCode==0)
    {
          //opencamera();
        return;
    }


     else   if(requestCode==1)  {


        Uri photoUri = data.getData();
        String[] projection = {MediaStore.Images.Media.DATA};
        try {
            Cursor cursor = getContentResolver().query(photoUri, projection, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(projection[0]);
            picturePath = cursor.getString(columnIndex);
            camera_image_path.add(compressImage(picturePath));
            cursor.close();
            Log.d("Picture Path", picturePath);
        }
        catch(Exception e) {
            Log.e("Path Error", e.toString());
        }

        //   camera_image_path.add(compressImage(fileUri.getPath()));





            // downsizing image as it throws OutOfMemory Exception for larger
            // images

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 5;
          //   bitmap =MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);// BitmapFactory.decodeFile(picturePath,options);
                bitmap=BitmapFactory.decodeFile(picturePath,options);
           // Bitmap b = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);

        }
        catch(Exception e){
            e.printStackTrace();
        }
            Log.d("jobin", "setting bitmap");

           // Bitmap bp = (Bitmap) data.getExtras().get("data");
        if(count==1){
            home_compalintImg1.setImageBitmap(bitmap);
            return;
        }
           else if(count==2){
            home_compalintImg2.setImageBitmap(bitmap);
            Log.d("rajeev","1111111111111111111111111111111111111");
            return;
        }

        else if(count==3){
            Log.d("rajeev","1111111111111111111111111111111111111");
            home_compalintImg3.setImageBitmap(bitmap);
            return;

        }


        }
        else if(requestCode==3){
        Uri photoUri = data.getData();
        String[] projection = {MediaStore.Images.Media.DATA};
        try {
            Cursor cursor = getContentResolver().query(photoUri, projection, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(projection[0]);
             picturePath = cursor.getString(columnIndex);
            camera_image_path.add(compressImage(picturePath));
            cursor.close();
            Log.d("Picture Path", picturePath);
        }
        catch(Exception e) {
            Log.e("Path Error", e.toString());
        }
        Log.d("rajeev","data"+photoUri);
        try {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inSampleSize=5;
            Bitmap b = BitmapFactory.decodeFile(picturePath,opt);
         //   Bitmap b = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);

            if(attach_count==1) {

                home_compalintImg1.setImageBitmap(b);
                Log.d("rajeev", "data" + MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri));
            }
          else if(attach_count==2){
                home_compalintImg2.setImageBitmap(b);
            }
            else if(attach_count==3){
                home_compalintImg3.setImageBitmap(b);
                return;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    }
    // compressing the image from camera//

    public String compressImage(String imageUri) {

        String filePath =imageUri;
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath,options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        options.inSampleSize =calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16*1024];

        try{
            bmp = BitmapFactory.decodeFile(filePath,options);
        }
        catch(OutOfMemoryError exception){
            exception.printStackTrace();

        }
        try{
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        }
        catch(OutOfMemoryError exception){
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float)options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth()/2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));


        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        String filename =getOutputMediaFileUri().getPath();
        try {
            out = new FileOutputStream(filename);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }




// creating a file path for saving the photos//


    public Uri getOutputMediaFileUri()
    {
        Log.d("jobin", "in the getOutputMediaFileUri");
        return Uri.fromFile(getOutputMediaFile());
    }

    private static File getOutputMediaFile()
    {
        Log.d("jobin", "in the getOutputMediaFile");
        File mediaStorageDir = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),dirname);
        if (!mediaStorageDir.exists())
        {
            Log.d("jobin", "in creating directory");
            if (!mediaStorageDir.mkdir())
            {
                mediaStorageDir.mkdir();

            }
        }
        Log.d("jobin", "in making timestamp");
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new java.util.Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");
        Log.d("jobin", "file name uri is: "+mediaFile.toString());
        return mediaFile;
    }


    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
    // class to upload image to the server//

    class RetrieveFeedTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            HttpURLConnection conn = null;
            Log.d("jobin", "2");
            DataOutputStream dos = null;
            DataInputStream inStream = null;
			/*	String existingFileName = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/DCIM/a.png";
						*/
            String existingFileName =params[0];
            Log.d("jobin", existingFileName);
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            String responseFromServer = "";
            String urlString = "http://10.0.0.118/complaintlogger/uploadimage.php";
            Log.d("jobin", "3");
            try {

                // ------------------ CLIENT REQUEST
                FileInputStream fileInputStream = new FileInputStream(new File(
                        existingFileName));
                // open a URL connection to the Servlet
                URL url = new URL(urlString);
                // Open a HTTP connection to the URL
                conn = (HttpURLConnection) url.openConnection();
                // Allow Inputs
                conn.setDoInput(true);
                // Allow Outputs
                conn.setDoOutput(true);
                // Don't use a cached copy.
                conn.setUseCaches(false);
                // Use a post method.
                Log.d("jobin", "4");
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type",
                        "multipart/form-data;boundary=" + boundary);
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
                        + existingFileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                // create a buffer of maximum size
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    Log.d("jobin", "5");
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                // close streams
                Log.d("jobin", "6");
                Log.e("Debug", "File is written");
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                Log.d("jobin", "7");
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            } catch (IOException ioe) {
                Log.d("jobin", "8");
                Log.e("Debug", "error: " + ioe.getMessage(), ioe);
            }

            // ------------------ read the SERVER RESPONSE
            try {

                inStream = new DataInputStream(conn.getInputStream());
                String str;

                while ((str = inStream.readLine()) != null) {

                    Log.e("Debug", "Server Response " + str);

                }

                inStream.close();

            } catch (IOException ioex) {
                Log.e("Debug", "error: " + ioex.getMessage(), ioex);
            }

            Intent i=new Intent(Home.this,StatusViewer.class);
            startActivity(i);
            finish();
            return null;
        }
    }

}
