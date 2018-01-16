package cn.truistic.enmicromsg.info;

/**
 * Created by Administrator on 2017/5/18.
 */

public class ContentInfo {

    private String uin;
    private String imei;
    private String username;
    private String alias;
    private String conRemark;
    private String domainList;
    private String nickname;
    private String pyInitial;
    private String quanPin;
    private String showHead;
    private String type;
    private String weiboFlag;
    private String weiboNickname;
    private String conRemarkPYFull;
    private String conRemarkPYShort;
    private byte[] lvbuff;
    private String verifyFlag;
    private String encryptUsername;
    private String chatroomFlag;
    private String deleteFlag;
    private String contactLabelIds;


    public ContentInfo(String uin,String imei,String username, String alias, String conRemark,
                       String domainList,String nickname, String pyInitial, String quanPin, String showHead, String type, String
            weiboFlag, String weiboNickname, String conRemarkPYFull, String conRemarkPYShort,
            byte[] lvbuff, String verifyFlag, String encryptUsername, String chatroomFlag, String deleteFlag, String contactLabelIds) {
        this.uin = uin;
        this.imei = imei;
        this.username = username;
        this.alias = alias;
        this.conRemark = conRemark;
        this.domainList = domainList;
        this.nickname = nickname;
        this.pyInitial = pyInitial;
        this.quanPin = quanPin;
        this.showHead = showHead;
        this.type = type;
        this.weiboFlag = weiboFlag;
        this.weiboNickname = weiboNickname;
        this.conRemarkPYFull = conRemarkPYFull;
        this.conRemarkPYShort = conRemarkPYShort;
        this.lvbuff = lvbuff;
        this.verifyFlag = verifyFlag;
        this.encryptUsername = encryptUsername;
        this.chatroomFlag = chatroomFlag;
        this.deleteFlag = deleteFlag;
        this.contactLabelIds = contactLabelIds;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "ContentInfo{" +
                "uin='" + uin + '\'' +
                ", imei='" + imei + '\'' +
                ", username='" + username + '\'' +
                ", alias='" + alias + '\'' +
                ", conRemark='" + conRemark + '\'' +
                ", domainList='" + domainList + '\'' +
                ", nickname='" + nickname + '\'' +
                ", pyInitial='" + pyInitial + '\'' +
                ", quanPin='" + quanPin + '\'' +
                ", showHead='" + showHead + '\'' +
                ", type='" + type + '\'' +
                ", weiboFlag='" + weiboFlag + '\'' +
                ", weiboNickname='" + weiboNickname + '\'' +
                ", conRemarkPYFull='" + conRemarkPYFull + '\'' +
                ", conRemarkPYShort='" + conRemarkPYShort + '\'' +
                ", lvbuff='" + lvbuff + '\'' +
                ", verifyFlag='" + verifyFlag + '\'' +
                ", encryptUsername='" + encryptUsername + '\'' +
                ", chatroomFlag='" + chatroomFlag + '\'' +
                ", deleteFlag='" + deleteFlag + '\'' +
                ", contactLabelIds='" + contactLabelIds + '\'' +
                '}';
    }
}
