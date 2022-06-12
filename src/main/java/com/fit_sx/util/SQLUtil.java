package com.fit_sx.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.fit_sx.model.Admin;
import com.fit_sx.model.User;


public class SQLUtil {
	private static String url = "jdbc:mysql://127.0.0.1:3306/fit_game?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai" ;
	private static String username ="root";
	private static String password = "jk123";
	private static String connection = "com.mysql.cj.jdbc.Driver";
	public static void main(String[] args) {
//		List<Admin> list=find(Admin.class,"select * from fit_admin");
//		for (Admin admin : list) {
//			System.out.println(admin);
//		}
//		Admin admin=findById(Admin.class, "fit_admin", "a0002","name");
//		System.out.println(admin);
//		System.out.println(getQueryDecimal("select count(*) from fit_admin where name = ?","count(*)","a0005"));
	}

	/**
	 *
	 * @param sql    sql语句
	 * @param decimalKey 要查询的键
	 * @param objs 参数
	 * @return int count
	 */
	public static BigDecimal getQueryDecimal(String sql,String decimalKey,Object ...objs) {
		BigDecimal decimal=null;
		try {
			Class.forName(connection);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		//连接
		Connection conn =null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			stm=conn.prepareStatement(sql);
			for (int i = 0; i < objs.length; i++) {
				stm.setObject(i+1, objs[i]);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			rs = stm.executeQuery();

			while(rs.next()) {
				decimal=rs.getBigDecimal(decimalKey);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return decimal;

	}

	public static <E> E findById(Class cl,String tableName,Object id) {
		E obj=null;//如果findById为查询找，返回的应该是null而不是一个空对象
		try {
			Class.forName(connection);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		//连接
		Connection conn =null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			stm=conn.prepareStatement("select * from "+tableName+" where id=?");
			stm.setObject(1, id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			rs = stm.executeQuery();
			ResultSetMetaData rsmd = stm.getMetaData();
			Field[] fields = cl.getDeclaredFields();
			while(rs.next()) {
				try {
					obj=(E) cl.newInstance();
					//反射获取属性 根据属性名去获取数据库数据
					//数据库字段名，设置反射属性数据
//					for(int i=1;i<rsmd.getColumnCount()+1;i++) {
//						System.out.println(rsmd.getColumnName(i));
//					}
					for (Field field : fields) {
						String key = Util.HumpToUnderline(field.getName());
						field.setAccessible(true);
						if(field.getType().equals(Long.class)) {
							field.set(obj, rs.getLong(key));
						}else if(field.getType().equals(Integer.class)) {
							field.set(obj, rs.getInt(key));
						}else if(field.getType().equals(BigDecimal.class)) {
							field.set(obj, rs.getBigDecimal(key));
						}else if(field.getType().equals(String.class)) {
							field.set(obj, rs.getString(key));
						}
					}
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return obj;

	}

	public static <E> E findById(Class cl,String tableName,Object id,String idKey) {
		E obj=null;//如果findById为查询找，返回的应该是null而不是一个空对象
		try {
			Class.forName(connection);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		//连接
		Connection conn =null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			stm=conn.prepareStatement("select * from "+tableName+" where "+idKey+"=?");
			stm.setObject(1, id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			rs = stm.executeQuery();
			Field[] fields = cl.getDeclaredFields();
			while(rs.next()) {
				try {
					obj=(E) cl.newInstance();
					//反射获取属性 根据属性名去获取数据库数据
					//数据库字段名，设置反射属性数据
//					for(int i=1;i<rsmd.getColumnCount()+1;i++) {
//						System.out.println(rsmd.getColumnName(i));
//					}
					for (Field field : fields) {
						String key = Util.HumpToUnderline(field.getName());
						field.setAccessible(true);
						if(field.getType().equals(Long.class)) {
							field.set(obj, rs.getLong(key));
						}else if(field.getType().equals(Integer.class)) {
							field.set(obj, rs.getInt(key));
						}else if(field.getType().equals(BigDecimal.class)) {
							field.set(obj, rs.getBigDecimal(key));
						}else if(field.getType().equals(String.class)) {
							field.set(obj, rs.getString(key));
						}
					}
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return obj;

	}

	/**
	 *
	 * @param cl  类
	 * @param sql 语句
	 * @param objs 可变参数 Select * From tableName Where id=? And Name=? pstmt.setInt(1,100)就表示此处id=100
	 * @param <E>
	 * @return 返回list
	 */
	public static <E> List<E>  find(Class cl,String sql,Object ...objs){
		List<E> list=new ArrayList<E>();
		try {
			Class.forName(connection);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		//连接
		Connection conn =null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
//			System.out.println(sql);
		try {
			stm=conn.prepareStatement(sql);
			for (int i = 0; i < objs.length; i++) {
				stm.setObject(i+1, objs[i]);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			rs = stm.executeQuery();
//			ResultSetMetaData rsmd = stm.getMetaData();
			Field[] fields = cl.getDeclaredFields();
			while(rs.next()) {
				try {
					E e=(E) cl.newInstance();
					//反射获取属性 根据属性名去获取数据库数据
					//数据库字段名，设置反射属性数据
//					for(int i=1;i<rsmd.getColumnCount()+1;i++) {
//						System.out.println(rsmd.getColumnName(i));
//					}
					for (Field field : fields) {
						String key = Util.HumpToUnderline(field.getName());
						field.setAccessible(true);
						if(field.getType().equals(Long.class)) {
							field.set(e, rs.getLong(key));
						}else if(field.getType().equals(Integer.class)) {
							field.set(e, rs.getInt(key));
						}else if(field.getType().equals(BigDecimal.class)) {
							field.set(e, rs.getBigDecimal(key));
						}else if(field.getType().equals(String.class)) {
							field.set(e, rs.getString(key));
						}
					}

					list.add(e);
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}


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
			Class.forName(connection);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		//连接
		Connection conn =null;
		try {
			conn = DriverManager.getConnection(url, username, password);
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
