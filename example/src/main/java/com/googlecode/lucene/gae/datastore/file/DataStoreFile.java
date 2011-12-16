package com.googlecode.lucene.gae.datastore.file;

import java.util.ArrayList;
import java.util.List;

public class DataStoreFile {

	private final String					name;

	private final List<DataStoreFilePart>	parts			= new ArrayList<DataStoreFilePart>();

	private long							lastModified	= System.currentTimeMillis();
	private boolean							deleted			= false;
	private long							length			= 0L;

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

		long len = this.length;

		if (!parts.isEmpty()) {

			len = 0;

			for (DataStoreFilePart part : parts) {
				len += part.getLength();
			}

		}

		return len;

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

			int size = left;

			if (canRead < left) {
				size = canRead;
			}

			int index = DataStoreFilePart.getIndexForPos(position);

			DataStoreFilePart part = parts.get(index);
			part.read(bytes, begin, start, size);

			position += size;
			begin += size;

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

			int size = left;

			if (canWrite < left) {
				size = canWrite;
			}

			int index = DataStoreFilePart.getIndexForPos(position);

			DataStoreFilePart part = parts.get(index);
			part.write(bytes, begin, start, size);

			position += size;
			begin += size;

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

	void setLength(long length) {
		this.length = length;
	}

}
