package UserAndHousehold;

import DeviceEquipment.Device;
import NormalException.RepeatedException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Room 类表示一个房间，包含房间ID、名称和面积等信息，以及房间内的设备列表
 */
public class Room {
    private int roomId;
    private String name;
    private double area;
    // 使用 LinkedHashMap 存储设备，以设备ID为键，设备对象为值
    private Map<Integer, Device> devices = new LinkedHashMap<>();

    /**
     * 创建一个新的房间对象
     * @param roomId 房间ID
     * @param name 房间名称
     * @param area 房间面积
     */
    public Room(int roomId, String name, double area) {
        this.roomId = roomId;
        this.name = name;
        this.area = area;
    }

    /**
     * 添加一个设备到房间中
     * @param device 要添加的设备对象
     */
    public void addDevice(Device device){
        try{
            if (device == null) {
                throw new IllegalArgumentException("设备不能为空");
            }
            if (devices.containsKey(device.getDeviceId())) {
                throw new RepeatedException("设备已存在");
            }

            // 将设备添加到设备映射中，以设备ID为键
            devices.put(device.getDeviceId(), device);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * 从房间中删除一个设备
     * @param deviceId 要删除的设备ID
     */
    public void removeDevice(int deviceId){
        try{
            if (devices.containsKey(deviceId)) {
                System.out.println("设备已删除");
                devices.remove(deviceId);
                return;
            }
            throw new IllegalArgumentException("设备不存在");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    /**
     * 根据设备ID查找设备对象
     * @param deviceId 设备ID
     * @return 找到的设备对象，如果未找到则返回null
     */
    public Device findDeviceById(int deviceId){
        // 直接通过键查找设备
        return devices.get(deviceId);
    }


    /**
     * 获取房间内的所有设备列表
     * @return 设备列表
     */
    public List<Device> getDevices(){
        // 将Map中的值转换为List返回
        return new ArrayList<>(devices.values());
    }

    /**
     * 获取房间ID
     * @return 房间ID
     */
    public int getRoomId(){
        return roomId;
    }

    /**
     * 获取房间名称
     * @return 房间名称
     */
    public String getName(){
        return name;
    }

    /**
     * 获取房间面积
     * @return 房间面积
     */
    public double getArea(){
        return area;
    }

    /**
     * 获取房间的详细信息，包括房间ID、名称、面积和房间内的设备列表
     * @return 房间的详细信息
     */
    @Override
    public String toString(){
        return "Room{" + "roomId=" + roomId + ", name=" + name + ", area=" + area + "}";
    }

    /**
     * 判断两个房间对象是否相等(依据id)
     * @param obj 要比较的对象
     * @return 如果两个房间对象相等则返回true，否则返回false
     */
    @Override
    public boolean equals(Object obj){
        if(obj instanceof Room){
            Room room = (Room) obj;
            return room.roomId == roomId;
        }
        return false;
    }
}
