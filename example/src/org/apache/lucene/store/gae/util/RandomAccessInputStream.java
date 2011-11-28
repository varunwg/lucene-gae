package org.apache.lucene.store.gae.util;

import java.io.ByteArrayInputStream;

public class RandomAccessInputStream extends ByteArrayInputStream {

	public RandomAccessInputStream(byte buf[]) {
		super(buf);
	}

	public RandomAccessInputStream(byte buf[], int offset, int length) {
		super(buf, offset, length);
	}

	public int getPos() {
		return pos;
	}

	public void seek(int pos) {
		this.pos = pos;
	}

}
