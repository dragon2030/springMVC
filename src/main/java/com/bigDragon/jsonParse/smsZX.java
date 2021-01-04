package com.bigDragon.jsonParse;

/**
 * @author bigDragon
 * @create 2020-12-07 20:29
 */
public class smsZX {
    private int code;
    private String msg;
    private Data data;

    @Override
    public String toString() {
        return "smsZX{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
