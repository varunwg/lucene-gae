package com.googlecode.lucene.gae.datastore;

import java.io.IOException;

import org.apache.lucene.store.IndexOutput;

import com.googlecode.lucene.gae.datastore.file.DataStoreFile;
import com.googlecode.lucene.gae.datastore.file.DataStoreFileRepository;

public class DataStoreIndexOutput extends IndexOutput {

	private final DataStoreFileRepository	repository;
	private final DataStoreFile				file;

	private long							pos;
	private long							length;

	public DataStoreIndexOutput(DataStoreFile file, DataStoreFileRepository repository)
			throws IOException {
		this.file = file;
		this.repository = repository;
		this.length = file.getLength();
	}

	@Override
	public void close() throws IOException {
		repository.put(file);
	}

	@Override
	public void flush() throws IOException {
		// nada
	}

	@Override
	public long getFilePointer() {
		return pos;
	}

	@Override
	public long length() {
		return length;
	}

	@Override
	public void seek(long pos) throws IOException {
		this.pos = pos;
	}

	@Override
	public void writeByte(byte b) throws IOException {
		writeBytes(new byte[] { b }, 0, 1);
	}

	@Override
	public void writeBytes(byte[] b, int off, int len) throws IOException {

		if (isInvalid(b, off, len)) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return;
		}

		long newcount = calculateNewCount(len);

		while (newcount > file.available()) {
			file.createNewPart();
		}

		byte[] src = getBytes(b, off, len);
		long writed = file.write(src, pos, len);

		length = newcount;
		pos += writed;

	}

	private long calculateNewCount(int len) {

		long newcount = 0;

		if (pos == length) {
			newcount = length + len;
		} else if (pos < length) {
			long dif = length - pos;
			if (dif < length) {
				newcount = length;
			} else {
				newcount = length + len - dif;
			}
		} else if (pos > length) {
			long dif = pos = length;
			newcount = length + len + dif;
		}

		return newcount;

	}

	private byte[] getBytes(byte[] b, int off, int len) {
		byte[] dest = new byte[len];
		System.arraycopy(b, off, dest, 0, len);
		return dest;
	}

	private boolean isInvalid(byte[] b, int off, int len) {
		return (off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length)
				|| ((off + len) < 0);
	}

}
