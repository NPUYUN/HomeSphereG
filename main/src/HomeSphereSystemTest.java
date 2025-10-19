import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import AutomatedWorkflow.*;
import UserAndHousehold.*;
import DeviceEquipment.*;
import EmissionReduction.*;

public class HomeSphereSystemTest {
	private HomeSphereSystem system;
	private Manufacturer acManufacturer;
	private Manufacturer lightManufacturer;
	private AirConditioner ac;
	private LightBulb light;
	private SmartLock lock;
	private BathroomScale scale;
	private Room livingRoom;
	private Room bedroom;
	private AutomationScene eveningScene;

	@Before
	public void setUp() {
		
		// 创建系统
		system = new HomeSphereSystem();
//		households = system.getHouseholds();
//		users = system.getUsers();
				
		// 创建制造商
		acManufacturer = new Manufacturer(1, "AC Corp", "WiFi, ZigBee");
		lightManufacturer = new Manufacturer(2, "Light Inc", "WiFi");

		// 创建设备
		ac = new AirConditioner(1, "Living Room AC", acManufacturer);
		light = new LightBulb(2, "Kitchen Light", lightManufacturer);
		lock = new SmartLock(3, "Front Door Lock", acManufacturer);
		scale = new BathroomScale(4, "Bathroom Scale", lightManufacturer);

		// 设置设备状态
		ac.setCurrTemp(25.0);
		ac.setTargetTemp(22.0);
		light.setBrightness(80);
		light.setColorTemp(4000);
		lock.setLocked(true);
		scale.setBodyMass(70.5);
		scale.setBatteryLevel(85);

		// 创建房间并添加设备
		livingRoom = new Room(1, "Living Room", 25.5);
		livingRoom.addDevice(ac);
		livingRoom.addDevice(light);

		bedroom = new Room(2, "Bedroom", 18.0);
		bedroom.addDevice(lock);

		// 创建家庭并添加房间
		Household household1 = system.addHousehold("西安市友谊西路127号");;
		household1.addRoom(livingRoom);
		household1.addRoom(bedroom);

		household1.addUser(system.findUserById(0), "admin");
		// 创建用户
		User user1 = system.register("hxt", "123456", "航小天", "13011112222");
		User user2 = system.register("jxc", "123456", "计小才", "13033334444");
		
		household1.addUser(user1, "brother");
		household1.addUser(user2, "sister");

		// 创建自动化场景
		eveningScene = new AutomationScene(1, "晚安模式", "关闭灯光调节至舒适温度");

		DeviceAction lightAction = new DeviceAction("powerOff", "", light);
		DeviceAction acAction = new DeviceAction("setTemperature", "26.0", ac);

		eveningScene.addAction(lightAction);
		eveningScene.addAction(acAction);
		
		//设置时间触发器，周一至周五，23点整
		Trigger trigger = new TimeTrigger("23:00:00", "1,2,3,4,5");
		eveningScene.addTrigger(trigger);

		household1.addAutoScene(eveningScene);
	}

	@After
	public void tearDown() {
		system = null;
	}

	@Test
	public void testHouseholdCreation() {
		assertNotNull(system.getHouseholds().get(0));
		assertEquals(1, system.getHouseholds().get(0).getHouseholdId());
		assertEquals("西安市友谊西路127号", system.getHouseholds().get(0).getAddress());
	}

	@Test
	public void testHouseholdRoomManagement() {
		// 测试添加房间
		assertEquals(2, system.findHouseholdById(1).getRooms().size());

		// 测试移除房间
		system.findHouseholdById(1).removeRoom(2);
		assertEquals(1, system.findHouseholdById(1).getRooms().size());

		// 测试获取房间
		Room room = system.findHouseholdById(1).getRooms().get(0);
		assertEquals(1, room.getRoomId());
		assertEquals("Living Room", room.getName());
		assertEquals(25.5, room.getArea(), 0.01);
	}

