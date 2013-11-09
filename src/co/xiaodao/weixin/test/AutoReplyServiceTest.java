package co.xiaodao.weixin.test;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import co.xiaodao.weixin.db.pojo.Question;
import co.xiaodao.weixin.db.util.BaseDBUtil;

import co.xiaodao.weixin.util.AutoReplyUtil;
import co.xiaodao.weixin.util.WeixinUtil;

/**
 * 智能应答服务
 * 
 * @author liufeng
 * @date 2012-12-28
 * 
 */
public class AutoReplyServiceTest {
	private static Logger log = LoggerFactory
			.getLogger(AutoReplyServiceTest.class);

	public static String FILE_PATH = "D:\\lucene\\index";

	/**
	 * 获取所有的问题
	 * 
	 * @return
	 */
	public static List<Question> getQuestions() {
		List<Question> questionList = new ArrayList<Question>();
		Connection conn = BaseDBUtil.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String checkSql = "select * from tb_question";
		try {
			pstmt = conn.prepareStatement(checkSql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				questionList.add(new Question(rs.getString("Id"), rs
						.getString("category"), rs.getString("question"), rs
						.getString("answer")));
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			BaseDBUtil.closeCon(conn);
		}
		return questionList;
	}

	/**
	 * 从索引中根据关键词检索
	 */
	@SuppressWarnings("deprecation")
	public static String searchTest(String keyWord) {
		long startTime = System.currentTimeMillis();

		String answer = WeixinUtil.MSG_CONTENT_OTHER_TYPE;
		try {
			Directory directory = FSDirectory.open(new File(FILE_PATH));
			// Directory directory = FSDirectory.open(new File(
			// AutoReplyUtil.PATH_INDEX));
			IndexReader reader = IndexReader.open(directory);
			IndexSearcher searcher = new IndexSearcher(reader);

			QueryParser parser = new QueryParser(Version.LUCENE_40,
					AutoReplyUtil.FIELD_QUESTION, new IKAnalyzer(true));
			Query query = parser.parse(QueryParser.escape(keyWord));
			TopDocs topDocs = searcher.search(query, 1);
			ScoreDoc[] scoreDoc = topDocs.scoreDocs;

			for (ScoreDoc sd : scoreDoc) {
				Document d = searcher.doc(sd.doc);
				answer = d.get(AutoReplyUtil.FIELD_ANSWER);

				System.out.println(d.get(AutoReplyUtil.FIELD_QUESTION));
			}

			reader.close();
			directory.close();
		} catch (Exception e) {
			log.error("{}", e);
		}

		long endTime = System.currentTimeMillis();
		System.out.println("检索用时 " + (endTime - startTime) / 1000.0 + " 秒");

		return answer;
	}

	/**
	 * 创建索引
	 */
	public static void indexTest() {
		System.out.println("正在创建索引...");
		Directory directory = null;
		IndexWriter indexWriter = null;
		Analyzer analyzer = new IKAnalyzer(true);
		try {
			// 创建Directory,暂存索引文件
			directory = FSDirectory.open(new File(FILE_PATH));
			// directory = FSDirectory.open(new File(AutoReplyUtil.PATH_INDEX));
			// 创建IndexWriter
			IndexWriterConfig iwConfig = new IndexWriterConfig(
					Version.LUCENE_40, analyzer);
			indexWriter = new IndexWriter(directory, iwConfig);
			// 创建Document对象
			Document doc = null;
			// 为Document添加Field

			List<Question> questionList = getQuestions();
			int indexId = 1;
			for (Question question : questionList) {
				System.out.println("===>创建索引ID=" + indexId);
				doc = new Document();
				doc.add(new TextField(AutoReplyUtil.FIELD_ID, question.getId(),
						Field.Store.YES));
				doc.add(new StringField(AutoReplyUtil.FIELD_CATEGORY, question
						.getCategory(), Field.Store.YES));
				doc.add(new TextField(AutoReplyUtil.FIELD_QUESTION, question
						.getQuestion(), Field.Store.YES));
				doc.add(new StringField(AutoReplyUtil.FIELD_ANSWER, question
						.getAnswer(), Field.Store.YES));
				// 通过IndexWriter添加文档到索引中
				indexWriter.addDocument(doc);
				indexId++;
			}
			indexWriter.commit();
			indexWriter.close();
			directory.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("创建索引完成！");
	}

	public static void main(String[] args) {
		indexTest();
		// System.out.println(searchTest("百合"));
	}

}
