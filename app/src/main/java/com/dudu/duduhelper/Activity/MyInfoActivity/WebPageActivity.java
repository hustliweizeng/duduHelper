package com.dudu.duduhelper.Activity.MyInfoActivity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.widget.ColorDialog;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

public class WebPageActivity extends BaseActivity 
{
    private String action;
    private Button reloadButton;
    private WebView webViewPage;
	private String about;
	private String help;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_page);
		action=getIntent().getStringExtra("action");
		initView();
		initData();
	}

	private void initData() 
	{
		ColorDialog.showRoundProcessDialog(this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
        HttpUtils.getConnection(context,params,ConstantParamPhone.GET_APP_INFO, "GET",new TextHttpResponseHandler()
		{

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(WebPageActivity.this, "网络不给力呀", Toast.LENGTH_SHORT).show();
				reloadButton.setVisibility(View.VISIBLE);
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				LogUtil.d("about",arg2);
				try {
					JSONObject object = new JSONObject(arg2);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						JSONObject url = object.getJSONObject("url");
						about = url.getString("about");
						help = url.getString("help");
						LogUtil.d("about",about);
						LogUtil.d("help",help);
					}else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onFinish() 
			{
				ColorDialog.dissmissProcessDialog();
				if(action.equals("help"))
				{
					initHeadView("帮助中心", true, false, 0);
					webViewPage.loadUrl(help);
				}
				if(action.equals("about"))
				{
					initHeadView("关于商家助手", true, false, 0);
					webViewPage.loadUrl(about);

				}
			}
		});
	}

	private void initView() 
	{
		webViewPage=(WebView) this.findViewById(R.id.webViewPage);
		reloadButton=(Button) this.findViewById(R.id.reloadButton);
		//数据重载按钮
		reloadButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				initData();
			}
		});
		webViewPage.getSettings().setJavaScriptEnabled(true); //设置js权限，比如js弹出窗，你懂得
		webViewPage.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webViewPage.getSettings().setAppCacheEnabled(true);
		//webViewPage.getSettings().setSupportMultipleWindows(true);
		
	}

}
