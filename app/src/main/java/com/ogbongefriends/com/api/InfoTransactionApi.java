package com.ogbongefriends.com.api;

import java.io.InputStream;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;
import com.ogbongefriends.com.custom.BasicApi;

public class InfoTransactionApi extends BasicApi implements Runnable {

	// 1=activate 7 day pass
	//2= advertise me 
	//3= to purchase backStage Album
	
	private DB db;
	private String url;
	
	private Context ctx;
	HashMap<String, String> map1;
	@SuppressWarnings("unused")
	
	public static String auth_token;
	public static int resCode;
	private String postData;
	public static String resMsg;
	public static  JsonObject jsondata;
	Preferences pref;
	public int pass_7daysvalue;
 public static	int itempoints;
 public static	int points;
	
	CustomLoader p;


	public InfoTransactionApi(Context ctx, DB db, CustomLoader p) {
		super(ctx, db);
		this.db = db;
		this.ctx = ctx;
		this.p = p;
		//setPostData(map);
		url = Utils.getCompleteApiUrl(ctx, R.string.infoTransaction);
		
		pref=new  Preferences(ctx);
	}

	public void setPostData(HashMap<String, String> map){
		
		
		map1 = map;
		postData = getPostData(map1);

	}

	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (url == null) {
			Utils.log(Constants.kApiTag, "Url Not Found in Registration Api");
		} else {
			if (Utils.CNet()) {
				postData(url, postData);
			} else {
				p.cancel();
				Utils.NoInternet(ctx);
			}

		}
	}

	@Override
	protected void onResponseReceived(InputStream is) {
		// TODO Auto-generated method stub
		try {

			String res = getString(is);
			Log.d("Responce>>>>>>>>>>>>"+url ,res);
			JsonParser p = new JsonParser();
			JsonElement jele = p.parse(res);
			JsonObject obj = jele.isJsonObject() ? jele.getAsJsonObject()
					: null;

			if (obj == null) {
				onError(new ApiException(Constants.kApiError));
			} else {

				
				JsonObject objJson = obj.get(Constants.kappTag).getAsJsonObject();
				resCode = objJson.get("res_code").getAsInt();
				resMsg = objJson.get("res_msg").getAsString();
				
				
				if(resCode==1){
				
					
					JsonArray daypass = objJson.get("data").getAsJsonArray();

					for (int i = 0; i < daypass.size(); i++) {							 		

							JsonObject ArrayObj = daypass.get(i).getAsJsonObject();
							 itempoints= ArrayObj.get("item_points").getAsInt();
							 points= ArrayObj.get("points").getAsInt();
			}
				}
			}
		} catch (Exception e) {
			onError(e);

		}

	}

	@Override
	protected void onError(final Exception e) {
		// TODO Auto-generated method stub
		if (ctx instanceof Activity) {
			((Activity) ctx).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Utils.onError(ctx, e);
				}
			});
		}
	}

	@Override
	protected void onDone() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateUI() {
		// TODO Auto-generated method stub

	}

	
	private String getPostData(HashMap<String, String> map12) {

		
		Log.v("infoTransaction", map12 + "");

		JSONObject json = new JSONObject();
		JSONObject json2 ;

		try {

			json2=new JSONObject(map12);
			
			json.put(Constants.kappTag, json2);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			json.toString(3);
			Log.v("signmap", json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json.toString();
	}
	
//	private String makrCompleteURL(HashMap<String, String> map12) {
//		// TODO Auto-generated method stub
//
//		
//		String fb_id=(map12.get("fb_id") != null) ? map12.get("fb_id") : "";
//		String endurl = "?email=" + map12.get("email") + "&password="+ map12.get("password") + "&fb_id=" + fb_id+ "&device_id=" + Utils.getDeviceID(ctx)+ "&platform_type=android-iphone";
//	
//		return endurl;
//	}


}


