package murphy.springframework.authservice.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import murphy.springframework.authservice.common.Role;
import org.hibernate.annotations.UuidGenerator;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

	@Id
	@UuidGenerator
	@Column(nullable = false, updatable = false, columnDefinition = "uuid")
	private UUID id;

	@Column(unique = true, nullable = false, updatable = false)
	private String username;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String passwordHash;

	private String firstName;

	private String lastName;

	@Enumerated(EnumType.STRING)
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
			name = "user_entity_roles",
			joinColumns = @JoinColumn(name = "user_entity_id")
	)
	private Set<Role> roles = new HashSet<>();

	// Helper role methods
	public void addRole(Role role) {
		roles.add(role);
	}

	public void removeRole(Role role) {
		roles.remove(role);
	}

	private Boolean active;

	private Instant createdDate;

	private Instant updatedDate;

	@PrePersist
	public void onPrePersist() {

		createdDate = Instant.now();
		updatedDate = Instant.now();
	}

	@PreUpdate
	public void onPreUpdate() {

		updatedDate = Instant.now();
	}

}
