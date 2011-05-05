package com.siga.gratuita;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.transaction.SystemException;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.crystaldecisions.sdk.occa.report.definition.DateFieldFormat;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenBajasTemporalesAdm;
import com.siga.beans.CenDireccionTipoDireccionAdm;
import com.siga.beans.CenDireccionTipoDireccionBean;
import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.CenDireccionesBean;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsInscripcionGuardiaAdm;
import com.siga.beans.ScsInscripcionGuardiaBean;
import com.siga.beans.ScsInscripcionTurnoAdm;
import com.siga.beans.ScsInscripcionTurnoBean;
import com.siga.beans.ScsOrdenacionColasAdm;
import com.siga.beans.ScsOrdenacionColasBean;
import com.siga.beans.ScsRetencionesIRPFAdm;
import com.siga.beans.ScsSaltosCompensacionesAdm;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.InscripcionTGForm;
import com.siga.gratuita.util.calendarioSJCS.LetradoInscripcion;

public class InscripcionTurno
{
	////////////////////
	// ATRIBUTOS
	////////////////////
	private ScsInscripcionTurnoBean bean = null;
	
	private static SimpleDateFormat formatoFecha = 
		new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
	
	
	////////////////////
	// CONSTRUCTORES
	////////////////////
	/**
	 * Constructor
	 */
	private InscripcionTurno (Integer idInstitucion,
							  Integer idTurno,
							  Long idPersona,
							  String fechaSolicitudAlta)
	{
		this.bean = new ScsInscripcionTurnoBean();
		this.bean.setIdInstitucion(idInstitucion);
		this.bean.setIdTurno(idTurno);
		this.bean.setIdPersona(idPersona);
		this.bean.setFechaSolicitud(fechaSolicitudAlta);
	} //InscripcionTurno ()
	public InscripcionTurno (ScsInscripcionTurnoBean bean)
	{
		this.bean = bean;
	} //InscripcionTurno ()
	
	
	////////////////////
	// GETTERS Y SETTERS
	////////////////////
	public ScsInscripcionTurnoBean getBean() { return bean; }
	public void setBean(ScsInscripcionTurnoBean bean) { this.bean = bean; }
	
	
	////////////////////
	// METODOS DE CLASE
	////////////////////
	/**
	 * Devuelve un objeto que representa la inscripcion de un colegiado a un turno. 
	 * Este metodo comprueba si existe realmente la inscripcion. 
	 * Sin embargo, es posible evitar la comprobacion con el parametro comprobarQueExiste=false
	 */
	public static InscripcionTurno getInscripcionTurno (Integer idInstitucion,
														Integer idTurno,
														Long idPersona,
														String fechaSolicitudAlta,
														UsrBean usr,
														boolean comprobarQueExiste)
		throws ClsExceptions, ParseException
	{
		InscripcionTurno inscripcion = null;
		
		if (! comprobarQueExiste)
			return new InscripcionTurno (
					idInstitucion, idTurno, idPersona, fechaSolicitudAlta);
		else {
			//preparando datos
			Hashtable<String, Object> hash = new Hashtable<String, Object>();
			hash.put(ScsInscripcionTurnoBean.C_IDINSTITUCION, idInstitucion);
			hash.put(ScsInscripcionTurnoBean.C_IDPERSONA, idPersona);
			hash.put(ScsInscripcionTurnoBean.C_IDTURNO, idTurno);
			hash.put(ScsInscripcionTurnoBean.C_FECHASOLICITUD, formatoFecha.format(formatoFecha.parse(fechaSolicitudAlta)));
			
			//obteniendo datos
			ScsInscripcionTurnoAdm admInscripcion = new ScsInscripcionTurnoAdm(usr);
			Vector<ScsInscripcionTurnoBean> inscripciones = admInscripcion.select(hash);
			if (inscripciones == null)
				throw new ClsExceptions("Error: no se ha encontrado la inscripcion al turno");
			if (inscripciones.size() < 1)
				throw new ClsExceptions("Error: no se ha encontrado la inscripcion al turno");
			if (inscripciones.size() > 1)
				throw new ClsExceptions("Error: se ha encontrado mas de una inscripcion al turno");
			
			inscripcion = new InscripcionTurno ((ScsInscripcionTurnoBean) inscripciones.get(0));
		}
		
		return inscripcion;
	} //getInscripcionTurno ()

	
	
	
	
