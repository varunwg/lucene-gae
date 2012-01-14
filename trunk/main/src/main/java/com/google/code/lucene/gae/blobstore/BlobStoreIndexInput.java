package com.google.code.lucene.gae.blobstore;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.lucene.store.BufferedIndexInput;

import com.google.appengine.api.files.FileReadChannel;
import com.google.code.lucene.gae.blobstore.wrapper.AppEngineFileWrapper;

public class BlobStoreIndexInput extends BufferedIndexInput {

	private FileReadChannel			reader;
	private AppEngineFileWrapper	file;

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

		reader.read(toRead);

	}

	@Override
	protected void seekInternal(long pos) throws IOException {
		reader.position(pos);
	}

}
