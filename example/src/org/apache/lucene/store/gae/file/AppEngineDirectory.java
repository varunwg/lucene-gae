package org.apache.lucene.store.gae.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.store.*;
import org.apache.lucene.store.gae.file.wrapper.AppEngineFileWrapper;
import org.apache.lucene.store.gae.file.wrapper.AppEngineFileWrapperRepository;

public class AppEngineDirectory extends Directory {

	private AppEngineFileWrapperRepository	fileWrapperRepository	= AppEngineFileWrapperRepository
																			.getInstance();

	public AppEngineDirectory() throws IOException {
		setLockFactory(new SingleInstanceLockFactory());
	}

	@Override
	public void close() throws IOException {

		// TODO: ver o que fazer
		System.out.println("close");

	}

	@Override
	public IndexOutput createOutput(String name) throws IOException {

		AppEngineFileWrapper fileWrapper = fileWrapperRepository.get(name);

		if (fileWrapper == null) {
			fileWrapper = new AppEngineFileWrapper(name);
			fileWrapperRepository.put(fileWrapper);
		}

		return new BufferAppEngineFileOutputStream(fileWrapper);

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

		return new BufferAppEngineFileInputStream(fileWrapper);

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
