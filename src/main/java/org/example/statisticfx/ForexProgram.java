package org.example.statisticfx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ForexProgram implements Runnable {

  private String inputFile;
  private String outputFolder;
  private String fileName;
  private String timeFrame;
  private String isOpenOnlyStrong;
  private String mondayStart;
  private String mondayEnd;
  private String tuesdayStart;
  private String tuesdayEnd;
  private String wednesdayStart;
  private String wednesdayEnd;
  private String thursdayStart;
  private String thursdayEnd;
  private String fridayStart;
  private String fridayEnd;
  private String maxSpread;
  private String takeProfit;
  private String stopLoss;
  private String minTradingDiff;
  private boolean isWindows;

  private String javaHome = "\"C:\\Program Files\\Java\\jdk-20.0.1\\bin\\java.exe\"";
  private String classpath = "C:\\Users\\allan\\IdeaProjects\\forex\\target\\classes;C:\\Users\\allan\\.m2\\repository\\org\\springframework\\boot\\spring-boot-starter\\3.1.0\\spring-boot-starter-3.1.0.jar;C:\\Users\\allan\\.m2\\repository\\org\\springframework\\boot\\spring-boot\\3.1.0\\spring-boot-3.1.0.jar;C:\\Users\\allan\\.m2\\repository\\org\\springframework\\spring-context\\6.0.9\\spring-context-6.0.9.jar;C:\\Users\\allan\\.m2\\repository\\org\\springframework\\spring-aop\\6.0.9\\spring-aop-6.0.9.jar;C:\\Users\\allan\\.m2\\repository\\org\\springframework\\spring-beans\\6.0.9\\spring-beans-6.0.9.jar;C:\\Users\\allan\\.m2\\repository\\org\\springframework\\spring-expression\\6.0.9\\spring-expression-6.0.9.jar;C:\\Users\\allan\\.m2\\repository\\org\\springframework\\boot\\spring-boot-autoconfigure\\3.1.0\\spring-boot-autoconfigure-3.1.0.jar;C:\\Users\\allan\\.m2\\repository\\org\\springframework\\boot\\spring-boot-starter-logging\\3.1.0\\spring-boot-starter-logging-3.1.0.jar;C:\\Users\\allan\\.m2\\repository\\ch\\qos\\logback\\logback-classic\\1.4.7\\logback-classic-1.4.7.jar;C:\\Users\\allan\\.m2\\repository\\ch\\qos\\logback\\logback-core\\1.4.7\\logback-core-1.4.7.jar;C:\\Users\\allan\\.m2\\repository\\org\\apache\\logging\\log4j\\log4j-to-slf4j\\2.20.0\\log4j-to-slf4j-2.20.0.jar;C:\\Users\\allan\\.m2\\repository\\org\\apache\\logging\\log4j\\log4j-api\\2.20.0\\log4j-api-2.20.0.jar;C:\\Users\\allan\\.m2\\repository\\org\\slf4j\\jul-to-slf4j\\2.0.7\\jul-to-slf4j-2.0.7.jar;C:\\Users\\allan\\.m2\\repository\\jakarta\\annotation\\jakarta.annotation-api\\2.1.1\\jakarta.annotation-api-2.1.1.jar;C:\\Users\\allan\\.m2\\repository\\org\\springframework\\spring-core\\6.0.9\\spring-core-6.0.9.jar;C:\\Users\\allan\\.m2\\repository\\org\\springframework\\spring-jcl\\6.0.9\\spring-jcl-6.0.9.jar;C:\\Users\\allan\\.m2\\repository\\org\\yaml\\snakeyaml\\1.33\\snakeyaml-1.33.jar;C:\\Users\\allan\\.m2\\repository\\org\\slf4j\\slf4j-api\\2.0.7\\slf4j-api-2.0.7.jar;C:\\Users\\allan\\.m2\\repository\\org\\projectlombok\\lombok\\1.18.28\\lombok-1.18.28.jar;C:\\Users\\allan\\.m2\\repository\\org\\apache\\commons\\commons-csv\\1.10.0\\commons-csv-1.10.0.jar;C:\\Users\\allan\\.m2\\repository\\org\\jetbrains\\annotations\\24.0.1\\annotations-24.0.1.jar;C:\\Users\\allan\\.m2\\repository\\jakarta\\persistence\\jakarta.persistence-api\\3.1.0\\jakarta.persistence-api-3.1.0.jar;C:\\Users\\allan\\.m2\\repository\\jakarta\\validation\\jakarta.validation-api\\3.0.2\\jakarta.validation-api-3.0.2.jar;C:\\Users\\allan\\.m2\\repository\\org\\apache\\commons\\commons-lang3\\3.12.0\\commons-lang3-3.12.0.jar";
  private String springConf = "--enable-preview -XX:TieredStopAtLevel=1 -Dspring.output.ansi.enabled=always -Dcom.sun.management.jmxremote -Dspring.jmx.enabled=true -Dspring.liveBeansView.mbeanDomain -Dspring.application.admin.enabled=true -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8";
  private String mainClass = "br.eti.allandemiranda.forex.ForexApplication";

  private Process getProcess(){
    try {
      if (isWindows) {
          return Runtime.getRuntime().exec(
              String.format("cmd.exe /c %s %s -classpath %s %s --config.mock.ticket.input=%s --config.root.folder=%s --config.statistic.fileName=%s --chart.timeframe=%s --order.open.onlyStrong=%s --order.open.spread.max=%s --order.safe.take-profit=%s --order.safe.stop-loss=%s --order.open.monday.start=%s --order.open.monday.end=%s --order.open.tuesday.start=%s --order.open.tuesday.end=%s --order.open.wednesday.start=%s --order.open.wednesday.end=%s --order.open.thursday.start=%s --order.open.thursday.end=%s --order.open.friday.start=%s --order.open.friday.end=%s --order.open.trading.min=%s",
                  javaHome, springConf, classpath, mainClass, inputFile, outputFolder, fileName, timeFrame, isOpenOnlyStrong, maxSpread, takeProfit, stopLoss, mondayStart, mondayEnd, tuesdayStart, tuesdayEnd, wednesdayStart, wednesdayEnd, thursdayStart, thursdayEnd, fridayStart, fridayEnd, minTradingDiff));
      } else {
        return Runtime.getRuntime()
            .exec(String.format("/bin/sh -c %s %s -classpath %s %s --config.mock.ticket.input=%s --config.root.folder=%s --config.statistic.fileName=%s --chart.timeframe=%s --order.open.onlyStrong=%s --order.open.spread.max=%s --order.safe.take-profit=%s --order.safe.stop-loss=%s --order.open.monday.start=%s --order.open.monday.end=%s --order.open.tuesday.start=%s --order.open.tuesday.end=%s --order.open.wednesday.start=%s --order.open.wednesday.end=%s --order.open.thursday.start=%s --order.open.thursday.end=%s --order.open.friday.start=%s --order.open.friday.end=%s --order.open.trading.min=%s",
                javaHome, springConf, classpath, mainClass, inputFile, outputFolder, fileName, timeFrame, isOpenOnlyStrong, maxSpread, takeProfit, stopLoss, mondayStart, mondayEnd, tuesdayStart, tuesdayEnd, wednesdayStart, wednesdayEnd, thursdayStart, thursdayEnd, fridayStart, fridayEnd, minTradingDiff));
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String getFileName() {
    return fileName;
  }

  public ForexProgram(final String inputFile, final String outputFolder, final String fileName, final String timeFrame, final String isOpenOnlyStrong,
      final String maxSpread, final String takeProfit, final String stopLoss,
      final String mondayStart, final String mondayEnd,
      final String tuesdayStart, final String tuesdayEnd, final String wednesdayStart, final String wednesdayEnd, final String thursdayStart, final String thursdayEnd,
      final String fridayStart, final String fridayEnd,
      final String minTradingDiff, final boolean isWindows) {
    this.inputFile = inputFile;
    this.outputFolder = outputFolder;
    this.fileName = fileName;
    this.timeFrame = timeFrame;
    this.isOpenOnlyStrong = isOpenOnlyStrong;
    this.mondayStart = mondayStart;
    this.mondayEnd = mondayEnd;
    this.tuesdayStart = tuesdayStart;
    this.tuesdayEnd = tuesdayEnd;
    this.wednesdayStart = wednesdayStart;
    this.wednesdayEnd = wednesdayEnd;
    this.thursdayStart = thursdayStart;
    this.thursdayEnd = thursdayEnd;
    this.fridayStart = fridayStart;
    this.fridayEnd = fridayEnd;
    this.maxSpread = maxSpread;
    this.takeProfit = takeProfit;
    this.stopLoss = stopLoss;
    this.minTradingDiff = minTradingDiff;
    this.isWindows = isWindows;
  }

  @Override
  public void run() {
    new BufferedReader(new InputStreamReader(getProcess().getInputStream())).lines()
        .forEach(System.out::println);
  }

}
