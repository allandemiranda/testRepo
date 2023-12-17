package br.eti.allandemiranda;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
@Log4j2
public class Algorithm implements Runnable {

  private static final String TIME_START = "00:00:01";
  private static final String TIME_END = "00:00:00";

  private final String inputFile;
  private final String outputFolder;
  private final String timeFrame;
  private final String isOpenOnlyStrong;
  private final String javaHome;
  private final String classpath;
  private final SimpleEntry<DayOfWeek, SimpleEntry<LocalTime, LocalTime>> time;

  private final int[] tp;
  private final int[] sl;
  private final int[] trading;
  private final int[] spread;

  private LinkedList<Integer> getTpCollection() {
    return Arrays.stream(this.getTp()).boxed().distinct().sorted().collect(Collectors.toCollection(LinkedList::new));
  }

  private LinkedList<Integer> getSlCollection() {
    return Arrays.stream(this.getSl()).boxed().distinct().sorted().collect(Collectors.toCollection(LinkedList::new));
  }

  private LinkedList<Integer> getTradingCollection() {
    return Arrays.stream(this.getTrading()).boxed().distinct().sorted(Comparator.reverseOrder()).collect(Collectors.toCollection(LinkedList::new));
  }

  private LinkedList<Integer> getSpreadCollection() {
    return Arrays.stream(this.getSpread()).boxed().distinct().sorted().collect(Collectors.toCollection(LinkedList::new));
  }

  @Override
  public void run() {
    final List<String[]> hashListFirst = this.hashSpCollection(this.getTradingCollection().getFirst());
    final List<String[]> hashListLast = this.hashSpCollection(this.getTradingCollection().getLast());
    if (!this.isListArrayStringEqual(hashListFirst, hashListLast)) {
      for (int i = 1; i < this.getTradingCollection().size() - 1; ++i) {
        this.hashSpCollection(this.getTradingCollection().get(i));
      }
    }
  }

  private @NotNull List<String[]> hashSpCollection(int tr) {
    List<String[]> hashList = new ArrayList<>();
    final List<String[]> hashListFirst = this.hashSlCollection(this.getSpreadCollection().getFirst(), tr);
    hashList.addAll(hashListFirst);
    final List<String[]> hashListLast = this.hashSlCollection(this.getSpreadCollection().getLast(), tr);
    hashList.addAll(hashListLast);
    if (!this.isListArrayStringEqual(hashListFirst, hashListLast)) {
      for (int i = 1; i < this.getSpreadCollection().size() - 1; ++i) {
        final List<String[]> hash = this.hashSlCollection(this.getSpreadCollection().get(i), tr);
        hashList.addAll(hash);
      }
    }
    return hashList;
  }

  private @NotNull List<String[]> hashSlCollection(int sp, int tr) {
    List<String[]> hashList = new ArrayList<>();
    final List<String[]> hashListFirst = this.hashTpCollection(this.getSlCollection().getFirst(), sp, tr);
    hashList.addAll(hashListFirst);
    final List<String[]> hashListLast = this.hashTpCollection(this.getSlCollection().getLast(), sp, tr);
    hashList.addAll(hashListLast);
    if (!this.isListArrayStringEqual(hashListFirst, hashListLast)) {
      for (int i = 1; i < this.getSlCollection().size() - 1; ++i) {
        final List<String[]> hash = this.hashTpCollection(this.getSlCollection().get(i), sp, tr);
        hashList.addAll(hash);
      }
    }
    return hashList;
  }

