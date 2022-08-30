package com.ml.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

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
	
	private String accessToken;
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;
}
