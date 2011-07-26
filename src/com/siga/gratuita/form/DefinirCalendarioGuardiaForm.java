package com.siga.gratuita.form;

import java.util.List;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.siga.administracion.SIGAConstants;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.MasterForm;
import com.siga.tlds.FilaExtElement;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Metodos set y get del formulario del caso de uso de Definir Calendario de Guardias. 
 * 
 * @author david.sanchezp
 * @since 19/1/2005 
 * @version 1.0
 */
public class DefinirCalendarioGuardiaForm extends MasterForm
{
	// Atributos
	// this.datos
	DefinirPermutaGuardiasForm permutaGuardias;


	// Setters
	public void setIdInstitucionPestanha (String valor)	{ this.datos.put("IDINSTITUCIONPESTAÑA",valor); 	}
	public void setIdTurnoPestanha (String valor)		{ this.datos.put("IDTURNOPESTAÑA",valor); 	 		}
	public void setIdGuardiaPestanha (String valor)		{ this.datos.put("IDGUARDIAPESTAÑA",valor); 	 	} 
	public void setIdCalendarioGuardias (String valor)	{ this.datos.put("IDCALENDARIOGUARDIAS",valor); 	}
	public void setIdInstitucion (String valor)			{ this.datos.put("IDINSTITUCION",valor); 			}
	public void setIdTurno (String valor)				{ this.datos.put("IDTURNO",valor); 	 				}
	public void setIdGuardia(String valor)				{ this.datos.put("IDGUARDIA",valor); 	 			} 
	public void setIdPersona(String valor)				{ this.datos.put("IDPERSONA",valor); 	 			}
	public void setAccion (String valor)				{ this.datos.put("ACCION",valor); 	 				}
	public void setFechaInicio (String valor)			{ this.datos.put("FECHAINICIO",valor); 	 			}
	public void setFechaFin (String valor)				{ this.datos.put("FECHAFIN",valor); 	 			}
	public void setObservaciones (String valor)			{ this.datos.put("OBSERVACIONES",valor); 	 		}
	public void setReserva (String valor)				{ this.datos.put("RESERVA",valor); 	 				}
	public void setDiasGuardia (String valor)			{ this.datos.put("DIASGUARDIA",valor); 	 			}
	public void setTipoDias (String valor)				{ this.datos.put("TIPODIAS",valor); 	 			}
	public void setDiasACobrar (String valor)			{ this.datos.put("DIASACOBRAR",valor); 	 			}
	public void setContador (String valor)				{ this.datos.put("CONTADOR",valor); 	 			}
	public void setFechaDesde (String valor)			{ this.datos.put("FECHADESDE",valor); 	 			}
	public void setFechaHasta (String valor)			{ this.datos.put("FECHAHASTA",valor); 	 			}
	public void setNumeroLetrados (String valor)		{ this.datos.put("NUMEROLETRADOS",valor); 	 		}
	public void setNumeroSustitutos (String valor)		{ this.datos.put("NUMEROSUSTITUTOS",valor); 	 	}
	public void setModoOriginal (String valor)			{ this.datos.put("MODOORIGINAL",valor); 	 		}
	public void setFechaApertura (String valor)			{ this.datos.put("FECHAAPERTURA",valor); 	 		}
	public void setNColegiado (String valor)			{ this.datos.put("NCOLEGIADO",valor); 	 			}
	public void setFlagSalto (String valor)				{ this.datos.put("FLAGSALTO",valor); 	 			}
	public void setFlagCompensacion (String valor)		{ this.datos.put("FLAGCOMPENSACION",valor); 	 	}
	public void setPeriodos (String valor)				{ this.datos.put("PERIODOS",valor); 	 			}
	public void setIndicePeriodo (String valor)			{ this.datos.put("INDICEPERIODO",valor); 	 		}
	public void setGuardiasPeriodo (String valor)		{ this.datos.put("GUARDIASPERIODO",valor); 	 		}
	public void setBuscarFechaDesde (String d) 			{UtilidadesHash.set(datos, "FECHA_DESDE", d);		}
	public void setBuscarFechaHasta (String  d) 		{UtilidadesHash.set(datos, "FECHA_HASTA", d); 		}
	public void setBuscarColegiado (String d) 			{UtilidadesHash.set(datos, "BUSCARCOLEGIADO", d);	}
	public void setMotivosSolicitante (String valor)	{ this.datos.put("MOTIVOSSOLICITANTE",valor);		}	
	public void setNombrePermutado (String valor)		{ this.datos.put("NOMBREPERMUTADO",valor);			}
	public void setNumeroPermutado (String valor)		{ this.datos.put("NCOLEGIADOSPERMUTADO",valor);		}
	public void setFechaConfirmacion (String valor)		{ this.datos.put("FECHACONFIRMADOR",valor);			}
	public void setNombreSolicitante (String valor)		{ this.datos.put("NOMBRESOLICITANTE",valor);		}
	public void setNcolegiadoSolicitante(String valor)	{ this.datos.put("NCOLEGIADOSOLICITANTE",valor); 	}
	public void setNombreSustituto(String valor)		{ this.datos.put("NOMBRESUSTITUTO",valor);			}
	public void setNcolegiadoSustituto(String valor)	{ this.datos.put("NCOLEGIADOSUSTITUTO",valor);		}
	public void setComentarioSustituto(String valor)	{ this.datos.put("COMENTARIOSUSTITUTO",valor);		}
	public void setFechaSustituto(String valor)			{ this.datos.put("FECHASUSTITUTO",valor);			}
	public void setPeriodoPermuta(String valor)			{ this.datos.put("PERIODOPERMUTA",valor);			}
	public void setDatosPersPermutada(String valor)		{ this.datos.put("DATOSPERSPERMUTADA",valor);		}
	public void setDatosPersSustituto(String valor)		{ this.datos.put("DATOSPERSSUSTITUTO",valor);		}
	public void setDatosPersSolicitante(String valor)	{ this.datos.put("DATOSPERSSOLICITANTE",valor); 	}
	public void setFechaSolicitud(String valor)			{ this.datos.put("FECHASOLICITUD",valor);			}
	public void setFlagConfirmacion(String valor)		{ this.datos.put("FLAGCONFIRMACION", valor);		}
	public void setPermutaGuardias(DefinirPermutaGuardiasForm valor) {this.permutaGuardias = valor;}


