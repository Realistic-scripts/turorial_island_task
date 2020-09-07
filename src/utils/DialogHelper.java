package utils;

import org.dreambot.api.methods.dialogues.Dialogues;

import java.util.concurrent.ThreadLocalRandom;

public class DialogHelper {
    int[] DialogOptions;

    public DialogHelper(int[] DialogOptions) {
        this.DialogOptions = DialogOptions;
    }

    public static void selectDialogOption(int optionInt) {
        String[] optionsText = Dialogues.getOptions();
        String totalText = concatStringList(optionsText);
        int tries = 0;

        LogHelper.log(totalText);
        SleepHelper.sleep(timeToRead(totalText));

        while (concatStringList(Dialogues.getOptions()).equals(totalText)) {
            LogHelper.log(concatStringList(Dialogues.getOptions()).equals(totalText));
            SleepHelper.randomSleep(400, 800);
            if (tries == 0 | tries == 5) {
                Dialogues.chooseOption(optionInt);
            }
            tries++;
        }
    }

    public static String concatStringList(String[] stringList) {
        String totalText = "";
        if (stringList == null) {
            return totalText;
        }

        for (String optionText : stringList) {
            totalText = totalText.concat(optionText);
        }
        return totalText;
    }

    public static void continueDialog() {
        SleepHelper.sleepUntil(Dialogues::canContinue, 10000);
        while (Dialogues.canContinue()) {
            SleepHelper.sleepRange(timeToRead(Dialogues.getNPCDialogue()), 600);
            Dialogues.continueDialogue();
            SleepHelper.sleepUntil(Dialogues::canContinue, 1000, 300);
        }
    }

    public static int timeToRead(String text) {
        String[] words = text.split(" ");
        return (int) ((double) words.length * ThreadLocalRandom.current().nextDouble(.14, .28) * 1000.0);
    }

    public void branchingDialog() {
        int optionsSelected = 0;
        int tries = 0;
        while (Dialogues.inDialogue() & tries < 20) {
            if (Dialogues.canContinue()) {
                continueDialog();
            } else if (Dialogues.canEnterInput()) {
                LogHelper.log("ENTER INPUT NOT SUPPORTED YET");
                return;
            } else if (Dialogues.getOptions() != null) {
                selectDialogOption(this.DialogOptions[optionsSelected]);
                optionsSelected++;
            } else {
                tries++;
            }
            SleepHelper.randomSleep(400, 800);
        }
    }
}
