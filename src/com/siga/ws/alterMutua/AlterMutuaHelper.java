/**
 * Colegios.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.siga.ws.alterMutua;

public class AlterMutuaHelper implements java.io.Serializable {
	
	public static final String CATALOGO = "ALTERMUTUA";
	
	public static final String CONJUNTO_PARENTESCO = "PARENTESCO";
	public static final String CONJUNTO_TIPOPROPUESTA = "TIPOPROPUESTA";
	public static final String CONJUNTO_TIPOEJERCICIO = "TIPOEJERCICIO";
	public static final String CONJUNTO_IDIOMA = "IDIOMA";
	public static final String CONJUNTO_TIPOCOMUNICACION = "TIPOCOMUNICACION";
	public static final String CONJUNTO_TIPOIDENTIFICADOR = "TIPOIDENTIFICADOR";
	public static final String CONJUNTO_TIPODIRECCION = "TIPODIRECCION";
	public static final String CONJUNTO_SEXO = "SEXO";
	public static final String CONJUNTO_ESTADOCIVIL = "ESTADOCIVIL";

	
	public class TipoPropuesta{
		public static final int Alternativa = 1;
		public static final int General = 2;
		public static final int Oferta = 3;
	}
	
	private class TipoComunicacion{
		private static final int Telefono = 1;
		private static final int CorreoElectronico = 2;
		private static final int Carta = 3;
	}
	
	private class TipoDireccion{
		private static final int Residencia = 1;
		private static final int Despacho = 2;
	}
	
	private class TipoIdentificador{
		private static final int NIF = 0;
		private static final int Pasaporte = 1;
		private static final int NIE = 2;
		private static final int PermisoResidencia = 3;
	}

	private class TipoSexo{
		private static final int Hombre = 1;
		private static final int Mujer = 2;
	}
	
	private class EstadoCivil{
		private static final int NoValido = 0;
		private static final int Casado = 1;
		private static final int Soltero = 2;
		private static final int Separado = 3;
		private static final int Viudo = 4;
	}

	private class TipoEjercicio{
		private static final int NoEjerciente = 0;
		private static final int EjercientePropia = 1;
		private static final int EjercienteAjena = 3;
	}
		
	private class Idioma{
		private static final int Castellano = 1;
		private static final int Catalan = 2;
	}
	
	public static int getTipoComunicacionAM(String comunicacion){
		try{
			int intComunicacion =Integer.parseInt(comunicacion);
			switch (intComunicacion) {
				case 1: return TipoComunicacion.Carta;
				case 3: return TipoComunicacion.Telefono;
				default: return TipoComunicacion.CorreoElectronico;
			}
		}catch (Exception e) {
			return TipoComunicacion.CorreoElectronico;
		}
	}
	
	public static int getTipoDireccion(String direccion){
		try{
			int intDireccion =Integer.parseInt(direccion);
			switch (intDireccion) {
				case 1: return TipoDireccion.Despacho;
				default: return TipoDireccion.Residencia;
			}
		}catch (Exception e) {
			return TipoDireccion.Residencia;
		}
	}
	
	public static int getTipoEjercicioAM(String tipoEjercicio) {
		try{
			int intEjercicio =Integer.parseInt(tipoEjercicio);
			switch (intEjercicio) {
				case 10: return TipoEjercicio.EjercientePropia;
				case 30: return TipoEjercicio.EjercientePropia;
				// En siga no tenemos cuenta ajena 
				default: return TipoEjercicio.NoEjerciente;
			}
		}catch (Exception e) {
			return TipoEjercicio.NoEjerciente;
		}
	}
	
	public static int getEstadoEjercicioAM(String tipoEjercicio) {
		try{
			int intEjercicio =Integer.parseInt(tipoEjercicio);
			switch (intEjercicio) {
				case 20: return TipoEjercicio.EjercientePropia;
				// En siga no tenemos cuenta ajena 
				default: return TipoEjercicio.NoEjerciente;
			}
		}catch (Exception e) {
			return TipoEjercicio.NoEjerciente;
		}
	}

	// Convierte el idioma de SIGA en el idioma de Alter Mutua
	public static int getIdiomaAM(String idioma) {
		try{
			int intIdioma =Integer.parseInt(idioma);
			switch (intIdioma) {
				case 1: return Idioma.Castellano;
				case 2: return Idioma.Catalan;
				default: return Idioma.Castellano;
			}
		}catch (Exception e) {
			return Idioma.Castellano;
		}
	}

	// Convierte el estado civil de SIGA en el estado civil de Alter Mutua
	public static int getEstadoCivilAM(String estadoCivil) {
		try{
			int intEstado =Integer.parseInt(estadoCivil);
			switch (intEstado) {
				case 1: return EstadoCivil.Casado;
				case 2: return EstadoCivil.Soltero;
				case 3: return EstadoCivil.Viudo;
				case 4: return EstadoCivil.Separado;
				default: return EstadoCivil.NoValido;
			}
		}catch (Exception e) {
			return EstadoCivil.NoValido;
		}
	}

	// Convierte el sexo de SIGA en sexo de Alter Mutua
	public static int getSexoAM(String sexo) {
		try{
			switch (sexo.charAt(0)) {
				case 'H': return TipoSexo.Hombre;
				case 'M': return TipoSexo.Mujer;
				default: return TipoSexo.Hombre;
			}
		}catch (Exception e) {
			return TipoSexo.Hombre;
		}
	}

	// Convierte el tipo de identificacion del SIGA al de Alter Mutua
	public static int getTipoIdentificacionAM(String tipoIdentificacion) {
		try{
			int intIdent =Integer.parseInt(tipoIdentificacion); 
			switch (intIdent) {
				case 10: return TipoIdentificador.NIF;
				// case 20: return TipoIdentificador.CIF;
				case 30: return TipoIdentificador.Pasaporte;
				case 40: return TipoIdentificador.NIE;
				// case 50: return TipoIdentificador.Otro;
				default: return TipoIdentificador.NIF;
			}
		}catch (Exception e) {
			return -1;
		}
	}

}