	// Getters
	public String getIdInstitucionPestanha ()	{ return ((String)this.datos.get("IDINSTITUCIONPESTAÑA")); 	}
	public String getIdGuardiaPestanha ()		{ return ((String)this.datos.get("IDGUARDIAPESTAÑA")); 		}
	public String getIdTurnoPestanha ()			{ return ((String)this.datos.get("IDTURNOPESTAÑA")); 		}
	public String getIdCalendarioGuardias ()	{ return ((String)this.datos.get("IDCALENDARIOGUARDIAS")); 	}
	public String getIdInstitucion ()			{ return ((String)this.datos.get("IDINSTITUCION")); 		}
	public String getIdGuardia ()				{ return ((String)this.datos.get("IDGUARDIA")); 			}
	public String getIdTurno ()					{ return ((String)this.datos.get("IDTURNO")); 				}
	public String getIdPersona ()				{ return ((String)this.datos.get("IDPERSONA")); 			}
	public String getAccion ()					{ return ((String)this.datos.get("ACCION")); 				}
	public String getFechaInicio ()				{ return ((String)this.datos.get("FECHAINICIO")); 			}
	public String getFechaFin ()				{ return ((String)this.datos.get("FECHAFIN")); 				}
	public String getObservaciones ()			{ return ((String)this.datos.get("OBSERVACIONES")); 		}
	public String getReserva ()					{ return ((String)this.datos.get("RESERVA")); 				}
	public String getDiasGuardia ()				{ return ((String)this.datos.get("DIASGUARDIA")); 			}
	public String getTipoDias ()				{ return ((String)this.datos.get("TIPODIAS")); 				}
	public String getDiasACobrar ()				{ return ((String)this.datos.get("DIASACOBRAR")); 			}
	public String getContador ()				{ return ((String)this.datos.get("CONTADOR")); 				}
	public String getFechaDesde ()				{ return ((String)this.datos.get("FECHADESDE")); 			}
	public String getFechaHasta ()				{ return ((String)this.datos.get("FECHAHASTA")); 			}
	public String getNumeroLetrados ()			{ return ((String)this.datos.get("NUMEROLETRADOS")); 		}
	public String getNumeroSustitutos ()		{ return ((String)this.datos.get("NUMEROSUSTITUTOS")); 		}
	public String getModoOriginal ()			{ return ((String)this.datos.get("MODOORIGINAL")); 			} 	
	public String getFechaApertura ()			{ return ((String)this.datos.get("FECHAAPERTURA")); 		}
	public String getNColegiado ()				{ return ((String)this.datos.get("NCOLEGIADO")); 			}
	public String getFlagSalto ()				{ return ((String)this.datos.get("FLAGSALTO")); 			}
	public String getFlagCompensacion ()		{ return ((String)this.datos.get("FLAGCOMPENSACION")); 		}
	public String getPeriodos ()				{ return ((String)this.datos.get("PERIODOS")); 				}
	public String getIndicePeriodo ()			{ return ((String)this.datos.get("INDICEPERIODO")); 		}
	public String getGuardiasPeriodo ()			{ return ((String)this.datos.get("GUARDIASPERIODO")); 		}
	public String getBuscarFechaDesde () 		{ return UtilidadesHash.getString(datos, "FECHA_DESDE");	}
	public String getBuscarFechaHasta () 		{ return UtilidadesHash.getString(datos, "FECHA_HASTA");	}
	public String getBuscarColegiado () 		{ return UtilidadesHash.getString(datos, "BUSCARCOLEGIADO");}
	public String getMotivosSolicitante ()		{ return ((String)this.datos.get("MOTIVOSSOLICITANTE")); 	}
	public String getNombrePermutado ()			{ return ((String)this.datos.get("NOMBREPERMUTADO"));		}
	public String getNumeroPermutado ()			{ return ((String)this.datos.get("NCOLEGIADOSPERMUTADO"));	}
	public String getFechaConfirmacion ()		{ return ((String)this.datos.get("FECHACONFIRMADOR"));		}
	public String getNombreSolicitante ()		{ return ((String)this.datos.get("NOMBRESOLICITANTE"));		}
	public String getNcolegiadoSolicitante ()	{ return ((String)this.datos.get("NCOLEGIADOSOLICITANTE"));	}
	public String getNombreSustituto ()			{ return ((String)this.datos.get("NOMBRESUSTITUTO"));		}
	public String getNcolegiadoSustituto ()		{ return ((String)this.datos.get("NCOLEGIADOSUSTITUTO"));	}
	public String getComentarioSustituto ()		{ return ((String)this.datos.get("COMENTARIOSUSTITUTO"));	}
	public String getFechaSustituto ()			{ return ((String)this.datos.get("FECHASUSTITUTO"));		}
	public String getPeriodoPermuta ()			{ return ((String)this.datos.get("PERIODOPERMUTA"));		}
	public String getDatosPersPermutada ()		{ return ((String)this.datos.get("DATOSPERSPERMUTADA"));	}
	public String getDatosPersSustituto ()		{ return ((String)this.datos.get("DATOSPERSSUSTITUTO"));	}
	public String getDatosPersSolicitante ()	{ return ((String)this.datos.get("DATOSPERSSOLICITANTE"));	}
	public String getFechaSolicitud ()			{ return ((String)this.datos.get("FECHASOLICITUD"));		}
	public String getFlagConfirmacion()			{ return ((String)this.datos.get("FLAGCONFIRMACION"));		}
	public DefinirPermutaGuardiasForm getPermutaGuardias() {return permutaGuardias;}
	private DefinirTurnosForm turno;
	private DefinirGuardiasTurnosForm guardia;
	private String estado;
	String idTurnoCalendario ;
	String idGuardiaCalendario ;
	List<ScsTurnoBean> turnos;
	List<ScsGuardiasTurnoBean> guardias;
	private String botones;
	private FilaExtElement[] elementosFila;

