CREATE TRIGGER CEN_COLEGIADO_AU
AFTER Update
ON CEN_COLEGIADO
Begin
  --Arreglo de campo calculado SITUACIONEJERCICIO
  Update Cen_Colegiado Col
     Set Col.Situacionejercicio = Nvl((Select Case Est.Idestado When 20 Then '1' Else '0' End
                                        From CEN_DATOSCOLEGIALESESTADO Est
                                       Where Est.Idpersona = Col.Idpersona
                                         And Est.Idinstitucion = Col.Idinstitucion
                                         And Est.Fechaestado =
                                             (Select Max(Fechaestado)
                                                From CEN_DATOSCOLEGIALESESTADO
                                               Where Idpersona = Est.Idpersona
                                                 And Idinstitucion = Est.Idinstitucion
                                                 And Trunc(Fechaestado) <= Trunc(Sysdate))),
                                      '0')
   Where Col.Situacionejercicio = '2';
  --Arreglo de campo calculado SITUACIONRESIDENTE
  Update Cen_Colegiado Col
     Set Col.Situacionresidente = Nvl((Select Est.Situacionresidente
                                        From CEN_DATOSCOLEGIALESESTADO Est
                                       Where Est.Idpersona = Col.Idpersona
                                         And Est.Idinstitucion = Col.Idinstitucion
                                         And Est.Fechaestado =
                                             (Select Max(Fechaestado)
                                                From CEN_DATOSCOLEGIALESESTADO
                                               Where Idpersona = Est.Idpersona
                                                 And Idinstitucion = Est.Idinstitucion
                                                 And Trunc(Fechaestado) <= Trunc(Sysdate))),
                                      '0')
   Where Col.Situacionresidente = '2';
End CEN_COLEGIADO_AF;
/
