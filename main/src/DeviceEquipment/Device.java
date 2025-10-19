package DeviceEquipment;

import EmissionReduction.RunningLog;

import java.util.List;
import java.util.Objects;

/**
 * 设备抽象类
 * 含有设备ID、名称、是否在线、是否运行、制造商等信息
 */
public abstract class Device {
    private int deviceId;
    private String name;
    private boolean isOnline;
    private boolean powerStatus;
    private Manufacturer manufacturer;
    private List<RunningLog> runningLogs;

    /**
     * 设备构造函数
     * @param deviceId 设备ID
     * @param name 设备名称
     * @param manufacturer 设备制造商
     */
    public Device(int deviceId, String name, Manufacturer manufacturer){
        this.deviceId = deviceId;
        this.name = name;
        this.manufacturer = manufacturer;
        this.isOnline = false;  // 默认设备不在线
        this.powerStatus = false;  // 默认设备关闭
    }

    /**
     * 设备上线
     * 该方法用于将设备设置为开启状态，并打印设备开启信息。
     */
    public void powerOn(){
        System.out.println("已开启" + name);// 输出设备开启提示信息
        powerStatus = true; // 设置电源状态为开启
    }

    /**
     * 设备下线
     * 将设备状态设置为关闭，并输出关闭信息
     */
    public void powerOff(){
        System.out.println("已关闭" + name); // 输出设备关闭提示信息
        powerStatus = false; // 设置电源状态为关闭
    }

    /**
     * 获取设备ID
     * @return 设备ID
     */
    public int getDeviceId(){
        return deviceId;
    }

    /**
     * 获取设备名称
     * @return 设备名称
     */
    public String getName(){
        return name;
    }

    /**
     * 获取设备是否在线
     * @return 设备是否在线
     */
    public boolean isOnline(){
        return isOnline;
    }

    /**
     * 获取设备状态
     * @return 设备状态
     */
    public boolean isPowerStatus(){
        return powerStatus;
    }

    /**
     * 获取设备制造商
     * @return 设备制造商
     */
    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    /**
     * 获取设备当前状态
     * @return 设备当前状态字符串
     */
    @Override
    public String toString(){
        return "Device{DeviceId=" + deviceId + ", Name=" + name + ", isOnline=" + isOnline + ", powerStatus=" + powerStatus + "}";
    }

    /**
     * 判断两个设备是否相等(依据id)
     * @param obj 设备对象
     * @return 是否相等
     */
    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Device device = (Device) obj;
        return deviceId == device.deviceId;
    }
}
