package org.apache.lucene.store.gae.datastore;

import java.io.IOException;

import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.gae.datastore.file.DataStoreFile;
import org.apache.lucene.store.gae.datastore.file.DataStoreFilePart;
import org.apache.lucene.store.gae.datastore.file.DataStoreFileRepository;

public class DataStoreFileIndexOutput extends IndexOutput {

	private final DataStoreFileRepository repository;
	private final DataStoreFile file;

	private long pos;
	private long length;

	public DataStoreFileIndexOutput(DataStoreFile file, DataStoreFileRepository repository)
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

		// extend file
		while (newcount > file.available()) {
			file.createPart();
		}

		byte[] src = DataStoreFileUtils.getBytes(b, off, len);
		long writed = writeInFile(src);

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

	private boolean isInvalid(byte[] b, int off, int len) {
		return (off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length)
				|| ((off + len) < 0);
	}

	private long writeInFile(byte[] src) {

		long position = pos;

		int begin = 0;
		int end = src.length;

		while (begin < end) {

			int left = end - begin;
			int start = DataStoreFileUtils.getStartForPos(position);
			int canWrite = DataStoreFileUtils.MAX_SIZE - start;

			int length = left;

			if (canWrite < left) {
				length = canWrite;
			}

			int index = DataStoreFileUtils.getIndexForPos(position);
			DataStoreFilePart part = file.getPart(index);
			byte[] dest = part.getBytes();

			System.arraycopy(src, begin, dest, start, length);

			int newLength = length + start;

			if (newLength < part.getLength()) {
				newLength = part.getLength();
			}

			part.updateLength(newLength);
			position += length;
			begin += length;

		}

		return end;

	}

}
