/*
 * VERSIONES:
 * 
 * carlos.vidal - 08-03-2005 - Creacion
 *	
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.EjecucionPLs;
import com.siga.general.SIGAException;

/**
*
* Clase que gestiona la tabla CenHistorico de la BBDD
* 
*/
public class FacPagosPorCajaAdm extends MasterBeanAdministrador {

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public FacPagosPorCajaAdm(UsrBean usu) {
		super(FacPagosPorCajaBean.T_NOMBRETABLA, usu);
	}

	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {	
				FacPagosPorCajaBean.C_IDINSTITUCION	   	,
				FacPagosPorCajaBean.C_IDFACTURA         ,
				FacPagosPorCajaBean.C_IDPAGOPORCAJA     ,
				FacPagosPorCajaBean.C_FECHA             ,
				FacPagosPorCajaBean.C_CONTABILIZADO     ,
				FacPagosPorCajaBean.C_FECHAMODIFICACION ,
				FacPagosPorCajaBean.C_USUMODIFICACION   ,
				FacPagosPorCajaBean.C_IMPORTE           ,
				FacPagosPorCajaBean.C_TARJETA           ,
				FacPagosPorCajaBean.C_IDABONO           ,
				FacPagosPorCajaBean.C_IDPAGOABONO,
				FacPagosPorCajaBean.C_OBSERVACIONES,
				FacPagosPorCajaBean.C_NUMEROAUTORIZACION,
				FacPagosPorCajaBean.C_TIPOAPUNTE,
				FacPagosPorCajaBean.C_REFERENCIA};

