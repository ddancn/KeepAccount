package com.ddancn.keepaccount.entity

import org.litepal.crud.LitePalSupport

/**
 * @author ddan.zhuang
 */
class Type : LitePalSupport() {

    var id: Int = 0
    var name: String? = null
    var type: Int = 0

}
