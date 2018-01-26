package tdt4140.gr1800.app.ui;

import fxmapcontrol.MapItem;
import fxmapcontrol.MapNode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import tdt4140.gr1800.app.core.LatLong;

public class MapPathLine extends MapItem<LatLong> {

	public MapPathLine(MapNode start, MapNode end) {
		setLocation(start.getLocation());
		final Line line = new Line();
		line.setStrokeWidth(3);
		line.setStroke(Color.GREEN);
		end.translateXProperty().addListener((prop, oldValue, newValue) -> {
			line.setEndX(end.getTranslateX() - getTranslateX());
		});
		end.translateYProperty().addListener((prop, oldValue, newValue) -> {
			line.setEndY(end.getTranslateY() - getTranslateY());
		});
		getChildren().add(line);
	}
}
