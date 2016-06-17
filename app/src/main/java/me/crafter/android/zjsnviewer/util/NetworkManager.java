package me.crafter.android.zjsnviewer.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.zip.InflaterInputStream;

import me.crafter.android.zjsnviewer.config.Storage;
import me.crafter.android.zjsnviewer.ZjsnApplication;

public class NetworkManager {

    // TODO this class needs cleanup
    public static String url_passport_p7 = "http://login.alpha.p7game.com/index/passportLogin/";// +username/password
    //hm change the login in url as http://login.jianniang.com/index/passportLogin/
    //hm change the login in url as http://login.jr.moefantasy.com/index/passportLogin/ in 6/4/2016
    public static String url_passport_hm = "http://login.jr.moefantasy.com/index/passportLogin/";// +username/password
    public static String url_passport_hm_ios = "http://loginios.jianniang.com/index/passportLogin/";// +username/password

    public static String api_login = "index/login/";//+uid
    public static String api_Explore = "explore/start/"; // +fleetID/+exploreID/
    public static String api_getExploreResult = "explore/getResult/"; // +exploreID/
    public static String api_init = "api/initGame";

    public static String url_login = "";
    public static String url_server = "";
    public static int uid = 0;
    public static String loginCookie = "";

    public static String[] url_server_p7 = {
            "http://zj.alpha.p7game.com/",
            "http://s2.zj.p7game.com/",
            "http://s3.zj.p7game.com/",
            "http://s4.zj.p7game.com/",
            "http://s5.zj.p7game.com/",
            "http://s6.zj.p7game.com/",
            "http://s7.zj.p7game.com/",
            "http://s8.zj.p7game.com/",
            "http://s9.zj.p7game.com/",
            "http://s10.zj.p7game.com/",
            "http://s11.zj.p7game.com/",
    };

    public static String[] url_server_hm = {
            "http://zj.alpha.jr.moefantasy.com/",
            "http://s2.jr.moefantasy.com/",
            "http://s3.jr.moefantasy.com/",
            "http://s4.jr.moefantasy.com/",
            "http://s5.jr.moefantasy.com/",
            "http://s6.jr.moefantasy.com/",
            "http://s7.jr.moefantasy.com/",
            "http://s8.jr.moefantasy.com/",
            "http://s9.jr.moefantasy.com/",
            "http://s10.jr.moefantasy.com/",
            "http://s11.jr.moefantasy.com/",
            "http://s12.jr.moefantasy.com/"
    };

    public static String[] url_server_hm_ios = {
        "http://s101.jr.moefantasy.com/",
        "http://s102.jr.moefantasy.com/",
        "http://s103.jr.moefantasy.com/",
        "http://s104.jr.moefantasy.com/",
        "http://s105.jr.moefantasy.com/",
        "http://s106.jr.moefantasy.com/"
    };
    public static String getCurrentUnixTime() {
        long unixTime = System.currentTimeMillis();
        return String.valueOf(unixTime);
    }
    public static int currentUnix(){
        return ((int)(System.currentTimeMillis() / 1000L));
    }

    //将gzip压缩的字符串解压
    //加入gz=1返回的应该不是gzip？
    //java.io.IOException: unknown format (magic number da78)
    //问题解决，不要使用GZIPInputStream， 78da - data compressed by "zlib.zlibConst.Z_BEST_COMPRESSION" (or int=9) marker
    //使用InflaterInputStream 代替 GZIPInputStream
    public static InputStream decompress(InputStream in) {
        return new InflaterInputStream(in);
        /*
        try {
        GZIPInputStream gis = new GZIPInputStream(in);
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
        String outStr = "";
        String line;
        while ((line=bf.readLine())!=null) {
            outStr += line;
        }
        return outStr;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return "ERR1";
    */
}

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString().substring(0, 16);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     *
     * @param urString url string without market
     * @return FinalUrl
     */
    public static String getFinalUrl(String urString) {
        String FinalUrl = urString + "/&t=" + getCurrentUnixTime();
        String market = "&gz=1&market=2&channel=0&version=2.4.0";
        //返回的是一个16位散列，暂时猜测是md5
        String FinalMd5 = md5(FinalUrl);
        FinalUrl += "&e=" + FinalMd5 + market;
        return FinalUrl;
    }

    public static class OperateSession{
        public String urlString;
        public String cookie;
        public URLConnection connection;
        private String response;
        OperateSession(String urString, String manualCookie){
            urlString = urString;
            cookie = manualCookie;
        }

