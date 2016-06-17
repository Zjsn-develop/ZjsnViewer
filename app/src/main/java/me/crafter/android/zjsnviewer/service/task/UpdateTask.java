package me.crafter.android.zjsnviewer.service.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import me.crafter.android.zjsnviewer.service.service.TimerService;
import me.crafter.android.zjsnviewer.util.DockInfo;
import me.crafter.android.zjsnviewer.ui.widget.Widget_Build;
import me.crafter.android.zjsnviewer.ui.widget.Widget_Main;
import me.crafter.android.zjsnviewer.ui.widget.Widget_Make;
import me.crafter.android.zjsnviewer.ui.widget.Widget_Repair;
import me.crafter.android.zjsnviewer.ui.widget.Widget_Travel;

/**
 * Created by paleneutron on 6/3/2016.
 */
public class UpdateTask extends AsyncTask<Void,Void,Void> {
    private Context context=null;
    private onUpdateTaskStateChange updateTaskStateChange;
    public UpdateTask(Context main_context){
        context = main_context;
    }

    public void setUpdateTaskStateChange(onUpdateTaskStateChange updateTaskStateChange) {
        this.updateTaskStateChange = updateTaskStateChange;
    }

    @Override
    protected Void doInBackground(Void... params) {
        DockInfo.requestUpdate();
        Widget_Main.updateWidget(context);
        Widget_Travel.updateWidget(context);
        Widget_Repair.updateWidget(context);
        Widget_Build.updateWidget(context);
        Widget_Make.updateWidget(context);
        context.startService(new Intent(context, TimerService.class));
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (null != updateTaskStateChange) updateTaskStateChange.AfterTask();
    }

    public interface onUpdateTaskStateChange{

        void AfterTask();
    }
}