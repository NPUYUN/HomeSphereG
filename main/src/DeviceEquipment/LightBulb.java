package DeviceEquipment;

import EmissionReduction.EnergyReporting;

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
        // 检查时间参数有效性
        if (startTime == null || endTime == null || startTime.after(endTime)) {
            return 0;
        }

        // 计算耗电量：功率 × 时间差(毫秒转秒)
        // 通过功率乘以运行时间（秒）来计算能耗值
        return getPower() * (endTime.getTime() - startTime.getTime()) / 1000;
    }


    /**
     * 设置灯泡的亮度
     * @param brightness 亮度值
     */
    public void setBrightness(int brightness) {
        if (brightness < 0 || brightness > 100) {
            throw new IllegalArgumentException("亮度值必须在0-100之间");
        }
        this.brightness = brightness;
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
        if (colorTemp < 1000 || colorTemp > 10000) {
            throw new IllegalArgumentException("色温值必须在1000K-10000K之间");
        }
        this.colorTemp = colorTemp;
    }

    /**
     * 获取当前灯泡的颜色温度
     * @return 颜色温度值
     */
    public int getColorTemp() {
        return colorTemp;
    }
}
