/**
 * VERSIONES:
 * 
 * jose.barrientos	- 28-11-2008 - Creacion
 *	
 */

package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;

public class PysAnticipoLetradoAdm extends MasterBeanAdministrador {
	
	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public PysAnticipoLetradoAdm(UsrBean usu) {
		super(PysAnticipoLetradoBean.T_NOMBRETABLA, usu);
	}
	
	/** 
	 *  Funcion que devuelve los campos de la tabla.
	 * @return  String[] Los campos de la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {		
			PysAnticipoLetradoBean.C_IDINSTITUCION,
			PysAnticipoLetradoBean.C_IDPERSONA,
			PysAnticipoLetradoBean.C_IDANTICIPO,
			PysAnticipoLetradoBean.C_DESCRIPCION,
			PysAnticipoLetradoBean.C_FECHA,
			PysAnticipoLetradoBean.C_IMPORTEINICIAL,
			PysAnticipoLetradoBean.C_CTACONTABLE,
			PysAnticipoLetradoBean.C_USUMODIFICACION,
			PysAnticipoLetradoBean.C_FECHAMODIFICACION};
		return campos;
	}

	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {
			PysAnticipoLetradoBean.C_IDINSTITUCION, 
			PysAnticipoLetradoBean.C_IDPERSONA, 
			PysAnticipoLetradoBean.C_IDANTICIPO};
		return claves;
	}

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysAnticipoLetradoBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		PysAnticipoLetradoBean bean = null;
		
		try {
			bean = new PysAnticipoLetradoBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,PysAnticipoLetradoBean.C_IDINSTITUCION));
			bean.setIdPersona(UtilidadesHash.getLong(hash,PysAnticipoLetradoBean.C_IDPERSONA));			
			bean.setIdAnticipo(UtilidadesHash.getInteger(hash,PysAnticipoLetradoBean.C_IDANTICIPO));
			bean.setDescripcion(UtilidadesHash.getString(hash,PysAnticipoLetradoBean.C_DESCRIPCION));	
			bean.setCtaContable(UtilidadesHash.getString(hash,PysAnticipoLetradoBean.C_CTACONTABLE));	
			bean.setFecha(UtilidadesHash.getString(hash,PysAnticipoLetradoBean.C_FECHA));	
			bean.setImporteInicial(UtilidadesHash.getDouble(hash,PysAnticipoLetradoBean.C_IMPORTEINICIAL));
			
			bean.setUsuMod (UtilidadesHash.getInteger (hash, PysAnticipoLetradoBean.C_USUMODIFICACION));
			bean.setFechaMod (UtilidadesHash.getString (hash, PysAnticipoLetradoBean.C_FECHAMODIFICACION));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	/** 
	 * Obtiene la tabla hash a partir del bean introducido
	 * @param  bean - bean con los valores de la tabla 
	 * @return  Hashtable - Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			PysAnticipoLetradoBean b = (PysAnticipoLetradoBean) bean; 
			UtilidadesHash.set(htData,PysAnticipoLetradoBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData,PysAnticipoLetradoBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData,PysAnticipoLetradoBean.C_IDANTICIPO, b.getIdAnticipo());
			UtilidadesHash.set(htData,PysAnticipoLetradoBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData,PysAnticipoLetradoBean.C_CTACONTABLE, b.getCtaContable());
			UtilidadesHash.set(htData,PysAnticipoLetradoBean.C_FECHA, b.getFecha());
			UtilidadesHash.set(htData,PysAnticipoLetradoBean.C_IMPORTEINICIAL, b.getImporteInicial());
			
			UtilidadesHash.set (htData, PysAnticipoLetradoBean.C_FECHAMODIFICACION,		b.getFechaMod());
			UtilidadesHash.set (htData, PysAnticipoLetradoBean.C_USUMODIFICACION,		b.getUsuMod());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
	
	public Hashtable getDatosAnticipo(String idInstitucion, String idPersona, String idAnticipo)  throws ClsExceptions{
		Hashtable elemento = new Hashtable();
		Hashtable codigos = new Hashtable();
		int contador = 0;
		try{
			String sql = "SELECT IDINSTITUCION,IDPERSONA, IDANTICIPO, DESCRIPCION, IMPORTEINICIAL, "+
			"(IMPORTEINICIAL - (SELECT NVL(SUM(L.IMPORTEANTICIPADO),0) "+ 
            " FROM   PYS_LINEAANTICIPO L  "+
			"  WHERE  L.IDINSTITUCION=PYS_ANTICIPOLETRADO.IDINSTITUCION "+
			"  AND    L.IDPERSONA=PYS_ANTICIPOLETRADO.IDPERSONA "+
			"  AND    L.IDANTICIPO=PYS_ANTICIPOLETRADO.IDANTICIPO "+
			"  AND    TRUNC(L.FECHAEFECTIVA) < SYSDATE)) AS IMPORTERESTANTE, " +
			"((SELECT NVL(SUM(L.IMPORTEANTICIPADO),0) "+ 
            " FROM   PYS_LINEAANTICIPO L  "+
			"  WHERE  L.IDINSTITUCION=PYS_ANTICIPOLETRADO.IDINSTITUCION "+
			"  AND    L.IDPERSONA=PYS_ANTICIPOLETRADO.IDPERSONA "+
			"  AND    L.IDANTICIPO=PYS_ANTICIPOLETRADO.IDANTICIPO "+
			"  AND    TRUNC(L.FECHAEFECTIVA) < SYSDATE)) AS IMPORTEUSADO, "+ 
			" FECHA, CTACONTABLE ";
			
			sql += " FROM PYS_ANTICIPOLETRADO ";
			contador ++;
			codigos.put(new Integer(contador), idInstitucion);
			sql += " WHERE   idinstitucion=:"+ contador;
			contador ++;
			codigos.put(new Integer(contador), idPersona);
			sql += " AND   idpersona =:" + contador;
			contador ++;
			codigos.put(new Integer(contador), idAnticipo);
			sql += " AND   idanticipo =:" + contador;	
			
		    RowsContainer rc = this.findBind(sql,codigos);
		    if (rc != null && rc.size() > 0) {  
				Row fila = (Row) rc.get(0);
				elemento = fila.getRow();
			}

		}catch(Exception e){ 
				throw new ClsExceptions(e,"Error al buscar las series de facturacion candidatas.");
		}
		return elemento;
	}

	public Vector getDatosAnticipoLetrado(String idInstitucion, String idPersona)  throws ClsExceptions{
		Vector salida = new Vector();
		Hashtable hash = new Hashtable();
		try{
			hash.put(PysAnticipoLetradoBean.C_IDINSTITUCION, idInstitucion);
			hash.put(PysAnticipoLetradoBean.C_IDPERSONA, idPersona);
			salida = this.select(hash);
		}catch(Exception e){
				throw new ClsExceptions(e,"Error al buscar las series de facturacion candidatas.");
		}
		return salida;
	}
	
	public PaginadorBind getConsultaAnticiposPaginador(Integer idInstitucion, Long idPersona, Integer anyosMostrados)  throws ClsExceptions{
		PaginadorBind paginador=null;
		try {
			Hashtable codigos = new Hashtable();
			String select = getConsultaAnticipos(idInstitucion, idPersona, codigos,anyosMostrados);
			paginador = new PaginadorBind(select,codigos);


		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta getServiciosSolicitadosPaginador.");
		}
		return paginador;  
		
	}
	
	
	public String getConsultaAnticipos(Integer idInstitucion, Long idPersona, Hashtable codigos, Integer anyosMostrados)  throws ClsExceptions{
		String sql;
		int contador = 0;
		try{
			sql = "SELECT IDINSTITUCION,IDPERSONA, IDANTICIPO, DESCRIPCION, IMPORTEINICIAL, "+
			"(IMPORTEINICIAL - (SELECT NVL(SUM(L.IMPORTEANTICIPADO),0) "+ 
            " FROM   PYS_LINEAANTICIPO L  "+
			"  WHERE  L.IDINSTITUCION=PYS_ANTICIPOLETRADO.IDINSTITUCION "+
			"  AND    L.IDPERSONA=PYS_ANTICIPOLETRADO.IDPERSONA "+
			"  AND    L.IDANTICIPO=PYS_ANTICIPOLETRADO.IDANTICIPO "+
			"  AND    TRUNC(L.FECHAEFECTIVA) < SYSDATE)) AS IMPORTERESTANTE, " +
			"((SELECT NVL(SUM(L.IMPORTEANTICIPADO),0) "+ 
            " FROM   PYS_LINEAANTICIPO L  "+
			"  WHERE  L.IDINSTITUCION=PYS_ANTICIPOLETRADO.IDINSTITUCION "+
			"  AND    L.IDPERSONA=PYS_ANTICIPOLETRADO.IDPERSONA "+
			"  AND    L.IDANTICIPO=PYS_ANTICIPOLETRADO.IDANTICIPO "+
			"  AND    TRUNC(L.FECHAEFECTIVA) < SYSDATE)) AS IMPORTEUSADO, "+ 
			" FECHA, CTACONTABLE ";
			
			sql += " FROM PYS_ANTICIPOLETRADO ";
			contador ++;
			codigos.put(new Integer(contador), idInstitucion);
			sql += " WHERE   idinstitucion=:"+ contador;
			contador ++;
			codigos.put(new Integer(contador), idPersona);
			sql += " AND   idpersona =:" + contador;
			if (anyosMostrados!=null){		 
				 contador++;
				 codigos.put(new Integer(contador),anyosMostrados.toString());
				 sql+=" AND SYSDATE - FECHA < :"+contador;
				}


		}catch(Exception e){ 
				throw new ClsExceptions(e,"Error al buscar las series de facturacion candidatas.");
		}
		return sql;
	}

	

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
	    String [] orden = {
				PysAnticipoLetradoBean.C_FECHA + " DESC", 
				PysAnticipoLetradoBean.C_DESCRIPCION};
	    return orden;
	}
	
	/**
	 * Obtiene un nuevo ID un anticipo de una persona e institucion determinada
	 * @param Bean datos del anticipo.
	 * @return nuevo ID.
	 */
	public Integer getNuevoId (PysAnticipoLetradoBean bean)throws ClsExceptions, SIGAException 
	{
		RowsContainer rc = null;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		try {		
			String sql = " SELECT (nvl(MAX(" + PysAnticipoLetradoBean.C_IDANTICIPO + "),0) + 1) AS " + PysAnticipoLetradoBean.C_IDANTICIPO + 
			  			 " FROM " + PysAnticipoLetradoBean.T_NOMBRETABLA + 
						 " WHERE " + PysAnticipoLetradoBean.T_NOMBRETABLA +"." + PysAnticipoLetradoBean.C_IDPERSONA + " = " + bean.getIdPersona() +
						 " AND " + PysAnticipoLetradoBean.T_NOMBRETABLA +"." + PysAnticipoLetradoBean.C_IDINSTITUCION + " = " + bean.getIdInstitucion();

			rc = this.findForUpdate(sql);
			if (rc!=null) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();
				Integer idAnticipo = UtilidadesHash.getInteger(prueba, PysAnticipoLetradoBean.C_IDANTICIPO);
				if (idAnticipo == null) {
					return new Integer(1);
				}
				else return idAnticipo;								
			}
		}	
		catch (Exception e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'getNuevoID' en B.D.");		
		}
		return null;
	}	

}