package tdt4140.gr1800.app.ui;

import fxmapcontrol.Location;
import fxmapcontrol.MapItem;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import tdt4140.gr1800.app.core.LatLong;

public class MapMarker extends MapItem<LatLong> {

	public MapMarker(LatLong latLong) {
		setLocation(new Location(latLong.latitude, latLong.longitude));
		final Circle circle = new Circle();
		circle.setRadius(5);
		circle.setFill(getMarkerColor(false));
		getChildren().add(circle);
		selectedProperty().addListener((booleanProperty, oldValue, newValue) -> {
			circle.setFill(getMarkerColor(newValue));
		});
	}

	protected Paint getMarkerColor(boolean selected) {
		return selected ? Color.BLUE : Color.GREEN;
	}
}
