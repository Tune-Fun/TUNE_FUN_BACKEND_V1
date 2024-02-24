package com.tune_fun.v1.external.aws.xray;

import com.amazonaws.xray.sql.TracingDataSource;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class AwsXRayTracingDataSource extends TracingDataSource {

	public AwsXRayTracingDataSource(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public Connection getConnection() throws SQLException {
		return AwsXRayTracingConnection.decorate(delegate.getConnection());
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return AwsXRayTracingConnection.decorate(delegate.getConnection(username, password));
	}

}
