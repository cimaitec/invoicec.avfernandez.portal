package com.puntoemision.controladores;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.primefaces.event.RowEditEvent;

import com.documentos.entidades.FacEmpresa;
import com.general.entidades.FacBitacoraAuditoria;
import com.general.entidades.FacEstablecimiento;
import com.general.entidades.FacPuntoEmision;
import com.general.entidades.FacPuntoEmisionPK;
import com.general.entidades.FacTiposDocumento;
import com.general.entidades.facDetallePuntoEmision;
import com.general.servicios.FacBitacoraAuditoriaServicio;
import com.puntoemision.servicios.fac_puntoemisionServicios;

@ViewScoped
@ManagedBean
public class Fac_puntoEmisionControlador {
	
	@EJB
	private fac_puntoemisionServicios puntoEmision;
	
	private String seleccionaEmpresa;
	private List<SelectItem> listaEmpresa;
	private String seleccionaEstablecimiento;
	private List<SelectItem> listaEstablecimiento;
	private List<facDetallePuntoEmision> datapuntoEmision;
	private List<SelectItem> listaTipodocumento;
	private List<SelectItem> listaAmbiente;
	private List<SelectItem> listaEstado;
	private String estiloColumna;
	private String caja;
	private String formaEmision;
	private List<SelectItem> listaFormaEmision;
	
	

	private facDetallePuntoEmision SeleccionarlistaPuntoEmision; 
	
	// datos de tabla dinamica
	private List<FacEstablecimiento> tablaEstablecimiento;
	private List<FacEstablecimiento> filtrotablaEstablecimiento;
	private FacEstablecimiento verCamposEstablecimiento;
	private String estiloColumnBottonDelete;
	
	private String fechaCreacion;
	private String userCreacion;
	private String fechaModificacion;
	private String userModificacion;
	private String user;
	
	@EJB
	private FacBitacoraAuditoriaServicio facAudit;

	//TODO Carga de datos dinamicos
	public void CargarTablaDinamica(String filtro){
		try {			
			
			if(filtro.toString().trim().equals("establecimiento")){
				if(seleccionaEmpresa.toString().trim().equals("0"))
				{
					mensajeAlerta("Mensaje del sistema","Por favor seleccione la empresa", "Informacion");
					return;
				}
				tablaEstablecimiento = new ArrayList<FacEstablecimiento>();
				
				tablaEstablecimiento = puntoEmision.BuscarDatosEstablecimeinto(seleccionaEmpresa);
			}
		}catch (Exception e) {
			FacesMessage mensaje=null;
			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
			e.printStackTrace();
		}
	}
	
	// TODO contructor de carga de datos
	public void cargarDatos(){
		try {
			//FacesContext context = FacesContext.getCurrentInstance();
			//HttpSession sesion = (HttpSession)context.getExternalContext().getSession(true);
			FacesContext context = FacesContext.getCurrentInstance();
			HttpSession sesion = (HttpSession)context.getExternalContext().getSession(false);
			user = sesion.getAttribute("id_usuario").toString().trim();
			
			seleccionaEmpresa = "";
			seleccionaEstablecimiento = "";
			datapuntoEmision = new ArrayList<facDetallePuntoEmision>();
			
			datapuntoEmision = new ArrayList<facDetallePuntoEmision>();
			Cargar_Combos();
			estiloColumna = "width:6% ; visibility:hidden";
			estiloColumnBottonDelete = "";
			//seleccionaEmpresa = (String)sesion.getAttribute("Ruc_Empresa");
		}catch (Exception e) {
			FacesMessage mensaje=null;
			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
			e.printStackTrace();
		}
	}

	//TODO evento de seleccionar tabla
	public void SeleccionarCampoTabla(String Filtro){
		if(Filtro.toString().trim().equals("establecimiento")){
			seleccionaEstablecimiento = verCamposEstablecimiento.getId().getCodEstablecimiento();
			ValidarCambioEventosControles("cambioEstablecimiento");
		}
	}
	
	//TODO contructor que valida que el detalle del registro no se repita
	private boolean ValidarRegistroDetalle(List<facDetallePuntoEmision> puntoEmision,List<String> Contenidos){
		Boolean validacion = false;
		int contador = 0;
		for (facDetallePuntoEmision contenido : puntoEmision) 
			if((Contenidos.contains(contenido.getRuc().toString().trim()) == true) &&
					(Contenidos.contains(contenido.getCodEstablecimiento().toString().trim()) == true) && 
					(Contenidos.contains(contenido.getCodPuntoEmision().toString().trim()) == true) && 
					(Contenidos.contains(contenido.getCodTipoDocumento().toString().trim()) == true)&&
					//(Contenidos.contains(contenido.getTipoAmbiente().toString().trim()) == true)&&
					(Contenidos.contains(contenido.getCaja().toString().trim())==true)){
				contador ++;
				if(contador == 2)
					return validacion = true;
			}
		
		return validacion;
	}
	
