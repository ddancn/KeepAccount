package com.ddancn.keepaccount.entity

import org.litepal.crud.LitePalSupport

import java.io.Serializable

/**
 * @author ddan.zhuang
 */
data class Record(var id: Int = 0,
                  var date: String = "",
                  var money: Double = 0.0,
                  var detail: String = "",
                  var type: Int = 1,
                  var categoryName: String = "")
    : LitePalSupport(), Serializable
