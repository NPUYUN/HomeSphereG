package AutomatedWorkflow;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import DeviceEquipment.*;

/**
 * 设备状态触发器类
 */
public class DeviceStatusTrigger implements Trigger{
    private Device device;
    private String condition;
    private boolean isActive;

    /**
     * 获取触发器状态
     * @return 触发器状态
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * 评估设备状态触发条件
     * 该方法用于检查当前条件是否满足触发器激活的要求，主要通过解析条件字符串中的温度值，
     * 并与空调设备的目标温度进行比较来确定触发器状态
     */
    @Override
    public void evaluate(){
        // 定义温度匹配的正则表达式模式，匹配数字后跟℃符号的格式
        String basicPattern = "(\\d+)℃";
        Pattern pattern = Pattern.compile(basicPattern);
        Matcher matcher = pattern.matcher(condition);

        // 查找条件字符串中是否包含符合格式的温度值
        if(matcher.find()){
            // 提取匹配到的温度数值
            int targetTemp = Integer.parseInt(matcher.group(1));
            // 检查设备是否为空调类型，并获取其目标温度进行比较
            if(device instanceof AirConditioner ac){
                // 比较空调目标温度与条件中指定的温度是否一致
                if(ac.getTargetTemp() == targetTemp){
                    isActive = true;
                    System.out.println("设备状态触发器已激活");
                }
                else{
                    isActive = false;
                    System.out.println("设备状态触发器未激活");
                }
            }
        } else {
            // 如果没有找到匹配的温度条件，则默认不激活
            isActive = false;
        }
    }

    /**
     * 获取触发器关联的设备
     * @return 触发器关联的设备
     */
    public Device getDevice() {
        return device;
    }

    /**
     * 获取触发条件
     * @return 触发条件
     */
    public String getCondition() {
        return condition;
    }
}
