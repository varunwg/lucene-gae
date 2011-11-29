package com.googlecode.lucene.gae.datastore;

import static org.junit.Assert.assertTrue;

import org.apache.lucene.store.Directory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.googlecode.lucene.gae.TestUtils;
import com.googlecode.lucene.gae.TestUtils.IndexStatus;

public class TestOptimize {

	@Before
	public void setUp() throws Exception {
		TestUtils.setUp();
	}

	@After
	public void tearDown() throws Exception {
		// TestUtil.tearDown();
	}

	@Test
	public void testOptimize() throws Exception {

		Directory dir = TestUtils.createTestDirectory();

		TestUtils.write(dir, "teste 1");
		TestUtils.write(dir, "teste 2");
		TestUtils.write(dir, "teste 3");

		TestUtils.delete(dir, "1");

		TestUtils.optimize(dir);

		IndexStatus status = TestUtils.getStatus(dir);
		assertTrue(status.isOptimized());

	}

}
