package com.siga.gratuita.util;

import javax.servlet.http.HttpServletRequest;

import com.atos.utils.UsrBean;
import com.siga.beans.*;
import java.util.*;

public class Colegio{
	private String idInstitucion;
	private Integer usu;
	private UsrBean usr= null; 
	
	public Colegio(){
		this.idInstitucion="";
		this.usu= new Integer(0);
		this.usr = new UsrBean();
		usr.setUserName(usu.toString());
		usr.setLocation(idInstitucion);
	}

	public Colegio(HttpServletRequest request){
		this.usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		this.idInstitucion = usr.getLocation();
		this.usu = Integer.valueOf(usr.getUserName());
		
	}
	
	public Colegio(String id){
		this.idInstitucion=id;
		this.usu= new Integer(0);
	}
	
	public Vector getAreas(){
		Vector v = null;
		ScsAreaAdm beanArea = new ScsAreaAdm(this.usr);
		try {
			String where =" where "+ScsAreaBean.C_IDINSTITUCION+"="+this.idInstitucion+" ";
			v = beanArea.select(where);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return v;		
	}
	
	public Vector getZonas(){
		Vector v = null;
		ScsZonaAdm beanZona = new ScsZonaAdm(this.usr);
		try {
			String where = " where " + ScsZonaBean.C_IDINSTITUCION+"="+this.idInstitucion+" ";
			v = beanZona.select(where);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return v;		
	}
	
	public Vector getPartidosJudiciales(){
		Vector v = null;
		CenPartidoJudicialAdm beanPartida = new CenPartidoJudicialAdm(this.usr);
		try {
			String where =""; //está por especificar este método
			v = beanPartida.select(where);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return v;		
	}
	
	public Vector getPartidasPresupuestarias(){
		Vector v = null;
		ScsPartidaPresupuestariaAdm beanPartida = new ScsPartidaPresupuestariaAdm(this.usr);
		try {
			v = beanPartida.select(this.idInstitucion);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return v;		
	}
	
	public Vector getGruposFacturacion(){
		Vector v = null;
		ScsGrupoFacturacionAdm beanPartida = new ScsGrupoFacturacionAdm(this.usr);
		try {
			String where= " where "+ScsGrupoFacturacionBean.C_IDINSTITUCION+"="+this.idInstitucion+" ";
			v = beanPartida.select(where);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return v;		
	}	
}