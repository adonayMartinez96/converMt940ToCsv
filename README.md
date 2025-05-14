# ConvertMT940ToCSV

[![Java](https://img.shields.io/badge/Java-8%2B-blue.svg)](https://www.java.com/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)

Herramienta para convertir archivos en formato SWIFT MT940 a CSV estructurado.

## 📋 Tabla de Contenidos
- [Descripción](#-descripción)
- [Características](#-características)
- [Requisitos](#-requisitos)
- [Instalación](#-instalación)
- [Uso](#-uso)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Ejemplo](#-ejemplo)
- [Contribución](#-contribución)
- [Licencia](#-licencia)

## 🚀 Descripción

Este proyecto Java convierte archivos bancarios en formato SWIFT MT940 a archivos CSV estructurados, facilitando su procesamiento y análisis. El sistema:
- Procesa archivos de una carpeta "pendientes"
- Genera archivos CSV en una carpeta de salida
- Mueve los archivos procesados a "procesados" o "errores" según el resultado

## ✨ Características

- ✅ Conversión completa de MT940 a CSV
- ✅ Validación de estructura MT940
- ✅ Manejo de códigos de transacción estándar y específicos
- ✅ Generación automática de nombres de archivo con timestamp
- ✅ Movimiento automático de archivos procesados
- ✅ Manejo de errores robusto

## 📦 Requisitos

- Java 8 o superior
- Maven (para construcción)
- Lombok (configuración IDE)

## 🔧 Instalación

1. Clona el repositorio:
   ```bash
   git clone https://github.com/tu-usuario/ConvertMT940ToCSV.git
Compila el proyecto:

bash
mvn clean package

## 🎯 Uso
Ejecuta el programa con los siguientes parámetros:

bash
java -jar target/convertMt940ToCsv.jar [PENDIENTES_DIR] [PROCESADOS_DIR] [CSV_DIR] [ERRORES_DIR] [NAME_BANK]

Parámetros:



![image](https://github.com/user-attachments/assets/9a299aa4-28c0-429d-b2e9-026a4a60a7eb)

🏗️ Estructura del Proyecto



![image](https://github.com/user-attachments/assets/e6b43546-899b-4236-90e4-882fbcd998db)

