package com.general.controladores;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import com.documentos.entidades.FacCabDocumento;
import com.documentos.entidades.FacDetAdicional;
import com.documentos.entidades.FacDetDocumento;
import com.documentos.entidades.FacDetRetencion;
import com.documentos.entidades.FacEmpresa;
import com.general.entidades.FacCliente;
import com.general.entidades.FacEstablecimiento;
import com.general.entidades.FacGeneral;
import com.general.entidades.FacProducto;
import com.general.entidades.FacTiposDocumento;
import com.general.entidades.facDetalleDocumentoEntidad;
import com.general.servicios.FacClienteServicios;
import com.general.servicios.FacDocumentoServicios;
import com.general.servicios.FacEmpresaEmisoraServicios;
import com.general.servicios.FacGeneralServicio;
import com.general.util.DetallesAdicionalesReporte;
import com.general.util.InformacionAdicional;
import com.general.util.ReporteConsultaExcel;



@ViewScoped
@ManagedBean
@SessionScoped
public class FacConsultaControladores2 {

	@EJB
	private FacDocumentoServicios facDocumentoServicios; // variable en la cual llama la clase de servicio
	
	@EJB
	private FacGeneralServicio facGenSer;
	@EJB
	private FacClienteServicios facCliSer;
	@EJB
	private FacEmpresaEmisoraServicios facServEmpresa;
	//buscaUsuarioEmpresa(String Usuario, String rucEmpresa)
	// metodod de variable del web
	private String ruc; // variable que recoge el ruc de la consulta
	private String RazonSocial; // variable que recoge la razon social de la consulta
	private String SeleccionTipo; // variable que recoge el item selecciondo del tipo de documento
	private String SeleccionTipoEstado; // variable que recoge el item selecciondo del tipo de Estado
	private Date FechaInicio;// dato que se encarga de recoger la fecha de inicio
	private Date FechaFinal;// dato que se encarga de recoger la fecha de final
	private String Correos;// dato de correos que se va a enviar de las facuras
	private String numDocumento;
	
	// metodos de tipo de documentos
	private FacTiposDocumento facTiposDocumento;
	private ArrayList<SelectItem> Tipo; // variable que recoge los tipos de documento	
	private ArrayList<SelectItem> TipoEstados; // variable que recoge los tipos de estados de los documentos
	private List<FacTiposDocumento> TipoDocumento; // variable que recoge los tipos de documento
		
	// metodo del detalle de documento
	private List<facDetalleDocumentoEntidad> DetalleDocumento; // registro de consulta de documentos
	private facDetalleDocumentoEntidad [] selectedfacDetalleDocumentoEntidad; // variable que me permite poder seleccionar toda las facturas
	private facDetalleDocumentoEntidad valorDetalleDocumento;// variable que recoge el dato seleccionado
	private String rucEmpresa;
	protected String loginUsuario;
	private String [] selectedOptionsEmail; // variable que recoge si desea enviar correo con xml o pdf  
	
	private FacEnviarMail claseEmail;// variable que contiene la clase de email
	
	private FacConsultaDocumentoSeleccControladores detalle_documentos_;
	private String dialect=null;
	private String sistemaOperativo=null;
	private String pathTemp= null;
	private String pathSubReport= null;
	private HashMap<String, String> pathReports = null;

	//TODO contructor que se encarga de limpiar el contenido de los campos
	public void Actualizar_paguina() {
		CargarDatos();
	}

	//TODO contructor general que se encarga de cargar los datos primordiales
	public void CargarDatos(){
		try{
			 //System.out.println("Prueba de Errores JZU");
			//Dialect parametrizado Default  idGeneral -> 100
			FacGeneral facGen = facGenSer.buscarDatosGeneralPrimerHijo("100");
			if (facGen !=null)
				dialect=facGen.getDescripcion().trim();
			else{
				String msg = "Configuracion General::FacGeneral::La carpeta de temporales no esta configurada::idGeneral->100";
				FacesMessage mensaje=null;
				mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,msg,null);
				FacesContext.getCurrentInstance().addMessage(null, mensaje);
			}	
			//SistemaOperativo por Default busca el del sistema Operativo Real.
			facGen = facGenSer.buscarDatosGeneralPadre("100");
			if (facGen !=null)
				sistemaOperativo=facGen.getDescripcion();
			else{
				sistemaOperativo=System.getProperty("os.name").toUpperCase().toString();
				if ((sistemaOperativo.indexOf("WIN")>0)||(System.getProperty("os.name").toUpperCase().indexOf("MAC")>0)){
					sistemaOperativo = "Windows";
				}
				if (sistemaOperativo.indexOf("LINUX")>0){
					sistemaOperativo = "Linux";
				}	
			}
			//PathReports ruta de archivos Jasper 
			pathReports = facGenSer.buscarDatosGeneralPadreHash("103");
			System.out.println("pathReports::"+pathReports.toString());
			if (pathReports ==null){
				String msg = "Configuracion General::FacGeneral::La carpeta de temporales no esta configurada::idGeneral->100";
				FacesMessage mensaje=null;
				mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,msg,null);
				FacesContext.getCurrentInstance().addMessage(null, mensaje);
			}
			
