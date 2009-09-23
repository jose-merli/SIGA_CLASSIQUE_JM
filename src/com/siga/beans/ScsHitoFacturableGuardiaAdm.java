package com.siga.beans;


import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;


/**
 * Implementa las operaciones sobre la base de datos a la tabla SCS_HITOFACTURABLEGUARDIA
 * 
 * @author ruben.fernandez
 * @since 01-11-2004
 * @version adrian.ayala - 18-07-2008: Cambios en hitos de facturacion 
 */
public class ScsHitoFacturableGuardiaAdm extends MasterBeanAdministrador
{
	//////////////////// CONSTRUCTOR ////////////////////
	public ScsHitoFacturableGuardiaAdm (UsrBean usuario)
	{
		super (ScsHitoFacturableGuardiaBean.T_NOMBRETABLA, usuario);
	}
	
	
	
	//////////////////// METODOS DE ADMINISTRADOR ////////////////////
	/**
	 * @return conjunto de datos con los nombres de todos los campos del bean
	 */
	protected String[] getCamposBean ()
	{
		String[] campos =
		{
				ScsHitoFacturableGuardiaBean.C_IDGUARDIA,
				ScsHitoFacturableGuardiaBean.C_IDHITO,
				ScsHitoFacturableGuardiaBean.C_IDINSTITUCION,
				ScsHitoFacturableGuardiaBean.C_IDTURNO,
				ScsHitoFacturableGuardiaBean.C_PAGOFACTURACION,
				ScsHitoFacturableGuardiaBean.C_PRECIOHITO,
				ScsHitoFacturableGuardiaBean.C_PUNTOS,
				ScsHitoFacturableGuardiaBean.C_DIASAPLICABLES,
				ScsHitoFacturableGuardiaBean.C_AGRUPAR,
				ScsHitoFacturableGuardiaBean.C_FECHAMODIFICACION,
				ScsHitoFacturableGuardiaBean.C_USUMODIFICACION
		};
		return campos;
	} //getCamposBean ()
	
	/**
	 * @return conjunto de datos con los nombres de todos los campos
	 * que forman la claves del bean
	 */
	protected String[] getClavesBean ()
	{
		String[] campos =
		{
				ScsHitoFacturableGuardiaBean.C_IDINSTITUCION,
				ScsHitoFacturableGuardiaBean.C_IDTURNO,
				ScsHitoFacturableGuardiaBean.C_IDGUARDIA,
				ScsHitoFacturableGuardiaBean.C_IDHITO,
				ScsHitoFacturableGuardiaBean.C_PAGOFACTURACION
		};
		return campos;
	} //getClavesBean ()
	
	/**
	 * @param hash Hashtable para crear el bean
	 * @return bean con la información de la hashtable
	 */
	protected MasterBean hashTableToBean (Hashtable hash)
			throws ClsExceptions
	{
		ScsHitoFacturableGuardiaBean bean = null;
		try {
			bean = new ScsHitoFacturableGuardiaBean ();
			bean.setIdGuardia		(UtilidadesHash.getInteger	(hash, ScsHitoFacturableGuardiaBean.C_IDGUARDIA));
			bean.setIdHito			(UtilidadesHash.getInteger	(hash, ScsHitoFacturableGuardiaBean.C_IDHITO));
			bean.setIdInstitucion	(UtilidadesHash.getInteger	(hash, ScsHitoFacturableGuardiaBean.C_IDINSTITUCION));
			bean.setIdTurno			(UtilidadesHash.getInteger	(hash, ScsHitoFacturableGuardiaBean.C_IDTURNO));
			bean.setPagoFacturacion	(UtilidadesHash.getString	(hash, ScsHitoFacturableGuardiaBean.C_PAGOFACTURACION));
			bean.setPrecioHito		(UtilidadesHash.getFloat	(hash, ScsHitoFacturableGuardiaBean.C_PRECIOHITO).floatValue ());
			bean.setPuntos			(UtilidadesHash.getInteger	(hash, ScsHitoFacturableGuardiaBean.C_PUNTOS));
			bean.setDiasAplicables	(UtilidadesHash.getString	(hash, ScsHitoFacturableGuardiaBean.C_DIASAPLICABLES));
			bean.setAgrupar			(UtilidadesHash.getBoolean	(hash, ScsHitoFacturableGuardiaBean.C_AGRUPAR).booleanValue ());
			bean.setFechaMod		(UtilidadesHash.getString	(hash, ScsHitoFacturableGuardiaBean.C_FECHAMODIFICACION));
			bean.setUsuMod			(UtilidadesHash.getInteger	(hash, ScsHitoFacturableGuardiaBean.C_USUMODIFICACION));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	} //hashTableToBean ()
	
	/**
	 * @param bean para crear el hashtable asociado
	 * @return hashtable con la información del bean
	 */
	protected Hashtable beanToHashTable (MasterBean bean)
			throws ClsExceptions
	{
		Hashtable hash = null;
		try {
			hash = new Hashtable ();
			ScsHitoFacturableGuardiaBean b = (ScsHitoFacturableGuardiaBean) bean;
			hash.put (ScsHitoFacturableGuardiaBean.C_IDGUARDIA,			String.valueOf (b.getIdInstitucion ()));
			hash.put (ScsHitoFacturableGuardiaBean.C_IDHITO,			String.valueOf (b.getIdHito ()));
			hash.put (ScsHitoFacturableGuardiaBean.C_IDINSTITUCION,		String.valueOf (b.getIdInstitucion ()));
			hash.put (ScsHitoFacturableGuardiaBean.C_IDTURNO,			String.valueOf (b.getIdTurno ()));
			hash.put (ScsHitoFacturableGuardiaBean.C_PAGOFACTURACION,	b.getPagoFacturacion ());
			hash.put (ScsHitoFacturableGuardiaBean.C_PRECIOHITO,		String.valueOf (b.getPrecioHito ()));
			hash.put (ScsHitoFacturableGuardiaBean.C_PUNTOS,			String.valueOf (b.getPuntos ()));
			hash.put (ScsHitoFacturableGuardiaBean.C_DIASAPLICABLES,	b.getDiasAplicables ());
			hash.put (ScsHitoFacturableGuardiaBean.C_AGRUPAR,			b.getAgrupar () ? "1" : "0");
			hash.put (ScsHitoFacturableGuardiaBean.C_FECHAMODIFICACION,	b.getFechaMod ());
			hash.put (ScsHitoFacturableGuardiaBean.C_USUMODIFICACION,	String.valueOf (b.getUsuMod ()));
		}
		catch (Exception e) {
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");
		}
		return null;
	} //beanToHashTable ()
	
	/**
	 * @return String[] conjunto de valores con los campos
	 * por los que se deberá ordenar la select que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenCampos ()
	{
		return null;
	} //getOrdenCampos ()
	
	/**
	 * @param select sentencia "select" sql valida, sin terminar en ";"
	 * @return Vector todos los registros que se seleccionen 
	 * en BBDD debido a la ejecucion de la sentencia select
	 */
	public Vector ejecutaSelect (String select)
			throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		//Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer (); 
			if (rc.query (select)) {
				for (int i=0; i < rc.size(); i++) {
					Row fila = (Row) rc.get (i);
					Hashtable registro = (Hashtable) fila.getRow (); 
					if (registro != null)
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	} //ejecutaSelect ()
	
}