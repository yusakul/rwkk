package com.rwkk.hooks;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;


import com.rwkk.HookMain;

import java.net.URL;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedBridge.hookAllConstructors;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

//hook 获取手机通讯录的相关api 检查是否被调用
public class Contacts extends XC_MethodHook {

    public static final String TAG = "rwkk_Contacts:";
    public static Context context111;

    public static void initAllHooks(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedBridge.log("333333");

        findAndHookMethod(android.content.ContentResolver.class, "query", Uri.class, String[].class, String.class, String[].class, String.class,
                new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("4444444"+ param.args[0]);
                //XposedBridge.log(ContactsContract.Data.CONTENT_URI.toString());


                // 联系人表的URI —— content://com.android.contacts/contacts ，对应类静态常量为ContactsContract.Contacts.CONTENT_URI
                if((ContactsContract.Contacts.CONTENT_URI.toString()).equals(param.args[0].toString()) )
                {
                    XposedBridge.log("联系人表的URI");
                }
                // 联系人电话URI —— content://com.android.contacts/data/phones ，对应静态常量为ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                if((ContactsContract.CommonDataKinds.Phone.CONTENT_URI.toString()).equals(param.args[0].toString()) )
                {
                    XposedBridge.log("联系人电话URI");
                }
                // 联系人邮箱URI —— content://com.android.contacts/data/emails  ，对应静态常量为ContactsContract.CommonDataKinds.Email.CONTENT_URI
                if((ContactsContract.CommonDataKinds.Email.CONTENT_URI.toString()).equals(param.args[0].toString()) )
                {
                    XposedBridge.log("联系人邮箱URI");
                }
                // 联系人地址URI —— content://com.android.contacts/data/postals  ，对应静态常量为ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI
                if((ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI.toString()).equals(param.args[0].toString()) )
                {
                    XposedBridge.log("联系人地址URI");
                }
                // 所有联系人的Uri —— content://contacts/people
                if(("content://contacts/people").equals(param.args[0].toString()) )
                {
                    XposedBridge.log("所有联系人的Uri");
                }
                // 某个联系人x的Ur —— content://contacts/people/x
                if(("content://contacts/people/x").equals(param.args[0].toString()) )
                {
                    XposedBridge.log("某个联系人x的Ur");
                }
                // data表URI —— content://com.android.contacts/data ，对应静态常量为ContactsContract.Data.CONTENT_URI
                if((ContactsContract.Data.CONTENT_URI.toString()).equals(param.args[0].toString()) )
                {
                    XposedBridge.log("所有创建过的手机联系人的所有信息");
                }

                // raw_contacts表URI —— content://com.android.contacts/raw_contacts，对应静态常量为
                if(("content://com.android.contacts/raw_contacts").equals(param.args[0].toString()) )
                {
                    XposedBridge.log("所有创建过的手机联系人");
                }
                // groups —— content://com.android.contacts/groups，对应静态常量为
                if(("content://com.android.contacts/raw_contacts").equals(param.args[0].toString()) )
                {
                    XposedBridge.log("所有手机联系人分组");
                }
                // profile —— content://com.android.contacts/groups，对应静态常量为
                if(("content://com.android.contacts/profile").equals(param.args[0].toString()) )
                {
                    XposedBridge.log("个人通讯录主页");
                }



                XposedBridge.log("555555555"+ param.args[1]);
                String[] strs = new String[]{
                        ContactsContract.CommonDataKinds.Phone._ID,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID};
                if(param.args[1].equals(strs)) {
                    XposedBridge.log(TAG   + "获取通讯录");
                }


            }
        });


    }
}

//  content://com.android.calendar/calendar_alerts