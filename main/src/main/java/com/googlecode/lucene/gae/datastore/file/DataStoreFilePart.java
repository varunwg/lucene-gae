package com.googlecode.lucene.gae.datastore.file;

public class DataStoreFilePart {

	public static final int	MAX_SIZE	= 500;

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

	private final String	name;
	private int				length	= 0;
	private byte[]			bytes	= new byte[MAX_SIZE];

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
