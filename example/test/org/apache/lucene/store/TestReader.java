package org.apache.lucene.store;

import static org.junit.Assert.assertEquals;

import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.gae.file.AppEngineDirectory;
import org.junit.*;

import util.TestUtil;

public class TestReader {

	@Before
	public void setUp() throws Exception {
		TestUtil.setUp();
	}

	@After
	public void tearDown() throws Exception {
		TestUtil.tearDown();
	}

	@Test
	public void testReader() throws Exception {

		int hits = 1;

		Directory gaeDir = new AppEngineDirectory();

		String[] texts = { "Lucene in Action" };

		TestUtil.write(gaeDir, texts);

		TopDocs results = TestUtil.search(gaeDir, "Lucene");

		assertEquals(hits, results.totalHits);

	}

	@Test
	public void testReader2() throws Exception {

		int hits = 3;

		Directory gaeDir = new AppEngineDirectory();

		String[] texts = { "Lucene in Action", "How Lucene Works", "Another Directory of Lucene" };

		TestUtil.write(gaeDir, texts);

		TopDocs results = TestUtil.search(gaeDir, "Lucene");

		assertEquals(hits, results.totalHits);

	}

}
