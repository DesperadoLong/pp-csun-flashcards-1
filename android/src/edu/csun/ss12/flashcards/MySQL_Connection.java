package edu.csun.ss12.flashcards;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MySQL_Connection {
/** Called when the activity is first created. */
   
   TextView txt;

public static final String KEY_121 = "http://calqlus.org/ss12/get_User_Id.php"; //i use my real ip here



private String[] getLoginInfo(String returnString,int user_id) {
   String[] returnArray = new String[3]; 
   InputStream is = null;
    
   String result = "";
    // send
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    nameValuePairs.add(new BasicNameValuePair("user_id",Integer.toString(user_id)));

    //http post
    try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(KEY_121);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

    }catch(Exception e){
            Log.e("log_tag", "Error in http connection "+e.toString());
    }

    //convert response to string
    try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
            }
            is.close();
            result=sb.toString();
    }catch(Exception e){
            Log.e("log_tag", "Error converting result "+e.toString());
    }
    //parse json data
    try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    Log.i("log_tag","id: "+json_data.getInt("user_id")+
                            ", name: "+json_data.getString("user_name")+
                            ", sex: "+json_data.getString("password")
                    );
                    returnArray[0] = Integer.toString(json_data.getInt("user_id"));
                    returnArray[1] = json_data.getString("user_name");
                    returnArray[2] = json_data.getString("password");
                    //Get an output to the screen
                    returnString += "\n\t" + jArray.getJSONObject(i); 
            }
    }catch(JSONException e){
            Log.e("log_tag", "Error parsing data "+e.toString());
    }
    return returnArray; 
}    
    
}