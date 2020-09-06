package tasks;

import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.TaskNode;
import utils.RandomUsernameGenerator;

public class PickName extends TaskNode {
    @Override
    public int priority() {
        return super.priority();
    }

    @Override
    public boolean accept() {
        if (Widgets.getWidget(558) != null) {
            return Widgets.getWidgetChild(558, 7).isVisible();
        }
        return false;
    }

    @Override
    public int execute() {
        selectName(1000);
        return 0;
    }

    private void selectName(int typeTimeout) {
        log("starting selecting name");
        Widgets.getWidgetChild(558, 7).interact();
        sleep(typeTimeout);

        String widget_text = Widgets.getWidgetChild(162, 44).getText();
        if (widget_text.contains("pick a unique display name")) {
            log("type name widget open");
            String name = RandomUsernameGenerator.generate();

            if (typeName(name)) {
                log("Name typed correctly");
            } else {
                log("name not typed correctly");
                selectName(typeTimeout * 2);
            }
            if (!nameAvailable()) {
                log("name not available");
                selectName(typeTimeout * 2);
            }
            log("Name available!");
            Widgets.getWidgetChild(558, 18).getChild(9).interact();
            sleepUntil(() -> Widgets.getWidgetChild(263, 1).getChild(0).getText().contains("Setting your appearance"), 400);
        }
    }

    private boolean typeName(String name) {
        Keyboard.type(name, false);
        log("typed name");
        sleep(1000);
        String typed_name = Widgets.getWidgetChild(162, 45).getText();
        Keyboard.type("", true);
        return typed_name.equals(name) | typed_name.equals(name + "*");
    }

    private boolean nameAvailable() {
        sleep(2000);
        String available = Widgets.getWidgetChild(558, 12).getText();
        return available.contains(">available<");
    }

}
