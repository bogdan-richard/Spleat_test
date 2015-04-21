package br.spleattest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class ListAdapter extends BaseAdapter {

    private ArrayList<Item> ItemsList = new ArrayList<Item>();

    private final Context mContext;
    private View mView;
    Location mLocation = new Location("");
    private String mUrl;

    public ListAdapter(Context mContext, ArrayList<Item> ItemsList) {
        this.mContext = mContext;
        this.ItemsList = ItemsList;
    }

    @Override
    public int getCount() {
        return ItemsList.size();
    }

    @Override
    public Object getItem(int position) {
        return ItemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ArrayList<Item> getArrayList() {
        return ItemsList;
    }

    public void setArrayList(ArrayList<Item> ItemsList) {
        this.ItemsList = ItemsList;
    }

    public void setLocation(Location mLocation) {
        this.mLocation = mLocation;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = convertView;
        if (mView == null) {
            mView = inflater.inflate(R.layout.list_item_layout, null);
        }
        //Text and image updates
        TextView venueTextView = (TextView) mView.findViewById(R.id.venue);
        TextView distanceTextView = (TextView) mView.findViewById(R.id.distance);

        ImageView mImageView = (ImageView) mView.findViewById(R.id.image);
        try {
            venueTextView.setText(ItemsList.get(position).getName());
            Location itemLocation = new Location("");
            itemLocation.setLatitude(ItemsList.get(position).getLatitude());
            itemLocation.setLongitude(ItemsList.get(position).getLongitude());
            float distance = mLocation.distanceTo(itemLocation);
            //no m to km conversion done
            distanceTextView.setText(distance +  " m");
            downloadImage Task = new downloadImage();
            Task.execute(ItemsList.get(position).image_link, mImageView);
        }
        catch (Exception e) {

        }
        return mView;
    }

 public class downloadImage extends AsyncTask<Object,Void,Bitmap> {
     ImageView mImage;

     @Override
     protected Bitmap doInBackground(Object... objects) {
         try {
             mImage = (ImageView) objects[1];
             URL Url = new URL((String)objects[0]);
             HttpURLConnection mConnection = (HttpURLConnection) Url
                     .openConnection();
             mConnection.setDoInput(true);
             mConnection.connect();
             InputStream mInput = mConnection.getInputStream();
             Bitmap mBitmap = BitmapFactory.decodeStream(mInput);

             return mBitmap;

         } catch (IOException e) {
            return null;
         }
     }

     @Override
     protected void onPostExecute(Bitmap mBitmap) {
         mImage.setImageBitmap(mBitmap);
        }
     }
}
