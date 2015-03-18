/**
 * 
 */
package com.usuario.controladores;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.documentos.entidades.FacEmpresa;
import com.documentos.servicios.FacturaServicio;
import com.general.controladores.FacEncriptarcadenasControlador;
import com.general.entidades.FacBitacoraAuditoria;
import com.general.entidades.FacCliente;
import com.general.entidades.FacGeneral;
import com.general.servicios.FacBitacoraAuditoriaServicio;
import com.usuario.entidades.FacRole;
import com.usuario.entidades.FacUsuario;
import com.usuario.entidades.FacUsuarioPK;
import com.usuario.entidades.FacUsuariosRole;
import com.usuario.entidades.FacUsuariosRolePK;
import com.usuario.servicios.FacUsuarioServicio;


@ViewScoped
@ManagedBean
public class FacUsuarioControlador {

	@EJB
	private FacUsuarioServicio facUsuarioServicio;

	@EJB
	private FacturaServicio facEmpresaServicio;
	
	@EJB
	private FacBitacoraAuditoriaServicio facAudit;
	
	private String rucEmpresa;
	private String rucUsuario;
	private String tipoUsuario;
	private String razonsocial;
	private String nombre;
	private String codUsuario;
	private String estado;
	private String password;
	private String auxPassword;
	private boolean grisado;
	private boolean grisadoCodUsuario = true;
	private String dialogoBusqueda = "diaUsu.hide()";
	
	private boolean grisadoModificar;
	private boolean modificar;
	private boolean requerido;
	private String rol;
	private String actualizar;
	private String fechaCreacion;
	private String userCreacion;
	private String fechaModificacion;
	private String userModificacion;
	private String user;


		//ENTIDAD FACEMPRESA
		private List<FacEmpresa> listaEmpresa;
	//metodo cliente
		private FacCliente clientes;
		private FacCliente selecionarClientes;
		private List<FacCliente> listaCliente;
		private List<FacCliente> filtarCliente;
	//metodo Empresa
		private FacEmpresa empresa;
	//metodos Generales
		private FacGeneral general;
		private List<FacGeneral> listGeneral;
	//metodo Usuario
		private List<FacUsuario> listaUsario;
		private List<FacUsuario> listFiltraUsario;
		private FacUsuario usuario;
		private  FacUsuarioPK id;
		private  FacUsuario seleccionarCampo;
		private FacUsuario CamposModificar;
	//metodo SelectItem
		private List<SelectItem> li_tipo,li_estado,li_rol,li_ruc;
	//metodo Role
		private List<FacRole> role;
	//metodo Usuario roles
		private FacUsuariosRole usurioRol;
		private FacUsuariosRolePK idRol;
		

