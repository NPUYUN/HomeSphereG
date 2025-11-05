package AutomatedWorkflow.DeviceCommand;

import DeviceEquipment.Device;

/**
 * PowerOffCommand类实现了DeviceCommand接口，用于执行设备关机命令
 * 该命令支持执行关机操作和撤销操作（恢复到关机前的状态）
 */
public class PowerOffCommand implements DeviceCommand{
    private Device device;
    private boolean previousStatus;

    /**
     * 构造函数，创建一个关机命令实例
     * @param device 需要执行关机操作的设备对象
     */
    public PowerOffCommand(Device device) {
        this.device = device;
        this.previousStatus = device.isPowerStatus();
    }

    /**
     * 执行关机命令
     * 保存设备当前状态并执行关机操作
     */
    @Override
    public void execute() {
        previousStatus = device.isPowerStatus();
        device.powerOff();
        System.out.println(device.getName() + " powered off");
    }

    /**
     * 撤销关机命令
     * 根据关机前的状态决定是否需要重新开机
     */
    @Override
    public void undo() {
        // 如果关机前设备是开机状态，则执行开机操作进行恢复
        if(previousStatus) {
            device.powerOn();
            System.out.println(device.getName() + " powered on");
        }
    }

    /**
     * 获取命令关联的设备对象
     * @return 设备对象
     */
    @Override
    public Device getDevice() {
        return device;
    }

    /**
     * 获取命令描述信息
     * @return 命令描述字符串"powerOff"
     */
    @Override
    public String getDescription() {
        return "powerOff";
    }
}

