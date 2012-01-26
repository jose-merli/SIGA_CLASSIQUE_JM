package com.siga.gratuita.form;

/**
 * @author carlos.vidal
 */
import com.siga.beans.ScsAsistenciasBean;
import com.siga.general.MasterForm;

public class AsistenciasForm extends MasterForm 
{
	String 	numeroDilegencia = "", 
			numeroProcedimiento = "", 
			estadoAsintecia = "",
			fechaEstadoAsistencia = "",
			estadoAsinteciaAnterior = "", 
			ejg_anio= "", 
			ejg_numero = "", 
			ejg_idTipoEJG = "", 
			ejg_idInstitucion = "",
			designa_anio= "", 
			designa_numero = "", 
			designa_turno = "", 
			designa_idInstitucion = "",
			nig="",
			idPretension="";
	String numeroDilegenciaAsistencia; 
	String numeroProcedimientoAsistencia;
	String comisariaAsistencia;
	String juzgadoAsistencia;
	
	
	private String modoPestanha=null;
	
	/**
	 * @return Returns the modoPestanha.
	 */
	public String getModoPestanha() {
		return modoPestanha;
	}
	/**
	 * @param modoPestanha The modoPestanha to set.
	 */
	public void setModoPestanha(String modoPestanha) {
		this.modoPestanha = modoPestanha;
	}
	
	private String asunto="asunto";
	private String idInstitucion;
	private String anio="anio";
	private String numero="numero";
	private String idTipoEJG="idTipoEJG";
	private String fechaHora="fechaHora";
	private String observaciones="observaciones";
	private String incidencias="incidencias";
	private String fechaPasoAvenia="fechaPasoAvenia";
	private String pasoAvenia="pasoAvenia";
	private String fechaAnulacion="fechaAnulacion";
	private String motivosAnulacion="motivosAnulacion";
	private String delitosImputados="delitosImputados";
	private String contrarios="contrarios";
	private String datosDefensaJuridica="datosDefensaJuridica";
	private String fechaCierre="fechaCierre";
	private String idTipoAsistencia="idTipoAsistencia";
	private String idTipoAsistenciaColegio="idTipoAsistenciaColegio";
	private String idTurno="idTurno";
	private String numeroColegiado="numeroColegiado";
	private String nombreColegiado="nombreColegiado";
	private String idGuardia="idGuardia";
	private String idCalendarioGuardias="idCalendarioGuardias";
	private String idPersonaColegiado="idPersonaColegiado";
	private String idPersonaJG="idPersonaJG";
	private String idFacturacion="idFacturacion";
	private String fechaGuardia="fechaGuardia";
	private String fechaModificacion="fechaModificacion";
	private String usuModificacion="usuModificacion";
	private String estado="estado";
	
	private String actuacionesPendientes="actuacionesPendientes";
	// Datos auxiliares
	private String fechaDesde="fechaDesde";
	private String fechaHasta="fechaHasta";
	private String colegiado="colegiado";
	private String nif="nif";
	private String nombre="nombre";
	private String apellido1="apellido1";
	private String apellido2="apellido2";
	private String datosModificables="datosModificables";
	private String idPersona="idPersona";
	private String direccion="direccion";
	private String codigoPostal="codigoPostal";
	private String regimenConyugal="regimenConyugal";
	private String idPais="idPais";
	private String idProvincia="idProvincia";
	private String idPoblacion="idPoblacion";
	private String idPaisAux="idPaisAux";
	private String idProvinciaAux="idProvinciaAux";
	private String idPoblacionAux="idPoblacionAux";
	private String idEstadoCivilAux="idEstadoCivilAux";
	private String regimenConyugalAux="regimenConyugalAux";
	private String idEstadoCivil="idEstadoCivil";
	private String fechaNacimiento="fechaNacimiento";
	private String modoActual="modoActual";
	private String actionActivo="actionActivo";
	private String titulo="titulo";
	private String localizacion="localizacion";
	
	// Relacioandos con la creacion de cartas a los interesados MAV 18-V-05
	private String cabeceraCarta="cabeceraCarta";
	private String motivoCarta="motivoCarta";
	private String pieCarta="pieCarta";
	
