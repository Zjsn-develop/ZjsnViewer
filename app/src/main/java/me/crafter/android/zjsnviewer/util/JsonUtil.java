package me.crafter.android.zjsnviewer.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.SparseArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import me.crafter.android.zjsnviewer.R;
import me.crafter.android.zjsnviewer.config.Storage;

/**
 * @author traburiss
 * @date 2016/6/17
 * @info ZjsnViewer
 * @desc
 */

public class JsonUtil {

    public static String getJsonFromRaw(Context context, int res){
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().openRawResource(res));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while((line = bufReader.readLine()) != null) {
                Result += line;
            }
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String loadJSONFromAsset(Context context, String filename) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static ArrayList<Long> TimesJsonGetTime(String json){

        ArrayList<Long> lists = new ArrayList<>();

        try {

            JSONArray jsonArray = new JSONArray(json);
            for(int i = 0; i < jsonArray.length(); i++){

                JSONArray subjson = jsonArray.getJSONArray(i);
                lists.add(subjson.getLong(0));
            }
        }catch (Exception e){

            lists.clear();
            e.printStackTrace();
        }
        return lists;
    }

    public static ArrayList<ArrayList<String>> TimesJsonGetName(String json){


        ArrayList<ArrayList<String>> lists = new ArrayList<>();

        try {

            JSONArray jsonArray = new JSONArray(json);
            for(int i = 0; i < jsonArray.length(); i++){

                JSONArray subjson = jsonArray.getJSONArray(i);

                JSONArray namearray = subjson.getJSONArray(1);
                ArrayList<String> namelist = new ArrayList<>();
                for (int j = 0; j < namearray.length(); j++){

                    namelist.add(namearray.getString(j));
                }

                lists.add(namelist);
            }
        }catch (Exception e){

            lists.clear();
            e.printStackTrace();
        }
        return lists;
    }

    public static SparseArray<JSONObject> getShipCard(Context context) throws JSONException{
        SparseArray<JSONObject> shipCard = new SparseArray<>();
        String json_file = JsonUtil.loadJSONFromAsset(context, "init.txt");
        JSONArray shipCardJson = new JSONObject(json_file).getJSONArray("shipCard");
        for (int i = 0; i < shipCardJson.length(); i++) {
            JSONObject ship = shipCardJson.getJSONObject(i);
            if (ship.has("cid")) {
                shipCard.append(ship.getInt("cid"), ship);
            }
        }
        return shipCard;
    }

    public static String long2hms(long time){
        String ret = "";

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        calendar.setTimeInMillis(time*1000);

        ret += calendar.get(Calendar.HOUR_OF_DAY) + Storage.str_hour[Storage.language];
        ret += calendar.get(Calendar.MINUTE) + Storage.str_minute_link2second[Storage.language];
        ret += calendar.get(Calendar.SECOND) + Storage.str_second[Storage.language];

        return ret;
    }

    public static String long2hms_digit(long time){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        return formatter.format(new Date(time*1000L));
    }
}
