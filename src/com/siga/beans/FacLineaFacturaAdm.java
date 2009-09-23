/*
 * VERSIONES:
 * 
 * nuria.rgonzalez - 16-03-2004 - Creación
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.general.SIGAException;

/**
*
* Clase que gestiona la tabla FAC_LINEAFACTURA de la BBDD
* 
*/
public class FacLineaFacturaAdm extends MasterBeanAdministrador {

	/**	
	 * @param usuario
	 */
	public FacLineaFacturaAdm(UsrBean usu)	{
		super (FacLineaFacturaBean.T_NOMBRETABLA, usu);
	
	}
	/** 
	 *  Funcion que devuelve los campos de la tabla.
	 * @return  String[] Los campos de la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {FacLineaFacturaBean.C_IDINSTITUCION,
							FacLineaFacturaBean.C_IDFACTURA,
							FacLineaFacturaBean.C_NUMEROLINEA,
							FacLineaFacturaBean.C_NUMEROORDEN,
							FacLineaFacturaBean.C_CANTIDAD,
							FacLineaFacturaBean.C_IMPORTEANTICIPADO,
							FacLineaFacturaBean.C_DESCRIPCION,
							FacLineaFacturaBean.C_PRECIOUNITARIO,
							FacLineaFacturaBean.C_IVA,
							FacLineaFacturaBean.C_CTAPRODUCTOSERVICIO,
							FacLineaFacturaBean.C_CTAIVA,
							FacLineaFacturaBean.C_FECHAMODIFICACION,
							FacLineaFacturaBean.C_USUMODIFICACION };
		return campos;
	}

	/** 
	 *  Funcion que devuelve las claves de la tabla.
	 * @return  String[] Los campos de la tabla   
	 */
	protected String[] getClavesBean() {
		String [] claves = {FacLineaFacturaBean.C_IDINSTITUCION,
							FacLineaFacturaBean.C_IDFACTURA,
							FacLineaFacturaBean.C_NUMEROLINEA};
		return claves;
	}

	/** 
	 *  Funcion que devuelve como claves únicamente el idInstirucion e idFactura de la tabla.
	 * @return  String[] Los campos de la tabla   
	 */
	protected String[] getClavesReducidaBean() {
		String [] claves = {FacLineaFacturaBean.C_IDINSTITUCION,
				FacLineaFacturaBean.C_IDFACTURA};
		return claves;
	}
	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  FacLineaFacturaBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacLineaFacturaBean bean = null;
		
