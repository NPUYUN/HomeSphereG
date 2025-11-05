package AutomatedWorkflow;

import AutomatedWorkflow.DeviceCommand.DeviceCommand;
import DeviceEquipment.Device;
import DeviceEquipment.DeviceObserver;
import EmissionReduction.RunningLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *  场景类，用于描述自动化场景
 */
public class AutomationScene implements DeviceObserver {
    private int sceneId;  // 修改字段名为更规范的 sceneId
    private String name;
    private String description;
    private List<Trigger> triggers = new ArrayList<>();
    private List<DeviceCommand> commands = new ArrayList<>();
    private List<DeviceObserver> deviceTriggers = new ArrayList<>(); // 存储设备状态变化触发器

    /**
     *  构造函数，初始化场景ID、名称和描述
     * @param sceneId 场景ID
     * @param name 场景名称
     * @param description 场景描述
     */
    public AutomationScene(int sceneId, String name, String description) {
        this.sceneId = sceneId;
        this.name = name;
        this.description = description;
    }

    /**
     * 向设备动作列表中添加一个新的设备动作
     * @param command 要添加的设备动作对象
     */
    public void addCommand(DeviceCommand command){
        commands.add(command);
    }

    /**
     * 从设备动作列表中删除一个设备动作
     * @param command 要删除的设备动作对象
     */
    public void removeAction(DeviceCommand command){
        commands.remove(command);
    }

    /**
     * 获取设备动作列表
     * @return 设备动作列表
     */
    public List<DeviceCommand> getCommands(){
        return commands;
    }

    /**
     * 向触发器列表中添加一个新的触发器
     * @param trigger 要添加的触发器对象
     */
    public void addTrigger(Trigger trigger){
        triggers.add(trigger);
    }

    /**
     * 从触发器列表中删除一个触发器
     * @param trigger 要删除的触发器对象
     */
    public void removeTrigger(Trigger trigger){
        triggers.remove(trigger);
    }

    /**
     * 获取触发器列表
     * @return 触发器列表
     */
    public List<Trigger> getTriggers(){
        return triggers;
    }

        /**
     * 手动触发场景，执行所有设备动作
     * 该方法会遍历场景中的所有设备动作并依次执行，同时记录执行日志
     */
    public void manualTrig(){
        System.out.println("Manually triggering scene：" + name);

        // 遍历并执行所有设备动作
        for(DeviceCommand command : commands){
            command.execute();
            Date date = new Date();
            RunningLog runningLog = new RunningLog(date, command.getDescription(), RunningLog.Type.INFO, "");
            command.getDevice().addRunningLog(runningLog);
        }
        System.out.println("Scene with " + "ID " + getSceneId() + " trigged!");
    }


    /**
     * 获取场景ID
     * @return 场景ID
     */
    public int getSceneId() {  // 更新 getter 方法名以匹配字段名
        return sceneId;
    }

    /**
     * 获取场景名称
     * @return 场景名称
     */
    public String getName() {
        return name;
    }


    /**
     * 添加基于设备状态变化的触发器
     * @param device 要观察的设备
     * @param triggerPowerState 触发的电源状态
     */
    public void addDeviceTrigger(Device device, boolean triggerPowerState) {
        // 创建设备状态触发器
        AutomationSceneTrigger trigger = new AutomationSceneTrigger(this, device.getDeviceId(), triggerPowerState);
        deviceTriggers.add(trigger);
        // 将触发器注册为设备的观察者
        device.addObserver(trigger);
    }
    
    /**
     * 移除设备触发器
     * @param device 要移除观察的设备
     */
    public void removeDeviceTriggers(Device device) {
        for (DeviceObserver trigger : deviceTriggers) {
            device.removeObserver(trigger);
        }
        deviceTriggers.clear();
    }
    
    /**
     * 执行触发器检查和相应的设备动作
     *
     * 该方法遍历所有注册的触发器，检查是否有触发器被激活。
     * 如果有任何触发器被激活，则执行所有注册的设备动作。
     * 支持的触发器类型包括时间触发器和设备状态触发器。
     */
    public void execute(){
        // 检查触发器是否被激活的标志位
        boolean isTriggered = false;

        // 遍历所有触发器，检查是否有触发器满足激活条件
        for(Trigger trigger : triggers){
            trigger.evaluate();
            if(trigger instanceof TimeTrigger){
                TimeTrigger timeTrigger = (TimeTrigger) trigger;
                if(timeTrigger.isActive()) {
                    isTriggered = true;
                    break;
                }
            }
            else if(trigger instanceof DeviceStatusTrigger){
                DeviceStatusTrigger deviceStatusTrigger = (DeviceStatusTrigger) trigger;
                if(deviceStatusTrigger.isActive()) {
                    isTriggered = true;
                    break;
                }
            }
        }

        // 如果有触发器被激活，执行所有设备动作
        if(isTriggered){
            for (DeviceCommand command : commands){
                command.execute();
                System.out.println(command.getDescription());
                // 记录执行日志
                Date date = new Date();
                RunningLog runningLog = new RunningLog(date, command.getDescription(), RunningLog.Type.INFO, "");
                command.getDevice().addRunningLog(runningLog);
            }
        }
    }

    /**
     * 撤销最后执行的命令
     * 该方法会检查命令列表是否为空，如果不为空则获取最后一个命令并执行撤销操作
     */
    public void undoLastCommand() {
        // 检查命令列表是否为空
        if (!commands.isEmpty()) {
            // 获取最后一个命令
            DeviceCommand lastCommand = commands.get(commands.size() - 1);
            // 执行撤销操作
            lastCommand.undo();
            System.out.println("Last command undone");
        }
    }



    /**
     * 获取场景描述
     * @return 场景描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 返回场景对象的字符串表示形式
     *
     * @return 包含场景ID、名称和描述信息的格式化字符串
     */
    @Override
    public String toString() {
        // 将场景的基本信息格式化为易读的字符串形式
        return "ID" + sceneId + "\n" +
                "名称：" + name + "\n" +
                "描述：" + description;
    }
    
    /**
     * 实现DeviceObserver接口的update方法
     * 当观察的设备状态变化时，该方法会被调用
     */
    @Override
    public void update(Device device) {
        // 设备状态变化时自动执行场景
        execute();
    }


}
