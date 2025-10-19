package EmissionReduction;

import java.util.Date;

/**
 * 能量报告接口
 */
public interface EnergyReporting {

    /**
     * 获取设备当前的功率
     * @return 设备的功率
     */
    public double getPower();

    /**
     * 获取指定时间段内的能量报告
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 设备在指定时间段内的能量报告
     */
    public double getReport(Date startTime , Date endTime);
}
