/*
 * Created on 
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;

import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;

/**
 * @author ivan.arias 28/11/08
 *
 */
public class PysLineaAnticipoAdm extends MasterBeanAdministrador {

	/**
	 * @param usuario
	 */
	public PysLineaAnticipoAdm(UsrBean usuario) {
		super(PysLineaAnticipoBean.T_NOMBRETABLA, usuario);
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		String[] campos = {	PysLineaAnticipoBean.C_IDINSTITUCION,
							PysLineaAnticipoBean.C_IDFACTURA ,
							PysLineaAnticipoBean.C_NUMEROLINEA ,
							PysLineaAnticipoBean.C_IDANTICIPO,
							PysLineaAnticipoBean.C_IDLINEA,
							PysLineaAnticipoBean.C_IDPERSONA,
							PysLineaAnticipoBean.C_IMPORTEANTICIPADO,
							PysLineaAnticipoBean.C_LIQUIDACION,
							PysLineaAnticipoBean.C_FECHAEFECTIVA,
							PysLineaAnticipoBean.C_FECHAMODIFICACION,
							PysLineaAnticipoBean.C_USUMODIFICACION};
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		String[] campos = {	PysLineaAnticipoBean.C_IDINSTITUCION,
							PysLineaAnticipoBean.C_IDPERSONA ,
							PysLineaAnticipoBean.C_IDANTICIPO,
							PysLineaAnticipoBean.C_IDLINEA};
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		PysLineaAnticipoBean bean = null;
		try{
			bean = new PysLineaAnticipoBean();
			bean.setFechaMod(UtilidadesHash.getString(hash,PysLineaAnticipoBean.C_FECHAMODIFICACION));
			bean.setIdAnticipo(UtilidadesHash.getInteger(hash,PysLineaAnticipoBean.C_IDANTICIPO));
			bean.setIdFactura(UtilidadesHash.getString(hash,PysLineaAnticipoBean.C_IDFACTURA));
			bean.setLiquidacion(UtilidadesHash.getString(hash,PysLineaAnticipoBean.C_LIQUIDACION));
			bean.setFechaEfectiva(UtilidadesHash.getString(hash,PysLineaAnticipoBean.C_FECHAEFECTIVA));
			bean.setNumeroLinea(UtilidadesHash.getLong(hash,PysLineaAnticipoBean.C_NUMEROLINEA));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,PysLineaAnticipoBean.C_IDINSTITUCION));
			bean.setIdLinea(UtilidadesHash.getInteger(hash,PysLineaAnticipoBean.C_IDLINEA));
			bean.setIdPersona(UtilidadesHash.getInteger(hash,PysLineaAnticipoBean.C_IDPERSONA));
			bean.setImporteAnticipado(UtilidadesHash.getDouble(hash,PysLineaAnticipoBean.C_IMPORTEANTICIPADO));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,PysLineaAnticipoBean.C_USUMODIFICACION));
			
			bean.setUsuMod (UtilidadesHash.getInteger (hash, PysLineaAnticipoBean.C_USUMODIFICACION));
			bean.setFechaMod (UtilidadesHash.getString (hash, PysLineaAnticipoBean.C_FECHAMODIFICACION));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#beanToHashTable(com.siga.beans.MasterBean)
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			PysLineaAnticipoBean b = (PysLineaAnticipoBean) bean;
			UtilidadesHash.set(hash, PysLineaAnticipoBean.C_IDANTICIPO, b.getIdAnticipo());
			UtilidadesHash.set(hash, PysLineaAnticipoBean.C_IDFACTURA, b.getIdFactura());
			UtilidadesHash.set(hash, PysLineaAnticipoBean.C_LIQUIDACION, b.getLiquidacion());
			UtilidadesHash.set(hash, PysLineaAnticipoBean.C_NUMEROLINEA, b.getNumeroLinea());
			UtilidadesHash.set(hash, PysLineaAnticipoBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(hash, PysLineaAnticipoBean.C_IDLINEA, b.getIdLinea());
			UtilidadesHash.set(hash, PysLineaAnticipoBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(hash, PysLineaAnticipoBean.C_IMPORTEANTICIPADO, b.getImporteAnticipado());
			UtilidadesHash.set(hash, PysLineaAnticipoBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(hash, PysLineaAnticipoBean.C_FECHAEFECTIVA, b.getFechaEfectiva());
			UtilidadesHash.set(hash, PysLineaAnticipoBean.C_USUMODIFICACION, b.getUsuMod());
			
			UtilidadesHash.set (hash, PysLineaAnticipoBean.C_FECHAMODIFICACION,	b.getFechaMod());
			UtilidadesHash.set (hash, PysLineaAnticipoBean.C_USUMODIFICACION,	b.getUsuMod());
		
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}
	
	/**
	 *   
	 * @author ivan.arias 28/11/08
	 * 	 
	 * @param idPersona, es el identificador de la persona de al que vamos a obtener los datos. 
	 * @param idInstitucion, es el identificador de la institucion de la persona de la que vamos a obtener los datos.
	 * @param idAnticipo
	 * 
	 * @return Vector con las lineas de uso de un anticipo
	 */	
	public Vector getLineasAnticipo(String idInstitucion, String idPersona, String idAnticipo)throws ClsExceptions{
		
		Vector salida = new Vector();
		Hashtable codigosBind = new Hashtable();
		int contador = 0;
		
		String select = " SELECT " +
							PysLineaAnticipoBean.T_NOMBRETABLA+"."+PysLineaAnticipoBean.C_IDPERSONA+", "+
							PysLineaAnticipoBean.T_NOMBRETABLA+"."+PysLineaAnticipoBean.C_IDANTICIPO+", "+
							PysLineaAnticipoBean.T_NOMBRETABLA+"."+PysLineaAnticipoBean.C_IDLINEA+", "+
							PysLineaAnticipoBean.T_NOMBRETABLA+"."+PysLineaAnticipoBean.C_FECHAEFECTIVA+", "+
							PysLineaAnticipoBean.T_NOMBRETABLA+"."+PysLineaAnticipoBean.C_LIQUIDACION+", "+
							PysLineaAnticipoBean.T_NOMBRETABLA+"."+PysLineaAnticipoBean.C_FECHAMODIFICACION+", "+
							PysLineaAnticipoBean.T_NOMBRETABLA+"."+PysLineaAnticipoBean.C_IMPORTEANTICIPADO+", "+
							FacFacturaBean.T_NOMBRETABLA+"."+FacFacturaBean.C_NUMEROFACTURA+", "+
							FacFacturaBean.T_NOMBRETABLA+"."+FacFacturaBean.C_FECHAEMISION;
		
		String from = " FROM "+
							PysLineaAnticipoBean.T_NOMBRETABLA+", "+
							FacFacturaBean.T_NOMBRETABLA;
		
		String where = " WHERE "+
							PysLineaAnticipoBean.T_NOMBRETABLA+"."+PysLineaAnticipoBean.C_IDINSTITUCION+" = "+FacFacturaBean.T_NOMBRETABLA+"."+FacFacturaBean.C_IDINSTITUCION + " (+) " +
						" AND "+
							PysLineaAnticipoBean.T_NOMBRETABLA+"."+PysLineaAnticipoBean.C_IDFACTURA+" = "+FacFacturaBean.T_NOMBRETABLA+"."+FacFacturaBean.C_IDFACTURA + " (+) ";

		contador++;
		codigosBind.put(new Integer(contador), idInstitucion);
			  where +=" AND "+PysLineaAnticipoBean.T_NOMBRETABLA+"."+PysLineaAnticipoBean.C_IDINSTITUCION+" =:"+contador;
		contador++;
		codigosBind.put(new Integer(contador), idPersona);
			  where+=" AND "+PysLineaAnticipoBean.T_NOMBRETABLA+"."+PysLineaAnticipoBean.C_IDPERSONA+" =:"+contador;
	    contador++;
		codigosBind.put(new Integer(contador), idAnticipo);
			  where+=" AND "+PysLineaAnticipoBean.T_NOMBRETABLA+"."+PysLineaAnticipoBean.C_IDANTICIPO+" =:"+contador;
		// RGG Cambio para usar fecha efectiva 
		where += " AND   "+PysLineaAnticipoBean.T_NOMBRETABLA+"."+PysLineaAnticipoBean.C_FECHAEFECTIVA+"  <= SYSDATE";	

			  
	    String consulta = select + from + where;
		
	    try{
	    	RowsContainer rc = this.findBind(consulta, codigosBind);
	    	if (rc != null && rc.size() > 0) {  
	    	    for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) { 
						if (registro.get(PysLineaAnticipoBean.C_LIQUIDACION)!=null && ((String)registro.get(PysLineaAnticipoBean.C_LIQUIDACION)).equals("0")) {
						    if (registro.get(FacFacturaBean.C_NUMEROFACTURA)==null || ((String)registro.get(FacFacturaBean.C_NUMEROFACTURA)).equals("")) {
								// la factura esta en revision.
						        registro.put(FacFacturaBean.C_NUMEROFACTURA, UtilidadesString.getMensajeIdioma(this.usrbean, "pys.anticipos.mensaje.facturaEnRevision"));
								registro.put(FacFacturaBean.C_FECHAEMISION, (String) registro.get(PysLineaAnticipoBean.C_FECHAEFECTIVA));
						    }
					    } else {
							registro.put(FacFacturaBean.C_NUMEROFACTURA, UtilidadesString.getMensajeIdioma(this.usrbean, "pys.anticipos.mensaje.anticipoLiquidado"));
							registro.put(FacFacturaBean.C_FECHAEMISION, (String) registro.get(PysLineaAnticipoBean.C_FECHAEFECTIVA));
					    }
						salida.add(registro);
					}
				}
			}
		}
		catch(Exception e){
			throw new ClsExceptions(e,"Error al buscar las lineas de uso de un anticipo.");
		}
		
		return salida;
	}
	
	
	/** 
	 * Obtiene si existen lineas para un anticipo
	 * @param  idInstitucion - identificador de la institucion
	 * @param  idAnticipo - identificador del anticipo        
	 * @return  Vector - Vector de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public static boolean getExistenLineasAnticipo (String idInstitucion, String idPersona, String idAnticipo) throws ClsExceptions{
		boolean hayLineas = false;
		Hashtable codigos = new Hashtable();
		int contador = 0;
		String sql = " SELECT idlinea ";
			   sql+= " FROM pys_lineaanticipo ";
		try{
			contador ++;
			codigos.put(new Integer(contador), idInstitucion);
			sql += " WHERE   idinstitucion=:"+ contador;
			contador ++;
			codigos.put(new Integer(contador), idPersona);
			sql += " AND   idpersona =:" + contador;
			contador ++;
			codigos.put(new Integer(contador), idAnticipo);
			sql += " AND   idanticipo =:" + contador;	
			// RGG Cambio para usar fecha efectiva 
			sql += " AND   fechaefectiva <= SYSDATE";	
			
			RowsContainer rc = new RowsContainer();
	    	if( rc.findBind(sql, codigos)){
		    	if (rc != null && rc.size() > 0) {  
		    		hayLineas = true;										
				}
	    	}	
		}
		catch(Exception e){
			throw new ClsExceptions(e,e.toString());
		}
		return hayLineas;	
	}
		
	/** 
	 * Obtiene elo importe usad de un anticipo
	 * @param  idInstitucion - identificador de la institucion
	 * @param  idAnticipo - identificador del anticipo        
	 * @return  Vector - Vector de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public static double getGastadoLineasAnticipo (String idInstitucion, String idPersona, String idAnticipo) throws ClsExceptions{
		double salida = 0.0;
		Hashtable codigos = new Hashtable();
		int contador = 0;
		String sql = " SELECT nvl(sum(nvl(IMPORTEANTICIPADO,0)),0) as TOTALGASTADO ";
			   sql+= " FROM pys_lineaanticipo ";
		try{
			contador ++;
			codigos.put(new Integer(contador), idInstitucion);
			sql += " WHERE   idinstitucion=:"+ contador;
			contador ++;
			codigos.put(new Integer(contador), idPersona);
			sql += " AND   idpersona =:" + contador;
			contador ++;
			codigos.put(new Integer(contador), idAnticipo);
			sql += " AND   idanticipo =:" + contador;	
			// RGG Cambio para usar fecha efectiva 
			sql += " AND   fechaefectiva <= SYSDATE ";	
			
			RowsContainer rc = new RowsContainer();
	    	if( rc.findBind(sql, codigos)){
		    	if (rc != null && rc.size() > 0) {  
		    	    Row fila = (Row) rc.get(0);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) { 
					    salida = (new Double((String)registro.get("TOTALGASTADO"))).doubleValue();
					}
				}
	    	}	
		}
		catch(Exception e){
			throw new ClsExceptions(e,e.toString());
		}
		return salida;	
	}
	
}
