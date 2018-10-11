package shishkin.cleanarchitecture.mvi.app.screen.fragment.map;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsFragment;

@SuppressWarnings("unused")
public class MapFragment extends AbsFragment<MapModel> implements MapView {

    public static final String NAME = MapFragment.class.getName();

    public static MapFragment newInstance() {
        return new MapFragment();
    }

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

        startMap();
    }

    private void startMap() {
        if (ApplicationUtils.checkPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            final GoogleMapOptions mapOptions = new GoogleMapOptions()
                    .compassEnabled(true)
                    .zoomControlsEnabled(false)
                    .mapType(GoogleMap.MAP_TYPE_NORMAL);
            final SupportMapFragment fragment = SupportMapFragment.newInstance(mapOptions);
            fragment.getMapAsync(getModel().getPresenter());
            final FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.map, fragment, "map");
            transaction.commit();
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void onPermisionGranted(final String permission) {
        if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permission)) {
            SLUtil.getLocationUnion().start();
            startMap();
        }
    }
}

