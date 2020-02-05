CREATE OR REPLACE TRIGGER CEN_COLEGIADO_AI
AFTER INSERT
ON CEN_COLEGIADO

Begin
  --Arreglo de campo calculado OTROSCOLEGIOS para todos los colegiados
  Update Cen_Colegiado Col
     Set Otroscolegios = 0
   Where Otroscolegios Is Null;
  Update Cen_Colegiado Col
     Set Otroscolegios = 0
   Where Otroscolegios = 1
     And Not Exists
   (Select *
            From Cen_Colegiado Col2
           Where Col2.Idpersona = Col.Idpersona
             And Col2.Idinstitucion <> Col.Idinstitucion);
  Update Cen_Colegiado Col
     Set Otroscolegios = 1
   Where Otroscolegios = 0
     And Exists
   (Select *
            From Cen_Colegiado Col2
           Where Col2.Idpersona = Col.Idpersona
             And Col2.Idinstitucion <> Col.Idinstitucion);

  --Arreglo de caracter privado para el colegiado actual
  Update Cen_Cliente Cli
     Set Caracter = 'A'
   Where Caracter = 'P'
     And Exists (Select *
            From Cen_Colegiado Col
           Where Col.Idpersona = Cli.Idpersona
             And Col.Idinstitucion = Cli.Idinstitucion);
End Cen_Colegiado_Ai;
/