		private void mensajeAlerta(String mensajeVentana, String mensajeDetalle, String tipo) {
			 FacesContext context = FacesContext.getCurrentInstance();            
		      context.addMessage(null, new FacesMessage((tipo.toString().trim().equals("alerta") ? FacesMessage.SEVERITY_ERROR : tipo.toString().trim().equals("peligro") ? FacesMessage.SEVERITY_WARN : FacesMessage.SEVERITY_INFO),mensajeVentana, mensajeDetalle));
	    }
		
//metodos  de aplicacion
	public void CargarDatos(){
			try{
				FacesContext context = FacesContext.getCurrentInstance();
				HttpSession sesion = (HttpSession)context.getExternalContext().getSession(false);
				
				this.setGrisado(false);
				this.setGrisadoModificar(false);
				
				this.setCodUsuario(null);
				this.setRucEmpresa(null);
				this.setRazonsocial(null);
				this.setPassword(null);
				this.setModificar(false);
				rucUsuario = "0";
				tipoUsuario= "0";
				rol = "0";
				this.llenaComboTipoUsuario();
				this.llenaComboEstado();
				//this.llenaTablausuario();
				this.LlenarRol();
				
				this.llenarRucEmpresa();
				//Desactivar en inputText de ruc Cliente o Provedor
				 

				this.setGrisado(true);
				requerido = true;
				user = sesion.getAttribute("id_usuario").toString().trim();
				 
			}catch (Exception e) {
				e.printStackTrace();
				FacesMessage mensaje=null;
				mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),null);
				FacesContext.getCurrentInstance().addMessage(null, mensaje);
			}
		}

		
	public void inserto(String NombreEvento){
		try{						

			FacBitacoraAuditoria audit = new FacBitacoraAuditoria();
			
			usuario = new FacUsuario();
			codUsuario = rucEmpresa.trim();
			//////// asigno PK 	q son 2 (ruc y cod-usuario)
			if(modificar!=true)
			{
				if(!(tipoUsuario.trim().equals("0"))&&!(rol.trim().equals("0"))&&codUsuario!=null&&rucUsuario!=null&&password!=null&&razonsocial!=null){
					id = new FacUsuarioPK();
			
					id.setCodUsuario(codUsuario);
					id.setRuc(rucUsuario);
				
					usuario.setId(id);
					///////llenamos los otros campos y asignamos en el objeto
					usuario.setRucEmpresa(rucEmpresa);
					usuario.setNombre(selecionarClientes.getRazonSocial());
					usuario.setPassword(FacEncriptarcadenasControlador.encrypt(password));
					usuario.setTipoUsuario(tipoUsuario);
					usuario.setIsActive(estado);
					usuario.setLoginErroneo(0);
					usuario.setUserCreacion(user);
					usuario.setFechaCreacion(new java.util.Date());
					facUsuarioServicio.insertarUsuario(usuario);
					facUsuarioServicio.modificarLoginError(usuario, 0);
					try{
						audit.setIdBitacoraAuditoria(facAudit.getSecuencialBitacoraAuditoria());
						audit.setValor(usuario.toString());
						audit.setTabla("fac_usuario");
						audit.setFecha(new Date());
						audit.setUsuario(user);
						audit.setAccion("INSERTAR");
						facAudit.IngresarBitacoraAuditoria(audit);
					}catch(Exception e){
						e.printStackTrace();
					}

					///SE EJECUTA EL METODO

					usurioRol = new FacUsuariosRole();
					idRol = new FacUsuariosRolePK();
					
					idRol.setRuc(rucUsuario);
					idRol.setCodRol(rol);
					idRol.setCodUsuario(codUsuario);
					usurioRol.setId(idRol);
					
					usurioRol.setIsActive(estado);
					facUsuarioServicio.insertarUsuarioRol(usurioRol);
					try{
						audit.setIdBitacoraAuditoria(facAudit.getSecuencialBitacoraAuditoria());
						audit.setValor(usurioRol.toString());
						audit.setTabla("fac_usuarios_roles");
						audit.setFecha(new Date());
						audit.setUsuario(user);
						audit.setAccion("INSERTAR");
						facAudit.IngresarBitacoraAuditoria(audit);
					}catch(Exception e){
						e.printStackTrace();
					}
					
					mensajeAlerta("Mensaje del sistema", "Registro guardado con exito", "Informacion");
					this.setActualizar(":frm1:datosUsuario");
					this.CargarDatos();
				}else if((rucEmpresa==null&&password==null&&razonsocial==null)||rucUsuario==null){
					mensajeAlerta("Mensaje del sistema", "Campos Obligatorios estan vacios", "alerta");
				}else{
					mensajeAlerta("Mensaje del sistema", "Error al guardar el registro", "alerta");
				}
			}else{
				if(!(rol.trim().equals("0"))||password==null){
					usurioRol = new FacUsuariosRole();
					idRol = new FacUsuariosRolePK();
					
					idRol.setRuc(rucUsuario);
					idRol.setCodRol(rol);
					idRol.setCodUsuario(codUsuario);
					usurioRol.setId(idRol);
					usurioRol.setIsActive(estado);
					facUsuarioServicio.modificarDatosRol(usurioRol);
					try{
						audit.setIdBitacoraAuditoria(facAudit.getSecuencialBitacoraAuditoria());
						audit.setValor(usurioRol.toString());
						audit.setTabla("fac_usuarios_roles");
						audit.setFecha(new Date());
						audit.setUsuario(user);
						audit.setAccion("MODIFICAR");
						facAudit.IngresarBitacoraAuditoria(audit);
					}catch(Exception e){
						e.printStackTrace();
					}

					id = new FacUsuarioPK();
					
					id.setCodUsuario(codUsuario);
					id.setRuc(rucUsuario);
					usuario = new FacUsuario();
					usuario.setId(id);
					///////llenamos los otros campos y asignamos en el objeto
					usuario.setRucEmpresa(rucEmpresa);
					usuario.setNombre(razonsocial);
					usuario.setLoginErroneo(-100);
					usuario.setTipoUsuario(tipoUsuario);
					usuario.setIsActive(estado);
					if(this.getPassword().trim().equals("")){
						usuario.setPassword(this.getAuxPassword());
					}else{
						usuario.setPassword(FacEncriptarcadenasControlador.encrypt(password));
					}
					//empresa.setUserCreacion(facemp.getUserCreacion());
					//empresa.setFechaCreacion(facemp.getFechaCreacion());					
					userModificacion = user;
					usuario.setUserModificacion(userModificacion);
					SimpleDateFormat dt1 = new SimpleDateFormat("yyyyy-mm-dd");
					usuario.setFechaModificacion(new java.util.Date());
					
					facUsuarioServicio.modificarDatos(usuario);
					try{
						audit.setIdBitacoraAuditoria(facAudit.getSecuencialBitacoraAuditoria());
						audit.setValor(usuario.toString());
						audit.setTabla("fac_usuario");
						audit.setFecha(new Date());
						audit.setUsuario(user);
						audit.setAccion("MODIFICAR");
						facAudit.IngresarBitacoraAuditoria(audit);
					}catch(Exception e){
						e.printStackTrace();
					}

					facUsuarioServicio.modificarLoginError(usuario, -100);

					this.CargarDatos();
					mensajeAlerta("Mensaje del sistema", "Registro modificado con exito", "informacion");
				}else{
					if (rol.trim().equals("0")) {
						mensajeAlerta("Mensaje del sistema", "Campos Obligatorios estan vacios", "alerta");
					}
					if (password==null) {
						mensajeAlerta("Mensaje del sistema", "Password:: Ingrese dato", "peligro");
					}
					
					
					grisadoModificar = true;
					
					this.setModificar(true);
					
				
				}
			}
		}catch (Exception e) {
			mensajeAlerta("Mensaje del sistema", "Error al guardar", "alerta");
		}
	}
	
	
	public void modificar(){
		FacUsuariosRole usuRoles = new FacUsuariosRole();
		try {
			if(CamposModificar.getTipoUsuario().trim().equals("Cliente")){
				this.setTipoUsuario("C");
			}else if(CamposModificar.getTipoUsuario().trim().equals("Empleado")){
				this.setTipoUsuario("E");
			}else{
				this.setTipoUsuario("P");
			}
			
			if(this.getTipoUsuario().trim().equals("C")){
			li_tipo.add(new SelectItem(this.getTipoUsuario(),"CLIENTE"));
			}if(this.getTipoUsuario().trim().equals("E")){
			li_tipo.add(new SelectItem(this.getTipoUsuario(),"EMPLEADO"));
			}if(this.getTipoUsuario().trim().equals("P")){
			li_tipo.add(new SelectItem(this.getTipoUsuario(),"PROVEEDOR"));
			}
			this.setRucEmpresa(CamposModificar.getId().getCodUsuario());
			this.setRucUsuario(CamposModificar.getId().getRuc());
			this.setRazonsocial(CamposModificar.getNombre());
			this.setCodUsuario(CamposModificar.getId().getCodUsuario());
			if(CamposModificar.getIsActive().trim().equals("Activo")){
				this.setEstado("Y");
			}else{
				this.setEstado("0");
			}
			this.setAuxPassword(CamposModificar.getPassword());
			
			
			grisadoModificar = true;
			
			this.setModificar(true);
			this.setRol("0");
			usuRoles=buscarCodUsuario();
			this.setRol(usuRoles.getId().getCodRol());
			

		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage mensaje=null;
			mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR,e.getMessage(),"Error al guardar");
			FacesContext.getCurrentInstance().addMessage("Error al guardar", mensaje);
		}
		
	}

	public List<FacGeneral> buscarPorCodigo(String codigo){
		
		List<FacGeneral> listaFacGeneral = null;
		
		try{
			
			listaFacGeneral = facEmpresaServicio.buscarDatosPorCodigo(codigo);

		}catch (Exception e) {
			e.printStackTrace();
			FacesMessage mensaje=null;
			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),"Error campo del combo no llenado");
			FacesContext.getCurrentInstance().addMessage("Error campo del combo no llenado", mensaje);
		}
		return  listaFacGeneral;
	}

	public List<FacUsuario> buscarDatosUsuarios(){
		
		List<FacUsuario> listaFacUsuario = null;
		
		try{
		
			
			listaFacUsuario = facUsuarioServicio.buscarTodosDatosUsuario(rucEmpresa);        

		}catch (Exception e) {
			e.printStackTrace();
			FacesMessage mensaje=null;
			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		}
		return  listaFacUsuario;
	}	
	
	public FacUsuariosRole buscarCodUsuario(){
		FacUsuariosRole usuRole = null;
		try {
			usuRole= facUsuarioServicio.buscarCodRol(rucUsuario, codUsuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
	return usuRole;
	}

	public FacUsuario verificarCodUsuario(String rucUsua,String rucEmp,String tipoUsua){
		FacUsuario Usuario = new FacUsuario();
		try {
			Usuario = facUsuarioServicio.BuscarCodUsuario(rucUsua,rucEmp,tipoUsua);
		} catch (Exception e) {
			FacesMessage mensaje=null;
			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,"Codigo del usuario","Codigo ya existente");
			FacesContext.getCurrentInstance().addMessage("Codigo de Usuario", mensaje);
		}
		
		return Usuario;
	}
	
    public FacEmpresa cargaRazonSocial(String rucEmpresa){
    	empresa = new FacEmpresa();
    	try{
    		empresa = facUsuarioServicio.buscarDatosPorRuc(rucEmpresa);
    	}catch (Exception e) {
    		FacesMessage mensaje=null;
    		mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,"Error al Buscar","Ruc no existente");
    		FacesContext.getCurrentInstance().addMessage(null, mensaje);
    	}
		return empresa;
    }
    
    
    public void LlenarRol(){
    	li_rol = new ArrayList<SelectItem>();
    	try {
    		role = facUsuarioServicio.buscarTodosRol();
    		
			 li_rol.add(new SelectItem("0","Selecione valor"));
			for(int i=0;i<role.size();i++){			
				li_rol.add(new SelectItem(role.get(i).getCodRol(),role.get(i).getDescripcion())); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public void llenarRucEmpresa (){
    	try {
			li_ruc = new ArrayList<SelectItem>();
			listaEmpresa = facUsuarioServicio.buscarEmpresa();
			if (!listaEmpresa.isEmpty()) {
				for (int i = 0; i < listaEmpresa.size(); i++) {
					li_ruc.add(new SelectItem(listaEmpresa.get(i).getRuc().trim(), listaEmpresa.get(i).getRazonSocial()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
	public void llenaTablausuario(){
		listaUsario = new ArrayList<FacUsuario>();  
		try{
			listaUsario = buscarDatosUsuarios();
			for(int i=0;i<listaUsario.size();i++){
				if(listaUsario.get(i).getTipoUsuario().trim().equals("E")){
					listaUsario.get(i).setTipoUsuario("Empleado");
				}else if(listaUsario.get(i).getTipoUsuario().trim().equals("C")){
					listaUsario.get(i).setTipoUsuario("Cliente");
				}else{
					listaUsario.get(i).setTipoUsuario("Proveedor");
				}
				if(listaUsario.get(i).getIsActive().trim().equals("Y")){
					listaUsario.get(i).setIsActive("Activo");
				}else{
					listaUsario.get(i).setIsActive("Inactivo");
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			FacesMessage mensaje=null;
			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		}

	}
	
	
	public void llenaComboTipoUsuario(){
		 li_tipo = new ArrayList<SelectItem>();  
		try{
			
			listGeneral = buscarPorCodigo("1");
			
			
			//si viene vacia
			if (listGeneral.isEmpty()){				
				general.setCodTabla("0");
				general.setCodUnico("0");
				general.setDescripcion("NO EXISTEN DATOS");
				general.setIdGeneral(0);
				general.setIsActive("Y");
				listGeneral.add(general);
			}
            
			 li_tipo.add(new SelectItem("0","Selecione valor"));
			for(int i=0;i<listGeneral.size();i++){			
				li_tipo.add(new SelectItem(listGeneral.get(i).getCodUnico(),listGeneral.get(i).getDescripcion())); 
			}
		}catch (Exception e) {
			e.printStackTrace();
			FacesMessage mensaje=null;
			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		}

	}
	
	public void llenaComboEstado(){
		 li_estado = new ArrayList<SelectItem>();  
		try{
			
			li_estado.add(new SelectItem("Y","Activo"));
			li_estado.add(new SelectItem("0","Inactivo"));
						
		}catch (Exception e) {
			e.printStackTrace();
			FacesMessage mensaje=null;
			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		}

	}
	 
	public void llenarIndentificacionUsuario(){
		try {
			requerido = true;
			if (this.getTipoUsuario().trim().equals("C")) {
				requerido = false;
				if (this.rucEmpresa.trim().length()>3) {
					//grisado=true;
					//grisadoCodUsuario = false;
						dialogoBusqueda = "diaUsu.hide()";
					listaUsario = buscarDatosUsuarios();
				}else{
					mensajeAlerta("Mensaje del sistema", "Ingrese una identifiación valida", "alerta");
					
				}
			}else{
				listaCliente = facUsuarioServicio.buscarClientes(rucUsuario, tipoUsuario);
				if (listaCliente.isEmpty()) {
					requerido = false;
					dialogoBusqueda = "diaUsu.show()";
					mensajeAlerta("Mensaje del sistema", "No Existen Usuario en esta empresa", "alerta");
				}
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void ubicarIdenUsuario(){
		try {
			rucEmpresa = selecionarClientes.getId().getRucCliente();
			usuario = new FacUsuario();
			usuario = this.verificarCodUsuario(rucEmpresa,rucUsuario,tipoUsuario);
			if(usuario!=null){
				if(rucUsuario.trim().equals(usuario.getId().getRuc().trim())){
					mensajeAlerta("Mensaje del sistema", "Codigo de Usuario:: ya existente \t\n En esta empresa", "alerta");
					rucEmpresa="";
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage mensaje=null;
			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),"Codigo ya existente");
			FacesContext.getCurrentInstance().addMessage("Codigo de Usuario", mensaje);
			
		}
	}
	
	public void activarBtnUsuario(){
		
		if (!this.getTipoUsuario().trim().equals("0")&&!this.getRucUsuario().trim().equals("0")) {
			grisado=false;
			grisadoCodUsuario = true;
			if (!this.getTipoUsuario().trim().equals("0")) {
				empresa = new FacEmpresa();
				empresa = this.cargaRazonSocial(rucUsuario);
				try{
					if(empresa!=null){
						this.setRazonsocial(empresa.getRazonSocial());
					}else{
						this.setRazonsocial(null);
					}
					if (this.getTipoUsuario().trim().equals("C")) {
						grisadoCodUsuario = false;	
					}else{
						grisadoCodUsuario = true;	
						rucEmpresa="";
					}
					
					
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			grisado=true;
			rucEmpresa="";
		}
		
		//rucEmpresa = "";
	}
	
	
	
	public void BorrarLiniea(){
		
		try {
			System.out.println("RucEmpresa::"+seleccionarCampo.getId().getRuc());
			System.out.println("CodUsuario::"+seleccionarCampo.getId().getCodUsuario());
			this.facUsuarioServicio.EliminarDatosLogico(seleccionarCampo);
			this.listaUsario.remove(seleccionarCampo);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public FacUsuarioServicio getFacUsuarioServicio() {
		return facUsuarioServicio;
	}
     
	public void setFacUsuarioServicio(FacUsuarioServicio facUsuarioServicio) {
		this.facUsuarioServicio = facUsuarioServicio;
	}
	
	public List<SelectItem> getli_tipo() {
		return li_tipo;
	}

	public void setli(List<SelectItem> li_tipo) {
		this.li_tipo = li_tipo;
	}

	
	public boolean isGrisadoModificar() {
		return grisadoModificar;
	}


	public void setGrisadoModificar(boolean grisadoModificar) {
		this.grisadoModificar = grisadoModificar;
	}


	public FacUsuario getCamposModificar() {
		return CamposModificar;
	}


	public void setCamposModificar(FacUsuario camposModificar) {
		CamposModificar = camposModificar;
	}


	public String getRucEmpresa() {
		return rucEmpresa;
	}

	public void setRucEmpresa(String rucEmpresa) {
		this.rucEmpresa = rucEmpresa;
	}


	public String getRucUsuario() {
		return rucUsuario;
	}


	public void setRucUsuario(String rucUsuario) {
		this.rucUsuario = rucUsuario;
	}


	public String getTipoUsuario() {
		return tipoUsuario;
	}


	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getRazonsocial() {
		return razonsocial;
	}


	public void setRazonsocial(String razonsocial) {
		this.razonsocial = razonsocial;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}	

	public FacGeneral getGeneral() {
		return general;
	}

	public void setGeneral(FacGeneral general) {
		this.general = general;
	}

	
	public List<SelectItem> getLi_estado() {
		return li_estado;
	}

	public void setLi_estado(List<SelectItem> li_estado) {
		this.li_estado = li_estado;
	}


	public FacturaServicio getFacEmpresaServicio() {
		return facEmpresaServicio;
	}


	public void setFacEmpresaServicio(FacturaServicio facEmpresaServicio) {
		this.facEmpresaServicio = facEmpresaServicio;
	}


	public List<FacGeneral> getListGeneral() {
		return listGeneral;
	}


	public void setListGeneral(List<FacGeneral> listGeneral) {
		this.listGeneral = listGeneral;
	}


	public List<SelectItem> getLi_tipo() {
		return li_tipo;
	}


	public void setLi_tipo(List<SelectItem> li_tipo) {
		this.li_tipo = li_tipo;
	}


	public FacUsuario getUsuario() {
		return usuario;
	}


	public void setUsuario(FacUsuario usuario) {
		this.usuario = usuario;
	}


	public FacUsuarioPK getId() {
		return id;
	}


	public void setId(FacUsuarioPK id) {
		this.id = id;
	}

	public List<FacUsuario> getListaUsario() {
		return listaUsario;
	}


	public void setListaUsario(List<FacUsuario> listaUsario) {
		this.listaUsario = listaUsario;
	}


	public FacUsuario getSeleccionarCampo() {
		return seleccionarCampo;
	}


	public void setSeleccionarCampo(FacUsuario seleccionarCampo) {
		this.seleccionarCampo = seleccionarCampo;
	}


	public FacEmpresa getEmpresa() {
		return empresa;
	}


	public void setEmpresa(FacEmpresa empresa) {
		this.empresa = empresa;
	}


	public String getCodUsuario() {
		return codUsuario;
	}


	public void setCodUsuario(String codUsuario) {
		this.codUsuario = codUsuario;
	}


	public FacCliente getClientes() {
		return clientes;
	}


	public void setClientes(FacCliente clientes) {
		this.clientes = clientes;
	}


	public boolean isGrisado() {
		return grisado;
	}


	public void setGrisado(boolean grisado) {
		this.grisado = grisado;
	}


	public boolean isModificar() {
		return modificar;
	}


	public void setModificar(boolean modificar) {
		this.modificar = modificar;
	}


	public boolean isRequerido() {
		return requerido;
	}


	public void setRequerido(boolean requerido) {
		this.requerido = requerido;
	}
	
	public String getActualizar() {
		return actualizar;
	}


	public void setActualizar(String actualizar) {
		this.actualizar = actualizar;
	}


	public List<FacRole> getRole() {
		return role;
	}


	public void setRole(List<FacRole> role) {
		this.role = role;
	}


	public List<SelectItem> getLi_rol() {
		return li_rol;
	}


	public void setLi_rol(List<SelectItem> li_rol) {
		this.li_rol = li_rol;
	}


	public String getRol() {
		return rol;
	}


	public void setRol(String rol) {
		this.rol = rol;
	}


	public FacUsuariosRole getUsurioRol() {
		return usurioRol;
	}


	public void setUsurioRol(FacUsuariosRole usurioRol) {
		this.usurioRol = usurioRol;
	}


	public FacUsuariosRolePK getIdRol() {
		return idRol;
	}


	public void setIdRol(FacUsuariosRolePK idRol) {
		this.idRol = idRol;
	}


	public String getAuxPassword() {
		return auxPassword;
	}


	public void setAuxPassword(String auxPassword) {
		this.auxPassword = auxPassword;
	}


	public List<SelectItem> getLi_ruc() {
		return li_ruc;
	}


	public void setLi_ruc(List<SelectItem> li_ruc) {
		this.li_ruc = li_ruc;
	}


	public List<FacEmpresa> getListaEmpresa() {
		return listaEmpresa;
	}


	public void setListaEmpresa(List<FacEmpresa> listaEmpresa) {
		this.listaEmpresa = listaEmpresa;
	}


	public List<FacCliente> getListaCliente() {
		return listaCliente;
	}


	public void setListaCliente(List<FacCliente> listaCliente) {
		this.listaCliente = listaCliente;
	}


	public List<FacCliente> getFiltarCliente() {
		return filtarCliente;
	}


	public void setFiltarCliente(List<FacCliente> filtarCliente) {
		this.filtarCliente = filtarCliente;
	}


	public FacCliente getSelecionarClientes() {
		return selecionarClientes;
	}


	public void setSelecionarClientes(FacCliente selecionarClientes) {
		this.selecionarClientes = selecionarClientes;
	}

	public List<FacUsuario> getListFiltraUsario() {
		return listFiltraUsario;
	}

	public void setListFiltraUsario(List<FacUsuario> listFiltraUsario) {
		this.listFiltraUsario = listFiltraUsario;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
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

	public boolean isGrisadoCodUsuario() {
		return grisadoCodUsuario;
	}

	public void setGrisadoCodUsuario(boolean grisadoCodUsuario) {
		this.grisadoCodUsuario = grisadoCodUsuario;
	}

	public String getDialogoBusqueda() {
		return dialogoBusqueda;
	}

	public void setDialogoBusqueda(String dialogoBusqueda) {
		this.dialogoBusqueda = dialogoBusqueda;
	}
	
	
	
}
