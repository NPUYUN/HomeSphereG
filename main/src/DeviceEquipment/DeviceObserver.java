package DeviceEquipment;

/**
 * 设备观察者接口
 * 实现观察者模式中的观察者角色，用于接收设备状态变化通知
 */
public interface DeviceObserver {
    /**
     * 当设备状态发生变化时被调用
     * @param device 状态发生变化的设备对象
     */
    void update(Device device);
}