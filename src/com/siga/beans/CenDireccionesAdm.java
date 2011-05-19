
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.censo.form.DireccionesForm;
import com.siga.general.SIGAException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.UserTransaction;

import com.siga.Utilidades.*;


/**
 * @author nuria.rgonzalez
 * @since 24-11-2004
 * @version raul.gonzalex - cambio visibilidad 
 *   public class CenDireccionesAdm extends MasterBeanAdministrador {
 * @version adrian.ayala - Limpieza general y Adicion del campo idDireccionAlta
 */
public class CenDireccionesAdm extends MasterBeanAdmVisible
{
	//////////////////// CONSTRUCTOR ////////////////////
	/**	
	 * @param usuario
	 */
	public CenDireccionesAdm (UsrBean usuario)
	{
		super (CenDireccionesBean.T_NOMBRETABLA, usuario);
	}
	
	/**
	 * @param tabla
	 * @param usuario
	 * @param userbean
	 * @param idInsitucionClientes
	 * @param idPersonaCliente
	 */
	public CenDireccionesAdm (Integer usuario, UsrBean usrbean, 
							  int idInstitucionCliente, long idPersonaCliente)
	{
		super (CenDireccionesBean.T_NOMBRETABLA, usuario, usrbean, 
				idInstitucionCliente, idPersonaCliente);
	}
	
	
	//////////////////// METODOS DE ADMINISTRADOR ////////////////////
	/**
	 * Devuelve un array con el nombre de los campos de la tabla CEN_DIRECCIONES 
	 */
	protected String[] getCamposBean ()
	{
		String[] campos =
		{
				CenDireccionesBean.C_IDINSTITUCION,	
				CenDireccionesBean.C_IDPERSONA,
				CenDireccionesBean.C_IDDIRECCION,
				CenDireccionesBean.C_DOMICILIO,
				CenDireccionesBean.C_CODIGOPOSTAL,	
				CenDireccionesBean.C_TELEFONO1,
				CenDireccionesBean.C_TELEFONO2,	
				CenDireccionesBean.C_MOVIL,
				CenDireccionesBean.C_FAX1,		
				CenDireccionesBean.C_FAX2,
				CenDireccionesBean.C_CORREOELECTRONICO,	
				CenDireccionesBean.C_PAGINAWEB,
				CenDireccionesBean.C_FECHABAJA,			
				CenDireccionesBean.C_PREFERENTE,
				CenDireccionesBean.C_IDPAIS,		
				CenDireccionesBean.C_IDPROVINCIA,
				CenDireccionesBean.C_IDPOBLACION,
				CenDireccionesBean.C_POBLACIONEXTRANJERA,
				CenDireccionesBean.C_FECHAMODIFICACION,
				CenDireccionesBean.C_USUMODIFICACION,
				CenDireccionesBean.C_IDINSTITUCIONALTA,
				CenDireccionesBean.C_IDDIRECCIONALTA
		};
		return campos;
	} //getCamposBean ()

	/**
	 * Devuelve un array con el nombre de los campos clave de la tabla CEN_DIRECCIONES 
	 */
	public String[] getClavesBean ()
	{
		String[] campos =
		{
				CenDireccionesBean.C_IDINSTITUCION,
				CenDireccionesBean.C_IDPERSONA,
				CenDireccionesBean.C_IDDIRECCION
		};
		return campos;
	} //getClavesBean ()
	
	protected String[] getOrdenCampos ()
	{
		return this.getClavesBean();
	} //getOrdenCampos ()
	
