package cn.truistic.enmicromsg.info;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/12.
 */

public class MessageInfo implements Serializable {


    private String mIMEI;
    private int msgId;
    private String msgSvrId;
    private String type;
    private String status;
    private String isSend;
    private String isShowTimer;
    private String createTime;
    private String talker;
    private String content;
    private String imgPath;
    private String reseved;
    private byte[] lvbuffer;
    private String transContent;
    private String transBrandWording;
    private String talkerId;
    private String bizClientMsgId;
    private String bizChatId;
    private String bizChatUserId;
    private String msgSeq;
    private String flag;

    public MessageInfo(String mIMEI,int msgId, String msgSvrId, String type, String status, String
            isSend,String isShowTimer, String createTime, String talker, String content,
            String imgPath, String reseved, byte[] lvbuffer, String transContent,
            String transBrandWording, String talkerId, String bizClientMsgId, String
            bizChatId, String bizChatUserId, String msgSeq, String flag) {
        this.mIMEI = mIMEI;
        this.msgId = msgId;
        this.msgSvrId = msgSvrId;
        this.type = type;
        this.status = status;
        this.isSend = isSend;
        this.isShowTimer = isShowTimer;
        this.createTime = createTime;
        this.talker = talker;
        this.content = content;
        this.imgPath = imgPath;
        this.reseved = reseved;
        this.lvbuffer = lvbuffer;
        this.transContent = transContent;
        this.transBrandWording = transBrandWording;
        this.talkerId = talkerId;
        this.bizClientMsgId = bizClientMsgId;
        this.bizChatId = bizChatId;
        this.bizChatUserId = bizChatUserId;
        this.msgSeq = msgSeq;
        this.flag = flag;
    }

    public String getIMEI() {
        return mIMEI;
    }

    public void setIMEI(String IMEI) {
        mIMEI = IMEI;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getMsgSvrId() {
        return msgSvrId;
    }

    public void setMsgSvrId(String msgSvrId) {
        this.msgSvrId = msgSvrId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsSend() {
        return isSend;
    }

    public void setIsSend(String isSend) {
        this.isSend = isSend;
    }

    public String getIsShowTimer() {
        return isShowTimer;
    }

    public void setIsShowTimer(String isShowTimer) {
        this.isShowTimer = isShowTimer;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTalker() {
        return talker;
    }

    public void setTalker(String talker) {
        this.talker = talker;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getReseved() {
        return reseved;
    }

    public void setReseved(String reseved) {
        this.reseved = reseved;
    }

    public byte[] getLvbuffer() {
        return lvbuffer;
    }

    public void setLvbuffer(byte[] lvbuffer) {
        this.lvbuffer = lvbuffer;
    }

    public String getTransContent() {
        return transContent;
    }

    public void setTransContent(String transContent) {
        this.transContent = transContent;
    }

    public String getTransBrandWording() {
        return transBrandWording;
    }

    public void setTransBrandWording(String transBrandWording) {
        this.transBrandWording = transBrandWording;
    }

    public String getTalkerId() {
        return talkerId;
    }

    public void setTalkerId(String talkerId) {
        this.talkerId = talkerId;
    }

    public String getBizClientMsgId() {
        return bizClientMsgId;
    }

    public void setBizClientMsgId(String bizClientMsgId) {
        this.bizClientMsgId = bizClientMsgId;
    }

    public String getBizChatId() {
        return bizChatId;
    }

    public void setBizChatId(String bizChatId) {
        this.bizChatId = bizChatId;
    }

    public String getBizChatUserId() {
        return bizChatUserId;
    }

    public void setBizChatUserId(String bizChatUserId) {
        this.bizChatUserId = bizChatUserId;
    }

    public String getMsgSeq() {
        return msgSeq;
    }

    public void setMsgSeq(String msgSeq) {
        this.msgSeq = msgSeq;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }


    @Override
    public String toString() {
        return "WeixinInfo{" +
                "mIMEI='" + mIMEI + '\'' +
                ", msgId='" + msgId + '\'' +
                ", msgSvrId='" + msgSvrId + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", isSend='" + isSend + '\'' +
                ", isShowTimer='" + isShowTimer + '\'' +
                ", createTime='" + createTime + '\'' +
                ", talker='" + talker + '\'' +
                ", content='" + content + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", reseved='" + reseved + '\'' +
                ", lvbuffer='" + lvbuffer + '\'' +
                ", transContent='" + transContent + '\'' +
                ", transBrandWording='" + transBrandWording + '\'' +
                ", talkerId='" + talkerId + '\'' +
                ", bizClientMsgId='" + bizClientMsgId + '\'' +
                ", bizChatId='" + bizChatId + '\'' +
                ", bizChatUserId='" + bizChatUserId + '\'' +
                ", msgSeq='" + msgSeq + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
