package com.siga.productos.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenClienteBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.PysProductosInstitucionAdm;
import com.siga.beans.PysProductosInstitucionBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.productos.form.CargaProductosForm;

public class CargaProductosAction extends MasterAction {
	
	
	//private String DELIMITADOR = "|";
	/**
	 * Redefinicion de la funcion executeInternal para controlar las nuevas acciones confirmar y denegar
	 */
	protected ActionForward executeInternal(ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try {		
			String mapDestino = "exception";
			MasterForm miForm = null;
		    
			do {
				miForm = (MasterForm) formulario;
				if (miForm == null) {
					break;
				}
			
				String accion = miForm.getModo();
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					mapDestino = abrir(mapping, miForm, request, response);
				} else 
				// guardar
				if (accion.equalsIgnoreCase("guardarFich")){
					mapDestino = guardarFichero(mapping, miForm, request, response);
					break;

				} else
					return super.executeInternal(mapping, formulario, request, response);
				
			} while (false);

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) { 				
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			return mapping.findForward(mapDestino);
		}
		catch (SIGAException es) {
			throw es;
		} 
		catch (Exception e) {
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.productos"}); 
		}
	}
	
	
	
	/** 
	 *  Funcion que atiende la accion abrir.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="abrir";
		
		try{
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String idInstitucion=user.getLocation();
		
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,null); 
		} 
		
		return result;
	}	
	
	/** 
	 *  Funcion que atiende la accion guardar.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String guardarFichero(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		UserTransaction tx = null;
		Hashtable htProducto = new Hashtable();
		Hashtable htProducto2 = new Hashtable();
		boolean procesoOk = true;
		String exito = "";
		
		try{
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String idInstitucion=user.getLocation();
			CargaProductosForm form = (CargaProductosForm) formulario;
			FormFile file = form.getFichero();
		    String nombre ="";
		    
		    ResourceBundle rp=ResourceBundle.getBundle("SIGA");
		    String pathTemporal = rp.getString("facturacion.directorioFisicoTemporalFacturasJava") + File.separator + "tmp" + File.separator + idInstitucion;
		    
			if(file==null || file.getFileSize()<1){
		    	throw new SIGAException("messages.general.error.ficheroNoExiste");
		    }
		    else{
		    	
		    	InputStream stream =null;
		    	nombre = file.getFileName();
		    	
		    	OutputStream bos = null;
		    	try {			
		    		//retrieve the file data
		    		stream = file.getInputStream();
		    		//write the file to the file specified
		    		File camino = new File (pathTemporal);
		    		camino.mkdirs();
		    		bos = new FileOutputStream(pathTemporal + File.separator+nombre );
		    		int bytesRead = 0;
		    		byte[] buffer = new byte[8192];
		    		while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
		    			bos.write(buffer, 0, bytesRead);
		    		}			    
		    		
		    	} 
		    	catch (FileNotFoundException fnfe) {
		    		throw new SIGAException("message.err.error.subiendoarchivo.datosgenerales",fnfe);
		    	}
		    	catch (IOException ioe) {
		    		throw new SIGAException("message.err.error.subiendoarchivo.datosgenerales",ioe);
		    	}
		    	finally	{
		    		// close the stream
		    		stream.close();
		    		bos.close();
		    	}
		    }
			
			tx = user.getTransactionPesada();
			tx.begin();
			
			File temporal = new File(pathTemporal + File.separator+nombre); 
			BufferedReader reader = new BufferedReader(new FileReader(temporal));
			String linea = reader.readLine();
			while (linea != null && procesoOk)
			{
				// tratamiento de cada línea
				String delimitador="";
				
				try {
					GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request));
					delimitador = paramAdm.getValor(user.getLocation(),"PYS","SEPARADOR_FICHEROCOMPRAS","");
		        } catch (Exception e) {
		        	//
		        }
				String datos[] =  UtilidadesString.splitNormal(linea,delimitador);
				String colegiado = "";
				String dni = "";
				String name = "";
				String unidades = "";
				String descripcion = "";
				String idTipoProducto = "";
				String idProducto = "";
				String idProductoInstitucion = "";
				try {
					colegiado = datos[0];
					dni = datos[1];
					name = datos[2];
					unidades = datos[3];
					descripcion = datos[4];
					idTipoProducto = datos[5];
					idProducto = datos[6];
					idProductoInstitucion = datos[7];	
				} catch (Exception e) {
					procesoOk = false;
					ClsLogging.writeFileLog(".-.-.-.-.-.-.-.-.-.", 10);
					ClsLogging.writeFileLog("CARGA COMPRAS "+idInstitucion+": Línea incompleta: "+linea, 10);
				}
				
				PysProductosInstitucionBean producto = null;
				PysProductosInstitucionAdm productoAdm = new PysProductosInstitucionAdm(this.getUserBean(request));
				
				if (!idTipoProducto.trim().equals("") && !idProducto.trim().equals("") && !idProductoInstitucion.trim().equals("")) {
					//obtener el producto por clave

					try{
						int esNumero = UtilidadesNumero.parseInt(idProducto) + UtilidadesNumero.parseInt(idTipoProducto) + UtilidadesNumero.parseInt(idProductoInstitucion);
						// Sumo los identificadores para forzar un error si no son enteros
						htProducto.put(PysProductosInstitucionBean.C_IDINSTITUCION, user.getLocation());
						htProducto.put(PysProductosInstitucionBean.C_IDTIPOPRODUCTO, idTipoProducto);
						htProducto.put(PysProductosInstitucionBean.C_IDPRODUCTO, idProducto);
						htProducto.put(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION, idProductoInstitucion);
						Vector vProductos = productoAdm.selectByPK(htProducto);
						if (vProductos!=null && vProductos.size()>0) {
							producto = (PysProductosInstitucionBean) vProductos.get(0);
						}
					}catch (Exception e){
						procesoOk = false;
						ClsLogging.writeFileLog(".-.-.-.-.-.-.-.-.-.", 10);
						ClsLogging.writeFileLog("CARGA COMPRAS "+idInstitucion+": Línea con identificadores no válidos: "+linea, 10);
						
					}
				}
			
				//si no lo encontramos
				if (producto==null && !descripcion.trim().equals("")) {
					// obtener el producto por nombre

					htProducto2.put(PysProductosInstitucionBean.C_IDINSTITUCION, user.getLocation());
					htProducto2.put(PysProductosInstitucionBean.C_DESCRIPCION, descripcion);
					Vector vProductos2 = productoAdm.selectNLS(htProducto2);
					if (vProductos2!=null && vProductos2.size()>0) {
						producto = (PysProductosInstitucionBean) vProductos2.get(0);
					}
				}

				CenClienteBean cliente = null;
				CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserBean(request));
				if (producto==null) { 
					ClsLogging.writeFileLog(".-.-.-.-.-.-.-.-.-.", 3);
					ClsLogging.writeFileLog("CARGA COMPRAS "+idInstitucion+": Error no encuentra Producto: "+linea, 3);
				} else {
					// tenemos producto
					if (!dni.trim().equals("") && !colegiado.trim().equals("")) {
						// buscamos persona por dni y numero colegiado

						Vector vProductos3 = clienteAdm.buscarPersona(idInstitucion,dni,colegiado);
						if (vProductos3!=null && vProductos3.size()>0) {
							cliente = (CenClienteBean) vProductos3.get(0);
						}						
					
					} else {
						if (!name.trim().equals("")) {
							// buscamos persona por nombre de persona (apellido1 apellido2, nombre)
							
							Vector vProductos4 = clienteAdm.buscarPersonaNombre(name);
							if (vProductos4!=null && vProductos4.size()>0) {
								cliente = (CenClienteBean)vProductos4.get(0);
							}							
						}
					}
				}
				if (producto!=null) {
					if (cliente==null) {
							ClsLogging.writeFileLog(".-.-.-.-.-.-.-.-.-.", 3);
							ClsLogging.writeFileLog("CARGA COMPRAS "+idInstitucion+": Error no encuentra Cliente: "+linea, 3);
					} else {
						// PROCESO DE COMPRA
						
						PysProductosInstitucionAdm prodinstadm = new PysProductosInstitucionAdm(user); 
						boolean insert = prodinstadm.cargarFicheroCompras( producto, cliente, unidades);
						if(insert==false){
							ClsLogging.writeFileLog(".-.-.-.-.-.-.-.-.-.", 3);
							ClsLogging.writeFileLog("PROCESO DE COMPRA "+idInstitucion+": Error al procesar la compra: "+linea, 3);
						}
						
					}
				}
				
				linea = reader.readLine();
				
			}//while (linea != null) 
			
			tx.commit();
			
			temporal.deleteOnExit();
			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,tx); 
		} 
		
		if(procesoOk == true){
			exito = this.exito("messages.cargaProductos.inserted.success", request);
		}else{
			exito = this.exito("facturacion.nuevoFichero.literal.errorFormato", request);
		}
		return exito;
		
	}
	
	

}
