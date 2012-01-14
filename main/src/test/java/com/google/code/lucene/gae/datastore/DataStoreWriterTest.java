package com.google.code.lucene.gae.datastore;

import static org.junit.Assert.assertEquals;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexInput;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.code.lucene.gae.TestUtils;
import com.google.code.lucene.gae.datastore.DataStoreDirectory;

public class DataStoreWriterTest {

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

		Directory dir = new DataStoreDirectory();

		TestUtils.write(dir, "teste");

	}

	@Test
	public void testReWriter() throws Exception {

		Directory dir = new DataStoreDirectory();

		TestUtils.write(dir, "teste1");

		Thread.sleep(1000);

		TestUtils.write(dir, "teste2");

	}

	@Test
	public void testWriter_Compare_Dirs() throws Exception {

		String words = "teste2";

		Directory gaeDir = new DataStoreDirectory();
		Directory sysDir = TestUtils.createTestDirectory();

		TestUtils.write(sysDir, words);

		// index are time aware
		Thread.sleep(1000);

		TestUtils.write(gaeDir, words);

		compareDirs(sysDir, gaeDir);

	}

	@Test
	public void testWriter_Compare_Dirs2() throws Exception {

		String[] words = new String[] { "teste1", "teste2", "teste3" };

		Directory gaeDir = new DataStoreDirectory();
		Directory sysDir = TestUtils.createTestDirectory();

		TestUtils.write(sysDir, words);

		// index are time aware
		Thread.sleep(1000);

		TestUtils.write(gaeDir, words);

		compareDirs(sysDir, gaeDir);

	}

	private void compareDirs(Directory sysDir, Directory gaeDir) throws Exception {

		for (String name : gaeDir.listAll()) {

			IndexInput sysIn = sysDir.openInput(name);
			IndexInput gaeIn = gaeDir.openInput(name);

			assertEquals(name, sysIn.length(), gaeIn.length());

			byte[] fileBytes = readIndex(sysIn);
			byte[] gaeBytes = readIndex(gaeIn);

			if (!"segments_1".equals(name)) {
				assertEquals(name, TestUtils.getArray(fileBytes), TestUtils.getArray(gaeBytes));
			} else {

				byte[] fileSegmentHead = TestUtils.subArray(fileBytes, 0, 9);
				byte[] gaeSegmentHead = TestUtils.subArray(gaeBytes, 0, 9);

				byte[] fileSegmentBody = TestUtils.subArray(fileBytes, 12, fileBytes.length - 4);
				byte[] gaeSegmentBody = TestUtils.subArray(gaeBytes, 12, fileBytes.length - 4);

				assertEquals(name, TestUtils.getArray(fileSegmentHead),
						TestUtils.getArray(gaeSegmentHead));
				assertEquals(name, TestUtils.getArray(fileSegmentBody),
						TestUtils.getArray(gaeSegmentBody));

			}

		}

	}

	private byte[] readIndex(IndexInput in) throws Exception {
		byte[] b = new byte[(int) in.length()];
		in.readBytes(b, 0, (int) in.length());
		in.close();
		return b;
	}

}
