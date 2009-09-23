/**
 * Copyright (C) 2002-2003, CodeHouse.com. All rights reserved.
 *
 * THIS SOURCE CODE MAY NOT BE MODIFIED, DISTRIBUTED, OR EXECUTED
 * FROM OUTSIDE THE CONTEXT OF CODEHOUSE.COM
 *
 * Written by: EID#2
 *
 * Created: 9/14/02
 *
 * Last Revision Date: 9/14/02
 */



/**
 * Returns a string which contains the Unicode equivalent of param s
 *
 * Params:
 *    s: Target String
 */
function convertString2Unicode(s)
{
   var uniString = "", hexVal, uniChar;

   for(var i = 0; i < s.length; ++i)
   {
      //Convert char to hex
      hexVal = Number(s.charCodeAt(i)).toString(16);

      //Convert to unicode by making sure hex is 4 chars long, padding with 0's if less
      uniChar = "\\u" + ("000" + hexVal).match(/.{4}$/)[0];

      uniString += uniChar;
   }

   return uniString;
}