	public void solicitarAlta(InscripcionTGForm form,UsrBean usr) throws Exception{
//		UserTransaction tx = null;
		try {
//			tx = usr.getTransaction();
			
//			tx.begin();
			
			solicitarAlta(new Integer(form.getIdInstitucion()),new Integer(form.getIdTurno()),new Long(form.getIdPersona()),
					form.getObservacionesSolicitud(),form.getFechaValidacion(),form.getObservacionesValidacion(),
					form.getValidarInscripciones(),new Integer(form.getTipoGuardias()),
					form.getGuardiasSel(),form.getIdDireccion(),
					form.getFax1(),form.getFax2(),form.getMovil(),form.getTelefono1(),form.getTelefono2(),
					form.getIdRetencion(),usr);
			
//			tx.commit();
			
		} catch (Exception e) {
//			tx.rollback();
			throw e;
			
		}
		
				
	}
	/**
	 * Obtiene la cola de turno dada una fecha
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idGuardia
	 * @param fecha
	 * @param usr
	 * @return
	 * @throws ClsExceptions
	 */
	public static List<LetradoInscripcion> getColaTurno(Integer idInstitucion,
														 Integer idTurno,
														 String fecha,
														 boolean quitarSaltos,
														 UsrBean usr)
			throws ClsExceptions
	{
		ScsTurnoAdm turadm = new ScsTurnoAdm(usr);
		Hashtable hashTurno = new Hashtable();
		ArrayList<LetradoInscripcion> colaLetrados = new ArrayList<LetradoInscripcion>();
		ScsInscripcionTurnoAdm insadm = new ScsInscripcionTurnoAdm(usr);
		ScsSaltosCompensacionesAdm saladm = new ScsSaltosCompensacionesAdm(usr);
		CenBajasTemporalesAdm bajasAdm = new CenBajasTemporalesAdm(usr);
		
		
		// obteniendo la guardia
		hashTurno.put(ScsTurnoBean.C_IDINSTITUCION, idInstitucion.toString());
		hashTurno.put(ScsTurnoBean.C_IDTURNO, idTurno.toString());
		Vector vTurno= turadm.select(hashTurno);
		
		ScsTurnoBean beanTurno = (ScsTurnoBean) vTurno.get(0);
		
		Integer idOrdenacionColas = beanTurno.getIdOrdenacionColas();
		if(idOrdenacionColas==null)
			throw new ClsExceptions("messages.general.error");
		
		
		Long idPersonaUltimo = beanTurno.getIdPersonaUltimo();
		String fechaUltimo = beanTurno.getFechaSolicitudUltimo();
		ScsOrdenacionColasAdm ordenacionColasAdm = new ScsOrdenacionColasAdm(usr);
		// obteniendo ordenacion de la guardia
		String orden = ordenacionColasAdm.getOrderBy(idOrdenacionColas.toString(), usr);
		
		ScsInscripcionTurnoBean ultimoAnterior;
		
		// obteniendo ultimo apuntado de la guardia
		if (idPersonaUltimo == null)
			ultimoAnterior = null;
		else
			ultimoAnterior = new ScsInscripcionTurnoBean(idInstitucion, idTurno, idPersonaUltimo, fechaUltimo);
		
//		obteniendo lista de letrados (ordenada)
		Vector<ScsInscripcionTurnoBean> listaLetrados = insadm.getColaTurno(idInstitucion.toString(), idTurno.toString(), fecha, orden);
		if (listaLetrados == null || listaLetrados.size()==0)
			return colaLetrados;
		
		/* FORMA ANTIGUA DE RELLENAR LA COLA DE TURNOS */
		 
		/*ScsInscripcionTurnoBean inscripcionTurno = null;
		boolean foundUltimo = false;
		Vector<LetradoInscripcion> colaAuxiliar = new Vector<LetradoInscripcion>();
		LetradoInscripcion letradoTurno;
		if(idPersonaUltimo!=null){
			for (int i = 0; i < listaLetrados.size(); i++) {
				inscripcionTurno = (ScsInscripcionTurnoBean) listaLetrados.get(i);
				letradoTurno = new LetradoInscripcion(inscripcionTurno);
				if(foundUltimo){
					//El primero que se añade es el ultimo. Depues habra que moverlo
					colaLetrados.add(letradoTurno);
				}else{
					colaAuxiliar.add(letradoTurno);
				}
				if(!foundUltimo && inscripcionTurno.getIdPersona().compareTo(idPersonaUltimo)==0){
					foundUltimo = true;
					//indexUltimo = i;
				}
			}
			colaLetrados.addAll(colaAuxiliar);
			}else{
		
		   // el idpersona ultimo es nulo con el orden que traian
			for (int i = 0; i < listaLetrados.size(); i++) {
				inscripcionTurno = (ScsInscripcionTurnoBean) listaLetrados.get(i);
				letradoTurno = new LetradoInscripcion(inscripcionTurno);
				colaLetrados.add(letradoTurno);
			}
		}*/
		
		ScsInscripcionTurnoBean punteroInscripciones = null;
		boolean foundUltimo;
		LetradoInscripcion letradoTurno;
		if (ultimoAnterior == null) {
			// si no existe ultimo colegiado, se empieza la cola desde el
			// primero en la lista
			for (int i = 0; i < listaLetrados.size(); i++) {
				punteroInscripciones = (ScsInscripcionTurnoBean) listaLetrados.get(i);
				if (punteroInscripciones.getEstado().equals(ClsConstants.DB_TRUE))
					colaLetrados.add(new LetradoInscripcion(punteroInscripciones));
			}
		} else {
			// ordenando la cola en funcion del idPersonaUltimo guardado
			Vector<LetradoInscripcion> colaAuxiliar = new Vector<LetradoInscripcion>();
			foundUltimo = false;
			for (int i = 0; i < listaLetrados.size(); i++) {
				punteroInscripciones = listaLetrados.get(i);

				// insertando en la cola si la inscripcion esta activa
				if (punteroInscripciones.getEstado().equals(ClsConstants.DB_TRUE)) {
					// El primero que se anyade es el siguiente al ultimo
					if (foundUltimo) {
						colaLetrados.add(new LetradoInscripcion(punteroInscripciones));
					} else {
						colaAuxiliar.add(new LetradoInscripcion(punteroInscripciones));
					}
				}

				// revisando si se encontro ya al ultimo
				if (!foundUltimo && punteroInscripciones.equals(ultimoAnterior))
					foundUltimo = true;
			}
			colaLetrados.addAll(colaAuxiliar);
		}
		
		
		// quitando letrados de la cola si tienen saltos
		if (quitarSaltos) {
			HashMap<Long, ArrayList<LetradoInscripcion>> personasConSaltos = saladm.getSaltos(idInstitucion, idTurno, null);
			ArrayList<LetradoInscripcion> alSaltos;
			for (Iterator iter = colaLetrados.iterator(); iter.hasNext(); ) {
				letradoTurno = (LetradoInscripcion) iter.next();
				if ( (alSaltos = personasConSaltos.get(letradoTurno.getIdPersona())) != null )
					iter.remove();
			}
		}
		
		return colaLetrados;
	} //getColaTurno()

	
	////////////////////
	// METODOS
	////////////////////
	/**
	 * Da de alta la inscripcion de un colegiado a un turno
	 * @throws SIGAException 
	 * @throws ClsExceptions 
	 * @throws SIGAException 
	 * 
	 * @TODO falta implementar. Actualmente no esta aqui
	 */
	private void solicitarAlta (Integer idInstitucion,
			Integer idTurno,
			Long idPersona,
			String observacionesSolicitudAlta,
			String fechaValidacion,
			String observacionesValidacion,
			String validarInscripciones,
			Integer tipoGuardias,
			String guardiasSeleccionadas,
			String idDireccion,
			String fax1,
			String fax2,
			String movil,
			String telefono1,
			String telefono2,
			String idRetencion,
			UsrBean usr) throws ClsExceptions, SIGAException
			{

		String resultado = "messages.inserted.error";
//		UserTransaction tx = null;


		RowsContainer rc=null;
		boolean existe=false;

		String fValidacion = null;
		if (guardiasSeleccionadas!=null && !guardiasSeleccionadas.equals("")&& guardiasSeleccionadas.indexOf("@")>=0){
			guardiasSeleccionadas=guardiasSeleccionadas.substring(0,guardiasSeleccionadas.lastIndexOf("@"));
		}  
		String guardiasSel[];
		String fv = null;
		Hashtable miHash = new Hashtable();
		// Comprobamos si hay que validarinscripciones
		if(validarInscripciones.equalsIgnoreCase("N"))
		{
			miHash.put("FECHAVALIDACION","sysdate");
			miHash.put("OBSERVACIONESVALIDACION",".");
		}else{

			if(fechaValidacion!=null && !fechaValidacion.equals(""))
			{
				if(fechaValidacion.equals("sysdate")){
					miHash.put("FECHAVALIDACION",fechaValidacion);
				}else{
					miHash.put("FECHAVALIDACION",GstDate.getApplicationFormatDate(usr.getLanguage(),fechaValidacion));
				}
				miHash.put("OBSERVACIONESVALIDACION",observacionesValidacion);
			}
		}
		// Si el campo del turno validarinscripciones = 'n', insertamos en la fechavalidacion y
		// en la fecha de baja la fecha del sistema.




		// Preparamos la hash
		miHash.put("IDPERSONA",idPersona);  
		miHash.put("IDINSTITUCION",usr.getLocation());
		miHash.put("IDTURNO",idTurno.toString());
		miHash.put("FECHASOLICITUD","sysdate");
		if(observacionesSolicitudAlta!=null)
			miHash.put("OBSERVACIONESSOLICITUD",observacionesSolicitudAlta);
		else
			miHash.put("OBSERVACIONESSOLICITUD","");
		// Vamos a realizar el alta. 
		// Comienzo control de transacciones
		// Hay que realizar un alta por cada registro de la tabla inscripcion turno.
		// Obtenemos todas las guardias del idinstitucion+idturno
		


		// ACTUALIZAMOS LA DIRECCIÓN DE GUARDIA DEL CLIENTE. EN CASO DE QUE NO EXISTIERA UNA DIRECCIÓN DE GUARDIA
		// ENTONCES LA INSERTAMOS Y SI YA EXISTIA ACTUALIZAMOS LOS DATOS DE LA MISMA
	
		CenDireccionesBean beanDir  = new CenDireccionesBean ();
		CenDireccionesAdm direccionesAdm = new CenDireccionesAdm (usr);
		beanDir.setFax1(fax1);
		beanDir.setFax2(fax2);
		beanDir.setIdInstitucion(idInstitucion);
		beanDir.setIdPersona(new Long(idPersona));
		beanDir.setMovil(movil);
		beanDir.setTelefono1(telefono1);
		beanDir.setTelefono2(telefono2);
		if(idDireccion!=null&&!idDireccion.equals(""))
		{
			// Actualizamos el registro de la dirección de guardia
			beanDir.setIdDireccion(new Long(idDireccion));
			String[] claves ={CenDireccionesBean.C_IDDIRECCION,CenDireccionesBean.C_IDINSTITUCION,CenDireccionesBean.C_IDPERSONA}; 
			String[] campos ={CenDireccionesBean.C_FAX1,CenDireccionesBean.C_FAX2,CenDireccionesBean.C_MOVIL,CenDireccionesBean.C_TELEFONO1,CenDireccionesBean.C_TELEFONO2};
			if (!direccionesAdm.updateDirect(beanDir,claves,campos)) {
				throw new ClsExceptions (direccionesAdm.getError());
			}
		}
		else
		{

			//Insertamos la nueva direccion de guardia
			beanDir.setCodigoPostal("");
			beanDir.setCorreoElectronico("");
			beanDir.setDomicilio("");
			beanDir.setFechaBaja("");
			beanDir.setIdPais("");
			beanDir.setIdPoblacion("");
			beanDir.setIdProvincia("");
			beanDir.setPaginaweb("");
			beanDir.setPreferente("");
			try {
				beanDir.setIdDireccion(direccionesAdm.getNuevoID(beanDir));
			} catch (SIGAException e) {
				throw new ClsExceptions (e.getMessage());
			}
			if (!direccionesAdm.insert(beanDir)) {
				throw new ClsExceptions (direccionesAdm.getError());
			}
			// insertamos el tipo de dirección
			CenDireccionTipoDireccionAdm admTipoDir = new CenDireccionTipoDireccionAdm (usr);
			CenDireccionTipoDireccionBean beanTipoDir=new CenDireccionTipoDireccionBean();
			beanTipoDir.setIdDireccion(beanDir.getIdDireccion());
			beanTipoDir.setIdInstitucion(beanDir.getIdInstitucion());
			beanTipoDir.setIdPersona(beanDir.getIdPersona());
			beanTipoDir.setIdTipoDireccion(new Integer(ClsConstants.TIPO_DIRECCION_GUARDIA));
			if (!admTipoDir.insert(beanTipoDir)){
				throw new ClsExceptions (admTipoDir.getError());
			}


		}
		

		ScsInscripcionTurnoAdm inscripcionTurnoAdm = new ScsInscripcionTurnoAdm(usr);

		
		ScsInscripcionTurnoBean inscripcionExistenteSinBaja = inscripcionTurnoAdm.getInscripcionSinBaja(idInstitucion, idTurno, idPersona); 
		if(inscripcionExistenteSinBaja!=null){
			throw new SIGAException("Existe una inscripcion en el turno que no tiene fecha de baja");
		}

			boolean result = inscripcionTurnoAdm.insert(miHash);
			
			InscripcionGuardia inscripcionGuardia = null;

			Hashtable hash = null; 
			if(result && tipoGuardias!=null ){
				if ( tipoGuardias.intValue()==0){
					
					ScsGuardiasTurnoAdm admGuardiasTurno = new ScsGuardiasTurnoAdm(usr);
					List<ScsGuardiasTurnoBean> lGuardias = admGuardiasTurno.getGuardiasTurnos(idTurno, idInstitucion, false);
					if(lGuardias!=null && lGuardias.size()>0){
						for(int x=0;x<lGuardias.size();x++)
						{
							ScsGuardiasTurnoBean guardiaBean = lGuardias.get(x);
	
							inscripcionGuardia = InscripcionGuardia.getInscripcionGuardia (
									new Integer(idInstitucion), 
									new Integer(idTurno), 
									guardiaBean.getIdGuardia(), 
									new Long(idPersona), 
									"sysdate", 
									usr, false);
							if(validarInscripciones.equalsIgnoreCase("N"))
							{
								fechaValidacion="sysdate";
								observacionesValidacion=".";
							}
							
							if(!fechaValidacion.equals(""))
							{
								inscripcionGuardia.setAltas(observacionesSolicitudAlta,fechaValidacion,observacionesValidacion);
							}else{
								inscripcionGuardia.setAltas(observacionesSolicitudAlta,null,null);
								
								
							}
							inscripcionGuardia.solicitarAlta(usr);
						}
					}
				}else{
					//if( tipoGuardias.intValue()==2){
						if( guardiasSeleccionadas!=null && !guardiasSeleccionadas.equals("")){
							guardiasSel=guardiasSeleccionadas.split("@");
							if(validarInscripciones.equalsIgnoreCase("N"))
							{
								fechaValidacion="sysdate";
								observacionesValidacion=".";
							}

							Date date = new Date();
							String fechaInscripcion= formatoFecha.format(date);

							for (int i=0; i<guardiasSel.length;i++){
								try {
									inscripcionGuardia = InscripcionGuardia.getInscripcionGuardia (
											new Integer(idInstitucion), 
											new Integer(idTurno), 
											new Integer(guardiasSel[i]), 
											new Long(idPersona), 
											formatoFecha.format(formatoFecha.parse(fechaInscripcion)), 
											usr, false);
								} catch (Exception e) {
									inscripcionGuardia = InscripcionGuardia.getInscripcionGuardia (
											new Integer(idInstitucion), 
											new Integer(idTurno), 
											new Integer(guardiasSel[i]), 
											new Long(idPersona), 
											"sysdate", 
											usr, false);
								} 
								
								if(!fechaValidacion.equals(""))
								{
									inscripcionGuardia.setAltas(observacionesSolicitudAlta,fechaValidacion,observacionesValidacion);
									
								}else{
									inscripcionGuardia.setAltas(observacionesSolicitudAlta,null,null);
									
									
								}
								inscripcionGuardia.solicitarAlta(usr);
							}
						}
					//}
				}
			}
			
			// Si se ha incluido retencion, se da de alta.
			if(idRetencion != null)
			{
				ScsRetencionesIRPFAdm scsRetencionesIRPFAdm = new ScsRetencionesIRPFAdm(usr);
				miHash = new Hashtable();
				miHash.put("IDINSTITUCION",usr.getLocation());
				miHash.put("IDPERSONA",idPersona);
				miHash.put("IDRETENCION",idRetencion);
				miHash.put("DESCRIPCION",".");
				miHash.put("FECHAINICIO",GstDate.getApplicationFormatDate(usr.getLanguage(),"01/01/2005"));
				result = scsRetencionesIRPFAdm.insert(miHash);
				idRetencion=null;
				
			}
			
			if (!result)
			{
				throw new SIGAException("messages.inserted.error");
			}
		
	} 
	//solicitarAlta ()
	
