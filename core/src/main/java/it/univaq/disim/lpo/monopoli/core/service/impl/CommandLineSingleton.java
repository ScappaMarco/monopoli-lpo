package it.univaq.disim.lpo.monopoli.core.service.impl;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import com.google.common.collect.Maps;

public class CommandLineSingleton implements Closeable {
    private Scanner scanner = new Scanner(System.in);
    private static CommandLineSingleton cls = new CommandLineSingleton();

    private CommandLineSingleton() {
    }

    public static CommandLineSingleton getInstance() {
        return cls;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void disposeScanner() {
        scanner.close();
    }

    @Override
    public void close() throws IOException {
        scanner.close();
    }

    Integer readInteger() {
        return scanner.nextInt();
    }

    String readString() {
        return scanner.next();
    }

    public <T> T readIntegerUntilPossibleValue(List<T> lista) {
        Map<Integer, T> mappaPezzi = Maps.newHashMap();
        for (int i = 0; i < lista.size(); i++) {
            System.out.println(String.format("%d - %s", i, lista.get(i).toString()));
            mappaPezzi.put(i, lista.get(i));
        }
        Integer[] values = new Integer[mappaPezzi.keySet().size()];
        Integer selezione = readIntegerUntilPossibleValue(mappaPezzi.keySet().toArray(values));
        return lista.get(selezione);
    }

    public Integer readIntegerUntilPossibleValue(Integer[] possibleValues) {
        while (true) {
            try {
                Integer scelta = CommandLineSingleton.getInstance().readInteger();
                if (Arrays.stream(possibleValues).anyMatch(x -> x == scelta)) {
                    return scelta;
                } else {
                    System.out.println("Scelta inammissibile. Devi inserire un valore idoneo. Valori ammissibili: "
                            + Arrays.toString(possibleValues));
                    System.out.print(":> ");
                }
            } catch (InputMismatchException e) {
                System.out.println("Scelta inammissibile. Devi inserire un valore idoneo.");
                scanner.nextLine();
            }
        }
    }

    public String readStringUntilPossibleValue(String[] possibleValues) {
        while (true) {
            try {
                String scelta = CommandLineSingleton.getInstance().readString();
                if (Arrays.stream(possibleValues).anyMatch(x -> x.equals(scelta))) {
                    return scelta;
                } else {
                    System.out.println("Scelta inammissibile. Devi inserire un valore idoneo. Valori ammissibili: "
                            + Arrays.toString(possibleValues));
                }
            } catch (InputMismatchException e) {
                System.out.println("Scelta inammissibile. Devi inserire un valore idoneo.");
                // Assume that CommandLineSingleton handles the next input correctly, no need
                // for scanner.nextLine() here
            }
        }
    }
}