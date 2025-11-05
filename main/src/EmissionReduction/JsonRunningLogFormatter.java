package EmissionReduction;

import DeviceEquipment.Device;
import UserAndHousehold.Household;
import UserAndHousehold.Room;

import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.util.ArrayList;

public class JsonRunningLogFormatter implements RunningLogFormatter {
    private static volatile JsonRunningLogFormatter instance;

    private JsonRunningLogFormatter() {
        instance = this;
    }

    public static JsonRunningLogFormatter getInstance() {
        if (instance == null) {
            synchronized (JsonRunningLogFormatter.class){
                instance = new JsonRunningLogFormatter();
            }
        }
        return instance;
    }

    /**
     * 将 Household 对象格式化为 JSON 字符串
     *
     * @param household 需要格式化的 Household 对象，包含家庭信息、房间列表、设备列表和运行日志
     * @return 格式化后的 JSON 字符串，包含完整的家庭层级结构信息
     */
    @Override
    public String format(Household household) {
        // 创建 household JSON 对象
        JSONObject householdJson = new JSONObject();
        householdJson.put("householdId", household.getHouseholdId());
        householdJson.put("address", household.getAddress());

        // 创建 rooms 数组
        List<JSONObject> roomList = new ArrayList<>();
        for (Room room : household.getRooms()) {
            JSONObject roomJson = new JSONObject();
            roomJson.put("roomId", room.getRoomId());
            roomJson.put("name", room.getName());

            // 创建 devices 数组
            List<JSONObject> deviceList = new ArrayList<>();
            for (Device device : room.getDevices()) {
                JSONObject deviceJson = new JSONObject();
                deviceJson.put("deviceId", device.getDeviceId());
                deviceJson.put("devicename", device.getName());

                // 创建 runningLogs 数组
                List<JSONObject> logList = new ArrayList<>();
                for (RunningLog log : device.getRunningLogs()) {
                    JSONObject logJson = new JSONObject();
                    logJson.put("dateTime", log.getDateTime());
                    logJson.put("event", log.getEvent());
                    logJson.put("type", log.getType());
                    logJson.put("note", log.getNote());
                    logList.add(logJson);
                }
                deviceJson.put("runningLogs", logList);
                deviceList.add(deviceJson);
            }
            roomJson.put("devices", deviceList);
            roomList.add(roomJson);
        }
        householdJson.put("rooms", roomList);

        // 返回格式化的 JSON 字符串
        return JSON.toJSONString(householdJson);
    }

}
