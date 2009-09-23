/*
 * Created on Feb 23, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.general;

/**
 * @author david.sanchezp
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SIGAExcOk extends SIGAException {

	/**
	 * @param _literal
	 */
	public SIGAExcOk(String _literal) {
		super(_literal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param _literal
	 * @param reem
	 */
	public SIGAExcOk(String _literal, String[] reem) {
		super(_literal, reem);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param e
	 * @param reem
	 */
	public SIGAExcOk(Exception e, String[] reem) {
		super(e, reem);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param e
	 */
	public SIGAExcOk(Exception e) {
		super(e);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param _literal
	 * @param e
	 */
	public SIGAExcOk(String _literal, Exception e) {
		super(_literal, e);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param _literal
	 * @param e
	 * @param reem
	 */
	public SIGAExcOk(String _literal, Exception e, String[] reem) {
		super(_literal, e, reem);
		// TODO Auto-generated constructor stub
	}
}
