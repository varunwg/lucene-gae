package org.apache.lucene.store.gae.datastore.file;

public class DataStoreFilePart {

	public static final int BYTE_ARRAY_LENGTH = 500;

	private final String name;
	private int length = 0;
	private byte[] bytes = new byte[BYTE_ARRAY_LENGTH];

	public DataStoreFilePart(String name) {
		this.name = name;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public int getLength() {
		return length;
	}

	public String getName() {
		return name;
	}

	public void updateLength(int length) {
		setLength(length);
	}

	void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	void setLength(int length) {
		this.length = length;
	}

}
