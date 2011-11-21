//VERSIONES:
//ruben.fernandez 09-03-2005 creacion
//Modificado por david.sanchezp para anhadir el metodo desasignarPago().

package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;

/**
* Administrador de Facturacion de justicia gratuita
* @author AtosOrigin 08-03-2005
*/
public class FcsMovimientosVariosAdm extends MasterBeanAdministrador {

	
	public FcsMovimientosVariosAdm(UsrBean usuario) {
		super(FcsMovimientosVariosBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FcsMovimientosVariosBean.C_CANTIDAD,		FcsMovimientosVariosBean.C_DESCRIPCION,
							FcsMovimientosVariosBean.C_FECHAALTA,		FcsMovimientosVariosBean.C_FECHAMODIFICACION,
							FcsMovimientosVariosBean.C_IDINSTITUCION,	FcsMovimientosVariosBean.C_IDMOVIMIENTO,
							FcsMovimientosVariosBean.C_IDPERSONA,       FcsMovimientosVariosBean.C_MOTIVO,			
							FcsMovimientosVariosBean.C_USUMODIFICACION, FcsMovimientosVariosBean.C_CONTABILIZADO};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsMovimientosVariosBean.C_IDINSTITUCION,	FcsMovimientosVariosBean.C_IDMOVIMIENTO};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsMovimientosVariosBean bean = null;
		