	// Actuaciones
	private String aanio            ="aanio";
	private String anumero          ="anumero";
	private String pjgapellido2     ="pjgapellido2";   
	private String pjgapellido1     ="pjgapellido1";   
	private String pjgnif           ="pjgnif";  
	private String pjgnombre        ="pjgnombre";
	private String gtnombre         ="gtnombre";
	private String tnombre          ="tnombre";
	private String cncolegiado      ="cncolegiado";
	private String pnombre          ="pnombre";
	private String papellidos2      ="papellidos2";
	private String papellidos1      ="papellidos1";
	private String acnumero         ="acnumero";
	private String acfecha          ="acfecha";
	private String aclugar          ="aclugar";
	private String acanulacion      ="acanulacion";
	private String acaextrajudicial ="acaextrajudicial";
	private String acfjustificacion ="acfjustificacion";
	private String acidactuacion    ="acidactuacion";
	private String acdbreve         ="acdbreve";
	private String acobservaciones  ="acobservaciones";
	private String acdiadespues     ="acdiadespues";
	private String acnumeroasunto   ="acnumeroasunto";
	private String acojustificacion ="acojustificacion";	
	private String nuevo 			="nuevo";	
	
	private String esFichaColegial = "";
	private String desdeDesigna = "";
	private String actuacionValidada = "";

