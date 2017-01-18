package exercise.message;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import exercise.jms.JmsQueueService;
import lombok.Getter;

/**
 * Unit test for message processor. This will test that the correct filtering is
 * done.
 * 
 * @author Hugo
 *
 */
public class MessageProcessorTest
{

    @Test
    public void should_filter_billing_messages()
    {
        // Given
        MessagePack messagePack = MessagePack.builder()
                .message("message")
                .message("billing")
                .build();

        MockedJmsQueueService mockedJmsService = new MockedJmsQueueService();
        MessageProcessorImpl sut = new MessageProcessorImpl(mockedJmsService);

        // When
        sut.processMessages(messagePack);

        // Then
        Assert.assertTrue(mockedJmsService.getPostedMessages().contains("billing"));
        Assert.assertFalse(mockedJmsService.getPostedMessages().contains("message"));
    }

    @MockBean(classes=JmsQueueService.class)
    public static class MockedJmsQueueService implements JmsQueueService
    {
        @Getter
        private Collection<String> postedMessages = new ConcurrentLinkedQueue<>();

        
        @Override
        public void postJmsMessage(String message)
        {
            postedMessages.add(message);
        }
    }
}
