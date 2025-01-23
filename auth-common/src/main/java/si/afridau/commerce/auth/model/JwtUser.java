package si.afridau.commerce.auth.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class JwtUser {
    private UUID id;
    private String firstname;
    private String lastname;
    private String email;

    public JwtUser(User userDetails) {
        this.id = userDetails.getId();
        this.firstname = userDetails.getFirstname();
        this.lastname = userDetails.getLastname();
        this.email = userDetails.getEmail();
    }
}
