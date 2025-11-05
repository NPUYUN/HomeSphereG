package AutomatedWorkflow.DeviceCommand;

import DeviceEquipment.AirConditioner;
import DeviceEquipment.Device;

/**
 * 设置温度命令类
 * 实现了DeviceCommand接口，用于设置空调设备的目标温度
 * 支持执行和撤销操作
 */
public class SetTemperatureCommand implements DeviceCommand{
    private Device device;
    private double previousTemperature;
    private double targetTemperature;

    /**
     * 构造函数
     * @param device 设备对象，必须是AirConditioner类型
     * @param targetTemperature 目标温度值
     * @throws IllegalArgumentException 当设备不是空调类型时抛出异常
     */
    public SetTemperatureCommand(Device device, double targetTemperature) {
        if (!(device instanceof AirConditioner)) {
            throw new IllegalArgumentException("设备不是空调类型");
        }
        this.device = device;
        this.targetTemperature = targetTemperature;
        this.previousTemperature = ((AirConditioner)device).getTargetTemp();
    }

    /**
     * 执行设置温度命令
     * 将空调设备的目标温度设置为指定值
     */
    @Override
    public void execute() {
        previousTemperature = ((AirConditioner)device).getTargetTemp();
        ((AirConditioner)device).setTargetTemp(targetTemperature);
        System.out.println(((AirConditioner)device).getName() + " set temperature to " + targetTemperature);
    }

    /**
     * 撤销设置温度命令
     * 将空调设备的目标温度恢复为执行前的值
     */
    @Override
    public void undo() {
        ((AirConditioner) device).setTargetTemp(previousTemperature);
        System.out.println(((AirConditioner) device).getName() + " set temperature to " + previousTemperature);
    }

    /**
     * 获取命令关联的设备
     * @return Device 设备对象
     */
    @Override
    public Device getDevice() {
        return device;
    }

    /**
     * 获取命令描述信息
     * @return String 命令描述字符串
     */
    @Override
    public String getDescription() {
        return "setTemperature " + targetTemperature;
    }
}

