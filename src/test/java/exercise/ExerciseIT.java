package exercise;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Sets;

import exercise.message.MessagePack;
import lombok.Getter;
import lombok.Setter;

/**
 * Integration test for Message system. This test will post a set of messages to
 * the REST endpoint and check if the correct messages were forwarded to the JMS
 * queue.
 * 
 * @author Hugo
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = Application.class)
public class ExerciseIT
{
    private static final String MESSAGE_ENDPOINT_URL = "http://localhost:8080/messages";

    private static final Collection<String> BILLING_MESSAGES = 
            Sets.newHashSet("billing", "billing-with-suffix", "prefixed-billing", "prefixed-billing-with-suffix");
    private static final Collection<String> OTHER_MESSAGES = 
            Sets.newHashSet("other-message-1", "other-message-2");

    @Autowired
    private ReceiverMock receiverMock;

    private RestTemplate restTemplate = new RestTemplate();

    /**
     * Tests that all billing messages should be 
     */
    @Test
    public void billing_messaged_should_be_posted_to_jms() throws InterruptedException
    {
        // Given
        MessagePack messagePack = buildMessages();
        receiverMock.setCountDownLatch(new CountDownLatch(BILLING_MESSAGES.size()));

        // When
        postMessage(messagePack);
        receiverMock.getCountDownLatch().await(2000, TimeUnit.MILLISECONDS);

        // Then
        Assert.assertTrue(receiverMock.receivedMessages +
                " should contain all items from " + BILLING_MESSAGES,
                receiverMock.receivedMessages.containsAll(BILLING_MESSAGES));
    }

    @Test
    public void non_billing_messaged_should_not_be_posted_to_jms() throws InterruptedException
    {
        // Given
        MessagePack messagePack = buildMessages();
        receiverMock.setCountDownLatch(new CountDownLatch(BILLING_MESSAGES.size()));
        
        // When
        postMessage(messagePack);
        receiverMock.countDownLatch.await(2000, TimeUnit.MILLISECONDS);
        
        // Then
        Assert.assertFalse(receiverMock.receivedMessages +
                " should not contain any items from " + OTHER_MESSAGES,
                receiverMock.receivedMessages.containsAll(OTHER_MESSAGES));
    }
    
    private MessagePack buildMessages()
    {
        MessagePack messagePack = MessagePack.builder()
                .messages(OTHER_MESSAGES)
                .messages(BILLING_MESSAGES)
                .build();
        return messagePack;
    }

    private void postMessage(MessagePack messagePack) {
        restTemplate.postForObject(MESSAGE_ENDPOINT_URL, messagePack, MessagePack.class);        
    }
    
    /**
     * Mocked JMS receiver.
     */
    @Component
    @Singleton
    public static class ReceiverMock
    {

        @Getter
        private Collection<String> receivedMessages = new ConcurrentLinkedQueue<>();
        
        /** Control for async jms calls. The tests needs to be notified when all jms messages have been received */
        @Getter
        @Setter
        private CountDownLatch countDownLatch;

        @JmsListener(destination = "billings")
        public void receiveMessage(String message)
        {
            receivedMessages.add(message);
            countDownLatch.countDown();
        }

    }
}
