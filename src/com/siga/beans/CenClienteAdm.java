
package com.siga.beans;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.GstStringTokenizer;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.GestorContadores;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.PaginadorCaseSensitiveBind;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.censo.action.Direccion;
import com.siga.censo.form.BusquedaClientesForm;
import com.siga.general.CenVisibilidad;
import com.siga.general.EjecucionPLs;
import com.siga.general.SIGAException;
import com.siga.informes.InformeCertificadosEspeciales;


/**
 * Administrador de Cliente
 * 
 * @author daniel.campos
 * @since 22-11-2004
 * @version miguel.villegas - 10-01-2005 - incorporacion 
 *  getNumColegiosEsCliente () y eliminarNoColegiado ()
 * @version raul.ggonzalez - 11-01-2005 - incorporacion 
 *  getDatosPersonalesCliente () y getGrupos ()
 * @version luismiguel.sanchezp - 22-03-2005 - Se anhade el m�todo 
 *  getDatosCertificado ()
 * @version david.sanchezp - 27-12-2005 - incluir el campo sexo
 * @version RGG - CAMBIO VISIBILIDAD 
 *  public class CenClienteAdm extends MasterBeanAdministrador {
 * @version adrian.ayala - 05-06-2008 - Limpieza de altaColegial ()
 */
public class CenClienteAdm extends MasterBeanAdmVisible
{
	private static final Logger log = Logger.getLogger(CenClienteAdm.class);
	public static final int TIPOCLIENTE_PROD_UNICO = 0;
	public static final int TIPOCLIENTE_PROD_NOC = 10; // No-colegiado
	public static final int TIPOCLIENTE_PROD_COL = 20; // Colegiado no residente
	public static final int TIPOCLIENTE_PROD_RES = 30; // Colegiado residente
	public static final int TIPOCLIENTE_PROD_NOCPRO = 40; // No-colegiado en colegio en produccion
	public static final int TIPOCLIENTE_PROD_COLPRO = 50; // Colegiado no residente en colegio en produccion
	public static final int TIPOCLIENTE_PROD_RESPRO = 60; // Colegiado residente en colegio en produccion

	//////////////////// CONSTRUCTORES ////////////////////
	public CenClienteAdm (UsrBean usuario) {
		super (CenClienteBean.T_NOMBRETABLA, usuario);
	}
	
	public CenClienteAdm (Integer usuario, UsrBean usrbean, 
			  int idInstitucionCliente, long idPersonaCliente)
	{
		super (CenClienteBean.T_NOMBRETABLA, usuario, usrbean, 
			idInstitucionCliente, idPersonaCliente);
	}
	
	
	
	//////////////////// METODOS DE ADMINISTRADOR ////////////////////
	protected String[] getCamposBean ()
	{
		String [] campos = 
		{
				CenClienteBean.C_ABONOSBANCO,
				CenClienteBean.C_ASIENTOCONTABLE,
				CenClienteBean.C_CARACTER,
				CenClienteBean.C_CARGOSBANCO,
				CenClienteBean.C_COMISIONES,
				CenClienteBean.C_FECHAALTA,
				CenClienteBean.C_FECHAMODIFICACION,
				CenClienteBean.C_FOTOGRAFIA,
				CenClienteBean.C_GUIAJUDICIAL,
				CenClienteBean.C_IDINSTITUCION,
				CenClienteBean.C_IDLENGUAJE,
				CenClienteBean.C_IDPERSONA,
				CenClienteBean.C_IDTRATAMIENTO,
				CenClienteBean.C_PUBLICIDAD,
				CenClienteBean.C_USUMODIFICACION,
				CenClienteBean.C_LETRADO,	
				CenClienteBean.C_FECHACARGA,
				CenClienteBean.C_NOENVIARREVISTA, 			
				CenClienteBean.C_NOAPARECERREDABOGACIA,
				CenClienteBean.C_EXPORTARFOTO
		};
		return campos;
	} //getCamposBean ()
	
	public String[] getClavesBean ()
	{
		String [] claves = 
		{
				CenClienteBean.C_IDINSTITUCION,
				CenClienteBean.C_IDPERSONA
		};
		return claves;
	} //getClavesBean ()

	protected String[] getOrdenCampos () {
		return getClavesBean ();
	} //getOrdenCampos ()

	protected MasterBean hashTableToBean (Hashtable hash)
			throws ClsExceptions
	{
		CenClienteBean bean = null;
		
		try
		{
			bean = new CenClienteBean ();
			bean.setAbonosBanco (UtilidadesHash.getString (hash, CenClienteBean.C_ABONOSBANCO));
			bean.setAsientoContable (UtilidadesHash.getString (hash, CenClienteBean.C_ASIENTOCONTABLE));
			bean.setCaracter (UtilidadesHash.getString (hash, CenClienteBean.C_CARACTER));
			bean.setCargosBanco (UtilidadesHash.getString (hash, CenClienteBean.C_CARGOSBANCO));
			bean.setComisiones (UtilidadesHash.getString (hash, CenClienteBean.C_COMISIONES));
			bean.setFechaAlta (UtilidadesHash.getString (hash, CenClienteBean.C_FECHAALTA));
			bean.setFechaMod (UtilidadesHash.getString (hash, CenClienteBean.C_FECHAMODIFICACION));
			bean.setFotografia (UtilidadesHash.getString (hash, CenClienteBean.C_FOTOGRAFIA));
			bean.setGuiaJudicial (UtilidadesHash.getString (hash, CenClienteBean.C_GUIAJUDICIAL));
			bean.setIdInstitucion (UtilidadesHash.getInteger (hash, CenClienteBean.C_IDINSTITUCION));
			bean.setIdLenguaje (UtilidadesHash.getString (hash, CenClienteBean.C_IDLENGUAJE));
			bean.setIdPersona (UtilidadesHash.getLong (hash, CenClienteBean.C_IDPERSONA));
			bean.setIdTratamiento (UtilidadesHash.getInteger (hash, CenClienteBean.C_IDTRATAMIENTO));
			bean.setPublicidad (UtilidadesHash.getString (hash, CenClienteBean.C_PUBLICIDAD));
			bean.setUsuMod (UtilidadesHash.getInteger (hash, CenClienteBean.C_USUMODIFICACION));
			bean.setLetrado (UtilidadesHash.getString (hash, CenClienteBean.C_LETRADO));
			bean.setFechaCarga (UtilidadesHash.getString (hash, CenClienteBean.C_FECHACARGA));
			bean.setNoEnviarRevista (UtilidadesHash.getString (hash, CenClienteBean.C_NOENVIARREVISTA));
			bean.setNoAparacerRedAbogacia (UtilidadesHash.getString (hash, CenClienteBean.C_NOAPARECERREDABOGACIA));
			bean.setExportarFoto (UtilidadesHash.getString (hash, CenClienteBean.C_EXPORTARFOTO));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	} //hashTableToBean ()

	protected Hashtable beanToHashTable (MasterBean bean)
			throws ClsExceptions
	{
		Hashtable htData = null;
		
		try
		{
			htData = new Hashtable ();
			CenClienteBean b = (CenClienteBean) bean;
			UtilidadesHash.set (htData, CenClienteBean.C_ABONOSBANCO,			b.getAbonosBanco());
			UtilidadesHash.set (htData, CenClienteBean.C_ASIENTOCONTABLE,		b.getAsientoContable());
			UtilidadesHash.set (htData, CenClienteBean.C_CARACTER,				b.getCaracter());
			UtilidadesHash.set (htData, CenClienteBean.C_CARGOSBANCO,			b.getCargosBanco());
			UtilidadesHash.set (htData, CenClienteBean.C_COMISIONES,			b.getComisiones());
			UtilidadesHash.set (htData, CenClienteBean.C_FECHAALTA,				b.getFechaAlta());
			UtilidadesHash.set (htData, CenClienteBean.C_FECHAMODIFICACION,		b.getFechaMod());
			UtilidadesHash.set (htData, CenClienteBean.C_FOTOGRAFIA,			b.getFotografia());
			UtilidadesHash.set (htData, CenClienteBean.C_GUIAJUDICIAL,			b.getGuiaJudicial());
			UtilidadesHash.set (htData, CenClienteBean.C_IDINSTITUCION,			b.getIdInstitucion());
			UtilidadesHash.set (htData, CenClienteBean.C_IDLENGUAJE,			b.getIdLenguaje());
			UtilidadesHash.set (htData, CenClienteBean.C_IDPERSONA,				b.getIdPersona());
			UtilidadesHash.set (htData, CenClienteBean.C_IDTRATAMIENTO,			b.getIdTratamiento());
			UtilidadesHash.set (htData, CenClienteBean.C_PUBLICIDAD,			b.getPublicidad());
			UtilidadesHash.set (htData, CenClienteBean.C_LETRADO,				b.getLetrado());
			UtilidadesHash.set (htData, CenClienteBean.C_FECHACARGA,			b.getFechaCarga());
			UtilidadesHash.set (htData, CenClienteBean.C_USUMODIFICACION,		b.getUsuMod());
			UtilidadesHash.set (htData, CenClienteBean.C_NOENVIARREVISTA,		b.getNoEnviarRevista());
			UtilidadesHash.set (htData, CenClienteBean.C_NOAPARECERREDABOGACIA,	b.getNoAparacerRedAbogacia());
			UtilidadesHash.set (htData, CenClienteBean.C_EXPORTARFOTO,			b.getExportarFoto());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	} //beanToHashTable ()
	
	
	
	//////////////////// OTROS METODOS ////////////////////
	public boolean insert(Hashtable hash) throws ClsExceptions{
		try
		{
		    String s = UtilidadesHash.getString(hash, CenClienteBean.C_LETRADO);
		    if (s == null || s.equals(""))
				UtilidadesHash.set(hash, CenClienteBean.C_LETRADO, ClsConstants.DB_FALSE);
		        
		    return super.insert(hash);
		}
		catch (Exception e) {
			throw new ClsExceptions(e, "Error al realizar el \"insert\" en B.D");
		}
	}

	/**
	 * Devuelve un vector con las direcciones del cliente pasado como par�metro 
	 * @author nuria.rgonzalez 18-01-05
	 * @version 3	 
	 * @param idPersona, es el identificador de la persona de al que vamos a obtener los datos. 
	 * @param idInstitucion, es el identificador de la institucion de la persona de al que vamos a obtener los datos. 
	 */	
	public Vector getDirecciones(Long idPersona, Integer idInstitucion, boolean bIncluirRegistrosConBajaLogica) throws ClsExceptions, SIGAException
	{
		Vector vector=null;
		try{
			CenDireccionesAdm direccionesAdm = null; 
			if (this.usrbean!=null) {
				direccionesAdm = new CenDireccionesAdm(this.usuModificacion,this.usrbean,this.idInstitucionCliente, this.idPersonaCliente);
			} else {
				direccionesAdm = new CenDireccionesAdm(this.usrbean);
			}
			vector=direccionesAdm.selectDirecciones(idPersona,idInstitucion, bIncluirRegistrosConBajaLogica);
		}
		catch (SIGAException e) { 
			throw e; 	
		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error al obtener Direcciones");
		}
		return vector;
	}
	
	/**
	 * Devuelve un Hastable con la direccion del cliente pasado como par�metro 
	 * @author nuria.rgonzalez 18-1-05
	 * @version 3	 
	 * @param idPersona, es el identificador de la persona de al que vamos a obtener los datos. 
	 * @param idInstitucion, es el identificador de la institucion de la persona de al que vamos a obtener los datos. 
	 * @param idDireccion, es el identificador de la direccion de la que queremos obtener los datos. 
	 */
	public Hashtable getDirecciones(Long idPersona, Integer idInstitucion, Long idDireccion) throws ClsExceptions, SIGAException{
		return getDirecciones(idPersona, idInstitucion, idDireccion, false);
	}

	public Hashtable getDirecciones(Long idPersona, Integer idInstitucion, Long idDireccion, boolean permitirNulos) throws ClsExceptions, SIGAException{
		//Hashtable hash=new Hashtable();
		Hashtable hash=null;
		try{
			CenDireccionesAdm direccionesAdm = null; 
			if (this.usrbean!=null) {
				direccionesAdm = new CenDireccionesAdm(this.usuModificacion,this.usrbean,this.idInstitucionCliente, this.idPersonaCliente);
			} else {
				direccionesAdm = new CenDireccionesAdm(this.usrbean);
			}
			hash=direccionesAdm.selectDirecciones(idPersona,idInstitucion,idDireccion, permitirNulos);
		}
		catch (SIGAException e) { 
			throw e; 	
		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error al obtener Direcciones");
		}
		return hash;
	}
	
	/**
	 * Devuelve un vector con las cuentas bancarias del cliente pasado como par�metro 
	 * @author nuria.rgonzalez 18-01-05
	 * @version 3	 
	 * @param idPersona, es el identificador de la persona de al que vamos a obtener los datos. 
	 * @param idInstitucion, es el identificador de la institucion de la persona de al que vamos a obtener los datos. 
	 */	
	public Vector<Hashtable<String, Object>> getCuentasBancarias(Long idPersona, Integer idInstitucion, boolean bIncluirRegistrosConBajaLogica) throws ClsExceptions {
		//Vector vector = new Vector();
		Vector<Hashtable<String, Object>> vector = null;
		
		try {
			CenCuentasBancariasAdm cuentasAdm = null; 
			if (this.usrbean!=null) {
				cuentasAdm = new CenCuentasBancariasAdm(this.usuModificacion,this.usrbean,this.idInstitucionCliente,this.idPersonaCliente);
			} else {
				cuentasAdm = new CenCuentasBancariasAdm(this.usrbean);
			} 
			vector = cuentasAdm.selectCuentas(idPersona,idInstitucion, bIncluirRegistrosConBajaLogica);
			
		} catch(Exception e) {
			throw new ClsExceptions (e, "Error al obtener Cuentas Bancarias");
		}
		
		return vector;
	}
	
	/**
	 * Devuelve un Hastable con la cuentas bancarias del cliente pasado como par�metro 
	 * @author nuria.rgonzalez 18-01-05
	 * @version 3	 
	 * @param idPersona, es el identificador de la persona de al que vamos a obtener los datos. 
	 * @param idInstitucion, es el identificador de la institucion de la persona de al que vamos a obtener los datos. 
	 * @param idCuenta, es el identificador de la cuenta de la que queremos obtener los datos. 
	 */	
	public Hashtable getCuentasBancarias(Long idPersona, Integer idInstitucion, Integer idCuenta ) throws ClsExceptions, SIGAException{
		//Hashtable hash = new Hashtable();
		Hashtable hash = null;
		try{
			CenCuentasBancariasAdm cuentasAdm = null; 
			if (this.usrbean!=null) {
				cuentasAdm = new CenCuentasBancariasAdm(this.usuModificacion,this.usrbean,this.idInstitucionCliente,this.idPersonaCliente);
			} else {
				cuentasAdm = new CenCuentasBancariasAdm(this.usrbean);
			} 
			
			hash=cuentasAdm.selectCuentas(idPersona,idInstitucion, idCuenta);
		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error al obtener Cuentas Bancarias");
		}
		return hash;		
	}
	
	/**
	 * Devuelve un vector con los DatosCV del cliente pasado como par�metro 
	 * @author nuria.rgonzalez 18-01-05
	 * @version 2	 
	 * @param idPersona, es el identificador de la persona de al que vamos a obtener los datos. 
	 * @param idInstitucion, es el identificador de la institucion de la persona de al que vamos a obtener los datos. 
	 */	
	public Vector getDatosCV(Long idPersona, Integer idInstitucion, boolean bIncluirRegistrosConBajaLogica) throws ClsExceptions, SIGAException{
		Vector vector = null;
		try{
			
			CenDatosCVAdm datosCVAdm = null;
			if (this.usrbean!=null) {
				datosCVAdm = new CenDatosCVAdm(this.usuModificacion,this.usrbean,this.idInstitucionCliente, this.idPersonaCliente);
			} else {
				datosCVAdm = new CenDatosCVAdm(this.usrbean); 
			}
			vector = datosCVAdm.selectDatosCV(idPersona,idInstitucion, bIncluirRegistrosConBajaLogica);
		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error al obtener DatosCV");
		}
		return vector;
	}
	
		/**
	 * Devuelve un vector con los DatosCV del cliente pasado como par�metro 
	 * @author nuria.rgonzalez 18-01-05
	 * @version 2	 
	 * @param idPersona, es el identificador de la persona de al que vamos a obtener los datos. 
	 * @param idInstitucion, es el identificador de la institucion de la persona de al que vamos a obtener los datos. 
	 */	
	public Vector getDatosCVInforme(Long idPersona, Integer idInstitucion, boolean bIncluirRegistrosConBajaLogica) throws ClsExceptions, SIGAException{
		Vector vector = null;
		try{
			
			CenDatosCVAdm datosCVAdm = null;
			if (this.usrbean!=null) {
				datosCVAdm = new CenDatosCVAdm(this.usuModificacion,this.usrbean,this.idInstitucionCliente, this.idPersonaCliente);
			} else {
				datosCVAdm = new CenDatosCVAdm(this.usrbean); 
			}
			vector = datosCVAdm.selectDatosCVInforme(idPersona,idInstitucion, bIncluirRegistrosConBajaLogica);
		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error al obtener DatosCV");
		}
		return vector;
	}
	
	/**
	 * Devuelve un Hastable con el CV del cliente pasado como par�metro 
	 * @author nuria.rgonzalez 18-01-05
	 * @version 1	 
	 * @param idPersona, es el identificador de la persona de al que vamos a obtener los datos. 
	 * @param idInstitucion, es el identificador de la institucion de la persona de al que vamos a obtener los datos. 
	 * @param idCV, es el identificador del CV del que queremos obtener los datos. 
	 */	
	public Hashtable getDatosCV(Long idPersona, Integer idInstitucion, Integer idCV ) throws ClsExceptions, SIGAException{
		//Hashtable hash = new Hashtable();
		Hashtable hash = null;
		try{
			CenDatosCVAdm datosCVAdm = null;
			if (this.usrbean!=null) {
				datosCVAdm = new CenDatosCVAdm(this.usuModificacion,this.usrbean,this.idInstitucionCliente, this.idPersonaCliente);
			} else {
				datosCVAdm = new CenDatosCVAdm(this.usrbean); 
			}
			hash=datosCVAdm.selectDatosCV(idPersona,idInstitucion, idCV);			
		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error al obtener DatosCV");
		}
		return hash;
	}
	public Hashtable getDatosCVJunta(Long idPersona, Integer idInstitucion, Integer idInstitucionCargo, Integer idCV) throws ClsExceptions, SIGAException{
		//Hashtable hash = new Hashtable();
		Hashtable hash = null;
		try{
			CenDatosCVAdm datosCVAdm = null;
			if (this.usrbean!=null) {
				datosCVAdm = new CenDatosCVAdm(this.usuModificacion,this.usrbean,this.idInstitucionCliente, this.idPersonaCliente);
			} else {
				datosCVAdm = new CenDatosCVAdm(this.usrbean); 
			}
			hash=datosCVAdm.selectDatosCVJunta(idPersona,idInstitucion,idInstitucionCargo, idCV);			
		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error al obtener DatosCV");
		}
		return hash;
	}

	
	/**
	 * Devuelve el numero de colegios del que es cliente un determinado usuario . 
	 * @param idPersona, es el identificador de la persona. 
	 * @param idInstitucion, es el identificador de la institucion.
	 * @return  Integer - Numero de colegios al que pertenece  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public String getNumColegiosEsCliente(String idPersona) throws ClsExceptions, SIGAException{

		String numero = "";
		try{
            RowsContainer rc = new RowsContainer(); 
            Hashtable codigos = new Hashtable();
    		codigos.put(new Integer(1),idPersona);
    		String sql ="SELECT COUNT(1) FROM " + 
						CenClienteBean.T_NOMBRETABLA + 
						" WHERE " + 
						CenClienteBean.T_NOMBRETABLA +"."+ CenClienteBean.C_IDPERSONA + "=:1";						 										
						
            if (rc.findForUpdateBind(sql,codigos)) {
                Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();				                
            	numero = (String)prueba.get("COUNT(1)");
            }
		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error al obtener el numero de colegios al que pertenece un usuario");
		}
		return numero;
	}	

	/** 
	 * Funcion que elimina una entrada de la tabla (siempre que hablemos de un usuario no colegiado) 
	 * @param  idPersona - identificador del usuario
	 * @param  idInstitucion - identificador de la institucion a la que pertenece y delque se quiere eliminar
	 * @return  boolean - Si se ha realizado correctamente o no la operacion	 	 
	 * @exception  ClsExceptions  En cualquier caso de error   
	 */	
	public boolean eliminarNoColegiado(String idPersona, String idInstitucion)throws ClsExceptions, SIGAException {
		
		boolean eliminada=false;
		boolean borrarPersona=false;
		Hashtable hash=new Hashtable();
		
		try{ 
			// Compruebo si el usuario se encuentra inscrito unicamente en este colegio
			if (this.getNumColegiosEsCliente(idPersona).equalsIgnoreCase("1")){
				borrarPersona=true;
			}
								
			// Cargo la tabla hash con los valores del formulario para eliminar en PYS_PRODUCTOSINSTITUCION y PYS_FORMAPAGOPRODUCTO		
			hash.put("IDPERSONA",idPersona);
			hash.put("IDINSTITUCION",idInstitucion);
			
			// Procedo a borrar en la tabla -> conlleva borrado en cascada				
			eliminada=this.delete(hash);
	
			// Si corresponde (el usuario solo estaba inscrito en un unico colegio), 
			// elimino la persona de la tabla correspondiente
			if (borrarPersona && eliminada){
				CenPersonaAdm personaAdm = new CenPersonaAdm(this.usrbean);
				eliminada = personaAdm.borrarPersona(idPersona);	
			}
			
			
		}
		catch (SIGAException e) { 
			throw e; 	
		}

		catch (Exception ee){
			throw new ClsExceptions(ee,"messages.deleted.error"+"messages.deletedCliente.error");				
		}
		return eliminada;
	}	
	
	/**
	 * Devuelve un vector de un registro con los datos generales de una persona 
	 * @param idPersona, es el identificador de la persona de al que vamos a obtener los datos. 
	 * @param idInstitucion, es el identificador de la institucion de la persona de al que vamos a obtener los datos. 
	 */	
	public Vector getDatosPersonales(Long idPersona, Integer idInstitucion)throws ClsExceptions, SIGAException{
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
		    Hashtable codigos = new Hashtable();
		    codigos.put(new Integer(1),idInstitucion.toString());
		    codigos.put(new Integer(2),idPersona.toString());
    		
		    String sql = " SELECT "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NOMBRE+","+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+","+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2+","+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NIFCIF+","+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NATURALDE+","+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDESTADOCIVIL+"," +CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_FALLECIDO+","+
			 "  " + CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_CARACTER+","+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_FOTOGRAFIA+" ,  "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_SEXO+" ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_PUBLICIDAD+" ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_GUIAJUDICIAL+" ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_EXPORTARFOTO+" ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_ABONOSBANCO+" ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_CARGOSBANCO+" , "+
			 "  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_ASIENTOCONTABLE+","+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_COMISIONES+" ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDTRATAMIENTO+" ,  "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDPERSONA+" ,  "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_FECHANACIMIENTO+" as "+CenPersonaBean.C_FECHANACIMIENTO+",  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDLENGUAJE+" ,  "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDTIPOIDENTIFICACION+" ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_FECHAALTA+"  " +" ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_NOAPARECERREDABOGACIA+ 
		     ",  TRUNC(f_siga_calculoanios ("+CenPersonaBean.C_FECHANACIMIENTO+", SYSDATE)) AS EDAD  ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_NOENVIARREVISTA+
			 " FROM   " + CenClienteBean.T_NOMBRETABLA + ",  "+CenPersonaBean.T_NOMBRETABLA +
			 " WHERE   " + CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDPERSONA+"  = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+"  " +
			 " AND    " + CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION +" =:1 "+
			 " AND   " + CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDPERSONA+" =:2 ";		  

			// RGG cambio para visibilidad
            rc = this.findBind(sql,codigos);
 			if (rc!=null) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error en getDatosPersonales");
		}
		return v;
	}
	
	/**
	 * Devuelve un vector con los grupos de un cliente e institucion 
	 * @param idPersona, es el identificador de la persona de al que vamos a obtener los datos. 
	 * @param idInstitucion, es el identificador de la institucion de la persona de al que vamos a obtener los datos. 
	 */	
	public Vector getGrupos(Long idPersona, Integer idInstitucion)throws ClsExceptions, SIGAException{
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
		    Hashtable codigos = new Hashtable();
		    codigos.put(new Integer(1),idPersona.toString());
    		codigos.put(new Integer(2),idInstitucion.toString());
		    String sql = " SELECT "+
							CenGruposClienteClienteBean.T_NOMBRETABLA+"."+CenGruposClienteClienteBean.C_IDGRUPO+"  ," +
							CenGruposClienteClienteBean.T_NOMBRETABLA+"."+CenGruposClienteClienteBean.C_IDINSTITUCION_GRUPO+"  " +			
						 " FROM  "+CenGruposClienteClienteBean.T_NOMBRETABLA+
						 " WHERE  "+CenGruposClienteClienteBean.T_NOMBRETABLA+"."+CenGruposClienteClienteBean.C_IDPERSONA+" =:1 " +
						 " AND  "+CenGruposClienteClienteBean.T_NOMBRETABLA+"."+CenGruposClienteClienteBean.C_IDINSTITUCION+" =:2 ";
		
			// RGG cambio para visibilidad
			rc = this.findBind(sql,codigos); 
 			if (rc!=null) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error en getGrupos");
		}
		return v;
	}
	
	/**
	 * Dice si una persona es colegiada
	 * @param idPersona, es el identificador de la persona de al que vamos a obtener los datos. 
	 * @param idInstitucion, es el identificador de la institucion de la persona de al que vamos a obtener los datos. 
	 * @return String Devuelve constantes asociadas a: Tipo cliente Colegiado, no colegiado y no colegiado en baja.   
	 */	
	public String getTipoCliente(Long idPersona, Integer idInstitucion)throws ClsExceptions, SIGAException{
		try{
			CenColegiadoAdm admCol = null;
			admCol = new CenColegiadoAdm(this.usrbean);
			CenColegiadoBean beanCol = admCol.getDatosColegiales(idPersona,idInstitucion);
			return getTipoCliente(beanCol);
			
		}
		catch (SIGAException e) { 
			throw e; 	
		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error en getDatosPersonales");
		}
	}
	
	/**
	 * Dice si una persona es colegiada
	 * @param idPersona, es el identificador de la persona de al que vamos a obtener los datos. 
	 * @param idInstitucion, es el identificador de la institucion de la persona de al que vamos a obtener los datos. 
	 * @return String Devuelve constantes asociadas a: Tipo cliente Colegiado, no colegiado y no colegiado en baja.   
	 */	
	public String getTipoCliente(CenColegiadoBean beanCol)throws ClsExceptions, SIGAException{
		try{
			CenColegiadoAdm admCol = new CenColegiadoAdm(this.usrbean);
			if (beanCol==null) {
				    return ClsConstants.TIPO_CLIENTE_NOCOLEGIADO;
				
			} else {
				Hashtable datos = admCol.getEstadoColegial(beanCol.getIdPersona(),beanCol.getIdInstitucion());
				if (datos!=null) {
					String aux = (String) datos.get(CenDatosColegialesEstadoBean.C_IDESTADO);
					int idEstado = new Integer(aux).intValue(); 
					if (idEstado==ClsConstants.ESTADO_COLEGIAL_BAJACOLEGIAL || idEstado==ClsConstants.ESTADO_COLEGIAL_SUSPENSION) {
						return ClsConstants.TIPO_CLIENTE_COLEGIADO_BAJA;
					} else {
						return ClsConstants.TIPO_CLIENTE_COLEGIADO;
					}
				} else {
					
					  return ClsConstants.TIPO_CLIENTE_COLEGIADO;
					
				}
			}

		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error en getDatosPersonales");
		}
	}
	
	/**
	 * Obtiene los clientes colegiados para una instituci�n y
	 * un formulario de datos (BusquedaClientesForm)
	 * 
	 * @param idInstitucion 
	 * @param formulario Formulario de busqueda de clientes con los datos de busqueda 
	 * @return java.util.Vector Vector de tablas hash  
	 */
	public PaginadorBind getClientesColegiados(String idInstitucion, BusquedaClientesForm formulario, String idioma) throws ClsExceptions, SIGAException {

		String sqlClientes = "";
		String sqlContarClientes=" Select 1 ";
		String sqlClientesWhere="";
		Integer idTipoCVSubtipo1 = null;
		Integer idInstitucionSubtipo1=null;
		Integer idTipoCVSubtipo2 = null;
		Integer idInstitucionSubtipo2=null;
		Hashtable codigosBind = new Hashtable();
		int contador=0;
	  	
	  	// Acceso a BBDD
		try { 
			// consulta de insituciones 
			//String bBusqueda = formulario.getChkBusqueda();
			boolean bBusqueda  = UtilidadesString.stringToBoolean(formulario.getValorCheck());
			sqlClientes = "SELECT "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NIFCIF+" , nvl(" + CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_NOAPARECERREDABOGACIA+" ,'0') "+CenClienteBean.C_NOAPARECERREDABOGACIA+"  ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_NOENVIARREVISTA+" , " +
				" DECODE("+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_COMUNITARIO+",'" + ClsConstants.DB_TRUE + "',"+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_NCOMUNITARIO+","+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_NCOLEGIADO+") AS "+CenColegiadoBean.C_NCOLEGIADO+"," +
				" "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_NCOMUNITARIO+", "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NOMBRE+", "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+","+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2+", "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_FECHANACIMIENTO+", "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_FECHAINCORPORACION+", "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDPERSONA+" , "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDINSTITUCION+", "+
			       
				" "+CenColegiadoBean.C_SITUACIONRESIDENTE+" ";
				//" F_SIGA_ESLETRADO("+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+", "+idInstitucion+") LETRADO";
			
			sqlClientes+=" FROM  "+CenPersonaBean.T_NOMBRETABLA+" ,  "+CenClienteBean.T_NOMBRETABLA+" ,  "+CenColegiadoBean.T_NOMBRETABLA+" " ;
			sqlContarClientes+=" FROM  "+CenPersonaBean.T_NOMBRETABLA+" ,  "+CenClienteBean.T_NOMBRETABLA+" ,  "+CenColegiadoBean.T_NOMBRETABLA+" " ;
			
			

			sqlClientesWhere=" WHERE " +

				" "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDPERSONA+"= "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" "+
				" AND "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" = "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDPERSONA+" "+
				" AND "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" = "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDINSTITUCION+" ";
			if(ClsConstants.esConsejoColegio(idInstitucion) || ClsConstants.esConsejoColegio(idInstitucion)){
				contador++;
				codigosBind.put(new Integer(contador),formulario.getNombreInstitucion().trim());
				sqlClientesWhere+=" AND ("+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" = :" + contador ;
				contador++;
				codigosBind.put(new Integer(contador),ClsConstants.TIPO_CARACTER_PRIVADO);
				sqlClientesWhere+=" OR "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_CARACTER+" <> :" +contador + ")";
			}	
//	 1 
	       if (!formulario.getNombreInstitucion().trim().equals("")) {
	       	contador++;
			codigosBind.put(new Integer(contador),formulario.getNombreInstitucion().trim());
	       	sqlClientesWhere += " AND "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" = :"+contador;
	       } else {
	       		String institucionesVisibles = CenVisibilidad.getVisibilidadInstitucion(idInstitucion);
	       		
				
	       		sqlClientesWhere += " AND "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" IN ("+institucionesVisibles +") ";
	       }
//	 2  
	       if (formulario.getNumeroColegiado()!=null && !formulario.getNumeroColegiado().trim().equals("")) {
//	       	if ((bBusqueda ) ) {
	       	contador++;
			codigosBind.put(new Integer(contador),formulario.getNumeroColegiado().trim());
	       	    sqlClientesWhere += " AND LTRIM(DECODE(CEN_COLEGIADO.COMUNITARIO,'1',CEN_COLEGIADO.NCOMUNITARIO, CEN_COLEGIADO.NCOLEGIADO),'0') = LTRIM(:"+contador+",'0') " ;
	       }
//	 3
	       if (!formulario.getNombrePersona().trim().equals("")) {
	       	
			if ((bBusqueda ) ) {
				contador++;
				sqlClientesWhere += " AND ("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NOMBRE+" "+ComodinBusquedas.prepararSentenciaExactaBind(formulario.getNombrePersona().trim(),contador,codigosBind)+") ";
				  
	       	 }else{
	       	    contador++;
				
	       	     sqlClientesWhere += " AND ("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getNombrePersona().trim(),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NOMBRE,contador, codigosBind )+") ";  
	       	 }	
	       }
				

	       
//   4 y 5 Criterio de busqueda especial para los campos Apellido1 y Apellido2
	       if (!formulario.getApellido1().trim().equals("")&&(!formulario.getApellido2().trim().equals(""))) {// Los dos campos rellenos
	       	if ((bBusqueda ) ) {
	       		contador++;
	       		sqlClientesWhere += " AND ((("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+" "+ComodinBusquedas.prepararSentenciaExactaBind(formulario.getApellido1().trim(),contador,codigosBind )+")";
	       		contador++;
	       		sqlClientesWhere += " AND ("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2+" "+ComodinBusquedas.prepararSentenciaExactaBind(formulario.getApellido2().trim(),contador,codigosBind )+"))";
	       		contador++;
	       		sqlClientesWhere += " OR ("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+" "+ComodinBusquedas.prepararSentenciaExactaBind(formulario.getApellido1().trim()+" "+formulario.getApellido2().trim(),contador,codigosBind )+"))";
	       	}else{
	       		contador++;
	       		sqlClientesWhere += " AND ((("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido1().trim(),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1,contador,codigosBind )+")";
	       		contador++;
	       		sqlClientesWhere += " AND ("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido2().trim(),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2,contador,codigosBind)+"))";
	       		contador++;
				               //" OR ("+ComodinBusquedas.prepararSentenciaCompleta(formulario.getApellido1().trim()+"% %"+formulario.getApellido2().trim() ,CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1 )+"))";
	       		sqlClientesWhere+= " OR ("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido1().trim()+" "+formulario.getApellido2().trim() ,CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1,contador,codigosBind )+"))";
	       		
	       	}
	       }else if (!formulario.getApellido1().trim().equals("")&&(formulario.getApellido2().trim().equals(""))) {//Apellido1 relleno
	       	if ((bBusqueda ) ) {
	       		contador++;
	       		sqlClientesWhere += " AND (("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+" "+ComodinBusquedas.prepararSentenciaExactaBind(formulario.getApellido1(),contador,codigosBind)+")";
	       		sqlClientesWhere +=" OR ("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+" LIKE '"+UtilidadesBDAdm.validateChars(formulario.getApellido1().trim())+" %'))";
	       	}else{
	       		//sqlClientesWhere += " AND ("+ComodinBusquedas.prepararSentenciaCompleta(formulario.getApellido1().trim(),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1 )+" OR ("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+"||' '||"+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2+" LIKE '%"+UtilidadesBDAdm.validateChars(formulario.getApellido1().trim())+"%')) ";
	       		contador++;
	       		sqlClientesWhere += " AND "+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido1().trim(),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1,contador,codigosBind )+"  ";
	       		
	       		
	       	}
	       }else if (formulario.getApellido1().trim().equals("")&&(!formulario.getApellido2().trim().equals(""))) {//Apellido2 relleno
	       	if ((bBusqueda) ) {
	       		contador++;
	       		sqlClientesWhere += " AND (("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2+" "+ComodinBusquedas.prepararSentenciaExactaBind(formulario.getApellido2().trim(),contador,codigosBind )+")";
	       		contador++;
	       		sqlClientesWhere += " OR ("+ComodinBusquedas.prepararSentenciaCompletaBind(UtilidadesBDAdm.validateChars(formulario.getApellido2().trim()),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1,contador,codigosBind)+" ))";
	       		 
	       	}else{
	       		contador++;
	       		sqlClientesWhere += " AND "+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido2().trim(),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2,contador, codigosBind);
				               
	       	}
	       	
	       }
