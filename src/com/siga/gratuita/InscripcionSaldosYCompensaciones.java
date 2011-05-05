package com.siga.gratuita;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenBajasTemporalesAdm;
import com.siga.beans.ScsGrupoGuardiaColegiadoAdm;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsInscripcionGuardiaAdm;
import com.siga.beans.ScsInscripcionGuardiaBean;
import com.siga.beans.ScsSaltosCompensacionesAdm;
import com.siga.beans.ScsSaltosCompensacionesBean;
import com.siga.beans.ScsInscripcionTurnoAdm;
import com.siga.beans.ScsOrdenacionColasAdm;
import com.siga.beans.ScsOrdenacionColasBean;
import com.siga.beans.ScsTurnoAdm;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.SaltosYCompensacionesForm;
import com.siga.gratuita.util.calendarioSJCS.LetradoInscripcion;

public class InscripcionSaldosYCompensaciones {
	
	
	////////////////////
	// ATRIBUTOS
	////////////////////
	private ScsSaltosCompensacionesBean bean = null;
	
	private static SimpleDateFormat formatoFecha = 
		new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
	
	
	////////////////////
	// CONSTRUCTORES
	////////////////////
	/**
	 * Constructor
	 */
	
	
	
	
	private InscripcionSaldosYCompensaciones (Integer idInstitucion,
			   Integer idTurno,
			   Integer idGuardia,
			   Long idPersona,
			   String saltoCompensacion)
	{
		this.bean = new ScsSaltosCompensacionesBean();
		this.bean.setIdInstitucion(idInstitucion);
		this.bean.setIdTurno(idTurno);
		this.bean.setIdGuardia(idGuardia);
		this.bean.setIdPersona(idPersona);
		this.bean.setSaltoCompensacion(saltoCompensacion);
	} //InscripcionSaldosYCompensaciones ()
	
	public void setBajas(String obsSolicitudBaja,String fechaSolicitudBaja,
			String fechaBaja,String observacionesValBaja){
		this.bean.setMotivos("Ha sido dado de baja en el Turno");
		this.bean.setFechaCumplimiento(fechaBaja);
		
	}
	
	
	private InscripcionSaldosYCompensaciones (ScsSaltosCompensacionesBean bean)
	{
		this.bean = bean;
	} //InscripcionGuardia ()
	
	
	////////////////////
	// GETTERS Y SETTERS
	////////////////////
	public ScsSaltosCompensacionesBean getBean() { return bean; }
	public void setBean(ScsSaltosCompensacionesBean bean) { this.bean = bean; }
	
	
	////////////////////
	// METODOS DE CLASE
	////////////////////
	/**
	 * Devuelve un objeto que representa la inscripcion de un colegiado a una guardia. 
	 * Este metodo comprueba si existe realmente la inscripcion. 
	 * Sin embargo, es posible evitar la comprobacion con el parametro comprobarQueExiste=false
	 */
	public static InscripcionSaldosYCompensaciones getInscripcionSaldosYCompensaciones (Integer idInstitucion,
															Integer idTurno,
															Integer idGuardia,
															Long idPersona,
															String fechaSolicitudAlta,
															UsrBean usr,
															boolean comprobarQueExiste)
		throws ClsExceptions 
	{
		InscripcionSaldosYCompensaciones inscripcion = null;
		
		if (! comprobarQueExiste){
			inscripcion = new InscripcionSaldosYCompensaciones (
					idInstitucion, idTurno, idGuardia, idPersona, fechaSolicitudAlta);
		}
		else {
			//preparando datos
			Hashtable<String, Object> hash = new Hashtable<String, Object>();
			hash.put(ScsSaltosCompensacionesBean.C_IDINSTITUCION, idInstitucion);
			hash.put(ScsSaltosCompensacionesBean.C_IDPERSONA, idPersona);
			hash.put(ScsSaltosCompensacionesBean.C_IDTURNO, idTurno);
			hash.put(ScsSaltosCompensacionesBean.C_IDGUARDIA, idGuardia);
			if(fechaSolicitudAlta.equalsIgnoreCase("sysdate"))
				hash.put(ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO, fechaSolicitudAlta);
			else
				hash.put(ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO, formatoFecha.format(fechaSolicitudAlta));
			
			//obteniendo datos
			Vector<ScsSaltosCompensacionesBean> inscripciones = new ScsInscripcionGuardiaAdm(usr).select(hash);
			if (inscripciones == null)
				throw new ClsExceptions("Error: no se ha encontrado la inscripcion a la guardia");
			if (inscripciones.size() < 1)
				throw new ClsExceptions("Error: no se ha encontrado la inscripcion a la guardia");
			if (inscripciones.size() > 1)
				throw new ClsExceptions("Error: se ha encontrado mas de una inscripcion a la guardia");
			
			inscripcion = new InscripcionSaldosYCompensaciones ((ScsSaltosCompensacionesBean) inscripciones.get(0));
		}
		
		return inscripcion;
	} //getSaldosYCompensaciones ()
	
	
	


	

}
