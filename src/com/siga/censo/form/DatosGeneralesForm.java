// VERSIONES
// raul.ggonzalez 11-01-2005 Creacion
//

package com.siga.censo.form;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;
import com.siga.beans.*;
import com.siga.censo.vos.ColegiadoVO;
import com.siga.comun.vos.Vo;

/**
 * Clase action form del caso de uso DATOS GENERALES
 * @author AtosOrigin 11-01-2005
 */

 public class DatosGeneralesForm extends MasterForm {
 	
	// Metodos Set (Formulario (*.jsp))
 	/**
     * El fichero a subir
     */
    protected FormFile fotografia;
    List<CenTipoSociedadBean> tipos;
    
    List<CenTipoSociedadBean> tiposJY;

    //List<CenTipoSociedadBean> tiposSoc;
    
	// BLOQUE PARA EL FORMULARIO DE DATOS GENEREALES 

    
 	public List<CenTipoSociedadBean> getTiposJY() {
		return tiposJY;
	}

	public void setTiposJY(List<CenTipoSociedadBean> tiposJY) {
		this.tiposJY = tiposJY;
	}

	public List<CenTipoSociedadBean> getTipos() {
		return tipos;
	}

	public void setTipos(List<CenTipoSociedadBean> tipos) {
		this.tipos = tipos;
	}

	public void setFoto (FormFile dato) { 
 		try {
 			//this.datos.put(CenClienteBean.C_FOTOGRAFIA, dato);
 			fotografia = dato;
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	public void setTratamiento (String dato) { 
 		try {
 			// lo meto en una clave auxiliar para luego tratarlo
 			// puesto que va a ir a un bean o a otro en funcion de otro valor
 			UtilidadesHash.set(this.datos,CenClienteBean.C_IDTRATAMIENTO, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	public void setNombre (String dato) { 
 		try {
 			// lo meto en una clave auxiliar para luego tratarlo
 			// puesto que va a ir a un bean o a otro en funcion de otro valor
 			UtilidadesHash.set(this.datos,CenPersonaBean.C_NOMBRE, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	public void setApellido1 (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,CenPersonaBean.C_APELLIDOS1, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	public void setApellido2 (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,CenPersonaBean.C_APELLIDOS2, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	public void setCliente (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"cliente", dato);
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

 	public void setNumIdentificacion (String dato) { 
 		try {      
 			UtilidadesHash.set(this.datos,CenPersonaBean.C_NIFCIF, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	public void setFechaNacimiento (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,CenPersonaBean.C_FECHANACIMIENTO, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	public void setLugarNacimiento (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,CenPersonaBean.C_NATURALDE, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	public void setEstadoCivil (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,CenPersonaBean.C_IDESTADOCIVIL, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	public void setSexo (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,CenPersonaBean.C_SEXO, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	public void setIdioma (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,CenClienteBean.C_IDLENGUAJE, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	
 	public void setCaracter (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,CenClienteBean.C_CARACTER, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	
 	public void setPublicidad (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,CenClienteBean.C_PUBLICIDAD, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	
 	public void setGuiaJudicial (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,CenClienteBean.C_GUIAJUDICIAL, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	
 	public void setExportarFoto(String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,CenClienteBean.C_EXPORTARFOTO, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	
 	public void setAbono (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,CenClienteBean.C_ABONOSBANCO, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	
 	public void setCargo (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,CenClienteBean.C_CARGOSBANCO, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	
 	public void setComisiones (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,CenClienteBean.C_COMISIONES, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	
 	public void setNoRevista (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,CenClienteBean.C_NOENVIARREVISTA, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	
 	public void setNoRedAbogacia (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,CenClienteBean.C_NOAPARECERREDABOGACIA, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	
 	public void setCuentaContable (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,CenClienteBean.C_ASIENTOCONTABLE, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	
 	public void setGrupos (String[] dato) {
 		try {
 			this.datos.put("GRUPOS",dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	
 	public void setFechaAlta (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"fechaAlta", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	// RGG 13-01-2005 - AÑADIDOS DESPUES PARA VER COMO SE USAN MEJOR
 	// no pertenecen al form de manera natural pero los utilizamos asi

 	public void setIdInstitucion (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"IDINSTITUCION", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	public void setIdPersona (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,CenPersonaBean.C_IDPERSONA, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	
 	public void setAccion (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"accion", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	public void setMotivo (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"MOTIVO", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	public void setLongitudSJ (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"LONGITUDSJ", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	public void setLongitudSP (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"LONGITUDSP", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	
 	// Metodos Get 1 por campo Formulario (*.jsp)
 	
	// BLOQUE PARA EL FORMULARIO DE DATOS GENERALES 

 	public FormFile getFoto	() 	{ 
 		//return (FormFile) this.datos.get(CenClienteBean.C_FOTOGRAFIA);
 		return fotografia;
 	}

 	public String getTratamiento	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenClienteBean.C_IDTRATAMIENTO);		
 	}

 	public String getNombre () 	{ 
 		return UtilidadesHash.getString(this.datos, CenPersonaBean.C_NOMBRE);		
 	}

 	public String getApellido1	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenPersonaBean.C_APELLIDOS1);		
 	}

 	public String getApellido2	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenPersonaBean.C_APELLIDOS2);		
 	}

 	public String getCliente	() 	{ 
 		return UtilidadesHash.getString(this.datos, "cliente");		
 	}

 	public String getTipoIdentificacion	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenPersonaBean.C_IDTIPOIDENTIFICACION);		
 	}
 	                  
 	public String getNumIdentificacion	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenPersonaBean.C_NIFCIF);		
 	}

 	public String getFechaNacimiento	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenPersonaBean.C_FECHANACIMIENTO);		
 	}

 	public String getLugarNacimiento	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenPersonaBean.C_NATURALDE);		
 	}

 	public String getEstadoCivil	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenPersonaBean.C_IDESTADOCIVIL);		
 	}

 	public String getSexo	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenPersonaBean.C_SEXO);		
 	}

 	public String getIdioma	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenClienteBean.C_IDLENGUAJE);		
 	}

 	public String getCaracter	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenClienteBean.C_CARACTER);		
 	}

 	public String getPublicidad	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenClienteBean.C_PUBLICIDAD);		
 	}

 	public String getGuiaJudicial	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenClienteBean.C_GUIAJUDICIAL);		
 	}
 	
 	public String getExportarFoto	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenClienteBean.C_EXPORTARFOTO);		
 	} 	

 	public String getAbono	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenClienteBean.C_ABONOSBANCO);		
 	}

 	public String getCargo	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenClienteBean.C_CARGOSBANCO);		
 	}

 	public String getComisiones	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenClienteBean.C_COMISIONES);		
 	}

 	public String getNoRevista	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenClienteBean.C_NOENVIARREVISTA );		
 	}

 	public String getNoRedAbogacia	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenClienteBean.C_NOAPARECERREDABOGACIA);		
 	}

 	public String getCuentaContable	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenClienteBean.C_ASIENTOCONTABLE);		
 	}

 	public String[] getGrupos	() 	{ 
 		return (String[]) this.datos.get("GRUPOS");		
  	}

 	public String getFechaAlta	() 	{ 
 		return UtilidadesHash.getString(this.datos, "fechaAlta");		
 	}

 	// RGG 13-01-2005 - AÑADIDOS DESPUES PARA VER COMO SE USAN MEJOR
 	// no pertenecen al form de manera natural pero los utilizamos asi

 	public String getIdInstitucion	() 	{ 
 		return UtilidadesHash.getString(this.datos, "IDINSTITUCION");		
 	}

 	public String getIdPersona	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenPersonaBean.C_IDPERSONA);		
 	}

 	public String getAccion	() 	{ 
 		return UtilidadesHash.getString(this.datos, "accion");		
 	}

 	public String getMotivo	() 	{ 
 		return UtilidadesHash.getString(this.datos, "MOTIVO");		
 	}
 	public String getLongitudSJ	() 	{ 
 		return UtilidadesHash.getString(this.datos, "LONGITUDSJ");		
 	}
 	public String getLongitudSP	() 	{ 
 		return UtilidadesHash.getString(this.datos, "LONGITUDSP");		
 	}

	// OTRAS FUNCIONES 

	/**
	 * Metodo que resetea el formulario
	 * @param  mapping - Mapeo de los struts
	 * @param  request - objeto llamada HTTP 
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		try {
			// resetea el formulario
			super.reset(mapping, request);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	//
	// Nuevos campos del form para no coelgiados y de tipo no personal
	//
	
 	public void setDenominacion (String dato) { 
 			UtilidadesHash.set(this.datos,"DENOMINACION", dato);
 	}	
 	public String getDenominacion	() 	{ 
 		return UtilidadesHash.getString(this.datos, "DENOMINACION");		
 	}

 	public void setSociedadSJ (String dato) { 
			UtilidadesHash.set(this.datos,"SOCIEDADSJ", dato);
	}	
	public String getSociedadSJ	() 	{ 
		return UtilidadesHash.getString(this.datos, "SOCIEDADSJ");		
	}
	public void setSociedadSP (String dato) { 
		UtilidadesHash.set(this.datos,"SOCIEDADSP", dato);
	}	
	public String getSociedadSP	() 	{ 
		return UtilidadesHash.getString(this.datos, "SOCIEDADSP");		
	}
 	public void setTipo (String dato) { 
		UtilidadesHash.set(this.datos,"TIPO", dato);
	}	
	public String getTipo	() 	{ 
		return UtilidadesHash.getString(this.datos, "TIPO");		
	}
	
	public void setTipoJY (String dato) { 
		UtilidadesHash.set(this.datos,"TIPOJY", dato);
	}	
	public String getTipoJY	() 	{ 
		return UtilidadesHash.getString(this.datos, "TIPOJY");		
	}	
	
	

 	/*public void setNumeroRegistro (String dato) { 
		UtilidadesHash.set(this.datos,"NUMEROREGISTRO", dato);
	}	
	public String getNumeroRegistro	() 	{ 
		return UtilidadesHash.getString(this.datos, "NUMEROREGISTRO");		
	}	*/	

 	public void setFuncion (String dato) { 
		UtilidadesHash.set(this.datos,"FUNCION", dato);
	}	
	public String getFuncion () { 
		return UtilidadesHash.getString(this.datos, "FUNCION");		
	}	

 	public void setAnotaciones (String dato) { 
		UtilidadesHash.set(this.datos,"ANOTACIONES", dato);
	}	
	public String getAnotaciones () { 
		return UtilidadesHash.getString(this.datos, "ANOTACIONES");		
	}		

 	public void setAbreviatura (String dato) { 
		UtilidadesHash.set(this.datos,"ABREVIATURA", dato);
	}	
	public String getAbreviatura () { 
		return UtilidadesHash.getString(this.datos, "ABREVIATURA");		
	}	

 	public void setFechaConstitucion (String dato) { 
		UtilidadesHash.set(this.datos,"FECHACONSTITUCION", dato);
	}	
	public String getFechaConstitucion () { 
		return UtilidadesHash.getString(this.datos, "FECHACONSTITUCION");		
	}	
	public void setPrefijoNumRegSP (String dato) { 
		UtilidadesHash.set(this.datos,"PREFIJONUMREGSP", dato);
	}	
	public String getPrefijoNumRegSP() 	{ 
		return UtilidadesHash.getString(this.datos, "PREFIJONUMREGSP");		
	}	
	public void setContadorNumRegSP (String dato) { 
		UtilidadesHash.set(this.datos,"CONTADORNUMREGSP", dato);
	}	
	public String getContadorNumRegSP() 	{ 
		return UtilidadesHash.getString(this.datos, "CONTADORNUMREGSP");		
	}
	public void setSufijoNumRegSP (String dato) { 
		UtilidadesHash.set(this.datos,"SUFIJONUMREGSP", dato);
	}	
	public String getSufijoNumRegSP() 	{ 
		return UtilidadesHash.getString(this.datos, "SUFIJONUMREGSP");		
	}
	public void setModoSociedadSP (String dato) { 
 		try {
 			// lo meto en una clave auxiliar para luego tratarlo
 			// puesto que va a ir a un bean o a otro en funcion de otro valor
 			UtilidadesHash.set(this.datos,"MODOSOCIEDADSP", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	public String getModoSociedadSP() 	{ 
		return UtilidadesHash.getString(this.datos, "MODOSOCIEDADSP");		
	}
	public void setPrefijoNumRegSJ (String dato) { 
		UtilidadesHash.set(this.datos,"PREFIJONUMREGSJ", dato);
	}	
	public String getPrefijoNumRegSJ() 	{ 
		return UtilidadesHash.getString(this.datos, "PREFIJONUMREGSJ");		
	}	
	public void setContadorNumRegSJ (String dato) { 
		UtilidadesHash.set(this.datos,"CONTADORNUMREGSJ", dato);
	}	
	public String getContadorNumRegSJ() 	{ 
		return UtilidadesHash.getString(this.datos, "CONTADORNUMREGSJ");		
	}
	public void setSufijoNumRegSJ (String dato) { 
		UtilidadesHash.set(this.datos,"SUFIJONUMREGSJ", dato);
	}	
	public String getSufijoNumRegSJ() 	{ 
		return UtilidadesHash.getString(this.datos, "SUFIJONUMREGSJ");		
	}
	public void setModoSociedadSJ (String dato) { 
 		try {
 			// lo meto en una clave auxiliar para luego tratarlo
 			// puesto que va a ir a un bean o a otro en funcion de otro valor
 			UtilidadesHash.set(this.datos,"MODOSOCIEDADSJ", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	public String getModoSociedadSJ() 	{ 
		return UtilidadesHash.getString(this.datos, "MODOSOCIEDADSJ");		
	}
	
	public String getIdGrupoFijo() 	{ 
		return UtilidadesHash.getString(this.datos, "IDGRUPOFIJO");		
	}
	public void setIdGrupoFijo(String dato) { 
		UtilidadesHash.set(this.datos,"IDGRUPOFIJO", dato);
	}
	public String getIdInstitucionGrupoFijo() 	{ 
		return UtilidadesHash.getString(this.datos, "IDINSTITUCIONGRUPOFIJO");		
	}
	public void setIdInstitucionGrupoFijo(String dato) { 
		UtilidadesHash.set(this.datos,"IDINSTITUCIONGRUPOFIJO", dato);
	}
	public void setIdprofesion (String dato) { 
			UtilidadesHash.set(this.datos,"IDPROFESION", dato);
	}	
	public String getIdProfesion() 	{ 
		return UtilidadesHash.getString(this.datos, "IDPROFESION");		
	}
	public void setIdInstitucionProf (String dato) { 
		UtilidadesHash.set(this.datos,"IDINSTITUCIONPROF", dato);
	}	
	public String getIdInstitucionProf	() 	{ 
		return UtilidadesHash.getString(this.datos, "IDINSTITUCIONPROF");		
	}
	public void setActividadProfesional (String actividadProfesional) { 
		UtilidadesHash.set(this.datos,"ACTIVIDADPROSESIONAL", actividadProfesional);
	}	
	public String getActividadProfesional	() 	{ 
		return UtilidadesHash.getString(this.datos, "ACTIVIDADPROSESIONAL");		
	}
	
	
	
	
	
	String idDireccion;
	String colegiadoen;
	String idTipoDireccion;
	String pais;
	String poblacion;
	String poblacionExt;
	String provincia;
	String motivo;
	String movil;
	String paginaWeb;
	String preferente;
	String telefono1;
	String telefono2;
	Boolean preferenteFax;
	Boolean preferenteCorreo;
	Boolean preferenteMail;	
	Boolean preferenteSms;	
	String codigoPostal;
	String correoElectronico;
	String domicilio;
	String fax1;
	String fax2;
	String continuarAprobacion;
	String numColegiado="";
	List<DireccionesForm> direcciones;
	String textoAlerta;

	public String getIdTipoDireccion() {
		return idTipoDireccion;
	}

	public void setIdTipoDireccion(String idTipoDireccion) {
		this.idTipoDireccion = idTipoDireccion;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getPoblacion() {
		return poblacion;
	}

	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}

	
	public String getPoblacionExt() {
		return poblacionExt;
	}

	public void setPoblacionExt(String poblacionExt) {
		this.poblacionExt = poblacionExt;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getMovil() {
		return movil;
	}

	public void setMovil(String movil) {
		this.movil = movil;
	}

	public String getPaginaWeb() {
		return paginaWeb;
	}

	public void setPaginaWeb(String paginaWeb) {
		this.paginaWeb = paginaWeb;
	}

	public String getPreferente() {
		return preferente;
	}

	public void setPreferente(String preferente) {
		this.preferente = preferente;
	}

	public String getTelefono1() {
		return telefono1;
	}

	public void setTelefono1(String telefono1) {
		this.telefono1 = telefono1;
	}

	public String getTelefono2() {
		return telefono2;
	}

	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}

		public Boolean getPreferenteFax() {
		return preferenteFax;
	}

	public void setPreferenteFax(Boolean preferenteFax) {
		this.preferenteFax = preferenteFax;
	}

	public Boolean getPreferenteCorreo() {
		return preferenteCorreo;
	}

	public void setPreferenteCorreo(Boolean preferenteCorreo) {
		this.preferenteCorreo = preferenteCorreo;
	}

	public Boolean getPreferenteMail() {
		return preferenteMail;
	}

	public void setPreferenteMail(Boolean preferenteMail) {
		this.preferenteMail = preferenteMail;
	}

	public Boolean getPreferenteSms() {
		return preferenteSms;
	}

	public void setPreferenteSms(Boolean preferenteSms) {
		this.preferenteSms = preferenteSms;
	}

	
	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getFax1() {
		return fax1;
	}

	public void setFax1(String fax1) {
		this.fax1 = fax1;
	}

	public String getFax2() {
		return fax2;
	}

	public void setFax2(String fax2) {
		this.fax2 = fax2;
	}

	public String getNumColegiado() {
		return numColegiado;
	}

	public void setNumColegiado(String numColegiado) {
		this.numColegiado = numColegiado;
	}
	
	public String getContinuarAprobacion() {
		return continuarAprobacion;
	}

	public void setContinuarAprobacion(String continuarAprobacion) {
		this.continuarAprobacion = continuarAprobacion;
	}
	
	public String getId() {
		ColegiadoVO colegiado = new ColegiadoVO();
		colegiado.setIdInstitucion(getIdInstitucion());
		colegiado.setIdPersona(getIdPersona());
		return colegiado.getId();
	}

	public String getIdDireccion() {
		return idDireccion;
	}

	public void setIdDireccion(String idDireccion) {
		this.idDireccion = idDireccion;
	}

	public List<DireccionesForm> getDirecciones() {
		return direcciones;
	}

	public void setDirecciones(List<DireccionesForm> direcciones) {
		this.direcciones = direcciones;
	}
	
	public String getColegiadoen() {
		return colegiadoen;
	}
	public void setColegiadoen(String colegiadoen) {
		this.colegiadoen = colegiadoen;
	}

	public String getTextoAlerta() {
		return textoAlerta;
	}

	public void setTextoAlerta(String textoAlerta) {
		this.textoAlerta = textoAlerta;
	}	
	
}