package AutomatedWorkflow;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;

/**
 *  TimeTrigger 类，继承自 Trigger 接口
 */
public class TimeTrigger implements Trigger{
    private String timeExpr;
    private String activeDays;
    private boolean isActive;

    /**
     *  构造函数
     * @param timeExpr 时间表达式
     * @param activeDays 活动时间段
     */
    public TimeTrigger(String timeExpr, String activeDays) {
        this.timeExpr = timeExpr;
        this.activeDays = activeDays;
    }

    /**
     *  获取触发器状态
     * @return 触发器状态
     */
    public boolean isActive(){
        return isActive;
    }

    /**
     * 评估时间触发器是否应该被激活
     * 该方法会解析activeDays和timeExpr属性，判断当前时间是否满足激活条件
     */
    @Override
    public void evaluate(){
        try{
            // 解析激活日期和时间范围
            String[] dates = activeDays.split("、");
            String[] times = timeExpr.split("~");
            if(dates.length < 4 || times.length != 2){
                throw new IllegalArgumentException("时间参数有误！");
            }

            LocalDate now = LocalDate.now();
            String datOfWeek = now.getDayOfWeek().getDisplayName(TextStyle.FULL , Locale.getDefault());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            boolean isActive = false;

            // 遍历所有激活日期和时间范围，判断当前时间是否满足条件
            for(String date : dates){
                if(datOfWeek.equals(date)){
                    LocalDate startTime = LocalDate.parse(times[0], formatter);
                    LocalDate endTime = LocalDate.parse(times[1], formatter);
                    if(now.isAfter(startTime) && now.isBefore(endTime)){
                        isActive = true;
                        break;
                    }
                }
            }

            // 根据评估结果设置触发器状态并输出日志
            if(isActive) {
                System.out.println("时间触发器已激活");
                this.isActive = true;
            }
            else {
                System.out.println("时间触发器未激活");
                this.isActive = false;
            }
        }
        catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }


    /**
     * 获取时间表达式
     * @return 时间表达式
     */
    public String getTimeExpr() {
        return timeExpr;
    }

    /**
     * 获取活动时间段
     * @return 活动时间段
     */
    public String getActiveDays() {
        return activeDays;
    }
}
