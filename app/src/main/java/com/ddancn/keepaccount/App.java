package com.ddancn.keepaccount;

import com.ddancn.keepaccount.util.ToastUtil;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import org.litepal.LitePalApplication;

public class App extends LitePalApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        ToastUtil.init(this);
        Iconify.with(new FontAwesomeModule());

    }
}
