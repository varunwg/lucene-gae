package org.apache.lucene.store;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.TestUtil;
import util.TestUtil.IndexStatus;

public class TestOptimize {

	@Before
	public void setUp() throws Exception {
		TestUtil.setUp();
	}

	@After
	public void tearDown() throws Exception {
		// TestUtil.tearDown();
	}

	@Test
	public void testOptimize() throws Exception {

		Directory dir = TestUtil.createTestDirectory();

		TestUtil.write(dir, "teste 1");
		TestUtil.write(dir, "teste 2");
		TestUtil.write(dir, "teste 3");

		TestUtil.delete(dir, "1");

		TestUtil.optimize(dir);

		IndexStatus status = TestUtil.getStatus(dir);
		assertTrue(status.isOptimized());

	}

}
