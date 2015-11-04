package ch.ututor.controller.pojos;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class ChangePasswordForm {	
	
	@NotBlank(message = "Please enter your actual password!")
	@Size(min = 8, max = 30, message = "Passwords contain between 8 and 30 characters!")
	private String oldPassword;
	
	@Size(min = 8, max = 30, message = "Your password should contain between 8 and 30 characters!")
	private String newPassword;
	
	private String newPasswordRepeat;
	
	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	
	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	public String getNewPasswordRepeat() {
		return newPasswordRepeat;
	}

	public void setNewPasswordRepeat(String newPasswordRepeat) {
		this.newPasswordRepeat = newPasswordRepeat;
	}
}