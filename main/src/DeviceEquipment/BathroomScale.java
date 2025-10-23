package DeviceEquipment;

import java.util.Date;

/**
 *  BathroomScale 类，继承自 Device 类
 *  表示体重秤设备
 */
public class BathroomScale extends Device {
    private double bodyMass;
    private int batteryLevel;

    /**
     *  构造函数，调用父类的构造函数初始化参数
     * @param deviceId 设备ID
     * @param name 设备名称
     * @param manufacturer 设备制造商
     */
    public BathroomScale(int deviceId, String name, Manufacturer manufacturer) {
        super(deviceId, name, manufacturer);
        this.bodyMass = 0.0;
        this.batteryLevel = 0; // 默认电量为0%
    }

    /**
     *  获取体重
     * @return 体重
     */
    public double getBodyMass() {
        return bodyMass;
    }

    /**
     *  设置体重
     * @param bodyMass 体重
     */
    public void setBodyMass(double bodyMass) {
        try{
            if (bodyMass < 0) {
                throw new IllegalArgumentException("体重不能为负数");
            }
            this.bodyMass = bodyMass;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     *  获取电量
     * @return 电量
     */
    public int getBatteryLevel() {
        return batteryLevel;
    }

    /**
     *  设置电量
     * @param batteryLevel 电量
     */
    public void setBatteryLevel(int batteryLevel) {
        try{
            if (batteryLevel < 0 || batteryLevel > 100) {
                throw new IllegalArgumentException("电量应在0-100之间");
            }
            this.batteryLevel = batteryLevel;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
