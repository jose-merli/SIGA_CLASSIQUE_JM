package com.siga.administracion;

import com.siga.beans.*;

/**
 * @author Luis Miguel Sánchez PIÑA.
 *
 * <p>Clase de constantes específicas de la aplicación SIGA.</p>
 * <p>Es independiente, aunque complementaria de la clase com.atos.utils.ClsConstants,
 * que es reutilizable entre distintas aplicaciones.</p>
 */
public interface SIGAConstants
{
	/**
	 * Para referenciar la hoja de estilos css entre los atributos de sesión.
	 */	
	public static final String STYLESHEET_REF="SIGA_STYLESHEET";
	public static final String STYLESHEET_SKIN="SKIN";
	public static final String PATH_LOGO="SIGA_LOGO";
	public static final String STYLESHEET_PATH="directorios.carpeta.css";

	/**
	 * Para referenciar el path del logotipo de la institucion entre los atributos de sesión.
	 */
	public static final String LOGO="LOGO";
	public static final String COLOR="COLOR";
	public static final String FONT="FONT";
	
	public static final String MENU_POSITION_REF="MENU_POSITION";
	public static final String MENU_TOP="TOP";
	public static final String MENU_LEFT="LEFT";
	
	public static final String ACCESS_DENY	         = "Acceso Denegado"; //"DENY";
	public static final String ACCESS_FULL	         = "Acceso Total"; //"FULL";
	public static final String ACCESS_READ	         = "Sólo Lectura"; //"READ";
	public static final String ACCESS_NONE           = "Sin acceso"; //"NONE";
	public static final String ACCESS_SIGAENPRODUCCION = "Acceso SIGA en produccion"; //"ACCESS_SIGAENPRODUCCION";
	public static final int    TIPO_COLEGIADO		 = 1;
	public static final int    TIPO_NO_COLEGIADO	 = 2;
	
	
	public static final String PERFIL_NOMBRETABLA   = AdmPerfilBean.T_NOMBRETABLA;
	public static final String PERFIL_IDPERFIL      = AdmPerfilBean.C_IDPERFIL;
	public static final String PERFIL_IDINSTITUCION = AdmPerfilBean.C_IDINSTITUCION;
	public static final String PERFIL_DESCRIPCION   = AdmPerfilBean.C_DESCRIPCION;
	
	public static final String LITERAL_VISIBILDAD   = "VISIBILIDAD=";
	public static final String VISIBILDAD_PUBLICA   = "PUBLICA ";
	public static final String VISIBILDAD_PRIVADA   = "SEMIPUBLICA ";	
	
	public static final int TAMANYO_ARRAY_CONSULTA = 50;
	public static final String TYPE_NUMERIC	         = "N"; 
	public static final String TYPE_ALPHANUMERIC     = "A"; 
	public static final String TYPE_LONG	         = "L";
	public static final String TYPE_DATE	         = "D"; 
	public static final String TYPE_MULTIVALOR       = "MV"; 
}
