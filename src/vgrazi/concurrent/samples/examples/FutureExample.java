package vgrazi.concurrent.samples.examples;

import vgrazi.concurrent.samples.examples.ConcurrentExample;
import vgrazi.concurrent.samples.ConcurrentExampleConstants;
import vgrazi.concurrent.samples.ExampleType;
import vgrazi.concurrent.samples.sprites.ConcurrentSprite;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.*;

/*
 * @user vgrazi.
 * Time: 12:26:11 AM
 */

public class FutureExample extends ConcurrentExample {

  private final JButton launchButton = new JButton("(Launch)");
  private final JButton getButton = new JButton("get");
  private Future<Object> future;
  private ConcurrentSprite sprite;

  private boolean initialized = false;
  private static final int MIN_SNIPPET_POSITION = 300;

  private void executorApproach() throws ExecutionException, InterruptedException {
    setAnimationCanvasVisible(true);

//    sprite = createAcquiringSprite(ConcurrentSprite.SpriteType.OVAL);
    sprite = createAcquiringSprite();
    sprite.setAcquired();
    sprite.moveToAcquiringBorder();
    future = Executors.newCachedThreadPool().submit(new Callable<Object>() {
      public Object call() throws Exception {
        try {
          Thread.sleep(1000);
          sleepRandom(500, 1000);
          sprite.setActionCompleted();
        }
        catch(InterruptedException e) {
          Thread.currentThread().interrupt();
        }
        return "execution completed.";
      }
    });


    //    Object result = future.get();
//    sprite.setReleased();
  }

  public String getTitle() {
    return "Future";
  }

  protected String getSnippet() {
    return "<html><PRE><FONT style=\"font-family:monospaced;\" COLOR=\"#000000\"> \n" +
       "    </FONT><FONT style=\"font-family:monospaced;\" COLOR=\"000000\"><I>// Future objects are returned from Executors</I></FONT><FONT style=\"font-family:monospaced;\" COLOR=\"#000000\"> \n" +
       "    </FONT><FONT style=\"font-family:monospaced;\" COLOR=\"000000\"><I>//   or can be created by developers.</I></FONT><FONT style=\"font-family:monospaced;\" COLOR=\"#000000\"> \n" +
       " \n" +
       "    </FONT><FONT style=\"font-family:monospaced;\" COLOR=\"000000\"><I>// The Future.get() method blocks</I></FONT><FONT style=\"font-family:monospaced;\" COLOR=\"#000000\"> \n" +
       "    </FONT><FONT style=\"font-family:monospaced;\" COLOR=\"000000\"><I>//   until some result is available.</I></FONT><FONT style=\"font-family:monospaced;\" COLOR=\"#000000\"> \n" +
       " \n" +
       "    </FONT><FONT style=\"font-family:monospaced;\" COLOR=\"" + ConcurrentExampleConstants.HTML_DISABLED_COLOR + "\"><I>// Executors submit() methods return Future objects</I></FONT><FONT style=\"font-family:monospaced;\" COLOR=\"#000000\"> \n" +
       "    </FONT><FONT style=\"font-family:monospaced;\" COLOR=\"<state1:#000080>\"><B>final</B></FONT><FONT style=\"font-family:monospaced;\" COLOR=\"<state1:#000000>\"> final Future&lt;Object> future = </FONT>\n" +
       "    </FONT><FONT style=\"font-family:monospaced;\" COLOR=\"<state1:#000080>\"><FONT style=\"font-family:monospaced;\" COLOR=\"<state1:#000000>\">     Executors.newCachedThreadPool().submit(someCallable); \n" +
       " \n" +
            "    </FONT><FONT style=\"font-family:monospaced;\" COLOR=\"<state2:#000000>\">//  Finally, the Future task completes</FONT>\n" +
            "    </FONT><FONT style=\"font-family:monospaced;\" COLOR=\"<state2:#000000>\">//       and the block passes through.</FONT>\n" +
            "    </FONT><FONT style=\"font-family:monospaced;\" COLOR=\"<state2:#000080>\">Object result = future.get(); \n" +
            " \n" +
       "    </FONT></PRE></html";
  }

  public FutureExample(String title, Container frame, int slideNumber) {
    super(title, frame, ExampleType.ONE_USE, MIN_SNIPPET_POSITION, false, slideNumber);
  }

  protected void initializeComponents() {
    if(!initialized) {
      initializeButton(launchButton, new Runnable() {
        public void run() {
          try {
            setState(1);
            enableSetButton();
            executorApproach();
          } catch(ExecutionException e) {
            e.printStackTrace();
          } catch(InterruptedException e) {
            e.printStackTrace();
          }
        }
      });
      initializeButton(getButton, new Runnable() {
        public void run() {
          setState(2);
          if(future != null) {
            try {
              future.get();
              if (sprite != null) {
                sprite.setReleased();
              }
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
              e.printStackTrace();
            }
          }
          getButton.setEnabled(false);
          // select a random mutex from the list

        }
      });
      initialized = true;
    }
  }

//  private void put() {
//    // need to lock or will get an IllegalMonitorStateException. This is analogous to synchronized(lock)
//    lock.lock();
//    try {
//      ConcurrentSprite sprite = createAcquiringSprite();
//      while(count >= 4) {
//        // Temporarily relinquish lock, give control to other threads, and wait until signalled (that the count has been reduced)
//        notFullCondition.await();
//      }
//      availableSprites.add(sprite);
//      sprite.setAcquired();
//      count++;
//      // note: it would have been possible to use 1 condition, and use signalAll
////      fullCondition.signalAll();
//      System.out.println("ConditionExample.put SIGNALLING");
////      fullCondition.signal();
//      System.out.println("ConditionExample.put SIGNALING COMPLETE");
//      notEmptyCondition.signal();
//    }
//    catch(InterruptedException e) {
//      Thread.currentThread().interrupt();
//    }
//    finally {
//      lock.unlock();
//      System.out.println("ConditionExample.put RELEASED");
//    }
//  }

  @Override
  public void spriteRemoved(ConcurrentSprite sprite) {
    bumpMutexVerticalIndex();    
    enableGetButton();
  }

  public String getDescriptionHtml() {
    StringBuffer sb = new StringBuffer();
    return sb.toString();
  }

  protected void reset() {
    message1(" ", ConcurrentExampleConstants.MESSAGE_COLOR);
    message2(" ", ConcurrentExampleConstants.MESSAGE_COLOR);
    resetMutexVerticalIndex();
    setState(0);
    enableGetButton();
    super.reset();
  }

  private void enableGetButton() {
    getButton.setEnabled(false);
    launchButton.setEnabled(true);
    launchButton.requestFocus();
  }

  private void enableSetButton() {
    getButton.setEnabled(true);
    launchButton.setEnabled(false);
    getButton.requestFocus();
  }
}