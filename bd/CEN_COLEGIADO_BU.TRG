CREATE Trigger CEN_COLEGIADO_BU
  Before Update ON CEN_COLEGIADO
  For Each Row
Begin
  --Guardando la persona actualizada para el siguiente trigger
  Update Cen_Colegiado
     Set Situacionejercicio = 2, Situacionresidente = 2
   Where Idinstitucion = :New.Idinstitucion
     And Idpersona = :New.Idpersona;
End CEN_COLEGIADO_BF;
/
