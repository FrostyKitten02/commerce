package si.afridau.commerce.auth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import si.afridau.commerce.auth.common.model.BaseModel;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "group_tbl")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Group extends BaseModel {
    private String name;
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "group_role",
            joinColumns = @JoinColumn(name = "groupId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    private Set<Role> roles = new HashSet<>();
}
