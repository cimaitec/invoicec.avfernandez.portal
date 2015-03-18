package com.company.module.bean.report;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import com.company.module.bean.common.AbstractBaseReportBean;

public class MyFirstJasperReportsBean extends AbstractBaseReportBean {
	
	private final String COMPILE_FILE_NAME = "my_first_report";
	
	@Override
	protected String getCompileFileName() {
		return COMPILE_FILE_NAME;
	}
	
	@Override
	protected Map<String, Object> getReportParameters() {
		Map<String, Object> reportParameters = new HashMap<String, Object>();
		
		reportParameters.put("ReportTitle", "Hello JasperReports");
		
		return reportParameters;
	}

	@Override
	protected JRDataSource getJRDataSource() {
		// return your custom datasource implementation
		MyFirstJasperReportsDataSource dataSource = new MyFirstJasperReportsDataSource();
		
		return dataSource;
	}
	
	public String execute() {
		try {
			super.prepareReport();
		} catch (Exception e) {
			// make your own exception handling
			e.printStackTrace();
		}
		
		return null;
	}
}
