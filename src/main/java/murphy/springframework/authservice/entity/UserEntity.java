package murphy.springframework.authservice.entity;

import java.time.ZonedDateTime;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;
import murphy.springframework.authservice.common.Role;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity
public class UserEntity {

	@Id
	@UuidGenerator
	private String id;

	@Column(unique = true)
	private String username;

	private String passwordHash;

	private String firstName;

	private String lastName;

	@Enumerated(EnumType.STRING)
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<Role> roles;

	private Boolean active;

	private ZonedDateTime createdDate;

	private ZonedDateTime updatedDate;

	@PrePersist
	public void onPrePersist() {

		createdDate = ZonedDateTime.now();
		updatedDate = ZonedDateTime.now();
	}

	@PreUpdate
	public void onPreUpdate() {

		updatedDate = ZonedDateTime.now();
	}
}