		try {
			bean = new FacLineaFacturaBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,FacLineaFacturaBean.C_IDINSTITUCION));
			bean.setIdFactura(UtilidadesHash.getString(hash,FacLineaFacturaBean.C_IDFACTURA));			
			bean.setNumeroLinea(UtilidadesHash.getLong(hash,FacLineaFacturaBean.C_NUMEROLINEA));
			bean.setNumeroOrden(UtilidadesHash.getLong(hash,FacLineaFacturaBean.C_NUMEROORDEN));
			bean.setCantidad(UtilidadesHash.getInteger(hash,FacLineaFacturaBean.C_CANTIDAD));	
			bean.setImporteAnticipado(UtilidadesHash.getDouble(hash,FacLineaFacturaBean.C_IMPORTEANTICIPADO));	
			bean.setDescripcion(UtilidadesHash.getString(hash,FacLineaFacturaBean.C_DESCRIPCION));	
			bean.setPrecioUnitario(UtilidadesHash.getDouble(hash,FacLineaFacturaBean.C_PRECIOUNITARIO));	
			bean.setIva(UtilidadesHash.getFloat(hash,FacLineaFacturaBean.C_IVA));	
			bean.setCtaProductoServicio(UtilidadesHash.getString(hash,FacLineaFacturaBean.C_CTAPRODUCTOSERVICIO));	
			bean.setCtaIva(UtilidadesHash.getString(hash,FacLineaFacturaBean.C_CTAIVA));	
			bean.setFechaMod(UtilidadesHash.getString(hash,FacLineaFacturaBean.C_FECHAMODIFICACION));	
			bean.setUsuMod(UtilidadesHash.getInteger(hash,FacLineaFacturaBean.C_USUMODIFICACION));	
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
			FacLineaFacturaBean b = (FacLineaFacturaBean) bean; 
			UtilidadesHash.set(htData,FacLineaFacturaBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData,FacLineaFacturaBean.C_IDFACTURA, b.getIdFactura());			
			UtilidadesHash.set(htData,FacLineaFacturaBean.C_NUMEROLINEA,b.getNumeroLinea());
			UtilidadesHash.set(htData,FacLineaFacturaBean.C_NUMEROORDEN, b.getNumeroOrden());
			UtilidadesHash.set(htData,FacLineaFacturaBean.C_CANTIDAD, b.getCantidad());
			UtilidadesHash.set(htData,FacLineaFacturaBean.C_IMPORTEANTICIPADO, b.getImporteAnticipado());
			UtilidadesHash.set(htData,FacLineaFacturaBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData,FacLineaFacturaBean.C_PRECIOUNITARIO, b.getPrecioUnitario());
			UtilidadesHash.set(htData,FacLineaFacturaBean.C_IVA, b.getIva());
			UtilidadesHash.set(htData,FacLineaFacturaBean.C_CTAPRODUCTOSERVICIO, b.getCtaProductoServicio());
			UtilidadesHash.set(htData,FacLineaFacturaBean.C_CTAIVA, b.getCtaIva());
			UtilidadesHash.set(htData,FacLineaFacturaBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData,FacLineaFacturaBean.C_USUMODIFICACION, b.getUsuMod());
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
	 * Recoge informacion sobre las devoluciones asociadas a una determinada institucion<br/> 
	 * @param  institucion - identificador de la institucion	 	  
	 * @param  factura - identificador de la factura
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getLineasImpresion (String institucion, String factura,boolean isInforme) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
							FacLineaFacturaBean.T_NOMBRETABLA + "." + FacLineaFacturaBean.C_IDINSTITUCION + "," +
			    			FacLineaFacturaBean.T_NOMBRETABLA + "." + FacLineaFacturaBean.C_IDFACTURA + "," +
			    			FacLineaFacturaBean.T_NOMBRETABLA + "." + FacLineaFacturaBean.C_NUMEROLINEA + "," +
			    			FacLineaFacturaBean.T_NOMBRETABLA + "." + FacLineaFacturaBean.C_CTAPRODUCTOSERVICIO + "," +
			    			FacLineaFacturaBean.T_NOMBRETABLA + "." + FacLineaFacturaBean.C_CTAIVA + "," +
			    			FacLineaFacturaBean.T_NOMBRETABLA + "." + FacLineaFacturaBean.C_CANTIDAD + ", " +
			    			FacLineaFacturaBean.T_NOMBRETABLA + "." + FacLineaFacturaBean.C_PRECIOUNITARIO + "," +
			    			FacLineaFacturaBean.T_NOMBRETABLA + "." + FacLineaFacturaBean.C_IVA + "," +
			    			/*FacLineaFacturaBean.T_NOMBRETABLA + "." + FacLineaFacturaBean.C_DESCRIPCION +*/
							"F_SIGA_DESCLINEAFACT("+institucion+","+factura+","+FacLineaFacturaBean.T_NOMBRETABLA +"."+ FacLineaFacturaBean.C_NUMEROLINEA+") DESCRIPCION"+
							" FROM " + FacLineaFacturaBean.T_NOMBRETABLA + 
							" WHERE " +
							FacLineaFacturaBean.T_NOMBRETABLA +"."+ FacLineaFacturaBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							FacLineaFacturaBean.T_NOMBRETABLA +"."+ FacLineaFacturaBean.C_IDFACTURA + "=" + factura +
							" ORDER BY " + FacLineaFacturaBean.T_NOMBRETABLA + "." + FacLineaFacturaBean.C_NUMEROLINEA + " ASC ";
														
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable resultado=fila.getRow();	                  
	                  double importeNeto=UtilidadesNumero.redondea(new Double((String)resultado.get(FacLineaFacturaBean.C_CANTIDAD)).doubleValue() * new Double((String)resultado.get(FacLineaFacturaBean.C_PRECIOUNITARIO)).doubleValue(),2);
	                  double importeIva=UtilidadesNumero.redondea((importeNeto * new Double((String)resultado.get(FacLineaFacturaBean.C_IVA)).doubleValue())/100,2);
	                  double importeTotal=UtilidadesNumero.redondea(importeNeto+importeIva,2);
	                  resultado.put("IMPORTENETO",new Double(importeNeto).toString());
	                  resultado.put("IMPORTEIVA",new Double(importeIva).toString());
	                  resultado.put("IMPORTETOTAL",new Double(importeTotal).toString());
	                  if(isInforme)
	                	  datos.add(fila);
	                  else
	                	  datos.add(fila.getRow());
	                	  
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre las lineas de una determinada factura.");
	       }
	       return datos;                        
	    }	

	/** 
	 * Recoge informacion sobre las devoluciones asociadas a una determinada institucion para el informe MasterRepor<br/> 
	 * @param  institucion - identificador de la institucion	 	  
	 * @param  factura - identificador de la factura
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getLineasImpresionInforme (String institucion, String factura) throws ClsExceptions {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
							FacLineaFacturaBean.T_NOMBRETABLA + "." + FacLineaFacturaBean.C_IDINSTITUCION + "," +
			    			FacLineaFacturaBean.T_NOMBRETABLA + "." + FacLineaFacturaBean.C_IDFACTURA + "," +
			    			FacLineaFacturaBean.T_NOMBRETABLA + "." + FacLineaFacturaBean.C_NUMEROLINEA + "," +
			    			FacLineaFacturaBean.T_NOMBRETABLA + "." + FacLineaFacturaBean.C_CANTIDAD + " CANTIDAD_LINEA, " +
			    			FacLineaFacturaBean.T_NOMBRETABLA + "." + FacLineaFacturaBean.C_CTAPRODUCTOSERVICIO + "," +
			    			FacLineaFacturaBean.T_NOMBRETABLA + "." + FacLineaFacturaBean.C_CTAIVA + "," +
			    			FacLineaFacturaBean.T_NOMBRETABLA + "." + FacLineaFacturaBean.C_PRECIOUNITARIO + " PRECIO_LINEA," +
			    			FacLineaFacturaBean.T_NOMBRETABLA + "." + FacLineaFacturaBean.C_IVA + " IVA_LINEA," +
			    			/*FacLineaFacturaBean.T_NOMBRETABLA + "." + FacLineaFacturaBean.C_DESCRIPCION +*/
							"F_SIGA_DESCLINEAFACT("+institucion+","+factura+","+FacLineaFacturaBean.T_NOMBRETABLA +"."+ FacLineaFacturaBean.C_NUMEROLINEA+") DESCRIPCION_LINEA"+
							" FROM " + FacLineaFacturaBean.T_NOMBRETABLA + 
							" WHERE " +
							FacLineaFacturaBean.T_NOMBRETABLA +"."+ FacLineaFacturaBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							FacLineaFacturaBean.T_NOMBRETABLA +"."+ FacLineaFacturaBean.C_IDFACTURA + "=" + factura +
							" ORDER BY " + FacLineaFacturaBean.T_NOMBRETABLA + "." + FacLineaFacturaBean.C_NUMEROLINEA + " ASC ";
														
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable resultado=fila.getRow();	                  
	                  double importeNeto=UtilidadesNumero.redondea(new Double((String)resultado.get("CANTIDAD_LINEA")).doubleValue() * new Double((String)resultado.get("PRECIO_LINEA")).doubleValue(),2);
	                  double importeIva=UtilidadesNumero.redondea((importeNeto * new Double((String)resultado.get("IVA_LINEA")).doubleValue())/100,2);
	                  double importeTotal=UtilidadesNumero.redondea(importeNeto+importeIva,2);
	                  
