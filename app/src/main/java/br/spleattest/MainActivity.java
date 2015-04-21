package br.spleattest;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends Activity {

    protected ListView mListView;
    private ListAdapter mListAdapter;
    Location mLocation = new Location("");
    LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //assume that access to location is enabled
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                5000,
                1,
                mLocationListener);
        mListView = (ListView) findViewById(R.id.mList);
    }
public class refreshOperation extends AsyncTask<String,Void,ArrayList<Item>>{
    ArrayList<Item> itemList = new ArrayList<Item>();

    @Override
    protected ArrayList<Item> doInBackground(String... strings) {
        try {
            StringBuilder mBuilder = new StringBuilder();
            HttpClient mClient = new DefaultHttpClient();
            HttpGet mHttpGet = new HttpGet("http://spleatdemo.herokuapp.com/api/venues");
            try {
                HttpResponse mResponse = mClient.execute(mHttpGet);
                StatusLine mStatusLine = mResponse.getStatusLine();
                int mStatusCode = mStatusLine.getStatusCode();
                if (mStatusCode == 200) {
                    HttpEntity mEntity = mResponse.getEntity();
                    InputStream mContent = mEntity.getContent();
                    BufferedReader mReader = new BufferedReader(new InputStreamReader(mContent));
                    String mLine;
                    while ((mLine = mReader.readLine()) != null) {
                        mBuilder.append(mLine);
                    }
                    String mResult = mBuilder.toString();
                    JSONObject basic;
                    JSONArray venues;
                    try {
                        basic = new JSONObject(mResult);
                        venues = basic.getJSONArray("venues");
                        for(int i = 0; i<venues.length();i++) {
                            itemList.add(new Item(
                                    venues.getJSONObject(i).getString("short_name"),
                                    venues.getJSONObject(i).getString("list_image"),
                                    venues.getJSONObject(i).getDouble("latitude"),
                                    venues.getJSONObject(i).getDouble("longitude")
                                    ));
                        }
                    }
                    catch (Exception e) {

                    }
                } else {

                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {

        }
        return itemList;
    }

    @Override
    protected void onPostExecute(ArrayList<Item> result) {
        mListAdapter = new ListAdapter(getApplicationContext(), result);
        mListAdapter.setLocation(mLocation);
        mListView.setAdapter(mListAdapter);
    }
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            mLocation = location;
            //assume that device has network connection
            refreshOperation mOperation = new refreshOperation();
            mOperation.execute();
            mLocationManager.removeUpdates(mLocationListener);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
}
