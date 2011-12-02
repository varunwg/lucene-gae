package com.googlecode.lucene.gae.datastore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.googlecode.lucene.gae.TestUtils;
import com.googlecode.lucene.gae.datastore.file.DataStoreFile;
import com.googlecode.lucene.gae.datastore.file.DataStoreFileRepository;

public class TestDataStoreFileRepository {

	private DataStoreFileRepository repository;

	FileService fileService = FileServiceFactory.getFileService();

	@Before
	public void setUp() throws Exception {

		TestUtils.setUp();

		repository = new DataStoreFileRepository("teste");

		DataStoreFile file1 = createDataStoreFile("teste1");
		DataStoreFile file2 = createDataStoreFile("teste2");
		DataStoreFile file3 = createDataStoreFile("teste3");
		repository.put(file1);
		repository.put(file2);
		repository.put(file3);

	}

	@After
	public void tearDown() throws Exception {
		TestUtils.tearDown();
	}

	@Test
	public void testDelete() {
		repository.delete("teste1");
		DataStoreFile file = repository.get("teste1");
		assertNull(file);
	}

	@Test
	public void testGet() {
		DataStoreFile file = repository.get("teste1");
		assertNotNull(file);
	}

	@Test
	public void testList() {
		int expected = 3;
		List<DataStoreFile> list = repository.list();
		assertEquals(expected, list.size());
	}

	@Test
	public void testPut() throws IOException {
		DataStoreFile file = createDataStoreFile("teste4");
		repository.put(file);
	}

	private DataStoreFile createDataStoreFile(String name) throws IOException {
		DataStoreFile result = new DataStoreFile(name);
		result.createNewPart();
		result.createNewPart();
		result.createNewPart();
		return result ;
	}

}
