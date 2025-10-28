package DeviceEquipment;

import EmissionReduction.EnergyReporting;
import EmissionReduction.RunningLog;
import com.alibaba.fastjson2.JSON;

import java.util.Date;

/**
 * LightBulb 类继承自 Device 类，并实现了 EnergyReporting 接口
 * 表示智能灯泡设备，并包含亮度和颜色温度属性
 */
public class LightBulb extends Device implements EnergyReporting {
    private int brightness;
    private int colorTemp;
    private static final double BASE_POWER = 10.0;

    /**
     * 构造函数，创建一个智能灯泡设备对象
     * @param deviceId 设备ID
     * @param name 设备名称
     * @param manufacturer 设备制造商
     */
    public LightBulb(int deviceId, String name, Manufacturer manufacturer) {
        super(deviceId , name , manufacturer);
    }


    /**
     * 获取当前灯泡的耗电量
     * @return 灯泡的耗电量，开启状态下返回50，关闭状态下返回0
     */
    @Override
    public double getPower() {
        // 根据设备电源状态确定功率值
        if (!isPowerStatus()) {
            return 0;
        }

        return BASE_POWER;
    }

    /**
     * 获取指定时间段内灯泡的耗电量报告
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 灯泡的耗电量报告
     */
    @Override
    public double getReport(Date startTime, Date endTime) {
        try{
            // 检查时间参数有效性
            if (startTime == null || endTime == null || startTime.after(endTime)) {
                throw new IllegalArgumentException("时间参数无效");
            }

            // 计算设备运行期间的能耗报告
            // 通过功率乘以运行时间（秒）来计算能耗值
            Date start = startTime;
            Date end = null;
            double report = 0;
            for(RunningLog log : getRunningLogs()){
                if(log.getEvent().equals("powerOn"))
                    start = log.getDateTime();
                if(log.getEvent().equals("powerOff")){
                    end = log.getDateTime();
                }
                // 计算每次开关机期间的耗电量
                if( end!= null && end.after(start)){
                    if(end.getTime() < endTime.getTime()){
                        // 设备在查询结束时间前关机，计算完整运行时间的耗电量
                        report += getPower() * (end.getTime() - start.getTime()) / 1000 / 3600.0;
                    }
                    else{
                        // 设备运行超过查询结束时间，只计算到查询结束时间的耗电量
                        report += getPower() * (endTime.getTime() - start.getTime()) / 1000 / 3600.0;
                        break;
                    }

                }
            }
            // 处理最后一次开机但未关机的情况
            if(start != startTime && end == null && start.before(endTime)){
                report += getPower() * (endTime.getTime() - start.getTime()) / 1000 / 3600.0;
            }

            return report;
        }
        catch (IllegalArgumentException e) {
            System.out.println("时间参数无效");
            return 0;
        }
    }

    /**
     * 设置灯泡的亮度
     * @param brightness 亮度值
     */
    public void setBrightness(int brightness) {
        try{
            if (brightness < 0 || brightness > 100) {
                throw new IllegalArgumentException("亮度值必须在0-100之间");
            }
            this.brightness = brightness;
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 获取当前灯泡的亮度
     * @return 亮度值
     */
    public int getBrightness() {
        return brightness;
    }

    /**
     * 设置灯泡的颜色温度
     * @param colorTemp 颜色温度值
     */
    public void setColorTemp(int colorTemp) {
        try{
            if (colorTemp < 1000 || colorTemp > 10000) {
                throw new IllegalArgumentException("色温值必须在1000K-10000K之间");
            }
            this.colorTemp = colorTemp;
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 获取当前灯泡的颜色温度
     * @return 颜色温度值
     */
    public int getColorTemp() {
        return colorTemp;
    }

    /**
     * 将当前对象格式化为JSON字符串
     *
     * @return 返回当前对象的JSON格式字符串表示
     */
    @Override
    public String formatToJsonString() {
        // 使用JSON工具类将当前对象序列化为JSON字符串
        return JSON.toJSONString(this);
    }

    /**
     * 从JSON字符串解析设备对象
     *
     * @param json 包含设备信息的JSON字符串
     * @return 解析后的LightBulb设备对象
     */
    @Override
    public Device parseFromJsonString(String json) {
        return JSON.parseObject(json, LightBulb.class);
    }

    /**
     * 重写toString方法，返回灯泡对象的字符串表示
     *
     * @return 包含类名和父类toString结果的字符串
     */
    @Override
    public String toString() {
        // 构造并返回包含父类信息的字符串表示
        return "LightBulb{" + super.toString();
    }

}
