package cn.truistic.enmicromsg.info;

/**
 * Created by Administrator on 2017/5/18.
 */

public class ContentInfo {

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


    public ContentInfo(String imei,String username, String alias, String conRemark, String
            domainList,String nickname, String pyInitial, String quanPin, String showHead, String type, String
            weiboFlag, String weiboNickname, String conRemarkPYFull, String conRemarkPYShort,
            byte[] lvbuff, String verifyFlag, String encryptUsername, String chatroomFlag, String deleteFlag, String contactLabelIds) {
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


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getConRemark() {
        return conRemark;
    }

    public void setConRemark(String conRemark) {
        this.conRemark = conRemark;
    }

    public String getDomainList() {
        return domainList;
    }

    public void setDomainList(String domainList) {
        this.domainList = domainList;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPyInitial() {
        return pyInitial;
    }

    public void setPyInitial(String pyInitial) {
        this.pyInitial = pyInitial;
    }

    public String getQuanPin() {
        return quanPin;
    }

    public void setQuanPin(String quanPin) {
        this.quanPin = quanPin;
    }

    public String getShowHead() {
        return showHead;
    }

    public void setShowHead(String showHead) {
        this.showHead = showHead;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWeiboFlag() {
        return weiboFlag;
    }

    public void setWeiboFlag(String weiboFlag) {
        this.weiboFlag = weiboFlag;
    }

    public String getWeiboNickname() {
        return weiboNickname;
    }

    public void setWeiboNickname(String weiboNickname) {
        this.weiboNickname = weiboNickname;
    }

    public String getConRemarkPYFull() {
        return conRemarkPYFull;
    }

    public void setConRemarkPYFull(String conRemarkPYFull) {
        this.conRemarkPYFull = conRemarkPYFull;
    }

    public String getConRemarkPYShort() {
        return conRemarkPYShort;
    }

    public void setConRemarkPYShort(String conRemarkPYShort) {
        this.conRemarkPYShort = conRemarkPYShort;
    }

    public byte[] getLvbuff() {
        return lvbuff;
    }

    public void setLvbuff(byte[] lvbuff) {
        this.lvbuff = lvbuff;
    }

    public String getVerifyFlag() {
        return verifyFlag;
    }

    public void setVerifyFlag(String verifyFlag) {
        this.verifyFlag = verifyFlag;
    }

    public String getEncryptUsername() {
        return encryptUsername;
    }

    public void setEncryptUsername(String encryptUsername) {
        this.encryptUsername = encryptUsername;
    }

    public String getChatroomFlag() {
        return chatroomFlag;
    }

    public void setChatroomFlag(String chatroomFlag) {
        this.chatroomFlag = chatroomFlag;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getContactLabelIds() {
        return contactLabelIds;
    }

    public void setContactLabelIds(String contactLabelIds) {
        this.contactLabelIds = contactLabelIds;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
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
