package com.dudu.duduhelper.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.dudu.duduhelper.bean.MessageReqBean;
import com.dudu.duduhelper.Utils.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;



public class HttpConnection {

	public static boolean  getConn(String urlStr){
		URL url = null;
		HttpURLConnection connection = null;
		try
		{
			url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(5000);
			connection.connect();
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		finally
		{
			if(connection != null)
			{
				connection.disconnect();
			}
		}
	}
	
	public static Bitmap getImg(String urlpath)
	{

		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(urlpath);
		client.getParams().getIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 5000);
		try 
		{
			HttpResponse res=client.execute(get);
			if(res.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
			{
				HttpEntity entity=res.getEntity();
				byte[] bytes=EntityUtils.toByteArray(entity);
				Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                return bm;         
 			}
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;

	}
	
     public static String httpPost(String urlStr,String method,String code)
     {
    	
    	String result=null;
    	urlStr = urlStr.replaceAll(" ", "%20");
 		HttpPost httpRequest = new HttpPost(urlStr);
 		MessageReqBean messagereqbean = new MessageReqBean();
		//messagereqbean.setMethod(method);
		messagereqbean.setContent(code);
		code = Util.tojson(messagereqbean);
		try 
		{
			if (!code.equals("")) 
			{
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("phoneParam", code));
				httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			}
			DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
			//请求超时	
			defaultHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000); 
			//读取超时
			defaultHttpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
			HttpResponse httpResponse = defaultHttpClient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) 
			{
				result = EntityUtils.toString(httpResponse.getEntity());
			} 
			else
			{
				
			}
		} 
		catch (ClientProtocolException e) 
		{
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();//org.apache.http.conn.HttpHostConnectException: Connection to http://192.168.1.104:8080 refused
		}
 		return result;
     }
	
	public static byte[] readData(InputStream inStream) throws IOException
	{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while((len = inStream.read(buffer)) != -1)
		{
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		inStream.close();
		outStream.close();
		return data;
	}
	
	
}
