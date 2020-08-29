package utils;

import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.wrappers.interactive.Character;
import org.dreambot.api.wrappers.interactive.NPC;

public class NPCHelper {
    NPC currentNPC = null;

    public NPCHelper(int npcId) {
        currentNPC = NPCs.closest(npcId);
    }

    public void interact() {
        while (!interactingWithMe()) {
            LogHelper.logMethod("Not interacting with NPC");
            currentNPC.interact();
        }
        LogHelper.logMethod("Interacting with NPC");
    }

    public void talkTo() {
        interact();
    }

    private boolean interactingWithMe() {
        Character interactingWithNPC = currentNPC.getCharacterInteractingWithMe();
        String charName = "";
        if (interactingWithNPC != null) {
            charName = interactingWithNPC.getName();
        }
        String meName = Me.name();
        return charName.equals(meName);
    }
}
