package chapter1;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

public class Statement {
    public static void main(String[] args) throws Exception {
     double totalAmount =0;
     double volumeCredits=0;
        ObjectMapper objectMapper = new ObjectMapper();
     JsonNode invoice= objectMapper.readTree(new FileInputStream(new File("src/main/resources/chapter1/invoice.json")));
     JsonNode plays = objectMapper.readTree(new FileInputStream(new File("src/main/resources/chapter1/plays.json")));
     JsonNode performance=invoice.get("performance");
     StringBuilder result =new StringBuilder("Statement for"+ invoice.get("customer").textValue());
     for(int i=0;i<performance.size();i++){
         double thisAmount =0;
         JsonNode per = performance.get(i);
          JsonNode play=plays.get(per.get("playId").textValue());
          Integer audience= per.get("audience").asInt();
          switch(play.get("type").textValue()){
             case "tragedy":
                  thisAmount =40000;
                  if(audience>30){
                      thisAmount+=1000*(audience-30);
                  }
                  break;
              case "comedy":
                  thisAmount =30000;
                  if(audience>20){
                      thisAmount+=10000+500*(audience-20);
                  }
                  thisAmount+=300*audience;
                  break;
              default:throw new Exception("unkonw type");

          }
        volumeCredits+= Math.max(audience-30,0);
          if("comedy".equals(play.get("type").textValue())){
              volumeCredits+=Math.floor(audience/5);
          }
          result.append(play.get("name").textValue()+":"+thisAmount/100+" ");
          result.append(audience+"seats\n");
          totalAmount+=thisAmount;

     }
     result.append("Amount owed is"+totalAmount/100);
     result.append("you earned"+ volumeCredits+"credits");
        System.out.println(result);

    }

}
