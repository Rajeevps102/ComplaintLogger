package complaintloggger.wiztelapp.com.complaint_logger;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Raju on 20-06-2015.
 */
public class App_introduction extends Activity implements View.OnClickListener {

    float x1,x2;
    float y1, y2;
    Integer count=1;
    Button signin;

    Fragment1 fragment1=new Fragment1();
    Fragment2 fragment_2=new Fragment2();
    Fragment3 fragment_3=new Fragment3();
    FragmentManager fManager;
    FragmentTransaction fTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_introduction_xml);
        signin=(Button)findViewById(R.id.button2);
        signin.setOnClickListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction())
        {
            // when user first touches the screen we get x and y coordinate
            case MotionEvent.ACTION_DOWN:
            {
                x1 = event.getX();
                y1 = event.getY();
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                x2 = event.getX();
                y2 = event.getY();


                if (x1 < x2&&count==4)
                {

                    Toast.makeText(this, "back to fragment 2 count" + "" + count, Toast.LENGTH_LONG).show();
                    count=count-1;
                    //  Fragment1 fragment1 = new Fragment1();
                    fManager = getFragmentManager();
                    fTransaction = fManager.beginTransaction();
                    fTransaction.replace(R.id.masterLayout, fragment_2, "Fragment2");

                    fTransaction.commit();
                    break;

                }

                if (x1 < x2&&count==3)
                {
                    count=count-1;
                    Toast.makeText(this, "back to fragment 1 count"+""+count, Toast.LENGTH_LONG).show();

                    //  Fragment1 fragment1 = new Fragment1();
                    fManager = getFragmentManager();
                    fTransaction = fManager.beginTransaction();
                    fTransaction.replace(R.id.masterLayout, fragment1, "Fragment");

                    fTransaction.commit();
                    break;

                }
                if (x1 < x2&&count==2)
                {
                    count=count-1;
                    Toast.makeText(this, "back to main activity count"+""+count, Toast.LENGTH_LONG).show();

                    //  Fragment1 fragment1 = new Fragment1();
                    fManager = getFragmentManager();
                    fTransaction = fManager.beginTransaction();
                    fTransaction.remove(fragment1);

                    fTransaction.commit();
                    break;

                }

                // if right to left swipe event on screen
                if (x1 > x2&&count==1)

                {
                    // when count =1 and we inflate the fragment 1 and increment cunt value to 2
                    //  Toast.makeText(this, "  Right  Swap Performed", Toast.LENGTH_LONG).show();
                    count=count+1;
                    Toast.makeText(this, "fragment 1 count"+""+count, Toast.LENGTH_LONG).show();


                    //  Fragment1 fragment1 = new Fragment1();
                    fManager = getFragmentManager();
                    fTransaction = fManager.beginTransaction();
                    fTransaction.replace(R.id.masterLayout, fragment1, "Fragment");

                    fTransaction.commit();
                    break;
                }
                if (x1 > x2&&count==2)

                {
                    // when count =1 and we inflate the fragment 2 and increment cunt value to 3
                    //  Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim);
                    Toast.makeText(this, "fragment 2 count"+""+count, Toast.LENGTH_LONG).show();
                    count=count+1;
                    //  Fragment_2 fragment2 = new Fragment_2();
                    fManager = getFragmentManager();
                    fTransaction = fManager.beginTransaction();
                    //   fTransaction.setCustomAnimations(R.anim.left,R.anim.right);
                    fTransaction.replace(R.id.masterLayout,fragment_2,"Fragment2");
                    // fTransaction.add(R.id.masterLayout, fragment, "Fragment");

                    fTransaction.commit();

                    break;
                }
                if (x1 > x2&&count==3)

                {
                    // when count =1 and we inflate the fragment 2 and increment cunt value to 3
                    //  Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim);
                    Toast.makeText(this, "fragment 3 count"+""+count, Toast.LENGTH_LONG).show();
                    count=count+1;
                    //  Fragment_2 fragment2 = new Fragment_2();
                    fManager = getFragmentManager();
                    fTransaction = fManager.beginTransaction();
                    //   fTransaction.setCustomAnimations(R.anim.left,R.anim.right);
                    fTransaction.replace(R.id.masterLayout,fragment_3,"Fragment3");
                    // fTransaction.add(R.id.masterLayout, fragment, "Fragment");

                    fTransaction.commit();

                    break;
                }
            }

        }

        return false;
    }

    @Override
    public void onClick(View view) {
        final Intent login=new Intent(App_introduction.this,Login.class);
        startActivity(login);
    }
}