//	 6 //Consulta sobre el campo NIF/CIF, si el usuario mete comodines la b�squeda es como se hac�a siempre, en el caso
	   // de no meter comodines se ha creado un nuevo metodo ComodinBusquedas.preparaCadenaNIFSinComodin para que monte 
	   // la consulta adecuada.    
	       if (!formulario.getNif().trim().equals("")) {
	    	   if ((bBusqueda) ) {
	    		   contador++;
	    		   codigosBind.put(new Integer(contador), formulario.getNif().trim().toUpperCase());
	    		   sqlClientesWhere +=" AND UPPER("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NIFCIF+")= :"+contador;
	    		   //a peticion de lp INC_06550_SIGA. Estaba la siguiente linea
	    		   //sqlClientesWhere +=" AND "+ComodinBusquedas.prepararSentenciaNIFExacta(formulario.getNif(),"UPPER("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NIFCIF+")");
	    	   }else{
	    		   if (ComodinBusquedas.hasComodin(formulario.getNif())){
	    			   contador++;
	    			   sqlClientesWhere += " AND ("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getNif().trim(),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NIFCIF,contador,codigosBind )+") ";
	    		   }else{

	    			   contador++;

	    			   sqlClientesWhere +=" AND "+ComodinBusquedas.prepararSentenciaNIFBind(formulario.getNif(),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NIFCIF,contador,codigosBind);	
	    		   }
	    	   }
	       }
//	 7
	       if (!formulario.getTipoCliente().trim().equals("")) {
	       	
		       if (formulario.getTipoCliente().trim().equals(ClsConstants.DB_FALSE)) {
		       	contador++;
		       	codigosBind.put(new Integer(contador),formulario.getTipoCliente().trim());
	       		// tipo persona juridica
		       	sqlClientesWhere += " AND ("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDTIPOIDENTIFICACION+" = :"+contador+") ";
		       } else {
		       	// tipo persona ficia
		       	sqlClientesWhere += " AND ("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDTIPOIDENTIFICACION+" IN ("+ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE+","+ClsConstants.TIPO_IDENTIFICACION_PASAPORTE+","+ClsConstants.TIPO_IDENTIFICACION_NIF+")) ";
		       }
	       } 
//	 8
	       String grupoClientes=formulario.getGrupoClientes();
	       if (grupoClientes!=null && !grupoClientes.trim().equals("")) {
	    	   int coma=grupoClientes.indexOf(",");
	    	   if(coma!=-1){
	    	   	contador++;
	    	   	codigosBind.put(new Integer(contador),grupoClientes.substring(0,coma));
	    	   	
	    	   	
	    	   	sqlClientesWhere +=
	    			   " AND EXISTS (SELECT 1 "+
	    			   " FROM  "+CenGruposClienteClienteBean.T_NOMBRETABLA+"   "+
	    			   " WHERE "+CenGruposClienteClienteBean.T_NOMBRETABLA+"."+CenGruposClienteClienteBean.C_IDPERSONA+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" "+
	    			   " AND "+CenGruposClienteClienteBean.T_NOMBRETABLA+"."+CenGruposClienteClienteBean.C_IDINSTITUCION+"= "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" "+
	    			   " AND "+CenGruposClienteClienteBean.T_NOMBRETABLA+"."+CenGruposClienteClienteBean.C_IDGRUPO+" = :"+contador;
	    	   	
	    		contador++;
	    	   	codigosBind.put(new Integer(contador),grupoClientes.substring(coma+1));
	    	   	sqlClientesWhere +=
	    			   " AND "+CenGruposClienteClienteBean.T_NOMBRETABLA+"."+CenGruposClienteClienteBean.C_IDINSTITUCION_GRUPO+" = :"+contador+
	    			   ") ";
	    	   }
	       }
//	 9 
	       if (formulario.getSexo()!=null &&!formulario.getSexo().trim().equals("")) {
	       	contador++;
	       	codigosBind.put(new Integer(contador),formulario.getSexo().trim());
	       	sqlClientesWhere += " AND ("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_SEXO+" = :"+contador+") ";
	       }
//	 10
	       if (formulario.getEjerciente()!=null && !formulario.getEjerciente().trim().equals("")) {
	       	contador++;
	       	codigosBind.put(new Integer(contador),formulario.getEjerciente().trim());
	       	sqlClientesWhere += " AND ("+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_SITUACIONEJERCICIO+" = :"+contador+") ";
	       }
//	 11
	       
	       String fDesdeInc = formulario.getFechaIncorporacionDesde(); 
		   String fHastaInc = formulario.getFechaIncorporacionHasta();
			if ((fDesdeInc != null && !fDesdeInc.trim().equals("")) || (fHastaInc != null && !fHastaInc.trim().equals(""))) {
				
				if (!fDesdeInc.equals(""))
					fDesdeInc = GstDate.getApplicationFormatDate("", fDesdeInc); 
				if (!fHastaInc.equals(""))
					fHastaInc = GstDate.getApplicationFormatDate("", fHastaInc);
				Vector vCondicion=GstDate.dateBetweenDesdeAndHastaBind(CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_FECHAINCORPORACION, fDesdeInc, fHastaInc,contador,codigosBind);
				
				contador=new Integer(vCondicion.get(0).toString()).intValue();
				sqlClientesWhere +=" and " + vCondicion.get(1) ;
				
			}
//	 12
	       if (formulario.getResidente()!=null && !formulario.getResidente().trim().equals("")) {
	    	contador++;
	       	codigosBind.put(new Integer(contador),formulario.getResidente().trim());
	       	sqlClientesWhere += " AND ("+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_SITUACIONRESIDENTE+" = :"+contador+") ";
	       }
//	 13
	       if (formulario.getDomicilio()!=null && !formulario.getDomicilio().trim().equals("")) {
	       	contador++;
	       	
	       	sqlClientesWhere += " AND EXISTS (SELECT "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_DOMICILIO+" "+
				" FROM  "+CenDireccionesBean.T_NOMBRETABLA+"  "+
				" WHERE "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDINSTITUCION+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" "+
				" AND "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDPERSONA+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" "+
				" AND "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_FECHABAJA+" is null "+
				" AND "+ ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getDomicilio().trim(),CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_DOMICILIO,contador,codigosBind)+") ";
				
	       }
//	 14
	       if (formulario.getCodigoPostal()!=null && !formulario.getCodigoPostal().trim().equals("")) {
	       	contador++;
	       	codigosBind.put(new Integer(contador),formulario.getCodigoPostal());
	       	sqlClientesWhere += " AND EXISTS (SELECT "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_CODIGOPOSTAL+" "+
				" FROM  "+CenDireccionesBean.T_NOMBRETABLA+"   "+
				" WHERE "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDINSTITUCION+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" "+
				" AND "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDPERSONA+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" "+
				" AND "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_FECHABAJA+" is null "+
				" AND "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_CODIGOPOSTAL+"= :"+contador+") ";
	       }
//	 15
	       if (formulario.getTelefono()!=null && !formulario.getTelefono().trim().equals("")) {
	       	sqlClientesWhere += " AND EXISTS (SELECT 1 "+
				" FROM  "+CenDireccionesBean.T_NOMBRETABLA+"  "+
				" WHERE "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDINSTITUCION+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" "+
				" AND "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDPERSONA+"= "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" "+
				" AND "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_FECHABAJA+" is null ";
	       	contador++;
	       	codigosBind.put(new Integer(contador),formulario.getTelefono().trim());
	       	
	       	sqlClientesWhere +=	" AND ("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getTelefono().trim(),CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_TELEFONO1,contador,codigosBind);
	       	contador++;
	       	codigosBind.put(new Integer(contador),formulario.getTelefono().trim());
	       	sqlClientesWhere +=" OR "+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getTelefono().trim(),CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_TELEFONO2,contador,codigosBind);
	       	contador++;
	       	codigosBind.put(new Integer(contador),formulario.getTelefono().trim());
	       	sqlClientesWhere+=" OR " +" "+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getTelefono(),CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_MOVIL,contador,codigosBind)+")) ";
		   }
//	 16
	       if (formulario.getFax()!=null && !formulario.getFax().trim().equals("")) {
	       	sqlClientesWhere += " AND EXISTS (SELECT 1 "+
				" FROM  "+CenDireccionesBean.T_NOMBRETABLA+"   "+
				" WHERE "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDINSTITUCION+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" "+
				" AND "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDPERSONA+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" "+
				" AND "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_FECHABAJA+" is null ";
	       	contador++;
	       	
	       	sqlClientesWhere += " AND ("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getFax(),CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_FAX1,contador,codigosBind);
	       	contador++;
	       	sqlClientesWhere +=" OR "+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getFax().trim(),CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_FAX2,contador,codigosBind)+")) ";
		   }
//	 17         
	       if (formulario.getCorreo()!=null && !formulario.getCorreo().trim().equals("")) {
	       	sqlClientesWhere += " AND EXISTS (SELECT 1 "+
				" FROM  "+CenDireccionesBean.T_NOMBRETABLA+"   "+
				" WHERE "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDINSTITUCION+"= "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" "+
				" AND "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDPERSONA+"= "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" "+
				" AND "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_FECHABAJA+" is null ";
	       	contador++;
	       	sqlClientesWhere +=	" AND "+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getCorreo().trim(),CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_CORREOELECTRONICO,contador,codigosBind)+") ";
				
		   }
//	 18
	       String fDesdeAlta = formulario.getFechaAltaDesde(); 
		   String fHastaAlta = formulario.getFechaAltaHasta();
			if ((fDesdeAlta != null && !fDesdeAlta.trim().equals("")) || (fHastaAlta != null && !fHastaAlta.trim().equals(""))) {
				if (!fDesdeAlta.equals(""))
					fDesdeAlta = GstDate.getApplicationFormatDate("", fDesdeAlta); 
				if (!fHastaAlta.equals(""))
					fHastaAlta = GstDate.getApplicationFormatDate("", fHastaAlta);
				sqlClientesWhere += " AND EXISTS (SELECT 1 "+ 
												" FROM FAC_FACTURA "+
												" WHERE FAC_FACTURA.IDPERSONA = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" "+
												" AND FAC_FACTURA.IDINSTITUCION = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" ";
				
				
				Vector vCondicion=GstDate.dateBetweenDesdeAndHastaBind("FAC_FACTURA.FECHAEMISION", fDesdeAlta, fHastaAlta,contador,codigosBind);
				
				contador=new Integer(vCondicion.get(0).toString()).intValue();
				sqlClientesWhere +=" and " + vCondicion.get(1) + ") ";
				
			}
	       
//		  	 20 fecha nacimiento
	       String fDesdeNac = formulario.getFechaNacimientoDesde(); 
		   String fHastaNac = formulario.getFechaNacimientoHasta();
			if ((fDesdeNac != null && !fDesdeNac.trim().equals("")) || (fHastaNac != null && !fHastaNac.trim().equals(""))) {
				if (!fDesdeNac.equals(""))
					fDesdeNac = GstDate.getApplicationFormatDate("", fDesdeNac); 
				if (!fHastaNac.equals(""))
					fHastaNac = GstDate.getApplicationFormatDate("", fHastaNac);
				
				Vector vCondicion=GstDate.dateBetweenDesdeAndHastaBind(CenPersonaBean.C_FECHANACIMIENTO, fDesdeNac, fHastaNac,contador,codigosBind);
				
				contador=new Integer(vCondicion.get(0).toString()).intValue();
				
				sqlClientesWhere +=" and " + vCondicion.get(1);

			}
//          21 Comunitario
	       
	       if (formulario.getComunitario()!=null && !formulario.getComunitario().trim().equals("")) {
	       	contador++;
	       	codigosBind.put(new Integer(contador),formulario.getComunitario().trim());
	       	sqlClientesWhere += " AND ("+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_COMUNITARIO+" = :"+contador+") ";
           }
	       
	       	// 22 Tipo Apunte
	       	if (formulario.getTipoApunte()!=null && !formulario.getTipoApunte().equals("")){
	       		contador++;
	       		codigosBind.put(new Integer(contador), formulario.getTipoApunte());
	       		sqlClientesWhere += " AND EXISTS (" +
	       								" SELECT 1 " +
		       							" FROM  " + CenDatosCVBean.T_NOMBRETABLA +
										" WHERE " + CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDPERSONA + " = " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDPERSONA +
					                    	" AND " + CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDINSTITUCION + " = " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDINSTITUCION +
					                    	" AND " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDTIPOCV + " = :" + contador +
					                    	" AND " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_FECHABAJA + " IS NULL";

	       		// 22 idTipoCVSubtipo1
	       		if (formulario.getIdTipoCVSubtipo1()!=null && !formulario.getIdTipoCVSubtipo1().equals("")){
	       			String[] datosCVSubtipo1 = formulario.getIdTipoCVSubtipo1().toString().split("@");

	       			contador++;
	       			idTipoCVSubtipo1 = new Integer(datosCVSubtipo1[0]);
	       			codigosBind.put(new Integer(contador), idTipoCVSubtipo1.toString());
	       			sqlClientesWhere += " AND " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDTIPOCVSUBTIPO1 + " = :" + contador;
	       		
	       			contador++;
	       			idInstitucionSubtipo1 = new Integer(datosCVSubtipo1[1]);
	       			codigosBind.put(new Integer(contador), idInstitucionSubtipo1.toString());	       		
	       			sqlClientesWhere += " AND " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDINSTITUCION_SUBT1 + " = :" + contador;		
	       			
	       			// 22 idTipoCVSubtipo2
	       			if (formulario.getIdTipoCVSubtipo2()!=null && !formulario.getIdTipoCVSubtipo2().equals("")){
	       				String[] datosCVSubtipo2 = formulario.getIdTipoCVSubtipo2().toString().split("@");

       					contador++;
       					idTipoCVSubtipo2 = new Integer(datosCVSubtipo2[0]);
       					codigosBind.put(new Integer(contador), idTipoCVSubtipo2.toString());
       					sqlClientesWhere += " AND " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDTIPOCVSUBTIPO2 + " = :" + contador;
       					
				       	contador++;
				       	idInstitucionSubtipo2 = new Integer(datosCVSubtipo2[1]);
				       	codigosBind.put(new Integer(contador), idInstitucionSubtipo2.toString()); 
				       	sqlClientesWhere += " AND " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDINSTITUCION_SUBT2 + " = :" + contador;
	       			}		       			
       			}
	       		
	       		sqlClientesWhere +=  ") ";
	       	}
	       
//         23 Comision	       
			if (formulario.getComision()!=null && !formulario.getComision().equals("")){
				  String[] datosCVSubtipo1;
				  datosCVSubtipo1=formulario.getComision().toString().split("@");
				  idTipoCVSubtipo1=new Integer(datosCVSubtipo1[0]);
				  idInstitucionSubtipo1=new Integer(datosCVSubtipo1[1]);
				  
				sqlClientesWhere += " AND EXISTS (SELECT 1 "+
											      " FROM  "+CenDatosCVBean.T_NOMBRETABLA+"   "+
												  " WHERE "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" = "+CenDatosCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_IDPERSONA+" "+
								                    " AND "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" = "+CenDatosCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_IDINSTITUCION+" ";
				contador++;
			      codigosBind.put(new Integer(contador),idTipoCVSubtipo1);
			      sqlClientesWhere+=				" AND "+CenDatosCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_IDTIPOCVSUBTIPO1+"=:"+contador;
			      contador++;
			      codigosBind.put(new Integer(contador),idInstitucionSubtipo1);
			      sqlClientesWhere+=                " AND "+CenDatosCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_IDINSTITUCION_SUBT1+"=:"+contador+
								                    " AND "+ CenDatosCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_FECHABAJA+" IS NULL)";
													
					
					
					
			}
			
//         24 Tipo Colegiacion
			 if (formulario.getTipoColegiacion()!=null && !formulario.getTipoColegiacion().equals("")){
			 	if (formulario.getTipoColegiacion().equals(String.valueOf(ClsConstants.ESTADO_COLEGIAL_ALTA))){
			 		
				  sqlClientesWhere += " AND 1 in (select 1"+ 
                                      "            from "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+
                                      "            where "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"."+CenDatosColegialesEstadoBean.C_IDINSTITUCION+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+
                                      "             and "+CenDatosColegialesEstadoBean.C_IDPERSONA+"="+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+
                                      "             and "+CenDatosColegialesEstadoBean.C_FECHAESTADO+" = (select max("+CenDatosColegialesEstadoBean.C_FECHAESTADO+")"+
                                      "                                                                   from "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+
									  "                                                                   where "+ CenDatosColegialesEstadoBean.C_IDINSTITUCION+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+
									  "																		and "+CenDatosColegialesEstadoBean.C_IDPERSONA+"="+ CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA;
				  
			      sqlClientesWhere +=                    "																		and "+CenDatosColegialesEstadoBean.C_FECHAESTADO+" <= sysdate)";
			      sqlClientesWhere +=                     "                                                                     and "+CenDatosColegialesEstadoBean.C_IDESTADO+" in ("+ClsConstants.ESTADO_COLEGIAL_EJERCIENTE+","+ClsConstants.ESTADO_COLEGIAL_SINEJERCER+"))";
				  
				  
				  
			 	} else{
			 	  sqlClientesWhere += " AND exists(select 1"+ 
                  "            from "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+
                  "            where "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"."+CenDatosColegialesEstadoBean.C_IDINSTITUCION+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+
                  "             and "+CenDatosColegialesEstadoBean.C_IDPERSONA+"="+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+
                  "             and "+CenDatosColegialesEstadoBean.C_FECHAESTADO+" = (select max("+CenDatosColegialesEstadoBean.C_FECHAESTADO+")"+
                  "                                                                   from "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+
				  "                                                                   where "+ CenDatosColegialesEstadoBean.C_IDINSTITUCION+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+
				  "																		and "+CenDatosColegialesEstadoBean.C_IDPERSONA+"="+ CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA;
			 	
			      sqlClientesWhere +="																		and "+CenDatosColegialesEstadoBean.C_FECHAESTADO+" <=sysdate)";
			      contador++;
			      codigosBind.put(new Integer(contador),formulario.getTipoColegiacion());
			      sqlClientesWhere +="                                                                     and "+CenDatosColegialesEstadoBean.C_IDESTADO+" in (:"+contador+"))";	
			 	}
			 }
			
	       sqlClientes+=sqlClientesWhere;
	       sqlContarClientes+=sqlClientesWhere;
	       //aalg: se a�ade la ordenacion en espa�ol 
	       sqlClientes+= " ORDER BY NLSSORT("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+ "||' '||"+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2+", 'NLS_SORT=SPANISH'), NLSSORT("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NOMBRE+", 'NLS_SORT=SPANISH')";
	       
	       PaginadorBind paginador = new PaginadorBind(sqlClientes,sqlContarClientes,codigosBind);				
			int totalRegistros = paginador.getNumeroTotalRegistros();
			
			if (totalRegistros==0){					
				paginador =null;
			}
			
			return paginador;
		} 
		catch (Exception e) { 	
			throw new ClsExceptions(e,"Error obteniendo clientes colegiados"); 
		}
	}

	/**
	 * Obtiene los clientes no colegiados para una instituci�n y
	 * un formulario de datos (BusquedaClientesForm)
	 * 
	 * @param idInstitucion 
	 * @param formulario Formulario de busqueda de clientes con los datos de busqueda 
	 * @return java.util.Vector Vector de tablas hash  
	 */
	public PaginadorBind getClientesNoColegiados(String idInstitucion, BusquedaClientesForm formulario) throws ClsExceptions , SIGAException{

		String sqlClientes = "";
		Integer idTipoCVSubtipo1 = null;
		Integer idInstitucionSubtipo1=null;
		Integer idTipoCVSubtipo2 = null;
		Integer idInstitucionSubtipo2=null;
		Hashtable codigosBind = new Hashtable();
		int contador=0;
	  	
	  	// Acceso a BBDD
		try { 
			//String bBusqueda = formulario.getChkBusqueda();
			boolean bBusqueda  = UtilidadesString.stringToBoolean(formulario.getValorCheck());
			// consulta de insituciones
			sqlClientes = "SELECT "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NIFCIF+", "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NOMBRE+", "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+", "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2+", "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_FECHANACIMIENTO+", "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" , "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" "+ " ,  nvl("+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_NOAPARECERREDABOGACIA+",'0') "+CenClienteBean.C_NOAPARECERREDABOGACIA+"  " +" ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_NOENVIARREVISTA+"  " +
							
			//Campos Sociedad SJ que dice si es o no una sociedad de Servicios Juridicos
							","+CenNoColegiadoBean.T_NOMBRETABLA+"."+CenNoColegiadoBean.C_SOCIEDADSJ+
							","+CenNoColegiadoBean.T_NOMBRETABLA+"."+CenNoColegiadoBean.C_TIPO+", "+
							"decode((select '1' "+
							"        from dual" +
							"        where "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" > 1000" +
							"          and  "+CenNoColegiadoBean.T_NOMBRETABLA+"."+CenNoColegiadoBean.C_TIPO+"='1'" +
							"       union" +
							"        select '2'" +
							"        from dual" +
							"        where "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" > 1000" +
							"          and "+CenNoColegiadoBean.T_NOMBRETABLA+"."+CenNoColegiadoBean.C_TIPO+"<>'1'" +
							"        )," +
							"        '1'," +
							"         1," +
							"        '2'," +
							"         2," +
							"         3" +
							"      ) TIPO1"+// Se quiere ordenar primero por los no colegiados, sociedades y por �ltimo las instituciones
							//" ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_NOAPARECERREDABOGACIA+"  " +
							" FROM  "+CenPersonaBean.T_NOMBRETABLA+","+
						  CenClienteBean.T_NOMBRETABLA+","+
						  CenNoColegiadoBean.T_NOMBRETABLA+" ";
			
			sqlClientes+=	" WHERE " +
				" NOT EXISTS (SELECT 1 "+
				" FROM  "+CenColegiadoBean.T_NOMBRETABLA+" "+
				" WHERE "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDPERSONA+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+"  "+
				" AND "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDINSTITUCION+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" ) "+
				" AND "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDPERSONA+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+"  ";
			
				contador++;
			     codigosBind.put(new Integer(contador),idInstitucion);
			  
			     sqlClientes+=" AND ("+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+"  = :" + contador ;
			     contador++;
			     codigosBind.put(new Integer(contador),ClsConstants.TIPO_CARACTER_PRIVADO);
			     
			     sqlClientes+=" OR "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_CARACTER+"  <> :" + contador + ")"+
				//JOIN entre CEN_CLIENTE y CEN_NOCOLEGIADO:
				" AND "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" = "+CenNoColegiadoBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" (+) "+
				" AND "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" = "+CenNoColegiadoBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" (+) ";
				contador++;
			     codigosBind.put(new Integer(contador),"0");
			     sqlClientes+=" AND "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_LETRADO+" =:"+contador;
			
			
//	 1
	       if (formulario.getNombreInstitucion()!= null && !formulario.getNombreInstitucion().trim().equals("")) {
	       	contador++;
		     codigosBind.put(new Integer(contador),formulario.getNombreInstitucion());
	       		sqlClientes += " AND "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" = :"+contador;
	       } else {
	       		String institucionesVisibles = CenVisibilidad.getVisibilidadInstitucion(idInstitucion);
	       		
	       		sqlClientes += " AND "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+"  IN ("+institucionesVisibles+") ";
	       }
//	 3
	       if (!formulario.getNombrePersona().trim().equals("")) {
	       	if ((bBusqueda ) ) {
	       		contador++;
	       		sqlClientes += " AND ("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NOMBRE+" "+ComodinBusquedas.prepararSentenciaExactaBind(formulario.getNombrePersona().trim(),contador,codigosBind)+") ";
				
	       	}else{
	       	contador++;	
	       	 sqlClientes += " AND ("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getNombrePersona().trim(),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NOMBRE,contador,codigosBind)+") ";
	       	}
				
	       }
	       
//	     4 y 5 Criterio de busqueda especial para los campos Apellido1 y Apellido2
	       if (!formulario.getApellido1().trim().equals("")&&(!formulario.getApellido2().trim().equals(""))) {// Los dos campos rellenos
	       	if ((bBusqueda ) ) {
	       		contador++;
	       		sqlClientes += " AND ((("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+" "+ComodinBusquedas.prepararSentenciaExactaBind(formulario.getApellido1().trim(),contador,codigosBind )+")";
	       		contador++;
	       		sqlClientes += " AND ("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2+" "+ComodinBusquedas.prepararSentenciaExactaBind(formulario.getApellido2().trim(),contador,codigosBind )+"))";
	       		contador++;
	       		sqlClientes += " OR ("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+" "+ComodinBusquedas.prepararSentenciaExactaBind(formulario.getApellido1().trim()+" "+formulario.getApellido2().trim(),contador,codigosBind )+"))";
	       	}else{
	       		contador++;
	       		sqlClientes += " AND ((("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido1().trim(),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1,contador,codigosBind )+")";
	       		contador++;
	       		sqlClientes += " AND ("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido2().trim(),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2,contador,codigosBind)+"))";
	       		contador++;
	       		sqlClientes += " OR ("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido1().trim()+" "+formulario.getApellido2().trim() ,CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1,contador,codigosBind )+"))";
	       		
	       	}
	       }else if (!formulario.getApellido1().trim().equals("")&&(formulario.getApellido2().trim().equals(""))) {//Apellido1 relleno
	       	if ((bBusqueda) ) {
	       		contador++;
	       		sqlClientes += " AND (("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+" "+ComodinBusquedas.prepararSentenciaExactaBind(formulario.getApellido1(),contador,codigosBind)+")";
	       		contador++;
	       		sqlClientes +=" OR ("+ComodinBusquedas.prepararSentenciaCompletaBind(UtilidadesBDAdm.validateChars(formulario.getApellido1().trim()),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1,contador,codigosBind)+" ))";
	       	}else{
	       		contador++;
	       		sqlClientes += " AND "+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido1().trim(),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1,contador,codigosBind );
	       		
	       	}
	       }else if (formulario.getApellido1().trim().equals("")&&(!formulario.getApellido2().trim().equals(""))) {//Apellido2 relleno
	       	if ((bBusqueda ) ) {
	       		contador++;
	       		sqlClientes += " AND (("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2+" "+ComodinBusquedas.prepararSentenciaExactaBind(formulario.getApellido2().trim(),contador,codigosBind )+")";
	       		contador++;
	       		sqlClientes += " OR ("+ComodinBusquedas.prepararSentenciaCompletaBind(UtilidadesBDAdm.validateChars(formulario.getApellido2().trim()),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1,contador,codigosBind)+" )) ";
	       		 
	       	}else{
	       		contador++;
	       		sqlClientes += " AND "+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido2().trim(),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2,contador,codigosBind);
				               
	       	}
	       	
	       }  

	      /* if (!formulario.getApellido1().trim().equals("")) {
	       	if ((bBusqueda != null && bBusqueda.equals("1")) ) {
	       		sqlClientes += " AND ("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+" "+ComodinBusquedas.prepararSentenciaExacta(formulario.getApellido1().trim())+") ";
	       		 
	       	}else{
	       	 sqlClientes += " AND ("+ComodinBusquedas.prepararSentenciaCompleta(formulario.getApellido1().trim(),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1)+") ";
	       	}	  
	       }
5
	       if (!formulario.getApellido2().trim().equals("")) {
	       	if ((bBusqueda != null && bBusqueda.equals("1")) ) {
	       		sqlClientes += " AND ("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2+" "+ComodinBusquedas.prepararSentenciaExacta(formulario.getApellido2().trim())+") ";
	       		
	       	}else{
	       	  sqlClientes += " AND ("+ComodinBusquedas.prepararSentenciaCompleta(formulario.getApellido2().trim(),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2)+") ";
	       	}
	       }*/
//	 6  //Consulta sobre el campo NIF/CIF, si el usuario mete comodines la b�squeda es como se hac�a siempre, en el caso
		// de no meter comodines se ha creado un nuevo metodo ComodinBusquedas.preparaCadenaNIFSinComodin para que monte 
	    // la consulta adecuada. 
	       if (formulario.getNif()!=null && !formulario.getNif().trim().equals("")) {
	    	   if ((bBusqueda ) ) {
	    		   contador++;
	    		   codigosBind.put(new Integer(contador), formulario.getNif().trim().toUpperCase());
	    		   sqlClientes +=" AND UPPER("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NIFCIF+")= :"+contador;
//	    		   //a peticion de lp INC_06550_SIGA. Estaba la siguiente linea
	    		  // sqlClientes +=" AND "+ComodinBusquedas.prepararSentenciaNIFExacta(formulario.getNif(),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NIFCIF);
	    	   }else{
	    		   if (ComodinBusquedas.hasComodin(formulario.getNif())){
	    			   contador++;
	    			   sqlClientes += " AND ("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getNif().trim(),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NIFCIF,contador,codigosBind)+") ";
	    		   }else{

	    			   contador++;
	    			   sqlClientes +=" AND "+ComodinBusquedas.prepararSentenciaNIFBind(formulario.getNif(),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NIFCIF,contador,codigosBind);	
	    		   }
	    	   }
	       }
//	 7
//	       if (!formulario.getTipoCliente().trim().equals("")) {
//		       if (formulario.getTipoCliente().trim().equals(ClsConstants.DB_FALSE)) {
//	       		// tipo persona juridica
//		       	sqlClientes += " AND ("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDTIPOIDENTIFICACION+" = "+ClsConstants.TIPO_IDENTIFICACION_CIF+") ";
//		       } else {
//	       		// tipo persona ficia
//		       	sqlClientes += " AND ("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDTIPOIDENTIFICACION+" IN ("+ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE+","+ClsConstants.TIPO_IDENTIFICACION_PASAPORTE+","+ClsConstants.TIPO_IDENTIFICACION_NIF+")) ";
//		       }
//	       } 
//	 8
	       String grupoClientes=formulario.getGrupoClientes();
	       if (grupoClientes!=null && !grupoClientes.trim().equals("")) {
	    	   int coma=grupoClientes.indexOf(",");
	    	   if(coma!=-1){
	    	   	
	       		sqlClientes += " AND EXISTS (SELECT 1 "+
				" FROM  "+CenGruposClienteClienteBean.T_NOMBRETABLA+"  "+
				" WHERE "+CenGruposClienteClienteBean.T_NOMBRETABLA+"."+CenGruposClienteClienteBean.C_IDPERSONA+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+"  "+
				" AND "+CenGruposClienteClienteBean.T_NOMBRETABLA+"."+CenGruposClienteClienteBean.C_IDINSTITUCION+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+"  ";
	       		contador++;
				codigosBind.put(new Integer(contador),grupoClientes.substring(0,coma));
				sqlClientes +=" AND "+CenGruposClienteClienteBean.T_NOMBRETABLA+"."+CenGruposClienteClienteBean.C_IDGRUPO+" = :"+contador;
				contador++;
				codigosBind.put(new Integer(contador),grupoClientes.substring(coma+1));
				sqlClientes +=" AND "+CenGruposClienteClienteBean.T_NOMBRETABLA+"."+CenGruposClienteClienteBean.C_IDINSTITUCION_GRUPO+" = :"+contador+
				") ";
	    	   }
	    	   }
//	 9 
	       
	       if (formulario.getSexo()!=null && !formulario.getSexo().trim().equals("")) {
	       	contador++;
			codigosBind.put(new Integer(contador),formulario.getSexo());
	       		sqlClientes += " AND ("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_SEXO+"  = :"+contador+") ";
	       }
//	 13
	       if (formulario.getDomicilio()!=null && !formulario.getDomicilio().trim().equals("")) {
	    	contador++;
	   			sqlClientes += " AND EXISTS (SELECT 1"+
				" FROM  "+CenDireccionesBean.T_NOMBRETABLA+"   "+
				" WHERE "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDINSTITUCION+"= "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+"  "+
				" AND "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDPERSONA+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+"  "+
				" AND "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_FECHABAJA+" is null "+
				" AND "+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getDomicilio().trim(),CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_DOMICILIO,contador,codigosBind)+") ";
				
	       }
//	 14
	       if (formulario.getCodigoPostal()!=null && !formulario.getCodigoPostal().trim().equals("")) {
	       	contador++;
			codigosBind.put(new Integer(contador),formulario.getCodigoPostal());
				sqlClientes += " AND EXISTS (SELECT 1"+
				" FROM  "+CenDireccionesBean.T_NOMBRETABLA+"  "+
				" WHERE "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDINSTITUCION+"= "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+"  "+
				" AND "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDPERSONA+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+"  "+
				" AND "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_FECHABAJA+" is null "+
				" AND "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_CODIGOPOSTAL+"= :"+contador+") ";
	       }
//	 15
	       if (formulario.getTelefono()!=null && !formulario.getTelefono().trim().equals("")) {
	    	contador++;
			
				sqlClientes += " AND EXISTS (SELECT 1 "+
				" FROM  "+CenDireccionesBean.T_NOMBRETABLA+"   "+
				" WHERE "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDINSTITUCION+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+"  "+
				" AND "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDPERSONA+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+"  "+
				" AND "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_FECHABAJA+" is null "+
				" AND ("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getTelefono().trim(),CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_TELEFONO1,contador,codigosBind);
			contador++;	
				sqlClientes +=" OR "+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getTelefono().trim(),CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_TELEFONO2,contador,codigosBind);
			contador++;		
				sqlClientes +=" OR " +" "+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getTelefono().trim(),CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_MOVIL,contador,codigosBind)+")) ";
		   }
//	 16
	       if (formulario.getFax()!=null && !formulario.getFax().trim().equals("")) {
				sqlClientes += " AND EXISTS (SELECT 1 "+
				" FROM  "+CenDireccionesBean.T_NOMBRETABLA+"  "+
				" WHERE "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDINSTITUCION+"= "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+"  "+
				" AND "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDPERSONA+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+"  "+
				" AND "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_FECHABAJA+" is null ";
				contador++;	
				sqlClientes +=" AND ("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getFax().trim(),CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_FAX1,contador,codigosBind);
				contador++;	
				sqlClientes +=" OR "+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getFax().trim(),CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_FAX2,contador,codigosBind)+")) ";
		   }
//	 17         
	       if (formulario.getCorreo()!=null && !formulario.getCorreo().trim().equals("")) {
				sqlClientes += " AND EXISTS (SELECT 1"+
				" FROM  "+CenDireccionesBean.T_NOMBRETABLA+"   "+
				" WHERE "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDINSTITUCION+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+"  "+
				" AND "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDPERSONA+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+"  "+
				" AND "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_FECHABAJA+" is null ";
				contador++;
				sqlClientes +=" AND "+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getCorreo().trim(),CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_CORREOELECTRONICO,contador,codigosBind)+") ";
				
		   }
//	 18
	       /*if (formulario.getFechaAlta()!=null && !formulario.getFechaAlta().trim().equals("")) {
				sqlClientes += " AND EXISTS (SELECT 1 "+
				" FROM FAC_FACTURA "+
				" WHERE FAC_FACTURA.IDPERSONA = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+"  "+
				" AND FAC_FACTURA.IDINSTITUCION = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+"  "+
				" AND TO_CHAR(FAC_FACTURA.FECHAEMISION,'DD/MM/YYYY') = '" + formulario.getFechaAlta() + "') ";
		   }*/
	       String fDesdeAlta = formulario.getFechaAltaDesde(); 
		   String fHastaAlta = formulario.getFechaAltaHasta();
			if ((fDesdeAlta != null && !fDesdeAlta.trim().equals("")) || (fHastaAlta != null && !fHastaAlta.trim().equals(""))) {
				if (!fDesdeAlta.equals(""))
					fDesdeAlta = GstDate.getApplicationFormatDate("", fDesdeAlta); 
				if (!fHastaAlta.equals(""))
					fHastaAlta = GstDate.getApplicationFormatDate("", fHastaAlta);
				sqlClientes += " AND EXISTS (SELECT 1 "+
												" FROM FAC_FACTURA "+
												" WHERE FAC_FACTURA.IDPERSONA = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" "+
												" AND FAC_FACTURA.IDINSTITUCION = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" ";
				Vector vCondicion=GstDate.dateBetweenDesdeAndHastaBind("FAC_FACTURA.FECHAEMISION", fDesdeAlta, fHastaAlta,contador,codigosBind);
				
				contador=new Integer(vCondicion.get(0).toString()).intValue();
				sqlClientes +=" and " + vCondicion.get(1) + ") ";
				
												
			}
			
