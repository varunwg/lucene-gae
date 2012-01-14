package com.google.code.lucene.gae.blobstore;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.apache.lucene.store.BufferedIndexOutput;

import com.google.appengine.api.files.FileWriteChannel;
import com.google.code.lucene.gae.blobstore.wrapper.AppEngineFileWrapper;

public class BlobStoreIndexOutput extends BufferedIndexOutput {

	private AppEngineFileWrapper	file;

	private byte[]					bytes	= new byte[0];

	public BlobStoreIndexOutput(AppEngineFileWrapper file) throws IOException {
		this.file = file;
	}

	@Override
	public void close() throws IOException {
		super.close();
		writeAll();
		file.close();
	}

	@Override
	public long length() {
		return file.getLength();
	}

	private void writeAll() throws IOException {
		FileWriteChannel writer = file.openWriteChannel();
		ByteBuffer toWrite = ByteBuffer.wrap(bytes);
		writer.write(toWrite);
		writer.closeFinally();
	}

	@Override
	protected void flushBuffer(byte[] b, int offset, int length) throws IOException {

		if (length > 0) {

			byte[] subarray = Arrays.copyOfRange(b, offset, length);

			int currentPos = (int) getFilePointer();
			int currentLen = (int) file.getLength();

			int totalLen = 0;
			int pos = 0;

			if (currentPos == length) {
				totalLen = currentLen + length;
			} else if (currentPos == currentLen) {
				pos = currentLen - length;
			} else if (currentPos > length) {
				pos = currentPos - length;
				totalLen = currentPos + length;
			} else {
				pos = 0;
			}

			if (totalLen > currentLen) {
				int dif = totalLen - currentLen;
				bytes = new byte[bytes.length + length];
				System.arraycopy(bytes, 0, bytes, 0, bytes.length);
				file.incrementLenght(dif);
			}

			System.arraycopy(subarray, 0, bytes, pos, subarray.length);

		}

	}

}
