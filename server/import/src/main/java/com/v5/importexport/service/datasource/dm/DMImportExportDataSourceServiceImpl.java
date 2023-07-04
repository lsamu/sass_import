package com.v5.importexport.service.datasource.dm;


import com.v5.importexport.dao.JDBCDao;
import com.v5.importexport.domain.*;
import com.v5.importexport.enums.DataType;
import com.v5.importexport.enums.OptionType;
import com.v5.importexport.enums.SQLType;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class DMImportExportDataSourceServiceImpl extends DMImportExportDataSourceService {

    private static final Logger logger = Logger.getLogger(DMImportExportDataSourceServiceImpl.class);

    @Autowired
    private JDBCDao _jdbcDao;

//    @Autowired
//    private CommonHelperService commonHelperService;

    @Override
    public boolean addImportData(String keyTable, List<RowData> lstRowData, Map<String, String> mapExtendField) {
        try {
            String condition = "";
            List<String> lstSQL = new LinkedList<String>();
            for (RowData rowData : lstRowData) {
                List<String> lstFieldName = new LinkedList<String>();
                List<String> lstFieldValue = new LinkedList<String>();

                Set<ColumnData> setColumnData = rowData.getRowData();
                for (ColumnData columnData : setColumnData) {
                    String fieldName = columnData.getColumnField();
                    String fieldValue = columnData.getColumnValue();
                    String dataType = columnData.getDataType();
                    if (StringUtils.isNotBlank(fieldName)) {
                        String value = getValueByDataType(fieldValue, dataType);
                        lstFieldName.add(fieldName);
                        lstFieldValue.add(value);
                    }
                }

                if (mapExtendField != null && mapExtendField.size() > 0) {
                    for (String key : mapExtendField.keySet()) {
                        String fieldName = key;
                        String fieldValue = mapExtendField.get(key);
                        if (fieldValue.equals("sys_guid()")) {
                            fieldValue = UUID.randomUUID().toString();
                            if (keyTable.contains("_SYSHJXY_")) {
                                if (StringUtils.isBlank(condition)) {
                                    condition = "ID IN ('" + fieldValue + "'";
                                } else {
                                    condition = condition + ",'" + fieldValue + "'";
                                }
                            }
                        }
                        String value = getValueByDataType(fieldValue, "");
                        lstFieldName.add(fieldName);
                        lstFieldValue.add(value);
                    }
                }

                String fieldNames = StringUtils.join(lstFieldName, ',');
                String fieldValues = StringUtils.join(lstFieldValue, ',');
                String strSQL = "insert into " + keyTable + "(" + fieldNames + ")values(" + fieldValues + ")";
                lstSQL.add(strSQL);
            }

            if (lstSQL.size() > 0) {
                _jdbcDao.ExecuteNoQuery(lstSQL);
            }

//            if (keyTable.contains("_SYSHJXY_")) {
//                condition = condition + ")";
//                commonHelperService.CreateSysProjectTable(keyTable, condition, "insert");
//            }

            return true;
        } catch (Exception e) {
            logger.error("执行异常" + e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<TableColumn> getTableColumn(String keyTable) {
        try {
            List<TableColumn> lstTableColumn = new LinkedList<TableColumn>();
            String strSQL = "select id, model_name, member_name, datatype, member_length, member_decimal, isallowempty  from DMD_MODELMEMBERS where model_name = '" + keyTable + "'";
            List<Map<String, Object>> lstObject = _jdbcDao.ExecuteQuery(strSQL);
            if (lstObject.size() > 0) {
                for (Map<String, Object> map : lstObject) {
                    String tableName = map.get("model_name") != null ? map.get("model_name").toString() : "";
                    String columnName = map.get("member_name") != null ? map.get("member_name").toString() : "";
                    String dataType = map.get("datatype") != null ? map.get("datatype").toString() : "";
                    Integer dataLength = map.get("member_length") != null ? Integer.valueOf(map.get("member_length").toString()) : 0;
                    Integer dataScale = map.get("member_decimal") != null ? Integer.valueOf(map.get("member_decimal").toString()) : 0;
                    String isNull = map.get("isallowempty") != null ? map.get("isallowempty").toString() : "true";
                    String columnid = map.get("id") != null ? map.get("id").toString() : "";

                    TableColumn tableColumn = new TableColumn();
                    tableColumn.setTableName(tableName);
                    tableColumn.setColumnName(columnName);
                    tableColumn.setDataType(dataType);
                    tableColumn.setLength(dataLength);
                    tableColumn.setScale(dataScale);
                    tableColumn.setIsNull(isNull);
                    tableColumn.setColumnId(columnid);

                    lstTableColumn.add(tableColumn);
                }
            }

            return lstTableColumn;
        } catch (Exception e) {
            logger.error("执行异常" + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean checkRowDataUnique(String keyTable, Set<ColumnData> setColumnData, List<String> fieldList) {
        boolean isUnique = true;
        try {
            List<String> lstNameValue = new LinkedList<String>();
            for (String fieldName : fieldList) {
                Iterator<ColumnData> iterator = setColumnData.iterator();
                while (iterator.hasNext()) {
                    ColumnData columnData = iterator.next();
                    String columnField = columnData.getColumnField();
                    if (columnField.equals(fieldName)) {
                        String fieldValue = columnData.getColumnValue();
                        String dataType = columnData.getDataType();

                        if (StringUtils.isNotBlank(fieldValue)) {
                            if (dataType.equals("NumberType")) {
                                lstNameValue.add(fieldName + "=" + fieldValue);
                            } else {
                                lstNameValue.add(fieldName + "='" + fieldValue + "'");
                            }
                        } else {
                            lstNameValue.add(fieldName + " is null ");
                        }

                        break;
                    }
                }
            }

            String fieldNames = StringUtils.join(fieldList, ',');
            String nameValues = StringUtils.join(lstNameValue, " and ");

            String strSQL = "select " + fieldNames + " from " + keyTable + " where " + nameValues;
            List<Map<String, Object>> lstObject = _jdbcDao.ExecuteQuery(strSQL);
            if (lstObject.size() > 0) {
                isUnique = false;
            }

        } catch (Exception e) {
            logger.error("执行异常" + e.getMessage(), e);
        }

        return isUnique;
    }

    @Override
    public String checkCellData(String keyTable, ColumnData columnData) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<RowData> getExportData(String keyTable, List<String> lstQueryField, String condition, String orderby) {
        try {
            if (StringUtils.isBlank(condition)) {
                condition = " 1=1 ";
            }
            String strSQL = String.format("select %s from %s where %s", StringUtils.join(lstQueryField, ","), keyTable, condition);
            if (StringUtils.isNotBlank(orderby)) {
                strSQL += " order by " + orderby;
            }

            List<RowData> lstRowData = new LinkedList<RowData>();
            List<Map<String, Object>> lstObject = _jdbcDao.ExecuteQuery(strSQL);
            if (lstObject.size() > 0) {
                int rowIndex = 0;
                for (Map<String, Object> map : lstObject) {
                    RowData rowData = new RowData();
                    Set<ColumnData> setColumnData = new HashSet<ColumnData>();
                    int columnIndex = 0;
                    for (Map.Entry kv : map.entrySet()) {
                        String fieldName = kv.getKey().toString();
                        String fieldValue = kv.getValue() != null ? kv.getValue().toString() : "";

                        ColumnData columnData = new ColumnData();
                        columnData.setColumnField(fieldName);
                        columnData.setColumnValue(fieldValue);
                        columnData.setColumnIndex(columnIndex);

                        setColumnData.add(columnData);

                        ++columnIndex;
                    }

                    rowData.setRowData(setColumnData);
                    rowData.setRowIndex(rowIndex);
                    lstRowData.add(rowData);

                    ++rowIndex;
                }
            }

            return lstRowData;
        } catch (Exception e) {
            logger.error("执行异常" + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public String getValueBySQL(String sql, Map<String, String> mapParameters) {
        String result = "";
        try {
            if (mapParameters != null) {
                for (Map.Entry<String, String> entry : mapParameters.entrySet()) {
                    String parameterName = entry.getKey();
                    String parameterValue = entry.getValue();

                    sql = sql.replace(parameterName, parameterValue);
                }
            }

            Object obj = _jdbcDao.ExecuteScalar(sql);
            if (obj != null) {
                result = obj.toString();
            }
        } catch (Exception e) {
            logger.error("执行异常" + e.getMessage(), e);
        }

        return result;
    }

    @Override
    public String getSQL(DynamicSQL dynamicSQL) {
        String strSQL = "";
        try {
            String keyTable = dynamicSQL.getKeyTable();
            List<String> lstQueryField = dynamicSQL.getQueryFieldList();
            List<NameValue> lstNameValue = dynamicSQL.getNameValueList();
            Set<Condition> setCondition = dynamicSQL.getSetCondition();

            SQLType sqlType = dynamicSQL.getType();
            switch (sqlType) {
                case Retrieve:
                    strSQL = getQuerySQL(keyTable, lstQueryField, setCondition);
                    break;
                case Create:
                    strSQL = getCreateSQL(keyTable, lstNameValue);
                    break;
                case Update:
                    strSQL = getUpdateSQL(keyTable, lstNameValue, setCondition);
                    break;
                case Delete:
                    strSQL = getDeleteSQL(keyTable, setCondition);
                    break;
            }
        } catch (Exception e) {
            logger.error("执行异常" + e.getMessage(), e);
        }

        return strSQL;
    }

    @Override
    public boolean CheckTableColumnUnique(String tableName, String fieldName, String fieldValue) {
        try {
            String sql = String.format("select distinct %s from %s where %s = '%s'", fieldName, tableName, fieldName, fieldValue);
            List<Map<String, Object>> lstObj = _jdbcDao.ExecuteQuery(sql);
            return lstObj.size() > 0 ? false : true;
        } catch (Exception e) {
            logger.error("执行异常" + e.getMessage(), e);
        }

        return false;
    }

    @Override
    public boolean checkRowDataCrux(String keyTable, Set<ColumnData> setColumnData, List<String> keyFieldList) {
        boolean isUnique = true;
        try {
            List<String> lstNameValue = new LinkedList<String>();
            String keyFieldsStr = "";
            if (StringUtils.isNotBlank(keyTable)) {
                if (keyFieldList != null && keyFieldList.size() > 0) {
                    keyFieldsStr = StringUtils.join(keyFieldList, ",");
                    for (String name : keyFieldList) {
                        Iterator<ColumnData> iterator = setColumnData.iterator();
                        while (iterator.hasNext()) {
                            ColumnData columnData = iterator.next();
                            String columnField = columnData.getColumnField();
                            if (columnField.equals(name)) {
                                String fieldValue = columnData.getColumnValue();
                                String dataType = columnData.getDataType();
                                if (dataType.equals("NumberType")) {
                                    lstNameValue.add(name + "=" + fieldValue);
                                } else {
                                    lstNameValue.add(name + "='" + fieldValue + "'");
                                }
                                break;
                            }
                        }
                    }
                }
            }

            if (lstNameValue.size() > 0) {
                String nameValues = StringUtils.join(lstNameValue, " and ");

                String strSQL = "select " + keyFieldsStr + " from " + keyTable + " where " + nameValues;
                List<Map<String, Object>> lstObject = _jdbcDao.ExecuteQuery(strSQL);
                if (lstObject.size() > 0) {
                    isUnique = false;
                }
            }

        } catch (Exception e) {
            logger.error("执行异常" + e.getMessage(), e);
        }

        return isUnique;
    }


    /***
     * 根据表名查询关键字段集合
     * @return
     */
    @Override
    public List<String> getCruxBysql(String keyTable) {
        List<String> lstFiledName = new LinkedList<String>();
        String sql = "SELECT DISTINCT ASSOCIATEDFIELD FROM FT_COMMON_TABLE_F WHERE ASSOCIATEDTABLENAME = '" + keyTable + "'";
        List<Map<String, Object>> lstObj = _jdbcDao.ExecuteQuery(sql);
        if (lstObj != null && lstObj.size() > 0) {
            for (Map<String, Object> map : lstObj) {
                String name = map.get("ASSOCIATEDFIELD") != null ? map.get("ASSOCIATEDFIELD").toString() : "";
                lstFiledName.add(name);
            }
        }
        return lstFiledName;
    }


    private String getQuerySQL(String keyTable, List<String> lstQueryField, Set<Condition> setCondition) {
        try {
            String strQueryField = "*";
            if (lstQueryField.size() > 0) {
                strQueryField = StringUtils.join(lstQueryField, ",");
            }

            String strCondition = getConditionSQL(setCondition);

            String strSQL = String.format("select %s from %s where %s ", strQueryField, keyTable, strCondition);

            return strSQL;
        } catch (Exception e) {
            logger.error("执行异常" + e.getMessage(), e);
        }

        return null;
    }

    private String getDeleteSQL(String keyTable, Set<Condition> setCondition) {
        try {
            String strCondition = getConditionSQL(setCondition);

            String strSQL = String.format("delete from %s where %s ", keyTable, strCondition);

            return strSQL;
        } catch (Exception e) {
            logger.error("执行异常" + e.getMessage(), e);
        }

        return null;
    }

    private String getCreateSQL(String keyTable, List<NameValue> lstNameValue) {
        try {
            if (lstNameValue.size() == 0) {
                return null;
            }

            List<String> lstName = new LinkedList<String>();
            List<String> lstValue = new LinkedList<String>();
            for (NameValue nv : lstNameValue) {
                String name = nv.getName();
                String value = nv.getValue();
                DataType dataType = nv.getDataType();

                value = getSQLValue(value, dataType);

                lstName.add(name);
                lstValue.add(value);
            }

            String strNames = StringUtils.join(lstName, ",");
            String strValues = StringUtils.join(lstValue, ",");

            String strSQL = String.format("insert into %s(%s)values(%s)", keyTable, strNames, strValues);

            return strSQL;
        } catch (Exception e) {
            logger.error("执行异常" + e.getMessage(), e);
        }

        return null;
    }


    private String getUpdateSQL(String keyTable, List<NameValue> lstNameValue, Set<Condition> setCondition) {
        try {
            if (lstNameValue.size() == 0) {
                return null;
            }

            List<String> lstNameValues = new LinkedList<String>();

            for (NameValue nv : lstNameValue) {
                String name = nv.getName();
                String value = nv.getValue();
                DataType dataType = nv.getDataType();

                value = getSQLValue(value, dataType);

                String strNameValue = String.format("%s = %s", name, value);
                lstNameValues.add(strNameValue);
            }

            String strNameValues = StringUtils.join(lstNameValues, ",");
            String strCondition = getConditionSQL(setCondition);

            String strSQL = String.format("update %s set %s where %s", keyTable, strNameValues, strCondition);

            return strSQL;
        } catch (Exception e) {
            logger.error("执行异常" + e.getMessage(), e);
        }

        return null;
    }

    private String getConditionSQL(Set<Condition> setCondition) {
        String strCondition = "";
        try {
            List<String> lstCondition = new LinkedList<String>();
            Iterator<Condition> iterator = setCondition.iterator();
            while (iterator.hasNext()) {
                String condition = "";
                Condition con = iterator.next();
                OptionType option = con.getOption();
                String name = con.getFieldName();
                String value = con.getFieldValue();
                DataType dataType = con.getDataType();
                switch (dataType) {
                    case string:
                        value = String.format("'%s'", value);
                        break;
                    case sql:
                    case number:
                        break;
                    case datetime:
                        name = String.format("to_char(%s,'yyyy-mm-dd HH:mi:ss')", name);
                        value = String.format("to_char('%s','yyyy-mm-dd HH:mi:ss')", value);
                        break;
                    case date:
                        name = String.format("to_char(%s,'yyyy-mm-dd')", name);
                        value = String.format("to_char('%s','yyyy-mm-dd')", value);
                        break;
                    case empty:
                        value = null;
                        break;
                    default:
                        value = String.format("'%s'", value);
                        break;
                }

                switch (option) {
                    case equals:
                        condition = String.format("%s = %s", name, value);
                        break;
                    case like:
                        condition = String.format("%s like '%%s%'", name, value);
                        break;
                    case in:
                        condition = String.format("%s in (%s)", name, value);
                        break;
                    case is:
                        condition = String.format("%s is %s", name, value);
                        break;
                    case greater:
                        condition = String.format("%s > %s", name, value);
                        break;
                    case greaterorequals:
                        condition = String.format("%s >= %s", name, value);
                        break;
                    case less:
                        condition = String.format("%s < %s", name, value);
                        break;
                    case lessorequals:
                        condition = String.format("%s <= %s", name, value);
                        break;
                    default:
                        condition = String.format("%s %s %s", name, option, value);
                        break;
                }

                if (StringUtils.isNotBlank(condition)) {
                    lstCondition.add(condition);
                }
            }

            if (lstCondition.size() > 0) {
                strCondition = StringUtils.join(lstCondition, " and ");
            }
        } catch (Exception e) {
            logger.error("执行异常" + e.getMessage(), e);
        }

        return strCondition;
    }

    private String getSQLValue(String value, DataType dataType) {
        try {
            switch (dataType) {
                case string:
                    value = String.format("'%s'", value);
                    break;
                case sql:
                case number:
                    break;
                case datetime:
                    value = String.format("to_date('%s','yyyy-mm-dd HH:mi:ss')", value);
                    break;
                case date:
                    value = String.format("to_date('%s','yyyy-mm-dd')", value);
                    break;
                case empty:
                    value = null;
                    break;
                default:
                    value = String.format("'%s'", value);
                    break;
            }
        } catch (Exception e) {
            logger.error("执行异常" + e.getMessage(), e);
        }

        return value;
    }

    private String getValueByDataType(String value, String dataType) {
        String result = "";
        if (StringUtils.isNotBlank(value)) {
            switch (dataType) {
                case "VARCHAR2":
                    result = "'" + value.replace("'", "''") + "'";
                    break;
                case "TimestampType":
                    result = "to_timestamp('" + value + "','yyyy-mm-dd hh24:mi:ss')";
                case "DATE":
                case "DATETIME":
                    result = "to_date('" + value + "','yyyy-mm-dd hh24:mi:ss')";
                    break;
                case "BoolenType":
                    result = value;
                    break;
                case "NUMBER":
                    result = value;
                    break;
                default:
                    result = "'" + value.replace("'", "''") + "'";
                    break;
            }
        } else {
            result = "null";
        }

        return result;
    }
}
