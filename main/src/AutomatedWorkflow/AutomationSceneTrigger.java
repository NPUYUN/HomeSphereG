package AutomatedWorkflow;

import DeviceEquipment.Device;
import DeviceEquipment.DeviceObserver;

/**
 * 自动化场景触发器
 * 实现设备观察者接口，用于监听设备状态变化并触发相应的自动化场景
 */
public class AutomationSceneTrigger implements DeviceObserver {
    private AutomationScene scene;
    private int triggerDeviceId;
    private boolean triggerPowerState;
    
    /**
     * 构造函数
     * @param scene 要触发的自动化场景
     * @param triggerDeviceId 触发设备的ID
     * @param triggerPowerState 触发的电源状态
     */
    public AutomationSceneTrigger(AutomationScene scene, int triggerDeviceId, boolean triggerPowerState) {
        this.scene = scene;
        this.triggerDeviceId = triggerDeviceId;
        this.triggerPowerState = triggerPowerState;
    }
    
    /**
     * 当设备状态发生变化时被调用
     * @param device 状态发生变化的设备对象
     */
    @Override
    public void update(Device device) {
        // 检查是否是触发设备并且达到触发状态
        if (device.getDeviceId() == triggerDeviceId && device.isPowerStatus() == triggerPowerState) {
            // 触发自动化场景
            scene.execute();
            System.out.println("设备状态变化触发自动化场景: " + scene.getName());
        }
    }
}