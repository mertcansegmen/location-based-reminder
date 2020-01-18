package com.mertcansegmen.locationbasedreminder.ui.addeditreminder;

/**
 * Interface representing objects that can be selected as a location for a reminder.
 */
public interface Selectable {

    /**
     * @return the id of the selectable object
     */
    Long getId();

    /**
     * @return display text of the selectable object, this will be shown as the chip text on
     *         reminder list item
     */
    String getDisplayText();

    /**
     * @return display icon of the selectable object, this will be shown as the chip icon on
     *         reminder list item
     */
    int getDisplayIcon();

}
