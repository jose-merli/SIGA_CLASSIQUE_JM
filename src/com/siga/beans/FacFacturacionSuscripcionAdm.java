/*
 * VERSIONES:
 * 
 * nuria.rgonzalez - 16-03-2004 - Creación
 */
package com.siga.beans;

import java.util.Hashtable;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.eejg.ScsEejgXmlBean;
/**
*
* Clase que gestiona la tabla FAC_FACTURACIONSUSCRIPCION de la BBDD
* 
*/
public class FacFacturacionSuscripcionAdm extends MasterBeanAdministrador {

	/**	
	 * @param usuario
	 */
	public FacFacturacionSuscripcionAdm(UsrBean usu)	{
		super (FacFacturacionSuscripcionBean.T_NOMBRETABLA, usu);
	
	}

	/** 
	 *  Funcion que devuelve los campos de la tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {FacFacturacionSuscripcionBean.C_IDINSTITUCION,
							FacFacturacionSuscripcionBean.C_IDFACTURA,
							FacFacturacionSuscripcionBean.C_NUMEROLINEA,
							FacFacturacionSuscripcionBean.C_IDTIPOSERVICIOS,
							FacFacturacionSuscripcionBean.C_IDSERVICIO,
							FacFacturacionSuscripcionBean.C_IDSERVICIOSINSTITUCION,
							FacFacturacionSuscripcionBean.C_IDSUSCRIPCION,
							FacFacturacionSuscripcionBean.C_IDFACTURACIONSUSCRIPCION,
							FacFacturacionSuscripcionBean.C_FECHAINICIO,
							FacFacturacionSuscripcionBean.C_FECHAFIN,
							FacFacturacionSuscripcionBean.C_DESCRIPCION,		
							FacFacturacionSuscripcionBean.C_FECHAMODIFICACION,
							FacFacturacionSuscripcionBean.C_USUMODIFICACION};
		return campos;
	}

	/** 
	 *  Funcion que devuelve las claves de la tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getClavesBean() {
		String [] claves = {FacFacturacionSuscripcionBean.C_IDINSTITUCION,
							FacFacturacionSuscripcionBean.C_IDFACTURA,
							FacFacturacionSuscripcionBean.C_NUMEROLINEA};
		return claves;
	}
	
	/** 
	 *  Funcion que devuelve como claves únicamente el idInstirucion e idFactura de la tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getClavesReducidaBean() {
		String [] claves = {FacFacturacionSuscripcionBean.C_IDINSTITUCION,
							FacFacturacionSuscripcionBean.C_IDFACTURA};
		return claves;
	}

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  FacFacturacionSuscripcionBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacFacturacionSuscripcionBean bean = null;
		
		try {
			bean = new FacFacturacionSuscripcionBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,FacFacturacionSuscripcionBean.C_IDINSTITUCION));
			bean.setIdFactura(UtilidadesHash.getString(hash,FacFacturacionSuscripcionBean.C_IDFACTURA));
			bean.setNumeroLinea(UtilidadesHash.getLong(hash,FacFacturacionSuscripcionBean.C_NUMEROLINEA));
			bean.setIdTipoServicio(UtilidadesHash.getInteger(hash,FacFacturacionSuscripcionBean.C_IDTIPOSERVICIOS));
			bean.setIdServicio(UtilidadesHash.getInteger(hash,FacFacturacionSuscripcionBean.C_IDSERVICIO));
			bean.setIdServiciosInstitucion(UtilidadesHash.getInteger(hash,FacFacturacionSuscripcionBean.C_IDSERVICIOSINSTITUCION));
			bean.setIdSuscripcion(UtilidadesHash.getInteger(hash,FacFacturacionSuscripcionBean.C_IDSUSCRIPCION));
			bean.setIdFacturacionSuscripcion(UtilidadesHash.getInteger(hash,FacFacturacionSuscripcionBean.C_IDFACTURACIONSUSCRIPCION));
			bean.setFechaInicio(UtilidadesHash.getString(hash,FacFacturacionSuscripcionBean.C_FECHAINICIO));
			bean.setFechaFin(UtilidadesHash.getString(hash,FacFacturacionSuscripcionBean.C_FECHAFIN));
			bean.setDescripcion(UtilidadesHash.getString(hash,FacFacturacionSuscripcionBean.C_DESCRIPCION));
			bean.setFechaMod(UtilidadesHash.getString (hash,FacFacturacionSuscripcionBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,FacFacturacionSuscripcionBean.C_USUMODIFICACION));			
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
			FacFacturacionSuscripcionBean b = (FacFacturacionSuscripcionBean) bean; 
			UtilidadesHash.set(htData,FacFacturacionSuscripcionBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData,FacFacturacionSuscripcionBean.C_IDFACTURA, b.getIdFactura());
			UtilidadesHash.set(htData,FacFacturacionSuscripcionBean.C_NUMEROLINEA, b.getNumeroLinea());
			UtilidadesHash.set(htData,FacFacturacionSuscripcionBean.C_IDTIPOSERVICIOS, b.getIdTipoServicio());
			UtilidadesHash.set(htData,FacFacturacionSuscripcionBean.C_IDSERVICIO, b.getIdServicio());
			UtilidadesHash.set(htData,FacFacturacionSuscripcionBean.C_IDSERVICIOSINSTITUCION, b.getIdServiciosInstitucion());
			UtilidadesHash.set(htData,FacFacturacionSuscripcionBean.C_IDSUSCRIPCION, b.getIdSuscripcion());
			UtilidadesHash.set(htData,FacFacturacionSuscripcionBean.C_IDFACTURACIONSUSCRIPCION, b.getIdFacturacionSuscripcion());
			UtilidadesHash.set(htData,FacFacturacionSuscripcionBean.C_FECHAINICIO, b.getFechaInicio());
			UtilidadesHash.set(htData,FacFacturacionSuscripcionBean.C_FECHAFIN, b.getFechaFin());
			UtilidadesHash.set(htData,FacFacturacionSuscripcionBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData,FacFacturacionSuscripcionBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData,FacFacturacionSuscripcionBean.C_USUMODIFICACION, b.getUsuMod());
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

	/** Funcion delete masivo de registros (Hashtable hash)
	 *  @param hasTable con las parejas campo-valor para realizar un where en el delete 
	 *  @return true si todo va bien OK, false si hay algun error 
	 * */
	public boolean deleteMasivo(Hashtable hash) throws ClsExceptions{

		try {
			Row row = new Row();	
			row.load(hash);

			String [] claves = getClavesReducidaBean();
			
			row.delete(this.nombreTabla, claves);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new ClsExceptions(e, "Error al realizar el \"delete\" en B.D.");
		}
		return true;
	}	
	/**
	 * No devuelve el numero de servicios que tiene la factura asociada
	 * @param idInstitucion
	 * @param idFactura
	 * @return
	 * @throws ClsExceptions
	 */
	public int getNumeroServiciosFactura(Integer idInstitucion, String idFactura) throws ClsExceptions
	{
		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT COUNT(*) NUMSERVICIOS FROM FAC_FACTURACIONSUSCRIPCION FS WHERE FS.IDINSTITUCION = :");
		contador++;
		sql.append(contador);
		htCodigos.put(new Integer(contador), idInstitucion);
		sql.append(" AND FS.IDFACTURA = :");
		contador++;
		sql.append(contador);
		htCodigos.put(new Integer(contador), idFactura);

	

		int numServicios = 0;
		try {
			RowsContainer rc = new RowsContainer();

			if (rc.findBind(sql.toString(), htCodigos)) {
				if(rc.size()>0){
					Row fila = (Row) rc.get(0);
            		Hashtable<String, Object> htFila=fila.getRow();
            		numServicios = UtilidadesHash.getInteger(htFila,"NUMSERVICIOS");
				
				}
				
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al ejecutar consulta.");
		}
		return numServicios;

	}	
	
}
