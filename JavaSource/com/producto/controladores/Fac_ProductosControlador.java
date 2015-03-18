package com.producto.controladores;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import com.general.entidades.FacGeneral;
import com.general.entidades.FacProducto;
import com.general.entidades.PantallaDetalleProductoentidades;
import com.producto.servicios.Fac_ProductosServicios;


@ViewScoped
@ManagedBean
public class Fac_ProductosControlador {
	
	@EJB
 	private Fac_ProductosServicios productoservicio;
	
	private int cod_auxiliar;
	private String tipoProducto;
	private String descripcion;
	private double valorUni;
	private double porcentaje;
	private String Tipoiva;
	private String TipoIvaDescripcion;
	private String TipoICE;
	private String TipoICEDescripcion;
	private String Atributo1;
	private String VAtributo1;
	private String Atributo2;
	private String VAtributo2;
	private String Atributo3;
	private String VAtributo3;
	private List<FacGeneral> listaimpuestos;
	private List<FacGeneral> filtrosimpuestos;
	private FacGeneral verCamposImpuestos;
	private String Seleccion;
	private boolean flagPorcentaje = true;
	private boolean flagIce = true;
	private boolean flagCtrlDetalle = true;

	private List<SelectItem> listaproductos;
	private List<PantallaDetalleProductoentidades> Filtroproductos;
	private List<PantallaDetalleProductoentidades> tablaproductos;
	private PantallaDetalleProductoentidades seleccionatablaproducto;
	
	public String Accion;

	//TODO contructor que se encarga de cargar los datos 
	public void cargarDatos(){
		try {
			flagPorcentaje = false;
			flagIce = true;
			this.tipoProducto = "S";
			limpiarControles();
			// llenar combo de tipo producto
			LlenarComboGeneral("92", listaproductos, "fac_general");
			DetalleProducto();
			
		} catch (Exception e) {
			FacesMessage mensaje=null;
			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		}
	}
	
	//TODO contructor que se encarga de cargar los datos 
		public void pruebas(){
			
				System.out.println("tipoProducto::"+tipoProducto);	
				if (tipoProducto.equals("B")){
					flagPorcentaje = false;
					flagIce = true;
				}else{
					flagPorcentaje = true;
					flagIce = false;
				}
				if (flagPorcentaje)
					System.out.println("flagPorcentaje::S");
				else
					System.out.println("flagPorcentaje::N");
		}
	
