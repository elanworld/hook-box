package com.alan.hook;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import java.lang.reflect.Field;

/**
 * description
 *
 * @author wu xianNeng
 * @date 2022/8/8 11:27
 * @since JDK1.8
 */
public class HookText implements IXposedHookLoadPackage {
    private static String HOOK_PACKAGE_NAME = "com.alan.purplebox";
    private static String HOOK_CLASS_NAME = "com.alan.purplebox.ui.slideshow.SlideshowFragment";


    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) {
        Log.i("hook", "【handleLoadPackage】" + lpparam.packageName);//任何一个app启动时都会调用
        if (lpparam.packageName.equals(HOOK_PACKAGE_NAME)) {
            // hook方法，必须有方法参数
            XposedHelpers.findAndHookMethod(HOOK_CLASS_NAME, lpparam.classLoader, "onCreateView", LayoutInflater.class, ViewGroup.class,Bundle.class,
            new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Log.i("hook", "【afterHookedMethod】" + param.method.getName());
                    Class c = lpparam.classLoader.loadClass(HOOK_CLASS_NAME);//不能通过Class.forName()来获取Class，在跨应用时会失效
                    Field field = c.getDeclaredField("textView");
                    field.setAccessible(true);
                    TextView textView = (TextView) field.get(param.thisObject);
                    textView.setText("Hello Xposed");
                }
            });
        }
    }
}
