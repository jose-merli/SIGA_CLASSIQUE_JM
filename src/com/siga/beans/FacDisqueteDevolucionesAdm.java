/*
 * VERSIONES:
 * julio.vicente - 11-03-2005 - Creación
 */

package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorCaseSensitive;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;


public class FacDisqueteDevolucionesAdm extends MasterBeanAdministrador {
	
	public FacDisqueteDevolucionesAdm (UsrBean usu) {
		super (FacDisqueteDevolucionesBean.T_NOMBRETABLA, usu);
	}
	
	protected String[] getCamposBean() {
		String [] campos = {FacDisqueteDevolucionesBean.C_IDINSTITUCION, 		
						    FacDisqueteDevolucionesBean.C_BANCOS_CODIGO,
							FacDisqueteDevolucionesBean.C_FECHAGENERACION,							
							FacDisqueteDevolucionesBean.C_NOMBREFICHERO,
							FacDisqueteDevolucionesBean.C_IDDISQUETEDEVOLUCIONES,							
							FacDisqueteDevolucionesBean.C_FECHAMODIFICACION,
							FacDisqueteDevolucionesBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FacDisqueteDevolucionesBean.C_IDINSTITUCION, FacDisqueteDevolucionesBean.C_IDDISQUETEDEVOLUCIONES};
		return claves;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacDisqueteDevolucionesBean bean = null;
		
		try {
			bean = new FacDisqueteDevolucionesBean();
			bean.setIdInstitucion				(UtilidadesHash.getInteger(hash, FacDisqueteDevolucionesBean.C_IDINSTITUCION));
			bean.setBancosCodigo				(UtilidadesHash.getString(hash, FacDisqueteDevolucionesBean.C_BANCOS_CODIGO));
			bean.setFechaGeneracion				(UtilidadesHash.getString(hash, FacDisqueteDevolucionesBean.C_FECHAGENERACION));
			bean.setNombreFichero				(UtilidadesHash.getString(hash, FacDisqueteDevolucionesBean.C_NOMBREFICHERO));
			bean.setIdDisqueteDevoluciones		(UtilidadesHash.getLong(hash, FacDisqueteDevolucionesBean.C_IDDISQUETEDEVOLUCIONES));					
			bean.setFechaMod					(UtilidadesHash.getString(hash, FacDisqueteDevolucionesBean.C_FECHAMODIFICACION));
			bean.setUsuMod						(UtilidadesHash.getInteger(hash, FacDisqueteDevolucionesBean.C_USUMODIFICACION));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			FacDisqueteDevolucionesBean b = (FacDisqueteDevolucionesBean) bean;
			UtilidadesHash.set(htData, FacDisqueteDevolucionesBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, FacDisqueteDevolucionesBean.C_BANCOS_CODIGO, b.getBancosCodigo());
			UtilidadesHash.set(htData, FacDisqueteDevolucionesBean.C_FECHAGENERACION, b.getFechaGeneracion());
			UtilidadesHash.set(htData, FacDisqueteDevolucionesBean.C_NOMBREFICHERO, b.getNombreFichero());
			UtilidadesHash.set(htData, FacDisqueteDevolucionesBean.C_IDDISQUETEDEVOLUCIONES, b.getIdDisqueteDevoluciones());
			UtilidadesHash.set(htData, FacDisqueteDevolucionesBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, FacDisqueteDevolucionesBean.C_USUMODIFICACION, b.getUsuMod());
			
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}
	
	/** 
	 * Recoge informacion sobre las devoluciones asociadas a una determinada institucion<br/> 
	 * @param  institucion - identificador de la institucion	 	  
	 * @return  PaginadorCaseSensitiveBind
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public PaginadorCaseSensitive getDevoluciones (String institucion) throws ClsExceptions {
		try {
			RowsContainer rc = new RowsContainer();
			String sql =
				"SELECT DIS."+FacDisqueteDevolucionesBean.C_IDINSTITUCION+", " +
				"       DIS."+FacDisqueteDevolucionesBean.C_IDDISQUETEDEVOLUCIONES+", " +
				"       BAN."+CenBancosBean.C_CODIGO+", " +
				"       DIS."+FacDisqueteDevolucionesBean.C_FECHAGENERACION+"," +
				"       DIS."+FacDisqueteDevolucionesBean.C_NOMBREFICHERO+"," +
				"       (SELECT COUNT (*) " +
				"          FROM "+FacLineaDevoluDisqBancoBean.T_NOMBRETABLA+" LIN " +
				"         WHERE LIN."+FacLineaDevoluDisqBancoBean.C_IDINSTITUCION+" = " +
				"               DIS."+FacDisqueteDevolucionesBean.C_IDINSTITUCION+" " +
				"           AND LIN."+FacLineaDevoluDisqBancoBean.C_IDDISQUETEDEVOLUCIONES+" = " +
				"               DIS."+FacDisqueteDevolucionesBean.C_IDDISQUETEDEVOLUCIONES+" " +
				"       ) AS FACTURAS, " +
				"       (SELECT COUNT (DISTINCT LIN."+FacLineaDevoluDisqBancoBean.C_CARGARCLIENTE+")+ 1" +
				"          FROM "+FacLineaDevoluDisqBancoBean.T_NOMBRETABLA+" LIN " +
				"         WHERE LIN."+FacLineaDevoluDisqBancoBean.C_IDINSTITUCION+" = " +
				"               DIS."+FacDisqueteDevolucionesBean.C_IDINSTITUCION+" " +
				"           AND LIN."+FacLineaDevoluDisqBancoBean.C_IDDISQUETEDEVOLUCIONES+" = " +
				"               DIS."+FacDisqueteDevolucionesBean.C_IDDISQUETEDEVOLUCIONES+" " +
				"           AND LIN."+FacLineaDevoluDisqBancoBean.C_CARGARCLIENTE+" = 'S') AS COMISION, " +
				"       BAN."+CenBancosBean.C_NOMBRE + " " +
				"  FROM "+FacDisqueteDevolucionesBean.T_NOMBRETABLA+" DIS, " +
				"       "+FacBancoInstitucionBean.T_NOMBRETABLA+" BANINS, " +
				"       "+CenBancosBean.T_NOMBRETABLA+" BAN " +
				" WHERE DIS."+FacDisqueteDevolucionesBean.C_IDINSTITUCION+" = BANINS."+FacBancoInstitucionBean.C_IDINSTITUCION+" " +
				"   AND DIS."+FacDisqueteDevolucionesBean.C_BANCOS_CODIGO+" = BANINS."+FacBancoInstitucionBean.C_BANCOS_CODIGO+" " +
				"   AND BANINS."+FacBancoInstitucionBean.C_COD_BANCO+" = BAN."+CenBancosBean.C_CODIGO+" " +
				"   AND DIS."+FacDisqueteDevolucionesBean.C_IDINSTITUCION+" = " + institucion +
				" ORDER BY DIS."+FacDisqueteDevolucionesBean.C_FECHAGENERACION+" DESC, " +
				"          DIS."+FacDisqueteDevolucionesBean.C_IDDISQUETEDEVOLUCIONES+" DESC";
			
			PaginadorCaseSensitive paginador = new PaginadorCaseSensitive(sql);				
			int totalRegistros = paginador.getNumeroTotalRegistros();
			
			if (totalRegistros==0){					
				paginador = null;
			}
			return paginador;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla de abonos.");
		}                    
	} //getDevoluciones()
	
	/** 
	 * Obtiene el valor IDDISQUETEDEVOLUCIONES <br/>
	 * @param  institucion - identificador de la institucion 
	 * @return  Long - Siguiente identificador de IDDISQUETEDEVOLUCIONES 
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Long getNuevoID (String institucion) throws ClsExceptions, SIGAException 
	{

		Long resultado=null;
		
		try { 
            RowsContainer rc = new RowsContainer();

			String sql;
			sql ="SELECT (MAX(IDDISQUETEDEVOLUCIONES) + 1) AS IDDISQUETEDEVOLUCIONES FROM " + nombreTabla +
				" WHERE IDINSTITUCION =" + institucion;
		
			if (rc.findForUpdate(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba==null || prueba.get("IDDISQUETEDEVOLUCIONES")==null || prueba.get("IDDISQUETEDEVOLUCIONES").equals("")) {
					resultado=new Long(1);
				}
				else 
					resultado=UtilidadesHash.getLong(prueba, "IDDISQUETEDEVOLUCIONES");;								
			}
		}	
//		catch (SIGAException e) {
//			throw e;
//		}

		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'getNuevoId' en B.D.");		
		}
		return resultado;
	}
	
	public Vector getFacturasDevueltasEnDisquete (Integer idInstitucion,Integer idDisquete)
	throws ClsExceptions,SIGAException
{
	Vector factDevueltasVector=new Vector();
	
	try {
		Hashtable codigosHashtable = new Hashtable();
		int contador = 0;
		RowsContainer rc = new RowsContainer();
		StringBuffer sql = new StringBuffer();
		sql.append(" ");
		sql.append(" SELECT FAC.NUMEROFACTURA,FAC.IDFACTURA,FAC.IDINSTITUCION,FAC.ESTADO,FAC.IMPTOTALPORPAGAR ");
		sql.append(" , LD.IDRECIBO ");
		sql.append(" FROM FAC_DISQUETEDEVOLUCIONES      DD, ");
		sql.append(" FAC_LINEADEVOLUDISQBANCO      LD, ");
		sql.append(" FAC_FACTURAINCLUIDAENDISQUETE FID, ");
		sql.append(" FAC_FACTURA                   FAC ");

		sql.append(" WHERE FAC.IDFACTURA = FID.IDFACTURA ");
		sql.append(" AND FAC.IDINSTITUCION = FID.IDINSTITUCION ");
		sql.append(" AND FID.IDINSTITUCION = LD.IDINSTITUCION ");
		sql.append(" AND FID.IDFACTURAINCLUIDAENDISQUETE = LD.IDFACTURAINCLUIDAENDISQUETE ");
		sql.append(" AND FID.IDDISQUETECARGOS = LD.IDDISQUETECARGOS ");
		sql.append(" AND LD.IDINSTITUCION = DD.IDINSTITUCION ");
		sql.append(" AND LD.IDDISQUETEDEVOLUCIONES = DD.IDDISQUETEDEVOLUCIONES ");
		sql.append(" AND DD.IDINSTITUCION = :");
		contador ++;
		sql.append(contador);
		codigosHashtable.put(new Integer(contador),idInstitucion);
		sql.append(" AND DD.IDDISQUETEDEVOLUCIONES = :");
		contador ++;
		sql.append(contador);
		codigosHashtable.put(new Integer(contador),idDisquete);
		
		if (rc.findBind(sql.toString(),codigosHashtable)) {
			for (int i = 0; i < rc.size(); i++){
				Row fila = (Row) rc.get(i);
				factDevueltasVector.add(fila);
			}
		}
	}
	catch (Exception e) {
		throw new ClsExceptions (e, "Error al obtener la informacion sobre del disquete de devoluciones.");
	}
	
	return factDevueltasVector;                        
} //getFacturasDevueltasEnDisquete()

	
	

	
}
