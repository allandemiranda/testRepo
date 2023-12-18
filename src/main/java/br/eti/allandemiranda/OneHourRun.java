package br.eti.allandemiranda;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OneHourRun implements Runnable {

  private static final LocalTime ZERO_TIME = LocalTime.of(0, 0, 0);

  private final String inputFile;
  private final String outputFolder;
  private final String timeFrame;
  private final String javaHome;
  private final String classpath;

  private final int[] tp;
  private final int[] sl;
  private final int[] trading;
  private final int[] spread;

  @Override
  public void run() {
    final List<LocalTime> startTime = IntStream.range(0, 23).mapToObj(ZERO_TIME::plusHours).toList();
    final List<LocalTime> endTime = IntStream.range(1, 24).mapToObj(ZERO_TIME::plusHours).map(localTime -> localTime.minusSeconds(1)).toList();
    final List<SimpleEntry<LocalTime, LocalTime>> pairTime = IntStream.range(0, startTime.size()).mapToObj(i -> new SimpleEntry<>(startTime.get(i), endTime.get(i))).toList();
    final List<SimpleEntry<DayOfWeek, SimpleEntry<LocalTime, LocalTime>>> pairWeek = Arrays.stream(DayOfWeek.values()).flatMap(dayOfWeek -> pairTime.stream().map(pair -> new SimpleEntry<>(dayOfWeek, pair))).toList();
    pairWeek.forEach(
        time -> new Algorithm(this.getInputFile(), this.getOutputFolder(), this.getTimeFrame(), this.getJavaHome(), this.getClasspath(), time, this.getTp(), this.getSl(), this.getTrading(),
            this.getSpread()).run());
  }
}
