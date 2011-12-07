package com.googlecode.lucene.gae.datastore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.NoLockFactory;

import com.googlecode.lucene.gae.datastore.file.DataStoreFile;
import com.googlecode.lucene.gae.datastore.file.DataStoreFileRepository;

public class DataStoreDirectory extends Directory {

	private static final String				DEFAULT_INDEX_NAME	= "default";

	private final DataStoreFileRepository	repository;

	public DataStoreDirectory() throws IOException {
		this(DEFAULT_INDEX_NAME);
	}

	public DataStoreDirectory(String indexName) throws IOException {
		setLockFactory(NoLockFactory.getNoLockFactory());
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
		repository.delete(name);
	}

	@Override
	public boolean fileExists(String name) throws IOException {

		try {
			DataStoreFile file = getFileByName(name, false);
			return file != null;
		} catch (FileNotFoundException e) {
			return false;
		}

	}

	@Override
	public long fileLength(String name) throws IOException {

		DataStoreFile file = getFileByName(name, true);

		return file.getLength();

	}

	@Override
	public long fileModified(String name) throws IOException {

		DataStoreFile file = getFileByName(name, false);

		return file.getLastModified();

	}

	@Override
	public String[] listAll() throws IOException {

		List<String> names = repository.listNames();
		return names.toArray(new String[names.size()]);

	}

	@Override
	public IndexInput openInput(String name) throws IOException {

		DataStoreFile file = getFileByName(name, true);

		return new DataStoreIndexInput(file);

	}

	@Override
	public void touchFile(String name) throws IOException {

		DataStoreFile file = getFileByName(name, false);

		file.touch();

		repository.put(file);

	}

	private DataStoreFile getFileByName(String name, boolean full) throws FileNotFoundException {

		DataStoreFile file = repository.get(name, full);

		if (file == null) {
			throw new FileNotFoundException(name);
		}

		return file;

	}

}
