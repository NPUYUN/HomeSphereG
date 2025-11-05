package DeviceEquipment;

import NormalException.CannotFindException;
import NormalException.RepeatedException;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 *  Manufacturer 类，用于描述设备制造商信息
 */
public class Manufacturer implements DeviceFactory{
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
     * 创建设备实例的方法
     * 根据设备ID、名称和类型动态创建对应的设备对象，并将其添加到设备列表中
     *
     * @param deviceId 设备的唯一标识符
     * @param name 设备名称
     * @param type 设备类型，用于确定要创建的具体设备类
     * @return 创建成功的设备实例，如果创建失败则返回null
     */
    @Override
    public Device createDevice(int deviceId, String name, String type) {
        // 验证输入参数
        if (type == null || type.trim().isEmpty()) {
            System.out.println("设备类型不能为空");
            return null;
        }

        try {
            // 直接使用类名创建设备实例
            String className = "DeviceEquipment." + type.trim();
            Class<?> clazz = Class.forName(className);

            // 检查是否是Device的子类且不是抽象类
            if (Device.class.isAssignableFrom(clazz) && !clazz.equals(Device.class) &&
                !clazz.isInterface() && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
                @SuppressWarnings("unchecked")
                Class<? extends Device> deviceClass = (Class<? extends Device>) clazz;

                // 查找匹配的构造函数并创建设备实例
                Constructor<? extends Device> constructor = deviceClass.getConstructor(
                        int.class, String.class, Manufacturer.class);
                Device device = constructor.newInstance(deviceId, name, this);

                // 添加到设备列表
                addDevice(device);
                return device;
            } else {
                System.out.println("指定的类型不是有效的Device子类: " + type);
                return null;
            }
        } catch (ClassNotFoundException e) {
            System.out.println("未找到指定的设备类: " + type);
            return null;
        } catch (NoSuchMethodException e) {
            System.out.println("设备类缺少必要的构造函数: " + type);
            return null;
        } catch (Exception e) {
            System.out.println("创建设备时发生错误: " + e.getMessage());
            return null;
        }
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
