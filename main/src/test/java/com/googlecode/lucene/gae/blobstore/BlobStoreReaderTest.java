package com.googlecode.lucene.gae.blobstore;

import static org.junit.Assert.assertEquals;

import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.googlecode.lucene.gae.TestUtils;

public class BlobStoreReaderTest {

	@Before
	public void setUp() throws Exception {
		TestUtils.setUp();
	}

	@After
	public void tearDown() throws Exception {
		TestUtils.tearDown();
	}

	@Test
	public void testReader() throws Exception {

		int hits = 1;

		Directory gaeDir = new BlobStoreDirectory();

		String[] texts = { "Lucene in Action" };

		TestUtils.write(gaeDir, texts);

		TopDocs results = TestUtils.search(gaeDir, "Lucene");

		assertEquals(hits, results.totalHits);

	}

	@Test
	public void testReader2() throws Exception {

		int hits = 3;

		Directory gaeDir = new BlobStoreDirectory();

		String[] texts = { "Lucene in Action", "How Lucene Works", "Another Directory of Lucene" };

		TestUtils.write(gaeDir, texts);

		TopDocs results = TestUtils.search(gaeDir, "Lucene");

		assertEquals(hits, results.totalHits);

	}

}