	private String comisaria;
	private String juzgado ;
	private String idDelito = "idDelito";
	private String acIdPrision = "acIdPrision";
	private String acIdInstitucionPrision = "acIdInstitucionPrision";
	private ScsAsistenciasBean asistenciaBean;
	private String botonesDetalle;
	String letradoActuaciones;
	/*
	 * Metodos SET y GET 
	 */
	public void    setIdDelito          	 (String	valor)	{ this.datos.put(this.idDelito, valor);}
	public void    setAsunto          		 (String	valor)	{ this.datos.put(this.asunto, valor);}
	public void    setEstado          		 (String	valor)	{ this.datos.put(this.estado, valor);}	
	public void    setFechaEstado      		 (String	valor)	{ this.datos.put(this.fechaEstadoAsistencia, valor);}
	public void    setIdInstitucion          (String	valor)	{ this.datos.put("idInstitucion", valor);}
	public void    setIdTipoEJG		         (String	valor)	{ this.datos.put(this.idTipoEJG, valor);}
	public void    setFechaHora              (String	valor)	{ this.datos.put(this.fechaHora, valor);}
	public void    setObservaciones          (String	valor)	{ this.datos.put(this.observaciones, valor);}
	public void    setIncidencias            (String	valor)	{ this.datos.put(this.incidencias, valor);}
	public void    setFechaPasoAvenia        (String	valor)	{ this.datos.put(this.fechaPasoAvenia, valor);}
	public void    setPasoAvenia             (String	valor)	{ this.datos.put(this.pasoAvenia, valor);}
	public void    setFechaAnulacion         (String	valor)	{ this.datos.put(this.fechaAnulacion, valor);}
	public void    setMotivosAnulacion       (String	valor)	{ this.datos.put(this.motivosAnulacion, valor);}
	public void    setDelitosImputados       (String	valor)	{ this.datos.put(this.delitosImputados, valor);}
	public void    setContrarios             (String	valor)	{ this.datos.put(this.contrarios, valor);}
	public void    setDatosDefensaJuridica   (String	valor)	{ this.datos.put(this.datosDefensaJuridica, valor);}
	public void    setFechaCierre            (String	valor)	{ this.datos.put(this.fechaCierre, valor);}
	public void    setIdTipoAsistencia       (String valor)	{ this.datos.put(this.idTipoAsistencia, valor);}
	public void    setIdTipoAsistenciaColegio(String valor)	{ this.datos.put(this.idTipoAsistenciaColegio, valor);}
	public void    setIdTurno                (String valor)	{ this.datos.put(this.idTurno, valor);}
	public void    setNombreColegiado        (String valor)	{ this.datos.put(this.nombreColegiado, valor);}
	public void    setNumeroColegiado        (String valor)	{ this.datos.put(this.numeroColegiado, valor);}
	public void    setIdGuardia              (String valor)	{ this.datos.put(this.idGuardia, valor);}
	public void    setIdCalendarioGuardias   (String valor)	{ this.datos.put(this.idCalendarioGuardias, valor);}
	public void    setIdPersonaColegiado     (String valor)	{ this.datos.put(this.idPersonaColegiado, valor);}
	public void    setIdPersonaJG            (String valor)	{ this.datos.put(this.idPersonaJG, valor);}
	public void    setIdFacturacion          (String valor)	{ this.datos.put(this.idFacturacion, valor);}
	public void    setFechaGuardia           (String	valor)	{ this.datos.put(this.fechaGuardia, valor);}
	public void    setFechaModificacion      (String	valor)	{ this.datos.put(this.fechaModificacion, valor);}
	public void    setUsuModificacion        (String	valor)	{ this.datos.put(this.usuModificacion, valor);}
	public void    setFechaDesde   			 (String valor)		{ this.datos.put(this.fechaDesde, valor);}
	public void    setFechaHasta     		 (String valor)		{ this.datos.put(this.fechaHasta, valor);}
	public void    setColegiado              (String valor)	{ this.datos.put(this.colegiado, valor);}
	public void    setNif          			 (String valor)		{ this.datos.put(this.nif, valor);}
	public void    setNombre                 (String	valor)	{ this.datos.put(this.nombre, valor);}
	public void    setApellido1      		 (String	valor)	{ this.datos.put(this.apellido1, valor);}
	public void    setApellido2        		 (String	valor)	{ this.datos.put(this.apellido2, valor);}
	public void    setDatosModificables		 (String	valor)	{ this.datos.put(this.datosModificables, valor);}
	public void    setIdPersona				 (String	valor)	{ this.datos.put(this.idPersona, valor);}
	public void    setDireccion				 (String	valor)	{ this.datos.put(this.direccion, valor);}
	public void    setCodigoPostal			 (String	valor)	{ this.datos.put(this.codigoPostal, valor);}
	public void    setRegimenConyugal		 (String	valor)	{ this.datos.put(this.regimenConyugal, valor);}
	public void    setRegimenConyugalAux	 (String	valor)	{ this.datos.put(this.regimenConyugalAux, valor);}
	public void    setIdPais				 (String	valor)	{ this.datos.put(this.idPais, valor);}
	public void    setIdProvincia			 (String	valor)	{ this.datos.put(this.idProvincia, valor);}
	public void    setIdPoblacion			 (String	valor)	{ this.datos.put(this.idPoblacion, valor);}
	public void    setIdPaisAux				 (String	valor)	{ this.datos.put(this.idPaisAux, valor);}
	public void    setIdProvinciaAux		 (String	valor)	{ this.datos.put(this.idProvinciaAux, valor);}
	public void    setIdPoblacionAux		 (String	valor)	{ this.datos.put(this.idPoblacionAux, valor);}
	public void    setIdEstadoCivil			 (String	valor)	{ this.datos.put(this.idEstadoCivil, valor);}
	public void    setIdEstadoCivilAux		 (String	valor)	{ this.datos.put(this.idEstadoCivilAux, valor);}
	public void    setFechaNacimiento		 (String	valor)	{ this.datos.put(this.fechaNacimiento, valor);}
	public void    setModoActual			 (String	valor)	{ this.datos.put(this.modoActual, valor);}
	public void    setActionActivo		 	 (String	valor)	{ this.datos.put(this.actionActivo, valor);}
	public void    setTitulo				 (String	valor)	{ this.datos.put(this.titulo, valor);}
	public void    setLocalizacion			 (String	valor)	{ this.datos.put(this.localizacion, valor);}
	public void    setNuevo			 		 (String	valor)	{ this.datos.put(this.nuevo, valor);}	
	public void    setCabeceraCarta			 (String	valor)	{ this.datos.put(this.cabeceraCarta, valor);}
	public void    setMotivoCarta			 (String	valor)	{ this.datos.put(this.motivoCarta, valor);}
	public void    setPieCarta			 	 (String	valor)	{ this.datos.put(this.pieCarta, valor);}
	
