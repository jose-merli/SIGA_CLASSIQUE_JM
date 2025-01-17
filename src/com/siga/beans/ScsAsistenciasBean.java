package com.siga.beans;

import com.siga.gratuita.form.AsistenciaForm;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_ASISTENCIAS
 * 
 * @author carlos.vidal
 * @since 20/1/2005
 */

public class ScsAsistenciasBean extends MasterBean{
	
	private static final long serialVersionUID = 196085411981959095L;
	
	// Variables de campos
	private Integer	idInstitucion;
	private Integer	anio;
	private Integer numero;
	private String	fechaHora;
	private String	observaciones;
	private String	incidencias;
	private String	fechaAnulacion;
	private String	motivosAnulacion;
	private String	delitosImputados;
	private String	contrarios;
	private String	datosDefensaJuridica;
	private String	fechaCierre;
	private Integer idTipoAsistencia;
	private Integer idTipoAsistenciaColegio;
	private Integer idTurno;
	private Integer idGuardia;
	private Long idPersonaColegiado;
	private Integer idPersonaJG;
	private Integer idFacturacion;
	private String	fechaModificacion;
	private String	usuModificacion;
	private Integer designaAnio;
	private Integer designaNumero;
	private Integer designaTurno;
	private Integer ejgidtipoejg;
	private Integer ejganio;
	private Long ejgnumero;
	private Integer facturado;
	private Double pagado;
	private String numeroDiligencia;
	private String numeroProcedimiento;
	private Long comisaria;
	private Integer comisariaIdInstitucion;
	private Long juzgado;
	private Integer juzgadoIdInstitucion;
	private Integer idEstadoAsistencia;
	private String  fechaEstadoAsistencia;
	private String NIG;
	private Integer idPretension;
	private String	fechaSolicitud;
	private Short idOrigenAsistencia;
	
	// Variables auxiliares
	private String hora;
	private String minuto;
	private String asistidoNif;
	private String asistidoNombre;
	private String asistidoApellido1;
	private String asistidoApellido2;
	private String sexo;      // necesario para los colegios Andaluces 18/02/2016
	private Integer idDelito;
	private String ejgNumEjg;
	private String	fechaGuardia;
	private Long idPersonaRepresentante;
	ScsTurnoBean turno ;
	ScsGuardiasTurnoBean guardia;
	ScsPersonaJGBean asistido;
	CenPersonaBean personaColegiado;
	ScsDesignaBean designa;
	ScsEJGBean ejg;
	AsistenciaForm asistenciaForm;
	private String fichaColegial;
	private Integer idMovimiento;

	// Nombre de Tabla
	static public String T_NOMBRETABLA = "SCS_ASISTENCIA";
	
	// Nombre de campos de la tabla
	static public final String	C_IDINSTITUCION          	=   "IDINSTITUCION";
	static public final String	C_ANIO                   	=	"ANIO";
	static public final String	C_NUMERO                	=	"NUMERO";                			
	static public final String	C_FECHAHORA              	=	"FECHAHORA";
	static public final String	C_OBSERVACIONES          	=	"OBSERVACIONES";
	static public final String	C_INCIDENCIAS            	=	"INCIDENCIAS";
	static public final String	C_FECHAANULACION         	=	"FECHAANULACION";
	static public final String	C_MOTIVOSANULACION       	=	"MOTIVOSANULACION";
	static public final String	C_DELITOSIMPUTADOS       	=	"DELITOSIMPUTADOS";
	static public final String	C_CONTRARIOS             	=	"CONTRARIOS";
	static public final String	C_DATOSDEFENSAJURIDICA   	=	"DATOSDEFENSAJURIDICA";
	static public final String	C_FECHACIERRE            	=	"FECHACIERRE";	
	static public final String	C_IDTIPOASISTENCIA       	=	"IDTIPOASISTENCIA";
	static public final String	C_IDTIPOASISTENCIACOLEGIO	=	"IDTIPOASISTENCIACOLEGIO";
	static public final String	C_IDTURNO                	=	"IDTURNO";	
	static public final String	C_IDGUARDIA              	=	"IDGUARDIA";
	static public final String	C_IDPERSONACOLEGIADO     	=	"IDPERSONACOLEGIADO";
	static public final String	C_IDPERSONAJG            	=	"IDPERSONAJG";
	static public final String	C_FECHAMODIFICACION      	=	"FECHAMODIFICACION";
	static public final String	C_USUMODIFICACION        	=	"USUMODIFICACION";
	static public final String	C_DESIGNA_ANIO        		=	"DESIGNA_ANIO";
	static public final String	C_DESIGNA_NUMERO       		=	"DESIGNA_NUMERO";
	static public final String	C_DESIGNA_TURNO       		=	"DESIGNA_TURNO";
	static public final String	C_FACTURADO = "FACTURADO";
	static public final String	C_PAGADO = "PAGADO";
	static public final String	C_IDFACTURACION = "IDFACTURACION";
	static public final String	C_JUZGADO                 = "JUZGADO";
	static public final String	C_COMISARIA               = "COMISARIA";
	static public final String	C_NUMERODILIGENCIA        = "NUMERODILIGENCIA";
	static public final String	C_NUMEROPROCEDIMIENTO     = "NUMEROPROCEDIMIENTO";
	static public final String	C_COMISARIA_IDINSTITUCION = "COMISARIAIDINSTITUCION";
	static public final String	C_JUZGADO_IDINSTITUCION   = "JUZGADOIDINSTITUCION";
	static public final String	C_IDESTADOASISTENCIA      = "IDESTADOASISTENCIA";
	static public final String	C_FECHAESTADOASISTENCIA   = "FECHAESTADOASISTENCIA";
	static public final String	C_EJGIDTIPOEJG       	=	"EJGIDTIPOEJG";
	static public final String	C_EJGANIO       		=	"EJGANIO";
	static public final String	C_EJGNUMERO       		=	"EJGNUMERO";
	static public final String  C_NIG   				= 	"NIG";	
	static public final String  C_IDPRETENSION   		= 	"IDPRETENSION";
	static public final String	C_FECHASOLICITUD       	=	"FECHASOLICITUD";
	static public final String	C_IDORIGENASISTENCIA       	=	"IDORIGENASISTENCIA";
	static public final String	C_SEXO       	=	"SEXO";
	static public final String	C_IDMOVIMIENTO       	=	"IDMOVIMIENTO";
	
