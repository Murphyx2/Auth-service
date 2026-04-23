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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import murphy.springframework.authservice.common.ApiKeyScope;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "api_keys")
public class ApiKeyEntity {

	@Id
	@UuidGenerator
	@Column(nullable = false, updatable = false, columnDefinition = "uuid")
	private UUID id;

	@Column(nullable = false)
	private String appName;

	@Column(nullable = false, unique = true)
	private String keyHash;

	@Column(nullable = false)
	private String keyPrefix;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(nullable = false)
	private boolean active;

	@Enumerated(EnumType.STRING)
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "api_keys_scope", joinColumns = @JoinColumn(name = "api_keys_id"))
	private Set<ApiKeyScope> scopes = new HashSet<>();

	private Integer rateLimit;

	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	@Column(nullable = false, updatable = false)
	private Instant updatedAt;

	@Column(nullable = false, updatable = false)
	private Instant expireAt;

	@Column(nullable = false, updatable = false)
	private Instant revokedAt;

	private String revokedReason;

	@Column(nullable = false, updatable = false)
	private Instant lastUsedAt;

	@Column(nullable = false, updatable = false)
	private Instant lastUpdatedAt;

	@Column(nullable = false, updatable = false)
	private UUID createdBy;

	public void addScope(ApiKeyScope scope) {
		this.scopes.add(scope);
	}

	@PrePersist
	public void onPrePersist() {
		this.createdAt = Instant.now();
		this.updatedAt = Instant.now();
	}

	@PreUpdate
	public void onPreUpdate() {
		this.updatedAt = Instant.now();
	}

	public void removeScope(ApiKeyScope scope) {
		this.scopes.remove(scope);
	}

}