	public void    setActuacionesPendientes  (String valor)	{ this.datos.put(this.actuacionesPendientes, valor);}

	//Actuaciones
	public void setAnio            (String	valor)	{ this.datos.put(this.anio, valor);}
	public void setNumero          (String	valor)	{ this.datos.put(this.numero, valor);}
	public void setAanio            (String	valor)	{ this.datos.put(this.aanio, valor);}
	public void setAnumero          (String	valor)	{ this.datos.put(this.anumero, valor);}
	public void setPjgapellido2     (String	valor)	{ this.datos.put(this.pjgapellido2, valor);}   
	public void setPjgapellido1     (String	valor)	{ this.datos.put(this.pjgapellido1, valor);}   
	public void setPjgnif           (String	valor)	{ this.datos.put(this.pjgnif, valor);}  
	public void setPjgnombre        (String	valor)	{ this.datos.put(this.pjgnombre, valor);}
	public void setGtnombre         (String	valor)	{ this.datos.put(this.gtnombre, valor);}
	public void setTnombre          (String	valor)	{ this.datos.put(this.tnombre, valor);}
	public void setCncolegiado      (String	valor)	{ this.datos.put(this.cncolegiado, valor);}
	public void setPnombre          (String	valor)	{ this.datos.put(this.pnombre, valor);}
	public void setPapellidos2      (String	valor)	{ this.datos.put(this.papellidos2, valor);}
	public void setPapellidos1      (String	valor)	{ this.datos.put(this.papellidos1, valor);}
	public void setAcnumero         (String	valor)	{ this.datos.put(this.acnumero, valor);}
	public void setAcfecha          (String	valor)	{ this.datos.put(this.acfecha, valor);}
	public void setAclugar          (String	valor)	{ this.datos.put(this.aclugar, valor);}
	public void setAcanulacion      (String	valor)	{ this.datos.put(this.acanulacion, valor);}
	public void setAcaextrajudicial (String	valor)	{ this.datos.put(this.acaextrajudicial, valor);}
	public void setAcfjustificacion (String	valor)	{ this.datos.put(this.acfjustificacion, valor);}
	public void setAcidactuacion    (String	valor)	{ this.datos.put(this.acidactuacion, valor);}
	public void setAcdbreve         (String	valor)	{ this.datos.put(this.acdbreve, valor);}
	public void setAcobservaciones  (String	valor)	{ this.datos.put(this.acobservaciones, valor);}
	public void setAcdiadespues     (String	valor)	{ this.datos.put(this.acdiadespues, valor);}
	public void setAcnumeroasunto   (String	valor)	{ this.datos.put(this.acnumeroasunto, valor);}
	public void setAcojustificacion (String	valor)	{ this.datos.put(this.acojustificacion, valor);}
	public void setComisaria (String	valor)		{ this.datos.put("comisaria", valor);}
	public void setJuzgado (String	valor)			{ this.datos.put("juzgado", valor);}
	
	public void setTipoActuacion (String	valor)	{ this.datos.put("TIPOACTUACION", valor);}
	public void setIdCosteFijo (String	valor)		{ this.datos.put("IDCOSTEFIJO", valor);}
	public void setIdTipoActuacion (String	valor)	{ this.datos.put("IDTIPOACTUACION", valor);}
	public void setAcIdPrision (String	valor)		{ this.datos.put(this.acIdPrision, valor);}
	public void setAcIdInstitucionPrision (String	valor)	{ this.datos.put(this.acIdInstitucionPrision, valor);}
	
