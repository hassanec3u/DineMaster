package com.example.demo.service;


import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.Term;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class IndexService {

    private final IndexWriter indexWriter;
    private DirectoryReader directoryReader;
    private IndexSearcher indexSearcher;
    private final QueryParser reviewQueryParser;

    public IndexService () throws IOException {
        String indexDirectoryPath = "reviewIndex";
        Path path = Paths.get(indexDirectoryPath);
        Directory index = FSDirectory.open(path);

        StandardAnalyzer analyzer = new StandardAnalyzer();
        this.reviewQueryParser = new QueryParser("reviewText", analyzer);

        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        this.indexWriter = new IndexWriter(index, indexWriterConfig);

        if (DirectoryReader.indexExists(index)) {
            this.directoryReader = DirectoryReader.open(index);
            this.indexSearcher = new IndexSearcher(directoryReader);
        } else {
            // si l'index n'existe pas on le crée
            this.indexWriter.commit();
            this.directoryReader = DirectoryReader.open(index);
            this.indexSearcher = new IndexSearcher(directoryReader);
        }
    }

    public void indexReview(Long evaluationID, String reviewText) {
        try {
            Document document = new Document();
            document.add(new StringField("evaluationID", String.valueOf(evaluationID), Field.Store.YES));
            document.add(new TextField("reviewText", reviewText, Field.Store.YES));


            //affiche le contenu du document

            this.indexWriter.addDocument(document);
            this.indexWriter.commit();

            DirectoryReader newDirectoryReader = DirectoryReader.openIfChanged(this.directoryReader);
            if (newDirectoryReader != null) {
                this.directoryReader.close();
                this.directoryReader = newDirectoryReader;
                this.indexSearcher = new IndexSearcher(this.directoryReader);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> searchReviewsByKeywords( String keywords) {
        try {
            Query reviewQuery = reviewQueryParser.parse(keywords);

            TopDocs topDocs = this.indexSearcher.search(reviewQuery, 10);

            return Arrays.stream(topDocs.scoreDocs)
                    .map(scoreDoc -> {
                        try {

                            return this.indexSearcher.storedFields().document(scoreDoc.doc).getField("evaluationID").stringValue();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteReview(Long evaluationID) {
        try {
            Query idQuery = new TermQuery(new Term("evaluationID", String.valueOf(evaluationID)));

            indexWriter.deleteDocuments(idQuery);
            indexWriter.commit();

            DirectoryReader newDirectoryReader = DirectoryReader.openIfChanged(this.directoryReader);
            if (newDirectoryReader != null) {
                this.directoryReader.close();
                this.directoryReader = newDirectoryReader;
                this.indexSearcher = new IndexSearcher(this.directoryReader);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