	/**
	 * Valida la inscripcion de un colegiado a un turno
	 * @throws SystemException 
	 * @throws SecurityException 
	 * @throws IllegalStateException 
	 * 
	 * @TODO falta implementar. Actualmente no esta aqui
	 */
	public void validarAlta (String fechaValidacion, String observacionesValidacion,
			 UsrBean usr) throws ClsExceptions{
		
		String[] clavesTurno = {ScsInscripcionTurnoBean.C_IDINSTITUCION,
				ScsInscripcionTurnoBean.C_IDPERSONA, 
				ScsInscripcionTurnoBean.C_IDTURNO,ScsInscripcionTurnoBean.C_FECHASOLICITUD};
		String[] camposTurno = {ScsInscripcionTurnoBean.C_FECHAVALIDACION,ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION};
		// Preparamos la hash
		Hashtable htInscTurno = new Hashtable();
		htInscTurno.put(ScsInscripcionTurnoBean.C_IDPERSONA,this.bean.getIdPersona());  
		htInscTurno.put(ScsInscripcionTurnoBean.C_IDINSTITUCION,this.bean.getIdInstitucion());
		htInscTurno.put(ScsInscripcionTurnoBean.C_IDTURNO,this.bean.getIdTurno());
		htInscTurno.put(ScsInscripcionTurnoBean.C_FECHASOLICITUD,this.bean.getFechaSolicitud());

		if(fechaValidacion.equals("sysdate")){
			htInscTurno.put(ScsInscripcionTurnoBean.C_FECHAVALIDACION,fechaValidacion);
		}else{
			htInscTurno.put(ScsInscripcionTurnoBean.C_FECHAVALIDACION,GstDate.getApplicationFormatDate(usr.getLanguage(),fechaValidacion));
		}
//		htInscTurno.put(ScsInscripcionTurnoBean.C_FECHAVALIDACION,GstDate.getApplicationFormatDate(usr.getLanguage(),fechaValidacion));
		
		htInscTurno.put(ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION,observacionesValidacion);
		//Solo validamos las guardias cuando sea a elegir
//		if(tipoGuardias.intValue()!=ScsTurnoBean.TURNO_GUARDIAS_ELEGIR){
			String[] clavesGuardia = {ScsInscripcionGuardiaBean.C_IDINSTITUCION,
					ScsInscripcionGuardiaBean.C_IDPERSONA, 
					ScsInscripcionGuardiaBean.C_IDTURNO};
			String[] camposGuardia = {ScsInscripcionGuardiaBean.C_FECHAVALIDACION,ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION};
			Hashtable htInscGuardia = new Hashtable();
			htInscGuardia.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION	,this.bean.getIdInstitucion());
			htInscGuardia.put(ScsInscripcionGuardiaBean.C_IDPERSONA	,this.bean.getIdPersona());
			htInscGuardia.put(ScsInscripcionGuardiaBean.C_IDTURNO,this.bean.getIdTurno());
			if(fechaValidacion.equals("sysdate")){
				htInscGuardia.put(ScsInscripcionTurnoBean.C_FECHAVALIDACION,fechaValidacion);
			}else{
				htInscGuardia.put(ScsInscripcionTurnoBean.C_FECHAVALIDACION,GstDate.getApplicationFormatDate(usr.getLanguage(),fechaValidacion));
			}
			
//			htInscGuardia.put(ScsInscripcionTurnoBean.C_FECHAVALIDACION,GstDate.getApplicationFormatDate(usr.getLanguage(),fechaValidacion));
			htInscGuardia.put(ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION,observacionesValidacion);
	
			
			
			
			ScsInscripcionGuardiaAdm admInscripcionGuardia = new ScsInscripcionGuardiaAdm(usr);
	        
	
			List vGuardiasTurno =  (List) admInscripcionGuardia.getGuardiasInscripcion(this.bean.getIdInstitucion(), 
					this.bean.getIdTurno(),this.bean.getIdPersona(),null);
//		}
		
		
		ScsInscripcionTurnoAdm scsInscripcionTurnoAdm = new ScsInscripcionTurnoAdm(usr);
		InscripcionGuardia inscripcionGuardia = null;
//		UserTransaction tx = null;
		
