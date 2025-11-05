package AutomatedWorkflow.DeviceCommand;

import DeviceEquipment.Device;

/**
 * 设备命令接口，定义了设备操作命令的标准接口
 * 该接口遵循命令模式，提供了执行、撤销操作以及获取设备信息的功能
 */
public interface DeviceCommand {
    /**
     * 执行设备命令
     * 该方法用于执行具体的设备操作
     */
    void execute();

    /**
     * 撤销设备命令
     * 该方法用于撤销之前执行的设备操作，恢复到执行前的状态
     */
    void undo();

    /**
     * 获取命令关联的设备对象
     * @return Device 设备对象实例
     */
    Device getDevice();

    /**
     * 获取命令的描述信息
     * @return String 命令的描述字符串
     */
    String getDescription();
}

