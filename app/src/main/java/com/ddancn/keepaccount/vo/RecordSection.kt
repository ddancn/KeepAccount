package com.ddancn.keepaccount.vo

import com.chad.library.adapter.base.entity.SectionEntity
import com.ddancn.keepaccount.entity.Record

/**
 * @author ddan.zhuang
 * @date 2023/6/16
 * @description
 */
class RecordSection : SectionEntity<Record> {
    constructor(isHeader: Boolean, header: String) : super(isHeader, header)

    constructor(r: Record) : super(r)
}
