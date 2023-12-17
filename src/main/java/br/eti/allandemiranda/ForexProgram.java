package br.eti.allandemiranda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForexProgram implements Runnable {

  private static final String BALANCE = "=BALANCE=";
  private static final String WIN = "=WIN=";
  private static final String LOSE = "=LOSE=";
  private static final String STARTED_FOREX_APPLICATION_IN = "Started ForexApplication in";
  private static final String NOT_FINISHED = "Not finished";

  private final String inputFile;
  private final String outputFolder;
  private final String fileName;
  private final String timeFrame;
  private final String isOpenOnlyStrong;
  private final String mondayStart;
  private final String mondayEnd;
  private final String tuesdayStart;
  private final String tuesdayEnd;
  private final String wednesdayStart;
  private final String wednesdayEnd;
  private final String thursdayStart;
  private final String thursdayEnd;
  private final String fridayStart;
  private final String fridayEnd;
  private final String maxSpread;
  private final String takeProfit;
  private final String stopLoss;
  private final String minTradingDiff;
  private final String javaHome;
  private final String classpath;

  private String balanceValue = NOT_FINISHED;
  private String winValue = NOT_FINISHED;
  private String loseValue = NOT_FINISHED;
  private String timeValue = NOT_FINISHED;

  public ForexProgram(final String inputFile, final String outputFolder, final String fileName, final String timeFrame, final String isOpenOnlyStrong, final String maxSpread, final String takeProfit,
      final String stopLoss, final String mondayStart, final String mondayEnd, final String tuesdayStart, final String tuesdayEnd, final String wednesdayStart, final String wednesdayEnd, final String thursdayStart,
      final String thursdayEnd, final String fridayStart, final String fridayEnd, final String minTradingDiff, final String javaHome, final String classpath) {
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
    this.javaHome = javaHome;
    this.classpath = classpath;
  }

  private Process getProcess() {
    try {
      final String mainClass = "br.eti.allandemiranda.forex.ForexApplication";
      final String springConf = "--enable-preview -XX:TieredStopAtLevel=1 -Dspring.output.ansi.enabled=always -Dcom.sun.management.jmxremote -Dspring.jmx.enabled=true -Dspring.liveBeansView.mbeanDomain -Dspring.application.admin.enabled=true -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8";
      if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
        return Runtime.getRuntime().exec(String.format(
            "cmd.exe /c %s %s -classpath %s %s --config.mock.ticket.input=%s --config.root.folder=%s --config.statistic.fileName=%s --chart.timeframe=%s --order.open.onlyStrong=%s --order.open.spread.max=%s --order.safe.take-profit=%s --order.safe.stop-loss=%s --order.open.monday.start=%s --order.open.monday.end=%s --order.open.tuesday.start=%s --order.open.tuesday.end=%s --order.open.wednesday.start=%s --order.open.wednesday.end=%s --order.open.thursday.start=%s --order.open.thursday.end=%s --order.open.friday.start=%s --order.open.friday.end=%s --order.open.trading.min=%s --spring.main.allow-bean-definition-overriding=true",
            this.getJavaHome(), springConf, this.getClasspath(), mainClass, this.getInputFile(), this.getOutputFolder(), this.getFileName(), this.getTimeFrame(), this.getIsOpenOnlyStrong(), this.getMaxSpread(),
            this.getTakeProfit(), this.getStopLoss(), this.getMondayStart(), this.getMondayEnd(), this.getTuesdayStart(), this.getTuesdayEnd(), this.getWednesdayStart(), this.getWednesdayEnd(), this.getThursdayStart(),
            this.getThursdayEnd(), this.getFridayStart(), this.getFridayEnd(), this.getMinTradingDiff()));
      } else {
        return Runtime.getRuntime().exec(String.format(
            "/bin/sh -c %s %s -classpath %s %s --config.mock.ticket.input=%s --config.root.folder=%s --config.statistic.fileName=%s --chart.timeframe=%s --order.open.onlyStrong=%s --order.open.spread.max=%s --order.safe.take-profit=%s --order.safe.stop-loss=%s --order.open.monday.start=%s --order.open.monday.end=%s --order.open.tuesday.start=%s --order.open.tuesday.end=%s --order.open.wednesday.start=%s --order.open.wednesday.end=%s --order.open.thursday.start=%s --order.open.thursday.end=%s --order.open.friday.start=%s --order.open.friday.end=%s --order.open.trading.min=%s --spring.main.allow-bean-definition-overriding=true",
            this.getJavaHome(), springConf, this.getClasspath(), mainClass, this.getInputFile(), this.getOutputFolder(), this.getFileName(), this.getTimeFrame(), this.getIsOpenOnlyStrong(), this.getMaxSpread(),
            this.getTakeProfit(), this.getStopLoss(), this.getMondayStart(), this.getMondayEnd(), this.getTuesdayStart(), this.getTuesdayEnd(), this.getWednesdayStart(), this.getWednesdayEnd(), this.getThursdayStart(),
            this.getThursdayEnd(), this.getFridayStart(), this.getFridayEnd(), this.getMinTradingDiff()));
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void run() {
    final List<String> output = new BufferedReader(new InputStreamReader(getProcess().getInputStream())).lines().toList();
    this.setBalanceValue(output.stream().filter(s -> s.contains(BALANCE)).findFirst().orElse("=BALANCE=ERROR=BALANCE=").split(BALANCE)[1]);
    this.setWinValue(output.stream().filter(s -> s.contains(WIN)).findFirst().orElse("=WIN=ERROR=WIN=").split(WIN)[1]);
    this.setLoseValue(output.stream().filter(s -> s.contains(LOSE)).findFirst().orElse("=LOSE=ERROR=LOSE=").split(LOSE)[1]);
    this.setTimeValue(output.stream().filter(s -> s.contains(STARTED_FOREX_APPLICATION_IN)).findFirst().orElse(":ERROR").split(":")[1]);
  }
}
