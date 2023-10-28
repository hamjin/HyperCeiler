package com.sevtinge.hyperceiler.module.hook.voiceassist

import com.sevtinge.hyperceiler.module.base.BaseHook
import com.sevtinge.hyperceiler.utils.DexKit.addUsingStringsEquals
import com.sevtinge.hyperceiler.utils.DexKit.dexKitBridge
import com.sevtinge.hyperceiler.utils.log.XposedLogUtils
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import java.lang.reflect.Method

object UseThirdPartyBrowser : BaseHook() {
    private var browserActivityWithIntent: Method? = null
    override fun init() {
        dexKitBridge.findMethod {
            matcher {
                addUsingStringsEquals(
                    "IntentUtils", "permission click No Application can handle your intent"
                )
            }
        }.forEach {
            browserActivityWithIntent = it.getMethodInstance(lpparam.classLoader)
        }

        // XposedBridge.log("Hook到小爱同学进程！");
        /*try {
            val result: List<DexMethodDescriptor> =
                java.util.Objects.requireNonNull<List<DexMethodDescriptor>>(
                    VoiceAssistDexKit.mVoiceAssistResultMethodsMap["BrowserActivityWithIntent"]
                )
            for (descriptor in result) {
                browserActivityWithIntent = descriptor.getMethodInstance(lpparam.classLoader)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }*/
        XposedLogUtils.logI(TAG, this.lpparam.packageName, "com.miui.voiceassist browserActivityWithIntent method is $browserActivityWithIntent")
        // Class<?> clazz = XposedHelpers.findClass("e.D.L.pa.Wa", lpparam.classLoader);
        XposedBridge.hookMethod(browserActivityWithIntent, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: MethodHookParam) {
                super.beforeHookedMethod(param)
                // XposedBridge.log("0)Hook到Activity启动，开始判断");
                val intent = param.args[0] as android.content.Intent
                XposedLogUtils.logI(TAG, this@UseThirdPartyBrowser.lpparam.packageName, intent.toString())
                try {
                    if (intent.getPackage() == "com.android.browser") {
                        XposedLogUtils.logI(TAG, this@UseThirdPartyBrowser.lpparam.packageName, "com.miui.voiceassist get URL " + intent.dataString)
                        val uri = android.net.Uri.parse(intent.dataString)
                        val newIntent = android.content.Intent()
                        newIntent.setAction("android.intent.action.VIEW")
                        newIntent.setData(uri)
                        param.args[0] = newIntent
                    }
                } catch (e: Exception) {
                   XposedLogUtils.logE(TAG, this@UseThirdPartyBrowser.lpparam.packageName, e)
                }
            }
        })
    }
}