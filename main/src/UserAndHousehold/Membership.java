package UserAndHousehold;

import java.util.Date;
import java.util.Objects;

/**
 * Membership 类表示用户和家庭户之间的关联关系
 *
 */
public class Membership {
    private Date joinDate;
    private String role;
    private User user;
    private Household household;

    /**
     * 创建一个新的 Membership 对象
     *
     * @param joinDate 加入家庭日期
     * @param role 角色，可以是 "owner"、"admin"、"member"
     * @param user 用户对象
     * @param household 家庭对象
     * @throws IllegalArgumentException 当参数无效时抛出异常
     */
    public Membership(Date joinDate, String role, User user, Household household) {
        try{
            if (joinDate == null) {
                throw new IllegalArgumentException("加入日期不能为空");
            }
            if (role == null || role.trim().isEmpty()) {
                throw new IllegalArgumentException("角色不能为空");
            }
            if (user == null) {
                throw new IllegalArgumentException("用户对象不能为空");
            }
            if (household == null) {
                throw new IllegalArgumentException("家庭对象不能为空");
            }

            this.joinDate = new Date(joinDate.getTime()); // 创建副本以防止外部修改
            this.role = role.trim();
            this.user = user;
            this.household = household;
        }
        catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * 获取用户对象
     *
     * @return 用户对象
     */
    public User getUser() {
        return user;
    }

    /**
     * 获取家庭对象
     *
     * @return 家庭对象
     */
    public Household getHousehold() {
        return household;
    }

    /**
     * 设置角色
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * 获取角色
     *
     * @return 角色
     */
    public String getRole() {
        return role;
    }

    /**
     * 获取加入家庭日期
     *
     * @return 加入家庭日期
     */
    public Date getJoinDate() {
        return joinDate;
    }

    /**
     * 比较两个 Membership 对象是否相等
     *
     * @param obj 要比较的对象
     * @return 如果两个对象相等则返回 true，否则返回 false
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Membership that){
            return Objects.equals(user.getUserId(), that.user.getUserId()) &&
                    Objects.equals(household.getHouseholdId(), that.household.getHouseholdId());
        }
        return false;
    }

    /**
     * 重写toString方法，返回用户信息的字符串表示
     * 该方法将用户的基本信息格式化为易于阅读的字符串格式
     * 包含用户ID、登录名、姓名、联系电话和角色信息
     *
     * @return 格式化的用户信息字符串，包含用户的基本信息，各信息项之间用换行符分隔
     */
    @Override
    public String toString() {
        return "ID：" + user.getUserId() + '\n' +
                "用户名：" + user.getLoginName() + '\n' +
                "姓名：" + user.getUserName() + '\n' +
                "联系电话：" + user.getPhoneNumber() + '\n' +
                "角色：" + role;
    }


}
