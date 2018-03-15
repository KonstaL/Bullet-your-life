package fi.konstal.bullet_your_life.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

import fi.konstal.bullet_your_life.recycler_view.CardViewAdapter;
import fi.konstal.bullet_your_life.recycler_view.DayCard;
import fi.konstal.bullet_your_life.R;

public class WeeklyLogsActivity extends AppCompatActivity {
    private List<DayCard> cardList= new ArrayList<>();
    private RecyclerView recyclerView;
    private CardViewAdapter cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_weekly_logs);



        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        cardAdapter= new CardViewAdapter(cardList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cardAdapter);

        prepareCardData();
    }

    private void prepareCardData() {

        DayCard card = new DayCard("Maanantai", "Affogato sugar, coffee iced in aftertaste flavour café au lait. Affogato bar, plunger pot decaffeinated grounds pumpkin spice milk caramelization. Frappuccino fair trade, skinny robust sit wings trifecta saucer con panna single origin rich. Qui, caffeine single shot flavour brewed macchiato americano beans lungo.");
        cardList.add(card);

        card = new DayCard("Tiistai", "Affogato sugar, coffee iced in aftertaste flavour café au lait. Affogato bar, plunger pot decaffeinated grounds pumpkin spice milk caramelization. Frappuccino fair trade, skinny robust sit wings trifecta saucer con panna single origin rich. Qui, caffeine single shot flavour brewed macchiato americano beans lungo.");
        cardList.add(card);

        card = new DayCard("Keskiviikko", "Affogato sugar, coffee iced in aftertaste flavour café au lait. Affogato bar, plunger pot decaffeinated grounds pumpkin spice milk caramelization. Frappuccino fair trade, skinny robust sit wings trifecta saucer con panna single origin rich. Qui, caffeine single shot flavour brewed macchiato americano beans lungo.");
        cardList.add(card);

        card = new DayCard("Torstai", "Affogato sugar, coffee iced in aftertaste flavour café au lait. Affogato bar, plunger pot decaffeinated grounds pumpkin spice milk caramelization. Frappuccino fair trade, skinny robust sit wings trifecta saucer con panna single origin rich. Qui, caffeine single shot flavour brewed macchiato americano beans lungo.");
        cardList.add(card);

        card = new DayCard("Perjantai", "Affogato sugar, coffee iced in aftertaste flavour café au lait. Affogato bar, plunger pot decaffeinated grounds pumpkin spice milk caramelization. Frappuccino fair trade, skinny robust sit wings trifecta saucer con panna single origin rich. Qui, caffeine single shot flavour brewed macchiato americano beans lungo.");
        cardList.add(card);

        card = new DayCard("Launtai", "Affogato sugar, coffee iced in aftertaste flavour café au lait. Affogato bar, plunger pot decaffeinated grounds pumpkin spice milk caramelization. Frappuccino fair trade, skinny robust sit wings trifecta saucer con panna single origin rich. Qui, caffeine single shot flavour brewed macchiato americano beans lungo.");
        cardList.add(card);

        card = new DayCard("Sunnuntai", "Affogato sugar, coffee iced in aftertaste flavour café au lait. Affogato bar, plunger pot decaffeinated grounds pumpkin spice milk caramelization. Frappuccino fair trade, skinny robust sit wings trifecta saucer con panna single origin rich. Qui, caffeine single shot flavour brewed macchiato americano beans lungo.");
        cardList.add(card);

        cardAdapter.notifyDataSetChanged();
    }
}
