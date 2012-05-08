package com.googlecode.lucene.gae.blobstore;

import org.apache.lucene.store.Directory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.googlecode.lucene.gae.TestUtils;

public class BlobStoreWriterTest {

	@Before
	public void setUp() throws Exception {
		TestUtils.setUp();
	}

	@After
	public void tearDown() throws Exception {
		TestUtils.tearDown();
	}

	@Test
	public void testWriter() throws Exception {

		Directory dir = new BlobStoreDirectory();

		TestUtils.write(dir, "teste");

	}

	@Test
	public void testReWriter() throws Exception {

		Directory dir = new BlobStoreDirectory();

		TestUtils.write(dir, "teste1");

		Thread.sleep(1000);

		TestUtils.write(dir, "teste2");

	}

	@Test
	public void testWriter_Compare_Dirs() throws Exception {

		String words = "teste2";

		Directory gaeDir = new BlobStoreDirectory();
		Directory sysDir = TestUtils.createTestDirectory();

		TestUtils.write(sysDir, words);

		// index are time aware
		Thread.sleep(1000);

		TestUtils.write(gaeDir, words);

		TestUtils.compareDirs(sysDir, gaeDir);

	}

	@Test
	public void testWriter_Compare_Dirs2() throws Exception {

		String[] words = new String[] { "teste1", "teste2", "teste3" };

		Directory gaeDir = new BlobStoreDirectory();
		Directory sysDir = TestUtils.createTestDirectory();

		TestUtils.write(sysDir, words);

		// index are time aware
		Thread.sleep(1000);

		TestUtils.write(gaeDir, words);

		TestUtils.compareDirs(sysDir, gaeDir);

	}

}
