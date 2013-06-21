package com.siga.beans;

import com.siga.Utilidades.AjaxXMLBuilderAnnotation;
import com.siga.Utilidades.AjaxXMLBuilderNameAnnotation;
import com.siga.Utilidades.AjaxXMLBuilderValueAnnotation;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_GUARDIASTURNO
 * 
 * @author ruben.fernandez
 * @since 6/12/2004
 */
@AjaxXMLBuilderAnnotation
public class ScsGuardiasTurnoBean extends MasterBean
{
	//////////////////// ATRIBUTOS DE CLASE ////////////////////
	/** Nombre de Tabla */
	static public String T_NOMBRETABLA = "SCS_GUARDIASTURNO";
	
	//Nombres de campos de la tabla
	static public final String C_IDINSTITUCION				= "IDINSTITUCION";
	static public final String C_IDTURNO					= "IDTURNO";
	static public final String C_IDGUARDIA					= "IDGUARDIA";
	static public final String C_NOMBRE						= "NOMBRE";
	static public final String C_NUMEROLETRADOSGUARDIA		= "NUMEROLETRADOSGUARDIA";
	static public final String C_NUMEROSUSTITUTOSGUARDIA	= "NUMEROSUSTITUTOSGUARDIA";	
	static public final String C_DIASGUARDIA				= "DIASGUARDIA";
	static public final String C_DIASPAGADOS				= "DIASPAGADOS";
	static public final String C_VALIDARJUSTIFICACIONES		= "VALIDARJUSTIFICACIONES";
	static public final String C_DIASSEPARACIONGUARDIAS		= "DIASSEPARACIONGUARDIAS";
	static public final String C_IDORDENACIONCOLAS			= "IDORDENACIONCOLAS";
	static public final String C_NUMEROASISTENCIAS			= "NUMEROASISTENCIAS";
	static public final String C_DESCRIPCION				= "DESCRIPCION";
	static public final String C_DESCRIPCIONFACTURACION		= "DESCRIPCIONFACTURACION";
	static public final String C_DESCRIPCIONPAGO			= "DESCRIPCIONPAGO";
	static public final String C_IDPARTIDAPRESUPUESTARIA	= "IDPARTIDAPRESUPUESTARIA";	
	static public final String C_NUMEROACTUACIONES			= "NUMEROACTUACIONES";
	static public final String C_IDPERSONA_ULTIMO			= "IDPERSONA_ULTIMO";
	static public final String C_IDGRUPOGUARDIACOLEGIADO_ULTIMO		= "IDGRUPOGUARDIA_ULTIMO";
	static public final String C_FECHASUSCRIPCION_ULTIMO	= "FECHASUSCRIPCION_ULTIMO";	
	static public final String C_DIASPERIODO				= "DIASPERIODO";
	static public final String C_TIPODIASPERIODO			= "TIPODIASPERIODO";
	static public final String C_FESTIVOS					= "FESTIVOS";
	static public final String C_SELECCIONLABORABLES		= "SELECCIONLABORABLES";
	static public final String C_SELECCIONFESTIVOS			= "SELECCIONFESTIVOS";
	static public final String C_IDGUARDIASUSTITUCION		= "IDGUARDIASUSTITUCION";
	static public final String C_IDTURNOSUSTITUCION			= "IDTURNOSUSTITUCION";	
	static public final String C_PORGRUPOS					= "PORGRUPOS";
	static public final String C_ROTARCOMPONENTES			= "ROTARCOMPONENTES";
	static public final String C_IDINSTITUCIONPRINCIPAL			= "IDINSTITUCIONPRINCIPAL";
	static public final String C_IDTURNOPRINCIPAL			= "IDTURNOPRINCIPAL";
	static public final String C_IDGUARDIAPRINCIPAL			= "IDGUARDIAPRINCIPAL";
	static public final String C_TIPODIASGUARDIA			= "TIPODIASGUARDIA";
	static public final String C_IDTIPOGUARDIA				= "IDTIPOGUARDIA";
	
	
	//////////////////// ATRIBUTOS ////////////////////
	private Integer 	idInstitucion;
	private Integer 	idTurno;
	private Integer 	idGuardia;
	private String		nombre;
	private Integer		numeroLetradosGuardia;
	private Integer		numeroSustitutosGuardia;
	private Integer 	diasGuardia;
	private Integer 	diasPagados;
	private String		validarJustificaciones;
	private Integer 	diasSeparacionGuardia;
	private Integer		idOrdenacionColas;
	private Integer 	numeroAsistencias;
	private String		descripcion;	
	private String		descripcionFacturacion;	
	private String		descripcionPago;
	private Integer		idPartidaPresupuestaria;
	private Integer 	numeroActuaciones;
	private String		designaDirecta;
	private Long 		idPersona_Ultimo;
	private Long 		idGrupoGuardiaColegiado_Ultimo;
	private String		fechaSuscripcion_Ultimo;
	private String 		tipodiasGuardia;
	private Integer		diasPeriodo;
	private String		tipoDiasPeriodo;
	private String		festivos;
	private String		seleccionLaborables;
	private String		seleccionFestivos;
	private String		porGrupos;
	private String		rotarComponentes;
	
