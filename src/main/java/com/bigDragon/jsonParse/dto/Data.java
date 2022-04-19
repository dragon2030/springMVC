package com.bigDragon.jsonParse.dto;

/**
 * @author bigDragon
 * @create 2020-12-07 20:31
 */
public class Data {
    private String content;
    private Long moTime;
    private String msgId;
    private String phone;
    private String channelNum;

    @Override
    public String toString() {
        return "Data{" +
                "content='" + content + '\'' +
                ", moTime=" + moTime +
                ", msgId='" + msgId + '\'' +
                ", phone='" + phone + '\'' +
                ", channelNum='" + channelNum + '\'' +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getMoTime() {
        return moTime;
    }

    public void setMoTime(Long moTime) {
        this.moTime = moTime;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getChannelNum() {
        return channelNum;
    }

    public void setChannelNum(String channelNum) {
        this.channelNum = channelNum;
    }
}
