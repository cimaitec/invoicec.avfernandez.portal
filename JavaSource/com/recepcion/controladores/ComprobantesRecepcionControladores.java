package com.recepcion.controladores;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.jdom2.Document;         // |
import org.jdom2.Element;          // |\ Librerías
//import org.jdom2.JDOMException;    // |/ JDOM
import org.jdom2.input.SAXBuilder; // |

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.documentos.entidades.FacEmpresa;
import com.general.controladores.FacEnviarMail;
import com.recepcion.servicios.ComprobanteRecepcionServicios;


@ViewScoped
@ManagedBean
public class ComprobantesRecepcionControladores {
	
	@EJB
	private ComprobanteRecepcionServicios RecepcionServicios;
	
	private FacEmpresa Empresa;
	private FacEnviarMail claseEmail;// variable que contiene la clase de email
	
	//TODO contructor que se encarga de cargar los datos 
	public void cargarDatos(){
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			HttpSession sesion = (HttpSession)context.getExternalContext().getSession(true);
			if(sesion.getAttribute("Ruc_Empresa") == null)
			{
				ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
				String ctxPath = ((ServletContext) ctx.getContext()).getContextPath();
				try {
					ctx.redirect(ctxPath + "/paginas/Administrador/Cuenta/Login.jsf");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			// datos empresa emisora
			cargarEmpresa(sesion.getAttribute("Ruc_Empresa").toString().trim());
			claseEmail = new FacEnviarMail(); 
		}catch (Exception e) {
			FacesMessage mensaje=null;
			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		}
	}
		
	//TODO contructor que se encarga de buscar el dialogo
	public void CargarArchivo(FileUploadEvent event){
		
		UploadedFile upload = event.getFile();
		File savedFileName;
		String dirPath = Empresa.getPathCompRecepcion();
		InputStream fileContent = null;
		try {
			fileContent = upload.getInputstream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		savedFileName = new File(dirPath.replace("/", "\"") + upload.getFileName());
		if(savedFileName.isFile())
		savedFileName.delete();
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(savedFileName));
		} catch (FileNotFoundException e) {
		}
		byte[] buffer = new byte[1024];
		int len;
		try {
			while ((len = fileContent.read(buffer)) >= 0) {
				bos.write(buffer, 0, len);
			}
		} catch (IOException e) {
		}
		try {
			fileContent.close();
			bos.close();
		} catch (IOException e) {
		}
		
		ValidandoFormatoXML(savedFileName);
		
		mensajeAlerta("Mensaje del sistema", "nombre del archivo" + event.getFile().getFileName(), "Informacion");
		

	}
		