	private Integer		idGuardiaSustitucion;
	private Integer		idTurnoSustitucion;
	private String		seleccionTiposDia;
	private String		descripcionTipoDiasGuardia;
	
	private ScsTurnoBean turno;
	private Integer 	idInstitucionPrincipal;
	private Integer 	idTurnoPrincipal;
	private Integer 	idGuardiaPrincipal;
	private Integer 	idTipoGuardiaSeleccionado;
	
	
	//////////////////// GETTERS ////////////////////
	public String getDescripcion() {return descripcion;}
	public String getDescripcionFacturacion() {return descripcionFacturacion;}
	public String getDescripcionPago() {return descripcionPago;}
	public String getDesignaDirecta() {return designaDirecta;}
	public Integer getDiasGuardia() {return diasGuardia;}
	public Integer getDiasPagados() {return diasPagados;}
	public Integer getDiasPeriodo() {return diasPeriodo;}
	public Integer getDiasSeparacionGuardia() {return diasSeparacionGuardia;}
	@AjaxXMLBuilderValueAnnotation(isCData=false)
	public Integer getIdGuardia() {return idGuardia;}
	public Integer getIdInstitucion() {return idInstitucion;}
	public Integer getIdOrdenacionColas() {return idOrdenacionColas;}
	public Integer getIdPartidaPresupuestaria() {return idPartidaPresupuestaria;}
	public Long getIdPersona_Ultimo() {return idPersona_Ultimo;}
	public String getFechaSuscripcion_Ultimo() {return fechaSuscripcion_Ultimo;}
	public Integer getIdTurno() {return idTurno;}
	@AjaxXMLBuilderNameAnnotation
	public String getNombre() {return nombre;}
	public Integer getNumeroActuaciones() {return numeroActuaciones;}
	public Integer getNumeroAsistencias() {return numeroAsistencias;}
	public Integer getNumeroLetradosGuardia() {return numeroLetradosGuardia;}
	public Integer getNumeroSustitutosGuardia() {return numeroSustitutosGuardia;}
	public String getTipodiasGuardia() {return tipodiasGuardia;}
	public String getTipoDiasPeriodo() {return tipoDiasPeriodo;}
	public String getValidarJustificaciones() {return validarJustificaciones;}
	public String getFestivos() {return festivos;}
	public String getSeleccionLaborables () {return seleccionLaborables;}
	public String getSeleccionFestivos () {return seleccionFestivos;}
	public Integer getIdTipoGuardiaSeleccionado() {return idTipoGuardiaSeleccionado;}
	public String getPorGrupos () {return porGrupos;}
	public String getRotarComponentes () {return rotarComponentes;}
	public ScsTurnoBean getTurno() {return turno;}
	
	public Integer getIdGuardiaSustitucion() {return idGuardiaSustitucion;}
	public Integer getIdTurnoSustitucion() {return idTurnoSustitucion;}
	public String getSeleccionTiposDia() {return seleccionTiposDia;}
	public String getDescripcionTipoDiasGuardia() {return descripcionTipoDiasGuardia;}
	
