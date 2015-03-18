package com.clearminds.servicios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.clearminds.entidades.Equipo;
import com.clearminds.entidades.Categoria;

public class ServicioEquipos {
	private static Map<Integer,List<Equipo>> equipos;
	private static List<Categoria> categorias;
	
	public static void cargarEquipos(){
		equipos=new HashMap<Integer, List<Equipo>>();
		categorias=new ArrayList<Categoria>();
		categorias.add(new Categoria(1, "Serie A"));
		categorias.add(new Categoria(2, "Serie B"));
		categorias.add(new Categoria(3, "Zonal"));
		
		List<Equipo> equipos1=new ArrayList<Equipo>();
		equipos1.add(new Equipo(1,"Barce"));
		equipos1.add(new Equipo(2,"Nacho"));
		equipos1.add(new Equipo(3,"Emelec"));
		equipos1.add(new Equipo(4,"Liga"));
		
		List<Equipo> equipos2=new ArrayList<Equipo>();
		equipos2.add(new Equipo(1,"Católica"));
		equipos2.add(new Equipo(2,"Espoli"));
		equipos2.add(new Equipo(3,"Quevedo"));
		
		List<Equipo> equipos3=new ArrayList<Equipo>();
		equipos3.add(new Equipo(1,"Auquitas"));
		equipos3.add(new Equipo(2,"UTE"));

		equipos.put(1,equipos1);
		equipos.put(2,equipos2);
		equipos.put(3,equipos3);
		
	}
	
	public static List<Categoria> recuperarCategorias(){
		cargarEquipos();
		return categorias;
	}
	
	public static List<Equipo> buscarEquipos(int codigoCategoria){
		cargarEquipos();
		return equipos.get(codigoCategoria);
	}
		
}
