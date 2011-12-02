package com.googlecode.lucene.gae.datastore.file;

import java.util.ArrayList;
import java.util.List;

public class DataStoreFile {

	private final String					name;

	private final List<DataStoreFilePart>	parts			= new ArrayList<DataStoreFilePart>();

	private long							lastModified	= System.currentTimeMillis();
	private boolean							deleted			= false;

	public DataStoreFile(String name) {
		this.name = name;
	}

	public long available() {
		return parts.size() * DataStoreFilePart.MAX_SIZE;
	}

	public void createNewPart() {

		DataStoreFilePart part = new DataStoreFilePart(this.name + "_" + parts.size(), true);
		parts.add(part);

	}

	public long getLastModified() {
		return lastModified;
	}

	public long getLength() {

		long length = 0;

		for (DataStoreFilePart part : parts) {
			length += part.getLength();
		}

		return length;

	}

	public String getName() {
		return name;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void markToDelete() {
		setDeleted(true);
	}

	public byte[] read(long pos, int len) {

		byte[] bytes = new byte[len];

		long position = pos;

		int begin = 0;
		int end = len;

		while (begin < end) {

			int left = end - begin;
			int start = DataStoreFilePart.getStartForPos(position);
			int canRead = DataStoreFilePart.MAX_SIZE - start;

			int length = left;

			if (canRead < left) {
				length = canRead;
			}

			int index = DataStoreFilePart.getIndexForPos(position);

			DataStoreFilePart part = parts.get(index);
			part.read(bytes, begin, start, length);

			position += length;
			begin += length;

		}

		return bytes;

	}

	public void touch() {
		lastModified = System.currentTimeMillis();
	}

	public long write(byte[] bytes, long pos, int len) {

		long position = pos;

		int begin = 0;
		int end = len;

		while (begin < end) {

			int left = end - begin;
			int start = DataStoreFilePart.getStartForPos(position);
			int canWrite = DataStoreFilePart.MAX_SIZE - start;

			int length = left;

			if (canWrite < left) {
				length = canWrite;
			}

			int index = DataStoreFilePart.getIndexForPos(position);

			DataStoreFilePart part = parts.get(index);
			part.write(bytes, begin, start, length);

			position += length;
			begin += length;

		}

		return end;

	}

	List<DataStoreFilePart> getParts() {
		return parts;
	}

	void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	void setLastModified(long time) {
		this.lastModified = time;
	}

}
