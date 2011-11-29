package com.googlecode.lucene.gae.blobstore;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import org.apache.lucene.store.BufferedIndexOutput;

import com.google.appengine.api.files.FileWriteChannel;
import com.googlecode.lucene.gae.blobstore.wrapper.AppEngineFileWrapper;

public class BlobStoreIndexOutput extends BufferedIndexOutput {

	private AppEngineFileWrapper	file;

	private byte[]					bytes	= new byte[0];

	private Logger					log		= Logger.getLogger("Write");

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

			byte[] subarray = ArrayUtils.subArray(b, offset, length);

			int currentPos = (int) getFilePointer();
			int currentLen = (int) file.getLength();

			log.info("Status: currentPos=" + currentPos + ",  currentLen=" + currentLen);

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
				bytes = ArrayUtils.addLenght(bytes, dif);
				file.incrementLenght(dif);
				log.info("Add (" + dif + ") " + file.getName() + " Lenght");
			}

			try {
				ArrayUtils.insert(bytes, subarray, pos);
				log.info("Write (" + length + ") from " + pos + " in " + file.getName() + " = "
						+ ArrayUtils.getArray(subarray));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			log.info("Total (" + file.getLength() + ") " + file.getName() + " = "
					+ ArrayUtils.getArray(bytes));

		}

	}

}
