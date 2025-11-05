package Common;

import AutomatedWorkflow.AutomationScene;
import AutomatedWorkflow.DeviceAction;
import AutomatedWorkflow.DeviceCommand.DeviceCommand;
import DeviceEquipment.*;
import EmissionReduction.HtmlRunningLogFormatter;
import EmissionReduction.JsonRunningLogFormatter;
import EmissionReduction.RunningLog;
import EmissionReduction.XmlRunningLogFormatter;
import UserAndHousehold.Household;
import UserAndHousehold.Room;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.System.exit;

/**
 * 命令行用户界面类，负责处理用户交互和系统菜单展示
 * 提供完整的系统操作界面，包括用户登录、注册、家庭管理、设备控制等功能
 */
public class CommandUI {
    private static Command command = new Command();
    private static Scanner scanner = new Scanner(System.in);

    /**
     * 主菜单函数，显示系统主界面并处理用户选择
     * 该函数提供用户登录、注册和退出系统的功能入口
     * 通过循环调用自身来维持菜单的持续显示，直到用户选择退出
     */
    public static void mainMenu() {
        while (true) {
            System.out.println("=== 智能家居生态系统HomeSphereGv2.0 ===");
            System.out.println("1. 用户登录");
            System.out.println("2. 用户注册");
            System.out.println("3. 退出系统");
            System.out.print("请选择操作：");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // 消费换行符

                switch (choice) {
                    case 1:
                        if (Command.loginUser()) {
                            householdMenu();
                        }
                        break;
                    case 2:
                        Command.registerUser();
                        break;
                    case 3:
                        System.out.println();
                        System.out.println("=== 退出系统 ===");
                        System.out.println("感谢使用HomeSphereG系统，再见！");
                        return;
                    default:
                        System.out.println("无效选择，请重新输入");
                }
            } catch (InputMismatchException e) {
                System.out.println("输入格式错误，请输入数字");
                scanner.nextLine(); // 清除错误输入
                System.out.println();
            }
        }
    }

    /**
     * 家庭管理主菜单函数
     * 显示家庭管理相关操作选项，包括家庭成员管理、设备管理、场景管理等功能
     * 通过用户输入选择对应功能模块进行操作
     */
    public static void householdMenu() {
        while (true) {
            System.out.println("=== 家庭管理菜单 ===");
            System.out.println("1. 管理家庭及成员");
            System.out.println("2. 管理房间设备");
            System.out.println("3. 智能场景管理");
            System.out.println("4. 日志能耗管理");
            System.out.println("5. 退出登录");
            System.out.print("请选择操作：");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // 消费换行符

                switch (choice) {
                    case 1:
                        System.out.println();
                        householdAndUserMenu();
                        break;
                    case 2:
                        System.out.println();
                        deviceMenu();
                        break;
                    case 3:
                        System.out.println();
                        autoSceneMenu();
                        break;
                    case 4:
                        System.out.println();
                        logEnergyMenu();
                        break;
                    case 5:
                        Command.system.logoff();
                        System.out.println();
                        return;
                    default:
                        System.out.println("无效选择，请重新输入");
                }
            } catch (InputMismatchException e) {
                System.out.println("输入格式错误，请输入数字");
                scanner.nextLine(); // 清除错误输入
                System.out.println();
            }
        }
    }

    /**
     * 家庭及成员管理菜单函数
     * 显示家庭及成员管理的操作选项，包括新建家庭、列出所有家庭、添加成员至家庭、
     * 列出家庭所有成员等功能，并根据用户选择执行相应的操作
     */
    public static void householdAndUserMenu() {
        while (true) {
            System.out.println("=== 家庭及成员管理 ===");
            System.out.println("1. 新建家庭");
            System.out.println("2. 列出所有家庭");
            System.out.println("3. 添加成员至家庭");
            System.out.println("4. 列出家庭所有成员");
            System.out.println("5. 返回上级");
            System.out.print("请选择操作：");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // 消费换行符

                switch (choice) {
                    case 1:
                        Command.CreateHousehold();
                        break;
                    case 2:
                        Command.listHousehold();
                        break;
                    case 3:
                        Command.addUserToHousehold();
                        break;
                    case 4:
                        Command.listUserInHousehold();
                        break;
                    case 5:
                        System.out.println();
                        return;
                    default:
                        System.out.println("无效选择，请重新输入");
                }
            } catch (InputMismatchException e) {
                System.out.println("输入格式错误，请输入数字");
                scanner.nextLine(); // 清除错误输入
                System.out.println();
            }
        }
    }

    /**
     * 设备管理菜单函数
     * 显示设备管理的操作选项，包括添加设备、移除设备、列出所有设备等功能
     * 并根据用户选择执行相应的操作
     */
    public static void deviceMenu() {
        while (true) {
            System.out.println("=== 设备管理 ===");
            System.out.println("1. 添加设备");
            System.out.println("2. 移除设备");
            System.out.println("3. 列出所有设备");
            System.out.println("4. 返回上级");
            System.out.print("请选择操作：");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // 消费换行符

                switch (choice) {
                    case 1:
                        Command.addDevice();
                        break;
                    case 2:
                        Command.removeDevice();
                        break;
                    case 3:
                        Command.listDevice();
                        break;
                    case 4:
                        System.out.println();
                        return;
                    default:
                        System.out.println("无效选择，请重新输入");
                }
            } catch (InputMismatchException e) {
                System.out.println("输入格式错误，请输入数字");
                scanner.nextLine(); // 清除错误输入
                System.out.println();
            }
        }
    }

    /**
     * 自动化场景菜单功能
     * 该方法显示场景管理菜单，提供创建场景、触发场景、列出场景等操作选项
     * 用户可通过输入数字选择相应功能，选择无效时会提示重新输入
     */
    public static void autoSceneMenu() {
        while (true) {
            System.out.println("=== 场景管理 ===");
            System.out.println("1. 创建新场景");
            System.out.println("2. 触发场景");
            System.out.println("3. 列出所有场景");
            System.out.println("4. 返回上级");
            System.out.print("请选择操作：");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // 消费换行符

                switch (choice) {
                    case 1:
                        Command.CreateScene();
                        break;
                    case 2:
                        Command.TriggerScene();
                        break;
                    case 3:
                        Command.listScene();
                        break;
                    case 4:
                        System.out.println();
                        return;
                    default:
                        System.out.println("无效选择，请重新输入");
                }
            } catch (InputMismatchException e) {
                System.out.println("输入格式错误，请输入数字");
                scanner.nextLine(); // 清除错误输入
                System.out.println();
            }
        }
    }

    /**
     * 日志能耗管理菜单函数
     * 该函数显示日志能耗管理的菜单选项，并根据用户选择执行相应的操作
     * 包括查看设备运行日志、查看能耗报告或返回上级菜单
     */
    public static void logEnergyMenu() {
        while (true) {
            System.out.println("=== 日志能耗管理 ===");
            System.out.println("1. 查看设备运行日志");
            System.out.println("2. 查看能耗报告");
            System.out.println("3. 返回上级");
            System.out.print("请选择操作：");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // 消费换行符

                switch (choice) {
                    case 1:
                        Command.logDeviceRunLog();
                        break;
                    case 2:
                        Command.logEnergyReport();
                        break;
                    case 3:
                        System.out.println();
                        return;
                    default:
                        System.out.println("无效选择，请重新输入");
                        continue;
                }
            } catch (InputMismatchException e) {
                System.out.println("输入格式错误，请输入数字");
                scanner.nextLine(); // 清除错误输入
                System.out.println();
                continue;
            }
        }
    }

    /**
     * 程序入口函数，用于初始化智能家居系统的数据结构并启动主菜单。
     *
     * 该方法创建了多个设备制造商、家庭、房间、设备以及自动化场景，并将它们组织成一个完整的家居结构，
     * 最后将其添加到系统命令管理器中，并进入主菜单交互界面。
     */
    public static void main(String[] args) {
        // 创建各类设备的制造商对象
        Manufacturer airConditionerManufacturer = new Manufacturer(1, "美的", "Wi-Fi");
        Manufacturer bulbLightManufacturer = new Manufacturer(2, "LED", "Wi-Fi");
        Manufacturer smartLockManufacturer = new Manufacturer(3, "欧莱雅", "Wi-Fi");
        Manufacturer bathroomScaleManufacturer = new Manufacturer(4, "Scale", "Wi-Fi");

        // 创建家庭及房间对象
        Household household = new Household(1, "北京");
        Room livingroom = new Room(1, "客厅", 20);
        Room mainBedroom = new Room(2, "主卧", 30);
        Room guestBedroom = new Room(3, "次卧", 10);

        // 创建各种智能设备实例
        Device airConditioner1 = airConditionerManufacturer.createDevice(1, "美的空调", "AirConditioner");
        Device airConditioner2 = airConditionerManufacturer.createDevice(2, "美的空调", "AirConditioner");
        Device airConditioner3 = airConditionerManufacturer.createDevice(3, "美的空调", "AirConditioner");

        Device bulbLight1 = bulbLightManufacturer.createDevice(4, "LED灯", "LightBulb");
        Device bulbLight2 = bulbLightManufacturer.createDevice(5, "LED灯", "LightBulb");
        Device bulbLight3 = bulbLightManufacturer.createDevice(6, "LED灯", "LightBulb");

        Device smartLock = smartLockManufacturer.createDevice(7, "欧莱雅锁", "SmartLock");
        Device bathroomScale = bathroomScaleManufacturer.createDevice(8, "Scale", "BathroomScale");

        // 创建自动化场景
        AutomationScene morningScene = new AutomationScene(1, "早安场景", "早安");
        DeviceCommand morningCommand = DeviceAction.createCommand("setTemperature", airConditioner1, "25");
        morningScene.addCommand(morningCommand);

        AutomationScene eveningScene = new AutomationScene(2, "晚安场景", "晚安");
        DeviceCommand eveningCommand = DeviceAction.createCommand("setTemperature", airConditioner2, "18");
        eveningScene.addCommand(eveningCommand);

        // 将设备分配至对应的房间
        livingroom.addDevice(airConditioner1);
        livingroom.addDevice(bulbLight1);
        livingroom.addDevice(smartLock);
        livingroom.addDevice(bathroomScale);

        mainBedroom.addDevice(airConditioner2);
        mainBedroom.addDevice(bulbLight2);

        guestBedroom.addDevice(airConditioner3);
        guestBedroom.addDevice(bulbLight3);

        // 组装整个家庭结构：添加房间和自动化场景
        household.addRoom(livingroom);
        household.addRoom(mainBedroom);
        household.addRoom(guestBedroom);
        household.addAutoScene(morningScene);
        household.addAutoScene(eveningScene);

        // 尝试将构建好的家庭加入系统命令管理器
        try {
            Command.system.addHousehold(household.getHouseholdId(), household);
        } catch (Exception e) {
            System.out.println("添加家庭失败: " + e.getMessage());
        }
        JsonRunningLogFormatter jsonFormatter = JsonRunningLogFormatter.getInstance();
        HtmlRunningLogFormatter htmlFormatter = HtmlRunningLogFormatter.getInstance();
        XmlRunningLogFormatter xmlFormatter = XmlRunningLogFormatter.getInstance();

        // 启动主菜单界面
        mainMenu();

        // 保存为不同格式的文件
        Command.saveHouseholdsToFile(jsonFormatter, "json");
        Command.saveHouseholdsToFile(htmlFormatter, "html");
        Command.saveHouseholdsToFile(xmlFormatter, "xml");

    }
}
