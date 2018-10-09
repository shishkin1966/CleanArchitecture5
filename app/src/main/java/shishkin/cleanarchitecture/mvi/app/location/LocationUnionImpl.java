package shishkin.cleanarchitecture.mvi.app.location;

import android.Manifest;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.common.net.Connectivity;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.sl.AbsSmallUnion;
import shishkin.cleanarchitecture.mvi.sl.ApplicationSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.ErrorSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.data.Result;

/**
 * Объединение, предоставляющее сервис геолокации подписчикам.
 */
public class LocationUnionImpl extends AbsSmallUnion<LocationSubscriber> implements LocationUnion {
    public static final String NAME = LocationUnionImpl.class.getName();

    private static final long POLLING_FREQ = TimeUnit.MINUTES.toMillis(1);
    private static final long FASTEST_UPDATE_FREQ = TimeUnit.SECONDS.toMillis(10);
    private static final float SMALLEST_DISPLACEMENT = 20;

    private FusedLocationProviderClient mFusedLocationClient = null;
    private Location mLocation = null;
    private LocationCallback mLocationCallback = null;
    private LocationRequest mLocationRequest = null;
    private Geocoder mGeocoder;
    private boolean isGetLocation = false;
    private boolean isRuning = false;

    @Override
    public void onRegister() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                setLocation(locationResult.getLastLocation());
            }
        };

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(POLLING_FREQ);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_FREQ);
        mLocationRequest.setSmallestDisplacement(SMALLEST_DISPLACEMENT);

        final LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        final LocationSettingsRequest locationSettingsRequest = builder.build();
    }

    @Override
    public void onRegisterFirstSubscriber() {
        if (ApplicationUtils.checkPermission(ApplicationSpecialistImpl.getInstance(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            start();
        }
    }

    @Override
    public void onUnRegisterLastSubscriber() {
        stop();
    }

    @Override
    public void start() {
        if (!validate()) return;

        if (!hasSubscribers()) {
            return;
        }

        if (isRuning) {
            stop();
        }

        isRuning = true;

        ApplicationUtils.runOnUiThread(() -> {
            final Context context = ApplicationSpecialistImpl.getInstance();
            if (ApplicationUtils.checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (context != null && mLocationRequest != null && mLocationCallback != null) {
                    isGetLocation = false;
                    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
                            .addOnFailureListener(e -> ErrorSpecialistImpl.getInstance().onError(NAME, e));

                    if (mGeocoder == null) {
                        mGeocoder = new Geocoder(context, Locale.getDefault());
                    }
                }
            }
        });

        if (mLocation != null) {
            setLocation(mLocation);
        }
    }

    @Override
    public void stop() {
        isRuning = false;
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            mFusedLocationClient = null;
        }
    }

    private void setLocation(final Location location) {
        isGetLocation = true;
        this.mLocation = location;

        if (location != null) {
            ApplicationUtils.runOnUiThread(() -> {
                for (WeakReference<LocationSubscriber> ref : getSubscribers()) {
                    if (ref != null && ref.get() != null && ref.get().validate()) {
                        ref.get().setLocation(mLocation);
                    }
                }
            });
        }
    }

    @Override
    public Location getLocation() {
        return mLocation;
    }

    @Override
    public void onAddSubscriber(final LocationSubscriber subscriber) {
        if (subscriber != null && subscriber.validate() && mLocation != null) {
            subscriber.setLocation(mLocation);
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public List<Address> getAddress(final Location location) {
        return getAddress(location, 1);
    }

    @Override
    public List<Address> getAddress(final Location location, final int countAddress) {
        int cnt = countAddress;
        if (cnt < 1) {
            cnt = 1;
        }

        final List<Address> list = new ArrayList<>();
        if (Connectivity.isNetworkConnectedOrConnecting(ApplicationSpecialistImpl.getInstance()) && mGeocoder != null && Geocoder.isPresent()) {
            try {
                list.addAll(mGeocoder.getFromLocation(
                        location.getLatitude(),
                        location.getLongitude(),
                        cnt));
            } catch (Exception e) {
                ErrorSpecialistImpl.getInstance().onError(NAME, SLUtil.getContext().getString(R.string.restart_location), true);
            }
        }
        return list;
    }

    @Override
    public Result<Boolean> validateExt() {
        final Context context = ApplicationSpecialistImpl.getInstance();
        if (context != null) {
            if (!ApplicationUtils.isGooglePlayServices(context)) {
                return new Result<>(false).setError(NAME, "Not google play services");
            }

            if (!ApplicationUtils.checkPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                return new Result<>(false).setError(NAME, "Not enabled geolocation permission");
            }

            if (!ApplicationUtils.isLocationEnabled(context)) {
                return new Result<>(false).setError(NAME, "Not enabled geolocation service");
            }
        }

        return super.validateExt();
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (LocationUnion.class.isInstance(o)) ? 0 : 1;
    }

    @Override
    public boolean isGetLocation() {
        return isGetLocation;
    }

    @Override
    public boolean isRuning() {
        return isRuning;
    }


}
