package org.apache.lucene.store;

import static org.junit.Assert.assertEquals;

import org.apache.lucene.store.gae.datastore.DataStoreDirectory;
import org.apache.lucene.store.gae.util.ArrayUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.TestUtil;

public class TestWriter2 {

	@Before
	public void setUp() throws Exception {
		TestUtil.setUp();
	}

	@After
	public void tearDown() throws Exception {
		TestUtil.tearDown();
	}

	@Test
	public void testWriter() throws Exception {

		Directory dir = new DataStoreDirectory();

		TestUtil.write(dir, "teste");

	}

	@Test
	public void testReWriter() throws Exception {

		Directory dir = new DataStoreDirectory();

		TestUtil.write(dir, "teste1");

		Thread.sleep(1000);

		TestUtil.write(dir, "teste2");

	}

	@Test
	public void testWriter_Compare_Dirs() throws Exception {

		String words = "teste2";

		Directory gaeDir = new DataStoreDirectory();
		Directory sysDir = TestUtil.createTestDirectory();

		TestUtil.write(sysDir, words);

		// index are time aware
		Thread.sleep(1000);

		TestUtil.write(gaeDir, words);

		compareDirs(sysDir, gaeDir);

	}

	@Test
	public void testWriter_Compare_Dirs2() throws Exception {

		String[] words = new String[] { "teste1", "teste2", "teste3" };

		Directory gaeDir = new DataStoreDirectory();
		Directory sysDir = TestUtil.createTestDirectory();

		TestUtil.write(sysDir, words);

		// index are time aware
		Thread.sleep(1000);

		TestUtil.write(gaeDir, words);

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
				assertEquals(name, ArrayUtils.getArray(fileBytes), ArrayUtils.getArray(gaeBytes));
			} else {

				byte[] fileSegmentHead = ArrayUtils.subArray(fileBytes, 0, 9);
				byte[] gaeSegmentHead = ArrayUtils.subArray(gaeBytes, 0, 9);

				byte[] fileSegmentBody = ArrayUtils.subArray(fileBytes, 12, fileBytes.length - 4);
				byte[] gaeSegmentBody = ArrayUtils.subArray(gaeBytes, 12, fileBytes.length - 4);

				assertEquals(name, ArrayUtils.getArray(fileSegmentHead),
						ArrayUtils.getArray(gaeSegmentHead));
				assertEquals(name, ArrayUtils.getArray(fileSegmentBody),
						ArrayUtils.getArray(gaeSegmentBody));

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