		return campos;
	}
	
	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {FacPagosPorCajaBean.C_IDINSTITUCION	   	,
				FacPagosPorCajaBean.C_IDFACTURA         ,
				FacPagosPorCajaBean.C_IDPAGOPORCAJA     ,
				};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}	

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysFormaPagoBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacPagosPorCajaBean bean = null;
		
		try {
			bean = new FacPagosPorCajaBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,FacPagosPorCajaBean.C_IDINSTITUCION));
			bean.setIdFactura(UtilidadesHash.getString(hash,FacPagosPorCajaBean.C_IDFACTURA));
			bean.setIdPagoPorCaja(UtilidadesHash.getInteger(hash,FacPagosPorCajaBean.C_IDPAGOPORCAJA));
			bean.setFecha(UtilidadesHash.getString(hash,FacPagosPorCajaBean.C_FECHA));
			bean.setContabilizado(UtilidadesHash.getString(hash,FacPagosPorCajaBean.C_CONTABILIZADO));
			bean.setImporte(UtilidadesHash.getDouble(hash,FacPagosPorCajaBean.C_IMPORTE));
			bean.setObservaciones(UtilidadesHash.getString(hash,FacPagosPorCajaBean.C_OBSERVACIONES));
			bean.setTarjeta(UtilidadesHash.getString(hash,FacPagosPorCajaBean.C_TARJETA));
			bean.setTipoApunte(UtilidadesHash.getString(hash,FacPagosPorCajaBean.C_TIPOAPUNTE));
			bean.setIdAbono(UtilidadesHash.getInteger(hash,FacPagosPorCajaBean.C_IDABONO));
			bean.setIdPagoAbono(UtilidadesHash.getInteger(hash,FacPagosPorCajaBean.C_IDPAGOABONO));		
			bean.setNumeroAutorizacion(UtilidadesHash.getInteger(hash,FacPagosPorCajaBean.C_NUMEROAUTORIZACION));		
			bean.setReferencia(UtilidadesHash.getLong(hash,FacPagosPorCajaBean.C_REFERENCIA));		
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
			FacPagosPorCajaBean b = (FacPagosPorCajaBean) bean;
			UtilidadesHash.set(htData,FacPagosPorCajaBean.C_IDINSTITUCION   ,b.getIdInstitucion     ());
			UtilidadesHash.set(htData,FacPagosPorCajaBean.C_IDFACTURA       ,b.getIdFactura         ());
			UtilidadesHash.set(htData,FacPagosPorCajaBean.C_IDPAGOPORCAJA   ,b.getIdPagoPorCaja     ());
			UtilidadesHash.set(htData,FacPagosPorCajaBean.C_FECHA           ,b.getFecha             ());
			UtilidadesHash.set(htData,FacPagosPorCajaBean.C_CONTABILIZADO   ,b.getContabilizado     ());
			UtilidadesHash.set(htData,FacPagosPorCajaBean.C_IMPORTE         ,b.getImporte           ());
			UtilidadesHash.set(htData,FacPagosPorCajaBean.C_TARJETA         ,b.getTarjeta           ());
			UtilidadesHash.set(htData,FacPagosPorCajaBean.C_TIPOAPUNTE      ,b.getTipoApunte		());
			UtilidadesHash.set(htData,FacPagosPorCajaBean.C_IDABONO         ,b.getIdAbono           ());
			UtilidadesHash.set(htData,FacPagosPorCajaBean.C_IDPAGOABONO     ,b.getIdPagoAbono       ());
			UtilidadesHash.set(htData,FacPagosPorCajaBean.C_OBSERVACIONES   ,b.getObservaciones     ());
			UtilidadesHash.set(htData,FacPagosPorCajaBean.C_NUMEROAUTORIZACION,b.getNumeroAutorizacion());
			UtilidadesHash.set(htData,FacPagosPorCajaBean.C_REFERENCIA   	,b.getReferencia		());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	/** 
	 * Obtiene el valor IDABONO, <br/>
	 * @param  institucion - identificador de la institucion 
	 * @return  Integer - Identificador del abono  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Integer getNuevoID (Integer institucion, String idFactura) throws ClsExceptions, SIGAException 
	{
		Integer resultado = null;
		
		try { 
            RowsContainer rc = new RowsContainer();

			String sql;
			sql ="SELECT NVL(MAX(" + FacPagosPorCajaBean.C_IDPAGOPORCAJA + "), 0) + 1 AS " + FacPagosPorCajaBean.C_IDPAGOPORCAJA + " FROM " + nombreTabla +
				" WHERE " + FacPagosPorCajaBean.C_IDINSTITUCION + " = " + institucion + 
				" AND " + FacPagosPorCajaBean.C_IDFACTURA + " = '" + idFactura + "'";   
		
			if (rc.findForUpdate(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get(FacPagosPorCajaBean.C_IDPAGOPORCAJA).equals("")) {
					resultado = new Integer(1);
				}
				else 
					resultado = UtilidadesHash.getInteger(prueba, FacPagosPorCajaBean.C_IDPAGOPORCAJA);
			}
		}	
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'getNuevoID' en B.D.");		
		}
		return resultado;
	}
	
	/**
	 * JPT: Creo una sentencia que vaya a reutilizar
	 * Obtiene la sentencia sql que obtiene todas las lineas de los pagos de una factura
	 * @param idInstitucion
	 * @param idFactura
	 * @param idPersona
	 * @param esProcesoMasivo
	 * @return
	 * @throws SIGAException
	 */
	public String getQueryPagos (Integer idInstitucion, String idFactura, Long idPersona, boolean esProcesoMasivo)  throws ClsExceptions, SIGAException {
		
		/* JPT: Cambios al aplicar comisiones en facturas (anula la anterior y crea una nueva con la comision)
		 * - Si es masivo ya tiene el listado final de facturas
		 * - En otro caso (pestaña de pagos de una factura), hay que obtener una lista con las facturas relacionadas por comision de la factura actual
		 */
		String listaFacturasComision = (esProcesoMasivo ? idFactura : EjecucionPLs.obtenerListaFacturasComision(idInstitucion.toString(), idFactura));
		
		StringBuilder consulta = new StringBuilder();  
		
		if (esProcesoMasivo) {
			consulta.append("select TO_CHAR(MAX(FECHA), '");
			consulta.append(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
			consulta.append("') AS ULTIMAFECHA FROM ");
			
			consulta.append("(select case when facHist.Idtipoaccion = 1 ");
			consulta.append("             then fac.fechaemision ");
			consulta.append("             else facHist.fechamodificacion ");
			consulta.append("         end AS Fecha ");
		} else {
			consulta.append("select facHist.IDFACTURA, facHist.IDINSTITUCION, facHist.Idhistorico, facHist.Idtipoaccion, ");
			consulta.append("       case when  (facHist.Idtipoaccion =1) then ");
			consulta.append("           (f_Siga_Getrecurso(facTipoAcc.nombre, 1) || ' (' || fac.numerofactura || ')')  ");
			consulta.append("           when (facHist.Idtipoaccion =6) then ");
			consulta.append("            (f_Siga_Getrecurso(facTipoAcc.nombre, 1) || ' (' || (select  Fac_Lineadevoludisqbanco.Descripcionmotivos  ");
			consulta.append("	            from Fac_Lineadevoludisqbanco  ");
			consulta.append("	                  where facHist.iddisquetedevoluciones = Fac_Lineadevoludisqbanco.iddisquetedevoluciones ");
			consulta.append("                               and facHist.Idinstitucion = Fac_Lineadevoludisqbanco.idinstitucion ");
			consulta.append("                               and facHist.Idrecibo = Fac_Lineadevoludisqbanco.Idrecibo ");
			consulta.append("                               and facHist.Idhistorico = facHist.Idhistorico ");
			consulta.append("                               ) || ')')     ");
			
			consulta.append("            when (facHist.Idtipoaccion =7) then ");
			consulta.append("             (f_Siga_Getrecurso(facTipoAcc.nombre, 1) || ' ' || (select Renegociacion.Comentario  ");
			consulta.append("                         from  Fac_Renegociacion renegociacion ");
			consulta.append("                         where facHist.Idrenegociacion= renegociacion.idrenegociacion "); 
			consulta.append("                               and facHist.Idinstitucion = renegociacion.idinstitucion ");
			consulta.append("                               and facHist.Idfactura = renegociacion.idfactura))    ");
			consulta.append("              when (facHist.Idtipoaccion =9) then ");
			consulta.append("                  (select  (f_Siga_Getrecurso_Etiqueta('facturacion.pagosFactura.accion.anulacion', 1) || ' (' || ");
			consulta.append("                          f_Siga_Getrecurso_Etiqueta('facturacion.pagosFactura.accion.anulacionComision', 1) ) from dual) ");
			consulta.append("              when (facHist.Idtipoaccion =10) then ");
			consulta.append("                    (f_Siga_Getrecurso_Etiqueta('facturacion.pagosFactura.accion.compensacion', 1) ) ");
			consulta.append("           else ");
			consulta.append("              f_Siga_Getrecurso(facTipoAcc.nombre, 1) ");
			consulta.append("       end AS TABLA, ");
			
			consulta.append("       f_Siga_Getrecurso_Etiqueta(facEstado.descripcion, 1) AS Estado, ");
			
			consulta.append("       case when facHist.Idtipoaccion = 1 then ");
			consulta.append("              fac.fechaemision ");
			consulta.append("            when facHist.Idtipoaccion = 2 And fachist.Comisionidfactura Is Null then ");
			consulta.append("              (Select Facpro.Fechaconfirmacion ");
			consulta.append("                 From Fac_Facturacionprogramada Facpro ");
			consulta.append("                Where Facpro.Idinstitucion = Fac.Idinstitucion ");
			consulta.append("                  And Facpro.Idseriefacturacion = Fac.Idseriefacturacion ");
			consulta.append("                  And Facpro.Idprogramacion = Fac.Idprogramacion) ");
			consulta.append("            when facHist.Idtipoaccion = 2 And fachist.Comisionidfactura Is Not Null then ");
			consulta.append("              fac.fechaemision ");
			consulta.append("            when facHist.Idtipoaccion in (4,10) then ");
			consulta.append("              (Select Fac_Pagosporcaja.Fecha ");
			consulta.append("                 from fac_pagosporcaja ");
			consulta.append("                where facHist.Idfactura = fac_pagosporcaja.Idfactura  ");
			consulta.append("                  and facHist.Idinstitucion = fac_pagosporcaja.idinstitucion ");
			consulta.append("                  and facHist.Idpagoporcaja = fac_pagosporcaja.idpagoporcaja) ");
			consulta.append("            when facHist.Idtipoaccion = 5 then ");
			consulta.append("              (Select Dis.Fechacreacion ");
			consulta.append("                 From Fac_Disquetecargos Dis ");
			consulta.append("                Where Fachist.Idinstitucion = Dis.Idinstitucion ");
			consulta.append("                  And Fachist.Iddisquetecargos = Dis.Iddisquetecargos) ");
			consulta.append("            when facHist.Idtipoaccion = 6 then ");
			consulta.append("              (Select Dev.Fechageneracion ");
			consulta.append("                 From Fac_Disquetedevoluciones Dev ");
			consulta.append("                Where Fachist.Idinstitucion = Dev.Idinstitucion ");
			consulta.append("                  And Fachist.Iddisquetedevoluciones = Dev.Iddisquetedevoluciones) ");
			consulta.append("            when facHist.Idtipoaccion = 7 then ");
			consulta.append("              (Select Ren.Fecharenegociacion ");
			consulta.append("                 From Fac_Renegociacion Ren ");
			consulta.append("                Where Fachist.Idinstitucion = Ren.Idinstitucion ");
			consulta.append("                  And Fachist.Idfactura = Ren.Idfactura ");
			consulta.append("                  And Fachist.Idrenegociacion = Ren.Idrenegociacion) ");
			consulta.append("            when facHist.Idtipoaccion in (8,9) then ");
			consulta.append("              (Select Abo.Fecha ");
			consulta.append("                 From Fac_Abono Abo ");
			consulta.append("                Where Abo.Idinstitucion = Fachist.Idinstitucion ");
			consulta.append("                  And Abo.Idabono = Fachist.Idabono) ");			
			consulta.append("            else ");
			consulta.append("              facHist.fechamodificacion ");
			consulta.append("       end AS Fecha, ");
			
			consulta.append("       case when  (facHist.Idtipoaccion =1 or facHist.Idtipoaccion =2) then ");
			consulta.append("                nvl( fac.Imptotal,0.0)  ");
			consulta.append("            when (facHist.Idtipoaccion =3) then ");
			consulta.append("               nvl(  facHist.Imptotalanticipado,0.0) ");
			consulta.append("            when (facHist.Idtipoaccion =4 or facHist.Idtipoaccion =10) then ");
			consulta.append("                nvl(  (select Fac_Pagosporcaja.Importe from fac_pagosporcaja ");
			consulta.append("                         where facHist.Idfactura = fac_pagosporcaja.Idfactura  ");
			consulta.append("                               and facHist.Idinstitucion = fac_pagosporcaja.idinstitucion ");
			consulta.append("                               and fac_pagosporcaja.Idpagoporcaja = facHist.idpagoporcaja ");
			consulta.append("                             ),0.0) ");
			consulta.append("            when (facHist.Idtipoaccion =5 ) then  ");               
			consulta.append("                  nvl( (select Incluidadisquete.importe from Fac_Facturaincluidaendisquete Incluidadisquete ");
			consulta.append("                where facHist.Idinstitucion = Incluidadisquete.idinstitucion ");
			consulta.append("                      and Incluidadisquete.Iddisquetecargos = facHist.iddisquetecargos ");
			consulta.append("                      and Incluidadisquete.Idfacturaincluidaendisquete = facHist.idfacturaincluidaendisquete ");
			consulta.append("                     ),0.0) ");
			consulta.append("             when (facHist.Idtipoaccion=6) then   ");
			consulta.append("              nvl( facHist.imptotalporpagar,0.0) ");
			consulta.append("             when (facHist.Idtipoaccion=7) then  ");
			consulta.append("                 nvl(  (select Renegociacion.Importe  ");
			consulta.append("                         from  Fac_Renegociacion renegociacion ");
			consulta.append("                         where facHist.Idrenegociacion= renegociacion.idrenegociacion  ");
			consulta.append("                               and facHist.Idinstitucion = renegociacion.idinstitucion ");
			consulta.append("                               and facHist.Idfactura = renegociacion.idfactura ");
			consulta.append("                          ),0.0) ");
			consulta.append("            when (facHist.Idtipoaccion=8) then  ");
			consulta.append("                 nvl(  (select abono.imptotal  ");
			consulta.append("                         from  Fac_Abono abono ");
			consulta.append("                         where facHist.idabono= abono.idabono ");
			consulta.append("                               and facHist.Idinstitucion = abono.idinstitucion  ");
			consulta.append("	                               ),0.0)  ");
			consulta.append("            when (facHist.Idtipoaccion=9) then  ");
			consulta.append("                 nvl(  fac.Imptotalporpagar,0.0) ");
			consulta.append("       end As IMPORTE, ");
			
			consulta.append("       fac.Idfactura As Idfactura,  ");
			
			consulta.append("       case when (facHist.Idtipoaccion =1 or facHist.Idtipoaccion =2 or facHist.Idtipoaccion =3 or facHist.Idtipoaccion =4 ");
			consulta.append("                                       or facHist.Idtipoaccion =5 or facHist.Idtipoaccion =6 or facHist.Idtipoaccion =7 or facHist.Idtipoaccion =8 or facHist.Idtipoaccion =10) then ");
			consulta.append("              '' ");
			consulta.append("            when (facHist.Idtipoaccion =9) then ");
			consulta.append("               TO_CHAR(1) ");
			consulta.append("       end As Anulacioncomision, ");
			
			consulta.append("       case when (facHist.Idtipoaccion =1 or facHist.Idtipoaccion =2 or facHist.Idtipoaccion =3 or facHist.Idtipoaccion =4 or facHist.Idtipoaccion =7 or facHist.Idtipoaccion =8 ");
			consulta.append("                 or facHist.Idtipoaccion =9 or facHist.Idtipoaccion =10) then ");
			consulta.append("              '' ");
			consulta.append("               when (facHist.Idtipoaccion =5 or facHist.Idtipoaccion =6) then ");
			consulta.append("                    (select Incluidadisquete.Devuelta from Fac_Facturaincluidaendisquete Incluidadisquete ");
			consulta.append("                       where facHist.Idinstitucion = Incluidadisquete.idinstitucion ");
			consulta.append("                             and Incluidadisquete.Iddisquetecargos = facHist.iddisquetecargos ");
			consulta.append("                             and Incluidadisquete.Idfacturaincluidaendisquete = facHist.idfacturaincluidaendisquete ");
			consulta.append("                           ) ");
			consulta.append("       end As Devuelta, ");
			
			consulta.append("       case when (facHist.Idtipoaccion =1 or facHist.Idtipoaccion =2 or facHist.Idtipoaccion =3 or facHist.Idtipoaccion =5 or facHist.Idtipoaccion =6 or facHist.Idtipoaccion =7 ");
			consulta.append("                  or facHist.Idtipoaccion =8 or facHist.Idtipoaccion =9 or facHist.Idtipoaccion =10) then ");
			consulta.append("              '' ");
			consulta.append("            when (facHist.Idtipoaccion =4) then ");
			consulta.append("                (select Fac_Pagosporcaja.Tarjeta from fac_pagosporcaja ");
			consulta.append("                       where facHist.Idfactura = fac_pagosporcaja.Idfactura  ");
			consulta.append("                             and facHist.Idinstitucion = fac_pagosporcaja.idinstitucion ");
			consulta.append("                             and fac_pagosporcaja.Idpagoporcaja = facHist.idpagoporcaja ");
			consulta.append("                            ) ");
			consulta.append("       end As Tarjeta, ");
			
			consulta.append("       case when (facHist.Idtipoaccion =1 or facHist.Idtipoaccion =2 or facHist.Idtipoaccion =3 or facHist.Idtipoaccion =7) then ");
			consulta.append("              0 ");
			consulta.append("            when (facHist.Idtipoaccion =4) then ");
			consulta.append("                 (select Fac_Pagosporcaja.Idabono from fac_pagosporcaja ");
			consulta.append("                        where facHist.Idfactura = fac_pagosporcaja.Idfactura ");
			consulta.append("                              and facHist.Idinstitucion = fac_pagosporcaja.idinstitucion ");
			consulta.append("                              and fac_pagosporcaja.Idpagoporcaja = facHist.idpagoporcaja ");
			consulta.append("                             ) ");
			consulta.append("            when (facHist.Idtipoaccion =5 or facHist.Idtipoaccion =6) then ");
			consulta.append("                 (select Incluidadisquete.Idcuenta from Fac_Facturaincluidaendisquete Incluidadisquete ");
			consulta.append("                       where facHist.Idinstitucion = Incluidadisquete.idinstitucion ");
			consulta.append("                             and Incluidadisquete.Iddisquetecargos = facHist.iddisquetecargos ");
			consulta.append("                             and Incluidadisquete.Idfacturaincluidaendisquete = facHist.idfacturaincluidaendisquete ");
			consulta.append("                            ) ");
			consulta.append("            when (facHist.Idtipoaccion =8 or facHist.Idtipoaccion =9) then ");
			consulta.append("                 fac.Idcuenta ");
			consulta.append("            when (facHist.Idtipoaccion =10) then ");
			consulta.append("                 (select fac_abono.idcuenta from fac_abono ");
			consulta.append("                     where facHist.idinstitucion=fac_abono.idinstitucion ");
			consulta.append("                           and facHist.idabono =  fac_abono.idabono ");
			consulta.append("                           )  ");
			consulta.append("       end As Idabono_Idcuenta, ");
			
			consulta.append("       case when (facHist.Idtipoaccion =1 or facHist.Idtipoaccion =2 or facHist.Idtipoaccion =3 or facHist.Idtipoaccion =5 or facHist.Idtipoaccion =6 or facHist.Idtipoaccion =7 ");
			consulta.append("                 or facHist.Idtipoaccion =9) then ");
			consulta.append("              '' ");
			consulta.append("            when (facHist.Idtipoaccion =4) then ");
			consulta.append("                  TO_CHAR(facHist.Idabono) ");
			consulta.append("            when (facHist.Idtipoaccion =8 or facHist.Idtipoaccion =10) then ");
			consulta.append("                 (select abono.Numeroabono ");
			consulta.append("                        from  Fac_Abono abono ");
			consulta.append("                        where facHist.idabono= abono.idabono  ");
			consulta.append("                              and facHist.Idinstitucion = abono.idinstitucion ");
			consulta.append("                              ) ");
			consulta.append("       end As Numeroabono, ");
		     
			consulta.append("       case when (facHist.Idtipoaccion =1 or facHist.Idtipoaccion =2 or facHist.Idtipoaccion =3 or facHist.Idtipoaccion =5 ");
			consulta.append("                  or facHist.Idtipoaccion =6 or facHist.Idtipoaccion =7 or facHist.Idtipoaccion=8 or facHist.Idtipoaccion=9) then ");
			consulta.append("              0 ");
			consulta.append("            when (facHist.Idtipoaccion =4) then ");
			consulta.append("                  facHist.idpagoporcaja ");
			consulta.append("            when (facHist.Idtipoaccion =10) then ");
			consulta.append("                  (select Fac_Pagosporcaja.Idpagoporcaja from Fac_Pagosporcaja ");
			consulta.append("                   where facHist.idinstitucion = Fac_Pagosporcaja.Idinstitucion ");
			consulta.append("                         and facHist.idfactura = Fac_Pagosporcaja.Idfactura ");
			consulta.append("                         and facHist.idpagoporcaja = Fac_Pagosporcaja.Idpagoporcaja ");
			consulta.append("                        ) ");
			consulta.append("       end As Idpago, ");
			
			consulta.append("       case when (facHist.Idtipoaccion =1 or facHist.Idtipoaccion =2 or facHist.Idtipoaccion =3 or facHist.Idtipoaccion =4) then ");
			consulta.append("              '' ");
			consulta.append("            when (facHist.Idtipoaccion =5 or facHist.Idtipoaccion =6 or facHist.Idtipoaccion =8 or facHist.Idtipoaccion =9 or facHist.Idtipoaccion =10) then ");
			consulta.append("                  (Select Banco.Nombre || ' nº ' || Cuenta.Iban ");
			consulta.append("                 From Cen_Cuentasbancarias Cuenta, Cen_Bancos Banco ");
			consulta.append("                Where Cuenta.Cbo_Codigo = Banco.Codigo ");
			consulta.append("                  And Cuenta.Idinstitucion = fac.idinstitucion  "); 
			consulta.append("	                  And Cuenta.Idpersona = nvl(fac.idPersonaDeudor,fac.idpersona)   "); 
			consulta.append("                  And Cuenta.Idcuenta = nvl(fac.idcuentadeudor,fac.Idcuenta))  ");
			consulta.append("            when (facHist.Idtipoaccion =7) then     ");
			consulta.append("                 (Select Banco.Nombre || ' nº ' || Cuenta.Iban ");
			consulta.append("               From Cen_Cuentasbancarias Cuenta, Cen_Bancos Banco, Fac_Renegociacion Renegocia2 ");
			consulta.append("                Where Cuenta.Cbo_Codigo = Banco.Codigo ");
			consulta.append("                  And facHist.Idinstitucion = Renegocia2.Idinstitucion ");
			consulta.append("                  And facHist.Idfactura = Renegocia2.Idfactura ");
			consulta.append("                  And facHist.Idrenegociacion = Renegocia2.Idrenegociacion ");
			consulta.append("                  And Cuenta.Idcuenta = Renegocia2.Idcuenta ");
			consulta.append("                  And Cuenta.Idinstitucion =Renegocia2.idinstitucion ");
			consulta.append("                  And Cuenta.Idpersona = Renegocia2.idpersona) ");
			consulta.append("       end As Nombrebanco ");
		}
			
		consulta.append("  from fac_historicofactura facHist, fac_tiposaccionfactura facTipoAcc, fac_factura fac, fac_Estadofactura facEstado ");
		consulta.append(" where facTipoAcc.Idtipoaccion = facHist.Idtipoaccion ");
		consulta.append("   and facEstado.Idestado = facHist.Estado ");
		consulta.append("   and fac.idfactura = facHist.Idfactura ");
		consulta.append("   and fac.idinstitucion = facHist.Idinstitucion ");
		consulta.append("   and fac.idfactura IN (");
		consulta.append(listaFacturasComision);
		consulta.append("       ) ");
		consulta.append("   and fac.idinstitucion = ");
		consulta.append(idInstitucion); 
		
		if (esProcesoMasivo) {
			consulta.append(" and facHist.idTipoAccion not in (8,9)");
			consulta.append(" ORDER BY TO_NUMBER(fac.idfactura), facHist.Idhistorico ");
			consulta.append(") ");
		} else{
			consulta.append(" ORDER BY TO_NUMBER(fac.idfactura), facHist.Idhistorico ");
		}

		return consulta.toString();
	}
	
	/**
	 * Obtiene las lineas de los pagos de una factura
	 * @param idInstitucion
	 * @param idFactura
	 * @param idPersona
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector getPagos (Integer idInstitucion, String idFactura, Long idPersona)  throws ClsExceptions,SIGAException {
		try {
			// JPT: Obtiene la sentencia sql que obtiene todas las lineas de los pagos de una factura
			String consulta = getQueryPagos(idInstitucion, idFactura, idPersona, false);
			
			RowsContainer rc = new RowsContainer(); 
			if (rc.query(consulta)) {
				Vector resultados = new Vector (); 
				
				Double pendiente=new Double(0);
				Double importePago=new Double(0);
				Double auxPendiente=new Double(0);
				String tabla="";
				
				for (int i = 0; i < rc.size(); i++)	{
					Hashtable aux = (Hashtable)((Row) rc.get(i)).getRow();
					
					tabla=UtilidadesHash.getString(aux,"TABLA").trim();
					
					if(tabla.startsWith(UtilidadesString.getMensajeIdioma(this.usrbean, "facturacion.pagosFactura.accion.emisionFactura")) ||  
					   tabla.startsWith((UtilidadesString.getMensajeIdioma(this.usrbean, "facturacion.pagosFactura.accion.confirmacionFactura")))){
						
						pendiente =  UtilidadesHash.getDouble(aux,"IMPORTE");
						
						if (pendiente.doubleValue() < 0.0) {
							pendiente = new Double(0.0);
						}
						aux.remove("IMPORTE");
						aux.put("IMPORTE",new Double(0.00));
						aux.put("IMPORTEPENDIENTE", pendiente);
	
					} else {
						if (tabla.startsWith(UtilidadesString.getMensajeIdioma(this.usrbean, "facturacion.pagosFactura.accion.devolucion")) ||  
						    tabla.startsWith((UtilidadesString.getMensajeIdioma(this.usrbean, "facturacion.pagosFactura.accion.renegociacion")))) {
							
							pendiente =  UtilidadesHash.getDouble(aux,"IMPORTE");
						
							if (pendiente.doubleValue() < 0.0) {
								pendiente = new Double(0.0);
							}
							aux.remove("IMPORTE");
							aux.put("IMPORTE",new Double(0.00));
							aux.put("IMPORTEPENDIENTE", pendiente);
							
						} else {
							
							importePago=UtilidadesHash.getDouble(aux,"IMPORTE");//formateo
							
							pendiente = new Double (pendiente.doubleValue() - importePago.doubleValue());	
							if (pendiente.doubleValue() < 0.0) {
								pendiente = new Double(0.0);
							}
							auxPendiente = new Double (UtilidadesNumero.redondea(pendiente.doubleValue(),2));
							aux.put("IMPORTEPENDIENTE", auxPendiente);
							aux.remove("IMPORTE");
							aux.put("IMPORTE",importePago);
						}
					}

					//Si es una compensación se calcula el estado de la misma forma que para los abonos
					if(tabla.startsWith(UtilidadesString.getMensajeIdioma(this.usrbean, "facturacion.pagosFactura.accion.compensacion"))){
						
						FacFacturaBean facBean = new FacFacturaBean();
						
						facBean.setImpTotalPorPagar(auxPendiente);
						
						if(aux.get("IDABONO_IDCUENTA")!=null)
							facBean.setIdCuenta(UtilidadesHash.getInteger(aux,"IDABONO_IDCUENTA"));		
						
						facBean.setIdInstitucion(idInstitucion);
						facBean.setIdFactura(idFactura);
						
						FacFacturaAdm facFactAdm= new FacFacturaAdm(this.usrbean);
						String estado=facFactAdm.consultarActNuevoEstadoFactura(facBean,Integer.parseInt(this.usrbean.getUserName()),false);						
						aux.remove("ESTADO");
						aux.put("ESTADO", UtilidadesString.getMensajeIdioma(this.usrbean, estado));
						
						tabla=UtilidadesHash.getString(aux,"TABLA").trim()+" ("+aux.get("NUMEROABONO")+")";
						aux.remove("TABLA");
						aux.put("TABLA",tabla);
						
					}
					
					resultados.add(aux);
					
					
				}
				return resultados;
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
	   				throw new ClsExceptions(e, "Error al obtener los datos de los pagos.");
	   			}
	   		}	
	    }
		return new Vector ();
	}
	
	/**
	 * Obtiene la ultima fecha de pago de una lista de facturas
	 * @param idInstitucion
	 * @param listaIdsFacturas
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public String getUltimaFechaPagosFacturas (Integer idInstitucion, String listaIdsFacturas) throws ClsExceptions, SIGAException {
		String resultado = null;
		
		try {
			// JPT: Obtiene la sentencia sql que obtiene todas las lineas de los pagos de una factura
			String consulta = getQueryPagos(idInstitucion, listaIdsFacturas, null, true);
			
			RowsContainer rc = new RowsContainer(); 
			if (rc.query(consulta)) {
				if (rc.size()>0) {
					Row fila = (Row) rc.get(0);
					resultado = fila.getString("ULTIMAFECHA");
				}
			}
			
		} catch (Exception e) {
	    	throw new ClsExceptions(e, "Error al obtener la última fecha de pago de las facturas");	    
	    }
		
		return resultado;
	}
		
	public Hashtable getTotalesPagos (Integer idInstitucion, String idFactura)  throws ClsExceptions,SIGAException {
		try {
		    String consulta = "SELECT " + FacFacturaBean.C_IMPTOTAL + " AS TOTAL_FACTURA, " +
					    		FacFacturaBean.C_IMPTOTALPAGADOPORBANCO + " AS TOTAL_PAGADOPORBANCO, " +
					    		FacFacturaBean.C_IMPTOTALCOMPENSADO + " AS TOTAL_COMPENSADO, " +
					    		FacFacturaBean.C_IMPTOTALPAGADOSOLOCAJA + " AS TOTALPAGADOSOLOCAJA, " +
					    		FacFacturaBean.C_IMPTOTALPAGADOPORCAJA + " AS TOTALPAGADOPORCAJA, " +
					    		FacFacturaBean.C_IMPTOTALPAGADOSOLOTARJETA + " AS TOTALPAGADOPORTARJETA, " +
					    		FacFacturaBean.C_IMPTOTALANTICIPADO + " AS TOTAL_ANTICIPADO, " +
					    		FacFacturaBean.C_IDFORMAPAGO + " AS FORMAPAGOFACTURA " +
					    	" FROM " + FacFacturaBean.T_NOMBRETABLA +
					    	" WHERE " + FacFacturaBean.C_IDINSTITUCION + " = " + idInstitucion +
					    		" AND " + FacFacturaBean.C_IDFACTURA + " =  " + idFactura;
	
			RowsContainer rc = new RowsContainer(); 
			if (rc.query(consulta) && rc.size() == 1) {
				Hashtable aux = (Hashtable)((Row) rc.get(0)).getRow();
				return aux;
			}
			
		} catch (Exception e) {
	   		if (e instanceof SIGAException){
	   			throw (SIGAException)e;
	   			
	   		} else if (e instanceof ClsExceptions){
   				throw (ClsExceptions)e;
   				
   			} else {
   				throw new ClsExceptions(e, "Error al obtener los datos de las facturas.");
   			}	
	    }
		
		return null;
	}
	
	/** Funcion ejecutaSelect(String select)
	 *	@param select sentencia "select" sql valida, sin terminar en ";"
	 *  @return Vector todos los registros que se seleccionen 
	 *  en BBDD debido a la ejecucion de la sentencia select
	 *
	 */
	public Vector ejecutaSelect(String select) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.query(select)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}

	public Vector ejecutaSelectBind(String select, Hashtable codigos) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.queryBind(select,codigos)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}

}
