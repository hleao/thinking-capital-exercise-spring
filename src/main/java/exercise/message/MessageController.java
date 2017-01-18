package exercise.message;

import javax.inject.Inject;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller for messages REST endpoint
 * 
 * @author Hugo
 *
 */
@Slf4j
@RestController
@EnableAutoConfiguration
public class MessageController
{

    public final MessageProcessor messageProcessor;
    
    @Inject
    public MessageController(MessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

    @RequestMapping(value="/messages", method=RequestMethod.POST)
    public ResponseEntity<MessagePack> postMessagePack(@RequestBody MessagePack messagePack)
    {
        log.debug("New message pack posted");
        
        messageProcessor.processMessages(messagePack);
        
        return new ResponseEntity<>(messagePack, HttpStatus.OK);
    }
}
