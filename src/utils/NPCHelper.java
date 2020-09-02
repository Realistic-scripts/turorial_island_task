package utils;

import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.wrappers.interactive.NPC;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

// TODO add rotating camera to see NPC. Make sure NPC is on screen.
public class NPCHelper {
    private NPC currentNPC = null;
    private int NpcId = 0;
    private List<Integer> dialogOptions;

    public NPCHelper(int npcId, Integer[] dialogArray) {
        currentNPC = NPCs.closest(npcId);
        NpcId = npcId;
        dialogOptions = Arrays.asList(dialogArray);
    }

    public void interact() {
        while (!interactingWithMe()) {
            LogHelper.logMethod("Not interacting with NPC");
            currentNPC.interact();
            SleepHelper.randomSleep(1000, 1500);
        }
        LogHelper.logMethod("Interacting with NPC");
    }

    public boolean talkTo() {
        LogHelper.logMethod("talking to NPC");
        interact();
        navigateText();
        return true;
    }

    public void navigateText() {
        // TODO use org.dreambot.api.methods.dialogues.Dialogues dumbass.


        while (new WidgetHelper(new int[]{4}, 231).valid() | new WidgetHelper(new int[]{1}, 219).valid()) {
            List<String> multiChoice = detectTextChoices();
            if (multiChoice == null) {
                String npcText = new WidgetHelper(new int[]{4}, 231).getWidgetText();
                SleepHelper.sleepRange(timeToRead(npcText), 200);
                goToNextText();
            } else {
                LogHelper.logMethod(multiChoice);
                LogHelper.logMethod("multi choice!");
                break;
            }
        }
    }

    private void goToNextText() {
        Keyboard.type(" ", false);
    }

    private List<String> detectTextChoices() {
        WidgetHelper textWidget = new WidgetHelper(new int[]{1}, 219);
        if (textWidget.valid()) {
            for (String text: textWidget.allChildText()) {
                LogHelper.logMethod(text);
            }
            return textWidget.allChildText();
        }
        return null;
    }

    private int timeToRead(String text) {
        String[] words = text.split(" ");
        LogHelper.logMethod(words.length);
        LogHelper.logMethod(ThreadLocalRandom.current().nextDouble(.143, .33));
        return (int) ((double) words.length * ThreadLocalRandom.current().nextDouble(.14, .28) * 1000.0);
    }

    private boolean interactingWithMe() {
        return Me.interactingWithNpc(NpcId);
    }
}
