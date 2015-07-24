package com.xlw.model;

/**
 * Created by xinliwei on 2015/7/4.
 *
 * 访问数据库\文件系统\网络的请求往往是异步的,所以可向其方法中传入这个接口参数
 * 当xxxxDBHelper方法执行结束,可回调该接口中的方法
 */
public interface ActionCallbackListener<T> {

    /**
     * 成功时调用
     *
     * @param data 返回的数据
     */
    public void onSuccess(T data);

    /**
     * 失败时调用
     *
     * @param errorEvent 错误码
     * @param message    错误信息
     */
    public void onFailure(String errorEvent, String message);

}
