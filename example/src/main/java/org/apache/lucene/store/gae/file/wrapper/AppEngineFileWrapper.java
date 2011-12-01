package org.apache.lucene.store.gae.file.wrapper;

import java.io.IOException;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;

public class AppEngineFileWrapper {

	private static final FileService fileService = FileServiceFactory.getFileService();
	private static final AppEngineFileWrapperRepository fileWrapperRepository = AppEngineFileWrapperRepository
			.getInstance();

	private final String name;

	private AppEngineFile file;

	private String blobKey;
	private long lastModified = System.currentTimeMillis();
	private long length = 0;

	public AppEngineFileWrapper(String name) {
		this.name = name;
	}

	public void close() {
		BlobKey key = fileService.getBlobKey(file);
		// this is hacky, but necessary as sometimes the
		// blobkey isn't available right away
		while (key == null) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			key = fileService.getBlobKey(file);
		}
		blobKey = key.getKeyString();
		fileWrapperRepository.put(this);
	}

	public String getBlobKey() {
		return blobKey;
	}

	public long getLastModified() {
		return lastModified;
	}

	public long getLength() {
		return length;
	}

	public String getName() {
		return name;
	}

	public void incrementLenght(int i) {
		length += i;
	}

	public boolean isNew() {
		return blobKey == null;
	}

	public FileReadChannel openReadChannel() throws IOException {
		boolean lock = false;
		loadFile();
		return fileService.openReadChannel(file, lock);
	}

	public FileWriteChannel openWriteChannel() throws IOException {
		boolean lock = true;
		loadFile();
		return fileService.openWriteChannel(file, lock);
	}

	public void updateLastModified(long now) {
		setLastModified(now);
		fileWrapperRepository.put(this);
	}

	private void loadFile() throws IOException {

		if (isNew()) {
			file = fileService.createNewBlobFile("", name);
		} else {
			BlobKey key = new BlobKey(blobKey);
			file = fileService.getBlobFile(key);
		}

	}

	void setBlobKey(String blobKey) {
		this.blobKey = blobKey;
	}

	void setLastModified(long time) {
		this.lastModified = time;
	}

	void setLength(long length) {
		this.length = length;
	}

}
