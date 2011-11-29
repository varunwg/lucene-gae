package com.googlecode.lucene.gae.datastore;

import static org.junit.Assert.assertEquals;

import org.apache.lucene.store.Directory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.googlecode.lucene.gae.TestUtils;
import com.googlecode.lucene.gae.TestUtils.IndexStatus;

public class TestDelete {

	@Before
	public void setUp() throws Exception {
		TestUtils.setUp();
	}

	@After
	public void tearDown() throws Exception {
		// TestUtil.tearDown();
	}

	@Test
	public void testDelete() throws Exception {

		long numDocs = 2;
		long maxDocs = 2;

		Directory dir = TestUtils.createTestDirectory();

		TestUtils.write(dir, "teste 1");
		TestUtils.write(dir, "teste 2");
		TestUtils.write(dir, "teste 3");

		TestUtils.delete(dir, "1");

		IndexStatus status = TestUtils.getStatus(dir);

		assertEquals(numDocs, status.getNumDocs());
		assertEquals(maxDocs, status.getMaxDoc());

	}

}
