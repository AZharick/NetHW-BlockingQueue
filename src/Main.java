import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

   private static BlockingQueue<String> queueA = new ArrayBlockingQueue<>(100);
   private static BlockingQueue<String> queueB = new ArrayBlockingQueue<>(100);
   private static BlockingQueue<String> queueC = new ArrayBlockingQueue<>(100);
   private static final int strQty = 10_000;
   private static final int strLength = 100_000;

   public static void main(String[] args) throws InterruptedException {

      Thread queueAFiller = new Thread(() -> {
         for (int i = 0; i < strQty; i++) {
            try {
               queueA.put(generateText("abc", strLength));
               queueB.put(generateText("abc", strLength));
               queueC.put(generateText("abc", strLength));
            } catch (InterruptedException e) {
               throw new RuntimeException(e);
            }
         }
      });

      Thread queueAcounter = new Thread(() -> {
         int maxAcount = 0;
         for (int i = 0; i < strQty; i++) {
            if (maxAcount<maxCharCount(queueA.peek(), 'a')) {
               maxAcount = maxCharCount(queueA.peek(), 'a');
               try {
                  queueA.take();
               } catch (InterruptedException e) {
                  throw new RuntimeException(e);
               }
            }
         }
         System.out.println("max A symbols: " + maxAcount);
      });

      Thread queueBcounter = new Thread(() -> {
         int maxAcount = 0;
         for (int i = 0; i < strQty; i++) {
            if (maxAcount<maxCharCount(queueB.peek(), 'b')) {
               maxAcount = maxCharCount(queueB.peek(), 'b');
               try {
                  queueB.take();
               } catch (InterruptedException e) {
                  throw new RuntimeException(e);
               }
            }
         }
         System.out.println("max B symbols: " + maxAcount);
      });

      Thread queueCcounter = new Thread(() -> {
         int maxAcount = 0;
         for (int i = 0; i < strQty; i++) {
            if (maxAcount<maxCharCount(queueC.peek(), 'c')) {
               maxAcount = maxCharCount(queueC.peek(), 'c');
               try {
                  queueC.take();
               } catch (InterruptedException e) {
                  throw new RuntimeException(e);
               }
            }
         }
         System.out.println("max C symbols: " + maxAcount);
      });

      queueAFiller.start();
      Thread.sleep(200);
      queueAcounter.start();
      queueBcounter.start();
      queueCcounter.start();
   }

   public static int maxCharCount(String processedString, char charToCount) {
      int count = 0;
      char[] arrayedString = processedString.toCharArray();
      for (int i = 0; i < arrayedString.length; i++) {
         if (arrayedString[i] == charToCount) {
            count++;
         }
      }
      return count;
   }

   public static String generateText(String letters, int length) {
      Random random = new Random();
      StringBuilder text = new StringBuilder();
      for (int i = 0; i < length; i++) {
         text.append(letters.charAt(random.nextInt(letters.length())));
      }
      return text.toString();
   }

}