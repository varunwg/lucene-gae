package org.apache.lucene.store;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.store.gae.file.wrapper.AppEngineFileWrapper;
import org.apache.lucene.store.gae.file.wrapper.AppEngineFileWrapperRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.TestUtil;

import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;

public class TestAppEngineFileWrapperRepository {

	private AppEngineFileWrapperRepository repository;

	FileService fileService = FileServiceFactory.getFileService();

	@Before
	public void setUp() throws Exception {

		TestUtil.setUp();

		repository = AppEngineFileWrapperRepository.getInstance();

		AppEngineFileWrapper file1 = createAppEngineFileWrapper("teste1");
		AppEngineFileWrapper file2 = createAppEngineFileWrapper("teste2");
		AppEngineFileWrapper file3 = createAppEngineFileWrapper("teste3");
		repository.put(file1);
		repository.put(file2);
		repository.put(file3);

	}

	@After
	public void tearDown() throws Exception {
		TestUtil.tearDown();
	}

	@Test
	public void testDelete() {
		repository.delete("teste1");
		AppEngineFileWrapper file = repository.get("teste1");
		assertNull(file);
	}

	@Test
	public void testGet() {
		AppEngineFileWrapper file = repository.get("teste1");
		assertNotNull(file);
	}

	@Test
	public void testList() {
		int expected = 3;
		List<AppEngineFileWrapper> list = repository.list();
		assertEquals(expected, list.size());
	}

	@Test
	public void testPut() throws IOException {
		AppEngineFileWrapper file = createAppEngineFileWrapper("teste4");
		repository.put(file);
	}

	private AppEngineFileWrapper createAppEngineFileWrapper(String name) throws IOException {
		return new AppEngineFileWrapper(name);
	}

}
