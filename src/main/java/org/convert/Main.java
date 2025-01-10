package org.convert;

import org.convert.ConvertToMt940.WriteCsv;
import org.convert.Core.MoveFIle;
import org.convert.Core.ProcessFolder;
import org.convert.Core.ValidateParams;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // Ruta "quemada" para pruebas
        //String intputFilePath = "C:/Users/User/Desktop/CURSOS/proyectos_spring/spring_security/Banco_azul/MT940.txt";
        //String outputFilePath = "C:/Users/User/Desktop/CURSOS/proyectos_spring/spring_security/Banco_azul/";

        //test lectura de multiples archivos, estos seran parametros
        String PENDIENTES_DIR = "C:/Users/User/Desktop/CURSOS/proyectos_spring/spring_security/Banco_azul/banco_azul_pendientes/";
        String PROCESADOS_DIR = "C:/Users/User/Desktop/CURSOS/proyectos_spring/spring_security/Banco_azul/banco_azul_procesado/";
        String CSV_DIR = "C:/Users/User/Desktop/CURSOS/proyectos_spring/spring_security/Banco_azul/banco_azul_csv/";
        String ERRORES_DIR = "C:/Users/User/Desktop/CURSOS/proyectos_spring/spring_security/Banco_azul/banco_azul_error/";
        String NAME_BANK = "YOP";

        //clase que contiene funcion para validar parametros
        ValidateParams validateParams = new ValidateParams();

        // Verificar si se recibió un argumento en la línea de comandos
        String filePathParameter;
        String outputPathParameter;

        if (args.length >= 5) {
            PENDIENTES_DIR = validateParams.validateParam(args[0], "PENDIENTES_DIR");
            PROCESADOS_DIR = validateParams.validateParam(args[1],"PROCESADOS_DIR");
            CSV_DIR = validateParams.validateParam(args[2],"CSV_DIR");
            ERRORES_DIR = validateParams.validateParam(args[3],"ERRORES_DIR");
            NAME_BANK =validateParams.validateParam(args[4],"NAME_BANK");


        } else {
            System.out.println("Se deben proporcionar al menos 5 parametros");
        }

        ProcessFolder processFolder = new ProcessFolder();
        processFolder.readFolder(PENDIENTES_DIR,PROCESADOS_DIR,ERRORES_DIR,CSV_DIR,NAME_BANK);


    }

}