package com.mertcansegmen.locationbasedreminder.ui.addeditreminder;

/**
 * Interface representing objects that can be selected for a reminder.
 */
public interface Selectable {

    int PLACE = 0;
    int PLACE_GROUP = 1;

    /**
     * @return the id of the selectable object
     */
    Long getId();

    /**
     * @return display text of the selectable object, this will be shown as the chip text on
     * reminder list item
     */
    String getDisplayText();

    /**
     * @return display icon of the selectable object, this will be shown as the chip icon on
     * reminder list item
     */
    int getDisplayIcon();

    /**
     * @return the type of the selectable object
     */
    int getType();
}
