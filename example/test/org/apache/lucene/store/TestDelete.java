package org.apache.lucene.store;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.TestUtil;
import util.TestUtil.IndexStatus;

public class TestDelete {

	@Before
	public void setUp() throws Exception {
		TestUtil.setUp();
	}

	@After
	public void tearDown() throws Exception {
		// TestUtil.tearDown();
	}

	@Test
	public void testDelete() throws Exception {

		long numDocs = 2;
		long maxDocs = 2;

		Directory dir = TestUtil.createTestDirectory();

		TestUtil.write(dir, "teste 1");
		TestUtil.write(dir, "teste 2");
		TestUtil.write(dir, "teste 3");

		TestUtil.delete(dir, "1");

		IndexStatus status = TestUtil.getStatus(dir);

		assertEquals(numDocs, status.getNumDocs());
		assertEquals(maxDocs, status.getMaxDoc());

	}

}