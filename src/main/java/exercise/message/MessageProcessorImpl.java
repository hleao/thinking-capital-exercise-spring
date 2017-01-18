package exercise.message;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import exercise.jms.JmsQueueService;

/**
 * Processor responsible for handling messages. This class will filter all
 * billing messages and post them using the Jms Queue Service.
 * 
 * @author Hugo
 *
 */
@Service
public class MessageProcessorImpl implements MessageProcessor
{

    private static final String MESSAGE_FILTER_TEXT = "billing";

    private final JmsQueueService jmsService;
    
    @Inject
    public MessageProcessorImpl(JmsQueueService jmsService) {
        this.jmsService = jmsService;
    }

    @Override
    public void processMessages(MessagePack messagePack)
    {
        Preconditions.checkArgument(messagePack.getMessages() != null,
                "MessagePack.messages is null. Cannot process MessagePack with invalid messages");

        messagePack.getMessages().parallelStream()
                .filter(m -> m.toLowerCase().contains(MESSAGE_FILTER_TEXT))
                .forEach(jmsService::postJmsMessage);
    }
}
