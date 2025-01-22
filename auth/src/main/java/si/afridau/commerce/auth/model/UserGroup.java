package si.afridau.commerce.auth.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import si.afridau.commerce.auth.common.model.BaseModel;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class UserGroup extends BaseModel {
    private UUID userId;
    private UUID groupId;
}
