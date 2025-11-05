package EmissionReduction;

import DeviceEquipment.Device;
import UserAndHousehold.Household;
import UserAndHousehold.Room;

import java.text.SimpleDateFormat;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import java.io.StringWriter;
import java.io.IOException;

public class XmlRunningLogFormatter implements RunningLogFormatter {
    private static volatile XmlRunningLogFormatter instance;

    private XmlRunningLogFormatter() {
        instance = this;
    }

    public static XmlRunningLogFormatter getInstance() {
        if (instance == null) {
            synchronized (XmlRunningLogFormatter.class) {
                if (instance == null) {
                    instance = new XmlRunningLogFormatter();
                }
            }
        }
        return instance;
    }

    /**
     * 将 Household 对象格式化为结构化的 XML 字符串。
     *
     * 该方法将传入的 Household 实例转换为一个包含家庭信息、房间列表、设备列表以及运行日志的 XML 格式字符串。
     * 所有数据均以属性形式表示，不使用子元素存储简单字段。
     * 若在转换过程中发生 IO 异常，则返回错误提示 XML。
     *
     * @param household 要格式化的 Household 实例，不可为 null
     * @return 格式化后的 XML 字符串；若出现异常则返回错误 XML 内容
     */
    @Override
    public String format(Household household) {
        try {
            // 创建Document对象
            Document document = DocumentHelper.createDocument();

            // 创建根元素，使用属性而不是子元素
            Element root = document.addElement("household");
            root.addAttribute("householdId", String.valueOf(household.getHouseholdId()));
            root.addAttribute("address", household.getAddress());

            // 添加房间列表
            Element roomsElement = root.addElement("rooms");

            // 日期格式化
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // 遍历房间并构建 XML 结构
            for (Room room : household.getRooms()) {
                Element roomElement = roomsElement.addElement("room");
                roomElement.addAttribute("roomId", String.valueOf(room.getRoomId()));
                roomElement.addAttribute("roomName", room.getName());

                // 添加设备列表
                Element devicesElement = roomElement.addElement("devices");

                // 遍历设备并构建 XML 结构
                for (Device device : room.getDevices()) {
                    Element deviceElement = devicesElement.addElement("device");
                    deviceElement.addAttribute("deviceId", String.valueOf(device.getDeviceId()));
                    deviceElement.addAttribute("deviceName", device.getName());

                    // 添加运行日志列表
                    Element logsElement = deviceElement.addElement("runningLogs");

                    // 遍历运行日志并构建 XML 结构
                    for (RunningLog log : device.getRunningLogs()) {
                        Element logElement = logsElement.addElement("runningLog");
                        logElement.addAttribute("dateTime", dateFormat.format(log.getDateTime()));
                        logElement.addAttribute("event", log.getEvent());
                        logElement.addAttribute("type", log.getType().toString());
                        logElement.addAttribute("note", log.getNote() != null ? log.getNote() : "");
                    }
                }
            }

            // 使用OutputFormat进行格式化，使XML更加美观
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");

            // 将Document对象转换为字符串
            StringWriter stringWriter = new StringWriter();
            XMLWriter xmlWriter = new XMLWriter(stringWriter, format);
            xmlWriter.write(document);
            xmlWriter.close();

            return stringWriter.toString();
        } catch (IOException e) {
            // 处理异常情况
            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><error>XML格式化失败: " + e.getMessage() + "</error>";
        }
    }

}
