package com.login.controladores;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.documentos.entidades.FacEmpresa;
import com.general.controladores.FacEncriptarcadenasControlador;
import com.general.entidades.FacGeneral;
import com.general.servicios.FacDocumentoServicios;
import com.general.servicios.FacGeneralServicio;
import com.usuario.entidades.FacLoginBitacora;
import com.usuario.entidades.FacUsuario;
import com.usuario.entidades.FacUsuarioPK;
import com.usuario.servicios.FacUsuarioServicio;

@ViewScoped
@ManagedBean
public class FacLoginControlador {
	@EJB
	private FacDocumentoServicios facDocumentoServicios;
	
	@EJB
	private FacUsuarioServicio usuarioServicio;
	
	@EJB
	private FacGeneralServicio facGenSer;
	
	private FacGeneral facGen;
		
	private FacEmpresa facEmpresas;
	private FacUsuario usuario; // variable que retiene la entidad del usuario
	private FacUsuarioPK Id;
	

	private ArrayList<SelectItem> Empresa; // variable que recoge las empresas
	private List<FacEmpresa> EmpresaGeneral;
	private String SeleccionaEmpresa;// variable que contiene el dato de la empresa
	
	private String loginUsuario;
	private String ContraseñaUsuario;
	

	//TODO contructor que carga todas las empresas
	public void CargarDatos(){
		
		CargarCombo();
		//Ruc parametrizado Default  idGeneral -> 99
		facGen = facGenSer.buscarDatosGeneralPrimerHijo("99");
		if (facGen !=null)
		SeleccionaEmpresa=facGen.getDescripcion();
		else
		SeleccionaEmpresa="0992701374001";	
	}
	
	//TODO contructor que carga los combox de la pantalla
	private void CargarCombo(){
		try{
			Empresa= new ArrayList<SelectItem>();
			EmpresaGeneral = fac_empresas();
			if(EmpresaGeneral.isEmpty()){
				facEmpresas.setRuc("0");
				facEmpresas.setRazonSocial("No hay datos de la empresa");
				EmpresaGeneral.add(facEmpresas);
			}else
				Empresa.add(new SelectItem("0", "Seleccione la empresa"));
	
			for(int i = 0;i<EmpresaGeneral.size();i++)
				Empresa.add(new SelectItem(EmpresaGeneral.get(i).getRuc(), EmpresaGeneral.get(i).getRazonSocial()));
			
		}catch (Exception e) {
			FacesMessage mensaje=null;
			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
			e.printStackTrace();
		}
	}
	
	//TODO contructor de lista de las empresas
	public List<FacEmpresa> fac_empresas() {
		List<FacEmpresa> detalledocumento = new ArrayList<FacEmpresa>();
				
		try{			
			detalledocumento = facDocumentoServicios.listadoTodasEmpresas();    
			
		}catch (Exception e) {
			FacesMessage mensaje=null;
			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
			e.printStackTrace();
		}
		return  detalledocumento;
	}
	
	public String Ingresar(){
		return ingresar_usuario();
	}
	
	public String verificaBitacora(String ruc, String usuario)
	{
		String usua = loginUsuario;
		FacUsuario usuarioLogin = new FacUsuario();
		Id = new FacUsuarioPK();
		Id.setCodUsuario(usua);
		Id.setRuc(SeleccionaEmpresa);
		usuarioLogin.setId(Id);		
		int liLoginFail = usuarioServicio.validarLoginErroneos(usuarioLogin);
		List<FacLoginBitacora>  listaBitacoraLogin = this.usuarioServicio.listaLoginBitacora(ruc, usuario);
		if(listaBitacoraLogin != null){
			System.out.println("Tamaño... " + listaBitacoraLogin.size());
			if (liLoginFail == -100)
				return "cambioContrasena.jsf";
			if(listaBitacoraLogin.size()<=0)
				return "cambioContrasena.jsf";			
		}
		return "";
	}
	
