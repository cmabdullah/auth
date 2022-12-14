package com.ml.auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ml.auth.constants.AuthProvider;
import com.ml.auth.constants.UserStatus;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * BookWormV2
 * Created on 19/8/22 - Friday
 * User Khan, C M Abdullah
 * Ref:
 */
@Getter
@Setter
@Entity
@Table(name = "users", uniqueConstraints = {
		@UniqueConstraint(columnNames = "email")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {
	
	//@Column(nullable = false)
	private String name;
	@Email
	@Column(nullable = false)
	private String email;
	@Column(nullable = false)
	private Boolean emailVerified = false;
	@JsonIgnore
	private String password;
	@Size(max=500)
	private String refreshToken;
	@NotNull
	@Enumerated(EnumType.STRING)
	private AuthProvider provider;
	private String providerId;
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Set<Role> roles = new HashSet<>();
	@Enumerated(EnumType.STRING)
	private UserStatus userStatus;
	
	@ToString.Exclude
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	private Set<AccessToken> accessTokens;

	@Column(nullable = false)
	private Instant lastPasswordChangeTime;
	
}