			//Path Directorio Temporal	
			facGen = facGenSer.buscarDatosGeneralPrimerHijo("109");
			if (facGen !=null)
				pathTemp=facGen.getDescripcion();
			else{
				String msg = "Configuracion General::FacGeneral::La carpeta de temporales no esta configurada::idGeneral->109";
				FacesMessage mensaje=null;
				mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,msg,null);
				FacesContext.getCurrentInstance().addMessage(null, mensaje);
			}
			
			//Path Directorio SubReports	
			facGen = facGenSer.buscarDatosGeneralPadre("112");
			if (facGen !=null)
				pathSubReport=facGen.getDescripcion();
			else{
				String msg = "Configuracion General::FacGeneral::La carpeta de SubReports no esta configurada::idGeneral->112";
				FacesMessage mensaje=null;
				mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,msg,null);
				FacesContext.getCurrentInstance().addMessage(null, mensaje);
			}
			List<FacGeneral> facGenList = new ArrayList<FacGeneral>();
			facGenList = facGenSer.buscarDatosGeneralHijo("117");
			TipoEstados= new ArrayList<SelectItem>();
			if (facGenList.size()>0){
				for(int i=0;i<facGenList.size();i++)
					TipoEstados.add(new SelectItem(facGenList.get(i).getCodUnico(), facGenList.get(i).getDescripcion()));				
			}else{
				TipoEstados.add(new SelectItem("", "NO HAY DATOS"));
			}
			
			FacesContext context = FacesContext.getCurrentInstance();
			HttpSession sesion = (HttpSession)context.getExternalContext().getSession(true);
			if(sesion.getAttribute("Ruc_Empresa") != null)
			{
				rucEmpresa = sesion.getAttribute("Ruc_Empresa").toString();
				loginUsuario = sesion.getAttribute("id_usuario").toString();
			}
			else{
				ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
				String ctxPath = ((ServletContext) ctx.getContext()).getContextPath();
				ctx.redirect(ctxPath + "/paginas/Administrador/Cuenta/Login.jsf");
			}
				System.out.println("Ruc::"+rucEmpresa);
			setCorreos("");
			setRazonSocial("");
			setRuc("");
			setSeleccionTipo("");
			FechaFinal = null;
			FechaInicio = null;
			
			FechaFinal =new Date();
			/*
	        Calendar cal = Calendar.getInstance();
	        cal.add(Calendar.DATE,0);*/
	        FechaInicio = new Date();
	        
			facTiposDocumento = new FacTiposDocumento();
			valorDetalleDocumento = new facDetalleDocumentoEntidad();
			selectedfacDetalleDocumentoEntidad = null;
			llenarCombo();
			llenarDetalleDocumento();
		}catch (Exception e) {
			FacesMessage mensaje=null;
			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		}
	}
		
	//TODO contructor que se llena el detalle de los documentos
	public void llenarDetalleDocumento(){
		try{
			String ls_dateFormat ="";
			List<facDetalleDocumentoEntidad> DetalleDocumento2; // registro de consulta de documentos
			List<FacCabDocumento> documento_detalles;//registro de detalle de documentos
			List<FacTiposDocumento> tipoDocumentos;// registro de tipos de documento
			List<FacEmpresa> detalleEmpresas; // registro de empresas
			DetalleDocumento = new ArrayList<facDetalleDocumentoEntidad>(); // inicializando DetalleDocumento
			detalle_documentos_ = new FacConsultaDocumentoSeleccControladores();
			
			if (dialect.equals("PostgreSQL"))
				ls_dateFormat = "yyyy-MM-dd";
			if (dialect.equals("SQLServer"))
				ls_dateFormat = "yyyyMMMdd";
			if (numDocumento!=null)
			if (numDocumento.trim().equals("")) numDocumento = null;
			/*
			documento_detalles = BuscarDatosdeDetalleDocumento(new Object[]{ruc,RazonSocial,SeleccionTipo, 
																			((FechaInicio == null)? FechaInicio: (new java.text.SimpleDateFormat(ls_dateFormat).format(FechaInicio))),
																			((FechaFinal == null)? FechaFinal: (new java.text.SimpleDateFormat(ls_dateFormat).format(FechaFinal))),
																			rucEmpresa,
																			dialect,
																			SeleccionTipoEstado,
																			numDocumento});
			*/
			
			documento_detalles = BuscarDatosdeDetalleDocumento(new Object[] {
					ruc,
					RazonSocial,
					SeleccionTipo,
					((FechaInicio == null) ? FechaInicio
							: (new java.text.SimpleDateFormat("yyyy-MM-dd")
									.format(FechaInicio))),
					((FechaFinal == null) ? FechaFinal
							: (new java.text.SimpleDateFormat("yyyy-MM-dd")
									.format(FechaFinal))), rucEmpresa,
					SeleccionTipoEstado, numDocumento });
			
			
			tipoDocumentos = buscarPorCodigo();
			detalleEmpresas = fac_empresas();
			if (documento_detalles!=null)
			{
			
			DetalleDocumento2 = MotrandoDocumentoFiltrados(documento_detalles,tipoDocumentos,detalleEmpresas);
		
			
			
			for (facDetalleDocumentoEntidad detalle : DetalleDocumento2) {
				DetalleDocumento.add(new facDetalleDocumentoEntidad(
																detalle.getRFCREC(),
																detalle.getNOMREC(),
																detalle.getCodDoc(),
																detalle.getTIPODOC(),
																detalle.getFOLFAC(),
																detalle.getTOTAL(),
																detalle.getFECHA(),
																detalle.getEDOFAC(),
																detalle.getPDFARC(),
																detalle.getXMLARC(),
																detalle.getEmail(),
																detalle.getDireccion(),
																detalle.getFormato(),
																detalle.getCodEstablecimiento(),
																detalle.getCodPuntoEmision(),
																detalle.getCodigoDocumento(),
																detalle.getSecuencial(), 
																detalle.getXmlAutorizacion(),
																detalle.getAmbiente()
																));

				detalle_documentos_ = new FacConsultaDocumentoSeleccControladores(DetalleDocumento);
			}
		}
			
		}catch (Exception e) {
			FacesMessage mensaje=null;
			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		}
	}
	
	//TODO mostrando filtrtados de documentos para el display del usuario
	private List<facDetalleDocumentoEntidad> MotrandoDocumentoFiltrados(List<FacCabDocumento> documento_detalles,List<FacTiposDocumento> tipoDocumentos,List<FacEmpresa> detalleEmpresas) {
		List<facDetalleDocumentoEntidad> detalledocumento = null;
			
		try{			
			detalledocumento = facDocumentoServicios.MotrandoDocumentoFiltrados(documento_detalles,tipoDocumentos,detalleEmpresas);
		}catch (Exception e) {
			FacesMessage mensaje=null;
			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		}
		return  detalledocumento;
	}
	
	//TODO listado de detalle de documento
	private List<FacCabDocumento> BuscarDatosdeDetalleDocumento(Object [] obj){
		List<FacCabDocumento> detalledocumento = null;
			
		try{			
			/*
			//for (int i=0; i<obj.length;i++)
				//System.out.println("param"+i+"::"+obj[i]);
			if(obj[3]!=null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
				String dateInString = (String) obj[3];
				Calendar c = Calendar.getInstance();
				c.setTime(sdf.parse(dateInString));
				c.add(Calendar.DATE, 1);  // number of days to add
				dateInString = sdf.format(c.getTime());
				System.out.println(dateInString);
				obj[3]=dateInString;
			}	
			if(obj[4]!=null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
				String dateInString = (String) obj[4];
				Calendar c = Calendar.getInstance();
				c.setTime(sdf.parse(dateInString));
				c.add(Calendar.DATE, 1);  // number of days to add
				dateInString = sdf.format(c.getTime());
				System.out.println(dateInString);
				obj[4]=dateInString;
			}*/
			detalledocumento = facDocumentoServicios.MostrandoDetalleDocumento(obj);
		}catch (Exception e) {
			FacesMessage mensaje=null;
			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		}
		return  detalledocumento;
	}

	//TODO contructr de lista de las empresas
	public List<FacEmpresa> fac_empresas() {
		List<FacEmpresa> detalledocumento = null;
			
		try{			
			detalledocumento = facDocumentoServicios.listadoEmpresas(rucEmpresa);        
	
		}catch (Exception e) {
			FacesMessage mensaje=null;
			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		}
		return  detalledocumento;
	}
	
	//TODO contructor que se encarga de llenar los combox
	public void llenarCombo(){
		Tipo= new ArrayList<SelectItem>();
			
		try{
			TipoDocumento = buscarPorCodigo();
				
			if(TipoDocumento.isEmpty()){
				facTiposDocumento.setIdDocumento(null);
				facTiposDocumento.setDescripcion("NO HAY DATOS");
				facTiposDocumento.setFormatoTexto("");
				facTiposDocumento.setFormatoXML("");
				facTiposDocumento.setIsActive("Y");
				TipoDocumento.add(facTiposDocumento);
			}
			for(int i = 0;i<TipoDocumento.size();i++)
				Tipo.add(new SelectItem(TipoDocumento.get(i).getIdDocumento(), TipoDocumento.get(i).getDescripcion()));
				
		}catch (Exception e) {
			FacesMessage mensaje=null;
			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		}
	}
			
	//TODO contructor de cargar combo 
	public List<FacTiposDocumento> buscarPorCodigo(){
			
		List<FacTiposDocumento> listaFacGeneral = null;
			
		try{
			listaFacGeneral = facDocumentoServicios.BuscarDatosdeTipoDocumento();           
			
		}catch (Exception e) {
			e.printStackTrace();
			FacesMessage mensaje=null;
			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		}
			return  listaFacGeneral;
	}
	
	//TODO contructor que se encarga de cargar el archivo seleccionado sea pdf o xml
	public void AbrirArchivo(String Archivo , String direccion){
		//sistemaOperativo.equals("Windows")
		
		try {
			//pdf();
			System.out.println("DOCUMENTO::"+direccion + Archivo);
			System.out.println("EDOFAC::"+valorDetalleDocumento.getEDOFAC());
			System.out.println("FOLFAC::"+valorDetalleDocumento.getFOLFAC());
			System.out.println("CodDoc::"+valorDetalleDocumento.getCodDoc());
			System.out.println("CodEstablecimiento::"+valorDetalleDocumento.getCodEstablecimiento());
			System.out.println("CodigoDocumento::"+valorDetalleDocumento.getCodigoDocumento());
			System.out.println("CodPuntoEmision::"+valorDetalleDocumento.getCodPuntoEmision());
			System.out.println("Secuencial::"+valorDetalleDocumento.getSecuencial());
			System.out.println("Ambiente::"+valorDetalleDocumento.getAmbiente());
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try 
		{
			//String Documento = "//192.168.32.90/DataExpress/jzurita/firmados/0992531940001010010100000603720130225.xml";
			if(Archivo.contains(".pdf") && !valorDetalleDocumento.getEDOFAC().toString().trim().equals("AUTORIZADO")){
				mensajeAlerta("Mensaje del sistema", "No se encuentra el archivo pdf porque el documento no esta AUTORIZADO", "peligro");
				return;
			}
			String Documento = direccion + Archivo;
			Documento.replace("/", "\"");
			System.out.println("DOCUMENTO::"+direccion + Archivo);
			File ficheroXLS = new File(Documento);
			FacesContext ctx = FacesContext.getCurrentInstance();
			FileInputStream fis = new FileInputStream(ficheroXLS);
			byte[] bytes = new byte[1000];
			int read = 0;

			if (!ctx.getResponseComplete()) {
			   String fileName = ficheroXLS.getName();
			   String contentType = "application/" + (Archivo.contains(".pdf") ? "pdf" : "xml");
			   HttpServletResponse response =(HttpServletResponse) ctx.getExternalContext().getResponse();
			   response.setContentType(contentType);
			   response.setHeader("Content-Disposition","attachment;filename=" + fileName);
			   ServletOutputStream out = response.getOutputStream();

			   while ((read = fis.read(bytes)) != -1) {
			        out.write(bytes, 0, read);
			   }

			   out.flush();
			   out.close();
			   ctx.responseComplete();
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		 	FacesContext context = FacesContext.getCurrentInstance();  
	        context.addMessage(null, new FacesMessage("Successful", "Hello " + "No se encuentra el archivo seleccionado"));  
		}
		/*
		if (sistemaOperativo.equals("Windows")){
			try 
			{
				//String Documento = "//192.168.32.90/DataExpress/jzurita/firmados/0992531940001010010100000603720130225.xml";
				if(Archivo.contains(".pdf") && !valorDetalleDocumento.getEDOFAC().toString().trim().equals("AUTORIZADO")){
					mensajeAlerta("Mensaje del sistema", "No se encuentra el archivo pdf porque el documento no esta AUTORIZADO", "peligro");
					return;
				}
				String Documento = direccion + Archivo;
				Documento.replace("/", "\"");
				System.out.println("DOCUMENTO::"+direccion + Archivo);
				File ficheroXLS = new File(Documento);
				FacesContext ctx = FacesContext.getCurrentInstance();
				FileInputStream fis = new FileInputStream(ficheroXLS);
				byte[] bytes = new byte[1000];
				int read = 0;
	
				if (!ctx.getResponseComplete()) {
				   String fileName = ficheroXLS.getName();
				   String contentType = "application/" + (Archivo.contains(".pdf") ? "pdf" : "xml");
				   HttpServletResponse response =(HttpServletResponse) ctx.getExternalContext().getResponse();
				   response.setContentType(contentType);
				   response.setHeader("Content-Disposition","attachment;filename=" + fileName);
				   ServletOutputStream out = response.getOutputStream();
	
				   while ((read = fis.read(bytes)) != -1) {
				        out.write(bytes, 0, read);
				   }
	
				   out.flush();
				   out.close();
				   ctx.responseComplete();
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			 	FacesContext context = FacesContext.getCurrentInstance();  
		        context.addMessage(null, new FacesMessage("Successful", "Hello " + "No se encuentra el archivo seleccionado"));  
			}
		}else{
			if (sistemaOperativo.equals("Linux")){
				try {
					pdf();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}*/
	}
	
	//TODO contructor que se encarga de autocompletar el ruc del comprador para facilitar al usuario
	public List<String> complete_RucComprador(String parametro_ruc) throws Exception{
		List<String> resultado = new ArrayList<String>();
		resultado = facDocumentoServicios.BuscarfitroEmpresaDocumentos(parametro_ruc, rucEmpresa,valorDetalleDocumento.getAmbiente());		
		return resultado;
	}
	
	//TODO Zipear archivos seleccionado
	private void Zippear(String[] pFile, String pZipFile) throws Exception {
		// objetos en memoria
		FileInputStream fis = null;
		FileOutputStream fos = null;
		ZipOutputStream zipos = null;
		// fichero contenedor del zip
		if(pFile[0] != "" || pFile[1] != ""){
			fos =  new FileOutputStream(pZipFile);
			// fichero comprimido
			zipos = new ZipOutputStream(fos);
		}
		// buffer
		byte[] buffer = new byte[1024];
		try {
			// fichero a comprimir
			for (int i = 0; i < pFile.length; i++) {
				if(pFile[i] != ""){
					fis = new FileInputStream(pFile[i]);
				
					ZipEntry zipEntry = new ZipEntry(new File(pFile[i]).getName());
					zipos.putNextEntry(zipEntry);
					int len = 0;
					// zippear
					while ((len = fis.read(buffer, 0, 1024)) != -1)
						zipos.write(buffer, 0, len);
					// volcar la memoria al disco
					zipos.flush();
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			// cerramos los files
			if(zipos != null && fis != null && fos != null){
			zipos.close();
			fis.close();
			fos.close();
			}
		} // end try
	} // end Zippear
		
	//TODO contructor que se encarga de enviar los correos
	public void EnviarCorreos(String Evento) throws Exception{
		Boolean correosnoEnviados = false;
		String subjectMensaje = "";
		claseEmail = new FacEnviarMail();
		String notificacionLectura = "";
		// verifica si se a seleccionado un documento del registro
		
		if(selectedfacDetalleDocumentoEntidad.length == 0){
			mensajeAlerta("Mensaje del sistema","Debe seleccionar algun documento a adjuntar", "informacion");
			return;
		}
		// validando si se a ingresado el formato re los correos
		String [] listaCorreo = Correos.split(",");

		for (int i = 0; i < listaCorreo.length; i++){ 
			if(!listaCorreo[i].toString().trim().equals(""))
				if(!claseEmail.validar_correo(listaCorreo[i])){
					mensajeAlerta("Mensaje del sistema","El correo :" + listaCorreo[i] + " \n no se encuentra en el formato", "peligro");
					mensajeAlerta("Mensaje del sistema","Por favor corregir el correo", "peligro");
					return;
				}
		}
		FacEmpresa empresas = facDocumentoServicios.listadoEmpr(rucEmpresa); 
		
			if(selectedOptionsEmail.length == 0){/// verifica si a seleccionado el check del archivo pdf
				mensajeAlerta("Mensaje del sistema","Por favor debe seleccionar que tipo de archivo desea adjuntar", "Informacion");
				return;
			}
			
			for(facDetalleDocumentoEntidad seleccionDocumentos: selectedfacDetalleDocumentoEntidad){
				String contenidoMensaje = facGenSer.buscarDatosGeneralPadre("114").getDescripcion();
				String nombreEmpresaMesaje =  facGenSer.buscarDatosGeneralPadre("115").getDescripcion();
				String portal =  facGenSer.buscarDatosGeneralPadre("116").getDescripcion();
				StringBuffer body = new StringBuffer();
				
				contenidoMensaje = contenidoMensaje.replace("|NOMEMAIL|", nombreEmpresaMesaje).toString();
				contenidoMensaje = contenidoMensaje.replace("|DOCFECHAGENERA|", seleccionDocumentos.getFECHA().toString()).toString();
				contenidoMensaje = contenidoMensaje.replace("|FECHA|", (seleccionDocumentos.getFECHA().toString()));
				contenidoMensaje = contenidoMensaje.replace("|NODOCUMENTO|", (seleccionDocumentos.getCodEstablecimiento()+"-"+seleccionDocumentos.getCodPuntoEmision()+"-"+seleccionDocumentos.getSecuencial()));	
				contenidoMensaje = contenidoMensaje.replace("|HELPDESK|", "");
				
				contenidoMensaje = contenidoMensaje.replace("|CLIENTE|", seleccionDocumentos.getNOMREC());
				System.out.println("RucEmpresa::"+rucEmpresa);		
				System.out.println("RucCliente::"+seleccionDocumentos.getRFCREC());
				try{
					FacCliente facCli = facCliSer.buscaUsuarioEmpresa(seleccionDocumentos.getRFCREC(), rucEmpresa);
					if (facCli!=null)
						contenidoMensaje = contenidoMensaje.replace("|CODCLIENTE|", String.valueOf(facCli.getCodCliente()));
					else
						contenidoMensaje = contenidoMensaje.replace("|CODCLIENTE|", String.valueOf(facCli.getCodCliente()));
				}catch (Exception e){
					contenidoMensaje = contenidoMensaje.replace("|CODCLIENTE|", "");
				}
				
				contenidoMensaje = contenidoMensaje.replace("|PORTAL|", portal);
				String ls_tipoDocumento ="";				
				if (seleccionDocumentos.getCodDoc().equals("01"))
					ls_tipoDocumento ="Factura";
				if (seleccionDocumentos.getCodDoc().equals("04"))
					ls_tipoDocumento ="Nota de Credito";
				if (seleccionDocumentos.getCodDoc().equals("07"))
					ls_tipoDocumento ="Comprobante de Retencion";
				contenidoMensaje = contenidoMensaje.replace("|TIPODOCUMENTO|", ls_tipoDocumento);
				
				body.append(contenidoMensaje);
				/*
				body.append("Estimado(a) cliente;  <br>");
				body.append("Acaba de recibir su documento electrónico generado el " + seleccionDocumentos.getFECHA());
				body.append("<br><br>Saludos cordiales, ");
				body.append("<br>Cima IT, ");
				body.append("<br><br>Servicio proporcionado por DataExpress Internacional");
				body.append("<br><br>Tel. 593 04 2280217");
				*/
				String pdf = "",xml = "";
				for (int i = 0; i < selectedOptionsEmail.length; i++) {
					File ficheroXLS = new File("");
					
					//File dirTemp = new File("C:/DataExpress/DMIRO/firmados/");
					File dirTemp = new File(System.getProperty("java.io.tmpdir"));
					if(!dirTemp.exists())
						dirTemp.mkdirs();
					
					if(selectedOptionsEmail[i].toString().trim().equals("PDF"))
					{						
						try {
							pdfMail(seleccionDocumentos);
						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						pdf = dirTemp + dirTemp.separator+ seleccionDocumentos.getPDFARC();
						
						ficheroXLS = new File(pdf);
						if(!ficheroXLS.isFile())
							pdf = "";
					}
					else
					{	
						xml = dirTemp + dirTemp.separator+ seleccionDocumentos.getXMLARC();
						stringToArchivo(xml, seleccionDocumentos.getXmlAutorizacion());
						ficheroXLS = new File(xml);
						if(!ficheroXLS.isFile())
							xml = "";
					}
				}
				String Adjuntararchivos[];
				String NombreArchivoZepado = "C:/" + seleccionDocumentos.getFOLFAC() + ".zip";
				if(Evento.trim().equals("ZIP")){
					NombreArchivoZepado.replace("/", "\"");
					Zippear(new String[]{pdf, xml}, NombreArchivoZepado);
					Adjuntararchivos = new String[]{NombreArchivoZepado};
				}else
				{
					Adjuntararchivos = new String[]{pdf, xml};
				}
				if(xml.toString().trim() != "" || pdf.toString().trim() != ""){
					subjectMensaje = facGenSer.buscarDatosGeneralPadre("113").getDescripcion();
					subjectMensaje = subjectMensaje.replace("|NOMEMAIL|", nombreEmpresaMesaje).toString();
					subjectMensaje = subjectMensaje.replace("|NUMDOC|", seleccionDocumentos.getFOLFAC()).toString();
					String ls_tipoDoc = "";
					if (seleccionDocumentos.getCodDoc().equals("01")){
						ls_tipoDoc = "Ha recibido una FACTURA";
					}
					if (seleccionDocumentos.getCodDoc().equals("04")){
						ls_tipoDoc = "Ha recibido una NOTA DE CREDITO";
					}
					if (seleccionDocumentos.getCodDoc().equals("07")){
						ls_tipoDoc = "Ha recibido un COMPROBANTE DE RETENCION";
					}
					if (seleccionDocumentos.getCodDoc().equals("06")){
						ls_tipoDoc = "Ha recibido una Guia de Remision";
					}
					if (seleccionDocumentos.getCodDoc().equals("05")){
						ls_tipoDoc = "Ha recibido una NOTA DE DEBITO";
					}
					subjectMensaje = subjectMensaje.replace("|TIPODOC|", ls_tipoDoc).toString();
					System.out.println("Content Message :"+contenidoMensaje);
					System.out.println("Subject Message :"+subjectMensaje);					
					
					if (claseEmail.enviar(empresas, //empresas
									  seleccionDocumentos.getEmail() + 
									  ((Correos.toString().trim().equals("")) ? Correos: "," + Correos), //toAddress
									  "",//ccAddress
									  "",//bccAddresss
									  subjectMensaje,//subject
									  //"Documento electrónico No:" + seleccionDocumentos.getFOLFAC() + " de Cima IT",//subject
									  true, //xisHTMLFormat
									  body, //body
									  false,//debug
									  notificacionLectura,
									  Adjuntararchivos////Adjuntararchivos
									  )){
						System.out.println("Mail Enviado");
					}else{
						System.out.println("Mail Fallido");
					}
				
					
				}else
					correosnoEnviados = true;
				if(Evento.trim().equals("ZIP")){
					File da = new File(NombreArchivoZepado);
					da.delete();
				}
			}
			if(correosnoEnviados)
				mensajeAlerta("Mensaje del sistema","Algunos documentos no se encuentra el archivo, y no se realizo el envio del correo", "Informacion");
			
		mensajeAlerta("Mensaje del sistema","Correos enviados", "informacion");
	}
	
	
	//TODO contructor de mensaje de alerta
	 private void mensajeAlerta(String mensajeVentana, String mensajeDetalle, String tipo) {
		 FacesContext context = FacesContext.getCurrentInstance();            
	      context.addMessage(null, new FacesMessage((tipo.toString().trim().equals("alerta") ? FacesMessage.SEVERITY_ERROR : tipo.toString().trim().equals("peligro") ? FacesMessage.SEVERITY_WARN : FacesMessage.SEVERITY_INFO),mensajeVentana, mensajeDetalle)); 
     }
			
	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public String getRazonSocial() {
		return RazonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		RazonSocial = razonSocial;
	}

	public Date getFechaInicio() {
		return FechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		FechaInicio = fechaInicio;
	}

	public Date getFechaFinal() {
		return FechaFinal;
	}

	public void setFechaFinal(Date fechaFinal) {
		FechaFinal = fechaFinal;
	}

	public String getCorreos() {
		return Correos;
	}

	public void setCorreos(String correos) {
		Correos = correos;
	}

	public FacDocumentoServicios getFacDocumentoServicios() {
		return facDocumentoServicios;
	}
	
	public void setFacDocumentoServicios(FacDocumentoServicios facDocumentoServicios) {
		this.facDocumentoServicios = facDocumentoServicios;
	}

	public FacTiposDocumento getFacTiposDocumento() {
		return facTiposDocumento;
	}

	public void setFacTiposDocumento(FacTiposDocumento facTiposDocumento) {
		this.facTiposDocumento = facTiposDocumento;
	}
	
	public ArrayList<SelectItem> getTipo() {
		return Tipo;
	}

	public void setTipo(ArrayList<SelectItem> tipo) {
		Tipo = tipo;
	}

	public List<FacTiposDocumento> getTipoDocumento() {
		return TipoDocumento;
	}

	public void setTipoDocumento(List<FacTiposDocumento> tipoDocumento) {
		TipoDocumento = tipoDocumento;
	}

	public String getSeleccionTipo() {
		return SeleccionTipo;
	}

	public void setSeleccionTipo(String seleccionTipo) {
		SeleccionTipo = seleccionTipo;
	}

	public List<facDetalleDocumentoEntidad> getDetalleDocumento() {
		return DetalleDocumento;
	}

	public void setDetalleDocumento(List<facDetalleDocumentoEntidad> detalleDocumento) {
		DetalleDocumento = detalleDocumento;
	}

	public facDetalleDocumentoEntidad [] getSelectedfacDetalleDocumentoEntidad() {
		return selectedfacDetalleDocumentoEntidad;
	}

	public void setSelectedfacDetalleDocumentoEntidad(
			facDetalleDocumentoEntidad [] selectedfacDetalleDocumentoEntidad) {
		this.selectedfacDetalleDocumentoEntidad = selectedfacDetalleDocumentoEntidad;
	}

	public facDetalleDocumentoEntidad getValorDetalleDocumento() {
		return valorDetalleDocumento;
	}

	public void setValorDetalleDocumento(facDetalleDocumentoEntidad valorDetalleDocumento) {
		this.valorDetalleDocumento = valorDetalleDocumento;
	}

	public String [] getSelectedOptionsEmail() {
		return selectedOptionsEmail;
	}

	public void setSelectedOptionsEmail(String [] selectedOptionsEmail) {
		this.selectedOptionsEmail = selectedOptionsEmail;
	}
	
	public FacConsultaDocumentoSeleccControladores getDetalle_documentos_prueba() {
		return detalle_documentos_;
	}

	public void setDetalle_documentos_prueba(
			FacConsultaDocumentoSeleccControladores detalle_documentos_prueba) {
		this.detalle_documentos_ = detalle_documentos_prueba;
	}

	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public void excel() throws Throwable{ 
		File dirTemp = new File(System.getProperty("java.io.tmpdir"));
		if(!dirTemp.exists())
			dirTemp.mkdirs();
		try{
		File dirFile = new File(dirTemp + dirTemp.separator+valorDetalleDocumento.getXMLARC());
		if(dirFile.exists())
			dirFile.exists();
		}catch(Exception e){
			;;
		}
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(','); 
        DecimalFormat df = new DecimalFormat("###,##0.00",otherSymbols);
        
        
        //DecimalFormat dfPorcentaje = new DecimalFormat("##0 %",otherSymbols);
        
		String name = "reporteGeneral";
		JasperPrint jasperPrint = null;
		System.out.println("::EXCEL::");
		
		String pathReport = "";//pathReports.get(valorDetalleDocumento.getCodigoDocumento());
		//System.out.println("pathReports::"+pathReports.toString());
		//System.out.println("pathReport::"+pathReport);		
		
			FileInputStream is = null;
		    JRDataSource dataSource = null;
			
			List<ReporteConsultaExcel> infoReporte = new ArrayList<ReporteConsultaExcel>();
			for(facDetalleDocumentoEntidad detalle : getDetalle_documentos_prueba().getListData()){
				ReporteConsultaExcel rep = new ReporteConsultaExcel();
				rep.setSecuencial(detalle.getSecuencial());
				infoReporte.add(rep);
			}
			
	        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(infoReporte);  
	        pathReport = "/reportes/reportDocumentos.jasper";
	        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(pathReport);  
	        jasperPrint = JasperFillManager.fillReport(reportPath, null, beanCollectionDataSource);  
	        
	        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();  
		    JRXlsExporter exporterXls = new JRXlsExporter ();  
		    exporterXls.setParameter(JRXlsExporterParameter.JASPER_PRINT, pathReport);  
		    exporterXls.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, false);  
		    exporterXls.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, true);  
		    exporterXls.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, false);  
		    exporterXls.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, true);  
		    exporterXls.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, true);  
		    exporterXls.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, true);  
		    exporterXls.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, true);
		    exporterXls.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,  name + ".xls");  
		    exporterXls.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);  
		    exporterXls.exportReport(); 
			
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();  
        httpServletResponse.addHeader("Content-disposition", "attachment; filename="+name+".xls");  
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();  
        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);  
        FacesContext.getCurrentInstance().responseComplete();  
    }
	
	public void pdf() throws Throwable{
		
		File dirTemp = new File(System.getProperty("java.io.tmpdir"));
		if(!dirTemp.exists())
			dirTemp.mkdirs();
		try{
		File dirFile = new File(dirTemp + dirTemp.separator+valorDetalleDocumento.getXMLARC());
		if(dirFile.exists())
			dirFile.exists();
		}catch(Exception e){
			;;
		}
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(','); 
        DecimalFormat df = new DecimalFormat("###,##0.00",otherSymbols);
        
        
        //DecimalFormat dfPorcentaje = new DecimalFormat("##0 %",otherSymbols);
        
		//String name = valorDetalleDocumento.getPDFARC();
        String name = valorDetalleDocumento.getCodigoDocumento() + valorDetalleDocumento.getCodEstablecimiento() + valorDetalleDocumento.getCodPuntoEmision() + ruc + valorDetalleDocumento.getSecuencial();
		JasperPrint jasperPrint = null;
		System.out.println("::PDF::");
		System.out.println("CodDoc::"+valorDetalleDocumento.getCodDoc());
		System.out.println("RucPdf::"+rucEmpresa);
		valorDetalleDocumento.setCodigoDocumento(valorDetalleDocumento.getCodDoc());	
		System.out.println("CodEstablecimiento::"+valorDetalleDocumento.getCodEstablecimiento());
		System.out.println("CodigoDocumento::"+valorDetalleDocumento.getCodigoDocumento());
		System.out.println("CodPuntoEmision::"+valorDetalleDocumento.getCodPuntoEmision());
		System.out.println("Secuencial::"+valorDetalleDocumento.getSecuencial());
		System.out.println("Ambiente::"+valorDetalleDocumento.getAmbiente());
		pathReports = facGenSer.buscarDatosGeneralPadreHash("103");
		String pathReport = pathReports.get(valorDetalleDocumento.getCodigoDocumento());
		System.out.println("pathReports::"+pathReports.toString());
		System.out.println("pathReport::"+pathReport);
		
		if (valorDetalleDocumento.getCodDoc().equals("04")||valorDetalleDocumento.getCodDoc().equals("01"))
		{			
			
			FileInputStream is = null;
		    JRDataSource dataSource = null;
			List<FacDetDocumento> lstFactDetDocumento = new ArrayList<FacDetDocumento>();
			List<FacDetAdicional> lstFactDetAdictDocumento = new ArrayList<FacDetAdicional>();
			List<InformacionAdicional> infoAdicional = new ArrayList<InformacionAdicional>();
			List<DetallesAdicionalesReporte> detallesAdiciones = new ArrayList<DetallesAdicionalesReporte>();
			lstFactDetDocumento = facDocumentoServicios.buscarDetDocumentoPorFk(rucEmpresa,valorDetalleDocumento.getCodEstablecimiento(),
					 	 valorDetalleDocumento.getCodPuntoEmision(), 
				 		 valorDetalleDocumento.getSecuencial(),
						 valorDetalleDocumento.getCodigoDocumento(),
						 valorDetalleDocumento.getAmbiente());
			lstFactDetAdictDocumento = facDocumentoServicios.buscarDetDocumentoAdicPorFk(rucEmpresa,valorDetalleDocumento.getCodEstablecimiento(),
				 	 valorDetalleDocumento.getCodPuntoEmision(), 
			 		 valorDetalleDocumento.getSecuencial(),
					 valorDetalleDocumento.getCodigoDocumento(),
					 valorDetalleDocumento.getAmbiente());
			for (FacDetDocumento detDocumento : lstFactDetDocumento) {
				FacProducto producto = new FacProducto();
	            DetallesAdicionalesReporte detAd = new DetallesAdicionalesReporte();
	            detAd.setCodigoPrincipal(detDocumento.getCodPrincipal());
	            //VPI se comenta
	            //detAd.setCodigoPrincipal(detDocumento.getCodAuxiliar());
	            detAd.setDescuento(df.format(detDocumento.getDescuento()));
	            detAd.setCodigoAuxiliar(detDocumento.getCodAuxiliar());
	            detAd.setDescripcion(detDocumento.getDescripcion());
	            detAd.setCantidad(String.valueOf(df.format(detDocumento.getCantidad())));
	            detAd.setPrecioTotalSinImpuesto(df.format(detDocumento.getPrecioTotalSinImpuesto()));
	            detAd.setPrecioUnitario(df.format(detDocumento.getPrecioUnitario()));
	            detAd.setInfoAdicional(infoAdicional.isEmpty() ? null : infoAdicional);            
	            detallesAdiciones.add(detAd);		
			}
	        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(detallesAdiciones);  
	        pathReport = pathReports.get(valorDetalleDocumento.getCodigoDocumento());
	        //String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(pathReport);
	        String reportPath = pathReport;
	        jasperPrint = JasperFillManager.fillReport(reportPath, obtenerMapaParametrosReportes(obtenerParametrosInfoTriobutaria(valorDetalleDocumento), obtenerInfoFactura(lstFactDetAdictDocumento,valorDetalleDocumento)), beanCollectionDataSource);  
		}
		if (valorDetalleDocumento.getCodDoc().equals("07"))
		{
					
			FileInputStream is = null;
		    JRDataSource dataSource = null;
			List<FacDetDocumento> lstFactDetDocumento = new ArrayList<FacDetDocumento>();
			List<FacDetAdicional> lstFactDetAdictDocumento = new ArrayList<FacDetAdicional>();
			List<InformacionAdicional> infoAdicional = new ArrayList<InformacionAdicional>();
			List<DetallesAdicionalesReporte> detallesAdiciones = new ArrayList<DetallesAdicionalesReporte>();
	
			List<FacDetRetencion> lstRetencion = new ArrayList<FacDetRetencion>();
			lstRetencion = facDocumentoServicios.BuscarDatosdeRetenciones(rucEmpresa,
														   valorDetalleDocumento.getCodEstablecimiento(),
														   valorDetalleDocumento.getCodPuntoEmision(),
														   valorDetalleDocumento.getSecuencial(),
														   valorDetalleDocumento.getCodigoDocumento(),
														   valorDetalleDocumento.getAmbiente());
			
			lstFactDetAdictDocumento = facDocumentoServicios.buscarDetDocumentoAdicPorFk(rucEmpresa,valorDetalleDocumento.getCodEstablecimiento(),
				 	 valorDetalleDocumento.getCodPuntoEmision(), 
			 		 valorDetalleDocumento.getSecuencial(),
					 valorDetalleDocumento.getCodigoDocumento(),
					 valorDetalleDocumento.getAmbiente());
			
			for (FacDetRetencion detRetencion : lstRetencion) {
	            DetallesAdicionalesReporte detAd = new DetallesAdicionalesReporte();
	            String ls_nombreComprobante = ""; 
	            if (detRetencion.getCodDocSustento().equals("01"))
	            	ls_nombreComprobante = "FACTURA";
	            if (detRetencion.getCodDocSustento().equals("04"))
	            	ls_nombreComprobante = "NOTA DE CREDITO";
		            detAd.setNombreComprobante(ls_nombreComprobante);
		            detAd.setNumeroComprobante(detRetencion.getNumDocSustento());
		            detAd.setFechaEmisionCcompModificado(detRetencion.getFechaEmisionDocSustento());
		            detAd.setBaseImponible(String.valueOf(detRetencion.getBaseImponible()).toString());
		            //detAd.setNombreImpuesto(ls_nombreComprobante);		            
		             
		            String descripcion = "";
					 if (detRetencion.getCodPorcentaje().equals("1")){
						 descripcion = "RET. FTE";
					 }
					 if (detRetencion.getCodPorcentaje().equals("2")){
						 descripcion = "IVA";
					 }
					 if (detRetencion.getCodPorcentaje().equals("6")){
						 descripcion = "ISD";
					 }		
					 
					 detAd.setNombreImpuesto(descripcion);
					 
		            detAd.setPorcentajeRetencion(detRetencion.getCodPorcentaje());
		            detAd.setPorcentajeRetener(detRetencion.getPorcentajeRetencion()+" %");
		            detAd.setValorRetenido(String.valueOf(detRetencion.getValor()).toString());
	            detallesAdiciones.add(detAd);		
			}
			
			JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(detallesAdiciones);  
	        //String pathReport = "C:\\DataExpress\\DMIRO\\generales\\Jasper\\factura.jasper";
	        pathReport = pathReports.get(valorDetalleDocumento.getCodigoDocumento());
	        //String pathReport = "/reportes/factura.jasper"; 
	        
	        //String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(pathReport); 
	        String reportPath = pathReport;
	        jasperPrint = JasperFillManager.fillReport(reportPath, obtenerMapaParametrosReportes(obtenerParametrosInfoTriobutaria(valorDetalleDocumento), obtenerInfoFactura(lstFactDetAdictDocumento,valorDetalleDocumento)), beanCollectionDataSource);
		}
		
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();  
        httpServletResponse.addHeader("Content-disposition", "attachment; filename="+name+".pdf");  
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();  
        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);  
        FacesContext.getCurrentInstance().responseComplete();  
    }
	
	public void pdfMail(facDetalleDocumentoEntidad valorDetalleDocumento) throws Throwable{ 
		System.out.println("::PDF::");
		System.out.println("CodDoc::"+valorDetalleDocumento.getCodDoc());
		System.out.println("RucPdf::"+rucEmpresa);
		
		valorDetalleDocumento.setCodigoDocumento(valorDetalleDocumento.getCodDoc());
		System.out.println("CodEstablecimiento::"+valorDetalleDocumento.getCodEstablecimiento());
		System.out.println("CodigoDocumento::"+valorDetalleDocumento.getCodigoDocumento());
		System.out.println("CodPuntoEmision::"+valorDetalleDocumento.getCodPuntoEmision());
		System.out.println("Secuencial::"+valorDetalleDocumento.getSecuencial());
		pathReports = facGenSer.buscarDatosGeneralPadreHash("103");
		String pathReport = pathReports.get(valorDetalleDocumento.getCodigoDocumento());
		System.out.println("pathReports::"+pathReports.toString());
		System.out.println("pathReport::"+pathReport);
		
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(','); 
        DecimalFormat df = new DecimalFormat("###,##0.00",otherSymbols);
                
        //DecimalFormat dfPorcentaje = new DecimalFormat("###,##0 %",otherSymbols);
        
		File dirTemp = new File(System.getProperty("java.io.tmpdir"));
		if(!dirTemp.exists())
			dirTemp.mkdirs();
        String name = dirTemp + dirTemp.separator+valorDetalleDocumento.getPDFARC();
        
		if (valorDetalleDocumento.getCodDoc().equals("04")||valorDetalleDocumento.getCodDoc().equals("01"))
		{
				FileInputStream is = null;
			    JRDataSource dataSource = null;
				List<FacDetDocumento> lstFactDetDocumento = new ArrayList<FacDetDocumento>();
				List<FacDetAdicional> lstFactDetAdictDocumento = new ArrayList<FacDetAdicional>();
				List<InformacionAdicional> infoAdicional = new ArrayList<InformacionAdicional>();
				List<DetallesAdicionalesReporte> detallesAdiciones = new ArrayList<DetallesAdicionalesReporte>();
				
				lstFactDetDocumento = facDocumentoServicios.buscarDetDocumentoPorFk(rucEmpresa,valorDetalleDocumento.getCodEstablecimiento(),
						 	 valorDetalleDocumento.getCodPuntoEmision(), 
					 		 valorDetalleDocumento.getSecuencial(),
							 valorDetalleDocumento.getCodigoDocumento(),
							 valorDetalleDocumento.getAmbiente());
				
				lstFactDetAdictDocumento = facDocumentoServicios.buscarDetDocumentoAdicPorFk(rucEmpresa,valorDetalleDocumento.getCodEstablecimiento(),
					 	 valorDetalleDocumento.getCodPuntoEmision(), 
				 		 valorDetalleDocumento.getSecuencial(),
						 valorDetalleDocumento.getCodigoDocumento(),
						 valorDetalleDocumento.getAmbiente());
				
		        
				for (FacDetDocumento detDocumento : lstFactDetDocumento) {
					FacProducto producto = new FacProducto();
		            DetallesAdicionalesReporte detAd = new DetallesAdicionalesReporte();
		            detAd.setCodigoPrincipal(detDocumento.getCodPrincipal());
		            //VPI se comenta para que no sobreescriba con null
		            //detAd.setCodigoPrincipal(detDocumento.getCodAuxiliar());
		            detAd.setDescuento(df.format(detDocumento.getDescuento()));
		            detAd.setCodigoAuxiliar(detDocumento.getCodAuxiliar());
		            detAd.setDescripcion(detDocumento.getDescripcion());
		            detAd.setCantidad(String.valueOf(df.format(detDocumento.getCantidad())));
		            detAd.setPrecioTotalSinImpuesto(df.format(detDocumento.getPrecioTotalSinImpuesto()));
		            detAd.setPrecioUnitario(df.format(detDocumento.getPrecioUnitario()));
		            detAd.setInfoAdicional(infoAdicional.isEmpty() ? null : infoAdicional);            
		            detallesAdiciones.add(detAd);		
				}
		

								
				//String name = FacesContext.getCurrentInstance().getExternalContext().getRealPath(dirTemp + dirTemp.separator+valorDetalleDocumento.getXMLARC());//"C:\\DataExpress\\DMIRO\\";
				
				JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(detallesAdiciones);
		        pathReport = pathReports.get(valorDetalleDocumento.getCodigoDocumento());
		        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(pathReport);
		        JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, obtenerMapaParametrosReportes(obtenerParametrosInfoTriobutaria(valorDetalleDocumento), obtenerInfoFactura(lstFactDetAdictDocumento,valorDetalleDocumento)), beanCollectionDataSource);
		        //HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();  
		        //httpServletResponse.addHeader("Content-disposition", "attachment; filename="+name+".pdf");  
		        //ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();  
		        //JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
		        JasperExportManager.exportReportToPdfFile(jasperPrint, name);
		}
        //FacesContext.getCurrentInstance().responseComplete(); 
		if (valorDetalleDocumento.getCodDoc().equals("07"))
		{
			FileInputStream is = null;
		    JRDataSource dataSource = null;
			List<FacDetDocumento> lstFactDetDocumento = new ArrayList<FacDetDocumento>();
			List<FacDetAdicional> lstFactDetAdictDocumento = new ArrayList<FacDetAdicional>();
			List<InformacionAdicional> infoAdicional = new ArrayList<InformacionAdicional>();
			List<DetallesAdicionalesReporte> detallesAdiciones = new ArrayList<DetallesAdicionalesReporte>();
			
			List<FacDetRetencion> lstRetencion = new ArrayList<FacDetRetencion>();
			lstRetencion = facDocumentoServicios.BuscarDatosdeRetenciones(rucEmpresa,
														   valorDetalleDocumento.getCodEstablecimiento(),
														   valorDetalleDocumento.getCodPuntoEmision(),
														   valorDetalleDocumento.getSecuencial(),
														   valorDetalleDocumento.getCodigoDocumento(),
														   valorDetalleDocumento.getAmbiente());
			
			lstFactDetAdictDocumento = facDocumentoServicios.buscarDetDocumentoAdicPorFk(rucEmpresa,valorDetalleDocumento.getCodEstablecimiento(),
				 	 valorDetalleDocumento.getCodPuntoEmision(), 
			 		 valorDetalleDocumento.getSecuencial(),
					 valorDetalleDocumento.getCodigoDocumento(),
					 valorDetalleDocumento.getAmbiente());
			
	        
	        for (FacDetRetencion detRetencion : lstRetencion) {
	            DetallesAdicionalesReporte detAd = new DetallesAdicionalesReporte();
	            String ls_nombreComprobante = ""; 
	            if (detRetencion.getCodDocSustento().equals("01"))
	            	ls_nombreComprobante = "FACTURA";
	            if (detRetencion.getCodDocSustento().equals("04"))
	            	ls_nombreComprobante = "NOTA DE CREDITO";
		            detAd.setNombreComprobante(ls_nombreComprobante);
		            detAd.setNumeroComprobante(detRetencion.getNumDocSustento());
		            detAd.setFechaEmisionCcompModificado(detRetencion.getFechaEmisionDocSustento());
		            detAd.setBaseImponible(String.valueOf(detRetencion.getBaseImponible()));
		            String descripcion = "";
						 if (detRetencion.getCodPorcentaje().equals("1")){
							 descripcion = "RENTA";
						 }
						 if (detRetencion.getCodPorcentaje().equals("2")){
							 descripcion = "IVA";
						 }
						 if (detRetencion.getCodPorcentaje().equals("6")){
							 descripcion = "ISD";
						 }
		            detAd.setNombreImpuesto(descripcion);
		            detAd.setPorcentajeRetener(detRetencion.getPorcentajeRetencion()+" %");
		            detAd.setValorRetenido(String.valueOf(detRetencion.getValor()));
	            detallesAdiciones.add(detAd);		
			}
	
			JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(detallesAdiciones);
	        //pathReport = pathReports.get(valorDetalleDocumento.getCodigoDocumento());
			String reportPath = pathReports.get(valorDetalleDocumento.getCodigoDocumento());
	        //String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(pathReport);
	        JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, obtenerMapaParametrosReportes(obtenerParametrosInfoTriobutaria(valorDetalleDocumento), obtenerInfoFactura(lstFactDetAdictDocumento,valorDetalleDocumento)), beanCollectionDataSource);
	        //HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();  
	        //httpServletResponse.addHeader("Content-disposition", "attachment; filename="+name+".pdf");  
	        //ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();  
	        //JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
	        JasperExportManager.exportReportToPdfFile(jasperPrint, name);
		}
    }
	
	public static File stringToArchivo(String rutaArchivo, String contenidoArchivo)
	  {
	    FileOutputStream fos = null;
	    File archivoCreado = null;
	    try
	    {
	    	System.out.println("error::"+rutaArchivo);
	      fos = new FileOutputStream(rutaArchivo);
	      OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");
	      for (int i = 0; i < contenidoArchivo.length(); i++) {
	        out.write(contenidoArchivo.charAt(i));
	      }
	      out.close();

	      archivoCreado = new File(rutaArchivo);
	    }
	    catch (Exception ex)
	    {
	      int i;
	      System.out.println(ex.getMessage());
	      return null;
	    } finally {
	      try {
	        if (fos != null)
	          fos.close();
	      }
	      catch (Exception ex) {
	    	  System.out.println(ex.getMessage());
	      }
	    }
	    return archivoCreado;
	  }
	
	public void xml() throws Throwable{ 
		File dirTemp = new File(System.getProperty("java.io.tmpdir"));
		if(!dirTemp.exists())
			dirTemp.mkdirs();
		try{
		File dirFile = new File(dirTemp + dirTemp.separator+valorDetalleDocumento.getXMLARC());
		if(dirFile.exists())
			dirFile.exists();
		}catch(Exception e){
			;;
		}
		String nameDocument = (dirTemp + dirTemp.separator+valorDetalleDocumento.getXMLARC());//"C:\\DataExpress\\DMIRO\\";
		System.out.println("File Xml::"+nameDocument);

		stringToArchivo(nameDocument, valorDetalleDocumento.getXmlAutorizacion());
		
        
		String Documento = nameDocument;
		Documento.replace("/", "\"");
		
		File ficheroXLS = new File(Documento);
		FacesContext ctx = FacesContext.getCurrentInstance();
		FileInputStream fis = new FileInputStream(ficheroXLS);
		byte[] bytes = new byte[1000];
		int read = 0;

		if (!ctx.getResponseComplete()) {
		   String fileName = ficheroXLS.getName();
		   String contentType = "application/" + (nameDocument.contains(".pdf") ? "pdf" : "xml");
		   HttpServletResponse response =(HttpServletResponse) ctx.getExternalContext().getResponse();
		   response.setContentType(contentType);
		   response.setHeader("Content-Disposition","attachment;filename=" + fileName);
		   ServletOutputStream outxml = response.getOutputStream();

		   while ((read = fis.read(bytes)) != -1) {
			   outxml.write(bytes, 0, read);
		   }

		   outxml.flush();
		   outxml.close();
		   ctx.responseComplete();
		}		
	}
	private Map<String, Object> obtenerParametrosInfoTriobutaria(facDetalleDocumentoEntidad valorDetalleDocumento)
	   {
	     Map param = new HashMap();
	 
	     FacCabDocumento cabDoc = new FacCabDocumento();
	     FacEmpresa empresa = new FacEmpresa();
	     FacEstablecimiento establecimiento = new FacEstablecimiento();
	     empresa=facServEmpresa.verificarRuc(rucEmpresa);
	     try {
	       cabDoc = facDocumentoServicios.buscarCabDocumentoPorPk(rucEmpresa,valorDetalleDocumento.getCodEstablecimiento(),
																		 	 valorDetalleDocumento.getCodPuntoEmision(), 
																	 		 valorDetalleDocumento.getSecuencial(),
																			 valorDetalleDocumento.getCodigoDocumento(),
																			 valorDetalleDocumento.getAmbiente());
	       if (cabDoc != null) {
	         empresa = facDocumentoServicios.listadoEmpr(rucEmpresa);
	         establecimiento = facDocumentoServicios.buscarCodEstablecimiento(rucEmpresa,valorDetalleDocumento.getCodEstablecimiento());
	         param.put("RUC", rucEmpresa);
	         param.put("CLAVE_ACC", (cabDoc.getClaveAcceso().trim().equals("")) || (cabDoc.getClaveAcceso() == null) ? "SIN CLAVE" : cabDoc.getClaveAcceso());
	         param.put("RAZON_SOCIAL", empresa.getRazonSocial());
	         param.put("NOM_COMERCIAL", empresa.getRazonComercial());
	         param.put("DIR_MATRIZ", empresa.getDireccionMatriz());
	         param.put("SUBREPORT_DIR", pathSubReport);
	         param.put("TIPO_EMISION", cabDoc.getTipoEmision().trim().equals("1") ? "NORMAL" : "CONTINGENCIA");
	         param.put("NUM_AUT", (cabDoc.getAutorizacion() == null) || (cabDoc.getAutorizacion().equals("")) ? "" : cabDoc.getAutorizacion());
	         param.put("FECHA_AUT", cabDoc.getFechaAutorizado() == null ? "" : cabDoc.getFechaAutorizado());
	         param.put("NUM_FACT", cabDoc.getId().getCodEstablecimiento() + "-" + cabDoc.getId().getCodPuntEmision() + "-" + cabDoc.getId().getSecuencial());
	         if (cabDoc.getId().getCodigoDocumento().equals("04")){
	        	 param.put("NUM_DOC_MODIFICADO", (cabDoc.getNumDocSustento() == null) || (cabDoc.getNumDocSustento() .equals("")) ? "" : cabDoc.getNumDocSustento() );
	        	 param.put("FECHA_EMISION_DOC_SUSTENTO", cabDoc.getFechaEmisionDocSustento() == null ? "" : cabDoc.getFechaEmisionDocSustento());
	         }
	         param.put("AMBIENTE", cabDoc.getId().getAmbiente().intValue() == 1 ? "PRUEBA" : "PRODUCCION");
	         param.put("DIR_SUCURSAL", establecimiento.getDireccionEstablecimiento());
	         param.put("CONT_ESPECIAL", empresa.getContribEspecial());
	         //param.put("LLEVA_CONTABILIDAD", cabDoc.getObligadoContabilidad()); 
	         //CPA
	         if (empresa.getObligContabilidad()!=null){
		         if ((empresa.getObligContabilidad().equals("S"))||(empresa.getObligContabilidad().equals("SI")))
		         param.put("LLEVA_CONTABILIDAD", "SI");
		         if ((empresa.getObligContabilidad().equals("N"))||(empresa.getObligContabilidad().equals("NO")))
			         param.put("LLEVA_CONTABILIDAD", "NO");
	         }else{
	        	 param.put("LLEVA_CONTABILIDAD", "NO");
	         }
	         
	         if (empresa.getPathLogoEmpresa() != null){
	        	 if (empresa.getPathLogoEmpresa().length()>0)
	        	 param.put("LOGO", empresa.getPathLogoEmpresa());
	         }
	         
	       //VPI se agregan campos para reporte
	         param.put("TELEFONO_LOCAL",establecimiento.getTelefono());
	         param.put("CORREO_RETENCION", establecimiento.getCorreo());
	         param.put("MOTIVO", cabDoc.getMotivoRazon());
	         
	         
	       }
	     }
	     catch (Exception e) { e.printStackTrace(); }
	 
	     return param;
	   }
	
	private Map<String, Object> obtenerInfoFactura(List<FacDetAdicional> lstFactDetAdictDocumento,facDetalleDocumentoEntidad valorDetalleDocumento)
	   {
	     Map param = new HashMap();
	 
	     FacCabDocumento cabDoc = new FacCabDocumento();
	     try {
	       cabDoc = facDocumentoServicios.buscarCabDocumentoPorPk(rucEmpresa,valorDetalleDocumento.getCodEstablecimiento(),
																 	 valorDetalleDocumento.getCodPuntoEmision(), 
															 		 valorDetalleDocumento.getSecuencial(),
																	 valorDetalleDocumento.getCodigoDocumento(),
																	 valorDetalleDocumento.getAmbiente());
	       if (cabDoc != null) {
	         param.put("RS_COMPRADOR", cabDoc.getRazonSocialComprador());
	         param.put("RUC_COMPRADOR", cabDoc.getIdentificacionComprador());
	         SimpleDateFormat dateFormat = 
	     	            new SimpleDateFormat("dd/MM/yyyy");
		         param.put("FECHA_EMISION", dateFormat.format(cabDoc.getFechaEmision()));
	         param.put("GUIA", cabDoc.getGuiaRemision());
	         
	         DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
	         otherSymbols.setDecimalSeparator('.');
	         otherSymbols.setGroupingSeparator(','); 
	         DecimalFormat df = new DecimalFormat("###,##0.00",otherSymbols);
	         if (valorDetalleDocumento.getCodigoDocumento().equals("01")){
	        	 //double total = Double.valueOf(cabDoc.getImporteTotal())+cabDoc.getSubtotal12()+cabDoc.getSubtotalNoIva()+cabDoc.getSubtotal0()+cabDoc.getIva12()+cabDoc.getTotalvalorICE();
	        	 ///VPI se comenta xq se duplica total
	        	 double total = cabDoc.getImporteTotal();
	        	 param.put("VALOR_TOTAL", df.format(total));
	         }
	        	
	         if (valorDetalleDocumento.getCodigoDocumento().equals("04")){
	        	 //double total = cabDoc.getSubtotal12()+cabDoc.getSubtotalNoIva()+cabDoc.getSubtotal0()+cabDoc.getIva12()+cabDoc.getTotalvalorICE();
	        	///VPI se comenta xq se duplica total
	        	 double total = cabDoc.getImporteTotal();
	        	 param.put("VALOR_TOTAL", df.format(total));
	         }
	         if (valorDetalleDocumento.getCodigoDocumento().equals("07")){
	        	 dateFormat = 
	        	            new SimpleDateFormat("MM/yyyy");
	        	 param.put("EJERCICIO_FISCAL",dateFormat.format(cabDoc.getFechaEmision()));
	         }
	         param.put("IVA", df.format(Double.valueOf(cabDoc.getIva12())));
	         param.put("IVA_0", df.format(Double.valueOf(cabDoc.getSubtotal0())));
	         param.put("IVA_12", df.format(Double.valueOf(cabDoc.getSubtotal12())));
	         param.put("ICE", df.format(Double.valueOf(cabDoc.getTotalvalorICE())));
	         param.put("NO_OBJETO_IVA", df.format(Double.valueOf(cabDoc.getSubtotalNoIva())));
	         param.put("SUBTOTAL", df.format(Double.valueOf(cabDoc.getTotalSinImpuesto())));
	         param.put("PROPINA", df.format(Double.valueOf(cabDoc.getPropina())));
	         param.put("TOTAL_DESCUENTO", df.format(Double.valueOf(cabDoc.getTotalDescuento())));
	       }
	       
	       for (FacDetAdicional detAdic : lstFactDetAdictDocumento) {							
		        param.put(detAdic.getNombre(), detAdic.getValor());
			}
	     } catch (Exception e) {
	       e.printStackTrace();
	     }
	     return param;
	   }
	
	private Map<String, Object> obtenerMapaParametrosReportes(Map<String, Object> mapa1, Map<String, Object> mapa2)
	   {
	     mapa1.putAll(mapa2);
	     return mapa1;
	   }

	public String getSistemaOperativo() {
		return sistemaOperativo;
	}

	public void setSistemaOperativo(String sistemaOperativo) {
		this.sistemaOperativo = sistemaOperativo;
	}

	public String getPathTemp() {
		return pathTemp;
	}

	public void setPathTemp(String pathTemp) {
		this.pathTemp = pathTemp;
	}

	public String getPathSubReport() {
		return pathSubReport;
	}

	public void setPathSubReport(String pathSubReport) {
		this.pathSubReport = pathSubReport;
	}

	public HashMap<String, String> getPathReports() {
		return pathReports;
	}

	public void setPathReports(HashMap<String, String> pathReports) {
		this.pathReports = pathReports;
	}

	public String getSeleccionTipoEstado() {
		return SeleccionTipoEstado;
	}

	public void setSeleccionTipoEstado(String seleccionTipoEstado) {
		SeleccionTipoEstado = seleccionTipoEstado;
	}

	public String getNumDocumento() {
		return numDocumento;
	}

	public void setNumDocumento(String numDocumento) {
		this.numDocumento = numDocumento;
	}

	public ArrayList<SelectItem> getTipoEstados() {
		return TipoEstados;
	}

	public void setTipoEstados(ArrayList<SelectItem> tipoEstados) {
		TipoEstados = tipoEstados;
	}
	
	
}
