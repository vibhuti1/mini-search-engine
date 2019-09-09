package output;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class HighlighterLucene {
	final int MAX_VALUE = 200;

	public static void main(String[] args) throws IOException, ParseException, InvalidTokenOffsetsException {
		// Load the index file and perform simple default retireval model and ranking algorithm
		
		HighlighterLucene re = new HighlighterLucene();
		StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
		Analyzer analyzer = new StandardAnalyzer();
		String inputFilePath = "C:\\Users\\Vibhuti\\eclipse-workspace\\index";

		Directory directory = FSDirectory.open(Paths.get(inputFilePath));
		IndexWriterConfig config = new IndexWriterConfig(standardAnalyzer);
		config.setOpenMode(OpenMode.CREATE);

		// Parse the query and get results from indexed documents.
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		QueryParser parser = new QueryParser("content", standardAnalyzer);
		Query query = parser.parse("Queensland");
		TopDocs results = searcher.search(query, 50);
		System.out.println("Results found-->" + results.totalHits);

		String content = "";
		int i = 0;
		HashMap<Object, Object> hmap = new HashMap<Object, Object>();
		for (ScoreDoc sd : results.scoreDocs) {
			Document d = searcher.doc(sd.doc);
			hmap.put(d.get("url"), d.get("content").substring(0, 250));
			content = d.get("content").substring(0, 250);
			TokenStream stream = TokenSources.getAnyTokenStream(reader, i, "content", analyzer);
			List<String> res = re.getHighlightString(stream, analyzer, content, query);
			System.out.println(res);
		}
		Set set = hmap.entrySet();
		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			Map.Entry mentry = (Map.Entry) iterator.next();
			System.out.print(mentry.getKey() + " :\n");
			System.out.println(mentry.getValue());
			System.out.println("\n");
		}

	}

	// Highlight the result
	@SuppressWarnings("deprecation")
	public static List<String> getHighlightString(TokenStream stream, Analyzer analyzer, String text, Query query)
			throws IOException, InvalidTokenOffsetsException {

		QueryScorer scorer = new QueryScorer(query);
		Formatter formatter = new SimpleHTMLFormatter();

		Highlighter highlighter = new Highlighter(formatter, scorer);

		Fragmenter fragmenter = new SimpleSpanFragmenter(scorer, 10);
		highlighter.setTextFragmenter(fragmenter);

		TokenStream tokenStream = TokenSources.getTokenStream("default", text, analyzer);
		List<String> highlight  = new ArrayList<String>();
		String[] target = highlighter.getBestFragments(stream, text, 10);
		for (String t : target) {
			System.out.println("=======================");
			System.out.println(t);
			highlight.add(t);
		}
		return highlight;
	}

}
