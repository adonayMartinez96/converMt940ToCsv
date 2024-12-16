package org.convert;

import org.convert.ConvertToMt940.WriteCsv;
import org.convert.Core.MoveFIle;
import org.convert.Core.ProcessFolder;

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

        // Verificar si se recibió un argumento en la línea de comandos
        String filePathParameter;
        String outputPathParameter;

        if (args.length > 0) {
            filePathParameter = args[0];
            outputPathParameter = args[1];

            System.out.println("Archivo a procesar recibido como argumento: " + filePathParameter);
            System.out.println("Archivo a procesar recibido como argumento: " + outputPathParameter);
        } else {
            // Usar la ruta por defecto si no se pasa un parámetro
            filePathParameter = PENDIENTES_DIR;
            outputPathParameter = CSV_DIR;

            System.out.println("No se proporcionó un argumento, usando ruta quemada: " + filePathParameter);
            System.out.println("El archivo se guardara en ----> "+  outputPathParameter);
        }

        ProcessFolder processFolder = new ProcessFolder();
        processFolder.readFolder(PENDIENTES_DIR,PROCESADOS_DIR,ERRORES_DIR,CSV_DIR,NAME_BANK);


    }

}