package com.siga.facturacion.action;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ReadProperties;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenCuentasBancariasBean;
import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenSucursalesAdm;
import com.siga.beans.CenSucursalesBean;
import com.siga.beans.FacAbonoAdm;
import com.siga.beans.FacAbonoBean;
import com.siga.beans.FacAbonoIncluidoEnDisqueteAdm;
import com.siga.beans.FacAbonoIncluidoEnDisqueteBean;
import com.siga.beans.FacBancoInstitucionAdm;
import com.siga.beans.FacBancoInstitucionBean;
import com.siga.beans.FacDisqueteAbonosAdm;
import com.siga.beans.FacDisqueteAbonosBean;
import com.siga.beans.FcsPagosJGBean;
import com.siga.beans.FicheroEmisorAbonoBean;
import com.siga.beans.FicheroReceptorAbonoBean;
import com.siga.facturacion.form.FicheroBancarioAbonosForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;


/**
 * Clase action para Descargar los ficheros bancarios.<br/>
 * Gestiona abrir y descargar Ficheros
 */
public class FicheroBancarioAbonosAction extends MasterAction{
	/** 	
	 *  *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String mapDestino = "exception";
		MasterForm miForm = null;
		
		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
				return mapping.findForward(mapDestino);
			}
			
			String accion = miForm.getModo();
			
			// La primera vez que se carga el formulario 
			// Abrir
			if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
				mapDestino = abrir(mapping, miForm, request, response);	
				
			}else if (accion.equalsIgnoreCase("download")){
				mapDestino = download(mapping, miForm, request, response);
			}else if (accion.equalsIgnoreCase("generarFichero")){
				mapDestino = generarFichero(mapping, miForm, request, response);
			}else if (accion.equalsIgnoreCase("informeRemesa")){
				mapDestino = informeRemesa(miForm, request);
			}				
			else {
				return super.executeInternal(mapping, formulario, request, response);
			}
			
//			Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	
			{ 
				throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			
			return mapping.findForward(mapDestino);
			
		}catch (SIGAException es) { 
			throw es; 
		}catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.facturacion"}); 
		} 
		
		
	}
	
	/** 
	 *  Funcion que atiende la accion abrir. Muestra todas las facturas programadas cuyas Fecha Real de Generación es null 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try{					
			Integer idInstitucion	= this.getIDInstitucion(request);			
			Integer usuario			= this.getUserName(request);		
			boolean abonosSJCS      = false;
			
			FacDisqueteAbonosAdm adm = new FacDisqueteAbonosAdm(this.getUserBean(request));
			
			// Recuperamos la procedencia de la llamada (1-Facturacion SJCS, 0-Facturacion)
			String sjcs = request.getParameter("sjcs");
			if ((sjcs!=null) && (sjcs.equals("1"))){
				abonosSJCS = true;
			}
			
			Vector vDatos = adm.getDatosFichero(idInstitucion, abonosSJCS);
			request.getSession().setAttribute("DATABACKUP", vDatos);	
			
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		} 	
		return "abrir";
	}
	
	/** 
	 *  Funcion que atiende la accion download. Descarga los ficheros
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String download(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		//UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		
		//String keyFichero 		= "facturacion.directorioBancosJava";	
		String directorioFisico = "facturacion.directorioFisicoAbonosBancosJava";
		String directorio 		= "facturacion.directorioAbonosBancosJava";
		String nombreFichero 	= "";
		String pathFichero		= "";
		String idInstitucion	= "";
		
		try{		
			//Integer usuario = this.getUserName(request);
			
			FicheroBancarioAbonosForm form 		= (FicheroBancarioAbonosForm)formulario;
			//FacDisqueteAbonosAdm adm 			= new FacDisqueteAbonosAdm(usuario);
			//FacDisqueteAbonosBean beanDisquete	= new FacDisqueteAbonosBean();
			//FacFacturaAdm admFactura = new FacFacturaAdm(usuario);
			
		    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties p 	= new ReadProperties ("SIGA.properties");
			pathFichero 		= p.returnProperty(directorioFisico) + p.returnProperty(directorio);		
			//String nombreFichero 	= p.returnProperty(keyFichero);						
			
			Vector ocultos 			= new Vector();		
			ocultos 				= (Vector)form.getDatosTablaOcultos(0);			
			nombreFichero 			= (String)ocultos.elementAt(1);	
			idInstitucion			= this.getIDInstitucion(request).toString();			
			
			
			// CAMBIO MAV 24/06/2005 SUSTITUYO
			// pathFichero += File.separator + idInstitucion + File.separator + nombreFichero;
			// POR
			// Generamos el nombre del fichero.
			String barra = "";
			if (pathFichero.indexOf("/") > -1){ 
				barra = "/";
			}
			if (pathFichero.indexOf("\\") > -1){ 
				barra = "\\";
			}
			
			pathFichero += barra + idInstitucion + barra + nombreFichero;
			// FIN CAMBIO
			
		}catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}
		request.setAttribute("nombreFichero", nombreFichero);
		request.setAttribute("rutaFichero", pathFichero);
		
		return "descargaFichero";		
	}	
	
	/** 
	 *  Funcion que atiende la accion generarFichero. Genera los ficheros de Renegociación
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String generarFichero(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		String lenguaje = usr.getLanguage();
		
		String resultado="";
		Long idDisqueteAbono=new Long(0);
		boolean correcto=true;
		UserTransaction tx = null;
		Hashtable bancoMenorComision = new Hashtable();
		Hashtable banco = new Hashtable();
		Vector resultados=new Vector();
		Vector abonosAjenos=new Vector();
		Vector bancosRestantes=new Vector();
		Vector bancos=new Vector();
		Vector abonosBanco=new Vector();
		boolean ficheroSJCS = false;
		int cont = 0;
		
		try {		 				
			
			
		    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties p 	= new ReadProperties ("SIGA.properties");
			String extensionFichero = p.returnProperty("facturacion.extension.ficherosAbonos");
			String prefijoFichero = p.returnProperty("facturacion.prefijo.ficherosAbonos");
			
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idInstitucion=user.getLocation();
			FicheroBancarioAbonosForm miForm = (FicheroBancarioAbonosForm)formulario;
			
			// Recuperamos la procedencia de la llamada (1-Facturacion SJCS, 0-Facturacion)
			String fcs = miForm.getSjcs();
			if (fcs.equals("1")){
				ficheroSJCS = true;
			}
			
			// Manejadores para los accesos a BBDD
			FacAbonoAdm adminAbono=new FacAbonoAdm(this.getUserBean(request));
			FacBancoInstitucionAdm adminBancoInst=new FacBancoInstitucionAdm(this.getUserBean(request));
			CenInstitucionAdm admInstitucion=new CenInstitucionAdm(this.getUserBean(request));
			CenPersonaAdm admPersona=new CenPersonaAdm(this.getUserBean(request));
			FacDisqueteAbonosAdm admDisqueteAbonos=new FacDisqueteAbonosAdm(this.getUserBean(request));
			FacAbonoIncluidoEnDisqueteAdm admAbonoDisquete=new FacAbonoIncluidoEnDisqueteAdm(this.getUserBean(request));
			CenSucursalesAdm admSucursal=new CenSucursalesAdm(this.getUserBean(request));
			
			CenDireccionesAdm admDirecciones = new CenDireccionesAdm(this.getUserBean(request));
			
			// Comienzo control de transacciones
			tx = user.getTransactionPesada();
			tx.begin();
			if (ficheroSJCS){
				// Obtenemos todos los bancos de la institucion
				bancos=adminBancoInst.obtenerBancos(idInstitucion);
				Enumeration listaBancos=bancos.elements();
				while (listaBancos.hasMoreElements()){
					banco=((Row)listaBancos.nextElement()).getRow();
					abonosBanco=adminAbono.getAbonosBancoSjcs(idInstitucion,(String)banco.get(FacBancoInstitucionBean.C_BANCOS_CODIGO));
					if (!abonosBanco.isEmpty()){
						// Obtenemos el identificador
						idDisqueteAbono=admDisqueteAbonos.getNuevoID(idInstitucion);
						// Los datos de la sucursal
						Hashtable sucursal = admSucursal.getSucursal((String)banco.get(FacBancoInstitucionBean.C_BANCOS_CODIGO),(String)banco.get(FacBancoInstitucionBean.C_COD_SUCURSAL));
						// Datos emisor
						FicheroEmisorAbonoBean emisor=new FicheroEmisorAbonoBean();
						emisor.setIdentificador(new Integer((String)banco.get(FacBancoInstitucionBean.C_IDINSTITUCION)));
						emisor.setCodigoBanco((String)banco.get(FacBancoInstitucionBean.C_COD_BANCO)); // JBD cambiado bancos_codigo por cod_banco
						emisor.setCodigoSucursal((String)banco.get(FacBancoInstitucionBean.C_COD_SUCURSAL));
						emisor.setNumeroCuenta((String)banco.get(FacBancoInstitucionBean.C_NUMEROCUENTA));
						emisor.setNif((String)banco.get(FacBancoInstitucionBean.C_NIF));
						String nombre=admInstitucion.getNombreInstitucion((String)banco.get(FacBancoInstitucionBean.C_IDINSTITUCION));
						emisor.setNombre(nombre);
						emisor.setIdentificadorDisquete(idDisqueteAbono);
						// JBD cambiada direccion de sucursal por colegio
						// emisor.setDomicilio((String)sucursal.get(CenSucursalesBean.C_DOMICILIO));
						// emisor.setPlaza((String)sucursal.get("PLAZA"));
						emisor.setDomicilio((String)admInstitucion.getDomicilioInstitucion((String)banco.get(FacBancoInstitucionBean.C_IDINSTITUCION)));
						emisor.setPlaza((String)admInstitucion.getPoblacionInstitucion((String)banco.get(FacBancoInstitucionBean.C_IDINSTITUCION)));
						// Datos receptores
						Enumeration listaReceptores=abonosBanco.elements();
						Vector receptores=new Vector();
						while (listaReceptores.hasMoreElements()){
							Hashtable temporal=new Hashtable();
							temporal=((Row)listaReceptores.nextElement()).getRow();
							FicheroReceptorAbonoBean receptor=new FicheroReceptorAbonoBean();
							receptor.setIdentificador(new Long((String)temporal.get(FacAbonoBean.C_IDPERSONA)));
							receptor.setCodigoBanco((String)temporal.get(CenCuentasBancariasBean.C_CBO_CODIGO));
							receptor.setCodigoSucursal((String)temporal.get(CenCuentasBancariasBean.C_CODIGOSUCURSAL));
							receptor.setNumeroCuenta((String)temporal.get(CenCuentasBancariasBean.C_NUMEROCUENTA));
							receptor.setDigitosControl((String)temporal.get(CenCuentasBancariasBean.C_DIGITOCONTROL));
							String nif=admPersona.obtenerNIF((String)temporal.get(FacAbonoBean.C_IDPERSONA));
							receptor.setConcepto((String)temporal.get(FcsPagosJGBean.C_CONCEPTO));
							receptor.setDni(nif);
							receptor.setImporte(new Double((String)temporal.get("IMPORTE")));							
							nombre=admPersona.obtenerNombreApellidos((String)temporal.get(FacAbonoBean.C_IDPERSONA));
							receptor.setNombre(nombre);
							// jbd 09/12/2008 INC_05507_SIGA >>>
							// receptor.setNombrePago((String)temporal.get(FacAbonoBean.C_MOTIVOS));
							String idPersona = (String)temporal.get(FacAbonoBean.C_IDPERSONA);
							Vector direccionDespacho = null;
							direccionDespacho = admDirecciones.getDireccionDespacho(idPersona, idInstitucion, lenguaje);
							if (direccionDespacho!=null && direccionDespacho.size()>0) {
								receptor.setDomicilio((String)((Hashtable)direccionDespacho.get(0)).get("DOMICILIO_DESPACHO"));
								receptor.setPoblacion((String)((Hashtable)direccionDespacho.get(0)).get("POBLACION_DESPACHO"));
							}
							receptor.setNombrePago((String)temporal.get("NOMBREPAGO"));
							// <<<
							receptores.addElement(receptor);						
						}
						// Creacion de un fichero de abonos por cada banco restante
						int nlineas = this.crearFichero(emisor, receptores);
						cont ++;
						if (nlineas>0){
							// Creacion entrada FAC_DISQUETEABONO
							Hashtable disqueteAbono=new Hashtable();
							
							disqueteAbono.put(FacDisqueteAbonosBean.C_IDINSTITUCION,idInstitucion);
							disqueteAbono.put(FacDisqueteAbonosBean.C_IDDISQUETEABONO,idDisqueteAbono.toString());
							disqueteAbono.put(FacDisqueteAbonosBean.C_FECHA,"SYSDATE");
							disqueteAbono.put(FacDisqueteAbonosBean.C_BANCOS_CODIGO,(String)banco.get(FacBancoInstitucionBean.C_BANCOS_CODIGO));
							disqueteAbono.put(FacDisqueteAbonosBean.C_NOMBREFICHERO,prefijoFichero+idDisqueteAbono.toString()+"."+extensionFichero);
							disqueteAbono.put(FacDisqueteAbonosBean.C_FCS,"1");
							disqueteAbono.put(FacDisqueteAbonosBean.C_NUMEROLINEAS, new Integer(nlineas));
							correcto=admDisqueteAbonos.insert(disqueteAbono);						
							if (correcto){
								// Por cada abono incluido en el disquete, inserto entrada en FAC_ABONOINCLUIDODISQUETE
								listaReceptores=abonosBanco.elements();
								while ((correcto)&&(listaReceptores.hasMoreElements())){
									Hashtable temporal=new Hashtable();
									temporal=((Row)listaReceptores.nextElement()).getRow();
									Hashtable abonoDisquete=new Hashtable();
									abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IDINSTITUCION,idInstitucion);
									abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IDABONO,(String)temporal.get(FacAbonoBean.C_IDABONO));
									abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IDDISQUETEABONO,idDisqueteAbono.toString());
									abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IMPORTEABONADO,admAbonoDisquete.getImporteAbonado(idInstitucion,(String)temporal.get(FacAbonoBean.C_IDABONO)));
									//double importeAbonado = new Double((String)abonoDisquete.get(FacAbonoIncluidoEnDisqueteBean.C_IMPORTEABONADO)).doubleValue();
									double importeAbonado = Double.parseDouble(abonoDisquete.get(FacAbonoIncluidoEnDisqueteBean.C_IMPORTEABONADO).toString());
									abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_CONTABILIZADO,"N");
									correcto=admAbonoDisquete.insert(abonoDisquete);
									if (correcto) {
										// RGG 29/05/2009 Cambio de funciones de abono
									    // Obtengo el abono insertado
									    Hashtable htA = new Hashtable();
										htA.put(FacAbonoBean.C_IDINSTITUCION,idInstitucion);
										htA.put(FacAbonoBean.C_IDABONO,(String)temporal.get(FacAbonoBean.C_IDABONO));
										Vector vAbono = adminAbono.selectByPK(htA);
										FacAbonoBean bAbono = null;
										if (vAbono!=null && vAbono.size()>0) {
										    bAbono = (FacAbonoBean) vAbono.get(0);
										}
										bAbono.setImpPendientePorAbonar(new Double(bAbono.getImpPendientePorAbonar().doubleValue()-importeAbonado));
									    bAbono.setImpTotalAbonado(new Double(bAbono.getImpTotalAbonado().doubleValue() + importeAbonado));
									    bAbono.setImpTotalAbonadoPorBanco(new Double(bAbono.getImpTotalAbonadoPorBanco().doubleValue() + importeAbonado));
									    if (bAbono.getImpPendientePorAbonar().doubleValue()<=0) {
									        // pagado
									        bAbono.setEstado(new Integer(1));
									    } else {
									        if (bAbono.getIdCuenta()!=null) {
									            // pendiente pago banco
										        bAbono.setEstado(new Integer(5));
									        } else {
									            // pendiente pago caja
										        bAbono.setEstado(new Integer(6));
									        }
									    }
									    
									   if (!adminAbono.update(bAbono)){
										   throw new ClsExceptions("Error al actualizar estado e importes del abono: "+adminAbono.getError());
										}

									} else {
									    new ClsExceptions("Error al insertar el abono en el disquete: "+admAbonoDisquete.getError());
									}									
									
								}							
							}		
						}
					}
				}
			}else{ // Fichero no sjcs
				resultados=adminBancoInst.getBancoMenorComision(idInstitucion);
				if (!resultados.isEmpty()){
					bancoMenorComision=((Row)resultados.firstElement()).getRow();
					abonosAjenos=adminAbono.getAbonosBancosMenorComision(idInstitucion,(String)bancoMenorComision.get(FacBancoInstitucionBean.C_BANCOS_CODIGO));
					if (!abonosAjenos.isEmpty()){
						// Obtenemos el identificador
						idDisqueteAbono=admDisqueteAbonos.getNuevoID(idInstitucion);
						// Los datos de la sucursal
						Hashtable sucursal = admSucursal.getSucursal((String)bancoMenorComision.get(FacBancoInstitucionBean.C_BANCOS_CODIGO),(String)bancoMenorComision.get(FacBancoInstitucionBean.C_COD_SUCURSAL)); 
						// Datos emisor
						FicheroEmisorAbonoBean emisor=new FicheroEmisorAbonoBean();
						emisor.setIdentificador(new Integer((String)bancoMenorComision.get(FacBancoInstitucionBean.C_IDINSTITUCION)));
						emisor.setCodigoBanco((String)bancoMenorComision.get(FacBancoInstitucionBean.C_COD_BANCO)); // JBD cambiado bancos_codigo por cod_banco
						emisor.setCodigoSucursal((String)bancoMenorComision.get(FacBancoInstitucionBean.C_COD_SUCURSAL));
						emisor.setNumeroCuenta((String)bancoMenorComision.get(FacBancoInstitucionBean.C_NUMEROCUENTA));
						emisor.setNif((String)bancoMenorComision.get(FacBancoInstitucionBean.C_NIF));
						String nombre=admInstitucion.getNombreInstitucion((String)bancoMenorComision.get(FacBancoInstitucionBean.C_IDINSTITUCION));
						emisor.setNombre(nombre);
						emisor.setIdentificadorDisquete(idDisqueteAbono);
						//emisor.setDomicilio((String)sucursal.get(CenSucursalesBean.C_DOMICILIO));
						// RGG 09/07/2008 Se pone la direccion del colegio
						emisor.setDomicilio(admInstitucion.getDomicilioInstitucion((String)bancoMenorComision.get(FacBancoInstitucionBean.C_IDINSTITUCION)));
						// RGG 09/07/2008 Se deja la plaza de la direccion de la sucursal.
						// emisor.setPlaza((String)sucursal.get("PLAZA"));
						emisor.setPlaza(admInstitucion.getPoblacionInstitucion((String)bancoMenorComision.get(FacBancoInstitucionBean.C_IDINSTITUCION)));
						// Datos receptores
						Enumeration listaReceptores=abonosAjenos.elements();
						Vector receptores=new Vector();
						while (listaReceptores.hasMoreElements()){
							Hashtable temporal=new Hashtable();
							temporal=((Row)listaReceptores.nextElement()).getRow();
							FicheroReceptorAbonoBean receptor=new FicheroReceptorAbonoBean();
							receptor.setIdentificador(new Long((String)temporal.get(FacAbonoBean.C_IDPERSONA)));
							receptor.setCodigoBanco((String)temporal.get(CenCuentasBancariasBean.C_CBO_CODIGO));
							receptor.setCodigoSucursal((String)temporal.get(CenCuentasBancariasBean.C_CODIGOSUCURSAL));
							receptor.setNumeroCuenta((String)temporal.get(CenCuentasBancariasBean.C_NUMEROCUENTA));
							receptor.setDigitosControl((String)temporal.get(CenCuentasBancariasBean.C_DIGITOCONTROL));
							String nif=admPersona.obtenerNIF((String)temporal.get(FacAbonoBean.C_IDPERSONA));
							receptor.setConcepto("9");
							receptor.setDni(nif);
							receptor.setImporte(new Double((String)temporal.get("IMPORTE")));
							nombre=admPersona.obtenerNombreApellidos((String)temporal.get(FacAbonoBean.C_IDPERSONA));
							receptor.setNombre(nombre);
							// jbd 09/12/2008 INC_05507_SIGA >>>
							receptor.setNombrePago((String)temporal.get(FacAbonoBean.C_MOTIVOS));
							String idPersona = (String)temporal.get(FacAbonoBean.C_IDPERSONA);
							Vector direccionDespacho = null;
							direccionDespacho = admDirecciones.getDireccionDespacho(idPersona, idInstitucion, lenguaje);
							if (direccionDespacho!=null && direccionDespacho.size()>0) {
								receptor.setDomicilio((String)((Hashtable)direccionDespacho.get(0)).get("DOMICILIO_DESPACHO"));
								receptor.setPoblacion((String)((Hashtable)direccionDespacho.get(0)).get("POBLACION_DESPACHO"));
							}
							// <<<
							receptores.addElement(receptor);	
							
						}
						
						// Creacion del fichero de abonos para el banco de menores comisiones ajenas
						int nlineas = this.crearFichero(emisor, receptores);
						cont ++;
						if (nlineas>0){
							// Creacion entrada FAC_DISQUETEABONO
							Hashtable disqueteAbono=new Hashtable();
							disqueteAbono.put(FacDisqueteAbonosBean.C_IDINSTITUCION,idInstitucion);
							disqueteAbono.put(FacDisqueteAbonosBean.C_IDDISQUETEABONO,idDisqueteAbono.toString());
							disqueteAbono.put(FacDisqueteAbonosBean.C_FECHA,"SYSDATE");
							disqueteAbono.put(FacDisqueteAbonosBean.C_BANCOS_CODIGO,(String)bancoMenorComision.get(FacBancoInstitucionBean.C_BANCOS_CODIGO));
							disqueteAbono.put(FacDisqueteAbonosBean.C_NOMBREFICHERO,prefijoFichero+idDisqueteAbono.toString()+"."+extensionFichero);
							disqueteAbono.put(FacDisqueteAbonosBean.C_FCS,"0");
							disqueteAbono.put(FacDisqueteAbonosBean.C_NUMEROLINEAS, new Integer(nlineas));
							correcto=admDisqueteAbonos.insert(disqueteAbono);						
							if (correcto){
								// Por cada abono incluido en el disquete, inserto entrada en FAC_ABONOINCLUIDODISQUETE
								listaReceptores=abonosAjenos.elements();
								while ((correcto)&&(listaReceptores.hasMoreElements())){
									Hashtable temporal=new Hashtable();
									temporal=((Row)listaReceptores.nextElement()).getRow();
									Hashtable abonoDisquete=new Hashtable();
									abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IDINSTITUCION,idInstitucion);
									abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IDABONO,(String)temporal.get(FacAbonoBean.C_IDABONO));
									abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IDDISQUETEABONO,idDisqueteAbono.toString());
									abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IMPORTEABONADO,admAbonoDisquete.getImporteAbonado(idInstitucion,(String)temporal.get(FacAbonoBean.C_IDABONO)));
									//double importeAbonado = ((Double)abonoDisquete.get(FacAbonoIncluidoEnDisqueteBean.C_IMPORTEABONADO)).doubleValue();
									double importeAbonado = Double.parseDouble(abonoDisquete.get(FacAbonoIncluidoEnDisqueteBean.C_IMPORTEABONADO).toString());
									abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_CONTABILIZADO,"N");
									correcto=admAbonoDisquete.insert(abonoDisquete);
									if (correcto) {
										// RGG 29/05/2009 Cambio de funciones de abono
									    // Obtengo el abono insertado
									    Hashtable htA = new Hashtable();
										htA.put(FacAbonoBean.C_IDINSTITUCION,idInstitucion);
										htA.put(FacAbonoBean.C_IDABONO,(String)temporal.get(FacAbonoBean.C_IDABONO));
										Vector vAbono = adminAbono.selectByPK(htA);
										FacAbonoBean bAbono = null;
										if (vAbono!=null && vAbono.size()>0) {
										    bAbono = (FacAbonoBean) vAbono.get(0);
										}
										bAbono.setImpPendientePorAbonar(new Double(bAbono.getImpPendientePorAbonar().doubleValue()-importeAbonado));
									    bAbono.setImpTotalAbonado(new Double(bAbono.getImpTotalAbonado().doubleValue() + importeAbonado));
									    bAbono.setImpTotalAbonadoPorBanco(new Double(bAbono.getImpTotalAbonadoPorBanco().doubleValue() + importeAbonado));
									    if (bAbono.getImpPendientePorAbonar().doubleValue()<=0) {
									        // pagado
									        bAbono.setEstado(new Integer(1));
									    } else {
									        if (bAbono.getIdCuenta()!=null) {
									            // pendiente pago banco
										        bAbono.setEstado(new Integer(5));
									        } else {
									            // pendiente pago caja
										        bAbono.setEstado(new Integer(6));
									        }
									    }
									    if (!adminAbono.update(bAbono)){
										    throw new ClsExceptions("Error al actualizar estado e importes del abono: "+adminAbono.getError());
										}

									} else {
									    new ClsExceptions("Error al insertar el abono en el disquete: "+admAbonoDisquete.getError());
									}									
								}							
							}
							
						}
					}
				}
				
				//Ahora los abonos para los bancos restantes 
				bancosRestantes=adminBancoInst.getRestoBancosConComision(idInstitucion,(String)bancoMenorComision.get(FacBancoInstitucionBean.C_BANCOS_CODIGO));
				if (!bancosRestantes.isEmpty()){
					Enumeration listaBancos=bancosRestantes.elements();
					while (listaBancos.hasMoreElements()){
						banco=((Row)listaBancos.nextElement()).getRow();
						abonosBanco=adminAbono.getAbonosBanco(idInstitucion,(String)banco.get(FacBancoInstitucionBean.C_BANCOS_CODIGO));
						if (!abonosBanco.isEmpty()){
							// Obtenemos el identificador
							idDisqueteAbono=admDisqueteAbonos.getNuevoID(idInstitucion);
							// Los datos de la sucursal
							Hashtable sucursal = admSucursal.getSucursal((String)banco.get(FacBancoInstitucionBean.C_BANCOS_CODIGO),(String)banco.get(FacBancoInstitucionBean.C_COD_SUCURSAL));
							// Datos emisor
							FicheroEmisorAbonoBean emisor=new FicheroEmisorAbonoBean();
							emisor.setIdentificador(new Integer((String)banco.get(FacBancoInstitucionBean.C_IDINSTITUCION)));
							emisor.setCodigoBanco((String)banco.get(FacBancoInstitucionBean.C_COD_BANCO)); // JBD cambiado bancos_codigo por cod_banco
							emisor.setCodigoSucursal((String)banco.get(FacBancoInstitucionBean.C_COD_SUCURSAL));
							emisor.setNumeroCuenta((String)banco.get(FacBancoInstitucionBean.C_NUMEROCUENTA));
							emisor.setNif((String)banco.get(FacBancoInstitucionBean.C_NIF));
							String nombre=admInstitucion.getNombreInstitucion((String)banco.get(FacBancoInstitucionBean.C_IDINSTITUCION));
							emisor.setNombre(nombre);
							emisor.setIdentificadorDisquete(idDisqueteAbono);
							// JBD cambiada direccion de sucursal por colegio
							//emisor.setDomicilio((String)sucursal.get(CenSucursalesBean.C_DOMICILIO));
							//emisor.setPlaza((String)sucursal.get("PLAZA"));
							emisor.setDomicilio((String)admInstitucion.getDomicilioInstitucion((String)banco.get(FacBancoInstitucionBean.C_IDINSTITUCION)));
							emisor.setPlaza((String)admInstitucion.getPoblacionInstitucion((String)banco.get(FacBancoInstitucionBean.C_IDINSTITUCION)));
							
							// Datos receptores
							Enumeration listaReceptores=abonosBanco.elements();
							Vector receptores=new Vector();
							while (listaReceptores.hasMoreElements()){
								Hashtable temporal=new Hashtable();
								temporal=((Row)listaReceptores.nextElement()).getRow();
								FicheroReceptorAbonoBean receptor=new FicheroReceptorAbonoBean();
								receptor.setIdentificador(new Long((String)temporal.get(FacAbonoBean.C_IDPERSONA)));
								receptor.setCodigoBanco((String)temporal.get(CenCuentasBancariasBean.C_CBO_CODIGO));
								receptor.setCodigoSucursal((String)temporal.get(CenCuentasBancariasBean.C_CODIGOSUCURSAL));
								receptor.setNumeroCuenta((String)temporal.get(CenCuentasBancariasBean.C_NUMEROCUENTA));
								receptor.setDigitosControl((String)temporal.get(CenCuentasBancariasBean.C_DIGITOCONTROL));
								String nif=admPersona.obtenerNIF((String)temporal.get(FacAbonoBean.C_IDPERSONA));
								receptor.setConcepto("9");
								receptor.setDni(nif);
								receptor.setImporte(new Double((String)temporal.get("IMPORTE")));
								nombre=admPersona.obtenerNombreApellidos((String)temporal.get(FacAbonoBean.C_IDPERSONA));
								receptor.setNombre(nombre);
								// jbd 09/12/2008 INC_05507_SIGA >>>
								// receptor.setNombrePago((String)temporal.get(FacAbonoBean.C_MOTIVOS));
								String idPersona = (String)temporal.get(FacAbonoBean.C_IDPERSONA);
								Vector direccionDespacho = null;
								direccionDespacho = admDirecciones.getDireccionDespacho(idPersona, idInstitucion, lenguaje);
								if (direccionDespacho!=null && direccionDespacho.size()>0) {
									receptor.setDomicilio((String)((Hashtable)direccionDespacho.get(0)).get("DOMICILIO_DESPACHO"));
									receptor.setPoblacion((String)((Hashtable)direccionDespacho.get(0)).get("POBLACION_DESPACHO"));
								}
								receptor.setNombrePago((String)temporal.get("NOMBREPAGO"));
								// <<<
								receptores.addElement(receptor);						
							}
							// Creacion de un fichero de abonos por cada banco restante
							int nlineas = this.crearFichero(emisor, receptores);
							cont ++;
							if (nlineas>0){
								// Creacion entrada FAC_DISQUETEABONO
								Hashtable disqueteAbono=new Hashtable();
								
								disqueteAbono.put(FacDisqueteAbonosBean.C_IDINSTITUCION,idInstitucion);
								disqueteAbono.put(FacDisqueteAbonosBean.C_IDDISQUETEABONO,idDisqueteAbono.toString());
								disqueteAbono.put(FacDisqueteAbonosBean.C_FECHA,"SYSDATE");
								disqueteAbono.put(FacDisqueteAbonosBean.C_BANCOS_CODIGO,(String)banco.get(FacBancoInstitucionBean.C_BANCOS_CODIGO));
								disqueteAbono.put(FacDisqueteAbonosBean.C_NOMBREFICHERO,prefijoFichero+idDisqueteAbono.toString()+"."+extensionFichero);
								disqueteAbono.put(FacDisqueteAbonosBean.C_FCS,fcs);
								disqueteAbono.put(FacDisqueteAbonosBean.C_NUMEROLINEAS, new Integer(nlineas));
								correcto=admDisqueteAbonos.insert(disqueteAbono);						
								if (correcto){
									// Por cada abono incluido en el disquete, inserto entrada en FAC_ABONOINCLUIDODISQUETE
									listaReceptores=abonosBanco.elements();
									while ((correcto)&&(listaReceptores.hasMoreElements())){
										Hashtable temporal=new Hashtable();
										temporal=((Row)listaReceptores.nextElement()).getRow();
										Hashtable abonoDisquete=new Hashtable();
										abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IDINSTITUCION,idInstitucion);
										abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IDABONO,(String)temporal.get(FacAbonoBean.C_IDABONO));
										abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IDDISQUETEABONO,idDisqueteAbono.toString());
										abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IMPORTEABONADO,admAbonoDisquete.getImporteAbonado(idInstitucion,(String)temporal.get(FacAbonoBean.C_IDABONO)));
										// double importeAbonado = new Double((String)abonoDisquete.get(FacAbonoIncluidoEnDisqueteBean.C_IMPORTEABONADO)).doubleValue();
										double importeAbonado = Double.parseDouble(abonoDisquete.get(FacAbonoIncluidoEnDisqueteBean.C_IMPORTEABONADO).toString());
										abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_CONTABILIZADO,"N");
										correcto=admAbonoDisquete.insert(abonoDisquete);
										if (correcto) {
											// RGG 29/05/2009 Cambio de funciones de abono
										    // Obtengo el abono insertado
										    Hashtable htA = new Hashtable();
											htA.put(FacAbonoBean.C_IDINSTITUCION,idInstitucion);
											htA.put(FacAbonoBean.C_IDABONO,(String)temporal.get(FacAbonoBean.C_IDABONO));
											Vector vAbono = adminAbono.selectByPK(htA);
											FacAbonoBean bAbono = null;
											if (vAbono!=null && vAbono.size()>0) {
											    bAbono = (FacAbonoBean) vAbono.get(0);
											}
											bAbono.setImpPendientePorAbonar(new Double(bAbono.getImpPendientePorAbonar().doubleValue()-importeAbonado));
										    bAbono.setImpTotalAbonado(new Double(bAbono.getImpTotalAbonado().doubleValue() + importeAbonado));
										    bAbono.setImpTotalAbonadoPorBanco(new Double(bAbono.getImpTotalAbonadoPorBanco().doubleValue() + importeAbonado));
										    if (bAbono.getImpPendientePorAbonar().doubleValue()<=0) {
										        // pagado
										        bAbono.setEstado(new Integer(1));
										    } else {
										        if (bAbono.getIdCuenta()!=null) {
										            // pendiente pago banco
											        bAbono.setEstado(new Integer(5));
										        } else {
										            // pendiente pago caja
											        bAbono.setEstado(new Integer(6));
										        }
										    }
										    
										   
										     if (!adminAbono.update(bAbono)){
											    throw new ClsExceptions("Error al actualizar estado e importes del abono: "+adminAbono.getError());
											  }
										   

										} else {
										    new ClsExceptions("Error al insertar el abono en el disquete: "+admAbonoDisquete.getError());
										}
									}							
								}		
							}
						}
					}
				}
			} // FI sjcs
//			
			if (correcto){
			    
			    
				tx.commit();
				
				String mensaje = "facturacion.ficheroBancarioAbonos.mensaje.generacionDisquetesOK";
				String[] datos = {String.valueOf(cont)};
				
				mensaje = UtilidadesString.getMensaje(mensaje, datos, lenguaje);				
				request.setAttribute("mensaje",mensaje);	
				
				resultado = "exitoConString";				
			}	
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,tx); 
		}
		return (resultado);		
	}	
	
	/** 
	 *  Funcion que genera las lineas del fichero e inserta en el fichero.
	 * @param  bEmisor 		- bean que contiene los datos del emisor.
	 * @param  vReceptores	- Vector que contiene los bean de cada uno de los receptores.	 
	 * @return  boolean		- devuelve true en el caso de que se haya generado correctamente el fichero  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	private int crearFichero(FicheroEmisorAbonoBean bEmisor, Vector vReceptores) throws SIGAException {
		//int n = (vReceptores.size() * 2) + 4 + 1;
		// (numeroReceptores * numeroCampos) + datosCabecera + datosTotales
		int n = (vReceptores.size() * 5) + 4 + 1;
		
		String[] cabecera = new String[n];
		boolean resul = false;		
		int nlinea = 0;
		try{	
			
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp 		= new ReadProperties("SIGA.properties");			
			String rutaServidor 	= rp.returnProperty("facturacion.directorioFisicoAbonosBancosJava") + rp.returnProperty("facturacion.directorioAbonosBancosJava");
			String sPrefijo 		= rp.returnProperty("facturacion.prefijo.ficherosAbonos");
			String sExtension 		= rp.returnProperty("facturacion.extension.ficherosAbonos");
			String nombreFichero	= "";
			String sIdInstitucion 	= bEmisor.getIdentificador().toString();	
			String numDisco			= bEmisor.getIdentificadorDisquete().toString();
			
//			Generamos el nombre del fichero.
			String barra = "";
			if (rutaServidor.indexOf("/") > -1){ 
				barra = "/";
			}
			if (rutaServidor.indexOf("\\") > -1){ 
				barra = "\\";
			}
			rutaServidor += barra + sIdInstitucion;
			nombreFichero = barra + sPrefijo + numDisco + "." + sExtension;
			
			// *********************	Generamos las lineas del fichero	*********************
			
			// ********************************* Datos Emisor **********************************
			String sCodRegistro			= "03";
			String sCodOperacion		= "56"; // 56:Si es una orden de transferencia.
			// 57:Si es ha de confecionarse un Cheque Bancario.
			String sCodOrdenante 		= completarEspacios("Nif", bEmisor.getNif(), "D", " ", 10, false);
			
			String fActual 				= UtilidadesBDAdm.getFechaBD("");
			String[] aux 				= fActual.split("/");
			String sFechaEnvio			= aux[0].concat(aux[1]).concat(aux[2].substring(2,4));
			String sFechaEmision		= sFechaEnvio;
			
			String sEntidadOrdenante	= completarEspacios("Entidad", bEmisor.getCodigoBanco(), "D", "0", 4, false);
			String sOficinaOrdenante	= completarEspacios("Oficina", bEmisor.getCodigoSucursal(), "D", "0", 4, false);
			String sCuentaOrdenante		= completarEspacios("Cuenta Bancaria", bEmisor.getNumeroCuenta(), "D", "0", 10, false);			
			String sDControlOrdenante	= obtenerDigitoControl("00" + sEntidadOrdenante + sOficinaOrdenante);
			sDControlOrdenante 			+=obtenerDigitoControl(sCuentaOrdenante);
			
			String sDetalleCargo		= "0"; // 0:Sin relacion
			// 1:Con relación 
			String sNombreOrdenante		= completarEspacios("Nombre", bEmisor.getNombre(), "I", " ", 36, true);
			String sDomicilioOrdenante	= completarEspacios("Domicilio", bEmisor.getDomicilio(), "I", " ", 36, true);	
			String sPlazaOrdenante		= completarEspacios("Plaza", bEmisor.getPlaza(), "I", " ", 36, true);
			
			String sNumDato	= "001";
			cabecera[0] = sCodRegistro + sCodOperacion + sCodOrdenante + rellenarEspacios(12)+ sNumDato + sFechaEnvio + sFechaEmision + sEntidadOrdenante + 
			sOficinaOrdenante +	sCuentaOrdenante + sDetalleCargo + rellenarEspacios(3) + sDControlOrdenante + rellenarEspacios(7);
			sNumDato		= "002";
			cabecera[1] = sCodRegistro + sCodOperacion + sCodOrdenante + rellenarEspacios(12)+ sNumDato + sNombreOrdenante + rellenarEspacios(7);
			sNumDato		= "003";
			cabecera[2] = sCodRegistro + sCodOperacion + sCodOrdenante + rellenarEspacios(12)+ sNumDato + sDomicilioOrdenante + rellenarEspacios(7);
			sNumDato		= "004";
			cabecera[3] = sCodRegistro + sCodOperacion + sCodOrdenante + rellenarEspacios(12)+ sNumDato + sPlazaOrdenante + rellenarEspacios(7);
			
			
//			****************************  Datos de los receptores   *****************************
			
			String sGastos 		= "1";
			String sConcepto 	= "9";			
			int cantidad 		= 0; 
			int importe;
			String sRefBeneficiario;			
			String sImporte;
			String sEntidadBeneficiario;
			String sOficinaBeneficiario;
			String sCuentaBeneficiario;
			String sDControlBeneficiario;
			String sNombreBeneficiario;
			String sNumRegistros;
			String sTotalRegistros;
			
			String sDomicilioBeneficiario;
			String sPoblacionBeneficiario;
			String sMotivos;
			
			nlinea = 3;
			Enumeration en = vReceptores.elements();
			while(en.hasMoreElements()){			
				FicheroReceptorAbonoBean bReceptor 	= (FicheroReceptorAbonoBean)en.nextElement();
				
				sCodRegistro			= "06";
				sRefBeneficiario 		= completarEspacios("Identificador", bReceptor.getIdentificador().toString(), "D", " ", 12, false);
				importe					= (int)Math.rint(bReceptor.getImporte().doubleValue()*100);
				cantidad 				+= importe;
				sImporte				= completarEspacios("Importe", Integer.toString(importe), "D", "0", 12, false);
				sEntidadBeneficiario	= completarEspacios("Entidad", bReceptor.getCodigoBanco(), "D", "0", 4, false);
				sOficinaBeneficiario	= completarEspacios("Oficina", bReceptor.getCodigoSucursal(), "D", "0", 4, false);	
				sCuentaBeneficiario		= completarEspacios("Cuenta Bancaria", bReceptor.getNumeroCuenta(), "D", "0", 10, false);
				sDControlBeneficiario	= obtenerDigitoControl("00" + sEntidadBeneficiario + sOficinaBeneficiario);
				sDControlBeneficiario	+=obtenerDigitoControl(sCuentaBeneficiario);
				sNombreBeneficiario		= completarEspacios("Nombre", bReceptor.getNombre(), "I", " ", 36, true);
				String direccion 		= UtilidadesString.replaceAllIgnoreCase(bReceptor.getDomicilio(), "\n", " ");
				direccion 		= UtilidadesString.replaceAllIgnoreCase(direccion, "\r", " ");
				sDomicilioBeneficiario = completarEspacios ("Direccion", direccion, "I", " ", 36, true);
				sPoblacionBeneficiario = completarEspacios ("Poblacion", bReceptor.getPoblacion(), "I", " ", 36, true);
				sMotivos = completarEspacios ("NombrePago", bReceptor.getNombrePago(), "I", " ", 36, true);
				
				if ((bReceptor.getConcepto()!=null)&& (bReceptor.getConcepto()!="0")){
					sConcepto = completarEspacios ("Concepto", bReceptor.getConcepto(), "I", " ", 1, true);
				}
				
				nlinea ++;
				sNumDato		 = "010";				
				cabecera[nlinea] = sCodRegistro + sCodOperacion + sCodOrdenante + sRefBeneficiario + sNumDato + sImporte + sEntidadBeneficiario + 
				sOficinaBeneficiario +	sCuentaBeneficiario + sGastos + sConcepto + rellenarEspacios(2) + sDControlBeneficiario + rellenarEspacios(7);
				
				nlinea ++;
				sNumDato		 = "011";
				cabecera[nlinea] = sCodRegistro + sCodOperacion + sCodOrdenante + sRefBeneficiario + sNumDato + sNombreBeneficiario + rellenarEspacios(7);
				
				// jbd 9/12/2008 - INC_05507_SIGA >>>
				nlinea ++;
				sNumDato		 = "012"; // Direccion
				cabecera[nlinea] = sCodRegistro + sCodOperacion + sCodOrdenante + sRefBeneficiario + sNumDato + sDomicilioBeneficiario + rellenarEspacios(7); 
				
				nlinea ++;
				sNumDato		 = "014"; // Poblacion
				cabecera[nlinea] = sCodRegistro + sCodOperacion + sCodOrdenante + sRefBeneficiario + sNumDato +  sPoblacionBeneficiario + rellenarEspacios(7);
				
				nlinea ++;
				sNumDato		 = "016"; // Concepto
				cabecera[nlinea] = sCodRegistro + sCodOperacion + sCodOrdenante + sRefBeneficiario + sNumDato + sMotivos + rellenarEspacios(7);
				// <<< INC_05507_SIGA
			}
			
//			****************************   Datos de los Totales   *****************************
			
			sCodRegistro		 = "08";
			sImporte		 	 = completarEspacios("Importe", Integer.toString(cantidad), "D", "0", 12, false);	
			sNumRegistros		 = completarEspacios("Numero Registros", Integer.toString(vReceptores.size()), "D", "0", 8, false);
			sTotalRegistros		 = completarEspacios("Numero Registros",Integer.toString(nlinea+2), "D", "0", 10, false);			
			cabecera[nlinea+1] 	 = sCodRegistro + sCodOperacion + sCodOrdenante + rellenarEspacios(15) + sImporte + sNumRegistros + sTotalRegistros + rellenarEspacios(13);
			
			resul 				 = escribirFichero(cabecera,rutaServidor,nombreFichero);	
			
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}
		if (resul){
			return nlinea+2; // Se suma 2 porque empieza en cero y no se incrementa en la ultima linea
		}else{
			return 0;
		}
	}
	
	/** 
	 *  Funcion para alinear y ajustar el texto segun los paramentros del fichero.
	 * @param  cadena 			- cadena que queremos ajustar.
	 * @param  alineacion		- alineación de la cadena 'D': derecha, 'I': izquierda.
	 * @param  comodin 			- comodin con el que queremos rellenar, (espacio en blanco o ceros).
	 * @param  longitudMaxima 	- longitud que debe contener la cadena, para incluirlo en el fichero
	 * @param  suprimir 		- true: permite cortar la cadena en el caso de superar el tamanho máximo.
	 * @return  String 			- devuelve la cadena con el formato adecuado.  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	private String completarEspacios(String campo, String cadena, String alineacion, String comodin, int longitudMaxima, boolean suprimir) throws SIGAException{
		String auxCadena="";
		try{
		    if (cadena!=null) {
		    	// Pasamos el texto a mayusculas, quitamos acentos y caracteres raros
		    	// Usamos el mismo metodo que en el modelo 190
		    	cadena = cadena.toUpperCase();
		    	cadena = UtilidadesString.quitarAcentos(cadena);
		    	cadena = UtilidadesString.validarModelo34(cadena);
				int longitudCadena = cadena.length();
				// La cadena excede el tamanho máximo del que le corresponde en el fichero.
				if (longitudCadena > longitudMaxima){
					if (suprimir){			// Cortamos la cadena al tamanho máximo (ej, nombre) 	
						auxCadena = cadena.substring(0,longitudMaxima);
					}
					else{					// No podemos cortar la cadena (ej. DNI) pero excede del tamanho. Error
						throw new Exception("Error el dato " + campo + ": " + cadena + " excede el tamanho  del permitido por el registro.");
						
					}
				}	
				else{						// Tenemos que completar la cadena
					String sRelleno = "";
					for(int i=0; i<(longitudMaxima - longitudCadena); i++ ){
						sRelleno += comodin;
					}	
					if (alineacion.equalsIgnoreCase("D")){	// Alineación a la derecha				
						auxCadena = sRelleno + cadena;
					}
					else{					// Alineación a la izquierda
						auxCadena = cadena + sRelleno;
					}
					
				}
		    }
		}catch (Exception e) {		
			throwExcp("menssages.general.error",e,null); 	  	
		}
		
		return auxCadena;
	}
	
	/** 
	 *  Funcion que devuelve tantos espacios en blanco como el numero pasado como paramentro.
	 * @param  longitudMaxima - numero de espacios que queremos generar.
	 * @return  String - cadena con en numero de espacios. 	
	 */
	private String rellenarEspacios(int longitudMaxima){
		String auxCadena="";
		for(int i=0; i<longitudMaxima; i++ ){
			auxCadena += " ";
		}		
		return auxCadena;
	}
	
