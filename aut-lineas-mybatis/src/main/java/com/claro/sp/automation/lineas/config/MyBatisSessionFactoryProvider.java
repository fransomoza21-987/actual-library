package com.claro.sp.automation.lineas.config;

import java.io.IOException;
import java.io.Reader;
import javax.sql.DataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

public class MyBatisSessionFactoryProvider {
    public SqlSessionFactory create(DataSource dataSource, String environmentId) throws IOException {
        try (Reader reader = Resources.getResourceAsReader("mybatis-config.xml")) {
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader);
            Environment environment = new Environment(environmentId, new JdbcTransactionFactory(), dataSource);
            factory.getConfiguration().setEnvironment(environment);
            return factory;
        }
    }
}
