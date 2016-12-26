package io.memcloud.cas.session;

import io.downgoon.jresty.commons.security.HexCodec;
import io.downgoon.jresty.commons.security.Md5Codec;

public class PasswordCodec {

	public static String encode(String pwd) {
		return HexCodec.b2HEX(Md5Codec.encode(pwd.getBytes()));
	}
}
