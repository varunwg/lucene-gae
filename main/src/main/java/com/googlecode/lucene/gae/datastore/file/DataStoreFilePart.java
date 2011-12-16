package com.googlecode.lucene.gae.datastore.file;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataStoreFilePart {

	public static final int	MAX_SIZE	= 5000;

	public static final int	BLOCK_SIZE	= 500;

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

	private boolean			modified;

	public DataStoreFilePart(String name) {
		this.name = name;
	}

	public DataStoreFilePart(String name, boolean isNew) {
		this(name);
		modified = isNew;
	}

	public int getLength() {
		return length;
	}

	public String getName() {
		return name;
	}

	public boolean isModified() {
		return modified;
	}

	public void read(byte[] dest, int begin, int pos, int len) {
		System.arraycopy(bytes, pos, dest, begin, len);
	}

	public void write(byte[] src, int begin, int pos, int len) {

		System.arraycopy(src, begin, bytes, pos, len);

		int newLength = len + pos;

		if (newLength < this.length) {
			newLength = this.length;
		}

		this.length = newLength;

		modified = true;

	}

	int getByteBlocksCount() {
		return (int) (length / BLOCK_SIZE) + 1;
	}

	List<byte[]> getByteBlocks() {

		int count = getByteBlocksCount();

		List<byte[]> blocks = new ArrayList<byte[]>(count);

		for (int i = 0; i < count; i++) {
			int begin = i * BLOCK_SIZE;
			int len = this.length - begin;
			byte[] block = new byte[len];
			System.arraycopy(bytes, begin, block, 0, len);
			blocks.add(block);
		}

		return blocks;

	}

	void setByteBlocks(List<byte[]> blocks) {

		int count = blocks.size();

		Iterator<byte[]> iter = blocks.iterator();

		for (int i = 0; i < count; i++) {
			byte[] block = iter.next();
			int begin = i * BLOCK_SIZE;
			System.arraycopy(block, 0, bytes, begin, block.length);
		}

	}

	void setLength(int length) {
		this.length = length;
	}

}
