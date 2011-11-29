package com.googlecode.lucene.gae.datastore;

import static org.junit.Assert.assertEquals;

import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.googlecode.lucene.gae.TestUtil;
import com.googlecode.lucene.gae.datastore.DataStoreDirectory;

public class TestReader2 {

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

		Directory gaeDir = new DataStoreDirectory();

		String[] texts = { "Lucene in Action" };

		TestUtil.write(gaeDir, texts);

		TopDocs results = TestUtil.search(gaeDir, "Lucene");

		assertEquals(hits, results.totalHits);

	}

	@Test
	public void testReader2() throws Exception {

		int hits = 3;

		Directory gaeDir = new DataStoreDirectory();

		String[] texts = { "Lucene in Action", "How Lucene Works", "Another Directory of Lucene" };

		TestUtil.write(gaeDir, texts);

		TopDocs results = TestUtil.search(gaeDir, "Lucene");

		assertEquals(hits, results.totalHits);

	}

}
