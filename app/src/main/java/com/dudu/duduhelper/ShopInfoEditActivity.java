package com.dudu.duduhelper;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.Utils.ViewUtils;
import com.dudu.duduhelper.adapter.ShopCategoryAdapter;
import com.dudu.duduhelper.adapter.ShopCircleAdapter;
import com.dudu.duduhelper.adapter.ShopImageAdapter;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.ShopCategoryBean;
import com.dudu.duduhelper.javabean.ShopCricleBean;
import com.dudu.duduhelper.javabean.ShopInfoBean;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.MyAlertDailog;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ShopInfoEditActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton backButton;
    private ImageButton selectClickButton;
    private Button selectTextClickButton;
    private TextView title;
    private RelativeLayout relayout_mytitle;
    private ImageView iv_logo_shop_info;
    private RelativeLayout rl_logo_shop_info;
    private EditText ed_title_shop_info;
    private TextView tv_class_shop_info;
    private RelativeLayout rl_class_shop_info;
    private TextView tv_shopcircle_shop_info;
    private RelativeLayout rl_shopcircle_shop_info;
    private EditText ed_mobile_shop_info;
    private TextView tv_opentime_shop_info;
    private RelativeLayout rl_opentime_shop_info;
    private EditText ed_address_shop_info;
    private EditText ed_des_shop_info;
    private ImageView iv_img_shop_info;
    private TextView tv_imgNum_shop_info;
    private Button btn_subimt_shop_info;
    private Uri urilocal;
    //生成文件
    private File tempFile = new File(Environment.getExternalStorageDirectory(),getPhotoFileName());
    private ShopImageAdapter shopImageAdapter =new ShopImageAdapter(this);
    private String imagepath;
    private String startTime;
    private String endTime;
    private ArrayList<String> picsPath;
    private int category_id;
    private int circle_id;
    private SharedPreferences sharePre;
    private String category_name;
    private String circle_name;
    private String backtUrl;
    private String uploadPicPath;
    private String[] urls;
    private String[] pics;
    private ArrayList<String> imagesToBroser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置新版样式，时间选择器是新版样式
       //setTheme(android.R.style.Theme_Holo_Light);
        sharePre = getSharedPreferences("shop_info",MODE_PRIVATE);
        setContentView(R.layout.shop_info_edit);
        initView();
        initHeadView("设置", true, false, 0);
        initData();
    }


    private void initView() {
        backButton = (ImageButton) findViewById(R.id.backButton);
        selectClickButton = (ImageButton) findViewById(R.id.selectClickButton);
        selectTextClickButton = (Button) findViewById(R.id.selectTextClickButton);
        title = (TextView) findViewById(R.id.title);
        relayout_mytitle = (RelativeLayout) findViewById(R.id.relayout_mytitle);
        iv_logo_shop_info = (ImageView) findViewById(R.id.iv_logo_shop_info);
        rl_logo_shop_info = (RelativeLayout) findViewById(R.id.rl_logo_shop_info);
        ed_title_shop_info = (EditText) findViewById(R.id.ed_title_shop_info);
        tv_class_shop_info = (TextView) findViewById(R.id.tv_class_shop_info);
        rl_class_shop_info = (RelativeLayout) findViewById(R.id.rl_class_shop_info);
        tv_shopcircle_shop_info = (TextView) findViewById(R.id.tv_shopcircle_shop_info);
        rl_shopcircle_shop_info = (RelativeLayout) findViewById(R.id.rl_shopcircle_shop_info);
        ed_mobile_shop_info = (EditText) findViewById(R.id.ed_mobile_shop_info);
        tv_opentime_shop_info = (TextView) findViewById(R.id.tv_opentime_shop_info);
        rl_opentime_shop_info = (RelativeLayout) findViewById(R.id.rl_opentime_shop_info);
        ed_address_shop_info = (EditText) findViewById(R.id.ed_address_shop_info);
        ed_des_shop_info = (EditText) findViewById(R.id.ed_des_shop_info);
        iv_img_shop_info = (ImageView) findViewById(R.id.iv_img_shop_info);
        tv_imgNum_shop_info = (TextView) findViewById(R.id.tv_imgNum_shop_info);
        btn_subimt_shop_info = (Button) findViewById(R.id.btn_subimt_shop_info);

        backButton.setOnClickListener(this);
        selectClickButton.setOnClickListener(this);
        selectTextClickButton.setOnClickListener(this);
        btn_subimt_shop_info.setOnClickListener(this);
        rl_logo_shop_info.setOnClickListener(this);
        rl_class_shop_info.setOnClickListener(this);
        rl_shopcircle_shop_info.setOnClickListener(this);
        rl_opentime_shop_info.setOnClickListener(this);
        iv_img_shop_info.setOnClickListener(this);
    }
    //初始化数据
    private void initData() {
        //1.先加载本地的数据
        long time = sharePre.getLong("update_time",0);
        LogUtil.d("ss",time+"="+sharePre.getString("info",""));
        //如果更新日期超过一周，重新加载
        long expireTime = 1000*60*60*24*1;
        if ((System.currentTimeMillis()-time) <= expireTime){
            //没超时的话加载本地数据
            if (TextUtils.isEmpty(sharePre.getString("info",""))){
                initDataFromJson(sharePre.getString("info",""));
                LogUtil.d("load","从缓存加载");
            }else {
                //2.本地没有数据时，请求网络数据
                requstHttpData();
            }
        }else {
            //2.请求网络数据
            requstHttpData();
        }


    }
    //请求网络数据
    private void requstHttpData() {
        //如果超时了，加载网络数据
        ColorDialog.showRoundProcessDialog(context,R.layout.loading_process_dialog_color);
        RequestParams params = new RequestParams();
        HttpUtils.getConnection(context, params, ConstantParamPhone.GET_SHOP_INFO, "GET", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                Toast.makeText(context,"网络异常，稍后再试",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                //LogUtil.d("info",s);
                //保存信息到本地,标记时间戳
                sharePre.edit().putLong("update_time", System.currentTimeMillis()).commit();
                sharePre.edit().putString("info",s).commit();
                try {
                    JSONObject object = new JSONObject(s);
                    String code =  object.getString("code");
                    if ("SUCCESS".equalsIgnoreCase(code)){
                        //数据请求成功后解析数据
                        ShopInfoEditActivity.this.initDataFromJson(s);
                        LogUtil.d("load","从网络加载");

                    }else {
                        //显示错误信息
                        String msg = object.getString("msg");
                        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ColorDialog.dissmissProcessDialog();
            }
        });
    }
   

    //根据json数据设置控件信息
    private void initDataFromJson(String s) {
        ShopInfoBean info =  new Gson().fromJson(s, ShopInfoBean.class);
        //设置logo
        ImageLoader.getInstance().displayImage(info.getLogo(),iv_logo_shop_info);
        //获取相册照片
        pics = info.getImages();
        if(pics!=null &&pics.length >0){
            //设置相册第一张
            ImageLoader.getInstance().displayImage(pics[0],iv_img_shop_info);
            //设置相册数量
            tv_imgNum_shop_info.setText("相册共"+pics.length+"张");
        }
        ed_title_shop_info.setText(info.getName());
        tv_class_shop_info.setText(info.getCategory_name());
        tv_shopcircle_shop_info.setText(info.getArea_name());
        ed_mobile_shop_info.setText(info.getContact());
        tv_opentime_shop_info.setText(info.getOpen_time());
        ed_address_shop_info.setText(info.getAddress());
        ed_des_shop_info.setText(info.getDescription());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.btn_subimt_shop_info:
                //提交按钮
                requstHttpConnection();
                break;
            case R.id.rl_logo_shop_info:
                //进入下个页面
                //打开系统相册
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/jpeg");
                startActivityForResult(intent,  1);
                break;
            case R.id.rl_class_shop_info:
                //请求网络数据获取行业分类信息
                HttpUtils.getConnection(context, null, ConstantParamPhone.GET_CATEGPRY_INFO, "GET", new TextHttpResponseHandler() {
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
                              //  LogUtil.d("category",s);
                               ShopCategoryBean categoryBean =  new Gson().fromJson(s, ShopCategoryBean.class);
                                showCategorySelctor(categoryBean.getData(),"选择行业");
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
            case R.id.rl_shopcircle_shop_info:
                //请求网络数据获取行业分类信息
                HttpUtils.getConnection(context, null, ConstantParamPhone.GET_SHOPCIRCLE_INFO, "GET", new TextHttpResponseHandler() {
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
                                ShopCricleBean cricleBean =  new Gson().fromJson(s, ShopCricleBean.class);
                                showCircleSelctor(cricleBean.getData(),"选择商圈");
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
            case R.id.rl_opentime_shop_info:
                final Calendar  calendar = Calendar.getInstance();

                //设置开始时间
                //第二个参数是设置选择器的主题，这样就不需要自定义修改了
                TimePickerDialog dailog = new TimePickerDialog(context, R.style.AppTheme_Dialog,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        startTime = hour+":"+minute;
                        //设置结束时间
                        TimePickerDialog dailog1 = new TimePickerDialog(context,R.style.AppTheme_Dialog, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                endTime = hour+":"+minute;
                                tv_opentime_shop_info.setText(startTime+"-"+endTime);
                            }
                        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);
                        dailog1.setTitle("设置结束时间");
                        dailog1.show();
                    }
                },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);
                dailog.setTitle("设置开始时间");
                dailog.show();

                break;
            case R.id.iv_img_shop_info:
                //进入选择相册页面
                Intent picIntent = new Intent(context,ShopImageViewBrower.class);
                //传递图片过去
                imagesToBroser = new ArrayList<>();
                for (String pic :pics){
                    imagesToBroser.add(pic);
                }
                picIntent.putExtra("imageList", imagesToBroser);
                picIntent.putExtra("type",5);
                //进入相册之前判断之前有无选择过
                if (picsPath!=null && picsPath.size()!=0){
                    picIntent.putStringArrayListExtra("imageList",picsPath);
                }
                startActivityForResult(picIntent,5);
                //设置图片监听
                final ShopImageViewBrower brower = new ShopImageViewBrower();
                brower.setOnDelListener(new ShopImageViewBrower.OnDelListener() {
                    @Override
                    public void Ondel() {
                        //当图片有删除动作时，主页动态监听
                        tv_imgNum_shop_info.setText("相册共"+brower.getImgeList().size()+"张");
                        //重新给当前图片集合赋值
                        LogUtil.d("info","执行了回调");
                    }
                });

                break;
        }
    }
    //显示行业选择框
    private void showCategorySelctor(final List<ShopCategoryBean.DataBean> category, final String title) {
        ShopCategoryAdapter adapter = new ShopCategoryAdapter(context,R.layout.item_circle_select);
        adapter.addAll(category);
        LogUtil.d("adapter",adapter.getCount()+"");
        MyAlertDailog.show(context,title,adapter );
        //通过接口回调，确认选择的条目，并展示出来
        MyAlertDailog.setOnItemClickListentner(new MyAlertDailog.OnItemClickListentner() {
            @Override
            public void Onclick(int poistion) {
                //设置选中的行业
                tv_class_shop_info.setText(category.get(poistion).getName());
                //设置选中的行业id
                category_id = Integer.parseInt(category.get(poistion).getId());
                category_name = category.get(poistion).getName();
            }
        });

    }
    //显示商圈选择框
    private void showCircleSelctor(final List<ShopCricleBean.DataBean> category, final String title) {
        ShopCircleAdapter adapter = new ShopCircleAdapter(context,R.layout.item_circle_select);
        adapter.addAll(category);
        MyAlertDailog.show(context,title,adapter );
        //通过接口回调，确认选择的条目，并展示出来
        MyAlertDailog.setOnItemClickListentner(new MyAlertDailog.OnItemClickListentner() {
            @Override
            public void Onclick(int poistion) {
                //设置选中的行业
                tv_shopcircle_shop_info.setText(category.get(poistion).getName());
                //设置选中的行业id
                circle_id = Integer.parseInt(category.get(poistion).getId());
                circle_name = category.get(poistion).getName();
            }
        });

    }

    //上传保存的信息
    private void requstHttpConnection() {
        //获取图片的信息
       
        /*iv_logo_shop_info.setDrawingCacheEnabled(true);
        Bitmap iv_logo = iv_logo_shop_info.getDrawingCache();
        iv_logo_shop_info.setDrawingCacheEnabled(false);*/
        String title = ed_title_shop_info.getText().toString().trim();
        String category = tv_class_shop_info.getText().toString();
        String circle = tv_shopcircle_shop_info.getText().toString();
        String mobile = ed_mobile_shop_info.getText().toString();
        String address = ed_address_shop_info.getText().toString();
        String descrip = ed_des_shop_info.getText().toString().trim();
        String open_time = tv_opentime_shop_info.getText().toString();
        //非空判断
        if (TextUtils.isEmpty(title)|TextUtils.isEmpty(category)|TextUtils.isEmpty(circle)|TextUtils.isEmpty(open_time)
                |TextUtils.isEmpty(mobile)|TextUtils.isEmpty(address)|TextUtils.isEmpty(descrip)){
            Toast.makeText(context,"内容填写不完整",Toast.LENGTH_LONG).show();
            return;
        }
        //修改哪个上传哪个
        RequestParams params = new RequestParams();
        params.put("name",title);
        params.put("logo",uploadPicPath);
        params.put("contact",mobile);
        params.put("address",address);
        params.put("description",descrip);
        params.put("images",urls);
        params.put("category",category_id);
        params.put("area",circle_id);
        params.put("open_time",open_time);
        System.out.print(uploadPicPath+"=="+urls);

        HttpUtils.getConnection(context, params, ConstantParamPhone.SAVE_SHOP_INFO, "POST", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                Toast.makeText(context,"网络异常，稍后再试",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                LogUtil.d("submit",s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code =  object.getString("code");
                    if ("SUCCESS".equalsIgnoreCase(code)){
                        //数据请求成功
                        Toast.makeText(context,"修改成功！",Toast.LENGTH_LONG).show();
                        finish();
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                //4.4以上的版本
                case 1:
                    //logo选择页面
                    String path = Util.getPath(context, data.getData());
                    Uri uri = Uri.fromFile(new File(path));
                    startPhotoZoom(uri);
                    System.out.println("开始裁剪"+data);
                    break;
                case 2:
                    //裁剪结束后返回
                    iv_logo_shop_info.setImageURI(urilocal);
                    //子线程上传图片，上传完毕handler告诉主线程
                    String imgPath = ViewUtils.getRealFilePath(context,urilocal);
                    uploadPicPath = ViewUtils.uploadImg(context,imgPath);
                    LogUtil.d("logo",uploadPicPath);

                    break;
                case 5:
                    //获取店家环境相册
                    picsPath = data.getStringArrayListExtra("pics");
                    LogUtil.d("back","backsize="+picsPath.size());
                    //设置封面照片
                    iv_img_shop_info.setImageBitmap(BitmapFactory.decodeFile(picsPath.get(0)));
                    //设置相册数量
                    tv_imgNum_shop_info.setText("相册共"+ picsPath.size()+"张");
                    //上传相册图片（网络地址）
                    urls = new String[picsPath.size()];
                    for (int i = 0;i<picsPath.size(); i++){
                        //只上传本地图片，网络图片地址不上传
                        if (!picsPath.get(i).startsWith("http")&&picsPath.get(i).contains("fail")){
                            urls[i] = ViewUtils.uploadImg(context,picsPath.get(i));
                            picsPath.remove(i);
                        }
                    }
                    //把剩余不需要上传的图片放到数组中
                    int i = urls.length;
                    for (String pp :picsPath){
                        urls[i] = pp;
                        i++;
                    }
                    LogUtil.d("pics", urls.toString());
                default:
                    break;
            }

        }
    }


    /**
         * 根据uri地址裁剪图片，缩放
         * @param uri
         */
    private void startPhotoZoom(Uri uri)
    {   //系统自带的裁剪工具
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);

        //裁剪后的名称,上传的时候用
        imagepath = Environment.getExternalStorageDirectory()+getPhotoFileName();
        File cropFile = new File(Environment.getExternalStorageDirectory(),getPhotoFileName());
        //把file转换成uri格式
        urilocal = Uri.fromFile(cropFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, urilocal);

        intent.putExtra("return-data", false);//设置返回data数据
        intent.putExtra("noFaceDetection", true); //关闭人脸检测

        startActivityForResult(intent, 2);
    }
    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName()
    {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