	public String getAsunto 	            ()	{return (String)this.datos.get(this.asunto);}
	public String getEstado 	            ()	{return (String)this.datos.get(this.estado);}
	public String getFechaEstado 	        ()	{return (String)this.datos.get(this.fechaEstadoAsistencia);}
	public String getIdInstitucion          ()	{return (String)this.datos.get("idInstitucion");}
	public String getIdTipoEJG	            ()	{return (String)this.datos.get(this.idTipoEJG);}
	public String getFechaHora              ()	{return (String)this.datos.get(this.fechaHora);}
	public String getObservaciones          ()	{return (String)this.datos.get(this.observaciones);}
	public String getIncidencias            ()	{return (String)this.datos.get(this.incidencias);}
	public String getFechaPasoAvenia        ()	{return (String)this.datos.get(this.fechaPasoAvenia);}
	public String getPasoAvenia             ()	{return (String)this.datos.get(this.pasoAvenia);}
	public String getFechaAnulacion         ()	{return (String)this.datos.get(this.fechaAnulacion);}
	public String getMotivosAnulacion       ()	{return (String)this.datos.get(this.motivosAnulacion);}
	public String getDelitosImputados       ()	{return (String)this.datos.get(this.delitosImputados);}
	public String getContrarios             ()	{return (String)this.datos.get(this.contrarios);}
	public String getDatosDefensaJuridica   ()	{return (String)this.datos.get(this.datosDefensaJuridica);}
	public String getFechaCierre            ()	{return (String)this.datos.get(this.fechaCierre);}
	public String getIdTipoAsistencia       ()	{return (String)this.datos.get(this.idTipoAsistencia);}
	public String getIdTipoAsistenciaColegio()	{return (String)this.datos.get(this.idTipoAsistenciaColegio);}
	public String getIdTurno                ()	{return (String)this.datos.get(this.idTurno);}
	public String getNombreColegiado        ()	{return (String)this.datos.get(this.nombreColegiado);}
	public String getNumeroColegiado        ()	{return (String)this.datos.get(this.numeroColegiado);}
	public String getIdGuardia              ()	{return (String)this.datos.get(this.idGuardia);}
	public String getIdCalendarioGuardias   ()	{return (String)this.datos.get(this.idCalendarioGuardias);}
	public String getIdPersonaColegiado     ()	{return (String)this.datos.get(this.idPersonaColegiado);}
	public String getIdPersonaJG            ()	{return (String)this.datos.get(this.idPersonaJG);}
	public String getIdFacturacion          ()	{return (String)this.datos.get(this.idFacturacion);}
	public String getFechaGuardia           ()	{return (String)this.datos.get(this.fechaGuardia);}
	public String getFechaModificacion      ()	{return (String)this.datos.get(this.fechaModificacion);}
	public String getUsuModificacion        ()	{return (String)this.datos.get(this.usuModificacion);}
	public String getFechaDesde   			()	{return (String)this.datos.get(this.fechaDesde);}
	public String getFechaHasta     		()	{return (String)this.datos.get(this.fechaHasta);}
	public String getColegiado             	()	{return (String)this.datos.get(this.colegiado);}
	public String getNif          			()	{return (String)this.datos.get(this.nif);}
	public String getNombre                 ()	{return (String)this.datos.get(this.nombre);}
	public String getApellido1      		()	{return (String)this.datos.get(this.apellido1);}
	public String getApellido2        		()	{return (String)this.datos.get(this.apellido2);}
	public String getDatosModificables 		()	{return (String)this.datos.get(this.datosModificables);}
	public String getIdPersona		 		()	{return (String)this.datos.get(this.idPersona);}
	public String getCodigoPostal	 		()	{return (String)this.datos.get(this.codigoPostal);}
	public String getDireccion		 		()	{return (String)this.datos.get(this.direccion);}
	public String getRegimenConyugal 		()	{return (String)this.datos.get(this.regimenConyugal);}
	public String getRegimenConyugalAux		()	{return (String)this.datos.get(this.regimenConyugalAux);}
	public String getIdPais		 			()	{return (String)this.datos.get(this.idPais);}
	public String getIdProvincia		 	()	{return (String)this.datos.get(this.idProvincia);}
	public String getIdPoblacion		 	()	{return (String)this.datos.get(this.idPoblacion);}
	public String getIdPaisAux	 			()	{return (String)this.datos.get(this.idPaisAux);}
	public String getIdProvinciaAux		 	()	{return (String)this.datos.get(this.idProvinciaAux);}
	public String getIdPoblacionAux		 	()	{return (String)this.datos.get(this.idPoblacionAux);}
	public String getIdEstadoCivilAux	 	()	{return (String)this.datos.get(this.idEstadoCivilAux);}
	public String getIdEstadoCivil		 	()	{return (String)this.datos.get(this.idEstadoCivil);}
	public String getFechaNacimiento	 	()	{return (String)this.datos.get(this.fechaNacimiento);}
	public String getModoActual	 			()	{return (String)this.datos.get(this.modoActual);}
	public String getActionActivo 			()	{return (String)this.datos.get(this.actionActivo);}
	public String getTitulo	 				()	{return (String)this.datos.get(this.titulo);}
	public String getLocalizacion		 	()	{return (String)this.datos.get(this.localizacion);}
	public String getNuevo				 	()	{return (String)this.datos.get(this.nuevo);}
	public String getCabeceraCarta			()	{return (String)this.datos.get(this.cabeceraCarta);}
	public String getMotivoCarta			()	{return (String)this.datos.get(this.motivoCarta);}
	public String getPieCarta			 	()	{return (String)this.datos.get(this.pieCarta);}
	public String getActuacionesPendientes  ()	{return (String)this.datos.get(this.actuacionesPendientes);}
		//Actuaciones
	public String getAnio            ()	{return (String)this.datos.get(this.anio);}
	public String getNumero          ()	{return (String)this.datos.get(this.numero);}
	public String getAanio            ()	{return (String)this.datos.get(this.aanio);}
	public String getAnumero          ()	{return (String)this.datos.get(this.anumero);}
	public String getPjgapellido2     ()	{return (String)this.datos.get(this.pjgapellido2);}   
	public String getPjgapellido1     ()	{return (String)this.datos.get(this.pjgapellido1);}   
	public String getPjgnif           ()	{return (String)this.datos.get(this.pjgnif);}  
	public String getPjgnombre        ()	{return (String)this.datos.get(this.pjgnombre);}
	public String getGtnombre         ()	{return (String)this.datos.get(this.gtnombre);}
	public String getTnombre          ()	{return (String)this.datos.get(this.tnombre);}
	public String getCncolegiado      ()	{return (String)this.datos.get(this.cncolegiado);}
	public String getPnombre          ()	{return (String)this.datos.get(this.pnombre);}
	public String getPapellidos2      ()	{return (String)this.datos.get(this.papellidos2);}
	public String getPapellidos1      ()	{return (String)this.datos.get(this.papellidos1);}
	public String getAcnumero         ()	{return (String)this.datos.get(this.acnumero);}
	public String getAcfecha          ()	{return (String)this.datos.get(this.acfecha);}
	public String getAclugar          ()	{return (String)this.datos.get(this.aclugar);}
	public String getAcanulacion      ()	{return (String)this.datos.get(this.acanulacion);}
	public String getAcaextrajudicial ()	{return (String)this.datos.get(this.acaextrajudicial);}
	public String getAcfjustificacion ()	{return (String)this.datos.get(this.acfjustificacion);}
	public String getAcidactuacion    ()	{return (String)this.datos.get(this.acidactuacion);}
	public String getAcdbreve         ()	{return (String)this.datos.get(this.acdbreve);}
	public String getAcobservaciones  ()	{return (String)this.datos.get(this.acobservaciones);}
	public String getAcdiadespues     ()	{return (String)this.datos.get(this.acdiadespues);}
	public String getAcnumeroasunto   ()	{return (String)this.datos.get(this.acnumeroasunto);}
	public String getAcojustificacion ()	{return (String)this.datos.get(this.acojustificacion);}
	public String getComisaria ()			{return (String)this.datos.get("comisaria");}
	public String getJuzgado ()				{return (String)this.datos.get("juzgado");}
	public String getAcIdPrision ()			{return (String)this.datos.get(this.acIdPrision);}
	public String getAcIdInstitucionPrision()	{return (String)this.datos.get(this.acIdInstitucionPrision);}
	
