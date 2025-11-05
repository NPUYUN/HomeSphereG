package AutomatedWorkflow;

import AutomatedWorkflow.DeviceCommand.DeviceCommand;
import AutomatedWorkflow.DeviceCommand.PowerOffCommand;
import AutomatedWorkflow.DeviceCommand.PowerOnCommand;
import AutomatedWorkflow.DeviceCommand.SetTemperatureCommand;
import DeviceEquipment.Device;

/**
 * 设备操作类
 */
public class DeviceAction {
    /**
     * 创建设备命令对象
     *
     * @param commandType 命令类型字符串，支持"poweron"、"poweroff"、"settemperature"
     * @param device 要操作的设备对象
     * @param parameters 命令参数，对于温度设置命令为必需参数
     * @return 返回对应的设备命令对象
     * @throws IllegalArgumentException 当命令类型或设备为空，或参数不符合要求时抛出异常
     */
    public static DeviceCommand createCommand(String commandType, Device device, String parameters) {
        if (commandType == null || device == null) {
            throw new IllegalArgumentException("命令类型和设备不能为空");
        }

        // 根据命令类型创建相应的命令对象
        switch (commandType.toLowerCase()) {
            case "poweron":
                return new PowerOnCommand(device);
            case "poweroff":
                return new PowerOffCommand(device);
            case "settemperature":
                if (parameters == null || parameters.trim().isEmpty()) {
                    throw new IllegalArgumentException("设置温度命令需要参数");
                }
                try {
                    double temp = Double.parseDouble(parameters);
                    return new SetTemperatureCommand(device, temp);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("无效的温度参数");
                }
            default:
                throw new IllegalArgumentException("不支持的命令类型: " + commandType);
        }
    }
}

