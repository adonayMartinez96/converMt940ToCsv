package org.convert.Core;

public class ValidateParams {
    public ValidateParams() {
    }

    public  String validateParam(String param, String name){

        if (param == null || param.isEmpty()) {
            throw new IllegalArgumentException("El parámetro " + name + " no puede estar vacío.");
        }
        return param;
    }
}
