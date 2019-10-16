package com.ddancn.keepaccount.constant;

/**
 * @author ddan.zhuang
 * @date 2019/10/16
 */
public enum TypeEnum {
    /**
     * 收入
     */
    IN(1),
    /**
     * 支出
     */
    OUT(-1);

    private int value;

    TypeEnum(int value){
        this.value = value;
    }

    public int value() {
        return value;
    }
}
