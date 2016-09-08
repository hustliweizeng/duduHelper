package com.dudu.duduhelper.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.dudu.duduhelper.R;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lwz on 2016/8/23.
 */
public class ViewUtils {
	/**
	 * 把图片转换为字节码流
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static byte[] imageToBytes(String path) throws IOException {
		InputStream inputStream = new FileInputStream(path);
		BufferedInputStream in = new BufferedInputStream(inputStream);
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);

		byte[] temp = new byte[1024];
		int size = 0;
		while ((size = in.read(temp)) != -1) {
			out.write(temp, 0, size);
		}
		in.close();
		inputStream.close();
		byte[] content = out.toByteArray();
		out.close();
		return content;
	}
	/**
	 * Try to return the absolute file path from the given Uri
	 *
	 * @param context
	 * @param uri
	 * @return the file path or null
	 */
	public static String getRealFilePath(final Context context, final Uri uri ) {
		if ( null == uri ) return null;
		final String scheme = uri.getScheme();
		String data = null;
		if ( scheme == null )
			data = uri.getPath();
		else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
			data = uri.getPath();
		} else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
			Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
			if ( null != cursor ) {
				if ( cursor.moveToFirst() ) {
					int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
					if ( index > -1 ) {
						data = cursor.getString( index );
					}
				}
				cursor.close();
			}
		}
		return data;
	}

	/**
	 * imageUri 上传图片的本地uri地址
	 */
	private static  String backtUrl;
	public static String uploadImg(final Context context,String imageUri) {

		String picBase64 = null;
		byte[] picByte = null;
		try {
			picByte = ViewUtils.imageToBytes(imageUri);
			//把字节流转换为BASE64编码
			picBase64 = Base64.encodeToString(picByte,1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		RequestParams params = new RequestParams();
		params.add("content",picBase64);
		HttpUtils.getConnection(context, params, ConstantParamPhone.UPLOAD_PIC, "post", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context,"网络异常，请稍后再试",Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						backtUrl = object.getString("url");
						LogUtil.d("pic_success",s);
					}else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
						LogUtil.d("pic_fail",s);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		if (!TextUtils.isEmpty(backtUrl)){
			return  backtUrl;
		}else {
			return "";
		}
	}



}
