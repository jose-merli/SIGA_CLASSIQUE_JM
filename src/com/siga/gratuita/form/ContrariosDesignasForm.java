package com.siga.gratuita.form;

import com.siga.beans.ScsContrariosDesignaBean;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.beans.ScsSOJBean;
import com.siga.beans.ScsTelefonosPersonaBean;
import com.siga.general.MasterForm;

/**
 * @author ruben.fernandez
 * @since 3/2/2005
 * @version 07/02/2006 (david.sachezp): nuevos campos Procurador
 */



public class ContrariosDesignasForm extends MasterForm {
			
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
		
	public void setNuevo				(String valor)			{ this.datos.put("NUEVO", valor);						}
	public String getNuevo				()			{ return (String)this.datos.get("NUEVO");}
	/**
		 * Almacena en la Hashtable el identificador de una persona 
		 * 
		 * @param Identificador solicitante SOJ 
		 * @return void 
		 */
		public void setIdPersona				(String idPersona)			{ this.datos.put(ScsPersonaJGBean.C_IDPERSONA, idPersona);						}
		/**
		 * Almacena en la Hashtable el identificador de una persona 
		 * 
		 * @param Identificador solicitante SOJ 
		 * @return void 
		 */
		public void setObservaciones				(String idPersona)			{ this.datos.put(ScsContrariosDesignaBean.C_OBSERVACIONES, idPersona);						}
		/**
		 * Almacena en la Hashtable el identificador de una persona 
		 * 
		 * @param Identificador solicitante SOJ 
		 * @return void 
		 */
		public void setNombreRepresentante				(String idPersona)			{ this.datos.put(ScsContrariosDesignaBean.C_NOMBREREPRESENTANTE, idPersona);						}
		
		/**
		 * Almacena en la Hashtable la institucion de la persona 
		 * 
		 * @param Institucion persona 
		 * @return void 
		 */
		public void setIdInstitucion	 		(String idInstitucion)		{ this.datos.put(ScsPersonaJGBean.C_IDINSTITUCION, idInstitucion);				}
		
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
		
		
		public void setProcurador		(String dato)			{ this.datos.put("PROCURADOR", dato);			}
		
		
		
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
		 * Recupera de la Hashtable el número de teléfono
		 * @return String 
		 */
		public String getNombreRepresentante				()			{ return (String)this.datos.get(ScsContrariosDesignaBean.C_NOMBREREPRESENTANTE);						}
		/**
		 * Almacena en la Hashtable el identificador de una persona 
		 * 
		 * @param Identificador solicitante SOJ 
		 * @return void 
		 */
		public String getObservaciones				()			{ return (String)this.datos.get(ScsContrariosDesignaBean.C_OBSERVACIONES);						}
	
		public String getProcurador				()			{ return (String)this.datos.get("PROCURADOR");						}
		
		
	}