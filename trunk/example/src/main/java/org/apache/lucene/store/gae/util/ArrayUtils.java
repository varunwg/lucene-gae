package org.apache.lucene.store.gae.util;

import java.util.Arrays;

public class ArrayUtils {

	public static String getArray(byte[] b) {

		StringBuilder builder = new StringBuilder();

		builder.append("[");

		for (int i = 0; i < b.length; i++) {
			byte t = b[i];
			builder.append(t).append(",");
		}

		builder.append("]");

		return builder.toString();

	}

	public static String getArray(byte[] b, int offset, int length) {
		return getArray(subArray(b, offset, length));
	}

	public static void insert(byte[] target, byte[] source, int pos) {
		System.arraycopy(source, 0, target, pos, source.length);
	}

	public static void printArray(byte[] b) {
		System.out.println(getArray(b));
	}

	public static void printArray(byte[] b, int offset, int length) {
		printArray(subArray(b, offset, length));
	}

	public static byte[] subArray(byte[] bytes, int offset, int length) {
		return Arrays.copyOfRange(bytes, offset, length);
	}

	public static byte[] addLenght(byte[] bytes, int length) {
		byte[] result = new byte[bytes.length + length];
		System.arraycopy(bytes, 0, result, 0, bytes.length);
		return result;
	}

}
