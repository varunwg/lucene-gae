package org.apache.lucene.store.gae.datastore;

import org.apache.lucene.store.gae.datastore.file.DataStoreFilePart;

public class DataStoreFileUtils {

	public static final int MAX_SIZE = DataStoreFilePart.BYTE_ARRAY_LENGTH;

	public static byte[] getBytes(byte[] b, int off, int len) {
		byte[] dest = new byte[len];
		System.arraycopy(b, off, dest, 0, len);
		return dest;
	}

	public static int getIndexForPos(long pos) {
		return (int) (pos / MAX_SIZE);
	}

	public static int getStartForPos(long pos) {

		int index = getIndexForPos(pos);

		if (index == 0) {
			return (int) pos;
		} else {
			int init = ((index - 1) * MAX_SIZE) + MAX_SIZE;
			return (int) (pos - init);
		}

	}

	private DataStoreFileUtils() {
	}

}
