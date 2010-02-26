package com.siga.gratuita;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.GenClientesTemporalAdm;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsInscripcionGuardiaAdm;
import com.siga.beans.ScsInscripcionGuardiaBean;
import com.siga.beans.ScsInscripcionTurnoAdm;
import com.siga.beans.ScsInscripcionTurnoBean;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.SIGAException;
import com.siga.gratuita.util.calendarioSJCS.LetradoGuardia;

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
	private InscripcionTurno (ScsInscripcionTurnoBean bean)
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
			Vector<ScsInscripcionTurnoBean> inscripciones = new ScsInscripcionTurnoAdm(usr).select(hash);
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
	
	
	////////////////////
	// METODOS
	////////////////////
	/**
	 * Da de alta la inscripcion de un colegiado a un turno
	 * 
	 * @TODO falta implementar. Actualmente no esta aqui
	 */
	public static InscripcionTurno solicitarAlta (Integer idInstitucion,
												  Integer idTurno,
												  Long idPersona,
												  String fechaSolicitudAlta,
												  String observacionesSolicitudAlta,
												  UsrBean usr)
	{
		return null;
	} //solicitarAlta ()
	
	/**
	 * Valida la inscripcion de un colegiado a un turno
	 * 
	 * @TODO falta implementar. Actualmente no esta aqui
	 */
	public void validarAlta (String fechaAlta, String observacionesAlta, UsrBean usr)
	{
	} //validarAlta ()
	
	/**
	 * Solicita la baja de la inscripcion actual del colegiado en el turno
	 */
	public void solicitarBaja (String fechaSolicitudBaja, String observacionesSolicitudBaja, UsrBean usr)
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
			if (v != null && v.size() == 1) {
				ScsInscripcionTurnoBean inscripcionBean = (ScsInscripcionTurnoBean) v.get(0);
				inscripcionBean.setFechaSolicitudBaja(fechaSolicitudBaja);
				inscripcionBean.setObservacionesBaja(observacionesSolicitudBaja);
				if (! inscripcionAdm.update(inscripcionBean)) {
					throw new ClsExceptions("Error al realizar la solicitud de baja en el turno");
				}
			}
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al solicitar la solicitud de baja en el turno");
		}
	} //solicitarBaja ()
	
	/**
	 * Confirma la baja de la inscripcion actual del colegiado en el turno
	 * 
	 * @TODO falta quizas algo de saltos y compensaciones, o pedir confirmacion por interfaz
	 */
	public void validarBaja (String fechaBaja, String observacionesBaja, UsrBean usr)
		throws ClsExceptions
	{
		try {
			Integer idInstitucion = this.bean.getIdInstitucion();
			Integer idTurno = this.bean.getIdTurno();
			Long idPersona = this.bean.getIdPersona();
			
			//comprobando si es el siguiente de la cola
			ArrayList<LetradoGuardia> colaLetrados = 
				new GenClientesTemporalAdm(usr).obtenerLetradosPosiblesPL(
					idInstitucion, 
					idTurno,
					null);
			if (colaLetrados.get(0) != null && !colaLetrados.isEmpty()) {
				int tamanyoCola = colaLetrados.size();
				Long ultimoDeLaCola = ((LetradoGuardia) colaLetrados.get(tamanyoCola-1)).getIdPersona();
				
				if (ultimoDeLaCola.equals(idPersona)) {
					//el letrado a dar de baja es el primero en la cola:
					//asi que hay que poner al anterior como ultimo en la cola
					Long penultimoDeLaCola;
					if (tamanyoCola == 1)
						penultimoDeLaCola = null;
					else
						penultimoDeLaCola = ((LetradoGuardia) colaLetrados.get(tamanyoCola-2)).getIdPersona();
					
					cambiarUltimoCola (idInstitucion, idTurno, penultimoDeLaCola, usr);
				}
			}
			
			//dando de baja todas sus guardias
			Hashtable<String, Object> turnoHash = new Hashtable<String, Object>();
			UtilidadesHash.set(turnoHash, ScsInscripcionGuardiaBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(turnoHash, ScsInscripcionGuardiaBean.C_IDPERSONA, idPersona);
			UtilidadesHash.set(turnoHash, ScsInscripcionGuardiaBean.C_IDTURNO, idTurno);
			turnoHash.put(ScsInscripcionGuardiaBean.C_FECHABAJA, "");
			Vector<ScsInscripcionGuardiaBean> vGuardias = new ScsInscripcionGuardiaAdm(usr).select(turnoHash);
			
			InscripcionGuardia inscripcionGuardia;
			for (int x = 0; x < vGuardias.size(); x++) {
				ScsInscripcionGuardiaBean c = (ScsInscripcionGuardiaBean) vGuardias.get(x);
				inscripcionGuardia = InscripcionGuardia.getInscripcionGuardia(
						c.getIdInstitucion(), c.getIdTurno(), c.getIdGuardia(), 
						c.getIdPersona(), c.getFechaSuscripcion(), usr, false);
				inscripcionGuardia.solicitarBaja(fechaBaja, observacionesBaja, usr);
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
				inscripcionBean.setFechaBaja(fechaBaja);
				inscripcionBean.setObservacionesBaja(observacionesBaja);
				if (! inscripcionAdm.update(inscripcionBean)) {
					throw new ClsExceptions("Error al realizar la baja en el turno");
				}
			}
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al solicitar la baja en el turno");
		}
	} //validarBaja ()
	
	/**
	 * Cambia el ultimo letrado de la cola del turno indicado
	 * por el nuevo que se ha solicitado
	 * 
	 * @TODO Este metodo y tambien las comprobaciones de cola que se hacen desde esta misma clase
	 * podrian ir en una nueva clase de logica de negocio "ColaTurno" 
	 */
	public void cambiarUltimoCola (Integer idInstitucion,
								   Integer idTurno,
								   Long idPersona,
								   UsrBean usr)
		throws ClsExceptions
	{
		String sql = 
			" update "+ScsTurnoBean.T_NOMBRETABLA+" " +
			"    set "+ScsTurnoBean.C_IDPERSONAULTIMO+" = "+(idPersona==null?"null":idPersona)+", " +
			"        "+ScsTurnoBean.C_FECHAMODIFICACION+" = sysdate, " +
			"        "+ScsTurnoBean.C_USUMODIFICACION+" = "+usr.getUserName()+" " +
			"  where "+ScsTurnoBean.C_IDINSTITUCION+" = "+idInstitucion+" " +
			"    and "+ScsTurnoBean.C_IDTURNO+" = "+idTurno+" ";
		
		if (! new ScsTurnoAdm(usr).updateSQL(sql)) {
			throw new ClsExceptions("Error al cambiar ultimo letrado de cola de turno");
		}
	} //cambiarUltimoCola()
	
}
