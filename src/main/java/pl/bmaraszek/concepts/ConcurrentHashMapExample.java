package pl.bmaraszek.concepts;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class Trade {

}

interface TradeAnalyser {
    void onTradeMessage(Trade trade);
    Double latestPrice(String product);
    Double averageVolume(String product);
}

public class ConcurrentHashMapExample {

    Map<String, MyDto> map = new ConcurrentHashMap<String, MyDto>();

    public static class MyDto {
        private String name;
        private Integer value;

        public MyDto(String name, Integer value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Integer getValue() {
            return value;
        }
    }

    public static void main(String[] args) {

    }



}
