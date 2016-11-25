package com.dudu.duduhelper.bean;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/11/25
 */

public class ss {

	private String comCode;
	private String id;
	private int noCount;
	private String noPre;
	private String startTime;

	public String getComCode() {
		String s = null;//这是请求到的字符串
		List<String> list = new ArrayList<>();
		try {
			JSONArray jsonArray = new JSONArray(s);
			for (int i = 0;i<jsonArray.length(); i++){
				JSONObject item = (JSONObject) jsonArray.get(i);
				list.add(item.getString("comCode"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}


		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNoCount() {
		return noCount;
	}

	public void setNoCount(int noCount) {
		this.noCount = noCount;
	}

	public String getNoPre() {
		return noPre;
	}

	public void setNoPre(String noPre) {
		this.noPre = noPre;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
}
