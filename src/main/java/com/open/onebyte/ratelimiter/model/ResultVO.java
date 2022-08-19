package com.open.onebyte.ratelimiter.model;

import com.open.onebyte.ratelimiter.util.TracerUtil;

import java.io.Serializable;

/**
 * 统一返回的结果对象
 *
 * @param <T>
 * @author yangqk
 */
public class ResultVO<T> implements Serializable {

    private static final long serialVersionUID = -2035995113337494171L;
    /**
     * 接口响应是否正确
     */
    private Boolean result = true;
    /**
     * 接口错误编码，可对msg控制显示与否，以及显示形式；固定前两位60表示显示，61不显示；600：toast显示，601弹出框显示
     */
    private String kind;
    /**
     * 异常提示信息，客户端其根据code值来处理显示
     */
    private String msg;
    /**
     * 当前系统环境时间戳
     */
    private Long timestamp = System.currentTimeMillis();
    /**
     * 日志流水id
     */
    private String tracerId;
    /**
     * 返回的业务数据
     */
    private T data;

    /**
     * 返回成功时候无参方法
     */
    public static <T> ResultVO<T> ofSuccess() {
        ResultVO<T> resultVO = new ResultVO<>();
        resultVO.setResult(true);
        resultVO.setTracerId(TracerUtil.getTraceId());
        return resultVO;
    }

    /**
     * 返回成功，携带业务数据
     */
    public static <T> ResultVO<T> ofSuccess(T data) {
        ResultVO<T> resultVO = new ResultVO<>();
        resultVO.setResult(true);
        resultVO.setData(data);
        resultVO.setTracerId(TracerUtil.getTraceId());
        return resultVO;
    }

    /**
     * 返回异常，携带异常编码，和异常提示信息
     */
    public static <T> ResultVO<T> ofFail(String kind, String msg) {
        ResultVO<T> resultVO = new ResultVO<>();
        resultVO.setResult(false);
        resultVO.setKind(kind);
        resultVO.setMsg(msg);
        resultVO.setTracerId(TracerUtil.getTraceId());
        return resultVO;
    }

    /**
     * 返回异常，携带编码，异常信息，及异常时需返回的数据
     */
    public static <T> ResultVO<T> ofFail(String kind, String msg, T data) {
        ResultVO<T> resultVO = new ResultVO<>();
        resultVO.setResult(false);
        resultVO.setKind(kind);
        resultVO.setMsg(msg);
        resultVO.setData(data);
        resultVO.setTracerId(TracerUtil.getTraceId());
        return resultVO;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTracerId() {
        return tracerId;
    }

    public void setTracerId(String tracerId) {
        this.tracerId = tracerId;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultVO{" +
                "result=" + result +
                ", kind='" + kind + '\'' +
                ", msg='" + msg + '\'' +
                ", timestamp=" + timestamp +
                ", tracerId='" + tracerId + '\'' +
                ", data=" + data +
                '}';
    }
}
