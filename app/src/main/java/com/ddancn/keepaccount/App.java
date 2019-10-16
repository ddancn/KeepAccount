package com.ddancn.keepaccount;

import com.blankj.utilcode.util.Utils;

import org.litepal.LitePalApplication;

/**
 * @author ddan.zhuang
 */
public class App extends LitePalApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        Utils.init(this);
    }
}