		try {
//			tx = usr.getTransaction();
//			tx.begin();
			scsInscripcionTurnoAdm.updateDirect(htInscTurno,clavesTurno,camposTurno);
			if(vGuardiasTurno!=null){
				for(int x=0;x<vGuardiasTurno.size();x++)
				{
					ScsInscripcionGuardiaBean beanInscripcionGuardia = (ScsInscripcionGuardiaBean)vGuardiasTurno.get(x);
					inscripcionGuardia = InscripcionGuardia.getInscripcionGuardia (
							this.bean.getIdInstitucion(), 
							this.bean.getIdTurno(), 
							beanInscripcionGuardia.getIdGuardia(), 
							this.bean.getIdPersona(), 
							beanInscripcionGuardia.getFechaSuscripcion(), 
							usr, false);
					
					inscripcionGuardia.setAltas(null,fechaValidacion,observacionesValidacion);
					inscripcionGuardia.validarAlta(usr);
				}
			}
			
//			tx.commit();
		} catch (ClsExceptions e) {
			try {
//				tx.rollback();
			} catch (Exception ex) {
				throw new ClsExceptions("error.messages.application");
			}
			throw e;
		} catch (Exception e) {
			try {
//				tx.rollback();
			} catch (Exception ex) {
				throw new ClsExceptions("error.messages.application");
			}
			throw new ClsExceptions("error.messages.application");
		}
			
			

			
	
	} 
	public void modificarFechaValidacion(String fechaValidacion, String observacionesValidacion,
			 UsrBean usr) throws ClsExceptions{
		
		String[] clavesTurno = {ScsInscripcionTurnoBean.C_IDINSTITUCION,
				ScsInscripcionTurnoBean.C_IDPERSONA, 
				ScsInscripcionTurnoBean.C_IDTURNO,ScsInscripcionTurnoBean.C_FECHASOLICITUD};
		String[] camposTurno = {ScsInscripcionTurnoBean.C_FECHAVALIDACION,ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION};
		// Preparamos la hash
		Hashtable htInscTurno = new Hashtable();
		htInscTurno.put(ScsInscripcionTurnoBean.C_IDPERSONA,this.bean.getIdPersona());  
		htInscTurno.put(ScsInscripcionTurnoBean.C_IDINSTITUCION,this.bean.getIdInstitucion());
		htInscTurno.put(ScsInscripcionTurnoBean.C_IDTURNO,this.bean.getIdTurno());
		htInscTurno.put(ScsInscripcionTurnoBean.C_FECHASOLICITUD,this.bean.getFechaSolicitud());

		
		htInscTurno.put(ScsInscripcionTurnoBean.C_FECHAVALIDACION,GstDate.getApplicationFormatDate(usr.getLanguage(),fechaValidacion));
		htInscTurno.put(ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION,observacionesValidacion);

		String[] clavesGuardia = {ScsInscripcionGuardiaBean.C_IDINSTITUCION,
				ScsInscripcionGuardiaBean.C_IDPERSONA, 
				ScsInscripcionGuardiaBean.C_IDTURNO};
		String[] camposGuardia = {ScsInscripcionGuardiaBean.C_FECHAVALIDACION,ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION};
		Hashtable htInscGuardia = new Hashtable();
		htInscGuardia.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION	,this.bean.getIdInstitucion());
		htInscGuardia.put(ScsInscripcionGuardiaBean.C_IDPERSONA	,this.bean.getIdPersona());
		htInscGuardia.put(ScsInscripcionGuardiaBean.C_IDTURNO,this.bean.getIdTurno());
		
		htInscGuardia.put(ScsInscripcionTurnoBean.C_FECHAVALIDACION,GstDate.getApplicationFormatDate(usr.getLanguage(),fechaValidacion));
		htInscGuardia.put(ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION,observacionesValidacion);

		
		
		
		ScsInscripcionGuardiaAdm admInscripcionGuardia = new ScsInscripcionGuardiaAdm(usr);
        
		
		List vGuardiasTurno =  (List) admInscripcionGuardia.getGuardiasInscripcion(this.bean.getIdInstitucion(), 
				this.bean.getIdTurno(),this.bean.getIdPersona(),null);
	
		
		ScsInscripcionTurnoAdm scsInscripcionTurnoAdm = new ScsInscripcionTurnoAdm(usr);
		InscripcionGuardia inscripcionGuardia = null;
