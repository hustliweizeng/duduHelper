package com.dudu.duduhelper.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dudu.duduhelper.Activity.BigBandActivity.ShopProductAddActivity;
import com.dudu.duduhelper.Activity.DiscountCardActivity.ShopDiscoutAddActivity;
import com.dudu.duduhelper.Activity.MyInfoActivity.ShopInfoEditActivity;
import com.dudu.duduhelper.Activity.RedBagActivity.EditRedbag2Activity;
import com.dudu.duduhelper.Activity.ShopManageActivity.ShopAddActivity;
import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.adapter.ShopImageAdapter;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.dudu.duduhelper.R;
/**
 * 选取图片的工具
 */
public class ShopImageViewBrower extends BaseActivity
{
	private ArrayList<String> imageList = new ArrayList<String>();
	private GridView ImageGridViewBrower;
	private LinearLayout selectLinearView;
	private ShopImageAdapter shopImageAdapter =new ShopImageAdapter(this);
	private Button selectFromImage;
	private Button selectFromCamare;
	private LinearLayout delectLinearView;
	//private ImageView imageView;
	private Uri uri = null;
	//编辑按钮
	private Button editButton;
	//生成文件
	private File tempFile = new File(Environment.getExternalStorageDirectory(),getPhotoFileName());
	private ImageView delectButton;
	public final int VERION_UP = 1;
	public final int VERION_DOWN = 2;
	public final int AFTER_CUT = 4;
	public final int AFTER_CAMERA = 3;
	private int sourceType;
	private String picPath;


	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_image_view_brower);
		//根据不同情况设置不同的页面标题
		sourceType = getIntent().getIntExtra("type",-1);
		//initHead();
		ImageButton btn_back = (ImageButton) findViewById(R.id.backButton);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		editButton=(Button) this.findViewById(R.id.selectClickButton);
		editButton.setText("编辑");
		editButton.setVisibility(View.VISIBLE);
		delectButton = (ImageView) this.findViewById(R.id.delectButton);
		delectButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				shopImageAdapter.delectSelectImageList();//删除选中的图片
			}
		});
		delectLinearView = (LinearLayout) this.findViewById(R.id.delectLinearView);
		editButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				shopImageAdapter.setCheckVisableOffOn();
				if(editButton.getText().equals("编辑"))
				{
			    	editButton.setText("取消");
			    	delectLinearView.setVisibility(View.VISIBLE);
			    	selectLinearView.setVisibility(View.GONE);
				}
				else
				{
					editButton.setText("编辑");
					delectLinearView.setVisibility(View.GONE);
					selectLinearView.setVisibility(View.GONE);
				}

			}
		});
		//获取传递过来的图片集合
		imageList = getIntent().getStringArrayListExtra("imageList");
		//从相册选取
		selectFromImage = (Button) this.findViewById(R.id.selectFromImage);
		//从相机选择
		selectFromCamare = (Button) this.findViewById(R.id.selectFromCamare);
		//多选图
		ImageGridViewBrower = (GridView) this.findViewById(R.id.ImageGridViewBrower);
		//选择框
		selectLinearView = (LinearLayout) this.findViewById(R.id.selectLinearView);
		//从相册选择按钮监听
		selectFromImage.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				//打开相册
				Intent intent=new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/jpeg");
				if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT)
				{
					    //4.4的版本，code=1
				        startActivityForResult(intent,  VERION_UP);
				}
				else
				{       //4.4一下的版本
				        startActivityForResult(intent, VERION_DOWN);
				}
			}
		});
		//从相机选择监听
		selectFromCamare.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				//指定调用相机拍照后照片的储存路径
				intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(tempFile));
                startActivityForResult(intent, AFTER_CAMERA);
			}
		});
		//点击照相机图片后，弹出从相册选择和拍照按钮
		shopImageAdapter.setOnSelectImageClickListener(new ShopImageAdapter.OnSelectImageClickListener()
		{
			@Override
			public void onSelectClick()
			{
				delectLinearView.setVisibility(View.GONE);
				selectLinearView.setVisibility(View.VISIBLE);
				Log.d("photeselect","从相册选择");
			}
		});
		//适配器添加数据
		//非空判断
		if (imageList!=null && imageList.size()!=0 ){
			shopImageAdapter.addAll(imageList);
		}
		//gridview设置适配器
		ImageGridViewBrower.setAdapter(shopImageAdapter);
	}

	private void initHead() {
		
		if (sourceType == 1){
			initHeadView("商品相册", true, false, 0);
		}else if(sourceType == 2){
			initHeadView("店铺相册", true, false, 0);
		}else if(sourceType == 3){
			initHeadView("红包相册", true, false, 0);
		}else if (sourceType == 5){
			initHeadView("店铺相册",true,false,0);
		}else if (sourceType ==10){
			initHeadView("优惠券相册",true,false,0);
		}else if(sourceType == 8){
			initHeadView("红包相册",true,false,0);
		}
	}

	@Override
	//点击相册后返回数据的处理
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK)
		{
			switch (requestCode)
			{
			    //4.4以上的版本
				case VERION_UP:

					String path = Util.getPath(ShopImageViewBrower.this, data.getData());
					uri = Uri.fromFile(new File(path));
					startPhotoZoom(uri);
					System.out.println("hahahahha------::"+data);
					break;
				//4.4以下的版本
				case VERION_DOWN:
					String path1 = Util.getPath(ShopImageViewBrower.this, data.getData());
					uri = Uri.fromFile(new File(path1));
					startPhotoZoom(uri);
					System.out.println("hahahahha------::"+data);
					break;
			    //拍照返回的结果
				case AFTER_CAMERA:
					uri = Uri.fromFile(tempFile);
					startPhotoZoom(uri);
					System.out.println("hahahahha------::"+data);
					break;
			    //裁剪返回的结果
				case AFTER_CUT:
					//把裁剪后的地址插入到适配器的数据源
					shopImageAdapter.add(picPath);
					break;
				default:
					break;
			}
			//把弹窗隐藏
			delectLinearView.setVisibility(View.GONE);
			selectLinearView.setVisibility(View.GONE);
		}
		else
		{
			Toast.makeText(ShopImageViewBrower.this, "操作已取消", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 根据uri地址裁剪图片
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

		//裁剪后的文件
		picPath = Environment.getExternalStorageDirectory()+File.separator+getPhotoFileName();
    	File cropFile = new File(Environment.getExternalStorageDirectory(),getPhotoFileName());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cropFile));
        intent.putExtra("return-data", false);//设置返回data数据
        intent.putExtra("noFaceDetection", true); //关闭人脸检测
        startActivityForResult(intent, AFTER_CUT);
    }
	// 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName()
    {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    @Override
    public void RightButtonClick()
    {
    	super.RightButtonClick();
    }


	@Override//一定要在最后调父super，或者手动finish
	public void onBackPressed() {
		ArrayList<String> uplodImgs = shopImageAdapter.getImageList();
		
		//设置返回的数据
		Intent intent =null;
		if (sourceType == 5){
			//相册
			intent = new Intent(context,ShopInfoEditActivity.class);
		}
		if (sourceType == 6){
			intent = new Intent(context,ShopAddActivity.class);
			LogUtil.d("uplodImgs1",uplodImgs.size()+"");

		}
		if (sourceType ==1){
			//大牌抢购添加
			intent = new Intent(context,ShopProductAddActivity.class);
		}
		if(sourceType ==10){
			//优惠券添加
			intent = new Intent(context, ShopDiscoutAddActivity.class);
		}
		if (sourceType == 8){
			//从红包详情
			intent = new Intent(context, EditRedbag2Activity.class);
		}
		//传递数据
		//返回数据之前做非空判断
		if (uplodImgs !=null ){
			intent.putExtra("pics", (Serializable) uplodImgs);
			setResult(RESULT_OK,intent);//成功
		}else {
			setResult(RESULT_CANCELED,intent);//取消
		}
		
		super.onBackPressed();

	}


}
