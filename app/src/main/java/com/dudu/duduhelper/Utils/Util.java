package com.dudu.duduhelper.Utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util 
{
	public static String cityName ;  //城市名    
	public static  Location citylocation;
    private static Geocoder geocoder;   //此对象能通过经纬度来获取相应的城市等信息
    //比较时间大小
	public static boolean compareDate(String DATE1, String DATE2) 
	{
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    try 
	    {
	        Date dt1 = df.parse(DATE1);
	        Date dt2 = df.parse(DATE2);
	        if (dt1.getTime() > dt2.getTime()) 
	        {
	            //System.out.println("dt1 在dt2前");
	            return false;
	        } 
	        else if (dt1.getTime() < dt2.getTime()) 
	        {
	            //System.out.println("dt1在dt2后");
	            return true;
	        } 
	     } 
        catch (Exception exception) 
        {
            exception.printStackTrace();
        }
        return true;
	}
	
	/** 
     * 日期格式字符串转换成时间戳 
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return 
     */  
    public static String date2TimeStamp(String date_str,String format){  
        try {  
            SimpleDateFormat sdf = new SimpleDateFormat(format);  
            return String.valueOf(sdf.parse(date_str).getTime()/1000);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return "";  
    }  
	
    //时间戳转换字符串
    public static String DataConVert(String data)
    {
    	return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(data)));
    	
    }
  //时间戳转换字符串
    public static String DataConVert2(String data)
    {
    	return new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date(Long.parseLong(data)));
    	
    }
	//字符串转换为时间戳
	public static long Data2Unix(String data){
		long time = 0;
			try {
				time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data).getTime();
			}
			catch (Exception e){
				e.printStackTrace();
			}
		return  time;
	}
    //时分秒转换
    public static String TimeConVert(String time)
    {
	    //date是毫秒
        return new SimpleDateFormat("HH:mm:ss").format(new Date(Long.parseLong(time)*1000));
    }
    //获取当前年月日
    public static String DateForomate3()
    {
        return new SimpleDateFormat("yyyy年MM月dd日").format(new Date());
    }
    
    //MD5加密
    public static String md5(String string) 
    {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
    //映射json
    public static String tojson(Object object) 
	{
		return (new Gson()).toJson(object);
	}
	// dp转像素
	public static int dip2px(Context context, float dpValue) 
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	@SuppressLint("SimpleDateFormat")
	public static String getNowTime() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
		return dateFormat.format(date);
	}

	// 判断是否有sdcard
	public static boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	 public static String DataConVertMint(String data)
	    {
//	    	SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd " );
//	    	return format.format(new Date(Long.parseLong(data)));
	    	
	    	return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(new Long(data) * 1000L));
	    	
	    }

	/**
	 * bitmap转为base64
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String bitmapToBase64(Bitmap bitmap) {

		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	// 密码正则表达式
	public static boolean checkString(String string) {
		return string
				.matches("(?![^a-zA-Z0-9]+$)(?![^a-zA-Z/D]+$)(?![^0-9/D]+$).{6,20}$");
	}
	//获取用户IP地址
	public static String getLocalIpAddress() 
	{
		try 
		{
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) 
			{
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) 
				{
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) 
					{
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		}
		catch (SocketException ex) 
		{
			Log.e("IPerror", ex.toString());
		}
		return null;
	}

	// 以下是获得版本信息的工具方法
	//版本名
	public static String getVersionName(Context context) {
	    return getPackageInfo(context).versionName;
	}
	 
	//版本号
	public static int getVersionCode(Context context) {
	    return getPackageInfo(context).versionCode;
	}
	 
	private static PackageInfo getPackageInfo(Context context) {
	    PackageInfo pi = null;
	 
	    try {
	        PackageManager pm = context.getPackageManager();
	        pi = pm.getPackageInfo(context.getPackageName(),
	                PackageManager.GET_CONFIGURATIONS);
	 
	        return pi;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	 
	    return pi;
	}
	//获取城市定位
	/**  
     * 通过地理坐标获取城市名  其中CN分别是city和name的首字母缩写  
     * @param context  
     */    
    public static void getCNBylocation(Context context)
    {    
        geocoder = new Geocoder(context);    
        //用于获取Location对象，以及其他    
        LocationManager locationManager;     
        String serviceName = Context.LOCATION_SERVICE;    
        //实例化一个LocationManager对象    
        locationManager = (LocationManager)context.getSystemService(serviceName);    
        //provider的类型    
        String provider = LocationManager.NETWORK_PROVIDER;    
    
        Criteria criteria = new Criteria();    
        criteria.setAccuracy(Criteria.ACCURACY_FINE);   //高精度    
        criteria.setAltitudeRequired(false);    //不要求海拔    
        criteria.setBearingRequired(false); //不要求方位    
        criteria.setCostAllowed(false); //不允许有话费    
        criteria.setPowerRequirement(Criteria.POWER_LOW);   //低功耗    
            
        //通过最后一次的地理位置来获得Location对象    
        citylocation = locationManager.getLastKnownLocation(provider);    
        String queryed_name = updateWithNewLocation(citylocation);    
        if((queryed_name != null) && (0 != queryed_name.length()))
        {    
            cityName = queryed_name;    
        }    
        /*  
         * 第二个参数表示更新的周期，单位为毫秒；第三个参数的含义表示最小距离间隔，单位是米  
         * 设定每30秒进行一次自动定位  
         */    
        locationManager.requestLocationUpdates(provider, 30000, 50,locationListener);    
        //移除监听器，在只有一个widget的时候，这个还是适用的    
        locationManager.removeUpdates(locationListener);    
    }    
        
    /**  
     * 方位改变时触发，进行调用  
     */    
    private final static LocationListener locationListener = new LocationListener() 
    {    
        String tempCityName;    
        public void onLocationChanged(Location location) {
        	
                
            tempCityName = updateWithNewLocation(location);    
            citylocation=location;
            if((tempCityName != null) && (tempCityName.length() != 0)){    
                    
                cityName = tempCityName;    
            }    
        }    
    
        public void onProviderDisabled(String provider) {    
            tempCityName = updateWithNewLocation(null);    
            if ((tempCityName != null) && (tempCityName.length() != 0)) {    
    
                cityName = tempCityName;    
            }    
        }    
    
        public void onProviderEnabled(String provider) {    
        }    
    
        public void onStatusChanged(String provider, int status, Bundle extras) {    
        }    
    };    
    
    /**  
     * 更新location  
     * @param location  
     * @return cityName  
     */    
    private static String updateWithNewLocation(Location location) {    
        String mcityName = "";    
        double lat = 0;    
        double lng = 0;    
        List<Address> addList = null;    
        if (location != null) {    
            lat = location.getLatitude();    
            lng = location.getLongitude();    
        } else {    
    
            System.out.println("无法获取地理信息");    
        }    
             
        try {    
                
            addList = geocoder.getFromLocation(lat, lng, 1);    //解析经纬度    
                
        } catch (IOException e) {    
            // TODO Auto-generated catch block    
            e.printStackTrace();    
        }    
        if (addList != null && addList.size() > 0) {    
            for (int i = 0; i < addList.size(); i++) {    
                Address add = addList.get(i);    
                mcityName += add.getLocality();    
            }    
        }    
        if(mcityName.length()!=0){    
                
            return mcityName.substring(0, (mcityName.length()-1));    
        } else {    
            return mcityName;    
        }    
    }    
    /** 
     * 得到本地或者网络上的bitmap url - 网络或者本地图片的绝对路径,比如: 
     *  
     * A.网络路径: url="http://blog.foreverlove.us/girl2.png" ; 
     *  
     * B.本地路径:url="file://mnt/sdcard/photo/image.png"; 
     *  
     * C.支持的图片格式 ,png, jpg,bmp,gif等等 
     *  
     * @return
     */  
    public static byte[] decodeBitmap(String path) 
    {  
        BitmapFactory.Options opts = new BitmapFactory.Options();  
        opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高  
        BitmapFactory.decodeFile(path, opts);  
        opts.inSampleSize = computeSampleSize(opts, -1, 480 * 320);  
        opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true  
        opts.inPurgeable = true;  
        opts.inInputShareable = true;  
        opts.inDither = false;  
        opts.inPurgeable = true;  
        opts.inTempStorage = new byte[8 * 1024];  
        FileInputStream is = null;  
        Bitmap bmp = null;  
        ByteArrayOutputStream baos = null;  
        try 
        {  
            is = new FileInputStream(path); 
            bmp = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);  
            double scale = getScaling(opts.outWidth * opts.outHeight,480 * 320);  
            Bitmap bmp2 = Bitmap.createScaledBitmap(bmp, (int) (opts.outWidth * scale), (int) (opts.outHeight * scale), true);  
            bmp.recycle();  
            baos = new ByteArrayOutputStream();  
            bmp2.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
            bmp2.recycle();  
            return baos.toByteArray();  
        }
        catch (FileNotFoundException e) 
        {  
            e.printStackTrace();  
        }
        catch (IOException e) 
        {  
            e.printStackTrace();  
        } 
        finally
        {  
            try 
            {  
                is.close();  
                baos.close();  
            } 
            catch (IOException e)
            {  
                e.printStackTrace();  
            }  
            System.gc();  
        }  
        return baos.toByteArray();  
    }  
  
    private static double getScaling(int src, int des) {  
        /** 
         * 48 目标尺寸÷原尺寸 sqrt开方，得出宽高百分比 49 
         */  
        double scale = Math.sqrt((double) des / (double) src);  
        return scale;  
    }  
  
    public static int computeSampleSize(BitmapFactory.Options options,  
            int minSideLength, int maxNumOfPixels) {  
        int initialSize = computeInitialSampleSize(options, minSideLength,  
                maxNumOfPixels);  
  
        int roundedSize;  
        if (initialSize <= 8) {  
            roundedSize = 1;  
            while (roundedSize < initialSize) {  
                roundedSize <<= 1;  
            }  
        } else {  
            roundedSize = (initialSize + 7) / 8 * 8;  
        }  
  
        return roundedSize;  
    }  
  
    private static int computeInitialSampleSize(BitmapFactory.Options options,  
            int minSideLength, int maxNumOfPixels) {  
        double w = options.outWidth;  
        double h = options.outHeight;  
  
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math  
                .sqrt(w * h / maxNumOfPixels));  
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(  
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));  
  
        if (upperBound < lowerBound) {  
            return lowerBound;  
        }  
  
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {  
            return 1;  
        } else if (minSideLength == -1) {  
            return lowerBound;  
        } else {  
            return upperBound;  
        }  
    }
    /** 
     * 得到本地或者网络上的bitmap url - 网络或者本地图片的绝对路径,比如: 
     *  
     * A.网络路径: url="http://blog.foreverlove.us/girl2.png" ; 
     *  
     * B.本地路径:url="file://mnt/sdcard/photo/image.png"; 
     *  
     * C.支持的图片格式 ,png, jpg,bmp,gif等等 
     *  
     * @param url 
     * @return 
     */  
    public static Bitmap GetLocalOrNetBitmap(String url)  
    {  
        Bitmap bitmap = BitmapFactory.decodeByteArray(Util.decodeBitmap(url), 0, Util.decodeBitmap(url).length);
        return bitmap;
//        InputStream in = null;  
//        BufferedOutputStream out = null;  
//        try  
//        {  
//            in = new BufferedInputStream(new URL(url).openStream(), 2*1024);  
//            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();  
//            out = new BufferedOutputStream(dataStream, 2*1024);  
//            copy(in, out);  
//            out.flush();  
//            byte[] data = dataStream.toByteArray();  
//            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);  
//            data = null;  
//        }  
//        catch (IOException e)  
//        {  
//            e.printStackTrace();  
//            return null;  
//        }  
    }  

    /**  
     * 通过经纬度获取地址信息的另一种方法  
     * @param latitude  
     * @param longitude  
     * @return 城市名  
     */    
    public static String GetAddr(String latitude, String longitude) {      
        String addr = "";      
            
        /*  
         * 也可以是http://maps.google.cn/maps/geo?output=csv&key=abcdef&q=%s,%s，不过解析出来的是英文地址  
         * 密钥可以随便写一个key=abc  
         * output=csv,也可以是xml或json，不过使用csv返回的数据最简洁方便解析      
         */    
        String url = String.format(      
            "http://ditu.google.cn/maps/geo?output=csv&key=abcdef&q=%s,%s",      
            latitude, longitude);      
        URL myURL = null;      
        URLConnection httpsConn = null;      
        try {      
                
            myURL = new URL(url);      
        } catch (MalformedURLException e) {      
          e.printStackTrace();      
          return null;      
        }      
            
        try {      
            
            httpsConn = (URLConnection) myURL.openConnection();      
                
            if (httpsConn != null) {      
                InputStreamReader insr = new InputStreamReader(      
                        httpsConn.getInputStream(), "UTF-8");      
                BufferedReader br = new BufferedReader(insr);      
                String data = null;      
                if ((data = br.readLine()) != null) {      
                    String[] retList = data.split(",");      
                    if (retList.length > 2 && ("200".equals(retList[0]))) {      
                        addr = retList[2];      
                    } else {      
                        addr = "";      
                    }      
                }      
                insr.close();      
            }      
        } catch (IOException e) {      
            
            e.printStackTrace();      
           return null;      
        }      
           return addr;      
    }
	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) 
	{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static boolean isNumber(String str)  
    {  
        Pattern pattern = Pattern.compile("^[0-9]+(.[0-9]{2})?$");  
        Matcher match=pattern.matcher(str);  
        return match.matches();  
    }  
	//4.4版本获取裁剪路径
	@TargetApi(Build.VERSION_CODES.KITKAT) 
	public static String getPath(final Context context, final Uri uri) {  
		  
	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;  
	    // DocumentProvider  
	    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) 
	    {  
	        // ExternalStorageProvider  
	        if (isExternalStorageDocument(uri)) {  
	            final String docId = DocumentsContract.getDocumentId(uri);  
	            final String[] split = docId.split(":");  
	            final String type = split[0];  
	  
	            if ("primary".equalsIgnoreCase(type)) {  
	                return Environment.getExternalStorageDirectory() + "/" + split[1];  
	            }  
	            // TODO handle non-primary volumes  
	        }  
	        // DownloadsProvider  
	        else if (isDownloadsDocument(uri)) {  
	  
	            final String id = DocumentsContract.getDocumentId(uri);  
	            final Uri contentUri = ContentUris.withAppendedId(  
	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));  
	  
	            return getDataColumn(context, contentUri, null, null);  
	        }  
	        // MediaProvider  
	        else if (isMediaDocument(uri)) {  
	            final String docId = DocumentsContract.getDocumentId(uri);  
	            final String[] split = docId.split(":");  
	            final String type = split[0];  
	  
	            Uri contentUri = null;  
	            if ("image".equals(type)) {  
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;  
	            } else if ("video".equals(type)) {  
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;  
	            } else if ("audio".equals(type)) {  
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;  
	            }  
	  
	            final String selection = "_id=?";  
	            final String[] selectionArgs = new String[] {  
	                    split[1]  
	            };  
	  
	            return getDataColumn(context, contentUri, selection, selectionArgs);  
	        }  
	    }  
	    // MediaStore (and general)  
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {  
	  
	        // Return the remote address  
	        if (isGooglePhotosUri(uri))  
	            return uri.getLastPathSegment();  
	  
	        return getDataColumn(context, uri, null, null);  
	    }  
	    // File  
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {  
	        return uri.getPath();  
	    }  
	  
	    return null;  
	}  
	  
	/** 
	 * Get the value of the data column for this Uri. This is useful for 
	 * MediaStore Uris, and other file-based ContentProviders. 
	 * 
	 * @param context The context. 
	 * @param uri The Uri to query. 
	 * @param selection (Optional) Filter used in the query. 
	 * @param selectionArgs (Optional) Selection arguments used in the query. 
	 * @return The value of the _data column, which is typically a file path. 
	 */  
	public static String getDataColumn(Context context, Uri uri, String selection,  
	        String[] selectionArgs) {  
	  
	    Cursor cursor = null;  
	    final String column = "_data";  
	    final String[] projection = {  
	            column  
	    };  
	  
	    try {  
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,  
	                null);  
	        if (cursor != null && cursor.moveToFirst()) {  
	            final int index = cursor.getColumnIndexOrThrow(column);  
	            return cursor.getString(index);  
	        }  
	    } finally {  
	        if (cursor != null)  
	            cursor.close();  
	    }  
	    return null;  
	}  
	
	//t图片读取方法
	public static byte[] getBytes(InputStream is)
	{
		try {
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024]; // 用数据装
        int len = -1;
        
			while ((len = is.read(buffer)) != -1) {
			    outstream.write(buffer, 0, len);
			}
		
        outstream.close();
        // 关闭流一定要记得。
        return outstream.toByteArray();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
    }
	
	//保存图片到本地相册
	public static void saveImageToGallery(Context context, Bitmap bmp) throws Exception
	{
	    // 首先保存图片
	    File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
	    if (!appDir.exists()) {
	        appDir.mkdir();
	    }
	    String fileName = System.currentTimeMillis() + ".jpg";
	    File file = new File(appDir, fileName);
	        FileOutputStream fos = new FileOutputStream(file);
	        bmp.compress(CompressFormat.JPEG, 100, fos);
	        fos.flush();
	        fos.close();
	    
	    // 其次把文件插入到系统图库
	        MediaStore.Images.Media.insertImage(context.getContentResolver(),file.getAbsolutePath(), fileName, null);
	    // 最后通知图库更新
	    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,	Uri.fromFile(new File(file.getPath()))));
	}
	  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is ExternalStorageProvider. 
	 */  
	public static boolean isExternalStorageDocument(Uri uri) {  
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is DownloadsProvider. 
	 */  
	public static boolean isDownloadsDocument(Uri uri) {  
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is MediaProvider. 
	 */  
	public static boolean isMediaDocument(Uri uri) {  
	    return "com.android.providers.media.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is Google Photos. 
	 */  
	public static boolean isGooglePhotosUri(Uri uri) {  
	    return "com.google.android.apps.photos.content".equals(uri.getAuthority());  
	} 
}
