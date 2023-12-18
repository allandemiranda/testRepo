package br.eti.allandemiranda;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@Log4j2
public class Algorithm implements Runnable {

  private static final String TIME_START = "00:00:01";
  private static final String TIME_END = "00:00:00";
  public static final String NUMBER = "0";

  private final String inputFile;
  private final String outputFolder;
  private final String timeFrame;
  private final String javaHome;
  private final String classpath;
  private final SimpleEntry<DayOfWeek, SimpleEntry<LocalTime, LocalTime>> time;

  private final int[] tp;
  private final int[] sl;
  private final int[] tr;
  private int[] sp;

  private boolean haveOperation = false;
  private LocalDateTime dateTime;
  private BigDecimal bid;
  private BigDecimal ask;
  private int minSpread;

  public Algorithm(final String inputFile, final String outputFolder, final String timeFrame, final String javaHome, final String classpath, final SimpleEntry<DayOfWeek, SimpleEntry<LocalTime, LocalTime>> time,
      final int[] tp, final int[] sl, final int[] tr, final int[] sp) {
    this.inputFile = inputFile;
    this.outputFolder = outputFolder;
    this.timeFrame = timeFrame;
    this.javaHome = javaHome;
    this.classpath = classpath;
    this.time = time;
    this.tp = tp;
    this.sl = sl;
    this.tr = tr;
    this.sp = sp;
  }

  public void update(final @NotNull LocalDateTime dateTime, final double bid, final double ask) {
    this.setDateTime(dateTime);
    if (bid > 0d) {
      this.setBid(BigDecimal.valueOf(bid).setScale(5, RoundingMode.DOWN));
    }
    if (ask > 0d) {
      this.setAsk(BigDecimal.valueOf(ask).setScale(5, RoundingMode.DOWN));
    }
    final BigDecimal price = this.getAsk().subtract(this.getBid());
    final int points = this.getPoints(price, 5);

    if (this.getMinSpread() > points) {
      this.setMinSpread(points);
    }
  }

  public int getPoints(final @NotNull BigDecimal price, final int digits) {
    return price.multiply(BigDecimal.valueOf(Math.pow(10, digits))).intValue();
  }

