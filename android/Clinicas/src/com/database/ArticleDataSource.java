package com.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ArticleDataSource {
	
	private SQLiteDatabase database;
	  private ClinicasSQLiteHelper dbHelper;

	  private String[] allColumns = { ClinicasSQLiteHelper.TABLE_ARTICLE+"."+ClinicasSQLiteHelper.COLUMN_CONTENT,
			  ClinicasSQLiteHelper.TABLE_ARTICLES+"."+ClinicasSQLiteHelper.COLUMN_DATE,
			  ClinicasSQLiteHelper.TABLE_ARTICLES+"."+ClinicasSQLiteHelper.COLUMN_PICTURE_URL, 
			  ClinicasSQLiteHelper.TABLE_ARTICLES+"."+ClinicasSQLiteHelper.COLUMN_TITLE, 
			  ClinicasSQLiteHelper.TABLE_ARTICLES+"."+ClinicasSQLiteHelper.COLUMN_ARTICLE_ID ,
			  ClinicasSQLiteHelper.TABLE_ARTICLE+"."+ClinicasSQLiteHelper.COLUMN_ARTICLE_ID 
	       };
	  
	  public ArticleDataSource(Context context){
		  dbHelper = new ClinicasSQLiteHelper(context, null, null, 0);
	  }

	  public void open() throws SQLException {
		    database = dbHelper.getWritableDatabase();
		  }

		  public void close() {
		    dbHelper.close();
		  }
	  
	  public void deleteArticle(String articleId) {
		    
		    
		    database.delete(ClinicasSQLiteHelper.TABLE_ARTICLE, ClinicasSQLiteHelper.COLUMN_ARTICLE_ID
		        + " = " + articleId, null);
		  }
	  
	  public void deleteArticles(){
		  database.delete(ClinicasSQLiteHelper.TABLE_ARTICLE, null, null);
	  }

		
		  
		  public Article getArticle(String articleId) {
			  Article article = new Article();
			  String rawQuery = "select "+ allColumns[0] +", " + allColumns[1] +", " + allColumns[2]+", " + allColumns[3] + ", " + allColumns[4] +
					  " from "+ClinicasSQLiteHelper.TABLE_ARTICLE+ " inner join " + ClinicasSQLiteHelper.TABLE_ARTICLES
					  +" on "+allColumns[5] +" = " +allColumns[4]
					  + " where "+allColumns[4]+" = ?";
			  Cursor cursor = database.rawQuery(rawQuery ,new String[]{articleId});
			 
					  //database.query(ClinicasSQLiteHelper.TABLE_ARTICLE,
				        //allColumns, ClinicasSQLiteHelper.COLUMN_ARTICLE_ID+"=?", new String[]{articleId}, null, null, null);

			    cursor.moveToFirst();
			    article = cursorToArticle(cursor);
			  return article;
		  }
		  
		  public List<Article> getArticles(String categoryId){
			  System.out.println("category id:"+ categoryId);
			  List<Article> articles = new ArrayList<Article>();
			  Cursor cursor = database.query(ClinicasSQLiteHelper.TABLE_ARTICLES,
				        allColumns, ClinicasSQLiteHelper.COLUMN_CATEGORY_ID+"=?", new String[]{categoryId}, null, null, null);
			  		
				    cursor.moveToFirst();
				    while (!cursor.isAfterLast()) {
				      Article article = cursorToArticle(cursor);
				      articles.add(article);
				      
				     /* System.out.println("-------------------------------------------------");
					  System.out.println("IN DATA SOURCE :: GET ARTICLE");
					  System.out.println("-------------------------------------------------");
					  System.out.println("Aid:"+article.getArticleId());
						System.out.println("CID:"+article.getCategoryId());
						System.out.println("Content:"+article.getContent());
						System.out.println("date:"+article.getDate());
						System.out.println(article.getId());
						System.out.println("Favs:"+article.getNumberOfFavorites());
						System.out.println("Pic:"+article.getPictureUrl());
						System.out.println("Title:"+article.getTitle());
						System.out.println("-------------------------------------------------");*/
				      
				      cursor.moveToNext();
				    }
				    // make sure to close the cursor
				    cursor.close();
				    return articles;
		  }
		  
		  public boolean articleExists(String articleId){
			  
			  
			  Cursor cursor = database.query(ClinicasSQLiteHelper.TABLE_ARTICLE,new String[]{ allColumns[0]}, ClinicasSQLiteHelper.COLUMN_ARTICLE_ID+"=?",new String[]{articleId}, null, null, null);
			  return cursor.getCount()>0;
		  }

		  private Article cursorToArticle(Cursor cursor) {
		    Article article = new Article();
		    
		    // get index has to  match allcolumns array
		    article.setContent(cursor.getString(0));
		    article.setDate(cursor.getString(1));
		    article.setPictureUrl(cursor.getString(2));
		    article.setTitle(cursor.getString(3));
		    article.setArticleId(cursor.getString(4));
		    return article;
		  }
		  /*
		  public static final String COLUMN_ARTICLE_ID = "articleId";
		  public static final String COLUMN_CATEGORY_ID = "categoryId";
		  public static final String COLUMN_DATE = "date";
		  public static final String COLUMN_TITLE = "title";
		  public static final String COLUMN_CONTENT = "content";
		  public static final String COLUMN_FAV = "numberOfFavorites";
		  public static final String COLUMN_PICTURE_URL = "pictureUrl";*/
		  public void createArticle(Article article){
			  System.out.println("called create article");
			  ContentValues values = new ContentValues();
			  values.put(ClinicasSQLiteHelper.COLUMN_ARTICLE_ID, article.getArticleId());
			  
			  values.put(ClinicasSQLiteHelper.COLUMN_CONTENT, article.getContent());
			  
/*			  System.out.println("-------------------------------------------------");
			  System.out.println("IN DATA SOURCE");
			  System.out.println("-------------------------------------------------");
			  System.out.println("Aid:"+article.getArticleId());
				System.out.println("CID:"+article.getCategoryId());
				System.out.println("Content:"+article.getContent());
				System.out.println("date:"+article.getDate());
				System.out.println(article.getId());
				System.out.println("Favs:"+article.getNumberOfFavorites());
				System.out.println("Pic:"+article.getPictureUrl());
				System.out.println("Title:"+article.getTitle());
				System.out.println("-------------------------------------------------");*/
			  try{
				  /*long insertId = */
				  database.insert(ClinicasSQLiteHelper.TABLE_ARTICLE, null, values);
				  
			  /*Cursor cursor = database.query(ClinicasSQLiteHelper.TABLE_ARTICLES,
				        allColumns, ClinicasSQLiteHelper.COLUMN_ID + " = " + insertId, null,
				        null, null, null);
				    cursor.moveToFirst();
				    Article newArticle = cursorToArticle(cursor);
				    cursor.close();*/
				    
	/*			    System.out.println("-------------------------------------------------");
					  System.out.println("IN DATA SOURCE after INSERT");
					  System.out.println("-------------------------------------------------");
					  System.out.println("Aid:"+article.getArticleId());
						System.out.println("CID:"+article.getCategoryId());
						System.out.println("Content:"+article.getContent());
						System.out.println("date:"+article.getDate());
						System.out.println(article.getId());
						System.out.println("Favs:"+article.getNumberOfFavorites());
						System.out.println("Pic:"+article.getPictureUrl());
						System.out.println("Title:"+article.getTitle());
						System.out.println("-------------------------------------------------");*/
			  }
			  catch(Exception e){
				  e.printStackTrace();
			  }
		  }
}
