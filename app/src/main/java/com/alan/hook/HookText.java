package com.alan.hook;

import android.os.Bundle;
import android.util.Log;
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
        Log.i("bqt", "【handleLoadPackage】" + lpparam.packageName);//任何一个app启动时都会调用
        if (lpparam.packageName.equals(HOOK_PACKAGE_NAME)) { //匹配指定的包名
            //参数：String className, ClassLoader classLoader, String methodName, Object... parameterTypesAndCallback
            XposedHelpers.findAndHookMethod(lpparam.packageName, lpparam.classLoader, "onCreateView", Bundle.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Log.i("bqt", "【afterHookedMethod】" + param.method.getName()); //当Hook成功后回调
                    Class c = lpparam.classLoader.loadClass(HOOK_CLASS_NAME);//不能通过Class.forName()来获取Class，在跨应用时会失效
                    Field field = c.getDeclaredField("textView");
                    field.setAccessible(true);
                    TextView textView = (TextView) field.get(param.thisObject);//param.thisObject为执行该方法的对象，在这里指Activity
                    textView.setText("Hello Xposed");
                    //可以调用param.setResult()设置方法的返回值！
                }
            });
        }
    }
}