	static public final String	C_IDPERSONA_REPRESENTANTE	=	"IDPERSONA_REPRESENTANTE";

	// Metodos SET
	public void    setIdInstitucion          (Integer valor)	{ this.idInstitucion           = 	valor;}
	public void    setAnio                   (Integer valor)	{ this.anio                    = 	valor;}
	public void    setNumero                 (Integer valor)	{ this.numero                  = 	valor;}
	public void    setFechaHora              (String  valor)	{ this.fechaHora               = 	valor;}
	public void    setObservaciones          (String  valor)	{ this.observaciones           = 	valor;}
	public void    setIncidencias            (String  valor)	{ this.incidencias             = 	valor;}
	public void    setFechaAnulacion         (String  valor)	{ this.fechaAnulacion          = 	valor;}
	public void    setMotivosAnulacion       (String  valor)	{ this.motivosAnulacion        = 	valor;}
	public void    setDelitosImputados       (String  valor)	{ this.delitosImputados        = 	valor;}
	public void    setContrarios             (String  valor)	{ this.contrarios              = 	valor;}
	public void    setDatosDefensaJuridica   (String  valor)	{ this.datosDefensaJuridica    = 	valor;}
	public void    setFechaCierre            (String  valor)	{ this.fechaCierre             = 	valor;}
	public void    setIdTipoAsistencia       (Integer valor)	{ this.idTipoAsistencia        = 	valor;}
	public void    setIdTipoAsistenciaColegio(Integer valor)	{ this.idTipoAsistenciaColegio = 	valor;}
	public void    setIdturno                (Integer valor)	{ this.idTurno                 = 	valor;}
	public void    setIdguardia              (Integer valor)	{ this.idGuardia               = 	valor;}
	public void    setIdPersonaColegiado     (Long valor)	{ this.idPersonaColegiado      = 	valor;}
	public void    setIdPersonaJG            (Integer valor)	{ this.idPersonaJG             = 	valor;}
	public void    setFechaModificacion      (String  valor)	{ this.fechaModificacion       = 	valor;}
	public void    setUsuModificacion        (String  valor)	{ this.usuModificacion         = 	valor;}
	public void    setIdPersonaRepresentante (Long valor)	{ this.idPersonaRepresentante  = 	valor;}
	public void    setDesignaAnio		     (Integer valor)	{ this.designaAnio		       = 	valor;}
	public void    setDesignaNumero		     (Integer valor)	{ this.designaNumero      	   = 	valor;}
	public void    setEjgIdTipoEjg		     (Integer valor)	{ this.ejgidtipoejg      	   = 	valor;}
	public void    setEjgAnio		     	 (Integer valor)	{ this.ejganio      	   = 	valor;}
	public void    setEjgNumero		     	 (Long valor)	{ this.ejgnumero      	   = 	valor;}
	
