package com.example.gpstest;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	MyLocationListener mLocListener,mLocListener2;
	LocationManager mLocManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TextView txtGPS = (TextView)findViewById(R.id.textView2);
		TextView txtLOC = (TextView)findViewById(R.id.textView3);

		mLocListener = new MyLocationListener(txtGPS,"GPS");
		mLocListener2 = new MyLocationListener(txtGPS,"NET");
		
		Button butStart= (Button)findViewById(R.id.buttonStart);
		butStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StartLocation();
            }
        });
		Button butStop= (Button)findViewById(R.id.buttonStop);
		butStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StopLocation();
            }
        });
	}

	void StartLocation()
	{
		mLocManager = (LocationManager) 
		        getSystemService(Context.LOCATION_SERVICE);
		mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
		            0, mLocListener);
		
		mLocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
		            0, mLocListener2);
		
	}
	
	void StopLocation()
	{
		mLocManager.removeUpdates(mLocListener);
		mLocManager.removeUpdates(mLocListener2);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	class MyLocationListener implements LocationListener
	{
		TextView txt;
		String   header;
		
		public MyLocationListener(TextView txt,String header)
		{
			this.txt    = txt;
			this.header = header;
		}
		@Override
		public void onLocationChanged(Location arg0) {
			// TODO Auto-generated method stub
	
			txt.setText(header+": SPD:"+arg0.getSpeed()+"\nLocX:"+arg0.getLongitude()+ "\nLocY:"+arg0.getLatitude()
					+ "\nAlt:"+arg0.getAltitude()+"\nAcc:"+arg0.getAccuracy());
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
	}
	
}