//	                  resultado.put("NETO_LINEA",new Double(importeNeto).toString());
//	                  resultado.put("IMPORTE_IVA_LINEA",new Double(importeIva).toString());
//	                  resultado.put("TOTAL_LINEA",new Double(importeTotal).toString());
	                  
	                  resultado.put("NETO_LINEA",        UtilidadesNumero.formato(new Double(importeNeto).toString()));
	                  resultado.put("IMPORTE_IVA_LINEA", UtilidadesNumero.formato(new Double(importeIva).toString()));
	                  resultado.put("TOTAL_LINEA",       UtilidadesNumero.formato(new Double(importeTotal).toString()));

	                  UtilidadesHash.set(resultado, "IVA_LINEA_AUX",UtilidadesHash.getFloat(resultado, "IVA_LINEA"));
	                  UtilidadesHash.set(resultado, "IVA_LINEA",    UtilidadesNumero.formato(UtilidadesHash.getFloat(resultado, "IVA_LINEA").floatValue()));
	                  UtilidadesHash.set(resultado, "PRECIO_LINEA", UtilidadesNumero.formato(UtilidadesHash.getFloat(resultado, "PRECIO_LINEA").floatValue()));

	                  datos.add(fila);
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre las lineas de una determinada factura.");
	       }
	       return datos;                        
	    }	
	
}
