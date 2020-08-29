package utils;

import org.dreambot.api.methods.MethodContext;
import org.dreambot.api.methods.widget.Widget;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.wrappers.widgets.WidgetChild;

public class WidgetHelper extends MethodContext {
    private WidgetChild childWidget = null;

    public WidgetHelper(int[] childIds, int parentWidgetId) {
        Widget parentWidget = Widgets.getWidget(parentWidgetId);
        for (int childId : childIds) {
            if (childWidget == null) {
                try {
                    childWidget = parentWidget.getChild(childId);
                } catch (Exception e) {
                    log(e);
                }
            } else {
                try {
                    childWidget = childWidget.getChild(childId);
                } catch (Exception e) {
                    log(e);
                }
            }
        }
    }

    public String getWidgetText() {
        if (childWidget == null) {
            return "NULL";
        }
        return childWidget.getText();
    }

    public boolean widgetContainsText(String text) {
        return getWidgetText().contains(text);
    }
}