  private @NotNull List<String[]> hashTpCollection(int sl, int sp, int tr) {
    log.info("hashTpCollection(sl={}, sp={}, tr={})", sl, sp, tr);
    final LinkedList<Integer> tpList = this.getTpCollection().stream().filter(tpValue -> tpValue.compareTo(sl) >= 0).collect(Collectors.toCollection(LinkedList::new));
    log.info("tpList={}", Arrays.toString(tpList.toArray()));
    List<String[]> hashList = new ArrayList<>();
    final String[] firstTp = startForexProgram(tpList.getFirst().toString(), String.valueOf(sl), String.valueOf(tr), String.valueOf(sp));
    log.info("firstTp=" + Arrays.toString(firstTp));
    hashList.add(firstTp);
    if (tpList.size() > 1) {
      log.info("tpList.size() > 1 => true");
      final String[] lastTp = startForexProgram(tpList.getLast().toString(), String.valueOf(sl), String.valueOf(tr), String.valueOf(sp));
      log.info("lastTp=" + Arrays.toString(firstTp));
      hashList.add(lastTp);
      if (!this.isArrayStringEqual(firstTp, lastTp)) {
        log.info("!this.isArrayStringEqual(firstTp, lastTp) => true");
        for (int i = 1; i < this.getTpCollection().size() - 1; ++i) {
          final String[] hash = startForexProgram(tpList.get(i).toString(), String.valueOf(sl), String.valueOf(tr), String.valueOf(sp));
          log.info("hash=" + Arrays.toString(hash));
          hashList.add(hash);
        }
      } else {
        log.info("!this.isArrayStringEqual(firstTp, lastTp) => false");
      }
    } else {
      log.info("tpList.size() > 1 => false");
    }
    log.info("hashList=" + Arrays.toString(hashList.toArray()));
    return hashList;
  }

  private boolean isArrayStringEqual(final String @NotNull [] first, final String @NotNull [] last) {
    return first.length == last.length && IntStream.range(0, first.length).mapToObj(i -> first[i].equals(last[i])).allMatch(Boolean::booleanValue);
  }

  private boolean isListArrayStringEqual(final @NotNull List<String[]> first, final @NotNull List<String[]> last) {
    return first.size() == last.size() && IntStream.range(0, first.size()).mapToObj(i -> this.isArrayStringEqual(first.get(i), last.get(i))).allMatch(Boolean::booleanValue);
  }

  @Contract("_, _, _, _ -> new")
  private String @NotNull [] startForexProgram(final String takeProfit, final String stopLoss, final String minTradingDiff, final String maxSpread) {
    final String threadName = String.format("TIME=%s %s-%s TF=%s TP=%s SL=%s OS=%s SPREAD=%s TRADING=%s => START %s", this.getTime().getKey().toString(), this.getTime().getValue().getKey().toString(),
        this.getTime().getValue().getValue().toString(), this.getTimeFrame(), takeProfit, stopLoss, this.getIsOpenOnlyStrong(), maxSpread, minTradingDiff, LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
    log.info(threadName);
    final ForexProgram forexProgram = new ForexProgram(inputFile, outputFolder, String.valueOf(Objects.hashCode(threadName)).replace("-", "a"), this.getTimeFrame(), this.getIsOpenOnlyStrong(), maxSpread, takeProfit,
        stopLoss, this.getTime().getKey().equals(DayOfWeek.MONDAY) ? this.getTime().getValue().getKey().toString() : TIME_START,
        this.getTime().getKey().equals(DayOfWeek.MONDAY) ? this.getTime().getValue().getValue().toString() : TIME_END,
        this.getTime().getKey().equals(DayOfWeek.TUESDAY) ? this.getTime().getValue().getKey().toString() : TIME_START,
        this.getTime().getKey().equals(DayOfWeek.TUESDAY) ? this.getTime().getValue().getValue().toString() : TIME_END,
        this.getTime().getKey().equals(DayOfWeek.WEDNESDAY) ? this.getTime().getValue().getKey().toString() : TIME_START,
        this.getTime().getKey().equals(DayOfWeek.WEDNESDAY) ? this.getTime().getValue().getValue().toString() : TIME_END,
        this.getTime().getKey().equals(DayOfWeek.THURSDAY) ? this.getTime().getValue().getKey().toString() : TIME_START,
        this.getTime().getKey().equals(DayOfWeek.THURSDAY) ? this.getTime().getValue().getValue().toString() : TIME_END,
        this.getTime().getKey().equals(DayOfWeek.FRIDAY) ? this.getTime().getValue().getKey().toString() : TIME_START,
        this.getTime().getKey().equals(DayOfWeek.FRIDAY) ? this.getTime().getValue().getValue().toString() : TIME_END, minTradingDiff, javaHome, classpath);
    final Thread thread = new Thread(forexProgram, threadName);
    thread.start();
    try {
      thread.join();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.error("Thread error {}", threadName);
    }
    log.info(threadName.split("=>")[0].concat(" => END ").concat(forexProgram.getTimeValue()));
    return new String[]{forexProgram.getBalanceValue(), forexProgram.getWinValue(), forexProgram.getLoseValue()};
  }
}
