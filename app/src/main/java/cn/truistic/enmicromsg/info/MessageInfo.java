package cn.truistic.enmicromsg.info;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/12.
 */

public class MessageInfo implements Serializable {

    private String uin;
    private String imei;
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

    public MessageInfo(String uin,String imei,int msgId, String msgSvrId, String type, String status, String
            isSend,String isShowTimer, String createTime, String talker, String content,
            String imgPath, String reseved, byte[] lvbuffer, String transContent,
            String transBrandWording, String talkerId, String bizClientMsgId, String
            bizChatId, String bizChatUserId, String msgSeq, String flag) {
        this.uin = uin;
        this.imei = imei;
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
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return "MessageInfo{" +
                "uin='" + uin + '\'' +
                ", imei='" + imei + '\'' +
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
