/**
 * Form que representa los campos de la ventana de persona JG y los datos 
 * de entrada para abrirla.
 * @author AtosOrigin SAE - S233735 
 * @since 23-3-2006
 */
package com.siga.gratuita.form;

import java.util.List;
import java.util.Vector;

import com.siga.beans.CenPersonaBean;
import com.siga.beans.CenTipoIdentificacionBean;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.beans.ScsTelefonosPersonaJGBean;
import com.siga.general.MasterForm;
import com.siga.Utilidades.UtilidadesHash;


public class PersonaJGForm extends MasterForm {
	
		
			
	// Datos de entrada de la ventana
	public void setNcolegiadoContrario(String dato) {
		UtilidadesHash.set(this.datos,"NcolegiadoContrario",dato);
	}
	
	public void setConceptoE(String dato) {
		UtilidadesHash.set(this.datos,"ConceptoE",dato);
	}
	public String getConceptoE() {
		return UtilidadesHash.getString(this.datos,"ConceptoE");
	}
	public void setTituloE(String dato) {
		UtilidadesHash.set(this.datos,"TituloE",dato);
	}
	public String getTituloE() {
		return UtilidadesHash.getString(this.datos,"TituloE");
	}
	public void setLocalizacionE(String dato) {
		UtilidadesHash.set(this.datos,"LocalizacionE",dato);
	}
	public void setSexo (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"Sexo", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	public void setIdioma (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"Idioma", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	public void setHijos (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"Hijos", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	public void setNumIdentificacion (String dato) { 
 		try {      
 			UtilidadesHash.set(this.datos,CenPersonaBean.C_NIFCIF, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	public void setTipoIdentificacion (String dato) { 
 		try {      
 			UtilidadesHash.set(this.datos,CenPersonaBean.C_IDTIPOIDENTIFICACION, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	
 	
 	
 	public String getSexo	() 	{ 
 		return UtilidadesHash.getString(this.datos, "Sexo");		
 	}

 	public String getIdioma	() 	{ 
 		return UtilidadesHash.getString(this.datos, "Idioma");		
 	}
 	public String getHijos() 	{ 
 		return UtilidadesHash.getString(this.datos, "Hijos");		
 	}
	public String getLocalizacionE() {
		return UtilidadesHash.getString(this.datos,"LocalizacionE");
	}
	public void setAccionE(String dato) {
		UtilidadesHash.set(this.datos,"AccionE",dato);
	}
	public String getAccionE() {
		return UtilidadesHash.getString(this.datos,"AccionE");
	}
	public void setPantallaE(String dato) {
		UtilidadesHash.set(this.datos,"PantallaE",dato);
	}
	public String getPantallaE() {
		return UtilidadesHash.getString(this.datos,"PantallaE");
	}
	public void setActionE(String dato) {
		UtilidadesHash.set(this.datos,"ActionE",dato);
	}
	public String getActionE() {
		return UtilidadesHash.getString(this.datos,"ActionE");
	}
	// persona JG
	public void setIdInstitucionJG(String dato) {
		UtilidadesHash.set(this.datos,"IdInstitucionJG",dato);
	}
	public String getIdInstitucionJG() {
		return UtilidadesHash.getString(this.datos,"IdInstitucionJG");
	}
	public void setIdPersonaJG(String dato) {
		UtilidadesHash.set(this.datos,"IdPersonaJG",dato);
	}
	public String getIdPersonaJG() {
		return UtilidadesHash.getString(this.datos,"IdPersonaJG");
	}
	// clave persona (para relacionar el representante)
	public void setIdInstitucionPER(String dato) {
		UtilidadesHash.set(this.datos,"IdInstitucionPER",dato);
	}
	public String getIdInstitucionPER() {
		return UtilidadesHash.getString(this.datos,"IdInstitucionPER");
	}
	public void setIdPersonaPER(String dato) {
		UtilidadesHash.set(this.datos,"IdPersonaPER",dato);
	}
	public String getIdPersonaPER() {
		return UtilidadesHash.getString(this.datos,"IdPersonaPER");
	}
	// clave EJG
	public void setIdInstitucionEJG(String dato) {
		UtilidadesHash.set(this.datos,"IdInstitucionEJG",dato);
	}
	public String getIdInstitucionEJG() {
		return UtilidadesHash.getString(this.datos,"IdInstitucionEJG");
	}
	public void setIdTipoEJG(String dato) {
		UtilidadesHash.set(this.datos,"IdTipoEJG",dato);
	}
	public String getIdTipoEJG() {
		return UtilidadesHash.getString(this.datos,"IdTipoEJG");
	}
	public void setAnioEJG(String dato) {
		UtilidadesHash.set(this.datos,"AnioEJG",dato);
	}
	public String getAnioEJG() {
		return UtilidadesHash.getString(this.datos,"AnioEJG");
	}
	public void setNumeroEJG(String dato) {
		UtilidadesHash.set(this.datos,"NumeroEJG",dato);
	}
	public String getNumeroEJG() {
		return UtilidadesHash.getString(this.datos,"NumeroEJG");
	}
	// clave SOJ
	public void setIdInstitucionSOJ(String dato) {
		UtilidadesHash.set(this.datos,"IdInstitucionSOJ",dato);
	}
	public String getIdInstitucionSOJ() {
		return UtilidadesHash.getString(this.datos,"IdInstitucionSOJ");
	}
	public void setIdTipoSOJ(String dato) {
		UtilidadesHash.set(this.datos,"IdTipoSOJ",dato);
	}
	public String getIdTipoSOJ() {
		return UtilidadesHash.getString(this.datos,"IdTipoSOJ");
	}
	public void setAnioSOJ(String dato) {
		UtilidadesHash.set(this.datos,"AnioSOJ",dato);
	}
	public String getAnioSOJ() {
		return UtilidadesHash.getString(this.datos,"AnioSOJ");
	}
	public void setNumeroSOJ(String dato) {
		UtilidadesHash.set(this.datos,"NumeroSOJ",dato);
	}
	public String getNumeroSOJ() {
		return UtilidadesHash.getString(this.datos,"NumeroSOJ");
	}
	// clave Designa
	public void setIdInstitucionDES(String dato) {
		UtilidadesHash.set(this.datos,"IdInstitucionDES",dato);
	}
	public String getIdInstitucionDES() {
		return UtilidadesHash.getString(this.datos,"IdInstitucionDES");
	}
	public void setIdTurnoDES(String dato) {
		UtilidadesHash.set(this.datos,"IdTurnoDES",dato);
	}
	public String getIdTurnoDES() {
		return UtilidadesHash.getString(this.datos,"IdTurnoDES");
	}
	public void setAnioDES(String dato) {
		UtilidadesHash.set(this.datos,"AnioDES",dato);
	}
	public String getAnioDES() {
		return UtilidadesHash.getString(this.datos,"AnioDES");
	}
	public void setNumeroDES(String dato) {
		UtilidadesHash.set(this.datos,"NumeroDES",dato);
	}
	public String getNumeroDES() {
		return UtilidadesHash.getString(this.datos,"NumeroDES");
	}
	// clave Asistencia
	public void setIdInstitucionASI(String dato) {
		UtilidadesHash.set(this.datos,"IdInstitucionASI",dato);
	}
	public String getIdInstitucionASI() {
		return UtilidadesHash.getString(this.datos,"IdInstitucionASI");
	}
	public void setAnioASI(String dato) {
		UtilidadesHash.set(this.datos,"AnioASI",dato);
	}
	public String getAnioASI() {
		return UtilidadesHash.getString(this.datos,"AnioASI");
	}
	public void setNumeroASI(String dato) {
		UtilidadesHash.set(this.datos,"NumeroASI",dato);
	}
	public String getNumeroASI() {
		return UtilidadesHash.getString(this.datos,"NumeroASI");
	}
	// CAMPOS DEL FORMULARIO
	public void setTipo(String dato) {
		UtilidadesHash.set(this.datos,"Tipo",dato);
	}
	public String getTipo() {
		return UtilidadesHash.getString(this.datos,"Tipo");
	}
	public void setTipoId(String dato) {
		UtilidadesHash.set(this.datos,"TipoId",dato);
	}
	public String getTipoId() {
		return UtilidadesHash.getString(this.datos,"TipoId");
	}
	public void setNIdentificacion(String dato) {
		UtilidadesHash.set(this.datos,"NIdentificacion",dato);
	}
	public String getNIdentificacion() {
		return UtilidadesHash.getString(this.datos,"NIdentificacion");
	}
	public void setNombre(String dato) {
		UtilidadesHash.set(this.datos,"Nombre",dato);
	}
	public String getNombre() {
		return UtilidadesHash.getString(this.datos,"Nombre");
	}
	public void setApellido1(String dato) {
		UtilidadesHash.set(this.datos,"Apellido1",dato);
	}
	public String getApellido1() {
		return UtilidadesHash.getString(this.datos,"Apellido1");
	}
	public void setApellido2(String dato) {
		UtilidadesHash.set(this.datos,"Apellido2",dato);
	}
	public String getApellido2() {
		return UtilidadesHash.getString(this.datos,"Apellido2");
	}
	public void setDireccion(String dato) {
		UtilidadesHash.set(this.datos,"Direccion",dato);
	}
	public String getDireccion() {
		return UtilidadesHash.getString(this.datos,"Direccion");
	}
	public void setCp(String dato) {
		UtilidadesHash.set(this.datos,"Cp",dato);
	}
	public String getCp() {
		return UtilidadesHash.getString(this.datos,"Cp");
	}
	public void setProvincia(String dato) {
		UtilidadesHash.set(this.datos,"Provincia",dato);
	}
	public String getProvincia() {
		return UtilidadesHash.getString(this.datos,"Provincia");
	}
	public void setPoblacion(String dato) {
		UtilidadesHash.set(this.datos,"Poblacion",dato);
	}
	public String getPoblacion() {
		return UtilidadesHash.getString(this.datos,"Poblacion");
	}
	public void setNacionalidad(String dato) {
		UtilidadesHash.set(this.datos,"Nacionalidad",dato);
	}
	public String getNacionalidad() {
		return UtilidadesHash.getString(this.datos,"Nacionalidad");
	}
	public void setFechaNac(String dato) {
		UtilidadesHash.set(this.datos,"FechaNac",dato);
	}
	public String getFechaNac() {
		return UtilidadesHash.getString(this.datos,"FechaNac");
	}
	public void setEstadoCivil(String dato) {
		UtilidadesHash.set(this.datos,"EstadoCivil",dato);
	}
	public String getEstadoCivil() {
		return UtilidadesHash.getString(this.datos,"EstadoCivil");
	}
	public void setRegimenConyugal(String dato) {
		UtilidadesHash.set(this.datos,"RegimenConyugal",dato);
	}
	public String getRegimenConyugal() {
		return UtilidadesHash.getString(this.datos,"RegimenConyugal");
	}
	public void setProfesion(String dato) {
		UtilidadesHash.set(this.datos,"Profesion",dato);
	}
	public void setParentesco(String dato) {
		UtilidadesHash.set(this.datos,"Parentesco",dato);
	}
	public String getProfesion() {
		return UtilidadesHash.getString(this.datos,"Profesion");
	}
	public String getParentesco() {
		return UtilidadesHash.getString(this.datos,"Parentesco");
	}
	public void setObservaciones(String dato) {
		UtilidadesHash.set(this.datos,"Observaciones",dato);
	}
	public String getObservaciones() {
		return UtilidadesHash.getString(this.datos,"Observaciones");
	}
	public void setUnidadObservaciones(String dato) {
		UtilidadesHash.set(this.datos,"UnidadObservaciones",dato);
	}
	public String getUnidadObservaciones() {
		return UtilidadesHash.getString(this.datos,"UnidadObservaciones");
	}
	// campo para tabla de tipo calidad en representante tutor.
	public void setEnCalidadDe(String dato) {
		UtilidadesHash.set(this.datos,"EnCalidadDe",dato);
	}
	public String getEnCalidadDe() {
		return UtilidadesHash.getString(this.datos,"EnCalidadDe");
	}
	// campo para tabla de tipo calidad en unidad familiar (Texto libre)
	public void setEnCalidadDeLibre(String dato) {
		UtilidadesHash.set(this.datos,"EnCalidadDeLibre",dato);
	}
	public String getEnCalidadDeLibre() {
		return UtilidadesHash.getString(this.datos,"EnCalidadDeLibre");
	}
	// campo para check (Demandante o demandado) o combo
	public void setCalidad(String dato) {
		UtilidadesHash.set(this.datos,"Calidad",dato);
	}
	public String getCalidad() {
		return UtilidadesHash.getString(this.datos,"Calidad");
	}
	
		
	public void setIdProcurador(String dato) {
		UtilidadesHash.set(this.datos,"IdProcurador",dato);
	}
	public String getIdProcurador() {
		return UtilidadesHash.getString(this.datos,"IdProcurador");
	}
	// para el representante TUTOR (personajg)
	public void setIdRepresentanteJG(String dato) {
		UtilidadesHash.set(this.datos,"IdRepresentanteJG",dato);
	}
	public String getIdRepresentanteJG() {
		return UtilidadesHash.getString(this.datos,"IdRepresentanteJG");
	}
	
	// para el agogado Contrario EJG
	public void setIdAbogadoContrarioEJG(String dato) {
		UtilidadesHash.set(this.datos,"IdAbogadoContrarioEJG",dato);
	}
	public String getIdAbogadoContrarioEJG() {
		return UtilidadesHash.getString(this.datos,"IdAbogadoContrarioEJG");
	}
	// praa el representante legal (letrado del censo, idpersona)
	public void setIdPersonaRepresentante(String dato) {
		UtilidadesHash.set(this.datos,"IdPersonaRepresentante",dato);
	}
	public String getIdPersonaRepresentante() {
		return UtilidadesHash.getString(this.datos,"IdPersonaRepresentante");
	}
	// Nombre del representante en ambos casos
	public void setRepresentante(String dato) {
		UtilidadesHash.set(this.datos,"Representante",dato);
	}	
	
	public String getRepresentante() {
		return UtilidadesHash.getString(this.datos,"Representante");
	}	
	// Nombre del Abogado contrario en ambos casos
	public void setAbogadoContrario(String dato) {
		UtilidadesHash.set(this.datos,"AbogadoContrario",dato);
	}
	public String getAbogadoContrario() {
		return UtilidadesHash.getString(this.datos,"AbogadoContrario");
	}
	// Nombre del Abogado contrario De EJG
	public void setAbogadoContrarioEJG(String dato) {
		UtilidadesHash.set(this.datos,"AbogadoContrarioEJG",dato);
	}
	public String getAbogadoContrarioEJG() {
		return UtilidadesHash.getString(this.datos,"AbogadoContrarioEJG");
	}
//	// praa el Abogado contrario(letrado del censo, idpersona)
	public void setIdPersonaContrario(String dato) {
		UtilidadesHash.set(this.datos,"IdPersonaContrario",dato);
	}
	public String getIdPersonaContrario() {
		return UtilidadesHash.getString(this.datos,"IdPersonaContrario");
	}
	
	public void setSolicitante(String dato) {
		UtilidadesHash.set(this.datos,"Solicitante",dato);
	}
	public String getSolicitante() {
		return UtilidadesHash.getString(this.datos,"Solicitante");
	}
	public void setBienesInmuebles(String dato) {
		UtilidadesHash.set(this.datos,"BienesInmuebles",dato);
	}
	public String getBienesInmuebles() {
		return UtilidadesHash.getString(this.datos,"BienesInmuebles");
	}
	public void setImporteBienesInmuebles(String dato) {
		UtilidadesHash.set(this.datos,"ImporteBienesInmuebles",dato);
	}
	public String getImporteBienesInmuebles() {
		return UtilidadesHash.getString(this.datos,"ImporteBienesInmuebles");
	}
	public void setBienesMuebles(String dato) {
		UtilidadesHash.set(this.datos,"BienesMuebles",dato);
	}
	public String getBienesMuebles() {
		return UtilidadesHash.getString(this.datos,"BienesMuebles");
	}
	public void setImporteBienesMuebles(String dato) {
		UtilidadesHash.set(this.datos,"ImporteBienesMuebles",dato);
	}
	public String getImporteBienesMuebles() {
		return UtilidadesHash.getString(this.datos,"ImporteBienesMuebles");
	}
	public void setOtrosBienes(String dato) {
		UtilidadesHash.set(this.datos,"OtrosBienes",dato);
	}
	public String getOtrosBienes() {
		return UtilidadesHash.getString(this.datos,"OtrosBienes");
	}
	public void setImporteOtrosBienes(String dato) {
		UtilidadesHash.set(this.datos,"ImporteOtrosBienes",dato);
	}
	public String getImporteOtrosBienes() {
		return UtilidadesHash.getString(this.datos,"ImporteOtrosBienes");
	}
	public void setIngresosAnuales(String dato) {
		UtilidadesHash.set(this.datos,"IngresosAnuales",dato);
	}
	public String getIngresosAnuales() {
		return UtilidadesHash.getString(this.datos,"IngresosAnuales");
	}
	public void setImporteIngresosAnuales(String dato) {
		UtilidadesHash.set(this.datos,"ImporteIngresosAnuales",dato);
	}
	public String getImporteIngresosAnuales() {
		return UtilidadesHash.getString(this.datos,"ImporteIngresosAnuales");
	}
	// Otros
	public void setPantalla(String dato) {
		UtilidadesHash.set(this.datos,"Pantalla",dato);
	}
	public String getPantalla() {
		return UtilidadesHash.getString(this.datos,"Pantalla");
	}
	public void setNuevo(String dato) {
		UtilidadesHash.set(this.datos,"Nuevo",dato);
	}
	public String getNuevo() {
		return UtilidadesHash.getString(this.datos,"Nuevo");
	}
	
	public void setChkSolicitaInfoJG(String dato) {
		UtilidadesHash.set(this.datos,"SolicitaInfoJG",dato);
	}
	public String getChkSolicitaInfoJG() {
		return UtilidadesHash.getString(this.datos,"SolicitaInfoJG");
	}	
	
	public void setChkPideJG(String dato) {
		UtilidadesHash.set(this.datos,"PideJG",dato);
	}
	public String getChkPideJG() {
		return UtilidadesHash.getString(this.datos,"PideJG");
	}	
	
	public void setTipoConoce(String dato) {
		UtilidadesHash.set(this.datos,"TipoConoce",dato);
	}
	public String getTipoConoce() {
		return UtilidadesHash.getString(this.datos,"TipoConoce");
	}
	public void setTipoGrupoLaboral(String dato) {
		UtilidadesHash.set(this.datos,"TipoGrupoLaboral",dato);
	}
	public String getTipoGrupoLaboral() {
		return UtilidadesHash.getString(this.datos,"TipoGrupoLaboral");
	}
	
	public void setNumVecesSOJ(String dato) {
		UtilidadesHash.set(this.datos,"NumVecesSOJ",dato);
	}
	public String getNumVecesSOJ() {
		return UtilidadesHash.getString(this.datos,"NumVecesSOJ");
	}
	
	public void setNombreObjetoDestino(String dato) {
		UtilidadesHash.set(this.datos, "NOMBRE_OBJETO_DESTINOP" , dato);
	}

	public String getNombreObjetoDestino() {
		return UtilidadesHash.getString(this.datos, "NOMBRE_OBJETO_DESTINOP");
	}
	public String getNumIdentificacion	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenPersonaBean.C_NIFCIF);		
 	}
	public String getTipoIdentificacion	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenPersonaBean.C_IDTIPOIDENTIFICACION);		
 	}
	public String getTipoIngreso	() 	{ 
 		return UtilidadesHash.getString(this.datos, "TIPO_INGRESO");		
 	}
	public void setTipoIngreso	(String dato) 	{ 
 		UtilidadesHash.set(this.datos, "TIPO_INGRESO" , dato);		
 	}
	
	
	public String getNcolegiadoContrario	() 	{ 
 		return UtilidadesHash.getString(this.datos, "NcolegiadoContrario");		
 	}
	
	public String getNcolegiadoRepresentante	() 	{ 
 		return UtilidadesHash.getString(this.datos, "NcolegiadoRepresentante");		
 	}
	
	
	public void setNcolegiadoRepresentante(String dato) {
		UtilidadesHash.set(this.datos,"NcolegiadoRepresentante",dato);
	}
	
	//Para el correoElectronico de la personajg
	
	String correoElectronico=null;
	String fax=null;	
	String lNumerosTelefonos;
	String idTipoenCalidad="";
	
	public String getIdTipoenCalidad() {
		return idTipoenCalidad;
	}

	public void setIdTipoenCalidad(String idTipoenCalidad) {
		this.idTipoenCalidad = idTipoenCalidad;
	}

	String calidadIdinstitucion="";
	
	public String getCalidadIdinstitucion() {
		return calidadIdinstitucion;
	}

	public void setCalidadIdinstitucion(String calidadIdinstitucion) {
		this.calidadIdinstitucion = calidadIdinstitucion;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	List<ScsTelefonosPersonaJGBean> telefonos;
	private Vector asuntos;
	private String modoGuardar;
	private String accionGuardar;
	private String nombreAnterior; 
	public List<ScsTelefonosPersonaJGBean> getTelefonos() {
		return telefonos;
	}

	public void setTelefonos(List<ScsTelefonosPersonaJGBean> telefonos) {
		this.telefonos = telefonos;
	}
	
	public String getlNumerosTelefonos() {
		return lNumerosTelefonos;
	}

	public void setlNumerosTelefonos(String lNumerosTelefonos) {
		this.lNumerosTelefonos = lNumerosTelefonos;
	}


	public void setAsuntos(Vector datos){
		this.asuntos = datos;
	}
	
	public Vector getAsuntos(){
		return this.asuntos;
	}
	
	public void setModoGuardar(String modo){
		this.modoGuardar = modo;
	}
	
	public String getModoGuardar(){
		return this.modoGuardar;
	}
	
	public void setAccionGuardar(String accion){
		this.accionGuardar = accion;
	}
	
	public String getAccionGuardar(){
		return this.accionGuardar;
	}
	
	public String getNombreAnterior(){
		return this.nombreAnterior;
	}
	
	public void setNombreAnterior(String nom){
		this.nombreAnterior = nom;
	}
	/**nos creamos las variables para sacar el idTipoPersona y el idTipoidentificacion.**/
	String idTipo;
	String idTipoPersona;	
	
	public String getIdTipoPersona() {
		return idTipoPersona;
	}

	public void setIdTipoPersona(String idTipoPersona) {
		this.idTipoPersona = idTipoPersona;
	}

	public String getIdTipo() {
		return idTipo;
	}

	public void setIdTipo(String idTipo) {
		this.idTipo = idTipo;
	}

	List<CenTipoIdentificacionBean> tipos;

	public List<CenTipoIdentificacionBean> getTipos() {
		return tipos;
	}

	public void setTipos(List<CenTipoIdentificacionBean>  tipos) {
		this.tipos = tipos;
	}
	
	List<CenTipoIdentificacionBean> identificadores;

	public List<CenTipoIdentificacionBean> getIdentificadores() {
		return identificadores;
	}

	public void setIdentificadores(List<CenTipoIdentificacionBean> identificadores) {
		this.identificadores = identificadores;
	}
	
	
	

}
