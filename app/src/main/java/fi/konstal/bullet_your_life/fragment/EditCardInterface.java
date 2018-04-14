package fi.konstal.bullet_your_life.fragment;

import java.io.Serializable;

import fi.konstal.bullet_your_life.task.CardItem;


/**
 * Created by e4klehti on 27.3.2018.
 */

public interface EditCardInterface extends Serializable {
    void addItemToCard(int cardIndex, CardItem... cardItems);
}
