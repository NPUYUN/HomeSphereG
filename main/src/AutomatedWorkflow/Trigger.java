package AutomatedWorkflow;

/**
 * Trigger 接口，定义触发器的基本方法
 */
public interface Trigger {
    /**
     * 评估触发器是否激活
     */
    public void evaluate();
}
