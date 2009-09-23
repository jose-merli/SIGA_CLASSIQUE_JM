package es.satec.siga.util.formbeans.censo;

import java.util.ArrayList;
import java.util.Date;

import es.satec.siga.util.formbeans.PagedBean;
import es.satec.siga.util.vos.impl.LetradoVo;

public class BusquedaBean extends PagedBean {
	private static final long serialVersionUID = -6574796519712502337L;

	private Boolean busquedaExacta;
	private String residente;
	private Long idPersona;
	private String nifCif;
	private String nombre;
	private String apellidos1;
	private String apellidos2;
	private Date fechaNacimiento;
	private Integer idInstitucion;
	private Boolean noAparecerRedAbogacia;
	
	public BusquedaBean(){
		super();

		columnTranslation=new ArrayList();
		columnTranslation.add("nombre");
		columnTranslation.add("apellidos1");
		columnTranslation.add("apellidos2");
		columnTranslation.add("nifCif");
		columnTranslation.add("fechaNacimiento");
		columnTranslation.add("idInstitucion");
	}

	public Boolean getBusquedaExacta() {
		return busquedaExacta;
	}

	public void setBusquedaExacta(Boolean busquedaExacta) {
		this.busquedaExacta = busquedaExacta;
	}

	public String getResidente() {
		return residente;
	}

	public void setResidente(String residente) {
		this.residente = residente;
	}

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public String getNifCif() {
		return nifCif;
	}

	public void setNifCif(String nifCif) {
		this.nifCif = nifCif;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos1() {
		return apellidos1;
	}

	public void setApellidos1(String apellidos1) {
		this.apellidos1 = apellidos1;
	}

	public String getApellidos2() {
		return apellidos2;
	}

	public void setApellidos2(String apellidos2) {
		this.apellidos2 = apellidos2;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public Integer getIdInstitucion() {
		return idInstitucion;
	}

	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}

	public Boolean getNoAparecerRedAbogacia() {
		return noAparecerRedAbogacia;
	}

	public void setNoAparecerRedAbogacia(Boolean noAparecerRedAbogacia) {
		this.noAparecerRedAbogacia = noAparecerRedAbogacia;
	}

	public LetradoVo getLetradoVo() {
		LetradoVo letrado=new LetradoVo();
		
		letrado.setNombre(nombre);
		letrado.setApellidos1(apellidos1);
		letrado.setApellidos2(apellidos2);
		letrado.setFechaNacimiento(fechaNacimiento);
		letrado.setNifCif(nifCif);
		
		return letrado;
	}
}
