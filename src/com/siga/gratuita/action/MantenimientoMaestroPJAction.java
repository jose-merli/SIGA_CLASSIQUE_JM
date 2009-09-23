package com.siga.gratuita.action;

import javax.servlet.http.*;
import javax.transaction.UserTransaction;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.*;
import com.atos.utils.*;

import java.util.Hashtable;
import org.apache.struts.action.*;

import java.util.*;

import com.siga.beans.*;

/**
 * Gestiona el mantenimiento de los maestros de partidos judiciales.
 * 
 * @author Luis Miguel Sánchez PIÑA
 * @since 31/08/2006 
 * @version 1.0
 */
public class MantenimientoMaestroPJAction extends MasterAction
{
	/** 
	 *  Método que atiende a las peticiones. Según el valor del parámetro modo del formulario ejecuta distintas acciones.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		String mapDestino = "exception";
		MasterForm miForm = null;

		try
		{ 
			miForm = (MasterForm) formulario;

			if (miForm != null)
			{
				String accion = miForm.getModo();

				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir"))
				{
					mapDestino = abrir(mapping, miForm, request, response);
				}

				else if (accion.equalsIgnoreCase("eliminarPoblacion"))
				{
					// buscarPersona
					mapDestino = eliminarPoblacion(mapping, miForm, request, response);
				}

				else if (accion.equalsIgnoreCase("anadirPoblacion"))
				{
					// enviarCliente
					mapDestino = anadirPoblacion(mapping, miForm, request, response);
				}

				else if (accion.equalsIgnoreCase("mostrarListaPob"))
				{
					// enviarCliente
					mapDestino = mostrarListaPoblacion(mapping, miForm, request, response);
				}

				else
				{
					return super.executeInternal(mapping,formulario,request,response);
				}
			}
			
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)
			{ 
			    throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			
			return mapping.findForward(mapDestino);
		}
		
		catch (SIGAException es)
		{
			throw es;
		}
		
		catch (Exception e)
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
	}

	/**
	 * EN ESTE METODO SIMPLEMENTE VAMOS A ABRIR LA VENTANA MODAL PARA LA SELECCIÓN DE PROVINCIAS Y POBLACIONES ASOCIADAS A UN PARTIDO JUDICIAL
	 */
	private String mostrarListaPoblacion(ActionMapping mapping, MasterForm miForm, HttpServletRequest request, HttpServletResponse response)
	{
		return "consultaMaestroPartidoPoblacion";
	}

	/**
	 * @param mapping
	 * @param miForm
	 * @param request
	 * @param response
	 * @return
	 */
	private String anadirPoblacion(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		UserTransaction tx = null;

		try
		{
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			

			tx=user.getTransaction();
			MantenimientoMaestroPJForm miForm = (MantenimientoMaestroPJForm) formulario;			
			ArrayList listaPoblaciones = new ArrayList();
			String poblaciones[]=miForm.getComboPoblaciones();
			// insertar poblaciones
			StringTokenizer st = new StringTokenizer(poblaciones[0],",");

			while (st.hasMoreElements())
			{
				Object cc = st.nextElement();
				
				if (cc!=null)
				{
					listaPoblaciones.add((String)cc);
				}
			}

			if(listaPoblaciones.size() == 0)
			{
				return exito("gratuita.modalPJ.literal.introducirPoblacion",request);
			}

			tx.begin();

			CenPoblacionesAdm admPob = new CenPoblacionesAdm(this.getUserBean(request));

			if (poblaciones!=null)
			{
				for (int i=0;i<listaPoblaciones.size();i++)
				{
					Hashtable miHash = new Hashtable();
					miHash.put(CenPoblacionesBean.C_IDPOBLACION,(String)listaPoblaciones.get(i));
					Vector v = admPob.selectByPK(miHash);

					if (v!=null && v.size()>0)
					{
						CenPoblacionesBean beanPob = (CenPoblacionesBean) v.get(0);
						beanPob.setIdPartido(new Integer(miForm.getIdPartido()));

						if (!admPob.updateDirect(beanPob,admPob.getClavesBean(),admPob.getCamposBean()))
						{
							throw new ClsExceptions(admPob.getError());
						}
					}
				}
			}

			//Borramos el String accion 
			request.getSession().removeAttribute("accion");

			tx.commit();
			
		}

		catch (Exception e)
		{
			 throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,tx);
		}

