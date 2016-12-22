package com.sohu.tv.utils;

public class PwdResetDemo {

	public static void main(String[] args) {
		String pwdPlain = "123456";
		String pwdEncry = MD5.MD5Encode(pwdPlain);
		System.out.println(pwdEncry);
	}

}
