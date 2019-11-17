package com.love.bts.ilovebts.listener;

// 통신에 쓰이는 interface
public interface IHTTPListener<T> {

    // 성공
    public void onSuccess(final T result);

    // 실패
    public void onFail(final int code, final String message, final T result);

}