	public Integer getIdInstitucionPrincipal() {return idInstitucionPrincipal;}
	public Integer getIdTurnoPrincipal() {return idTurnoPrincipal;}
	public Integer getIdGuardiaPrincipal() {return idGuardiaPrincipal;}
	public Long getIdGrupoGuardiaColegiado_Ultimo() {return idGrupoGuardiaColegiado_Ultimo;}

	
	//////////////////// SETTERS ////////////////////
	public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
	public void setDescripcionFacturacion(String descripcionFacturacion) {this.descripcionFacturacion = descripcionFacturacion;}
	public void setDescripcionPago(String descripcionPago) {this.descripcionPago = descripcionPago;}
	public void setDesignaDirecta(String designaDirecta) {this.designaDirecta = designaDirecta;}
	public void setDiasGuardia(Integer diasGuardia) {this.diasGuardia = diasGuardia;}
	public void setDiasPagados(Integer diasPagados) {this.diasPagados = diasPagados;}
	public void setDiasPeriodo(Integer diasPeriodo) {this.diasPeriodo = diasPeriodo;}
	public void setDiasSeparacionGuardia(Integer diasSeparacionGuardia) {this.diasSeparacionGuardia = diasSeparacionGuardia;}
	public void setIdGuardia(Integer idGuardia) {this.idGuardia = idGuardia;}
	public void setIdInstitucion(Integer idInstitucion) {this.idInstitucion = idInstitucion;}
	public void setIdOrdenacionColas(Integer idOrdenacionColas) {this.idOrdenacionColas = idOrdenacionColas;}
	public void setIdPartidaPresupuestaria(Integer idPartidaPresupuestaria) {this.idPartidaPresupuestaria = idPartidaPresupuestaria;}
	public void setIdPersona_Ultimo(Long idPersona_Ultimo) {this.idPersona_Ultimo = idPersona_Ultimo;}
	public void setFechaSuscripcion_Ultimo(String fechaSuscripcionUltimo){fechaSuscripcion_Ultimo = fechaSuscripcionUltimo;}
	public void setIdTurno(Integer idTurno) {this.idTurno = idTurno;}
	public void setNombre(String nombre) {this.nombre = nombre;}
	public void setNumeroActuaciones(Integer numeroActuaciones) {this.numeroActuaciones = numeroActuaciones;}
	public void setNumeroAsistencias(Integer numeroAsistencias) {this.numeroAsistencias = numeroAsistencias;}
	public void setNumeroLetradosGuardia(Integer numeroLetradosGuardia) {this.numeroLetradosGuardia = numeroLetradosGuardia;}
	public void setNumeroSustitutosGuardia(Integer numeroSustitutosGuardia) {this.numeroSustitutosGuardia = numeroSustitutosGuardia;}
	public void setTipodiasGuardia(String tipodiasGuardia) {this.tipodiasGuardia = tipodiasGuardia;}
	public void setTipoDiasPeriodo(String tipoDiasPeriodo) {this.tipoDiasPeriodo = tipoDiasPeriodo;}
	public void setValidarJustificaciones(String validarJustificaciones) {this.validarJustificaciones = validarJustificaciones;}
	public void setFestivos(String festivos) {this.festivos = festivos;}
	public void setSeleccionLaborables (String valor) {this.seleccionLaborables = valor;}
	public void setSeleccionFestivos (String valor) {this.seleccionFestivos = valor;}
	public void setIdTipoGuardiaSeleccionado(Integer valor) {this.idTipoGuardiaSeleccionado = valor;}
	public void setPorGrupos (String valor) {this.porGrupos = valor;}
	public void setRotarComponentes (String valor) {this.rotarComponentes = valor;}
	public void setTurno(ScsTurnoBean turno) {this.turno = turno;}
	
	public void setIdGuardiaSustitucion(Integer idGuardiaSustitucion) {this.idGuardiaSustitucion = idGuardiaSustitucion;}
	public void setIdTurnoSustitucion(Integer idTurnoSustitucion) {this.idTurnoSustitucion = idTurnoSustitucion;}
	public void setSeleccionTiposDia(String seleccionTiposDia) {this.seleccionTiposDia = seleccionTiposDia;}
	public void setDescripcionTipoDiasGuardia(String descripcionTipoDiasGuardia) {this.descripcionTipoDiasGuardia = descripcionTipoDiasGuardia;}
	
	public void setIdInstitucionPrincipal(Integer idInstitucionPrincipal) {this.idInstitucionPrincipal = idInstitucionPrincipal;}
	public void setIdTurnoPrincipal(Integer idTurnoPrincipal) {this.idTurnoPrincipal = idTurnoPrincipal;}
	public void setIdGuardiaPrincipal(Integer idGuardiaPrincipal) {this.idGuardiaPrincipal = idGuardiaPrincipal;}
	public void setIdGrupoGuardiaColegiado_Ultimo(Long idGrupoGuardiaUltimo) {this.idGrupoGuardiaColegiado_Ultimo = idGrupoGuardiaUltimo;
	}
}