	public String getTipoActuacion ()		{return (String)this.datos.get("TIPOACTUACION");}
	public String getIdCosteFijo ()			{return (String)this.datos.get("IDCOSTEFIJO");}
	public String getIdTipoActuacion ()		{return (String)this.datos.get("IDTIPOACTUACION");}
	
	/**
	 * @return Returns the esFichaColegial.
	 */
	public String getEsFichaColegial() {
		return esFichaColegial;
	}
	/**
	 * @param esFichaColegial The esFichaColegial to set.
	 */
	public void setEsFichaColegial(String esFichaColegial) {
		this.esFichaColegial = esFichaColegial;
	}
	
	/**
	 * @return Returns the desdeDesigna.
	 */
	public String getDesdeDesigna() {
		return desdeDesigna;
	}
	/**
	 * @param desdeDesigna The desdeDesigna to set.
	 */
	public void setDesdeDesigna(String desdeDesigna) {
		this.desdeDesigna = desdeDesigna;
		
	}
	/**
	 * @return Returns the actuacionValidada.
	 */
	public String getActuacionValidada() {
		return actuacionValidada;
	}
	/**
	 * @param actuacionValidada The actuacionValidada to set.
	 */
	public void setActuacionValidada(String actuacionValidada) {
		this.actuacionValidada = actuacionValidada;
	}
	

