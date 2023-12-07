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

  private String javaHome;
  private String classpath;
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
      final String minTradingDiff, final boolean isWindows, final String javaHome, final String classpath) {
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
    this.javaHome = javaHome;
    this.classpath = classpath;
  }

  @Override
  public void run() {
    new BufferedReader(new InputStreamReader(getProcess().getInputStream())).lines()
        .forEach(System.out::println);
  }

}
