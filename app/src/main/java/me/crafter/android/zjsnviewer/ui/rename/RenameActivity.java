package me.crafter.android.zjsnviewer.ui.rename;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.crafter.android.zjsnviewer.R;
import me.crafter.android.zjsnviewer.ui.BaseFragmentActivity;
import me.crafter.android.zjsnviewer.util.DockInfo;
import me.crafter.android.zjsnviewer.util.JsonUtil;
import me.crafter.android.zjsnviewer.util.NetworkManager;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class RenameActivity extends BaseFragmentActivity {

    private final String TAG = "RenameActivity";
    private static String log = "";
    private static int UPDATE_LOG = 1;
    private static Handler updat_handler;


    @BindView(R.id.rv_rename)
    TextView rv_rename;
    @BindView(R.id.fb_rename)
    FloatingActionButton fb_rename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_rename);
        setToolbarTitle(R.string.rename_title);
        ButterKnife.bind(this);

        updat_handler = new UpdateHandler();
        rv_rename.setText(R.string.rename_not_start);
        fb_rename.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new RenameTask().execute();
            }
        });

    }

    private class UpdateHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==UPDATE_LOG){
                log += "\n" + (String)msg.obj;
                rv_rename.setText(log);
            }
            super.handleMessage(msg);
        }
    }

    private class RenameTask extends AsyncTask{
        @Override
        protected Object doInBackground(Object... params) {
            rename();
            return null;
        }
    }

    private void rename(){
        try {
            if (DockInfo.Dock == null){
                Toast.makeText(RenameActivity.this,R.string.username_hint, Toast.LENGTH_LONG).show();
                rv_rename.setText(R.string.username_hint);
                return;
            }
            append("开始初始化数据……");
            JSONArray shipVO = DockInfo.Dock.getJSONArray("userShipVO");
            SparseArray<JSONObject> shipCard = JsonUtil.getShipCard(context);
            append("数据初始化完成……");
            for (int i = 0; i < shipVO.length(); i++) {
                JSONObject ship = shipVO.getJSONObject(i);
                int cid = 0;
                if (ship.has("ship_cid")){
                    cid = ship.getInt("ship_cid");
                }else if (ship.has("shipCid")){
                    cid = ship.getInt("shipCid");
                } else {
                    continue;
                }

                JSONObject card = shipCard.get(cid);
                if (card == null){
                    append("can not find ship with cid: " + Integer.toString(cid));
                }else {
                    String nick_name = ship.getString("title");
                    String name = card.getString("title");
                    name = name.replace("日", "曰").trim();

                    if (!nick_name.equals(name)){
                        NetworkManager.rename(ship.getInt("id"), name);
                        append(nick_name + "改名为" + name);
                        Thread.sleep(3000);
                    }
                }
            }
        } catch (JSONException|InterruptedException e) {
            e.printStackTrace();
        }
        append("去除动物园结束");
    }

    private void append(String text){
        Handler handler = updat_handler;
        Message msg = handler.obtainMessage();
        msg.obj = text;
        msg.what = UPDATE_LOG;
        handler.sendMessage(msg);
    }
}
