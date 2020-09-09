spool  01_SIGA-470_CEN_DATOSCOLEGIALESESTADO_AF.log
prompt 01_SIGA-470_CEN_DATOSCOLEGIALESESTADO_AF
select to_char(sysdate, 'hh24:mi:ss') as "Inicio" from dual;
prompt .

CREATE OR REPLACE TRIGGER CEN_DATOSCOLEGIALESESTADO_AF
AFTER Insert Or Update Or Delete
ON CEN_DATOSCOLEGIALESESTADO
Begin
  --Arreglo de campo calculado SITUACIONEJERCICIO
  Update Cen_Colegiado Col
     Set Col.Situacionejercicio = Nvl((Select Case Est.Idestado When 20 Then '1' Else '0' End
                                        From Cen_Datoscolegialesestado Est
                                       Where Est.Idpersona = Col.Idpersona
                                         And Est.Idinstitucion = Col.Idinstitucion
                                         And Est.Fechaestado =
                                             (Select Max(Fechaestado)
                                                From Cen_Datoscolegialesestado
                                               Where Idpersona = Est.Idpersona
                                                 And Idinstitucion = Est.Idinstitucion
                                                 And Trunc(Fechaestado) <= Trunc(Sysdate))),
                                      '0')
   Where Col.Situacionejercicio = '2';
  --Arreglo de campo calculado SITUACIONRESIDENTE
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
                                      '0')
   Where Col.Situacionresidente = '2';
End CEN_DATOSCOLEGIALESESTADO_AF;
/

prompt .
select to_char(sysdate, 'hh24:mi:ss') as "Fin" from dual;
spool off
