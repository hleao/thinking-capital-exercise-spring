package exercise.jms;

import javax.inject.Inject;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

/**
 * JMS Queue Service wrapping Spring JMS Template.
 * 
 * @author Hugo
 *
 */
@Service
public class SpringJmsQueueService implements JmsQueueService
{

    private final JmsTemplate jmsTemplate;

    @Inject
    public SpringJmsQueueService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
    
    @Override
    public void postJmsMessage(String message)
    {
        jmsTemplate.convertAndSend("billings", message);
    }
}
