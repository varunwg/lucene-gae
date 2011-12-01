package com.googlecode.lucene.gae.datastore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.SingleInstanceLockFactory;

import com.googlecode.lucene.gae.datastore.file.DataStoreFile;
import com.googlecode.lucene.gae.datastore.file.DataStoreFileRepository;

public class DataStoreDirectory extends Directory {

	private static final String				DEFAULT_INDEX_NAME	= "default";

	private final DataStoreFileRepository	repository;

	public DataStoreDirectory() throws IOException {
		this(DEFAULT_INDEX_NAME);
	}

	public DataStoreDirectory(String indexName) throws IOException {
		setLockFactory(new SingleInstanceLockFactory());
		this.repository = new DataStoreFileRepository(indexName);
	}

	@Override
	public void close() throws IOException {

	}

	@Override
	public IndexOutput createOutput(String name) throws IOException {

		DataStoreFile file = new DataStoreFile(name);

		repository.put(file);

		return new DataStoreIndexOutput(file, repository);

	}

	@Override
	public void deleteFile(String name) throws IOException {

		DataStoreFile file = repository.get(name);

		if (file != null) {
			file.delete();
			repository.put(file);
		}

	}

	@Override
	public boolean fileExists(String name) throws IOException {
		return repository.get(name) != null;
	}

	@Override
	public long fileLength(String name) throws IOException {

		DataStoreFile file = getFileByName(name);

		return file.getLength();

	}

	@Override
	public long fileModified(String name) throws IOException {

		DataStoreFile file = getFileByName(name);

		return file.getLastModified();

	}

	@Override
	public String[] listAll() throws IOException {

		List<String> names = repository.listNames();
		return names.toArray(new String[names.size()]);

	}

	@Override
	public IndexInput openInput(String name) throws IOException {

		DataStoreFile file = getFileByName(name);

		return new DataStoreIndexInput(file);

	}

	@Override
	public void touchFile(String name) throws IOException {

		DataStoreFile file = getFileByName(name);

		long now = System.currentTimeMillis();

		file.updateLastModified(now);

		repository.put(file);

	}

	private DataStoreFile getFileByName(String name)
			throws FileNotFoundException {

		DataStoreFile file = repository.get(name);

		if (file == null) {
			throw new FileNotFoundException(name);
		}

		return file;

	}

}