		return exitoModal("messages.updated.success",request);
	}

	/**
	 * @param mapping
	 * @param miForm
	 * @param request
	 * @param response
	 * @return
	 */
	private String eliminarPoblacion(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		Vector ocultos = new Vector();
		String forward="";
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		UserTransaction tx = null;
		Hashtable miHash=new Hashtable();
		Vector poblacion=new Vector();

		try
		{
			MantenimientoMaestroPJForm miForm = (MantenimientoMaestroPJForm) formulario;			
			CenPoblacionesAdm cenPoblacionAdm = new CenPoblacionesAdm(this.getUserBean(request)); 
			CenPoblacionesBean cenPoblacionBean=new CenPoblacionesBean();

			tx=usr.getTransaction();

			String idPoblacion=miForm.getIdPoblacion();
			//BUSCAMOS LOS DATOS DE POBLACION ASOCIADOS AL PATIDO JUDICIAL
			//BUSCAR EL BEAN DE PARTIDO JUDICIAL CON EL IDPARTIDO
			Hashtable consulta=new Hashtable();

			consulta.put(CenPoblacionesBean.C_IDPOBLACION,idPoblacion);
			poblacion=cenPoblacionAdm.selectByPK(consulta);
			CenPoblacionesBean result=null;

			if (!poblacion.isEmpty())
			{
				result=(CenPoblacionesBean)poblacion.elementAt(0);
			}

			//DELETE
			tx.begin();

			result.setIdPartido(null);

			if (!cenPoblacionAdm.updateDirect(result,cenPoblacionAdm.getClavesBean(),cenPoblacionAdm.getCamposBean()))
			{
				throw new ClsExceptions(cenPoblacionAdm.getError());
			}

			forward = exitoRefresco("messages.deleted.success",request);					            	
		    request.setAttribute("modo","abrir");

		    tx.commit();
		}

		catch (Exception e)
		{
				throwExcp("messages.deleted.error",e,tx);
		}

		return forward;
	}

	/**
	 * Es el metodo inicial que se ejecuta al entrar a la pantalla de busqueda.
	 * Limpia la sesion de los datos del formulario.
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		try
		{
			//Borramos las tablas hash: DATOSFORMULARIO, DATABACKUP, DATABACKUP_PJ
			request.getSession().removeAttribute("DATOSFORMULARIO");
			request.getSession().removeAttribute("DATABACKUP");
			request.getSession().removeAttribute("DATABACKUP_PJ");
			
			//Borramos el String accion 
			request.getSession().removeAttribute("accion");
		}

		catch (Exception e)
		{
			throwExcp("messages.select.error",e,null);
		}

		return "inicio";
	}	
	
	/**
	 * Hace una busqueda en base de datos de las poblaciones de un partido judicial de una provincia.
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		MantenimientoMaestroPJForm miForm = (MantenimientoMaestroPJForm) formulario;
		CenPartidoJudicialAdm admPartidoBean = new CenPartidoJudicialAdm(this.getUserBean(request));

		Vector v = new Vector();
		String forward = "error";
		String idpartido = "";
		Hashtable backupPartido = new Hashtable();

		try
		{		
			//Recupero el idpartido del formulario si estoy en editar en la pantalla de mantenimiento y vengo de la pantalla de buscar.
			//Sino, estoy en editar viniendo de nuevo->insertar un nuevo partido judicial.
			//En este caso recupero el idpartido de la sesion. Lo almacene en sesion al insertar
			if (miForm.getIdPartido()!=null && !miForm.getIdPartido().equals(""))
			{
				idpartido = miForm.getIdPartido();
			}

			else
			{
				backupPartido = (Hashtable)request.getSession().getAttribute("DATABACKUP_PJ");
				idpartido =  (String)backupPartido.get("IDPARTIDO");
			}

			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");			
			v = admPartidoBean.selectGenerico(admPartidoBean.selectBusquedaPoblacionesPJ(idpartido));			
				
			request.setAttribute("resultado",v);
			request.setAttribute("idPartido",miForm.getIdPartido());
			request.setAttribute("idProvincia",miForm.getIdProvincia());

			// En la sessión se almacena la acción para abrir el jsp que opera sobre el registro en modo edición.			
			request.setAttribute("modo",formulario.getModo());
			request.setAttribute("cambiar",formulario.getModo());

			if(request.getParameter("accion") != null && request.getParameter("accion").equalsIgnoreCase("ver"))
			{
				request.setAttribute("accion","ver");
			}
		}

		catch (Exception e)
		{
			throwExcp("messages.select.error",e,null);
		}			

		forward =  "listar2MaestroPJ";

		return forward;			
	}

	/**
	 * Si vamos a editar/ver consulta en Base de Datos los partidos judiciales de una Provincia y almacena el resultado en el request.
	 * Sino vamos a la modal.
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{		
		MantenimientoMaestroPJForm miForm = (MantenimientoMaestroPJForm) formulario;
		CenPartidoJudicialAdm admPartidoBean = new CenPartidoJudicialAdm(this.getUserBean(request));
		CenPoblacionesAdm admPoblacionBean = new CenPoblacionesAdm(this.getUserBean(request));
		Vector v = new Vector ();
		String cambio = "";		
		Vector ocultos = new Vector();
		Vector visibles = new Vector();
		String forward = "error";
		Hashtable backupPartido = new Hashtable();
		Hashtable backupPoblacion = new Hashtable();
		Vector v_poblacion = new Vector();
		Vector v_partido = new Vector();
		UsrBean usr;
		
		try
		{
			request.setAttribute("modo",miForm.getModo());				
			
			//PANTALLA MODAL
			if (miForm.getCambiar()!=null && miForm.getCambiar().equals("modal"))
			{
				ocultos = miForm.getDatosTablaOcultos(0);
				visibles = miForm.getDatosTablaVisibles(0);

				request.setAttribute("partidoJudicial",visibles.get(0));
				request.setAttribute("poblacion",visibles.get(1));	
				request.setAttribute("idPoblacion",ocultos.get(1));
				request.setAttribute("idProvincia",ocultos.get(2));
				request.setAttribute("idPartido",ocultos.get(3));
				request.setAttribute("usuMod",miForm.getUsuMod());

				//Consultamos la fecha y usuario de ese id
				v_poblacion = admPartidoBean.selectGenerico(admPoblacionBean.getFechaYUsu((String)ocultos.get(1)));

				//Almacenamos en sesion el registro del partido judicial
				backupPoblacion.put(CenPoblacionesBean.C_IDPOBLACION,ocultos.get(1));
				backupPoblacion.put(CenPoblacionesBean.C_IDPROVINCIA,ocultos.get(2));
				backupPoblacion.put(CenPoblacionesBean.C_IDPARTIDO,ocultos.get(3));
				backupPoblacion.put(CenPoblacionesBean.C_NOMBRE,visibles.get(1));
				backupPoblacion.put(CenPoblacionesBean.C_FECHAMODIFICACION,(String)((Hashtable)v_poblacion.elementAt(0)).get(CenPoblacionesBean.C_FECHAMODIFICACION));
				backupPoblacion.put(CenPoblacionesBean.C_USUMODIFICACION,(String)((Hashtable)v_poblacion.elementAt(0)).get(CenPoblacionesBean.C_USUMODIFICACION));
				request.getSession().setAttribute("DATABACKUP",backupPoblacion);

				// En la sessión se almacena la acción para abrir el jsp que opera sobre el registro en modo edición.
				request.getSession().setAttribute("accion",miForm.getModo());
				
				request.setAttribute("modo","Editar");
				request.setAttribute("cambiar","modal");
			
				forward = "modalMaestroPJ";
			}

			//CONSULTAR/EDITAR
			else
			{
				String idPartido = request.getParameter("idPartido");

				if (idPartido==null)
				{
					ocultos = miForm.getDatosTablaOcultos(0);
					visibles = miForm.getDatosTablaVisibles(0);
										
					idPartido=(String)ocultos.get(0);
				}

				//Consultamos la fecha y usuario de ese id			
				v_partido = admPartidoBean.selectGenerico(admPartidoBean.getFechaYUsu(idPartido));

				// BUSCAR EL BEAN DE PARTIDO JUDICIAL CON EL IDPARTIDO
				Hashtable consulta=new Hashtable();
				consulta.put(CenPartidoJudicialBean.C_IDPARTIDO,idPartido);
				Vector partido=admPartidoBean.select(consulta);
				CenPartidoJudicialBean result=null;

				if (!partido.isEmpty())
				{
					result=(CenPartidoJudicialBean)partido.elementAt(0);
				}

				// COGEMOS LOS VALORES DEL BEAN Y LOS PASAMOS A REQUEST 
				request.setAttribute("partidoJudicial",result.getNombre());
				request.setAttribute("idPartido",result.getIdPartido().toString());
				request.setAttribute("idInstitucionPropietario",result.getIdInstitucionPropietario());
				request.setAttribute("usuMod",result.getUsuMod().toString());
				request.setAttribute("idInstitucionPropietario",result.getIdInstitucionPropietario());
				
				//Almacenamos en sesion el registro de la poblacion			
				backupPartido.put(CenPartidoJudicialBean.C_IDPARTIDO,idPartido);
				backupPartido.put(CenPartidoJudicialBean.C_NOMBRE,result.getNombre());
				backupPartido.put(CenPartidoJudicialBean.C_IDINSTITUCIONPROPIETARIO,result.getIdInstitucionPropietario());
				backupPartido.put(CenPartidoJudicialBean.C_FECHAMODIFICACION,result.getFechaMod().toString());
				backupPartido.put(CenPartidoJudicialBean.C_USUMODIFICACION,result.getUsuMod().toString());

				request.getSession().setAttribute("DATABACKUP_PJ",backupPartido);

				// En la sessión se almacena la acción para abrir el jsp que opera sobre el registro en modo edición.
				request.getSession().setAttribute("accion",miForm.getModo());

				request.setAttribute("modo","Editar");
				request.setAttribute("cambiar","Editar");

				forward =  "consultarMaestroPJ";
			}
		}

		catch (Exception e)
		{
			throwExcp("messages.select.error",e,null);
		}			

		return forward;
	}

	/**
	 * Ejecuta el metodo editar().
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		String forward=editar(mapping,formulario,request,response);
		request.setAttribute("modo","ver");
		request.setAttribute("accion","ver");
		return forward;
	}

	/**
	 * Vamos a la pantalla de mantenimiento al pulsar en nuevo.
	 * Añade al request el nombre del partido judicial.
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		MantenimientoMaestroPJForm miForm = (MantenimientoMaestroPJForm) formulario;
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

		try
		{
			// En la sessión se almacena la acción para abrir el jsp que opera sobre el registro en modo edición.
			request.getSession().setAttribute("accion",miForm.getModo());

			ArrayList codigoInstitucion = new ArrayList();
			//codigoInstitucion.add("2000");
			codigoInstitucion.add(miForm.getIdInstitucionPropietario());
			
			request.setAttribute("arrayInstituciones",codigoInstitucion);
			request.setAttribute("modo","nuevo");
			request.setAttribute("cambiar","nuevo");
			request.setAttribute("partidoJudicial",miForm.getPartidoJudicial());
		}

		catch (Exception e)
		{
			throwExcp("messages.select.error",e,null);
		}		

		return "consultarMaestroPJ";
	}

	/**
	 * Añade a una tabla hash los datos necesarios para hacer una insercion en base de datos de un nuevo <br> 
	 * partido judicial. 
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		MantenimientoMaestroPJForm miForm = (MantenimientoMaestroPJForm) formulario;
		CenPartidoJudicialAdm admPartido = new CenPartidoJudicialAdm(this.getUserBean(request));
		//CenInfluenciaAdm admInfluencia = new CenInfluenciaAdm(this.getUserBean(request));
		String forward = "error";
		Hashtable miHash = new Hashtable();
		Hashtable backupHash = new Hashtable();
		//Hashtable miHashInfluen = new Hashtable();
		//Hashtable backupHashInfluen = new Hashtable();
		UserTransaction tx = null;
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

		try
		{			
			tx=usr.getTransaction();

			request.setAttribute("modo",miForm.getModo());

			miHash.put(CenPartidoJudicialBean.C_NOMBRE,miForm.getPartidoJudicial());
			miHash.put(CenPartidoJudicialBean.C_IDINSTITUCIONPROPIETARIO, miForm.getIdInstitucionPropietario());
			miHash.put(CenPartidoJudicialBean.C_FECHAMODIFICACION,miForm.getFechaMod());
			miHash.put(CenPartidoJudicialBean.C_USUMODIFICACION,miForm.getUsuMod());

            //SELECT
			String idpartido = (String)admPartido.getIdPartido(admPartido.selectIdPartido());
			//Si no hay elementos le asigno el primero: 1
			if (idpartido.equals(""))
			{
				idpartido = "1";
			}
			
			miHash.put(CenPartidoJudicialBean.C_IDPARTIDO,idpartido);

			//INSERT (INICIO TRANSACCION)
            tx.begin();

            // insertamos los partidos en cen_partidosjudiciales
            if (!admPartido.insert(miHash))
            {
            	throw new ClsExceptions(admPartido.getError());
            }

        	tx.commit();

        	request.setAttribute("modo","editar");
        	request.setAttribute("idPartido",idpartido);

	        //Consultamos la fecha y usuario de registro a partir de sus id			
			Vector datosPartido = admPartido.selectGenerico(admPartido.getDatosPartido(idpartido));

			//CONSULTA DE INSTITUCIONES
			//String consulta="SELECT IDINSTITUCION FROM CEN_INFLUENCIA WHERE IDPARTIDO ="+idpartido;

			//Almacenamos en sesion el partido y las instituciones asociadas		
			backupHash.put(CenPartidoJudicialBean.C_IDPARTIDO,(String)((Hashtable)datosPartido.elementAt(0)).get(CenPartidoJudicialBean.C_IDPARTIDO));
			backupHash.put(CenPartidoJudicialBean.C_NOMBRE,(String)((Hashtable)datosPartido.elementAt(0)).get(CenPartidoJudicialBean.C_NOMBRE));
			backupHash.put(CenPartidoJudicialBean.C_IDINSTITUCIONPROPIETARIO,(String)((Hashtable)datosPartido.elementAt(0)).get(CenPartidoJudicialBean.C_IDINSTITUCIONPROPIETARIO));
			backupHash.put(CenPartidoJudicialBean.C_USUMODIFICACION,(String)((Hashtable)datosPartido.elementAt(0)).get(CenPartidoJudicialBean.C_USUMODIFICACION));
			backupHash.put(CenPartidoJudicialBean.C_FECHAMODIFICACION,(String)((Hashtable)datosPartido.elementAt(0)).get(CenPartidoJudicialBean.C_FECHAMODIFICACION));
			//backupHash.put(CenInfluenciaBean.C_IDINSTITUCION,admInfluencia.selectGenerico(consulta));
			request.getSession().removeAttribute("DATABACKUP_PJ");
			request.getSession().setAttribute("DATABACKUP_PJ",backupHash);

			request.setAttribute("mensaje","messages.inserted.success");
			forward = "exitoMio";
		}

		catch (Exception e)
		{
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}

		return forward;
	}

	/**
	 * Añade a una tabla hash los datos necesarios para hacer un update en base de datos de un partido judicial <br> 
	 * asignado a una poblacion en el caso de estar en la modal. 
	 * En el caso de estar en mantenimeinto modifica los datos del partido judicial.
	 * El parametro para diferenciar estos 2 casos es "cambiar". Si vale "modal" estamos en la modal. 
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		MantenimientoMaestroPJForm miForm = (MantenimientoMaestroPJForm) formulario;
		CenPoblacionesAdm admPoblacionesBean = new CenPoblacionesAdm(this.getUserBean(request));
		CenPartidoJudicialAdm admPartidoBean = new CenPartidoJudicialAdm(this.getUserBean(request));
		//CenInfluenciaAdm admInfluencias =new CenInfluenciaAdm(this.getUserBean(request));
		Hashtable backupPartido = new Hashtable();
		Hashtable miHash = new Hashtable();
		Hashtable backupHash = new Hashtable();
		String forward = "error";
		UserTransaction tx = null;
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		Vector ocultos = new Vector();
		Vector visibles = new Vector();
		String idPartidoSeleccionado, idpartido="";

		try
		{
			tx=usr.getTransaction();

			//Caso de guardar y cerrar desde la ventana modal
			if (miForm.getCambiar()!=null && miForm.getCambiar().equals("modal"))
			{		
				ocultos = miForm.getDatosTablaOcultos(0);
				visibles = miForm.getDatosTablaVisibles(0);				
				backupHash = (Hashtable)request.getSession().getAttribute("DATABACKUP");
				
				miHash.put(CenPoblacionesBean.C_IDPOBLACION,miForm.getIdPoblacion());			
				miHash.put(CenPoblacionesBean.C_IDPROVINCIA,miForm.getIdProvincia());			
				miHash.put(CenPoblacionesBean.C_NOMBRE,miForm.getPoblacion());			
				idPartidoSeleccionado = request.getParameter("idPartidoSeleccionado")==null?"":request.getParameter("idPartidoSeleccionado");				
				miHash.put(CenPoblacionesBean.C_IDPARTIDO,idPartidoSeleccionado);
				miHash.put(CenPoblacionesBean.C_USUMODIFICACION,miForm.getUsuMod());
				miHash.put(CenPoblacionesBean.C_FECHAMODIFICACION,"sysdate");

				//UPDATE (INICIO TRANSACCION)
	            tx.begin();                 

	            if (admPoblacionesBean.update(miHash,backupHash))
	            {
					forward = exitoModal("messages.updated.success",request);
	            }

				else
				{
					forward = exitoModalSinRefresco("messages.updated.error",request);
				}

	            tx.commit();

	            request.setAttribute("modo","modificar");
		        request.setAttribute("cambiar","modal");
			}

			// Caso de guardar en la pantalla de mantenimiento de partidos judiciales
			else
			{
				backupPartido = (Hashtable)request.getSession().getAttribute("DATABACKUP_PJ");
				//Recupero el idpartido del formulario si estoy en editar en la pantalla de mantenimiento y vengo de la pantalla de buscar.
				//Sino, estoy en editar viniendo de nuevo->insertar un nuevo partido judicial.
				//En este caso recupero el idpartido de la sesion. Lo almacene en sesion al insertar
				if (miForm.getIdPartido()!=null && !miForm.getIdPartido().equals(""))
				{
					idpartido = miForm.getIdPartido();
				}

				else
				{
					idpartido = (String)backupPartido.get(CenPoblacionesBean.C_IDPARTIDO);
				}

				miHash.put(CenPartidoJudicialBean.C_IDPARTIDO,idpartido);
				miHash.put(CenPartidoJudicialBean.C_NOMBRE,miForm.getPartidoJudicial());
				miHash.put(CenPartidoJudicialBean.C_IDINSTITUCIONPROPIETARIO,miForm.getIdInstitucionPropietario());
				miHash.put(CenPoblacionesBean.C_FECHAMODIFICACION,"sysdate");
				miHash.put(CenPoblacionesBean.C_USUMODIFICACION,usr.getUserName());

				//INICIO TRANSACCION
	            tx.begin();                 
				
	            //UPDATE DE PARTIDO
	            if (admPartidoBean.update(miHash,backupPartido))
	            {
					forward = exitoRefresco("messages.updated.success",request);
	            }

	            else
				{
					forward = exito("messages.updated.error",request);
				}

	            tx.commit();

		        //Consultamos la fecha y usuario de registro a partir de sus id			
				Vector datosPartido = admPartidoBean.selectGenerico(admPartidoBean.getDatosPartido(idpartido));

				//Almacenamos en sesión el registro de la lista de guardias
				backupHash.put(CenPoblacionesBean.C_IDPARTIDO,(String)((Hashtable)datosPartido.elementAt(0)).get(CenPoblacionesBean.C_IDPARTIDO));
				backupHash.put(CenPoblacionesBean.C_NOMBRE,(String)((Hashtable)datosPartido.elementAt(0)).get(CenPoblacionesBean.C_NOMBRE));
				backupHash.put(CenPoblacionesBean.C_USUMODIFICACION,(String)((Hashtable)datosPartido.elementAt(0)).get(CenPoblacionesBean.C_USUMODIFICACION));
				backupHash.put(CenPoblacionesBean.C_FECHAMODIFICACION,(String)((Hashtable)datosPartido.elementAt(0)).get(CenPoblacionesBean.C_FECHAMODIFICACION));
				
				request.getSession().removeAttribute("DATABACKUP_PJ");
				request.getSession().setAttribute("DATABACKUP_PJ",backupHash);
	            
	            request.setAttribute("cambiar",miForm.getModo());	            
	            request.setAttribute("modo","editar");
	            request.setAttribute("hiddenFrame", "1");

	            forward = exitoRefresco("messages.updated.success",request);		
			}
		}

		catch (Exception e)
		{
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}

		return forward;
	}		


	/**
	 * Añade a una tabla hash el IDPARTIDO para hacer un delete en base de datos de un partido judicial.
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{		
		String forward = "error";
		Hashtable miHash = new Hashtable();
		CenPartidoJudicialAdm admBean =  new CenPartidoJudicialAdm(this.getUserBean(request));
		MantenimientoMaestroPJForm miForm = (MantenimientoMaestroPJForm) formulario;
		Vector ocultos = new Vector();
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		UserTransaction tx = null;

		try
		{
			CenPoblacionesAdm cenPoblacionAdm = new CenPoblacionesAdm(this.getUserBean(request)); 
			CenPoblacionesBean cenPoblacionBean=new CenPoblacionesBean();
			CenPartidoJudicialAdm cenPJAdm=new CenPartidoJudicialAdm(this.getUserBean(request));
			CenPartidoJudicialBean cenPJBean=new CenPartidoJudicialBean();
			CenInfluenciaAdm cenInfluenciaAdm=new CenInfluenciaAdm(this.getUserBean(request));
			CenInfluenciaBean cenInfluenciaBean=new CenInfluenciaBean();
			tx=usr.getTransaction();
			request.setAttribute("modo",miForm.getModo());

			tx.begin();

			ocultos = miForm.getDatosTablaOcultos(0);

			// HACEMOS EL UPDATE DE LA TABLA CEN_POBLACIONES CON IDPARTIDO = NULL PARA CADA REGISTRO OBTENIDO
			String where="WHERE "+CenPoblacionesBean.C_IDPARTIDO +"="+ocultos.get(0);
			Vector poblacionesTabla=cenPoblacionAdm.select(where);

			for(int i=0;i<poblacionesTabla.size();i++)
			{
				CenPoblacionesBean bean=(CenPoblacionesBean)poblacionesTabla.elementAt(i);
				bean.setIdPartido(null);

				if (!cenPoblacionAdm.updateDirect(bean,cenPoblacionAdm.getClavesBean(),cenPoblacionAdm.getCamposBean()))
				{
					throw new ClsExceptions(cenPoblacionAdm.getError());
				}
			}

			//BORRAMOS EL REGISTRO DEL PARTIDO JUDICIAL
			Hashtable borraPJ=new Hashtable();
			borraPJ.put(CenPartidoJudicialBean.C_IDPARTIDO,ocultos.get(0));

			if(!cenPJAdm.delete(borraPJ))
			{
				throw new ClsExceptions(cenPJAdm.getError());
			}

			//BORRAMOS DE LA TABLA INFLUENCIA LAS ENTRADAS CORRESPONDIENTES AL PATIDO SELECCIONADO
			String where2="WHERE "+CenInfluenciaBean.C_IDPARTIDO +"="+ocultos.get(0);
            Vector influenciasTabla=cenInfluenciaAdm.select(where2);
            Hashtable delete=new Hashtable();
			delete.put(CenInfluenciaBean.C_IDPARTIDO,ocultos.get(0));

			for(int i=0;i<influenciasTabla.size();i++)
			{
				delete.put(CenInfluenciaBean.C_IDINSTITUCION,((Hashtable)influenciasTabla.elementAt(i)).get(CenInfluenciaBean.C_IDINSTITUCION));

				if(!cenInfluenciaAdm.delete(delete))
				{
					throw new ClsExceptions(cenInfluenciaAdm.getError());
				}

				delete.remove(CenInfluenciaBean.C_IDINSTITUCION);
			}

			forward = exitoRefresco("messages.deleted.success",request);
		    request.setAttribute("modo","abrir");

		    tx.commit();
		}

		catch (Exception e)
		{
				throwExcp("messages.deleted.error",e,null);
		}

		return forward;
	}


	/**
	 * Consulta en base de datos todos los partidos judiciales.
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		MantenimientoMaestroPJForm miForm = (MantenimientoMaestroPJForm)formulario;			
		CenPartidoJudicialAdm admPartidoBean = new CenPartidoJudicialAdm(this.getUserBean(request));
		Vector v = new Vector();
		Hashtable miHash = new Hashtable();
		UsrBean usr;
		String idInstitucion;
		Hashtable hashFormulario = new Hashtable();

		try
		{
			//Borramos las tablas hash: DATOSFORMULARIO, DATABACKUP, DATABACKUP_PJ
			request.getSession().removeAttribute("DATOSFORMULARIO");
			request.getSession().removeAttribute("DATABACKUP");
			request.getSession().removeAttribute("DATABACKUP_PJ");

			usr = (UsrBean)request.getSession().getAttribute("USRBEAN");			

			idInstitucion = usr.getLocation();
			miHash.put("PARTIDOJUDICIAL",miForm.getPartidoJudicial());

			hashFormulario.put("PARTIDOJUDICIAL",miForm.getPartidoJudicial());
			hashFormulario.put("INICIARBUSQUEDA","SI");			

			request.getSession().setAttribute("DATOSFORMULARIO",hashFormulario);

			//Siempre será CGAE, así es que siempre se sacan todos los partidos judiciales.
			v = admPartidoBean.selectGenerico(admPartidoBean.selectBusqueda(miHash));

			request.setAttribute("resultado",v);
			request.setAttribute("modo",miForm.getModo());
			request.setAttribute("cambiar",miForm.getModo());
		}

		catch (Exception e)
		{
			throwExcp("messages.select.error",e,null);
		}

		return "listarMaestroPJ";
	}

	/**
	 * Ejecuta el jsp inicial sin borrar datos del formulario de la sesion pero si de los datos de backup.
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
		//Borramos las tablas hash: DATABACKUP, DATABACKUP_PJ
		request.getSession().removeAttribute("DATABACKUP");
		request.getSession().removeAttribute("DATABACKUP_PJ");
		//Borramos el String accion
		request.getSession().removeAttribute("accion");
		} catch (Exception e) {
			throwExcp("messages.select.error",e,null);
		}

		return "inicio";	
	}
}