/*
 * Fecha creación: 01/02/2005
 * Autor: julio.vicente
 */

package com.siga.gratuita.form;

import java.util.List;

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
	
	private List<DefinirUnidadFamiliarEJGForm> unidadFamiliar;
	ScsParentescoBean parentesco;
	ScsPersonaJGBean personaJG;
	ScsEejgPeticionesBean peticionEejg;
	ScsEJGBean ejg ;
	private String botones;
	FilaExtElement[] elementosFila;
	
	
	
	private String idParentesco;
	private String idTipoGrupoLab;
	private String idTipoIngreso;
	private String idPaisAux="idPaisAux";
	private String idProvinciaAux="idProvinciaAux";
	private String idPoblacionAux="idPoblacionAux";
	private String idEstadoCivilAux="idEstadoCivilAux";
	private String regimenConyugalAux="regimenConyugalAux";
	
	public void    setIdPaisAux				 (String	valor)	{ this.datos.put(this.idPaisAux, valor);}
	public void    setIdProvinciaAux		 (String	valor)	{ this.datos.put(this.idProvinciaAux, valor);}
	public void    setIdPoblacionAux		 (String	valor)	{ this.datos.put(this.idPoblacionAux, valor);}
	public void    setIdEstadoCivilAux		 (String	valor)	{ this.datos.put(this.idEstadoCivilAux, valor);}
	public void    setRegimenConyugalAux	 (String	valor)	{ this.datos.put(this.regimenConyugalAux, valor);}
	public String getIdPaisAux	 			()	{return (String)this.datos.get(this.idPaisAux);}
	public String getIdProvinciaAux		 	()	{return (String)this.datos.get(this.idProvinciaAux);}
	public String getIdPoblacionAux		 	()	{return (String)this.datos.get(this.idPoblacionAux);}
	public String getIdEstadoCivilAux	 	()	{return (String)this.datos.get(this.idEstadoCivilAux);}
	public String getRegimenConyugalAux		()	{return (String)this.datos.get(this.regimenConyugalAux);}

	// Metodos Set (Formulario (*.jsp))
	
 	/**
	 * Almacena en la Hashtable si la persona existía en la base de datos 
	 * 
	 * @param Si existía o no 
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
	 * Almacena en la Hashtable la dirección de la persona 
	 * 
	 * @param Dirección solicitante
	 * @return void 
	 */
	public void setDireccion				(String direccion)			{ this.datos.put(ScsPersonaJGBean.C_DIRECCION, direccion);							}
	
	/**
	 * Almacena en la Hashtable el código postal de la persona 
	 * 
	 * @param Código postal solicitante
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
	 * Almacena en la Hashtable el código de profesión de la persona 
	 * 
	 * @param Código profesión solicitante
	 * @return void 
	 */
	public void setIdProfesion				(String idprofesion)		{ this.datos.put(ScsPersonaJGBean.C_IDPROFESION, idprofesion);						}
	
	/**
	 * Almacena en la Hashtable el código del país de la persona 
	 * 
	 * @param Código país solicitante
	 * @return void 
	 */
	public void setIdPais					(String pais)				{ this.datos.put(ScsPersonaJGBean.C_IDPAIS, pais);									}
	
	/**
	 * Almacena en la Hashtable el código de la provincia de la persona 
	 * 
	 * @param Código postal solicitante
	 * @return void 
	 */
	public void setIdProvincia				(String provincia)			{ this.datos.put(ScsPersonaJGBean.C_IDPROVINCIA, provincia);						}
	
	/**
	 * Almacena en la Hashtable el código postal de la persona 
	 * 
	 * @param Código postal solicitante
	 * @return void 
	 */
	public void setIdPoblacion				(String poblacion)			{ this.datos.put(ScsPersonaJGBean.C_IDPOBLACION, poblacion);						}
	
	/**
	 * Almacena en la Hashtable el código postal de la persona 
	 * 
	 * @param Código postal solicitante
	 * @return void 
	 */
	public void setRegimenConyugal			(String regimen)			{ this.datos.put(ScsPersonaJGBean.C_REGIMENCONYUGAL, regimen);						}
	
	/**
	 * Recupera de la Hashtable el identificador del estado civil
	 * @return String 
	 */
	public void setIdEstadoCivil			(String estado)				{ this.datos.put(ScsPersonaJGBean.C_ESTADOCIVIL, estado);							}
	
	/**
	 * Almacena en la Hashtable el identificador del teléfono 
	 * 
	 * @param Identificador teléfono
	 * @return void 
	 */
	public void setIdTelefono		 	(String idTelefono)				{ this.datos.put(ScsTelefonosPersonaBean.C_IDTELEFONO, idTelefono);					}
	
	/**
	 * Almacena en la Hashtable el nombre del teléfono 
	 * 
	 * @param Nombre teléfono
	 * @return void 
	 */
	public void setNombreTelefono		(String nombreTelefono)			{ this.datos.put(ScsTelefonosPersonaBean.C_NOMBRETELEFONO, nombreTelefono);			}
	
	/**
	 * Almacena en la Hashtable el número de teléfono 
	 * 
	 * @param Número teléfono
	 * @return void 
	 */
	public void setNumeroTelefono		(String numeroTelefono)			{ this.datos.put(ScsTelefonosPersonaBean.C_NUMEROTELEFONO, numeroTelefono);			}	
	/**
	 * Almacena en la Hashtable la descripción de los ingresos anuales 
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
	 * Almacena en la Hashtable la descripción de los bienes inmuebles 
	 * 
	 * @param Descripción bienes inmuebles
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
	 * Almacena en la Hashtable la descripción de los bienes muebles 
	 * 
	 * @param Descripción bienes inmuebles
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
	 * Almacena en la Hashtable la descripción de otros bienes 
	 * 
	 * @param Descripción otros bienes
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
	 * Almacena en la Hashtable en calidad de que está relacinada la persona con el EJG
	 * 
	 * @param En calidad de que está relacionado
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
	public String getIdPersona				()							{ return this.datos.get(ScsSOJBean.C_IDPERSONA).toString();						}
	
	/**
	 * Recupera de la Hashtable la institucion de un SOJ 
	 * @return String
	 */
	public String getIdInstitucion	 		()							{return this.datos.get(ScsSOJBean.C_IDINSTITUCION).toString();					}
	/**
	 * Recupera de la Hashtable el anho de la EJG
	 * 
	 * @return Anho de la EJG
	 */
	public String getAnio 					()		{ return this.datos.get(ScsEJGBean.C_ANIO).toString();}
	/**
	 * Recupera de la Hashtable el numero de la EJG
	 * 
	 * @return Numero de la EJG
	 */
	public String getNumero 				()		{ return this.datos.get(ScsEJGBean.C_NUMERO).toString();}
	/**
	 * Recupera de la Hashtable el identificador del tipo  de la EJG
	 * 
	 * @return Identificador del tipo de la EJG
	 */
	public String getIdTipoEJG				()		{ return this.datos.get(ScsEJGBean.C_IDTIPOEJG).toString();}
	
	/**
	 * Recupera de la Hashtable el nombre del solicitante de un SOJ
	 * @return String 
	 */
	public String getNombre		 			()						{ return this.datos.get(ScsPersonaJGBean.C_NOMBRE).toString();						}
	
	/**
	 * Recupera de la Hashtable el apellido del solicitante de un SOJ
	 * @return String 
	 */
	public String getApellido1	 			()						{ return this.datos.get(ScsPersonaJGBean.C_APELLIDO1).toString();					}
	
	/**
	 * Recupera de la Hashtable el apellido del solicitante de un SOJ 
	 * @return String 
	 */
	public String getApellido2	 			()						{ return this.datos.get(ScsPersonaJGBean.C_APELLIDO2).toString();					}
	
	/**
	 * Recupera de la Hashtable el NIF de la persona
	 * @return String
	 */
	public String getNif					()						{ return this.datos.get(ScsPersonaJGBean.C_NIF).toString();							}
	
	/**
	 * Recupera de la Hashtable la dirección de la persona
	 * @return String
	 */
	public String getDireccion				()						{ return this.datos.get(ScsPersonaJGBean.C_DIRECCION).toString();					}
	
	/**
	 * Recupera de la Hashtable el código postal de la persona
	 * @return String
	 */
	public String getCodigoPostal			()						{ return this.datos.get(ScsPersonaJGBean.C_CODIGOPOSTAL).toString();				}
	
	/**
	 * Recupera de la Hashtable la fecha de nacimiento de la persona
	 * @return String
	 */
	public String getFechaNacimiento		()						{ return this.datos.get(ScsPersonaJGBean.C_FECHANACIMIENTO).toString();				}
	
	/**
	 * Recupera de la Hashtable el código de profesión de la persona
	 * @return String
	 */
	public String getIdProfesion			()						{ return this.datos.get(ScsPersonaJGBean.C_IDPROFESION).toString();					}
	
	/**
	 * Recupera de la Hashtable el código del país de la persona
	 * @return String 
	 */
	public String getIdPais					()						{ return this.datos.get(ScsPersonaJGBean.C_IDPAIS).toString();						}
	
	/**
	 * Recupera de la Hashtable el código de la provincia de la persona
	 * @return String 
	 */
	public String getIdProvincia			()						{ return this.datos.get(ScsPersonaJGBean.C_IDPROVINCIA).toString();					}
	
	/**
	 * Recupera de la Hashtable el código postal de la persona 
	 * 
	 * @param Código postal solicitante
	 * @return String 
	 */
	public String getIdPoblacion			()						{ return this.datos.get(ScsPersonaJGBean.C_IDPOBLACION).toString();					}
	
	/**
	 * Recupera de la Hashtable el código postal de la persona
	 * @return String 
	 */
	public String getRegimenConyugal		()						{ return this.datos.get(ScsPersonaJGBean.C_REGIMENCONYUGAL).toString();				}
	
	/**
	 * Recupera de la Hashtable el identificador del estado civil
	 * @return String 
	 */
	public String getIdEstadoCivil			()						{ return this.datos.get(ScsPersonaJGBean.C_ESTADOCIVIL).toString();					}
	
	/**
	 * Recupera de la Hashtable el identificador del teléfono
	 * @return Integer 
	 */
	public String getIdTelefono			()								{ return this.datos.get(ScsTelefonosPersonaBean.C_IDTELEFONO).toString();		}
	
	/**
	 * Recupera de la Hashtable el nombre del teléfono
	 * @return String 
	 */
	public String getNombreTelefono		()								{ return this.datos.get(ScsTelefonosPersonaBean.C_NOMBRETELEFONO).toString();	}
	
	/**
	 * Recupera de la Hashtable el número de teléfono
	 * @return String 
	 */
	public String getNumeroTelefono		()								{ return this.datos.get(ScsTelefonosPersonaBean.C_NUMEROTELEFONO).toString();	}
	
	/**
	 * Recupera de la Hashtable la descripción de los ingresos anuales
	 * @return void 
	 */
	public String getDescripcionIngresosAnuales	()						{ return this.datos.get(ScsUnidadFamiliarEJGBean.C_DESCRIPCIONINGRESOSANUALES).toString();}
	/** 
	 * Recupera de la Hashtable el importe de los ingresos anuales
	 * @return String 
	 */
	public String getImporteIngresosAnuales		()						{ return this.datos.get(ScsUnidadFamiliarEJGBean.C_IMPORTEINGRESOSANUALES).toString();}
	/** 
	 * Recupera de la Hashtable la descripción de los bienes inmuebles
	 * @return String 
	 */
	public String getBienesInmuebles			()						{ return this.datos.get(ScsUnidadFamiliarEJGBean.C_BIENESINMUEBLES).toString();}
	/** 
	 * Recupera de la Hashtable el importe de los bienes inmuebles
	 * @return String 
	 */
	public String getImporteBienesInmuebles		()						{ return this.datos.get(ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESINMUEBLES).toString();}
	/** 
	 * Recupera de la Hashtable la descripción de otros bienes
	 * @return String 
	 */
	public String getOtrosBienes				()						{ return this.datos.get(ScsUnidadFamiliarEJGBean.C_OTROSBIENES).toString();}
	/** 
	 * Recupera de la Hashtable el importe de otros bienes
	 * @return String 
	 */
	public String getImporteOtrosBienes			()						{ return this.datos.get(ScsUnidadFamiliarEJGBean.C_IMPORTEOTROSBIENES).toString();}
	
	/** 
	 * Recupera de la Hashtable la descripción de los bienes muebles
	 * @return String 
	 */
	public String getBienesMuebles				()						{ return this.datos.get(ScsUnidadFamiliarEJGBean.C_BIENESMUEBLES).toString();}
	/** 
	 * Recupera de la Hashtable el importe de los bienes inmuebles
	 * @return String 
	 */
	public String getImporteBienesMuebles		()						{ return this.datos.get(ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESMUEBLES).toString();}
	/** 
	 * Recupera de la Hashtable si es solicitante
	 * @return String
	 */	
	public String getSolicitante				()						{ return this.datos.get(ScsUnidadFamiliarEJGBean.C_SOLICITANTE).toString();}
	/** 
	 * Recupera de la Hashtable en calidad de que está relacinada la persona con el EJG
	 * @return String 
	 */
	public String getEnCalidadDe				()						{ return this.datos.get(ScsUnidadFamiliarEJGBean.C_ENCALIDADDE).toString();}
	/** 
	 * Recupera de la Hashtable las observaciones
	 * @return String 
	 */
	public String getObservaciones				()						{ return this.datos.get(ScsUnidadFamiliarEJGBean.C_OBSERVACIONES).toString();}
	/**
	 * Almacena en la Hashtable si la persona existía en la base de datos 
	 * @return void 
	 */
	public String getExistia					()						{ return this.datos.get("EXISTIA").toString();								}
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
		ScsPersonaJGBean personaJG = new ScsPersonaJGBean();
		personaJG.setIdPersona(unidadFamiliarVo.getIdPersona());
		unidadFamiliarVo.setPersonaJG(personaJG);
		unidadFamiliarVo.setSolicitante(new Integer(getSolicitante()));
		unidadFamiliarVo.setPeticionEejg(peticionEejg);
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
	
	public FilaExtElement[] getElementosFila() {
		FilaExtElement[] elementosFila = null;
		if (getModo().equalsIgnoreCase("ver")){
			elementosFila = new FilaExtElement[3];
		}else{
			elementosFila = new FilaExtElement[5];
			elementosFila[3] = new FilaExtElement("comunicar", "comunicar",	SIGAConstants.ACCESS_READ);
			if(getPeticionEejg()!=null){
				int estado = 0;
				try {
					estado = getPeticionEejg().getEstado();
					
				} catch (Exception e) {
					System.out.println(e.toString());
					// TODO: handle exception
				}
				
				switch (estado) {
					case ScsEejgPeticionesBean.EEJG_ESTADO_INICIAL:case ScsEejgPeticionesBean.EEJG_ESTADO_ESPERA:
						elementosFila[4] = new FilaExtElement("verSolicitud", "esperaEejg","esperaEejg",	SIGAConstants.ACCESS_DENY);
					break;
					case ScsEejgPeticionesBean.EEJG_ESTADO_ERROR_SOLICITUD:case ScsEejgPeticionesBean.EEJG_ESTADO_ERROR_CONSULTA_INFO:
						elementosFila[4] = new FilaExtElement("solicitar", "solicitarEejg","errorEejg",SIGAConstants.ACCESS_READ);
					break;
					case ScsEejgPeticionesBean.EEJG_ESTADO_FINALIZADO:
						elementosFila[4] = new FilaExtElement("descargar", "descargarEejg","descargarEejg",	SIGAConstants.ACCESS_READ);
					break;
				default:
					break;
				}
			}else{
				elementosFila[4] = new FilaExtElement("verSolicitud", "solicitarEejg","solicitarEejg",	SIGAConstants.ACCESS_READ);
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
	
}