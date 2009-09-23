/*
 * Created on 01-feb-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

/**
 * @author s230298
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ScsJurisdiccionBean extends MasterBean {

		/* Variables */	
		private String 	descripcion;
		private Integer idJurisdiccion;
		
		/* Nombre tabla */
		static public String T_NOMBRETABLA = "SCS_JURISDICCION";
		
		/* Nombre campos de la tabla */
		static public final String C_IDJURISDICCION		= "IDJURISDICCION";
		static public final String C_DESCRIPCION		= "DESCRIPCION";
	
		/* cambio para codigo ext */
		private String codigoExt;
		static public final String C_CODIGOEXT = "CODIGOEXT";
		public void setCodigoExt (String valor)
		{
			this.codigoExt = valor;
		}
		public String getCodigoExt ()
		{
			return codigoExt;
		}
		//////
		
		/**
		 * @return Returns the descripcion.
		 */
		public String getDescripcion() {
			return descripcion;
		}
		/**
		 * @param descripcion The descripcion to set.
		 */
		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}
		/**
		 * @return Returns the idJurisdiccion.
		 */
		public Integer getIdJurisdiccion() {
			return idJurisdiccion;
		}
		/**
		 * @param idJurisdiccion The idJurisdiccion to set.
		 */
		public void setIdJurisdiccion(Integer idJurisdiccion) {
			this.idJurisdiccion = idJurisdiccion;
		}
}
