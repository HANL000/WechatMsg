package cn.truistic.enmicromsg.info;

/**
 * Created by Administrator on 2017/5/25.
 */

public class UserInfo {

    private String uin;
    private String imei;
    private int id;
    private String type;
    private String value;

    public UserInfo(String uin,String imei,int id, String type, String value) {
        this.uin = uin;
        this.imei = imei;
        this.id = id;
        this.type = type;
        this.value = value;
    }

    public String getUin() {
        return uin;
    }

    public void setUin(String uin) {
        this.uin = uin;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "uin='" + uin + '\'' +
                ", imei='" + imei + '\'' +
                ", id=" + id +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
