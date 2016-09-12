package com.dudu.duduhelper.Activity.WelcomeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.Activity.WelcomeActivity.LoginActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.fragment.NewOrderFragment;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class ForgetPwdCertifyMobileActivity extends BaseActivity implements OnClickListener {

    private TextView bindPhoneText;
    private Button btnGetmess;
    private EditText messageCodeEditText;
    private Button submitPhoneBtn;
    private ImageView clear_bind_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        initHeadView("验证绑定手机", true, false, 0);
        initView();
    }

    private void initView() {
        bindPhoneText = (TextView) findViewById(R.id.bindPhoneText);
        //短信验证
        btnGetmess = (Button) findViewById(R.id.btnGetmess);
        btnGetmess.setOnClickListener(this);
        messageCodeEditText = (EditText) findViewById(R.id.messageCodeEditText);
        //提交按钮
        submitPhoneBtn = (Button) findViewById(R.id.submitPhoneBtn);
        submitPhoneBtn.setOnClickListener(this);
        //清除内容
        clear_bind_phone = (ImageView) findViewById(R.id.clear_bind_phone);
        clear_bind_phone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear_bind_phone:
                bindPhoneText.setText("");
                break;
            case R.id.btnGetmess:
                //验证码按钮
                if (TextUtils.isEmpty(bindPhoneText.getText().toString().trim())){
                    Toast.makeText(context,"手机号码不能为空",Toast.LENGTH_SHORT).show();
                    break;
                }
                RequestParams params = new RequestParams();
                params.put("mobile",sp.getString("mobile",""));
                params.put("type","password");
                HttpUtils.getConnection(context, params, ConstantParamPhone.GET_SMS_CONFIRM, "get", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                        Toast.makeText(context,"网络异常，稍后再试",Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onSuccess(int i, Header[] headers, String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            String code =  object.getString("code");
                            if ("SUCCESS".equalsIgnoreCase(code)){
                                //数据请求成功
                                showResidueSeconds();
                            }else {
                                //数据请求失败
                                String msg = object.getString("msg");
                                Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                break;
            case R.id.submitPhoneBtn:
                //提交按钮
                if (TextUtils.isEmpty(messageCodeEditText.getText().toString().trim())){
                    Toast.makeText(context,"验证码不能为空",Toast.LENGTH_SHORT).show();
                    break;
                }
                if (TextUtils.isEmpty(bindPhoneText.getText().toString().trim())){
                    Toast.makeText(context,"手机号码不能为空",Toast.LENGTH_SHORT).show();
                    break;
                }
                //进入设置密码页面
                Intent intent = new Intent(context,ForgetPasswordActivity.class);
                intent.putExtra("code",messageCodeEditText.getText().toString().trim());
                intent.putExtra("mobile",bindPhoneText.getText().toString().trim());
                startActivity(intent);
                break;
        }
    }

    private void submit() {
        // validate
        String messageCodeEditTextString = messageCodeEditText.getText().toString().trim();
        if (TextUtils.isEmpty(messageCodeEditTextString)) {
            Toast.makeText(this, "输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }



    }
    /**
     * 显示剩余秒数
     */
    private void showResidueSeconds() {
        //显示倒计时按钮
        new CountDownTimer(60*1000,1000){

            @Override
            public void onTick(long lastTime) {
                //倒计时执行的方法
                btnGetmess.setClickable(false);
                btnGetmess.setFocusable(false);
                btnGetmess.setText(lastTime/1000+"秒后重发");
                btnGetmess.setTextColor(getResources().getColor(R.color.text_hint_color));
                btnGetmess.setBackgroundResource(R.drawable.btn_bg_hint);
                LogUtil.d("lasttime","剩余时间:"+lastTime/1000);
            }

            @Override
            public void onFinish() {
                btnGetmess.setClickable(true);
                btnGetmess.setFocusable(true);
                btnGetmess.setText("重新获取");

            }
        }.start();
    }
}
