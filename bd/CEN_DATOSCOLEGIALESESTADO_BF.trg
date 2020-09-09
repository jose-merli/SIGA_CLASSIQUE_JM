spool  02_SIGA-470_CEN_DATOSCOLEGIALESESTADO_BF.log
prompt 02_SIGA-470_CEN_DATOSCOLEGIALESESTADO_BF
select to_char(sysdate, 'hh24:mi:ss') as "Inicio" from dual;
prompt .

Create Or Replace Trigger CEN_DATOSCOLEGIALESESTADO_BF
  Before Insert Or Update Or Delete On Cen_Datoscolegialesestado
  For Each Row
Begin
  --Guardando la persona actualizada para el siguiente trigger
  If (:New.Idinstitucion Is Null) Then
    Update Cen_Colegiado
       Set Situacionejercicio = 2, Situacionresidente = 2
     Where Idinstitucion = :Old.Idinstitucion
       And Idpersona = :Old.Idpersona;
  Else
    Update Cen_Colegiado
       Set Situacionejercicio = 2, Situacionresidente = 2
     Where Idinstitucion = :New.Idinstitucion
       And Idpersona = :New.Idpersona;
  End If;
End CEN_DATOSCOLEGIALESESTADO_BF;
/

prompt .
select to_char(sysdate, 'hh24:mi:ss') as "Fin" from dual;
spool off
