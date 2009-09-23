package com.siga.gui.processTree;

import com.siga.administracion.SIGAConstants;

public interface SIGAPTConstants extends SIGAConstants
{
	public static final int    DRAG                  = 1;
	public static final int    DROP                  = 2;
	public static final String LOAD_PROCESSES        = "loadprocess";
	public static final String MOVE_PROCESS          = "moveprocess";
	public static final String LOAD_ACCESS           = "loadaccess";
	public static final String MODIFYACCESS          = "modifyaccess";
	public static final String LOAD_USUARIOS_GRUPO   = "loadusuariosgrupo";
	public static final String SAVE_USUARIOS_GRUPO   = "saveusuariosgrupo";
	public static final String LOAD_GRUPOS           = "loadgrupos";
	public static final String CHARACTER_ENCODING	 = "ISO-8859-1";
	public static final String SERVLET_FUNCTION      = "function";
	public static final String OIDMOVED		         = "oidmoved";
	public static final String OIDTARGET		     = "oidtarget";
	public static final String PROFILE		         = "profile";
	public static final String INSTITUCION	         = "institucion";
	public static final String LITERALES		     = "lit";
	public static final String CREAR_USUARIO_GRUPO   = "crearusuariogrupo";
	public static final String BORRAR_USUARIO_GRUPO  = "borrarusuariogrupo";
	
	public static final String SERVLET_LOG		     = "log";
	public static final String SERVLET_ERROR		 = "error";
	
	public static final String NAME		             = "NAME";
	public static final String OID	  	             = "OID";
	public static final String PARENT		         = "PARENT";
	public static final String USERMOD		         = "USERMOD";
	public static final String DATEMOD		         = "DATEMOD";
	public static final String ACCESS		         = "ACCESS";
	
	// Avisos
//	public static final String STRUC_CHANGED_reload  = "Estructura cambiada, ¿Quiere recargarla?"; //W050";
	public static final String STRUC_CHANGED_reload  = "Estructura cambiada correctamente"; //W050";
	public static final String warning               = "Aviso"; //W010";
	// errores
	public static final String STRUC_CHANGED         = "Estructura cambiada"; //E100";
	public static final String REG_CHANGED           = "Registro modificado"; //E101";
	public static final String PL_ERROR              = "Database Error"; //E200";
	public static final String REG_DELETED           = "Registro borrado"; //E300";
	public static final String PATER_RESTRIC         = "El padre es más restrictivo que el hijo"; //E400";
	public static final String PARTIAL_PATER_RESTRIC = "Algún padre es más restrictivo que el hijo"; //E401";
	public static final String CONFIRM_SAVE          = "¿Desea guardar los cambios?";
	public static final String USUARIOS              = "USUARIOS";
	public static final String DESASIGNAR_GRUPO      = "DESASIGNAR GRUPO/S";
	public static final String USUARIO_YA_ASIGNADO   = "El Usuario ya está asignado al Grupo";
	public static final String MOVIMIENTO_NO_VALIDO  = "Movimiento no válido";
	public static final String ASIGNAR_USUARIO_GRUPO = "Debe asignar Usuarios a un Grupo o un Grupo a un Usuario";
}