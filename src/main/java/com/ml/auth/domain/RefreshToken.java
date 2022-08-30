package com.ml.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

/**
 * Reusable Lib
 * Created on 30/8/22 - Tuesday
 * User Khan, C M Abdullah
 * Ref:
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken extends BaseEntity {
	private String refreshToken;
	@ToString.Exclude
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
}
