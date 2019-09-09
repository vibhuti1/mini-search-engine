package output;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

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
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class doPost {
	HashMap<Object, Object> hmap = new HashMap<Object,Object>();
	
	public HashMap<Object, Object> getQueryResult(String value) throws IOException,ParseException { 
		StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
		String inputFilePath = "C:\\Users\\Vibhuti\\eclipse-workspace\\index";

		Directory directory = FSDirectory.open(Paths.get(inputFilePath));
		IndexWriterConfig config = new IndexWriterConfig(standardAnalyzer);
		config.setOpenMode(OpenMode.CREATE);
		
		// Search for the query given by the user
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		QueryParser parser = new QueryParser("content", standardAnalyzer);
		Query query = parser.parse(value);
		TopDocs results = searcher.search(query,50);
		
		//Parse the result to obtain relevant URL and content information
		
		for (ScoreDoc sd : results.scoreDocs) {
			Document d = searcher.doc(sd.doc);
			hmap.put(d.get("url"), d.get("content").substring(0, 250));
			}
		return hmap;
}
	
}
