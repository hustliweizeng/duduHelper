package com.dudu.duduhelper.Activity.PrinterActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.dudu.duduhelper.GpService;
import com.gprinter.io.GpDevice;
import com.gprinter.service.GpPrintService;

/**
 * @author
 * @version 1.0
 * @date 2016/9/30
 */

public class BlutoothUtils extends Activity {
	private static final String DEBUG_TAG = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

			super.onCreate(savedInstanceState);
			startService();
			connection();
		}
		
	private void startService() {
		Intent i= new Intent(this, GpPrintService.class);
		startService(i);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	private GpService mGpService = null;
	private PrinterServiceConnection conn = null;
	class PrinterServiceConnection implements ServiceConnection {
	@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.i("ServiceConnection", "onServiceDisconnected() called");
			mGpService = null;
		}
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mGpService =GpService.Stub.asInterface(service);
		}
	}
	private void connection() {
			conn = new PrinterServiceConnection();
			Intent intent = new Intent("com.dudu.duduhelpe.GpPrintService");
			bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService
	}

	public static final String ACTION_CONNECT_STATUS = "action.connect.status";
	private void registerBroadcast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_CONNECT_STATUS);
		this.registerReceiver(PrinterStatusBroadcastReceiver, filter);
	}
	private BroadcastReceiver PrinterStatusBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (ACTION_CONNECT_STATUS.equals(intent.getAction())) {
				int type = intent.getIntExtra(GpPrintService.CONNECT_STATUS, 0);
				int id = intent.getIntExtra(GpPrintService.PRINTER_ID, 0);
				Log.d(DEBUG_TAG, "connect status " + type);
				if (type == GpDevice.STATE_CONNECTING) {
				} else if (type == GpDevice.STATE_NONE) {
				} else if (type == GpDevice.STATE_VALID_PRINTER) {
				} else if (type == GpDevice.STATE_INVALID_PRINTER) {
				}
			}
		}
	};
	
}
