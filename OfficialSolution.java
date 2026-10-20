import java.util.Map;
import java.util.EnumMap;
import java.util.Random;

public class OfficialSolution {
  static final long CACHE_TTL_MS = 30_000; // 30 second cache
  static final long TOTAL_API_HIT = 50_000; // according to the requirement
  static final long REDIS_DELAY_MS = 1_000; // simulate 1 second delay

  record ValueL1(double value, long expiry) {}

  enum CurrencyPair {
    IDR_SGD,
    SGD_IDR,
  }

  // L2
  static double mockRedisFetchL2(CurrencyPair currencyPair) throws InterruptedException {
    double value = switch (currencyPair) {
      case CurrencyPair.IDR_SGD -> 0.0001;
      case CurrencyPair.SGD_IDR -> 10000.0;
    };

    System.out.println("Redis Call for " + currencyPair.name()); // prove redis call has been reduced
    Thread.sleep(REDIS_DELAY_MS);
    return value;
  }

  // L1
  static double solutionL1(Map<CurrencyPair, ValueL1> cache, CurrencyPair currencyPair) throws InterruptedException {
    if (cache.containsKey(currencyPair)) {
      ValueL1 cached = cache.get(currencyPair);
      long currentTime = System.currentTimeMillis();
      if (cached.expiry > currentTime) return cached.value;
    }

    double value = mockRedisFetchL2(currencyPair);
    long expiry = System.currentTimeMillis() + CACHE_TTL_MS;
    cache.put(currencyPair, new ValueL1(value, expiry));
    return value;
  }

  static void simulate() throws InterruptedException {
    Map<CurrencyPair, ValueL1> cache = new EnumMap<>(CurrencyPair.class);
    Random random = new Random();
    CurrencyPair[] pairs = CurrencyPair.values();
    int pairsLength = pairs.length;

    for (int i = 0; i < TOTAL_API_HIT; i++) {
      CurrencyPair currencyPair = pairs[random.nextInt(pairsLength)]; // random select a currency pair
      double convertValue = solutionL1(cache, currencyPair);
      // System.out.println("Convert value: " + convertValue); // uncomment to see the details
    }
  }

  public static void main(String[] args) throws InterruptedException {
    simulate();
  }
}