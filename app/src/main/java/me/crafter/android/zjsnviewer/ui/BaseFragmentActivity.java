package me.crafter.android.zjsnviewer.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import me.crafter.android.zjsnviewer.R;
import me.crafter.android.zjsnviewer.ZjsnApplication;

/**
 * @author traburiss
 * @date 2016/6/16
 * @info ZjsnViewer
 * @desc
 */

public class BaseFragmentActivity extends AppCompatActivity {

    private final String TAG = BaseFragmentActivity.class.getSimpleName();

    protected Context context;
    protected ZjsnApplication application;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application = ZjsnApplication.getInstance();
        context = this;
    }

    protected void setToolbarTitle(int res){

        String title = getString(res);
        setToolbarTitle(title);
    }

    protected void setToolbarTitle(String title){

        try {

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle(title);
            toolbar.setNavigationOnClickListener(v -> finish());
        }catch (Exception e){

            e.printStackTrace();
        }
    }

    protected void startActivity(Class<?> cls){

        Intent intent = new Intent(context, cls);
        startActivity(intent);
    }

    protected void startActivityForResult(Class<?> cls, int requestCode){

        Intent intent = new Intent(context, cls);
        startActivityForResult(intent, requestCode);
    }
}
