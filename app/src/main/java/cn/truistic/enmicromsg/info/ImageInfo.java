package cn.truistic.enmicromsg.info;

/**
 * Created by Administrator on 2017/8/25.
 */

public class ImageInfo {
    private String uin;
    private String imei;
    private String msgSvrId;
    private String bigImgPath;
    private String thumbImgPath;
    private int createtime;

    public ImageInfo(String uin, String imei, String msgSvrId, String bigImgPath, String
            thumbImgPath, int createtime) {
        this.uin = uin;
        this.imei = imei;
        this.msgSvrId = msgSvrId;
        this.bigImgPath = bigImgPath;
        this.thumbImgPath = thumbImgPath;
        this.createtime = createtime;
    }
    @Override
    public String toString() {
        return "ImageInfo{" +
                "uin='" + uin + '\'' +
                ", imei='" + imei + '\'' +
                ", msgSvrId='" + msgSvrId + '\'' +
                ", bigImgPath='" + bigImgPath + '\'' +
                ", thumbImgPath='" + thumbImgPath + '\'' +
                ", createtime=" + createtime +
                '}';
    }
}
