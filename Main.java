import java.util.*;

public class Main {

  // L2
  static double mockRedisFetch(CurrencyPair currencyPair) throws InterruptedException {
    double value = switch (currencyPair) {
      case CurrencyPair.IDR_SGD -> 0.0001;
      case CurrencyPair.SGD_IDR -> 10000.0;
    };

    Thread.sleep(1000);
    return value;
  }

  public enum CurrencyPair {
    IDR_SGD,
    SGD_IDR,
  }

  static double L1() throws InterruptedException {
    long mins = 60 * 1000;
    Thread.sleep(mins);

    return mockRedisFetch(CurrencyPair.SGD_IDR);
  }

  public static void main(String[] args) throws InterruptedException {
    CurrencyPair currencyPair = CurrencyPair.SGD_IDR;
    double convertValue = L1(currencyPair);

    long totalApiHit = 50000;

    for (int i = 0; i < totalApiHit; i++) {
      System.out.println("Convert value: " + convertValue);
    }
  }
}