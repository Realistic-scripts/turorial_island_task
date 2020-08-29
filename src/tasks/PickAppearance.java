package tasks;

import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.widgets.Menu;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PickAppearance extends TaskNode {
    private final List<Integer> headWidgets = Collections.unmodifiableList(Arrays.asList(106, 113));
    private final List<Integer> jawWidgets = Collections.unmodifiableList(Arrays.asList(107, 114));
    private final List<Integer> torsoWidgets = Collections.unmodifiableList(Arrays.asList(108, 115));
    private final List<Integer> armsWidgets = Collections.unmodifiableList(Arrays.asList(109, 116));
    private final List<Integer> handsWidgets = Collections.unmodifiableList(Arrays.asList(110, 117));
    private final List<Integer> legsWidgets = Collections.unmodifiableList(Arrays.asList(111, 118));
    private final List<Integer> feetWidgets = Collections.unmodifiableList(Arrays.asList(112, 119));
    private final List<Integer> hairColorWidgets = Collections.unmodifiableList(Arrays.asList(105, 121));
    private final List<Integer> torsoColorWidgets = Collections.unmodifiableList(Arrays.asList(123, 127));
    private final List<Integer> legsColorWidgets = Collections.unmodifiableList(Arrays.asList(122, 129));
    private final List<Integer> feetColorWidgets = Collections.unmodifiableList(Arrays.asList(124, 130));
    private final List<Integer> skinColorWidgets = Collections.unmodifiableList(Arrays.asList(125, 131));
    private final List<Integer> allHeadWidgets = Stream.concat(headWidgets.stream(), hairColorWidgets.stream()).collect(Collectors.toList());
    private final List<Integer> allTorsoWidgets = Stream.concat(torsoWidgets.stream(), torsoColorWidgets.stream()).collect(Collectors.toList());
    private final List<Integer> allArmsWidgets = Stream.concat(armsWidgets.stream(), skinColorWidgets.stream()).collect(Collectors.toList());
    private final List<Integer> allLegsWidgets = Stream.concat(legsWidgets.stream(), legsColorWidgets.stream()).collect(Collectors.toList());
    private final List<Integer> allFeetWidgets = Stream.concat(feetWidgets.stream(), feetColorWidgets.stream()).collect(Collectors.toList());


    private final List<List<Integer>> appearanceWidgets = Collections.unmodifiableList(Arrays.asList(allHeadWidgets,
            jawWidgets, allTorsoWidgets, allArmsWidgets, handsWidgets, allLegsWidgets, allFeetWidgets));

    @Override
    public boolean accept() {
        if (Widgets.getWidget(263) != null) {
            if (Widgets.getWidgetChild(263, 1) != null) {
                return Widgets.getWidgetChild(263, 1).getChild(0).getText().contains("Setting your appearance");
            }
        }
        return false;
    }

    @Override
    public int execute() {
        log("setting appearance");
        pickGender();
        selectDesign();
        sleep(1000);
        return 0;
    }


    private void selectDesign() {
        log("selecting design");
        for (List<Integer> i : appearanceWidgets) {
            Integer leftDesign = i.get(0);
            Integer rightDesign = i.get(1);
            Integer leftColour = 0;
            Integer rightColour = 0;
            if (i.size() > 2) {
                leftColour = i.get(2);
                rightColour = i.get(3);
            }
            changeDesign(leftDesign, rightDesign, leftColour, rightColour);
        }
        Rectangle widgetLocation = Widgets.getWidgetChild(269, 99).getRectangle();
        clickWidget(widgetLocation);

    }

    private void changeDesign(int leftDesign, int rightDesign, int leftColour, int rightColour) {
        int changeRounds = ThreadLocalRandom.current().nextInt(2, 6);
        boolean firstRound = true;
        for (int i = 0; i <= changeRounds; i++) {
            int toChange = 0;
            int changingWidget = 0;
            if (firstRound) {
                toChange = ThreadLocalRandom.current().nextInt(0, 2);
            } else {
                toChange = ThreadLocalRandom.current().nextInt(0, 4);
            }
            switch (toChange) {
                case 0:
                    changingWidget = leftDesign;
                    break;
                case 1:
                    changingWidget = rightDesign;
                    break;
                case 2:
                    changingWidget = leftColour;
                    break;
                case 3:
                    changingWidget = rightColour;
            }
            if (changingWidget == 0) {
                continue;
            }
            int changeTimes = ThreadLocalRandom.current().nextInt(0, 5);
            for (int j = 0; j <= changeTimes; j++) {
                Rectangle widgetLocation = Widgets.getWidgetChild(269, changingWidget).getRectangle();
                clickWidget(widgetLocation);
            }
            firstRound = false;
        }
    }

    private void clickWidget(Rectangle widgetLocation) {
        sleepUntil(() -> Mouse.move(widgetLocation), 500);
        while (Menu.getCount() != 2) {
            ensureMouseOnWidget(widgetLocation);
        }
        Mouse.click();
        sleep(ThreadLocalRandom.current().nextInt(800, 1500));
    }

    private void ensureMouseOnWidget(Rectangle widgetLocation) {
        widgetLocation.x = widgetLocation.x + 3;
        widgetLocation.y = widgetLocation.y + 3;
        widgetLocation.height = widgetLocation.height - 3;
        widgetLocation.width = widgetLocation.width - 3;
        sleep(500);
        sleepUntil(() -> Mouse.move(widgetLocation), 500);
    }

    private void pickGender() {
        int random_num = ThreadLocalRandom.current().nextInt(0, 1 + 1);
        log(random_num);
        if (random_num == 0) {
            log("selecting Female");
            Widgets.getWidgetChild(269, 137).interact();
        } else {
            log("selecting male");
            Widgets.getWidgetChild(269, 137).interact();
            sleep(5000);
            Widgets.getWidgetChild(269, 136).interact();
        }

    }
}
