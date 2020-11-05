import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * The class describes a User entity. Currently (according to the audition task), there are only two
 * fields - name and surname of the user, where name is a primary key
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class User {
    private final String name;
    private final String surname;
}