		try {
			bean = new FcsMovimientosVariosBean();
			bean.setCantidad(UtilidadesHash.getDouble(hash,FcsMovimientosVariosBean.C_CANTIDAD));
			bean.setDescripcion(UtilidadesHash.getString(hash,FcsMovimientosVariosBean.C_DESCRIPCION));
			bean.setFechaAlta(UtilidadesHash.getString(hash,FcsMovimientosVariosBean.C_FECHAALTA));
			bean.setFechaMod(UtilidadesHash.getString(hash,FcsMovimientosVariosBean.C_FECHAMODIFICACION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,FcsMovimientosVariosBean.C_IDINSTITUCION));
			bean.setIdMovimiento(UtilidadesHash.getInteger(hash,FcsMovimientosVariosBean.C_IDMOVIMIENTO));
			bean.setIdPersona(UtilidadesHash.getInteger(hash,FcsMovimientosVariosBean.C_IDPERSONA));
			bean.setMotivo(UtilidadesHash.getString(hash,FcsMovimientosVariosBean.C_MOTIVO));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,FcsMovimientosVariosBean.C_USUMODIFICACION));
			bean.setContabilizado(UtilidadesHash.getString(hash,FcsMovimientosVariosBean.C_CONTABILIZADO));
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
			FcsMovimientosVariosBean b = (FcsMovimientosVariosBean) bean;
			UtilidadesHash.set(htData, FcsMovimientosVariosBean.C_CANTIDAD, b.getCantidad().toString());
			UtilidadesHash.set(htData, FcsMovimientosVariosBean.C_DESCRIPCION , b.getDescripcion() );
			UtilidadesHash.set(htData, FcsMovimientosVariosBean.C_FECHAALTA , b.getFechaAlta() );
			UtilidadesHash.set(htData, FcsMovimientosVariosBean.C_FECHAMODIFICACION , b.getFechaMod() );
			UtilidadesHash.set(htData, FcsMovimientosVariosBean.C_IDINSTITUCION , b.getIdInstitucion() );
			UtilidadesHash.set(htData, FcsMovimientosVariosBean.C_IDMOVIMIENTO , b.getIdMovimiento() );
			UtilidadesHash.set(htData, FcsMovimientosVariosBean.C_IDPERSONA , b.getIdPersona() );
			UtilidadesHash.set(htData, FcsMovimientosVariosBean.C_MOTIVO , b.getMotivo() );
			UtilidadesHash.set(htData, FcsMovimientosVariosBean.C_USUMODIFICACION , b.getUsuMod() );
			UtilidadesHash.set(htData, FcsMovimientosVariosBean.C_CONTABILIZADO , b.getContabilizado() );
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
	
	/**
	 * Devuelve en un Vector de Hashtables, registros de la BD que son resultado de ejecutar la select.  
	 * @param String select: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con Hashtables. Cada Hashtable es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String select) throws ClsExceptions {
		
		Vector datos = new Vector();
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.queryNLS(select)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en FcsMovimientosVariosAdm.selectGenerico(). Consulta SQL:"+select);
		}
		return datos;	
	}
	
	/**
	 * Calcular el identificador del movimiento que se va a insertar. Necesita que el hashtable que se le pasa
	 * tenga una key IdInstitucion con el cod de institucion del usuario logado  
	 * 
	 * @param entrada Hashtable con los campos a insertar. De tipo "Hashtable". 
	 * @return Hashtable con los campos adaptados.
	 */
	
	public Hashtable prepararInsert (Hashtable entrada)throws ClsExceptions 
	{
		String values;	
		RowsContainer rc = null;
		int contador = 0;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		String sql ="SELECT (MAX(IDMOVIMIENTO) + 1) AS IDMOVIMIENTO FROM " + nombreTabla;	
		try {		
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDMOVIMIENTO").equals("")) {
					entrada.put(FcsMovimientosVariosBean.C_IDMOVIMIENTO,"1");
				}
				else entrada.put(FcsMovimientosVariosBean.C_IDMOVIMIENTO,(String)prueba.get("IDMOVIMIENTO"));								
			}
		}	
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error en clase FcsMovimientoVariosAdm 'prepararInsert()'" + e.getErrorType());		
		}
		return entrada;
	}
	
	/**
	 * Devuelve un vector con los mivimientos varios que hay que pagar para una persona
	 *  
	 * @param idInstitucion
	 * @param idPago
	 * @param idPersona
	 * @return
	 */
	public Vector getMovimientos (String idInstitucion, String idPago, String idPersona) throws ClsExceptions 
	{
		//donde devolveremos el resultado
		Vector resultado = new Vector();
		//query con la select a ejecutar
		String consulta = " SELECT M." + FcsMovimientosVariosBean.C_MOTIVO + " " + FcsMovimientosVariosBean.C_MOTIVO + ","+
							" M." + FcsMovimientosVariosBean.C_DESCRIPCION + " " + FcsMovimientosVariosBean.C_DESCRIPCION + ","+
							" M." + FcsMovimientosVariosBean.C_IDINSTITUCION + " " + FcsMovimientosVariosBean.C_IDINSTITUCION + ","+
							" M." + FcsMovimientosVariosBean.C_IDMOVIMIENTO + " " + FcsMovimientosVariosBean.C_IDMOVIMIENTO + ","+
							" M." + FcsMovimientosVariosBean.C_CANTIDAD + " " + FcsMovimientosVariosBean.C_CANTIDAD + " "+
							" FROM " + FcsMovimientosVariosBean.T_NOMBRETABLA + " M, " + FcsPagosJGBean.T_NOMBRETABLA + " P, " +
							FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + " A " +
							" WHERE M." + FcsMovimientosVariosBean.C_IDINSTITUCION + " = A." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION + " " +
							" AND M." + FcsMovimientosVariosBean.C_IDMOVIMIENTO + " = A." + FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO + " " +
							" AND M." + FcsMovimientosVariosBean.C_IDINSTITUCION + " = P." + FcsPagosJGBean.C_IDINSTITUCION + " " +
							" AND A." + FcsAplicaMovimientosVariosBean.C_IDPAGOSJG + " = P." + FcsPagosJGBean.C_IDPAGOSJG + " " +
							" AND M." + FcsMovimientosVariosBean.C_IDINSTITUCION + "=" + idInstitucion +
							" AND A." + FcsAplicaMovimientosVariosBean.C_IDPAGOSJG + "=" + idPago +
							" AND M." + FcsMovimientosVariosBean.C_IDPERSONA + "=" + idPersona +" " +
							" ORDER BY  M." + FcsMovimientosVariosBean.C_FECHAALTA ;		
							
		try{
			resultado = (Vector)this.selectGenerico(consulta);
		}catch(Exception e){
			throw new ClsExceptions (e,"Error en FcsMovimientosVarios.getMovimientos()"+consulta);
		}
		
		return resultado;
		
	}
	
	/**
	 * Devuelve un vector con los movimientos varios que hay que pagar para una persona
	 * ordenados por fecha de alta utilizando el pool RW
	 * @param idInstitucion
	 * @param idPago
	 * @param idPersona
	 * @return
	 */
	public Vector getMovimientosRW (String idInstitucion, String idPago, String idPersona) throws ClsExceptions 
	{
		//donde devolveremos el resultado
		Vector resultado = new Vector();
		//query con la select a ejecutar
		String consulta = " SELECT M." + FcsMovimientosVariosBean.C_MOTIVO + " " + FcsMovimientosVariosBean.C_MOTIVO + ","+
							" M." + FcsMovimientosVariosBean.C_DESCRIPCION + " " + FcsMovimientosVariosBean.C_DESCRIPCION + ","+
							" M." + FcsMovimientosVariosBean.C_IDINSTITUCION + " " + FcsMovimientosVariosBean.C_IDINSTITUCION + ","+
							" M." + FcsMovimientosVariosBean.C_IDMOVIMIENTO + " " + FcsMovimientosVariosBean.C_IDMOVIMIENTO + ","+
							" M." + FcsMovimientosVariosBean.C_CANTIDAD + " " + FcsMovimientosVariosBean.C_CANTIDAD + ", "+
							" M." + FcsMovimientosVariosBean.C_IDMOVIMIENTO + " " + FcsMovimientosVariosBean.C_IDMOVIMIENTO + ", "+
							" M." + FcsMovimientosVariosBean.C_FECHAMODIFICACION + " " + FcsMovimientosVariosBean.C_FECHAMODIFICACION + ", "+
							" M." + FcsMovimientosVariosBean.C_USUMODIFICACION + " " + FcsMovimientosVariosBean.C_USUMODIFICACION + " "+
							" FROM " + FcsMovimientosVariosBean.T_NOMBRETABLA + " M, " +
							FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + " A " + 
							" WHERE M." + FcsMovimientosVariosBean.C_IDINSTITUCION + "=A." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION + "(+)" +
							" AND M." + FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO + "=A." + FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO + "(+)" +
							" AND M." + FcsMovimientosVariosBean.C_IDINSTITUCION + "=" + idInstitucion +
							" AND M." + FcsMovimientosVariosBean.C_IDPERSONA + "=" + idPersona +" " +
							" ORDER BY  M." + FcsMovimientosVariosBean.C_FECHAALTA ;		
							
		try{
			resultado = (Vector)super.selectGenerico(consulta);
		}catch(Exception e){
			throw new ClsExceptions (e,"Error en FcsMovimientosVarios.getMovimientosRW()"+consulta);
		}
		
		return resultado;
		
	}
	
	
	/**
	 * Actualiza a NULL el idPagosJG de los movimientos si conincide el idPago del registro con el del método. 
	 * @param idInstitucion
	 * @param idPago
	 * @throws ClsExceptions
	 */
	public void desasignarPago(String idInstitucion, String idPago) throws ClsExceptions {
		FcsMovimientosVariosAdm fcsMovimientosVariosAdm = new FcsMovimientosVariosAdm(this.usrbean);
		Hashtable registroBD, registroCopia;
		String where = null;
		Vector vRegistros;
		FcsMovimientosVariosBean beanBD, beanCopia;

		//query con la select a ejecutar
		String consulta = " SELECT M." + FcsMovimientosVariosBean.C_MOTIVO + " " + FcsMovimientosVariosBean.C_MOTIVO + ","+
							" M." + FcsMovimientosVariosBean.C_DESCRIPCION + " " + FcsMovimientosVariosBean.C_DESCRIPCION + ","+
							" M." + FcsMovimientosVariosBean.C_IDINSTITUCION + " " + FcsMovimientosVariosBean.C_IDINSTITUCION + ","+
							" M." + FcsMovimientosVariosBean.C_IDMOVIMIENTO + " " + FcsMovimientosVariosBean.C_IDMOVIMIENTO + ","+
							" M." + FcsMovimientosVariosBean.C_CANTIDAD + " " + FcsMovimientosVariosBean.C_CANTIDAD + " "+
							" FROM " + FcsMovimientosVariosBean.T_NOMBRETABLA + " M, " +
							FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + " A " + 
							" WHERE M." + FcsMovimientosVariosBean.C_IDINSTITUCION + "=A." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION +
							" AND A." + FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO + "=A." + FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO +
							" AND M." + FcsMovimientosVariosBean.C_IDINSTITUCION + "=" + idInstitucion +
							" AND A." + FcsAplicaMovimientosVariosBean.C_IDPAGOSJG + "=" + idPago +
							" ORDER BY  M." + FcsMovimientosVariosBean.C_FECHAALTA ;		
							
		try{
			vRegistros = (Vector)super.selectGenerico(consulta);
			
			for (int i=0; i<vRegistros.size();i++) {
				beanBD = (FcsMovimientosVariosBean)vRegistros.get(i);
				registroBD = new Hashtable();
				registroBD = (Hashtable)beanBD.getOriginalHash();
				registroCopia = new Hashtable();
				registroCopia = (Hashtable)registroBD.clone();				
				registroCopia.put(FcsAplicaMovimientosVariosBean.C_IDPAGOSJG,"NULL");
				fcsMovimientosVariosAdm.update(registroCopia,registroBD);
			}
		} 
		catch (Exception e) { 
			throw new ClsExceptions (e,"Error en FcsMovimientosVarios.desasignarPago()"); 
		}			
	}
	
	/**
	 * Devuelve el valor del importe que hay que pagar para un colegiado en un pago determinado
	 *   
	 * @param String idInstitucion 
	 * @param String idPago
	 * @param String idPersona
	 * 
	 * @return Hashtable resultado con el importe 
	 */
