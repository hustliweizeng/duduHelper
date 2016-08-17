package com.dudu.duduhelper.http;

import android.content.Context;

import com.dudu.duduhelper.R;
import com.dudu.duduhelper.widget.ColorDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

/**
 * Created by lwz on 2016/8/17.
 */
public class HttpUtils {
	/**
	 * 联网工具类
	 * @param mContext 上下文
	 * @param params    请求的参数
	 * @param url   请求的地址
	 * @param method    请求方式
	 * @param mTextHttpResponseHandler  处理方式
	 */
	public static  void getConnection(final Context mContext, RequestParams params, String url,String method, TextHttpResponseHandler mTextHttpResponseHandler) {
		//请求联网时，主线程显示进度条
		ColorDialog.showRoundProcessDialog(mContext, R.layout.loading_process_dialog_color);
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece
		PersistentCookieStore myCookieStore = new PersistentCookieStore(mContext);
		client.setCookieStore(myCookieStore);
		if ("get".equalsIgnoreCase(method)){

			client.get(url, params,mTextHttpResponseHandler);

		}else if ("post".equalsIgnoreCase(method)){
			client.post(mContext,url,params,mTextHttpResponseHandler);

		}else{
			return;
		}

	}
}
