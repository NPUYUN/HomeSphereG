package EmissionReduction;

import DeviceEquipment.Device;
import UserAndHousehold.Household;
import UserAndHousehold.Room;

import java.text.SimpleDateFormat;

public class HtmlRunningLogFormatter implements RunningLogFormatter {
    private static volatile HtmlRunningLogFormatter instance;

    private HtmlRunningLogFormatter() {
        instance = this;
    }

    public static HtmlRunningLogFormatter getInstance() {
        if (instance == null) {
            synchronized (HtmlRunningLogFormatter.class){
                instance = new HtmlRunningLogFormatter();
            }
        }
        return instance;
    }

    /**
     * 将指定的家庭信息格式化为HTML格式的字符串。
     *
     * @param household 需要格式化的家庭对象，包含房间、设备及运行日志等信息
     * @return 格式化后的HTML字符串，展示家庭结构及其设备运行日志
     */
    @Override
    public String format(Household household) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder htmlBuilder = new StringBuilder();

        // 构建HTML文档的基本结构和样式，并输出家庭基本信息
        htmlBuilder.append("<!DOCTYPE html>")
                 .append("<html>")
                 .append("<head>")
                 .append("<meta charset=\"UTF-8\">")
                 .append("<style>")
                 .append("table, th, td { border: 1px solid black; border-collapse: collapse; }")
                 .append("</style>")
                 .append("</head>")
                 .append("<body>")
                 .append("<p>智能家居生态系统\"HomeSphereG\" v3.0</p>")
                 .append("<p>householdId：").append(household.getHouseholdId()).append("， address：").append(household.getAddress()).append("</p>")
                 .append("<ul>");

        // 遍历所有房间并输出房间信息
        for (Room room : household.getRooms()) {
            htmlBuilder.append("<li>roomId：").append(room.getRoomId()).append("， roomName：").append(room.getName()).append("</li>");

            // 遍历房间内所有设备并输出设备信息及运行日志
            for (Device device : room.getDevices()) {
                htmlBuilder.append("<table>")
                         .append("<tr>")
                         .append("<th>deviceId</th>")
                         .append("<td>").append(device.getDeviceId()).append("</td>")
                         .append("<th>deviceName</th>")
                         .append("<td>").append(device.getName()).append("</td>")
                         .append("</tr>")
                         .append("<tr>")
                         .append("<th colspan=\"4\">runningLogs：</th>")
                         .append("</tr>")
                         .append("<td colspan=\"4\">")
                         .append("<ul>");

                // 遍历设备的所有运行日志并格式化输出
                for (RunningLog log : device.getRunningLogs()) {
                    htmlBuilder.append("<li>").append(dateFormat.format(log.getDateTime()))
                             .append(", ").append(log.getEvent())
                             .append(", ").append(log.getType())
                             .append(", ").append(log.getNote() != null ? log.getNote() : "")
                             .append("</li>");
                }

                htmlBuilder.append("</ul>")
                         .append("</td>")
                         .append("</table>")
                         .append("</p>");
            }
            htmlBuilder.append("</p>");
        }

        // 完成HTML文档结构
        htmlBuilder.append("</ul>")
                 .append("</body>")
                 .append("</html>");

        return htmlBuilder.toString();
    }

}
