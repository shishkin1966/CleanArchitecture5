package shishkin.cleanarchitecture.mvi.app.screen.fragment.map;

import android.Manifest;
import android.location.Address;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;


import java.util.List;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.location.LocationSubscriber;
import shishkin.cleanarchitecture.mvi.app.location.LocationUnionImpl;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.ApplicationSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.presenter.AbsPresenter;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class MapPresenter extends AbsPresenter<MapModel> implements OnMapReadyCallback, LocationSubscriber {

    public static final String NAME = MapPresenter.class.getName();

    private GoogleMap googleMap;
    private boolean isInit = false;

    public MapPresenter(MapModel model) {
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
        if (!SLUtil.getActivityUnion().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            SLUtil.getActivityUnion().grantPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (ApplicationUtils.checkPermission(SLUtil.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            this.googleMap.setTrafficEnabled(true);
            this.googleMap.setMyLocationEnabled(true);
            this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);

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
                SLUtil.getNotificationSpecialist().replaceMessage(ApplicationSpecialistImpl.getInstance().getString(R.string.location), sb.toString());
            }
        }

    }

    @Override
    public List<String> getSpecialistSubscription() {
        return StringUtils.arrayToList(
                super.getSpecialistSubscription(),
                LocationUnionImpl.NAME);
    }

}

