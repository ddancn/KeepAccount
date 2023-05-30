package com.ddancn.keepaccount.exception

import com.blankj.utilcode.util.Utils
import com.ddancn.keepaccount.R

/**
 * @author ddan.zhuang
 * @date 2019/10/16
 */
class CategoryNameDuplicateException
    : Exception(Utils.getApp().getString(R.string.setting_category_duplicate))