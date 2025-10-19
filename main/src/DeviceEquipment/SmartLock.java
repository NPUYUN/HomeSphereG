package DeviceEquipment;

/**
 * SmartLock 类继承自 Device 类
 * 描述智能锁设备
 */
public class SmartLock extends Device{
    private boolean isLocked;
    private int batteryLevel;

    /**
     * 构造函数
     * @param deviceId 设备 ID
     * @param name 设备名称
     * @param manufacturer 设备制造商
     */
    public SmartLock(int deviceId, String name, Manufacturer manufacturer) {
        super(deviceId, name, manufacturer);
        this.isLocked = false; // 默认未锁定
        this.batteryLevel = 0; // 默认电量0%
    }

    /**
     * 设置对象的锁定状态
     *
     * @param locked 锁定状态标识，true表示锁定，false表示解锁
     */
    public void setLocked(boolean locked) {
        this.isLocked = locked;
    }

    /**
     * 获取对象的锁定状态
     *
     * @return 锁定状态标识，true表示锁定，false表示解锁
     */
    public boolean isLocked() {
        return isLocked;
    }

    /**
     * 设置对象的电量等级
     *
     * @param batteryLevel 电量等级，范围0-100
     */
    public void setBatteryLevel(int batteryLevel) {
        if (batteryLevel >= 0 && batteryLevel <= 100) {
            this.batteryLevel = batteryLevel;
        }
    }

    /**
     * 获取对象的电量等级
     *
     * @return 电量等级，范围0-100
     */
    public int getBatteryLevel() {
        return batteryLevel;
    }
}
