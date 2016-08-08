package com.dudu.duduhelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dudu.duduhelper.adapter.ShopImageAdapter;
import com.dudu.duduhelper.adapter.ShopImageAdapter.OnSelectImageClickListener;
import com.dudu.duduhelper.bean.ImageBean;
import com.dudu.duduhelper.common.Util;
import com.dudu.duduhelper.widget.MyKeyBoard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ShopImageViewBrower extends BaseActivity 
{
	private List<ImageBean> imageList = new ArrayList<ImageBean>();
	private GridView ImageGridViewBrower;
	private LinearLayout selectLinearView;
	private ShopImageAdapter shopImageAdapter =new ShopImageAdapter(this);
	private Button selectFromImage;
	private Button selectFromCamare;
	private LinearLayout delectLinearView;
	//private ImageView imageView;
	private Uri uri = null;
	private Uri urilocal = null;
	private String imagepath = null;
	//编辑按钮
	private Button editButton;
	//生成文件
	private File tempFile = new File(Environment.getExternalStorageDirectory(),getPhotoFileName());
	private ImageView delectButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_image_view_brower);
		initHeadView("商品相册", true, false, 0);
		//imageView = (ImageView) this.findViewById(R.id.imageView);
		editButton=(Button) this.findViewById(R.id.selectTextClickButton);
		editButton.setText("编辑");
		editButton.setVisibility(View.VISIBLE);
		delectButton = (ImageView) this.findViewById(R.id.delectButton);
		delectButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shopImageAdapter.delectSelectImageList();
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
		imageList = (List<ImageBean>) getIntent().getSerializableExtra("imageList");
		selectFromImage = (Button) this.findViewById(R.id.selectFromImage);
		selectFromCamare = (Button) this.findViewById(R.id.selectFromCamare);
		ImageGridViewBrower = (GridView) this.findViewById(R.id.ImageGridViewBrower);
		selectLinearView = (LinearLayout) this.findViewById(R.id.selectLinearView);
		//从图库选择
		selectFromImage.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent=new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT  
				intent.addCategory(Intent.CATEGORY_OPENABLE);  
				intent.setType("image/jpeg");  
				if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT)
				{        
					    //4.4的版本
				        startActivityForResult(intent, 1);    
				}
				else
				{       //4.4一下的版本
				        startActivityForResult(intent, 2);   
				}   
			}
		});
		selectFromCamare.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
				//指定调用相机拍照后照片的储存路径
				intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(tempFile));
                startActivityForResult(intent, 3); 
			}
		});
		shopImageAdapter.setOnSelectImageClickListener(new OnSelectImageClickListener() 
		{
			@Override
			public void onSelectClick() 
			{
				delectLinearView.setVisibility(View.GONE);
				selectLinearView.setVisibility(View.VISIBLE);
			}
		});
		shopImageAdapter.addAll(imageList);
		ImageGridViewBrower.setAdapter(shopImageAdapter);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		 //4.4系统获取裁剪路径的方法，但是拍照是不适用的，因为只适合图库就是图片存在的前提下
		 
        //System.out.println(path);
		//针对4.4，可以把拍照后的图片保存在本地，通过文件名的方式寻找到uri，推荐做法
        //Uri.fromFile(tempFile);
		if(resultCode == RESULT_OK)
		{
			switch (requestCode) 
			{
			    //4.4以上的版本
				case 1:
					
					String path = Util.getPath(ShopImageViewBrower.this, data.getData());
					uri = Uri.fromFile(new File(path));
					startPhotoZoom(uri);
					System.out.println("hahahahha------::"+data);
					break;
				//4.4以下的版本
				case 2:
					String path1 = Util.getPath(ShopImageViewBrower.this, data.getData());
					uri = Uri.fromFile(new File(path1));
					startPhotoZoom(uri);
					System.out.println("hahahahha------::"+data);
					break;
			    //拍照返回的结果
				case 3:
					uri = Uri.fromFile(tempFile);
					startPhotoZoom(uri);
					System.out.println("hahahahha------::"+data);
					break;
			    //裁剪返回的结果
				case 4:
					ImageBean imageBean = new ImageBean();
					imageBean.setImageUri(urilocal);
					shopImageAdapter.add(imageBean);
					//imageView.setImageURI(urilocal);
					break;
				default:
					break;
			}
		}
		else
		{
			Toast.makeText(ShopImageViewBrower.this, "操作已取消", Toast.LENGTH_SHORT).show();
		}
		
	}
	private void startPhotoZoom(Uri uri) 
	{
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
        
        //裁剪后的名称
        imagepath = Environment.getExternalStorageDirectory()+getPhotoFileName();
    	File cropFile = new File(Environment.getExternalStorageDirectory(),getPhotoFileName());
        urilocal = Uri.fromFile(cropFile);
        
        intent.putExtra(MediaStore.EXTRA_OUTPUT, urilocal);
        
        intent.putExtra("return-data", false);//设置返回data数据
        intent.putExtra("noFaceDetection", true); //关闭人脸检测
        startActivityForResult(intent, 4);
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
    	// TODO Auto-generated method stub
    	super.RightButtonClick();
    }
	
}
