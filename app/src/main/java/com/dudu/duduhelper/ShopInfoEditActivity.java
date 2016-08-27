package com.dudu.duduhelper;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.adapter.ShopImageAdapter;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.widget.MyAlertDailog;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

                    }

                    @Override
                    public void onSuccess(int i, Header[] headers, String s) {

                    }
                });
                final String[] cats = {"旅游","餐饮","休闲","酒店"};
                showDailogSelctor(cats,"选择行业");
                break;
            case R.id.rl_shopcircle_shop_info:
                //请求网络数据获取行业分类信息
                HttpUtils.getConnection(context, null, ConstantParamPhone.GET_SHOPCIRCLE_INFO, "GET", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(int i, Header[] headers, String s) {

                    }
                });
                //请求网络数据
                //伪造数据,弹出对话选择框
                final String[] circls = {"二七广场","万达","火车站","酒店"};
                showDailogSelctor(circls,"选择商圈");

                break;
            case R.id.rl_opentime_shop_info:
                final Calendar  calendar = Calendar.getInstance();
                //设置开始时间
                TimePickerDialog dailog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        startTime = hour+":"+minute;
                        //设置结束时间
                        TimePickerDialog dailog1 = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
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
                picIntent.putExtra("type",5);
                //进入相册之前判断之前有无选择过
                if (picsPath!=null && picsPath.size()!=0){
                    picIntent.putStringArrayListExtra("imageList",picsPath);
                }
                startActivityForResult(picIntent,5);

                break;
        }
    }
    //显示选择框
    private void showDailogSelctor(final String[]  items, final String title) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,R.layout.item_circle_select);
        adapter.addAll(items);
        MyAlertDailog.show(context,title,adapter );
        //通过接口回调，确认选择的条目，并展示出来
        MyAlertDailog.setOnItemClickListentner(new MyAlertDailog.OnItemClickListentner() {
            @Override
            public void Onclick(int poistion) {
                if ("选择行业".equals(title)){
                    tv_class_shop_info.setText(items[poistion]);
                }else if ("选择商圈".equals(title)){
                    tv_shopcircle_shop_info.setText(items[poistion]);
                }
            }
        });

    }

    private void requstHttpConnection() {
       // HttpUtils.getConnection();
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

                    break;
                case 5:
                    //店家环境相册
                    picsPath = data.getStringArrayListExtra("pics");
                   // LogUtil.d("pics",picsPath.size()-1+"");
                    //设置封面照片
                    iv_img_shop_info.setImageBitmap(BitmapFactory.decodeFile(picsPath.get(0)));
                    //设置相册数量
                    tv_imgNum_shop_info.setText("相册共"+ picsPath.size()+"张");
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

    private void submit() {
        // validate
        String shopName = ed_title_shop_info.getText().toString().trim();
        if (TextUtils.isEmpty(shopName)) {
            Toast.makeText(this, "请输入店铺名称", Toast.LENGTH_SHORT).show();
            return;
        }

        String mobie = ed_mobile_shop_info.getText().toString().trim();
        if (TextUtils.isEmpty(mobie)) {
            Toast.makeText(this, "请输入联系方式", Toast.LENGTH_SHORT).show();
            return;
        }

        String address = ed_address_shop_info.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "请输入店铺地址", Toast.LENGTH_SHORT).show();
            return;
        }

        String descrption = ed_des_shop_info.getText().toString().trim();
        if (TextUtils.isEmpty(descrption)) {
            Toast.makeText(this, "请输入店家描述...", Toast.LENGTH_SHORT).show();
            return;
        }

       String url = ConstantParamPhone.SAVE_SHOP_INFO;
        RequestParams params = new RequestParams();
//        params.add("name",);
//        params.add("contact",);
//        params.add("address",);
//        params.add("descprition",);
//        params.add("category",);
//        params.add("area",);
//        params.add("open_time",);

        //请求网络传递信息
       // HttpUtils.getConnection(context,);



    }
}
