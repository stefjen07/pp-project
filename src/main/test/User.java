import com.stefjen07.decoder.Decodable;
import com.stefjen07.encoder.Encodable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Encodable
@Decodable
public class User {
    String name;
    String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return name.equals(user.name) && password.equals(user.password);
    }
}