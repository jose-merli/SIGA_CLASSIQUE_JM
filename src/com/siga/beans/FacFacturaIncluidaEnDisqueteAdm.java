/*
 * VERSIONES:
 * 
 * carlos.vidal - 09-03-2005 - Creacion
 *	
 */
package com.siga.beans;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
//import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
//import com.atos.utils.ReadProperties;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
//import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.PaginadorCaseSensitive;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.SIGAException;
import com.siga.beans.FacFacturaAdm;

/**
*
* Clase que gestiona la tabla CenHistorico de la BBDD
* 
*/
public class FacFacturaIncluidaEnDisqueteAdm extends MasterBeanAdministrador {

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public FacFacturaIncluidaEnDisqueteAdm(UsrBean usu) {
		super(FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA, usu);
	}

	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION,              
				FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS,           
				FacFacturaIncluidaEnDisqueteBean.C_IDFACTURAINCLUIDAENDISQUETE,
				FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA,                  
				FacFacturaIncluidaEnDisqueteBean.C_DEVUELTA,                   
				FacFacturaIncluidaEnDisqueteBean.C_CONTABILIZADA,              
				FacFacturaIncluidaEnDisqueteBean.C_FECHAMODIFICACION,          
				FacFacturaIncluidaEnDisqueteBean.C_USUMODIFICACION,            
				FacFacturaIncluidaEnDisqueteBean.C_IDRECIBO,                   
				FacFacturaIncluidaEnDisqueteBean.C_IDRENEGOCIACION,            
				FacFacturaIncluidaEnDisqueteBean.C_FECHADEVOLUCION,            
				FacFacturaIncluidaEnDisqueteBean.C_IDPERSONA,                  
				FacFacturaIncluidaEnDisqueteBean.C_IDCUENTA,                   
				FacFacturaIncluidaEnDisqueteBean.C_IMPORTE};                   

		return campos;
	}
	
	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION,
							FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS,
							FacFacturaIncluidaEnDisqueteBean.C_IDFACTURAINCLUIDAENDISQUETE};
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

		FacFacturaIncluidaEnDisqueteBean bean = null;
		
		try {
			bean = new FacFacturaIncluidaEnDisqueteBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,				FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION));
			bean.setIdDisqueteCargos(UtilidadesHash.getInteger(hash,           	FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS));
			bean.setIdFacturaIncluidaEnDisquete(UtilidadesHash.getInteger(hash,	FacFacturaIncluidaEnDisqueteBean.C_IDFACTURAINCLUIDAENDISQUETE));
			bean.setIdFactura(UtilidadesHash.getString(hash,                  	FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA));
			bean.setDevuelta(UtilidadesHash.getString(hash,                   	FacFacturaIncluidaEnDisqueteBean.C_DEVUELTA));
			bean.setContabilizada(UtilidadesHash.getString(hash,              	FacFacturaIncluidaEnDisqueteBean.C_CONTABILIZADA));
			bean.setIdRecibo(UtilidadesHash.getString(hash,                   	FacFacturaIncluidaEnDisqueteBean.C_IDRECIBO));       
			bean.setIdRenegociacion(UtilidadesHash.getInteger(hash,           	FacFacturaIncluidaEnDisqueteBean.C_IDRENEGOCIACION));
			bean.setFechaDevolucion(UtilidadesHash.getString(hash,            	FacFacturaIncluidaEnDisqueteBean.C_FECHADEVOLUCION));
			bean.setIdPersona(UtilidadesHash.getLong(hash,                  	FacFacturaIncluidaEnDisqueteBean.C_IDPERSONA));      
			bean.setIdCuenta(UtilidadesHash.getInteger(hash,                   	FacFacturaIncluidaEnDisqueteBean.C_IDCUENTA));       
			bean.setImporte(UtilidadesHash.getDouble(hash,                    	FacFacturaIncluidaEnDisqueteBean.C_IMPORTE));        
			bean.setFechaMod(UtilidadesHash.getString(hash,						FacFacturaIncluidaEnDisqueteBean.C_FECHAMODIFICACION));  
			bean.setUsuMod(UtilidadesHash.getInteger(hash,						FacFacturaIncluidaEnDisqueteBean.C_USUMODIFICACION));		}
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
			FacFacturaIncluidaEnDisqueteBean b = (FacFacturaIncluidaEnDisqueteBean) bean;
			UtilidadesHash.set(htData,FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION,              b.getIdInstitucion());              
			UtilidadesHash.set(htData,FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS,           b.getIdDisqueteCargos());           
			UtilidadesHash.set(htData,FacFacturaIncluidaEnDisqueteBean.C_IDFACTURAINCLUIDAENDISQUETE,b.getIdFacturaIncluidaEnDisquete());
			UtilidadesHash.set(htData,FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA,                  b.getIdFactura());                 
			UtilidadesHash.set(htData,FacFacturaIncluidaEnDisqueteBean.C_DEVUELTA,                   b.getDevuelta());                  
			UtilidadesHash.set(htData,FacFacturaIncluidaEnDisqueteBean.C_CONTABILIZADA,              b.getContabilizada());             
			UtilidadesHash.set(htData,FacFacturaIncluidaEnDisqueteBean.C_FECHAMODIFICACION,          b.getIdRecibo());                  
			UtilidadesHash.set(htData,FacFacturaIncluidaEnDisqueteBean.C_USUMODIFICACION,            b.getIdRenegociacion());           
			UtilidadesHash.set(htData,FacFacturaIncluidaEnDisqueteBean.C_IDRECIBO,                   b.getFechaDevolucion());           
			UtilidadesHash.set(htData,FacFacturaIncluidaEnDisqueteBean.C_IDRENEGOCIACION,            b.getIdPersona());                 
			UtilidadesHash.set(htData,FacFacturaIncluidaEnDisqueteBean.C_FECHADEVOLUCION,            b.getIdCuenta());                  
			UtilidadesHash.set(htData,FacFacturaIncluidaEnDisqueteBean.C_IDPERSONA,                  b.getImporte());                   
			UtilidadesHash.set(htData,FacFacturaIncluidaEnDisqueteBean.C_IDCUENTA,                   b.getFechaMod());                  
      UtilidadesHash.set(htData,FacFacturaIncluidaEnDisqueteBean.C_IMPORTE,                    		 b.getUsuMod());                    
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	/**
	 * Funcion que actualiza el idRegnegociacion de todos los registros de la tabla 
	 * que correspondan a la factura, institucion y el idRenegociacion sea null 
	 * @param idInstitucion institucion a mofidicar
	 * @param idFactura factura a modificar
	 * @param idRenegociacion nuevo ID
	 * @return true si ok, false en caso contrario
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public boolean updateRenegociacion (Integer idInstitucion, String idFactura, Integer idRenegociacion) throws ClsExceptions, SIGAException {

		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			FacFacturaIncluidaEnDisqueteAdm adm = new FacFacturaIncluidaEnDisqueteAdm(this.usrbean);
			rc = new RowsContainer(); 
			String sql = " SELECT " + 
			 " " + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + 
			 ", " + FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS + 
			 ", " + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURAINCLUIDAENDISQUETE + 
			 " FROM " + this.nombreTabla + 
			 " WHERE " + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = " + idInstitucion +
			 " AND " + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA + " = '" + idFactura + "'" +
			 " AND " + FacFacturaIncluidaEnDisqueteBean.C_IDRENEGOCIACION + " IS NULL ";

			if (rc.findForUpdate(sql)) {

				// TENEMOS LOS UPDATES.
				if (rc!=null) {
		       		for (int i = 0; i < rc.size(); i++)	{
						Row filaClientes = (Row) rc.get(i);
						Hashtable fila = filaClientes.getRow();
						/*
						FacFacturaIncluidaEnDisqueteBean bean = new FacFacturaIncluidaEnDisqueteBean();
						bean.setIdInstitucion(new Integer((String)fila.get(FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION)));
						bean.setIdDisqueteCargos((Integer)fila.get(FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS));
						bean.setIdFacturaIncluidaEnDisquete(new Integer((String)fila.get(FacFacturaIncluidaEnDisqueteBean.C_IDFACTURAINCLUIDAENDISQUETE)));
						bean.setIdRenegociacion(idRenegociacion);
						*/
						
						fila.put(FacFacturaIncluidaEnDisqueteBean.C_IDRENEGOCIACION,idRenegociacion);
						
						String claves[] = {FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION,FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS,FacFacturaIncluidaEnDisqueteBean.C_IDFACTURAINCLUIDAENDISQUETE};
						String campos[] = {FacFacturaIncluidaEnDisqueteBean.C_IDRENEGOCIACION};
						
						if (!adm.updateDirect(fila,claves,campos)) {
							this.setError(adm.getError());
							return false;
						}
		       		}
				}
				return true;
				
			} else  {
				return false;
			}