	// Metodos GET
	public Integer getIdInstitucion          ()	{ return idInstitucion           ;}
	public Integer getAnio                   ()	{ return anio                    ;}
	public Integer getNumero                 ()	{ return numero                  ;}
	public String  getFechaHora              ()	{ return fechaHora               ;}
	public String  getObservaciones          ()	{ return observaciones           ;}
	public String  getIncidencias            ()	{ return incidencias             ;}
	public String  getFechaAnulacion         ()	{ return fechaAnulacion          ;}
	public String  getMotivosAnulacion       ()	{ return motivosAnulacion        ;}
	public String  getDelitosImputados       ()	{ return delitosImputados        ;}
	public String  getContrarios             ()	{ return contrarios              ;}
	public String  getDatosDefensaJuridica   ()	{ return datosDefensaJuridica    ;}
	public String  getFechaCierre            ()	{ return fechaCierre             ;}
	public Integer getIdTipoAsistencia       ()	{ return idTipoAsistencia        ;}
	public Integer getIdTipoAsistenciaColegio()	{ return idTipoAsistenciaColegio ;}
	public Integer getIdTurno                ()	{ return idTurno                 ;}
	public Integer getIdGuardia              ()	{ return idGuardia               ;}
	public Long getIdPersonaColegiado     ()	{ return idPersonaColegiado      ;}
	public Integer getIdPersonaJG            ()	{ return idPersonaJG             ;}
	public String  getFechaModificacion      ()	{ return fechaModificacion       ;}
	public String  getUsuModificacion        ()	{ return usuModificacion         ;}   
	public Long getIdPersonaRepresentante ()	{ return idPersonaRepresentante  ;}
	public Integer getDesignaAnio		     ()	{ return designaAnio		     ;}
	public Integer getDesignaNumero		     ()	{ return designaNumero      	 ;}
	public Integer getEjgIdTipoEjg		     ()	{ return this.ejgidtipoejg      	 ;}
	public Integer getEjgAnio			     ()	{ return this.ejganio      	 ;}
	public Long getEjgNumero			     ()	{ return this.ejgnumero      	 ;}
	
