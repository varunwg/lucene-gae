package com.google.code.lucene.gae.blobstore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.SingleInstanceLockFactory;

import com.google.code.lucene.gae.blobstore.wrapper.AppEngineFileWrapper;
import com.google.code.lucene.gae.blobstore.wrapper.AppEngineFileWrapperRepository;

public class BlobStoreDirectory extends Directory {

	private AppEngineFileWrapperRepository	fileWrapperRepository	= AppEngineFileWrapperRepository
																			.getInstance();

	public BlobStoreDirectory() throws IOException {
		setLockFactory(new SingleInstanceLockFactory());
	}

	@Override
	public void close() throws IOException {

	}

	@Override
	public IndexOutput createOutput(String name) throws IOException {

		AppEngineFileWrapper fileWrapper = fileWrapperRepository.get(name);

		if (fileWrapper == null) {
			fileWrapper = new AppEngineFileWrapper(name);
			fileWrapperRepository.put(fileWrapper);
		}

		return new BlobStoreIndexOutput(fileWrapper);

	}

	@Override
	public void deleteFile(String name) throws IOException {
		fileWrapperRepository.delete(name);
	}

	@Override
	public boolean fileExists(String name) throws IOException {
		return fileWrapperRepository.get(name) != null;
	}

	@Override
	public long fileLength(String name) throws IOException {

		AppEngineFileWrapper file = getFileByName(name);

		return file.getLength();

	}

	@Override
	public long fileModified(String name) throws IOException {

		AppEngineFileWrapper file = getFileByName(name);

		return file.getLastModified();

	}

	@Override
	public String[] listAll() throws IOException {

		List<AppEngineFileWrapper> files = fileWrapperRepository.list();
		List<String> names = new ArrayList<String>(files.size());

		for (AppEngineFileWrapper file : files) {
			names.add(file.getName());
		}

		return names.toArray(new String[names.size()]);

	}

	@Override
	public IndexInput openInput(String name) throws IOException {

		AppEngineFileWrapper fileWrapper = getFileByName(name);

		return new BlobStoreIndexInput(fileWrapper);

	}

	@Override
	public void touchFile(String name) throws IOException {

		AppEngineFileWrapper file = getFileByName(name);

		long now = System.currentTimeMillis();

		file.updateLastModified(now);

	}

	private AppEngineFileWrapper getFileByName(String name) throws FileNotFoundException {

		AppEngineFileWrapper file = fileWrapperRepository.get(name);

		if (file == null) {
			throw new FileNotFoundException(name);
		}

		return file;

	}

}
