package chapter1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Statement {
    public static ObjectMapper objectMapper = new ObjectMapper();
    private static Map<String, Play> plays;
    private static Invoice invoice;
    private static Double totalAmount = 0.0;
    private static Double volumeCredits = 0.0;

    public Statement() throws IOException {


    }

    public static void init() throws IOException {
        invoice = objectMapper.readValue(new File("src/main/resources/chapter1/invoice.json"), Invoice.class);
        plays = objectMapper.readValue(new File("src/main/resources/chapter1/plays.json"), new TypeReference<Map<String, Play>>() {
        });
    }

    public static void main(String[] args) throws Exception {

        init();
        List<Performance> performance = invoice.getPerformance();
        StringBuilder result = new StringBuilder("Statement for" + invoice.getCustomer() + "\n");
        for (Performance p : performance) {

            result.append(getPlay(p.getPlayId()).getName()).append(":").append(amountFor(p) / 100).append(" ");
            result.append(p.getAudience()).append("seats\n");


        }

        result.append("Amount owed is ").append(getTotalAmount() / 100).append("\n");
        result.append("you earned ").append(allVolumeCredits()).append("credits");
        System.out.println(result);

    }

    public static Double amountFor(Performance p) throws Exception {
        double result = 0;
        switch (getPlay(p.getPlayId()).getType()) {
            case "tragedy":
                result = 40000;
                if (p.getAudience() > 30) {
                    result += 1000 * (p.getAudience() - 30);
                }
                break;
            case "comedy":
                result = 30000;
                if (p.getAudience() > 20) {
                    result += 10000 + 500 * (p.getAudience() - 20);
                }
                result += 300 * p.getAudience();
                break;
            default:
                throw new Exception("unkonw type");

        }
        return result;
    }

    public static Play getPlay(String key) {
        return plays.get(key);
    }

    public static Double getVolumeCredits(Performance p) {
        Double result = Math.max(p.getAudience() - 30, 0.0);

        if (getPlay(p.getPlayId()).getType().equals("comedy")) {
            result += Math.floor(p.getAudience() / 5.0);
        }
        return result;
    }

    public static Double allVolumeCredits() {
        Double result = 0.0;
        for (Performance p : invoice.getPerformance()) {
            result += getVolumeCredits(p);
        }
        return result;
    }

    public static Double getTotalAmount() throws Exception {
        Double result = 0.0;
        for (Performance p : invoice.getPerformance()) {
            result += amountFor(p);
        }
        return result;
    }
}