	String rutaFicheroDownload, ficheroDownload, borrarFicheroDownload;
    /**
     * @return Returns the borrarFicheroDownload.
     */
    public String getBorrarFicheroDownload() {
        return borrarFicheroDownload;
    }
    /**
     * @param borrarFicheroDownload The borrarFicheroDownload to set.
     */
    public void setBorrarFicheroDownload(String borrarFicheroDownload) {
        this.borrarFicheroDownload = borrarFicheroDownload;
    }
    /**
     * @return Returns the ficheroDownload.
     */
    public String getFicheroDownload() {
        return ficheroDownload;
    }
    /**
     * @param ficheroDownload The ficheroDownload to set.
     */
    public void setFicheroDownload(String ficheroDownload) {
        this.ficheroDownload = ficheroDownload;
    }
    /**
     * @return Returns the rutaFicheroDownload.
     */
    public String getRutaFicheroDownload() {
        return rutaFicheroDownload;
    }
    /**
     * @param rutaFicheroDownload The rutaFicheroDownload to set.
     */
    public void setRutaFicheroDownload(String rutaFicheroDownload) {
        this.rutaFicheroDownload = rutaFicheroDownload;
    }

	public String getNumeroDilegencia() {
		return numeroDilegencia;
	}
	public void setNumeroDilegencia(String numeroDilegencia) {
		this.numeroDilegencia = numeroDilegencia;
	}
	public String getNumeroProcedimiento() {
		return numeroProcedimiento;
	}
	public void setNumeroProcedimiento(String numeroProcedimiento) {
		this.numeroProcedimiento = numeroProcedimiento;
	}
	public String getNig() {
		return nig;
	}
	public void setNig(String nig) {
		this.nig = nig;
	}
	public String getEstadoAsintecia() {
		return estadoAsintecia;
	}
	public void setEstadoAsintecia(String estadoAsintecia) {
		this.estadoAsintecia = estadoAsintecia;
	}
	public String getEstadoAsinteciaAnterior() {
		return estadoAsinteciaAnterior;
	}
	public void setFechaEstadoAsistencia(String fechaEstado) {
		this.fechaEstadoAsistencia = fechaEstado;
	}
	public String getFechaEstadoAsistencia() {
		return fechaEstadoAsistencia;
	}
	public void setEstadoAsinteciaAnterior(String estadoAsinteciaAnterior) {
		this.estadoAsinteciaAnterior = estadoAsinteciaAnterior;
	}
	
	public String getEjg_anio() {
		return ejg_anio;
	}
	public void setEjg_anio(String ejg_anio) {
		this.ejg_anio = ejg_anio;
	}
	public String getEjg_idInstitucion() {
		return ejg_idInstitucion;
	}
	public void setEjg_idInstitucion(String ejg_idInstitucion) {
		this.ejg_idInstitucion = ejg_idInstitucion;
	}
	public String getEjg_idTipoEJG() {
		return ejg_idTipoEJG;
	}
	public void setEjg_idTipoEJG(String ejg_idTipoEJG) {
		this.ejg_idTipoEJG = ejg_idTipoEJG;
	}
	public String getEjg_numero() {
		return ejg_numero;
	}
	public void setEjg_numero(String ejg_numero) {
		this.ejg_numero = ejg_numero;
	}
	
