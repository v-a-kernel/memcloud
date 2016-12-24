package io.memcloud.utils;

import io.memcloud.utils.session.PasswordCodec;

public class PwdResetDemo {

	public static void main(String[] args) {
		String pwdPlain = "123456";
		// String pwdEncry = MD5.MD5Encode(pwdPlain);
		String pwdEncry = PasswordCodec.encode(pwdPlain);
		System.out.println(pwdEncry);
	}

}
