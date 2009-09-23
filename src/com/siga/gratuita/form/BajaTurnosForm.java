package com.siga.gratuita.form;

/**
 * @author carlos.vidal
 */
import com.siga.general.MasterForm;

public class BajaTurnosForm extends MasterForm {
	//Campos para la tabla de turnos
	Integer IDINSTITUCION = null;
	Integer IDPERSONA = null;
	Integer IDTURNO = null;
	String  FECHASOLICITUD = null;
	String  OSBSERVACIONESSOLICITUD = null;
	String  FECHAVALIDACION = null;
	String  OSBSERVACIONESVALIDACION = null;
	String  FECHABAJA = null;
	String  FECHASOLICITUDBAJA = null;
	String  OSBSERVACIONESBAJA = null;
	String  PASO = null;
	String  confirmacion = "";

	public void setIdInstitucion			(Integer idinstitucion)	{ IDINSTITUCION = idinstitucion;	}	
	public void setIdPersona				(Integer idpersona)		{ IDPERSONA = idpersona;	}	
	public void setIdTurno		 			(Integer idturno)	 	{ IDTURNO = idturno;	}
	public void setFechaSolicitud			(String fechasolicitud)	{ FECHASOLICITUD = fechasolicitud;	}
	public void setObservacionesSolicitud	(String observacionessolicitud) { OSBSERVACIONESSOLICITUD = observacionessolicitud;	}
	public void setFechaBaja				(String fechabaja)		{ FECHABAJA = fechabaja;	}
	public void setFechaSolicitudBaja		(String fechasolicitudbaja)		{ FECHASOLICITUDBAJA = fechasolicitudbaja;	}
	public void setObservacionesBaja		(String observacionesbaja) { OSBSERVACIONESBAJA = observacionesbaja;	}
	public void setFechaValidacion			(String fechavalidacion)		{ FECHAVALIDACION = fechavalidacion;	}
	public void setObservacionesValidacion	(String observacionesvalidacion) { OSBSERVACIONESVALIDACION = observacionesvalidacion;	}
	public void setPaso						(String paso)			{ PASO = paso;	}	

	public Integer	  getIdInstitucion() 		{ return IDINSTITUCION; }
	public Integer 	  getIdPersona() 			{ return IDPERSONA; }	
	public Integer    getIdTurno() 				{ return IDTURNO; }	
	public String 	  getFechaSolicitud() 		{ return FECHASOLICITUD; }
	public String 	  getObservacionesSolicitud() 	{ return OSBSERVACIONESSOLICITUD; }
	public String 	  getFechaValidacion()	 		{ return FECHAVALIDACION; }
	public String 	  getObservacionesValidacion() 	{ return OSBSERVACIONESVALIDACION; }
	public String 	  getFechaBaja()	 		{ return FECHABAJA; }
	public String 	  getFechaSolicitudBaja()	 		{ return FECHASOLICITUDBAJA; }
	public String 	  getObservacionesBaja() 	{ return OSBSERVACIONESBAJA; }
	public String 	  getPaso() 				{ return PASO; }
	
	
	public String getConfirmacion() {
		return confirmacion;
	}
	public void setConfirmacion(String confirmacion) {
		this.confirmacion = confirmacion;
	}
}