package org.convert.ConvertToMt940;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.stream.Collectors;

public class WriteCsv {

    private String filePath;
    private String outputPath;

    // Códigos de transacción según documentación SWIFT MT940
    private static final Set<String> CODIGOS_TRANSACCION_VALIDOS = new HashSet<>(
            Arrays.asList(
                    // Códigos estándar
                    "NTR", "CLR", "DIV", "INT", "COM", "CHG", "SAL", "FEE", "LCR", "INS", "TAX",
                    // Códigos específicos de bancos
                    "MSC", "NRT", "NDC", "ACH", "POS", "ATM", "WIR", "SCT", "SDD"
            )
    );

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public WriteCsv(String filePath, String outputPath) {
        this.filePath = filePath;
        this.outputPath = outputPath;
    }

    public WriteCsv() {
    }

    public boolean extractMt940(String filePath, String outputPath, String nameBank) throws IOException {
        List<String> lines = limpiarLineasMT940(Files.readAllLines(Paths.get(filePath)));

        if (lines.isEmpty()) {
            System.err.println("Error: El archivo está vacío.");
            return false;
        }

        if (!validarEstructuraMT940(lines)) {
            System.err.println("Error: El archivo no contiene la estructura MT940 básica");
            return false;
        }

        String cuenta = "N/A";
        String saldoInicialStr = "0.00";
        BigDecimal saldoActual = BigDecimal.ZERO;
        String moneda = "XXX";
        String nameFile = outputPath + generateName();

        // Extraer información de cabecera
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.startsWith(":25:")) {
                cuenta = extraerCuenta(line);
            }
            else if (line.startsWith(":60F:")) {
                saldoInicialStr = extraerSaldoInicial(line);
                moneda = extraerMoneda(line);
                try {
                    saldoActual = new BigDecimal(saldoInicialStr);
                } catch (NumberFormatException e) {
                    System.err.println("Error en formato de saldo inicial: " + saldoInicialStr);
                    saldoActual = BigDecimal.ZERO;
                }
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nameFile))) {
            writer.write("Fecha,Banco,Cuenta,Moneda,Referencia,Codigo,Monto,Tipo,Saldo,Detalle,Info_Adicional\n");

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();

                if (line.startsWith(":61:")) {
                    // Procesar transacción
                    String infoAdicional = "";
                    if (i + 1 < lines.size() && lines.get(i + 1).startsWith(":86:")) {
                        infoAdicional = extraerInfoAdicionalCompleta(lines, i + 1);
                        i += infoAdicional.split("\n").length;
                    }

                    // Extraer campos
                    String fecha = extraerFecha(line);
                    String marcaDC = extraerMarcaDebitoCredito(line);
                    String montoStr = extraerMonto(line, marcaDC);
                    String codigoTrans = extraerCodigoTransaccion(line);
                    String referencia = extraerReferencia(line);
                    String detalle = procesarDetalleTransaccion(line);

                    // Validar código de transacción
                    if (!CODIGOS_TRANSACCION_VALIDOS.contains(codigoTrans)) {
                        System.err.println("Código de transacción no reconocido: " + codigoTrans);
                    }

                    // Actualizar saldo
                    try {
                        BigDecimal monto = new BigDecimal(montoStr);
                        saldoActual = actualizarSaldo(saldoActual, monto, marcaDC);
                    } catch (NumberFormatException e) {
                        System.err.println("Error en formato de monto: " + montoStr);
                    }

                    // Escribir en CSV
                    writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%.2f,\"%s\",\"%s\"%n",
                            formatFecha(fecha),
                            nameBank,
                            cuenta,
                            moneda,
                            referencia,
                            codigoTrans,
                            montoStr,
                            "D".equals(marcaDC) ? "Debito" : "Credito",
                            saldoActual.doubleValue(),
                            detalle,
                            infoAdicional
                    ));
                }
                else if (line.startsWith(":62F:") || line.startsWith(":64:")) {
                    // Registrar saldos finales si es necesario
                    String saldo = extraerSaldo(line);
                    // Puedes agregarlo al CSV si lo necesitas
                }
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error al generar CSV: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Métodos auxiliares

    private List<String> limpiarLineasMT940(List<String> lines) {
        return lines.stream()
                .filter(line -> !line.matches("^\\{.*\\}")) // Eliminar {1:}, {2:}, {4:}, -}
                .filter(line -> !line.trim().isEmpty())
                .collect(Collectors.toList()); // Compatible con Java 8+
    }

    private boolean validarEstructuraMT940(List<String> lines) {
        return lines.stream().anyMatch(l -> l.startsWith(":20:")) &&
                lines.stream().anyMatch(l -> l.startsWith(":25:")) &&
                lines.stream().anyMatch(l -> l.startsWith(":60F:"));
    }

    private String extraerCuenta(String line25) {
        return line25.length() > 4 ? line25.substring(4).trim() : "N/A";
    }

    private String extraerSaldoInicial(String line60F) {
        return line60F.length() > 15 ? line60F.substring(15).replace(",", ".") : "0.00";
    }

    private String extraerMoneda(String lineBalance) {
        return lineBalance.length() > 13 ? lineBalance.substring(10, 13) : "XXX";
    }

    private String extraerFecha(String line61) {
        return line61.length() >= 10 ? line61.substring(4, 10) : "000000";
    }

    private String extraerMarcaDebitoCredito(String line61) {
        int dcIndex = -1;
        if (line61.length() > 10) {
            dcIndex = line61.indexOf('C', 10);
            if (dcIndex == -1) dcIndex = line61.indexOf('D', 10);
        }
        return (dcIndex != -1 && line61.length() > dcIndex) ?
                line61.substring(dcIndex, dcIndex + 1) : "N/A";
    }

    private String extraerMonto(String line61, String marcaDC) {
        int dcIndex = line61.indexOf(marcaDC);
        if (dcIndex == -1) return "0.00";

        int indexN = line61.indexOf('N', dcIndex + 1);
        if (indexN == -1 || indexN <= dcIndex + 1) return "0.00";

        return line61.substring(dcIndex + 1, indexN).replace(",", ".");
    }

    private String extraerCodigoTransaccion(String line61) {
        int indexN = line61.indexOf('N');
        if (indexN == -1 || line61.length() <= indexN + 3) return "N/A";

        String codigo = line61.substring(indexN + 1, indexN + 4);

        // Manejar códigos extendidos (ej: NMSC -> MSC)
        if (codigo.length() > 0 && codigo.charAt(0) == 'M') {
            codigo = codigo.substring(1);
        }

        return codigo;
    }

    private String extraerReferencia(String line61) {
        int indexN = line61.indexOf('N');
        if (indexN == -1 || line61.length() <= indexN + 4) return "N/A";

        int endRef = line61.indexOf("//", indexN + 4);
        return endRef == -1 ?
                line61.substring(indexN + 4).trim() :
                line61.substring(indexN + 4, endRef).trim();
    }

    private String extraerInfoAdicionalCompleta(List<String> lines, int startIndex) {
        StringBuilder info = new StringBuilder();
        int i = startIndex;

        // Procesar primera línea (:86:)
        if (i < lines.size() && lines.get(i).startsWith(":86:")) {
            info.append(lines.get(i).substring(4).trim());
            i++;

            // Procesar líneas continuas de información (sin tag)
            while (i < lines.size() && !lines.get(i).startsWith(":")) {
                info.append(" ").append(lines.get(i).trim());
                i++;
            }
        }

        return limpiarTextoCSV(info.toString());
    }

    private String procesarDetalleTransaccion(String line61) {
        String detalle = line61.substring(4)  // Eliminar ":61:"
                .replace(",", ".")    // Normalizar decimales
                .replace(";", ".");   // Reemplazar otros separadores

        return limpiarTextoCSV(detalle);
    }

    private String extraerSaldo(String lineBalance) {
        if (lineBalance.length() <= 15) return "0.00";
        return lineBalance.substring(lineBalance.indexOf("Q") + 1)
                .replace(",", ".");
    }

    private BigDecimal actualizarSaldo(BigDecimal saldoActual, BigDecimal monto, String marcaDC) {
        return "D".equals(marcaDC) ? saldoActual.subtract(monto) : saldoActual.add(monto);
    }

    private String limpiarTextoCSV(String texto) {
        if (texto == null) return "";
        return texto.replace("\"", "'")      // Reemplazar comillas dobles
                .replace("\n", " ")      // Eliminar saltos de línea
                .replace("\r", "")       // Eliminar retornos de carro
                .trim();
    }

    private static String formatFecha(String fechaAAMMDD) {
        if (fechaAAMMDD == null || fechaAAMMDD.length() != 6) return "N/A";
        return String.format("%s/%s/20%s",
                fechaAAMMDD.substring(4, 6), // Día
                fechaAAMMDD.substring(2, 4), // Mes
                fechaAAMMDD.substring(0, 2)); // Año
    }

    private static String generateName() {
        return LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("ddMMyyyyHHmmss")) + ".csv";
    }
}