/*	public Hashtable getImporteMovimientos (String idInstitucion, String idPago, String idPersona)
	{
		Hashtable resultado = new Hashtable();
		String resultado1 = "",resultado2 = "", consulta = "";
		//query para consultar el importe 
		consulta = 	" SELECT " + 
					" SUM(" + FcsMovimientosVariosBean.C_CANTIDAD + ") AS IMPORTE ," +
					" SUM(" + FcsMovimientosVariosBean.C_IMPORTEIRPF + ") AS IRPF " +
					" FROM " + FcsMovimientosVariosBean.T_NOMBRETABLA + " " +
					" WHERE " + FcsMovimientosVariosBean.C_IDINSTITUCION + "=" + idInstitucion + " " +
					" AND " + FcsMovimientosVariosBean.C_IDPAGOSJG + "=" + idPago + " " +
					" AND " + FcsMovimientosVariosBean.C_IDPERSONA + "=" + idPersona + " ";
		
		//Hashtable para recoger el resultado de la contulta
		Hashtable hash = new Hashtable();
		try{
			hash = (Hashtable)((Vector)this.selectGenerico(consulta)).get(0);
			//recogemos el resultado
			resultado1 = (String)hash.get("IMPORTE");
			if (resultado1.equals(""))resultado1="0";
			resultado2 = (String)hash.get("IRPF");
			if (resultado2.equals(""))resultado2="0";
		}catch(Exception e){
			//si no se ha obtenido resultado es porque no hay nada que pagar para el colegiado con ese idPersona
			resultado1 = "0";
			resultado2 = "0";
		}
		resultado.put("IMPORTE",resultado1);
		resultado.put("IRPF",resultado2);
		return resultado;
	}

	
	/**
	 * Metodo que recibiendo un Hashtable con los datos introducidos como criterios de busqueda,
	 * devuelve un String con la sentencia SQL a ejecutar.
	 * 
	 * @param datos con los criterios de busqueda
	 * @return consulta Sql
	 * @throws ClsExceptions
	 */

	public Vector consultaBusqueda (Hashtable datos) throws ClsExceptions{
		String consulta = " select ";
		
		String campos = //Pago Asociado:
						" (select "+ FcsPagosJGBean.T_NOMBRETABLA + "." + FcsPagosJGBean.C_NOMBRE +
						" from " + FcsPagosJGBean.T_NOMBRETABLA + ", " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA +
						" where " + FcsPagosJGBean.T_NOMBRETABLA + "." + FcsPagosJGBean.C_IDINSTITUCION + " = " +
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION +
						" AND " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDPAGOSJG + " = " + 
						FcsPagosJGBean.T_NOMBRETABLA + "." + FcsPagosJGBean.C_IDPAGOSJG +
						" AND " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDINSTITUCION + " = " +
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION +
						" AND " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDMOVIMIENTO + " = " +
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO +
						" AND " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDAPLICACION + " = " +
						" (SELECT MAX(APLICA." + FcsAplicaMovimientosVariosBean.C_IDAPLICACION +
						") FROM " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + " APLICA " +
						" WHERE " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION + " = " +
						" APLICA." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION +
						" AND " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO + " = " +
						" APLICA." + FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO + ")) PAGOASOCIADO," + 						
						//Numero de Colegiado o Comunitario segun proceda:
						" (CASE "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_COMUNITARIO+" WHEN '"+ClsConstants.DB_FALSE+"' THEN "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_NCOLEGIADO+" ELSE "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_NCOMUNITARIO+" END ) AS NUMERO, "+
						//Nombre:
						" (" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 +"||' '||"
							 + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 +"||' '||"
						     + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE + ") NOMBRE," +
						//NIF:
						" " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NIFCIF + " NIF," +
						" " + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_NCOLEGIADO + " NCOLEGIADO," +
						" " + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_NCOMUNITARIO + " NCOMUNITARIO," +
						" " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDINSTITUCION + " IDINSTITUCION," +
						" " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDPERSONA + " IDPERSONA," +
						" " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_CANTIDAD + " CANTIDAD," +
						" (CASE " +
						" WHEN (" + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_CANTIDAD + ") >=   " +
						" (select decode( sum("+
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IMPORTEAPLICADO + "), null, 0, sum( " +
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IMPORTEAPLICADO + "))" +
						" from " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA +
						" where " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDINSTITUCION + "=" +
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION + " and" +
						" " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDMOVIMIENTO + "=" +
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO + ") " +
						" THEN (" + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_CANTIDAD + " - " +
						" (select decode( sum("+
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IMPORTEAPLICADO + "), null, 0, sum( " +
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IMPORTEAPLICADO + "))" +
						" from " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA +
						" where " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDINSTITUCION + "=" +
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION + " and" +
						" " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDMOVIMIENTO + "=" +
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO + ")) " +
						" ELSE (" + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_CANTIDAD + " + " +
						" (select decode( sum("+
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IMPORTEAPLICADO + "), null, 0, sum( " +
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IMPORTEAPLICADO + "))" +
						" from " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA +
						" where " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDINSTITUCION + "=" +
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION + " and" +
						" " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDMOVIMIENTO + "=" +
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO + ")) " +						
						" END) cantidadRestante," + 
						" " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_FECHAALTA + " FECHAALTA," +
						" " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_DESCRIPCION + " MOVIMIENTO," +
						" " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDMOVIMIENTO + " IDMOVIMIENTO," +
						"   ( SELECT  " + FcsPagosEstadosPagosBean.T_NOMBRETABLA + "." + FcsPagosEstadosPagosBean.C_IDESTADOPAGOSJG +" " +
							" FROM " + FcsPagosEstadosPagosBean.T_NOMBRETABLA + ", " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA +
							" WHERE " + FcsPagosEstadosPagosBean.C_FECHAESTADO + " = (SELECT MAX(" + FcsPagosEstadosPagosBean.C_FECHAESTADO + ")" +		
	                            " FROM " + FcsPagosEstadosPagosBean.T_NOMBRETABLA + " " + 
	                            " WHERE " + FcsPagosEstadosPagosBean.C_IDINSTITUCION + " = " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA +"."+ FcsAplicaMovimientosVariosBean.C_IDINSTITUCION + 
	                            " AND " + FcsPagosEstadosPagosBean.C_IDPAGOSJG + " = " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDPAGOSJG + ") " +							
							" AND " + FcsPagosEstadosPagosBean.T_NOMBRETABLA + "." + FcsPagosEstadosPagosBean.C_IDINSTITUCION + " = " +
							FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION +
							" AND " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDPAGOSJG + " = " + 
							FcsPagosEstadosPagosBean.T_NOMBRETABLA + "." + FcsPagosEstadosPagosBean.C_IDPAGOSJG +
							" AND " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDINSTITUCION + " = " +
							FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION +
							" AND " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDMOVIMIENTO + " = " +
							FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO +
							" AND " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDAPLICACION + " = " +
							" (SELECT MAX(APLICA." + FcsAplicaMovimientosVariosBean.C_IDAPLICACION +
							") FROM " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + " APLICA " +
							" WHERE " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION + " = " +
							" APLICA." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION +
							" AND " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO + " = " +
							" APLICA." + FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO + ")) IDESTADOPAGOSJG" ; 		 
                   		
		
		String tablas = " from "+
						" "  + FcsMovimientosVariosBean.T_NOMBRETABLA + 
						", " + CenColegiadoBean.T_NOMBRETABLA + 
						", " + CenPersonaBean.T_NOMBRETABLA; 
		
		String where =  " where " + 
						" " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + "=" + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDPERSONA +
						" and " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDINSTITUCION + "=" + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDINSTITUCION + " (+) " +
						" and " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDPERSONA + "=" + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDPERSONA + " (+) " +
						" and " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDINSTITUCION + "=" + (String)datos.get("IDINSTITUCION");
		
		//con estos datos construir en trozo de sentencia sql correspondiente
		String nif = (String)datos.get("NIF");
		if (nif != null && !nif.equals("")) {
			where += " and "+ComodinBusquedas.prepararSentenciaCompleta(nif.trim(),CenPersonaBean.T_NOMBRETABLA + "." +CenPersonaBean.C_NIFCIF )+ " ";
		}
		
		String ncolegiado = (String)datos.get("NCOLEGIADO");
		if (ncolegiado != null && !ncolegiado.equals("")) {
			where += " and "+ ComodinBusquedas.tratarNumeroColegiado(ncolegiado,CenColegiadoBean.T_NOMBRETABLA + "."+ CenColegiadoBean.C_NCOLEGIADO )+ " ";
		}
		
		String nombre = (String)datos.get("NOMBRE");
		if (nombre != null && !nombre.equals("")) {
			where += " and "+ComodinBusquedas.prepararSentenciaCompleta(nombre.trim(),FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_DESCRIPCION ) + " ";
		}

		String idPago = (String)datos.get("PAGOASOCIADO");
		if (idPago != null && !idPago.equals("")) {
			where += " and " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDMOVIMIENTO + " in" + 
			" (SELECT APLICA." + FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO +
			" FROM " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + " APLICA " +
			" WHERE " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDINSTITUCION + " = " +
			" APLICA." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION +
			" AND aplica." + FcsAplicaMovimientosVariosBean.C_IDPAGOSJG + "=" + idPago + ") "; 	
		}

		String idPersona = (String)datos.get("IDPERSONA");
		if (idPersona != null && !idPersona.equals("")) {
			where += " and "+ CenPersonaBean.T_NOMBRETABLA + "." +CenPersonaBean.C_IDPERSONA + " = " + idPersona.trim() + " ";
		}
		
		consulta += campos + tablas + where;
		
		boolean checkHistorico =UtilidadesString.stringToBoolean((String)datos.get("CHECKHISTORICO"));
		
		if (checkHistorico == false) {
			
			String consulta2 = " minus select ";
		
			String campos2 = //Pago Asociado:
						" (select "+ FcsPagosJGBean.T_NOMBRETABLA + "." + FcsPagosJGBean.C_NOMBRE +
						" from " + FcsPagosJGBean.T_NOMBRETABLA + ", " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA +
						" where " + FcsPagosJGBean.T_NOMBRETABLA + "." + FcsPagosJGBean.C_IDINSTITUCION + " = " +
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION +
						" AND " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDPAGOSJG + " = " + 
						FcsPagosJGBean.T_NOMBRETABLA + "." + FcsPagosJGBean.C_IDPAGOSJG +
						" AND " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDINSTITUCION + " = " +
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION +
						" AND " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDMOVIMIENTO + " = " +
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO +
						" AND " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDAPLICACION + " = " +
						" (SELECT MAX(APLICA." + FcsAplicaMovimientosVariosBean.C_IDAPLICACION +
						") FROM " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + " APLICA " +
						" WHERE " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION + " = " +
						" APLICA." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION +
						" AND " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO + " = " +
						" APLICA." + FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO + ")) PAGOASOCIADO," +
						//Numero de Colegiado o Comunitario segun proceda:
						" (CASE "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_COMUNITARIO+" WHEN '"+ClsConstants.DB_FALSE+"' THEN "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_NCOLEGIADO+" ELSE "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_NCOMUNITARIO+" END ) AS NUMERO, "+
						//Nombre:
						" (" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 +"||' '||"
							 + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 +"||' '||"
						     + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE + ") NOMBRE," +
						//NIF:
						" " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NIFCIF + " NIF," +
						" " + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_NCOLEGIADO + " NCOLEGIADO," +
						" " + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_NCOMUNITARIO + " NCOMUNITARIO," +
						" " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDINSTITUCION + " IDINSTITUCION," +
						" " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDPERSONA + " IDPERSONA," +
						" " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_CANTIDAD + " CANTIDAD," +
						" (CASE " +
						" WHEN (" + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_CANTIDAD + ") >=   " +
						" (select decode( sum("+
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IMPORTEAPLICADO + "), null, 0, sum( " +
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IMPORTEAPLICADO + "))" +
						" from " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA +
						" where " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDINSTITUCION + "=" +
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION + " and" +
						" " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDMOVIMIENTO + "=" +
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO + ") " +
						" THEN (" + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_CANTIDAD + " - " +
						" (select decode( sum("+
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IMPORTEAPLICADO + "), null, 0, sum( " +
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IMPORTEAPLICADO + "))" +
						" from " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA +
						" where " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDINSTITUCION + "=" +
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION + " and" +
						" " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDMOVIMIENTO + "=" +
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO + ")) " +
						" ELSE (" + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_CANTIDAD + " + " +
						" (select decode( sum("+
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IMPORTEAPLICADO + "), null, 0, sum( " +
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IMPORTEAPLICADO + "))" +
						" from " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA +
						" where " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDINSTITUCION + "=" +
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION + " and" +
						" " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDMOVIMIENTO + "=" +
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO + ")) " +						
						" END) cantidadRestante," + 					
						" " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_FECHAALTA + " FECHAALTA," +
						" " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_DESCRIPCION + " MOVIMIENTO," +
						" " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDMOVIMIENTO + " IDMOVIMIENTO," +
						"   ( SELECT  " + FcsPagosEstadosPagosBean.T_NOMBRETABLA + "." + FcsPagosEstadosPagosBean.C_IDESTADOPAGOSJG +" " +
						" FROM " + FcsPagosEstadosPagosBean.T_NOMBRETABLA + ", " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA +
						" WHERE " + FcsPagosEstadosPagosBean.C_FECHAESTADO + " = (SELECT MAX(" + FcsPagosEstadosPagosBean.C_FECHAESTADO + ")" +		
                            " FROM " + FcsPagosEstadosPagosBean.T_NOMBRETABLA + " " + 
                            " WHERE " + FcsPagosEstadosPagosBean.C_IDINSTITUCION + " = " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA +"."+ FcsAplicaMovimientosVariosBean.C_IDINSTITUCION + 
                            " AND " + FcsPagosEstadosPagosBean.C_IDPAGOSJG + " = " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDPAGOSJG + ") " +							
						" AND " + FcsPagosEstadosPagosBean.T_NOMBRETABLA + "." + FcsPagosEstadosPagosBean.C_IDINSTITUCION + " = " +
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION +
						" AND " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDPAGOSJG + " = " + 
						FcsPagosEstadosPagosBean.T_NOMBRETABLA + "." + FcsPagosEstadosPagosBean.C_IDPAGOSJG +
						" AND " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDINSTITUCION + " = " +
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION +
						" AND " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDMOVIMIENTO + " = " +
						FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO +
						" AND " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDAPLICACION + " = " +
						" (SELECT MAX(APLICA." + FcsAplicaMovimientosVariosBean.C_IDAPLICACION +
						") FROM " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + " APLICA " +
						" WHERE " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION + " = " +
						" APLICA." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION +
						" AND " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO + " = " +
						" APLICA." + FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO + ")) IDESTADOPAGOSJG" ; 		 

		
			String tablas2 = " from "+
						" "  + FcsMovimientosVariosBean.T_NOMBRETABLA + 
						", " + CenColegiadoBean.T_NOMBRETABLA + 
						", " + CenPersonaBean.T_NOMBRETABLA ;
		
			String where2 =  " where " + 
						" " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + "=" + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDPERSONA +
						" and " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDINSTITUCION + "=" + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDINSTITUCION + " (+) " +
						" and " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDPERSONA + "=" + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDPERSONA + " (+) " +
						" and " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_CANTIDAD + " = (select sum(aplica." + FcsAplicaMovimientosVariosBean.C_IMPORTEAPLICADO + ")  " +
						" from " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + " aplica, " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + " APLICA2 where aplica." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION + "= APLICA2." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION +
						" and aplica." + FcsAplicaMovimientosVariosBean.C_IDAPLICACION + " = APLICA2." + FcsAplicaMovimientosVariosBean.C_IDAPLICACION + ")" +
						" and " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDINSTITUCION + "=" + (String)datos.get("IDINSTITUCION");
		
		//con estos datos construir en trozo de sentencia sql correspondiente
			if (nif != null && !nif.equals("")) {
				where += " and "+ComodinBusquedas.prepararSentenciaCompleta(nif.trim(),CenPersonaBean.T_NOMBRETABLA + "." +CenPersonaBean.C_NIFCIF )+ " ";
			}

			if (ncolegiado != null && !ncolegiado.equals("")) {
				where += " and "+ ComodinBusquedas.tratarNumeroColegiado(ncolegiado,CenColegiadoBean.T_NOMBRETABLA + "."+ CenColegiadoBean.C_NCOLEGIADO )+ " ";
			}
		
			if (nombre != null && !nombre.equals("")) {
				where += " and "+ComodinBusquedas.prepararSentenciaCompleta(nombre.trim(),FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_DESCRIPCION ) + " ";
			}

			if (idPago != null && !idPago.equals("")) {
				where += " and " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDMOVIMIENTO + " in" + 
				" (SELECT APLICA." + FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO +
				" FROM " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + " APLICA " +
				" WHERE " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDINSTITUCION + " = " +
				" APLICA." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION +
				" AND aplica." + FcsAplicaMovimientosVariosBean.C_IDPAGOSJG + "=" + idPago + ") "; 	
			}

			if (idPersona != null && !idPersona.equals("")) {
				where += " and "+ CenPersonaBean.T_NOMBRETABLA + "." +CenPersonaBean.C_IDPERSONA + " = " + idPersona.trim() + " ";
			}
			
			consulta += consulta2 + campos2 + tablas2 + where2;
		}

		consulta += " ORDER BY nombre, fechaalta, cantidad DESC ";
		
		return this.selectGenerico(consulta);
	}

	/**
	 * Metodo que recibiendo un Hashtable con los siguientes datos; idinstitución, idpersona, idmovimiento
	 * devuelve las aplicaciones del movimiento para una persona
	 * 
	 * @param datos con los criterios de busqueda
	 * @return consulta Sql
	 * @throws ClsExceptions
	 */

	public Vector consultaAplicacion (Hashtable datos) throws ClsExceptions{
		
		
		String consulta = " select ";
		
		String campos = //Pago Asociado:
						" (select nombre "+
						" from fcs_pagosjg "+
						" where idinstitucion =   FCS_APLICA_MOVIMIENTOSVARIOS.idinstitucion"+ 
						" and idpagosjg = FCS_APLICA_MOVIMIENTOSVARIOS.idpagosjg) PAGOASOCIADO,"+
						" " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IMPORTEAPLICADO + " IMPORTEAPLICADO";

		
		String tablas = " from "+
						" "  + FcsMovimientosVariosBean.T_NOMBRETABLA + 
						", " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + " "; 
			
		String where =  " where " + 
						FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDINSTITUCION + "=" + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION + 
						" and " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDMOVIMIENTO + "=" + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO + 
						" and " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDINSTITUCION + "=" + (String)datos.get("IDINSTITUCION");
		
		String idPersona = (String)datos.get("IDPERSONA");
		if (idPersona != null && !idPersona.equals("")) {
			where += " and " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDPERSONA + "=" + (String)datos.get("IDPERSONA") ;
		} else {
			where += " and " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDPERSONA + "= null" ;
		}
		String idMovimiento = (String)datos.get("IDMOVIMIENTO");
		if (idMovimiento != null && !idMovimiento.equals("")) {
			where += " and " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDMOVIMIENTO + "=" + (String)datos.get("IDMOVIMIENTO");
		} else {
			where += " and " + FcsMovimientosVariosBean.T_NOMBRETABLA + "." + FcsMovimientosVariosBean.C_IDMOVIMIENTO + "= null";
		}
 
						

		
	
		consulta += campos + tablas + where;
		
		consulta += " ORDER BY cantidad DESC ";
		
		return this.selectGenerico(consulta);
	}
	
	
	/**
	 * Actualizar el idpagojg, el importeIRPF y el porcentajeIRPF 
	 * de los movimientos de un colegiado que no tengan pago asociado.
	 * @param idInstitucion
	 * @param idPago
	 * @param idPersona
	 * @param porcentajeIRPF
	 * @return
	 * @throws ClsExceptions
	 */
/*	public boolean updatePago(
			String idInstitucion, String idPago, String idPersona, String porcentajeIRPF) throws ClsExceptions {
		//query con la select a ejecutar
		StringBuffer consulta = new StringBuffer();
		consulta.append("UPDATE FCS_MOVIMIENTOSVARIOS SET IDPAGOSJG = ");
		consulta.append(idPago);
		consulta.append(" , PORCENTAJEIRPF = ");
		consulta.append(porcentajeIRPF);
		consulta.append(" , IMPORTEIRPF = CANTIDAD * " +porcentajeIRPF+" / 100 ");
		consulta.append(" WHERE IDINSTITUCION = ");
		consulta.append(idInstitucion);
		consulta.append(" AND IDPERSONA = ");
		consulta.append(idPersona);
		consulta.append(" AND IDPAGOSJG IS NULL");		
		int resultado;				
		try{
			resultado = ClsMngBBDD.executeUpdate(consulta.toString());
		}catch(Exception e){
			throw new ClsExceptions (e,"Error en FcsMovimientosVarios.getMovimientos()"+consulta);
		}
		return (resultado > 0);
	}*/
}