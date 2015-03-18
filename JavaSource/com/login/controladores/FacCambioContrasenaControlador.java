package com.login.controladores;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.documentos.entidades.FacEmpresa;
import com.general.servicios.FacDocumentoServicios;
import com.usuario.entidades.FacLoginBitacora;
import com.usuario.entidades.FacUsuario;
import com.usuario.entidades.FacUsuarioPK;
import com.usuario.servicios.FacUsuarioServicio;

@ViewScoped
@ManagedBean
public class FacCambioContrasenaControlador
{
	private String usuario;
	private String actualContrasena;
	private String nuevaContrasena;
	private String confirmarContrasena;
	private FacesContext context;
	private HttpSession sesion;
	
	private String seleccionEmpresa;
	private ArrayList<SelectItem> listaEmpresas;
	@EJB
	private FacUsuarioServicio usuarioServicio;
	
	
	@EJB
	private FacDocumentoServicios facDocumentoServicios;
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getActualContrasena() {
		return actualContrasena;
	}
	public void setActualContrasena(String actualContrasena) {
		this.actualContrasena = actualContrasena;
	}
	
	public String getSeleccionEmpresa() {
		return seleccionEmpresa;
	}
	public void setSeleccionEmpresa(String seleccionEmpresa) {
		this.seleccionEmpresa = seleccionEmpresa;
	}
	
	public String getNuevaContrasena() {
		return nuevaContrasena;
	}
	public void setNuevaContrasena(String nuevaContrasena) {
		this.nuevaContrasena = nuevaContrasena;
	}
	
	public String getConfirmarContrasena() {
		return confirmarContrasena;
	}
	public void setConfirmarContrasena(String confirmarContrasena) {
		this.confirmarContrasena = confirmarContrasena;
	}
	
	public ArrayList<SelectItem> getListaEmpresas() {
		return listaEmpresas;
	}
	public void setListaEmpresas(ArrayList<SelectItem> listaEmpresas) {
		this.listaEmpresas = listaEmpresas;
	}
	
	public void cargaVariablesSesion()
	{
		context = FacesContext.getCurrentInstance();
		sesion = (HttpSession)context.getExternalContext().getSession(true);
		this.setUsuario(sesion.getAttribute("id_usuario").toString());
		this.setSeleccionEmpresa(sesion.getAttribute("Ruc_Empresa").toString());
		System.out.println("--Me pinto la sesion--");
		System.out.println(sesion.getAttribute("Ruc_Empresa").toString());
		System.out.println(this.getUsuario());
		System.out.println(this.getSeleccionEmpresa());
	}
	
	public String cambiarContrasena()
	{
		System.out.println("Inicio cambio de contraseña");
		FacUsuario usuario = new FacUsuario();
		FacUsuarioPK usuarioPk = new FacUsuarioPK();
		String mensaje = "";
		HttpSession sesion;
		
		System.out.println("Comparacion contraseñas...");
		if(!this.getNuevaContrasena().equals(this.getConfirmarContrasena()))
		{
			mensaje = "La contraseña no coincide, Confirme la contraseña";
			//ResultadoUsuario= false;
			mensajeSistema (mensaje,FacesMessage.SEVERITY_ERROR);
			return mensaje;
		}
		System.out.println("Creo y obtengo sesion...");
		FacesContext context = FacesContext.getCurrentInstance();
		sesion = (HttpSession) context.getExternalContext().getSession(true);
		
		
		System.out.println("Seleccion empresa..."+sesion.getAttribute("Ruc_Empresa").toString());
		System.out.println("Seleccion usuario..."+this.getUsuario());
		
		//usuarioPk.setRuc(this.getSeleccionEmpresa());
		usuarioPk.setRuc(sesion.getAttribute("Ruc_Empresa").toString());
		System.out.println("1...");
		usuarioPk.setCodUsuario(this.getUsuario());
		System.out.println("2...");
		usuario.setId(usuarioPk);
		System.out.println("3...");
		usuario.setPassword(this.getActualContrasena());
		
		//usuarioServicio = new FacUsuarioServicio();
		System.out.println("Valido si usuario actual...");
		System.out.println(usuario.getId().getRuc());
		System.out.println(usuario.getId().getCodUsuario());
		
		//System.out.println("Resultado... " +usuarioServicio.validarUsuarioMensaje(usuario));
		usuarioServicio.cambiarContrasena(usuario, this.getConfirmarContrasena());
		
		try {
			usuarioServicio.modificarLoginError(usuario, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			;;
		}
		return "Login.jsf";
		
		
	}
	
	public void mensajeSistema (String Mensaje , Severity tipo){
		FacesMessage mensaje= new FacesMessage(tipo,Mensaje, null);
		FacesContext.getCurrentInstance().addMessage("frm1", mensaje);	
	}
	
	public void cargarListas()
	{
		cargaVariablesSesion();
		FacEmpresa facEmpresas = new FacEmpresa();
		listaEmpresas = new ArrayList<SelectItem>();
		List<FacEmpresa> EmpresaGeneral;
		
		EmpresaGeneral = fac_empresas();
		
		if(EmpresaGeneral.isEmpty()){
			facEmpresas.setRuc("0");
			facEmpresas.setRazonSocial("No hay datos de la empresa");
			EmpresaGeneral.add(facEmpresas);
		}else
			listaEmpresas.add(new SelectItem("0", "Seleccione la empresa"));

		for(int i = 0;i<EmpresaGeneral.size();i++)
			listaEmpresas.add(new SelectItem(EmpresaGeneral.get(i).getRuc(), EmpresaGeneral.get(i).getRazonSocial()));
	}
	
	
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

}
