import com.stefjen07.decoder.Decodable;
import com.stefjen07.encoder.Encodable;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Encodable
@Decodable
public class User {
    String name;
    String password;

    public User() {

    }
}
