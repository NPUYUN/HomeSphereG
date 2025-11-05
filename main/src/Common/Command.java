package Common;

import AutomatedWorkflow.AutomationScene;
import AutomatedWorkflow.DeviceAction;
import AutomatedWorkflow.DeviceCommand.DeviceCommand;
import DeviceEquipment.*;
import EmissionReduction.RunningLog;
import EmissionReduction.RunningLogFormatter;
import NormalException.CannotFindException;
import NormalException.NotAdminException;
import UserAndHousehold.Household;
import UserAndHousehold.Membership;
import UserAndHousehold.Room;
import UserAndHousehold.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * 命令行界面控制类，负责处理用户输入的各种指令并调用相应系统功能
 */
public class Command {
    private static final Manufacturer DEFAULT_AIR_CONDITIONER_MANUFACTURER =
            new Manufacturer(1 , "美的" , "Wi-Fi");
    private static final Manufacturer DEFAULT_LIGHT_BULB_MANUFACTURER =
            new Manufacturer(2 , "LED" , "Wi-Fi");
    private static final Manufacturer DEFAULT_SMART_LOCK_MANUFACTURER =
            new Manufacturer(3 , "欧莱雅" , "Wi-Fi");
    private static final Manufacturer DEFAULT_BATHROOM_SCALE_MANUFACTURER =
            new Manufacturer(4 , "Scale" , "Wi-Fi");

    public static HomeSphereSystem system = HomeSphereSystem.getInstance();
    public static Scanner scanner = new Scanner(System.in);

    /**
     * 用户登录功能
     * 提示用户输入用户名和密码，并尝试登录系统
     * @return 登录成功返回true，失败返回false
     */
    public static boolean loginUser() {
        System.out.println();
        System.out.print("请输入用户名：");
        String username = scanner.next();
        System.out.print("请输入密码：");
        String password = scanner.next();

        // 尝试执行用户登录操作
        try{
            system.login(username, password);
            System.out.println("User " + username + " logged in");
            return true;
        }
        catch(Exception e){
            // 捕获登录异常并输出错误信息
            System.out.println(e.getMessage());
            return false;
        }
        finally {
            System.out.println();
        }
    }

