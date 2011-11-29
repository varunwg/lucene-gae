package com.googlecode.lucene.gae;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import com.google.appengine.tools.development.testing.LocalBlobstoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class TestUtil {

	public static class IndexStatus {

		private boolean hasDeletions;
		private boolean optimized;
		private long maxDoc;
		private long numDocs;

		public long getMaxDoc() {
			return maxDoc;
		}

		public long getNumDocs() {
			return numDocs;
		}

		public boolean hasDeletions() {
			return hasDeletions;
		}

		public boolean isOptimized() {
			return optimized;
		}

		protected void setHasDeletions(boolean hasDeletions) {
			this.hasDeletions = hasDeletions;
		}

		protected void setMaxDoc(long maxDoc) {
			this.maxDoc = maxDoc;
		}

		protected void setNumDocs(long numDocs) {
			this.numDocs = numDocs;
		}

		protected void setOptimized(boolean optimized) {
			this.optimized = optimized;
		}

	}

	public static final String TEST_INDEX_PATH = "./lucene";
	public static final String TEST_INDEX_FIELD = "title";

	private static final Version TEST_LUCENE_VERSION = Version.LUCENE_33;
	private static final Analyzer ANALYZER = new StandardAnalyzer(TEST_LUCENE_VERSION);
	private static final File TEST_INDEX_DIR = new File(TEST_INDEX_PATH);

	private static LocalServiceTestHelper helper;

	public static Directory createTestDirectory() throws Exception {
		return new SimpleFSDirectory(TEST_INDEX_DIR);
	}

	public static void delete(Directory dir, String query) throws Exception {

		IndexReader reader = IndexReader.open(dir, false);

		TopDocs search = TestUtil.search(dir, query);

		for (ScoreDoc doc : search.scoreDocs) {
			reader.deleteDocument(doc.doc);
		}

		reader.close();

	}

	public static IndexStatus getStatus(Directory dir) throws Exception {

		IndexReader reader = IndexReader.open(dir);

		IndexStatus status = new IndexStatus();
		status.setHasDeletions(reader.hasDeletions());
		status.setOptimized(reader.isOptimized());
		status.setNumDocs(reader.numDocs());
		status.setMaxDoc(reader.maxDoc());

		reader.close();

		return status;

	}

	public static void optimize(Directory dir) throws Exception {

		IndexWriterConfig config = new IndexWriterConfig(TEST_LUCENE_VERSION, ANALYZER);
		IndexWriter writer = new IndexWriter(dir, config);
		writer.optimize();
		writer.close();

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

	}

	public static void tearDown() throws Exception {

		helper.tearDown();

		FileUtils.deleteDirectory(TEST_INDEX_DIR);

	}

	public static void write(Directory dir, String... texts) throws Exception {

		IndexWriterConfig config = new IndexWriterConfig(TEST_LUCENE_VERSION, ANALYZER);
		IndexWriter writer = new IndexWriter(dir, config);

		for (String text : texts) {

			Document doc = new Document();
			doc.add(new Field(TEST_INDEX_FIELD, text, Field.Store.YES, Field.Index.ANALYZED));
			writer.addDocument(doc);

		}

		writer.close();

	}

}
