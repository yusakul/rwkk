package com.rwkk;

import android.app.ActionBar;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.support.v7.app.AppCompatActivity;

import static com.rwkk.HookMain.packagename;


public class AppListActivity extends AppCompatActivity {
    private ArrayList<PackageInfo> apps;
    private SharedPreferences mPrefs;
    private SharedPreferences mPrefs_permission;
    ContentResolver resolver;
    public static String KEY_PACKAGE = "package";
    public  static final String strPrefs = "rwkkPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        resolver = this.getContentResolver();

        mPrefs_permission = getSharedPreferences("mPrefs_permission", Context.MODE_MULTI_PROCESS);

        mPrefs = getSharedPreferences(strPrefs, Context.MODE_MULTI_PROCESS);
        ListView appList= loadListView();

        appList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
            {
                loadListView();

                PackageInfo app = apps.get(position);
               // Toast.makeText(getApplicationContext(), "Bypass applied to " + app.getAppName(), Toast.LENGTH_LONG).show();


                //保存选择的包名
                clear(getApplicationContext());
                SharedPreferences.Editor edit = mPrefs.edit();
                edit.putString("package",app.getPckName());
                edit.apply();

                //传给一个函数，用来被hook获取点击的包名
                getAppList(app.getPckName());







                //获取选择应用的权限并保存
                HashMap<String, String[]> map = new HashMap<String, String[]>();

                android.content.pm.PackageInfo packageInfo = null;
                try {
                    packageInfo = getPackageManager().getPackageInfo(app.getPckName(), PackageManager.GET_PERMISSIONS);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                if(packageInfo==null) {
                    Toast.makeText(getApplicationContext(), "packageInfo null", Toast.LENGTH_LONG).show();
                    return;
                }
                //String[] permission = new String[10];
                String permission[] = packageInfo.requestedPermissions;//获取权限列表

                if(permission==null) {
                    Toast.makeText(getApplicationContext(), "permission null", Toast.LENGTH_LONG).show();
                    return;
                }
                map.put(app.getPckName(), permission);
                StringBuilder sb=new StringBuilder();
                for (int i = 0; i < permission.length; i++) {
                    sb.append("权限"+permission[i]+"\n");
                    SharedPreferences.Editor edit2 = mPrefs.edit();
                    edit2.putString(i + ":permission" ,permission[i]);
                    edit2.apply();
                }


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

    public static void clear(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(strPrefs, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();

        editor.commit();
    }

}
