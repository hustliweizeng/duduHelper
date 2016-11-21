package com.dudu.duduhelper.bean;

import android.net.Uri;

import java.io.Serializable;

public class ImageBean implements Serializable
{
	//本地路径
	private String path;
	//网络路径
	private Uri imageUri;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Uri getImageUri() {
		return imageUri;
	}
	public void setImageUri(Uri imageUri) {
		this.imageUri = imageUri;
	}
	

}
