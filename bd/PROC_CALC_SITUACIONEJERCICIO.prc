spool  03_SIGA-470_PROC_CALC_SITUACIONEJERCICIO.log
prompt 03_SIGA-470_PROC_CALC_SITUACIONEJERCICIO
select to_char(sysdate, 'hh24:mi:ss') as "Inicio" from dual;
prompt .

Create Or Replace Procedure PROC_CALC_SITUACIONEJERCICIO(p_Codretorno Out Varchar2,
                                                         p_Datoserror Out Varchar2) Is
  v_Datoserror Varchar2(4000);
Begin
  Update Cen_Colegiado Col
     Set Col.Situacionejercicio = Nvl((Select Decode(Est.Idestado, 20, '1', '0')
                                        From Cen_Datoscolegialesestado Est
                                       Where Est.Idpersona = Col.Idpersona
                                         And Est.Idinstitucion = Col.Idinstitucion
                                         And Est.Fechaestado =
                                             (Select Max(Fechaestado)
                                                From Cen_Datoscolegialesestado
                                               Where Idpersona = Est.Idpersona
                                                 And Idinstitucion = Est.Idinstitucion
                                                 And Trunc(Fechaestado) <= Trunc(Sysdate))),
                                      '0');
  Update Cen_Colegiado Col
     Set Col.Situacionresidente = Nvl((Select Est.Situacionresidente
                                        From Cen_Datoscolegialesestado Est
                                       Where Est.Idpersona = Col.Idpersona
                                         And Est.Idinstitucion = Col.Idinstitucion
                                         And Est.Fechaestado =
                                             (Select Max(Fechaestado)
                                                From Cen_Datoscolegialesestado
                                               Where Idpersona = Est.Idpersona
                                                 And Idinstitucion = Est.Idinstitucion
                                                 And Trunc(Fechaestado) <= Trunc(Sysdate))),
                                      '0');
  v_Datoserror := 'Fin Correcto';
  p_Codretorno := To_Char(0);
  p_Datoserror := v_Datoserror;

Exception
  When Others Then
    p_Codretorno := To_Char(Sqlcode);
    p_Datoserror := v_Datoserror || '- ' || Sqlerrm;
End Proc_Calc_Situacionejercicio;
/

prompt .
select to_char(sysdate, 'hh24:mi:ss') as "Fin" from dual;
spool off
