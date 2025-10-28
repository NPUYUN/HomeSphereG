package Common;

import AutomatedWorkflow.AutomationScene;
import DeviceEquipment.AirConditioner;
import DeviceEquipment.Device;
import DeviceEquipment.LightBulb;
import NormalException.CannotDoException;
import NormalException.CannotFindException;
import NormalException.InvalidUserException;
import NormalException.RepeatedException;
import UserAndHousehold.Household;
import UserAndHousehold.Membership;
import UserAndHousehold.Room;
import UserAndHousehold.User;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Common.HomeSphereSystem 类是智能家居系统的主控制器
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

    /**
     * 用户名正则表达式：3-20位，以字母开头，只能包含字母、数字、下划线
     * 用于验证用户名格式是否符合要求
     */
    private static final String USERNAME_PATTERN = "^[a-zA-Z][a-zA-Z0-9_]{2,19}$";

    /**
     * 修改后的密码正则表达式：至少8位，必须包含大小写字母和数字
     * 用于验证密码强度，确保密码安全性
     */
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$";

    /**
     * 修改后的手机号正则表达式，匹配常见的手机号前三位
     * 用于验证手机号格式是否符合中国大陆手机号规则
     */
    private static final String PHONE_PATTERN = "^1(3[0-9]|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[0-35-9])\\d{8}$";

    /**
     * 系统超级用户名
     * 用于标识系统最高权限用户账户
     */
    public static final String SUPER_USER_NAME = "administrator";

    /**
     * 管理员角色标识符
     * 用于标识具有管理员权限的用户角色
     */
    public static final String MANAGE_USER_ROLE = "admin";

    /**
     * HomeSphereSystem单例实例
     * 用于存储HomeSphereSystem类的唯一实例，实现单例模式
     */
    private static HomeSphereSystem instance;

    /**
     * 私有构造函数，用于初始化HomeSphereSystem实例
     * 创建系统管理员用户并将其添加到用户集合中，同时设置当前用户为管理员
     */
    private HomeSphereSystem(){
        this.admin = new User(0, SUPER_USER_NAME, "111111", "管理员", "13512345678");
        users.put(admin.getUserId(), admin);
        currentUser = admin;
    }

    /**
     * 获取HomeSphereSystem的单例实例
     * 使用双重检查锁定模式确保线程安全的单例实现
     * @return HomeSphereSystem的唯一实例
     */
    public static HomeSphereSystem getInstance(){
        if(instance == null){
            instance = new HomeSphereSystem();
        }
        return instance;
    }


        /**
     * 注册新用户
     * @param loginName 登录名
     * @param loginPassword 登录密码
     * @param userName 用户姓名
     * @param phoneNumber 手机号码
     * @return 注册成功的用户对象，如果用户已存在则返回null
     */
    public User register(String loginName, String loginPassword, String userName, String phoneNumber) throws Exception{
        // 检查必要参数是否为空
        if (loginName == null || loginName.trim().isEmpty() ||
                loginPassword == null || loginPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("参数不能为空！");
        }

        // 检查用户是否已存在
        if(users.values().stream().anyMatch(user -> user.getLoginName().equals(loginName))){
            throw new RepeatedException("用户已存在！");
        }

        // 参数校验
        if (!loginName.matches(USERNAME_PATTERN)) {
            throw new IllegalArgumentException("用户名格式不正确！请输入3-20位，以字母开头，只能包含字母、数字、下划线的用户名。");
        }
        if (!loginPassword.matches(PASSWORD_PATTERN)) {
            throw new SecurityException("密码不安全！请输入至少8位，必须包含大小写字母和数字的密码。");
        }
        if (!phoneNumber.matches(PHONE_PATTERN)) {
            throw new IllegalArgumentException("手机号格式不正确！请输入正确的手机号码。");
        }


        // 创建新用户并添加到用户映射中
        int userId = generateUniqueId(users);
        User user = new User(userId, loginName, loginPassword, userName, phoneNumber);
        users.put(user.getUserId(), user);
        return user;
    }

        /**
     * 用户登录功能
     * @param loginName 登录用户名
     * @param loginPassword 登录密码
     */
    public void login(String loginName, String loginPassword) throws Exception{
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
            if(user.getLoginName().equals(loginName) && !user.getLoginPassword().equals(loginPassword)){
                throw new InvalidUserException("登录失败！密码错误!");
            }
        }
        throw new InvalidUserException("登录失败！用户不存在!");
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
        return users.get(userId);
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
    public Household addHousehold(String address) throws  Exception{
        // 参数验证
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("地址不能为空！");
        }

        // 生成唯一ID
        int newId = generateUniqueId(households);
        Household household = new Household(newId, address);
        households.put(household.getHouseholdId(), household);
        return household;
    }

        /**
     * 添加住户信息
     * @param id 住户唯一标识符
     * @param household 住户对象
     * @throws Exception 当参数为空时抛出异常
     */
    public void addHousehold(int id, Household household) throws  Exception{
        // 参数验证
        if (household == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }

        // 生成唯一ID
        households.put(id, household);

    }


      /**
     * 生成唯一的家庭ID
     *
     * @param map 包含家庭ID映射关系的Map对象
     * @return 返回一个新的唯一家庭ID，该ID比当前所有家庭ID中的最大值大1
     */
    public int generateUniqueId(Map map) {
        // 查找当前所有家庭ID中的最大值
        int maxId = map.keySet().stream()
                .filter(key -> key instanceof Integer)
                .mapToInt(key -> (Integer) key)
                .max()
                .orElse(0); // 或者根据业务需求提供合适的默认值


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
            // 查找指定ID的家庭并验证是否存在
            Household household = findHouseholdById(householdId);
            if(household == null) {
                throw new CannotFindException("家庭不存在！");
            }

            // 遍历并显示家庭中所有房间的设备信息
            System.out.println("=== 设备列表 ===");
            for(Room room : household.getRooms()){
                System.out.println("房间：" + room.getName());
                for(Device device : room.getDevices()){
                    System.out.println(device);
                }
                System.out.println("------------------------------");
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
            // 遍历家庭成员，检查当前用户是否有权限查看
            for(Membership membership : household.getMemberships()){
                if(membership.getUser().getUserId() == currentUser.getUserId()){
                    System.out.println("=== 智能场景列表 ===");
                    // 输出该家庭下的所有自动场景
                    for(AutomationScene autoScene : household.getAutoScenes()){
                        System.out.println(autoScene);
                        System.out.println("------------------------------");
                    }
                }
            }
            // 检查当前用户是否为超级用户
            if(currentUser.getLoginName().equals(SUPER_USER_NAME)){
                System.out.println("=== 智能场景列表 ===");
                // 输出该家庭下的所有自动场景
                for(AutomationScene autoScene : household.getAutoScenes()){
                    System.out.println(autoScene);
                }
                System.out.println("------------------------------");
                return;
            }
            throw new CannotDoException("您没有权限查看该家庭下的智能场景！");
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

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String startDate = dateFormat.format(startTime);
            String endDate = dateFormat.format(endTime);

            System.out.println("=== 能耗报告 ===");
            System.out.println("时间范围: " + startDate + " 至 " + endDate);
            System.out.println();

            double totalEnergy = 0;

            // 遍历房间和设备，计算并输出各设备能耗
            for(Room room : household.getRooms()){
                System.out.println("房间：" + room.getName());
                for(Device device : room.getDevices()){
                    if(device instanceof AirConditioner){
                        System.out.printf("设备：" + device.getName() + " - %.3f Wh\n", ((AirConditioner)device).getReport(startTime , endTime));
                        totalEnergy += ((AirConditioner)device).getReport(startTime , endTime);
                    }
                    if(device instanceof LightBulb){
                        System.out.printf("设备：" + device.getName() + " - %.3f Wh\n", ((LightBulb)device).getReport(startTime , endTime));
                        totalEnergy += ((LightBulb)device).getReport(startTime , endTime);
                    }
                }
                System.out.println();
            }

            // 输出总能耗（转换为kWh单位）
            System.out.printf("总能耗：%.3f kWh\n", totalEnergy / 1000);
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
        return "Common.HomeSphereSystem{users='" + users + ", households='" + households + "}";
    }

}