//		UserTransaction tx = null;
		
		try {
//			tx = usr.getTransaction();
//			tx.begin();
			scsInscripcionTurnoAdm.updateDirect(htInscTurno,clavesTurno,camposTurno);
			
			if(vGuardiasTurno!=null){
				for(int x=0;x<vGuardiasTurno.size();x++)
				{
					ScsInscripcionGuardiaBean beanInscripcionGuardia = (ScsInscripcionGuardiaBean)vGuardiasTurno.get(x);
					inscripcionGuardia = InscripcionGuardia.getInscripcionGuardia (
							this.bean.getIdInstitucion(), 
							this.bean.getIdTurno(), 
							beanInscripcionGuardia.getIdGuardia(), 
							this.bean.getIdPersona(), 
							beanInscripcionGuardia.getFechaSuscripcion(), 
							usr, false);
					
					inscripcionGuardia.setAltas(null,fechaValidacion,observacionesValidacion);
					inscripcionGuardia.validarAlta(usr);
				}
			}
			
//			tx.commit();
		} catch (ClsExceptions e) {
			try {
//				tx.rollback();
			} catch (Exception ex) {
				throw new ClsExceptions("error.messages.application");
			}
			throw e;
		} catch (Exception e) {
			try {
//				tx.rollback();
			} catch (Exception ex) {
				throw new ClsExceptions("error.messages.application");
			}
			throw new ClsExceptions("error.messages.application");
		}
			
			

			
	
	} 
	
	

	
	
	private void solicitarBaja (String fechaSolicitudBaja, String observacionesSolicitudBaja, String validarInscripciones, UsrBean usr)
		throws ClsExceptions
	{
		try {
			Integer idInstitucion = this.bean.getIdInstitucion();
			Integer idTurno = this.bean.getIdTurno();
			Long idPersona = this.bean.getIdPersona();
			
			//insertando solicitud de baja
			Hashtable<String, Object> inscripcionHash = new Hashtable<String, Object>();
			UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_IDTURNO, idTurno);
			UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_IDPERSONA, idPersona);
			UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_FECHASOLICITUD, this.bean.getFechaSolicitud());
			ScsInscripcionTurnoAdm inscripcionAdm = new ScsInscripcionTurnoAdm(usr);
			Vector<ScsInscripcionTurnoBean> v = inscripcionAdm.selectByPKForUpdate(inscripcionHash);
			
			boolean tieneBajaDenegada = false;
			if (v != null && v.size() == 1) {
				ScsInscripcionTurnoBean inscripcionBean = (ScsInscripcionTurnoBean) v.get(0);
				if(validarInscripciones.equalsIgnoreCase("N"))
				{
				
					inscripcionBean.setFechaBaja("sysdate");
					inscripcionBean.setFechaSolicitudBaja(fechaSolicitudBaja);
					
				}else{
					if(inscripcionBean.getFechaDenegacion()!=null && !inscripcionBean.getFechaDenegacion().equals("")){
						tieneBajaDenegada = true;
						String observacionDenegacion = inscripcionBean.getObservacionesDenegacion()==null?"":inscripcionBean.getObservacionesDenegacion();
						observacionesSolicitudBaja += "[Fecha denegación previa:"+GstDate.getFormatedDateShort("", inscripcionBean.getFechaDenegacion()) +"]";
						observacionesSolicitudBaja += "[Observaciones denegación previa:"+observacionDenegacion+"]";
//						inscripcionBean.setFechaDenegacion(null);
//						inscripcionBean.setObservacionesDenegacion(null								);
						
					}
					inscripcionBean.setFechaSolicitudBaja(fechaSolicitudBaja);
					String observacionesPrevias = inscripcionBean.getObservacionesBaja()==null||inscripcionBean.getObservacionesBaja().equals("")?"":"{"+inscripcionBean.getObservacionesBaja()+"}";
					inscripcionBean.setObservacionesBaja(observacionesSolicitudBaja+observacionesPrevias);
					
				}
				if (! inscripcionAdm.update(inscripcionBean)) {
					throw new ClsExceptions("Error al realizar la solicitud de baja en el turno");
				}
			}
			if(tieneBajaDenegada)
				denegarBajaInscripcionTurno(null, null, usr);
			
			
			ScsInscripcionGuardiaAdm admInscripcionGuardia = new ScsInscripcionGuardiaAdm(usr);
	        
			
			List vGuardiasTurno =  (List) admInscripcionGuardia.getGuardiasInscripcion(this.bean.getIdInstitucion(), 
					this.bean.getIdTurno(),this.bean.getIdPersona(),null);
			
			
			ScsInscripcionTurnoAdm scsInscripcionTurnoAdm = new ScsInscripcionTurnoAdm(usr);
			InscripcionGuardia inscripcionGuardia = null;
			if(vGuardiasTurno!=null && vGuardiasTurno.size()>0){
				for(int x=0;x<vGuardiasTurno.size();x++)
				{
					ScsInscripcionGuardiaBean beanInscripcionGuardia = (ScsInscripcionGuardiaBean)vGuardiasTurno.get(x);
					inscripcionGuardia = InscripcionGuardia.getInscripcionGuardia (
							this.bean.getIdInstitucion(), 
							this.bean.getIdTurno(), 
							beanInscripcionGuardia.getIdGuardia(), 
							this.bean.getIdPersona(), 
							beanInscripcionGuardia.getFechaSuscripcion(), 
							usr, false);
					String fechaBaja = null;
					if(validarInscripciones.equalsIgnoreCase("N"))
					{
						fechaBaja="sysdate";
						
					}
					inscripcionGuardia.setBajas(observacionesSolicitudBaja,fechaSolicitudBaja,fechaBaja,"-----");
					inscripcionGuardia.solicitarBaja(usr, null);
				}
			}
					

			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al solicitar la solicitud de baja en el turno");
		}
	} //solicitarBaja ()
	

	public void solicitarBaja (String fechaSolicitudBaja, String observacionesSolicitudBaja,String fechaBaja,String observacionesValBaja,String fechaValidacion, String validarInscripciones, String cancelarSyC, UsrBean usr)
	throws ClsExceptions
{
		try {
			solicitarBaja(fechaSolicitudBaja, observacionesSolicitudBaja,validarInscripciones,  usr);
			//el proceso de solicitar baja valida las que no es necesario la validacion de las inscripciones
			if(validarInscripciones.equals("S")){
				if(fechaBaja!=null&&!fechaBaja.equals(""))
					validarBaja(fechaBaja,fechaValidacion,observacionesValBaja, null, usr);
			}
			if(cancelarSyC!=null && cancelarSyC.equalsIgnoreCase("T")){
				try {
					//String actuali = miForm.getTipoActualizacionSyC();
					
					//saltos y compensaciones
					ScsSaltosCompensacionesAdm saladm = new ScsSaltosCompensacionesAdm(usr);
					//ArrayList<ScsSaltosCompensacionesBean> vSaldosyCompensTurno =  (ArrayList<ScsSaltosCompensacionesBean>) admSaltosYCompensacions.getSaltosCompensaciones(this.bean.getIdInstitucion(), 
					//this.bean.getIdTurno(),this.bean.getIdPersona(), null);
					saladm.updateSaltosCompensacionesBajaTurno(this.bean.getIdInstitucion(), 
							this.bean.getIdTurno(),this.bean.getIdPersona(), null, cancelarSyC);
					
				}catch (Exception e) {
						throw new ClsExceptions (e, "Error al solicitar la baja en los Saltos y turnos");
					}
			}
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al solicitar la solicitud de baja en el turno");
		}
} //solicitarBaja ()

	public void denegarInscripcionTurno (String fechaDenegacion,String observacionesDenegacion, UsrBean usr)
	throws ClsExceptions
{
	try {
		Integer idInstitucion = this.bean.getIdInstitucion();
		Integer idTurno = this.bean.getIdTurno();
		Long idPersona = this.bean.getIdPersona();
		ScsInscripcionGuardiaAdm admInscripcionGuardia = new ScsInscripcionGuardiaAdm(usr);
		List vGuardiasTurno =  (List) admInscripcionGuardia.getGuardiasInscripcion(this.bean.getIdInstitucion(), 
					this.bean.getIdTurno(),this.bean.getIdPersona(),null);
			
		InscripcionGuardia inscripcionGuardia = null;
		if(vGuardiasTurno!=null && vGuardiasTurno.size()>0){
			for(int x=0;x<vGuardiasTurno.size();x++)
			{
				ScsInscripcionGuardiaBean beanInscripcionGuardia = (ScsInscripcionGuardiaBean)vGuardiasTurno.get(x);
				inscripcionGuardia = InscripcionGuardia.getInscripcionGuardia (
						this.bean.getIdInstitucion(), 
						this.bean.getIdTurno(), 
						beanInscripcionGuardia.getIdGuardia(), 
						this.bean.getIdPersona(), 
						beanInscripcionGuardia.getFechaSuscripcion(), 
						usr, false);
				
				inscripcionGuardia.setDenegacion(observacionesDenegacion,fechaDenegacion);
				inscripcionGuardia.denegarAltaGuardia(usr);
			}
		}
		//insertando baja
		Hashtable<String, Object> inscripcionHash = new Hashtable<String, Object>();
		UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_IDINSTITUCION, idInstitucion);
		UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_IDTURNO, idTurno);
		UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_IDPERSONA, idPersona);
		UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_FECHASOLICITUD, this.bean.getFechaSolicitud());
		ScsInscripcionTurnoAdm inscripcionAdm = new ScsInscripcionTurnoAdm(usr);
		Vector<ScsInscripcionTurnoBean> v = inscripcionAdm.selectByPKForUpdate(inscripcionHash);
		if (v != null && v.size() == 1) {
			ScsInscripcionTurnoBean inscripcionBean = (ScsInscripcionTurnoBean) v.get(0);
			if(!fechaDenegacion.equalsIgnoreCase("sysdate")){
				//inscripcionBean.setFechaBaja(GstDate.getApplicationFormatDate(usr.getLanguage(),fechaDenegacion));
				inscripcionBean.setFechaDenegacion(GstDate.getApplicationFormatDate(usr.getLanguage(),fechaDenegacion));
			}else{
				// inscripcionBean.setFechaBaja(fechaDenegacion);
				inscripcionBean.setFechaDenegacion(fechaDenegacion);
			}
			inscripcionBean.setObservacionesDenegacion(observacionesDenegacion);
			
			if (! inscripcionAdm.update(inscripcionBean)) {
				throw new ClsExceptions("Error al denegar el alta en el turno");
			}
		}
		
		
		
	}
	catch (Exception e) {
		throw new ClsExceptions (e, "Error al denegar el alta en el turno");
	}
} //validarBaja ()
	
	public void denegarBajaInscripcionTurno (String fechaDenegacion,String observacionesDenegacion, UsrBean usr)
	throws ClsExceptions
{
	try {
		Integer idInstitucion = this.bean.getIdInstitucion();
		Integer idTurno = this.bean.getIdTurno();
		Long idPersona = this.bean.getIdPersona();
		ScsInscripcionGuardiaAdm admInscripcionGuardia = new ScsInscripcionGuardiaAdm(usr);
		List vGuardiasTurno =  (List) admInscripcionGuardia.getGuardiasInscripcion(this.bean.getIdInstitucion(), 
				this.bean.getIdTurno(),this.bean.getIdPersona(),null);

		InscripcionGuardia inscripcionGuardia = null;
		if(vGuardiasTurno!=null && vGuardiasTurno.size()>0){
			for(int x=0;x<vGuardiasTurno.size();x++)
			{
				ScsInscripcionGuardiaBean beanInscripcionGuardia = (ScsInscripcionGuardiaBean)vGuardiasTurno.get(x);			inscripcionGuardia = InscripcionGuardia.getInscripcionGuardia (
						this.bean.getIdInstitucion(), 
						this.bean.getIdTurno(), 
						beanInscripcionGuardia.getIdGuardia(), 
						this.bean.getIdPersona(), 
						beanInscripcionGuardia.getFechaSuscripcion(), 
						usr, false);
				
				inscripcionGuardia.setDenegacion(observacionesDenegacion,fechaDenegacion);
				inscripcionGuardia.denegarBajaGuardia(usr);
			}
		}
		
		//denegando baja turno
		String[] claves = new String[]{ScsInscripcionTurnoBean.C_IDINSTITUCION,
				ScsInscripcionTurnoBean.C_IDPERSONA, 
				ScsInscripcionTurnoBean.C_IDTURNO, 
				ScsInscripcionTurnoBean.C_FECHASOLICITUD};
		
			
		Hashtable<String, Object> inscripcionHash = new Hashtable<String, Object>();
		UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_IDINSTITUCION, idInstitucion);
		UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_IDTURNO, idTurno);
		UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_IDPERSONA, idPersona);
		UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_FECHASOLICITUD, this.bean.getFechaSolicitud());
		if(fechaDenegacion!=null){
			if(!fechaDenegacion.equalsIgnoreCase("sysdate")){
				// inscripcionBean.setFechaBaja(GstDate.getApplicationFormatDate(usr.getLanguage(),fechaDenegacion));
				UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_FECHADENEGACION, GstDate.getApplicationFormatDate(usr.getLanguage(),fechaDenegacion));
			}else{
				UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_FECHADENEGACION,fechaDenegacion);
				// inscripcionBean.setFechaBaja(fechaDenegacion);
			}
			UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_OBSERVACIONESDENEGACION,observacionesDenegacion);
		}
		

		String[] campos = {ScsInscripcionTurnoBean.C_FECHADENEGACION,ScsInscripcionTurnoBean.C_OBSERVACIONESDENEGACION};
		ScsInscripcionTurnoAdm insturno = new ScsInscripcionTurnoAdm(usr);
		insturno.updateDirect(inscripcionHash,claves,campos);
		
		
		
	}
	
	
	catch (Exception e) {
		throw new ClsExceptions (e, "Error al denegar el alta en el turno");
	}
} //validarBaja ()
	/**
	 * Confirma la baja de la inscripcion actual del colegiado en el turno
	 * 
	 * @TODO falta quizas algo de saltos y compensaciones, o pedir confirmacion por interfaz
	 */
	public void validarBaja (String fechaBaja,String fechaValidacion,String obsValBaja, String bajaSyC, UsrBean usr)
		throws ClsExceptions
	{
		try {
			Integer idInstitucion = this.bean.getIdInstitucion();
			Integer idTurno = this.bean.getIdTurno();
			Long idPersona = this.bean.getIdPersona();
//			if(!fechaBaja.equalsIgnoreCase("sysdate")){
//				fechaBaja = GstDate.getApplicationFormatDate(usr.getLanguage(),fechaBaja);
//			}
			
			//comprobando si es el siguiente de la cola
			//Solo se comrpueba lo de la cola cuando no sea una denegacion de alta, esto es cuando no haya estado validado
			if(fechaValidacion!=null){
				Hashtable<String, Object> hashTurno = new Hashtable<String, Object>();
				hashTurno.put(ScsTurnoBean.C_IDINSTITUCION, idInstitucion);
				hashTurno.put(ScsTurnoBean.C_IDTURNO, idTurno);
				Vector vTurnos = new ScsTurnoAdm(usr).select(hashTurno);
				Long idPersonaUltimoTurno = null;
				if(vTurnos!=null && vTurnos.size()>0)
					idPersonaUltimoTurno = ((ScsTurnoBean)vTurnos.get(0) 
						).getIdPersonaUltimo();
				
					
				if (idPersonaUltimoTurno!=null &&  idPersonaUltimoTurno.equals(idPersona)) {
					List<LetradoInscripcion> colaLetrados =InscripcionTurno.getColaTurno(idInstitucion,idTurno,"sysdate",false,usr);
					if (colaLetrados.get(0) != null && !colaLetrados.isEmpty()) {
						int tamanyoCola = colaLetrados.size();
						//Long ultimoDeLaCola = ((LetradoGuardia) colaLetrados.get(tamanyoCola-1)).getIdPersona();
					//el letrado a dar de baja es el primero en la cola:
					//asi que hay que poner al anterior como ultimo en la cola
					Long penultimoDeLaCola;
					String fechaSusc_penultimo;
					if (tamanyoCola == 1){
						penultimoDeLaCola = null;
						fechaSusc_penultimo = null;
					}
					else{
						penultimoDeLaCola = ((LetradoInscripcion) colaLetrados.get(tamanyoCola-2)).getIdPersona();
						fechaSusc_penultimo = ((LetradoInscripcion) colaLetrados.get(tamanyoCola-2)).getInscripcionTurno().getFechaSolicitud();
					}
					ScsTurnoAdm turnoAdm = new ScsTurnoAdm(usr);
					turnoAdm.cambiarUltimoCola(idInstitucion, idTurno, penultimoDeLaCola, fechaSusc_penultimo);
				}
			}
			}
			
			ScsInscripcionGuardiaAdm admInscripcionGuardia = new ScsInscripcionGuardiaAdm(usr);
			List vGuardiasTurno =  (List) admInscripcionGuardia.getGuardiasInscripcion(this.bean.getIdInstitucion(), 
					this.bean.getIdTurno(),this.bean.getIdPersona(),null);
			
			
			InscripcionGuardia inscripcionGuardia = null;
			if(vGuardiasTurno!=null && vGuardiasTurno.size()>0){
				for(int x=0;x<vGuardiasTurno.size();x++)
				{
					ScsInscripcionGuardiaBean beanInscripcionGuardia = (ScsInscripcionGuardiaBean)vGuardiasTurno.get(x);			
					inscripcionGuardia = InscripcionGuardia.getInscripcionGuardia (
							this.bean.getIdInstitucion(), 
							this.bean.getIdTurno(), 
							beanInscripcionGuardia.getIdGuardia(), 
							this.bean.getIdPersona(), 
							beanInscripcionGuardia.getFechaSuscripcion(), 
							usr, false);
					
					inscripcionGuardia.setBajas(null,null,fechaBaja,obsValBaja);
					inscripcionGuardia.validarBaja(usr, null);
				}
			}
			
			//insertando baja
			Hashtable<String, Object> inscripcionHash = new Hashtable<String, Object>();
			UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_IDTURNO, idTurno);
			UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_IDPERSONA, idPersona);
			UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_FECHASOLICITUD, this.bean.getFechaSolicitud());
			ScsInscripcionTurnoAdm inscripcionAdm = new ScsInscripcionTurnoAdm(usr);
			Vector<ScsInscripcionTurnoBean> v = inscripcionAdm.selectByPKForUpdate(inscripcionHash);
			if (v != null && v.size() == 1) {
				ScsInscripcionTurnoBean inscripcionBean = (ScsInscripcionTurnoBean) v.get(0);
				if(!fechaBaja.equalsIgnoreCase("sysdate")){
					inscripcionBean.setFechaBaja(GstDate.getApplicationFormatDate(usr.getLanguage(),fechaBaja));
				}else{
					inscripcionBean.setFechaBaja(fechaBaja);
				}
				inscripcionBean.setObservacionesValBaja(obsValBaja);
				if (! inscripcionAdm.update(inscripcionBean)) {
					throw new ClsExceptions("Error al realizar la baja en el turno");
				}
			}

			if(bajaSyC!=null && bajaSyC.equalsIgnoreCase("T")){
				try {
					//String actuali = miForm.getTipoActualizacionSyC();
					
					//saltos y compensaciones
					ScsSaltosCompensacionesAdm saladm = new ScsSaltosCompensacionesAdm(usr);
					//ArrayList<ScsSaltosCompensacionesBean> vSaldosyCompensTurno =  (ArrayList<ScsSaltosCompensacionesBean>) admSaltosYCompensacions.getSaltosCompensaciones(this.bean.getIdInstitucion(), 
					//this.bean.getIdTurno(),this.bean.getIdPersona(), null);
					saladm.updateSaltosCompensacionesBajaTurno(this.bean.getIdInstitucion(), 
							this.bean.getIdTurno(),this.bean.getIdPersona(), null, bajaSyC);
					
				}catch (Exception e) {
						System.out.println( "Error al solicitar la baja en los Saltos y turnos");
					}
			}

			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al solicitar la baja en el turno");
		}
	} //validarBaja ()
	
	public void modificarFechaBaja (String fechaBaja, UsrBean usr)
	throws ClsExceptions
{
	try {
		Integer idInstitucion = this.bean.getIdInstitucion();
		Integer idTurno = this.bean.getIdTurno();
		Long idPersona = this.bean.getIdPersona();


		
		//insertando baja
		Hashtable<String, Object> inscripcionHash = new Hashtable<String, Object>();
		UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_IDINSTITUCION, idInstitucion);
		UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_IDTURNO, idTurno);
		UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_IDPERSONA, idPersona);
		UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_FECHASOLICITUD, this.bean.getFechaSolicitud());
		
		
		ScsInscripcionTurnoAdm inscripcionAdm = new ScsInscripcionTurnoAdm(usr);
		Vector<ScsInscripcionTurnoBean> v = inscripcionAdm.selectByPKForUpdate(inscripcionHash);
		String fechaBajaOld = null;
		if (v != null && v.size() == 1) {
			ScsInscripcionTurnoBean inscripcionBean = (ScsInscripcionTurnoBean) v.get(0);
			fechaBajaOld = inscripcionBean.getFechaBaja();
				inscripcionBean.setFechaBaja(GstDate.getApplicationFormatDate(usr.getLanguage(),fechaBaja));
			if (! inscripcionAdm.update(inscripcionBean)) {
				throw new ClsExceptions("Error al realizar la baja en el turno");
			}
		}
		
		ScsInscripcionGuardiaAdm admInscripcionGuardia = new ScsInscripcionGuardiaAdm(usr);
		Hashtable<String, Object> inscripcionGuardiaHash = new Hashtable<String, Object>();
		UtilidadesHash.set(inscripcionGuardiaHash, ScsInscripcionGuardiaBean.C_IDINSTITUCION, idInstitucion);
		UtilidadesHash.set(inscripcionGuardiaHash, ScsInscripcionGuardiaBean.C_IDTURNO, idTurno);
		UtilidadesHash.set(inscripcionGuardiaHash, ScsInscripcionGuardiaBean.C_IDPERSONA, idPersona);
		UtilidadesHash.set(inscripcionGuardiaHash, ScsInscripcionGuardiaBean.C_FECHABAJA, fechaBajaOld);
		
		
		
		
		Vector vGuardiasTurno =  admInscripcionGuardia.select(inscripcionGuardiaHash);
		InscripcionGuardia inscripcionGuardia = null;
		if(vGuardiasTurno!=null){
			for(int x=0;x<vGuardiasTurno.size();x++)
			{
				ScsInscripcionGuardiaBean beanInscripcionGuardia = (ScsInscripcionGuardiaBean)vGuardiasTurno.get(x);
				inscripcionGuardia = InscripcionGuardia.getInscripcionGuardia (
						this.bean.getIdInstitucion(), 
						this.bean.getIdTurno(), 
						beanInscripcionGuardia.getIdGuardia(), 
						this.bean.getIdPersona(), 
						beanInscripcionGuardia.getFechaSuscripcion(), 
						usr, false);
				inscripcionGuardia.setBajas(null, null,fechaBaja,null);
				inscripcionGuardia.modificarFechaBaja(usr);
				
				
			}
		}
		
		
		
	}
	catch (Exception e) {
		throw new ClsExceptions (e, "Error al solicitar la baja en el turno");
	}
}
	
	
	
	
	
}