	private FacEmpresa consultaEmpresa(){
		try{
			for(FacEmpresa fa : EmpresaGeneral){
				if(fa.getRuc().trim().equals(SeleccionaEmpresa.trim())){
					return fa;
				}
			}
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public void mensajeSistema (String Mensaje , Severity tipo){
		FacesMessage mensaje= new FacesMessage(tipo,Mensaje, null);
		FacesContext.getCurrentInstance().addMessage("frm1", mensaje);	
	}
	public static void main(String[] args) {
		//String contra = FacEncriptarcadenasControlador.decrypt("3lMyqjSeeOW96RGP/XGv4g==");
		String contra = FacEncriptarcadenasControlador.decrypt("fkCpNqJqrXrb8cqYbIf1pw==");
		System.out.println("Clave::"+contra);
	}
	
	//TODO metodo que es llamado del boton ingresar para verificar si el usuario existe
	protected String ingresar_usuario(){
		String ReturnPagina = "";
		Boolean ResultadoUsuario = true;
		String Mensaje = "";
		int liLoginFail = 0, liLoginFailVig = 0;
		try{
			if(SeleccionaEmpresa.equals("0") && ResultadoUsuario){
				
				Mensaje = "Debe seleccionar la empresa";
				ResultadoUsuario= false;
				mensajeSistema (Mensaje,FacesMessage.SEVERITY_ERROR);
				return Mensaje;
			}
			if(loginUsuario.equals("") && (ResultadoUsuario == true)){
				Mensaje = "Debe ingresar el usuario";
				ResultadoUsuario= false;
				mensajeSistema (Mensaje,FacesMessage.SEVERITY_ERROR);
				return Mensaje;
			}
			/*
			if(loginUsuario.length()<10){
				Mensaje = "Debe ingresar el usuario valido";
				ResultadoUsuario= false;
				mensajeSistema (Mensaje,FacesMessage.SEVERITY_ERROR);
				return Mensaje;
			}
			*/
			if(ContraseñaUsuario.equals("") && (ResultadoUsuario == true)){
				Mensaje = "Debe ingresar la contraseña";
				ResultadoUsuario= false;
				mensajeSistema (Mensaje,FacesMessage.SEVERITY_ERROR);
				return Mensaje;
			}
			
			String usua = loginUsuario;
			String contra = FacEncriptarcadenasControlador.encrypt(ContraseñaUsuario);

			FacUsuario datosUsuario = null;
			if(ResultadoUsuario){
				usuario = new FacUsuario();
				Id = new FacUsuarioPK();
				Id.setCodUsuario(usua);
				Id.setRuc(SeleccionaEmpresa);
				usuario.setId(Id);
				usuario.setPassword(contra);
				liLoginFail = usuarioServicio.validarLoginErroneos(usuario);
				if (liLoginFail >= 3){
					Mensaje = "Usuario excedio limite de intentos. Solicitar a Sistemas que le reinicie la clave";
					FacesMessage mensaje= new FacesMessage(FacesMessage.SEVERITY_ERROR,Mensaje, null);
					FacesContext.getCurrentInstance().addMessage("frm1", mensaje);
					ReturnPagina = "";
					return Mensaje;
				}
				liLoginFailVig = usuarioServicio.validarVigenciaClave(usuario);
				if (liLoginFailVig >= 90){
					Mensaje = "Usuario tiene la clave caducada. Solicitar a Sistemas que le reinicie la clave";
					FacesMessage mensaje= new FacesMessage(FacesMessage.SEVERITY_ERROR,Mensaje, null);
					FacesContext.getCurrentInstance().addMessage("frm1", mensaje);
					ReturnPagina = "";
					return Mensaje;
				}
				Mensaje=usuarioServicio.validarUsuarioMensaje(usuario);
				if (!Mensaje.equals("ok")){
					liLoginFail ++;
					usuarioServicio.modificarLoginError(usuario, liLoginFail);
					mensajeSistema (Mensaje,FacesMessage.SEVERITY_ERROR);
					return Mensaje;
				}				
				datosUsuario = usuarioServicio.validarUsuario(usuario);
				Mensaje = "Usuario y contraseña incorrectos";
			}
				
			if(datosUsuario != null)
			{
				
				HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
				if (session != null)  
		        {
		            session.setAttribute("id_usuario", datosUsuario.getId().getCodUsuario());
		            session.setAttribute("Ruc_Empresa", datosUsuario.getId().getRuc());
		            session.setAttribute("NombreUsua", datosUsuario.getNombre());
		            FacEmpresa datoEmpresa = consultaEmpresa();
		            if(datoEmpresa!= null)
		            	session.setAttribute("NombreEmpresa", datoEmpresa.getRazonSocial());
		            
		          //HFU
		            String verificaBitacora = verificaBitacora(datosUsuario.getId().getRuc(), datosUsuario.getId().getCodUsuario()); 
		            FacLoginBitacora facLoginBitacora = new FacLoginBitacora();
		            System.out.println("SecuencialLoginBitacora::"+usuarioServicio.geSecuencialLoginBitacora());
					facLoginBitacora.setId(usuarioServicio.geSecuencialLoginBitacora());
					facLoginBitacora.setRuc(datosUsuario.getId().getRuc());
					facLoginBitacora.setUsuario(datosUsuario.getId().getCodUsuario());
					facLoginBitacora.setFechaInicioSesion(new Date());
					String remoteAddr = "";
						remoteAddr = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr();
					
					facLoginBitacora.setIpCliente(remoteAddr);
					usuarioServicio.ingresaLoginBitacora(facLoginBitacora);
					
		            if(!verificaBitacora.equals(""))
						return verificaBitacora;
					//HFU
		        }
				ReturnPagina = "/paginas/Blanco.jsf";
			}else
			{
				liLoginFail ++;
				usuarioServicio.modificarLoginError(usuario, liLoginFail);
				FacesMessage mensaje= new FacesMessage(FacesMessage.SEVERITY_ERROR,Mensaje, null);
				FacesContext.getCurrentInstance().addMessage("frm1", mensaje);
				ReturnPagina = "";
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ReturnPagina;
	}
		
	public String getSeleccionaEmpresa() {
		return SeleccionaEmpresa;
	}
	public void setSeleccionaEmpresa(String seleccionaEmpresa) {
		SeleccionaEmpresa = seleccionaEmpresa;
	}
	
	public ArrayList<SelectItem> getEmpresa() {
		return Empresa;
	}
	public void setEmpresa(ArrayList<SelectItem> empresa) {
		Empresa = empresa;
	}

	public List<FacEmpresa> getEmpresaGeneral() {
		return EmpresaGeneral;
	}
	public void setEmpresaGeneral(List<FacEmpresa> empresaGeneral) {
		EmpresaGeneral = empresaGeneral;
	}
	
	public String getLoginUsuario() {
		return loginUsuario;
	}
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	public String getContraseñaUsuario() {
		return ContraseñaUsuario;
	}
	public void setContraseñaUsuario(String contraseñaUsuario) {
		ContraseñaUsuario = contraseñaUsuario;
	}
		
	public FacUsuario getUsuario() {
		return usuario;
	}
	public void setUsuario(FacUsuario usuario) {
		this.usuario = usuario;
	}

	public FacUsuarioPK getId() {
		return Id;
	}
	public void setId(FacUsuarioPK id) {
		Id = id;
	}
}
