package com.googlecode.lucene.gae.datastore;

import java.io.IOException;

import org.apache.lucene.store.IndexInput;

import com.googlecode.lucene.gae.datastore.file.DataStoreFile;
import com.googlecode.lucene.gae.datastore.file.DataStoreFilePart;

public class DataStoreIndexInput extends IndexInput {

	private final DataStoreFile	file;

	private long				pos;
	private final long			length;

	public DataStoreIndexInput(DataStoreFile file) throws IOException {
		this.file = file;
		this.length = file.getLength();
	}

	@Override
	public void close() throws IOException {
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
	public byte readByte() throws IOException {
		byte[] buf = new byte[1];
		readBytes(buf, 0, 1);
		return buf[0];
	}

	@Override
	public void readBytes(byte[] b, int off, int len) throws IOException {

		if (b == null) {
			throw new NullPointerException();
		} else if (isInvalid(b, off, len)) {
			throw new IndexOutOfBoundsException();
		}

		if (pos >= length) {
			return;
		}
		if (pos + len > length) {
			len = (int) (length - pos);
		}
		if (len <= 0) {
			return;
		}

		byte[] src = readFromFile(len);
		System.arraycopy(src, 0, b, off, len);

		pos += len;

	}

	@Override
	public void seek(long pos) throws IOException {
		this.pos = pos;
	}

	private boolean isInvalid(byte[] b, int off, int len) {
		return off < 0 || len < 0 || len > b.length - off;
	}

	private byte[] readFromFile(int len) {

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
			DataStoreFilePart part = file.getPart(index);
			byte[] src = part.getBytes();

			System.arraycopy(src, start, bytes, begin, length);

			position += length;
			begin += length;

		}

		return bytes;

	}

}
