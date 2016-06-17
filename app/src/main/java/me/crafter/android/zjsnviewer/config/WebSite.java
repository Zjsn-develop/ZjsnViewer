package me.crafter.android.zjsnviewer.config;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author traburiss
 * @date 2016/6/16
 * @info ZjsnViewer
 * @desc
 */

public class WebSite {

    public static final String WEB_NAME = "WEB_NAME";
    public static final String WEB_URL = "WEB_URL";
    public static final String WEB_ICON = "WEB_ICON";

    private ArrayList<HashMap<String,String>> list;

    private static WebSite instant;

    private WebSite(){

        addNameAndUrl("幻萌官网(没啥卯月)", "http://www.jianniang.com/default.html");
        addNameAndUrl("舰少资料库", "http://js.ntwikis.com/" , "http://js.ntwikis.com/jsp/apps/cancollezh/img/can.ico");
        addNameAndUrl("舰R百科", "http://www.zjsnrwiki.com/wiki/%E9%A6%96%E9%A1%B5", "http://www.zjsnrwiki.com/icon.ico");
        addNameAndUrl("萌娘百科", "https://zh.moegirl.org/%E6%88%98%E8%88%B0%E5%B0%91%E5%A5%B3","https://zh.moegirl.org/favicon.ico");
    }

    public static WebSite getInstant() {

        if (null == instant){

            instant = new WebSite();
        }
        return instant;
    }

    public ArrayList<HashMap<String, String>> getList() {

        return list;
    }

    private void addNameAndUrl(String name, String Url){

        addNameAndUrl(name, Url, "");
    }

    private void addNameAndUrl(String name, String Url, String Icon_Url){

        HashMap<String,String> item = new HashMap<>();
        item.put(WEB_NAME, name);
        item.put(WEB_URL, Url);
        item.put(WEB_ICON, Icon_Url);

        if (null == list){

            list = new ArrayList<>();
        }

        list.add(item);
    }
}
