package AutomatedWorkflow.DeviceCommand;

import DeviceEquipment.Device;

/**
 * PowerOnCommand类实现了DeviceCommand接口，用于执行设备开机命令
 * 该命令支持执行和撤销操作，可以将设备状态恢复到执行前的状态
 */
public class PowerOnCommand implements DeviceCommand{
    private Device device;
    private boolean previousStatus;

    /**
     * 构造函数，创建一个开机命令实例
     * @param device 要执行开机操作的设备对象
     */
    public PowerOnCommand(Device device) {
        this.device = device;
        this.previousStatus = device.isPowerStatus();
    }

    /**
     * 执行开机命令，将设备开机并打印相关信息
     */
    @Override
    public void execute() {
        previousStatus = device.isPowerStatus();
        device.powerOn();
        System.out.println(device.getName() + " powered on");
    }

    /**
     * 撤销开机命令，如果执行前设备是关机状态，则将其关闭
     */
    @Override
    public void undo() {
        // 如果执行命令前设备是关机状态，则执行撤销操作将其关机
        if(!previousStatus) {
            device.powerOff();
            System.out.println(device.getName() + " powered off");
        }
    }

    /**
     * 获取当前命令关联的设备对象
     * @return 设备对象
     */
    @Override
    public Device getDevice() {
        return device;
    }

    /**
     * 获取命令描述信息
     * @return 命令描述字符串"powerOn"
     */
    @Override
    public String getDescription() {
        return "powerOn";
    }
}
