package br.com.faturaweb.fatura;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;


public class DataSourceConfiguration {

    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
        dataSourceBuilder.url("jdbc:mysql://localhost:3306/fatura");
        dataSourceBuilder.username("root");
        dataSourceBuilder.password("202649");
        return dataSourceBuilder.build();
    }
}
