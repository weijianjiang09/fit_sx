package com.fit_sx.util;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.fit_sx.model.Admin;
import com.fit_sx.model.User;


public class SQLUtil {
//	public static void main(String[] args) {
//		Admin admin=new Admin();
//		admin.setId(8L);
//		admin.setName("a0002");
//		admin.setPassword("aa123456");
//		update("fit_admin", admin);
//	}
	
	
	public static boolean deleteById(String tableName,Object id) {
		String sql="delete from "+tableName+" where `id` = ?";
		return executeUpdate(sql, id)>0;
	}
	
	public static boolean deleteById(String tableName,Object id,String idKey) {
		String sql="delete from "+tableName+" where `"+idKey+"` = ?";
		return executeUpdate(sql, id)>0;
	}
	
	public static boolean update(String tableName,Object obj,String idKey) {
		StringBuffer sql = new StringBuffer("update ");
		sql.append(tableName);
		sql.append(" set ");
		Field[] fields=obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			if(!idKey.equals(field.getName())) {
				field.setAccessible(true);
				try {
					sql.append("`"+field.getName()+"` = ?,");
				} catch (IllegalArgumentException  e) {
					e.printStackTrace();
				}
			}
		}
		sql.delete(sql.length()-1, sql.length());
		sql.append(" where `"+idKey+"` = ?");
		List<Object> objs = new ArrayList<Object>();
		for(int i=0;i<fields.length;i++) {
			if(!idKey.equals(fields[i].getName())) {
				fields[i].setAccessible(true);
				try {
					objs.add(fields[i].get(obj));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			Field idField=obj.getClass().getDeclaredField(idKey);
			idField.setAccessible(true);
 			objs.add(idField.get(obj));
		} catch (NoSuchFieldException | SecurityException |IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return executeUpdate(sql.toString(), objs.toArray())>0;
	}
		
	public static boolean update(String tableName,Object obj) {
		StringBuffer sql = new StringBuffer("update ");
		sql.append(tableName);
		sql.append(" set ");
		Field[] fields=obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			if(!"id".equals(field.getName())) {
				field.setAccessible(true);
				try {
					sql.append("`"+field.getName()+"` = ?,");
				} catch (IllegalArgumentException  e) {
					e.printStackTrace();
				}
			}
		}
		sql.delete(sql.length()-1, sql.length());
		sql.append(" where `id` = ?");
		List<Object> objs = new ArrayList<Object>();
		for(int i=0;i<fields.length;i++) {
			if(!"id".equals(fields[i].getName())) {
				fields[i].setAccessible(true);
				try {
					objs.add(fields[i].get(obj));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			Field idField=obj.getClass().getDeclaredField("id");
			idField.setAccessible(true);
 			objs.add(idField.get(obj));
		} catch (NoSuchFieldException | SecurityException |IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return executeUpdate(sql.toString(), objs.toArray())>0;
	}
	
	/**
	 * 新增
	 * @param tableName
	 * @param obj
	 * @return
	 */
	public static boolean insert(String tableName,Object obj) {
		String sql = "insert into ";
		sql+=tableName;
		sql+="(";
		Field[] fields=obj.getClass().getDeclaredFields();
		StringBuffer values = new StringBuffer("values(");
		StringBuffer keys=new StringBuffer();
		for(int i=0;i<fields.length;i++) {
			if(!"id".equals(fields[i].getName())) {
				keys.append("`"+Util.HumpToUnderline(fields[i].getName())+"`,");
				values.append("?,");
			}
		}
		values.replace(values.length()-1, values.length(), ")");
		keys.replace(keys.length()-1, keys.length(), ")");
		sql+=keys.toString()+" "+values;
		List<Object> objs = new ArrayList<Object>();
		for(int i=0;i<fields.length;i++) {
			if(!"id".equals(fields[i].getName())) {
				fields[i].setAccessible(true);
				try {
					objs.add(fields[i].get(obj));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return executeUpdate(sql,objs.toArray())>0;
	}
	
	public static boolean insert(String tableName,Object obj,String idKey) {
		String sql = "insert into ";
		sql+=tableName;
		sql+="(";
		Field[] fields=obj.getClass().getDeclaredFields();
		StringBuffer values = new StringBuffer("values(");
		StringBuffer keys=new StringBuffer();
		for(int i=0;i<fields.length;i++) {
			if(!idKey.equals(fields[i].getName())) {
				keys.append("`"+Util.HumpToUnderline(fields[i].getName())+"`,");
				values.append("?,");
			}
		}
		values.replace(values.length()-1, values.length(), ")");
		keys.replace(keys.length()-1, keys.length(), ")");
		sql+=keys.toString()+" "+values;
		List<Object> objs = new ArrayList<Object>();
		for(int i=0;i<fields.length;i++) {
			if(!idKey.equals(fields[i].getName())) {
				fields[i].setAccessible(true);
				try {
					objs.add(fields[i].get(obj));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		
		return executeUpdate(sql, objs)>0;
	}

	public static int executeUpdate(String sql,Object ...obj) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		//连接
		Connection conn =null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/fit_game?useUnicode=true&characterEncoding=utf-8", "root", "jk123");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		PreparedStatement stm = null;
		try {
			stm=conn.prepareStatement(sql);
			for (int i = 0; i < obj.length; i++) {
				stm.setObject(i+1, obj[i]);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		

		int count=0;
		try {
			count = stm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			stm.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

}
