package pl.mt.cookbook.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import pl.mt.cookbook.recipe.Recipe;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String nickname;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    @Column(unique = true)
    private String email;

    private String password;

    private boolean newsletter;

    @OneToMany(mappedBy = "user",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private Set<UserRole> roles;

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private List<Recipe> recipe;
}
