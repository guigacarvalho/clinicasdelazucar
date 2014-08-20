package com.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ClinicasSQLiteHelper extends SQLiteOpenHelper {

	  public static final String TABLE_ARTICLES = "articles";
	  public static final String TABLE_ARTICLE = "article";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_ARTICLE_ID = "articleId";
	  public static final String COLUMN_CATEGORY_ID = "categoryId";
	  public static final String COLUMN_DATE = "date";
	  public static final String COLUMN_TITLE = "title";
	  public static final String COLUMN_CONTENT = "content";
	  public static final String COLUMN_FAV = "numberOfFavorites";
	  public static final String COLUMN_PICTURE_URL = "pictureUrl";
	  public static final String COLUMN_CREATED_AT = "created_at";
	  
	  private static final String DATABASE_NAME = "articles.db";
	  private static final int DATABASE_VERSION = 2;
	  
	  private static final String ARTICLES_TABLE_CREATE = "create table "
		      + TABLE_ARTICLES + "(" + COLUMN_ID
		      + " integer primary key autoincrement, " + COLUMN_ARTICLE_ID
		      + " text not null unique, "+COLUMN_CATEGORY_ID + " text not null, "+COLUMN_DATE + " text not null, "
		      + COLUMN_TITLE + " text not null, "+ COLUMN_CONTENT + " text not null, " 
		      + COLUMN_FAV + " text not null, " + COLUMN_PICTURE_URL + " text not null, created_at DATETIME DEFAULT CURRENT_TIMESTAMP)";

	  public static final String ARTICLE_TABLE_CREATE = "create table " + TABLE_ARTICLE +"("+ COLUMN_CONTENT + " text not null, "
			  + COLUMN_ARTICLE_ID  + " text not null unique, created_at DATETIME DEFAULT CURRENT_TIMESTAMP, "+ " FOREIGN KEY ("+COLUMN_ARTICLE_ID+") REFERENCES "+TABLE_ARTICLES
			  +" ("+COLUMN_ARTICLE_ID+"));";
	  
	public ClinicasSQLiteHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(ARTICLES_TABLE_CREATE);
		database.execSQL(ARTICLE_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLE);
		System.out.println("Upgrading DB from "+oldVersion +" to " + newVersion);
	    onCreate(db);
	}

}
