package com.ddancn.keepaccount.entity

import org.litepal.crud.LitePalSupport

import java.io.Serializable

/**
 * @author ddan.zhuang
 */
class Record : LitePalSupport(), Serializable {

    var id: Int = 0
    var date: String? = null
    var money: Double = 0.0
    var detail: String? = null
    var type: Int = 0
    var typeName: String? = null

}
