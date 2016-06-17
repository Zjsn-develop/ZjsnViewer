package me.crafter.android.zjsnviewer.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import me.crafter.android.zjsnviewer.R;
import me.crafter.android.zjsnviewer.ZjsnApplication;

/**
 * @author traburiss
 * @date 2016/6/16
 * @info ZjsnViewer
 * @desc
 */

public class BaseFragmentActivity extends FragmentActivity {

    private final String TAG = "BaseFragmentActivity";

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
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();
                }
            });
        }catch (Exception e){

            e.printStackTrace();
        }
    };
}