        OperateSession(String urString){
            urlString = urString;
            cookie = loginCookie;
        }
        public String open(){
            response = "";
            try {
                urlString = getFinalUrl(urlString);
                URL url = new URL(urlString);
                Log.i("Session", url.toString());
                connection = url.openConnection();
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(15000);
                connection.setDoOutput(false);
                if (!cookie.equals("")) connection.setRequestProperty("cookie", cookie);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                decompress(connection.getInputStream()), "UTF-8"));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response += inputLine;
                }
                in.close();
                if (response.contains("\"eid\"")){
                    Log.i("Session", "get eid when get reward.");
                }

            }catch (Exception ex) {
                Log.e("Session", ex.toString());
            }
            // force sleep 3s after every network operation
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e){

            }
            return response;
        }


        public String getCookie(){
            try{
                List<String> cookies = connection.getHeaderFields().get("Set-Cookie");
                return TextUtils.join(";",cookies);
            } catch (Exception ex) {
                Log.e("Session", "can not get cookie");
            }
            return "";
        }
    }

    //// TODO: 6/8/2016    This should be out of NetworkManager, be apart. New Class Needed.
    public static boolean autoExplore() {
        Log.d("NetworkManager", "autoExplore");
        Log.d("NetworkManager", "check time");
        boolean explored = false;
        for (int i = 0; i < DockInfo.dockTravelTime.length; i++){
            int endTime = DockInfo.dockTravelTime[i];
            if(endTime < currentUnix()) {
                explored = explored|NetworkManager.explore(DockInfo.exploreID[i], i+1);
            }
        }
        return explored;
    }

    /**
     * @param exploreId 远征区域ID
     * @param fleetId 舰队编号
     * @return true or false
     */
    public static boolean explore(int exploreId, int fleetId){
        //// TODO: 6/12/2016  Use isNoAutoExploreNow instead  isNoDisturbNow.
        if (Storage.isNoDisturbNow()) return false;

//        get explore result
        String urString = url_server + api_getExploreResult + exploreId;
        OperateSession exploreSession = new OperateSession(urString);
        String response;
        response = exploreSession.open();

        if (response.contains("\"eid\"")){
            Log.i("autoExplore()", "get eid when get reward.");
            return false;
        }
        // Auto Explore
        urString = url_server + api_Explore + fleetId + "/" + exploreId;
        exploreSession.urlString = urString;

        response = exploreSession.open();
        if (response.contains("\"eid\"")){
            Log.i("autoExplore()", "get eid when auto explore.");
            return false;
        }
        return true;
    }
    public static void initServer(){
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ZjsnApplication.getAppContext());
        String server = prefs.getString("server", "-1");
        if (server.equals("-1")) return;
        int serverId = Integer.parseInt(server);

        if (serverId < 100){
            url_server = url_server_p7[serverId];
        }
        else if (serverId < 200) {
            url_server = url_server_hm[serverId-100];
        }
        else {
            url_server = url_server_hm_ios[serverId-201];
        }

        // Black: Alt Server
        boolean altserver = prefs.getBoolean("altserver", false);
        if (altserver){
            url_server = prefs.getString("alt_url_server", "");
        }
        Log.i("NetworkManager", "url_server:" + url_server);

        if (serverId < 100){
            url_login = url_passport_p7;
        }else if (serverId < 200){
            url_login = url_passport_hm;
        }else {
            url_login = url_passport_hm_ios;
        }
        if (altserver){
            url_login = prefs.getString("alt_url_login", "");
        }
        Log.i("NetworkManager", "url_login:" + url_login);
    }
    public static void updateDockInfo(){
        initServer();
        Log.i("NetworkManager", "updateDockInfo()");
        Log.i("NetworkManager", "Unix: " + getCurrentUnixTime());
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ZjsnApplication.getAppContext());

        Boolean on = prefs.getBoolean("on", false);
        if (!on){
            Storage.str_tiduName = Storage.str_notOn[Storage.language];
            DockInfo.updateInterval = 15;
            return;
        }
        if (ZjsnState.getZjsnState() == 0 && prefs.getBoolean("auto_run", true)){
            DockInfo.updateInterval = 15;
            Storage.str_tiduName = Storage.str_gameRunning[Storage.language];
            return;
        }
        boolean success;
        // STEP 1 PASSPORT LOGIN
        success = getAccountCookie();
        if (!success) return;
        // STEP 2 UID SERVER LOGIN
        success = login();
        if (!success) return;
        // STEP 3 GET USER DATA
        success =initGame();
        if (!success) return;
        boolean explored = autoExplore();
        if (explored) initGame();
    }
    public static boolean getAccountCookie(){
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ZjsnApplication.getAppContext());
        String error = "";
        String username = prefs.getString("username", "none");
        String password = prefs.getString("password", "none");
        String url;
        url = url_login + username+"/"+password;
        OperateSession workingSession = new OperateSession(url);
        String response = workingSession.open();
        loginCookie = workingSession.getCookie();
        try {
            JSONObject obj = new JSONObject(response);
            uid = obj.getInt("userId");
        } catch (JSONException ex) {
            Log.e("UpdateDockInfo()", "getAccountCookie error");
            ex.printStackTrace();
            return false;
        }
        return !response.equals("");
    }
    public static boolean login(){
        String url = url_server + api_login + uid;
        OperateSession workingSession = new OperateSession(url);
        String response = workingSession.open();
        return !response.equals("");
    }
    public static boolean initGame(){
        String urString = url_server + api_init;
        OperateSession workingSession = new OperateSession(urString);
        String response = workingSession.open();
//          Log.i("NetworkManager", response.toString());
        if (response.equals("")) return false;
        boolean success = DockInfo.parseDockInfo(response);
        if (!success) return false;
        Log.i("NetworkManager", "initGame successful");
        DockInfo.updateInterval += 75;
        DockInfo.updateInterval = Math.min(DockInfo.updateInterval, 1210);
        return true;
    }
}
