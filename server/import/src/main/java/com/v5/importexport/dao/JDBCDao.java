package com.v5.importexport.dao;

import java.util.List;
import java.util.Map;

public interface JDBCDao {

    Object ExecuteScalar(String sql);

    int ExecuteNoQuery(String sql);

    int[] ExecuteNoQuery(List<String> lstSql);

    List<Map<String, Object>> ExecuteQuery(String sql);

    List<DataModelMember> GetModelMembers(String tableName);
}
