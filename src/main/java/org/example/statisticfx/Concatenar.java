package org.example.statisticfx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.StreamSupport;

public class Concatenar {

  public static void main(String[] args) {
    final String outputFolder = "C:\\Users\\allan\\OneDrive\\Documentos\\FX";
    File folder = new File(outputFolder);
    File endFile = new File(outputFolder, "allStatistic.csv");

    try (final FileWriter fileWriter = new FileWriter(endFile)) {
      fileWriter.write(
          "*TIME FRAME\t*SLOT OPEN\t*TP\t*SL\t*MAX SPREAD\t*MIN TRADING\t*ONLY STRONG\tWIN %\tTOTAL POSITION\tCONSISTENCE %\tNUMBER OF BAR\tLOW POINT\tHIGH POINT\n");
      Arrays.stream(folder.listFiles()).map(file -> {
        try (final FileReader fileReader = new FileReader(file); final BufferedReader bufferreader = new BufferedReader(fileReader)) {
          return StreamSupport.stream(bufferreader.lines().spliterator(), false).toList();
        } catch (IOException e) {

          throw new RuntimeException(e);
        }
      }).flatMap(Collection::stream).filter(s -> !(s.isBlank() || s.isEmpty())).forEach(line -> {
        try {
          fileWriter.write(line.concat("\n"));
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
