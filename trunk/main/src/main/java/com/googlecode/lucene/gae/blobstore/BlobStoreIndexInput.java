package com.googlecode.lucene.gae.blobstore;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import org.apache.lucene.store.BufferedIndexInput;

import com.google.appengine.api.files.FileReadChannel;
import com.googlecode.lucene.gae.blobstore.wrapper.AppEngineFileWrapper;

public class BlobStoreIndexInput extends BufferedIndexInput {

	private FileReadChannel			reader;
	private AppEngineFileWrapper	file;

	private Logger					log	= Logger.getLogger("Read");

	public BlobStoreIndexInput(AppEngineFileWrapper file) throws IOException {
		this.reader = file.openReadChannel();
		this.file = file;
	}

	@Override
	public void close() throws IOException {
		reader.close();
		file.close();
	}

	@Override
	public long length() {
		return file.getLength();
	}

	@Override
	protected void readInternal(byte[] b, int offset, int length) throws IOException {

		ByteBuffer toRead = ByteBuffer.wrap(b, offset, length);

		int read = reader.read(toRead);

		log.info("Read (" + read + ") " + file.getName() + " = "
				+ ArrayUtils.getArray(b, offset, length));

	}

	@Override
	protected void seekInternal(long pos) throws IOException {
		reader.position(pos);
	}

}
