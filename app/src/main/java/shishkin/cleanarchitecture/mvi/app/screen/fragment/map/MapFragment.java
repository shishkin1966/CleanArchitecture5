package shishkin.cleanarchitecture.mvi.app.screen.fragment.map;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


import java.util.List;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.location.LocationSubscriber;
import shishkin.cleanarchitecture.mvi.app.location.LocationUnionImpl;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsFragment;

@SuppressWarnings("unused")
public class MapFragment extends AbsFragment<MapModel> implements OnMapReadyCallback, MapView, LocationSubscriber {

    public static final String NAME = MapFragment.class.getName();

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    private GoogleMap mGoogleMap;
    private boolean isInit = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public MapModel createModel() {
        return new MapModel(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (ApplicationUtils.checkPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            startGoogleMap();
        }
    }

    private void startGoogleMap() {
        final GoogleMapOptions mapOptions = new GoogleMapOptions()
                .compassEnabled(true)
                .zoomControlsEnabled(false)
                .mapType(GoogleMap.MAP_TYPE_NORMAL);
        final SupportMapFragment fragment = SupportMapFragment.newInstance(mapOptions);
        fragment.getMapAsync(this);
        final FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.map, fragment, "map");
        transaction.commit();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        if (ApplicationUtils.checkPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            mGoogleMap.setTrafficEnabled(true);
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

            setLocation(SLUtil.getLocationUnion().getLocation());
        }
    }

    @Override
    public void setLocation(Location location) {
        if (mGoogleMap != null && location != null) {
            final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            float zoomLevel = 13;
            if (isInit) {
                zoomLevel = mGoogleMap.getCameraPosition().zoom;
            } else {
                isInit = true;
            }
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
        }
    }

    @Override
    public List<String> getSpecialistSubscription() {
        return StringUtils.arrayToList(LocationUnionImpl.NAME);
    }
}

