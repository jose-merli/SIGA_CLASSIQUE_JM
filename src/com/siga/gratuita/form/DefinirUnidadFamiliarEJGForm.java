/*
 * Fecha creaci�n: 01/02/2005
 * Autor: julio.vicente
 */

package com.siga.gratuita.form;

import java.util.List;

import org.redabogacia.sigaservices.app.AppConstants.EEJG_ESTADO;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.administracion.SIGAConstants;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsParentescoBean;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.beans.ScsSOJBean;
import com.siga.beans.ScsTelefonosPersonaBean;
import com.siga.beans.ScsUnidadFamiliarEJGBean;
import com.siga.beans.eejg.ScsEejgPeticionesBean;
import com.siga.general.MasterForm;
import com.siga.tlds.FilaExtElement;

/**
* Maneja el formulario que mantiene la tabla SCS_PERSONAJG
*/
 public class DefinirUnidadFamiliarEJGForm extends MasterForm{	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9169934500658939569L;
	private List<DefinirUnidadFamiliarEJGForm> unidadFamiliar;
	private List<ScsEejgPeticionesBean> peticionesEejg;
	ScsParentescoBean parentesco;
	ScsPersonaJGBean personaJG;
	ScsEejgPeticionesBean peticionEejg;
	ScsEJGBean ejg ;
	private String botones;
	FilaExtElement[] elementosFila;
	String datosInforme;
	private String idXml;
	Boolean permisoEejg;
	String idioma;
	
	
	private String idParentesco;
	
	private String idTipoGrupoLab;
	private String idTipoIngreso;
	private String idPaisAux="idPaisAux";
	private String idProvinciaAux="idProvinciaAux";
	private String idPoblacionAux="idPoblacionAux";
	private String idEstadoCivilAux="idEstadoCivilAux";
	private String regimenConyugalAux="regimenConyugalAux";
	private boolean esComision=false;;
	
	public void    setIdPaisAux				 (String	valor)	{ this.datos.put(this.idPaisAux, valor);}
	public void    setIdProvinciaAux		 (String	valor)	{ this.datos.put(this.idProvinciaAux, valor);}
	public void    setIdPoblacionAux		 (String	valor)	{ this.datos.put(this.idPoblacionAux, valor);}
	public void    setIdEstadoCivilAux		 (String	valor)	{ this.datos.put(this.idEstadoCivilAux, valor);}
	public void    setRegimenConyugalAux	 (String	valor)	{ this.datos.put(this.regimenConyugalAux, valor);}
	public void    setEsComision			 (boolean   valor)	{ this.esComision=valor;}
	public String getIdPaisAux	 			()	{return (String)this.datos.get(this.idPaisAux);}
	public String getIdProvinciaAux		 	()	{return (String)this.datos.get(this.idProvinciaAux);}
	public String getIdPoblacionAux		 	()	{return (String)this.datos.get(this.idPoblacionAux);}
	public String getIdEstadoCivilAux	 	()	{return (String)this.datos.get(this.idEstadoCivilAux);}
	public String getRegimenConyugalAux		()	{return (String)this.datos.get(this.regimenConyugalAux);}
	public boolean getEsComision			()	{return this.esComision;}

	// Metodos Set (Formulario (*.jsp))
	
 	/**
	 * Almacena en la Hashtable si la persona exist�a en la base de datos 
	 * 
	 * @param Si exist�a o no 
	 * @return void 
	 */
	public void setExistia					(String existia)			{ this.datos.put("EXISTIA", existia);							}
 	
 	/**
	 * Almacena en la Hashtable el identificador de una persona 
	 * 
	 * @param Identificador solicitante SOJ 
	 * @return void 
	 */
	public void setIdPersona				(String idPersona)			{ this.datos.put(ScsPersonaJGBean.C_IDPERSONA, idPersona);						}
	
	/**
	 * Almacena en la Hashtable la institucion de la persona 
	 * 
	 * @param Institucion persona 
	 * @return void 
	 */
	public void setIdInstitucion	 		(String idInstitucion)		{ this.datos.put(ScsPersonaJGBean.C_IDINSTITUCION, idInstitucion);				}
	/**
	 * Almacena en la Hashtable el anho del EJG 
	 * 
	 * @param valor Anho de la EJG. De tipo "Integer". 
	 * @return void 
	 */
	public void setAnio 					(String anio)					{ this.datos.put(ScsEJGBean.C_ANIO, anio);									}
	/**
	 * Almacena en la Hashtable el numero del EJG 
	 * 
	 * @param valor Numero de la EJG. De tipo "String". 
	 * @return void 
	 */
	public void setNumero 					(String numero)					{ this.datos.put(ScsEJGBean.C_NUMERO, numero);								}
	/**
	 * Almacena en la Hashtable el identificador del tipo de EJG del dictamen del EJG 
	 * 
	 * @param valor Identificador del tipo de EJG del dictamen del EJG. De tipo "String". 
	 * @return void 
	 */
	public void setIdTipoEJG				(String idTipoEJG)				{ this.datos.put(ScsEJGBean.C_IDTIPOEJG, idTipoEJG);				}
	
	/**
	 * Almacena en la Hashtable el nombre de la persona 
	 * 
	 * @param Nombre solicitante de un SOJ 
	 * @return void 
	 */
	public void setNombre		 			(String nombre)				{ this.datos.put(ScsPersonaJGBean.C_NOMBRE, nombre);							}
	
	/**
	 * Almacena en la Hashtable el apellido de la persona 
	 * 
	 * @param Apellido solicitante de un SOJ 
	 * @return void 
	 */
	public void setApellido1	 			(String apellido)			{ this.datos.put(ScsPersonaJGBean.C_APELLIDO1, apellido);						}
	
	/**
	 * Almacena en la Hashtable el apellido de la persona 
	 * 
	 * @param Apellido solicitante de un SOJ 
	 * @return void 
	 */
	public void setApellido2	 			(String apellido)			{ this.datos.put(ScsPersonaJGBean.C_APELLIDO2, apellido);						}
	
	/**
	 * Almacena en la Hashtable el NIF de la persona 
	 * 
	 * @param Nif solicitante
	 * @return void 
	 */
	public void setNif						(String nif)				{ this.datos.put(ScsPersonaJGBean.C_NIF, nif);								}
	
	/**
	 * Almacena en la Hashtable la direcci�n de la persona 
	 * 
	 * @param Direcci�n solicitante
	 * @return void 
	 */
	public void setDireccion				(String direccion)			{ this.datos.put(ScsPersonaJGBean.C_DIRECCION, direccion);							}
	
	/**
	 * Almacena en la Hashtable el c�digo postal de la persona 
	 * 
	 * @param C�digo postal solicitante
	 * @return void 
	 */
	public void setCodigoPostal				(String cp)					{ this.datos.put(ScsPersonaJGBean.C_CODIGOPOSTAL, cp);								}
	
	/**
	 * Almacena en la Hashtable la fecha de nacimiento de la persona 
	 * 
	 * @param Fecha nacimiento solicitante
	 * @return void 
	 */
	public void setFechaNacimiento			(String fecha)				{ this.datos.put(ScsPersonaJGBean.C_FECHANACIMIENTO, fecha);						}
	
	/**
	 * Almacena en la Hashtable el c�digo de profesi�n de la persona 
	 * 
	 * @param C�digo profesi�n solicitante
	 * @return void 
	 */
	public void setIdProfesion				(String idprofesion)		{ this.datos.put(ScsPersonaJGBean.C_IDPROFESION, idprofesion);						}
	
	/**
	 * Almacena en la Hashtable el c�digo del pa�s de la persona 
	 * 
	 * @param C�digo pa�s solicitante
	 * @return void 
	 */
	public void setIdPais					(String pais)				{ this.datos.put(ScsPersonaJGBean.C_IDPAIS, pais);									}
	
	/**
	 * Almacena en la Hashtable el c�digo de la provincia de la persona 
	 * 
	 * @param C�digo postal solicitante
	 * @return void 
	 */
	public void setIdProvincia				(String provincia)			{ this.datos.put(ScsPersonaJGBean.C_IDPROVINCIA, provincia);						}
	
	/**
	 * Almacena en la Hashtable el c�digo postal de la persona 
	 * 
	 * @param C�digo postal solicitante
	 * @return void 
	 */
	public void setIdPoblacion				(String poblacion)			{ this.datos.put(ScsPersonaJGBean.C_IDPOBLACION, poblacion);						}
	
	/**
	 * Almacena en la Hashtable el c�digo postal de la persona 
	 * 
	 * @param C�digo postal solicitante
	 * @return void 
	 */
	public void setRegimenConyugal			(String regimen)			{ this.datos.put(ScsPersonaJGBean.C_REGIMENCONYUGAL, regimen);						}
	
	/**
	 * Recupera de la Hashtable el identificador del estado civil
	 * @return String 
	 */
	public void setIdEstadoCivil			(String estado)				{ this.datos.put(ScsPersonaJGBean.C_ESTADOCIVIL, estado);							}
	
	/**
	 * Almacena en la Hashtable el identificador del tel�fono 
	 * 
	 * @param Identificador tel�fono
	 * @return void 
	 */
	public void setIdTelefono		 	(String idTelefono)				{ this.datos.put(ScsTelefonosPersonaBean.C_IDTELEFONO, idTelefono);					}
	
	/**
	 * Almacena en la Hashtable el nombre del tel�fono 
	 * 
	 * @param Nombre tel�fono
	 * @return void 
	 */
	public void setNombreTelefono		(String nombreTelefono)			{ this.datos.put(ScsTelefonosPersonaBean.C_NOMBRETELEFONO, nombreTelefono);			}
	
	/**
	 * Almacena en la Hashtable el n�mero de tel�fono 
	 * 
	 * @param N�mero tel�fono
	 * @return void 
	 */
	public void setNumeroTelefono		(String numeroTelefono)			{ this.datos.put(ScsTelefonosPersonaBean.C_NUMEROTELEFONO, numeroTelefono);			}	
	/**
	 * Almacena en la Hashtable la descripci�n de los ingresos anuales 
	 * 
	 * @param Ingresos anuales
	 * @return void 
	 */
	public void setDescripcionIngresosAnuales	(String descripcionIngresos)	{ this.datos.put(ScsUnidadFamiliarEJGBean.C_DESCRIPCIONINGRESOSANUALES, descripcionIngresos);}
	/** 
	 * Almacena en la Hashtable el importe de los ingresos anuales 
	 * 
	 * @param Importe ingresos anuales
	 * @return void 
	 */
	public void setImporteIngresosAnuales		(String ingresos)				{ this.datos.put(ScsUnidadFamiliarEJGBean.C_IMPORTEINGRESOSANUALES, ingresos);}
	/** 
	 * Almacena en la Hashtable la descripci�n de los bienes inmuebles 
	 * 
	 * @param Descripci�n bienes inmuebles
	 * @return void 
	 */
	public void setBienesInmuebles				(String bienesInmbuebles)		{ this.datos.put(ScsUnidadFamiliarEJGBean.C_BIENESINMUEBLES, bienesInmbuebles);}
	/** 
	 * Almacena en la Hashtable el importe de los bienes inmuebles 
	 * 
	 * @param Importe bienes inmuebles
	 * @return void 
	 */
	public void setImporteBienesInmuebles		(String importeBienesInmbuebles){ this.datos.put(ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESINMUEBLES, importeBienesInmbuebles);}	
	/** 
	 * Almacena en la Hashtable la descripci�n de los bienes muebles 
	 * 
	 * @param Descripci�n bienes inmuebles
	 * @return void 
	 */
	public void setBienesMuebles				(String bienesMuebles)		{ this.datos.put(ScsUnidadFamiliarEJGBean.C_BIENESMUEBLES, bienesMuebles);}
	/** 
	 * Almacena en la Hashtable el importe de los bienes inmuebles 
	 * 
	 * @param Importe bienes inmuebles
	 * @return void 
	 */
	public void setImporteBienesMuebles		(String importeBienesMuebles)	{ this.datos.put(ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESMUEBLES, importeBienesMuebles);}
	/** 
	 * Almacena en la Hashtable la descripci�n de otros bienes 
	 * 
	 * @param Descripci�n otros bienes
	 * @return void 
	 */
	public void setOtrosBienes				(String otrosBienes)			{ this.datos.put(ScsUnidadFamiliarEJGBean.C_OTROSBIENES, otrosBienes);}
	/** 
	 * Almacena en la Hashtable el importe de otros bienes 
	 * 
	 * @param Importe otros bienes
	 * @return void 
	 */
	public void setImporteOtrosBienes		(String importeOtrosBienes)		{ this.datos.put(ScsUnidadFamiliarEJGBean.C_IMPORTEOTROSBIENES, importeOtrosBienes);}
	/** 
	 * Almacena en la Hashtable si es solicitante 
	 * 
	 * @param Si es solicitante
	 * @return void 
	 */
	public void setSolicitante				(String solicitante)			{ this.datos.put(ScsUnidadFamiliarEJGBean.C_SOLICITANTE, solicitante);}	
	/** 
	 * Almacena en la Hashtable en calidad de que est� relacinada la persona con el EJG
	 * 
	 * @param En calidad de que est� relacionado
	 * @return void 
	 */
	public void setEnCalidadDe				(String calidad)				{ this.datos.put(ScsUnidadFamiliarEJGBean.C_ENCALIDADDE, calidad);}
	/** 
	 * Almacena en la Hashtable las observaciones 
	 * 
	 * @param Observaciones
	 * @return void 
	 */
	public void setObservaciones			(String observaciones)			{ this.datos.put(ScsUnidadFamiliarEJGBean.C_OBSERVACIONES, observaciones);}
	
	
	// Metodos Get 1 por campo Formulario (*.jsp)
	
	/**
	 * Recupera de la Hashtable el identificador abogado de un SOJ
	 * @return String 
	 */
	public String getIdPersona				()							{ return (String)this.datos.get(ScsSOJBean.C_IDPERSONA);						}
	
	/**
	 * Recupera de la Hashtable la institucion de un SOJ 
	 * @return String
	 */
	public String getIdInstitucion	 		()							{return (String)this.datos.get(ScsSOJBean.C_IDINSTITUCION);					}
	/**
	 * Recupera de la Hashtable el anho de la EJG
	 * 
	 * @return Anho de la EJG
	 */
	public String getAnio 					()		{ return (String)this.datos.get(ScsEJGBean.C_ANIO);}
	/**
	 * Recupera de la Hashtable el numero de la EJG
	 * 
	 * @return Numero de la EJG
	 */
	public String getNumero 				()		{ return (String)this.datos.get(ScsEJGBean.C_NUMERO);}
	/**
	 * Recupera de la Hashtable el identificador del tipo  de la EJG
	 * 
	 * @return Identificador del tipo de la EJG
	 */
	public String getIdTipoEJG				()		{ return (String)this.datos.get(ScsEJGBean.C_IDTIPOEJG);}
	
	/**
	 * Recupera de la Hashtable el nombre del solicitante de un SOJ
	 * @return String 
	 */
	public String getNombre		 			()						{ return (String)this.datos.get(ScsPersonaJGBean.C_NOMBRE);						}
	
	/**
	 * Recupera de la Hashtable el apellido del solicitante de un SOJ
	 * @return String 
	 */
	public String getApellido1	 			()						{ return (String)this.datos.get(ScsPersonaJGBean.C_APELLIDO1);					}
	
	/**
	 * Recupera de la Hashtable el apellido del solicitante de un SOJ 
	 * @return String 
	 */
	public String getApellido2	 			()						{ return (String)this.datos.get(ScsPersonaJGBean.C_APELLIDO2);					}
	
	/**
	 * Recupera de la Hashtable el NIF de la persona
	 * @return String
	 */
	public String getNif					()						{ return (String)this.datos.get(ScsPersonaJGBean.C_NIF);							}
	
	/**
	 * Recupera de la Hashtable la direcci�n de la persona
	 * @return String
	 */
	public String getDireccion				()						{ return (String)this.datos.get(ScsPersonaJGBean.C_DIRECCION);					}
	
	/**
	 * Recupera de la Hashtable el c�digo postal de la persona
	 * @return String
	 */
	public String getCodigoPostal			()						{ return (String)this.datos.get(ScsPersonaJGBean.C_CODIGOPOSTAL);				}
	
	/**
	 * Recupera de la Hashtable la fecha de nacimiento de la persona
	 * @return String
	 */
	public String getFechaNacimiento		()						{ return (String)this.datos.get(ScsPersonaJGBean.C_FECHANACIMIENTO);				}
	
	/**
	 * Recupera de la Hashtable el c�digo de profesi�n de la persona
	 * @return String
	 */
	public String getIdProfesion			()						{ return (String)this.datos.get(ScsPersonaJGBean.C_IDPROFESION);					}
	
	/**
	 * Recupera de la Hashtable el c�digo del pa�s de la persona
	 * @return String 
	 */
	public String getIdPais					()						{ return (String)this.datos.get(ScsPersonaJGBean.C_IDPAIS);						}
	
	/**
	 * Recupera de la Hashtable el c�digo de la provincia de la persona
	 * @return String 
	 */
	public String getIdProvincia			()						{ return (String)this.datos.get(ScsPersonaJGBean.C_IDPROVINCIA);					}
	
	/**
	 * Recupera de la Hashtable el c�digo postal de la persona 
	 * 
	 * @param C�digo postal solicitante
	 * @return String 
	 */
	public String getIdPoblacion			()						{ return (String)this.datos.get(ScsPersonaJGBean.C_IDPOBLACION);					}
	
	/**
	 * Recupera de la Hashtable el c�digo postal de la persona
	 * @return String 
	 */
	public String getRegimenConyugal		()						{ return (String)this.datos.get(ScsPersonaJGBean.C_REGIMENCONYUGAL);				}
	
	/**
	 * Recupera de la Hashtable el identificador del estado civil
	 * @return String 
	 */
	public String getIdEstadoCivil			()						{ return (String)this.datos.get(ScsPersonaJGBean.C_ESTADOCIVIL);					}
	
	/**
	 * Recupera de la Hashtable el identificador del tel�fono
	 * @return Integer 
	 */
	public String getIdTelefono			()								{ return (String)this.datos.get(ScsTelefonosPersonaBean.C_IDTELEFONO);		}
	
	/**
	 * Recupera de la Hashtable el nombre del tel�fono
	 * @return String 
	 */
	public String getNombreTelefono		()								{ return (String)this.datos.get(ScsTelefonosPersonaBean.C_NOMBRETELEFONO);	}
	
	/**
	 * Recupera de la Hashtable el n�mero de tel�fono
	 * @return String 
	 */
	public String getNumeroTelefono		()								{ return (String)this.datos.get(ScsTelefonosPersonaBean.C_NUMEROTELEFONO);	}
	
	/**
	 * Recupera de la Hashtable la descripci�n de los ingresos anuales
	 * @return void 
	 */
	public String getDescripcionIngresosAnuales	()						{ return (String)this.datos.get(ScsUnidadFamiliarEJGBean.C_DESCRIPCIONINGRESOSANUALES);}
	/** 
	 * Recupera de la Hashtable el importe de los ingresos anuales
	 * @return String 
	 */
	public String getImporteIngresosAnuales		()						{ return (String)this.datos.get(ScsUnidadFamiliarEJGBean.C_IMPORTEINGRESOSANUALES);}
	/** 
	 * Recupera de la Hashtable la descripci�n de los bienes inmuebles
	 * @return String 
	 */
	public String getBienesInmuebles			()						{ return (String)this.datos.get(ScsUnidadFamiliarEJGBean.C_BIENESINMUEBLES);}
	/** 
	 * Recupera de la Hashtable el importe de los bienes inmuebles
	 * @return String 
	 */
	public String getImporteBienesInmuebles		()						{ return (String)this.datos.get(ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESINMUEBLES);}
	/** 
	 * Recupera de la Hashtable la descripci�n de otros bienes
	 * @return String 
	 */
	public String getOtrosBienes				()						{ return (String)this.datos.get(ScsUnidadFamiliarEJGBean.C_OTROSBIENES);}
	/** 
	 * Recupera de la Hashtable el importe de otros bienes
	 * @return String 
	 */
	public String getImporteOtrosBienes			()						{ return (String)this.datos.get(ScsUnidadFamiliarEJGBean.C_IMPORTEOTROSBIENES);}
	
	/** 
	 * Recupera de la Hashtable la descripci�n de los bienes muebles
	 * @return String 
	 */
	public String getBienesMuebles				()						{ return (String)this.datos.get(ScsUnidadFamiliarEJGBean.C_BIENESMUEBLES);}
	/** 
	 * Recupera de la Hashtable el importe de los bienes inmuebles
	 * @return String 
	 */
	public String getImporteBienesMuebles		()						{ return (String)this.datos.get(ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESMUEBLES);}
	/** 
	 * Recupera de la Hashtable si es solicitante
	 * @return String
	 */	
	public String getSolicitante				()						{ 
		return (String)this.datos.get(ScsUnidadFamiliarEJGBean.C_SOLICITANTE);
	}
	/** 
	 * Recupera de la Hashtable en calidad de que est� relacinada la persona con el EJG
	 * @return String 
	 */
	public String getEnCalidadDe				()						{ return (String)this.datos.get(ScsUnidadFamiliarEJGBean.C_ENCALIDADDE);}
	/** 
	 * Recupera de la Hashtable las observaciones
	 * @return String 
	 */
	public String getObservaciones				()						{ return (String)this.datos.get(ScsUnidadFamiliarEJGBean.C_OBSERVACIONES);}
	/**
	 * Almacena en la Hashtable si la persona exist�a en la base de datos 
	 * @return void 
	 */
	public String getExistia					()						{ return (String)this.datos.get("EXISTIA");								}
	public List<DefinirUnidadFamiliarEJGForm> getUnidadFamiliar() {
		return unidadFamiliar;
	}
	public void setUnidadFamiliar(List<DefinirUnidadFamiliarEJGForm> unidadFamiliar) {
		this.unidadFamiliar = unidadFamiliar;
	}
	public ScsUnidadFamiliarEJGBean getUnidadFamiliarEjgVo() throws ClsExceptions {
		ScsUnidadFamiliarEJGBean unidadFamiliarVo = new ScsUnidadFamiliarEJGBean();
		unidadFamiliarVo.setIdInstitucion(new Integer(getIdInstitucion()));
		unidadFamiliarVo.setAnio(new Integer(getAnio()));
		unidadFamiliarVo.setIdTipoEJG(new Integer(getIdTipoEJG()));
		unidadFamiliarVo.setNumero(new Integer(getNumero()));
		unidadFamiliarVo.setIdPersona(new Integer(getIdPersona()));
		peticionEejg = new ScsEejgPeticionesBean();
		if(getIdXml()!=null){
			peticionEejg.setIdXml(new Integer(idXml));
		}
		
		ScsPersonaJGBean personaJG = new ScsPersonaJGBean();
		personaJG.setIdPersona(unidadFamiliarVo.getIdPersona());
		unidadFamiliarVo.setPersonaJG(personaJG);
		unidadFamiliarVo.setPeticionEejg(peticionEejg);
		peticionEejg.setIdioma(idioma);
		
		if(getSolicitante()!=null)
			unidadFamiliarVo.setSolicitante(new Integer(getSolicitante()));
		if(getBienesMuebles()!=null && !getBienesMuebles().equals(""))
			unidadFamiliarVo.setBienesMuebles(getBienesMuebles());
		if(getBienesInmuebles()!=null && !getBienesInmuebles().equals(""))
			unidadFamiliarVo.setBienesInmuebles(getBienesInmuebles());
		if(getOtrosBienes()!=null && !getOtrosBienes().equals(""))
			unidadFamiliarVo.setOtrosBienes(getOtrosBienes());
		if(getDescripcionIngresosAnuales()!=null && !getDescripcionIngresosAnuales().equals(""))
			unidadFamiliarVo.setDescripcionIngresosAnuales(getDescripcionIngresosAnuales());
		
		if(getImporteBienesInmuebles()!=null && !getImporteBienesInmuebles().equals(""))
			unidadFamiliarVo.setImoporteBienesInmuebles(UtilidadesNumero.getDouble(getImporteBienesInmuebles()));
		if(getImporteBienesMuebles()!=null && !getImporteBienesMuebles().equals(""))
			unidadFamiliarVo.setImoporteBienesMuebles(UtilidadesNumero.getDouble(getImporteBienesMuebles()));
		if(getImporteOtrosBienes()!=null && !getImporteOtrosBienes().equals(""))
			unidadFamiliarVo.setImporteOtrosBienes(UtilidadesNumero.getDouble(getImporteOtrosBienes()));
		if(getImporteIngresosAnuales()!=null && !getImporteIngresosAnuales().equals(""))
			unidadFamiliarVo.setIngresosAnuales(UtilidadesNumero.getDouble(getImporteIngresosAnuales()));
		
		if(getEnCalidadDe()!=null && !getEnCalidadDe().equals(""))
			unidadFamiliarVo.setEnCalidadDe(getEnCalidadDe());
		if(getIdParentesco()!=null && !getIdParentesco().equals("")){
			unidadFamiliarVo.setIdParentesco(new Integer(getIdParentesco()));
			ScsParentescoBean parentesco = new ScsParentescoBean();
			parentesco.setIdParentesco(unidadFamiliarVo.getIdParentesco());
			unidadFamiliarVo.setParentesco(parentesco);
			
		}else if(getParentesco()!=null){
			unidadFamiliarVo.setParentesco(getParentesco());
			
		}
		
		if(getObservaciones()!=null && !getObservaciones().equals(""))
			unidadFamiliarVo.setObservaciones(getObservaciones());
		
		if(getIdTipoGrupoLab()!=null && !getIdTipoGrupoLab().equals(""))
			unidadFamiliarVo.setTipoGrupoLab(new Integer(getIdTipoGrupoLab()));
		
		if(getIdTipoIngreso()!=null && !getIdTipoIngreso().equals(""))
			unidadFamiliarVo.setTipoIngreso(new Integer(getIdTipoIngreso()));
		
		
		
		
		
		return unidadFamiliarVo;
	}
	public String getIdParentesco() {
		return idParentesco;
	}
	public void setIdParentesco(String idParentesco) {
		this.idParentesco = idParentesco;
	}
	public String getIdTipoGrupoLab() {
		return idTipoGrupoLab;
	}
	public void setIdTipoGrupoLab(String idTipoGrupoLab) {
		this.idTipoGrupoLab = idTipoGrupoLab;
	}
	public String getIdTipoIngreso() {
		return idTipoIngreso;
	}
	public void setIdTipoIngreso(String idTipoIngreso) {
		this.idTipoIngreso = idTipoIngreso;
	}
	public String getBotones() {
		String botones = "";
		if (!getModo().equalsIgnoreCase("ver")){
			botones = "C,E,B";
		}
		this.setBotones(botones);
		
		
		return botones;
	}
	public void setBotones(String botones) {
		this.botones = botones;
	}
	/*
	private String getIdLenguaje(String idioma){
		String idLenguaje = "1";
		if(idioma!=null && idioma.toUpperCase().equals("ES_ES"))
			idLenguaje = "1";
		else if(idioma!=null && idioma.toUpperCase().equals("CA_ES"))
			idLenguaje = "2";
		else if(idioma!=null && idioma.toUpperCase().equals("GL_ES"))
			idLenguaje = "3";
		else if(idioma!=null && idioma.toUpperCase().equals("EU_ES"))
			idLenguaje = "4";
		return idLenguaje;
		
		
	}
	
	public FilaExtElement[] getElementosFilaNew() {
		FilaExtElement[] elementosFila = null;
		
		if (getModo().equalsIgnoreCase("ver")){
			elementosFila = new FilaExtElement[3];
		}else{
			//System.out.println("ejg"+ejg);
			//System.out.println("ejg.getestado"+ejg.getIdEstadoEjg());
			if(permisoEejg!=null && permisoEejg.booleanValue() && 
					(personaJG!=null && personaJG.getNif()!=null && !personaJG.getNif().trim().equals("") &&personaJG.getTipoIdentificacion()!=null&&!personaJG.getTipoIdentificacion().equalsIgnoreCase("")&&(Integer.parseInt(personaJG.getTipoIdentificacion())==ClsConstants.TIPO_IDENTIFICACION_NIF||Integer.parseInt(personaJG.getTipoIdentificacion())==ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE) )
					&& ejg!=null && ((ejg.getIdEstadoEjg()==null || (ejg.getIdEstadoEjg()!=null&&ejg.getIdEstadoEjg().shortValue()<9))||esComision)){
				
				
				if(getPeticionEejg()!=null){
					int	estado = getPeticionEejg().getEstado();
					String idLenguajeMsg = getIdLenguaje(getPeticionEejg().getIdioma());
					switch (estado) {
						case ScsEejgPeticionesBean.EEJG_ESTADO_INICIAL:
							elementosFila = new FilaExtElement[5];
							elementosFila[3] = new FilaExtElement("espera", "esperaEejg",UtilidadesString.getMensajeIdioma(idLenguajeMsg, "general.boton.esperaEejg")+" "+UtilidadesString.getMensajeIdioma(idLenguajeMsg, "eejg.usuariopeticion")+": "+peticionEejg.getUsuarioPeticion().getDescripcion(),SIGAConstants.ACCESS_READ);
							elementosFila[4] = new FilaExtElement("comunicar", "comunicar",	SIGAConstants.ACCESS_READ);
						break;
						case ScsEejgPeticionesBean.EEJG_ESTADO_PENDIENTE_INFO:
							elementosFila = new FilaExtElement[6];
							elementosFila[3] = new FilaExtElement("espera", "avisoEsperaInfoEejg",UtilidadesString.getMensajeIdioma(idLenguajeMsg, "general.boton.esperaInfoEejg")+" "+UtilidadesString.getMensajeIdioma(idLenguajeMsg, "eejg.usuariopeticion")+": "+peticionEejg.getUsuarioPeticion().getDescripcion(),SIGAConstants.ACCESS_READ);
							elementosFila[4] = new FilaExtElement("download", "esperaInfoEejg",UtilidadesString.getMensajeIdioma(idLenguajeMsg, "general.boton.descargarEejg")+" "+UtilidadesString.getMensajeIdioma(idLenguajeMsg, "eejg.usuariopeticion")+": "+peticionEejg.getUsuarioPeticion().getDescripcion(),	SIGAConstants.ACCESS_READ);
							elementosFila[5] = new FilaExtElement("comunicar", "comunicar",	SIGAConstants.ACCESS_READ);
						break;
						case ScsEejgPeticionesBean.EEJG_ESTADO_ESPERA:case ScsEejgPeticionesBean.EEJG_ESTADO_ESPERA_ESPERANDO:case ScsEejgPeticionesBean.EEJG_ESTADO_INICIAL_ESPERANDO:
							elementosFila = new FilaExtElement[5];
							elementosFila[3] = new FilaExtElement("espera", "esperaAdministracionesEejg",UtilidadesString.getMensajeIdioma(idLenguajeMsg, "general.boton.esperaAdministracionesEejg")+" "+UtilidadesString.getMensajeIdioma(idLenguajeMsg, "eejg.usuariopeticion")+": "+peticionEejg.getUsuarioPeticion().getDescripcion(),SIGAConstants.ACCESS_READ);
							elementosFila[4] = new FilaExtElement("comunicar", "comunicar",	SIGAConstants.ACCESS_READ);
						break;
						case ScsEejgPeticionesBean.EEJG_ESTADO_ERROR_SOLICITUD:case ScsEejgPeticionesBean.EEJG_ESTADO_ERROR_CONSULTA_INFO:
							elementosFila = new FilaExtElement[6];
							elementosFila[3] = new FilaExtElement(null, "solicitarEejg","general.boton.solicitarEejg",	SIGAConstants.ACCESS_READ,"general.boton.solicitudEejg","90");
							elementosFila[4] = new FilaExtElement("descargaLog", "errorEejg",UtilidadesString.getMensajeIdioma(idLenguajeMsg, "general.boton.errorEejg")+" "+UtilidadesString.getMensajeIdioma(idLenguajeMsg, "eejg.usuariopeticion")+": "+peticionEejg.getUsuarioPeticion().getDescripcion(),SIGAConstants.ACCESS_READ);
							elementosFila[5] = new FilaExtElement("comunicar", "comunicar",	SIGAConstants.ACCESS_READ);
						break;
						case ScsEejgPeticionesBean.EEJG_ESTADO_FINALIZADO:
							elementosFila = new FilaExtElement[5];
							elementosFila[3] = new FilaExtElement("download", "descargarEejg",UtilidadesString.getMensajeIdioma(idLenguajeMsg, "general.boton.descargarEejg")+" "+UtilidadesString.getMensajeIdioma(idLenguajeMsg, "eejg.usuariopeticion")+": "+peticionEejg.getUsuarioPeticion().getDescripcion(),	SIGAConstants.ACCESS_READ);
							elementosFila[4] = new FilaExtElement("comunicar", "comunicar",	SIGAConstants.ACCESS_READ);
						break;
					default:
						elementosFila = new FilaExtElement[5];
						elementosFila[3] = new FilaExtElement(null, "solicitarEejg","general.boton.solicitarEejg",	SIGAConstants.ACCESS_READ,"general.boton.solicitudEejg","90");
						elementosFila[4] = new FilaExtElement("comunicar", "comunicar",	SIGAConstants.ACCESS_READ);
						break;
					}
				}else{
					if(esComision){
						// A la comision no les dejaremos solicitar
						elementosFila = new FilaExtElement[4];
						elementosFila[3] = new FilaExtElement("comunicar", "comunicar",	SIGAConstants.ACCESS_READ);
					}else{
						elementosFila = new FilaExtElement[5];
						elementosFila[3] = new FilaExtElement(null, "solicitarEejg","general.boton.solicitarEejg",	SIGAConstants.ACCESS_READ,"general.boton.solicitudEejg","100");
						elementosFila[4] = new FilaExtElement("comunicar", "comunicar",	SIGAConstants.ACCESS_READ);
					}
				}
			}else{
				elementosFila = new FilaExtElement[4];
				elementosFila[3] = new FilaExtElement("comunicar", "comunicar",	SIGAConstants.ACCESS_READ);
			}
		}
		this.setElementosFila(elementosFila);
		return elementosFila;
	}*/
	public FilaExtElement[] getElementosFila() {
		FilaExtElement[] elementosFila = null;
		
		if (getModo().equalsIgnoreCase("ver")){
			elementosFila = new FilaExtElement[3];
		}else{

//			Si es comision solo podra comunicar
//			Si es el colegio podra comunicar y solictar los expedientes siempre que cumpla todas las condiciones siguientes:
//				La persona tenga TIPO_IDENTIFICACION_NIF o TIPO_IDENTIFICACION_TRESIDENTE
//				La persona tenga numero de identificacion(que sera correcto ya que por interfaz se valida)	
//					No Exista estado del Expediente 
//						o 
//					El estado en el que se encuentra el expediente no sea un estado de la comision
			
			if(permisoEejg!=null && permisoEejg.booleanValue() && !esComision){
				if(
						personaJG!=null && personaJG.getNif()!=null 
						&& !personaJG.getNif().trim().equals("") 
						&&	personaJG.getTipoIdentificacion()!=null 
						&& !personaJG.getTipoIdentificacion().equalsIgnoreCase("")
						&&(Integer.parseInt(personaJG.getTipoIdentificacion())==ClsConstants.TIPO_IDENTIFICACION_NIF||Integer.parseInt(personaJG.getTipoIdentificacion())==ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE) 
						&& ejg!=null 
						&& (ejg.getIdEstadoEjg()==null || (ejg.getIdEstadoEjg()!=null && ejg.getMaestroEstadoEJG().getVisiblecomision().equals(ClsConstants.DB_FALSE)))
							
						
				)
				{
					if(getPeticionEejg()!=null){
						int	estado = getPeticionEejg().getEstado();
							
						if (estado == EEJG_ESTADO.INICIAL.getId()) {
								elementosFila = new FilaExtElement[4];
								//elementosFila[3] = new FilaExtElement("espera", "esperaEejg","general.boton.esperaEejg",SIGAConstants.ACCESS_READ);
								elementosFila[3] = new FilaExtElement("comunicar", "comunicar",	SIGAConstants.ACCESS_READ);
						} else if (estado == EEJG_ESTADO.PENDIENTE_INFO.getId()) {
								elementosFila = new FilaExtElement[4];
								//elementosFila[3] = new FilaExtElement("espera", "avisoEsperaInfoEejg","general.boton.esperaInfoEejg",SIGAConstants.ACCESS_READ);
								//elementosFila[4] = new FilaExtElement("download", "esperaInfoEejg","general.boton.descargarEejg",	SIGAConstants.ACCESS_READ);
								elementosFila[3] = new FilaExtElement("comunicar", "comunicar",	SIGAConstants.ACCESS_READ);
						} else if (estado == EEJG_ESTADO.ESPERA.getId() || estado == EEJG_ESTADO.ESPERA_ESPERANDO.getId() || estado == EEJG_ESTADO.INICIAL_ESPERANDO.getId()) {
								elementosFila = new FilaExtElement[4];
								//elementosFila[3] = new FilaExtElement("espera", "esperaAdministracionesEejg","general.boton.esperaAdministracionesEejg",SIGAConstants.ACCESS_READ);
								elementosFila[3] = new FilaExtElement("comunicar", "comunicar",	SIGAConstants.ACCESS_READ);
						} else if (estado == EEJG_ESTADO.ERROR_SOLICITUD.getId() || estado == EEJG_ESTADO.ERROR_CONSULTA_INFO.getId()) {
								elementosFila = new FilaExtElement[5];
								elementosFila[3] = new FilaExtElement(null, "solicitarEejg","general.boton.solicitarEejg",	SIGAConstants.ACCESS_READ,"general.boton.solicitudEejg","90");
								//elementosFila[4] = new FilaExtElement("descargaLog", "errorEejg","general.boton.errorEejg",SIGAConstants.ACCESS_READ);
								elementosFila[4] = new FilaExtElement("comunicar", "comunicar",	SIGAConstants.ACCESS_READ);
						} else if (estado ==  EEJG_ESTADO.FINALIZADO.getId()) {
								elementosFila = new FilaExtElement[5];
								elementosFila[3] = new FilaExtElement(null, "solicitarEejg","general.boton.solicitarEejg",	SIGAConstants.ACCESS_READ,"general.boton.solicitudEejg","90");
								//elementosFila[3] = new FilaExtElement("download", "descargarEejg","general.boton.descargarEejg",	SIGAConstants.ACCESS_READ);
								elementosFila[4] = new FilaExtElement("comunicar", "comunicar",	SIGAConstants.ACCESS_READ);
						} else {
							elementosFila = new FilaExtElement[5];
							elementosFila[3] = new FilaExtElement(null, "solicitarEejg","general.boton.solicitarEejg",	SIGAConstants.ACCESS_READ,"general.boton.solicitudEejg","90");
							elementosFila[4] = new FilaExtElement("comunicar", "comunicar",	SIGAConstants.ACCESS_READ);						
						}
					}else{
						
							elementosFila = new FilaExtElement[5];
							elementosFila[3] = new FilaExtElement(null, "solicitarEejg","general.boton.solicitarEejg",	SIGAConstants.ACCESS_READ,"general.boton.solicitudEejg","100");
							elementosFila[4] = new FilaExtElement("comunicar", "comunicar",	SIGAConstants.ACCESS_READ);
						
					}
				}else{
					elementosFila = new FilaExtElement[4];
					elementosFila[3] = new FilaExtElement("comunicar", "comunicar",	SIGAConstants.ACCESS_READ);
					
				}
				
			}else{
				elementosFila = new FilaExtElement[4];
				elementosFila[3] = new FilaExtElement("comunicar", "comunicar",	SIGAConstants.ACCESS_READ);
				
			}
					
		}
		this.setElementosFila(elementosFila);
		return elementosFila;
	}
	public void setElementosFila(FilaExtElement[] elementosFila) {
		this.elementosFila = elementosFila;
	}
	public ScsPersonaJGBean getPersonaJG() {
		return personaJG;
	}
	public void setPersonaJG(ScsPersonaJGBean personaJG) {
		this.personaJG = personaJG;
	}
	public ScsEJGBean getEjg() {
		return ejg;
	}
	public void setEjg(ScsEJGBean ejg) {
		this.ejg = ejg;
	}
	public ScsParentescoBean getParentesco() {
		return parentesco;
	}
	public void setParentesco(ScsParentescoBean parentesco) {
		this.parentesco = parentesco;
	}
	public ScsEejgPeticionesBean getPeticionEejg() {
		return peticionEejg;
	}
	public void setPeticionEejg(ScsEejgPeticionesBean peticionEejg) {
		this.peticionEejg = peticionEejg;
	}
	public String getDatosInforme() {
		return datosInforme;
	}
	public void setDatosInforme(String datosInforme) {
		this.datosInforme = datosInforme;
	}
	public String getIdXml() {
		return idXml;
	}
	public void setIdXml(String idXml) {
		this.idXml = idXml;
	}
	public Boolean getPermisoEejg() {
		return permisoEejg;
	}
	public void setPermisoEejg(Boolean permisoEejg) {
		this.permisoEejg = permisoEejg;
	}
	public String getIdioma() {
		return idioma;
	}
	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}
	public List<ScsEejgPeticionesBean> getPeticionesEejg() {
		return peticionesEejg;
	}
	public void setPeticionesEejg(List<ScsEejgPeticionesBean> peticionesEejg) {
		this.peticionesEejg = peticionesEejg;
	}
	
}