	// TODO evento del datatable que verifica que celda se a modificado
	public void onEdit(RowEditEvent event) {
		if(ValidarRegistroDetalle(datapuntoEmision, Arrays.asList(((facDetallePuntoEmision) event.getObject()).getCodEstablecimiento(),
																	((facDetallePuntoEmision) event.getObject()).getCodPuntoEmision(),
																	((facDetallePuntoEmision) event.getObject()).getCodTipoDocumento(),
																	((facDetallePuntoEmision) event.getObject()).getTipoAmbiente(),
																	((facDetallePuntoEmision) event.getObject()).getCaja()))){
			mensajeAlerta("Mensaje del sistema", "el registro editado ya se encuenta registrado", "peligro");  
			mensajeAlerta("Mensaje del sistema", "Por favor cambie el registro o no se procedera a guardar", "informacion");  
		}
	}
	
	//TODO contructor que se encarga de cargar todo los combox para la pagina
	private void Cargar_Combos(){
		// cargando combo de la empresa
		List<FacEmpresa> comboEmpresa = new ArrayList<FacEmpresa>();
		listaEmpresa= new ArrayList<SelectItem>();
		try {
			comboEmpresa = puntoEmision.BuscarDatosEmpresa();
			FacEmpresa generales = new FacEmpresa();
			if (comboEmpresa.isEmpty()){			
				generales.setRuc("0");
				generales.setRazonSocial("No hay datos");
				comboEmpresa.add(generales);
			}
			listaEmpresa.add(new SelectItem("0","escoja una opcion"));
			for(int i=0;i<comboEmpresa.size();i++){
				listaEmpresa.add(new SelectItem(comboEmpresa.get(i).getRuc(),comboEmpresa.get(i).getRazonSocial())); 
			}
			
		// cargando combo de tipo de documento
			List<FacTiposDocumento> combodocumento = new ArrayList<FacTiposDocumento>();
			combodocumento = puntoEmision.BuscarDatosTipoDocumentos();
			FacTiposDocumento generalesdocumento = new FacTiposDocumento();
			listaTipodocumento= new ArrayList<SelectItem>();
				if (combodocumento.isEmpty()){
					generalesdocumento.setIdDocumento("0");
					generalesdocumento.setDescripcion("No hay datos");
					combodocumento.add(generalesdocumento);
				}
				listaTipodocumento.add(new SelectItem("0","Escoja una opcion"));
				for(int i=0;i<combodocumento.size();i++){
					listaTipodocumento.add(new SelectItem(combodocumento.get(i).getDescripcion(),combodocumento.get(i).getDescripcion())); 
				}
		//cargando combo tipo ambiente
			listaAmbiente = new ArrayList<SelectItem>();
			listaAmbiente.add(new SelectItem("0", "Seleccione Tipo Ambiente"));
			listaAmbiente.add(new SelectItem("Desarrollo", "Desarrollo"));
			listaAmbiente.add(new SelectItem("Produccion", "Produccion"));
			
		//cargando combo de estado
			listaEstado = new ArrayList<SelectItem>();
			listaEstado.add(new SelectItem("Activo", "Activo"));
			listaEstado.add(new SelectItem("Inactivo", "Inactivo"));
			
		//cargando Forma de emision
			listaFormaEmision = new ArrayList<SelectItem>();
			listaFormaEmision.add(new SelectItem("0", "Seleccione Forma de Emision"));
			listaFormaEmision.add(new SelectItem("Normal", "Normal"));
			listaFormaEmision.add(new SelectItem("Contingencia", "Contingencia"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//TODO contructor de mensaje de alerta
	private void mensajeAlerta(String mensajeVentana, String mensajeDetalle, String tipo) {
		 FacesContext context = FacesContext.getCurrentInstance();            
	     context.addMessage(null, new FacesMessage((tipo.toString().trim().equals("alerta") ? FacesMessage.SEVERITY_ERROR : tipo.toString().trim().equals("peligro") ? FacesMessage.SEVERITY_WARN : FacesMessage.SEVERITY_INFO),mensajeVentana, mensajeDetalle));
	}
	
	//TODO evento del boton agregar que se encarga de agredar el detalle de punto de emision
	public void EventoBotonDetalle(String Evento){
		
		System.out.println("Evento::"+Evento.toString().trim());
		if(Evento.toString().trim().equals("Agregar"))
		{
			if(seleccionaEmpresa.toString().trim().equals(""))
			{
				mensajeAlerta("Mensaje del sistema","Por favor seleccione la empresa", "peligro");
				return;
			}
			if(seleccionaEstablecimiento.toString().trim().equals(""))
			{
				mensajeAlerta("Mensaje del sistema","Por favor seleccione el establecimiento", "peligro");
				return;
			}
			estiloColumna = "width:6% ; visibility:hidden";
			AgregarRegistro();
			estiloColumna = "width:6%";
			estiloColumnBottonDelete = "visibility:hidden";
		}
		
		if(Evento.toString().trim().equals("Guardar")){
			estiloColumna = "width:6% ; visibility:hidden";
			estiloColumnBottonDelete = "";
			GuardarRegistro();
		}		
		
		if(Evento.toString().trim().equals("Modificar")){
			estiloColumna = "width:6%";
			estiloColumnBottonDelete = "visibility:hidden";
			
		}
		
		if(Evento.toString().trim().equals("Eliminar")){
			estiloColumna = "width:6%; visibility:hidden";
		}
		if(Evento.toString().trim().equals("No_Eliminar")){
			estiloColumna = "width:6%; visibility:hidden";
		}
	}
	
	//TODO contructor que agrega un registro del detalle de punto emision
	private void AgregarRegistro(){
		datapuntoEmision.add( new facDetallePuntoEmision(seleccionaEmpresa, seleccionaEstablecimiento, "000", "Escoja una opcion", "Activo", "Seleccione Tipo Ambiente", 0, "Seleccione Forma de Emision", "Nuevo", "Ingrese código de caja"));
	}
	
	//TODO constructor que se encarga de validar si hay algun cambio en los controles como parametro el nombre del control
	public void ValidarCambioEventosControles(String Controles){
		try{

			datapuntoEmision = new ArrayList<facDetallePuntoEmision>();
			if(Controles.toString().trim().equals("cambioEmpresa")){
				seleccionaEstablecimiento = null;
			}
			if(Controles.toString().trim().equals("cambioEstablecimiento")){
				List<FacPuntoEmision> detallePuntoEmision = new ArrayList<FacPuntoEmision>();
				detallePuntoEmision = puntoEmision.consultaDetallePuntoEmision(seleccionaEmpresa, seleccionaEstablecimiento);
				
				if(!detallePuntoEmision.isEmpty()){
					List<FacTiposDocumento> combodocumento = new ArrayList<FacTiposDocumento>();
					combodocumento = puntoEmision.BuscarDatosTipoDocumentos();
					
					for (FacPuntoEmision facPuntoEmis : detallePuntoEmision) {
						String idTipodocumento = new String();
						for (FacTiposDocumento documento : combodocumento) {
							if(facPuntoEmis.getId().getTipoDocumento().toString().trim().equals(documento.getIdDocumento().toString().trim()))
							{
								idTipodocumento = documento.getDescripcion();
								break;
							}
						}
						
						datapuntoEmision.add(new facDetallePuntoEmision(
								facPuntoEmis.getId().getRuc(),
								facPuntoEmis.getId().getCodEstablecimiento(),
								facPuntoEmis.getId().getCodPuntEmision(), 
								idTipodocumento,
								((facPuntoEmis.getIsActive().toString().trim().equals("Y"))? "Activo" : "Inactivo" ),
								((facPuntoEmis.getTipoAmbiente().toString().trim().equals("D"))? "Desarrollo" : "Produccion" ),
								facPuntoEmis.getSecuencial(),
								((facPuntoEmis.getFormaEmision().toString().trim().equals("1"))? "Normal" : "Contingencia" ),
								"Modificar", facPuntoEmis.getId().getCaja()));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//TODO validar que todo los campos esten correctos
	private Boolean validarCampos(){
		if(seleccionaEmpresa.toString().trim().equals(""))
		{
			mensajeAlerta("Mensaje del sistema","Por favor seleccione la empresa", "peligro");
			return true;
		}
		if(seleccionaEstablecimiento.toString().trim().equals(""))
		{
			mensajeAlerta("Mensaje del sistema","Por favor seleccione el establecimiento", "peligro");
			return true;
		}
		if(datapuntoEmision.size() == 0){
			mensajeAlerta("Mensaje del sistema","No hay registro de punto de emision", "peligro");
			return true;
		}
		for (facDetallePuntoEmision contenido : datapuntoEmision) {
			if(contenido.getCodEstablecimiento() != null)
				if(contenido.getCodEstablecimiento().toString().trim().equals(""))
				{
					mensajeAlerta("Mensaje del sistema","Por seleccione el Cod. de establecimiento", "peligro");
					return true;
				}
			if(contenido.getCodPuntoEmision().toString().trim().equals("0"))
			{
				mensajeAlerta("Mensaje del sistema","Por seleccione el Cod. Punto Emision", "peligro");
				return true;
			}
			if(contenido.getCodTipoDocumento().toString().trim().equals("0"))
			{
				mensajeAlerta("Mensaje del sistema","Por seleccione el Cod. Tipo documento", "peligro");
				return true;
			}
			if(contenido.getTipoAmbiente().toString().trim().equals("0"))
			{
				mensajeAlerta("Mensaje del sistema","Por seleccione el Tipo de Ambiente", "peligro");
				return true;
			}
			if(contenido.getFormaEmision().toString().trim().equals("0"))
			{
				mensajeAlerta("Mensaje del sistema","Por seleccione la Forma de Emision", "peligro");
				return true;
			}
			/*if(contenido.getDmiroCaja().toString().trim().equals("0"))
			{
				mensajeAlerta("Mensaje del sistema","Por seleccione la Caja", "peligro");
				return true;
			}*/
			
			if(ValidarRegistroDetalle(datapuntoEmision, Arrays.asList(contenido.getCodEstablecimiento(),
					contenido.getCodPuntoEmision(),
					contenido.getCodTipoDocumento()))){
				mensajeAlerta("Mensaje del sistema", "Registro cod. Establecimiento:" + contenido.getCodEstablecimiento() + "\n Punto Emision: " + contenido.getCodPuntoEmision() +
							"\n Tipo Documento: " + contenido.getCodTipoDocumento(), "peligro");  
				mensajeAlerta("Mensaje del sistema", "Por favor cambie el registro o no se procedera a guardar", "peligro");  
				return true;
			}
		}
		return false;
	}
	
	// TODO contructor en la cual guarda el registro en la base
	private void GuardarRegistro(){
		try{
			FacBitacoraAuditoria audit = new FacBitacoraAuditoria();
		// ingresando contenido
		if(validarCampos())
			return;
		
		ReemplazarDatos(datapuntoEmision);
		
		for (facDetallePuntoEmision contenido : datapuntoEmision) {			
			// ingresando datos de puntoEmisionPK
			FacPuntoEmisionPK ingresandoPuntoEmisionPK = new FacPuntoEmisionPK();
			ingresandoPuntoEmisionPK.setRuc(contenido.getRuc());
			ingresandoPuntoEmisionPK.setCodEstablecimiento(contenido.getCodEstablecimiento());
			ingresandoPuntoEmisionPK.setTipoDocumento(contenido.getCodTipoDocumento());
			ingresandoPuntoEmisionPK.setCodPuntEmision(contenido.getCodPuntoEmision());
			ingresandoPuntoEmisionPK.setCaja(contenido.getCaja());
			if(contenido.getAccion().toString().trim().equals("Nuevo"))
			{
				ingresandoPuntoEmisionPK.setRuc(contenido.getRuc());
				ingresandoPuntoEmisionPK.setCodEstablecimiento(contenido.getCodEstablecimiento());
				ingresandoPuntoEmisionPK.setCodPuntEmision(contenido.getCodPuntoEmision());
				ingresandoPuntoEmisionPK.setTipoDocumento(contenido.getCodTipoDocumento());
				ingresandoPuntoEmisionPK.setCaja(contenido.getCaja());
			}
				//ingresandoPuntoEmisionPK.setSecuencial(puntoEmision.SecuencialPuntoEmision());
			//else if(contenido.getAccion().toString().trim().equals("Modificar"))
				//ingresandoPuntoEmisionPK.setSecuencial(contenido.getSecuencial());
			
			// ingresando datos de puntoEmision
			FacPuntoEmision ingresandoPuntoEmision = new FacPuntoEmision();
			ingresandoPuntoEmision.setId(ingresandoPuntoEmisionPK);
			ingresandoPuntoEmision.setTipoAmbiente(contenido.getTipoAmbiente());
			ingresandoPuntoEmision.setFormaEmision(contenido.getFormaEmision());
			
			ingresandoPuntoEmision.setIsActive(contenido.getIsActive());
			
			if(contenido.getAccion().toString().trim().equals("Nuevo")){
				ingresandoPuntoEmision.setUserCreacion(user);
			    ingresandoPuntoEmision.setFechaCreacion(new java.util.Date());
				puntoEmision.ingresarDatos(ingresandoPuntoEmision);
				try{
					audit.setIdBitacoraAuditoria(facAudit.getSecuencialBitacoraAuditoria());
					audit.setValor(puntoEmision.toString());
					audit.setTabla("fac_punto_emision");
					audit.setFecha(new Date());
					audit.setUsuario(user);
					audit.setAccion("INSERTAR");
					facAudit.IngresarBitacoraAuditoria(audit);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(contenido.getAccion().toString().trim().equals("Modificar")){
				ingresandoPuntoEmision.setUserModificacion(userModificacion);
				SimpleDateFormat dt1 = new SimpleDateFormat("yyyyy-mm-dd");
				ingresandoPuntoEmision.setFechaModificacion(new java.util.Date());
				puntoEmision.modificarDatos(ingresandoPuntoEmision);
				try{
					audit.setIdBitacoraAuditoria(facAudit.getSecuencialBitacoraAuditoria());
					audit.setValor(puntoEmision.toString());
					audit.setTabla("fac_punto_emision");
					audit.setFecha(new Date());
					audit.setUsuario(user);
					audit.setAccion("MODIFICAR");
					facAudit.IngresarBitacoraAuditoria(audit);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
		}

		mensajeAlerta("Mensaje del sistema", "Registro Guardado/Modificado con exito", "informacion");
		cargarDatos();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//TODO contructor que se encarga de reemplazar los datos de los combox
	private void ReemplazarDatos(List<facDetallePuntoEmision> detallepuntoemision){
		try{
			List<FacTiposDocumento> combodocumento = new ArrayList<FacTiposDocumento>();

			combodocumento = puntoEmision.BuscarDatosTipoDocumentos();
			
			for (facDetallePuntoEmision contenido : detallepuntoemision) {
				contenido.setIsActive(((contenido.getIsActive().toString().trim().equals("Activo"))? "Y" : "0" ));
				contenido.setTipoAmbiente(((contenido.getTipoAmbiente().toString().trim().equals("Desarrollo"))? "D" : "P" ));
				contenido.setFormaEmision(((contenido.getFormaEmision().toString().trim().equals("Normal"))? "1" : "2" ));
				for (FacTiposDocumento documento : combodocumento) {
					if(documento.getDescripcion().equalsIgnoreCase(contenido.getCodTipoDocumento())){
						contenido.setCodTipoDocumento(documento.getIdDocumento());
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void EliminarRegistros(){
		try{
			FacBitacoraAuditoria audit = new FacBitacoraAuditoria();
			SeleccionarlistaPuntoEmision.setIsActive(((SeleccionarlistaPuntoEmision.getIsActive().toString().trim().equals("Activo"))? "Y" : "0" ));
			SeleccionarlistaPuntoEmision.setTipoAmbiente(((SeleccionarlistaPuntoEmision.getTipoAmbiente().toString().trim().equals("Activo"))? "Y" : "0" ));
			SeleccionarlistaPuntoEmision.setFormaEmision(((SeleccionarlistaPuntoEmision.getFormaEmision().toString().trim().equals("Normal"))? "1" : "2" ));
			List<FacTiposDocumento> combodocumento = new ArrayList<FacTiposDocumento>();
			combodocumento = puntoEmision.BuscarDatosTipoDocumentos();
			for (FacTiposDocumento documento : combodocumento) {
				if(documento.getDescripcion().equalsIgnoreCase(SeleccionarlistaPuntoEmision.getCodTipoDocumento())){
					SeleccionarlistaPuntoEmision.setCodTipoDocumento(documento.getIdDocumento());
					break;
				}
			}
			puntoEmision.eliminardatos(SeleccionarlistaPuntoEmision);
			try{
				audit.setIdBitacoraAuditoria(facAudit.getSecuencialBitacoraAuditoria());
				audit.setValor(SeleccionarlistaPuntoEmision.toString());
				audit.setTabla("fac_empresa");
				audit.setFecha(new Date());
				audit.setUsuario(user);
				audit.setAccion("ELIMINAR");
				facAudit.IngresarBitacoraAuditoria(audit);
			}catch(Exception e){
				e.printStackTrace();
			}
			mensajeAlerta("Mensaje del sistema", "Registro Eliminado", "informacion");
			ValidarCambioEventosControles("cambioEstablecimiento");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getSeleccionaEmpresa() {
		return seleccionaEmpresa;
	}
	public void setSeleccionaEmpresa(String seleccionaEmpresa) {
		this.seleccionaEmpresa = seleccionaEmpresa;
	}

	public List<SelectItem> getListaEmpresa() {
		return listaEmpresa;
	}
	public void setListaEmpresa(List<SelectItem> listaEmpresa) {
		this.listaEmpresa = listaEmpresa;
	}

	public String getSeleccionaEstablecimiento() {
		return seleccionaEstablecimiento;
	}
	public void setSeleccionaEstablecimiento(String seleccionaEstablecimiento) {
		this.seleccionaEstablecimiento = seleccionaEstablecimiento;
	}

	public List<SelectItem> getListaEstablecimiento() {
		return listaEstablecimiento;
	}
	public void setListaEstablecimiento(List<SelectItem> listaEstablecimiento) {
		this.listaEstablecimiento = listaEstablecimiento;
	}

	public List<facDetallePuntoEmision> getDatapuntoEmision() {
		return datapuntoEmision;
	}
	public void setDatapuntoEmision(List<facDetallePuntoEmision> datapuntoEmision) {
		this.datapuntoEmision = datapuntoEmision;
	}

	public List<SelectItem> getListaTipodocumento() {
		return listaTipodocumento;
	}
	public void setListaTipodocumento(List<SelectItem> listaTipodocumento) {
		this.listaTipodocumento = listaTipodocumento;
	}

	public List<SelectItem> getListaAmbiente() {
		return listaAmbiente;
	}
	public void setListaAmbiente(List<SelectItem> listaAmbiente) {
		this.listaAmbiente = listaAmbiente;
	}
	
	public List<FacEstablecimiento> getTablaEstablecimiento() {
		return tablaEstablecimiento;
	}
	public void setTablaEstablecimiento(
			List<FacEstablecimiento> tablaEstablecimiento) {
		this.tablaEstablecimiento = tablaEstablecimiento;
	}
	
	public List<FacEstablecimiento> getFiltrotablaEstablecimiento() {
		return filtrotablaEstablecimiento;
	}
	public void setFiltrotablaEstablecimiento(
			List<FacEstablecimiento> filtrotablaEstablecimiento) {
		this.filtrotablaEstablecimiento = filtrotablaEstablecimiento;
	}

	public FacEstablecimiento getVerCamposEstablecimiento() {
		return verCamposEstablecimiento;
	}
	public void setVerCamposEstablecimiento(
			FacEstablecimiento verCamposEstablecimiento) {
		this.verCamposEstablecimiento = verCamposEstablecimiento;
	}

	public List<SelectItem> getListaEstado() {
		return listaEstado;
	}
	public void setListaEstado(List<SelectItem> listaEstado) {
		this.listaEstado = listaEstado;
	}

	public String getEstiloColumna() {
		return estiloColumna;
	}
	public void setEstiloColumna(String estiloColumna) {
		this.estiloColumna = estiloColumna;
	}

	public facDetallePuntoEmision getSeleccionarlistaPuntoEmision() {
		return SeleccionarlistaPuntoEmision;
	}

	public void setSeleccionarlistaPuntoEmision(
			facDetallePuntoEmision seleccionarlistaPuntoEmision) {
		SeleccionarlistaPuntoEmision = seleccionarlistaPuntoEmision;
	}

	public String getEstiloColumnBottonDelete() {
		return estiloColumnBottonDelete;
	}

	public void setEstiloColumnBottonDelete(String estiloColumnBottonDelete) {
		this.estiloColumnBottonDelete = estiloColumnBottonDelete;
	}
	
	public List<SelectItem> getListaFormaEmision() {
		return listaFormaEmision;
	}
	public void setListaFormaEmision(List<SelectItem> listaFormaEmision) {
		this.listaFormaEmision = listaFormaEmision;
	}

	public String getCaja() {
		return caja;
	}
	public void setCaja(String caja) {
		this.caja = caja;
	}

	public String getUserCreacion() {
		return userCreacion;
	}

	public void setUserCreacion(String userCreacion) {
		this.userCreacion = userCreacion;
	}

	public String getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getUserModificacion() {
		return userModificacion;
	}

	public void setUserModificacion(String userModificacion) {
		this.userModificacion = userModificacion;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
}
