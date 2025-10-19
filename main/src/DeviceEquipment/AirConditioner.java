package DeviceEquipment;

import EmissionReduction.EnergyReporting;

import java.util.Date;

/**
 * AirConditioner 类，继承自 Device 类，同时实现 EnergyReporting 接口
 * 表示一个空调设备，提供获取功率和运行报告的功能
 */
public class AirConditioner extends Device implements EnergyReporting {
    private double currTemp;
    private double targetTemp;
    private static final double BASE_POWER = 100.0; // 基础功率
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
        // 根据设备电源状态确定功率值
        if (!isPowerStatus()) {
            return 0;
        }

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
        // 检查时间参数有效性
        if (startTime == null || endTime == null || startTime.after(endTime)) {
            return 0;
        }

        // 计算设备运行期间的能耗报告
        // 通过功率乘以运行时间（秒）来计算能耗值
        return getPower() * (endTime.getTime() - startTime.getTime()) / 1000;
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
}