	public DefinirTurnosForm getTurno() {
		return turno;
	}
	public void setTurno(DefinirTurnosForm turno) {
		this.turno = turno;
	}
	public DefinirGuardiasTurnosForm getGuardia() {
		return guardia;
	}
	public void setGuardia(DefinirGuardiasTurnosForm guardia) {
		this.guardia = guardia;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<ScsTurnoBean> getTurnos() {
		return turnos;
	}
	public void setTurnos(List<ScsTurnoBean> turnos) {
		this.turnos = turnos;
	}
	public List<ScsGuardiasTurnoBean> getGuardias() {
		return guardias;
	}
	public void setGuardias(List<ScsGuardiasTurnoBean> guardias) {
		this.guardias = guardias;
	}
	public String getIdTurnoCalendario() {
		return idTurnoCalendario;
	}
	public void setIdTurnoCalendario(String idTurnoCalendario) {
		this.idTurnoCalendario = idTurnoCalendario;
	}
	public String getIdGuardiaCalendario() {
		return idGuardiaCalendario;
	}
	public void setIdGuardiaCalendario(String idGuardiaCalendario) {
		this.idGuardiaCalendario = idGuardiaCalendario;
	}
	public String  getFechaInicioTxt() {
		String fechaInicioTxt = "";
		try {
			String fechaInicio = getFechaInicio();
			if(fechaInicio!=null && !fechaInicio.equals(""))
				fechaInicioTxt = GstDate.getFormatedDateShort("",  fechaInicio);
		} catch (ClsExceptions e) {
			
		}
		return fechaInicioTxt;
	}
	public String  getFechaFinTxt() {
		String fechaFinTxt = "";
		try {
			String fechaFin = getFechaFin();
			if(fechaFin!=null && !fechaFin.equals(""))
				fechaFinTxt = GstDate.getFormatedDateShort("",  fechaFin);
		} catch (ClsExceptions e) {
			
		}
		return fechaFinTxt;
	}
//	static public final String estadoProgramado = "0";
//	static public final String estadoProcesando = "1";
//	static public final String estadoError = "2";
//	static public final String estadoGenerado = "3";
//    static public final String estadoCancelado = "4";
//    static public final String estadoPteManual = "5";
	
	public String getBotones() {
//		<html:option value="0">Programada</html:option>
//		<html:option value="1">En proceso</html:option>
//		<html:option value="2">Generada con Errores</html:option>
//		<html:option value="3">Generada</html:option>
		switch (Integer.parseInt(estado)) {
		
		case 3:case 5:
			botones  = "C,E,B";
			break;
		
		default:
			botones="";
			break;
		}
		return botones;
	}
	public FilaExtElement[] getElementosFila() {
		return elementosFila;
	}
	public void setElementosFila(FilaExtElement[] elementosFila) {
		this.elementosFila = elementosFila;
	}
	
	

}