	@Test
	public void testHouseholdUserManagement() {
		// 测试添加用户
		assertEquals(3, system.findHouseholdById(1).getMemberships().size());

		// 测试移除用户
		system.findHouseholdById(1).removeUser(system.findUserById(2));
		assertEquals(2, system.findHouseholdById(1).getMemberships().size());

		// 测试获取用户
		User user = system.findHouseholdById(1).getMemberships().get(0).getUser();
		assertEquals(0, user.getUserId());
		assertEquals("管理员", user.getUserName());
	}

	@Test
	public void testHouseholdAutomationSceneManagement() {
		// 测试添加自动化场景
		assertEquals(1, system.findHouseholdById(1).getAutoScenes().size());

		// 测试移除自动化场景
		system.findHouseholdById(1).removeAutoScene(1);
		assertEquals(0, system.findHouseholdById(1).getAutoScenes().size());

		// 测试获取自动化场景
		system.findHouseholdById(1).addAutoScene(eveningScene);
		AutomationScene scene = system.findHouseholdById(1).getAutoScenes().get(0);
		assertEquals(1, scene.getSceneId());
		assertEquals("晚安模式", scene.getName());
	}

	@Test
	public void testHouseholdListAllDevices() {
		List<Device> devices = system.findHouseholdById(1).listAllDevices();
		assertEquals(3, devices.size()); // AC, Light, Lock

		// 验证设备类型
		boolean hasAC = false;
		boolean hasLight = false;
		boolean hasLock = false;

		for (Device device : devices) {
			if (device instanceof AirConditioner)
				hasAC = true;
			if (device instanceof LightBulb)
				hasLight = true;
			if (device instanceof SmartLock)
				hasLock = true;
		}

		assertTrue(hasAC);
		assertTrue(hasLight);
		assertTrue(hasLock);
	}

	@Test
	public void testRoomDeviceManagement() {
		// 测试添加设备
		assertEquals(2, livingRoom.getDevices().size());

		// 测试移除设备
		livingRoom.removeDevice(2);
		assertEquals(1, livingRoom.getDevices().size());

		// 测试获取设备
		Device device = livingRoom.getDevices().get(0);
		assertEquals(1, device.getDeviceId());
		assertEquals("Living Room AC", device.getName());
	}

	@Test
	public void testDevicePowerManagement() {
		// 测试设备电源状态
		assertFalse(ac.isPowerStatus());

		// 测试开机
		ac.powerOn();
		assertTrue(ac.isPowerStatus());

		// 测试关机
		ac.powerOff();
		assertFalse(ac.isPowerStatus());
	}

	@Test
	public void testAirConditionerTemperature() {
		// 测试当前温度
		assertEquals(25.0, ac.getCurrTemp(), 0.01);

		// 测试目标温度设置
		ac.setTargetTemp(20.0);
		assertEquals(20.0, ac.getTargetTemp(), 0.01);
	}

	@Test
	public void testLightBulbSettings() {
		// 测试亮度
		assertEquals(80, light.getBrightness());

		// 测试色温
		assertEquals(4000, light.getColorTemp());

		// 测试设置亮度
		light.setBrightness(50);
		assertEquals(50, light.getBrightness());

		// 测试设置色温
		light.setColorTemp(3000);
		assertEquals(3000, light.getColorTemp());
	}

	@Test
	public void testSmartLock() {
		// 测试锁状态
		assertTrue(lock.isLocked());

		// 测试解锁
		lock.setLocked(false);
		assertFalse(lock.isLocked());

		// 测试电池电量
		assertEquals(0, lock.getBatteryLevel()); // 初始值为0
		lock.setBatteryLevel(75);
		assertEquals(75, lock.getBatteryLevel());
	}

	@Test
	public void testBathroomScale() {
		// 测试体重
		assertEquals(70.5, scale.getBodyMass(), 0.01);

		// 测试电池电量
		assertEquals(85, scale.getBatteryLevel());

		// 测试设置体重
		scale.setBodyMass(72.0);
		assertEquals(72.0, scale.getBodyMass(), 0.01);

		// 测试设置电池电量
		scale.setBatteryLevel(90);
		assertEquals(90, scale.getBatteryLevel());
	}

