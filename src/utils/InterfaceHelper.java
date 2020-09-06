package utils;

import org.dreambot.api.methods.widget.Widget;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import java.util.HashSet;
import java.util.List;

public class InterfaceHelper {
    HashSet<Integer> preWidgets;
    HashSet<Integer> newWidgets;

    public InterfaceHelper(HashSet<Integer> preWidgets) {
        this.preWidgets = preWidgets;
    }

    public static HashSet<Integer> widgetIdList() {
        HashSet<Integer> holdSet = new HashSet<Integer>();
        for (Widget widget : Widgets.getAllWidgets()) {
            holdSet.add(widget.getID());
        }
        return holdSet;
    }

    public int widgetDiff() {
        return getNewWidget();
    }

    public void interactWith(String name) {
        // TODO make this recursive to find children/grandchildren
        LogHelper.log("Interacting with");
        Widget currentParent = Widgets.getWidget(widgetDiff());
        List<WidgetChild> allChildren = currentParent.getChildren();
        for (WidgetChild widgetChild : allChildren) {
            String[] actions = null;
            String text = null;
            LogHelper.log(widgetChild.getID());
            actions = widgetChild.getActions();
            if (actions != null) {
                for (WidgetChild grandChild : widgetChild.getChildren()) {
                    if (grandChild.getText() != null) {
                        text = grandChild.getText();
                        break;
                    }
                }
            }
            if (text == null) {
                continue;
            }
            if (text.equals(name)) {
                widgetChild.interact();
            }
        }
    }

    private int getNewWidget() {
        this.newWidgets = widgetIdList();
        newWidgets.removeAll(preWidgets);
        if (newWidgets.size() > 1 | newWidgets.size() == 0) {
            return -401;
        }
        return newWidgets.iterator().next();
    }
}
