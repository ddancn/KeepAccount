package com.ddancn.keepaccount.exception;

/**
 * @author ddan.zhuang
 * @date 2019/10/16
 */
public class TypeNameDuplicateException extends Exception{

    public TypeNameDuplicateException() {
        super("类型名不要重复~");
    }
}