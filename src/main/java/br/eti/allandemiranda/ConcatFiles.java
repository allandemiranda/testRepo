package br.eti.allandemiranda;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.StreamSupport;

public class ConcatFiles {

  public static final String LINE_BREAK = "\n";

  public static void main(String[] args) {
    final String outputFolder = "C:\\Users\\allan\\OneDrive\\Documentos\\FX\\STATISTIC";
    final String outputFile = "C:\\Users\\allan\\OneDrive\\Documentos\\FX\\";
    File folder = new File(outputFolder);
    File endFile = new File(outputFile, "allStatistic.csv");

    try (final FileWriter fileWriter = new FileWriter(endFile)) {
      fileWriter.write(
          "*TIME FRAME\t*SLOT OPEN DAY\t*SLOT OPEN TIME\t*TP\t*SL\t*MAX SPREAD\t*MIN TRADING\t*ONLY STRONG\tWIN %\tWIN\tLOSE\tTOTAL POSITION\tCONSISTENCE %\tNUMBER OF BAR\tLOW POINT\tHIGH POINT\tFINAL BALANCE\n");
      Arrays.stream(Objects.requireNonNull(folder.listFiles())).map(file -> {
        try (final FileReader fileReader = new FileReader(file); final BufferedReader bufferedReader = new BufferedReader(fileReader)) {
          return StreamSupport.stream(bufferedReader.lines().spliterator(), false).toList();
        } catch (IOException e) {

          throw new RuntimeException(e);
        }
      }).flatMap(Collection::stream).filter(s -> !(s.isBlank() || s.isEmpty())).forEach(line -> {
        try {
          fileWriter.write(line.concat(LINE_BREAK));
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
