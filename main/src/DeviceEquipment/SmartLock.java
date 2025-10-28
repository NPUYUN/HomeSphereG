package DeviceEquipment;

import com.alibaba.fastjson2.JSON;

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

    /**
     * 将当前对象格式化为JSON字符串
     *
     * @return 返回表示当前对象的JSON字符串
     */
    @Override
    public String formatToJsonString() {
        return JSON.toJSONString(this);
    }

    /**
     * 从JSON字符串解析设备对象
     *
     * @param json 包含设备信息的JSON字符串
     * @return 返回解析后的Device对象实例
     */
    @Override
    public Device parseFromJsonString(String json) {
        return JSON.parseObject(json, SmartLock.class);
    }

    /**
     * 返回当前对象的字符串表示形式
     *
     * @return 返回SmartLock对象的字符串描述
     */
    @Override
    public String toString() {
        return "SmartLock{" + super.toString();
    }

}
