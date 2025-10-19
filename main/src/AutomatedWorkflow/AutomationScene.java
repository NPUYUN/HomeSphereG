package AutomatedWorkflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     */
    public void manualTrig(){
        for (DeviceAction action : actions){
            System.out.println(action.getCommand() + " " + action.getParameters());
        }
        System.out.println("手动触发场景完成！");
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

}
