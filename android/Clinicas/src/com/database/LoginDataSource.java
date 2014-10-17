package com.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LoginDataSource {
	
	private SQLiteDatabase database;
	  private ClinicasSQLiteHelper dbHelper;
	  
	  private String[] allColumns = { ClinicasSQLiteHelper.TABLE_LOGIN+"."+ClinicasSQLiteHelper.COLUMN_LOGGED_IN,
			  ClinicasSQLiteHelper.TABLE_LOGIN+"."+ClinicasSQLiteHelper.COLUMN_USER_ID,
			  ClinicasSQLiteHelper.TABLE_LOGIN+"."+ClinicasSQLiteHelper.COLUMN_USER_NAME,
			  ClinicasSQLiteHelper.TABLE_LOGIN+"."+ClinicasSQLiteHelper.COLUMN_AUTH_TOKEN
	  };
	  
	  public LoginDataSource(Context context){
		  dbHelper = new ClinicasSQLiteHelper(context, null, null, 0);
	  }

	  public void open() throws SQLException {
		    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }
	  
	  public void deleteLogin() {
		    
		    
		    database.delete(ClinicasSQLiteHelper.TABLE_LOGIN, null, null);
	  }
	  
	  
	  public void storeLogin(User user){
		  ContentValues values = new ContentValues();
		  values.put(ClinicasSQLiteHelper.COLUMN_USER_ID, user.getUserID());
		  values.put(ClinicasSQLiteHelper.COLUMN_USER_NAME, user.getUserName());
		  values.put(ClinicasSQLiteHelper.COLUMN_AUTH_TOKEN, user.getAuthToken());
		  try{
		   long id = database.insert(ClinicasSQLiteHelper.TABLE_LOGIN, null, values);
		   System.out.println("insert id:"+ id);
		  }
		  catch(Exception e){
			  e.printStackTrace();
		  }
	  }
	  
	  public User getUser(){
		  Cursor cursor = database.query(ClinicasSQLiteHelper.TABLE_LOGIN,
			        allColumns, null, null, null, null, null);
		  User user = null;
		  System.out.println(cursor.getCount());
		  cursor.moveToFirst();
		  
			  user = new User();
			  user.setAuthToken(cursor.getString(3));
			  user.setUserName(cursor.getString(2));
			  user.setUserID(cursor.getString(1));
		  
		  
		  
		  return user;
		  		
	  }

}
