package servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.SerialMergeScheduler;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.gae.datastore.DataStoreDirectory;
import org.apache.lucene.store.profile.Profile;
import org.apache.lucene.store.profile.ProfileDirectory;
import org.apache.lucene.util.Version;

public class LuceneUtils {

	public static final int						NUMBER_OF_DIRECTORYS	= 10;

	private static Map<Integer, Directory>		directorys				= new HashMap<Integer, Directory>();

	private static Map<Integer, IndexSearcher>	searchers				= new HashMap<Integer, IndexSearcher>();
	private static Map<Integer, IndexWriter>	writers					= new HashMap<Integer, IndexWriter>();

	private static final Analyzer				analyzer				= new StandardAnalyzer(Version.LUCENE_33);

	static {

		try {
			for (int i = 0; i < NUMBER_OF_DIRECTORYS; i++) {
				Directory directory = new ProfileDirectory(new DataStoreDirectory(getNameForDirectory(i)));
				directorys.put(i, directory);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public static QueryParser createQuery(String field) {
		return new QueryParser(Version.LUCENE_33, field, analyzer);
	}

	public static Analyzer getAnalyzer() {
		return analyzer;
	}

	public static Directory getDirectory(int index) throws IOException {
		return directorys.get(index);
	}

	public static String getNameForDirectory(int index) {
		return "Index" + index;
	}

	public static IndexSearcher getSearcher(int index) throws IOException {

		Profile prof = new Profile("getIndexSearcher").start();

		Directory directory = getDirectory(index);

		IndexSearcher result = null;

		synchronized (directory) {
			if (!searchers.containsKey(index)) {
				result = createSearcher(directory, index);
				searchers.put(index, result);
			} else {
				result = searchers.get(index);
			}
		}

		prof.end().log();

		return result;

	}

	public static IndexWriter getWriter(int index) throws IOException {

		Profile prof = new Profile("getIndexWriter").start();

		Directory directory = getDirectory(index);

		IndexWriter result = null;

		synchronized (directory) {
			if (!writers.containsKey(index)) {
				result = createWriter(directory, index);
				writers.put(index, result);
			} else {
				result = writers.get(index);
			}
		}

		prof.end().log();

		return result;

	}

	public static void resetSearcher(int index) {

		Profile prof = new Profile("resetSearcher").start();

		try {

			Directory directory = getDirectory(index);

			synchronized (directory) {

				if (searchers.containsKey(index)) {
					IndexSearcher searcher = searchers.remove(index);
					searcher.close();
				}

			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		prof.end().log();

	}

	public static void resetWriter(int index) {

		Profile prof = new Profile("resetWriter").start();

		try {

			Directory directory = getDirectory(index);

			synchronized (directory) {

				if (writers.containsKey(index)) {
					IndexWriter writer = writers.remove(index);
					writer.close();
				}

			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		prof.end().log();

	}

	public static void writeTexts(IndexWriter writer, String texts, int index) throws IOException {

		int i = 1;

		String[] fields = texts.split("[\\r\\n]+");

		for (String text : fields) {

			String id = System.currentTimeMillis() + "-" + index + "-" + i;

			Document doc = new Document();
			doc.add(new Field("id", id, Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field("text", text, Field.Store.YES, Field.Index.ANALYZED));

			writer.addDocument(doc);

			i++;

		}

	}

	private static IndexSearcher createSearcher(Directory directory, int index) {
		try {
			return new IndexSearcher(directory, true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static IndexWriter createWriter(Directory directory, int index) {
		try {
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_33, getAnalyzer());
			iwc.setMergeScheduler(new SerialMergeScheduler());
			return new IndexWriter(directory, iwc);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private LuceneUtils() {
		// TODO Auto-generated constructor stub
	}

}
