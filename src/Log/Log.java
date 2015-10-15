/*
 * @(#)Log.java 		1.0 23/04/2014
 * 
 * Copyrigth (C) 2013 Jorge Antonio Perez Espinoza.
 *
 * Centro de Investigación y de Estudios Avanzados 
 * del Instituto Politécnico Nacional - Tamaulipas 
 * jperez@tamps.cinvestav.mx
 */

package Log;
import Utilities.Configuration;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

/**
 * This class keeps a log file for save the historical data
 * @author Jorge Antonio Pérez Espinoza
 */
public class Log {

     public synchronized static void writeLog(String operacion) {
              FileWriter archivo;  //nuestro archivo log
try{
        //Pregunta el archivo existe, caso contrario crea uno con el nombre log.txt
        if (new File(Configuration.DISTRIBUTOR_LOGFILENAME).exists()==false){
            archivo=new FileWriter(new File(Configuration.DISTRIBUTOR_LOGFILENAME),false);}
             archivo = new FileWriter(new File(Configuration.DISTRIBUTOR_LOGFILENAME), true);
             Calendar fechaActual = Calendar.getInstance(); //Para poder utilizar el paquete calendar     
             //Empieza a escribir en el archivo
             archivo.write((String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH))
                  +"/"+String.valueOf(fechaActual.get(Calendar.MONTH)+1)
                  +"/"+String.valueOf(fechaActual.get(Calendar.YEAR))
                  +";"+String.valueOf(fechaActual.get(Calendar.HOUR_OF_DAY))
                  +":"+String.valueOf(fechaActual.get(Calendar.MINUTE))
                  +":"+String.valueOf(fechaActual.get(Calendar.SECOND)))+";"+operacion+"\r\n");
             archivo.close(); //Se cierra el archivo
     }catch(IOException e){
         System.out.println("Error: " + e.getMessage());
     }   
     }
}
