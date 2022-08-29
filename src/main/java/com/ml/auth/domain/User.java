package com.ml.auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ml.auth.constants.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * BookWormV2
 * Created on 19/8/22 - Friday
 * User Khan, C M Abdullah
 * Ref:
 */
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
}
