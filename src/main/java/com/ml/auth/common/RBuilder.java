package com.ml.auth.common;

import org.apache.commons.lang3.StringUtils;

/*
 * @created 20/06/2021 - 12:08 PM
 * @author C M Abdullah Khan
 * REF:
 */
public class RBuilder {
	private static final String ROLE_PREFIX = "ROLE_";
	private static final String START = "hasRole('";
	private static final String END = "')";
	private static final String COMMA = ", ";
	private static final String DEFAULT = "hasRole('ROLE_USER')";
	private static final String USER = "ROLE_USER";
	private static final String ADMIN = "ROLE_ADMIN";
	private static final String SUPER_ADMIN = "ROLE_SUPER_ADMIN";
	private static final String EMPLOYEE = "ROLE_EMPLOYEE";
	private StringBuilder sb;

	public RBuilder() {
		sb = new StringBuilder();
	}

	public RBuilder start() {
		sb.append(START);
		return this;
	}

	public RBuilder comma() {
		sb.append(COMMA);
		return this;
	}

	public RBuilder user() {
		sb.append(USER);
		return this;
	}
	public RBuilder admin() {
		sb.append(ADMIN);
		return this;
	}
	public RBuilder superAdmin() {
		sb.append(SUPER_ADMIN);
		return this;
	}
	public RBuilder employee() {
		sb.append(EMPLOYEE);
		return this;
	}

	public RBuilder end() {
		sb.append(END);
		return this;
	}

	public String build() {
		String roles = sb.toString();
		return StringUtils.isNotBlank(roles) ? roles : DEFAULT;
	}
}
