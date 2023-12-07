package org.example.statisticfx;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Application {

  public static void main(String[] args) {
    System.out.println("Generating scenarios");

    final LocalTime zeroTime = LocalTime.of(0, 0, 0);
    final List<LocalTime> startTime = IntStream.range(0, 96).mapToObj(operand -> zeroTime.plusMinutes(15L * operand)).toList();
    final List<LocalTime> endTime = IntStream.range(1, 97).mapToObj(operand -> zeroTime.plusMinutes(15L * operand).minusSeconds(1)).toList();

    //M1
    String[] onlyStrongM1 = new String[]{"true", "false"};
    Integer[] spreadM1 = new Integer[]{5};
    Integer[] tpM1 = new Integer[]{20};
    Integer[] slM1 = new Integer[]{10};
    Integer[] tradingM1 = new Integer[]{-1, 10, 50};

    final List<Object[]> parametersListM1 = Arrays.stream(onlyStrongM1).flatMap(os -> Arrays.stream(spreadM1).flatMap(s -> Arrays.stream(tpM1)
        .flatMap(tp -> Arrays.stream(slM1).filter(sl -> sl < tp).flatMap(sl -> Arrays.stream(tradingM1).map(t -> new Object[]{"M1", os, s, tp, sl, t, "", ""}))))).toList();

    //M5
    String[] onlyStrongM5 = new String[]{"true", "false"};
    Integer[] spreadM5 = new Integer[]{5, 10, 12, 15};
    Integer[] tpM5 = new Integer[]{20, 30, 40, 50};
    Integer[] slM5 = new Integer[]{19, 30};
    Integer[] tradingM5 = new Integer[]{-1, 10, 30, 60, 90};

    final List<Object[]> parametersListM5 = Arrays.stream(onlyStrongM5).flatMap(os -> Arrays.stream(spreadM5).flatMap(s -> Arrays.stream(tpM5)
        .flatMap(tp -> Arrays.stream(slM5).filter(sl -> sl < tp).flatMap(sl -> Arrays.stream(tradingM5).map(t -> new Object[]{"M5", os, s, tp, sl, t, "", ""}))))).toList();

    //M15
    String[] onlyStrongM15 = new String[]{"true", "false"};
    Integer[] spreadM15 = new Integer[]{5, 10, 12, 15};
    Integer[] tpM15 = new Integer[]{50, 70, 90, 100, 120, 150};
    Integer[] slM15 = new Integer[]{20, 50, 100};
    Integer[] tradingM15 = new Integer[]{-1, 50, 100};

    final List<Object[]> parametersListM15 = Arrays.stream(onlyStrongM15).flatMap(os -> Arrays.stream(spreadM15).flatMap(s -> Arrays.stream(tpM15)
        .flatMap(tp -> Arrays.stream(slM15).filter(sl -> sl < tp).flatMap(sl -> Arrays.stream(tradingM15).map(t -> new Object[]{"M15",os, s, tp, sl, t, "", ""}))))).toList();

    //M30
    String[] onlyStrongM30 = new String[]{"true", "false"};
    Integer[] spreadM30 = new Integer[]{5, 10, 12, 15};
    Integer[] tpM30 = new Integer[]{50, 100, 150, 200};
    Integer[] slM30 = new Integer[]{20, 50, 100, 120};
    Integer[] tradingM30 = new Integer[]{-1, 50, 100};

    final List<Object[]> parametersListM30 = Arrays.stream(onlyStrongM30).flatMap(os -> Arrays.stream(spreadM30).flatMap(s -> Arrays.stream(tpM30)
        .flatMap(tp -> Arrays.stream(slM30).filter(sl -> sl < tp).flatMap(sl -> Arrays.stream(tradingM30).map(t -> new Object[]{"M30",os, s, tp, sl, t, "", ""}))))).toList();

    //H1
    String[] onlyStrongH1 = new String[]{"true", "false"};
    Integer[] spreadH1 = new Integer[]{5, 10, 12, 15};
    Integer[] tpH1 = new Integer[]{50, 100, 150, 200, 250, 500};
    Integer[] slH1 = new Integer[]{20, 50, 100, 150, 200, 300};
    Integer[] tradingH1 = new Integer[]{-1, 50, 100};

    final List<Object[]> parametersListH1 = Arrays.stream(onlyStrongH1).flatMap(os -> Arrays.stream(spreadH1).flatMap(s -> Arrays.stream(tpH1)
        .flatMap(tp -> Arrays.stream(slH1).filter(sl -> sl < tp).flatMap(sl -> Arrays.stream(tradingH1).map(t -> new Object[]{"H1",os, s, tp, sl, t, "", ""}))))).toList();

    //H2
    String[] onlyStrongH2 = new String[]{"true", "false"};
    Integer[] spreadH2 = new Integer[]{5, 10, 12, 15};
    Integer[] tpH2 = new Integer[]{50, 100, 150, 200, 250, 500};
    Integer[] slH2 = new Integer[]{20, 50, 100, 150, 200, 300};
    Integer[] tradingH2 = new Integer[]{-1, 50, 100};

    final List<Object[]> parametersListH2 = Arrays.stream(onlyStrongH2).flatMap(os -> Arrays.stream(spreadH2).flatMap(s -> Arrays.stream(tpH2)
        .flatMap(tp -> Arrays.stream(slH2).filter(sl -> sl < tp).flatMap(sl -> Arrays.stream(tradingH2).map(t -> new Object[]{"H2",os, s, tp, sl, t, "", ""}))))).toList();

    //D1
    String[] onlyStrongD1 = new String[]{"true", "false"};
    Integer[] spreadD1 = new Integer[]{5, 10, 12, 15};
    Integer[] tpD1 = new Integer[]{50, 100, 150, 200, 250, 500};
    Integer[] slD1 = new Integer[]{20, 50, 100, 150, 200, 300};
    Integer[] tradingD1 = new Integer[]{-1, 50, 100};

    final List<Object[]> parametersListD1 = Arrays.stream(onlyStrongD1).flatMap(os -> Arrays.stream(spreadD1).flatMap(s -> Arrays.stream(tpD1)
        .flatMap(tp -> Arrays.stream(slD1).filter(sl -> sl < tp).flatMap(sl -> Arrays.stream(tradingD1).map(t -> new Object[]{"D1",os, s, tp, sl, t, "", ""}))))).toList();


    List<Object[]> parameters = new ArrayList<>();
    parameters.addAll(parametersListM1);
    parameters.addAll(parametersListM5);
    parameters.addAll(parametersListM15);
    parameters.addAll(parametersListM30);
    parameters.addAll(parametersListH1);
    parameters.addAll(parametersListH2);
    parameters.addAll(parametersListD1);
    final List<Object[]> listTime = IntStream.range(0, startTime.size()).mapToObj(i -> new SimpleEntry(startTime.get(i).format(DateTimeFormatter.ISO_TIME), endTime.get(i).format(DateTimeFormatter.ISO_TIME))).flatMap(time -> parameters.stream().map(objects -> new Object[]{objects[0], objects[1], objects[2], objects[3], objects[4], objects[5], time.getKey(), time.getValue()})).toList();

    final List<DayOfWeek> dayOfWeeks = Arrays.stream(DayOfWeek.values()).filter(dayOfWeek -> !(DayOfWeek.SATURDAY.equals(dayOfWeek) || DayOfWeek.SUNDAY.equals(dayOfWeek))).toList();
    final List<SimpleEntry<DayOfWeek, Object[]>> allScenarios = listTime.stream().flatMap(scenario -> dayOfWeeks.stream().map(dayOfWeek -> new SimpleEntry<>(dayOfWeek, scenario))).toList();

    System.out.println("Generated " + listTime.size() + " scenarios");

    IntStream.range(0, allScenarios.size()).mapToObj(i -> new ForexProgram(
        "file:c:/Users/allan/OneDrive/Documentos/FX/EURUSD_202307030007_202307282358.csv",
        "file:c:/Users/allan/OneDrive/Documentos/FX/STATISTIC/",
        String.valueOf(i),
        String.valueOf(allScenarios.get(i).getValue()[0]),
        String.valueOf(allScenarios.get(i).getValue()[1]),
        String.valueOf(allScenarios.get(i).getValue()[2]),
        String.valueOf(allScenarios.get(i).getValue()[3]),
        String.valueOf(allScenarios.get(i).getValue()[4]),
        allScenarios.get(i).getKey().equals(DayOfWeek.MONDAY) ? (String) allScenarios.get(i).getValue()[6] : "00:00:01", allScenarios.get(i).getKey().equals(DayOfWeek.MONDAY) ? (String) allScenarios.get(i).getValue()[7] : "00:00:00",
        allScenarios.get(i).getKey().equals(DayOfWeek.TUESDAY) ? (String) allScenarios.get(i).getValue()[6] : "00:00:01", allScenarios.get(i).getKey().equals(DayOfWeek.TUESDAY) ? (String) allScenarios.get(i).getValue()[7] : "00:00:00",
        allScenarios.get(i).getKey().equals(DayOfWeek.WEDNESDAY) ? (String) allScenarios.get(i).getValue()[6] : "00:00:01", allScenarios.get(i).getKey().equals(DayOfWeek.WEDNESDAY) ? (String) allScenarios.get(i).getValue()[7] : "00:00:00",
        allScenarios.get(i).getKey().equals(DayOfWeek.THURSDAY) ? (String) allScenarios.get(i).getValue()[6] : "00:00:01", allScenarios.get(i).getKey().equals(DayOfWeek.THURSDAY) ? (String) allScenarios.get(i).getValue()[7] : "00:00:00",
        allScenarios.get(i).getKey().equals(DayOfWeek.FRIDAY) ? (String) allScenarios.get(i).getValue()[6] : "00:00:01", allScenarios.get(i).getKey().equals(DayOfWeek.FRIDAY) ? (String) allScenarios.get(i).getValue()[7] : "00:00:00",
        String.valueOf(allScenarios.get(i).getValue()[5]),
        true)).parallel().map(forexProgram -> {
      Thread thread = new Thread(forexProgram, forexProgram.getFileName());
      thread.start();
      return thread;
    }).forEach(thread -> {
      try {
        thread.join();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    });

    System.out.println("END!");
  }
}