package cn.truistic.enmicromsg.info;

/**
 * Created by Administrator on 2017/5/25.
 */

public class UserInfo {

    private String mIMEI;
    private int id;
    private String type;
    private String value;

    public UserInfo(String IMEI,int id, String type, String value) {
        this.mIMEI = IMEI;
        this.id = id;
        this.type = type;
        this.value = value;
    }

    public String getIMEI() {
        return mIMEI;
    }

    public void setIMEI(String IMEI) {
        mIMEI = IMEI;
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
                "mIMEI='" + mIMEI + '\'' +
                ", id=" + id +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
