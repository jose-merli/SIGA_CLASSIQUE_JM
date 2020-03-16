CREATE OR REPLACE Function F_SIGA_GETRECURSO_ETIQUETA(p_Idrecurso Varchar2, p_Idlenguaje Number)
  Return Varchar2 Is
  -- VARIABLES
  v_Descripcion Varchar2(2000);
Begin
  Select Descripcion
    Into v_Descripcion
    From Gen_Recursos
   Where Upper(Idrecurso) = Upper(p_Idrecurso)
     And Idlenguaje = p_Idlenguaje;
  --no se si el upper es necesario pero puede que si se quita se estropee alguna cosa
  --lo que si es necesario es que NO sea lower, pq existe un indice ya definido con el UPPER

  Return v_Descripcion;

Exception
  When Others Then
    v_Descripcion := p_Idrecurso;
    Return v_Descripcion;
  
End F_SIGA_GETRECURSO_ETIQUETA;
/
