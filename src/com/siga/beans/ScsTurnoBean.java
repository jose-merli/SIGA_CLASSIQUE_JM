package com.siga.beans;

import com.siga.Utilidades.AjaxXMLBuilderAnnotation;
import com.siga.Utilidades.AjaxXMLBuilderNameAnnotation;
import com.siga.Utilidades.AjaxXMLBuilderValueAnnotation;
/**
 * Implementa las operaciones sobre el bean de la tabla SCS_TURNO
 * 
 * @author ruben.fernandez
 * @since 26/10/2004 
 */
@AjaxXMLBuilderAnnotation 
public class ScsTurnoBean extends MasterBean{
	
	/* Variables */ 
	
	private Integer 	idInstitucion;
	private Integer 	idTurno;
	private String		nombre;
	private String		abreviatura;
	private String		descripcion;
	private Integer		guardias;
	private String		validarJustificaciones;
	private String		designaDirecta;
	private String		validarInscripciones;
	private Integer		idArea;
	private Integer		idMateria;
	private Integer		idZona;
	private Integer		idSubzona;
	private Integer		idOrdenacionColas;
	private Integer		idPartidaPresupuestaria;
	private Integer		idGrupoFacturacion;	
	private String		requisitos;	
	private Integer		idPersonaUltimo;
	private String      activarRestriccionAcreditacion;
	private String      letradoAsistencias;
	private String      letradoActuaciones;
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_TURNO";
	
	
	/*Nombre de campos de la tabla*/
	
	static public final String 	C_IDINSTITUCION = 			"IDINSTITUCION";
	static public final String 	C_IDTURNO = 				"IDTURNO";
	static public final String 	C_NOMBRE = 					"NOMBRE";
	static public final String 	C_ABREVIATURA = 			"ABREVIATURA";
	static public final String 	C_REQUISITOS = 				"REQUISITOS";	
	static public final String	C_DESCRIPCION = 			"DESCRIPCION";
	static public final String	C_GUARDIAS = 				"GUARDIAS";
	static public final String 	C_VALIDARJUSTIFICACIONES = 	"VALIDARJUSTIFICACIONES";
	static public final String 	C_DESIGNADIRECTA = 			"DESIGNADIRECTA";
	static public final String 	C_VALIDARINSCRIPCIONES = 	"VALIDARINSCRIPCIONES";
	static public final String	C_IDAREA = 					"IDAREA";
	static public final String	C_IDMATERIA = 				"IDMATERIA";
	static public final String	C_IDZONA = 					"IDZONA";
	static public final String	C_IDSUBZONA = 				"IDSUBZONA";
	static public final String	C_IDORDENADIONCOLAS = 		"IDORDENACIONCOLAS";
	static public final String	C_IDPARTIDAPRESUPUESTARIA = "IDPARTIDAPRESUPUESTARIA";
	static public final String	C_IDGRUPOFACTURACION = 		"IDGRUPOFACTURACION";	
	static public final String 	C_IDPERSONAULTIMO = 		"IDPERSONA_ULTIMO";
	static public final String  C_ACTIVARRETRICCIONACREDIT ="ACTIVARRETRICCIONACREDIT";
	
	static public final String  C_LETRADOASISTENCIAS		= "LETRADOASISTENCIAS";
	static public final String  C_LETRADOACTUACIONES		= "LETRADOACTUACIONES";
		
