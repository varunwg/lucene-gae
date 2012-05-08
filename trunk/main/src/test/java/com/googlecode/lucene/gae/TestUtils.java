package com.googlecode.lucene.gae;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import com.google.appengine.tools.development.testing.LocalBlobstoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class TestUtils {

	public static final String TEST_INDEX_PATH = "./lucene";
	public static final String DEST_INDEX_PATH = "./dest";
	public static final String TEST_INDEX_FIELD = "title";

	public static final File TEST_INDEX_DIR = new File(TEST_INDEX_PATH);
	public static final File DEST_INDEX_DIR = new File(DEST_INDEX_PATH);

	public static final Version TEST_LUCENE_VERSION = Version.LUCENE_34;
	public static final Analyzer ANALYZER = new StandardAnalyzer(TEST_LUCENE_VERSION);

	private static LocalServiceTestHelper helper;

	public static void compareDirs(Directory sysDir, Directory gaeDir) throws Exception {

		for (String name : gaeDir.listAll()) {

			IndexInput sysIn = sysDir.openInput(name);
			IndexInput gaeIn = gaeDir.openInput(name);

			assertEquals(name, sysIn.length(), gaeIn.length());

			byte[] fileBytes = readIndex(sysIn);
			byte[] gaeBytes = readIndex(gaeIn);

			if (!"segments_1".equals(name)) {
				assertEquals(name, TestUtils.toString(fileBytes), TestUtils.toString(gaeBytes));
			} else {

				byte[] fileSegmentHead = TestUtils.subArray(fileBytes, 0, 9);
				byte[] gaeSegmentHead = TestUtils.subArray(gaeBytes, 0, 9);

				byte[] fileSegmentBody = TestUtils.subArray(fileBytes, 12, fileBytes.length - 4);
				byte[] gaeSegmentBody = TestUtils.subArray(gaeBytes, 12, fileBytes.length - 4);

				assertEquals(name, TestUtils.toString(fileSegmentHead), TestUtils.toString(gaeSegmentHead));
				assertEquals(name, TestUtils.toString(fileSegmentBody), TestUtils.toString(gaeSegmentBody));

			}

		}

	}

	public static Directory createDestDirectory() throws Exception {
		return new SimpleFSDirectory(DEST_INDEX_DIR);
	}

	public static Directory createTestDirectory() throws Exception {
		return new SimpleFSDirectory(TEST_INDEX_DIR);
	}

	public static IndexWriterConfig getWriterConfig() {
		return new IndexWriterConfig(TEST_LUCENE_VERSION, ANALYZER);
	}

	public static TopDocs search(Directory dir, String query) throws Exception {

		IndexSearcher searcher = new IndexSearcher(dir, true);

		Query q = new QueryParser(TEST_LUCENE_VERSION, TEST_INDEX_FIELD, ANALYZER).parse(query);

		TopDocs results = searcher.search(q, 10);

		searcher.close();

		return results;

	}

	public static void setUp() throws Exception {

		helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig(),
				new LocalBlobstoreServiceTestConfig());

		helper.setUp();

		FileUtils.deleteDirectory(TEST_INDEX_DIR);
		FileUtils.deleteDirectory(DEST_INDEX_DIR);

	}

	public static void tearDown() throws Exception {

		try {
			helper.tearDown();
		} catch (Throwable e) {
			// TODO: handle exception
		}

		FileUtils.deleteDirectory(TEST_INDEX_DIR);
		FileUtils.deleteDirectory(DEST_INDEX_DIR);

	}

	public static String toString(byte[] b) {

		StringBuilder builder = new StringBuilder();

		builder.append("[");

		for (int i = 0; i < b.length; i++) {
			byte t = b[i];
			builder.append(t).append(",");
		}

		builder.append("]");

		return builder.toString();

	}

	public static void write(Directory dir, String... texts) throws Exception {

		IndexWriter writer = new IndexWriter(dir, getWriterConfig());

		for (String text : texts) {
			Document doc = new Document();
			doc.add(new Field(TEST_INDEX_FIELD, text, Field.Store.YES, Field.Index.ANALYZED));
			writer.addDocument(doc);
		}

		writer.close();

	}

	private static byte[] readIndex(IndexInput in) throws Exception {
		byte[] b = new byte[(int) in.length()];
		in.readBytes(b, 0, (int) in.length());
		in.close();
		return b;
	}

	private static byte[] subArray(byte[] bytes, int offset, int length) {
		return Arrays.copyOfRange(bytes, offset, length);
	}

}
