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
import com.siga.gratuita.util.calendarioSJCS.LetradoGuardia;

public class InscripcionGuardia
{
	////////////////////
	// ATRIBUTOS
	////////////////////
	private ScsInscripcionGuardiaBean bean = null;
	
	private static SimpleDateFormat formatoFecha = 
		new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
	
	
	////////////////////
	// CONSTRUCTORES
	////////////////////
	/**
	 * Constructor
	 */
	private InscripcionGuardia (Integer idInstitucion,
								Integer idTurno,
								Integer idGuardia,
								Long idPersona,
								String fechaSolicitudAlta)
	{
		this.bean = new ScsInscripcionGuardiaBean();
		this.bean.setIdInstitucion(idInstitucion);
		this.bean.setIdTurno(idTurno);
		this.bean.setIdGuardia(idGuardia);
		this.bean.setIdPersona(idPersona);
		this.bean.setFechaSuscripcion(fechaSolicitudAlta);
	} //InscripcionGuardia ()
	private InscripcionGuardia (ScsInscripcionGuardiaBean bean)
	{
		this.bean = bean;
	} //InscripcionGuardia ()
	
	
	////////////////////
	// GETTERS Y SETTERS
	////////////////////
	public ScsInscripcionGuardiaBean getBean() { return bean; }
	public void setBean(ScsInscripcionGuardiaBean bean) { this.bean = bean; }
	
	
	////////////////////
	// METODOS DE CLASE
	////////////////////
	/**
	 * Devuelve un objeto que representa la inscripcion de un colegiado a una guardia. 
	 * Este metodo comprueba si existe realmente la inscripcion. 
	 * Sin embargo, es posible evitar la comprobacion con el parametro comprobarQueExiste=false
	 */
	public static InscripcionGuardia getInscripcionGuardia (Integer idInstitucion,
															Integer idTurno,
															Integer idGuardia,
															Long idPersona,
															String fechaSolicitudAlta,
															UsrBean usr,
															boolean comprobarQueExiste)
		throws ClsExceptions, ParseException
	{
		InscripcionGuardia inscripcion = null;
		
		if (! comprobarQueExiste)
			return new InscripcionGuardia (
					idInstitucion, idTurno, idGuardia, idPersona, fechaSolicitudAlta);
		else {
			//preparando datos
			Hashtable<String, Object> hash = new Hashtable<String, Object>();
			hash.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION, idInstitucion);
			hash.put(ScsInscripcionGuardiaBean.C_IDPERSONA, idPersona);
			hash.put(ScsInscripcionGuardiaBean.C_IDTURNO, idTurno);
			hash.put(ScsInscripcionGuardiaBean.C_IDGUARDIA, idGuardia);
			hash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION, formatoFecha.format(fechaSolicitudAlta));
			
			//obteniendo datos
			Vector<ScsInscripcionGuardiaBean> inscripciones = new ScsInscripcionGuardiaAdm(usr).select(hash);
			if (inscripciones == null)
				throw new ClsExceptions("Error: no se ha encontrado la inscripcion a la guardia");
			if (inscripciones.size() < 1)
				throw new ClsExceptions("Error: no se ha encontrado la inscripcion a la guardia");
			if (inscripciones.size() > 1)
				throw new ClsExceptions("Error: se ha encontrado mas de una inscripcion a la guardia");
			
