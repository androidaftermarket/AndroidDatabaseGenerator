package com.android.gda.dbgenerator.processors.elements;

/**
 * Created by gdaAquino on 5/22/14.
 */
public class ColumnElement {

    private String columnName;

    private String columnType;

    private boolean unique;

    private boolean notNull;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ColumnElement{");
        sb.append("columnName='").append(columnName).append('\'');
        sb.append(", columnType='").append(columnType).append('\'');
        sb.append(", unique=").append(unique);
        sb.append(", notNull=").append(notNull);
        sb.append('}');
        return sb.toString();
    }
}
