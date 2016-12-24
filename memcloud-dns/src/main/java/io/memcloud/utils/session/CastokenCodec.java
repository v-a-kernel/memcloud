package io.memcloud.utils.session;

import io.downgoon.jresty.commons.security.HexCodec;
import io.downgoon.jresty.commons.security.Md5Codec;

public class CastokenCodec {

	public static String encode(String plain) {
		return HexCodec.b2HEX(Md5Codec.encode(plain.toString().getBytes())).substring(16, 24);
	}
}
