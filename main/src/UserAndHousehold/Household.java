package UserAndHousehold;

import AutomatedWorkflow.AutomationScene;
import Common.HomeSphereSystem;
import DeviceEquipment.Device;
import NormalException.CannotDoException;
import NormalException.CannotFindException;
import NormalException.NotAdminException;
import NormalException.RepeatedException;

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
        try{
            if (householdId <= 0) {
                throw new IllegalArgumentException("家庭ID必须为正数");
            }
            if (address == null || address.trim().isEmpty()) {
                throw new IllegalArgumentException("家庭地址不能为空");
            }
            this.householdId = householdId;
            this.address = address;
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 添加用户到当前组织中
     *
     * @param user 要添加的用户对象
     * @param role 用户在组织中的角色
     * @throws IllegalArgumentException 当参数无效时抛出异常
     */
    public void addUser(User user, String role) {
        try{
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
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 检查指定用户是否为管理员用户
     * @param userId 用户ID，用于标识需要检查的用户
     * @throws NotAdminException 当用户存在但不是管理员时抛出此异常
     * @throws CannotFindException 当用户不存在时抛出此异常
     */
    public void isAdminUser(int userId) throws NotAdminException, CannotFindException {
        // 默认超管id为0，直接返回表示具有管理员权限
        if(userId == 0) return;

        // 遍历所有成员关系，查找匹配的用户
        for(Membership membership : memberships.values()){
            if(membership.getUser().getUserId() == userId){
                // 检查用户角色是否为管理员角色
                if(membership.getRole().equals(HomeSphereSystem.MANAGE_USER_ROLE)){
                    return;
                }
                else{
                    throw new NotAdminException("非管理员用户");
                }
            }
        }
        throw new CannotFindException("用户不存在");
    }

    /**
     * 从成员资格映射中移除指定用户
     * @param user 需要移除的用户对象
     */
    public void removeUser(User  user){
        try{
            if(user == null){
                throw new IllegalArgumentException("用户对象不能为空");
            }

            // 遍历所有成员资格，查找并移除与指定用户相关的成员资格
            Iterator<Membership> iterator = memberships.values().iterator();
            while (iterator.hasNext()) {
                Membership membership = iterator.next();
                if (membership.getUser().equals(user)) {
                    iterator.remove();
                    user.removeMembership(membership);
                    System.out.println("用户已移除");
                    return;
                }
            }

            // 如果用户不存在，则返回
            throw new CannotDoException("用户不存在");

        }

        catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
        try{
            if(room == null){
                throw new IllegalArgumentException("房间对象不能为空");
            }

            if(rooms.containsKey(room.getRoomId())){
                throw new RepeatedException("房间已存在");
            }

            rooms.put(room.getRoomId(), room);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

        /**
     * 根据房间ID移除房间
     * @param roomId 要移除的房间的ID
     */
    public void removeRoom(int roomId){
        try{
            // 遍历所有房间，查找并移除指定ID的房间
            Iterator<Room> iterator = rooms.values().iterator();
            while (iterator.hasNext()) {
                Room room = iterator.next();
                if(room.getRoomId() == roomId){
                    iterator.remove();
                    System.out.println("房间已移除");
                    return;
                }
            }
            throw new CannotDoException("房间不存在");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

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
     * 根据房间ID查找房间
     * @param roomId 房间的ID
     * @return 找到的房间对象，如果没有找到，则返回null
     */
    public Room findRoomById(int roomId){
        return rooms.get(roomId);
    }

    /**
     * 添加自动化场景到场景映射中
     *
     * @param autoScene 要添加的自动化场景对象
     */
    public void addAutoScene(AutomationScene autoScene){
        try{
            // 验证输入参数是否为空
            if(autoScene == null){
                throw new IllegalArgumentException("自动化场景对象不能为空");
            }

            // 检查是否存在重复的自动化场景
            for(AutomationScene theAutoScene : autoScenes.values()){
                if(theAutoScene.equals(autoScene)){
                    throw new RepeatedException("自动化场景已存在");
                }
            }

            // 将自动化场景添加到映射中
            autoScenes.put(autoScene.getSceneId(), autoScene);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 根据自动场景ID移除指定的自动场景
     * @param autoSceneId 要移除的自动场景的ID
     */
    public void removeAutoScene(int autoSceneId){
        try{
            // 遍历自动场景集合，查找并移除指定ID的场景
            Iterator<AutomationScene> iterator = autoScenes.values().iterator();
            while (iterator.hasNext()) {
                AutomationScene autoScene = iterator.next();
                if(autoScene.getSceneId() == autoSceneId){
                    iterator.remove();
                    System.out.println("自动化场景已移除");
                    return;
                }
            }
            throw new CannotDoException("自动化场景不存在");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

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
        try{
            for (AutomationScene autoScene : autoScenes.values()) {
                if (autoScene.getSceneId() == autoSceneId) {
                    return autoScene;
                }
            }
            throw new CannotFindException("自动化场景不存在");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
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
     * 根据设备ID查找设备
     * @param deviceId 设备ID
     * @return 找到的设备对象，如果未找到则返回null
     */
    public Device findDeviceById(int deviceId){
        // 遍历所有房间查找指定ID的设备
        for(Room room : rooms.values()){
            for(Device device : room.getDevices()){
                if(device.getDeviceId() == deviceId){
                    return device;
                }
            }
        }
        return null;
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
        return "Household{householdId='" + householdId + ", address='" + address + "}";
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
