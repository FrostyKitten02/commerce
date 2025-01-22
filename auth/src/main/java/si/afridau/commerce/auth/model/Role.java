package si.afridau.commerce.auth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import si.afridau.commerce.auth.common.model.BaseModel;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Role extends BaseModel implements GrantedAuthority {
    private String name;
    private String description;

    @Override
    public String getAuthority() {
        return name;
    }
}
