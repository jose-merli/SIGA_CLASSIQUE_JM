Pkg_Siga_Censo;

Declare
  Cursor c_direccionesduplicadas Is
  Select dir.*
    From Cen_Direcciones Dir, Cen_Direccion_Tipodireccion Tip
   Where Dir.Idinstitucion = Tip.Idinstitucion
     And Dir.Idpersona = Tip.Idpersona
     And Dir.Iddireccion = Tip.Iddireccion
     And Dir.Fechabaja Is Null
     And Dir.Idinstitucion = 2000
     And Tip.Idtipodireccion In (3, 9)
     And Exists (Select 1
            From Cen_Direcciones Dir2, Cen_Direccion_Tipodireccion Tip2
           Where Dir2.Idinstitucion = Tip2.Idinstitucion
             And Dir2.Idpersona = Tip2.Idpersona
             And Dir2.Iddireccion = Tip2.Iddireccion
             And Dir2.Fechabaja Is Null
             
             And Tip.Idinstitucion = Tip2.Idinstitucion
             And Tip.Idpersona = Tip2.Idpersona
             And Tip.Idtipodireccion = Tip2.Idtipodireccion
             And Tip.Iddireccion > Tip2.Iddireccion);
  p_Codretorno Varchar2(4000);
  p_Datoserror Varchar2(4000);
Begin
  For r_dir In c_direccionesduplicadas Loop
    Pkg_Siga_Censo.Actualizardatosletrado(r_dir.Idpersona, r_dir.Idinstitucion, 30, r_dir.Iddireccion, 0, p_Codretorno, p_Datoserror);
  End Loop;
End;
/

-- Entregado a version por AAG el 14/11 a las 11:25

