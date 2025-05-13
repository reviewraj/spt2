package com.tekpyramid.sp.entity;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tekpyramid.sp.enums.Status;
import com.tekpyramid.sp.utility.AuditableDocument;

import lombok.Data;

@Data
@Document
public class User extends AuditableDocument{
	@Id
	private String userid;

	private String userName;
	@Indexed(unique = true)
	private String email;

	private String password;

	private String phoneNo;
	@DBRef
	private Privilege privilege;
	@DBRef
	private Role role;

	private Status status = Status.PENDING;
	public User() {

	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email) && Objects.equals(password, other.password)
				&& Objects.equals(phoneNo, other.phoneNo) && Objects.equals(privilege, other.privilege)
				&& Objects.equals(role, other.role) && status == other.status
				&& Objects.equals(userName, other.userName) && Objects.equals(userid, other.userid);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(email, password, phoneNo, privilege, role, status, userName, userid);
		return result;
	}

}
