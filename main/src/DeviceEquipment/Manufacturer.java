package DeviceEquipment;

import NormalException.CannotFindException;
import NormalException.RepeatedException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *  Manufacturer 类，用于描述设备制造商信息
 */
public class Manufacturer {
    private int manufacturerId;
    private String name;
    private String protocols;
    private Map<Integer, Device> devices = new LinkedHashMap<>();

    /**
     *  构造函数，初始化参数
     * @param manufacturerId 制造商ID
     * @param name 制造商名称
     * @param protocols 制造商协议
     */
    public Manufacturer(int manufacturerId, String name, String protocols) {
        this.manufacturerId = manufacturerId;
        this.name = name;
        this.protocols = protocols;
    }

    /**
     *  获取制造商设备列表
     * @return 设备列表
     */
    public List<Device> getDevices() {
        return new ArrayList<>(devices.values());
    }

    /**
     *  添加设备
     * @param device 设备
     */
    public void addDevice(Device device) {
        try{
            if (device == null) {
                throw new IllegalArgumentException("设备不能为空");
            }

            if (devices.containsKey(device.getDeviceId())) {
                throw new RepeatedException("设备已存在");
            }

            devices.put(device.getDeviceId(), device);
        }
        catch (RepeatedException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     *  删除设备
     * @param device 设备
     */
    public void removeDevice(Device device) {
        try{
            for(Device theDevice : devices.values()){
                if(theDevice.equals(device)){
                    devices.remove(theDevice.getDeviceId());
                    System.out.println("删除成功");
                    return;
                }
            }
            throw new CannotFindException("未找到该设备");
        }
        catch (CannotFindException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     *  获取制造商ID
     * @return 制造商ID
     */
    public int getManufacturerId() {
        return manufacturerId;
    }

    /**
     *  获取制造商名称
     * @return 制造商名称
     */
    public String getName() {
        return name;
    }

    /**
     *  获取制造商协议
     * @return 制造商协议
     */
    public String getProtocols() {
        return protocols;
    }

    /**
     * 返回制造商对象的字符串表示形式
     *
     * @return 包含制造商ID、名称和协议信息的格式化字符串
     */
    @Override
    public String toString() {
        return "manufacturer{" +
                "manufacturerId=" + manufacturerId +
                ", name='" + name + '\'' +
                ", protocols='" + protocols + '\'' +
                '}';
    }

}