	public String getDesigna_anio() {
		return designa_anio;
	}
	public void setDesigna_anio(String designa_anio) {
		this.designa_anio = designa_anio;
	}
	public String getDesigna_idInstitucion() {
		return designa_idInstitucion;
	}
	public void setDesigna_idInstitucion(String designa_idInstitucion) {
		this.designa_idInstitucion = designa_idInstitucion;
	}
	public String getDesigna_numero() {
		return designa_numero;
	}
	public void setDesigna_numero(String designa_numero) {
		this.designa_numero = designa_numero;
	}
	public String getDesigna_turno() {
		return designa_turno;
	}
	public void setDesigna_turno(String designa_turno) {
		this.designa_turno = designa_turno;
	}
	public ScsAsistenciasBean getAsistenciaBean() {
		return asistenciaBean;
	}
	public void setAsistenciaBean(ScsAsistenciasBean asistenciaBean) {
		this.asistenciaBean = asistenciaBean;
	}
	public String getNumeroDilegenciaAsistencia() {
		return numeroDilegenciaAsistencia;
	}
	public void setNumeroDilegenciaAsistencia(String numeroDilegenciaAsistencia) {
		this.numeroDilegenciaAsistencia = numeroDilegenciaAsistencia;
	}
	public String getNumeroProcedimientoAsistencia() {
		return numeroProcedimientoAsistencia;
	}
	public void setNumeroProcedimientoAsistencia(
			String numeroProcedimientoAsistencia) {
		this.numeroProcedimientoAsistencia = numeroProcedimientoAsistencia;
	}
	public String getComisariaAsistencia() {
		return comisariaAsistencia;
	}
	public void setComisariaAsistencia(String comisariaAsistencia) {
		this.comisariaAsistencia = comisariaAsistencia;
	}
	public String getJuzgadoAsistencia() {
		return juzgadoAsistencia;
	}
	public void setJuzgadoAsistencia(String juzgadoAsistencia) {
		this.juzgadoAsistencia = juzgadoAsistencia;
	}
	public ScsAsistenciasBean getAsistenciaVO(){
		ScsAsistenciasBean asistenciasBean = new ScsAsistenciasBean();
		idInstitucion = getIdInstitucion();
		anio = getAnio();
		numero = getNumero();
		if(idInstitucion!=null && !idInstitucion.equals(""))
			asistenciasBean.setIdInstitucion(new Integer(idInstitucion));
		if(anio!=null && !anio.equals(""))
			asistenciasBean.setAnio(new Integer(anio));
		if(numero!=null && !numero.equals(""))
			asistenciasBean.setNumero(new Integer(numero));
		
		return asistenciasBean;
		
	}
	public String getBotonesDetalle() {
		if(esFichaColegial!=null && esFichaColegial.equals("0")){
			if(fechaAnulacion!=null && !fechaAnulacion.equals(""))
				botonesDetalle = "";
			else
				botonesDetalle = "N";
		}else{
			if(fechaAnulacion!=null && !fechaAnulacion.equals(""))
				botonesDetalle = "V";
			else{
				if(letradoActuaciones!=null && letradoActuaciones.equals("S"))
					botonesDetalle = "V,N";
				else
					botonesDetalle = "V";
			}
		}
		return botonesDetalle;
	}
	public String getLetradoActuaciones() {
		return letradoActuaciones;
	}
	public void setLetradoActuaciones(String letradoActuaciones) {
		this.letradoActuaciones = letradoActuaciones;
	}

	public void setNig2	(String a)	{
		this.datos.put("NIG2", a);
	}	
	public String getNig2()	{
		return (String)this.datos.get("NIG2");
	}
	
	public String getIdPretension() {
		return idPretension;
	}
	public void setIdPretension(String idPretension) {
		this.idPretension = idPretension;
	}
	public void setProcedimiento	(String procedimiento)	{
		this.datos.put("PROCEDIMIENTO", procedimiento);
	}
	
	public String getProcedimiento	()	{
		return (String)this.datos.get("PROCEDIMIENTO");
	}	
}