	/*Metodos SET*/
	/**
	 * Almacena en la Bean el identificador de la institucion "idInstitucion" 
	 * 
	 * @param valor Integer institucion. De tipo "Integer". 
	 * @return void 
	 */	
	public void setIdInstitucion 			(Integer valor) 	{ this.idInstitucion = valor;}
	/**
	 * Almacena en la Bean el identificador del turno "idTurno" 
	 * 
	 * @param valor Integer turno. De tipo "Integer". 
	 * @return void 
	 */	
	public void setIdTurno					(Integer valor)		{ this.idTurno = valor;}
	/**
	 * Almacena en la Bean el nombre del turno "nombre" 
	 * 
	 * @param valor String nombre. De tipo "String". 
	 * @return void 
	 */	
	public void setNombre					(String valor)		{ this.nombre = valor;}
	/**
	 * Almacena en la Bean el abreviatura del turno "abreviatura" 
	 * 
	 * @param valor String abreviatura. De tipo "String". 
	 * @return void 
	 */	
	public void setAbreviatura				(String valor)		{ this.abreviatura = valor;}
	/**
	 * Almacena en la Bean el descripcion del turno "descripcion" 
	 * 
	 * @param valor String descripcion. De tipo "String". 
	 * @return void 
	 */	
	public void setDescripcion				(String valor)		{ this.descripcion = valor;}
	/**
	 * Almacena en la Bean el guardias del turno "guardias" 
	 * 
	 * @param valor Integer guardias. De tipo "Integer". 
	 * @return void 
	 */	
	public void setGuardias					(Integer valor)		{ this.guardias = valor;}
	/**
	 * Almacena en la Bean el  campo validarJustificaciones del turno "validarJustificaciones" 
	 * 
	 * @param valor String guardias. De tipo "String". 
	 * @return void 
	 */	
	public void setValidarJustificaciones	(String valor)		{ this.validarJustificaciones = valor;}
	/**
	 * Almacena en la Bean el  campo designaDirecta del turno "designaDirecta" 
	 * 
	 * @param valor String designaDirecta. De tipo "String". 
	 * @return void 
	 */	
	public void setDesignaDirecta			(String valor)		{ this.designaDirecta = valor;}
	/**
	 * Almacena en la Bean el  campo validarInscripciones del turno "validarInscripciones" 
	 * 
	 * @param valor String validarInscripciones. De tipo "String". 
	 * @return void 
	 */	
	public void setValidarInscripciones		(String valor)		{ this.validarInscripciones = valor;}
	/**
	 * Almacena en la Bean el  campo idArea del turno "idArea" 
	 * 
	 * @param valor Integer idArea. De tipo "Integer". 
	 * @return void 
	 */	
	public void setIdArea					(Integer valor)		{ this.idArea = valor;}
	/**
	 * Almacena en la Bean el  campo idMateria del turno "idMateria" 
	 * 
	 * @param valor Integer idMateria. De tipo "Integer". 
	 * @return void 
	 */	
	public void setIdMateria				(Integer valor)		{ this.idMateria = valor;}
	/**
	 * Almacena en la Bean el  campo idZona del turno "idZona" 
	 * 
	 * @param valor Integer idZona. De tipo "Integer". 
	 * @return void 
	 */	
	public void setIdZona					(Integer valor)		{ this.idZona = valor;}
	/**
	 * Almacena en la Bean el  campo idSubzona del turno "idSubzona" 
	 * 
	 * @param valor Integer idSubzona. De tipo "Integer". 
	 * @return void 
	 */	
	public void setIdSubzona				(Integer valor)		{ this.idSubzona = valor;}
	/**
	 * Almacena en la Bean el  campo idOrdenacionColas del turno "idOrdenacionColas" 
	 * 
	 * @param valor Integer idOrdenacionColas. De tipo "Integer". 
	 * @return void 
	 */	
	public void setIdOrdenacionColas		(Integer valor)		{ this.idOrdenacionColas = valor;}
	/**
	 * Almacena en la Bean el  campo idPartidaPresupuestaria del turno "idPartidaPresupuestaria" 
	 * 
	 * @param valor Integer idPartidaPresupuestaria. De tipo "Integer". 
	 * @return void 
	 */	
	public void setIdPartidaPresupuestaria	(Integer valor)		{ this.idPartidaPresupuestaria = valor;}
	/**
	 * Almacena en la Bean el  campo idGrupoFacturacion del turno "idGrupoFacturacion" 
	 * 
	 * @param valor Integer idGrupoFacturacion. De tipo "Integer". 
	 * @return void 
	 */	
	public void setIdGrupoFacturacion		(Integer valor)		{ this.idGrupoFacturacion = valor;}
	/**
	 * Almacena en la Bean el  campo requisitos del turno "requisitos" 
	 * 
	 * @param valor String requisitos. De tipo "String". 
	 * @return void 
	 */	
	public void setRequisitos				(String valor)		{ this.requisitos = valor;}
	/**
	 * Almacena en la Bean el  campo idPersonaUltimo del turno "idPersonaUltimo" 
	 * 
	 * @param valor Long idPersonaUltimo. De tipo "Long". 
	 * @return void 
	 */	
	public void setIdPersonaUltimo			(Integer valor)		{ this.idPersonaUltimo = valor;}
	
	/*Metodos GET*/
	/**
	 * Recupera del Bean el valor idInstitucion.  
	 * 
	 * @return Valor idInstitucion. De tipo "Integer" 
	 */
	
