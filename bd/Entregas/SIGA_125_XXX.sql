Update Adm_Gestorinterfaz ges Set ges.Color = '0' Where ges.Color <> '0';
Update Gen_Procesos Set Descripcion = 'HIDDEN_' || Descripcion Where Idproceso = '80' and Descripcion not like 'HIDDEN%';
Delete Adm_Tiposacceso acc Where acc.Idproceso = '80' And acc.Idperfil <> 'ADG';

-- Ejecutado en Integracion por AAG el 17/01 a las 10:30

Declare
  Cursor c_Aux Is
    Select Distinct Idinstitucion, Idperfil
      From Adm_Tiposacceso sub
     Where Idproceso In (Select Men.Idproceso
                           From Gen_Menu Men
                          Where Idmenu In ('7', '17', '19', '15E', '15F', '99E'))
       And Derechoacceso > 1
       And Not Exists (Select 1
              From Adm_Tiposacceso super
             Where sub.Idperfil = super.Idperfil
               And sub.Idinstitucion = super.Idinstitucion
               And super.Idproceso = '180');
  n_permisosanyadidos Number;

Begin
  n_permisosanyadidos := 0;
  For Rec In c_Aux Loop
    Begin
      Insert Into Adm_Tiposacceso
        (Idproceso, Idperfil, Fechamodificacion, Usumodificacion, Derechoacceso, Idinstitucion)
      Values
        ('180', Rec.Idperfil, Sysdate, 0, 3, Rec.Idinstitucion);
      n_permisosanyadidos := n_permisosanyadidos + sql%rowcount;
      Dbms_Output.Put_Line('Insertado: ' || Rec.Idperfil || ', ' || Rec.Idinstitucion);
    
    Exception
      When Others Then
        Dbms_Output.Put_Line('Error=' || Sqlerrm);
        Rollback;
    End;
  End Loop;
  dbms_output.put_line('Total: ' || n_permisosanyadidos);
End;
/

-- Ejecutado en Integracion por AAG el 18/01 a las 13:30
