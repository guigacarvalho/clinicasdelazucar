package com.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Article {

	/*
	  public static final String TABLE_ARTICLES = "articles";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_ARTICLE_ID = "articleId";
	  public static final String COLUMN_CATEGORY_ID = "categoryId";
	  public static final String COLUMN_DATE = "date";
	  public static final String COLUMN_TITLE = "title";
	  public static final String COLUMN_CONTENT = "content";
	  public static final String COLUMN_FAV = "numberOfFavorites";
	  public static final String COLUMN_PICTURE_URL = "pictureUrl";*/
	private long id;
	private String articleId;
	private String categoryId;
	private String date;
	private String title;
	private String content;
	private String numberOfFavorites;
	private String pictureUrl;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getNumberOfFavorites() {
		return numberOfFavorites;
	}
	public void setNumberOfFavorites(String numberOfFavorites) {
		this.numberOfFavorites = numberOfFavorites;
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	
	
	public String getFormattedDate(SimpleDateFormat sFormatter, SimpleDateFormat dFormatter){
		
		try {
			
			Date fDate =  sFormatter.parse(date);
			//System.out.println(fDate);
			return dFormatter.format(fDate);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		return "";
	}
	

}