	public Integer getIdInstitucion 			() 	{ return this.idInstitucion;}
	/**
	 * Recupera del Bean el valor idTurno.  
	 * 
	 * @return Valor idTurno. De tipo "Integer" 
	 */
	@AjaxXMLBuilderValueAnnotation(isCData=false)
	public Integer getIdTurno					()	{ return this.idTurno;}
	/**
	 * Recupera del Bean el valor nombre.  
	 * 
	 * @return Valor nombre. De tipo "String" 
	 */
	@AjaxXMLBuilderNameAnnotation
	public String getNombre						()	{ return this.nombre;}
	/**
	 * Recupera del Bean el valor abreviatura.  
	 * 
	 * @return Valor abreviatura. De tipo "String" 
	 */
	public String getAbreviatura				()	{ return this.abreviatura;}
	/**
	 * Recupera del Bean el valor descripcion.  
	 * 
	 * @return Valor descripcion. De tipo "String" 
	 */
	
	public String getDescripcion				()	{ return this.descripcion;}
	/**
	 * Recupera del Bean el valor guardias.  
	 * 
	 * @return Valor guardias. De tipo "Integer" 
	 */
	public Integer getGuardias					()	{ return this.guardias;}
	/**
	 * Recupera del Bean el valor validarJustificaciones.  
	 * 
	 * @return Valor validarJustificaciones. De tipo "String" 
	 */
	public String getValidarJustificaciones		()	{ return this.validarJustificaciones;}
	/**
	 * Recupera del Bean el valor designaDirecta.  
	 * 
	 * @return Valor designaDirecta. De tipo "String" 
	 */
	public String getDesignaDirecta				()	{ return this.designaDirecta;}
	/**
	 * Recupera del Bean el valor validarInscripciones.  
	 * 
	 * @return Valor validarInscripciones. De tipo "String" 
	 */
	public String getValidarInscripciones		()	{ return this.validarInscripciones;}
	/**
	 * Recupera del Bean el valor idArea.  
	 * 
	 * @return Valor idArea. De tipo "Integer" 
	 */
	public Integer getIdArea					()	{ return this.idArea;}
	/**
	 * Recupera del Bean el valor idMateria.  
	 * 
	 * @return Valor idMateria. De tipo "Integer" 
	 */
	public Integer getIdMateria					()	{ return this.idMateria;}
	/**
	 * Recupera del Bean el valor idZona.  
	 * 
	 * @return Valor idZona. De tipo "Integer" 
	 */
	public Integer getIdZona					()	{ return this.idZona;}
	/**
	 * Recupera del Bean el valor idSubzona.  
	 * 
	 * @return Valor idSubzona. De tipo "Integer" 
	 */
	public Integer getIdSubzona					()	{ return this.idSubzona;}
	/**
	 * Recupera del Bean el valor idOrdenacionColas.  
	 * 
	 * @return Valor idOrdenacionColas. De tipo "Integer" 
	 */
	public Integer getIdOrdenacionColas			()	{ return this.idOrdenacionColas;}
	/**
	 * Recupera del Bean el valor idPartidaPresupuestaria.  
	 * 
	 * @return Valor idPartidaPresupuestaria. De tipo "Integer" 
	 */
	public Integer getIdPartidaPresupuestaria	()	{ return this.idPartidaPresupuestaria;}
	/**
	 * Recupera del Bean el valor idGrupoFacturacion.  
	 * 
	 * @return Valor idGrupoFacturacion. De tipo "Integer" 
	 */
	public Integer getIdGrupoFacturacion		()	{ return this.idGrupoFacturacion;}
	/**
	 * Recupera del Bean el valor requisitos.  
	 * 
	 * @return Valor requisitos. De tipo "String" 
	 */
	public String getRequisitos					()	{ return this.requisitos;}
	/**
	 * Recupera del Bean el valor idPersonaUltimo.  
	 * 
	 * @return Valor idPersonaUltimo. De tipo "Long" 
	 */
	public Integer getIdPersonaUltimo			()	{ return this.idPersonaUltimo;}
	
	
	public String getActivarRestriccionAcreditacion() {
		return activarRestriccionAcreditacion;
	}
	public void setActivarRestriccionAcreditacion(String activarRestriccionAcreditacion) {
		this.activarRestriccionAcreditacion = activarRestriccionAcreditacion;
	}
	
	public String getLetradoActuaciones() {
		return letradoActuaciones;
	}
	public void setLetradoActuaciones(String letradoActuaciones) {
		this.letradoActuaciones = letradoActuaciones;
	}
	public String getLetradoAsistencias() {
		return letradoAsistencias;
	}
	public void setLetradoAsistencias(String letradoAsistencias) {
		this.letradoAsistencias = letradoAsistencias;
	}
}