/*
			String sql = " UPDATE " + this.nombreTabla + 
						 " SET " + FacFacturaIncluidaEnDisqueteBean.C_IDRENEGOCIACION + " = " + idRenegociacion +
						 " WHERE " + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = " + idInstitucion +
						 " AND " + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA + " = '" + idFactura + "'" +
						 " AND " + FacFacturaIncluidaEnDisqueteBean.C_IDRENEGOCIACION + " IS NULL ";
			
			if (rc.findForUpdate(sql)) {
				return true;
			}
			else {
				return false;
			}
*/
			
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'updateRenegociacion' en B.D."); 
		}
	}

	/**
	 * Realiza la busqueda de recibos para devoluciones manuales mediante criterios de busqueda.
	 * @param idInstitucion
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param numRecibo
	 * @param titular
	 * @param numRemesa
	 * @return Vector de resultados
	 * @throws ClsExceptions
	 */
	public PaginadorCaseSensitive getRecibosParaDevolucion(String idInstitucion,String fechaDesde,String fechaHasta,String numRecibo,String titular, String numRemesa, String numFactura, String destinatario) throws ClsExceptions {
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
			String sql = " select " + 
				" " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS + ", " +
				" " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURAINCLUIDAENDISQUETE + ", " +
				" " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA + ", " +
				" " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDRECIBO + ", " +
				" " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDPERSONA + ", " +
				" " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IMPORTE + ", " +
				" " + FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_FECHACREACION + ", " +
				" " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE + ", " +
				" " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 + ", " +
				" " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 + ", " +
				" " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_NUMEROFACTURA + " " +
				" from " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + ", " + CenPersonaBean.T_NOMBRETABLA + ", " + FacFacturaBean.T_NOMBRETABLA + " " + ", " + FacDisqueteCargosBean.T_NOMBRETABLA + " " +
				" where " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDPERSONA + " = " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + " " +
				" and  " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + " " +
				" and  " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " " +
				" and  " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS + " = " + FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDDISQUETECARGOS + " " +
				" and  " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = " + FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDINSTITUCION + " " +
				" and " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = " + idInstitucion +
				" and " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_DEVUELTA + " = 'N'"+
			
			" and " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_ESTADO + " = "+FacEstadoFacturaBean.ID_ESTADO_PAGADA;

			
			
				if ((fechaDesde != null && !fechaDesde.trim().equals("")) || (fechaHasta != null && !fechaHasta.trim().equals(""))) {
					if (!fechaDesde.equals(""))
						fechaDesde = GstDate.getApplicationFormatDate("", fechaDesde); 
					if (!fechaHasta.equals(""))
						fechaHasta = GstDate.getApplicationFormatDate("", fechaHasta);
					sql+= " AND " + GstDate.dateBetweenDesdeAndHasta(FacDisqueteCargosBean.T_NOMBRETABLA+"."+FacDisqueteCargosBean.C_FECHACREACION, fechaDesde, fechaHasta);
				}
				
			if (!numRecibo.equals("")) {
				
			
				sql += " AND ("+ComodinBusquedas.prepararSentenciaCompleta(numRecibo.trim(),FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDRECIBO )+") ";
			}
			
			if (!numRemesa.equals("")) {
				
			
				sql += " AND ("+ComodinBusquedas.prepararSentenciaCompleta(numRemesa.trim(),FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS)+") ";
			}
			
			if (!numFactura.equals("")) {				
			
				sql += " AND ("+ComodinBusquedas.prepararSentenciaCompleta(numFactura.trim(),FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_NUMEROFACTURA)+") ";
			}
			
			if (!titular.equals("")) {
				sql += " AND " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDPERSONA + " = " + titular + " ";
				
			}
			
			if (!destinatario.equals("")) {
				sql += " AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONADEUDOR + " = " + destinatario + " ";
				
			}
			
			sql += " ORDER BY " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDRECIBO + " ";
			
				
				
           /* rc = this.find(sql);
 			if (rc!=null) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}*/
			PaginadorCaseSensitive paginador = new PaginadorCaseSensitive(sql);				
				int totalRegistros = paginador.getNumeroTotalRegistros();
				
				if (totalRegistros==0){					
					paginador =null;
				}
	 
		       
				
				return paginador;

		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error en getRecibosParaDevolucion");
		}
	//	return v;
	
	}
	
	
	
	/**
	 * Genera el fichero de devoluciones manuales segun recibos parados por parametro y datos de cabecera
	 * @param banco
	 * @param fechaDevolucion
	 * @param aplicaComisiones
	 * @param recibos String que contiene todos los registros seleccionados separados por ";" y los datos de cada registro separados por "%%" tal que "motivo%%idfactura%%idrecibo"
	 * @return
	 * @throws ClsExceptions 
	 */
	public File crearFicheroDevoluciones(String banco, String fechaDevolucion, String aplicaComisiones, String recibos, String idInstitucion, String identificador, String nombreFichero) throws ClsExceptions, SIGAException {
		Vector v = new Vector();
		RowsContainer rc = null;
		File salida = null;
		String numeroFactura="";
		BufferedWriter bw = null;
		try{

			//obtener los datos de idrecibo y motivos
			ArrayList datos = new ArrayList();
		    StringTokenizer st = null;
		    int contadorReg=1;
		    String tok=recibos;
		    try {
		    	st = new StringTokenizer(tok, ";");
			    contadorReg=st.countTokens();
		    } catch (java.util.NoSuchElementException nee) {
		    	// solamente existe un token
		    }

		    while (st.hasMoreElements())
		    {
		    	ArrayList aux = new ArrayList();
		        StringTokenizer st2 = new StringTokenizer(st.nextToken(), "%%");
		        // token: motivo, idfactura, idrecibo, importe
		        aux.add(st2.nextToken());
		        aux.add(st2.nextToken());
		        aux.add(st2.nextToken());
		        aux.add(st2.nextToken());

		        datos.add(aux);
		    }
		    

		    salida = new File(nombreFichero);
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(salida),ClsConstants.IMPRESO190_ENCODING));

			String relleno= ".";
		    // 1. CABECERAS
			String linea = "";
			linea += "51"; // cod reg
			linea += "90"; // cod dato
			linea += UtilidadesString.relleno(relleno,12); // nada
			linea += UtilidadesString.formateaFicheros(UtilidadesString.formatoFecha(fechaDevolucion,"dd/MM/yyyy","ddMMyy"),6,false); // fecha (6)
			linea += UtilidadesString.relleno(relleno,66); // nada
			linea += UtilidadesString.formateaFicheros(banco,4,true); // entidad
			linea += UtilidadesString.relleno(relleno,70); // nada
			// escribo
			// RGG cambio para formato DOS
			linea+="\r\n";
			bw.write(linea);
			//bw.newLine();
			


			boolean primeraVez = true;
			
		    // 2. LINEAS
		    for (int i=0;i<datos.size();i++) {
		    	ArrayList aux2 = (ArrayList)datos.get(i);
		    	String motivo=(String)aux2.get(0);
		    	String idFactura=(String)aux2.get(1);
		    	String idRecibo=(String)aux2.get(2);
		    	String importe=(String)aux2.get(3);
		    	String idRenegociacion="";
		    	
		    	idRecibo=idRecibo+"00";
		    	if (idRecibo!=null && !idRecibo.equals("")) {
		    	    idRenegociacion=idRecibo.substring(idRecibo.length()-2);
		    	}
		    	
		    	FacFacturaAdm facturaAdm =new FacFacturaAdm(this.usrbean);
		    	Vector factura=facturaAdm.getFactura(idInstitucion,idFactura);
		    	String idFacturaAux="";
		    	if (!factura.isEmpty()){
		    		Hashtable factHash = ((Row)factura.firstElement()).getRow();
		    	    numeroFactura=(String)factHash.get(FacFacturaBean.C_NUMEROFACTURA);
		    	    idFacturaAux=(String)factHash.get(FacFacturaBean.C_IDFACTURA);
		    	}

		    	FacBancoInstitucionAdm bancoAdm =new FacBancoInstitucionAdm(this.usrbean);
		    	Vector vbancos_codigo=bancoAdm.getBancosCodigoDesdeFichero(idInstitucion,idFactura, idRenegociacion);
		    	String numerocuenta = "";
		    	if (!vbancos_codigo.isEmpty()){
		    		Hashtable factBancos = ((Row)vbancos_codigo.firstElement()).getRow();
		    	    numerocuenta=(String)factBancos.get("NUMEROCUENTA");
		    	}else{// caso extraño en el que no tengamos el codigo de banco.
		    		throw new SIGAException("messages.devolucionesManuales.error");
		    	}

				if (!primeraVez) {
				    // LINEA TE TOTAL ORDENANTE
				    linea = "";
					linea += "58"; // cod reg
					linea += "90"; // cod dato
					linea += UtilidadesString.relleno(relleno,158); // nada
					// escribo
					// RGG cambio para formato DOS
					linea+="\r\n";
					bw.write(linea);
					//bw.newLine();
				}
				
				
		    	// LINEA DE BANCO ORDENANTE
				linea = "";
				linea += "53"; // cod reg
				linea += "90"; // cod dato
				linea += UtilidadesString.relleno(relleno,64); // nada
				linea += numerocuenta.substring(0,20); // cod dato
				linea += UtilidadesString.relleno(relleno,74); // nada
				// escribo
				// RGG cambio para formato DOS
				linea+="\r\n";
				bw.write(linea);
				//bw.newLine();
		    	
				primeraVez=false;
				
		    	// formateo el importe
		    	importe = importe.replaceAll(",",".");
		    	Double dimporte = new Double(importe);
				Vector valor = UtilidadesString.desdoblarDouble(dimporte); 
				String importeFormat="";
				importeFormat += UtilidadesString.formatea(valor.get(1),8,true); // entera
				importeFormat += UtilidadesString.formatea(valor.get(2),2,true); // decimal
				
				// Obtengo el concepto para la referecia a la factura
				// n.doc.:+idfactura+concepto
				String concepto = "";
				Object[] param_in = new Object[]{idInstitucion,idFactura};
				String resultadoPl[] = new String[4];
				//Ejecucion del PL
				resultadoPl = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_CARGOS.PROC_CALC_SUFIJO(?,?,?,?,?,?)}", 4, param_in);
				//Resultado del PL
				if (resultadoPl!=null && resultadoPl[2].equals("0")) {
					concepto=resultadoPl[1];
					if (concepto==null || concepto.equalsIgnoreCase("null")) {
						concepto="";
					}
				} else {
					throw new ClsExceptions("Error al ejecutar PKG_SIGA_CARGOS.PROC_CALC_SUFIJO: ");
				}

				//String ref = "n.doc.:"+idFactura+concepto;
				String ref = numeroFactura+"-"+concepto;//Se modifica el idFactura por el numero de factura
				
				linea = "";
				linea += "56"; // cod reg
				linea += "90"; // cod dato
				linea += UtilidadesString.relleno(relleno,12); // nada
				//linea += UtilidadesString.formateaFicheros(idRecibo,12,false); // referencia
				//linea += UtilidadesString.relleno(relleno,60); // nada
				linea += UtilidadesString.relleno(relleno,72); // nada
				linea += UtilidadesString.formateaFicheros(importeFormat,10,true); // importe
				linea += UtilidadesString.relleno(relleno,6); 
				linea += UtilidadesString.formateaFicheros(idRecibo,10,false); // referencia
				//linea += UtilidadesString.relleno(relleno,16); // nada
				linea += UtilidadesString.formateaFicheros(ref,40,false); // numero factura / CONCEPTO
				linea += UtilidadesString.formateaFicheros(motivo,1,true); // motivo devolucion
				linea += UtilidadesString.relleno(relleno,7); // nada
				// escribo
				// RGG cambio para formato DOS
				linea+="\r\n";
				bw.write(linea);
				//bw.newLine();
		    
		    
		    }

		    // 3. TOTALES
			linea = "";
			linea += "58"; // cod reg
			linea += "90"; // cod dato
			linea += UtilidadesString.relleno(relleno,158); // nada
			// escribo
			// RGG cambio para formato DOS
			linea+="\r\n";
			bw.write(linea);
			//bw.newLine();

			linea = "";
			linea += "59"; // cod reg
			linea += "90"; // cod dato
			linea += UtilidadesString.relleno(relleno,158); // nada
			// escribo
			// RGG cambio para formato DOS
			//ACG Se suprime en el ultimo registro el salto de carro y final de linea
		//	linea+="\r\n"; 
			bw.write(linea);
			//bw.newLine();
			
			// cierro el fichero
			bw.close();

			
		}catch(SIGAException e) {
			throw e;
			
		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error en crearFicheroDevoluciones");
		}
		finally {
			if (bw != null){
				try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return salida;
	
	}
	
}