			inscripcion = new InscripcionGuardia ((ScsInscripcionGuardiaBean) inscripciones.get(0));
		}
		
		return inscripcion;
	} //getInscripcionGuardia ()
	
	
	////////////////////
	// METODOS
	////////////////////
	/**
	 * Da de alta la inscripcion de un colegiado a una guardia.
	 * 
	 * @TODO falta implementar. Actualmente no esta aqui
	 */
	public static InscripcionGuardia solicitarAlta (Integer idInstitucion,
													Integer idTurno,
													Integer idGuardia,
													Long idPersona,
													String fechaSolicitudAlta,
													String observacionesSolicitudAlta,
													UsrBean usr)
	{
		return null;
	} //solicitarAlta ()
	
	/*
	 * Este procedimiento no hace nada, 
	 * porque no existe la validacion de alta de guardia, todavia
	public void validarAlta (String fechaAlta, String observacionesAlta, UsrBean usr)
		throws ClsExceptions
	{
	} //validarAlta ()
	 */
	
	/**
	 * Da de baja la inscripcion actual del colegiado en la guardia
	 */
	public void solicitarBaja (String fechaSolicitudBaja, String observacionesSolicitudBaja, UsrBean usr)
		throws ClsExceptions
	{
		try {
			Integer idInstitucion = this.bean.getIdInstitucion();
			Integer idTurno = this.bean.getIdTurno();
			Integer idGuardia = this.bean.getIdGuardia();
			Long idPersona = this.bean.getIdPersona();
			
			//comprobando si es el siguiente de la cola
			ArrayList<LetradoGuardia> colaLetrados = new GenClientesTemporalAdm(usr).obtenerLetradosPosiblesPL(
					idInstitucion, 
					idTurno, 
					idGuardia);
			
			//obteniendo ultimo indicado en la cola
			Hashtable<String, Object> hashGuardia = new Hashtable<String, Object>();
			hashGuardia.put(ScsGuardiasTurnoBean.C_IDINSTITUCION, idInstitucion);
			hashGuardia.put(ScsGuardiasTurnoBean.C_IDTURNO, idTurno);
			hashGuardia.put(ScsGuardiasTurnoBean.C_IDGUARDIA, idGuardia);
			Long ultimoGuardia = ((ScsGuardiasTurnoBean) 
					new ScsGuardiasTurnoAdm(usr).select(hashGuardia).get(0)).getIdPersona_Ultimo();
			
			//cambiando el ultimo de la cola si es necesario
			if (colaLetrados.get(0) != null && !colaLetrados.isEmpty()) {
				int tamanyoCola = colaLetrados.size();
				Long ultimoDeLaCola = ((LetradoGuardia) colaLetrados.get(tamanyoCola-1)).getIdPersona();
				
				if (ultimoDeLaCola.equals(idPersona) && ultimoDeLaCola.equals(ultimoGuardia)) {
					//el letrado a dar de baja es el primero en la cola:
					//asi que hay que poner al anterior como ultimo en la cola
					Long penultimoDeLaCola;
					if (tamanyoCola == 1)
						penultimoDeLaCola = null;
					else
						penultimoDeLaCola = ((LetradoGuardia) colaLetrados.get(tamanyoCola-2)).getIdPersona();
					
					cambiarUltimoCola (idInstitucion, idTurno, idGuardia, penultimoDeLaCola, usr);
				}
			}
			
			//insertando baja
			Hashtable<String, Object> inscripcionHash = new Hashtable<String, Object>();
			UtilidadesHash.set(inscripcionHash, ScsInscripcionGuardiaBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(inscripcionHash, ScsInscripcionGuardiaBean.C_IDTURNO, idTurno);
			UtilidadesHash.set(inscripcionHash, ScsInscripcionGuardiaBean.C_IDGUARDIA, idGuardia);
			UtilidadesHash.set(inscripcionHash, ScsInscripcionGuardiaBean.C_IDPERSONA, idPersona);
			UtilidadesHash.set(inscripcionHash, ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION, this.bean.getFechaSuscripcion());
			ScsInscripcionGuardiaAdm inscripcionAdm = new ScsInscripcionGuardiaAdm(usr);
			Vector<ScsInscripcionGuardiaBean> v = inscripcionAdm.selectByPKForUpdate(inscripcionHash);
			if (v != null && v.size() == 1) {
				ScsInscripcionGuardiaBean inscripcionBean = (ScsInscripcionGuardiaBean) v.get(0);
				inscripcionBean.setFechaBaja(fechaSolicitudBaja);
				inscripcionBean.setObservacionesBaja(observacionesSolicitudBaja);
				if (! inscripcionAdm.update(inscripcionBean)) {
					throw new ClsExceptions("Error al realizar la baja en la guardia");
				}
			}
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al solicitar la baja en la guardia");
		}
	} //solicitarBaja ()
	
	/*
	 * Este procedimiento no hace nada, llama directamente a validarBaja(),
	 * porque no existe la validacion de baja de guardia, todavia
	public void validarBaja (String fechaBaja, String observacionesBaja, UsrBean usr)
		throws ClsExceptions
	{
	} //validarBaja ()
	 */
	
	/**
	 * Cambia el ultimo letrado de la cola de la guardia indicada
	 * por el nuevo que se ha solicitado
	 * 
	 * @TODO Este metodo y tambien las comprobaciones de cola que se hacen desde esta misma clase
	 * podrian ir en una nueva clase de logica de negocio "ColaGuardia" 
	 */
	public void cambiarUltimoCola (Integer idInstitucion,
								   Integer idTurno,
								   Integer idGuardia,
								   Long idPersona,
								   UsrBean usr)
		throws ClsExceptions
	{
		String sql = 
			" update "+ScsGuardiasTurnoBean.T_NOMBRETABLA+" " +
			"    set "+ScsGuardiasTurnoBean.C_IDPERSONA_ULTIMO+" = "+(idPersona==null?"null":idPersona)+", " +
			"        "+ScsGuardiasTurnoBean.C_FECHAMODIFICACION+" = sysdate, " +
			"        "+ScsGuardiasTurnoBean.C_USUMODIFICACION+" = "+usr.getUserName()+" " +
			"  where "+ScsGuardiasTurnoBean.C_IDINSTITUCION+" = "+idInstitucion+" " +
			"    and "+ScsGuardiasTurnoBean.C_IDTURNO+" = "+idTurno+" " +
			"    and "+ScsGuardiasTurnoBean.C_IDGUARDIA+" = "+idGuardia+" ";
		
		if (! new ScsGuardiasTurnoAdm(usr).updateSQL(sql)) {
			throw new ClsExceptions("Error al cambiar ultimo letrado de cola de guardia");
		}
	} //cambiarUltimoCola()
	
}
