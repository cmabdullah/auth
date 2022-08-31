package com.ml.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;

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
public class AccessToken extends BaseEntity {
	@Size(max=500)
	private String accessToken;
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;
}
