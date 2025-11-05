package DeviceEquipment;

import EmissionReduction.RunningLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备抽象类
 * 含有设备ID、名称、是否在线、是否运行、制造商等信息
 */
public abstract class Device {
    protected int deviceId;
    protected String name;
    protected boolean isOnline;
    protected boolean powerStatus;
    protected Manufacturer manufacturer;
    protected List<RunningLog> runningLogs = new ArrayList<>();
    // 观察者列表，用于存储所有观察该设备的观察者
    protected List<DeviceObserver> observers = new ArrayList<>();


    /**
     * 设备构造函数
     * @param deviceId 设备ID
     * @param name 设备名称
     * @param manufacturer 设备制造商
     */
    protected Device(int deviceId, String name, Manufacturer manufacturer){
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
        boolean oldStatus = powerStatus;
        powerStatus = true; // 设置电源状态为开启
        // 如果状态发生变化，通知所有观察者
        if (!oldStatus) {
            notifyObservers();
        }
    }

    /**
     * 设备下线
     * 将设备状态设置为关闭，并输出关闭信息
     */
    public void powerOff(){
        boolean oldStatus = powerStatus;
        powerStatus = false; // 设置电源状态为关闭
        // 如果状态发生变化，通知所有观察者
        if (oldStatus) {
            notifyObservers();
        }
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
     * 获取运行日志列表
     *
     * @return 运行日志列表，包含所有的运行日志信息
     */
    public List<RunningLog> getRunningLogs() {
        return runningLogs;
    }


        /**
     * 添加运行日志到日志列表中
     *
     * @param runningLog 要添加的运行日志对象
     */
    public void addRunningLog(RunningLog runningLog) {
        runningLogs.add(runningLog);
    }
    
    /**
     * 添加设备观察者
     * @param observer 要添加的观察者对象
     */
    public void addObserver(DeviceObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    /**
     * 移除设备观察者
     * @param observer 要移除的观察者对象
     */
    public void removeObserver(DeviceObserver observer) {
        observers.remove(observer);
    }
    
    /**
     * 通知所有观察者设备状态发生变化
     */
    protected void notifyObservers() {
        for (DeviceObserver observer : observers) {
            observer.update(this);
        }
    }

    /**
     * 将对象格式化为JSON字符串
     * @return 返回表示当前对象的JSON格式字符串
     */
    public abstract String formatToJsonString();

    /**
     * 从JSON字符串解析创建设备对象
     * @param jsonString 包含设备信息的JSON格式字符串，不能为空
     * @return 解析成功后返回对应的Device对象
     */
    public abstract Device parseFromJsonString(String jsonString);


    /**
     * 获取设备当前状态
     * @return 设备当前状态字符串
     */
    @Override
    public String toString(){
        return "{deviceId='" + deviceId + ", name='" + name + ", manufacturer=" + manufacturer + "}";
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