	/**
	 * Devuelve un CenDireccionesBean con los campos de la tabla CEN_DIRECCIONES
	 * @param Hashtable 
	 */
	protected MasterBean hashTableToBean (Hashtable hash)
			throws ClsExceptions
	{
		CenDireccionesBean bean = null;
		
		try {
			bean = new CenDireccionesBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,CenDireccionesBean.C_IDINSTITUCION));
			bean.setIdPersona(UtilidadesHash.getLong(hash,CenDireccionesBean.C_IDPERSONA));
			bean.setIdDireccion(UtilidadesHash.getLong(hash,CenDireccionesBean.C_IDDIRECCION));
			bean.setDomicilio(UtilidadesHash.getString(hash,CenDireccionesBean.C_DOMICILIO));
			bean.setCodigoPostal(UtilidadesHash.getString(hash,CenDireccionesBean.C_CODIGOPOSTAL));
			bean.setTelefono1(UtilidadesHash.getString(hash,CenDireccionesBean.C_TELEFONO1));
			bean.setTelefono2(UtilidadesHash.getString(hash,CenDireccionesBean.C_TELEFONO2));
			bean.setMovil(UtilidadesHash.getString(hash,CenDireccionesBean.C_MOVIL));
			bean.setFax1(UtilidadesHash.getString(hash,CenDireccionesBean.C_FAX1));
			bean.setFax2(UtilidadesHash.getString(hash,CenDireccionesBean.C_FAX2));
			bean.setCorreoElectronico(UtilidadesHash.getString(hash,CenDireccionesBean.C_CORREOELECTRONICO));
			bean.setPaginaweb(UtilidadesHash.getString(hash,CenDireccionesBean.C_PAGINAWEB));
			bean.setFechaBaja(UtilidadesHash.getString(hash,CenDireccionesBean.C_FECHABAJA));
			bean.setPreferente(UtilidadesHash.getString(hash,CenDireccionesBean.C_PREFERENTE));
			bean.setIdPais(UtilidadesHash.getString(hash,CenDireccionesBean.C_IDPAIS));
			bean.setIdProvincia(UtilidadesHash.getString(hash,CenDireccionesBean.C_IDPROVINCIA));
			bean.setIdPoblacion(UtilidadesHash.getString(hash,CenDireccionesBean.C_IDPOBLACION));
			bean.setPoblacionExtranjera(UtilidadesHash.getString(hash,CenDireccionesBean.C_POBLACIONEXTRANJERA));
			bean.setFechaMod(UtilidadesHash.getString(hash,CenDireccionesBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenDireccionesBean.C_USUMODIFICACION));
			bean.setIdInstitucionAlta(UtilidadesHash.getInteger(hash,CenDireccionesBean.C_IDINSTITUCIONALTA));
			bean.setIdDireccionAlta(UtilidadesHash.getLong(hash,CenDireccionesBean.C_IDDIRECCIONALTA));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	} //hashTableToBean ()

	/**
	 * Devuelve un Hashtable con los campos de la tabla CEN_DIRECCIONES 
	 * @param CenDireccionesBean 
	 */
	public Hashtable beanToHashTable (MasterBean bean)
			throws ClsExceptions
	{
		Hashtable hash = null;
		
		try{
			hash = new Hashtable();
			CenDireccionesBean b = (CenDireccionesBean) bean;
			UtilidadesHash.set(hash, CenDireccionesBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(hash, CenDireccionesBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(hash, CenDireccionesBean.C_IDDIRECCION, b.getIdDireccion());			
			UtilidadesHash.set(hash, CenDireccionesBean.C_DOMICILIO, b.getDomicilio());
			UtilidadesHash.set(hash, CenDireccionesBean.C_CODIGOPOSTAL, b.getCodigoPostal());
			UtilidadesHash.set(hash, CenDireccionesBean.C_TELEFONO1, b.getTelefono1());
			UtilidadesHash.set(hash, CenDireccionesBean.C_TELEFONO2, b.getTelefono2());
			UtilidadesHash.set(hash, CenDireccionesBean.C_MOVIL, b.getMovil());
			UtilidadesHash.set(hash, CenDireccionesBean.C_FAX1, b.getFax1());
			UtilidadesHash.set(hash, CenDireccionesBean.C_FAX2, b.getFax2());
			UtilidadesHash.set(hash, CenDireccionesBean.C_CORREOELECTRONICO, b.getCorreoElectronico());
			UtilidadesHash.set(hash, CenDireccionesBean.C_PAGINAWEB, b.getPaginaweb());
			UtilidadesHash.set(hash, CenDireccionesBean.C_FECHABAJA, b.getFechaBaja());
			UtilidadesHash.set(hash, CenDireccionesBean.C_PREFERENTE, b.getPreferente());
			UtilidadesHash.set(hash, CenDireccionesBean.C_IDPAIS, b.getIdPais());
			UtilidadesHash.set(hash, CenDireccionesBean.C_IDPROVINCIA, b.getIdProvincia());
			UtilidadesHash.set(hash, CenDireccionesBean.C_IDPOBLACION, b.getIdPoblacion());
			UtilidadesHash.set(hash, CenDireccionesBean.C_POBLACIONEXTRANJERA, b.getPoblacionExtranjera());
			UtilidadesHash.set(hash, CenDireccionesBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(hash, CenDireccionesBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(hash, CenDireccionesBean.C_IDINSTITUCIONALTA, b.getIdInstitucionAlta());
			UtilidadesHash.set(hash, CenDireccionesBean.C_IDDIRECCIONALTA, b.getIdDireccionAlta());			
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	} //beanToHashTable ()
	
	
	//////////////////// OTROS METODOS ////////////////////
	/**
	 * Devuelve un array con el nombre de los campos de la tablas CEN_DIRECCIONES, CEN_DIRECCION_TIPODIRECCION, CEN_TIPODIRECCION, CEN_POBLACIONES, CEN_PROVINCIA, CEN_PAIS, para construir la Query.
	 * @author nuria.rgonzalez 13-12-04	 
	 */
	protected String[] getCamposDirecciones() throws ClsExceptions {
		String tipoDireccion = "";
		if (this.usrbean!=null && !this.compruebaVisibilidadCampo(CenDireccionTipoDireccionBean.T_NOMBRETABLA,CenDireccionTipoDireccionBean.C_IDTIPODIRECCION)) {
			tipoDireccion = "' ' as \"" + CenTipoDireccionBean.T_NOMBRETABLA +"."+CenTipoDireccionBean.C_DESCRIPCION+"\"";
		} else {
			tipoDireccion = "f_siga_gettiposdireccion(" + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDINSTITUCION + "," + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPERSONA + "," + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDDIRECCION + ", " + this.usrbean.getLanguage() + ") as \"" + CenTipoDireccionBean.T_NOMBRETABLA +"."+CenTipoDireccionBean.C_DESCRIPCION+"\"";
		}
		String[] campos = {	
				CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDINSTITUCION,	
				CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPERSONA,	
				CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDDIRECCION,				
				CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_DOMICILIO,	
				CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_CODIGOPOSTAL,	
				CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_TELEFONO1,		
				CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_TELEFONO2,	
				CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_MOVIL,			
				CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FAX1,						
				CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FAX2,		
				CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_CORREOELECTRONICO,	
				CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_PAGINAWEB,	
				CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHABAJA,			
				CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_PREFERENTE,	
				CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPAIS,		
				CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPROVINCIA,
				CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPOBLACION,
				CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_POBLACIONEXTRANJERA,
				CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHAMODIFICACION,
/////////////////////////////////////////////////////////////////////////////
// DCG Cambios realizamos por el nuevo tratamiento de direcciones  //////////
//				CenDireccionTipoDireccionBean.T_NOMBRETABLA + "." + CenDireccionTipoDireccionBean.C_IDTIPODIRECCION,
//				CenTipoDireccionBean.T_NOMBRETABLA + "." + CenTipoDireccionBean.C_DESCRIPCION + " as TIPODIRECCION",
//				CenTipoDireccionBean.T_NOMBRETABLA + "." + CenTipoDireccionBean.C_DESCRIPCION,
/////////////////////////////////////////////////////////////////////////////
				CenPoblacionesBean.T_NOMBRETABLA + "." + CenPoblacionesBean.C_NOMBRE +" AS POBLACION ", 
				CenProvinciaBean.T_NOMBRETABLA + "." + CenProvinciaBean.C_NOMBRE +" AS PROVINCIA ",
				UtilidadesMultidioma.getCampoMultidiomaSimple(CenPaisBean.T_NOMBRETABLA + "." + CenPaisBean.C_NOMBRE, this.usrbean.getLanguage()) + " AS PAIS", 
				CenPaisBean.T_NOMBRETABLA + "." + CenPaisBean.C_IDPAIS + " AS IDPAIS", 
				tipoDireccion
		};
		return campos;
	}
	
	/**
	 * Devuelve un array con el nombre de las tablas CEN_DIRECCIONES, CEN_DIRECCION_TIPODIRECCION, CEN_TIPODIRECCION, CEN_POBLACIONES, CEN_PROVINCIA, CEN_PAIS y las relaciones entre ellas para construir la Query.
	 * @author nuria.rgonzalez 13-12-04	 
	 */
	protected String getTablasDirecciones(){
		
		String campos = CenDireccionesBean.T_NOMBRETABLA

//////////////////////////////////////////////////////////////////////////		
// DCG cambios para el nuevo tratamiento de direcciones //////////////////
//			+ " INNER JOIN "+ 
//			 CenDireccionTipoDireccionBean.T_NOMBRETABLA +
//			 " ON "+
//			 	CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDINSTITUCION + "=" +
//				CenDireccionTipoDireccionBean.T_NOMBRETABLA + "." + CenDireccionTipoDireccionBean.C_IDINSTITUCION + " AND " +
//				CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPERSONA + "=" +
//				CenDireccionTipoDireccionBean.T_NOMBRETABLA + "." + CenDireccionTipoDireccionBean.C_IDPERSONA + " AND " +
//				CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDDIRECCION + "=" +
//				CenDireccionTipoDireccionBean.T_NOMBRETABLA + "." + CenDireccionTipoDireccionBean.C_IDDIRECCION  
//			 + " INNER JOIN "+ 
//			 CenTipoDireccionBean.T_NOMBRETABLA +
//			 " ON "+
//				CenDireccionTipoDireccionBean.T_NOMBRETABLA +"."+ CenDireccionTipoDireccionBean.C_IDTIPODIRECCION + "=" +
//				CenTipoDireccionBean.T_NOMBRETABLA +"."+ CenTipoDireccionBean.C_IDTIPODIRECCION
//////////////////////////////////////////////////////////////////////////

			+ " LEFT JOIN "+ 
			 CenPoblacionesBean.T_NOMBRETABLA +
			 " ON "+
				 CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPOBLACION + "=" +
				 CenPoblacionesBean.T_NOMBRETABLA +"."+ CenPoblacionesBean.C_IDPOBLACION
			 + " LEFT JOIN "+ 
			 CenProvinciaBean.T_NOMBRETABLA +
			 " ON "+
			 	CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPROVINCIA + "=" +
				CenProvinciaBean.T_NOMBRETABLA +"."+ CenProvinciaBean.C_IDPROVINCIA
				+ " LEFT JOIN "+ 
			 CenPaisBean.T_NOMBRETABLA +
			 " ON "+
				CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPAIS + "=" +
					CenPaisBean.T_NOMBRETABLA +"."+ CenPaisBean.C_IDPAIS;

	/* RGG 17-02-2005 cambio los LEFT JOIN por COMAS para 
	 *  el tratamiento de visibilidad de campos (NO APLICADO)
	 * 		
		
		 + " (+) , "+ 
		 CenPoblacionesBean.T_NOMBRETABLA +
		 " (+) , "+ 
		 CenProvinciaBean.T_NOMBRETABLA +
		 " (+) , "+ 
		 CenPaisBean.T_NOMBRETABLA +
		 // PRIMERA JOIN
		 " WHERE "+
		 CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPOBLACION + "=" +
		 CenPoblacionesBean.T_NOMBRETABLA +"."+ CenPoblacionesBean.C_IDPOBLACION +
		 // SEGUNDA JOIN
		 " AND "+
		 CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPROVINCIA + "=" +
		 CenProvinciaBean.T_NOMBRETABLA +"."+ CenProvinciaBean.C_IDPROVINCIA +
		 // TERCERA JOIN
		 " AND "+
		 CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPAIS + "=" +
		 CenPaisBean.T_NOMBRETABLA +"."+ CenPaisBean.C_IDPAIS;
*/
		
		return campos;
	}
	
	/**
	 * Devuelve un array con el nombre de los campos por los cuales se ordenarán los resultados de la Query.
	 * @author nuria.rgonzalez 13-12-04	 
	 */
	protected String[] getOrdenDirecciones(){
		String[] campos = { CenDireccionTipoDireccionBean.T_NOMBRETABLA +"."+ CenDireccionTipoDireccionBean.C_IDDIRECCION };
		return campos;
	}
	
	/**
	 * Devuelve un vector con los datos de las direcciones del cliente pasado como parámetro 
	 * y además obtenemos todas las cuentas bancarias de las sociedades de las que el cliente es componente. 
	 * @author nuria.rgonzalez 18-01-05
	 * @version 3	 
	 * @param idPersona, es el identificador de la persona de al que vamos a obtener los datos. 
	 * @param idInstitucion, es el identificador de la institucion de la persona de la que vamos a obtener los datos. 
	 */	
	public Vector selectDirecciones (Long idPersona, Integer idInstitucion, boolean bIncluirRegistrosConBajaLogica)  throws ClsExceptions, SIGAException {
		Vector v = null;
		try {
			RowsContainer rc = null;
			String where = " WHERE " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPERSONA + " = " + idPersona +
			  				 " AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDINSTITUCION + " = " + idInstitucion;

			if(!bIncluirRegistrosConBajaLogica) {
				where += " AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHABAJA + " IS NULL";
			}
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.getTablasDirecciones(), this.getCamposDirecciones());
			
			sql += where;
//			sql += UtilidadesBDAdm.sqlOrderBy(this.getOrdenDirecciones()); // DCG cambio de la select de direcciones
			sql += UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos());
			// RGG cambio visibilidad
			rc = this.find(sql);
			if (rc!=null) {
				v = new Vector();
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
		}
		catch(Exception e){
			throw new ClsExceptions (e, "Error en selectDirecciones");
		}
		return v;
	}

	/**
	 * Devuelve un Hastable con los datos de la direccion del cliente pasado como parámetro.
	 * @author nuria.rgonzalez 18-01-05
	 * @version 3	 
	 * @param idPersona, es el identificador de la persona de al que vamos a obtener los datos. 
	 * @param idInstitucion, es el identificador de la institucion de la persona de la que vamos a obtener los datos.
	 * @param idDireccion, es el identificador de la direccion de la que queremos obtener los datos.  
	 */
	public Hashtable selectDirecciones(Long idPersona, Integer idInstitucion, Long idDireccion) throws ClsExceptions, SIGAException {
		return 	selectDirecciones(idPersona, idInstitucion, idDireccion, false);
	}
	
	public Hashtable selectDirecciones(Long idPersona, Integer idInstitucion, Long idDireccion,boolean permitirNulos) throws ClsExceptions, SIGAException {
		Hashtable registro = null;
		RowsContainer rc = null;
		String where = null;

		try {
			where = " WHERE " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPERSONA + " = " + idPersona +
				  	  " AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDINSTITUCION + " = " + idInstitucion +
					  " AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDDIRECCION + " = " + idDireccion;
		
			if (!permitirNulos){
				where += " AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHABAJA + " IS NULL"; 
			}

			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.getTablasDirecciones(), this.getCamposDirecciones());
			
			sql += where;
			sql += UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos());
	
			// RGG cambio visibilidad
			rc = this.find(sql);
			if (rc!=null) {
				if(rc.size()>0){
					Row fila = (Row) rc.get(0);
					registro = (Hashtable)fila.getRow(); 					
				}
			}
		}
		catch(Exception e){
			throw new ClsExceptions (e, "Error en selectDirecciones");
		}
		return registro;
	}
	
	
	/**
	 * Inserta los datos de una direccion y rellena la tabla de historicos (CEN_HISTORICO)
	 * @author daniel.campos 10-01-05
	 * @version 1	 
	 * @param beanDir datos de la direccion.
	 * @param beanTipoDir datos el tipo de la direccion.
	 * @param BeanHis con el motivo y el tipo, para almacenar en el Historico
	 */
	public boolean insertarConHistorico (CenDireccionesBean beanDir, CenDireccionTipoDireccionBean beanTipoDir[], CenHistoricoBean beanHis, String idioma) throws ClsExceptions, SIGAException 
	{
		try {
			// Insertamos la direccion
			beanDir.setIdDireccion(this.getNuevoID(beanDir));
			if (insert(beanDir)) {
				
				boolean error = false;
				
				for (int i = 0; i < beanTipoDir.length; i++) {
					  String no=beanTipoDir[i].getIdTipoDireccion().toString();
					beanTipoDir[i].setIdDireccion(beanDir.getIdDireccion());
					beanTipoDir[i].setIdInstitucion(beanDir.getIdInstitucion());
					beanTipoDir[i].setIdPersona(beanDir.getIdPersona());

					// Insertamos el tipo de direccion
					CenDireccionTipoDireccionAdm admTipoDir = new CenDireccionTipoDireccionAdm(this.usrbean);
					if (admTipoDir.insert(beanTipoDir[i])) {						
					}
					else error = true;
				}

				// si es direccion postal
				if (beanDir.getDomicilio()!=null && !beanDir.getDomicilio().equals("")) { 
					validarRestricciones(beanDir);
				}
				
				if (!error) {
					// Insertamos el historico
					CenHistoricoAdm admHis = new CenHistoricoAdm(this.usrbean);
					if (admHis.insertCompleto(beanHis, beanDir, CenHistoricoAdm.ACCION_INSERT, idioma)) {
						return true;
					}
				}
			}
			return false;
		}
		catch (SIGAException e) {
			throw e;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al insertar datos en B.D.");
		}
	}

	/**
	 * Obtiene un nuevo ID del curriculum de una persona e institucion determinada
	 * @author daniel.campos 12-01-05
	 * @version 1	 
	 * @param Bean datos de la direccion.
	 * @return nuevo ID.
	 */
	public Long getNuevoID (CenDireccionesBean bean)throws ClsExceptions, SIGAException 
	{
		RowsContainer rc = null;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		try {		
			String sql = " SELECT (MAX(" + CenDireccionesBean.C_IDDIRECCION + ") + 1) AS " + CenDireccionesBean.C_IDDIRECCION + 
			  			 " FROM " + CenDireccionesBean.T_NOMBRETABLA + 
						 " WHERE " + CenDireccionesBean.T_NOMBRETABLA +"." + CenDireccionesBean.C_IDPERSONA + " = " + bean.getIdPersona() +
						 " AND " + CenDireccionesBean.T_NOMBRETABLA +"." + CenDireccionesBean.C_IDINSTITUCION + " = " + bean.getIdInstitucion();

			// RGG cambio visibilidad
			rc = this.findForUpdate(sql);
			if (rc!=null) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();
				Long idDireccion = UtilidadesHash.getLong(prueba, CenDireccionesBean.C_IDDIRECCION);
				if (idDireccion == null) {
					return new Long(1);
				}
				else return idDireccion;								
			}
		}	
		catch (Exception e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'getNuevoID' en B.D.");		
		}
		return null;
	}	
	
	/**
	 * Actualiza los datos de una direccion y rellena la tabla de historicos (CEN_HISTORICO)
	 * @author daniel.campos 20-01-05
	 * @version 1	 
	 * @param BeanDir datos de la direccion
	 * @param BeanTipoDir conjunto de tipos de la direccion
	 * @param BeanHis con el motivo y el tipo, para almacenar en el Historico
	 */
	public boolean updateConHistorico (CenDireccionesBean beanDir, CenDireccionTipoDireccionBean beanTipoDir[], CenHistoricoBean beanHis, String idioma) throws ClsExceptions, SIGAException 
	{
		try {
			if (update(beanToHashTable(beanDir), beanDir.getOriginalHash())) {
				
				// Tratamos el tipo de direccion
				boolean error = false;
				
				if (beanTipoDir != null) {
					CenDireccionTipoDireccionAdm admTipoDir = new CenDireccionTipoDireccionAdm(this.usrbean);
	
					// Borramos todos los tipos de esa direccion
					Hashtable claves = new Hashtable();
					UtilidadesHash.set(claves, CenDireccionTipoDireccionBean.C_IDINSTITUCION, beanDir.getIdInstitucion());
					UtilidadesHash.set(claves, CenDireccionTipoDireccionBean.C_IDPERSONA, beanDir.getIdPersona());
					UtilidadesHash.set(claves, CenDireccionTipoDireccionBean.C_IDDIRECCION, beanDir.getIdDireccion());
					Vector v = admTipoDir.selectForUpdate(claves);
					for (int j = 0; j < v.size() && (!error); j++) {
						CenDireccionTipoDireccionBean b = (CenDireccionTipoDireccionBean) v.get(j);
						if (!admTipoDir.delete(b)) {
							error = true;
						}
					}
					
					int numero=beanTipoDir.length;
					// Insertamos los nuevos tipos de esta direccion
					for (int i = 0; ((i < beanTipoDir.length) && (!error)); i++) {
						
						
					  String no=beanTipoDir[i].getIdTipoDireccion().toString();
						beanTipoDir[i].setIdDireccion(beanDir.getIdDireccion());
						beanTipoDir[i].setIdInstitucion(beanDir.getIdInstitucion());
						beanTipoDir[i].setIdPersona(beanDir.getIdPersona());	
						
						// Insertamos el tipo de direccion
						if (!admTipoDir.insert(beanTipoDir[i])) {						
							error = true;
						}
					}
					validarRestricciones (beanDir);
				}
				
				// Insertamos el historico
				if (!error) {
					// Insertamos el historico
					CenHistoricoAdm admHis = new CenHistoricoAdm (this.usrbean);
					if (admHis.insertCompleto(beanHis, beanDir, CenHistoricoAdm.ACCION_UPDATE, idioma)) {
						return true;
					}
				}
			}
			return false;
		}
		
		catch (SIGAException e) { 
			throw e;
		}

		catch (Exception e) {
			throw new ClsExceptions (e, "Error al actualizar los datos en B.D.");
		}
	}
	
	/** 
	 * Recoge la informacion del registro seleccionado a partir de sus claves para <br/>
	 * transacciones (for update)
	 * @param  idPersona - identificador de persona 
	 * @param  idInstitucion - identificador de la institucion
	 * @param  idDireccion - identificador de la direccion
	 * @return  hashtable - Entrada seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Hashtable getEntradaDireccion (String idPersona, String idInstitucion, String idDireccion) throws ClsExceptions, SIGAException {
		   Hashtable datos=new Hashtable();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPERSONA + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDINSTITUCION + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDDIRECCION + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_DOMICILIO + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_CODIGOPOSTAL + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_TELEFONO1 + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_TELEFONO2 + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_MOVIL + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FAX1 + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FAX2 + "," +							
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_CORREOELECTRONICO + "," +
			    			CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_PAGINAWEB + "," +
			    			CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHABAJA + "," +							
			    			CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_PREFERENTE + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPAIS + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPROVINCIA + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPOBLACION + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_POBLACIONEXTRANJERA + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHAMODIFICACION + "," +
	            			CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_USUMODIFICACION +
							" FROM " + CenDireccionesBean.T_NOMBRETABLA + 
							" WHERE " +
							CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPERSONA + "=" + idPersona +
							" AND " +
							CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDINSTITUCION + "=" + idInstitucion +
							" AND " +
							CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDDIRECCION + "=" + idDireccion+
//	          Se vuelve a poner las restriccion de que solo muestre direcciones que no se hayan dado de baja		  
	  		               " AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHABAJA + " IS NULL";
														
	            // RGG cambio visibilidad
	            rc = this.findForUpdate(sql);
	            if (rc!=null) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos=fila.getRow();
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla direcciones.");
	       }
	       return datos;                        
	    }
	
	
	/** 
	 * Recoge la informacion del registro seleccionado esté dado de baja o no a partir de sus claves para <br/>
	 * transacciones (for update)
	 * @param  idPersona - identificador de persona 
	 * @param  idInstitucion - identificador de la institucion
	 * @param  idDireccion - identificador de la direccion
	 * @return  hashtable - Entrada seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Hashtable getEntradaDireccionGeneral (String idPersona, String idInstitucion, String idDireccion) throws ClsExceptions, SIGAException {
		   Hashtable datos=new Hashtable();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPERSONA + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDINSTITUCION + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDDIRECCION + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_DOMICILIO + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_CODIGOPOSTAL + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_TELEFONO1 + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_TELEFONO2 + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_MOVIL + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FAX1 + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FAX2 + "," +							
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_CORREOELECTRONICO + "," +
			    			CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_PAGINAWEB + "," +
			    			CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHABAJA + "," +							
			    			CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_PREFERENTE + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPAIS + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPROVINCIA + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPOBLACION + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_POBLACIONEXTRANJERA + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHAMODIFICACION + "," +
	            			CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_USUMODIFICACION +
							" FROM " + CenDireccionesBean.T_NOMBRETABLA + 
							" WHERE " +
							CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPERSONA + "=" + idPersona +
							" AND " +
							CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDINSTITUCION + "=" + idInstitucion +
							" AND " +
							CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDDIRECCION + "=" + idDireccion;

														
	            // RGG cambio visibilidad
	            rc = this.findForUpdate(sql);
	            if (rc!=null) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos=fila.getRow();
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla direcciones.");
	       }
	       return datos;                        
	    }
	/** 
	 * Recoge la informacion de la direccion especificada en los parametros
	 * @param  idPersona - identificador de persona 
	 * @param  idInstitucion - identificador de la institucion
	 * @param  idDireccion - identificador de la direccion(oficina, correo...)
	 * @return  hashtable - Entrada seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Hashtable getEntradaDireccionEspecifica (String idPersona, String idInstitucion, String idDireccion) throws ClsExceptions, SIGAException {
		   Hashtable datos=new Hashtable();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPERSONA + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDINSTITUCION + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDDIRECCION + "," +
							"nvl("+CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_DOMICILIO + ",'-') DOMICILIO, " +
							"nvl("+CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_CODIGOPOSTAL + ",'-') CODIGOPOSTAL, " +
							"nvl("+CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_TELEFONO1 + ",'-') TELEFONO1, " +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_TELEFONO2 + " TELEFONO2, " +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_MOVIL + " MOVIL, " +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FAX1 + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FAX2 + "," +							
							"nvl("+CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_CORREOELECTRONICO + ",'-') CORREOELECTRONICO," +
			    			CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_PAGINAWEB + "," +
			    			CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHABAJA + "," +							
			    			CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_PREFERENTE + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPAIS + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPROVINCIA + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPOBLACION + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_POBLACIONEXTRANJERA + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHAMODIFICACION + "," +
	            			CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_USUMODIFICACION + "," +
							" (select "+CenPoblacionesBean.C_NOMBRE +
							"  from "+CenPoblacionesBean.T_NOMBRETABLA+
							"  where "+CenPoblacionesBean.C_IDPOBLACION+"="+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDPOBLACION+") POBLACION, "+
							" (select "+CenProvinciaBean.C_NOMBRE +
							"  from "+CenProvinciaBean.T_NOMBRETABLA+
							"  where "+CenProvinciaBean.C_IDPROVINCIA+"="+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDPROVINCIA+") PROVINCIA "+
							" FROM " + CenDireccionesBean.T_NOMBRETABLA +
								" INNER JOIN " + CenDireccionTipoDireccionBean.T_NOMBRETABLA +
									" ON " + CenDireccionTipoDireccionBean.T_NOMBRETABLA + "." + CenDireccionTipoDireccionBean.C_IDINSTITUCION + "=" +
										   CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDINSTITUCION + 
										   " AND " +
										   CenDireccionTipoDireccionBean.T_NOMBRETABLA + "." + CenDireccionTipoDireccionBean.C_IDPERSONA + "=" +
										   CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPERSONA +
										   " AND " +
										   CenDireccionTipoDireccionBean.T_NOMBRETABLA + "." + CenDireccionTipoDireccionBean.C_IDDIRECCION + "=" +
										   CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDDIRECCION ;
										  ;
							try{
							 if ((idDireccion!=null)&&(!idDireccion.equals(""))){
								if ((new Integer(idDireccion)) != null){ 
									sql+=  " AND "+CenDireccionTipoDireccionBean.T_NOMBRETABLA + "." + CenDireccionTipoDireccionBean.C_IDTIPODIRECCION + "=" +idDireccion;
								}	
								else {
									throw new Exception ("");
								}
							 }	
							 	
							}
							catch(Exception e){
								sql+=  " AND upper("+CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_PREFERENTE + ") like upper('%" +idDireccion+"%') ";
							}
							
							sql+=" WHERE " +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDINSTITUCION + " = " + idInstitucion +
							" AND " +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPERSONA + " = " + idPersona +
//							 Se vuelve a poner las restriccion de que solo muestre direcciones que no se hayan dado de baja		  
							" AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHABAJA + " IS NULL"+							
							" AND " +
							" ROWNUM<2 ";
														
	            // RGG cambio visibilidad
	            rc = this.findForUpdate(sql);
	            if (rc!=null) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos=fila.getRow();
	               }
	            } 
	       }
	       catch (ClsExceptions e1) {
	       	//throw new ClsExceptions (e, "Error al obtener la informacion de direcciones: "+e.getLocalizedMessage());
	       	throw new SIGAException("Error al obtener la informacion de direcciones: "+e1.getLocalizedMessage(),e1);
	       }
	       return datos;                        
	    }
	
	/** 
	 * Recoge la informacion de la direccion junto con la poblacion y provincia
	 * @param  idPersona - identificador de persona 
	 * @param  idInstitucion - identificador de la institucion
	 * @param  idDireccion - identificador de la direccion(oficina, correo...)
	 * @return  hashtable - Entrada seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Hashtable getEntradaDireccionEspecificaYUbicacion (String idPersona, String idInstitucion, String idDireccion) throws ClsExceptions, SIGAException {
		   Hashtable datos=new Hashtable();
		  
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPERSONA + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDINSTITUCION + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDDIRECCION + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_DOMICILIO + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_CODIGOPOSTAL + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_TELEFONO1 + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_TELEFONO2 + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_MOVIL + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FAX1 + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FAX2 + "," +							
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_CORREOELECTRONICO + "," +
			    			CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_PAGINAWEB + "," +
			    			CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHABAJA + "," +							
			    			CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_PREFERENTE + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPAIS + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPROVINCIA + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPOBLACION + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_POBLACIONEXTRANJERA + "," +
							CenPoblacionesBean.T_NOMBRETABLA + "." + CenPoblacionesBean.C_NOMBRE + " as POBLACION," +
							CenProvinciaBean.T_NOMBRETABLA + "." + CenProvinciaBean.C_NOMBRE + " as PROVINCIA" +
							" FROM " + CenDireccionesBean.T_NOMBRETABLA +
								" INNER JOIN " + CenDireccionTipoDireccionBean.T_NOMBRETABLA +
									" ON " + CenDireccionTipoDireccionBean.T_NOMBRETABLA + "." + CenDireccionTipoDireccionBean.C_IDINSTITUCION + "=" +
										   CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDINSTITUCION + 
										   " AND " +
										   CenDireccionTipoDireccionBean.T_NOMBRETABLA + "." + CenDireccionTipoDireccionBean.C_IDPERSONA + "=" +
										   CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPERSONA +
										   " AND " +
										   CenDireccionTipoDireccionBean.T_NOMBRETABLA + "." + CenDireccionTipoDireccionBean.C_IDDIRECCION + "=" +
										   CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDDIRECCION +
										   " AND " +
										   CenDireccionTipoDireccionBean.T_NOMBRETABLA + "." + CenDireccionTipoDireccionBean.C_IDTIPODIRECCION + "=" +
										   idDireccion +
								" LEFT JOIN " + CenPoblacionesBean.T_NOMBRETABLA +
									" ON " + CenPoblacionesBean.T_NOMBRETABLA + "." + CenPoblacionesBean.C_IDPOBLACION + "=" +
										   	 CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPOBLACION + 
								" LEFT JOIN " + CenProvinciaBean.T_NOMBRETABLA +
									" ON " + CenProvinciaBean.T_NOMBRETABLA + "." + CenProvinciaBean.C_IDPROVINCIA + "=" +
									   	 	 CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPROVINCIA +
							" WHERE " +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDINSTITUCION + " = " + idInstitucion +
							" AND " +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPERSONA + " = " + idPersona +
//							 Se vuelve a poner las restriccion de que solo muestre direcciones que no se hayan dado de baja		  
							" AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHABAJA + " IS NULL"+							
							" AND " +
							" ROWNUM<2 ";
														
	            // RGG cambio visibilidad
	            rc = this.findForUpdate(sql);
	            if (rc!=null) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos=fila.getRow();
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla direcciones.");
	       }
	       return datos;                        
	    }
	
	
	void validarRestricciones (CenDireccionesBean beanDir) throws SIGAException {
		try {

			// BUSCO SU ESTADO COLEGIAL
			CenColegiadoAdm admCol = new CenColegiadoAdm(this.usrbean);
			CenClienteAdm admCli = new CenClienteAdm(this.usrbean);
			String esLetrado=CenClienteAdm.getEsLetrado(beanDir.getIdPersona().toString(), beanDir.getIdInstitucion().toString());
			
			Hashtable d = admCol.getEstadoColegial (beanDir.getIdPersona(), beanDir.getIdInstitucion());
			if (d == null && esLetrado.equals("0")) {
				// no es colegiado o no tiene estado colegial y ademas no es letrado
				return;
			}
			Integer estado = UtilidadesHash.getInteger(d, CenEstadoColegialBean.C_IDESTADO);

			// QUE EXISTA UNA DIRECCION DE CORREO
			if (this.getNumDirecciones(beanDir, ClsConstants.TIPO_DIRECCION_CORREO) < 1) {
				SIGAException sigaExp = new SIGAException ("messages.censo.direcciones.tipoCorreo");
				throw sigaExp;
			}

			//obtener las colegiaciones del letrado en estado ejerciente
			Vector vInstitucionesEjerciente = admCli.getInstitucionesEjerciente(
					beanDir.getIdPersona().toString(), beanDir.getIdInstitucion().toString());
			boolean tieneColegiacionEjerciente = false;
			if (vInstitucionesEjerciente != null && vInstitucionesEjerciente.size() > 0 ){
				tieneColegiacionEjerciente = true;
			}

			// si es letrado y no tiene alguna colegiación ejerciente,
			// no se comprueba ni la dirección de despacho ni la de guia judicial
			if (!(esLetrado.equals("1") && !tieneColegiacionEjerciente)){
			// SI ES EJERCIENTE O LETRADO QUE EXISTA UNA DIRECCION DE DESPACHO
			if ((((estado!=null && estado.intValue() == ClsConstants.ESTADO_COLEGIAL_EJERCIENTE) || esLetrado.equals("1"))) && 
				(this.getNumDirecciones(beanDir, ClsConstants.TIPO_DIRECCION_DESPACHO) < 1)) {
				SIGAException sigaExp = new SIGAException ("messages.censo.direcciones.tipoDespacho");
				throw sigaExp;
			}
			
			// SI ES EJERCIENTE O LETRADO QUE EXISTA UNA DIRECCION DE GUIA JUDICIAL
			if ((((estado!=null && estado.intValue() == ClsConstants.ESTADO_COLEGIAL_EJERCIENTE)) || esLetrado.equals("1"))&& 
				(this.getNumDirecciones(beanDir, ClsConstants.TIPO_DIRECCION_GUIA) < 1)) {
				SIGAException sigaExp = new SIGAException ("messages.censo.direcciones.tipoGuia");
				throw sigaExp;
			}
		}
		}
		catch (SIGAException e) {
			throw e;
		}
		catch (ClsExceptions e) {
			throw new SIGAException ("Error al validar las restricciones de direcciones");
		}
	}
	
	int getNumDirecciones (CenDireccionesBean beanDir, int tipoDir) throws ClsExceptions, SIGAException {
		
		RowsContainer rc = null;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		try {		
			String sql = " SELECT COUNT(1) AS CANTIDAD" + 
			  			 " FROM " + CenDireccionesBean.T_NOMBRETABLA + ", " + CenDireccionTipoDireccionBean.T_NOMBRETABLA + " " +
						 " WHERE " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDINSTITUCION + " = " + beanDir.getIdInstitucion() +
						 " AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPERSONA + "= " + beanDir.getIdPersona() +
						 " AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDINSTITUCION + " = " + CenDireccionTipoDireccionBean.T_NOMBRETABLA + "." + CenDireccionTipoDireccionBean.C_IDINSTITUCION +  
						 " AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPERSONA + " = " + CenDireccionTipoDireccionBean.T_NOMBRETABLA + "." + CenDireccionTipoDireccionBean.C_IDPERSONA +  
						 " AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDDIRECCION + " = " + CenDireccionTipoDireccionBean.T_NOMBRETABLA + "." + CenDireccionTipoDireccionBean.C_IDDIRECCION+  
						 " AND " + CenDireccionTipoDireccionBean.T_NOMBRETABLA + "." + CenDireccionTipoDireccionBean.C_IDTIPODIRECCION + " = " + tipoDir+
//						 Se vuelve a poner las restriccion de que solo muestre direcciones que no se hayan dado de baja		  
						 " AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHABAJA + " IS NULL";

			// RGG cambio visibilidad
			rc = this.findForUpdate(sql);
			if (rc!=null) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();
				Integer cantidad = UtilidadesHash.getInteger(prueba, "CANTIDAD");
				if (cantidad == null) {
					return 0;
				}
				else 
					return cantidad.intValue();								
			}
		}
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'getNumDirecciones' en B.D.");		
		}
		return 0;
	}
	
	/**
	 * Borrar los datos de una direccion y rellena la tabla de historicos (CEN_HISTORICO)
	 * @author daniel.campos 30-01-05
	 * @version 1	 
	 * @param Hash con las claves de la direccion a borrar
	 * @param BeanHis con el motivo y el tipo, para almacenar en el Historico.
	 */
	public boolean deleteConHistorico (Hashtable clavesDir, CenHistoricoBean beanHis, String idioma, boolean validar) throws ClsExceptions, SIGAException 
	{
		try {
			CenDireccionesBean beanDir = (CenDireccionesBean) this.selectByPK(clavesDir).get(0);

			if (delete(clavesDir)) {
				
				if(validar) validarRestricciones((CenDireccionesBean)hashTableToBean(clavesDir));
				
				CenHistoricoAdm admHis = new CenHistoricoAdm(this.usrbean);
				if (admHis.insertCompleto(beanHis, beanDir, CenHistoricoAdm.ACCION_DELETE, idioma)) {
					return true;
				}
			}
			return false;
		}
		catch (SIGAException e) {
			throw e;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al insertar datos en B.D.");
		}
	}
	

	/**
	 * Borrar los datos de una direccion y rellena la tabla de historicos (CEN_HISTORICO)
	 * @param idPersona 
	 * @param idInstitucion 
	 * @param preferente Valor preferente a insertar
	 * @param request llamada de usuario
	 * @return boolean - true si no existe otro (OK) false en caso contrario
	 */
	public boolean comprobarPreferenteDirecciones (String idPersona, String idInstitucion, String preferente, Long idDireccion, HttpServletRequest request) 
	{
		boolean salida = false;
		int i;
		int j;
		int k;
		UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
		try {
			String valores[] = {"E","F","C","EF","EC","FC","EFC"};
			if (preferente==null || preferente.trim().equals("")) {
				salida = true;
			} else {
				String preferentes[] = new String[preferente.trim().length()];
				for (k=0;k<preferentes.length;k++) {
					preferentes[k] = preferente.substring(k,k+1);
				}
				String criterios = "";
				criterios += " WHERE " + CenDireccionesBean.C_IDINSTITUCION + "=" + idInstitucion;
				criterios += " AND " + CenDireccionesBean.C_IDPERSONA + "=" + idPersona;
//				 Se vuelve a poner las restriccion de que solo muestre direcciones que no se hayan dado de baja		  
				criterios+=  " AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHABAJA + " IS NULL";
				if (idDireccion!=null) {
					criterios += " AND " + CenDireccionesBean.C_IDDIRECCION + "<>" + idDireccion.toString();
				}
				Vector resultados = this.select(criterios);
				if (resultados!=null && resultados.size()>0) {
					for (i=0;i<resultados.size();i++) {
						
						CenDireccionesBean bean = (CenDireccionesBean) resultados.get(i);
						String pref = bean.getPreferente();
						// RGG 14/04/2005 Esta linea la puse a ver si se arregla el error en redAbogacia
						if (pref!=null && !pref.equals("")) 
						
						for (j=0;j<preferentes.length;j++) {
							if (pref.indexOf(preferentes[j])!=-1) {
								String param[] = new String[1];
								if (preferentes[j].equals("E")) {
									param[0] = "censo.preferente.mail";
								} else 
								if (preferentes[j].equals("F")) {
									param[0] = "censo.preferente.fax";
								} else 
								if (preferentes[j].equals("C")) {
									param[0] = "censo.preferente.correo";
								} else
								if (preferentes[j].equals("S")) {
									param[0] = "censo.preferente.sms";
								} 
								throw new SIGAException("messages.censo.direcciones.errorPreferente",null,param);
							}
						}
						
						
						// existe otro
						salida = true;
					}
				} else {
					salida = true;
				}
			}
		}
		catch (SIGAException se) {
			this.setError(se.getMsg(user.getLanguage()));
			salida = false;
		}
		catch (Exception e) { 
			// RGG 14/04/2005 
			//throw new ClsExceptions (e, "Error al insertar datos en B.D.");
			this.setError(e.getLocalizedMessage());
			salida = false;
		}
		return salida;
		
	}
	
	public String obtenerPreferenteDirecciones (String idPersona, String idInstitucion, String preferente, Long idDireccion, HttpServletRequest request) 
	{
		
		String idDirecciones="";
		int i;
		int j;
		int k;
		try {
			if (preferente==null || preferente.trim().equals("")) {
				;
			} else {
				String preferentes[] = new String[preferente.trim().length()];
				for (k=0;k<preferentes.length;k++) {
					preferentes[k] = preferente.substring(k,k+1);
				}
				String criterios = "";
				criterios += " WHERE " + CenDireccionesBean.C_IDINSTITUCION + "=" + idInstitucion;
				criterios += " AND " + CenDireccionesBean.C_IDPERSONA + "=" + idPersona;
//				 Se vuelve a poner las restriccion de que solo muestre direcciones que no se hayan dado de baja		  
				criterios+=  " AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHABAJA + " IS NULL";
				if (idDireccion!=null) {
					criterios += " AND " + CenDireccionesBean.C_IDDIRECCION + "<>" + idDireccion.toString();
				}
				Vector resultados = this.select(criterios);
				if (resultados!=null && resultados.size()>0) {
					for (i=0;i<resultados.size();i++) {
						
						CenDireccionesBean bean = (CenDireccionesBean) resultados.get(i);
						String pref = bean.getPreferente();
						// RGG 14/04/2005 Esta linea la puse a ver si se arregla el error en redAbogacia
						if (pref!=null && !pref.equals("")) 
						
						for (j=0;j<preferentes.length;j++) {
							if (pref.indexOf(preferentes[j])!=-1) {
								String param[] = new String[1];
								if (preferentes[j].equals("E")) {
									param[0] = "censo.preferente.mail";
								} else 
								if (preferentes[j].equals("F")) {
									param[0] = "censo.preferente.fax";
								} else 
								if (preferentes[j].equals("C")) {
									param[0] = "censo.preferente.correo";
								} else
								if (preferentes[j].equals("S")) {
									param[0] = "censo.preferente.sms";
								} 
								idDirecciones+=bean.getIdDireccion().toString()+"@";
							}
						}
						
						
					
						
					}
				} 
			}
		}
		
		
		catch (Exception e) { 
			
			this.setError(e.getLocalizedMessage());
			
		}
		return idDirecciones;
		
	}
	
	public boolean modificarDireccionesPreferentes (Long idPersona, String idInstitucion,  String idDireccion, String preferencia, HttpServletRequest request) throws SIGAException, ClsExceptions 
	{
		boolean salida = false;
		
		Hashtable hDireccion= new Hashtable();
		
		UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");		
		String[] idDir;
		String[] pref;
		String modificarPreferencia="";
		Vector vDir=new Vector();
		
		if (idDireccion == null || idDireccion.equals("")) return salida;
		
		try {
			idDir=idDireccion.split("@");
			pref=preferencia.split("#");
			CenDireccionesAdm direccionesAdm =new CenDireccionesAdm(this.usrbean);
			CenDireccionesBean dirBean=new CenDireccionesBean();
			dirBean.setIdPersona(idPersona);
			dirBean.setIdInstitucion(new Integer(idInstitucion));
			dirBean.setPreferente("");
			CenHistoricoBean beanHis = new CenHistoricoBean ();
			String msg=UtilidadesString.getMensajeIdioma(this.usrbean, "messages.censo.direcciones.modificacionAutoPref");
			beanHis.setMotivo (msg);
			
			for (int i=0; i<idDir.length; i++){
				
				UtilidadesHash.set(hDireccion,CenDireccionesBean.C_IDDIRECCION,idDir[i]);
				UtilidadesHash.set(hDireccion,CenDireccionesBean.C_IDPERSONA,idPersona);
				UtilidadesHash.set(hDireccion,CenDireccionesBean.C_IDINSTITUCION,idInstitucion);
				vDir=direccionesAdm.select(hDireccion);
				if ( (vDir != null) && (vDir.size() > 0) ){
					dirBean = (CenDireccionesBean) vDir.get(0);
					modificarPreferencia=dirBean.getPreferente();
					for(int j=0;j<pref.length; j++){
					  modificarPreferencia= 	UtilidadesString.replaceAllIgnoreCase(modificarPreferencia, pref[j], "");
					 
					}
					dirBean.setPreferente(modificarPreferencia);
				}	
				
				
				if (!direccionesAdm.updateConHistorico(dirBean,null, beanHis,user.getLanguageInstitucion())){
					throw new SIGAException (direccionesAdm.getError ());
				}
				//insertando en la cola de modificacion de datos para Consejos
				CenColaCambioLetradoAdm colaAdm = new CenColaCambioLetradoAdm (this.usrbean);
				if (! colaAdm.insertarCambioEnCola (ClsConstants.COLA_CAMBIO_LETRADO_MODIFICACION_DIRECCION, 
						new Integer(idInstitucion), idPersona, new Long(idDir[i]))){
					throw new SIGAException (colaAdm.getError ());
				}	
			}
			
		}
		catch(SIGAException e){
			throw e;
		}catch(Exception e){
			
			throw new ClsExceptions (e, "Error al actualizar las preferencias de direcciones.");
		}
		return salida;
		
	}
	/** 
	 * Recoge la dirección preferente de correo, o la única direccion de correo.
	 * @param  idPersona - identificador de persona 
	 * @param  idInstitucion - identificador de la institucion
	 * @param  idDireccion - identificador de la direccion
	 * @return  hashtable - Entrada seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Hashtable getDireccionCorreo (String idPersona, String idInstitucion) throws ClsExceptions, SIGAException {
		   Hashtable datos=new Hashtable();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPERSONA + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDINSTITUCION + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDDIRECCION + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_DOMICILIO + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_CODIGOPOSTAL + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_TELEFONO1 + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_TELEFONO2 + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_MOVIL + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FAX1 + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FAX2 + "," +							
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_CORREOELECTRONICO + "," +
			    			CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_PAGINAWEB + "," +
			    			CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHABAJA + "," +							
			    			CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_PREFERENTE + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPAIS + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPROVINCIA + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPOBLACION + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_POBLACIONEXTRANJERA + "," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHAMODIFICACION + "," +
	            			CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_USUMODIFICACION +
							" FROM " + CenDireccionesBean.T_NOMBRETABLA + 
							" WHERE " +
							CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPERSONA + "=" + idPersona +
							" AND " +
							CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDINSTITUCION + "=" + idInstitucion +
							" AND " +
							CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_PREFERENTE + " like '%C%'"+
//							 Se vuelve a poner las restriccion de que solo muestre direcciones que no se hayan dado de baja		  
							" AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHABAJA + " IS NULL";
														
	            // RGG cambio visibilidad
	            rc = this.findForUpdate(sql);
	            if (rc!=null) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos=fila.getRow();
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla direcciones.");
	       }
	       return datos;                        
	    }
	
	/**
	 * Obtiene la direccion de una persona de un tipo determinado. Obtiene la primera que encuentra y si no encuentra para ese tipo devuelve nulo.
	 * @param idPersona
	 * @param idInstitucion
	 * @param idTipoEnvios
	 * @return Bean de direcciones. En cdaso de no encontrarlo devuelve nulo.
	 * @throws ClsExceptions en cualquier caso de error.
	 */
	public CenDireccionesBean obtenerDireccionPorTipo(String idPersona, String idInstitucion, String idTipoEnvios) throws ClsExceptions
	{
		CenDireccionesBean salida = null;
		try {
            
			int intTipo=new Integer(idTipoEnvios).intValue();
			
			String where =" WHERE " + 
			CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPERSONA + "=" + idPersona +
			" AND " +
			CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDINSTITUCION + "=" + idInstitucion +
			" AND " +
			// INC-6205-SIGA jbd 8/7/2009
			/* CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_PREFERENTE + " LIKE '%"+idTipoEnvios+"%'"+
//			 Se vuelve a poner las restriccion de que solo muestre direcciones que no se hayan dado de baja		  
			  " AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHABAJA + " IS NULL";*/
			CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDDIRECCION + "=" + 
			    " F_SIGA_GETDIRECCION("+ idInstitucion +","+ idPersona +","+ intTipo +")";
			
            Vector v = this.select(where);
            if (v!=null && v.size()>0) {
            	// cojo la primera
            	salida = (CenDireccionesBean) v.get(0);
            }
            
			return salida;
/*			
            RowsContainer rc = new RowsContainer(); 
            String sql ="SELECT " +
						CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDPERSONA + "," +
						CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDINSTITUCION + "," +
						CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_IDDIRECCION + 
						" FROM " + CenDireccionesBean.T_NOMBRETABLA + ", " + CenDireccionTipoDireccionBean.T_NOMBRETABLA +  
						" WHERE " +
						CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPERSONA + "=" + CenDireccionTipoDireccionBean.T_NOMBRETABLA +"."+ CenDireccionTipoDireccionBean.C_IDPERSONA + 
						" AND " +
						CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDINSTITUCION + "=" + CenDireccionTipoDireccionBean.T_NOMBRETABLA +"."+ CenDireccionTipoDireccionBean.C_IDINSTITUCION + 
						" AND " +
						CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDDIRECCION + "=" + CenDireccionTipoDireccionBean.T_NOMBRETABLA +"."+ CenDireccionTipoDireccionBean.C_IDDIRECCION + 
						" AND " +
						CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPERSONA + "=" + idPersona +
						" AND " +
						CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDINSTITUCION + "=" + idInstitucion +
						" AND " +
						CenDireccionTipoDireccionBean.T_NOMBRETABLA +"."+ CenDireccionTipoDireccionBean.C_IDTIPODIRECCION + "='"+idTipoEnvios+"'";							
													
            // RGG cambio visibilidad
            rc = this.findForUpdate(sql);
            if (rc!=null) {
               for (int i = 0; i < rc.size(); i++){
                  Row fila = (Row) rc.get(i);
                  datos=fila.getRow();
               }
            } 
*/			

		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al buscar la direccion adecuada segun tipo. ");
		}
		
	}
	/**
	 * Obtiene la primera direccion de una persona y si no encuentra para ese tipo devuelve nulo.
	 * @param idPersona
	 * @param idInstitucion
	 * @return Bean de direcciones. En cdaso de no encontrarlo devuelve nulo.
	 * @throws ClsExceptions en cualquier caso de error.
	 */
	public CenDireccionesBean obtenerDireccionPrimera(String idPersona, String idInstitucion) throws ClsExceptions
	{
		CenDireccionesBean salida = null;
		try {
            
			
			String where =" WHERE " + CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPERSONA + "=" + idPersona +
			" AND " +
			CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDINSTITUCION + "=" + idInstitucion+
//			 Se vuelve a poner las restriccion de que solo muestre direcciones que no se hayan dado de baja		  
			" AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHABAJA + " IS NULL";
			
            Vector v = this.select(where);
            if (v!=null && v.size()>0) {
            	// cojo la primera
            	salida = (CenDireccionesBean) v.get(0);
            }
            
			return salida;

		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al buscar la direccion adecuada segun tipo. ");
		}
		
	}
	
	
	public boolean comprobarDireccionTipoDireccion(String idPersona, String idInstitucion, String idDireccion, DireccionesForm form) throws ClsExceptions
	{
		try {
			
			Hashtable htTipos = new Hashtable();
			htTipos.put(CenDireccionTipoDireccionBean.C_IDINSTITUCION,idInstitucion);
			htTipos.put(CenDireccionTipoDireccionBean.C_IDDIRECCION,idDireccion);
			htTipos.put(CenDireccionTipoDireccionBean.C_IDPERSONA,idPersona);
			CenDireccionTipoDireccionAdm admTipo = new CenDireccionTipoDireccionAdm(this.usrbean);
			boolean postal=false;
			Vector v = admTipo.select(htTipos);
			if (v!=null) {
				for (int i=0;i<v.size();i++) {
					CenDireccionTipoDireccionBean b = (CenDireccionTipoDireccionBean)v.get(i);
					if (b.getIdTipoDireccion().equals(new Integer(2)) ||
					b.getIdTipoDireccion().equals(new Integer(3)) ||
					b.getIdTipoDireccion().equals(new Integer(5))) {
						postal=true;
					}
				}
			}
			
			if (postal) {
				if (
						(form.getDomicilio().trim().equals("")) ||
						(form.getCodigoPostal().trim().equals("")) ||
						(!form.getPais().equals(ClsConstants.ID_PAIS_ESPANA) && (form.getPoblacionExt().trim().equals(""))) ||
						(form.getPais().equals(ClsConstants.ID_PAIS_ESPANA) && (form.getProvincia().trim().equals(""))) ||
						(form.getPais().equals(ClsConstants.ID_PAIS_ESPANA) && (form.getPoblacion().trim().equals(""))) 
					) 
				{
				   return false;
				}
			}
			return true;			
						
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al buscar comprobar si es direccion postal al tener que serlo. ");
		}
	}
	
	
	/* DELETE LOGICO: no se borra fisicamente */
	public boolean delete(Hashtable hash) throws ClsExceptions 
	{
		try {
			Hashtable h = new Hashtable();
			String [] claves = this.getClavesBean();
			for (int i = 0; i < claves.length; i++) {
				h.put (claves[i], hash.get(claves[i]));
			}

			String [] campos = {CenDireccionesBean.C_FECHABAJA};
			UtilidadesHash.set(h, CenDireccionesBean.C_FECHABAJA, "SYSDATE");

			if (this.updateDirect(h, claves, campos)) {
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new ClsExceptions(e, "Error al realizar el \"delete\" en B.D.");
		}
		return false;
	}
	
	/**
	 * Obtiene la direccion del despacho de una persona
	 * @param idPersona
	 * @param idInstitucion
	 * @param idioma
	 * @return Vector con la direccion del despacho
	 * @throws ClsExceptions en cualquier caso de error.
	 */
	public Vector getDireccionDespacho (String idPersona, String idInstitucion, String idioma) throws ClsExceptions
	{
		Vector direccion = new Vector();
		Hashtable codigos = new Hashtable();
		codigos.put(new Integer(1), idInstitucion);
		codigos.put(new Integer(2), idPersona);
		codigos.put(new Integer(3), idioma);
		codigos.put(new Integer(4), idInstitucion);
		codigos.put(new Integer(5), idPersona);
		codigos.put(new Integer(6), idioma);
		String sql = "SELECT" +
					" F_SIGA_GETDIRECCIONDESPACHO(:1,:2,:3,1) AS DOMICILIO_DESPACHO" + 
					", F_SIGA_GETDIRECCIONDESPACHO(:4,:5,:6,3) AS POBLACION_DESPACHO" +
					" FROM DUAL ";
		try{
            RowsContainer rc = this.findBind(sql,codigos);
 			if (rc!=null) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						direccion.add(registro);
				}
			}

		}catch (Exception e) {
			throw new ClsExceptions(e, e.toString());
		}
		return direccion;
	}
	
	public Vector getDireccionPreferente (String idInstitucion, String idPersona, String idTipoEnvio) throws ClsExceptions  
	{
		Vector datos = null;
		try {
			HelperInformesAdm helperInformes = new HelperInformesAdm();
			Hashtable htFuncion = new Hashtable();
			htFuncion.put(new Integer(1), idInstitucion);
			htFuncion.put(new Integer(2), idPersona);
			// jbd inc-6812 Si llamamos a una funcion de envios solo funcionara para direcciones con ese campo
			// Este metodo solo se llama desde los getInformeX (letrado, colegiado y no colegiado)
			//htFuncion.put(new Integer(3), idTipoEnvio);
			//Vector vSalida = helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETDIRECCION", "IDDIRECCIONPREFERENTE");
			htFuncion.put(new Integer(3), "2"); // Preferiblemente la de despacho
			htFuncion.put(new Integer(4), "3"); // Si no la de censo
			Vector vSalida = helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETIDDIRECCION_TIPOPRE2", "IDDIRECCIONPREFERENTE");
			Hashtable registro = (Hashtable) vSalida.get(0);
			String idDireccionPreferente = (String)registro.get("IDDIRECCIONPREFERENTE");
			
			Hashtable htCodigos = new Hashtable();
			
			int keyContador = 0;

				
			
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			sql.append(" SELECT DIR.DOMICILIO,       DIR.CODIGOPOSTAL, ");
			sql.append(" DIR.TELEFONO1,       DIR.TELEFONO2, ");
			sql.append(" DIR.MOVIL,       DIR.FAX1, ");
			sql.append(" DIR.FAX2,       DIR.CORREOELECTRONICO, ");
			sql.append(" DIR.PAGINAWEB,  ");
			sql.append(" DIR.POBLACIONEXTRANJERA, F_SIGA_GETRECURSO(POB.NOMBRE,1) POBLACION, ");
			sql.append(" F_SIGA_GETRECURSO(PRO.NOMBRE,1) PROVINCIA, ");
			sql.append(" F_SIGA_GETRECURSO(PA.NOMBRE,1) PAIS ");
			sql.append(" FROM CEN_DIRECCIONES DIR,CEN_PAIS PA,CEN_POBLACIONES POB,CEN_PROVINCIAS PRO ");
			sql.append(" WHERE   PRO.IDPROVINCIA(+) = DIR.IDPROVINCIA ");
			sql.append(" AND   POB.IDPOBLACION(+) = DIR.IDPOBLACION ");
			sql.append(" AND  PA.IDPAIS(+) = DIR.IDPAIS ");
			sql.append(" AND DIR.IDPERSONA = :");
			sql.append(keyContador);
			keyContador++;
			htCodigos.put(new Integer(keyContador), idPersona);
			sql.append(" AND DIR.IDDIRECCION = :");
			sql.append(keyContador);

			keyContador++;
			htCodigos.put(new Integer(keyContador), idDireccionPreferente);
			sql.append(" AND DIR.IDINSTITUCION = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" AND DIR.FECHABAJA IS NULL ");

			
			datos = helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos); 
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion getDireccionPreferente");
		}
		return datos;
	}

	/**
	 * Metodo que obtiene el idDireccion para una persona en una institucion.
	 * @param idPersona
	 * @param idInstitucion
	 * @param idTipoEnvios
	 * @return
	 * @throws ClsExceptions
	 */
	public String getIdDireccionPorTipo(String idPersona, String idInstitucion, String idTipoEnvios) throws ClsExceptions
	{
		String idDireccion = null;


		Vector direccion = new Vector();
		Hashtable codigos = new Hashtable();
		codigos.put(new Integer(1), idInstitucion);
		codigos.put(new Integer(2), idPersona);
		codigos.put(new Integer(3), idTipoEnvios);
		String sql = "SELECT" +
		" F_SIGA_GETDIRECCION(:1,:2,:3) AS IDDIRECCION" + 
		" FROM DUAL ";
		try{
			RowsContainer rc = this.findBind(sql,codigos);
			if (rc!=null && rc.size()>0) {
				Row fila = (Row) rc.get(0);
				Hashtable registro = (Hashtable)fila.getRow();
				if (registro != null) 
					idDireccion = registro.get("IDDIRECCION").toString();
			}
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al buscar la direccion adecuada segun tipo. ");
		}

		return idDireccion;

	}

	
	public Vector getDireccionEspecificaConTipo(String idInstitucion, String idPersona, String idDireccion, String idTipoEnvio) throws ClsExceptions
	{
		Vector salida = new Vector();
		
		Hashtable codigos=new Hashtable();
		codigos.put(new Integer(1), idTipoEnvio);
		codigos.put(new Integer(2), idInstitucion);
		codigos.put(new Integer(3), idPersona);
		codigos.put(new Integer(4), idDireccion);
		String sql = "select * from cen_direcciones where (select count(*) from cen_direccion_tipodireccion t where cen_direcciones.idinstitucion = t.idinstitucion and   cen_direcciones.idpersona = t.idpersona and   cen_direcciones.iddireccion = t.iddireccion " +
						" and t.idtipodireccion=:1) > 0 and   cen_direcciones.idinstitucion=:2 and   cen_direcciones.idpersona=:3 and   cen_direcciones.iddireccion=:4 ";
		
		try{
			RowsContainer rows = new RowsContainer();
            RowsContainer rc = this.findBind(sql,codigos);
			if (rc!=null) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						salida.add(registro);
				}
			}


		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al buscar la direccion adecuada con un tipo determinado. ");
		}

		return salida;

	}	
	
	
	public String obtenerTipodireccionCensoWeb(String idPersona, String idInstitucion, String tcensoweb, Long idDireccion, HttpServletRequest request) 
	{
		
		String idDirecciones="";
		int i;
		int j;
		int k;
		
		int c;
		int b;
		try {
			if (tcensoweb==null || tcensoweb.trim().equals("")) {
				;
			} else {
				String vcenso[] = new String[tcensoweb.trim().length()];
				for (k=0;k<vcenso.length;k++) {
					vcenso[k] = tcensoweb.substring(k,k+1);
				}
				String criterios = "";
				criterios += " WHERE " + CenDireccionesBean.C_IDINSTITUCION + "=" + idInstitucion;
				criterios += " AND " + CenDireccionesBean.C_IDPERSONA + "=" + idPersona;
//				 Se vuelve a poner las restriccion de que solo muestre direcciones que no se hayan dado de baja		  
				criterios+=  " AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHABAJA + " IS NULL";
				if (idDireccion!=null) {
					criterios += " AND " + CenDireccionesBean.C_IDDIRECCION + "<>" + idDireccion.toString();
				}
				Vector resultados = this.select(criterios);
				String iddirecc="";
				if (resultados!=null && resultados.size()>0) {
					for (i=0;i<resultados.size();i++) {
						
						CenDireccionesBean bean = (CenDireccionesBean) resultados.get(i);
						String pref = bean.getPreferente();
						Long iddireccion= bean.getIdDireccion();								
						
						Hashtable htTipos = new Hashtable();
						htTipos.put(CenDireccionTipoDireccionBean.C_IDINSTITUCION,idInstitucion);
						htTipos.put(CenDireccionTipoDireccionBean.C_IDDIRECCION,iddireccion);
						htTipos.put(CenDireccionTipoDireccionBean.C_IDPERSONA,idPersona);
						boolean postal=false;
						
						CenDireccionTipoDireccionAdm admTipoDir = new CenDireccionTipoDireccionAdm (this.usrbean);
						Vector resultadocensoweb =admTipoDir.select(htTipos);
					
						if (resultadocensoweb!=null && resultadocensoweb.size()>0) {
							for (c=0;c<resultadocensoweb.size();c++) {
								
								CenDireccionTipoDireccionBean beancenso = (CenDireccionTipoDireccionBean) resultadocensoweb.get(c);
								
								String censoweb=beancenso.getIdTipoDireccion().toString();								
								if (censoweb!=null && !censoweb.equals("")) 
						
									for (b=0;b<vcenso.length;b++) {
										if (censoweb.indexOf(vcenso[b])!=-1) {
											String param[] = new String[1];
											if (vcenso[b].equals("3")) {
												param[0] = "censo.tipodireccion.censoweb";
												iddirecc+= beancenso.getIdDireccion().toString()+"@";
											}
											idDirecciones = iddirecc;
									}
								}
						}
					 } 
			   }
		   }
		
		 }
		
		}catch (Exception e) { 
			
			this.setError(e.getLocalizedMessage());
			
		}
		return idDirecciones;
		
	}
	
	public String obtenerTipodireccionFacturacion(String idPersona, String idInstitucion, String tcensoweb, Long idDireccion, HttpServletRequest request) 
	{
		
		String idDirecciones="";
		int i;
		int j;
		int k;
		
		int c;
		int b;
		try {
			if (tcensoweb==null || tcensoweb.trim().equals("")) {
				;
			} else {
				String vcenso[] = new String[tcensoweb.trim().length()];
				for (k=0;k<vcenso.length;k++) {
					vcenso[k] = tcensoweb.substring(k,k+1);
				}
				String criterios = "";
				criterios += " WHERE " + CenDireccionesBean.C_IDINSTITUCION + "=" + idInstitucion;
				criterios += " AND " + CenDireccionesBean.C_IDPERSONA + "=" + idPersona;
//				 Se vuelve a poner las restriccion de que solo muestre direcciones que no se hayan dado de baja		  
				criterios+=  " AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHABAJA + " IS NULL";
				if (idDireccion!=null) {
					criterios += " AND " + CenDireccionesBean.C_IDDIRECCION + "<>" + idDireccion.toString();
				}
				Vector resultados = this.select(criterios);
				String iddirecc="";
				if (resultados!=null && resultados.size()>0) {
					for (i=0;i<resultados.size();i++) {
						
						CenDireccionesBean bean = (CenDireccionesBean) resultados.get(i);
						String pref = bean.getPreferente();
						Long iddireccion= bean.getIdDireccion();								
						
						Hashtable htTipos = new Hashtable();
						htTipos.put(CenDireccionTipoDireccionBean.C_IDINSTITUCION,idInstitucion);
						htTipos.put(CenDireccionTipoDireccionBean.C_IDDIRECCION,iddireccion);
						htTipos.put(CenDireccionTipoDireccionBean.C_IDPERSONA,idPersona);
						boolean postal=false;
						
						CenDireccionTipoDireccionAdm admTipoDir = new CenDireccionTipoDireccionAdm (this.usrbean);
						Vector resultadocensoweb =admTipoDir.select(htTipos);
					
						if (resultadocensoweb!=null && resultadocensoweb.size()>0) {
							for (c=0;c<resultadocensoweb.size();c++) {
								
								CenDireccionTipoDireccionBean beancenso = (CenDireccionTipoDireccionBean) resultadocensoweb.get(c);
								
								String censoweb=beancenso.getIdTipoDireccion().toString();								
								if (censoweb!=null && !censoweb.equals("")) 
						
									for (b=0;b<vcenso.length;b++) {
										if (censoweb.indexOf(vcenso[b])!=-1) {
											String param[] = new String[1];
											if (vcenso[b].equals("8")) {
												param[0] = "censo.tipodireccion.censoweb";
												iddirecc+= beancenso.getIdDireccion().toString()+"@";
											}
											idDirecciones = iddirecc;
									}
								}
						}
					 } 
			   }
		   }
		
		 }
		
		}catch (Exception e) { 
			
			this.setError(e.getLocalizedMessage());
			
		}
		return idDirecciones;
		
	}
	
	public String comprobarTipoDireccion(String tipo, String idInstitucion, String idpersona) 
	{
		
		String sentencia="";
		
		sentencia= "SELECT TIPO."+CenDireccionTipoDireccionBean.C_FECHAMODIFICACION+", " +
						"       TIPO."+CenDireccionTipoDireccionBean.C_IDDIRECCION+", " +
						"       TIPO."+CenDireccionTipoDireccionBean.C_IDINSTITUCION+", " +
						"       TIPO."+CenDireccionTipoDireccionBean.C_IDPERSONA+", " +
						"       TIPO."+CenDireccionTipoDireccionBean.C_IDTIPODIRECCION+", " +
						"       TIPO."+CenDireccionTipoDireccionBean.C_USUMODIFICACION+" " +
						"  FROM "+CenDireccionTipoDireccionBean.T_NOMBRETABLA+" TIPO, " +
						"       "+CenDireccionesBean.T_NOMBRETABLA+" DIR " +
						" WHERE TIPO."+CenDireccionTipoDireccionBean.C_IDTIPODIRECCION+" = "+tipo+" " +
						"   AND TIPO."+CenDireccionTipoDireccionBean.C_IDPERSONA+" = "+idpersona+" " +
						"   AND TIPO."+CenDireccionTipoDireccionBean.C_IDINSTITUCION+" = "+idInstitucion+" " +
						"   AND TIPO."+CenDireccionTipoDireccionBean.C_IDINSTITUCION+" = DIR."+CenDireccionesBean.C_IDINSTITUCION+" " +
						"   AND TIPO."+CenDireccionTipoDireccionBean.C_IDPERSONA+" = DIR."+CenDireccionesBean.C_IDPERSONA+" " +
						"   AND TIPO."+CenDireccionTipoDireccionBean.C_IDDIRECCION+" = DIR."+CenDireccionesBean.C_IDDIRECCION+" " +
						"   AND DIR."+CenDireccionesBean.C_FECHABAJA +" IS NULL";
		 
		
		
		return sentencia;
		
	}

	public void insertarDireccionGuardia(Integer idInstitucion, Long idPersona, String idDireccion, String fax1, String fax2, String movil,
			String telefono1, String telefono2) throws ClsExceptions, SIGAException {
	
		// ACTUALIZAMOS LA DIRECCIÓN DE GUARDIA DEL CLIENTE. EN CASO DE QUE NO EXISTIERA UNA DIRECCIÓN DE GUARDIA
		// ENTONCES LA INSERTAMOS Y SI YA EXISTIA ACTUALIZAMOS LOS DATOS DE LA MISMA
	
		CenDireccionesBean beanDir  = new CenDireccionesBean ();
		beanDir.setFax1(fax1);
		beanDir.setFax2(fax2);
		beanDir.setIdInstitucion(idInstitucion);
		beanDir.setIdPersona(new Long(idPersona));
		beanDir.setMovil(movil);
		beanDir.setTelefono1(telefono1);
		beanDir.setTelefono2(telefono2);
		
		if(idDireccion!=null&&!idDireccion.equals("")){
			// Actualizamos el registro de la dirección de guardia
			beanDir.setIdDireccion(new Long(idDireccion));
			String[] claves ={CenDireccionesBean.C_IDDIRECCION,CenDireccionesBean.C_IDINSTITUCION,CenDireccionesBean.C_IDPERSONA}; 
			String[] campos ={CenDireccionesBean.C_FAX1,CenDireccionesBean.C_FAX2,CenDireccionesBean.C_MOVIL,CenDireccionesBean.C_TELEFONO1,CenDireccionesBean.C_TELEFONO2};
			if (!this.updateDirect(beanDir,claves,campos)) {
				throw new ClsExceptions (this.getError());
			}
		}else{

			//Insertamos la nueva direccion de guardia
			beanDir.setCodigoPostal("");
			beanDir.setCorreoElectronico("");
			beanDir.setDomicilio("");
			beanDir.setFechaBaja("");
			beanDir.setIdPais("");
			beanDir.setIdPoblacion("");
			beanDir.setIdProvincia("");
			beanDir.setPaginaweb("");
			beanDir.setPreferente("");
			try {
				beanDir.setIdDireccion(this.getNuevoID(beanDir));
			} catch (SIGAException e) {
				throw new ClsExceptions (e.getMessage());
			}
			if (!this.insert(beanDir)) {
				throw new ClsExceptions (this.getError());
			}
			// insertamos el tipo de dirección
			CenDireccionTipoDireccionAdm admTipoDir = new CenDireccionTipoDireccionAdm (this.usrbean);
			CenDireccionTipoDireccionBean beanTipoDir=new CenDireccionTipoDireccionBean();
			beanTipoDir.setIdDireccion(beanDir.getIdDireccion());
			beanTipoDir.setIdInstitucion(beanDir.getIdInstitucion());
			beanTipoDir.setIdPersona(beanDir.getIdPersona());
			beanTipoDir.setIdTipoDireccion(new Integer(ClsConstants.TIPO_DIRECCION_GUARDIA));
			if (!admTipoDir.insert(beanTipoDir)){
				throw new ClsExceptions (admTipoDir.getError());
			}
		}
	}
	
}