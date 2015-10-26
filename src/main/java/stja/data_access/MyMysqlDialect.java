package stja.data_access;

import org.hibernate.dialect.MySQL5InnoDBDialect;

/**
 * Created by Delth on 25.10.2015.
 */
public class MyMysqlDialect extends MySQL5InnoDBDialect {
    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }
}