# Introduction #

Run Lucene in App Engine.

Example: http://lucene-gae-example.appspot.com/


## Index ##

Index texts in Lucene just like in the FileSystem Directory.

```
public class IndexServlet extends HttpServlet {

	private static final Version VERSION = Version.LUCENE_34;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String text = req.getParameter("text");

		DataStoreDirectory directory = new DataStoreDirectory();

		IndexWriterConfig iwc = new IndexWriterConfig(VERSION, new StandardAnalyzer(VERSION));
		iwc.setMergeScheduler(new SerialMergeScheduler());

		IndexWriter writer = new IndexWriter(directory, iwc);

		Document doc = new Document();
		doc.add(new Field("text", text, Field.Store.YES, Field.Index.ANALYZED));

		writer.addDocument(doc);

		writer.close();

	}

}

```

## Search ##

```
Search texts in Lucene using all Lucene Features.

public class SearchServlet extends HttpServlet {

	private static final Version VERSION = Version.LUCENE_34;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String q = req.getParameter("q");

		DataStoreDirectory directory = new DataStoreDirectory();

		IndexSearcher searcher = new IndexSearcher(directory, true);
		IndexReader reader = searcher.getIndexReader();

		Query query = parseQuery("text", q);

		TopDocs topDocs = searcher.search(query, 10);

		for (ScoreDoc doc : topDocs.scoreDocs) {
			Document document = reader.document(doc.doc);
			String docText = document.get("text");
			System.out.println("Founded Document " + doc.doc + " with text '" + docText + "'");
		}

		searcher.close();
		reader.close();

	}

	private Query parseQuery(String field, String q) {
		try {
			return new QueryParser(VERSION, "field", new StandardAnalyzer(VERSION)).parse(q);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

}
```