	//TODO contrucrtor que valida el xml del proveedor o cliente que lo cargo al servidor
	private boolean ValidandoFormatoXML(File Archivo){
		//Se crea el documento a traves del archivo
		try {
				/** Creamos el documento xml a partir del archivo File **/
				SAXBuilder constructorSAX = new SAXBuilder();
				Document documento = (Document)constructorSAX.build(Archivo);
				/** Obtenemos el nodo raiz o principal **/
				Element nodoRaiz1 = documento.getRootElement();
				@SuppressWarnings("rawtypes")
				List listaacomprobante = null;
				String Nombre_Cabecera = "";
				/** verificando que sea de un formato sencillo en caso de ser True
				 * caso contrario filtra hasta llegar al punto  del documento**/
				if(nodoRaiz1.getName().trim().equals("factura") || nodoRaiz1.getName().trim().equals("comprobanteRetencion") || 
						nodoRaiz1.getName().trim().equals("guiaRemision") || nodoRaiz1.getName().trim().equals("notaCredito") ||
						 nodoRaiz1.getName().trim().equals("notaDebito"))
				{
					listaacomprobante = nodoRaiz1.getChildren("infoTributaria");
					Nombre_Cabecera = nodoRaiz1.getName();
				}
			
				/** aqui va todo igual para en cualquier formato de xml **/
				if(listaacomprobante.size() == 0){
					mensajeAlerta("Mensaje del sistema", "nombre del archivo:: " + Nombre_Cabecera + " \n no tiene al información tributaria", "alerta");
					return true;
				}
				StringBuffer body = new StringBuffer();
				body.append("Estimado(a);  <br>");
				body.append("Acaba de recibir el(los) documento(s) electrónico cargado con fecha " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
				body.append("<br><br>Saludos cordiales, ");
				
				claseEmail.enviar(Empresa, //empresas
						  Empresa.getCorreoRecepcion(), //toAddress
						  "",//ccAddress
						  "",//bccAddresss
						  "Documento electrónico No:",//subject
						  true, //xisHTMLFormat
						  body, //body
						  false,//debug
						  "",
						  new String [] { Archivo.getPath() }////Adjuntararchivos
						  );
				
			} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
//
//	
//	//TODO contructor de la factura
//	private boolean FacturaXML(Element nodoRaiz1){
//		@SuppressWarnings("rawtypes")
//		List listainfoFactura = nodoRaiz1.getChildren("infoFactura");
//		for (int i = 0; i < listainfoFactura.size(); i++) {
//			Element nodo = (Element)listainfoFactura.get(i);
//			
//			 if(nodo.getChildTextTrim("fechaEmision") == "")
//			    	return true;
//			 if(nodo.getChildTextTrim("obligadoContabilidad") == "")
//			    	return true;
//			 if(nodo.getChildTextTrim("tipoIdentificacionComprador") == "")
//			    	return true;
//			 if(nodo.getChildTextTrim("guiaRemision") == "")
//			    	return true;
//			 if(nodo.getChildTextTrim("razonSocialComprador") == "")
//			    	return true;
//			 if(nodo.getChildTextTrim("identificacionComprador") == "")
//			    	return true;
//			 if(nodo.getChildTextTrim("totalSinImpuestos") == "")
//			    	return true;
//			 if(nodo.getChildTextTrim("totalDescuento") == "")
//			    	return true;
//			
//			 @SuppressWarnings("rawtypes")
//			 List listatotalImpuesto = nodo.getChildren("totalImpuesto");
//			 for (int j = 0; j < listatotalImpuesto.size(); j++) {
//				 Element nodoj = (Element)listatotalImpuesto.get(j);
//				 if(nodoj.getChildTextTrim("codigo") == "")
//				    	return true;
//				 if(nodoj.getChildTextTrim("codigoPorcentaje") == "")
//				    	return true;
//				 if(nodoj.getChildTextTrim("baseImponible") == "")
//				    	return true;
//				 if(nodoj.getChildTextTrim("tarifa") == "")
//				    	return true;
//				 if(nodoj.getChildTextTrim("valor") == "")
//				    	return true;
//			 }
//			 
//			 if(nodo.getChildTextTrim("propina") == "")
//			    	return true;
//			 if(nodo.getChildTextTrim("importeTotal") == "")
//			    	return true;
//			 if(nodo.getChildTextTrim("moneda") == "")
//			    	return true;
//		}
//	
//		@SuppressWarnings("rawtypes")
//		List listadetalles = nodoRaiz1.getChildren("detalles");
//		Element nododetalle = (Element)listadetalles.get(0);
//		@SuppressWarnings("rawtypes")
//		List listatodetalle = nododetalle.getChildren("detalle");
//		for (int i = 0; i < listatodetalle.size(); i++) {
//			Element nodo = (Element)listatodetalle.get(i);
//			if(nodo.getChildTextTrim("codigoPrincipal") == "")
//		    	return true;
//			if(nodo.getChildTextTrim("descripcion") == "")
//		    	return true;
//			if(nodo.getChildTextTrim("cantidad") == "")
//		    	return true;
//			if(nodo.getChildTextTrim("precioUnitario") == "")
//		    	return true;
//			if(nodo.getChildTextTrim("descuento") == "")
//		    	return true;
//			if(nodo.getChildTextTrim("precioTotalSinImpuesto") == "")
//		    	return true;
//			@SuppressWarnings("rawtypes")
//			List listaimpuestos = nodo.getChildren("impuestos");
//			Element nodoimpuestos = (Element)listaimpuestos.get(0);
//			@SuppressWarnings("rawtypes")
//			List listatotalimpuesto = nodoimpuestos.getChildren("impuesto");
//			for (int j = 0; j < listatotalimpuesto.size(); j++) {
//				Element nodoj = (Element)listatotalimpuesto.get(j);
//				if(nodoj.getChildTextTrim("codigo") == "")
//			    	return true;
//				if(nodoj.getChildTextTrim("codigoPorcentaje") == "")
//			    	return true;
//				if(nodoj.getChildTextTrim("tarifa") == "")
//			    	return true;
//				if(nodoj.getChildTextTrim("baseImponible") == "")
//			    	return true;
//				if(nodoj.getChildTextTrim("valor") == "")
//			    	return true;
//			}
//		}
//		return false;
//	}
	
	//TODO contructor de mensaje de alerta para mostrar al usuario
	private void mensajeAlerta(String mensajeVentana, String mensajeDetalle, String tipo) {
	       FacesContext context = FacesContext.getCurrentInstance();            
	       context.addMessage(null, new FacesMessage((tipo.toString().trim().equals("alerta") ? FacesMessage.SEVERITY_ERROR : tipo.toString().trim().equals("peligro") ? FacesMessage.SEVERITY_WARN : FacesMessage.SEVERITY_INFO),mensajeVentana, mensajeDetalle));
	       RequestContext reqContext = RequestContext.getCurrentInstance();  //get your hands on request context
		   reqContext.update(":frm1:growl"); //update the datatable for each execution
	}
	
	//TODO contructor que carga los datos de la empresa emisora
	private FacEmpresa cargarEmpresa(String ruc){
		Empresa = new FacEmpresa();
		try{
			Empresa = RecepcionServicios.verificarRuc(ruc);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return Empresa;
	}


}
