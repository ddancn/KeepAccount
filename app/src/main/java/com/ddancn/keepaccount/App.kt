package com.ddancn.keepaccount;

import android.app.Application
import com.blankj.utilcode.util.Utils
import org.litepal.LitePal

/**
 * @author ddan.zhuang
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Utils.init(this)
        LitePal.initialize(this)
    }
}
