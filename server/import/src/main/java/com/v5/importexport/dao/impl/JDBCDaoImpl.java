package com.v5.importexport.dao.impl;

import com.v5.importexport.dao.JDBCDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class JDBCDaoImpl extends JdbcBaseDao implements JDBCDao {
   
	@Autowired
	private BaseDao<DataModelMember> memberDao;
	
    @Override
    public Object ExecuteScalar(String sql)
    {
        Object obj = "";
        List<Map<String,Object>> rows = this.sysDbJdbcTemplate.queryForList(sql);
        if(rows.size() > 0)
        {
            Map<String,Object> map = rows.get(0);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                obj = entry.getValue();
            }
        }
    	
    	return obj != null ? obj.toString() : "";
    }
    
    @Override
    public int[] ExecuteNoQuery(List<String> lstSql) 
    {
    	int size = lstSql.size();
    	int[] counts = this.sysDbJdbcTemplate.batchUpdate(lstSql.toArray(new String[size]));
    	
    	return counts;
    }
    
    @Override
    public int ExecuteNoQuery(String sql)
    {
    	int count = this.sysDbJdbcTemplate.update(sql);
    	return count;
    }
    
    @Override
    public List<Map<String,Object>> ExecuteQuery(String sql) 
    {
    	List<Map<String,Object>> rows = this.sysDbJdbcTemplate.queryForList(sql);
    	
    	return rows;
    }
    
    @Override
    public List<DataModelMember> GetModelMembers(String tableName)
    {
    	return memberDao.list("modelName", tableName, DataModelMember.class);
    }
}
