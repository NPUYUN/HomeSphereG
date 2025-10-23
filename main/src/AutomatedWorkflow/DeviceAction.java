package AutomatedWorkflow;

import DeviceEquipment.*;

/**
 * 设备操作类
 */
public class DeviceAction {
    private Device device;
    private String command;
    private String parameters;

    /**
     * 构造函数
     * @param command 命令
     * @param parameters 参数
     * @param device 设备
     */
    public DeviceAction(String command, String parameters, Device device) {
        this.command = command;
        this.parameters = parameters;
        this.device = device;
    }

    /**
     * 执行命令
     * 根据命令类型执行相应的设备操作
     * - powerOn: 开启设备
     * - powerOff: 关闭设备
     * - setTemperature: 设置空调目标温度（仅适用于空调设备）
     */
    public void execute() {
        try{
            // 检查设备是否存在
            if (device == null) {
                throw new IllegalArgumentException("设备不存在");
            }

            // 根据命令类型执行相应的操作
            if (command.equals("powerOn")) {
                device.powerOn();
            } else if (command.equals("powerOff")) {
                device.powerOff();
            } else if (command.equals("setTemperature") && parameters != null) {
                double temp = Double.parseDouble(parameters);
                // 检查设备是否为空调类型，如果是则设置目标温度
                if (device instanceof AirConditioner) {
                    ((AirConditioner) device).setTargetTemp(temp);
                    System.out.println("已设置空调目标温度为：" + temp + "℃");
                    return;
                }
                // 忽略无效的温度参数
                throw new IllegalArgumentException("无效的温度参数");
            }
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 获取设备
     * @return 设备
     */
    public Device getDevice() {
        return device;
    }

    /**
     * 获取命令
     * @return 命令
     */
    public String getCommand() {
        return command;
    }

    /**
     * 获取参数
     * @return 参数
     */
    public String getParameters() {
        return parameters;
    }
}