	/** 
	 *  Funcion que escribe en el fichero.
	 * @param  lineas[] - lineas que queremos escribir en el fichero.
	 * @param  fileName	- ruta del fichero donde vamos a escribir.
	 * @param  nomFich 	- nombre del fichero que vamos a crear.
	 * @return  boolean - devuelve true en el caso de que se haya generado correctamente el fichero  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	private boolean escribirFichero(String[] lineas, String fileName, String nomFich) throws SIGAException{
		boolean result = false;
		File ficLog;		
		try{
			
			ficLog = new File(fileName);
			if (!ficLog.exists()){
				ficLog.mkdirs();			     	       
			}
			ficLog = new File(fileName + nomFich);
			if (ficLog.exists()){
				ficLog.delete();
				ficLog.createNewFile();
			}
			
			PrintWriter printer = new PrintWriter(new BufferedWriter(new FileWriter(ficLog, true)));
			for (int i=0; i<lineas.length; i++){
				printer.print(lineas[i]+"\r\n");
			}			
			printer.flush();
			printer.close();	
			result = true;
			
		}catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}		
		return result;
	}
	
	
	/** 
	 *  Funcion para obtener el digito de control de la cuenta bancaria.
	 * @param  String - cadena con la que calcularemos el dígito de control.
	 * @return  String - digito de control  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	public String obtenerDigitoControl(String cadena) throws SIGAException{
		int[] valores = {1, 2, 4, 8, 5, 10, 9, 7, 3, 6};
		int  control = 0;
		try{			
			for (int i=0; i<=9; i++){
				control += Integer.parseInt(String.valueOf(cadena.charAt(i))) * valores[i];	
			}			   	  
			control = 11 - (control % 11);
			if (control == 11) control = 0;
			else if (control == 10) control = 1;			
		}catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}
		return String.valueOf(control);
	}
	
	protected String informeRemesa (MasterForm formulario, HttpServletRequest request) throws SIGAException 
	{
		FicheroBancarioAbonosForm form = (FicheroBancarioAbonosForm)formulario;

		Vector ocultos 			= (Vector)form.getDatosTablaOcultos(0);			
		String idDisqueteCargo 	= (String)ocultos.elementAt(0);	
		String idInstitucion	= this.getIDInstitucion(request).toString();			

		FacDisqueteAbonosAdm adm = new FacDisqueteAbonosAdm(this.getUserBean(request));
		Hashtable h = adm.getInformeRemesa(idInstitucion, idDisqueteCargo);
		if (h != null) {
			
			request.setAttribute("datosImpreso", h);
			request.setAttribute("abonos", "1");
			
//			request.setAttribute("importeTotal",         UtilidadesHash.getString(h,"importeTotal"));
//			request.setAttribute("numOrdenes",           UtilidadesHash.getString(h,"numOrdenes"));
//			request.setAttribute("numRegistros",         UtilidadesHash.getString(h,"numRegistros"));
//			request.setAttribute("fechaCreacionFichero", UtilidadesHash.getString(h,"fechaCreacionFichero"));
//			request.setAttribute("fechaEmisionOrdenes",  UtilidadesHash.getString(h,"fechaEmisionOrdenes"));
//			request.setAttribute("nombreInstitucion",    UtilidadesHash.getString(h,"nombreInstitucion"));
//			request.setAttribute("codigoOrdenante",      UtilidadesHash.getString(h,"codigoOrdenante"));
//			request.setAttribute("cuentaAbono",          UtilidadesHash.getString(h,"cuentaAbono"));
		}
		
		return "informeRemesa"; 
	}
	
}





