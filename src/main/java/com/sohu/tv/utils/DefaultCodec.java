package com.sohu.tv.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class DefaultCodec {
	public final static String SUPPORTED_CHARSET_NAME = "UTF-8";	// this is for jdk 1.5 compliance
	public final static Charset SUPPORTED_CHARSET = Charset.forName ("UTF-8");
	
	/**
	 * This helper method is mainly intended for use with a list of
	 * keys returned from Redis, given that it will use the UTF-8
	 * {@link java.nio.charset.Charset } in decoding the byte array.  Typical use would
	 * be to convert from the List<byte[]> output of JRedis.keys()
	 * 
	 * @param bytearray
	 */
	public static final List<String> toStr (List<byte[]> bytearray) {
		if(null == bytearray) return null;
		List<String> list = new ArrayList<String>(bytearray.size());
		for(byte[] b : bytearray) 
			if(null!= b) 
				list.add(toStr(b)); 
			else 
				list.add(null);
		return list;
	}
	/**
	 * @param bytes
	 * @return new {@link String#String(byte[])} or null if bytes is null. 
	 */
	public static final String toStr (byte[] bytes) {
        String str = null;
        if(null != bytes) {
			try {
				str = new String(bytes, SUPPORTED_CHARSET_NAME);
	        }
	        catch (UnsupportedEncodingException e) {
		        e.printStackTrace();
	        }
        }
        return str;
//		return new String(bytes, SUPPORTED_CHARSET); // Java 1.6 only
	}
	
	public static final byte[] encode(String value) {
		byte[] bytes = null;
		try {
	        bytes = value.getBytes(SUPPORTED_CHARSET_NAME);
        }
        catch (UnsupportedEncodingException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
        return bytes;
//		return value.getBytes(SUPPORTED_CHARSET);
	}
		
	public static final Short toShort (byte[]  bytes) {
		if(bytes==null) return null;
		return Short.valueOf(toStr(bytes));
	}
	
	public static final Byte toByte (byte[]  bytes) {
		if(bytes==null) return null;
		return Byte.valueOf(toStr(bytes));
	}	
	
	public static final <T extends Serializable>  
	List<T> decode (List<byte[]> byteList) {
		if(null == byteList) return null;
		List<T>		objectList = new ArrayList<T>(byteList.size());
		for (byte[] bytes : byteList) {
			if(null != bytes){
				T object = (T) decode(bytes);
				objectList.add (object);
			}
			else{
				objectList.add(null);
			}
		}
		return objectList;
	}
	
	public static final <T extends Serializable>  
	T  decode(byte [] bytes) 
	{
		T t = null;
		Exception thrown = null;
		try {
			ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(bytes));
			t = (T) oin.readObject();
		}
		catch (IOException e) {
			e.printStackTrace();
			thrown = e;
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			thrown = e;
		}
		catch (ClassCastException e) {
			e.printStackTrace();
			thrown = e;
		}
		finally {
			if(null != thrown)
				throw new RuntimeException(
						"Error decoding byte[] data to instantiate java object - " +
						"data at key may not have been of this type or even an object", thrown
				);
		}
		return t;
	}	
	
	public static final <T extends Serializable>  byte[]  encode(T obj) {
		byte[] bytes = null;
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bout);
			out.writeObject(obj);
			bytes = bout.toByteArray();
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error serializing object"+obj+" => " + e);
		}
		return bytes;
	}
}