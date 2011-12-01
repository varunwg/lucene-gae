/*
 * %W% %E%
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.apache.lucene.store.gae.util;

import java.io.*;
import java.util.Arrays;

public class RandomAccessOutputStream extends OutputStream {

	private byte	buf[];
	private int		count;
	private int		pos;

	public RandomAccessOutputStream() {
		this(1024);
	}

	public RandomAccessOutputStream(byte[] buf) {
		this.buf = buf;
	}

	public RandomAccessOutputStream(int size) {
		if (size < 0) {
			throw new IllegalArgumentException("Negative initial size: " + size);
		}
		buf = new byte[size];
	}

	public void close() throws IOException {
	}

	public int getPos() {
		return pos;
	}

	public void reset() {
		count = 0;
		pos = 0;
	}

	public void seek(int pos) {
		this.pos = pos;
	}

	public int size() {
		return count;
	}

	public byte toByteArray()[] {
		return Arrays.copyOf(buf, count);
	}

	public String toString() {
		return new String(buf, 0, count);
	}

	@Deprecated
	public String toString(int hibyte) {
		return new String(buf, hibyte, 0, count);
	}

	public String toString(String charsetName)
			throws UnsupportedEncodingException {
		return new String(buf, 0, count, charsetName);
	}

	/**
	 * Writes <code>len</code> bytes from the specified byte array
	 * starting at offset <code>off</code> to this byte array output stream.
	 * 
	 * @param b
	 *            the data.
	 * @param off
	 *            the start offset in the data.
	 * @param len
	 *            the number of bytes to write.
	 */
	public void write(byte b[], int off, int len) {

		if ((off < 0) || (off > b.length) || (len < 0) ||
				((off + len) > b.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return;
		}

		int newcount = 0;

		if (pos == count) {
			newcount = count + len;
		} else if (pos < count) {
			int dif = count - pos;
			if (dif < count) {
				newcount = count;
			} else {
				newcount = count + len - dif;
			}
		} else if (pos > count) {
			int dif = pos = count;
			newcount = count + len + dif;
		}

		if (newcount > buf.length) {
			buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
		}

		System.arraycopy(b, off, buf, pos, len);
		count = newcount;
		pos += len;

	}

	@Override
	public void write(int b) throws IOException {
		byte value = Integer.valueOf(b).byteValue();
		write(new byte[] { value }, 0, 1);
	}

	public void writeTo(OutputStream out) throws IOException {
		out.write(buf, 0, count);
	}

}
