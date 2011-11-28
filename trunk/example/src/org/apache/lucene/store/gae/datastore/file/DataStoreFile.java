package org.apache.lucene.store.gae.datastore.file;

import java.util.ArrayList;
import java.util.List;

public class DataStoreFile {

	private final String name;

	private List<DataStoreFilePart> parts = new ArrayList<DataStoreFilePart>();

	private long lastModified = System.currentTimeMillis();
	private boolean deleted = false;

	public DataStoreFile(String name) {
		this.name = name;
	}

	public long available() {
		return parts.size() * DataStoreFilePart.BYTE_ARRAY_LENGTH;
	}

	public void createPart() {

		DataStoreFilePart part = new DataStoreFilePart(this.name + "_" + parts.size());
		parts.add(part);

	}

	public void delete() {
		setDeleted(true);
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

	public DataStoreFilePart getPart(int index) {
		return parts.get(index);
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void updateLastModified(long now) {
		setLastModified(now);
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

	void setParts(List<DataStoreFilePart> parts) {
		this.parts = parts;
	}

}
