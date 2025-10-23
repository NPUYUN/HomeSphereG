import AutomatedWorkflow.*;
import DeviceEquipment.*;
import EmissionReduction.EnergyReporting;
import NormalException.CannotDoException;
import NormalException.CannotFindException;
import NormalException.RepeatedException;
import UserAndHousehold.*;

import java.util.*;

/**
 * HomeSphereSystem 类是智能家居系统的主控制器
 * 负责管理用户、家庭户、设备和自动化场景等核心功能
 * 提供用户注册登录、家庭户管理、设备控制、能耗统计等服务
 */
public class HomeSphereSystem {
    // 使用 LinkedHashMap 存储用户，以用户ID为键，用户对象为值
    private Map<Integer, User> users = new LinkedHashMap<>();
    // 使用 LinkedHashMap 存储家庭户，以家庭户ID为键，家庭户对象为值
    private Map<Integer, Household> households = new LinkedHashMap<>();
    private User admin;
    private User currentUser;

    public static final String SUPER_USER_NAME = "administrator";

    /**
     * 构造函数，初始化系统超级用户
     */
    public HomeSphereSystem(){
        this.admin = new User(0, SUPER_USER_NAME, "111111", "管理员", "13512345678");
        users.put(admin.getUserId(), admin);
        currentUser = admin;
    }

