
package com.siga.censo.action;

import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenColaCambioLetradoAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenDatosColegialesEstadoAdm;
import com.siga.beans.CenDatosColegialesEstadoBean;
import com.siga.beans.CenDireccionTipoDireccionAdm;
import com.siga.beans.CenDireccionTipoDireccionBean;
import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.CenDireccionesBean;
import com.siga.beans.CenHistoricoBean;
import com.siga.beans.CenNoColegiadoAdm;
import com.siga.beans.CenNoColegiadoBean;
import com.siga.beans.ScsRetencionesAdm;
import com.siga.general.EjecucionPLs;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.action.RetencionesIRPFAction;


/**
 * Clase action del caso de uso COLEGIAR NO COLEGIADOS
 * @author pilar.duran
 * @since 18-04-2007
 * @version adrian.ayala - 2008-06-03 - revision de insertarColegiado()
 *   para asegurar el correcto funcionamiento de la insercion en cola 
 *   para la copia de direcciones de colegiados a Consejos
 */
public class ColegiarAction extends MasterAction
{
	/** 
	 * Funcion que atiende a las peticiones. Segun el valor del parametro 
	 *   modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public ActionForward executeInternal (ActionMapping mapping,
										  ActionForm formulario,
										  HttpServletRequest request,
										  HttpServletResponse response)
			throws SIGAException
	{
		String mapDestino = "exception";
		MasterForm miForm = null;
		
		try { 
			do {
				miForm = (MasterForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();

					if (accion.equalsIgnoreCase("abrirColegiar")){
						mapDestino = abrirColegiar(mapping, miForm, request, response);
						break;
					}else if (accion.equalsIgnoreCase("insertar")){
						mapDestino = insertarColegiado( mapping, miForm, request, response);
						break;
					}else {
						return super.executeInternal(mapping,formulario,request,response);
					}
				}
			} while (false);

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	{ 
			    throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {
			es.setSubLiteral("");
			throw es;
//			throwExcp (es.getLiteral(), new String[] {"modulo.censo"}, es, tx);
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"});
		}
	} //executeInternal()
	
	/**
	 * Metodo que implementa el modo abrir
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrirColegiar (ActionMapping mapping, 		
									MasterForm formulario, 
									HttpServletRequest request, 
									HttpServletResponse response)
			throws SIGAException
	{
		String idPersonaX=request.getParameter("idPersona");
		request.setAttribute("idPersonaX",idPersonaX);
		return "abrirColegiar";
	} //abrirColegiar()
	
	/**
	 * Realiza la colegiacion de un No colegiado o un Letrado de Consejo
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	private String insertarColegiado (ActionMapping mapping, 
									  MasterForm formulario, 
									  HttpServletRequest request, 
									  HttpServletResponse response) 
			throws SIGAException
	{
		//Controles de adm utilizados
		CenClienteAdm admCliente;
		CenColegiadoAdm admCol;
		CenDatosColegialesEstadoAdm admDCE;
		CenNoColegiadoAdm admNoCol;
		CenDireccionesAdm admDir;
		CenDireccionTipoDireccionAdm admTipoDir;
		
		//Variables generales
		UsrBean user;
		String idInstitucionOrigen;
		UserTransaction t = null;
		String forward = "exitoInsercion";
		

		try
		{
			////////// OBTENIENDO DATOS //////////
			
			//obteniendo usuario e institucion
			user = this.getUserBean (request);
			idInstitucionOrigen = user.getLocation ();
			
			//obteniendo administradores de las tablas
			admCliente = new CenClienteAdm (user);
			admCol = new CenColegiadoAdm (user);
			admDCE = new CenDatosColegialesEstadoAdm (user);
			admNoCol = new CenNoColegiadoAdm (user);
			admDir = new CenDireccionesAdm (user);
			admTipoDir = new CenDireccionTipoDireccionAdm (user);
			
			//obteniendo parametros
			String idPersona = request.getParameter ("idPersonaX");
			String colegio = request.getParameter ("nombreColegios");
			String numero = request.getParameter ("numeroColegiado");
			boolean residente = UtilidadesString.stringToBoolean
					(request.getParameter ("situacionResidente"));
			boolean comunitario = UtilidadesString.stringToBoolean
					(request.getParameter ("comunitario"));
			String estadoColegial = request.getParameter ("estadoColegial");
			String fechaEstado = request.getParameter ("fechaEstado");
			String observaciones = request.getParameter("observaciones");
			
			//A la fecha estado recuperada le añadimos la hora del sistema 
			//  porque cuando se recupera del calendario viene las 00:00:00
			String hora = UtilidadesString.formatoFecha (new Date (), "HH:mm:ss");
			fechaEstado = UtilidadesString.formatoFecha	(fechaEstado + " " + hora, 
					"dd/MM/yyyy HH:mm:ss", "yyyy/MM/dd HH:mm:ss");
			
			//obteniendo campos del cliente origen que no habia en el formulario
			Hashtable hashCliente = new Hashtable ();
			hashCliente.put (CenClienteBean.C_IDPERSONA, idPersona);
			hashCliente.put (CenClienteBean.C_IDINSTITUCION, idInstitucionOrigen);
			Vector beanCliente = admCliente.selectByPK (hashCliente);
			Integer idTratamiento = null;
			if (beanCliente != null)
				if (beanCliente.size () == 1)
					idTratamiento = ((CenClienteBean) beanCliente.get (0)).
							getIdTratamiento ();
			
			
			////////// REALIZANDO COLEGIACION //////////
			
			//iniciando la transaccion para modificaciones
			t = user.getTransactionPesada ();
			t.begin ();
			
			//comprobando que la persona no esta ya dada de alta 
			//  como colegiado en el colegio seleccionado
			String sComunitario = "0";
			if(comunitario ==true)
				sComunitario = "1";
			if (admCol.existeColegiado 
					(new Integer (colegio),numero,numero) != null)
				throw new SIGAException ("error.message.NumColegiadoRepetido");
			
			//borrando registro de no colegiado en colegio (si existe)
			Hashtable hashNoCol = new Hashtable ();
			hashNoCol.put (CenNoColegiadoBean.C_IDPERSONA, idPersona);
			hashNoCol.put (CenNoColegiadoBean.C_IDINSTITUCION, colegio);
			boolean habiaNoColegiado = admNoCol.delete (hashNoCol);
			
			CenClienteBean beanClienteDestino = new CenClienteBean ();
			if (! habiaNoColegiado) {
				//insertando un nuevo cliente si no existia ya
				beanClienteDestino.setIdTratamiento (idTratamiento);
				beanClienteDestino.setIdInstitucion (new Integer (colegio));
				beanClienteDestino.setIdPersona (new Long (idPersona));
				beanClienteDestino.setFechaAlta ("SYSDATE");
				beanClienteDestino.setIdLenguaje (ClsConstants.LENGUAJE_ESP);
				beanClienteDestino.setAbonosBanco (ClsConstants.TIPO_CARGO_BANCO);
				beanClienteDestino.setCargosBanco (ClsConstants.TIPO_CARGO_BANCO);
				beanClienteDestino.setPublicidad (ClsConstants.DB_FALSE);
				beanClienteDestino.setGuiaJudicial (ClsConstants.DB_FALSE);
				beanClienteDestino.setComisiones (ClsConstants.DB_FALSE);					
				beanClienteDestino.setCaracter (ClsConstants.TIPO_CARACTER_PUBLICO);
				beanClienteDestino.setLetrado (ClsConstants.DB_FALSE);
				
				if (! admCliente.insert (beanClienteDestino))
					throw new SIGAException (admCliente.getError ());
			}
			else {
				//asegurando que el cliente de colegiado se queda publico
				Vector listaClientes = admCliente.select (hashNoCol);
				Iterator iterListaClientes = listaClientes.iterator ();
				if (iterListaClientes.hasNext ()) {
					beanClienteDestino = (CenClienteBean) iterListaClientes.next ();
					beanClienteDestino.setCaracter (ClsConstants.TIPO_CARACTER_PUBLICO);
					if (! admCliente.update (beanClienteDestino))
						throw new SIGAException (admCliente.getError ());
				}
			}
			
			//creando el bean de colegiado para insertar
			CenColegiadoBean beanCol = new CenColegiadoBean ();
			
			//escribiendo el numero de colegiado
			if (comunitario) {
				beanCol.setNComunitario (numero);
				beanCol.setComunitario (ClsConstants.DB_TRUE);
			} else {
				beanCol.setNColegiado(numero);
				beanCol.setComunitario (ClsConstants.DB_FALSE);
			}
			
			//escribiendo si esta colegiado en otros colegios
			CenColegiadoBean beanColOtra = admCol.existeColegiadoOtraInstitucion(new Long (idPersona), new Integer (colegio));
			if (beanColOtra != null) { //existe cliente en otra institucion
				beanCol.setOtrosColegios (ClsConstants.DB_TRUE);
				
				//actualizando el valor en los otros colegios tambien
				admCol.actualizaColegiadoOtraInstitucion 
						(new Long (idPersona), new Integer (colegio));
			} else {
				beanCol.setOtrosColegios (ClsConstants.DB_FALSE);
			}
			
			//escribiendo otros datos del colegiado
			beanCol.setFechaIncorporacion (fechaEstado);
			beanCol.setFechaPresentacion (fechaEstado);
			beanCol.setIndTitulacion (ClsConstants.DB_FALSE);
			beanCol.setJubilacionCuota (ClsConstants.DB_FALSE);
			beanCol.setSituacionEjercicio (ClsConstants.DB_FALSE);
			beanCol.setSituacionResidente 
					(residente ? ClsConstants.DB_TRUE : ClsConstants.DB_FALSE);
			beanCol.setSituacionEmpresa (ClsConstants.DB_FALSE);
			beanCol.setIdPersona (new Long (idPersona));
			beanCol.setIdInstitucion (new Integer (colegio));
			
			//insertando en la tabla de colegiados y datos estado colegiales
			if (! admCol.insert(beanCol)) {
				throw new SIGAException (admCol.getError ());
			} else {
				//insertando los datoColegialesEstado
				CenDatosColegialesEstadoBean beanDCE = new CenDatosColegialesEstadoBean ();
				beanDCE.setIdPersona (beanCol.getIdPersona ());
				beanDCE.setIdInstitucion (beanCol.getIdInstitucion ());
				beanDCE.setIdEstado (new Integer (estadoColegial));
				beanDCE.setFechaEstado (fechaEstado);
				beanDCE.setObservaciones (observaciones);
				if (! admDCE.insert (beanDCE))
					throw new ClsExceptions (admDCE.getError ());
			}
			
			//lanzando el proceso de revision de suscripciones del letrado 
			String resultado[] = EjecucionPLs.ejecutarPL_RevisionSuscripcionesLetrado
					(colegio, idPersona, "", "" + this.getUserName (request));
			if ((resultado == null) || (! resultado[0].equals ("0")))
				throw new ClsExceptions ("Error al ejecutar el PL " +
						"PKG_SERVICIOS_AUTOMATICOS.PROCESO_REVISION_LETRADO");
			
			//insertando en la cola de modificacion de datos de letrado 
			CenColaCambioLetradoAdm colaAdm = new CenColaCambioLetradoAdm (user);
			if (! colaAdm.insertarCambioEnCola 
					(ClsConstants.COLA_CAMBIO_LETRADO_APROBACION_COLEGIACION, 
					new Integer (colegio), new Long (idPersona), null))
				throw new SIGAException (colaAdm.getError ());
			
////////////////////////////////   INSERTANDO LA DIRECCION ///////////////////////////////////////////////
			Hashtable hashDir = new Hashtable ();
			hashDir.put (CenDireccionesBean.C_IDPERSONA, idPersona);
			hashDir.put (CenDireccionesBean.C_IDINSTITUCION, idInstitucionOrigen);
			Vector listaBeanDireccion = admDir.select (hashDir);
			if ((listaBeanDireccion != null) && (listaBeanDireccion.size () > 0)) {
				Long idDireccionDestino, idDireccionOrigen;
				CenDireccionesBean beanDir;
				Direccion direccion;
				for (int i=0; i < listaBeanDireccion.size (); i++) {
					
					//obteniendo direccion de Consejo
					beanDir = (CenDireccionesBean) listaBeanDireccion.get (i);
					idDireccionOrigen = beanDir.getIdDireccion ();
					direccion = new Direccion();
					String tiposDir ="";
					
					//buscando los tipos de la direccion del consejo
					Hashtable hashDirTipoDir = new Hashtable ();
					hashDirTipoDir.put (CenDireccionTipoDireccionBean.C_IDPERSONA, beanDir.getIdPersona ());
					hashDirTipoDir.put (CenDireccionTipoDireccionBean.C_IDINSTITUCION, idInstitucionOrigen);
					hashDirTipoDir.put (CenDireccionTipoDireccionBean.C_IDDIRECCION, idDireccionOrigen);
					Vector beanDireccionTipoDireccion = admTipoDir.select (hashDirTipoDir);
					if ((beanDireccionTipoDireccion != null) && (beanDireccionTipoDireccion.size () >0)) {
						CenDireccionTipoDireccionBean beanTipoDir;
						for (int j=0; j < beanDireccionTipoDireccion.size (); j++){
							//insertando tipo direccion del consejo en colegio
							beanTipoDir = (CenDireccionTipoDireccionBean) beanDireccionTipoDireccion.get (j); 
							tiposDir = beanTipoDir.getIdTipoDireccion() + ",";
						} //for
					} //if
					
					direccion.insertar(beanDir, tiposDir, "", null, user);
					
					//enlazando la direccion de Consejo con la del Colegio
					beanDir = (CenDireccionesBean) listaBeanDireccion.get (i);
					beanDir.setIdInstitucion (new Integer (idInstitucionOrigen));
					beanDir.setIdDireccionAlta (beanDir.getIdDireccion());
					beanDir.setIdDireccion (idDireccionOrigen);
					beanDir.setIdInstitucionAlta (new Integer (colegio));
					
					direccion.actualizar(beanDir, tiposDir, "", null, user);
					
				} //for
			} //if
			
			if(estadoColegial.equalsIgnoreCase("20")){
				try{
					RetencionesIRPFAction irpf = new  RetencionesIRPFAction();
					irpf.insertarNuevo(idPersona, "SYSDATE",request);
				} catch (Exception e) {
					t.rollback();
					throw e;
				}
			}

			//cerrando los cambios en BD
			t.commit();
			
			//devolviendo la peticion
			request.setAttribute ("mensaje", "botonAccion.message.error2");
			request.setAttribute ("idPersona", idPersona);
			request.setAttribute ("idInstitucion", colegio);
			
			//realizando la devolucion
			forward = this.exitoModal ("botonAccion.message.error2", request);
		
		} catch (SIGAException es) {
			throwExcp (es.getLiteral(), new String[] {"modulo.censo"}, es, t);
		}catch (Exception e) {
			throwExcp ("messages.general.error", new String[] {"modulo.censo"}, e, t);
		}
		return forward;
	} //insertarColegiado ()
	
}
