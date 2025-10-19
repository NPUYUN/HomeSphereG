package UserAndHousehold;

import AutomatedWorkflow.*;
import DeviceEquipment.Device;

import java.util.*;

/**
 * 家庭类，表示一个组织，包含多个房间和用户
 */
public class Household {
    private int householdId;
    private String address;
    // 使用 LinkedHashMap 存储房间，以房间ID为键，房间对象为值
    private Map<Integer, Room> rooms = new LinkedHashMap<>();
    // 使用 LinkedHashMap 存储成员资格，以成员资格ID为键，成员资格对象为值
    private Map<Integer, Membership> memberships = new LinkedHashMap<>();
    // 使用 LinkedHashMap 存储自动化场景，以场景ID为键，自动化场景对象为值
    private Map<Integer, AutomationScene> autoScenes = new LinkedHashMap<>();

    /**
     * 创建一个新的家庭对象
     * @param householdId 家庭ID
     * @param address 家庭地址
     */
    public Household(int householdId, String address) {
        if (householdId <= 0) {
            throw new IllegalArgumentException("家庭ID必须为正数");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("家庭地址不能为空");
        }
        this.householdId = householdId;
        this.address = address;
    }

    /**
     * 添加用户到当前组织中
     *
     * @param user 要添加的用户对象
     * @param role 用户在组织中的角色
     * @throws IllegalArgumentException 当参数无效时抛出异常
     */
    public void addUser(User user, String role) {
        if (user == null) {
            throw new IllegalArgumentException("用户对象不能为空");
        }
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("角色不能为空");
        }

        for(Membership membership : memberships.values()){
            if(membership.getUser().equals(user)){
                // 如果用户已存在，更新其角色
                membership.setRole(role);
                return;
            }
        }

        // 创建用户加入组织的会员关系
        Date joinDate = new Date();
        Membership membership = new Membership(joinDate, role, user, this);
        memberships.put(membership.getUser().getUserId(), membership);
        user.addMembership(membership);
    }


    /**
     * 从成员资格映射中移除指定用户
     * @param user 需要移除的用户对象
     */
    public void removeUser(User  user){
        if(user == null){
            System.out.println("用户对象不能为空");
            return;
        }

        boolean isExist = false;

        // 遍历所有成员资格，查找并移除与指定用户相关的成员资格
        Iterator<Membership> iterator = memberships.values().iterator();
        while (iterator.hasNext()) {
            Membership membership = iterator.next();
            if (membership.getUser().equals(user)) {
                iterator.remove();
                user.removeMembership(membership);
                isExist = true;
            }
        }

        // 如果用户不存在，则返回
        if(!isExist){
            System.out.println("用户不存在");
            return;
        }
        System.out.println("用户已移除");
    }

    /**
     * 获取当前组织中的所有成员资格
     * @return 成员资格列表
     */
    public List<Membership> getMemberships(){
        // 将Map中的值转换为List返回
        return new ArrayList<>(memberships.values());
    }

    /**
     * 向房间映射中添加一个房间
     * @param room 要添加的房间对象
     */
    public void addRoom(Room room){
        if(room == null){
            System.out.println("房间对象不能为空");
            return;
        }

        if(rooms.containsKey(room.getRoomId())){
            System.out.println("房间已存在");
            return;
        }

        rooms.put(room.getRoomId(), room);
    }

    /**
     * 根据房间ID移除房间
     * @param roomId 要移除的房间的ID
     */
    public void removeRoom(int roomId){
        boolean isExist = false;

        Iterator<Room> iterator = rooms.values().iterator();
        while (iterator.hasNext()) {
            Room room = iterator.next();
            if(room.getRoomId() == roomId){
                iterator.remove();
                isExist = true;
            }
        }

        if(!isExist){
            System.out.println("房间不存在");
            return;
        }
        System.out.println("房间已移除");
    }

    /**
     * 获取房间列表
     * @return 房间列表
     */
    public List<Room> getRooms(){
        // 将Map中的值转换为List返回
        return new ArrayList<>(rooms.values());
    }

    /**
     * 添加自动化场景到场景映射中
     *
     * @param autoScene 要添加的自动化场景对象
     */
    public void addAutoScene(AutomationScene autoScene){
        if(autoScene == null){
            System.out.println("自动化场景对象不能为空");
            return;
        }

        for(AutomationScene theAutoScene : autoScenes.values()){
            if(theAutoScene.equals(autoScene)){
                System.out.println("自动化场景已存在");
                return;
            }
        }

        autoScenes.put(autoScene.getSceneId(), autoScene);
    }

    /**
     * 根据自动场景ID移除指定的自动场景
     * @param autoSceneId 要移除的自动场景的ID
     */
    public void removeAutoScene(int autoSceneId){
        boolean isExist = false;

        Iterator<AutomationScene> iterator = autoScenes.values().iterator();
        while (iterator.hasNext()) {
            AutomationScene autoScene = iterator.next();
            if(autoScene.getSceneId() == autoSceneId){
                iterator.remove();
                isExist = true;
            }
        }
        if(!isExist){
            System.out.println("自动化场景不存在");
            return;
        }
        System.out.println("自动化场景已移除");
    }

    /**
     * 获取自动化场景列表
     * @return 自动化场景列表
     */
    public List<AutomationScene> getAutoScenes(){
        // 将Map中的值转换为List返回
        return new ArrayList<>(autoScenes.values());
    }

    /**
     * 根据自动化场景ID查找对应的自动化场景对象
     * @param autoSceneId 自动化场景的ID
     * @return 返回找到的自动化场景对象，如果未找到则返回null
     */
    public AutomationScene findAutoSceneById(int autoSceneId) {
        for (AutomationScene autoScene : autoScenes.values()) {
            if (autoScene.getSceneId() == autoSceneId) {
                return autoScene;
            }
        }
        return null;
    }

    /**
     * 获取所有设备列表
     *
     * @return 包含所有房间中设备的列表
     */
    public List<Device> listAllDevices(){
        List< Device> devices = new ArrayList<>();
        // 遍历所有房间，将房间中的设备添加到设备列表中
        for(Room room : rooms.values()){
            devices.addAll(room.getDevices());
        }
        return devices;
    }

    /**
     * 获取家庭ID
     * @return 家庭ID
     */
    public int getHouseholdId(){
        return householdId;
    }

    /**
     * 获取家庭地址
     * @return 家庭地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 重写toString方法，返回家庭对象的字符串表示
     * @return 家庭对象的字符串表示
     */
    @Override
    public String toString(){
        return "Household{HouseholdId=" + householdId + ", Address=" + address + "}";
    }

    /**
     * 重写equals方法，比较两个家庭对象是否相等(依据id)
     * @param obj 要比较的对象
     * @return 如果两个对象相等则返回true，否则返回false
     */
    @Override
    public boolean equals(Object obj){
        if(obj instanceof Household household){
            return household.householdId == householdId;
        }
        return false;
    }
}