	//TODO contructor que carga todo los impuestos sea ICE o IVA
	public void CargarImpuestos(String Evento){
		try{
			listaimpuestos = new ArrayList<FacGeneral>();
			String Codigo = (Evento.trim().equals("IVA") ? "24" : "28");
			Seleccion = Evento;
			listaimpuestos = productoservicio.buscarDatosPorCodigo(Codigo);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//TODO contructor que carga todo los impuestos sea ICE o IVA
	public void CargarImpuestosseleccion(){
		try{
			if(Seleccion.trim().equals("IVA")){
				Tipoiva = verCamposImpuestos.getCodUnico();
				TipoIvaDescripcion = verCamposImpuestos.getDescripcion();
			}
			else{
				TipoICE = verCamposImpuestos.getCodUnico();
				TipoICEDescripcion = verCamposImpuestos.getDescripcion();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//TODO contructor que carga el detalle del producto
	private void DetalleProducto(){
		try {
			List<FacProducto> con_producto = new ArrayList<FacProducto>();
			con_producto = productoservicio.ConsultarProductoProducto();
			if(!con_producto.isEmpty()){
				for (FacProducto facProducto : con_producto) {
					
					tablaproductos.add(new PantallaDetalleProductoentidades(facProducto.getCodPrincipal(), facProducto.getCodAuxiliar(), facProducto.getDescripcion(), 
															consultaTipoDe(listaproductos,facProducto.getTipoProducto()), facProducto.getValorUnitario(), 
															consultaTipoDeImpuestos(productoservicio.buscarDatosPorCodigo("24"), facProducto.getTipoIva().toString()), 
															consultaTipoDeImpuestos(productoservicio.buscarDatosPorCodigo("28"), facProducto.getCodIce().toString()), facProducto.getPorcentaje()));
				}
			}
		} catch (Exception e) {
			FacesMessage mensaje=null;
			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		}
	}
	
	//TODO Contructor que se encarga de consultar los tipos de  impuesto para el detalle del producto
	private String consultaTipoDeImpuestos( List<FacGeneral> lista, String filtro){
		String descripcion = "";
		for (FacGeneral detallecombo : lista) {
			if(detallecombo.getCodUnico().toString().trim().equals(filtro.toString().trim()))
				descripcion = detallecombo.getDescripcion();
		}
		return descripcion;
	}
	
	//TODO Contructor que se encarga de consultar los tipos de para el detalle del producto
	private String consultaTipoDe( List<SelectItem> lista, String filtro){
		String descripcion = "";
		for (SelectItem detallecombo : lista) {
			if(detallecombo.getValue().toString().trim().equals(filtro.toString().trim()))
				descripcion = detallecombo.getLabel();
		}
		return descripcion;
	}
	
	//TODO contructor que se encarga de limpiar todos los controles
	private void limpiarControles(){
		Accion = "";
		Seleccion = "";
		cod_auxiliar = 0;
		valorUni = 0.00;
		porcentaje = 0.00;
		tipoProducto = "";
		descripcion = "";
		TipoICE = "";
		TipoICEDescripcion = "";
		Tipoiva = "";
		TipoIvaDescripcion = "";
		Atributo1 = "";
		Atributo2 = "";
		Atributo3 = "";
		VAtributo1 = "";
		VAtributo2 = "";
		VAtributo3 = "";
		listaimpuestos = new ArrayList<FacGeneral>();
		listaproductos = new ArrayList<SelectItem>();
		tablaproductos = new ArrayList<PantallaDetalleProductoentidades>();
	}
	
	//TODO contructor que se encarga de llenar los combo
	private void LlenarComboGeneral(String codigoGeneral, List<SelectItem> listaItems, String lista){
		try{
			if(lista.toString().trim().equals("fac_general")){
				List<FacGeneral> listGeneral = new ArrayList<FacGeneral>();
				FacGeneral generales = new FacGeneral(); 
				listGeneral = productoservicio.buscarDatosPorCodigo(codigoGeneral);
				if (listGeneral.isEmpty()){		
					generales.setCodTabla("0");
					generales.setCodUnico("0");
					generales.setDescripcion("NO EXISTEN DATOS");
					generales.setIdGeneral(0);
					generales.setIsActive("0");
					listGeneral.add(generales);
				}
				for(int i=0;i<listGeneral.size();i++)
					listaItems.add(new SelectItem(listGeneral.get(i).getCodUnico().trim(), listGeneral.get(i).getDescripcion())); 
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	//TODO contructor que sirve para validar los numeros
	public void validarNumero(String textoNumerico){
		String texto = new String();
		if(textoNumerico.toString().trim().equals("codAuxiliar"))
			texto= String.valueOf(cod_auxiliar);
		for(char x: texto.toCharArray()){
			if(Character.isLetter(x)){
				mensajeAlerta("Mensaje de sistema","El texto "+ textoNumerico +" solo es numerico","alerta");
				break;
			}
		}
	}
	
	//TODO contructor de mensaje de alerta para mostrar al usuario
	private void mensajeAlerta(String mensajeVentana, String mensajeDetalle, String tipo) {
		 FacesContext context = FacesContext.getCurrentInstance();            
	     context.addMessage(null, new FacesMessage((tipo.toString().trim().equals("alerta") ? FacesMessage.SEVERITY_ERROR : tipo.toString().trim().equals("peligro") ? FacesMessage.SEVERITY_WARN : FacesMessage.SEVERITY_INFO),mensajeVentana, mensajeDetalle));
	}

	//TODO Contructor que son los acciones de los botones
	public void AccionBoton(String Accion){

		if(Accion.toString().trim().equals("Modificar"))
			this.Accion = Accion;
		
		if(Accion.toString().trim().equals("Guardar"))
			GuardarDatos();
			
		if(Accion.toString().trim().equals("Actualizar"))
			cargarDatos();
		
		if(Accion.toString().trim().equals("Modificar"))
			RetornandoDatosparaModificar();
	}

	private void RetornandoDatosparaModificar(){
		try{
			
			List<FacProducto> producto = productoservicio.consultaModificables(seleccionatablaproducto.getCod_producto());
			if(!producto.isEmpty()){
			 	cod_auxiliar = producto.get(0).getCodAuxiliar();
			 	String Tipproducto = producto.get(0).getTipoProducto().trim();
			 	String IVA = producto.get(0).getTipoIva().toString().trim();
			 	String ICE = producto.get(0).getCodIce().toString().trim();
			 	if(ICE.equals("-1"))
			 		ICE = "";
			 	
			 	setTipoProducto(Tipproducto);
			 	descripcion = producto.get(0).getDescripcion();
			 	valorUni = producto.get(0).getValorUnitario();
			 	Tipoiva = IVA;
			 	TipoIvaDescripcion = seleccionatablaproducto.getTipoIvadescrip();
			 	TipoICE = ICE;
			 	TipoICEDescripcion = seleccionatablaproducto.getCodIcedescrip();
			 	Atributo1 = producto.get(0).getAtributo1();
			 	VAtributo1 = producto.get(0).getValor1();
			 	Atributo2 = producto.get(0).getAtributo2();
			 	VAtributo2 = producto.get(0).getValor2();
			 	Atributo3 = producto.get(0).getAtributo3();
			 	VAtributo3 = producto.get(0).getValor3();
			 	porcentaje = producto.get(0).getPorcentaje();
			}
		}catch (Exception e) {
  			e.printStackTrace();
			FacesMessage mensaje=null;
			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		}
	}
	//TODO Contructor que guarda el contenido
	private void GuardarDatos(){
		try{
		if(ValidarControles()){
			return;
		}
		FacProducto RegistroProducto = new FacProducto();
		int secuencial = ((Accion.toString().trim().equals("")) ? productoservicio.secuencialproducto() : seleccionatablaproducto.getCod_producto());
		RegistroProducto.setCodPrincipal(secuencial);
		RegistroProducto.setCodAuxiliar(cod_auxiliar);
		RegistroProducto.setDescripcion(descripcion);
		RegistroProducto.setTipoProducto(tipoProducto);
			RegistroProducto.setValorUnitario(valorUni);
		if((Tipoiva==null)||(Tipoiva.equals("")))
			RegistroProducto.setTipoIva(0);
		else
			RegistroProducto.setTipoIva(Integer.parseInt(Tipoiva.toString().trim()));
		if((TipoICE==null)||(TipoICE.equals("")))
			RegistroProducto.setCodIce(0);
		else
			RegistroProducto.setCodIce(Integer.parseInt((TipoICE != "" ? TipoICE.toString().trim() : "-1")));
		RegistroProducto.setAtributo1(Atributo1);
		RegistroProducto.setValor1(VAtributo1);
		RegistroProducto.setAtributo2(Atributo2);
		RegistroProducto.setValor2(VAtributo2);
		RegistroProducto.setAtributo3(Atributo3);
		RegistroProducto.setValor3(VAtributo3);
		RegistroProducto.setPorcentaje(porcentaje);
		if(((Accion.toString().trim().equals("")) ? productoservicio.insertarProducto(RegistroProducto) : productoservicio.modificarProducto(RegistroProducto)) == 0){
			mensajeAlerta("Mensaje del sistema", "Registro " + ((Accion.toString().trim().equals("")) ? "Ingresado" : "Modificado") + "con exito", "informacion");
			cargarDatos();
		}else
			mensajeAlerta("Mensaje del sistema", "problema para guardar el registro", "alerta");
		
		}catch (Exception e) {
  			e.printStackTrace();
			FacesMessage mensaje=null;
			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		}
	}
	
	private boolean ValidarControles(){
		if((tipoProducto == "" || tipoProducto == null) && !Accion.trim().equals("Modificar")){
			mensajeAlerta("Mensaje del sistema", "Por favor seleccione el tipo del producto", "peligro");
			return true;
		}
		if(descripcion.toString().trim().equals("")){
			mensajeAlerta("Mensaje del sistema", "Por favor ingrese la descripcion del producto", "peligro");
			return true;
		}
		System.out.println("tipoProducto::"+tipoProducto);
		if (tipoProducto.equals("B")){
		if(valorUni == 0.0){
			mensajeAlerta("Mensaje del sistema", "Por favor el valor del producto tiene que ser mayor que 0 ", "peligro");
			return true;
		}}
		if (!((porcentaje > 0)||(porcentaje<=100))){
			mensajeAlerta("Mensaje del sistema", "Por favor ingrese el porcentaje entre el rango >0 y <=100", "peligro");
			return true;
		}
		
		if(Tipoiva.toString().trim().equals("-1")  && !Accion.trim().equals("Modificar")){
			mensajeAlerta("Mensaje del sistema", "Por favor seleccione el tipo de IVA", "peligro");
			return true;
		}
		return false;
	}

	public List<SelectItem> getListaproductos() {
		return listaproductos;
	}

	public void setListaproductos(List<SelectItem> listaproductos) {
		this.listaproductos = listaproductos;
	}

	public int getCod_auxiliar() {
		return cod_auxiliar;
	}

	public void setCod_auxiliar(int cod_auxiliar) {
		this.cod_auxiliar = cod_auxiliar;
	}

	public String getTipoProducto() {
		return tipoProducto;
	}

	public void setTipoProducto(String tipoProducto) {
		this.tipoProducto = tipoProducto;
	}

	public double getValorUni() {
		return valorUni;
	}

	public void setValorUni(double valorUni) {
		this.valorUni = valorUni;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTipoiva() {
		return Tipoiva;
	}

	public void setTipoiva(String tipoiva) {
		Tipoiva = tipoiva;
	}

	public String getTipoICE() {
		return TipoICE;
	}

	public void setTipoICE(String tipoICE) {
		TipoICE = tipoICE;
	}

	public String getAtributo1() {
		return Atributo1;
	}

	public void setAtributo1(String atributo1) {
		Atributo1 = atributo1;
	}

	public String getVAtributo1() {
		return VAtributo1;
	}

	public void setVAtributo1(String vAtributo1) {
		VAtributo1 = vAtributo1;
	}

	public String getAtributo2() {
		return Atributo2;
	}

	public void setAtributo2(String atributo2) {
		Atributo2 = atributo2;
	}

	public String getVAtributo2() {
		return VAtributo2;
	}

	public void setVAtributo2(String vAtributo2) {
		VAtributo2 = vAtributo2;
	}

	public String getAtributo3() {
		return Atributo3;
	}

	public void setAtributo3(String atributo3) {
		Atributo3 = atributo3;
	}

	public String getVAtributo3() {
		return VAtributo3;
	}

	public void setVAtributo3(String vAtributo3) {
		VAtributo3 = vAtributo3;
	}

	public List<PantallaDetalleProductoentidades> getTablaproductos() {
		return tablaproductos;
	}

	public void setTablaproductos(List<PantallaDetalleProductoentidades> tablaproductos) {
		this.tablaproductos = tablaproductos;
	}

	public PantallaDetalleProductoentidades getSeleccionatablaproducto() {
		return seleccionatablaproducto;
	}

	public void setSeleccionatablaproducto(
			PantallaDetalleProductoentidades seleccionatablaproducto) {
		this.seleccionatablaproducto = seleccionatablaproducto;
	}

	public String getTipoIvaDescripcion() {
		return TipoIvaDescripcion;
	}

	public void setTipoIvaDescripcion(String tipoIvaDescripcion) {
		TipoIvaDescripcion = tipoIvaDescripcion;
	}

	public String getTipoICEDescripcion() {
		return TipoICEDescripcion;
	}

	public void setTipoICEDescripcion(String tipoICEDescripcion) {
		TipoICEDescripcion = tipoICEDescripcion;
	}

	public List<FacGeneral> getListaimpuestos() {
		return listaimpuestos;
	}

	public void setListaimpuestos(List<FacGeneral> listaimpuestos) {
		this.listaimpuestos = listaimpuestos;
	}

	public List<FacGeneral> getFiltrosimpuestos() {
		return filtrosimpuestos;
	}

	public void setFiltrosimpuestos(List<FacGeneral> filtrosimpuestos) {
		this.filtrosimpuestos = filtrosimpuestos;
	}

	public FacGeneral getVerCamposImpuestos() {
		return verCamposImpuestos;
	}

	public void setVerCamposImpuestos(FacGeneral verCamposImpuestos) {
		this.verCamposImpuestos = verCamposImpuestos;
	}

	public String getSeleccion() {
		return Seleccion;
	}

	public void setSeleccion(String seleccion) {
		Seleccion = seleccion;
	}

	public List<PantallaDetalleProductoentidades> getFiltroproductos() {
		return Filtroproductos;
	}

	public void setFiltroproductos(
			List<PantallaDetalleProductoentidades> filtroproductos) {
		Filtroproductos = filtroproductos;
	}

	public double getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(double porcentaje) {
		this.porcentaje = porcentaje;
	}

	public Fac_ProductosServicios getProductoservicio() {
		return productoservicio;
	}

	public void setProductoservicio(Fac_ProductosServicios productoservicio) {
		this.productoservicio = productoservicio;
	}
	public boolean getFlagPorcentaje() {
		return flagPorcentaje;
	}
	
	public boolean isFlagPorcentaje() {
		return flagPorcentaje;
	}

	public void setFlagPorcentaje(boolean flagPorcentaje) {
		this.flagPorcentaje = flagPorcentaje;
	}

	public boolean getFlagIce() {
		return flagIce;
	}
	public boolean isFlagIce() {
		return flagIce;
	}

	public void setFlagIce(boolean flagIce) {
		this.flagIce = flagIce;
	}
	
	public boolean getFlagCtrlDetalle() {
		return flagCtrlDetalle;
	}
	
	public boolean isFlagCtrlDetalle() {
		return flagCtrlDetalle;
	}

	public void setFlagCtrlDetalle(boolean flagCtrlDetalle) {
		this.flagCtrlDetalle = flagCtrlDetalle;
	}
	
	
}
