package com.rainfe.tdm.fasttdm.dao;



import com.rainfe.mapletr.dmt.domainmodel.datamodel.domain.DataModelMember;

import java.util.List;
import java.util.Map;

public interface JDBCDao {

    Object ExecuteScalar(String sql);

    int ExecuteNoQuery(String sql);

    int[] ExecuteNoQuery(List<String> lstSql);

    List<Map<String, Object>> ExecuteQuery(String sql);

    List<DataModelMember> GetModelMembers(String tableName);
}
