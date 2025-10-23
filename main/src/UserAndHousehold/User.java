package UserAndHousehold;

import NormalException.RepeatedException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户类，表示一个组织中的用户
 */
public class User {
    private int userId;
    private String loginName;
    private String loginPassword;
    private String userName;
    private String phoneNumber;
    // 使用 LinkedHashMap 存储成员资格，以成员资格ID为键，成员资格对象为值
    public Map<Integer, Membership> memberships = new LinkedHashMap<>();

    /**
     * 获取用户加入组织的所有成员资格
     * @return 用户加入组织的所有成员资格列表
     */
    public List<Membership> getMemberships() {
        // 将Map中的值转换为List返回
        return new ArrayList<>(memberships.values());
    }

    /**
     * 创建一个新的用户对象
     * @param userId 用户ID
     * @param loginName 登录名
     * @param loginPassword 登录密码
     * @param userName 用户名
     * @param phoneNumber 手机号码
     */
    public User(int userId, String loginName, String loginPassword, String userName, String phoneNumber) {
        try{
            if (userId < 0) {
                throw new IllegalArgumentException("用户ID不能为负数");
            }
            if (loginName == null || loginName.trim().isEmpty()) {
                throw new IllegalArgumentException("登录名不能为空");
            }
            if (loginPassword == null || loginPassword.trim().isEmpty()) {
                throw new IllegalArgumentException("登录密码不能为空");
            }
            if (userName == null || userName.trim().isEmpty()) {
                throw new IllegalArgumentException("用户名不能为空");
            }

            this.userId = userId;
            this.loginName = loginName;
            this.loginPassword = loginPassword;
            this.userName = userName;
            this.phoneNumber = phoneNumber != null ? phoneNumber : "";
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * 获取用户ID
     * @return 用户ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * 获取用户登录名
     * @return 用户登录名
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * 获取用户名
     * @return 用户名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 获取用户手机号码
     * @return 用户手机号码
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * 获取用户登录密码
     * @return 用户登录密码
     */
    public String getLoginPassword() {
        return loginPassword;
    }

    /**
     * 添加一个成员资格到用户中
     * @param membership 要添加的成员资格对象
     */
    public void addMembership(Membership membership) {
        try{
            if (membership == null) {
                throw new IllegalArgumentException("成员资格对象不能为空");
            }
            // 如果已存在该成员资格，则不添加
            if (memberships.containsKey(membership.getUser().getUserId())) {
                throw new RepeatedException("该用户已加入组织");
            }

            // 将成员资格添加到映射中，以成员资格ID为键
            memberships.put(membership.getUser().getUserId(), membership);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 从用户中移除一个成员资格
     * @param membership 要移除的成员资格对象
     */
    public void removeMembership(Membership membership) {
        try{
            if(membership == null){
                throw new IllegalArgumentException("成员资格对象不能为空");
            }

            // 从成员资格映射中移除指定的成员资格
            memberships.remove(membership.getUser().getUserId());
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 获取用户信息字符串表示
     * @return 用户信息字符串
     */
    @Override
    public String toString() {
        return "User{userId='" + userId + ", loginName='" + loginName + ", loginPassword='" + loginPassword + ", userName='" + userName + ", phoneNumber='" + phoneNumber + "}";
    }

    /**
     * 判断两个用户对象是否相等(依据id)
     * @param obj 要比较的对象
     * @return 如果两个对象相等则返回true，否则返回false
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof User user){
            return user.userId == userId;
        }
        return false;
    }
}