    /**
     * 用户注册功能
     * 提示用户输入注册信息，并尝试在系统中创建新用户
     */
    public static void registerUser() {
        System.out.println();
        System.out.print("请输入用户名：");
        String username = scanner.next();
        System.out.print("请输入密码：");
        String password = scanner.next();
        System.out.print("请输入姓名：");
        String nickname = scanner.next();
        System.out.print("请输入联系电话：");
        String phoneNumber = scanner.next();

        // 调用系统注册方法进行用户注册，并处理可能发生的异常
        try{
            User user =system.register(username, password, nickname, phoneNumber);
            System.out.println("用户注册成功：" +  user);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        finally {
            System.out.println();
        }
    }

    /**
     * 创建家庭功能
     * 仅限超级管理员使用，用于添加新的家庭信息到系统中
     */
    public static void CreateHousehold() {
        System.out.println();

        // 尝试添加家庭信息到系统
        try{
            if(!system.getCurrentUser().getLoginName().equals(HomeSphereSystem.SUPER_USER_NAME)){
                throw new NotAdminException("非超级管理员无权创建家庭");
            }

            System.out.print("请输入家庭地址： ");
            String address = scanner.next();

            Household household = system.addHousehold(address);
            System.out.println("家庭添加成功：" +  household);
        }
        // 捕获并显示添加过程中可能出现的异常
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        finally {
            System.out.println();
        }
    }

    /**
     * 列出家庭列表
     * 超级管理员可以看到所有家庭，普通用户只能看到自己所属的家庭
     */
    public static void listHousehold() {
        System.out.println();
        System.out.println("=== 家庭列表 ===");

        // 判断当前用户是否为超级管理员
        if(system.getCurrentUser().getLoginName().equals(HomeSphereSystem.SUPER_USER_NAME)){
            // 超级管理员显示所有家庭
            for (Household household : system.getHouseholds()) {
                System.out.println(household);
            }
        }
        else{
            // 其他用户只显示自己所属的家庭
            for (Membership membership: system.getCurrentUser().getMemberships()) {
                System.out.println(membership.getHousehold());
            }
        }

        System.out.println();
    }

    /**
     * 添加用户到家庭
     * 需要管理员权限，用于将指定用户添加到指定家庭中
     */
    public static void addUserToHousehold() {
        // 根据用户ID和家庭ID查找对应的用户和家庭对象，然后添加用户到家庭中
        try{
            System.out.println();
            System.out.print("请输入用户ID：");
            int userId = scanner.nextInt();
            User user = system.findUserById(userId);
            if(user == null){
                throw new CannotFindException("用户不存在");
            }

            System.out.print("请输入家庭ID：");
            int householdId = scanner.nextInt();
            Household household = system.findHouseholdById(householdId);
            if(household == null){
                throw new CannotFindException("家庭不存在");
            }

            // 判断当前用户是否为管理员
            household.isAdminUser(system.getCurrentUser().getUserId());

            System.out.print("请输入角色：");
            String role = scanner.next();

            household.addUser(user, role);
            System.out.println("家庭成员添加/更新成功");
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        finally {
            System.out.println();
        }
    }

    /**
     * 列出家庭成员列表
     * 超级管理员可以看到所有家庭的成员，普通用户只能看到自己所在家庭的成员
     */
    public static void listUserInHousehold() {
        System.out.println();
        System.out.println("=== 家庭成员列表 ===");

        // 判断当前用户是否为超级管理员
        if(system.getCurrentUser().getLoginName().equals(HomeSphereSystem.SUPER_USER_NAME)){
            // 超级管理员：遍历所有家庭并显示每个家庭的成员信息
            for(Household household : system.getHouseholds()){
                System.out.println("家庭ID：" + household.getHouseholdId() + ", 家庭地址：" + household.getAddress());
                for(Membership membership : household.getMemberships()){
                    System.out.println(membership);
                    System.out.println("------------------------------");
                }
            }
        }
        else{
            // 其他用户：只显示当前用户所在家庭的成员信息
            for(Membership membership : system.getCurrentUser().getMemberships()){
                Household household = membership.getHousehold();
                System.out.println("家庭ID：" + household.getHouseholdId() + ", 家庭地址：" + household.getAddress());
                for(Membership member : household.getMemberships()){
                    System.out.println(member);
                    System.out.println("------------------------------");
                }
            }
        }
        System.out.println();
    }

    /**
     * 添加设备到房间
     * 需要管理员权限，支持添加四种类型的设备：空调、智能灯泡、智能锁、智能体重秤
     */
    public static void addDevice() {
        System.out.println();
        try {
            System.out.print("请输入家庭ID：");
            int householdId = scanner.nextInt();
            Household household = system.findHouseholdById(householdId);
            if(household == null) {
                throw new CannotFindException("家庭不存在！");
            }

            // 判断当前用户是否为管理员或超级用户
            household.isAdminUser(system.getCurrentUser().getUserId());

            System.out.print("请输入设备名称：");
            String name = scanner.next();
            System.out.print("选择设备类型(1.空调 2.智能灯泡 3.智能锁 4.智能体重秤)：");
            int type = scanner.nextInt();
            System.out.print("请输入要添加到的房间ID：");
            int roomId = scanner.nextInt();

            Room room = household.findRoomById(roomId);
            if(room == null) {throw new CannotFindException("房间不存在！");}

            // 获取当前家庭中所有设备的最大ID，并加1作为新设备ID
            int deviceId = household.listAllDevices().stream()
                .mapToInt(Device::getDeviceId)
                .max()
                .orElse(0) + 1;

            // 根据设备类型创建设备对象并添加到房间中
            switch (type){
                case 1:
                    AirConditioner airConditioner = (AirConditioner) DEFAULT_AIR_CONDITIONER_MANUFACTURER.createDevice(deviceId, name, "AirConditioner");
                    room.addDevice(airConditioner);
                    System.out.println("添加设备成功：" + airConditioner);
                    break;
                case 2:
                    LightBulb lightBulb = (LightBulb) DEFAULT_LIGHT_BULB_MANUFACTURER.createDevice(deviceId, name, "LightBulb");
                    room.addDevice(lightBulb);
                    System.out.println("添加设备成功：" + lightBulb);
                    break;
                case 3:
                    SmartLock smartLock = (SmartLock) DEFAULT_SMART_LOCK_MANUFACTURER.createDevice(deviceId, name, "SmartLock");
                    room.addDevice(smartLock);
                    System.out.println("添加设备成功：" + smartLock);
                    break;
                case 4:
                    BathroomScale bathroomScale = (BathroomScale) DEFAULT_BATHROOM_SCALE_MANUFACTURER.createDevice(deviceId, name, "BathroomScale");
                    room.addDevice(bathroomScale);
                    System.out.println("添加设备成功：" + bathroomScale);
                    break;
                default:
                    throw new IllegalArgumentException("设备类型错误！");
            }


        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        finally {
            System.out.println();
        }

    }

    /**
     * 从房间移除设备
     * 需要管理员权限，根据设备ID从指定家庭中移除设备
     */
    public static void removeDevice() {
        System.out.println();
        try{
            System.out.print("请输入家庭ID：");
            int householdId = scanner.nextInt();

            // 查找指定ID的家庭
            Household household = system.findHouseholdById(householdId);
            if(household == null) {
                throw new CannotFindException("家庭不存在！");
            }

            // 判断当前用户是否为管理员或超级用户
            household.isAdminUser(system.getCurrentUser().getUserId());

            System.out.print("请输入要移除的设备ID：");
            int deviceId = scanner.nextInt();
            scanner.nextLine();

            Device device = household.findDeviceById(deviceId);
            if(device == null) {
                throw new CannotFindException("设备不存在！");
            }
            for(Room room : household.getRooms()){
                if(room.findDeviceById(deviceId) != null){
                    room.removeDevice(deviceId);
                    System.out.println("移除设备成功：" + device);
                    return;
                }
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        finally {
            System.out.println();
        }
    }

    /**
     * 列出家庭中的所有设备
     * 显示指定家庭中所有房间的设备信息
     */
    public static void listDevice() {
        System.out.println();
        System.out.print("请输入家庭ID：");
        // 获取用户输入的家庭ID
        int householdId = scanner.nextInt();
        // 调用系统方法列出指定家庭的所有设备
        system.listDeviceByHousehold(householdId);
        System.out.println();
    }


    /**
     * 创建自动化场景
     * 需要管理员权限，允许用户定义一系列设备操作组合成场景
     */
    public static void CreateScene() {
        System.out.println();
        try{
            // 获取用户输入的家庭ID并验证家庭是否存在
            System.out.print("请输入家庭ID：");
            int householdId = scanner.nextInt();
            Household household = system.findHouseholdById(householdId);
            if(household == null) {
                throw new CannotFindException("家庭不存在！");
            }

            // 验证当前用户是否为管理员
            household.isAdminUser(system.getCurrentUser().getUserId());

            // 获取场景名称和描述
            System.out.print("请输入场景名称：");
            String name = scanner.next();
            System.out.print("请输入场景描述：");
            String description = scanner.next();

            // 创建新的自动化场景对象
            AutomationScene autoScene = new AutomationScene(household.getAutoScenes().size() + 1, name, description);

            System.out.println("当前场景中的设备操作：");
            // 循环添加设备操作到场景中
            while(true){
                System.out.print("添加设备操作？(y/n)：");
                String input = scanner.next();
                if(input.equals("y")){
                    // 获取设备ID并查找对应设备
                    System.out.print("请输入设备ID：");
                    int deviceId = scanner.nextInt();
                    Device device = household.findDeviceById(deviceId);

                    // 获取操作命令
                    System.out.print("请输入操作命令(例如powerOn/setTemperature)：");
                    String command = scanner.next();
                    DeviceCommand deviceCommand = null;

                    // 处理设置温度的特殊命令
                    if(command.equals("setTemperature")){
                        if(!(device instanceof AirConditioner)){
                            System.out.println("设备不是空调！");
                            continue;
                        }
                        System.out.print("请输入温度值：");
                        String parameters = scanner.next();

                        // 验证温度值范围
                        if(Double.parseDouble(parameters) > 30 || Double.parseDouble(parameters) < 16){
                            System.out.println("温度值超出范围！");
                            continue;
                        }
                        deviceCommand = DeviceAction.createCommand(command, device, parameters);
                    }
                    else {
                        deviceCommand = DeviceAction.createCommand(command, device, null);
                    }
                    autoScene.addCommand(deviceCommand);

                    // 显示当前场景中的所有设备操作
                    System.out.println("当前场景中的设备操作：");
                    for(DeviceCommand commands : autoScene.getCommands()){
                        System.out.println(deviceCommand.getDevice().getName() + " - " + deviceCommand.getDescription());
                    }
                }
                else{
                    break;
                }
            }
            // 将创建的场景添加到家庭中
            household.addAutoScene(autoScene);
            System.out.println("场景创建成功！");

        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        finally {
            System.out.println();
        }
    }


    /**
     * 触发自动化场景
     * 手动执行指定家庭中的某个自动化场景
     */
    public static void TriggerScene() {
        System.out.println();

        // 获取用户输入的家庭ID和场景ID
        System.out.print("请输入家庭ID：");
        int householdId = scanner.nextInt();

        System.out.print("请输入要触发的场景ID：");
        int sceneId = scanner.nextInt();

        // 调用系统接口手动触发指定场景
        system.manualTrigSceneById(householdId, sceneId);

        System.out.println();
    }

    /**
     * 列出家庭中的所有自动化场景
     * 显示指定家庭中定义的所有自动化场景信息
     */
    public static void listScene() {
        // 获取用户输入的家庭ID并显示该家庭的所有自动化场景
        System.out.println();
        System.out.print("请输入家庭ID：");
        int householdId = scanner.nextInt();
        system.listAutoScenesByHousehold(householdId);
        System.out.println();
    }


    /**
     * 查看设备运行日志
     * 显示指定家庭中所有设备的运行日志记录
     */
    public static void logDeviceRunLog() {
        System.out.println();
        try{
            System.out.print("请输入家庭ID：");
            int householdId = scanner.nextInt();
            Household household = system.findHouseholdById(householdId);
            if(household == null) {
                throw new CannotFindException("家庭不存在！");
            }

            // 遍历家庭中的所有房间和设备，显示设备运行日志
            System.out.println();
            System.out.println("=== 设备运行日志 ===");
            for(Room room : household.getRooms()){
                System.out.println("房间：" + room.getName());
                for(Device device : room.getDevices()){
                    System.out.println("ID：" + device.getDeviceId());
                    System.out.println("名称：" + device.getName());
                    System.out.println("类型：" + device.getClass().getSimpleName());
                    System.out.println("运行日志:");
                        for(RunningLog runningLog : device.getRunningLogs()){
                            System.out.println(runningLog);
                        }
                    System.out.println("--------------------------------------------------");
                }
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        finally {
            System.out.println();
        }

    }

    /**
     * 查看能耗报告
     * 显示指定时间段内指定家庭的能耗统计信息
     */
    public static void logEnergyReport() {
        System.out.println();
        try{
            // 获取用户输入的家庭ID和查询时间范围
            System.out.print("请输入家庭ID：");
            int householdId = scanner.nextInt();

            System.out.print("请输入起始时间(格式：yyyy-MM-dd)：");
            String start = scanner.next();
            Date startTime = new SimpleDateFormat("yyyy-MM-dd").parse(start);


            System.out.print("请输入结束时间(格式：yyyy-MM-dd)：");
            String end = scanner.next();
            Date endTime = new SimpleDateFormat("yyyy-MM-dd").parse(end);

            // 调用系统方法显示能耗报告
            system.displayEnergyReportings(householdId, startTime, endTime);

        }
        catch (Exception e) {
            // 处理输入异常情况
            System.out.println(e.getMessage());
        }
        finally {
            System.out.println();
        }
    }

    /**
     * 将所有家庭数据保存为指定格式的文件
     * @param formatter 日志格式化器
     * @param extension 文件扩展名
     */
    public static void saveHouseholdsToFile(RunningLogFormatter formatter, String extension) {
        for (Household household : Command.system.getHouseholds()) {
            File file = new File("./data/"+ household.getHouseholdId() + "." + extension);
            try (FileWriter writer = new FileWriter(file)) {
                // 先确保文件存在
                file.createNewFile();
                writer.write(formatter.format(household));
                System.out.println("保存 " + extension.toUpperCase() + " 文件成功");
            } catch (IOException e) {
                System.out.println("保存 " + extension.toUpperCase() + " 文件失败: " + e.getMessage());
            }
        }
    }


}
