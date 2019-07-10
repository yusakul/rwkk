package com.rwkk;




import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;


public class HookMain implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    public static String packagename;
    public static final String TAG = "rwkk_HookMain:";
    public static final String ERROR = "rwkk_Error";

    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {

    }



    @Override
    public void handleLoadPackage(final LoadPackageParam loadPackageParam) throws Throwable {

        if(packagename!=null && packagename.equals("com.rwkk"))
        {
            com.rwkk.hooks.HttpHook.initAllHooks(loadPackageParam);
        }

        if (loadPackageParam.packageName.equals("com.rwkk")) {    //过滤包名
            //XposedBridge.log("find object" );

            XposedHelpers.findAndHookMethod("com.rwkk.AppListActivity", loadPackageParam.classLoader, "getAppList", String.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    packagename = param.args[0].toString();
                    XposedBridge.log("hook目标：" + param.args[0]);
                }
            });
        }

        if(packagename==null)
        {
            return;
        }

    }



    public static void logError(Error e){
        XposedBridge.log(HookMain.ERROR + " " + e.getMessage());
    }
}

