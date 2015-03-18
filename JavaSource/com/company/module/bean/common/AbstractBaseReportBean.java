package com.company.module.bean.common;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;

import com.company.module.util.ReportConfigUtil;

public abstract class AbstractBaseReportBean {
	public enum ExportOption {PDF, HTML, EXCEL, RTF}
	
	private ExportOption exportOption;
	
	private final String COMPILE_DIR = "/pages/report/design/";
			
	public AbstractBaseReportBean() {
		super();
		setExportOption(ExportOption.PDF);
	}

	protected void prepareReport() throws JRException, IOException {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		
		ServletContext context = (ServletContext) externalContext.getContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		
		ReportConfigUtil.compileReport(context, getCompileDir(), getCompileFileName());
		
		File reportFile = new File(ReportConfigUtil.getJasperFilePath(context, getCompileDir(), getCompileFileName()+".jasper"));
		
		JasperPrint jasperPrint = ReportConfigUtil.fillReport(reportFile, getReportParameters(), getJRDataSource());
		
		if(getExportOption().equals(ExportOption.HTML)) {
			ReportConfigUtil.exportReportAsHtml(jasperPrint, response.getWriter());
		}else {		
			request.getSession().setAttribute(BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);		
			response.sendRedirect(request.getContextPath()+"/servlets/report/"+getExportOption());
		}
		
		FacesContext.getCurrentInstance().responseComplete();
	}

	public ExportOption getExportOption() {
		return exportOption;
	}

	public void setExportOption(ExportOption exportOption) {
		this.exportOption = exportOption;
	}

	protected Map<String, Object> getReportParameters() {
		return new HashMap<String, Object>();
	}

	protected String getCompileDir() {
		return COMPILE_DIR;
	}
	
	protected abstract JRDataSource getJRDataSource();

	protected abstract String getCompileFileName();

}
