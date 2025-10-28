package DeviceEquipment;

import EmissionReduction.EnergyReporting;
import EmissionReduction.RunningLog;
import com.alibaba.fastjson2.JSON;

import java.util.Date;

/**
 * AirConditioner 类，继承自 Device 类，同时实现 EnergyReporting 接口
 * 表示一个空调设备，提供获取功率和运行报告的功能
 */
public class AirConditioner extends Device implements EnergyReporting {
    private double currTemp;
    private double targetTemp;
    private static final double BASE_POWER = 100.0;

    // 基础功率
    private static final double POWER_PER_DEGREE = 20.0; // 每度温差的功率

    /**
     * 构造函数
     * @param deviceId 设备 ID
     * @param name 设备名称
     * @param manufacturer 设备制造商
     */
    public AirConditioner(int deviceId, String name, Manufacturer manufacturer) {
        super(deviceId, name, manufacturer);
        this.currTemp = 25.0; // 默认当前温度25度
        this.targetTemp = 25.0; // 默认目标温度25度
    }

    /**
     * 获取当前设备功率
     * @return 设备功率，单位为瓦特
     */
    @Override
    public double getPower() {
        // 根据温差计算实际功率
        double tempDiff = Math.abs(currTemp - targetTemp);
        return BASE_POWER + (tempDiff * POWER_PER_DEGREE);
    }

    /**
     * 获取设备运行报告
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 设备运行报告值，基于功率与运行时间的乘积计算得出
     */
    @Override
    public double getReport(Date startTime, Date endTime) {
        try {
            // 检查时间参数有效性
            if (startTime == null || endTime == null || startTime.after(endTime)) {
                throw new IllegalArgumentException("时间范围错误");
            }

            double totalEnergy = 0.0;
            Date lastPowerOnTime = null;

            // 遍历运行日志，计算能耗
            for (RunningLog log : getRunningLogs()) {
                Date logTime = log.getDateTime();

                // 只处理时间范围内的日志
                if (logTime.before(startTime) || logTime.after(endTime)) {
                    continue;
                }

                if ("powerOn".equals(log.getEvent())) {
                    lastPowerOnTime = logTime;
                } else if ("powerOff".equals(log.getEvent()) && lastPowerOnTime != null) {
                    // 计算这一段运行时间的能耗
                    long durationMillis = logTime.getTime() - lastPowerOnTime.getTime();
                    totalEnergy += getPower() * durationMillis / 1000.0 / 3600.0;
                    lastPowerOnTime = null; // 重置
                }
            }

            // 处理设备在结束时间仍处于开启状态的情况
            if (lastPowerOnTime != null && isPowerStatus()) {
                long durationMillis = endTime.getTime() - lastPowerOnTime.getTime();
                totalEnergy += getPower() * durationMillis / 1000.0 / 3600.0;
            }

            return totalEnergy;
        } catch (IllegalArgumentException e) {
            System.out.println("时间参数错误：" + e.getMessage());
            return 0;
        }
    }

    /**
     * 设置当前温度
     * @param currTemp 当前温度
     */
    public void setCurrTemp(double currTemp) {
        this.currTemp = currTemp;
    }

    /**
     * 获取当前温度
     * @return 当前温度
     */
    public double getCurrTemp() {
        return currTemp;
    }

    /**
     * 设置目标温度
     * @param targetTemp 目标温度
     */
    public void setTargetTemp(double targetTemp) {
        this.targetTemp = targetTemp;
    }

    /**
     * 获取目标温度
     * @return 目标温度
     */
    public double getTargetTemp() {
        return targetTemp;
    }

    /**
     * 将当前对象格式化为JSON字符串
     *
     * @return 返回当前对象的JSON格式字符串表示
     */
    @Override
    public String formatToJsonString() {
        // 使用fastjson将当前对象序列化为JSON字符串
        return JSON.toJSONString(this);
    }


    @Override
    public Device parseFromJsonString(String json) {
        return JSON.parseObject(json, AirConditioner.class);
    }

    /**
     * 返回空调对象的字符串表示形式
     *
     * @return 包含空调信息的字符串，格式为"AirConditioner{[父类信息]}"
     */
    @Override
    public String toString() {
        return "AirConditioner{" + super.toString();
    }

}
