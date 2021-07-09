CREATE Trigger CEN_COLEGIADO_BU
  Before Update ON CEN_COLEGIADO
  For Each Row
DECLARE 
  v_ejercicio varchar2(1);
  v_residente varchar2(1);
Begin
  --Arreglo de campo calculado SITUACIONEJERCICIO
  select Nvl((Select Case Est.Idestado When 20 Then '1' Else '0' End
                From CEN_DATOSCOLEGIALESESTADO Est
               Where Est.Idpersona = :NEW.Idpersona
                 And Est.Idinstitucion = :NEW.Idinstitucion
                 And Est.Fechaestado =
                     (Select Max(Fechaestado)
                        From CEN_DATOSCOLEGIALESESTADO
                       Where Idpersona = Est.Idpersona
                         And Idinstitucion = Est.Idinstitucion
                         And Trunc(Fechaestado) <= Trunc(Sysdate))),
              '0') into v_ejercicio from dual;
  :NEW.Situacionejercicio := v_ejercicio;
  --Arreglo de campo calculado SITUACIONRESIDENTE
  select Nvl((Select Est.Situacionresidente
                From CEN_DATOSCOLEGIALESESTADO Est
               Where Est.Idpersona = :NEW.Idpersona
                 And Est.Idinstitucion = :NEW.Idinstitucion
                 And Est.Fechaestado =
                     (Select Max(Fechaestado)
                        From CEN_DATOSCOLEGIALESESTADO
                       Where Idpersona = Est.Idpersona
                         And Idinstitucion = Est.Idinstitucion
                         And Trunc(Fechaestado) <= Trunc(Sysdate))),
              '0') into v_residente from dual;
  :NEW.Situacionresidente := v_residente;
End CEN_COLEGIADO_BU;
/