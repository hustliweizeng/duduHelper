package com.dudu.duduhelper.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;

public class ColorDialog 
{
	 private static Dialog mDialog;
	 public static void showRoundProcessDialog(Context mContext, int layout)
	    {
	        OnKeyListener keyListener = new OnKeyListener()
	        {
	            @Override
	            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
	            {
	                if (keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_SEARCH)
	                {
	                    return true;
	                }
	                return false;
	            }
	        };
	        mDialog = new AlertDialog.Builder(mContext).create();
	        mDialog.setOnKeyListener(keyListener);
	        mDialog.show();
	        mDialog.setContentView(layout);
	        mDialog.setCancelable(false);
	    }
      public static void dissmissProcessDialog()
      {
    	  if(mDialog!=null)
    	  {
    		  mDialog.dismiss();
    	  }
      }
}