	// Mas Metodos SET y GET
	public Integer getFacturado() {
		return facturado;
	}
	public void setFacturado(Integer facturado) {
		this.facturado = facturado;
	}
	public String getFechaGuardia() {
		return fechaGuardia;
	}
	public void setFechaGuardia(String fechaGuardia) {
		this.fechaGuardia = fechaGuardia;
	}
	public Integer getIdFacturacion() {
		return idFacturacion;
	}
	public void setIdFacturacion(Integer idFacturacion) {
		this.idFacturacion = idFacturacion;
	}
	public Double getPagado() {
		return pagado;
	}
	public void setPagado(Double pagado) {
		this.pagado = pagado;
	}
	public Integer getDesignaTurno() {
		return designaTurno;
	}
	public void setDesignaTurno(Integer designaIdTurno) {
		this.designaTurno = designaIdTurno;
	}
	public Long getComisaria() {
		return comisaria;
	}
	public void setComisaria(Long comisaria) {
		this.comisaria = comisaria;
	}
	public Long getJuzgado() {
		return juzgado;
	}
	public void setJuzgado(Long juzgado) {
		this.juzgado = juzgado;
	}
	public String getNumeroDiligencia() {
		return numeroDiligencia;
	}
	public void setNumeroDiligencia(String numeroDiligencia) {
		this.numeroDiligencia = numeroDiligencia;
	}
	public String getNumeroProcedimiento() {
		return numeroProcedimiento;
	}
	public void setNumeroProcedimiento(String numeroProcedimiento) {
		this.numeroProcedimiento = numeroProcedimiento;
	}
	public Integer getComisariaIdInstitucion() {
		return comisariaIdInstitucion;
	}
	public void setComisariaIdInstitucion(Integer comisariaIdInstitucion) {
		this.comisariaIdInstitucion = comisariaIdInstitucion;
	}
	public Integer getJuzgadoIdInstitucion() {
		return juzgadoIdInstitucion;
	}
	public void setJuzgadoIdInstitucion(Integer juzgadoIdInstitucion) {
		this.juzgadoIdInstitucion = juzgadoIdInstitucion;
	}
	public Integer getIdEstadoAsistencia() {
		return idEstadoAsistencia;
	}
	public void setIdEstadoAsistencia(Integer idEstadoAsistencia) {
		this.idEstadoAsistencia = idEstadoAsistencia;
	}
	public String getFechaEstadoAsistencia() {
		return fechaEstadoAsistencia;
	}
	public void setFechaEstadoAsistencia(String fechaEstadoAsistencia) {
		this.fechaEstadoAsistencia = fechaEstadoAsistencia;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	public String getMinuto() {
		return minuto;
	}
	public void setMinuto(String minuto) {
		this.minuto = minuto;
	}
	public String getAsistidoNombre() {
		return asistidoNombre;
	}
	public void setAsistidoNombre(String asistidoNombre) {
		this.asistidoNombre = asistidoNombre;
	}
	public String getAsistidoApellido1() {
		return asistidoApellido1;
	}
	public void setAsistidoApellido1(String asistidoApellido1) {
		this.asistidoApellido1 = asistidoApellido1;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getAsistidoApellido2() {
		return asistidoApellido2;
	}
	public void setAsistidoApellido2(String asistidoApellido2) {
		this.asistidoApellido2 = asistidoApellido2;
	}
	public void setIdDelito(Integer idDelito) {
		this.idDelito = idDelito;
	}
	public Integer getIdDelito() {
		return idDelito;
	}
	public String getAsistidoNif() {
		return asistidoNif;
	}
	public void setAsistidoNif(String asistidoNif) {
		this.asistidoNif = asistidoNif;
	}
	public String getEjgNumEjg() {
		return ejgNumEjg;
	}
	public void setEjgNumEjg(String ejgNumEjg) {
		this.ejgNumEjg = ejgNumEjg;
	}
	public ScsTurnoBean getTurno() {
		return turno;
	}
	public void setTurno(ScsTurnoBean turno) {
		this.turno = turno;
	}
	public ScsGuardiasTurnoBean getGuardia() {
		return guardia;
	}
	public void setGuardia(ScsGuardiasTurnoBean guardia) {
		this.guardia = guardia;
	}
	public ScsPersonaJGBean getAsistido() {
		return asistido;
	}
	public void setAsistido(ScsPersonaJGBean asistido) {
		this.asistido = asistido;
	}
	public CenPersonaBean getPersonaColegiado() {
		return personaColegiado;
	}
	public void setPersonaColegiado(CenPersonaBean personaColegiado) {
		this.personaColegiado = personaColegiado;
	}
	public ScsDesignaBean getDesigna() {
		return designa;
	}
	public void setDesigna(ScsDesignaBean designa) {
		this.designa = designa;
	}
	public ScsEJGBean getEjg() {
		return ejg;
	}
	public void setEjg(ScsEJGBean ejg) {
		this.ejg = ejg;
	}
	public String getFichaColegial() {
		return fichaColegial;
	}
	public void setFichaColegial(String fichaColegial) {
		this.fichaColegial = fichaColegial;
	}
	public String getNIG() {
		return NIG;
	}
	public void setNIG(String nIG) {
		NIG = nIG;
	}
	public Integer getIdPretension() {
		return idPretension;
	}
	public Short getIdOrigenAsistencia() {
		return idOrigenAsistencia;
	}
	public void setIdOrigenAsistencia(Short idOrigenAsistencia) {
		this.idOrigenAsistencia = idOrigenAsistencia;
	}
	public void setIdPretension(Integer idPretension) {
		this.idPretension = idPretension;
	}
	public String getFechaSolicitud() {
		return fechaSolicitud;
	}
	public void setFechaSolicitud(String fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}
	
	public AsistenciaForm getAsistenciaForm(){
		if(asistenciaForm==null)
			asistenciaForm = new AsistenciaForm();
		if(idInstitucion!=null && !idInstitucion.equals(""))
			asistenciaForm.setIdInstitucion(idInstitucion.toString());
		if(anio!=null&& !anio.equals(""))
			asistenciaForm.setAnio(anio.toString());
		if(numero!=null&& !numero.equals(""))
			asistenciaForm.setNumero(numero.toString());
		if(idTurno!=null && !idTurno.equals(""))
			asistenciaForm.setIdTurno(idTurno.toString());
		if(idTipoAsistenciaColegio!=null && !idTipoAsistenciaColegio.equals(""))
			asistenciaForm.setIdTipoAsistenciaColegio(idTipoAsistenciaColegio.toString());
		
		asistenciaForm.setFichaColegial(fichaColegial);
		
		
		return asistenciaForm;
		
	}
	
	public Integer getIdMovimiento() {
		return idMovimiento;
	}
	public void setIdMovimiento(Integer idMovimiento) {
		this.idMovimiento = idMovimiento;
	}
	
	
}