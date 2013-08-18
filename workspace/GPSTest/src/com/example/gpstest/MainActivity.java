package com.example.gpstest;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	Intent          mBluetoothService;
	ResponseReceiver receiver;
	
	TextView txtGPS;
	TextView txtLOC;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mBluetoothService = new Intent(this,BluetoothInterfaceService.class);
		
		txtGPS = (TextView)findViewById(R.id.textView2);
		txtLOC = (TextView)findViewById(R.id.textView3);
		
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
		if (receiver == null)
		{
			IntentFilter filter = new IntentFilter(BluetoothInterfaceService.ACTION_RESP);
			filter.addCategory(Intent.CATEGORY_DEFAULT);
			receiver = new ResponseReceiver();
			registerReceiver(receiver, filter);
		}
		startService(mBluetoothService);
	}
	
	void StopLocation()
	{
		if (receiver != null)
		{
			unregisterReceiver(receiver);
			receiver = null;
		}
		stopService(mBluetoothService);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	class ResponseReceiver extends BroadcastReceiver {
		public static final String ACTION_RESP =
			      "uk.me.geekylou.GPSTest.MESSAGE_PROCESSED";
			ResponseReceiver()
			{
				super();
			}
		   @Override
		    public void onReceive(Context context, Intent intent) {
		       String text = intent.getStringExtra("GPS");
		       if (text != null) txtGPS.setText(text);
		       text = intent.getStringExtra("NET");
		       if (text != null) txtGPS.setText(text);
		       
		    }
		}
}
