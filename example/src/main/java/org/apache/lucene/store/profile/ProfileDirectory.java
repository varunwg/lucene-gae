package org.apache.lucene.store.profile;

import java.io.IOException;
import java.util.Collection;

import org.apache.lucene.store.*;

public class ProfileDirectory extends Directory {

	private final Directory	delegate;

	public ProfileDirectory(Directory delegate) throws IOException {
		this.delegate = delegate;
	}

	@Override
	public void close() throws IOException {

		Profile prof = new Profile("close").start();

		delegate.close();

		prof.end().log();

	}

	@Override
	public IndexOutput createOutput(String name) throws IOException {

		Profile prof = new Profile("createOutput").start();

		IndexOutput result = delegate.createOutput(name);

		prof.end().log(name);

		return result;

	}

	@Override
	public void deleteFile(String name) throws IOException {

		Profile prof = new Profile("deleteFile").start();

		delegate.deleteFile(name);

		prof.end().log(name);

	}

	@Override
	public boolean fileExists(String name) throws IOException {

		Profile prof = new Profile("fileExists").start();

		boolean result = delegate.fileExists(name);

		prof.end().log(name);

		return result;

	}

	@Override
	public long fileLength(String name) throws IOException {

		Profile prof = new Profile("fileLength").start();

		long result = delegate.fileLength(name);

		prof.end().log(name);

		return result;

	}

	@Override
	public long fileModified(String name) throws IOException {

		Profile prof = new Profile("fileModified").start();

		long result = delegate.fileModified(name);

		prof.end().log(name);

		return result;

	}

	@Override
	public String[] listAll() throws IOException {

		Profile prof = new Profile("listAll").start();

		String[] result = delegate.listAll();

		prof.end().log();

		return result;

	}

	@Override
	public IndexInput openInput(String name) throws IOException {

		Profile prof = new Profile("openInput").start();

		IndexInput result = delegate.openInput(name);

		prof.end().log(name);

		return result;

	}

	@SuppressWarnings("deprecation")
	@Override
	public void touchFile(String name) throws IOException {

		Profile prof = new Profile("touchFile").start();

		delegate.touchFile(name);

		prof.end().log(name);

	}

	@Override
	public void clearLock(String name) throws IOException {

		Profile prof = new Profile("clearLock").start();

		delegate.clearLock(name);

		prof.end().log(name);

	}

	@Override
	public void copy(Directory to, String src, String dest) throws IOException {

		Profile prof = new Profile("copy").start();

		delegate.copy(to, src, dest);

		prof.end().log(src, dest);

	}

	@Override
	public LockFactory getLockFactory() {

		Profile prof = new Profile("getLockFactory").start();

		LockFactory result = delegate.getLockFactory();

		prof.end().log();

		return result;

	}

	@Override
	public String getLockID() {

		Profile prof = new Profile("getLockID").start();

		String result = delegate.getLockID();

		prof.end().log();

		return result;

	}

	@Override
	public Lock makeLock(String name) {

		Profile prof = new Profile("makeLock").start();

		Lock result = delegate.makeLock(name);

		prof.end().log(name);

		return result;

	}

	@Override
	public IndexInput openInput(String name, int bufferSize) throws IOException {

		Profile prof = new Profile("openInput").start();

		IndexInput result = delegate.openInput(name, bufferSize);

		prof.end().log(name);

		return result;

	}

	@Override
	public void setLockFactory(LockFactory lockFactory) throws IOException {

		Profile prof = new Profile("setLockFactory").start();

		delegate.setLockFactory(lockFactory);

		prof.end().log();

	}

	@Override
	public void sync(Collection<String> names) throws IOException {

		Profile prof = new Profile("sync").start();

		delegate.sync(names);

		prof.end().log();

	}

	@SuppressWarnings("deprecation")
	@Override
	public void sync(String name) throws IOException {

		Profile prof = new Profile("sync").start();

		delegate.sync(name);

		prof.end().log(name);

	}

}
