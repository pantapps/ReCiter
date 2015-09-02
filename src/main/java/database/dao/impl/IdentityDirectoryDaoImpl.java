package database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DbConnectionFactory;
import database.dao.IdentityDirectoryDao;
import database.model.IdentityDirectory;
/**
 * 
 * @author htadimeti
 *
 */
public class IdentityDirectoryDaoImpl implements IdentityDirectoryDao{
	/**
	 * 
	 */
	public List<IdentityDirectory> getIdentityDirectoriesByCwid(String cwid){
		List<IdentityDirectory> list = new ArrayList<IdentityDirectory>();
		
		Connection con = DbConnectionFactory.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "SELECT id,recordType,cwid, givenName,middleName, surname from rc_identity_directory where cwid=?";
		try{
			pst=con.prepareStatement(sql);
			pst.setString(1, cwid);
			rs=pst.executeQuery();
			while(rs.next()){
				IdentityDirectory directory = new IdentityDirectory();
				directory.setId(rs.getInt(1));
				directory.setRecordType(rs.getString(2));
				directory.setCwid(rs.getString(3));
				directory.setGivenName(rs.getString(4));
				directory.setMiddleName(rs.getString(5));
				directory.setSurname(rs.getString(6));
				list.add(directory);
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return list;
	}
}
