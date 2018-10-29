package shishkin.cleanarchitecture.mvi.app.screen.fragment.map;

import android.Manifest;
import android.location.Address;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.List;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.specialist.location.LocationSubscriber;
import shishkin.cleanarchitecture.mvi.app.specialist.location.LocationUnionImpl;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.common.utils.ViewUtils;
import shishkin.cleanarchitecture.mvi.sl.presenter.AbsPresenter;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class MapPresenter extends AbsPresenter<MapModel> implements OnMapReadyCallback, LocationSubscriber, GoogleMap.OnMyLocationButtonClickListener {

    public static final String NAME = MapPresenter.class.getName();

    private GoogleMap googleMap;
    private boolean isInit = false;
    private MapViewData viewData;
    private Marker marker;

    MapPresenter(MapModel model) {
        super(model);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isRegister() {
        return true;
    }

    @Override
    public void onStart() {
        if (viewData == null) {
            viewData = new MapViewData();
        }
    }

    @Override
    public void onResumeView() {
        super.onResumeView();

        if (!ApplicationUtils.checkPermission(SLUtil.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            SLUtil.getViewUnion().grantPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (ApplicationUtils.checkPermission(SLUtil.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            this.googleMap.setTrafficEnabled(true);
            this.googleMap.setMyLocationEnabled(true);
            this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            this.googleMap.setOnMyLocationButtonClickListener(this);
            setLocation(SLUtil.getLocationUnion().getLocation());
        }
    }


    @Override
    public void setLocation(Location location) {
        if (googleMap != null && location != null) {
            final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            float zoomLevel = 13;
            if (isInit) {
                zoomLevel = googleMap.getCameraPosition().zoom;
            } else {
                isInit = true;
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
            addMarker(location);

            final List<Address> list = SLUtil.getLocationUnion().getAddress(location);
            if (list != null && !list.isEmpty()) {
                final Address address = list.get(0);
                final StringBuilder sb = new StringBuilder();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i));
                    if (i < address.getMaxAddressLineIndex()) {
                        sb.append("\n");
                    }
                }
                viewData.setAddress(sb.toString());
                getModel().getView().refreshViews(viewData);
            }
        }
    }

    @Override
    public List<String> getSpecialistSubscription() {
        return StringUtils.arrayToList(
                super.getSpecialistSubscription(),
                LocationUnionImpl.NAME);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        getModel().getView().getRootView().post(() -> setLocation(SLUtil.getLocationUnion().getLocation()));
        return false;
    }

    private void addMarker(Location location) {
        if (marker != null) {
            marker.remove();
        }

        marker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                .alpha(0.7f)
                .icon(ViewUtils.generateBitmapDescriptorFromRes(SLUtil.getContext(), R.drawable.pin))
                .title("Мое местоположение"));

    }
}

