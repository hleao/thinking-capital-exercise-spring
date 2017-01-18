package exercise.message;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.experimental.Tolerate;

/**
 * Domain model for MessagePack
 * 
 * @author Hugo
 *
 */
@Data
@Builder
public class MessagePack
{

    @Singular
    private List<String> messages;

    @Tolerate
    public MessagePack() {
    }
}