//	 19
	       ////jta comento esto porque tarda mucho
			/*if (formulario.getConcepto()!=null && !formulario.getConcepto().trim().equals("")) {
				sqlClientes += " AND EXISTS (SELECT 1 "+
				" FROM FAC_LINEAFACTURA , FAC_FACTURA  "+
				" WHERE FAC_LINEAFACTURA.IDINSTITUCION = FAC_FACTURA.IDINSTITUCION "+
				" AND FAC_LINEAFACTURA.IDFACTURA = FAC_FACTURA.IDFACTURA "+
				" AND FAC_FACTURA.IDPERSONA = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+"  "+
				" AND FAC_FACTURA.IDINSTITUCION = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+"  ";
				contador++;
				sqlClientes +=" AND "+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getConcepto().trim(),"FAC_LINEAFACTURA.DESCRIPCION",contador,codigosBind)+") ";
		   }*/
//	  	 20 fecha nacimiento
	       String fDesdeNac = formulario.getFechaNacimientoDesde(); 
		   String fHastaNac = formulario.getFechaNacimientoHasta();
			if ((fDesdeNac != null && !fDesdeNac.trim().equals("")) || (fHastaNac != null && !fHastaNac.trim().equals(""))) {
				if (!fDesdeNac.equals(""))
					fDesdeNac = GstDate.getApplicationFormatDate("", fDesdeNac); 
				if (!fHastaNac.equals(""))
					fHastaNac = GstDate.getApplicationFormatDate("", fHastaNac);
				
				
				Vector vCondicion=GstDate.dateBetweenDesdeAndHastaBind(CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_FECHANACIMIENTO, fDesdeNac, fHastaNac,contador,codigosBind);
				
				contador=new Integer(vCondicion.get(0).toString()).intValue();
				sqlClientes +=" and " + vCondicion.get(1) ;
				
				
				
			}
	       /*if (formulario.getFechaNacimiento()!=null && !formulario.getFechaNacimiento().trim().equals("")) {
				sqlClientes += " AND "+
				" TO_CHAR("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_FECHANACIMIENTO+",'DD/MM/YYYY') = '" + formulario.getFechaNacimiento() + "' ";
		   }*/
//	  	 21 Tipo (si el cliente es Personal, Sociedad, etc)      
	       if (formulario.getTipo()!=null && !formulario.getTipo().trim().equals("")) {
	       		// 21.a) Chequeo si se selecciona en el combo Sociedad de Servicios Juridicos este valor:
	       		if (formulario.getTipo().trim().equals(ClsConstants.COMBO_TIPO_SOCIEDAD_SERVICIOS_JURIDICOS)) {
					sqlClientes += " AND EXISTS (SELECT 1"+
					" FROM  "+CenNoColegiadoBean.T_NOMBRETABLA+"   "+
					" WHERE "+CenNoColegiadoBean.T_NOMBRETABLA+"."+CenNoColegiadoBean.C_IDINSTITUCION+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+"  "+
					" AND "+CenNoColegiadoBean.T_NOMBRETABLA+"."+CenNoColegiadoBean.C_IDPERSONA+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+"  ";
					contador++;
					codigosBind.put(new Integer(contador),ClsConstants.DB_TRUE);
					sqlClientes +=" AND "+CenNoColegiadoBean.T_NOMBRETABLA+"."+CenNoColegiadoBean.C_SOCIEDADSJ+" = :"+contador+" ) ";
				// 21.b) Chequeo el tipo seleccionado en el combo si no es de Sociedad de Servicios Juridicos:
	       		} else 
	       		if (formulario.getTipo().trim().equals(ClsConstants.COMBO_TIPO_SOCIEDAD_PROFESIONAL)) {
					sqlClientes += " AND EXISTS (SELECT 1 "+
					" FROM  "+CenNoColegiadoBean.T_NOMBRETABLA+"   "+
					" WHERE "+CenNoColegiadoBean.T_NOMBRETABLA+"."+CenNoColegiadoBean.C_IDINSTITUCION+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+"  "+
					" AND "+CenNoColegiadoBean.T_NOMBRETABLA+"."+CenNoColegiadoBean.C_IDPERSONA+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+"  ";
					contador++;
					codigosBind.put(new Integer(contador),ClsConstants.DB_TRUE);
					sqlClientes +=" AND "+CenNoColegiadoBean.T_NOMBRETABLA+"."+CenNoColegiadoBean.C_SOCIEDADSP+" = :"+contador+" ) ";
				// 21.b) Chequeo el tipo seleccionado en el combo si no es de Sociedad de Servicios Juridicos:
	       		} else 
	       		 if (formulario.getTipo().trim().equals(ClsConstants.COMBO_TIPO_SOCIEDAD_MULTIDISCIPLINAR)) {
						sqlClientes += "  AND 1 <(SELECT count(1)"+
						" FROM  "+CenNoColegiadoActividadBean.T_NOMBRETABLA+"   "+
						" WHERE "+CenNoColegiadoBean.T_NOMBRETABLA+"."+CenNoColegiadoBean.C_IDINSTITUCION+" = "+CenNoColegiadoActividadBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+"  "+
						" AND "+CenNoColegiadoBean.T_NOMBRETABLA+"."+CenNoColegiadoBean.C_IDPERSONA+" = "+CenNoColegiadoActividadBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+"  ";
						contador++;
						codigosBind.put(new Integer(contador),ClsConstants.DB_TRUE);
						sqlClientes +=" AND "+CenNoColegiadoBean.T_NOMBRETABLA+"."+CenNoColegiadoBean.C_SOCIEDADSP+" = :"+contador+" "+
						" ) ";
		       		}else 
			       		 if (formulario.getTipo().trim().equals(ClsConstants.COMBO_TIPO_PERSONAL)) {
								sqlClientes += " AND EXISTS (SELECT "+CenNoColegiadoBean.T_NOMBRETABLA+"."+CenNoColegiadoBean.C_TIPO+" "+
								" FROM  "+CenNoColegiadoBean.T_NOMBRETABLA+"   "+
								" WHERE "+CenNoColegiadoBean.T_NOMBRETABLA+"."+CenNoColegiadoBean.C_IDINSTITUCION+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+"  "+
								" AND "+CenNoColegiadoBean.T_NOMBRETABLA+"."+CenNoColegiadoBean.C_IDPERSONA+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+"  ";
				       			contador++;
								codigosBind.put(new Integer(contador),ClsConstants.DB_TRUE);
								sqlClientes +=" AND "+CenNoColegiadoBean.T_NOMBRETABLA+"."+CenNoColegiadoBean.C_TIPO+" = :"+contador+" "+	" ) ";

			       		 } else{

			       			 // caso se sociedad que est� incluida en los tipos de sociedad de la tabla.
			       			 // la busqueda es por la letra definida del CIF.
				       			contador++;
								codigosBind.put(new Integer(contador),formulario.getTipo().trim());
								// para busqueda por la tletra del CIF
								//sqlClientes +=" AND SUBSTR(CEN_PERSONA.NIFCIF,1,1)=:"+contador+" ";
								
								sqlClientes +=" AND CEN_NOCOLEGIADO.TIPO=:"+contador+" ";
			       		} 
	       		
	       		
	       		//Si es Sociedad Multidisciplinar
	       		/*
	       		 SELECT NOCOL.IDPERSONA,NOCOL.IDINSTITUCION FROM CEN_NOCOLEGIADO NOCOL,CEN_NOCOLEGIADO_ACTIVIDAD ACT
				WHERE NOCOL.SOCIEDADPROFESIONAL=1
				AND NOCOL.IDPERSONA=ACT.IDPERSONA
				AND NOCOL.IDINSTITUCION=ACT.IDINSTITUCION
				GROUP BY NOCOL.IDPERSONA,NOCOL.IDINSTITUCION
				HAVING COUNT(1)>1
	       		 
	       		 */		
	       		
	       		
		   }
	       
	       if (formulario.getTipoApunte()!=null && !formulario.getTipoApunte().equals("")){
	       	sqlClientes +=   " AND EXISTS (SELECT 1 "+
									      " FROM  "+CenDatosCVBean.T_NOMBRETABLA+"   "+
										  " WHERE "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" = "+CenDatosCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_IDPERSONA+" "+
							              " AND "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" = "+CenDatosCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_IDINSTITUCION+" ";
	       	contador++;
			codigosBind.put(new Integer(contador),formulario.getTipoApunte());
			sqlClientes +=	              " AND "+CenDatosCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_IDTIPOCV+"=:"+contador+
										  " AND "+ CenDatosCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_FECHABAJA+" IS NULL)";
		   }
	       
	       
//	       22 idTipoCVSubtipo1
	       if (formulario.getIdTipoCVSubtipo1()!=null && !formulario.getIdTipoCVSubtipo1().equals("")){
	    	   String[] datosCVSubtipo1;
	    	   datosCVSubtipo1=formulario.getIdTipoCVSubtipo1().toString().split("@");
				  idTipoCVSubtipo1=new Integer(datosCVSubtipo1[0]);
				  idInstitucionSubtipo1=new Integer(datosCVSubtipo1[1]);
				  sqlClientes +=   " AND EXISTS (SELECT 1 "+
			 " FROM  "+CenDatosCVBean.T_NOMBRETABLA+"   "+
			  " WHERE "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" = "+CenDatosCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_IDPERSONA+" "+
               " AND "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" = "+CenDatosCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_IDINSTITUCION+" ";
			contador++;
	       	codigosBind.put(new Integer(contador),formulario.getTipoApunte());
	       	sqlClientes +=  " AND "+CenDatosCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_IDTIPOCV+"=:"+contador;
	       	contador++;
	       	codigosBind.put(new Integer(contador),idTipoCVSubtipo1.toString());
	       	sqlClientes +=   " AND "+CenDatosCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_IDTIPOCVSUBTIPO1+"=:"+contador;
	       	contador++;
	       	codigosBind.put(new Integer(contador),idInstitucionSubtipo1.toString());
	       	sqlClientes +=   " AND "+CenDatosCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_IDINSTITUCION_SUBT1+"=:"+contador+
				                  " AND "+ CenDatosCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_FECHABAJA+" IS NULL)";												
			
			
			
					
		   }
	      	       
	       
//	       22 idTipoCVSubtipo2
	       if (formulario.getIdTipoCVSubtipo2()!=null && !formulario.getIdTipoCVSubtipo2().equals("")){
	    	   String[] datosCVSubtipo2;
				 datosCVSubtipo2=formulario.getIdTipoCVSubtipo2().toString().split("@");
				 idTipoCVSubtipo2=new Integer(datosCVSubtipo2[0]);
				 idInstitucionSubtipo2=new Integer(datosCVSubtipo2[1]);
				 sqlClientes +=   " AND EXISTS (SELECT 1 "+
			" FROM  "+CenDatosCVBean.T_NOMBRETABLA+"   "+
			  " WHERE "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" = "+CenDatosCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_IDPERSONA+" "+
             " AND "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" = "+CenDatosCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_IDINSTITUCION+" ";
			contador++;
	       	codigosBind.put(new Integer(contador),formulario.getTipoApunte());
	       	sqlClientes += " AND "+CenDatosCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_IDTIPOCV+"=:"+contador;
	       	contador++;
	       	codigosBind.put(new Integer(contador),idTipoCVSubtipo2.toString());
	       	sqlClientes += " AND "+CenDatosCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_IDTIPOCVSUBTIPO2+"=:"+contador;
	       	contador++;
	       	codigosBind.put(new Integer(contador),idInstitucionSubtipo2.toString()); 
	       	sqlClientes += " AND "+CenDatosCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_IDINSTITUCION_SUBT2+"=:"+contador;
	       	
	       	sqlClientes +=  " AND "+ CenDatosCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_FECHABAJA+" IS NULL)";
												
					
					
		   }
//         23 Comision	       
			if (formulario.getComision()!=null && !formulario.getComision().equals("")){
				  String[] datosCVSubtipo1;
				  datosCVSubtipo1=formulario.getComision().toString().split("@");
				  idTipoCVSubtipo1=new Integer(datosCVSubtipo1[0]);
				  idInstitucionSubtipo1=new Integer(datosCVSubtipo1[1]);
				  sqlClientes += " AND EXISTS (SELECT 1 "+
			      " FROM  "+CenDatosCVBean.T_NOMBRETABLA+"   "+
				  " WHERE "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" = "+CenDatosCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_IDPERSONA+" "+
                    " AND "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" = "+CenDatosCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_IDINSTITUCION+" ";
				  contador++;
				  codigosBind.put(new Integer(contador),idTipoCVSubtipo1.toString());
				  sqlClientes +=	" AND "+CenDatosCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_IDTIPOCVSUBTIPO1+"=:"+contador;
				  contador++;
				  codigosBind.put(new Integer(contador),idInstitucionSubtipo1.toString());
				  sqlClientes +=  " AND "+CenDatosCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_IDINSTITUCION_SUBT1+"=:"+contador+
                                  " AND "+ CenDatosCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_FECHABAJA+" IS NULL)";
			}
