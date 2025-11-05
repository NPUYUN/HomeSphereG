package DeviceEquipment;

public interface DeviceFactory {
    Device createDevice(int deviceId, String name, String type);
}