	@Test
	public void testEnergyReporting() {
		// 测试能源报告接口
		assertTrue(ac instanceof EnergyReporting);
		assertTrue(light instanceof EnergyReporting);
		assertFalse(lock instanceof EnergyReporting);
		assertFalse(scale instanceof EnergyReporting);

		// 测试获取功率
		ac.powerOn();
		double acPower = ((EnergyReporting) ac).getPower();
		assertTrue(acPower > 0);

		light.powerOn();
		double lightPower = ((EnergyReporting) light).getPower();
		assertTrue(lightPower > 0);

		// 测试能源报告
		Date startTime = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000); // 24小时前
		Date endTime = new Date(); // 现在

		double acEnergy = ((EnergyReporting) ac).getReport(startTime, endTime);
		double lightEnergy = ((EnergyReporting) light).getReport(startTime, endTime);

		assertTrue(acEnergy >= 0);
		assertTrue(lightEnergy >= 0);
	}

	@Test
	public void testAutomationScene() {
		// 测试场景属性
		assertEquals(1, eveningScene.getSceneId());
		assertEquals("晚安模式", eveningScene.getName());
		assertEquals("关闭灯光调节至舒适温度", eveningScene.getDescription());

		// 测试场景动作
		List<DeviceAction> actions = eveningScene.getActions();
		assertEquals(2, actions.size());
		
		//测试自动触发器
		List<Trigger> triggers = eveningScene.getTriggers();
		assertEquals(1, triggers.size());

		// 测试手动触发场景
		eveningScene.manualTrig(); // 应该执行两个动作
	}

	@Test
	public void testDeviceAction() {
		// 创建设备动作
		DeviceAction action = new DeviceAction("setTemperature", "20.0", ac);

		// 测试动作属性
		assertEquals("setTemperature", action.getCommand());
		assertEquals("20.0", action.getParameters());
		assertEquals(ac, action.getDevice());

		// 测试执行动作
		action.execute(); // 应该设置空调目标温度为20.0
		assertEquals(20.0, ac.getTargetTemp(), 0.01);
	}

	@Test
	public void testManufacturer() {
		// 测试制造商属性
		assertEquals(1, acManufacturer.getManufacturerId());
		assertEquals("AC Corp", acManufacturer.getName());
		assertEquals("WiFi, ZigBee", acManufacturer.getProtocols());

		// 测试设备管理
		assertEquals(0, acManufacturer.getDevices().size()); // 初始为空
		acManufacturer.addDevice(ac);
		assertEquals(1, acManufacturer.getDevices().size());

		acManufacturer.removeDevice(ac);
		assertEquals(0, acManufacturer.getDevices().size());
	}

	@Test
	public void testRunningLog() {
		// 创建运行日志
		Date now = new Date();
		RunningLog log = new RunningLog(now, "Device turned on", RunningLog.Type.INFO, "User initiated");

		// 测试日志属性
		assertEquals(now, log.getDateTime());
		assertEquals("Device turned on", log.getEvent());
		assertEquals(RunningLog.Type.INFO, log.getType());
		assertEquals("User initiated", log.getNote());
	}

	@Test
	public void testHomeSphereSystem() {
		// 测试系统功能
		system.login("admin", "admin"); // 应该打印登录消息
		
		assertEquals(0, system.getCurrentUser().getUserId());

		// 测试能源报告
		Date startTime = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000); // 24小时前
		Date endTime = new Date(); // 现在
		system.displayEnergyReportings(1, startTime, endTime); // 应该打印能源报告

		// 测试手动触发场景
		system.manualTrigSceneById(1, 1); // 应该触发场景

		system.logoff(); // 应该打印登出消息
		assertNull(system.getCurrentUser());
	}

}