// ORDER BY:
			//aalg: se a�ade la ordenacion en espa�ol
	       sqlClientes+= " ORDER BY TIPO1,TIPO, NLSSORT("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+"||' '||"+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2+", 'NLS_SORT=SPANISH'), NLSSORT("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NOMBRE+", 'NLS_SORT=SPANISH')";
	       PaginadorBind paginador = new PaginadorBind(sqlClientes,codigosBind);				
			int totalRegistros = paginador.getNumeroTotalRegistros();
			
			if (totalRegistros==0){					
				paginador =null;
			}
  
			// RGG cambio para visibilidad
			/*rcClientes = this.find(sqlClientes); 
			salida = new Vector();
			if (rcClientes!=null) {
				for (int i = 0; i < rcClientes.size(); i++)	{
					Row filaClientes = (Row) rcClientes.get(i);
					salida.add(filaClientes.getRow());
				}
			}*/
			return paginador;
		} 
		catch (Exception e) { 	
			throw new ClsExceptions(e,"Error obteniendo clientes No colegiados"); 
		}
	}

	/**
	 * Obtiene los clientes no colegiados para una instituci�n y
	 * un formulario de datos (BusquedaClientesForm)
	 * 
	 * @param idInstitucion 
	 * @param formulario Formulario de busqueda de clientes con los datos de busqueda 
	 * @return java.util.Vector Vector de tablas hash  
	 */
	public PaginadorCaseSensitiveBind getClientesLetrados(String idInstitucion, BusquedaClientesForm formulario) throws ClsExceptions , SIGAException{
		Integer idTipoCVSubtipo1 = null;
		Integer idInstitucionSubtipo1=null;
		Integer idTipoCVSubtipo2 = null;
		Integer idInstitucionSubtipo2=null;
		String sqlClientes = "";
	  	
	  	// Acceso a BBDD
		int totalRegistros=0;
		int contador = 0;	            
        Hashtable codigos = new Hashtable();
		
		try {
			//String bBusqueda = formulario.getChkBusqueda();
			boolean bBusqueda  = UtilidadesString.stringToBoolean(formulario.getValorCheck());
			sqlClientes = "SELECT " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NIFCIF + ", " +			
					CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE + ", " +
					CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 + "," + 
					CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 + ", " +
					CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_FECHANACIMIENTO + ", " +
					CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + ", " +
					CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDINSTITUCION + ", " +
					CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_DOMICILIO + ", " +
					CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_CODIGOPOSTAL + ", " +
					" NVL(" + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_POBLACIONEXTRANJERA + "," + CenPoblacionesBean.T_NOMBRETABLA + "." + CenPoblacionesBean.C_NOMBRE + ") as POBLACION, " +
					" NVL(" + CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_NOAPARECERREDABOGACIA + ",'0') " + CenClienteBean.C_NOAPARECERREDABOGACIA +
				" FROM " + CenPersonaBean.T_NOMBRETABLA + ", " +
					CenClienteBean.T_NOMBRETABLA + ", " +
					CenDireccionesBean.T_NOMBRETABLA + ", " + 
					CenPoblacionesBean.T_NOMBRETABLA;
			
			sqlClientes += " WHERE " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + " = " + CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDPERSONA +
				" AND " + CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_LETRADO + " = 1 " +
				" AND " + CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDINSTITUCION + " = " + idInstitucion;			   
			
			//a�adimos la informaci�n sobre las direcciones del censo web
			sqlClientes+= " AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPOBLACION + " = " + CenPoblacionesBean.T_NOMBRETABLA + "." + CenPoblacionesBean.C_IDPOBLACION + "(+) " +
                  " AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPERSONA + "(+) = " + CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDPERSONA +
                  " AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDINSTITUCION + "(+) = " + CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDINSTITUCION +
                  " AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDDIRECCION + "(+) = F_SIGA_GETIDDIRECCION_TIPOPRE2(" + idInstitucion + "," + CenClienteBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + ", 3, null) ";
                  
	       if (formulario.getNombrePersona()!=null && !formulario.getNombrePersona().trim().equals("")) {
	       		if (bBusqueda) {       		
	       			sqlClientes += " AND (TRANSLATE(UPPER(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE + "), " + ComodinBusquedas.vocalesBuscar + ", " + ComodinBusquedas.vocalesSustituir + ") " + ComodinBusquedas.prepararSentenciaExactaTranslateUpper(formulario.getNombrePersona().trim()) + ") ";			  
	       		}else{
	       			sqlClientes += " AND (" + ComodinBusquedas.prepararSentenciaCompletaTranslateUpper(formulario.getNombrePersona().trim(), CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE) + ") ";  
	       		}
	       }
				
	       // Criterio de busqueda especial para los campos Apellido1 y Apellido2
	       if (!formulario.getApellido1().trim().equals("") && (!formulario.getApellido2().trim().equals(""))) {// Los dos campos rellenos
	       		if (bBusqueda) {
	       			sqlClientes += " AND (((TRANSLATE(UPPER(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 + "), " + ComodinBusquedas.vocalesBuscar + ", " + ComodinBusquedas.vocalesSustituir + ") " + ComodinBusquedas.prepararSentenciaExactaTranslateUpper(formulario.getApellido1().trim()) + ") " +
	       				" AND (TRANSLATE(UPPER(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 + "), " + ComodinBusquedas.vocalesBuscar + ", " + ComodinBusquedas.vocalesSustituir + ") " + ComodinBusquedas.prepararSentenciaExactaTranslateUpper(formulario.getApellido2().trim()) + ")) " +
	       		        " OR (TRANSLATE(UPPER(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 + "), " + ComodinBusquedas.vocalesBuscar + ", " + ComodinBusquedas.vocalesSustituir + ") " + ComodinBusquedas.prepararSentenciaExactaTranslateUpper(formulario.getApellido1().trim() + " " + formulario.getApellido2().trim()) + ")) ";
	       		} else {
	       			sqlClientes += " AND (((" + ComodinBusquedas.prepararSentenciaCompletaTranslateUpper(formulario.getApellido1().trim(), CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1) + ") " +
	   					" AND (" + ComodinBusquedas.prepararSentenciaCompletaTranslateUpper(formulario.getApellido2().trim(), CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2) + ")) " +
				        " OR (" + ComodinBusquedas.prepararSentenciaCompletaTranslateUpper(formulario.getApellido1().trim() + " " + formulario.getApellido2().trim(), CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1) + ")) ";       		
	       		}
	       		
	       } else if (!formulario.getApellido1().trim().equals("") && (formulario.getApellido2().trim().equals(""))) {//Apellido1 relleno
	       		if (bBusqueda) {
	       			sqlClientes += " AND ((TRANSLATE(UPPER(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 + "), " + ComodinBusquedas.vocalesBuscar + ", " + ComodinBusquedas.vocalesSustituir + ") " + ComodinBusquedas.prepararSentenciaExactaTranslateUpper(formulario.getApellido1()) + ") " +
	       				" OR (TRANSLATE(UPPER(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 + "), " + ComodinBusquedas.vocalesBuscar + ", " + ComodinBusquedas.vocalesSustituir + ") LIKE TRANSLATE(UPPER('" + UtilidadesBDAdm.validateChars(formulario.getApellido1().trim()) + " %'), " + ComodinBusquedas.vocalesBuscar + ", " + ComodinBusquedas.vocalesSustituir + "))) ";
	       		}else{
	       			sqlClientes += " AND " + ComodinBusquedas.prepararSentenciaCompletaTranslateUpper(formulario.getApellido1().trim(), CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1);       		
	       		}
	       		
	       } else if (formulario.getApellido1().trim().equals("") && (!formulario.getApellido2().trim().equals(""))) {//Apellido2 relleno
	       		if (bBusqueda) {
	       			sqlClientes += " AND (TRANSLATE(UPPER(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 + "), " + ComodinBusquedas.vocalesBuscar + ", " + ComodinBusquedas.vocalesSustituir + ") " + ComodinBusquedas.prepararSentenciaExactaTranslateUpper(formulario.getApellido2().trim()) + ") ";
	       		} else {
	       			sqlClientes += " AND " + ComodinBusquedas.prepararSentenciaCompletaTranslateUpper(formulario.getApellido2().trim(), CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2); 			               
	       		}       	
	       }           
	       
	       /*Consulta sobre el campo NIF/CIF, si el usuario mete comodines la b�squeda es como se hac�a siempre, 
	        * en el caso de no meter comodines se ha creado un nuevo metodo ComodinBusquedas.preparaCadenaNIFSinComodin para que monte la consulta adecuada.    
	        */
	       if (formulario.getNif()!=null && !formulario.getNif().trim().equals("")) {
	    	   if (bBusqueda) {
	    		   sqlClientes +=" AND UPPER(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NIFCIF + ") = '" + formulario.getNif().trim().toUpperCase() + "' ";    		   
	    	   }else{
	    		   if (ComodinBusquedas.hasComodin(formulario.getNif())){
	    			   sqlClientes += " AND (" + ComodinBusquedas.prepararSentenciaCompletaTranslateUpper(formulario.getNif().trim(), CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NIFCIF) + ") ";
	    		   }else{
	    			   sqlClientes +=" AND " + ComodinBusquedas.prepararSentenciaNIFUpper(formulario.getNif(), "UPPER(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NIFCIF + ") ");
	    		   }
	    	   }
	       }
	       
	       String grupoClientes=formulario.getGrupoClientes();
	       if (grupoClientes!=null && !grupoClientes.trim().equals("")) {
	    	   int coma=grupoClientes.indexOf(",");
	    	   if(coma!=-1){
	    	   		contador++;
	    	   		codigos.put(new Integer(contador), grupoClientes.substring(0,coma));   	   	    	   	
	    	   		sqlClientes += " AND EXISTS (SELECT 1 " +
		   				" FROM " + CenGruposClienteClienteBean.T_NOMBRETABLA +
	    			   	" WHERE " + CenGruposClienteClienteBean.T_NOMBRETABLA + "." + CenGruposClienteClienteBean.C_IDPERSONA + " = " + CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDPERSONA +
	    			   		" AND " + CenGruposClienteClienteBean.T_NOMBRETABLA + "." + CenGruposClienteClienteBean.C_IDINSTITUCION + " = " + CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDINSTITUCION +
	    			   		" AND " + CenGruposClienteClienteBean.T_NOMBRETABLA + "." + CenGruposClienteClienteBean.C_IDGRUPO + " = :" + contador;
	    	   	
	    	   		contador++;
	    	   		codigos.put(new Integer(contador),grupoClientes.substring(coma+1));
	    	   		sqlClientes += " AND " + CenGruposClienteClienteBean.T_NOMBRETABLA + "." + CenGruposClienteClienteBean.C_IDINSTITUCION_GRUPO + " = :" + contador + ") ";
	    	   }
	       }       
	
	       if (formulario.getSexo()!=null && !formulario.getSexo().trim().equals("")) {
	    	   	contador++;
	    	   	codigos.put(new Integer (contador), formulario.getSexo());
	       		sqlClientes += " AND (" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_SEXO + " = :" + contador + ") ";
	       }
	       
	       if (formulario.getFechaIncorporacion()!=null && !formulario.getFechaIncorporacion().trim().equals("")) {
	    	   	contador++;
	    	   	codigos.put(new Integer (contador), formulario.getFechaIncorporacion());
	       		sqlClientes += " AND (TO_CHAR(" + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_FECHAINCORPORACION + ", 'DD/MM/YYYY') = :" + contador + ") ";
	       }
	       
	       if (formulario.getDomicilio()!=null && !formulario.getDomicilio().trim().equals("")) {
	       	  	sqlClientes += " AND EXISTS (SELECT " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_DOMICILIO +
	       			" FROM " + CenDireccionesBean.T_NOMBRETABLA +
	       			" WHERE " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDINSTITUCION + " = " + CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDINSTITUCION +
	       				" AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPERSONA + " = " + CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDPERSONA +
	       				" AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHABAJA + " is null " +
	       				" AND " + ComodinBusquedas.prepararSentenciaCompletaTranslateUpper(formulario.getDomicilio().trim(), CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_DOMICILIO) + ") ";
	       }       
	
	       if (formulario.getCodigoPostal()!=null && !formulario.getCodigoPostal().trim().equals("")) {
	    	   	contador++;
	    	   	codigos.put(new Integer (contador),formulario.getCodigoPostal());
	    	   	sqlClientes += " AND EXISTS (SELECT " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_CODIGOPOSTAL +
		   			" FROM " + CenDireccionesBean.T_NOMBRETABLA +
		   			" WHERE " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDINSTITUCION + " = " + CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDINSTITUCION +
		   				" AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPERSONA + " = " + CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDPERSONA +
		   				" AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHABAJA + " is null " +
		   				" AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_CODIGOPOSTAL + " = :" + contador + ") ";
	       }
	
	       if (formulario.getTelefono()!=null && !formulario.getTelefono().trim().equals("")) {
	    	   	contador++;
	    	   	codigos.put(new Integer (contador),formulario.getTelefono());
	    	   	sqlClientes += " AND EXISTS (SELECT 1 "+
		   			" FROM " + CenDireccionesBean.T_NOMBRETABLA +
		   			" WHERE " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDINSTITUCION + " = " + CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDINSTITUCION +
		   				" AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPERSONA + " = " + CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDPERSONA +
		   				" AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHABAJA + " is null ";
	    	   	
				contador++;
				sqlClientes += " AND (" + ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getTelefono().trim(), CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_TELEFONO1, contador, codigos);
				
				contador++;
				sqlClientes += " OR " + ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getTelefono().trim(), CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_TELEFONO2, contador, codigos) +
					" OR " + ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getTelefono().trim(), CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_MOVIL, contador, codigos) + ")) ";
		   }
	       
	       if (formulario.getFax()!=null && !formulario.getFax().trim().equals("")) {
				sqlClientes += " AND EXISTS (SELECT 1 "+
					" FROM  " + CenDireccionesBean.T_NOMBRETABLA +
					" WHERE " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDINSTITUCION + " = " + CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDINSTITUCION +
						" AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPERSONA + " = " + CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDPERSONA +
						" AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHABAJA + " is null " +
						" AND (" + ComodinBusquedas.prepararSentenciaCompleta(formulario.getFax().trim(), CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FAX1) + 
						" OR " + ComodinBusquedas.prepararSentenciaCompleta(formulario.getFax().trim(), CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FAX2) + ")) ";
		   }
	
	       if (formulario.getCorreo()!=null && !formulario.getCorreo().trim().equals("")) {
				sqlClientes += " AND EXISTS (SELECT " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_CORREOELECTRONICO +
					" FROM " + CenDireccionesBean.T_NOMBRETABLA +
					" WHERE " +CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDINSTITUCION + " = " + CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDINSTITUCION +
						" AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPERSONA + " = " + CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDPERSONA +
						" AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHABAJA + " is null " +
						" AND " + ComodinBusquedas.prepararSentenciaCompleta(formulario.getCorreo().trim(), CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_CORREOELECTRONICO) + ") ";			
		   }
	 
	       String fDesdeAlta = formulario.getFechaAltaDesde(); 
		   String fHastaAlta = formulario.getFechaAltaHasta();
			if ((fDesdeAlta != null && !fDesdeAlta.trim().equals("")) || (fHastaAlta != null && !fHastaAlta.trim().equals(""))) {
				if (!fDesdeAlta.equals(""))
					fDesdeAlta = GstDate.getApplicationFormatDate("", fDesdeAlta); 
				
				if (!fHastaAlta.equals(""))
					fHastaAlta = GstDate.getApplicationFormatDate("", fHastaAlta);
				
				sqlClientes += " AND EXISTS (SELECT 1 " + 
					" FROM FAC_FACTURA " +
					" WHERE FAC_FACTURA.IDPERSONA = " + CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDPERSONA +
					" AND FAC_FACTURA.IDINSTITUCION = " + CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDINSTITUCION;
				
				Vector vCondicion = GstDate.dateBetweenDesdeAndHastaBind("FAC_FACTURA.FECHAEMISION", fDesdeAlta, fHastaAlta, contador, codigos);			
				contador = new Integer(vCondicion.get(0).toString()).intValue();
				sqlClientes += " AND " + vCondicion.get(1) + ") ";			
			}
	      
			// Fecha Nacimiento
			String fDesdeNac = formulario.getFechaNacimientoDesde(); 
			String fHastaNac = formulario.getFechaNacimientoHasta();
			if ((fDesdeNac != null && !fDesdeNac.trim().equals("")) || (fHastaNac != null && !fHastaNac.trim().equals(""))) {
				if (!fDesdeNac.equals(""))
					fDesdeNac = GstDate.getApplicationFormatDate("", fDesdeNac); 
				
				if (!fHastaNac.equals(""))
					fHastaNac = GstDate.getApplicationFormatDate("", fHastaNac);
				
				Vector vCondicion=GstDate.dateBetweenDesdeAndHastaBind(CenPersonaBean.C_FECHANACIMIENTO, fDesdeNac, fHastaNac, contador, codigos);			
				contador=new Integer(vCondicion.get(0).toString()).intValue();			
				sqlClientes += " AND " + vCondicion.get(1);
			}
	
			// Tipo Apunte
			if (formulario.getTipoApunte()!=null && !formulario.getTipoApunte().equals("")){
	       		contador++;
	       		codigos.put(new Integer(contador), formulario.getTipoApunte());
	       		sqlClientes += " AND EXISTS (SELECT 1 " +
	   				" FROM " + CenDatosCVBean.T_NOMBRETABLA +
					" WHERE " + CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDPERSONA + " = " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDPERSONA +
				    	" AND " + CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDINSTITUCION + " = " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDINSTITUCION +
			            " AND " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDTIPOCV + " = :" + contador +
						" AND " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_FECHABAJA + " IS NULL) ";
		   	}
	       
	       	// idTipoCVSubtipo1
	       	if (formulario.getIdTipoCVSubtipo1()!=null && !formulario.getIdTipoCVSubtipo1().equals("")){
	    	   	String[] datosCVSubtipo1;
	    	   	datosCVSubtipo1 = formulario.getIdTipoCVSubtipo1().toString().split("@");
	    	   	idTipoCVSubtipo1 = new Integer(datosCVSubtipo1[0]);
				idInstitucionSubtipo1 = new Integer(datosCVSubtipo1[1]);
				sqlClientes +=   " AND EXISTS (SELECT 1 " +
					" FROM  " + CenDatosCVBean.T_NOMBRETABLA +
					" WHERE " + CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDPERSONA + " = " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDPERSONA +
						" AND " + CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDINSTITUCION + " = " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDINSTITUCION;
			
				contador++;
				codigos.put(new Integer(contador), formulario.getTipoApunte());
				sqlClientes += " AND " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDTIPOCV + " = :" + contador;
				
				contador++;
				codigos.put(new Integer(contador), idTipoCVSubtipo1.toString());
				sqlClientes += " AND " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDTIPOCVSUBTIPO1 + " = :" + contador;
				
				contador++;
				codigos.put(new Integer(contador), idInstitucionSubtipo1.toString());
				sqlClientes += " AND " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDINSTITUCION_SUBT1 + " = :" + contador +
					" AND " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_FECHABAJA + " IS NULL) ";														
		   	}
	      	              
	       	// idTipoCVSubtipo2
	       	if (formulario.getIdTipoCVSubtipo2()!=null && !formulario.getIdTipoCVSubtipo2().equals("")){
	    	   	String[] datosCVSubtipo2;
				datosCVSubtipo2 = formulario.getIdTipoCVSubtipo2().toString().split("@");
				idTipoCVSubtipo2 = new Integer(datosCVSubtipo2[0]);
				idInstitucionSubtipo2 = new Integer(datosCVSubtipo2[1]);
				sqlClientes += " AND EXISTS (SELECT 1 " +
					" FROM " + CenDatosCVBean.T_NOMBRETABLA +
					" WHERE " + CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDPERSONA + " = " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDPERSONA +
						" AND " + CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDINSTITUCION + " = " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDINSTITUCION;
				
				contador++;
				codigos.put(new Integer(contador), formulario.getTipoApunte());
				sqlClientes += " AND " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDTIPOCV + " = :" + contador;
				
				contador++;
				codigos.put(new Integer(contador), idTipoCVSubtipo2.toString());
				sqlClientes += " AND " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDTIPOCVSUBTIPO2 + " = :" + contador;
				
				contador++;
				codigos.put(new Integer(contador),idInstitucionSubtipo2.toString()); 
				sqlClientes += " AND " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDINSTITUCION_SUBT2 + " = :" + contador +        	
					" AND " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_FECHABAJA + " IS NULL) ";
		   	}	       
	   
	       	//aalg: se a�aden los filtros por colegio. Se buscar�n aquellos letrados que tengan alguna colegiaci�n que cumpla todos los criterios especificados
	       	String sqlSubSelect = "SELECT DISTINCT " + CenColegiadoBean.C_IDPERSONA + 
			   " FROM " + CenColegiadoBean.T_NOMBRETABLA + " WHERE 1 = 1 ";
	       
	       	// Instituci�n
	       	if (formulario.getNombreInstitucion()!=null && !formulario.getNombreInstitucion().trim().equals("")) {
		       	contador++;
				codigos.put(new Integer(contador), formulario.getNombreInstitucion().trim());
				sqlSubSelect += " AND " + CenColegiadoBean.C_IDINSTITUCION + " = :" + contador;
	       	}
	       
	       	// N�mero de colegiado 
		   	if (formulario.getNumeroColegiado()!=null && !formulario.getNumeroColegiado().trim().equals("")) {
		       	contador++;
				codigos.put(new Integer(contador), formulario.getNumeroColegiado().trim());
				sqlSubSelect += " AND LTRIM(DECODE(" + CenColegiadoBean.C_COMUNITARIO + ", '1', " + CenColegiadoBean.C_NCOMUNITARIO + ", " + CenColegiadoBean.C_NCOLEGIADO + "),'0') = LTRIM(:" + contador + ", '0') " ;
		   	}
		   
		   	// Ejerciente
	       	if (formulario.getEjerciente()!=null && !formulario.getEjerciente().trim().equals("")) {
	       		contador++;
	       		codigos.put(new Integer(contador), formulario.getEjerciente().trim());
	       		sqlSubSelect += " AND (" + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_SITUACIONEJERCICIO + " = :" + contador + ") ";
	       	}
	       
	       	String fDesdeInc = formulario.getFechaIncorporacionDesde(); 
	       	String fHastaInc = formulario.getFechaIncorporacionHasta();
			if ((fDesdeInc != null && !fDesdeInc.trim().equals("")) || (fHastaInc != null && !fHastaInc.trim().equals(""))) {			
				if (!fDesdeInc.equals(""))
					fDesdeInc = GstDate.getApplicationFormatDate("", fDesdeInc); 
				
				if (!fHastaInc.equals(""))
					fHastaInc = GstDate.getApplicationFormatDate("", fHastaInc);
				
				Vector vCondicion=GstDate.dateBetweenDesdeAndHastaBind(CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_FECHAINCORPORACION, fDesdeInc, fHastaInc, contador, codigos);			
				contador = new Integer(vCondicion.get(0).toString()).intValue();
				sqlSubSelect +=" AND " + vCondicion.get(1);
			}
	       
			// Comunitario       
			if (formulario.getComunitario()!=null && !formulario.getComunitario().trim().equals("")) {
				contador++;
				codigos.put(new Integer(contador), formulario.getComunitario().trim());
				sqlSubSelect += " AND (" + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_COMUNITARIO + " = :" + contador + ") ";
			}  
	      
			// Residencia. Tiene que estar en �ltimo lugar dentro de la subselect de colegiado
			if (formulario.getResidente()!=null && !formulario.getResidente().equals("")){
				if (formulario.getResidente().trim().equals("S")) {	       		
		    	   sqlSubSelect += " HAVING SUM(DECODE(" + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_SITUACIONRESIDENTE + ", '1', 1, 'S', 1, 0)) = 0 " +
	    			   " GROUP BY IdPersona ";
	
		       	} else {
		       		if (formulario.getResidente().trim().equals("M")) {	       		
		       			sqlSubSelect += " HAVING SUM(DECODE(" + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_SITUACIONRESIDENTE + ", '1', 1, 'S', 1, 0)) > 1 " +
		       				" GROUP BY IdPersona ";
		       		} else {
		       			contador++;
		       			codigos.put(new Integer(contador), formulario.getResidente().trim());
		       			sqlSubSelect += " AND (" + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_SITUACIONRESIDENTE + " = :" + contador + ") ";
		       		}
		       	}
			}
			
			// Esta sentencia tiene que estar aqui, porque sino se pierden los contadores de sqlSubSelect
			if (!sqlSubSelect.substring(sqlSubSelect.length()-6, sqlSubSelect.length()).equals("1 = 1 "))
	    	   sqlClientes += " AND " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + " IN (" + sqlSubSelect + ") ";
	
			// Tipo Colegiacion
	       String sqlTipoCol = "";
	       if (formulario.getTipoColegiacion()!=null && !formulario.getTipoColegiacion().equals("")){
	
	    	   sqlTipoCol += "SELECT IDPERSONA " +
		 			" FROM CEN_DATOSCOLEGIALESESTADO A " +
		 			" WHERE FECHAESTADO  = (SELECT MAX(FECHAESTADO) " +      
			 			" FROM CEN_DATOSCOLEGIALESESTADO B " + 
			 			" WHERE B.IDINSTITUCION = A.IDINSTITUCION " +
			 				" AND B.IDPERSONA = A.IDPERSONA " +	
			 				" AND B.FECHAESTADO <= sysdate) ";
			 	
			 	if (formulario.getTipoColegiacion().equals(String.valueOf(ClsConstants.ESTADO_COLEGIAL_ALTA))){
			 		sqlTipoCol += " AND " + CenDatosColegialesEstadoBean.C_IDESTADO + " IN (" + ClsConstants.ESTADO_COLEGIAL_EJERCIENTE + ", " + ClsConstants.ESTADO_COLEGIAL_SINEJERCER + ") ";
			 		
			 	} else {
			 		contador++;
				    codigos.put(new Integer(contador), formulario.getTipoColegiacion());
				    sqlTipoCol += " AND "+CenDatosColegialesEstadoBean.C_IDESTADO + " IN (:" + contador + ") ";
			 	}
			 	
			 	sqlClientes += " AND " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + " IN (" + sqlTipoCol + ") ";
	       }
	       
	       //aalg: fin
	       sqlClientes +=" GROUP BY " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NIFCIF + ", " +
			  CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE + ", " +
			  CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 + ", " +
	          CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 + ", " + 
			  CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_FECHANACIMIENTO + ", " +
	          CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + ", "+
	          CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDINSTITUCION + ", " +
	          CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_NOAPARECERREDABOGACIA + ", " +
	          CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_DOMICILIO + ", " +
	          CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_CODIGOPOSTAL + ", "+ 
	          CenPoblacionesBean.T_NOMBRETABLA + "." + CenPoblacionesBean.C_NOMBRE + ", " +
	          CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_POBLACIONEXTRANJERA;	                 
	       
	       //aalg: se a�ade la ordenacion en espa�ol
	       sqlClientes += " ORDER BY NLSSORT(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 + ", 'NLS_SORT=SPANISH'), " +
			   " NLSSORT(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 + ", 'NLS_SORT=SPANISH'), " +
			   " NLSSORT(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE + ", 'NLS_SORT=SPANISH')";
	       
	       	/*No utilizamos la clase Paginador para la busqueda de letrados porque al filtrar por residencia la consulta no devolvia bien los datos que eran de tipo varchar (devolv�a n veces el mismo resultado), 
	       	 * utilizamos el paginador PaginadorCaseSensitive y hacemos a parte el tratamiento de mayusculas y signos de acentuaci�n
	       	 */
	       	PaginadorCaseSensitiveBind paginador = new PaginadorCaseSensitiveBind(sqlClientes,codigos);
	        totalRegistros = paginador.getNumeroTotalRegistros();
	 		
	 		if (totalRegistros==0){
	 			contador++;
	 			paginador =null;
	 		}
			
			return paginador;
			
		} catch (Exception e) { 	
			throw new ClsExceptions(e,"Error obteniendo clientes colegiados"); 
		}
	}

	/**
	 * Actualiza los datos generales y rellena la tabla de historicos (CEN_HISTORICO)
	 * @param BeanCliente datos generales cliente
	 * @param BeanHis con el motivo y el tipo, para almacenar en el Historico
	 */
	public boolean updateConHistorico (CenClienteBean beanCliente, CenHistoricoBean beanHis, String idioma) throws ClsExceptions, SIGAException 
	{
		try {
			if (update(beanToHashTable(beanCliente), beanCliente.getOriginalHash())) {
				CenHistoricoAdm admHis = new CenHistoricoAdm (this.usrbean);
				if (admHis.insertCompleto(beanHis, beanCliente, CenHistoricoAdm.ACCION_UPDATE, idioma)) {
					return true;
				}
			}
			return false;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al insertar datos en B.D.");
		}
	}

	/** 
	 * Recoge la informacion del registro seleccionado a partir de sus claves para <br/>
	 * transacciones (for update)
	 * @param  idPersona - identificador de persona 
	 * @param  idInstitucion - identificador de la institucion
	 * @return  hashtable - Entrada seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Hashtable getEntradaCliente (String idPersona, String idInstitucion) throws ClsExceptions, SIGAException {
	   Hashtable datos=new Hashtable();
       try {
            RowsContainer rc = new RowsContainer(); 
            Hashtable codigos = new Hashtable();
		    codigos.put(new Integer(1),idPersona);
    		codigos.put(new Integer(2),idInstitucion);
		    String sql ="SELECT " +
						CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDPERSONA + "," + CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_NOAPARECERREDABOGACIA+"  " +" ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_NOENVIARREVISTA+"  , " +
						CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDINSTITUCION + "," +
						CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_FECHAALTA + "," +
						CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_CARACTER + "," +
						CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_FOTOGRAFIA + "," +
						CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_SEXO + "," +
						CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_PUBLICIDAD + "," +
						CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_GUIAJUDICIAL + "," +
						CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_ABONOSBANCO + "," +
						CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_CARGOSBANCO + "," +							
						CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_ASIENTOCONTABLE + "," +
		    			CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_COMISIONES + "," +
		    			CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDTRATAMIENTO + "," +							
		    			CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_IDLENGUAJE + "," +
						CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_FECHAMODIFICACION + "," +
            			CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_USUMODIFICACION +
						" FROM " + CenClienteBean.T_NOMBRETABLA + ","+CenPersonaBean.T_NOMBRETABLA+
						" WHERE " +
						CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+"="+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDPERSONA+
						" AND "+CenClienteBean.T_NOMBRETABLA +"."+ CenClienteBean.C_IDPERSONA + "=:1" +
						" AND " +
						CenClienteBean.T_NOMBRETABLA +"."+ CenClienteBean.C_IDINSTITUCION + "=:2";
													
			// RGG cambio para visibilidad
			rc = this.findBind(sql,codigos); 
			if (rc!=null) {
               for (int i = 0; i < rc.size(); i++){
                  Row fila = (Row) rc.get(i);
                  datos=fila.getRow();
               }
            } 
       }
       catch (Exception e) {
       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla cliente.");
       }
       return datos;                        
    }	
	
	/**
	 * Obtiene los clientes (colegiados o no) para una instituci�n y
	 * un formulario de datos (BusquedaClientesModalForm)
	 * 
	 * @param idInstitucion 
	 * @param formulario Formulario de busqueda de clientes con los datos de busqueda 
	 * @return java.util.Vector Vector de tablas hash  
	 */


	
	
	private Vector getIdPersonaColegiadosBusqueda(BusquedaClientesForm formulario, boolean isBusquedaExactaSolicitante,
			Short idInstitucion)throws BusinessException{
		RowsContainer rc = new RowsContainer();
		Vector datos = null;
		Hashtable codigos = new Hashtable();
		String sql = getQueryColegiados(formulario, codigos, isBusquedaExactaSolicitante, idInstitucion);
		
		try {    	   	    	   			
			datos = this.selectGenericoBind(sql.toString(), codigos);
			
			
		} catch (Exception e) {
			log.error("Error al obtener getIdPersonaColegiadosBusqueda",e);
			throw new BusinessException("Error al obtener getIdPersonaColegiadosBusqueda");	
		} 
		return datos;
		
	}
	
	public String getQueryColegiados(BusquedaClientesForm formulario,Hashtable codigos, boolean isBusquedaExactaSolicitante,
			Short idInstitucion) {
		StringBuilder consulta = new StringBuilder();
		int contador =0;
//		si busqeuda exacta
//		SELECT PER.IDPERSONA FROM CEN_PERSONA PER
//		INNER JOIN CEN_CLIENTE CLI ON
//		PER.IDPERSONA = CLI.IDPERSONA
//		WHERE IDINSTITUCION = 2014
//		/*--sI BUSQEUDA EXACTA
//		AND UPPER(PER.NOMBRE) = UPPER('BECKY')
//		 --si mete solo apellido 1
//		AND UPPER(PER.APELLIDOS1) = UPPER('ABBY')
//		--si mete apellido 1 y 2
//		AND ( (UPPER(PER.APELLIDOS1) = UPPER('ABBY') AND UPPER(PER.APELLIDOS2) = UPPER('ANGEL'))
//		OR UPPER(PER.APELLIDOS1) = 'ABBY% %ANGEL' 
//		)*/
//		--SI BUSQUEDA NO EXACTA
//		AND translate(upper(UPPER(PER.NOMBRE)),'���������������','AEIOUAEIOUAEIOU') LIKE translate(upper('BECKY%'),'���������������','AEIOUAEIOUAEIOU')
//		--si mete solo apellido 1
//		--AND translate(upper(UPPER(PER.APELLIDOS1)),'���������������','AEIOUAEIOUAEIOU') LIKE translate(upper('ABBY%'),'���������������','AEIOUAEIOUAEIOU') 
//		--si mete apellido 1 y 2
//		AND ((translate(upper(UPPER(PER.APELLIDOS1)),'���������������','AEIOUAEIOUAEIOU') LIKE translate(upper('ABBY%'),'���������������','AEIOUAEIOUAEIOU') 
//			AND translate(upper(UPPER(PER.APELLIDOS2)),'���������������','AEIOUAEIOUAEIOU') LIKE translate(upper('ANGEL%'),'���������������','AEIOUAEIOUAEIOU')
//			)
//		OR translate(upper(UPPER(PER.APELLIDOS1)),'���������������','AEIOUAEIOUAEIOU') LIKE translate(upper('%ABBY% %ANGEL%'),'���������������','AEIOUAEIOUAEIOU')
//		)

		
		
		/*if (
				(formulario.getNumeroColegiado()!=null && !formulario.getNumeroColegiado().trim().equals(""))
				&&( (formulario.getNombrePersona()==null || formulario.getNombrePersona().trim().equals(""))
						 && (formulario.getApellido1()==null  || formulario.getApellido1().trim().equals(""))
						 && (formulario.getApellido2()==null  || formulario.getApellido2().trim().equals(""))
						 && (formulario.getNif()==null  || formulario.getNif().trim().equals(""))
						)) {
				consulta.append("SELECT COL.IDPERSONA FROM CEN_COLEGIADO COL ");
				consulta.append("WHERE COL.IDINSTITUCION = ");
				consulta.append(idInstitucion);
				contador++;
				codigos.put(new Integer(contador),formulario.getNumeroColegiado().trim());
				consulta.append(" AND LTRIM(DECODE(COL.COMUNITARIO,'1',COL.NCOMUNITARIO, COL.NCOLEGIADO),'0') = LTRIM(:");
				consulta.append(contador);
				consulta.append(",'0') ");
				
				
		} else {
		*/
			
		consulta.append("SELECT PER.IDPERSONA FROM CEN_PERSONA PER ");
		consulta.append("INNER JOIN CEN_CLIENTE CLI ON ");
		consulta.append("PER.IDPERSONA = CLI.IDPERSONA ");
		consulta.append("INNER JOIN CEN_COLEGIADO COL ON ");
		consulta.append("COL.IDPERSONA = CLI.IDPERSONA ");
		consulta.append("AND COL.IDINSTITUCION = CLI.IDINSTITUCION ");
		consulta.append("WHERE CLI.IDINSTITUCION = ");
		consulta.append(idInstitucion);
		if ((formulario.getNumeroColegiado()!=null && !formulario.getNumeroColegiado().trim().equals(""))) {
			
				
				contador++;
				codigos.put(new Integer(contador),formulario.getNumeroColegiado().trim());
				consulta.append(" AND LTRIM(DECODE(COL.COMUNITARIO,'1',COL.NCOMUNITARIO, COL.NCOLEGIADO),'0') = LTRIM(:");
				consulta.append(contador);
				consulta.append(",'0') ");
				
				
				
			}
			/* else{
				consulta.append("SELECT PER.IDPERSONA FROM CEN_PERSONA PER ");
				consulta.append("INNER JOIN CEN_CLIENTE CLI ON ");
				consulta.append("PER.IDPERSONA = CLI.IDPERSONA ");
				consulta.append("WHERE CLI.IDINSTITUCION = ");
				consulta.append(idInstitucion);
				
				
			}*/
			if (isBusquedaExactaSolicitante) {
				if (formulario.getNif()!=null && !formulario.getNif().trim().equals("")) {
					contador++;
					codigos.put(new Integer(contador), formulario.getNif().trim());
					consulta.append(" AND UPPER(PER.NIFCIF ) = UPPER(:");
					consulta.append(contador);
					consulta.append(")");
				}
				
				if (formulario.getNombrePersona()!=null && !formulario.getNombrePersona().trim().equals("")) {
					contador++;
					codigos.put(new Integer(contador), formulario.getNombrePersona().trim());
					consulta.append(" AND UPPER(PER.NOMBRE) = UPPER(:");
					consulta.append(contador);
					consulta.append(")");
				}
//				 si mete solo apellido 1
				if (formulario.getApellido1()!=null && !formulario.getApellido1().trim().equals("") &&
						(formulario.getApellido2()==null || formulario.getApellido2().trim().equals(""))
					){
					contador++;
					codigos.put(new Integer(contador), formulario.getApellido1().trim());
					consulta.append(" AND UPPER(PER.APELLIDOS1) = UPPER(:");
					consulta.append(contador);
					consulta.append(")");
//					si mete apellido 1 y apellido 2
				}else if (formulario.getApellido1()!=null && !formulario.getApellido1().trim().equals("") &&
						formulario.getApellido2()!=null && !formulario.getApellido2().trim().equals("")
					){
					contador++;
					codigos.put(new Integer(contador), formulario.getApellido1().trim());
					consulta.append(" AND (( UPPER(PER.APELLIDOS1) = UPPER(:");
					consulta.append(contador);
					consulta.append(")");
					contador++;
					codigos.put(new Integer(contador), formulario.getApellido2().trim());
					consulta.append(" AND UPPER(PER.APELLIDOS2) = UPPER(:");
					consulta.append(contador);
					consulta.append("))	OR UPPER(PER.APELLIDOS1) LIKE :");
					contador++;
					StringBuilder apellidos = new StringBuilder();
					apellidos.append(formulario.getApellido1().trim());
					apellidos.append("% %");
					apellidos.append(formulario.getApellido2().trim());
					codigos.put(new Integer(contador), apellidos.toString().toUpperCase());
					consulta.append(contador);
					
					
					consulta.append(")");
					
//					si no mete apellido 1 
				}else {
					//si mete apellido 2
					if (formulario.getApellido2()!=null && !formulario.getApellido2().trim().equals("")) {
						contador++;
						codigos.put(new Integer(contador),formulario.getApellido2().trim());
						consulta.append(" AND UPPER(PER.APELLIDOS2) = UPPER(:");
						consulta.append(contador);
						consulta.append(")");
					}	
				} 
			}else {
				if (formulario.getNif()!=null && !formulario.getNif().trim().equals("")) {
					
					if (ComodinBusquedas.hasComodin(formulario.getNif())){
						contador++;
						consulta.append(" AND ");
						consulta.append(ComodinBusquedas.prepararSentenciaCompletaTranslateUpperBind(formulario.getNif().trim(), "UPPER(PER.NIFCIF)", contador, codigos));
			       	}else{
			       		
			       		contador++;
						consulta.append(" AND ");
						consulta.append(ComodinBusquedas.prepararSentenciaNIFBind(formulario.getNif().trim(), "UPPER(PER.NIFCIF)", contador, codigos));
			       	}
				}
				
				if (formulario.getNombrePersona()!=null && !formulario.getNombrePersona().trim().equals("")) {
					contador++;
					consulta.append(" AND ");
					consulta.append(ComodinBusquedas.prepararSentenciaCompletaTranslateUpperBind(formulario.getNombrePersona().trim(), "UPPER(PER.NOMBRE)", contador, codigos));
				}
//				 si mete solo apellido 1
				if (formulario.getApellido1()!=null && !formulario.getApellido1().trim().equals("") &&
						(formulario.getApellido2()==null || formulario.getApellido2().trim().equals(""))
					){
					contador++;
					consulta.append(" AND ");
					consulta.append(ComodinBusquedas.prepararSentenciaCompletaTranslateUpperBind(
							formulario.getApellido1().trim(), "UPPER(PER.APELLIDOS1)", contador, codigos));
//					si mete apellido 1 y apellido 2
				}else if (formulario.getApellido1()!=null && !formulario.getApellido1().trim().equals("") &&
						formulario.getApellido2()!=null && !formulario.getApellido2().trim().equals("")
					){
					contador++;
					consulta.append(" AND ( (");
					consulta.append(ComodinBusquedas.prepararSentenciaCompletaTranslateUpperBind(
							formulario.getApellido1().trim(), "UPPER(PER.APELLIDOS1)", contador, codigos));
					
					contador++;
					consulta.append(" AND ");
					consulta.append(ComodinBusquedas.prepararSentenciaCompletaTranslateUpperBind(
							formulario.getApellido2().trim(), "UPPER(PER.APELLIDOS2)", contador, codigos));
					consulta.append(")	OR ");
					StringBuilder apellidos = new StringBuilder();
					apellidos.append(formulario.getApellido1().trim());
					apellidos.append("% %");
					apellidos.append(formulario.getApellido2().trim());
					contador++;
					consulta.append(ComodinBusquedas.prepararSentenciaCompletaTranslateUpperBind(
							apellidos.toString().toUpperCase(), "UPPER(PER.APELLIDOS1)", contador, codigos));
					consulta.append(")");
					
//					si no mete apellido 1 
				}else {
					//si mete apellido 2
					if (formulario.getApellido2()!=null && !formulario.getApellido2().trim().equals("")) {
						contador++;
						consulta.append(" AND ");
						consulta.append(ComodinBusquedas.prepararSentenciaCompletaTranslateUpperBind(
								formulario.getApellido2().trim(), "UPPER(PER.APELLIDOS2)", contador, codigos));
					}	
					
				} 
				
				
			}
			
		//}
				
		return consulta.toString();
	}
	
	
	/**
	 * Obtiene los clientes (colegiados o no) para una instituci�n y
	 * un formulario de datos (BusquedaClientesModalForm)
	 * 
	 * @param idInstitucion 
	 * @param formulario Formulario de busqueda de clientes con los datos de busqueda 
	 * @return java.util.Vector Vector de tablas hash  
	 */
	public PaginadorBind getColegiados(String idInstitucion, BusquedaClientesForm formulario, String idioma) throws ClsExceptions, SIGAException {

		int contador = 0;             

        Hashtable codigos = new Hashtable();
	  	// Acceso a BBDD
		try { 
			// consulta de todos los colegiados de una institucion y si estamos en CGAE o en Consejos tambien nos devuelven los que son letrados.
			StringBuilder sqlColegiados = new StringBuilder();
			sqlColegiados.append(" ");
			sqlColegiados.append(" SELECT			CLI.IDINSTITUCION,PER.IDPERSONA, ");
			sqlColegiados.append(" PER.NIFCIF, PER.NOMBRE,	PER.APELLIDOS1,	PER.APELLIDOS2, ");
			sqlColegiados.append(" (CASE COL.COMUNITARIO   WHEN '1' THEN COL.NCOMUNITARIO  ");
			sqlColegiados.append(" ELSE  COL.NCOLEGIADO  END) AS NCOLEGIADO,");
			sqlColegiados.append(" (SELECT CEN_ESTADOCOLEGIAL.DESCRIPCION FROM CEN_DATOSCOLEGIALESESTADO, CEN_ESTADOCOLEGIAL WHERE CEN_DATOSCOLEGIALESESTADO.IDESTADO = CEN_ESTADOCOLEGIAL.IDESTADO AND CEN_DATOSCOLEGIALESESTADO.IDPERSONA = CLI.IDPERSONA AND CEN_DATOSCOLEGIALESESTADO.IDINSTITUCION = CLI.IDINSTITUCION AND CEN_DATOSCOLEGIALESESTADO.FECHAESTADO = (SELECT MAX(CEN_DATOSCOLEGIALESESTADO.FECHAESTADO) FROM CEN_DATOSCOLEGIALESESTADO WHERE CEN_DATOSCOLEGIALESESTADO.IDPERSONA = CLI.IDPERSONA AND CEN_DATOSCOLEGIALESESTADO.IDINSTITUCION = CLI.IDINSTITUCION AND CEN_DATOSCOLEGIALESESTADO.FECHAESTADO <= SYSDATE)) AS ESTADOCOLEGIAL, ");
			sqlColegiados.append(" CLI.NOAPARECERREDABOGACIA ");
			sqlColegiados.append(" FROM CEN_PERSONA PER ");
			sqlColegiados.append(" INNER JOIN CEN_CLIENTE CLI ON ");
			sqlColegiados.append(" PER.IDPERSONA = CLI.IDPERSONA ");
			sqlColegiados.append(" INNER JOIN CEN_COLEGIADO COL ON ");
			sqlColegiados.append(" COL.IDPERSONA = CLI.IDPERSONA ");
			sqlColegiados.append(" AND COL.IDINSTITUCION = CLI.IDINSTITUCION ");
			sqlColegiados.append(" WHERE 1= 1 ");

				
			boolean esComision = usrbean.isComision();
			boolean esComisionMultiple = usrbean.getInstitucionesComision()!=null &&usrbean.getInstitucionesComision().length>1;
		
			if(esComision && esComisionMultiple){
				sqlColegiados.append( " AND CLI.IDINSTITUCION IN ( ");
				for (int i = 0; i < usrbean.getInstitucionesComision().length; i++) {
					sqlColegiados.append( usrbean.getInstitucionesComision()[i]);
					if(i!=usrbean.getInstitucionesComision().length-1)
						sqlColegiados.append(", ");
					
				}
				sqlColegiados.append( " ) ");
				
			}else{
				sqlColegiados.append( " AND CLI.IDINSTITUCION =  ");
				sqlColegiados.append(idInstitucion);
				
			}
			boolean isBusquedaExactaSolicitante  = UtilidadesString.stringToBoolean(formulario.getChkBusqueda());
			
			//Filtamos solamente por los colegiados que con esso filtros 
			boolean existeFiltroColegiado = false;
			if (
					(formulario.getNumeroColegiado()!=null && !formulario.getNumeroColegiado().trim().equals(""))
					||(formulario.getNombrePersona()!=null && !formulario.getNombrePersona().trim().equals(""))
					||(formulario.getApellido1()!=null && !formulario.getApellido1().trim().equals(""))
					||(formulario.getApellido2()!=null && !formulario.getApellido2().trim().equals(""))
					||(formulario.getNif()!=null && !formulario.getNif().trim().equals(""))
					) {
				existeFiltroColegiado = true;
			}
			if(existeFiltroColegiado) {
				
				Vector idPersonasVector = getIdPersonaColegiadosBusqueda(formulario, isBusquedaExactaSolicitante, Short.parseShort(idInstitucion));
				
				if(idPersonasVector!=null && idPersonasVector.size()>0 ) {
					
//					String filtroJusticiable = getQueryColegiados(formulario, codigos, isBusquedaExactaSolicitante, Short.parseShort(idInstitucion));
					sqlColegiados.append(" AND PER.IDPERSONA IN (");
					for (int i = 0; i < idPersonasVector.size(); i++) {
						Hashtable persona = (Hashtable) idPersonasVector.get(i);
						sqlColegiados.append(persona.get("IDPERSONA"));						
						sqlColegiados.append(",");
					}
					sqlColegiados.delete(sqlColegiados.length()-1,sqlColegiados.length());
					
					sqlColegiados.append(")");
					
				}else {
					//como no hay datos que devolver no seguimos
					return null;
					
				}
			}
			sqlColegiados.append(" ORDER BY "+CenPersonaBean.C_APELLIDOS1+", "+CenPersonaBean.C_APELLIDOS2+", "+CenPersonaBean.C_NOMBRE);
	       PaginadorBind paginador = new PaginadorBind(sqlColegiados.toString(),codigos);				
			int totalRegistros = paginador.getNumeroTotalRegistros();
			
			if (totalRegistros==0){					
				paginador =null;
			}
		
			
			return paginador;
		} 
		catch (Exception e) {
			log.error("Error en getColegiados",e);
			throw new ClsExceptions(e,"Error obteniendo clientes "); 
		}
	}
	/**
	 * Obtiene los clientes (colegiados o no) para una instituci�n y
	 * un formulario de datos (BusquedaClientesModalForm)
	 * 
	 * @param idInstitucion 
	 * @param formulario Formulario de busqueda de clientes con los datos de busqueda 
	 * @return java.util.Vector Vector de tablas hash  
	 */
	public PaginadorBind getClientesInstitucion(String idInstitucion,
			BusquedaClientesForm formulario, String idioma)
			throws ClsExceptions, SIGAException {

		String sqlClientes = "";

		int contador = 0;

		Hashtable codigos = new Hashtable();

		// Acceso a BBDD

		try {

			// consulta de insituciones

			sqlClientes = "SELECT "
					+ CenPersonaBean.T_NOMBRETABLA
					+ "."
					+ CenPersonaBean.C_NIFCIF
					+ ", "
					+

					" f_siga_calculoncolegiado(cEN_CLIENTE.IDINSTITUCION,CEN_CLIENTE.IDPERSONA) AS "
					+ CenColegiadoBean.C_NCOLEGIADO
					+ ","
					+

					" "
					+ CenPersonaBean.T_NOMBRETABLA
					+ "."
					+ CenPersonaBean.C_NOMBRE
					+ ", "
					+ CenPersonaBean.T_NOMBRETABLA
					+ "."
					+ CenPersonaBean.C_APELLIDOS1
					+ ", "
					+ CenPersonaBean.T_NOMBRETABLA
					+ "."
					+ CenPersonaBean.C_APELLIDOS2
					+ ",  "
					+ CenClienteBean.T_NOMBRETABLA
					+ "."
					+ CenClienteBean.C_IDPERSONA
					+ " ,  "
					+

					" DECODE("
					+ CenClienteBean.T_NOMBRETABLA
					+ "."
					+ CenClienteBean.C_LETRADO
					+ ",'1','Letrado',NVL((SELECT "
					+ CenEstadoColegialBean.T_NOMBRETABLA
					+ "."
					+ CenEstadoColegialBean.C_DESCRIPCION
					+ " "
					+

					" FROM "
					+ CenDatosColegialesEstadoBean.T_NOMBRETABLA
					+ ", "
					+ CenEstadoColegialBean.T_NOMBRETABLA
					+ " "
					+

					" WHERE "
					+ CenDatosColegialesEstadoBean.T_NOMBRETABLA
					+ "."
					+ CenDatosColegialesEstadoBean.C_IDESTADO
					+ " = "
					+ CenEstadoColegialBean.T_NOMBRETABLA
					+ "."
					+ CenEstadoColegialBean.C_IDESTADO
					+ "  "
					+

					" AND "
					+ CenDatosColegialesEstadoBean.T_NOMBRETABLA
					+ "."
					+ CenDatosColegialesEstadoBean.C_IDPERSONA
					+ " = "
					+ CenClienteBean.T_NOMBRETABLA
					+ "."
					+ CenClienteBean.C_IDPERSONA
					+ "  "
					+

					" AND "
					+ CenDatosColegialesEstadoBean.T_NOMBRETABLA
					+ "."
					+ CenDatosColegialesEstadoBean.C_IDINSTITUCION
					+ " = "
					+ CenClienteBean.T_NOMBRETABLA
					+ "."
					+ CenClienteBean.C_IDINSTITUCION
					+ " "
					+

					" AND "
					+ CenDatosColegialesEstadoBean.T_NOMBRETABLA
					+ "."
					+ CenDatosColegialesEstadoBean.C_FECHAESTADO
					+ " = (SELECT MAX("
					+ CenDatosColegialesEstadoBean.T_NOMBRETABLA
					+ "."
					+ CenDatosColegialesEstadoBean.C_FECHAESTADO
					+ ") "
					+

					" FROM "
					+ CenDatosColegialesEstadoBean.T_NOMBRETABLA
					+ "  "
					+

					" WHERE "
					+ CenDatosColegialesEstadoBean.T_NOMBRETABLA
					+ "."
					+ CenDatosColegialesEstadoBean.C_IDPERSONA
					+ "= "
					+ CenClienteBean.T_NOMBRETABLA
					+ "."
					+ CenClienteBean.C_IDPERSONA
					+ " "
					+

					" AND "
					+ CenDatosColegialesEstadoBean.T_NOMBRETABLA
					+ "."
					+ CenDatosColegialesEstadoBean.C_IDINSTITUCION
					+ " = "
					+ CenClienteBean.T_NOMBRETABLA
					+ "."
					+ CenClienteBean.C_IDINSTITUCION
					+ " AND CEN_DATOSCOLEGIALESESTADO.FECHAESTADO <= SYSDATE)), 'No Colegiado')) AS ESTADOCOLEGIAL, "
					+

					// Se a�ade un nuevo campo de salida para que en la
					// solicitud de compra aparezcan ordenados por letrados,
					// colegiados, no colegiados

					" DECODE(" + CenClienteBean.T_NOMBRETABLA + "."
					+ CenClienteBean.C_LETRADO + ",'1','1', NVL(( SELECT '2' " +

					" FROM " + CenColegiadoBean.T_NOMBRETABLA +

					" WHERE " + CenColegiadoBean.T_NOMBRETABLA + "."
					+ CenColegiadoBean.C_IDPERSONA + " = "
					+ CenClienteBean.T_NOMBRETABLA + "."
					+ CenClienteBean.C_IDPERSONA +

					" AND " + CenColegiadoBean.T_NOMBRETABLA + "."
					+ CenColegiadoBean.C_IDINSTITUCION + " = "
					+ CenClienteBean.T_NOMBRETABLA + "."
					+ CenClienteBean.C_IDINSTITUCION + "), " +

					"                        decode((select '3' " +

					"                        from dual " +

					"                   where " + CenClienteBean.T_NOMBRETABLA
					+ "." + CenClienteBean.C_IDPERSONA
					+ ">1000),'3',3,4) )) AS COLEGIACION ";// Se quiere ordenar
															// primero por los
															// no colegiados,
															// sociedades y por
															// �ltimo las
															// instituciones

			sqlClientes += " ,  " + CenClienteBean.T_NOMBRETABLA + "."
					+ CenClienteBean.C_NOAPARECERREDABOGACIA + "  ";
			if (formulario.getNumeroColegiado() != null
					&& !formulario.getNumeroColegiado().trim().equals("")) {

				sqlClientes += " FROM " + CenPersonaBean.T_NOMBRETABLA + " , "
						+ CenClienteBean.T_NOMBRETABLA + " ,"
						+ CenColegiadoBean.T_NOMBRETABLA +

						" WHERE " + CenPersonaBean.T_NOMBRETABLA + "."
						+ CenPersonaBean.C_IDPERSONA + " = "
						+ CenClienteBean.T_NOMBRETABLA + "."
						+ CenClienteBean.C_IDPERSONA +

						"  and " + CenClienteBean.T_NOMBRETABLA + "."
						+ CenClienteBean.C_IDINSTITUCION + " = "
						+ idInstitucion +

						"  and " + CenColegiadoBean.T_NOMBRETABLA + "."
						+ CenColegiadoBean.C_IDPERSONA + " = "
						+ CenClienteBean.T_NOMBRETABLA + "."
						+ CenClienteBean.C_IDPERSONA +

						"  and " + CenColegiadoBean.T_NOMBRETABLA + "."
						+ CenColegiadoBean.C_IDINSTITUCION + " = "
						+ CenClienteBean.T_NOMBRETABLA + "."
						+ CenClienteBean.C_IDINSTITUCION;

			} else {

				sqlClientes += " FROM " + CenPersonaBean.T_NOMBRETABLA + " , "
						+ CenClienteBean.T_NOMBRETABLA +
						" WHERE " + CenPersonaBean.T_NOMBRETABLA + "."
						+ CenPersonaBean.C_IDPERSONA + " = "
						+ CenClienteBean.T_NOMBRETABLA + "."
						+ CenClienteBean.C_IDPERSONA +
						"  and " + CenClienteBean.T_NOMBRETABLA + "."
						+ CenClienteBean.C_IDINSTITUCION + " = "
						+ idInstitucion;

			}

			//se saca al propio colegio tambien
			//por ejemplo, es necesario en remitentes
			//Hecho el 17/06/2010. Borrar estas lineas si ya se ha comprado
			//que las busquedas estan bien
			/*
			sqlClientes += " and " + CenClienteBean.T_NOMBRETABLA + "."
					+ CenClienteBean.C_IDPERSONA
					+ " not in (select t.idpersona " +
					" from " + CenInstitucionBean.T_NOMBRETABLA + " t" +
					" where t.idinstitucion=" + idInstitucion + ")";
			*/

			boolean bBusqueda = UtilidadesString.stringToBoolean(formulario
					.getChkBusqueda());

			// 2

			if (formulario.getNumeroColegiado() != null
					&& !formulario.getNumeroColegiado().trim().equals("")) {

				contador++;
				codigos.put(new Integer(contador), formulario
						.getNumeroColegiado().trim());
				sqlClientes += " AND LTRIM(DECODE(CEN_COLEGIADO.COMUNITARIO,'1',CEN_COLEGIADO.NCOMUNITARIO, CEN_COLEGIADO.NCOLEGIADO),'0') = LTRIM(:"
						+ contador + ",'0') ";

			}

			// 3

			if (!formulario.getNombrePersona().trim().equals("")) {

				if (bBusqueda) {

					contador++;
					sqlClientes += " AND (UPPER("
							+ CenPersonaBean.T_NOMBRETABLA
							+ "."
							+ CenPersonaBean.C_NOMBRE
							+ ") "
							+ ComodinBusquedas.prepararSentenciaExactaBind(
									formulario.getNombrePersona().trim()
											.toUpperCase(), contador, codigos)
							+ ")";

				} else {

					contador++;
					sqlClientes += " AND ("
							+ ComodinBusquedas.prepararSentenciaCompletaBind(
									formulario.getNombrePersona().trim()
											.toUpperCase(), "upper("
											+ CenPersonaBean.T_NOMBRETABLA
											+ "." + CenPersonaBean.C_NOMBRE
											+ ")", contador, codigos) + ") ";

				}

			}

			// 4 y 5 Criterio de busqueda especial para los campos Apellido1 y
			// Apellido2

			if (!formulario.getApellido1().trim().equals("")
					&& (!formulario.getApellido2().trim().equals(""))) {// Los
																		// dos
																		// campos
																		// rellenos

				if (bBusqueda) {

					contador++;
					sqlClientes += " AND ((("
							+ CenPersonaBean.T_NOMBRETABLA
							+ "."
							+ CenPersonaBean.C_APELLIDOS1
							+ " "
							+ ComodinBusquedas.prepararSentenciaExactaBind(
									formulario.getApellido1().trim(), contador,
									codigos) + ")";

					contador++;
					sqlClientes += " AND ("
							+ CenPersonaBean.T_NOMBRETABLA
							+ "."
							+ CenPersonaBean.C_APELLIDOS2
							+ " "
							+ ComodinBusquedas.prepararSentenciaExactaBind(
									formulario.getApellido2().trim(), contador,
									codigos) + "))";

					contador++;
					sqlClientes += " OR ("
							+ CenPersonaBean.T_NOMBRETABLA
							+ "."
							+ CenPersonaBean.C_APELLIDOS1
							+ " "
							+ ComodinBusquedas.prepararSentenciaExactaBind(
									formulario.getApellido1().trim() + " "
											+ formulario.getApellido2().trim(),
									contador, codigos) + "))";

				} else {

					contador++;
					sqlClientes += " AND ((("
							+ ComodinBusquedas.prepararSentenciaCompletaBind(
									formulario.getApellido1().trim(),
									CenPersonaBean.T_NOMBRETABLA + "."
											+ CenPersonaBean.C_APELLIDOS1,
									contador, codigos) + ")";

					contador++;
					sqlClientes += " AND ("
							+ ComodinBusquedas.prepararSentenciaCompletaBind(
									formulario.getApellido2().trim(),
									CenPersonaBean.T_NOMBRETABLA + "."
											+ CenPersonaBean.C_APELLIDOS2,
									contador, codigos) + "))";

					contador++;
					sqlClientes += " OR ("
							+ ComodinBusquedas.prepararSentenciaCompletaBind(
									formulario.getApellido1().trim() + "* "
											+ formulario.getApellido2().trim()
											+ "*",
									CenPersonaBean.T_NOMBRETABLA + "."
											+ CenPersonaBean.C_APELLIDOS1,
									contador, codigos) + "))";

				}

			} else if (!formulario.getApellido1().trim().equals("")
					&& (formulario.getApellido2().trim().equals(""))) {// Apellido1 relleno

				if (bBusqueda) {

					contador++;
					sqlClientes += " AND (("
							+ CenPersonaBean.T_NOMBRETABLA
							+ "."
							+ CenPersonaBean.C_APELLIDOS1
							+ " "
							+ ComodinBusquedas.prepararSentenciaExactaBind(
									formulario.getApellido1(), contador,
									codigos);

					contador++;
					codigos.put(new Integer(contador), UtilidadesBDAdm
							.validateChars(formulario.getApellido1().trim())
							+ " %");
					sqlClientes += " OR (" + CenPersonaBean.T_NOMBRETABLA + "."
							+ CenPersonaBean.C_APELLIDOS1 + " LIKE :"
							+ contador + ")))";

				} else {

					contador++;
					sqlClientes += " AND ("
							+ ComodinBusquedas.prepararSentenciaCompletaBind(
									formulario.getApellido1().trim(),
									CenPersonaBean.T_NOMBRETABLA + "."
											+ CenPersonaBean.C_APELLIDOS1,
									contador, codigos);

					contador++;
					codigos.put(new Integer(contador), "%"
							+ UtilidadesBDAdm.validateChars(formulario
									.getApellido1().trim()) + "%");
					sqlClientes += " OR (" + CenPersonaBean.T_NOMBRETABLA + "."
							+ CenPersonaBean.C_APELLIDOS1 + "||' '||"
							+ CenPersonaBean.T_NOMBRETABLA + "."
							+ CenPersonaBean.C_APELLIDOS2 + " LIKE :"
							+ contador + ")) ";

				}

			} else if (formulario.getApellido1().trim().equals("")
					&& (!formulario.getApellido2().trim().equals(""))) {// Apellido2 relleno

				if (bBusqueda) {

					contador++;
					sqlClientes += " AND (("
							+ CenPersonaBean.T_NOMBRETABLA
							+ "."
							+ CenPersonaBean.C_APELLIDOS2
							+ " "
							+ ComodinBusquedas.prepararSentenciaExactaBind(
									formulario.getApellido2().trim(), contador,
									codigos) + ")";

					contador++;
					codigos.put(new Integer(contador), "% "
							+ UtilidadesBDAdm.validateChars(formulario
									.getApellido2().trim()));
					sqlClientes += " OR (" + CenPersonaBean.T_NOMBRETABLA + "."
							+ CenPersonaBean.C_APELLIDOS1 + " LIKE  :"
							+ contador + " )) ";

				} else {

					contador++;
					sqlClientes += " AND (("
							+ ComodinBusquedas.prepararSentenciaCompletaBind(
									formulario.getApellido2().trim(),
									CenPersonaBean.T_NOMBRETABLA + "."
											+ CenPersonaBean.C_APELLIDOS2,
									contador, codigos) + ") ";

					contador++;
					sqlClientes += " OR ("
							+ ComodinBusquedas.prepararSentenciaCompletaBind(
									" " + formulario.getApellido2().trim(),
									CenPersonaBean.T_NOMBRETABLA + "."
											+ CenPersonaBean.C_APELLIDOS1,
									contador, codigos) + ")  )";
				}
			}

			// 6 //Consulta sobre el campo NIF/CIF, si el usuario mete comodines
			// la b�squeda es como se hac�a siempre, en el caso

			// de no meter comodines se ha creado un nuevo metodo
			// ComodinBusquedas.preparaCadenaNIFSinComodin para que monte

			// la consulta adecuada.

			if (!formulario.getNif().trim().equals("")) {
				if (bBusqueda) {
					
					contador++;
					codigos.put(new Integer(contador), formulario.getNif()
							.trim().toUpperCase());
					sqlClientes += " AND UPPER(" + CenPersonaBean.T_NOMBRETABLA
							+ "." + CenPersonaBean.C_NIFCIF + ")= :" + contador;

				} else {
					if (ComodinBusquedas.hasComodin(formulario.getNif())) {
						
						contador++;
						sqlClientes += " AND ("
								+ ComodinBusquedas
										.prepararSentenciaCompletaBind(
												formulario.getNif().trim(),
												CenPersonaBean.T_NOMBRETABLA
														+ "."
														+ CenPersonaBean.C_NIFCIF,
												contador, codigos) + ")";

					} else {
						
						contador++;
						sqlClientes += " AND "
								+ ComodinBusquedas.prepararSentenciaNIFBind(
										formulario.getNif(),
										CenPersonaBean.T_NOMBRETABLA + "."
												+ CenPersonaBean.C_NIFCIF,
										contador, codigos);
					}
				}
			}

			sqlClientes += " ORDER BY COLEGIACION, "
					+ CenPersonaBean.T_NOMBRETABLA + "."
					+ CenPersonaBean.C_APELLIDOS1 + ", "
					+ CenPersonaBean.T_NOMBRETABLA + "."
					+ CenPersonaBean.C_APELLIDOS2 + ", "
					+ CenPersonaBean.T_NOMBRETABLA + "."
					+ CenPersonaBean.C_NOMBRE;

			PaginadorBind paginador = new PaginadorBind(sqlClientes, codigos);

			int totalRegistros = paginador.getNumeroTotalRegistros();
			if (totalRegistros == 0) {
				paginador = null;
			}

			return paginador;
		}
		catch (Exception e) {
			throw new ClsExceptions(e, "Error obteniendo clientes ");
		}

	} //getClientesInstitucion()
	
	/**
	 * Actualiza los datos generales en la tabla cliente y persona
	 * @param Hashtable datos generales
	 * @return CenClienteBean con el cliente insertado
	 */
	public CenClienteBean insertNoColegiado (Hashtable hashDatosGenerales, HttpServletRequest request) throws SIGAException 
	{
		try {
			// obtengo los beasn de cliente y persona del mismo hash
			
			CenClienteBean beanCli = (CenClienteBean) hashTableToBean(hashDatosGenerales);						
			
			String sociedadSJ = (String)request.getParameter("sociedadSJ");  
			String sociedadSP = (String)request.getParameter("sociedadSP");
			
			String tipoIdentificacionBloqueada = (String)request.getParameter("tipoIdentificacionBloqueada");  
			
			if 	((sociedadSJ!=null && sociedadSJ.equals("1"))||(sociedadSP!=null && sociedadSP.equals("1")))
				beanCli.setCaracter("S");
			else
				beanCli.setCaracter("P");
			
			CenPersonaAdm admPer = new CenPersonaAdm(this.usrbean);
			CenPersonaBean beanPer = (CenPersonaBean) admPer.hashTableToBean(hashDatosGenerales);
			if(beanPer.getIdTipoIdentificacion()==null && tipoIdentificacionBloqueada!= null )
			{
				beanPer.setIdTipoIdentificacion(new Integer(tipoIdentificacionBloqueada));
			}
			return insertNoColegiado(beanPer, beanCli, request, request.getParameter("continuarAprobacion"));
		}
		catch (SIGAException e) { 
			throw e; 	
		}
		catch (Exception e) {
			throw new SIGAException ("Error al insertar datos en B.D.", e);
		}
	}

	/**
	 * Actualiza los datos generales en la tabla cliente y persona
	 * @param beanCli CenClienteBean con los datos de cliente
	 * @param beanPer CenPersonaBean con los datos de persona
	 * @return cenClienteBean con el cliente insertado
	 */
	public CenClienteBean insertNoColegiado (CenPersonaBean beanPer, CenClienteBean beanCli, HttpServletRequest request, String continuar) throws SIGAException
	{
		
			CenClienteBean auxCli = null;			
			CenPersonaBean auxPer = null; 
			boolean existePersona = false; 
			boolean existeCliente = false; 						
		try	{
		
			CenPersonaAdm admPer = new CenPersonaAdm(this.usrbean);
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		try {
			//pongo en mayusculas el nifcif
			beanPer.setNIFCIF(beanPer.getNIFCIF().toUpperCase());
			
			//compruebo existencias
			
			auxPer = admPer.existePersona(beanPer.getNIFCIF(), beanPer.getNombre(), beanPer.getApellido1(), beanPer.getApellido2(),continuar);
		
			if (auxPer!=null) {				
				existePersona= true;
								 

				/**Busca si existe cliente en la institucion actual**/
				auxCli =  this.existeCliente(auxPer.getIdPersona(),new Integer(usr.getLocation()));
				if (auxCli!=null) {
					existeCliente= true;
				}else{
					
					/**Busca si no existe el cliente en la instituci�n actual, busca si existe en cualquier colegio y si existe 
					 * se realiza la pregunta si quiere que utilize los mismos datos ya existentes**/
					auxCli = this.existeClienteOtraInstitucion (auxPer.getIdPersona(),new Integer(usr.getLocation()));
					if (auxCli!=null){
						if (auxPer.isExisteDatos()){
							CenClienteBean beanCliente = new CenClienteBean ();
							beanCliente.setExisteDatos(true);
							//Se deberia de insertar un registro en cen_clientes para la nueva institucion
							beanCliente.setIdTratamiento (auxCli.getIdTratamiento());
							beanCliente.setIdInstitucion(new Integer(this.usrbean.getLocation()));
							beanCliente.setIdPersona (auxPer.getIdPersona());
							beanCliente.setFechaAlta ("SYSDATE");
							beanCliente.setIdLenguaje (ClsConstants.LENGUAJE_ESP);
							beanCliente.setAbonosBanco (ClsConstants.TIPO_CARGO_BANCO);
							beanCliente.setCargosBanco (ClsConstants.TIPO_CARGO_BANCO);
							beanCliente.setPublicidad (ClsConstants.DB_FALSE);
							beanCliente.setExportarFoto("0");
							beanCliente.setGuiaJudicial (ClsConstants.DB_FALSE);
							beanCliente.setComisiones (ClsConstants.DB_FALSE);					
							beanCliente.setCaracter (ClsConstants.TIPO_CARACTER_PUBLICO);
							beanCliente.setLetrado(ClsConstants.DB_FALSE);
							if (!this.insert(beanCliente)) {
								throw new SIGAException(this.getError());
							}
							return beanCliente;
						}else{
							existeCliente= false;
						}
					}
				}				
			}			
		
		} catch (SIGAException e) {
				//auxCli = null;
				//return auxCli;
				throw e;
		}	
			
			// proceso
			if (existePersona) {				
				this.setError("messages.fichaCliente.personaExiste");
				if (existeCliente) {
					throw new SIGAException("messages.fichaCliente.clienteExiste");
				} else {
					// no existe el cliente
					
					auxCli = beanCli;
					if (beanCli.getIdTratamiento()==null){
						auxCli.setIdTratamiento(new Integer("1"));
					}
					// le meto el idpersona del cliente que existe
					auxCli.setIdPersona(auxPer.getIdPersona());
					auxCli.setIdInstitucion(new Integer(usr.getLocation()));
					auxCli.setFechaAlta("SYSDATE");
					auxCli.setExportarFoto(ClsConstants.DB_FALSE);
					if (!this.insert(auxCli)) {
						throw new SIGAException(this.getError());
					}
					// vuelvo a coger ya el insertado
					auxCli = this.existeCliente(auxPer.getIdPersona(),new Integer(usr.getLocation()));
				}
			} else {
				// se da por hecho que NO EXISTE EL CLIENTE, puesto que no existe la persona
				auxPer = beanPer;
				
				// le meto un idpersona nuevo
				Long nuevoIdPersona = admPer.getIdPersona(new Integer(usr.getLocation())); 
				auxPer.setIdPersona(nuevoIdPersona);
				auxPer.setFallecido(ClsConstants.DB_FALSE);
				
				if (!admPer.insert(auxPer)) {
					throw new SIGAException(this.getError());
				}
				auxCli = beanCli;
				//Forzamos a que los cargos y abonos sean de tipo B y el tratamiento sea 1:
				auxCli.setAbonosBanco("B");
				auxCli.setCargosBanco("B");
				auxCli.setIdPersona(nuevoIdPersona);
				auxCli.setIdInstitucion(new Integer(usr.getLocation()));
				auxCli.setFechaAlta("SYSDATE");
				auxCli.setExportarFoto(ClsConstants.DB_FALSE);
				
				if (beanCli.getIdTratamiento()==null){
					// Como el campo tratamiento es obligatorio, metemos unos por defecto, nos da igual el elemento pq no se ver�
					try {
						CenTratamientoAdm tratamientoAdm = new CenTratamientoAdm(this.usrbean);
						Vector vT = tratamientoAdm.select();
						if (vT == null || vT.size() < 1) {
							throw new SIGAException("messages.fichaCliente.noExisteTratamiento");
						}
						else { 				
						    CenTratamientoBean b = (CenTratamientoBean)vT.get(0);
						    auxCli.setIdTratamiento(b.getIdTratamiento());
						}
					}
					catch (Exception e) {
						throw new SIGAException("messages.fichaCliente.noExisteTratamiento");
	                }
				}
                
				if (!this.insert(auxCli)) {
					throw new SIGAException(this.getError());
				}
				// vuelvo a coger ya el insertado
				auxCli = this.existeCliente(auxPer.getIdPersona(),new Integer(usr.getLocation()));
			}	
	
		} catch (SIGAException se) {
			throw se;
		}
		catch (Exception e) {
			auxCli=null;
			return auxCli;
		//	throw new SIGAException ("Error al insertar datos en B.D.",e);
		}
		return auxCli;
	}

	/**
	 * Comprueba si existe un cliente a traves de idpersona e idinstitucion
	 * @param idPersona Long con el id de persona
	 * @param idInstitucion Integer con el id de institucion
	 * @return CenClienteBean con el objeto encontrado o nulo si no existe
	 */
	public CenClienteBean existeCliente (Long idPersona, Integer idInstitucion) throws ClsExceptions, SIGAException 
	{
		try {
			CenClienteBean salida = null;
			// obtengo los beasn de cliente y persona del mismo hash
			Hashtable datos = new Hashtable();
			datos.put("IDPERSONA",idPersona.toString());
			datos.put("IDINSTITUCION",idInstitucion.toString());
			Vector v = this.select(datos);
			if (v!=null && v.size()>0) {
				salida = (CenClienteBean) v.get(0);
			}
			return salida;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al consultar datos en B.D.");
		}
	}	

	public String getLenguaje (String idInstitucion, String idPersona) throws ClsExceptions, SIGAException 
	{
		try {
			String lenguaje = ClsConstants.LENGUAJE_ESP;
			CenClienteBean salida = null;
			// obtengo los beasn de cliente y persona del mismo hash
			Hashtable datos = new Hashtable();
			datos.put("IDPERSONA",idPersona);
			datos.put("IDINSTITUCION",idInstitucion);
			Vector v = this.select(datos);
			if (v!=null && v.size()>0) {
				salida = (CenClienteBean) v.get(0);
				lenguaje = salida.getIdLenguaje();
			}
			return lenguaje;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al consultar datos en B.D.");
		}
	}
	/**
	 * Devuelve el campo idioma de cen_cleinte. Si no tiene devuelve null
	 * @param idInstitucion
	 * @param idPersona
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public String getIdiomaPersonaInforme (String idInstitucion, String idPersona) throws ClsExceptions, SIGAException 
	{
		try {
			CenClienteBean salida = null;
			// obtengo los beasn de cliente y persona del mismo hash
			String lenguaje = null;
			Hashtable datos = new Hashtable();
			datos.put("IDPERSONA",idPersona);
			datos.put("IDINSTITUCION",idInstitucion);
			Vector v = this.select(datos);
			if (v!=null && v.size()>0) {
				salida = (CenClienteBean) v.get(0);
				lenguaje = salida.getIdLenguaje();
			}
			return lenguaje;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al consultar datos en B.D.");
		}
	}
	
	

	/**
	 * Comprueba si existe un cliente a traves de idpersona para una
	 * institucion diferente a la pasada como parametro
	 * @param idPersona Long con el id de persona
	 * @param idInstitucion Integer con el id de institucion
	 * @return CenClienteBean con el objeto encontrado o nulo si no existe
	 */
	public CenClienteBean existeClienteOtraInstitucion (Long idPersona, Integer idInstitucion) throws ClsExceptions, SIGAException 
	{
		try {
			CenClienteBean salida = null;
			Hashtable codigos = new Hashtable();
		    codigos.put(new Integer(1),idPersona.toString());
    		codigos.put(new Integer(2),idInstitucion.toString());
		    Vector v = this.selectBind(" WHERE IDPERSONA=:1" + " AND IDINSTITUCION<>:2",codigos);
			if (v!=null && v.size()>0) {
				salida = (CenClienteBean) v.get(0);
			}
			return salida;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al consultar datos en B.D.");
		}
	}
	
	private int selectTipoClienteProduccion (Long idPersona, Integer idInstitucion, boolean igualdad) throws ClsExceptions, SIGAException 
	{
		int salida = TIPOCLIENTE_PROD_UNICO;
		try {
			Hashtable<Integer, String> codigos = new Hashtable();
			codigos.put(new Integer(1),idPersona.toString());
			codigos.put(new Integer(2),idInstitucion.toString());
			
			StringBuffer select = new StringBuffer();
			select.append(" ");
			select.append("Select Decode(Ins.Fechaenproduccion, ");
			select.append("              Null, ");
			select.append("              Decode(f_Siga_Gettipocliente(Col.Idpersona, ");
			select.append("                                           Col.Idinstitucion, ");
			select.append("                                           Sysdate), ");
			select.append("                     0, ");
			select.append("                     "+TIPOCLIENTE_PROD_NOC+", ");
			select.append("                     30, ");
			select.append("                     "+TIPOCLIENTE_PROD_NOC+", ");
			select.append("                     40, ");
			select.append("                     "+TIPOCLIENTE_PROD_NOC+", ");
			select.append("                     50, ");
			select.append("                     "+TIPOCLIENTE_PROD_NOC+", ");
			select.append("                     Decode(Col.Situacionresidente, 1, "+TIPOCLIENTE_PROD_RES+", "+TIPOCLIENTE_PROD_COL+")), ");
			select.append("              Decode(f_Siga_Gettipocliente(Col.Idpersona, ");
			select.append("                                           Col.Idinstitucion, ");
			select.append("                                           Sysdate), ");
			select.append("                     0, ");
			select.append("                     "+TIPOCLIENTE_PROD_NOCPRO+", ");
			select.append("                     30, ");
			select.append("                     "+TIPOCLIENTE_PROD_NOCPRO+", ");
			select.append("                     40, ");
			select.append("                     "+TIPOCLIENTE_PROD_NOCPRO+", ");
			select.append("                     50, ");
			select.append("                     "+TIPOCLIENTE_PROD_NOCPRO+", ");
			select.append("                     Decode(Col.Situacionresidente, 1, "+TIPOCLIENTE_PROD_RESPRO+", "+TIPOCLIENTE_PROD_COLPRO+"))) as TIPO ");

			select.append("  From Cen_Cliente Cli, Cen_Colegiado Col, Cen_Institucion Ins ");
			select.append(" Where Cli.Idinstitucion = Col.Idinstitucion(+) ");
			select.append("   And Cli.Idpersona = Col.Idpersona(+) ");
			select.append("   And Cli.Idinstitucion = Ins.Idinstitucion ");
			select.append("   And Cli.Idpersona = :1 ");
			select.append("   And Cli.Idinstitucion "+(igualdad ? "=" : "<>")+" :2 ");
			select.append("   Order By 1 desc ");
			
			Vector v = this.selectGenericoBind(select.toString(), codigos);
			if (v!=null && v.size()>0) {
				salida = Integer.parseInt((String) ((Hashtable) v.get(0)).get("TIPO"));
			}
			return salida;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al consultar datos en B.D.");
		}
	}
	
	/**
	 * Comprueba si existe un cliente a traves de idpersona para una
	 * institucion diferente a la pasada como parametro 
	 * devolviendo alguna constante tipo CLIENTE_
	 * @param idPersona Long con el id de persona
	 * @param idInstitucion Integer con el id de institucion
	 */
	public int getTipoClienteOtraInstitucionProduccion (Long idPersona, Integer idInstitucion) throws ClsExceptions, SIGAException 
	{
		return selectTipoClienteProduccion(idPersona, idInstitucion, false);
	}
	/**
	 * Comprueba el tipo de un cliente a traves de idpersona e idinstitucion
	 * devolviendo alguna constante tipo CLIENTE_
	 * @param idPersona Long con el id de persona
	 * @param idInstitucion Integer con el id de institucion
	 * @return CenClienteBean con el objeto encontrado o nulo si no existe
	 */
	public int getTipoClienteProduccion (Long idPersona, Integer idInstitucion) throws ClsExceptions, SIGAException 
	{
		return selectTipoClienteProduccion(idPersona, idInstitucion, true);
	}

	/**
	 * Realiza el alta de un colegiado desde una solicitud de incorporaci�n
	 * @param beanSolic CenSolicitudIncorporacionBean con los datos de la solicitud
	 * @return cenClienteBean con el cliente insertado
	 * @throws SIGAException con la excepcion de aplicaci�n
	 */
	public CenClienteBean altaColegial (CenSolicitudIncorporacionBean beanSolic, 
										String continuar)
			throws SIGAException
	{
		//Controles de adm utilizados
		CenPersonaAdm admPer;
		CenClienteAdm admCliente;
		CenColegiadoAdm admCol;
		CenNoColegiadoAdm admNoCol;
		
		//Beans utilizados
		CenPersonaBean beanPer = null;
		CenColegiadoBean beanCol = null;
		CenClienteBean beanCli = null;
		CenDireccionesBean beanDir = null;
		
		//Otras variables
		int tipoSolicitud;
		boolean esNuevo = false;
		
		try
		{
			//obteniendo los adms
			admPer = new CenPersonaAdm(this.usrbean);
			admCliente = new CenClienteAdm(this.usrbean);
			admCol = new CenColegiadoAdm(this.usrbean);
			admNoCol = new CenNoColegiadoAdm(this.usrbean);
			
			//comprobando la existencia de la persona y el cliente
			boolean existePersona = false;
			boolean existeCliente = false;
			try
			{
				beanPer = admPer.existePersonaAlta (beanSolic.getNumeroIdentificador (), 
						beanSolic.getNombre (), beanSolic.getApellido1 (), 
						beanSolic.getApellido2 (), continuar);
				
				if (beanPer != null) {
					existePersona = true;
					beanCli = this.existeCliente (beanPer.getIdPersona (), 
							beanSolic.getIdInstitucion ());
					
					if (beanCli != null) {
						existeCliente = true;
					}
				}
			}
			catch (SIGAException e) {
				beanCli = null;
				return beanCli;
			}	
			
			//obteniendo el tipo de solicitud de alta colegial
			tipoSolicitud = beanSolic.getIdTipoSolicitud ().intValue ();
			
			//Si es una reincorporacion
			if (tipoSolicitud == ClsConstants.TIPO_SOLICITUD_EJERCIENTE_REINCORPORACION ||
				tipoSolicitud == ClsConstants.TIPO_SOLICITUD_NOEJERCIENTE_REINCORPORACION)
			{
				if (! existePersona || ! existeCliente)
					//si no existe no se puede reincorporar
					throw new SIGAException ("messages.censo.altaColegial.errorNoExisteCliente");
				
				//obteniendo sus datos colegiales
				if (admCol.getDatosColegiales (beanPer.getIdPersona (), 
						beanSolic.getIdInstitucion ()) == null)
					//no tiene datos colegiales
					throw new SIGAException ("messages.censo.altaColegial.errorNoDatosColegiales");
				
				//actualizando datos de persona y cliente con los de la solicitud
				beanPer = admPer.updatePersona (beanPer, beanSolic);
				beanCli = admCliente.updateCliente (beanCli, beanSolic);
				
				//obteniendo nuevo estado colegial
				int idEstado;
				if (tipoSolicitud == ClsConstants.TIPO_SOLICITUD_EJERCIENTE_REINCORPORACION)
					idEstado = ClsConstants.ESTADO_COLEGIAL_EJERCIENTE;
				else
					idEstado = ClsConstants.ESTADO_COLEGIAL_SINEJERCER;
				Integer estado = new Integer (idEstado);
				
				//creando estado colegial y colegiado para insertar en BD
				CenDatosColegialesEstadoBean beanEstadoCol = new CenDatosColegialesEstadoBean ();
				beanEstadoCol.setIdPersona (beanPer.getIdPersona ());
				beanEstadoCol.setIdInstitucion (beanSolic.getIdInstitucion ());
				beanEstadoCol.setIdEstado (estado);
				beanEstadoCol.setFechaEstado ("sysdate");
				beanEstadoCol.setFechaMod ("sysdate");
				
				//comparando con el estado colegial anterior
				CenColegiadoAdm admCenColegiado = new CenColegiadoAdm (this.usrbean);
				Hashtable hashEstado = admCenColegiado.getEstadoColegial 
						(beanEstadoCol.getIdPersona (), beanEstadoCol.getIdInstitucion ());
				int estadoOld = Integer.parseInt ((String) hashEstado.get (CenEstadoColegialBean.C_IDESTADO));
				if (estadoOld != idEstado) {
					//insertando el nuevo estado colegial si ha cambiado
				    CenDatosColegialesEstadoAdm admDCE = new CenDatosColegialesEstadoAdm (this.usrbean);
					if (! admDCE.insert (beanEstadoCol))
						throw new ClsExceptions (admDCE.getError ());
				}
				
				this.setError ("messages.censo.altaColegial.verificarDatos");
			}
			else //Si es una nueva incorporacion
			{
				esNuevo = true; //Es una nueva incorporacion
				
				//Tratamiento de la persona
				if (! existePersona) {
					//rellenando los datos a insertar
					beanPer = new CenPersonaBean ();
					beanPer.setIdPersona (admPer.getIdPersona (beanSolic.getIdInstitucion ()));
					beanPer.setNombre (beanSolic.getNombre ());
					beanPer.setApellido1 (beanSolic.getApellido1 ());
					beanPer.setApellido2 (beanSolic.getApellido2 ());
					beanPer.setNIFCIF (beanSolic.getNumeroIdentificador ());
					beanPer.setNIFCIF (beanPer.getNIFCIF().toUpperCase ());
					beanPer.setFechaNacimiento (beanSolic.getFechaNacimiento ());
					beanPer.setNaturalDe (beanSolic.getNaturalDe ());
					beanPer.setIdTipoIdentificacion (beanSolic.getIdTipoIdentificacion ());
					beanPer.setIdEstadoCivil (beanSolic.getIdEstadoCivil ());					
					beanPer.setSexo (beanSolic.getSexo ());
					beanPer.setFallecido (ClsConstants.DB_FALSE);
					
					//insertando la nueva persona
					if (! admPer.insert (beanPer))
						throw new ClsExceptions (admPer.getError ());
				}
				else {
					//existe la persona: 
					//solo se atualizan los datos de persona con los de la solicitud
					beanPer = admPer.updatePersona (beanPer, beanSolic);
				}
				
				//Tratamiento del cliente
				if (existeCliente) {
					// Existe el Cliente
					
					//Actualizo los datos del cliente con los de la solicitud
					
					beanCli = admCliente.updateCliente(beanCli, beanSolic);
					beanCli =admCliente.updateCaracterCliente(beanCli,ClsConstants.TIPO_CARACTER_PRIVADO_COLEGIADO);
					if (admCol.getDatosColegiales(beanPer.getIdPersona(),beanSolic.getIdInstitucion())==null) {

						// creamos el bean para insertar
						beanCol = new CenColegiadoBean();
						
						if (beanSolic.getIdTipoColegiacion().intValue()==ClsConstants.TIPO_COLEGIACION_ESPANHOL) {
							beanCol.setComunitario(ClsConstants.DB_FALSE);
						} else {
							beanCol.setComunitario(ClsConstants.DB_TRUE);
						}
					
						beanCol.setIdPersona(beanPer.getIdPersona());
						beanCol.setIdInstitucion(beanSolic.getIdInstitucion());
						// jbd 04-02-2010 Se pone por defecto la situacion de residente a TRUE en vez de false porque asi lo pide ciudad real
						// jbd 15-02-2010 Se vuelve a poner a false porque el TRUE tiene implicaciones, se dan de alta servicios
						//					Se volvera a cambiar para que se pueda modificar al solicitar el alta
						// jbd 22-04-2010 Como ahora se puede fijar el check de residente en la pantalla de solicitud pues copiamos ese valor
						if (beanSolic.getResidente()){
							beanCol.setSituacionResidente(ClsConstants.DB_TRUE);
						} else {
							beanCol.setSituacionResidente(ClsConstants.DB_FALSE);
						}
						
						int idTipoColegiacion = beanSolic.getIdTipoColegiacion ().intValue ();
						if(beanSolic.getNColegiado()==null || beanSolic.getNColegiado().equalsIgnoreCase("")){
							if (beanSolic.getIdTipoColegiacion().intValue()==ClsConstants.TIPO_COLEGIACION_COMUNITARIO) {
								beanCol.setNComunitario(admCol.getNColegiado_NComunitario(beanSolic.getIdInstitucion(), ClsConstants.TIPO_COLEGIACION_COMUNITARIO));	
							} else {
								beanCol.setNColegiado(admCol.getNColegiado_NComunitario(beanSolic.getIdInstitucion(), ClsConstants.TIPO_COLEGIACION_ESPANHOL));
							}
						}else{
							if (idTipoColegiacion == ClsConstants.TIPO_COLEGIACION_COMUNITARIO)
								beanCol.setNComunitario(beanSolic.getNColegiado());
							else
								beanCol.setNColegiado(beanSolic.getNColegiado());
						}
						
						beanCol.setFechaIncorporacion("SYSDATE");
						beanCol.setFechaPresentacion("SYSDATE");
						beanCol.setIndTitulacion(ClsConstants.DB_FALSE);
						beanCol.setJubilacionCuota(ClsConstants.DB_FALSE);
						beanCol.setSituacionEjercicio(ClsConstants.DB_FALSE);
						beanCol.setSituacionEmpresa(ClsConstants.DB_FALSE);
						
						// otros colegios
						boolean existeColegiadoOtraInstitucion = false;
						
						CenColegiadoBean beanColOtra = admCol.existeColegiadoOtraInstitucion(beanPer.getIdPersona(),beanSolic.getIdInstitucion());
						if (beanColOtra!=null) {
							existeColegiadoOtraInstitucion = true;
						}
						
						if (existeColegiadoOtraInstitucion) {
							beanCol.setOtrosColegios(ClsConstants.DB_TRUE);
							// actualizo el valor en los otros colegios tambien
							admCol.actualizaColegiadoOtraInstitucion(beanPer.getIdPersona(),beanSolic.getIdInstitucion());
						} else {
							beanCol.setOtrosColegios(ClsConstants.DB_FALSE);
						}
						
						// preparo el estado para insertar tambien los datosColegialesEstado
						int idEstado;
						String fechaEstadoCol= beanSolic.getFechaEstadoColegial();
						if (beanSolic.getIdTipoSolicitud().intValue()==ClsConstants.TIPO_SOLICITUD_EJERCIENTE_INCORPORACION) {
							idEstado = ClsConstants.ESTADO_COLEGIAL_EJERCIENTE;
						} else {
							idEstado = ClsConstants.ESTADO_COLEGIAL_SINEJERCER;
						}
						
						// insertamos un nuevo colegiado
						if (!admCol.insertConEstado(beanCol,idEstado,fechaEstadoCol)) {
							throw new SIGAException(admCol.getError());
						}
//						 Lo borramos de no colegiado
						Hashtable hashNoColegiado =new Hashtable();
						hashNoColegiado.put(CenNoColegiadoBean.C_IDINSTITUCION,beanCol.getIdInstitucion());
						hashNoColegiado.put(CenNoColegiadoBean.C_IDPERSONA,beanCol.getIdPersona());
						Vector vExisteNoColegiado=admNoCol.selectByPK(hashNoColegiado);
						if (vExisteNoColegiado.size()==1){// Si est� dado de alta como no colegiado, lo borramos para posteriormente
							                              // darle de alta como colegiado
						  if (!admNoCol.delete(hashNoColegiado)) {
							throw new SIGAException(admNoCol.getError());
						  }
						}  

						this.setError("messages.censo.altaColegial.verificarDatos");
						
					} else {
						throw new SIGAException("messages.censo.altaColegial.errorExisteCliente");
					}

				} else { //El cliente NO existe
					
					//creando el bean de cliente
					beanCli = new CenClienteBean ();
					
					//rellenando campos para el registro de cliente
					beanCli.setIdTratamiento (beanSolic.getIdTratamiento ());
					beanCli.setIdInstitucion (beanSolic.getIdInstitucion ());
					beanCli.setIdPersona (beanPer.getIdPersona ());
					beanCli.setFechaAlta ("SYSDATE");
					beanCli.setIdLenguaje (ClsConstants.LENGUAJE_ESP);
					beanCli.setAbonosBanco (ClsConstants.TIPO_CARGO_BANCO);
					beanCli.setCargosBanco (ClsConstants.TIPO_CARGO_BANCO);
					beanCli.setPublicidad (ClsConstants.DB_FALSE);
					beanCli.setExportarFoto("0");
					beanCli.setGuiaJudicial (ClsConstants.DB_FALSE);
					beanCli.setComisiones (ClsConstants.DB_FALSE);					
					beanCli.setCaracter (ClsConstants.TIPO_CARACTER_PUBLICO);
					
					//insertando el nuevo cliente
					if (! this.insert (beanCli))
						throw new SIGAException (this.getError ());
				
				
				//Tratamiento del colegiado
				if (admCol.getDatosColegiales (beanPer.getIdPersona (), beanSolic.getIdInstitucion ()) != null)
					//no puede existir ya el colegiado en una nueva incorporacion
					throw new SIGAException ("messages.censo.altaColegial.errorExisteCliente");
				
				//obteniendo el tipo de colegiacion
				int idTipoColegiacion = beanSolic.getIdTipoColegiacion ().intValue ();
				//creando el bean para insertar
				beanCol = new CenColegiadoBean ();
				
				//rellenando el bean de colegiado para insertar
				beanCol.setIdPersona (beanPer.getIdPersona ());
				beanCol.setIdInstitucion (beanSolic.getIdInstitucion ());
				// jbd 15-02-2010 Se vuelve a poner a false porque el TRUE tiene implicaciones, se dan de alta servicios
				//					Se volvera a cambiar para que se pueda modificar al solicitar el alta
				// jbd 22-04-2010 Como ahora se puede fijar el check de residente en la pantalla de solicitud pues copiamos ese valor
				if (beanSolic.getResidente()){
					beanCol.setSituacionResidente(ClsConstants.DB_TRUE);
				} else {
					beanCol.setSituacionResidente(ClsConstants.DB_FALSE);
				}
				
				if (idTipoColegiacion == ClsConstants.TIPO_COLEGIACION_ESPANHOL)
					beanCol.setComunitario (ClsConstants.DB_FALSE);
				else
					beanCol.setComunitario (ClsConstants.DB_TRUE);
				
				if(beanSolic.getNColegiado()==null || beanSolic.getNColegiado().equalsIgnoreCase("")){
					if (idTipoColegiacion == ClsConstants.TIPO_COLEGIACION_COMUNITARIO)
						beanCol.setNComunitario (admCol.getNColegiado_NComunitario 
								(beanSolic.getIdInstitucion (), 
								ClsConstants.TIPO_COLEGIACION_COMUNITARIO));	
					else
						beanCol.setNColegiado (admCol.getNColegiado_NComunitario 
								(beanSolic.getIdInstitucion (), 
								ClsConstants.TIPO_COLEGIACION_ESPANHOL));
				}else{
					if (idTipoColegiacion == ClsConstants.TIPO_COLEGIACION_COMUNITARIO)
						beanCol.setNComunitario(beanSolic.getNColegiado());
					else
						beanCol.setNColegiado(beanSolic.getNColegiado());
				}
				// TODO //jbd // Utilizamos la fecha de estado como la fecha de incorporacion, ya que en interfaz es la fecha de incorporacion
				if(beanSolic.getFechaEstadoColegial()!=null && !beanSolic.getFechaEstadoColegial().equalsIgnoreCase("")){
					beanCol.setFechaIncorporacion (beanSolic.getFechaEstadoColegial());  
				}else{
					beanCol.setFechaIncorporacion ("SYSDATE");
				}
				beanCol.setFechaPresentacion ("SYSDATE");
				beanCol.setIndTitulacion (ClsConstants.DB_FALSE);
				beanCol.setJubilacionCuota (ClsConstants.DB_FALSE);
				beanCol.setSituacionEjercicio (ClsConstants.DB_FALSE);
				beanCol.setSituacionEmpresa (ClsConstants.DB_FALSE);
				
				CenColegiadoBean beanColOtra = admCol.existeColegiadoOtraInstitucion(beanPer.getIdPersona (), beanSolic.getIdInstitucion ());
				if (beanColOtra != null) { //existe Cliente en otra Institucion
					beanCol.setOtrosColegios (ClsConstants.DB_TRUE);
					//actualizando el valor en los otros colegios tambien
					admCol.actualizaColegiadoOtraInstitucion 
							(beanPer.getIdPersona (), beanSolic.getIdInstitucion ());
				} else {
					beanCol.setOtrosColegios (ClsConstants.DB_FALSE);
				}
				
				// preparo el estado para insertar tambien los datosColegialesEstado
				int idEstado;
				String fechaEstadoCol= beanSolic.getFechaEstadoColegial();
				if (tipoSolicitud == ClsConstants.TIPO_SOLICITUD_EJERCIENTE_INCORPORACION)
					idEstado = ClsConstants.ESTADO_COLEGIAL_EJERCIENTE;
				else
					idEstado = ClsConstants.ESTADO_COLEGIAL_SINEJERCER;
				
				//insertando nuevo colegiado
				if (! admCol.insertConEstado (beanCol, idEstado, fechaEstadoCol))
					throw new SIGAException (admCol.getError ());
				
				//borrando no colegiado
				Hashtable hashNoColegiado = new Hashtable ();
				hashNoColegiado.put (CenNoColegiadoBean.C_IDINSTITUCION, beanCol.getIdInstitucion ());
				hashNoColegiado.put (CenNoColegiadoBean.C_IDPERSONA, beanCol.getIdPersona ());
				Vector vExisteNoColegiado = admNoCol.selectByPK (hashNoColegiado);
				if (vExisteNoColegiado.size () == 1)
					if (! admNoCol.delete (hashNoColegiado))
						throw new SIGAException (admNoCol.getError ());
				
				this.setError ("messages.censo.altaColegial.clienteNuevoInsertado");
				}
			}
			
			//obteniendo el adm para las actualizaciones de los datos en Consejo
			CenColaCambioLetradoAdm colaAdm = new CenColaCambioLetradoAdm (this.usrbean);
			
			//insertando en la cola de modificacion de datos
			if (!colaAdm.insertarCambioEnCola 
					(ClsConstants.COLA_CAMBIO_LETRADO_APROBACION_COLEGIACION, 
					beanCli.getIdInstitucion (), beanCli.getIdPersona (), null))
				throw new SIGAException (colaAdm.getError());
			
			//////   TRATAMIENTO DE DIRECCIONES   //////
			Direccion direccion = new Direccion();
			beanDir = new CenDireccionesBean ();
			CenDireccionesAdm direccionAdm = new CenDireccionesAdm(this.usrbean);
			//rellenando el bean de direcciones
			beanDir.setIdPersona (beanPer.getIdPersona ());
			beanDir.setIdInstitucion (beanSolic.getIdInstitucion ());
			beanDir.setDomicilio (beanSolic.getDomicilio ());
			beanDir.setCodigoPostal (beanSolic.getCodigoPostal ());
			beanDir.setTelefono1 (beanSolic.getTelefono1 ());
			beanDir.setTelefono2 (beanSolic.getTelefono2 ());
			beanDir.setMovil (beanSolic.getMovil ());
			beanDir.setFax1 (beanSolic.getFax1 ());
			beanDir.setFax2 (beanSolic.getFax2 ());
			beanDir.setCorreoElectronico (beanSolic.getCorreoElectronico ());
			beanDir.setIdPais (beanSolic.getIdPais ());
			if (beanDir.getIdPais ().equals ("")) {
				beanDir.setIdPais (ClsConstants.ID_PAIS_ESPANA);
			}
			if (beanDir.getIdPais ().equals (ClsConstants.ID_PAIS_ESPANA)) { 
				beanDir.setIdProvincia (beanSolic.getIdProvincia ());
				beanDir.setIdPoblacion (beanSolic.getIdPoblacion ());
			} else {
				beanDir.setIdProvincia ("");
				beanDir.setIdPoblacion ("");
			}
			beanDir.setPoblacionExtranjera (beanSolic.getPoblacionExtranjera ());
			
			//si es una nueva incorporacion y no existe como cliente ni como persona debe tener como preferente el correo y el mail
			if (esNuevo && !existePersona && !existeCliente)
				beanDir.setPreferente (ClsConstants.TIPO_PREFERENTE_CORREO + ClsConstants.TIPO_PREFERENTE_CORREOELECTRONICO);
			
			//estableciendo los datos del tipo de direccion guardia
			//Si existe una direcci�n de facturaci�n no se inserta el tipo de direcci�n de facturaci�n
			String tiposDir = "";
			Row row = direccionAdm.comprobarTipoDireccion(String.valueOf(ClsConstants.TIPO_DIRECCION_FACTURACION), beanDir.getIdInstitucion().toString(),
					beanDir.getIdPersona().toString());
			List<Integer> direccionesObligatorias = new ArrayList<Integer>();
			if (row != null) {
				// Solo se permite una direcci�n de facturaci�n
				direccionesObligatorias = Direccion.getListaDireccionesObligatorias(ClsConstants.TIPO_ACCESO_PESTANAS_COLEGIADO);

				// Si existe la direcci�n de fact. elimino esta de direcciones obligatorias.
				// MJM: Lo hago leyendo la lista por si cambian las direcciones obligatorias.
				List<Integer> direccionesObligatorias_aux = direccionesObligatorias;
				for (int d = 0; d < direccionesObligatorias_aux.size(); d++) {
					if (direccionesObligatorias.get(d).intValue() == ClsConstants.TIPO_DIRECCION_FACTURACION)
						direccionesObligatorias.remove(d);
				}

				tiposDir = ClsConstants.TIPO_DIRECCION_CENSOWEB + "," + ClsConstants.TIPO_DIRECCION_TRASPASO_OJ + "," + ClsConstants.TIPO_DIRECCION_DESPACHO
						+ "," + ClsConstants.TIPO_DIRECCION_GUIA;
			} else {
				tiposDir = ClsConstants.TIPO_DIRECCION_CENSOWEB + "," + ClsConstants.TIPO_DIRECCION_TRASPASO_OJ + "," + ClsConstants.TIPO_DIRECCION_DESPACHO
						+ "," + ClsConstants.TIPO_DIRECCION_GUIA + "," + ClsConstants.TIPO_DIRECCION_FACTURACION;
			}

			// Se llama a la interfaz Direccion para actualizar una nueva direccion
			direccion.insertar(beanDir, tiposDir, null, direccionesObligatorias, null, this.usrbean);
			
		}
		catch (SIGAException se) {
			throw se;
		}
		catch (Exception e) {
			throw new SIGAException ("Error al insertar datos en B.D.", e);
		}
		
		return beanCli;
	} //altaColegial ()
	
	public Hashtable getDatosCertificado(Long idPersona, Integer idInstitucion, String location,boolean isFo) throws SIGAException
	{
	    Hashtable htTabla = new Hashtable();
	    
	    htTabla.put("@idpersona@", ""+idPersona);
	    htTabla.put("@idinstitucion@", ""+idInstitucion);
	    
	    return getDatosCertificado(htTabla, location,isFo);
	}
	
	public Hashtable getDatosCertificado(Hashtable ht,String idInstitucion,boolean isFo) throws SIGAException
	{
	    try
	    {
	        Hashtable htDatos = new Hashtable();
	        Hashtable htDatos1 = new Hashtable();
	        
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
	        
	        String sSQL = new InformeCertificadosEspeciales().getSqlCamposGeneral(new Integer(idInstitucion));
	  
	        htDatos=this.getEtiquetasComunesCertificados(ht,idInstitucion,isFo);
	        Enumeration enumHash = ht.keys();
	        
	        while (enumHash.hasMoreElements())
	        {
	            String sClave = (String)enumHash.nextElement();
	            String sValor = (String)ht.get(sClave);
	            
	            //sSQL = sSQL.replaceFirst(sClave, sValor);
	            sSQL = sSQL.replaceAll("(?i)"+sClave, sValor);
	        }
	        
	        sSQL = sSQL.replaceAll("(?i)" + "@IDIOMA@", this.usrbean.getLanguage());
//	        sSQL = sSQL.replaceAll("@idioma@", this.usrbean.getLanguage());
	        
	        RowsContainer rc = find(sSQL);

	        if (rc==null || rc.size()==0) 
	        {
	            throw new SIGAException("messages.certificados.error.noexistendatos", null, null);
	        }
            
			Row fila = (Row) rc.get(0);
			
			htDatos1 = (Hashtable)fila.getRow();
			htDatos.putAll(htDatos1);
			
			
			if (htDatos == null)
			{
			    throw new SIGAException("messages.certificados.error.noexistendatos", null, null);
			}
			
			// TRATAMIENTO DEL CONTADOR   
			// RGG cambio para diligencias desde cgae
			// Como los datos de institucion de la consulta son de la institucion origen, nunca vamos a encontrar el contador ni el certificado adecuado.
			// Obtenemos como regla general el contador y el certificado con el id de certificado y la isntitucion parsada como parametro
			// Y obviamos la solicitud devuelta por la consulta, que solo vale cuando es comunicado o certificado normal.
	        return htDatos;
	    }
	    catch(SIGAException e)
	    {
	    	throw e;
	    }
	    catch(Exception e)
	    {
	        throw new SIGAException("messages.certificados.error.obteciondatoscliente", e);
	    }
	}

	
	
	public Hashtable getEtiquetasComunesCertificados(Hashtable ht,String idInstitucion, boolean isFo) throws SIGAException
	{
	    try
	    {  Hashtable htDatos = new Hashtable();
	      
			// TRATAMIENTO DEL CONTADOR   
			// RGG cambio para diligencias desde cgae
			// Como los datos de institucion de la consulta son de la institucion origen, nunca vamos a encontrar el contador ni el certificado adecuado.
			// Obtenemos como regla general el contador y el certificado con el id de certificado y la isntitucion parsada como parametro
			// Y obviamos la solicitud devuelta por la consulta, que solo vale cuando es comunicado o certificado normal.
			CerSolicitudCertificadosAdm admCer=new CerSolicitudCertificadosAdm(this.usrbean);
			Hashtable htCer = new Hashtable();
			String idSolicitud = (String)ht.get("@idsolicitud@");
			htCer.put(CerSolicitudCertificadosBean.C_IDINSTITUCION,idInstitucion);
			htCer.put(CerSolicitudCertificadosBean.C_IDSOLICITUD,idSolicitud);
			Vector v = admCer.selectByPK(htCer);
			CerSolicitudCertificadosBean beanCer = null;
			if (v!=null && v.size()>0) {
				beanCer = (CerSolicitudCertificadosBean) v.get(0);	
			}
/*			
			String pre = (htDatos.get("PREFIJO_CER")!=null)?(String)htDatos.get("PREFIJO_CER"):"";
			String suf = (htDatos.get("SUFIJO_CER")!=null)?(String)htDatos.get("SUFIJO_CER"):"";
			String numContador=(String)htDatos.get("CONTADOR_CER");
			String idtipop=(String)htDatos.get("PPN_IDTIPOPRODUCTO");
			String idp=(String)htDatos.get("PPN_IDPRODUCTO");
			String idpi=(String)htDatos.get("PPN_IDPRODUCTOINSTITUCION");
*/		
			String idtipop=beanCer.getPpn_IdTipoProducto().toString();
			String idp=beanCer.getPpn_IdProducto().toString();
			String idpi=beanCer.getPpn_IdProductoInstitucion().toString();
			
			// Obtengo las sanciones
			//String textoCertificado = admCer.getTextoCertificado(beanCer.getIdInstitucion().toString(), beanCer.getIdSolicitud().toString());
			CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(this.usrbean);
			CerSolicitudCertificadosTextoBean beanTextoCertificado = admSolicitud.getSolicitudCertificadosTexto(idInstitucion, idSolicitud);
		    String textoCertificado = "";
		    boolean isIncluirSanciones =false;
		    boolean isIncluirDeudas=false;
		   
		    if(beanTextoCertificado!=null){
		    	textoCertificado = beanTextoCertificado.getTexto();
	    		isIncluirSanciones = beanTextoCertificado.getIncluirSanciones()!=null && beanTextoCertificado.getIncluirSanciones().equalsIgnoreCase("S");
	    		isIncluirDeudas = beanTextoCertificado.getIncluirDeudas()!=null && beanTextoCertificado.getIncluirDeudas().equalsIgnoreCase("S");
		    	
		    }
		    
		   			
			
			
			
			// SUSTITUIR los saltos de linea por esto: <fo:block  space-before="0.5cm" />
		    if(isFo)
		    	textoCertificado = textoCertificado.replaceAll("\\n","BRdummyBR");
			// lo guardamos 
			htDatos.put("TEXTOCERTIFICADO",textoCertificado);
			
			Long personaCertificado = beanCer.getIdPersona_Des();
						
			//Vemos si hay que incluir los expedientes sancionados
			if(isIncluirSanciones){
				ExpExpedienteAdm admExpedientes = new ExpExpedienteAdm(this.usrbean);
				//CenSancionAdm admSanciones = new CenSancionAdm(this.usrbean);
	    		//Hashtable htFiltroSanciones = new Hashtable();
	    		
	    		//htFiltroSanciones.put(ExpExpedienteBean.C_IDINSTITUCION,idInstitucion);
	    		//htFiltroSanciones.put(ExpDenunciadoBean.C_IDPERSONA,personaCertificado);
	    		//htFiltroSanciones.put(ExpExpedienteBean.C_SANCIONADO,"S");
	    		Vector vSanciones = admExpedientes.selectExpedientesCliente(personaCertificado, Integer.valueOf(idInstitucion), "S");	    		
	    		StringBuffer descSancion = new StringBuffer("");
	    		if(vSanciones!=null && !vSanciones.isEmpty()){
	    			descSancion.append(UtilidadesString.getMensajeIdioma(usrbean.getLanguage(),"certificados.solicitudes.informe.sanciones.si").trim());
	    			
	    			for (int i=0;i<vSanciones.size();i++) {
	    				Hashtable row = ((com.atos.utils.Row)vSanciones.get(i)).getRow();
	    				descSancion.append(" ");
	    				descSancion.append(UtilidadesString.getMensajeIdioma(usrbean.getLanguage(),"certificados.solicitudes.informe.sanciones.expediente").trim());
	    				descSancion.append(" ");
	    				descSancion.append(row.get(ExpExpedienteBean.C_ANIOEXPEDIENTE));
	    				descSancion.append("/");
	    				descSancion.append(row.get(ExpExpedienteBean.C_NUMEROEXPEDIENTE));
	    				if(i!=vSanciones.size()-1)
	    					descSancion.append(",");
	    				descSancion.append("\n\r\n\r");
		    			
	    			
	    			}
	    		}else{
	    	
	    			descSancion.append(UtilidadesString.getMensajeIdioma(usrbean.getLanguage(),"certificados.solicitudes.informe.sanciones.no").trim());
	    			
	    		}
				htDatos.put("NOTAS_DESFAVORABLES",descSancion.toString());
			}else{
				htDatos.put("NOTAS_DESFAVORABLES","");
				
			}
			
			//Vemos si hay que incluir las deudas
			
			
			if(isIncluirDeudas){
				FacFacturaAdm facturaAdm = new FacFacturaAdm (this.usrbean);       
		        Vector vMorosos = facturaAdm.selectFacturasMoroso(idInstitucion,personaCertificado.toString(),null,null,null,null,false,false,this.usrbean.getLanguage());
		        StringBuffer descMorosos = new StringBuffer("");
	    		if(vMorosos!=null && !vMorosos.isEmpty()){
	    			double totalDeuda = 0; 
	    			for (int i=0;i<vMorosos.size();i++) {
	        			Row rowFactura = (Row) vMorosos.get(i);
	        			String deuda = rowFactura.getString("DEUDA");
	        			
		    			if (deuda!=null && !deuda.trim().equals("")) {
		    				totalDeuda= totalDeuda + UtilidadesNumero.redondea(Double.parseDouble(deuda),2);
		    				
		    			}
		    			
	    			
	    			}
	    			
	    			
	    			descMorosos.append(UtilidadesString.getMensajeIdioma(usrbean.getLanguage(),"certificados.solicitudes.informe.deudas.si").trim());
	    			descMorosos.append(" ");
	    			//descMorosos.append();
	    			double dblTotalDeuda = UtilidadesNumero.redondea(totalDeuda,2);  
	    			
	    			String entero = String.valueOf((int)dblTotalDeuda) ;
	    			if (Integer.parseInt(entero)<=4){// No est� implementada la funci�n F_SIGA_NUMEROENLETRA para numeros con parte entera mayor de 4 d�gitos	
	    			descMorosos.append(EjecucionPLs.ejecutarF_SIGA_NUMEROENLETRA
	    					(entero,usrbean.getLanguage()));
	    				
	    			String decimal = String.valueOf(dblTotalDeuda);
	    			decimal = decimal.substring(decimal.indexOf(".")+1);
	    			  
	    			descMorosos.append(" ");
	    			descMorosos.append(UtilidadesString.getMensajeIdioma(usrbean.getLanguage(),"certificados.solicitudes.informe.deudas.moneda").trim());
	    			
	    			if(Integer.parseInt(decimal)!=0){
	    				descMorosos.append(" ");
	    				descMorosos.append(UtilidadesString.getMensajeIdioma(usrbean.getLanguage(),"certificados.solicitudes.informe.deudas.centimos.con").trim());
	    				descMorosos.append(" ");
	    				descMorosos.append(EjecucionPLs.ejecutarF_SIGA_NUMEROENLETRA
		    					(decimal,usrbean.getLanguage()));
	    				descMorosos.append(" ");
		    			descMorosos.append(UtilidadesString.getMensajeIdioma(usrbean.getLanguage(),"certificados.solicitudes.informe.deudas.centimos").trim());
	    				
	    			}
	    			}else{
	    				descMorosos.append(EjecucionPLs.ejecutarF_SIGA_NUMEROENLETRA
		    					(String.valueOf(totalDeuda),usrbean.getLanguage()));
	    			}
	    			descMorosos.append("\n\r\n\r");
	    		}else{
	    			descMorosos.append(UtilidadesString.getMensajeIdioma(usrbean.getLanguage(),"certificados.solicitudes.informe.deudas.no").trim());
	    			
	    		}
		        
				htDatos.put("DEUDAS",descMorosos.toString());
				
			}else{
				htDatos.put("DEUDAS","");
				
			}
			
			// obtengo el objeto contador
			GestorContadores gc = new GestorContadores(this.usrbean);
//			String idContador="PYS_"+idtipop+"_"+idp+"_"+idpi;
			String idContador="";
			PysProductosInstitucionAdm admProd = new PysProductosInstitucionAdm(this.usrbean);
			Vector v2=admProd.select("WHERE "+PysProductosInstitucionBean.C_IDINSTITUCION+"="+idInstitucion+" AND "+PysProductosInstitucionBean.C_IDTIPOPRODUCTO+"="+idtipop+" AND "+PysProductosInstitucionBean.C_IDPRODUCTO+"="+idp+" AND "+PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION+"="+idpi);
			PysProductosInstitucionBean beanProd=null;
			if (v2!=null && v2.size()>0) {
				beanProd = (PysProductosInstitucionBean) v2.get(0);
				idContador=beanProd.getIdContador();
			}
			
			Hashtable contadorTablaHash=gc.getContador(new Integer(idInstitucion),idContador);

			String pre = (beanCer.getPrefijoCer()!=null)?(String)beanCer.getPrefijoCer():"";
			String suf = (beanCer.getSufijoCer()!=null)?(String)beanCer.getSufijoCer():"";
			String numContador=beanCer.getContadorCer().toString();

			// formateo el contador
			Integer longitud= new Integer((contadorTablaHash.get("LONGITUDCONTADOR").toString()));
			int longitudContador=longitud.intValue();
		  	Integer contadorSugerido=new Integer(numContador);
		  	String contadorFinalSugerido=UtilidadesString.formatea(contadorSugerido,longitudContador,true);
			String contador =pre.trim()+contadorFinalSugerido+suf.trim(); 			

			// lo guardamos formateado
			htDatos.put("CONTADOR",contador);
			
	        return htDatos;
	    }
	    catch(SIGAException e)
	    {
	    	throw e;
	    }
	    catch(Exception e)
	    {
	        throw new SIGAException("messages.certificados.error.obteciondatoscliente", e);
	    }
	}

	/**
	 * Devuelve la cuenta de abono para una persona, comprobando si representa a una sociedad o a si mismo 
	 * 
	 * @param idInstitucion
	 * @param idPersona 
	 * @return ArrayList (0=idInstitucion; 1=idPersona; 3=idCuentaAbonoSJCS)
	 * <br> Si la cuenta es -1 es que pasa a pagar por caja  
	 * @exception  ClsExceptions En cualquier caso de error
	 */	
	public ArrayList getCuentaAbonoSJCS(String idInstitucion, String idPersona) throws ClsExceptions {
		ArrayList salida = new ArrayList();
		salida.add(idInstitucion);
		try {
            RowsContainer rc = new RowsContainer(); 
            Hashtable hFila = null;
            StringBuilder sql = new StringBuilder();
            
            // Buscamos si la persona pertenece a una sociedad y no tiene de baja la relacion
            sql.append("SELECT ");
            sql.append(CenComponentesBean.C_IDPERSONA);
            sql.append(", ");
            sql.append(CenComponentesBean.C_IDCUENTA);
            sql.append(" FROM ");
            sql.append(CenComponentesBean.T_NOMBRETABLA);
            sql.append(" WHERE ");
            sql.append(CenComponentesBean.C_IDINSTITUCION);
            sql.append(" = ");
            sql.append(idInstitucion);
            sql.append(" AND ");
            sql.append(CenComponentesBean.C_CEN_CLIENTE_IDPERSONA);
            sql.append(" = ");
            sql.append(idPersona);
            sql.append(" AND ");
            sql.append(CenComponentesBean.C_IDINSTITUCION);
            sql.append(" = ");
            sql.append(CenComponentesBean.C_CEN_CLIENTE_IDINSTITUCION);
            sql.append(" AND ");
            sql.append(CenComponentesBean.C_SOCIEDAD);
            sql.append(" = '");
            sql.append(ClsConstants.DB_TRUE);
            sql.append("' AND ");
            sql.append(CenComponentesBean.C_FECHABAJA);
            sql.append(" IS NULL");
												
			rc = this.find(sql.toString()); 
			if (rc!=null && rc.size()>0) { // Tiene registros activos en componentes
				hFila = ((Row) rc.get(0)).getRow();
				String sIdPersonaSociedad = (String) hFila.get(CenComponentesBean.C_IDPERSONA);
				Object oIdCuenta = hFila.get(CenComponentesBean.C_IDCUENTA);
				salida.add(sIdPersonaSociedad);
				
				if (oIdCuenta!=null) {// Tiene una cuenta de abono asociada			
					
					sql = new StringBuilder(); // Comprobamos que la cuenta siga activa y sea ABONOSJSCS=1					
					sql.append("SELECT ");
					sql.append(CenCuentasBancariasBean.C_IDCUENTA); 
					sql.append(" FROM ");
					sql.append(CenCuentasBancariasBean.T_NOMBRETABLA);
					sql.append(" WHERE ");
					sql.append(CenCuentasBancariasBean.C_IDINSTITUCION);
					sql.append(" = ");
					sql.append(idInstitucion);
					sql.append(" AND ");
					sql.append(CenCuentasBancariasBean.C_IDPERSONA);
					sql.append(" = ");
					sql.append(sIdPersonaSociedad);
					sql.append(" AND ");
					sql.append(CenCuentasBancariasBean.C_FECHABAJA);
					sql.append(" IS NULL AND ");
					sql.append(CenCuentasBancariasBean.C_ABONOSJCS);
					sql.append(" = '1' AND ");
					sql.append(CenCuentasBancariasBean.C_IDCUENTA);
					sql.append(" = ");
					sql.append(oIdCuenta);
														
					rc = this.find(sql.toString()); 
					if (rc!=null && rc.size()>0) { // La sociedad tiene una cuenta de abono activa y es AbonoSJCS=1
						salida.add(oIdCuenta);
						
					} else { // La sociedad tiene cuenta de abono de baja o es AbonoSJCS=0
						salida.add("-1"); // paga por caja
					}					
					
				} else { // No tiene una cuenta la sociedad
					salida.add("-1"); // paga por caja
				}
				
			} else { // No tiene registro en componentes, y por lo tanto, hay que buscar una cuenta bancaria activa de la persona con AbonoSJCS=1
				salida.add(idPersona);
				
				sql = new StringBuilder();					
				sql.append("SELECT ");
				sql.append(CenCuentasBancariasBean.C_IDCUENTA); 
				sql.append(" FROM ");
				sql.append(CenCuentasBancariasBean.T_NOMBRETABLA);
				sql.append(" WHERE ");
				sql.append(CenCuentasBancariasBean.C_IDINSTITUCION);
				sql.append(" = ");
				sql.append(idInstitucion);
				sql.append(" AND ");
				sql.append(CenCuentasBancariasBean.C_IDPERSONA);
				sql.append(" = ");
				sql.append(idPersona);
				sql.append(" AND ");
				sql.append(CenCuentasBancariasBean.C_FECHABAJA);
				sql.append(" IS NULL AND ");   
				sql.append(CenCuentasBancariasBean.C_ABONOSJCS);
				sql.append(" = '1' ");
				sql.append(" ORDER BY ");
				sql.append(CenCuentasBancariasBean.C_FECHAMODIFICACION);
				sql.append(" DESC "); 
				
				rc = this.find(sql.toString()); 
				if (rc!=null && rc.size()>0) { // La persona tiene alguna cuenta de AbonoSJCS=1 activa
					hFila = ((Row) rc.get(0)).getRow();
					salida.add((String)hFila.get(CenCuentasBancariasBean.C_IDCUENTA));
					
				} else { // La persona no tiene ninguna cuenta de AbonoSJCS=1 activa
					salida.add("-1"); // paga por caja
				}
			}
			
		} catch(Exception e) {
			throw new ClsExceptions (e, "Error al obtener la cuenta de abono SJCS");
		}
		
		return salida;
	}	

	/**
	 * actualiza los datos de la persona con los de la solicitud de incorporacion
	 * @param beanPersona
	 * @param beanSolicitud
	 * @return CenClienteBean
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public CenClienteBean updateCliente (CenClienteBean beanCliente, CenSolicitudIncorporacionBean beanSolicitud) throws ClsExceptions, SIGAException 
	{
		CenClienteBean beanClienteTmp = null;
		
		try {
			//Clono el objeto Cliente
			beanClienteTmp = (CenClienteBean) this.hashTableToBean((Hashtable)(beanCliente.getOriginalHash()).clone());
			
			//Actualizo los campos de la solicitud
			beanClienteTmp.setIdTratamiento(beanSolicitud.getIdTratamiento());
			
			//Actualizo la persona
			update(beanClienteTmp, beanCliente);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al insertar datos en B.D.");
		}
		return beanClienteTmp;
	}
	
	/**
	 * Devuelve el nivel de error:
	 * -0: NO hay error
	 * -1: Error, tiene guardias pendientes
	 * -2: Error, tiene designas pendientes
	 * -3: Excepcion
	 */
	public int tieneTrabajosSJCSPendientes(Long idPersona, Integer idInstitucion,String fechaDesde,String fechaHasta)
	{
		return tieneTrabajosSJCSPendientes(idPersona, idInstitucion, null,null,fechaDesde,fechaHasta);
	}
	/**
	 * Devuelve el nivel de error:
	 * -0: NO hay error
	 * -1: Error, tiene guardias pendientes
	 * -2: Error, tiene designas pendientes
	 * -3: Excepcion
	 */
	public int tieneTrabajosSJCSPendientes(Long idPersona, Integer idInstitucion, Integer idTurno,Integer idGuardia,String fechaDesde,String fechaHasta) {
		//1.- Chequeo que no tiene guardias pendientes de realizar:
		
		int error = tieneGuardiasSJCSPendientes(idPersona, idInstitucion, idTurno,idGuardia,fechaDesde, fechaHasta);
		//En caso de no tener error en la primera comprobacion:
		if (error == 0) {
			//2.- Chequeo que no tiene designas pendientes de realizar:
			try {
				Vector designasPendientesVector = getDesignacionesSJCSPendientes(idPersona, idInstitucion, idTurno, fechaDesde, fechaHasta);
				if (designasPendientesVector!=null && !designasPendientesVector.isEmpty() )
					error = 2;	
			} catch (SIGAException e) {
				error = 3;
			}
		
		}
		
		//Devuelvo el nivel del error:
		return error;
	}
	
	
	public Vector getDesignacionesSJCSPendientes(Long idPersona, Integer idInstitucion, Integer idTurno, String fechaDesde,String fechaHasta)throws SIGAException {
		//2.- Chequeo que no tiene designas pendientes de realizar:
		ScsDesignaAdm admDesignas = new ScsDesignaAdm(this.usrbean);
		String designacion= UtilidadesString.getMensajeIdioma(this.usrbean.getLanguageInstitucion(),"gratuita.busquedaDesignas.literal.designa");
		StringBuffer sql2 = new StringBuffer();
		sql2.append("SELECT des.ANIO||'/'||des.CODIGO INCIDENCIA ");
		sql2.append(",TO_CHAR(des.FECHAENTRADA,'dd/mm/yyyy') FECHA ");
		sql2.append(",'"+designacion+"' DESCRIPCION ");
		
		
		sql2.append(" FROM "+ScsDesignaBean.T_NOMBRETABLA+" des,"+ScsDesignasLetradoBean.T_NOMBRETABLA+" deslet");
		sql2.append(" WHERE des."+ScsDesignaBean.C_IDINSTITUCION+"="+idInstitucion);
		sql2.append(" AND des."+ScsDesignaBean.C_ESTADO+"<>'F'");
		sql2.append(" AND deslet."+ScsDesignasLetradoBean.C_IDPERSONA+"="+idPersona);
		sql2.append(" AND deslet."+ScsDesignasLetradoBean.C_FECHARENUNCIA+" IS NULL");
		
		if (idTurno!=null)
		{
			sql2.append(" AND deslet."+ScsDesignasLetradoBean.C_IDTURNO+"="+idTurno);
		}
		
		sql2.append(" AND des."+ScsDesignaBean.C_IDINSTITUCION+" = deslet."+ScsDesignasLetradoBean.C_IDINSTITUCION);
		sql2.append(" AND des."+ScsDesignaBean.C_IDTURNO+" = deslet."+ScsDesignasLetradoBean.C_IDTURNO);
		sql2.append(" AND des."+ScsDesignaBean.C_ANIO+" = deslet."+ScsDesignasLetradoBean.C_ANIO);
		sql2.append(" AND des."+ScsDesignaBean.C_NUMERO+" = deslet."+ScsDesignasLetradoBean.C_NUMERO);
		
		if(fechaDesde!=null && fechaHasta!=null){
			sql2.append(" AND TRUNC(des."+ScsDesignaBean.C_FECHAENTRADA+") BETWEEN '"+fechaDesde+"' AND '"+fechaHasta+"' ");
		}else if(fechaHasta!=null ){
			if(fechaHasta.equalsIgnoreCase("sysdate"))
				sql2.append(" AND des."+ScsDesignaBean.C_FECHAENTRADA+" > sysdate ");
			else
				sql2.append(" AND TRUNC(des."+ScsDesignaBean.C_FECHAENTRADA+") > '"+fechaHasta+"' ");
				
		} 
		
		Vector designasPendientesVector = null;
		try {
			designasPendientesVector = admDesignas.selectGenerico(sql2.toString());
			
		} catch (Exception e) {
			throw new SIGAException("Error al obtener las designaciones pendientes");
		}
		return designasPendientesVector;
	}
	public int tieneGuardiasSJCSPendientes(Long idPersona, Integer idInstitucion, Integer idTurno,Integer idGuardia,String fechaDesde,String fechaHasta) {
		//1.- Chequeo que no tiene guardias pendientes de realizar:
		
		int error =0;
		try {
			Vector guardiasPendientesVector = getGuardiasSJCSPendientes(idPersona, idInstitucion, idTurno,idGuardia,fechaDesde,fechaHasta);
			if (guardiasPendientesVector!=null && !guardiasPendientesVector.isEmpty())
				error = 1;	
		} catch (SIGAException e) {
			error = 3;
		}

		return error;
	}
	public int tieneDesignacionesPendientes(Long idPersona, Integer idInstitucion, Integer idTurno,String fechaDesde,String fechaHasta) {
		//1.- Chequeo que no tiene guardias pendientes de realizar:
		
		int error =0;
		try {
			Vector designasPendientesVector = getDesignacionesSJCSPendientes(idPersona, idInstitucion, idTurno,fechaDesde,fechaHasta);
			if (designasPendientesVector!=null && !designasPendientesVector.isEmpty())
				error = 2;	
		} catch (SIGAException e) {
			error = 3;
		}

		return error;
	}
	
	
	
	public Vector getGuardiasSJCSPendientes(Long idPersona, Integer idInstitucion, Integer idTurno, Integer idGuardia,String fechaDesde,String fechaHasta)throws SIGAException {
	
	
		//1.- Chequeo que no tiene guardias pendientes de realizar:
		ScsGuardiasColegiadoAdm admGuardiasColegiado = new ScsGuardiasColegiadoAdm(this.usrbean); 
		String guardia= UtilidadesString.getMensajeIdioma(this.usrbean.getLanguageInstitucion(),"gratuita.gestionInscripciones.guardia.literal");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT TO_CHAR(GC.FECHAINICIO,'dd/mm/yyyy')||' - '||TO_CHAR(GC.FECHA_FIN,'dd/mm/yyyy') FECHA ");
		sql.append(" ,GT.NOMBRE INCIDENCIA ");
		sql.append(" ,'"+guardia+"' DESCRIPCION ");
		sql.append(" FROM SCS_CABECERAGUARDIAS GC,SCS_GUARDIASTURNO GT ");
		sql.append(" WHERE  ");
		sql.append(" GC.IDINSTITUCION = GT.IDINSTITUCION ");
		sql.append(" AND GC.IDTURNO = GT.IDTURNO ");
		sql.append(" AND GC.IDGUARDIA = GT.IDGUARDIA ");
	 
		sql.append("AND GC."+ScsCabeceraGuardiasBean.C_IDINSTITUCION+"="+idInstitucion);
		sql.append(" AND GC."+ScsCabeceraGuardiasBean.C_IDPERSONA+"="+idPersona);
		if(fechaDesde!=null && fechaHasta!=null){
			sql.append(" AND TRUNC(GC."+ScsCabeceraGuardiasBean.C_FECHA_FIN+") BETWEEN '"+fechaDesde+"' AND '"+fechaHasta+"'  ");
		}else if(fechaHasta!=null && !fechaHasta.equalsIgnoreCase("sysdate")){
			sql.append(" AND TRUNC(GC."+ScsCabeceraGuardiasBean.C_FECHA_FIN+") > '"+fechaHasta+"' ");
		}else{
			sql.append(" AND GC."+ScsCabeceraGuardiasBean.C_FECHA_FIN+" > SYSDATE ");
		}
		if(idGuardia!=null)
			sql.append(" AND GC."+ScsCabeceraGuardiasBean.C_IDGUARDIA+"="+idGuardia);
		
		if (idTurno!=null)
		{
			sql.append(" AND GC." + ScsCabeceraGuardiasBean.C_IDTURNO+"="+idTurno);
		}
		sql.append(" ORDER BY GC.FECHAINICIO DESC");
		Vector vGuardiasPendientes =  new Vector(); 
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.query(sql.toString())) {
				
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);

					 
					vGuardiasPendientes.add(fila.getRow());
				}
			}
		} 
		catch (Exception e) { 	
			throw new SIGAException("Error al obtener las guardias pendientes"); 
		}
		return vGuardiasPendientes;
		
	}
	
	
	/** 
	 * Recoge informacion sobre los estados colegiales de un determinado cliente colegiado <br/>
	 * @param  idPersona - identificador de la persona
	 * @param  idInstitucion - identificador de la institucion	
	 * @return  Vector - Filas de la tabla seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */		
	public Vector getDatosColegiacion (Long idPersona, String idInstitucion, String idioma) throws ClsExceptions, SIGAException{ 
		
		RowsContainer rc = null;
		Vector datos=new Vector();
		
		try { 
			rc = new RowsContainer(); 
			String sql = "";
			sql = "SELECT "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NIFCIF+" , " +
			" DECODE("+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_COMUNITARIO+",'" + ClsConstants.DB_TRUE + "',"+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_NCOMUNITARIO+","+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_NCOLEGIADO+") AS "+CenColegiadoBean.C_NCOLEGIADO+"," +
			" "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_NCOMUNITARIO+", "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NOMBRE+", "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+","+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2+", "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_FECHANACIMIENTO+", "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_FECHAINCORPORACION+", "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDPERSONA+" , "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDINSTITUCION+", "+
		" (SELECT " + CenEstadoColegialBean.T_NOMBRETABLA+"."+CenEstadoColegialBean.C_IDESTADO +   
			" FROM  "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"  ,  "+CenEstadoColegialBean.T_NOMBRETABLA+"   " + 
			" WHERE "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"."+CenDatosColegialesEstadoBean.C_IDESTADO+" = "+CenEstadoColegialBean.T_NOMBRETABLA+"."+CenEstadoColegialBean.C_IDESTADO+"  " +
			" AND "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"."+CenDatosColegialesEstadoBean.C_IDPERSONA+"= "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDPERSONA+"  " +
			" AND "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"."+CenDatosColegialesEstadoBean.C_IDINSTITUCION+" = "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDINSTITUCION + " " + 
			" AND "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"."+CenDatosColegialesEstadoBean.C_FECHAESTADO+" = (SELECT MAX("+CenDatosColegialesEstadoBean.C_FECHAESTADO+") " + 
			" FROM  "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"   " +
			" WHERE "+CenColegiadoBean.C_IDPERSONA+" = "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDPERSONA+"  " +
			" AND "+CenColegiadoBean.C_IDINSTITUCION+"= "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDINSTITUCION+" AND CEN_DATOSCOLEGIALESESTADO.FECHAESTADO <= SYSDATE)) AS IDESTADOCOLEGIAL, " +
			
			" (SELECT " + UtilidadesMultidioma.getCampoMultidioma(CenEstadoColegialBean.T_NOMBRETABLA+"."+CenEstadoColegialBean.C_DESCRIPCION, idioma) +   
			" FROM  "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"  ,  "+CenEstadoColegialBean.T_NOMBRETABLA+"   " + 
			" WHERE "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"."+CenDatosColegialesEstadoBean.C_IDESTADO+" = "+CenEstadoColegialBean.T_NOMBRETABLA+"."+CenEstadoColegialBean.C_IDESTADO+"  " +
			" AND "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"."+CenDatosColegialesEstadoBean.C_IDPERSONA+"= "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDPERSONA+"  " +
			" AND "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"."+CenDatosColegialesEstadoBean.C_IDINSTITUCION+" = "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDINSTITUCION + " " + 
			" AND "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"."+CenDatosColegialesEstadoBean.C_FECHAESTADO+" = (SELECT MAX("+CenDatosColegialesEstadoBean.C_FECHAESTADO+") " + 
			" FROM  "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"   " +
			" WHERE "+CenColegiadoBean.C_IDPERSONA+" = "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDPERSONA+"  " +
			" AND "+CenColegiadoBean.C_IDINSTITUCION+"= "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDINSTITUCION+" AND CEN_DATOSCOLEGIALESESTADO.FECHAESTADO <= SYSDATE)) " +
			"     || case when "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_COMUNITARIO+" = '"+ClsConstants.DB_TRUE+"' then ' (' || F_SIGA_GETRECURSO_ETIQUETA('censo.consultaDatosColegiacion.literal.comunitario', "+idioma+") || ')' " +
			"        end AS ESTADOCOLEGIAL, " +
			" "+CenColegiadoBean.C_SITUACIONRESIDENTE+", "+
			" (select t.fechaenproduccion "+
			"  from "+CenInstitucionBean.T_NOMBRETABLA+" t "+
            "  where t.idinstitucion="+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+") FECHAPRODUCCION, "+
			" (select MAX("+CenDatosColegialesEstadoBean.C_FECHAESTADO+")"+
            "  from "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+
            "  where "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"."+CenDatosColegialesEstadoBean.C_IDINSTITUCION+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+
            "    and "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"."+CenDatosColegialesEstadoBean.C_IDPERSONA+" = "+ CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" AND CEN_DATOSCOLEGIALESESTADO.FECHAESTADO <= SYSDATE)  FECHAESTADO, "+
            " (select COUNT("+CenDatosColegialesEstadoBean.C_FECHAESTADO+")"+
            "  from "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+
            "  where "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"."+CenDatosColegialesEstadoBean.C_IDINSTITUCION+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+
            "    and "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"."+CenDatosColegialesEstadoBean.C_IDPERSONA+" = "+ CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+") NUMEROESTADO, "+
			"   F_SIGA_OBTENERFECHAACTUALIZ(" + CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDINSTITUCION	+", " + idPersona 	+") FECHAACTUALIZ"+
			" FROM  "+CenPersonaBean.T_NOMBRETABLA+" ,  "+CenClienteBean.T_NOMBRETABLA+" ,  "+CenColegiadoBean.T_NOMBRETABLA+" " +
			" WHERE " +
     		" "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDPERSONA+"= "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" "+
			" AND "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" = "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDPERSONA+" "+
			" AND "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" = "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDINSTITUCION+" ";

// 1
       		String institucionesVisibles = CenVisibilidad.getVisibilidadInstitucion(idInstitucion.toString());
       		sql += " AND "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" IN ("+ institucionesVisibles +") ";
       		sql += " AND "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" = "+idPersona;
			
            // RGG cambio visibilidad
			rc = this.find(sql);
			if (rc!=null) {
            	for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
	                datos.add(fila);
	            }
	        }			
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al recueperar los datos");
		}
		return datos;
	}
	
	public String getTratmiento (Long idPersona, Integer idInstitucion) 
	{
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			Hashtable codigos = new Hashtable();
		    codigos.put(new Integer(1),idPersona.toString());
    		codigos.put(new Integer(2),idInstitucion.toString());
		    String sql = " SELECT " + UtilidadesMultidioma.getCampoMultidiomaSimple("DECODE(T." +CenTratamientoBean.C_IDTRATAMIENTO+",1,'',T."+ CenTratamientoBean.C_DESCRIPCION+")",this.usrbean.getLanguage()) + " AS TRATAMIENTO " +
						   " FROM " + CenTratamientoBean.T_NOMBRETABLA + " T, " + CenClienteBean.T_NOMBRETABLA + " C " +
						  " WHERE " + "C." + CenClienteBean.C_IDPERSONA + " = :1 " + 
						  	" AND "	+ "C." + CenClienteBean.C_IDINSTITUCION + " = :2" +
						  	" AND "	+ "T. " + CenTratamientoBean.C_IDTRATAMIENTO + " = C." + CenClienteBean.C_IDTRATAMIENTO;
		
			rc = this.findBind(sql,codigos);
			if (rc != null) {
				if (rc.size() != 1) 
					return null;

				Row fila = (Row) rc.get(0);
				return UtilidadesHash.getString(fila.getRow(), "TRATAMIENTO");
	        }
		}
		catch (Exception e) {
			return null;
		}
		return null;
	}

	public String getTipoClientePorSexo (Long idPersona, Integer idInstitucion) 
	{
		RowsContainer rc = null;
		try { 
			CenInstitucionAdm adm = new CenInstitucionAdm(this.usrbean);
			Hashtable h = new Hashtable();
			UtilidadesHash.set(h, CenInstitucionBean.C_IDINSTITUCION, idInstitucion);
			Vector v = adm.selectByPK(h);
			Hashtable codigos = new Hashtable();
		    codigos.put(new Integer(1),idPersona.toString());
		    codigos.put(new Integer(2),idInstitucion.toString());
		    codigos.put(new Integer(3),((CenInstitucionBean)v.get(0)).getIdLenguaje());
		    
			rc = new RowsContainer(); 
			String sql = " SELECT F_SIGA_GETTIPOCLIENTE_SEXO (:1,:2,:3) AS TIPO_CLIENTE " +
						   " FROM DUAL ";
		
			rc = this.findBind(sql,codigos);
			if (rc != null) {
				if (rc.size() != 1) 
					return null;

				Row fila = (Row) rc.get(0);
				return UtilidadesHash.getString(fila.getRow(), "TIPO_CLIENTE");
	        }
		}
		catch (Exception e) {
			return null;
		}
		return null;
	}
	
	
	public String getClienteEjercientePorSexo (Long idPersona, Integer idInstitucion) 
	{
		RowsContainer rc = null;
		try { 
			CenInstitucionAdm adm = new CenInstitucionAdm(this.usrbean);
			Hashtable h = new Hashtable();
			UtilidadesHash.set(h, CenInstitucionBean.C_IDINSTITUCION, idInstitucion);
			Vector v = adm.selectByPK(h);
			Hashtable codigos = new Hashtable();
		    codigos.put(new Integer(1),idPersona.toString());
		    codigos.put(new Integer(2),idInstitucion.toString());
		    codigos.put(new Integer(3),((CenInstitucionBean)v.get(0)).getIdLenguaje());
		    
			rc = new RowsContainer(); 
			String sql = " SELECT F_SIGA_GETCLIENEJERCIENTE_SEXO (:1,:2,:3) AS TIPO_CLIENTE_EJERCIENTE " +
						   " FROM DUAL ";
		
			rc = this.findBind(sql,codigos);
			if (rc != null) {
				if (rc.size() != 1) 
					return null;

				Row fila = (Row) rc.get(0);
				return UtilidadesHash.getString(fila.getRow(), "TIPO_CLIENTE_EJERCIENTE");
	        }
		}
		catch (Exception e) {
			return null;
		}
		return null;
	}
	
	/**
	 * Obtiene el estado de una factura
	 * @param idInstitucion
	 * @param idFactura
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public static String getEsLetrado ( String idPersona, String idInstitucion) throws ClsExceptions,SIGAException {
		try {
		    Hashtable codigos = new Hashtable();
		    codigos.put(new Integer(1),idPersona);
		    codigos.put(new Integer(2),idInstitucion);
			String select =	"SELECT F_SIGA_ESLETRADO(:1,:2) as LETRADO FROM DUAL"; 

			RowsContainer rc = new RowsContainer(); 
			if (rc.queryBind(select,codigos)) {
				if (rc.size() != 1) return null;
				Hashtable aux = (Hashtable)((Row) rc.get(0)).getRow();
				String num = UtilidadesHash.getString(aux, "LETRADO");
				return num;
			}
		}
	    catch (Exception e) {
	   		if (e instanceof SIGAException){
	   			throw (SIGAException)e;
	   		}
	   		else {
	   			if (e instanceof ClsExceptions){
	   				throw (ClsExceptions)e;
	   			}
	   			else {
	   				throw new ClsExceptions(e, "Error al obtener el estado de las facturas.");
	   			}
	   		}	
	    }
		return null;
	}
	
	public static String getEsCliente ( String idPersona, String idInstitucion) throws ClsExceptions,SIGAException {
		String salida = "";
	    try {
		    Hashtable codigos = new Hashtable();
		    codigos.put(new Integer(1),idInstitucion);
			codigos.put(new Integer(2),idPersona);
		    String select =	"SELECT IDPERSONA as CLIENTE FROM CEN_CLIENTE WHERE IDINSTITUCION=:1 AND IDPERSONA=:2 "; 
			
			RowsContainer rc = new RowsContainer(); 
			if (rc.queryBind(select,codigos)) {
				if (rc.size() > 0) {
				    salida = "S";
				} else {
				    salida = "N";
				}
			}
		}
	    catch (Exception e) {
	        throw new ClsExceptions(e, "Error al realizar consulta.");
	    }
		return salida;
	}
	
	public Vector getClientePorNombreColegiado(String idInstitucion, String nombreColegiado, String apellidosColegiado)  throws ClsExceptions,SIGAException {
		    
	    Vector salida = new Vector();
	    Hashtable aux = new Hashtable();
		try {
		    Hashtable codigos = new Hashtable();
		    String sql = "SELECT distinct P.IDPERSONA, P.NOMBRE || ' ' || P.APELLIDOS1 || ' ' || P.APELLIDOS2 as NOMCOLEGIADO, "+
		    		" P.NOMBRE as NOMBRECOLEGIADO, "+
		    		" P.APELLIDOS1 || ' ' || P.APELLIDOS2 as APECOLEGIADO, " +
		    		"	DECODE(COL.COMUNITARIO,1,COL.NCOMUNITARIO,COL.NCOLEGIADO) NCOLEGIADO "+
		    	" FROM CEN_PERSONA P, "+
		    		" CEN_CLIENTE C, "+
		    		" CEN_COLEGIADO COL" +
		        " WHERE P.IDPERSONA=C.IDPERSONA" +
		        	" AND C.IDPERSONA=COL.IDPERSONA(+)" +
		        	" AND C.IDINSTITUCION=COL.IDINSTITUCION(+)" +
		        	" AND C.IDINSTITUCION=:1";
		    		codigos.put(new Integer(1),idInstitucion);
		    		if(nombreColegiado!=null && !nombreColegiado.trim().equals("")){
		    			sql +=" AND UPPER(TRIM(P.nombre)) LIKE UPPER(TRIM(:2))";
		    			codigos.put(new Integer(2),"%"+nombreColegiado.trim()+"%");
		    			if(apellidosColegiado!=null && !apellidosColegiado.trim().equals("")){
			        		sql +=" AND UPPER(TRIM(P.APELLIDOS1 || ' ' || P.APELLIDOS2)) LIKE UPPER(TRIM(:3))";
			        		codigos.put(new Integer(3),"%"+apellidosColegiado+"%");
		    			}
		        	}else	if(apellidosColegiado!=null && !apellidosColegiado.trim().equals("")){
		        	
		        		sql +=" AND UPPER(TRIM(P.APELLIDOS1 || ' ' || P.APELLIDOS2)) LIKE UPPER(TRIM(:2))";
		        		codigos.put(new Integer(2),"%"+apellidosColegiado+"%");
		        	}

			RowsContainer rc = new RowsContainer(); 
			if (rc.queryBind(sql,codigos)) {
				for (int i=0 ;i<rc.size();i++){
					aux = (Hashtable)((Row) rc.get(i)).getRow();
					salida.add(aux);
				}
			}
		}
	    catch (Exception e) {
	        throw new ClsExceptions(e,"Error al buscar al cliente por numero de colegiado");
	    }
	    return salida;
	}
	
	public Vector getClientePorNColegiado2(String idInstitucion, String nColegiado)  throws ClsExceptions,SIGAException {
	    
	    Vector salida = new Vector();
	    Hashtable aux = new Hashtable();
	    try {
	   
	    	
		    String sql = "SELECT distinct P.IDPERSONA, P.NOMBRE || ' ' || P.APELLIDOS1 || ' ' || P.APELLIDOS2 as NOMCOLEGIADO, "+
		    		" P.NOMBRE as NOMBRECOLEGIADO, "+
		    		" P.APELLIDOS1 || ' ' || P.APELLIDOS2 as APECOLEGIADO, " +
		    		"	DECODE(COL.COMUNITARIO,1,COL.NCOMUNITARIO,COL.NCOLEGIADO) NCOLEGIADO, "+
		    		" DCE.IDESTADO "+
		    	" FROM CEN_PERSONA P, "+
		    		" CEN_CLIENTE C, "+
		    		" CEN_COLEGIADO COL," +
		    		" CEN_DATOSCOLEGIALESESTADO DCE "+
		        " WHERE P.IDPERSONA=C.IDPERSONA "+
		        	" AND C.IDPERSONA=COL.IDPERSONA "+
		        	" AND C.IDINSTITUCION=COL.IDINSTITUCION "+
		        	" AND DCE.IDINSTITUCION = C.IDINSTITUCION "+
		        	" AND DCE.IDPERSONA = C.IDPERSONA "+
		        	" AND DCE.FECHAESTADO = (SELECT MAX(AUX.FECHAESTADO) FROM CEN_DATOSCOLEGIALESESTADO AUX WHERE AUX.IDINSTITUCION=DCE.IDINSTITUCION AND AUX.IDPERSONA=DCE.IDPERSONA) "+
		        	" AND C.IDINSTITUCION=:1 "+
		        	" AND F_SIGA_CALCULONCOLEGIADO(C.IDINSTITUCION,C.IDPERSONA)=:2";
		    Hashtable codigos = new Hashtable();
		    codigos.put(new Integer(1),idInstitucion);
		    codigos.put(new Integer(2),nColegiado);
		    
			RowsContainer rc = new RowsContainer(); 
				if (rc.queryBind(sql,codigos)) {

					for (int i=0 ;i<rc.size();i++){
						aux = (Hashtable)((Row) rc.get(i)).getRow();
						salida.add(aux);
					}
				}
			
		}

	    catch (Exception e) {
	        throw new ClsExceptions(e,"Error al buscar al cliente por numero de colegiado");
	    }
	    return salida;
	}	
	
	public Vector getClientePorNColegiado(String idInstitucion, String nColegiado)  throws ClsExceptions,SIGAException {
	    
	    Vector salida = new Vector();
	    Hashtable aux = new Hashtable();
	    try {
	   
	    	
		    String sql = "SELECT distinct P.IDPERSONA, P.NOMBRE || ' ' || P.APELLIDOS1 || ' ' || P.APELLIDOS2 as NOMCOLEGIADO, P.NOMBRE as NOMBRECOLEGIADO,P.APELLIDOS1 || ' ' || P.APELLIDOS2 as APECOLEGIADO,"+
		    "	DECODE(COL.COMUNITARIO,1,COL.NCOMUNITARIO,COL.NCOLEGIADO) NCOLEGIADO FROM CEN_PERSONA P, CEN_CLIENTE C, CEN_COLEGIADO COL" +
		        	" WHERE P.IDPERSONA=C.IDPERSONA" +
		        	" AND C.IDPERSONA=COL.IDPERSONA" +
		        	" AND C.IDINSTITUCION=COL.IDINSTITUCION" +
		            " AND (select idestado " +
		        	"       from cen_datoscolegialesestado e2 " +
		        	"        where e2.idinstitucion = C.IDINSTITUCION " +
		        	"          and e2.idpersona = C.IDPERSONA " +
		        	"          and e2.fechaestado = (select max(e.fechaestado) from cen_datoscolegialesestado e where e.idinstitucion=e2.idinstitucion and e.idpersona=e2.idpersona) " +
		        	"          ) not in ('30', '40') " +
		        	" AND C.IDINSTITUCION=:1"+
		        	" AND f_siga_calculoncolegiado(C.IDINSTITUCION,C.IDPERSONA)=:2";
		    Hashtable codigos = new Hashtable();
		    codigos.put(new Integer(1),idInstitucion);
		    codigos.put(new Integer(2),nColegiado);
		    
			RowsContainer rc = new RowsContainer(); 
				if (rc.queryBind(sql,codigos)) {

					for (int i=0 ;i<rc.size();i++){
						aux = (Hashtable)((Row) rc.get(i)).getRow();
						salida.add(aux);
					}
				}
			
		}

	    catch (Exception e) {
	        throw new ClsExceptions(e,"Error al buscar al cliente por numero de colegiado");
	    }
	    return salida;
	}
	public PaginadorBind getClientePorNColegiadoResultado(String idInstitucion, String nColegiado)  throws ClsExceptions,SIGAException {
	    
	    Vector salida = new Vector();
	    Hashtable aux = new Hashtable();
	    try {
		    String sql = "SELECT distinct p.nifcif as NIFCIF, P.IDPERSONA, P.NOMBRE as NOMBRE, P.APELLIDOS1 AS APELLIDOS1,  P.APELLIDOS2 as APELLIDOS2, DECODE(COL.COMUNITARIO,1,COL.NCOMUNITARIO,COL.NCOLEGIADO) as NCOLEGIADO, CEN_CLIENTE.IDINSTITUCION as IDINSTITUCION," +
		    " DECODE("+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_LETRADO+",'1','Letrado',NVL((SELECT " + CenEstadoColegialBean.T_NOMBRETABLA+"."+CenEstadoColegialBean.C_DESCRIPCION +
			" FROM "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+", "+CenEstadoColegialBean.T_NOMBRETABLA+" " + 
			" WHERE "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"."+CenDatosColegialesEstadoBean.C_IDESTADO+" = "+CenEstadoColegialBean.T_NOMBRETABLA+"."+CenEstadoColegialBean.C_IDESTADO+"  " +
			" AND "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"."+CenDatosColegialesEstadoBean.C_IDPERSONA+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+"  " +
			" AND "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"."+CenDatosColegialesEstadoBean.C_IDINSTITUCION+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" " +
			" AND "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"."+CenDatosColegialesEstadoBean.C_FECHAESTADO+" = (SELECT MAX("+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"."+CenDatosColegialesEstadoBean.C_FECHAESTADO+") " + 
			" FROM "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"  " +
			" WHERE "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"."+CenDatosColegialesEstadoBean.C_IDPERSONA+"= "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" " +
			" AND "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"."+CenDatosColegialesEstadoBean.C_IDINSTITUCION+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+"  AND CEN_DATOSCOLEGIALESESTADO.FECHAESTADO <= SYSDATE)),'No Colegiado')) AS ESTADOCOLEGIAL " +
		    		"FROM CEN_PERSONA P, CEN_CLIENTE , CEN_COLEGIADO COL" +
		        	" WHERE P.IDPERSONA=CEN_CLIENTE.IDPERSONA" +
		        	" AND CEN_CLIENTE.IDPERSONA=COL.IDPERSONA" +
		        	" AND CEN_CLIENTE.IDINSTITUCION=COL.IDINSTITUCION" +
		            " AND (select idestado " +
		        	"       from cen_datoscolegialesestado e2 " +
		        	"        where e2.idinstitucion = CEN_CLIENTE.IDINSTITUCION " +
		        	"          and e2.idpersona = CEN_CLIENTE.IDPERSONA " +
		        	"          and e2.fechaestado = (select max(e.fechaestado) from cen_datoscolegialesestado e where e.idinstitucion=e2.idinstitucion and e.idpersona=e2.idpersona) " +
		        	"          ) not in ('30', '40') " +
		        	" AND CEN_CLIENTE.IDINSTITUCION=:1"+
		        	" AND f_siga_calculoncolegiado(CEN_CLIENTE.IDINSTITUCION,CEN_CLIENTE.IDPERSONA)=:2";
		    
		    Hashtable codigos = new Hashtable();
		    codigos.put(new Integer(1),idInstitucion);
		    codigos.put(new Integer(2),nColegiado);
		    
		    PaginadorBind paginador = new PaginadorBind(sql,codigos);				
			int totalRegistros = paginador.getNumeroTotalRegistros();
			
			if (totalRegistros==0){					
				paginador =null;
			}
		    return paginador;
	    }

	    catch (Exception e) {
	        throw new ClsExceptions(e,"Error al buscar al cliente por numero de colegiado");
	    }

	}
	public PaginadorBind getClientePorNombreColegiadoResultado(String idInstitucion, String nombreColegiado, String apellidosColegiado)  throws ClsExceptions,SIGAException {
	    
	    Vector salida = new Vector();
	    Hashtable aux = new Hashtable();
		try {
		    Hashtable codigos = new Hashtable();
		    String sql = "SELECT distinct p.nifcif as NIFCIF, P.IDPERSONA, P.NOMBRE as NOMBRE, P.APELLIDOS1 AS APELLIDOS1,  P.APELLIDOS2 as APELLIDOS2, DECODE(COL.COMUNITARIO,1,COL.NCOMUNITARIO,COL.NCOLEGIADO) as NCOLEGIADO, CEN_CLIENTE.IDINSTITUCION as IDINSTITUCION, " +
			" DECODE("+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_LETRADO+",'1','Letrado',NVL((SELECT " + CenEstadoColegialBean.T_NOMBRETABLA+"."+CenEstadoColegialBean.C_DESCRIPCION +
			" FROM "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+", "+CenEstadoColegialBean.T_NOMBRETABLA+" " + 
			" WHERE "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"."+CenDatosColegialesEstadoBean.C_IDESTADO+" = "+CenEstadoColegialBean.T_NOMBRETABLA+"."+CenEstadoColegialBean.C_IDESTADO+"  " +
			" AND "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"."+CenDatosColegialesEstadoBean.C_IDPERSONA+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+"  " +
			" AND "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"."+CenDatosColegialesEstadoBean.C_IDINSTITUCION+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" " +
			" AND "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"."+CenDatosColegialesEstadoBean.C_FECHAESTADO+" = (SELECT MAX("+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"."+CenDatosColegialesEstadoBean.C_FECHAESTADO+") " + 
			" FROM "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"  " +
			" WHERE "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"."+CenDatosColegialesEstadoBean.C_IDPERSONA+"= "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" " +
			" AND "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+"."+CenDatosColegialesEstadoBean.C_IDINSTITUCION+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+"  AND CEN_DATOSCOLEGIALESESTADO.FECHAESTADO <= SYSDATE)),'No Colegiado')) AS ESTADOCOLEGIAL " +
		    		"FROM CEN_PERSONA P, CEN_CLIENTE, CEN_COLEGIADO COL" +
		        	" WHERE P.IDPERSONA=CEN_CLIENTE.IDPERSONA" +
		        	" AND CEN_CLIENTE.IDPERSONA=COL.IDPERSONA" +
		        	" AND CEN_CLIENTE.IDINSTITUCION=COL.IDINSTITUCION" +
		            " AND (select idestado " +
		        	"       from cen_datoscolegialesestado e2 " +
		        	"        where e2.idinstitucion = CEN_CLIENTE.IDINSTITUCION " +
		        	"          and e2.idpersona = CEN_CLIENTE.IDPERSONA " +
		        	"          and e2.fechaestado = (select max(e.fechaestado) from cen_datoscolegialesestado e where e.idinstitucion=e2.idinstitucion and e.idpersona=e2.idpersona) " +
		        	"          ) not in ('30', '40') " +
		        	" AND CEN_CLIENTE.IDINSTITUCION=:1";
		    		codigos.put(new Integer(1),idInstitucion);
		    		if(nombreColegiado!=null && !nombreColegiado.trim().equals("")){
		    			sql +=" AND UPPER(TRIM(P.nombre)) LIKE UPPER(TRIM(:2))";
		    			codigos.put(new Integer(2),"%"+nombreColegiado+"%");
		    			if(apellidosColegiado!=null && !apellidosColegiado.trim().equals("")){
			        		sql +=" AND UPPER(TRIM(P.APELLIDOS1 || ' ' || P.APELLIDOS2)) LIKE UPPER(TRIM(:3))";
			        		codigos.put(new Integer(3),"%"+apellidosColegiado+"%");
		    			}
		        	}else	if(apellidosColegiado!=null && !apellidosColegiado.trim().equals("")){
		        	
		        		sql +=" AND UPPER(TRIM(P.APELLIDOS1 || ' ' || P.APELLIDOS2)) LIKE UPPER(TRIM(:2))";
		        		codigos.put(new Integer(2),"%"+apellidosColegiado+"%");
		        	}
		    PaginadorBind paginador = new PaginadorBind(sql,codigos);				
			int totalRegistros = paginador.getNumeroTotalRegistros();
			
			if (totalRegistros==0){					
				paginador =null;
			}
		    return paginador;
		}
	    catch (Exception e) {
	        throw new ClsExceptions(e,"Error al buscar al cliente por numero de colegiado");
	    }
	}



	public Vector selectComponentes(String nifcif) throws ClsExceptions, SIGAException {
		Vector v = new Vector();
		RowsContainer rc = null;
       try {
            Hashtable codigos = new Hashtable();
		    codigos.put(new Integer(1),this.usrbean.getLocation());
		    codigos.put(new Integer(2),nifcif.toUpperCase());
		    codigos.put(new Integer(3),this.usrbean.getLocation());
		    codigos.put(new Integer(4),nifcif.toUpperCase());
		    codigos.put(new Integer(5),this.usrbean.getLocation());
		    codigos.put(new Integer(6),nifcif.toUpperCase());
		    codigos.put(new Integer(7),this.usrbean.getLocation());
		    codigos.put(new Integer(8),nifcif.toUpperCase());
            String sql = "SELECT * FROM (";
		    sql=sql + "SELECT 1 AS TIPO, p.nifcif as NIFCIF, c.idpersona AS IDPERSONA, C.IDINSTITUCION AS IDINSTITUCION,";
            sql=sql + "P.NOMBRE AS NOMBRE, P.APELLIDOS1 AS APELLIDOS1, P.APELLIDOS2 AS APELLIDOS2,";
            sql=sql + "f_siga_calculoncolegiado(C.IDINSTITUCION,C.IDPERSONA) AS NCOLEGIADO, ";
            sql=sql + "f_siga_gettipocliente(C.IDPERSONA,C.IDINSTITUCION,sysdate) AS ESTADO ";
            sql=sql + ",1 AS ORDEN ";
            sql=sql + "from cen_persona p, cen_cliente c, cen_colegiado col";
            sql=sql + " where p.idpersona = c.idpersona";
            sql=sql + " and c.idpersona = col.idpersona";
            sql=sql + " and c.idinstitucion = col.idinstitucion";
            sql=sql + " and c.idinstitucion = :1";            
            sql=sql + " and UPPER(p.NIFCIF)=:2";
            
            sql=sql + " union";            
            sql=sql + " select 2 AS TIPO, p.nifcif as NIFCIF, c.idpersona AS IDPERSONA, C.IDINSTITUCION AS IDINSTITUCION,";
            sql=sql + "P.NOMBRE AS NOMBRE, P.APELLIDOS1 AS APELLIDOS1, P.APELLIDOS2 AS APELLIDOS2,";
            sql=sql + "f_siga_calculoncolegiado(C.IDINSTITUCION,C.IDPERSONA) AS NCOLEGIADO, ";
            sql=sql + "f_siga_gettipocliente(C.IDPERSONA,C.IDINSTITUCION,sysdate) AS ESTADO ";
            sql=sql + ",3 AS ORDEN ";
            sql=sql + " from cen_persona p, cen_cliente c, cen_colegiado col";
            sql=sql + " where p.idpersona = c.idpersona";
            sql=sql + " and c.idpersona = col.idpersona";
            sql=sql + " and c.idinstitucion = col.idinstitucion";
            sql=sql + " and c.idinstitucion != :3";            
            sql=sql + " and UPPER(p.NIFCIF)=:4";      
            
            sql=sql + " union";
            sql=sql + " select 3 AS TIPO, p.nifcif as NIFCIF, c.idpersona AS IDPERSONA, C.IDINSTITUCION AS IDINSTITUCION, ";
            sql=sql + " P.NOMBRE AS NOMBRE, P.APELLIDOS1 AS APELLIDOS1, P.APELLIDOS2 AS APELLIDOS2, '' AS NCOLEGIADO, ";
            sql=sql + " 0 AS ESTADO ";
            sql=sql + ",2 AS ORDEN ";
            sql=sql + " from cen_persona p, cen_nocolegiado c" ;
            sql=sql + " where p.idpersona = c.idpersona" ;
            sql=sql + " and c.idinstitucion =:5 and UPPER(p.NIFCIF)=:6";
            
            sql=sql + " union";            
            sql=sql + " select 4 AS TIPO, p.nifcif as NIFCIF, c.idpersona AS IDPERSONA, C.IDINSTITUCION AS IDINSTITUCION, ";
            sql=sql + " P.NOMBRE AS NOMBRE, P.APELLIDOS1 AS APELLIDOS1, P.APELLIDOS2 AS APELLIDOS2, '' AS NCOLEGIADO, ";
            sql=sql + " 0 AS ESTADO ";
            sql=sql + ",4 AS ORDEN ";
            sql=sql + " from cen_persona p, cen_nocolegiado c" ;
            sql=sql + " where p.idpersona = c.idpersona" ;
            sql=sql + " and c.idinstitucion !=:7 and UPPER(p.NIFCIF)=:8";
            
            sql=sql + ") ORDER BY ORDEN";


													
			// RGG cambio para visibilidad
            rc = this.findBind(sql,codigos);
 			if (rc!=null) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
       }
       catch (Exception e) {
       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla cliente.");
       }
       return v;                        
    }
	public Vector selectNotarios(String nifcif) throws ClsExceptions, SIGAException {
		Vector v = new Vector();
		RowsContainer rc = null;
       try {
          
           	Hashtable codigos = new Hashtable();
		    codigos.put(new Integer(1),nifcif.toUpperCase());
		    
		    String sql ="select per.idpersona AS IDPERSONA,per.nombre AS NOMBRE,per.apellidos1 AS APELLIDOS1,per.apellidos2 AS APELLIDOS2,per.nifcif AS NIFCIF,per.idtipoidentificacion AS TIPOIDENT";
            sql=sql + " from cen_persona per";
            sql=sql + " where per.idpersona in(select idpersona from cen_gruposcliente_cliente where idgrupo = 5) and UPPER(per.NIFCIF)=:1";
							
			// RGG cambio para visibilidad
            rc = this.findBind(sql,codigos);
 			if (rc!=null) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
       }
       catch (Exception e) {
       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla cliente.");
       }
       return v;                        
	}
      
     public CenClienteBean updateCaracterCliente (CenClienteBean beanCliente, String caracter) throws ClsExceptions, SIGAException 
	{
		
		
		try {
			
			//Actualizo los campos de la solicitud
			beanCliente.setCaracter(caracter);
	
			
			//Actualizo el cliente
			updateDirect(beanCliente);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al insertar datos en B.D.");
		}
		return beanCliente;
	} 
     
     public String getEstadoColegial (String idPersona, String idInstitucion) throws ClsExceptions,SIGAException {
	    Hashtable codigos = new Hashtable();
	    try {
	        codigos.put(new Integer(1),idPersona);
	        codigos.put(new Integer(2),idInstitucion);
			String select =	"SELECT f_siga_getrecurso(cen_estadocolegial.descripcion,"+this.usrbean.getLanguage()+") ESTADOCOLEGIAL"+
			                " FROM cen_estadocolegial "+
			                " where cen_estadocolegial.idestado=f_siga_gettipocliente(:1,:2,sysdate)";
			

			RowsContainer rc = new RowsContainer(); 
			if (rc.queryBind(select, codigos)) {
				if (rc.size() != 1) return null;
				Hashtable aux = (Hashtable)((Row) rc.get(0)).getRow();
				String num = UtilidadesHash.getString(aux, "ESTADOCOLEGIAL");
				return num;
			}
		}
	    catch (Exception e) {
	   		if (e instanceof SIGAException){
	   			throw (SIGAException)e;
	   		}
	   		else {
	   			if (e instanceof ClsExceptions){
	   				throw (ClsExceptions)e;
	   			}
	   			else {
	   				throw new ClsExceptions(e, "Error al obtener el nombre del estado.");
	   			}
	   		}	
	    }
		return null;
	}
     
     public String getFechaEstadoColegial (String idPersona, String idInstitucion) throws ClsExceptions,SIGAException {
	    Hashtable codigos = new Hashtable();
	    try {
	        codigos.put(new Integer(1),idPersona);
	        codigos.put(new Integer(2),idInstitucion);
	        String select = "select max(fechaestado) as FECHAESTADO FROM CEN_DATOSCOLEGIALESESTADO "+
	        				" WHERE IDPERSONA = :1 "+
	        				" AND IDINSTITUCION = :2 "+ 
	        				" order by fechaestado desc";
			RowsContainer rc = new RowsContainer(); 
			if (rc.queryBind(select, codigos)) {
				Hashtable aux = (Hashtable)((Row) rc.get(0)).getRow();
				String fecha = UtilidadesHash.getString(aux, "FECHAESTADO");
				return fecha;
			}
		}
	    catch (Exception e) {
	   		if (e instanceof SIGAException){
	   			throw (SIGAException)e;
	   		}
	   		else {
	   			if (e instanceof ClsExceptions){
	   				throw (ClsExceptions)e;
	   			}
	   			else {
	   				throw new ClsExceptions(e, "Error al obtener la fecha de estado colegial");
	   			}
	   		}	
	    }
		return null;
 	}

     
  	/** 
  	 * Nos devuelve una persona en funcion de su nifcif y numero colegiado
  	 * @param  colegiado 
  	 * @param  idInstitucion  	
  	 * @param  nifcif  
  	 * @return  Vector - Filas de la tabla seleccionadas  
  	 * @exception  ClsExceptions  En cualquier caso de error
  	 */	  
  	public Vector buscarPersona(String idInstitucion, String nifcif, String colegiado) throws ClsExceptions, SIGAException {
 		
  		Vector v = new Vector();
        	Hashtable codigos = new Hashtable();
        	Hashtable registro = new Hashtable();
        	int contador = 0;
 		RowsContainer rc = null;
 		boolean buscado = false;
        try {

     	   String sql = " SELECT col.idinstitucion, per.idpersona AS IDPERSONA,per.nombre AS NOMBRE,per.apellidos1 AS APELLIDOS1,per.apellidos2 AS APELLIDOS2";
             	  sql+= " FROM cen_colegiado col, cen_persona per";
             	  sql+= " WHERE col.idpersona = per.idpersona";
             	   
             	  contador ++;
             	  codigos.put(new Integer(contador), idInstitucion);
             	   
             	  sql+= " AND col.idinstitucion =:"+contador;
      
             	  if(!colegiado.equals("")){
             		  
 		     	      contador++;
 			      	  codigos.put(new Integer(contador),colegiado);
 			      	   
 			          sql+= " AND LTRIM(DECODE(col.COMUNITARIO,'1',col.NCOMUNITARIO, col.NCOLEGIADO),'0') = LTRIM(:"+contador+",'0') " ;
 			          
 			          buscado= true;
        			  }
 		          if(!nifcif.equals("")){
 		        	  
 			          contador++;
 			           
 			          sql+= " AND "+ComodinBusquedas.prepararSentenciaCompletaBind(nifcif,"per.nifcif",contador,codigos);
 			          
 			          buscado = true;
 		          }
 		          if (buscado == true){
 		          
 		            rc = this.findBind(sql,codigos);
 		 			if (rc!=null) {
 						for (int i = 0; i < rc.size(); i++)	{
 							Row fila = (Row) rc.get(i);
 							registro = fila.getRow();
 							
 							if (registro != null) 
 								v=this.selectByPK(registro);
 						}
 					}
 		          }
        }
        catch (Exception e) {
        	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla cliente.");
        }
        return v;                        
 	}
  	
  	/** 
  	 * Nos devuelve una persona en funcion de su nombre completo (apellido1 apellido2 , nombre)
  	 * @param  nombreCompleto 
  	 * @return  Vector - Filas de la tabla seleccionadas  
  	 * @exception  ClsExceptions  En cualquier caso de error
  	 */	  
  	public Vector buscarPersonaNombre(String nombreCompleto) throws ClsExceptions, SIGAException {
 		
  		Vector v = new Vector();
        	Hashtable codigos = new Hashtable();
        	Hashtable registro = new Hashtable();
        	int contador = 0;
 		RowsContainer rc = null;
        try {

     	   String sql = " SELECT per.idpersona AS IDPERSONA ";
             	  sql+= " FROM cen_cliente cli, cen_persona per";
             	  sql+= " WHERE cli.idpersona = per.idpersona ";
             	  
             	  contador ++;
             	  codigos.put(new Integer(contador), this.usrbean.getLocation());
             	   
             	  sql+= " AND cli.idinstitucion = :"+contador;
             
 	     	      contador++;
 		      	  codigos.put(new Integer(contador),nombreCompleto.toUpperCase());
 		      	   
 		          sql+= " AND UPPER(PER.APELLIDO1 || ' ' PER.APELLIDO2 || ', ' PER.NOMBRE) = :"+contador ;
 		           
  
             rc = this.findBind(sql,codigos);
  			if (rc!=null) {
 				for (int i = 0; i < rc.size(); i++)	{
 					Row fila = (Row) rc.get(i);
 					registro = fila.getRow(); 
 					if (registro != null) 
 						v.add(registro);
 				}
 			}
        }
        catch (Exception e) {
        	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla cliente.");
        }
        return v;                        
 	}
  	
  	/** 
  	 * Nos devuelve una persona en funcion de su nombre DNI
  	 * @param  nombreCompleto 
  	 * @return  Vector - Filas de la tabla seleccionadas  
  	 * @exception  ClsExceptions  En cualquier caso de error
  	 */	  
  	public Vector buscarPersonaNifCif(String nifcif) throws ClsExceptions, SIGAException {
 		
  		Vector v = new Vector();
        	Hashtable codigos = new Hashtable();
        	Hashtable registro = new Hashtable();
        	int contador = 0;
 		RowsContainer rc = null;
 		try {

 			String sql = "SELECT cli.idinstitucion, per.idpersona AS IDPERSONA,per.nombre AS NOMBRE,per.apellidos1 AS APELLIDOS1,per.apellidos2 AS APELLIDOS2";
 			sql+= " FROM cen_cliente cli, cen_persona per";
 			sql+= " WHERE cli.idpersona = per.idpersona ";
 			
 			contador ++;
 			codigos.put(new Integer(contador), this.usrbean.getLocation());   
 			sql+= " AND cli.idinstitucion =:"+contador;
 			
 			contador++;
 			sql+= " AND "+ComodinBusquedas.prepararSentenciaCompletaBind(nifcif,"per.nifcif",contador,codigos);
 		  
 			
 			rc = this.findBind(sql,codigos);
  			if (rc!=null) {
 				for (int i = 0; i < rc.size(); i++)	{
 					Row fila = (Row) rc.get(i);
 					registro = fila.getRow();
 					
 					if (registro != null) 
 						v=this.selectByPK(registro);
 				}
 			}
        }
        catch (Exception e) {
        	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla cliente.");
        }
        return v;                        
 	}
  	
 	
 	/**
 	 * Obtiene las instituciones en las cuales el cliente es colegiado ejerciente
 	 * @param idPersona
 	 * @return
 	 */
 	public Vector getInstitucionesEjerciente(String idPersona, String idInstitucion) throws ClsExceptions, SIGAException {
		
 		Vector v = new Vector();
       	Hashtable codigos = new Hashtable();
       	Hashtable registro = new Hashtable();
       	int contador = 0;
		RowsContainer rc = null;
       try {

    	  String sql = " SELECT IDINSTITUCION ";
            	 sql+= " FROM CEN_DATOSCOLEGIALESESTADO";
            	 contador ++;
            	 codigos.put(new Integer(contador), idPersona);
            	 sql+= " WHERE IDPERSONA = :"+contador+" AND IDESTADO = 20 ";
            	 sql+= " and fechamodificacion = (SELECT max(fechamodificacion) ";
            	 sql+= " FROM CEN_DATOSCOLEGIALESESTADO ";
            	 contador ++;
            	 codigos.put(new Integer(contador), idPersona);
            	 sql+= "  WHERE IDPERSONA = :"+contador+"  ";
            	 sql+= "  and idinstitucion in  ";
            	 sql+= "    (SELECT idinstitucion  ";
            	 sql+= "       FROM CEN_INSTITUCION  ";
            	 contador ++;
            	 codigos.put(new Integer(contador), idInstitucion);
            	 sql+= " START WITH IDINSTITUCION = :"+contador+"  ";
            	 sql+= " CONNECT BY PRIOR IDINSTITUCION = CEN_INST_IDINSTITUCION) )";
                           	  
            rc = this.findBind(sql,codigos);
 			if (rc!=null) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					registro = fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
       }
       catch (Exception e) {
       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla cliente.");
       }
       return v;                        
	}
 	
 	public Vector getTelefonosPaginador(Vector datos){
 		for (int i=0; i<datos.size(); i++){
 			Row row = (Row)datos.get(i);
 			String idInstitucion = (String)row.getRow().get(CenClienteBean.C_IDINSTITUCION);
 			String idPersona = (String)row.getRow().get(CenClienteBean.C_IDPERSONA);
 			
 			Object[] paramIn = new Object[4]; //Parametros de entrada del PL
 			String resultado[] = new String[1]; //Parametros de salida del PL
 			try {
 				// Parametros de entrada del PL
 				paramIn[0] = idInstitucion.toString();
 				paramIn[1] = idPersona.toString();
 				paramIn[2] = "2";
 				paramIn[3] = "11"; // telefono

 				resultado = ClsMngBBDD.callPLFunction("{? = call f_siga_getdireccioncliente(?,?,?,?)}",0,paramIn);
 				if (resultado != null && resultado[0]!=null){
 					row.setValue("TELEFONO", resultado[0]);
 				}else{
 					row.setValue("TELEFONO", "");
 				}

 				paramIn[3] = "13"; // movil
 				resultado = ClsMngBBDD.callPLFunction("{? = call f_siga_getdireccioncliente(?,?,?,?)}",0,paramIn);
 	 			if (resultado != null && resultado[0]!=null){
 					row.setValue("MOVIL", resultado[0]);
 				}else{
 					row.setValue("MOVIL", "");
 				}
 			
 			} catch (Exception e) {
 					System.out.println(e);
 			}
 		}
 		return datos;
 	}
 	
 	public Vector getDatosPersonalesOtraInstitucion(Long idPersona)throws ClsExceptions, SIGAException{
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
		    Hashtable codigos = new Hashtable();
		    //codigos.put(new Integer(1),idInstitucion.toString());
		    codigos.put(new Integer(1),idPersona.toString());
    		
		    String sql = " SELECT "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NOMBRE+","+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+","+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2+","+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NIFCIF+","+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NATURALDE+","+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDESTADOCIVIL+"," +CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_FALLECIDO+","+
			 "  " + CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_CARACTER+","+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_FOTOGRAFIA+" ,  "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_SEXO+" ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_PUBLICIDAD+" ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_GUIAJUDICIAL+" ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_ABONOSBANCO+" ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_CARGOSBANCO+" , "+
			 "  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_ASIENTOCONTABLE+","+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_COMISIONES+" ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDTRATAMIENTO+" ,  "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDPERSONA+" ,  "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_FECHANACIMIENTO+" as "+CenPersonaBean.C_FECHANACIMIENTO+",  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDLENGUAJE+" ,  "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDTIPOIDENTIFICACION+" ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_FECHAALTA+"  " +" ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_NOAPARECERREDABOGACIA+ 
		     ",  TRUNC(f_siga_calculoanios ("+CenPersonaBean.C_FECHANACIMIENTO+", SYSDATE)) AS EDAD  ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_NOENVIARREVISTA+
			 " FROM   " + CenClienteBean.T_NOMBRETABLA + ",  "+CenPersonaBean.T_NOMBRETABLA +
			 " WHERE   " + CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDPERSONA+"  = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+"  " +			 
			 " AND   " + CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDPERSONA+" =:1 "+
			 " order by fechaalta ASC ";

			// RGG cambio para visibilidad
            rc = this.findBind(sql,codigos);
 			if (rc!=null) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error en getDatosPersonales1");
		}
		return v;
	}
 	
 	/**
 	 * Devuelve true si el cliente puede ser gestionado por su propio colegio
 	 * Si no puede ser gestionado por el debera gestionarse desde otro colegio
 	 * @param idPersona
 	 * @return true si coincide el idpersona con idinstitucion
 	 */
 	public boolean getExisteColegioCreador(Long idPersona){
 		String sql = " SELECT cli."+ CenClienteBean.C_IDINSTITUCION + " , cli."+CenClienteBean.C_IDPERSONA +
			 " FROM   " + CenClienteBean.T_NOMBRETABLA + " cli "+
			 " WHERE   cli."+CenClienteBean.C_IDINSTITUCION+"  = substr(cli."+CenClienteBean.C_IDPERSONA+",1,4)  " +			 
			 " AND   cli."+CenClienteBean.C_IDPERSONA+" = " + idPersona.toString();
 			Vector v;
			try {
				v = this.selectGenerico(sql);
				if (v!=null && v.size()>0) {
					return true;
				}else{
					return false;
				}
			} catch (Exception e) {
				return false;
			}
 	}
	
 	/** 
	 * Obtiene los datos colegiales de una persona dependiendo de la institucion
	 * @param  idPersona - identificador de la persona
	 * @param  idInstitucion - identificador de la institucion	
	 * @return  CenColegiadoBean con los datos colegiales  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */		
	public Vector getClientes (String idPersona) throws ClsExceptions, SIGAException{
		Vector clientes = new Vector();
		try {
			Hashtable hash = new Hashtable();
			UtilidadesHash.set(hash, CenClienteBean.C_IDPERSONA, idPersona);
			Vector v = this.select(hash);
			if ((v != null) && (v.size()>0)) {
				for (int i = 0; i < v.size(); i++) {
					clientes.add(((CenClienteBean)v.get(i)).getIdInstitucion());
				}
			}
		}catch (Exception e) {
			throw new ClsExceptions (e, "Error al recuperar los datos");
		}
		return clientes;
	}
	
	public CenClienteBean insertNoColegiadoCenso (HttpServletRequest request, Long idPersona, String institucion, String idTratamiento) throws SIGAException
	{
		
			CenClienteBean auxCli = null;			
			CenPersonaBean auxPer = null; 
			boolean existePersona = false; 
			boolean existeCliente = false; 						
		try	{
		
			CenPersonaAdm admPer = new CenPersonaAdm(this.usrbean);
			
			//pongo en mayusculas el nifcif
				
				existePersona= false;
								 

				/**Busca si existe cliente en la institucion actual**/
				auxCli =  this.existeCliente(idPersona,new Integer(this.usrbean.getLocation()));
				if (auxCli!=null) {
					existeCliente= true;
				}else{				
						/**Busca si no existe el cliente en la instituci�n actual, busca si existe en cualquier colegio y si existe 
						 * se realiza la pregunta si quiere que utilize los mismos datos ya existentes**/
					auxCli = this.existeClienteOtraInstitucion (idPersona,new Integer(institucion));
					if (auxCli!=null){
							CenClienteBean beanCli = new CenClienteBean ();
							beanCli.setExisteDatos(true);
							
							//Se deberia de insertar un registro en cen_clientes para la nueva institucion
							if(idTratamiento != null && !idTratamiento.equals("")){
								beanCli.setIdTratamiento (Integer.parseInt(idTratamiento));
							}else{
								beanCli.setIdTratamiento (auxCli.getIdTratamiento());
							}
							beanCli.setIdInstitucion(new Integer(this.usrbean.getLocation()));
							beanCli.setIdPersona (idPersona);
							beanCli.setFechaAlta ("SYSDATE");
							beanCli.setIdLenguaje (ClsConstants.LENGUAJE_ESP);
							beanCli.setAbonosBanco (ClsConstants.TIPO_CARGO_BANCO);
							beanCli.setCargosBanco (ClsConstants.TIPO_CARGO_BANCO);
							beanCli.setPublicidad (ClsConstants.DB_FALSE);
							beanCli.setExportarFoto("0");
							beanCli.setGuiaJudicial (ClsConstants.DB_FALSE);
							beanCli.setComisiones (ClsConstants.DB_FALSE);					
							beanCli.setCaracter (ClsConstants.TIPO_CARACTER_PUBLICO);
							
							if (!this.insert(beanCli)) {
								throw new SIGAException(this.getError());
							}
							return beanCli;

					}
				}
				return auxCli;
			}
			catch (SIGAException e) {
				auxCli = null;
				return auxCli;
			} catch (NumberFormatException e) {
				auxCli = null;
			return auxCli;
			} catch (ClsExceptions e) {
				auxCli = null;
				return auxCli;
			}	
		
	}
	
	
	/**
	 * mhg - INC_10323_SIGA
	 * Metodo para insertar una Persona/Cliente/NoColegiado si no existen. Tambien creamos una direcci�n para los envios.
	 * @param beanSolic
	 * @param continuar
	 * @return
	 * @throws SIGAException
	 */
	public CenClienteBean insertarNoColegiadoParaEnvio (CenSolicitudIncorporacionBean beanSolic, 
			String continuar, HttpServletRequest request) throws SIGAException
	{
		
		// Controles de adm utilizados
		CenPersonaAdm admPer;
		CenClienteAdm admCliente;
		CenNoColegiadoAdm admNoColegiado;
		CenColegiadoAdm admColegiado;
		CenDireccionesAdm admDirecciones;
		CenSolicitudIncorporacionAdm admSolic;
		
		// Beans utilizados
		CenPersonaBean beanPer = null;
		CenClienteBean beanCli = null;
		CenDireccionesBean beanDir = null;
		CenNoColegiadoBean beanNoCol = null;
		CenColegiadoBean beanCol = null;

		// Otras variables
		int tipoSolicitud;
		
		try {
			// obteniendo los adms
			admPer = new CenPersonaAdm(this.usrbean);
			admCliente = new CenClienteAdm(this.usrbean);
			admNoColegiado = new CenNoColegiadoAdm(this.usrbean);
			admColegiado = new CenColegiadoAdm(this.usrbean);
			admDirecciones = new CenDireccionesAdm(this.usrbean);
			admSolic = new CenSolicitudIncorporacionAdm(this.usrbean);

			// comprobando la existencia de la persona y el cliente
			boolean existePersona = false;
			boolean existeCliente = false;
			boolean existeNoColegiado = false;
			boolean existeColegiado = false;
			
			try {
				
				beanPer = admPer.existePersonaAlta(
						beanSolic.getNumeroIdentificador(),
						beanSolic.getNombre(), beanSolic.getApellido1(),
						beanSolic.getApellido2(), continuar);

				if (beanPer != null) {
					existePersona = true;

					Hashtable hashNoColegiado = new Hashtable();
					hashNoColegiado.put(CenNoColegiadoBean.C_IDINSTITUCION,beanSolic.getIdInstitucion());
					hashNoColegiado.put(CenNoColegiadoBean.C_IDPERSONA,beanPer.getIdPersona());
					Vector vNoColegiados = admNoColegiado.select(hashNoColegiado);
					if (!vNoColegiados.isEmpty()) {
						existeNoColegiado = true;
					}
					beanCol = admColegiado.existeColegiado(beanPer.getIdPersona(), beanSolic.getIdInstitucion());
					if(beanCol != null){
						existeColegiado = true;
					}

					beanCli = this.existeCliente(beanPer.getIdPersona(), beanSolic.getIdInstitucion());
					if (beanCli != null) {
						existeCliente = true;
					}
				}
			} catch (SIGAException e) {
				throw e;
			}

			// obteniendo el tipo de solicitud de alta colegial
			tipoSolicitud = beanSolic.getIdTipoSolicitud().intValue();

			// Tratamiento de la persona
			if (!existePersona) {
				// rellenando los datos a insertar
				beanPer = new CenPersonaBean();
				beanPer.setIdPersona(admPer.getIdPersona(beanSolic
						.getIdInstitucion()));
				beanPer.setNombre(beanSolic.getNombre());
				beanPer.setApellido1(beanSolic.getApellido1());
				beanPer.setApellido2(beanSolic.getApellido2());
				beanPer.setNIFCIF(beanSolic.getNumeroIdentificador());
				beanPer.setNIFCIF(beanPer.getNIFCIF().toUpperCase());
				beanPer.setFechaNacimiento(beanSolic.getFechaNacimiento());
				beanPer.setNaturalDe(beanSolic.getNaturalDe());
				beanPer.setIdTipoIdentificacion(beanSolic
						.getIdTipoIdentificacion());
				beanPer.setIdEstadoCivil(beanSolic.getIdEstadoCivil());
				beanPer.setSexo(beanSolic.getSexo());
				beanPer.setFallecido(ClsConstants.DB_FALSE);

				// insertando la nueva persona
				if (!admPer.insert(beanPer))
					throw new ClsExceptions(admPer.getError());
			} 
			
			if(!existeCliente){
				// creando el bean de cliente
				beanCli = new CenClienteBean();
	
				// rellenando campos para el registro de cliente
				beanCli.setIdTratamiento(beanSolic.getIdTratamiento());
				beanCli.setIdInstitucion(beanSolic.getIdInstitucion());
				beanCli.setIdPersona(beanPer.getIdPersona());
				beanCli.setFechaAlta("SYSDATE");
				beanCli.setIdLenguaje(ClsConstants.LENGUAJE_ESP);
				beanCli.setAbonosBanco(ClsConstants.TIPO_CARGO_BANCO);
				beanCli.setCargosBanco(ClsConstants.TIPO_CARGO_BANCO);
				beanCli.setPublicidad(ClsConstants.DB_FALSE);
				beanCli.setExportarFoto("0");
				beanCli.setGuiaJudicial(ClsConstants.DB_FALSE);
				beanCli.setComisiones(ClsConstants.DB_FALSE);
				beanCli.setCaracter(ClsConstants.TIPO_CARACTER_PUBLICO);
	
				// insertando el nuevo cliente
				if (!this.insert(beanCli))
					throw new SIGAException(this.getError());
	
				// obteniendo el tipo de colegiacion
				int idTipoColegiacion = beanSolic.getIdTipoColegiacion()
						.intValue();
			}
			
			if(!existeNoColegiado && !existeColegiado){
				// Inserto los datos del no colegiado en CenNoColegiado:
				Hashtable hashNoColegiado = new Hashtable();
				hashNoColegiado.put(CenNoColegiadoBean.C_IDINSTITUCION,beanSolic.getIdInstitucion());
				hashNoColegiado.put(CenNoColegiadoBean.C_IDPERSONA,beanPer.getIdPersona());
				//El tipo sera siempre Personal:
				hashNoColegiado.put(CenNoColegiadoBean.C_TIPO,ClsConstants.COMBO_TIPO_PERSONAL);
				//No es sociedad SJ:
				hashNoColegiado.put(CenNoColegiadoBean.C_SOCIEDADSJ,ClsConstants.DB_FALSE);
				hashNoColegiado.put(CenNoColegiadoBean.C_USUMODIFICACION,this.usrbean.getUserName());
				hashNoColegiado.put(CenNoColegiadoBean.C_FECHAMODIFICACION,"SYSDATE");
				
	   		    admNoColegiado.insert(hashNoColegiado);
				
			}

			// //// TRATAMIENTO DE DIRECCIONES //////
			
			Hashtable hashDireccion = new Hashtable();
			if(beanSolic.getIdPersonaTemp() != null && beanSolic.getIdDireccionTemp() != null){
				hashDireccion.put(CenDireccionesBean.C_IDPERSONA,beanSolic.getIdPersonaTemp());
				hashDireccion.put(CenDireccionesBean.C_IDINSTITUCION,beanSolic.getIdInstitucion());
				hashDireccion.put(CenDireccionesBean.C_IDDIRECCION,beanSolic.getIdDireccionTemp());
				Vector vDireccion = admDirecciones.select(hashDireccion);
				if (!vDireccion.isEmpty()) {
					beanDir = (CenDireccionesBean)vDireccion.get(0);
					beanDir.setFechaBaja("SYSDATE");
					admDirecciones.update(beanDir);
				}
			}
			Direccion direccion = new Direccion();
			beanDir = new CenDireccionesBean();

			// rellenando el bean de direcciones
			beanDir.setIdPersona(beanPer.getIdPersona());
			beanDir.setIdInstitucion(beanSolic.getIdInstitucion());
			beanDir.setDomicilio(beanSolic.getDomicilio());
			beanDir.setCodigoPostal(beanSolic.getCodigoPostal());
			beanDir.setTelefono1(beanSolic.getTelefono1());
			beanDir.setTelefono2(beanSolic.getTelefono2());
			beanDir.setMovil(beanSolic.getMovil());
			beanDir.setFax1(beanSolic.getFax1());
			beanDir.setFax2(beanSolic.getFax2());
			beanDir.setCorreoElectronico(beanSolic.getCorreoElectronico());
			beanDir.setIdPais(beanSolic.getIdPais());
			if (beanDir.getIdPais().equals("")) {
				beanDir.setIdPais(ClsConstants.ID_PAIS_ESPANA);
			}
			if (beanDir.getIdPais().equals(ClsConstants.ID_PAIS_ESPANA)) {
				beanDir.setIdProvincia(beanSolic.getIdProvincia());
				beanDir.setIdPoblacion(beanSolic.getIdPoblacion());
			} else {
				beanDir.setIdProvincia("");
				beanDir.setIdPoblacion("");
			}
			beanDir.setPoblacionExtranjera(beanSolic.getPoblacionExtranjera());

			// si es una nueva incorporacion y no existe como cliente ni como
			// persona debe tener como preferente el correo y el mail
			if (!existePersona && !existeCliente)
				beanDir.setPreferente(ClsConstants.TIPO_PREFERENTE_CORREO
						+ ClsConstants.TIPO_PREFERENTE_CORREOELECTRONICO);

			// estableciendo los datos del tipo de direccion guardia
			String tiposDir = "";

			// Se llama a la interfaz Direccion para actualizar una nueva
			// direccion
			
			direccion.insertar(beanDir, tiposDir, "",null,null, this.usrbean);
			
			//actualiza los campos temporales tras insertar
			beanSolic.setIdPersonaTemp(beanDir.getIdPersona());
			beanSolic.setIdDireccionTemp(beanDir.getIdDireccion());
			admSolic.update(beanSolic);
			
		} catch (SIGAException se) {
			throw se;
		} catch (Exception e) {
			throw new SIGAException("Error al insertar datos en B.D.", e);
		}

		return beanCli;
	}
	
	/**aalg: Para poder obtener los datos del No colegiado. INC_10396_SIGA
	 * @param idInstitucion Integer con el id de institucion
	 * @param idPersona Long con el id de persona
	 * @param lenguaje
	 * @return sexo, numero colegiado vac�o, nombre, direccion y telefonos
	 * @throws ClsExceptions
	 */
	public Hashtable obtenerDatosColegiadoONo(boolean colegiado, String idInstitucion, String idPersona, String lenguaje)throws ClsExceptions{
		Hashtable ht=null;
		String lg=lenguaje.toUpperCase();
		String hombre=UtilidadesString.getMensajeIdioma(lg,"censo.sexo.hombre");
		String mujer=UtilidadesString.getMensajeIdioma(lg,"censo.sexo.mujer");
		
		Hashtable codigos = new Hashtable();
		codigos.put(new Integer(1),idInstitucion);
		codigos.put(new Integer(2),idInstitucion);
		codigos.put(new Integer(3),idInstitucion);
        codigos.put(new Integer(4),idPersona);
        String consultaLetrado=
			"select "+
			"decode(p."+CenPersonaBean.C_SEXO+",'H','"+hombre+"','M','"+mujer+"','') SEXO_LETRADO,"+
			(colegiado ? "c." + CenColegiadoBean.C_NCOLEGIADO  : "'' ")+
			" NCOLEGIADO_LETRADO,"+
			"p."+CenPersonaBean.C_APELLIDOS1+"||' '||p."+CenPersonaBean.C_APELLIDOS2+"||', '|| p."+CenPersonaBean.C_NOMBRE+" NOMBRE_LETRADO,"+
			"p."+CenPersonaBean.C_APELLIDOS1+ " APELLIDO1_LETRADO,"+
			"p."+CenPersonaBean.C_APELLIDOS2+ " APELLIDO2_LETRADO,"+
			"p."+CenPersonaBean.C_NOMBRE+ " N_LETRADO,"+
			" F_SIGA_GETRECURSO(tra."+CenTratamientoBean.C_DESCRIPCION +", "+lenguaje+") TRATAMIENTO, "+
			"tra."+CenTratamientoBean.C_IDTRATAMIENTO +" IDTRATAMIENTO,"+
			"d2.*,"+
			"d6."+CenDireccionesBean.C_TELEFONO1+" TELEFONO1_LETRADO,"+
			"d6."+CenDireccionesBean.C_TELEFONO2+" TELEFONO2_LETRADO,"+
			"d6."+CenDireccionesBean.C_MOVIL+" MOVIL_LETRADO,"+
			"d6."+CenDireccionesBean.C_FAX1+" FAX1"+
			" from "+
			CenPersonaBean.T_NOMBRETABLA+" p,"+
			(colegiado ? CenColegiadoBean.T_NOMBRETABLA : CenNoColegiadoBean.T_NOMBRETABLA)+" c,"+
			CenClienteBean.T_NOMBRETABLA+" cl,"+
			CenTratamientoBean.T_NOMBRETABLA+" tra,"+		
			"(select d."+CenDireccionesBean.C_IDPERSONA+", d."+CenDireccionesBean.C_DOMICILIO+"||' '||d."+CenDireccionesBean.C_CODIGOPOSTAL+"||' '||pb."+CenPoblacionesBean.C_NOMBRE+" DIRECCION_LETRADO,"+
			"d.DOMICILIO DIRECCION_LETR," +
			"d.CODIGOPOSTAL CP_LETR," +
			"pb.NOMBRE POBLACION_LETR," +
			"d.poblacionextranjera POBLACION_EXTRANJERA,"+
			"(select pr.nombre from cen_provincias pr where pr.idprovincia=d.idprovincia) PROVINCIA_LETR"+
			"   from "+CenDireccionesBean.T_NOMBRETABLA+" d,"+CenPoblacionesBean.T_NOMBRETABLA+" pb,"+CenDireccionTipoDireccionBean.T_NOMBRETABLA+" td "+
			"  where d."+CenDireccionesBean.C_IDINSTITUCION+"=:1"+
			"    and d."+CenDireccionesBean.C_IDINSTITUCION+"= td."+CenDireccionTipoDireccionBean.C_IDINSTITUCION+
			"    and d."+CenDireccionesBean.C_IDDIRECCION+"= td."+CenDireccionTipoDireccionBean.C_IDDIRECCION+
			"    and d."+CenDireccionesBean.C_IDPERSONA+"= td."+CenDireccionTipoDireccionBean.C_IDPERSONA+
			"    AND d."+CenDireccionesBean.C_FECHABAJA+" is null "+
			"    and td."+CenTipoDireccionBean.C_IDTIPODIRECCION+"=2 "+
			
			"    and d."+CenDireccionesBean.C_IDPOBLACION+"=pb."+CenPoblacionesBean.C_IDPOBLACION+"(+)"+
			"  order by d."+CenDireccionesBean.C_FECHAMODIFICACION+" desc "+
			")    d2,"+
			"(select d."+CenDireccionesBean.C_IDPERSONA+",d."+CenDireccionesBean.C_TELEFONO1+",d."+CenDireccionesBean.C_TELEFONO2+",d."+CenDireccionesBean.C_MOVIL+",d."+CenDireccionesBean.C_FAX1+
			"   from "+CenDireccionesBean.T_NOMBRETABLA+" d,"+CenDireccionTipoDireccionBean.T_NOMBRETABLA+" td "+
			"  where d."+CenDireccionesBean.C_IDINSTITUCION+"=:2"+
			"    and d."+CenDireccionesBean.C_IDINSTITUCION+"= td."+CenDireccionTipoDireccionBean.C_IDINSTITUCION+
			"    and d."+CenDireccionesBean.C_IDDIRECCION+"= td."+CenDireccionTipoDireccionBean.C_IDDIRECCION+
			"    and d."+CenDireccionesBean.C_IDPERSONA+"= td."+CenDireccionTipoDireccionBean.C_IDPERSONA+
			"    AND d."+CenDireccionesBean.C_FECHABAJA+" is null "+
			"    and td."+CenTipoDireccionBean.C_IDTIPODIRECCION+"=6 "+
			"  order by d."+CenDireccionesBean.C_FECHAMODIFICACION+" desc "+
			")    d6 "+
			"where c."+(colegiado ? CenColegiadoBean.C_IDINSTITUCION : CenNoColegiadoBean.C_IDINSTITUCION)+"=:3"+
			"  and c."+(colegiado ? CenColegiadoBean.C_IDINSTITUCION : CenNoColegiadoBean.C_IDINSTITUCION)+"=cl."+CenClienteBean.C_IDINSTITUCION+
			"  and p."+CenPersonaBean.C_IDPERSONA+" = :4"+
			"  and p."+CenPersonaBean.C_IDPERSONA+"=c."+CenNoColegiadoBean.C_IDPERSONA+
			"  and p."+CenPersonaBean.C_IDPERSONA+"=cl."+CenClienteBean.C_IDPERSONA+
			"  and p."+CenPersonaBean.C_IDPERSONA+"=d2."+CenDireccionesBean.C_IDPERSONA+"(+)"+
			"  and p."+CenPersonaBean.C_IDPERSONA+"=d6."+CenDireccionesBean.C_IDPERSONA+"(+)"+
			"  and cl."+ CenClienteBean.C_IDTRATAMIENTO+"= tra."+CenTratamientoBean.C_IDTRATAMIENTO+
			"  and rownum <2";
		
		RowsContainer rc = this.findBind(consultaLetrado,codigos);
		if(rc!=null && rc.size()==1){
			ht=((Row)rc.get(0)).getRow();
		}
		return ht;		
	}
	public Vector getTrabajosSJCSPendientes(Long idPersona,Integer idTurno,String idTurnosMasivos, 
			Integer idGuardia, String fechaBaja,Integer idInstitucion)throws SIGAException{
		
//		try {
			Vector trabajosSJCSPendientes = new Vector();
			if(idTurno!=null){
				trabajosSJCSPendientes.addAll(getGuardiasSJCSPendientes(idPersona,
								idInstitucion, idTurno,
								idGuardia, null, fechaBaja));
			}else if(idTurnosMasivos!=null && !idTurnosMasivos.equals("")){
				GstStringTokenizer st1 = new GstStringTokenizer(idTurnosMasivos,",");

				
				while (st1.hasMoreTokens()) {
					String registro = st1.nextToken();
					String d[]= registro.split("##");
					trabajosSJCSPendientes.addAll(getGuardiasSJCSPendientes(idPersona,
									idInstitucion, Integer.valueOf(d[0]),
									null, null,fechaBaja));
					
				}
					
			}
//			List<Hashtable> trabajosSJCSPendientes = new ArrayList<Hashtable>();
//			ValueKeyVO trabajoSJCSPendientes = null;
//			for (int i = 0; i < guardiasPendientesVector.size(); i++) {
////				Hashtable guardiaPendiente = (Hashtable) guardiasPendientesVector
////						.get(i);
////				trabajoSJCSPendientes = new ValueKeyVO();
////				trabajoSJCSPendientes.setKey("GUARDIA: "+(String) guardiaPendiente.get("NOMBRE"));
////				trabajoSJCSPendientes.setValue((String)guardiaPendiente.get("PERIODO"));
//				trabajosSJCSPendientes.add((Hashtable)guardiasPendientesVector.get(i));
//
//			}
			if(idGuardia==null){
				Vector designacionesPendientesVector = new Vector();
				if(idTurno!=null){
					trabajosSJCSPendientes.addAll(getDesignacionesSJCSPendientes(idPersona,
										idInstitucion, idTurno,
										null,fechaBaja));
				}else if(idTurnosMasivos!=null && !idTurnosMasivos.equals("")){
					GstStringTokenizer st1 = new GstStringTokenizer(idTurnosMasivos,",");
					while (st1.hasMoreTokens()) {
						String registro = st1.nextToken();
						String d[]= registro.split("##");
						trabajosSJCSPendientes.addAll(getDesignacionesSJCSPendientes(idPersona,
											idInstitucion, Integer.valueOf(d[0]),
											null, fechaBaja));
					}
				}
//				for (int i = 0; i < designacionesPendientesVector.size(); i++) {
//					Hashtable designaPendiente = (Hashtable) designacionesPendientesVector
//							.get(i);
//					trabajoSJCSPendientes = new ValueKeyVO();
//					trabajoSJCSPendientes.setKey("DESIGNACI�N");
//					trabajoSJCSPendientes.setValue((String)designaPendiente.get("DESIGNACION"));
//					trabajosSJCSPendientes.add(trabajoSJCSPendientes);
//					trabajosSJCSPendientes.add((Hashtable) designacionesPendientesVector.get(i));
	
//				}
			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		return trabajosSJCSPendientes;
	}
	

}