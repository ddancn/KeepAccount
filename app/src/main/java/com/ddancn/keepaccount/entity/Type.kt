package com.ddancn.keepaccount.entity

import org.litepal.crud.LitePalSupport

/**
 * @author ddan.zhuang
 */
data class Type(var id: Int = 0,
                var name: String = "",
                var type: Int = 1)
    : LitePalSupport()
