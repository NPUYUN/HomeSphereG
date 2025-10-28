package AutomatedWorkflow;

import EmissionReduction.RunningLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *  场景类，用于描述自动化场景
 */
public class AutomationScene {
    private int sceneId;  // 修改字段名为更规范的 sceneId
    private String name;
    private String description;
    private List<Trigger> triggers = new ArrayList<>();
    private List<DeviceAction> actions = new ArrayList<>();

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
     * @param action 要添加的设备动作对象
     */
    public void addAction(DeviceAction action){
        actions.add(action);
    }

    /**
     * 从设备动作列表中删除一个设备动作
     * @param action 要删除的设备动作对象
     */
    public void removeAction(DeviceAction action){
        actions.remove(action);
    }

    /**
     * 获取设备动作列表
     * @return 设备动作列表
     */
    public List<DeviceAction> getActions(){
        return actions;
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
        for(DeviceAction action : actions){
            action.execute();
            Date date = new Date();
            RunningLog runningLog = null;

            // 根据命令类型创建不同的运行日志
            if(action.getCommand().equals("setTemperature")){
                runningLog = new RunningLog(date, action.getCommand() + " " + action.getParameters(), RunningLog.Type.INFO, "设备动作执行成功");
            }
            else{
                runningLog = new RunningLog(date, action.getCommand(), RunningLog.Type.INFO, "设备动作执行成功");
            }
            action.getDevice().addRunningLog(runningLog);
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
            if(trigger instanceof TimeTrigger timeTrigger){
                if(timeTrigger.isActive()) {
                    isTriggered = true;
                    break;
                }
            }
            else if(trigger instanceof DeviceStatusTrigger deviceStatusTrigger){
                if(deviceStatusTrigger.isActive()) {
                    isTriggered = true;
                    break;
                }
            }
        }

        // 如果有触发器被激活，执行所有设备动作
        if(isTriggered){
            for (DeviceAction action : actions){
                System.out.println(action.getCommand() + " " + action.getParameters());
            }
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


}
