package com.claro.sp.automation.lineas.config;

import com.claro.sp.automation.lineas.application.creation.LineCreationValidator;
import com.claro.sp.automation.lineas.application.creation.LineCreator;
import com.claro.sp.automation.lineas.application.creation.MyBatisLineCreator;
import com.claro.sp.automation.lineas.application.migration.LineMigrator;
import com.claro.sp.automation.lineas.application.migration.MyBatisLineMigrator;
import com.claro.sp.automation.lineas.controller.LineController;
import com.claro.sp.automation.lineas.domain.country.CountryDefaultsResolver;
import com.claro.sp.automation.lineas.integration.tecnotree.NoOpTecnoTreeClient;
import com.claro.sp.automation.lineas.integration.tecnotree.TecnoTreeClient;
import com.claro.sp.automation.lineas.repository.LineRepository;
import com.claro.sp.automation.lineas.repository.MyBatisLineRepository;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.util.Properties;
import org.apache.ibatis.session.SqlSessionFactory;

public class LineasFactory implements AutoCloseable {
    private final HikariDataSource prodDataSource;
    private final HikariDataSource ccardDataSource;
    private final LineController lineController;

    public LineasFactory() throws IOException {
        this(new NoOpTecnoTreeClient());
    }

    public LineasFactory(TecnoTreeClient tecnoTreeClient) throws IOException {
        ApplicationPropertiesLoader loader = new ApplicationPropertiesLoader();
        Properties properties = loader.load();
        HikariDataSourceFactory dataSourceFactory = new HikariDataSourceFactory();
        this.prodDataSource = dataSourceFactory.create(loader.databaseProperties(properties, "lineas.prod"), DataSourceName.PROD);
        this.ccardDataSource = dataSourceFactory.create(loader.databaseProperties(properties, "lineas.ccard"), DataSourceName.CCARD);

        MyBatisSessionFactoryProvider provider = new MyBatisSessionFactoryProvider();
        SqlSessionFactory prodSessionFactory = provider.create(prodDataSource, "prod");
        SqlSessionFactory ccardSessionFactory = provider.create(ccardDataSource, "ccard");
        LineRepository repository = new MyBatisLineRepository(prodSessionFactory, ccardSessionFactory);
        LineCreator creator = new MyBatisLineCreator(repository, new CountryDefaultsResolver(), new LineCreationValidator(), tecnoTreeClient);
        LineMigrator migrator = new MyBatisLineMigrator();
        this.lineController = new LineController(creator, migrator);
    }

    public LineController lineController() {
        return lineController;
    }

    @Override
    public void close() {
        ccardDataSource.close();
        prodDataSource.close();
    }
}
