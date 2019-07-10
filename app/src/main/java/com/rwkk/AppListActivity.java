package com.rwkk;

import android.app.ActionBar;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import android.support.v7.app.AppCompatActivity;


public class AppListActivity extends AppCompatActivity {
    private ArrayList<PackageInfo> apps;
    private SharedPreferences mPrefs;
    ContentResolver resolver;
    public static String KEY_PACKAGE = "package";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        resolver = this.getContentResolver();


        mPrefs = getSharedPreferences("rwkkPrefs", Context.MODE_MULTI_PROCESS);
        ListView appList= loadListView();

        appList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
            {
                PackageInfo app = apps.get(position);
                Toast.makeText(getApplicationContext(), "Bypass applied to " + app.getAppName(), Toast.LENGTH_LONG).show();

                SharedPreferences.Editor edit = mPrefs.edit();
                edit.putString("package",app.getPckName());
                edit.apply();

                getAppList(app.getPckName());

                loadListView();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences.Editor edit = mPrefs.edit();
        edit.putString("package","");
        edit.apply();


        loadListView();
        Toast.makeText(getApplicationContext(), "All hooks cleaned!", Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<PackageInfo> getInstalledApps() {
        ArrayList<PackageInfo> appsList = new ArrayList<>();
        List<android.content.pm.PackageInfo> packs = getPackageManager().getInstalledPackages(0);

        String packBypassed = mPrefs.getString("package","");

        for(int i=0;i<packs.size();i++) {

            android.content.pm.PackageInfo p = packs.get(i);
            PackageInfo pInfo = new PackageInfo();
            pInfo.setAppName(p.applicationInfo.loadLabel(getPackageManager()).toString());
            pInfo.setPckName(p.packageName);
            pInfo.setIcon(p.applicationInfo.loadIcon(getPackageManager()));

            if(p.packageName.trim().equals(packBypassed.trim()))
            {
                pInfo.setBypassed(true);
            }
            // Installed by user
            if ((p.applicationInfo.flags & 129) == 0) {
                appsList.add(pInfo);
            }
        }
        for(int i=0;i<packs.size();i++) {

            android.content.pm.PackageInfo p = packs.get(i);
            PackageInfo pInfo = new PackageInfo();
            pInfo.setAppName(p.applicationInfo.loadLabel(getPackageManager()).toString());
            pInfo.setPckName(p.packageName);
            pInfo.setIcon(p.applicationInfo.loadIcon(getPackageManager()));

            if(p.packageName.trim().equals(packBypassed.trim()))
            {
                pInfo.setBypassed(true);
            }
            // Installed by user
            if ((p.applicationInfo.flags & 129) == 1) {
                appsList.add(pInfo);
            }
        }
        return appsList;
    }

    private ListView loadListView()
    {
        ListView appList=(ListView)findViewById(R.id.apps_view);
        apps = getInstalledApps();
        appList.setAdapter(new AppsAdapter(this,apps));
        return appList;
    }

    private void getAppList(String str) {

    }

}