    /**
     * 注册新用户
     * @param loginName 登录名
     * @param loginPassword 登录密码
     * @param userName 用户姓名
     * @param phoneNumber 手机号码
     * @return 注册成功的用户对象，如果用户已存在则返回null
     */
    public User register(String loginName, String loginPassword, String userName, String phoneNumber) {
        try{
            // 检查必要参数是否为空
            if (loginName == null || loginName.trim().isEmpty() ||
                    loginPassword == null || loginPassword.trim().isEmpty()) {
                throw new IllegalArgumentException("参数不能为空！");
            }

            // 检查用户是否已存在
            for (User user : users.values()) {
                if (user.getLoginName().equals(loginName)) {
                    throw new RepeatedException("用户已存在！");
                }
            }

            // 创建新用户并添加到用户映射中
            User user = new User(users.size() + 1, loginName, loginPassword, userName, phoneNumber);
            users.put(user.getUserId(), user);
            return user;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }



    /**
     * 用户登录功能
     * @param loginName 登录用户名
     * @param loginPassword 登录密码
     */
    public void login(String loginName, String loginPassword) {
        try{
            // 参数验证
            if (loginName == null || loginPassword == null) {
                throw new IllegalArgumentException("参数不能为空！");
            }

            // 遍历用户映射查找匹配的用户名和密码
            for (User user : users.values()) {
                if (user.getLoginName().equals(loginName) && user.getLoginPassword().equals(loginPassword)) {
                    currentUser = user;
                    System.out.println("登录成功！欢迎 " + user.getUserName());
                    return;
                }
            }
            System.out.println("登录失败！用户名或密码错误。");
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * 用户退出登录功能
     * 该方法用于清除当前用户信息并显示退出登录提示
     */
    public void logoff(){
        currentUser = null; // 清空当前用户信息
        System.out.println("已退出登录！");
    }

    /**
     * 根据用户ID查找用户信息
     * @param userId 用户ID
     * @return 返回找到的用户对象，如果未找到则返回null
     */
    public User findUserById(int userId){
        // 直接通过键查找用户
        User user = users.get(userId);
        try{
            if(user == null){
                throw new CannotFindException("用户不存在！");
            }
            return user;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * 根据家庭ID查找对应的家庭对象
     * @param householdId 要查找的家庭ID
     * @return 找到的家庭对象，如果未找到则返回null
     */
    public Household findHouseholdById(int householdId){
        // 直接通过键查找家庭户
        return households.get(householdId);
    }

    /**
     * 添加一个新的家庭户
     * @param address 家庭户的地址信息
     * @return 创建的家庭户对象
     */
    public Household addHousehold(String address) {
        try{
            // 参数验证
            if (address == null || address.trim().isEmpty()) {
                throw new IllegalArgumentException("地址不能为空！");
            }

            // 生成唯一ID
            int newId = generateUniqueHouseholdId();
            Household household = new Household(newId, address);
            households.put(household.getHouseholdId(), household);
            return household;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

   /**
     * 生成唯一的家庭ID
     *
     * @return 返回一个新的唯一家庭ID，该ID比当前所有家庭ID中的最大值大1
     */
    private int generateUniqueHouseholdId() {
        // 查找当前所有家庭ID中的最大值
        int maxId = 0;
        for (Integer id : households.keySet()) {
            if (id > maxId) {
                maxId = id;
            }
        }
        // 返回最大ID加1作为新的唯一ID
        return maxId + 1;
    }

    /**
     * 根据户主ID删除户主信息
     * @param householdId 户主ID，用于标识要删除的户主
     */
    public void removeHousehold(int householdId){
        try{
            // 检查要删除的户主是否存在
            if(findHouseholdById(householdId) == null){
                throw new CannotFindException("户主不存在！");
            }

            // 从户主映射中移除指定的户主
            households.remove(householdId);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * 获取当前登录的用户对象
     * @return 当前登录的用户对象
     */
    public User getCurrentUser(){
        return currentUser;
    }

    /**
     * 删除用户信息
     * @param userId 要删除的用户ID
     */
    public void removeUser(int userId) {
        try{
            // 检查用户是否存在
            User userToRemove = findUserById(userId);
            if (userToRemove == null) {
                throw new CannotFindException("用户不存在！");
            }

            // 检查是否为当前用户，禁止删除自己
            if (currentUser != null && currentUser.getUserId() == userId) {
                throw new CannotDoException("不能删除自己！");
            }

            // 检查是否为超级用户，禁止删除超级用户
            if (userId == 0) {
                throw new CannotDoException("不能删除超级用户！");
            }

            // 执行删除操作
            users.remove(userId);
            System.out.println("用户删除成功！");
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }


    /**
     * 获取所有用户列表
     * @return 用户列表
     */
    public List<User> getUsers(){
        // 将Map中的值转换为List返回
        return new ArrayList<>(users.values());
    }

    /**
     * 获取所有家庭户列表
     * @return 家庭户列表
     */
    public List<Household> getHouseholds(){
        // 将Map中的值转换为List返回
        return new ArrayList<>(households.values());
    }

    /**
     * 根据家庭ID列出该家庭下的所有设备
     * @param householdId 家庭ID，用于查找对应的家庭对象
     */
    public void listDeviceByHousehold(int householdId) {
        try{
            // 查找指定ID的家庭对象
            Household household = findHouseholdById(householdId);
            if (household == null) {
                throw new CannotFindException("家庭不存在！");
            }

            // 检查是否有设备
            List<Device> devices = household.listAllDevices();
            if (devices.isEmpty()) {
                throw new CannotFindException("该家庭没有设备！");
            }

            // 遍历并打印该家庭下的所有设备
            System.out.println("家庭 " + householdId + " 下的设备列表：");
            for (Device device : devices) {
                System.out.println(device);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }


    /**
     * 根据家庭ID列出所有自动场景
     * @param householdId 家庭ID，用于查找对应的家庭对象
     */
    public void listAutoScenesByHousehold(int householdId){
        try{
            // 查找指定ID的家庭对象
            Household household = findHouseholdById(householdId);
            if(household == null){
                throw new CannotFindException("家庭不存在！");
            }
            // 遍历并打印该家庭的所有自动场景
            for(AutomationScene autoScene : household.getAutoScenes()){
                System.out.println(autoScene);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * 显示指定家庭ID的能耗报告
     * @param householdId 家庭ID，用于查找对应的家庭对象
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    public void displayEnergyReportings(int householdId, Date startTime, Date endTime) {
        try{
            // 参数验证
            if (startTime == null || endTime == null) {
                throw new IllegalArgumentException("开始和结束时间不能为空！");
            }

            if (startTime.after(endTime)) {
                throw new IllegalArgumentException("开始时间不能早于结束时间！");
            }

            // 根据家庭ID查找家庭对象
            Household household = findHouseholdById(householdId);
            if (household == null) {
                throw new CannotFindException("家庭不存在！");
            }

            System.out.println("=== 家庭 " + householdId + " 能耗报告 ===");
            System.out.println("时间段: " + startTime + " 至 " + endTime);
            System.out.println("----------------------------------------");

            double totalEnergy = 0;
            boolean hasEnergyDevices = false;

            // 遍历家庭中的所有设备，统计能耗信息
            for (Device device : household.listAllDevices()) {
                if (device instanceof EnergyReporting) {
                    double energy = ((EnergyReporting) device).getReport(startTime, endTime);
                    System.out.println(device.getName() + ": " + energy + " kWh");
                    totalEnergy += energy;
                    hasEnergyDevices = true;
                }
            }

            if (!hasEnergyDevices) {
                System.out.println("该家庭没有可统计能耗的设备。");
            } else {
                System.out.println("----------------------------------------");
                System.out.println("总耗能: " + totalEnergy + " kWh");
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * 手动触发指定家庭ID的指定自动场景
     * @param householdId 家庭ID，用于查找对应的家庭对象
     * @param sceneId 自动场景ID，用于查找对应的自动场景对象
     */
    public void manualTrigSceneById(int householdId , int sceneId){
        try{
            // 根据家庭ID查找家庭对象
            Household household = findHouseholdById(householdId);
            if (household == null) {
                throw new CannotFindException("家庭不存在！");
            }
            // 在家庭对象中查找指定的自动场景
            AutomationScene autoScene = household.findAutoSceneById(sceneId);
            if (autoScene == null) {
                throw new CannotFindException("自动场景不存在！");
            }
            // 手动触发该自动场景
            autoScene.manualTrig();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * 重写toString方法，返回系统对象的字符串表示
     * @return 系统对象的字符串表示
     */
    @Override
    public String toString(){
        return "HomeSphereSystem{users='" + users + ", households='" + households + "}";
    }
}
