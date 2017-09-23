package me.crafter.android.zjsnviewer.ui.unlock;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.crafter.android.zjsnviewer.R;
import me.crafter.android.zjsnviewer.ui.BaseFragmentActivity;
import me.crafter.android.zjsnviewer.util.DockInfo;
import me.crafter.android.zjsnviewer.util.JsonUtil;
import rx.Observable;

// // TODO: 9/23/2017 add progress bar for loading
public class UnlockActivity extends BaseFragmentActivity{

    @BindView(R.id.rv_unlock)
    RecyclerView rv_unlock;
    UnlockAdapter adapter;

    public ArrayList<HashMap<String, String>>  locked_ships = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_unlock);
        ButterKnife.bind(this);

        setToolbarTitle(R.string.unlock_title);
        changeNameAndUrlAndIcon("loading", "", "").subscribe(locked_ships::add);
        adapter = new UnlockAdapter(context, locked_ships);
        rv_unlock.setLayoutManager(new LinearLayoutManager(context));
        rv_unlock.setAdapter(adapter);
        new UnlockTask().execute();
    }

    private class UnlockTask extends AsyncTask{
        @Override
        protected Object doInBackground(Object... params) {
            locked_ships = getList();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            adapter.list = locked_ships;
            adapter.notifyDataSetChanged();
            super.onPostExecute(o);
        }
    }

    public ArrayList<HashMap<String, String>> getList() {

        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        try {
            JSONArray unlockShipVO = DockInfo.Dock.getJSONArray("unlockShip");
            ArrayList<Integer> unlockShips = new ArrayList<>();
            for (int i = 0; i < unlockShipVO.length(); i++) {
                unlockShips.add(unlockShipVO.getInt(i));
            }
            SparseArray<JSONObject> shipCard = JsonUtil.getShipCard(context);
            for (int i = 0; i < shipCard.size(); i++) {
                int cid = shipCard.keyAt(i);
                if (unlockShips.contains(cid) || cid > 18000000) continue;
                JSONObject card = shipCard.get(cid);
                String name = card.getString("title");
                int evolved = card.getInt("evoClass");

                String link = "http://js.ntwikis.com/jsp/apps/cancollezh/mainquery.jsp?uinput=" + name;
                if (evolved == 1) name += "(改)";
                changeNameAndUrlAndIcon(name, link, null).subscribe(list::add);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Observable<HashMap<String,String>> changeNameAndUrlAndIcon(String name, String Url, String Icon_Url){

        HashMap<String,String> item = new HashMap<>();
        item.put("WEB_NAME", name);
        item.put("WEB_URL", Url);
        item.put("WEB_ICON", Icon_Url);

        return Observable.just(item);
    }
}
