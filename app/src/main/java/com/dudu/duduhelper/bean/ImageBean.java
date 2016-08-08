package com.dudu.duduhelper.bean;

import java.io.Serializable;

import android.net.Uri;

public class ImageBean implements Serializable
{
	private String path;
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
