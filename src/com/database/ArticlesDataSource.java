package com.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ArticlesDataSource {
	
	private SQLiteDatabase database;
	  private ClinicasSQLiteHelper dbHelper;

	  private String[] allColumns = { ClinicasSQLiteHelper.COLUMN_ID,
	      ClinicasSQLiteHelper.COLUMN_ARTICLE_ID, ClinicasSQLiteHelper.COLUMN_CATEGORY_ID, 
	      ClinicasSQLiteHelper.COLUMN_DATE, ClinicasSQLiteHelper.COLUMN_FAV,
	      ClinicasSQLiteHelper.COLUMN_PICTURE_URL, ClinicasSQLiteHelper.COLUMN_TITLE, 
	      ClinicasSQLiteHelper.COLUMN_CONTENT };
	  
	  public ArticlesDataSource(Context context){
		  dbHelper = new ClinicasSQLiteHelper(context, null, null, 0);
	  }

	  public void open() throws SQLException {
		    database = dbHelper.getWritableDatabase();
		  }

		  public void close() {
		    dbHelper.close();
		  }
	  
	  public void deleteArticle(Article article) {
		    long id = article.getId();
		    System.out.println("Article deleted with id: " + id);
		    database.delete(ClinicasSQLiteHelper.TABLE_ARTICLES, ClinicasSQLiteHelper.COLUMN_ID
		        + " = " + id, null);
		  }
	  
	  public void deleteArticles(){
		  database.delete(ClinicasSQLiteHelper.TABLE_ARTICLES, null, null);
	  }

		  public List<Article> getAllArticles() {
		    List<Article> articles = new ArrayList<Article>();

		    Cursor cursor = database.query(ClinicasSQLiteHelper.TABLE_ARTICLES,
		        allColumns, null, null, null, null, null);

		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		      Article Article = cursorToArticle(cursor);
		      articles.add(Article);
		      cursor.moveToNext();
		    }
		    // make sure to close the cursor
		    cursor.close();
		    return articles;
		  }
		  
		  public Article getArticle(String articleId) {
			  Article article = new Article();
			  Cursor cursor = database.query(ClinicasSQLiteHelper.TABLE_ARTICLES,
				        allColumns, ClinicasSQLiteHelper.COLUMN_ARTICLE_ID+"=?", new String[]{articleId}, null, null, null);

				    cursor.moveToFirst();
				    article = cursorToArticle(cursor);
			  return article;
		  }
		  
		  public List<Article> getArticles(String categoryId){
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

		  private Article cursorToArticle(Cursor cursor) {
		    Article article = new Article();
		    
		    // get index has to  match allcolumns array
		    article.setId(cursor.getLong(0));
		    article.setArticleId(cursor.getString(1));
		    article.setCategoryId(cursor.getString(2));
		    article.setDate(cursor.getString(3));
		    article.setNumberOfFavorites(cursor.getString(4));
		    article.setPictureUrl(cursor.getString(5));
		    article.setTitle(cursor.getString(6));
		    article.setContent(cursor.getString(7));
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
			  ContentValues values = new ContentValues();
			  values.put(ClinicasSQLiteHelper.COLUMN_ARTICLE_ID, article.getArticleId());
			  values.put(ClinicasSQLiteHelper.COLUMN_CATEGORY_ID, article.getCategoryId());
			  values.put(ClinicasSQLiteHelper.COLUMN_DATE, article.getDate());
			  values.put(ClinicasSQLiteHelper.COLUMN_TITLE, article.getTitle());
			  values.put(ClinicasSQLiteHelper.COLUMN_CONTENT, article.getContent());
			  values.put(ClinicasSQLiteHelper.COLUMN_FAV, article.getNumberOfFavorites());
			  values.put(ClinicasSQLiteHelper.COLUMN_PICTURE_URL, article.getPictureUrl());
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
				  database.insert(ClinicasSQLiteHelper.TABLE_ARTICLES, null, values);
			  
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
