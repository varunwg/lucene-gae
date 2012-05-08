package com.googlecode.lucene.gae.tool;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.lucene.store.Directory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.googlecode.lucene.gae.TestUtils;
import com.googlecode.lucene.gae.datastore.DataStoreDirectory;

public class IndexToolTest {

	@Before
	public void setUp() throws Exception {
		TestUtils.setUp();
	}

	@After
	public void tearDown() throws Exception {
		TestUtils.tearDown();
	}

	@Test
	public void testClean() throws Exception {

		Directory dir = new DataStoreDirectory("teste");

		IndexTool tool = new IndexTool(dir);

		tool.clean();

	}

	@Test
	public void testExportZip() throws Exception {

		Directory testDir = TestUtils.createTestDirectory();

		TestUtils.write(testDir, "teste1");
		TestUtils.write(testDir, "teste2");
		TestUtils.write(testDir, "teste3");

		IndexTool tool = new IndexTool(testDir);

		File zip = createDestFile("export.zip");

		FileOutputStream out = new FileOutputStream(zip);
		tool.exportZip(out);
		out.close();

		decompress(zip, TestUtils.DEST_INDEX_DIR);

		compareDirs(TestUtils.TEST_INDEX_DIR, TestUtils.DEST_INDEX_DIR);

	}

	@Test
	public void testImportZip() throws Exception {

		Directory testDir = TestUtils.createTestDirectory();

		TestUtils.write(testDir, "teste1");
		TestUtils.write(testDir, "teste2");
		TestUtils.write(testDir, "teste3");

		File zip = createDestFile("export.zip");

		compress(TestUtils.TEST_INDEX_DIR, zip);

		Directory detDir = TestUtils.createDestDirectory();

		IndexTool tool = new IndexTool(detDir);

		FileInputStream in = new FileInputStream(zip);
		tool.importZip(in);
		in.close();

		compareDirs(TestUtils.TEST_INDEX_DIR, TestUtils.DEST_INDEX_DIR);

	}

	@Test
	public void testOptimize() throws Exception {

		Directory dir = new DataStoreDirectory("teste");

		IndexTool tool = new IndexTool(dir);

		tool.optimize(TestUtils.getWriterConfig());

	}

	@Test
	public void testOptimizeAndClean() throws Exception {

		Directory dir = new DataStoreDirectory("teste");

		IndexTool tool = new IndexTool(dir);

		tool.optimizeAndClean(TestUtils.getWriterConfig());

	}

	@Test
	public void testReadFile() throws Exception {

		Directory testDir = TestUtils.createTestDirectory();

		TestUtils.write(testDir, "teste1");
		TestUtils.write(testDir, "teste2");
		TestUtils.write(testDir, "teste3");

		String[] files = testDir.listAll();

		Directory destDir = TestUtils.createDestDirectory();

		IndexTool tool = new IndexTool(destDir);

		for (String name : files) {

			File file = new File(TestUtils.TEST_INDEX_PATH, name);
			FileInputStream in = new FileInputStream(file);
			tool.readFile(name, in);
			in.close();

		}

		compareDirs(TestUtils.TEST_INDEX_DIR, TestUtils.DEST_INDEX_DIR);

	}

	@Test
	public void testWriteFile() throws Exception {

		Directory testDir = TestUtils.createTestDirectory();

		TestUtils.write(testDir, "teste1");
		TestUtils.write(testDir, "teste2");
		TestUtils.write(testDir, "teste3");

		String[] files = testDir.listAll();

		TestUtils.createDestDirectory();

		IndexTool tool = new IndexTool(testDir);

		for (String name : files) {

			File file = createDestFile(name);
			FileOutputStream out = new FileOutputStream(file);
			tool.writeFile(name, out);
			out.close();

		}

		compareDirs(TestUtils.TEST_INDEX_DIR, TestUtils.DEST_INDEX_DIR);

	}

	private void compareDirs(File testDir, File destDir) throws Exception {

		IOFileFilter filter = FileFilterUtils.trueFileFilter();
		Collection<File> files = FileUtils.listFiles(testDir, filter, filter);

		for (File testFile : files) {

			File destFile = new File(destDir, testFile.getName());

			assertEquals(testFile.exists(), destFile.exists());
			assertEquals(testFile.length(), destFile.length());

			byte[] testBytes = FileUtils.readFileToByteArray(testFile);
			byte[] destBytes = FileUtils.readFileToByteArray(destFile);

			assertEquals(testFile.getName(), TestUtils.toString(testBytes), TestUtils.toString(destBytes));

		}

	}

	private void compress(File scrDir, File zip) throws Exception {

		// create byte buffer
		byte[] buffer = new byte[1024];

		IOFileFilter filter = FileFilterUtils.trueFileFilter();
		Collection<File> files = FileUtils.listFiles(scrDir, filter, filter);

		ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zip));

		for (File file : files) {

			FileInputStream srcIn = new FileInputStream(file);
			zipOut.putNextEntry(new ZipEntry(file.getName()));

			int length;
			while ((length = srcIn.read(buffer)) > 0) {
				zipOut.write(buffer, 0, length);
			}

			srcIn.close();
			zipOut.closeEntry();

		}

		zipOut.close();

	}

	private File createDestFile(String name) throws Exception {

		File dir = new File(TestUtils.DEST_INDEX_PATH);

		if (!dir.exists()) {
			dir.mkdirs();
		}

		File file = new File(dir, name);

		if (!file.exists()) {
			file.createNewFile();
		}

		return file;

	}

	private void decompress(File zip, File destDir) throws Exception {

		byte buffer[] = new byte[1024];

		ZipInputStream zis = new ZipInputStream(new FileInputStream(zip));

		ZipEntry entry = null;

		while ((entry = zis.getNextEntry()) != null) {

			File destFile = new File(destDir, entry.getName());
			FileOutputStream destOut = new FileOutputStream(destFile);

			int count;
			while ((count = zis.read(buffer)) != -1) {
				destOut.write(buffer, 0, count);
			}

			destOut.close();

		}

		zis.close();

	}

}
