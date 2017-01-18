package exercise.jms;

/**
 * Abstraction layer for the JMS Queue Service.
 * 
 * @author Hugo Leao
 *
 */
public interface JmsQueueService
{
    public void postJmsMessage(String message);
}
