package com.dudu.duduhelper;

import org.apache.http.Header;

import com.dudu.duduhelper.bean.OrderBean;
import com.dudu.duduhelper.bean.UrlBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.MyDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class WebPageActivity extends BaseActivity 
{
    private String action;
    private Button reloadButton;
    private WebView webViewPage;
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
		// TODO Auto-generated method stub
		ColorDialog.showRoundProcessDialog(this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(WebPageActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.get(ConstantParamPhone.IP+ConstantParamPhone.GET_URL, params,new TextHttpResponseHandler()
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
				final UrlBean orderBean=new Gson().fromJson(arg2,UrlBean.class);
				if(!orderBean.getStatus().equals("1"))
				{
					//Toast.makeText(getActivity(), "g！", Toast.LENGTH_SHORT).show();
					//Toast.makeText(getActivity(), "出错啦！", Toast.LENGTH_SHORT).show();
					MyDialog.showDialog(WebPageActivity.this, "该账号已在其他手机登录，是否重新登录", false, true, "取消", "确定",new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(WebPageActivity.this,LoginActivity.class);
							startActivity(intent);
						}
					});
				}
				else
				{
					if(orderBean.getData()!=null)
					{
						if(action.equals("help"))
						{
							if(!TextUtils.isEmpty(orderBean.getData().getUrl().getHelp()))
							{
								webViewPage.loadUrl(orderBean.getData().getUrl().getHelp());
								webViewPage.setWebViewClient(new WebViewClient()
								{

								      @Override
								      public boolean shouldOverrideUrlLoading(WebView view, String url) {
								        // TODO Auto-generated method stub
								        view.loadUrl(url);
								        return true;
								      }
								});
							}
						}
						if(action.equals("about"))
						{
							if(!TextUtils.isEmpty(orderBean.getData().getUrl().getAbout()))
							{
								webViewPage.loadUrl(orderBean.getData().getUrl().getAbout());
								webViewPage.setWebViewClient(new WebViewClient()
								{
								      @Override
								      public boolean shouldOverrideUrlLoading(WebView view, String url) {
								        // TODO Auto-generated method stub
								        view.loadUrl(url);
								        return true;
								      }
								});
							}
						}
						
						
					}
					else
					{
						reloadButton.setVisibility(View.VISIBLE);
					}
				}
			}
			@Override
			public void onFinish() 
			{
				// TODO Auto-generated method stub
				ColorDialog.dissmissProcessDialog();
			}
		});
	}

	private void initView() 
	{
		// TODO Auto-generated method stub
		if(action.equals("help"))
		{
			initHeadView("帮助中心", true, false, 0);
		}
		if(action.equals("about"))
		{
			initHeadView("关于商家助手", true, false, 0);
		}
		webViewPage=(WebView) this.findViewById(R.id.webViewPage);
		reloadButton=(Button) this.findViewById(R.id.reloadButton);
		//数据重载按钮
		reloadButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				initData();
			}
		});
		webViewPage.getSettings().setJavaScriptEnabled(true); //设置js权限，比如js弹出窗，你懂得
		webViewPage.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//		webViewPage.getSettings().setSupportMultipleWindows(true);
		//webViewPage.setWebViewClient(new );
	}

}
