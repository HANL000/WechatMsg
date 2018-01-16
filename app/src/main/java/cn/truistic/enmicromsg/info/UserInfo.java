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