  private void checkSpread() {
    this.setMinSpread(this.getSpreadCollection().getLast());
    try (final FileReader fileReader = new FileReader(this.getInputFile()); final CSVParser csvParser = CSVFormat.TDF.builder().build().parse(fileReader)) {
      StreamSupport.stream(csvParser.spliterator(), false).skip(1).forEachOrdered(csvRecord -> {
        String date = csvRecord.get(0);
        String lTime = csvRecord.get(1);
        String dataTime = date.replace(".", "-").concat("T").concat(lTime);

        LocalDateTime localDateTime = LocalDateTime.parse(dataTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        if (localDateTime.getDayOfWeek().equals(this.getTime().getKey()) && (!localDateTime.toLocalTime().isBefore(this.getTime().getValue().getKey()) && (
            localDateTime.toLocalTime().isBefore(this.getTime().getValue().getValue())))) {
          double lBid = csvRecord.get(2).isEmpty() ? 0d : Double.parseDouble(csvRecord.get(2));
          double lAsk = csvRecord.get(3).isEmpty() ? 0d : Double.parseDouble(csvRecord.get(3));
          this.update(localDateTime, lBid, lAsk);
        }

      });
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
    this.setSp(Arrays.stream(this.getSp()).filter(value -> value >= this.getMinSpread()).toArray());
  }

  private LinkedList<Integer> getTpCollection() {
    return Arrays.stream(this.getTp()).boxed().distinct().sorted().collect(Collectors.toCollection(LinkedList::new));
  }

  private LinkedList<Integer> getSlCollection() {
    return Arrays.stream(this.getSl()).boxed().distinct().sorted().collect(Collectors.toCollection(LinkedList::new));
  }

  private LinkedList<Integer> getTradingCollection() {
    return Arrays.stream(this.getTr()).boxed().distinct().sorted().collect(Collectors.toCollection(LinkedList::new));
  }

  private LinkedList<Integer> getSpreadCollection() {
    return Arrays.stream(this.getSp()).boxed().distinct().sorted().collect(Collectors.toCollection(LinkedList::new));
  }

  @Override
  public void run() {
    this.checkSpread();
    for (int i = 0; i < this.getTradingCollection().size(); ++i) {
      final List<String[]> hashList = this.hashSpCollection(this.getTradingCollection().getFirst(), false);
      if (hashList.stream().noneMatch(strings -> Arrays.stream(strings).anyMatch(s -> !s.equals(NUMBER)))) {
        break;
      }
    }
    if (this.isHaveOperation()) {
      for (int i = 0; i < this.getTradingCollection().size(); ++i) {
        final List<String[]> hashList = this.hashSpCollection(this.getTradingCollection().getFirst(), true);
        if (hashList.stream().noneMatch(strings -> Arrays.stream(strings).anyMatch(s -> !s.equals(NUMBER)))) {
          break;
        }
      }
    }
  }

  private @NotNull List<String[]> hashSpCollection(int trading, final boolean openOnlyStrong) {
    List<String[]> hashList = new ArrayList<>();
    final List<String[]> hashListFirst = this.hashSlCollection(this.getSpreadCollection().getFirst(), trading, openOnlyStrong);
    hashList.addAll(hashListFirst);
    final List<String[]> hashListLast = this.hashSlCollection(this.getSpreadCollection().getLast(), trading, openOnlyStrong);
    hashList.addAll(hashListLast);
    if (!this.isListArrayStringEqual(hashListFirst, hashListLast)) {
      for (int i = 1; i < this.getSpreadCollection().size() - 1; ++i) {
        final List<String[]> hash = this.hashSlCollection(this.getSpreadCollection().get(i), trading, openOnlyStrong);
        hashList.addAll(hash);
      }
    }
    return hashList;
  }

  private @NotNull List<String[]> hashSlCollection(int spread, int trading, final boolean openOnlyStrong) {
    List<String[]> hashList = new ArrayList<>();
    final List<String[]> hashListFirst = this.hashTpCollection(this.getSlCollection().getFirst(), spread, trading, openOnlyStrong);
    hashList.addAll(hashListFirst);
    final List<String[]> hashListLast = this.hashTpCollection(this.getSlCollection().getLast(), spread, trading, openOnlyStrong);
    hashList.addAll(hashListLast);
    if (!this.isListArrayStringEqual(hashListFirst, hashListLast)) {
      for (int i = 1; i < this.getSlCollection().size() - 1; ++i) {
        final List<String[]> hash = this.hashTpCollection(this.getSlCollection().get(i), spread, trading, openOnlyStrong);
        hashList.addAll(hash);
      }
    }
    return hashList;
  }

  private @NotNull List<String[]> hashTpCollection(int stopLoss, int spread, int trading, final boolean openOnlyStrong) {
    log.info("hashTpCollection(sl={}, sp={}, tr={})", stopLoss, spread, trading);
    final LinkedList<Integer> tpList = this.getTpCollection().stream().filter(tpValue -> tpValue.compareTo(stopLoss) >= 0).collect(Collectors.toCollection(LinkedList::new));
    log.info("tpList={}", Arrays.toString(tpList.toArray()));
    List<String[]> hashList = new ArrayList<>();
    final String[] firstTp = startForexProgram(tpList.getFirst().toString(), String.valueOf(stopLoss), String.valueOf(trading), String.valueOf(spread), openOnlyStrong);
    log.info("firstTp=" + Arrays.toString(firstTp));
    hashList.add(firstTp);
    if (tpList.size() > 1) {
      log.info("tpList.size() > 1 => true");
      final String[] lastTp = startForexProgram(tpList.getLast().toString(), String.valueOf(stopLoss), String.valueOf(trading), String.valueOf(spread), openOnlyStrong);
      log.info("lastTp=" + Arrays.toString(firstTp));
      hashList.add(lastTp);
      if (!this.isArrayStringEqual(firstTp, lastTp)) {
        log.info("!this.isArrayStringEqual(firstTp, lastTp) => true");
        for (int i = 1; i < this.getTpCollection().size() - 1; ++i) {
          final String[] hash = startForexProgram(tpList.get(i).toString(), String.valueOf(stopLoss), String.valueOf(trading), String.valueOf(spread), openOnlyStrong);
          hashList.add(hash);
        }
      } else {
        log.info("!this.isArrayStringEqual(firstTp, lastTp) => false");
      }
    } else {
      log.info("tpList.size() > 1 => false");
    }
    log.info("hashList=" + Arrays.toString(hashList.stream().map(strings -> Arrays.toString(strings)).toArray()));
    return hashList;
  }

  private boolean isArrayStringEqual(final String @NotNull [] first, final String @NotNull [] last) {
    return first.length == last.length && IntStream.range(0, first.length).mapToObj(i -> first[i].equals(last[i])).allMatch(Boolean::booleanValue);
  }

  private boolean isListArrayStringEqual(final @NotNull List<String[]> first, final @NotNull List<String[]> last) {
    return first.size() == last.size() && IntStream.range(0, first.size()).mapToObj(i -> this.isArrayStringEqual(first.get(i), last.get(i))).allMatch(Boolean::booleanValue);
  }

  @Contract("_, _, _, _ -> new")
  private String @NotNull [] startForexProgram(final String takeProfit, final String stopLoss, final String minTradingDiff, final String maxSpread, final boolean openOnlyStrong) {
    final String threadName = String.format("TIME=%s %s-%s TF=%s TP=%s SL=%s OS=%s SPREAD=%s TRADING=%s => START %s", this.getTime().getKey().toString(), this.getTime().getValue().getKey().toString(),
        this.getTime().getValue().getValue().toString(), this.getTimeFrame(), takeProfit, stopLoss, openOnlyStrong, maxSpread, minTradingDiff, LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
    log.info(threadName);
    final ForexProgram forexProgram = new ForexProgram(inputFile, outputFolder, String.valueOf(Objects.hashCode(threadName)).replace("-", "a"), this.getTimeFrame(), String.valueOf(openOnlyStrong), maxSpread, takeProfit,
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

    final String[] hash = {forexProgram.getBalanceValue(), forexProgram.getWinValue(), forexProgram.getLoseValue()};
    log.info(threadName.split("=>")[0].concat(" => END ").concat(forexProgram.getTimeValue()).concat(" -> hash=").concat(Arrays.toString(hash)));
    if (!this.isHaveOperation() && Arrays.stream(hash).filter(s -> !s.equals(NUMBER)).isParallel()) {
      this.setHaveOperation(true);
    }
    return hash;
  }
}
