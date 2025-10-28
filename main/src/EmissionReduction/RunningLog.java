package EmissionReduction;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * RunningLog 类用于记录运行日志，包括时间、事件、类型和备注等信息
 */
public class RunningLog {
    private Date dateTime;
    private String event;
    private Type type;
    private String note;

    /**
     * 构造函数，用于创建一个新的运行日志对象
     * @param dateTime 日志时间
     * @param event 日志事件
     * @param type 日志类型
     * @param note 日志备注
     */
    public RunningLog(Date dateTime, String event, Type type, String note) {
        this.dateTime = dateTime;
        this.event = event;
        this.type = type;
        this.note = note;
    }

    /**
     * 日志类型枚举类，包含三种类型：INFO、WARNING 和 ERROR
     */
    public static enum Type {
        INFO, WARNING, ERROR
    }

    /**
     * 获取日志时间
     * @return 日志时间
     */
    public Date getDateTime() {
        return dateTime;
    }

    /**
     * 获取日志事件
     * @return 日志事件
     */
    public String getEvent() {
        return event;
    }

    /**
     * 获取日志类型
     * @return 日志类型
     */
    public Type getType() {
        return type;
    }

    /**
     * 获取日志备注
     * @return 日志备注
     */
    public String getNote() {
        return note;
    }

    /**
     * 重写 toString 方法，返回日志信息
     * @return 日志信息
     */
    @Override
    public String toString() {
        // 将 LocalDate 转换为 Date 格式进行显示
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(dateTime);
        return "RunningLog{" +
                "dateTime=" + date +
                ", event='" + event  +
                ", type=" + type +
                ", note='" + note  +
                '}';
    }

}
