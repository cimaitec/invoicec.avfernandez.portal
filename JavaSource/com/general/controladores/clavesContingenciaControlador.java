package com.general.controladores;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.primefaces.event.FileUploadEvent;

import com.documentos.entidades.FacEmpresa;
import com.general.entidades.FacClavescontingencia;
import com.general.entidades.FacGeneral;
import com.general.servicios.FacClaveContingenciaServicios;


@ViewScoped
@ManagedBean
public class clavesContingenciaControlador {

	@EJB
	private FacClaveContingenciaServicios servicio;
	private FacEmpresa Empresa;
	private String ambiente;
	private FacClavescontingencia entidadClave;
	private String destination="C:\\Invoice\\";

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
		
		}catch (Exception e) {
			FacesMessage mensaje=null;
			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		}
	}


    public void handleFileUpload(FileUploadEvent event) {
    	try {
    		FacesMessage msg=null;
    		BufferedReader leerInsert=null;
    		BufferedReader leerNumero=null;
    		boolean mostar=false;
    		ArrayList<String> ListErrores = new ArrayList<String>();
    		//ambiente="1";
    		
    		System.out.println("ambiente::"+getAmbiente());    		
    		
    		if(ambiente.equals("0")){
    			msg = new FacesMessage("Seleccionar Tipo de ambiente", "");
    			FacesContext.getCurrentInstance().addMessage(null, msg);
    			return;
    		}else{
    			try {
    				File nameFile =new File (event.getFile().getFileName());
                    //copyFile(event.getFile().getFileName(), event.getFile().getInputstream(),destination);
    				copyFile(nameFile.getName(), event.getFile().getInputstream(),destination);
                    //Revision del Archivo
                    //FileReader fileRevision = new FileReader(destination+event.getFile().getFileName());
                    FileReader fileRevision = new FileReader(destination+nameFile.getName());
                    leerNumero = new BufferedReader(fileRevision);
                    String linea ="";
            		int length = 1;
            		int idxLinea = 0;
            		int contInsert=0;
            		int contFail=0;
            		boolean flagOk = true;
            		while((linea=leerNumero.readLine())!=null){
            			for (char x : linea.toCharArray()) {
            				idxLinea ++;
            				if (Character.isLetter(x)) {            					
            					ListErrores.add(""+idxLinea);  
            					flagOk = false;
            				}
            			}
            		}
            		if (!flagOk){
            			System.out.println("El txt no se puede ingresar las siguientes Lineas no son validas ->"+ListErrores.toArray()); 
            			msg = new FacesMessage("El txt no se puede ingresar las siguientes Lineas no son validas ->"+ListErrores.toArray(), "");
            			FacesContext.getCurrentInstance().addMessage(null, msg);
            			return;
            		}else{
            			System.out.println("cargado::ok");  
            			ListErrores.clear();
            			fileRevision = new FileReader(destination+event.getFile().getFileName());
                        leerNumero = new BufferedReader(fileRevision);
                        linea = "";
                        idxLinea = 0;
                		while ((linea=leerNumero.readLine())!=null) {
                			int corte = 13;
                			int corteFin = 37;
                			int digitos = linea.length();
                			String ruc="";
                			String clave="";
                			idxLinea ++;
                			for (int i = 0; i < digitos; i++) {
                				if(i<corte){
                					ruc = ruc+linea.charAt(i);
                				}
                				if(i<=corteFin){
                					clave = clave+linea.charAt(i);
                				}
                				if((i+1)==corteFin){
                					entidadClave = new FacClavescontingencia();
                					entidadClave.setClave(clave);
                					entidadClave.setEstado("0");
                					entidadClave.setRuc(ruc);
                					entidadClave.setTipo(ambiente);
                					entidadClave.setIdclavecontingencia(servicio.lengthTabla());
                					try {
                						List<FacClavescontingencia> listEntidadClaveFin = new ArrayList<FacClavescontingencia>();
                						try{
                							listEntidadClaveFin = servicio.findByClave(clave,ambiente);
                							if (listEntidadClaveFin.size()==0)
                								listEntidadClaveFin = null;
                						}catch(Exception e){
                							listEntidadClaveFin = null;
                						}
                						if (listEntidadClaveFin == null){
                							servicio.IngresarClaveContingencia(entidadClave);
                							contInsert ++; 
                						}else{
                							contFail ++;
                						}
                						mostar=true;
            						} catch (Exception e) {
            							mostar=false;
            							e.printStackTrace();
            							ListErrores.add(""+idxLinea);  
            							System.out.println("Error al guardar" +length+" "+e.getLocalizedMessage());
            							
            						}
                					corte=corteFin+13;
                					corteFin=37+corteFin;
                    				clave="";ruc="";
                				}
            				}
                			length++;
                		}
                		
                		if (ListErrores.size()>0){
                			msg = new FacesMessage("El txt no se puede ingresar las siguientes Lineas no se almacenaron->"+ListErrores.toArray(), "");
                			FacesContext.getCurrentInstance().addMessage(null, msg);
                		}else{
                			String msgCarga = "Claves Cargadas "+contInsert+ " Claves Fallidas " +contFail;
                			msg = new FacesMessage("Archivo de Claves de Contingencia Almacenado Correctamente. "+msgCarga, "");
                			FacesContext.getCurrentInstance().addMessage(null, msg);
                		}
            		}
            		
                } catch (IOException e) {
                    e.printStackTrace();
                }    			
    			
    		}
    			
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage msg = new FacesMessage("Error generar el txt ","");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
    
    public void copyFile(String fileName, InputStream in, String destination) {
        try {
          
          
             // write the inputStream to a FileOutputStream
             OutputStream out = new FileOutputStream(new File(destination + fileName));
          
             int read = 0;
             byte[] bytes = new byte[1024];
          
             while ((read = in.read(bytes)) != -1) {
                 out.write(bytes, 0, read);
             }
          
             in.close();
             out.flush();
             out.close();
          
             System.out.println("New file created!");
             } catch (IOException e) {
             System.out.println(e.getMessage());
             }
 }

    //contructor que carga los datos de la empresa emisora
	private FacEmpresa cargarEmpresa(String ruc){
		Empresa = new FacEmpresa();
		try{
			Empresa = servicio.verificarRuc(ruc);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return Empresa;
	}

	public String getAmbiente() {
		return ambiente;
	}
	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}


	public FacClaveContingenciaServicios getServicio() {
		return servicio;
	}


	public void setServicio(FacClaveContingenciaServicios servicio) {
		this.servicio = servicio;
	}


	public FacEmpresa getEmpresa() {
		return Empresa;
	}


	public void setEmpresa(FacEmpresa empresa) {
		Empresa = empresa;
	}


	public FacClavescontingencia getEntidadClave() {
		return entidadClave;
	}


	public void setEntidadClave(FacClavescontingencia entidadClave) {
		this.entidadClave = entidadClave;
	}


}
