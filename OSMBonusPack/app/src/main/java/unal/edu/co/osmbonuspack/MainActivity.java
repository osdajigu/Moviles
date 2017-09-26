package unal.edu.co.osmbonuspack;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.location.GeoNamesPOIProvider;
import org.osmdroid.bonuspack.location.NominatimPOIProvider;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /// para el tutorial
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        MapView map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        GeoPoint startPoint = new GeoPoint(4.63731, -74.08274);
        IMapController mapController = map.getController();
        mapController.setZoom(12);
        mapController.setCenter(startPoint);

        Marker startMarker = new Marker(map);
        startMarker.setPosition(new GeoPoint(4.63731, -74.08274));
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);
        map.invalidate();


        RoadManager roadManagerLine = new OSRMRoadManager(this);
        ArrayList<GeoPoint> points = new ArrayList<GeoPoint>();
        points.add(new GeoPoint(4.6503, -74.0660));
        points.add(new GeoPoint(4.6346, -74.0795));
        Road roadLine = roadManagerLine.getRoad(points);
        Polyline roadOverlayLine = RoadManager.buildRoadOverlay(roadLine);
        map.getOverlays().add(roadOverlayLine);
        /**/


        RoadManager roadManager = new MapQuestRoadManager("MUZl8STAmTurypwahyxuc3cedYmQ6Gdd");
        roadManager.addRequestOption("routeType=bicycle");
        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        waypoints.add(new GeoPoint(4.63731, -74.08274));
        waypoints.add(new GeoPoint(4.67356, -74.13597));
        Road road = roadManager.getRoad(waypoints);
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
        map.getOverlays().add(roadOverlay);

        Drawable nodeIcon = getResources().getDrawable(R.drawable.marker_node);
        for (int i=0; i<road.mNodes.size(); i++){
            RoadNode node = road.mNodes.get(i);
            Marker nodeMarker = new Marker(map);
            nodeMarker.setPosition(node.mLocation);
            nodeMarker.setIcon(nodeIcon);
            nodeMarker.setSnippet(node.mInstructions);
            nodeMarker.setTitle("Step "+i);
            nodeMarker.setSubDescription(Road.getLengthDurationText(this, node.mLength, node.mDuration));
            Drawable icon = getResources().getDrawable(R.drawable.ic_continue);
            nodeMarker.setImage(icon);
            map.getOverlays().add(nodeMarker);
        }

        map.invalidate();
        /**/



        NominatimPOIProvider poiProvider = new NominatimPOIProvider("http://nominatim.openstreetmap.org/");
        ArrayList<POI> pois = poiProvider.getPOICloseTo(startPoint, "cinema", 50, 0.1);

        //FolderOverlay poiMarkers = new FolderOverlay(this);
        RadiusMarkerClusterer poiMarkers = new RadiusMarkerClusterer(this);
        map.getOverlays().add(poiMarkers);

        Drawable poiIcon = getResources().getDrawable(R.drawable.marker_poi_default);
        for (POI poi : pois){
            Marker poiMarker = new Marker(map);
            poiMarker.setTitle(poi.mType);
            poiMarker.setSnippet(poi.mDescription);
            poiMarker.setPosition(poi.mLocation);
            poiMarker.setIcon(poiIcon);
            if (poi.mThumbnail != null){
                poiMarker.setImage(new BitmapDrawable(poi.mThumbnail));
            }
            poiMarkers.add(poiMarker);
        }

        /**/


        KmlDocument kmlDocument = new KmlDocument();
        String url = "http://mapsengine.google.com/map/kml?forcekml=1&mid=z6IJfj90QEd4.kUUY9FoHFRdE";
        kmlDocument.parseKMLUrl(url);

        FolderOverlay kmlOverlay = (FolderOverlay)kmlDocument.mKmlRoot.buildOverlay(map, null, null, kmlDocument);
        map.getOverlays().add(kmlOverlay);

        BoundingBox bb = kmlDocument.mKmlRoot.getBoundingBox();
        map.getController().setCenter(bb.getCenter());

        map.invalidate();

        /**/

    }
}
