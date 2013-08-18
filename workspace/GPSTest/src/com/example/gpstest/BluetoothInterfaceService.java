package com.example.gpstest;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class BluetoothInterfaceService extends Service {
	public static final String ACTION_RESP =
		      "uk.me.geekylou.GPSTest.MESSAGE_PROCESSED";
	public static final String PARAM_OUT_MSG = "GPS";
	MyLocationListener mLocListener=null,mLocListener2=null;
	LocationManager mLocManager;
	IPSocketThread  mIPSocketThread = new IPSocketThread();
	Object          mLock[]=new Object[1];
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return (IBinder) BluetoothInterfaceService.this;
	}

	@Override
    public void onCreate() {
	    Toast.makeText(this, "Bluetooth Service Created", Toast.LENGTH_SHORT).show();
        }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        Toast.makeText(this, "Bluetooth Service Started", Toast.LENGTH_SHORT).show();

    	mLocManager = (LocationManager) 
    	        getSystemService(Context.LOCATION_SERVICE);

        if (mLocListener == null)
        {
        	mLocListener = new MyLocationListener("GPS",mLock);
        
        	mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
    	            0, mLocListener);
        }
        if (mLocListener2 == null)
        {
        	mLocListener2 = new MyLocationListener("NET",mLock);        
        
        	mLocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
    	            0, mLocListener2);
        }    	
        mLock[0] = mIPSocketThread.lock;
        mIPSocketThread.start();
        
    	// We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }
	
    @SuppressWarnings("deprecation")
	@Override
    public void onDestroy() {
        // Tell the user we stopped.
        Toast.makeText(this, "Bluetooth Service Stopped", Toast.LENGTH_SHORT).show();

		if (mLocManager != null && mLocListener != null)  
		{
			mLocManager.removeUpdates(mLocListener);
			mLocListener = null;
		}
		if (mLocManager != null && mLocListener2 != null)
		{
			mLocManager.removeUpdates(mLocListener2);
			mLocListener2 = null;
		}
		
        mIPSocketThread.stop();
    }
    
	class MyLocationListener implements LocationListener
	{
		String   header;
		Object   locks[];
		public MyLocationListener(String header,Object lock[])
		{
			this.locks = lock;
			this.header = header;
		}
		@Override
		public void onLocationChanged(Location arg0) {
			// TODO Auto-generated method stub
			String resultTxt = header+": SPD:"+arg0.getSpeed()+"\nLocX:"+arg0.getLongitude()+ "\nLocY:"+arg0.getLatitude()
					+ "\nAlt:"+arg0.getAltitude()+"\nAcc:"+arg0.getAccuracy();
			
			// processing done here….
			Intent broadcastIntent = new Intent();
			broadcastIntent.setAction(ACTION_RESP);
			broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
			broadcastIntent.putExtra(header, resultTxt);
			sendBroadcast(broadcastIntent);
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

	class IPSocketThread extends Thread
	{
		public Object lock = new Object();
		boolean running = true;
		public void run()
		{
			try {
				ServerSocket mServerSocket = new ServerSocket(10025);
				
				while(running)
				{
					Socket mSocket = mServerSocket.accept();
				
					DataOutputStream out = new DataOutputStream(mSocket.getOutputStream());
				
					while(running)
					{
						lock.wait();
						out.writeBytes("Test");
					}
					out.close();
				}
					
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class BluetoothSocketThread extends Thread
	{
		public void run()
		{
			
		}
	}
}

