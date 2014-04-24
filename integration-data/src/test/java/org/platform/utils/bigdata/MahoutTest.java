package org.platform.utils.bigdata;

import java.io.File;
import java.util.List;

import org.apache.hive.jdbc.HiveDataSource;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.junit.Test;
import org.platform.utils.common.resource.ResourceManager;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;


@SuppressWarnings("deprecation")
public class MahoutTest {
	
	private static final String path = ResourceManager.getAbsolutePath("mahout/intro.csv");

	@Test
	public void testUserBasedRecommenderByFile() throws Exception {
		DataModel dataModel = new FileDataModel(new File(path));
		UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(dataModel);
		UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(2, userSimilarity, dataModel);
		Recommender recommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, userSimilarity);
		List<RecommendedItem> recommendedItems = recommender.recommend(1, 1);
		for (RecommendedItem recommendedItem : recommendedItems) {
			System.out.println(recommendedItem);
		}
	}
	
	@Test
	public void testUserBasedRecommenderByDB() throws Exception {
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setURL("jdbc:mysql://localhost:3306/mahout?useUnicode=true&amp;characterEncoding=UTF-8");
		dataSource.setUser("mahout");
		dataSource.setPassword("mahout");
		JDBCDataModel dataModel = new MySQLJDBCDataModel(dataSource, 
				"movie_preferences", "userId", "movieId", "preference", "timestamp");
		UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(dataModel);
		UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(5, userSimilarity, dataModel);
		Recommender recommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, userSimilarity);
		List<RecommendedItem> recommendedItems = recommender.recommend(1, 5);
		for (RecommendedItem recommendedItem : recommendedItems) {
			System.out.println(recommendedItem);
		}
	}
	
	@Test
	public void testUserBasedRecommenderByHive() throws Exception {
		HiveDataSource dataSource = new HiveDataSource();
		//String url = "jdbc:hive://192.168.10.10:10000/default";
		JDBCDataModel dataModel = new MySQLJDBCDataModel(dataSource, 
				"movie_preferences", "userId", "movieId", "preference", "timestamp");
		UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(dataModel);
		UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(5, userSimilarity, dataModel);
		Recommender recommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, userSimilarity);
		List<RecommendedItem> recommendedItems = recommender.recommend(1, 5);
		for (RecommendedItem recommendedItem : recommendedItems) {
			System.out.println(recommendedItem);
		}
	}
	
	@Test
	public void testItemBasedRecommender() throws Exception {
		DataModel dataModel = new FileDataModel(new File(path));
		ItemSimilarity itemSimilarity = new PearsonCorrelationSimilarity(dataModel);
		Recommender recommender = new GenericItemBasedRecommender(dataModel, itemSimilarity);
		List<RecommendedItem> recommendedItems = recommender.recommend(1, 1);
		for (RecommendedItem recommendedItem : recommendedItems) {
			System.out.println(recommendedItem);
		}
	}
	
	@Test
	public void testSlopeOneRecommended() throws Exception {
		DataModel dataModel = new FileDataModel(new File(path));
		Recommender recommender = new SlopeOneRecommender(dataModel);
		List<RecommendedItem> recommendedItems = recommender.recommend(1, 1);
		for (RecommendedItem recommendedItem : recommendedItems) {
			System.out.println(recommendedItem);
		}
	}
	
}
