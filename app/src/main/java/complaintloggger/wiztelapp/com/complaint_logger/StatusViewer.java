package complaintloggger.wiztelapp.com.complaint_logger;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by dep2 on 5/15/2015.
 */
public class StatusViewer extends ActionBarActivity {
    private ListView statusViewerLV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_viewer_page);
        statusViewerLV=(ListView)findViewById(R.id.statusviewerLV);
        String[] items = new String[] {"Item 1", "Item 2", "Item 3"};
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);

        statusViewerLV.setAdapter(adapter);
    }


}
