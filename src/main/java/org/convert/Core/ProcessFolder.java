package org.convert.Core;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProcessFolder {
    public  void readFolder(String folderPath, String pathProcesado){
        MoveFIle moveFile = new MoveFIle();

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(folderPath))){
            for(Path file: stream){
                if(!Files.isDirectory(file)){
                    System.out.println("procesando archivo: " + file.getFileName() );
                    if (moveFile.testFuncion()){
                        moveFile.moveFile(file, Paths.get(pathProcesado));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("error al procesar la carpeta " + e.getMessage());
        }
    }
}
