package utils;

import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.wrappers.interactive.NPC;
import sun.rmi.runtime.Log;

import java.util.concurrent.ThreadLocalRandom;

public class NPCHelper {
    private NPC currentNPC = null;
    private int NpcId = 0;

    public NPCHelper(int npcId) {
        currentNPC = NPCs.closest(npcId);
        NpcId = npcId;
    }

    public void interact() {
        while (!interactingWithMe()) {
            LogHelper.logMethod("Not interacting with NPC");
            currentNPC.interact();
            SleepHelper.randomSleep(1000, 1500);
        }
        LogHelper.logMethod("Interacting with NPC");
    }

    public void talkTo() {
        LogHelper.logMethod("talking to NPC");
        interact();
        navigateText();
    }

    public void navigateText() {
        String npcText = new WidgetHelper(new int[]{4}, 231).getWidgetText();
        SleepHelper.sleepRange(timeToRead(npcText), 200);
        goToNextText();
    }
    private void goToNextText(){
        Keyboard.type(" ", false);
    }
    private String[] detectTextChoices(){
        WidgetHelper textWidget = new WidgetHelper(new int[]{1}, 219);
        if (textWidget.valid()){
            LogHelper.logMethod(textWidget.allChildText());
        }
    }

    private int timeToRead(String text) {
        String[] words = text.split(" ");
        LogHelper.logMethod(words.length);
        LogHelper.logMethod(ThreadLocalRandom.current().nextDouble(.143, .33));
        return (int) ((double) words.length * ThreadLocalRandom.current().nextDouble(.143, .33) * 1000.0);
    }

    private boolean interactingWithMe() {
        return Me.interactingWithNpc(NpcId);
    }
}
