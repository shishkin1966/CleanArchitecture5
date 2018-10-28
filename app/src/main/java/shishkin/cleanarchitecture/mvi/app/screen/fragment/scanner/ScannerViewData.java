package shishkin.cleanarchitecture.mvi.app.screen.fragment.scanner;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.ArrayList;
import java.util.List;


import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerViewData implements Parcelable {

    public static final String NAME = ScannerViewData.class.getName();

    private List<Integer> selectedIndices;
    private int cameraId = -1;

    ScannerViewData() {
        selectedIndices = new ArrayList<>();
        for (int i = 0; i < ZXingScannerView.ALL_FORMATS.size(); i++) {
            selectedIndices.add(i);
        }
    }

    List<Integer> getSelectedIndices() {
        return selectedIndices;
    }

    int getCameraId() {
        return cameraId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.selectedIndices);
        dest.writeInt(this.cameraId);
    }

    private ScannerViewData(Parcel in) {
        this.selectedIndices = new ArrayList<>();
        in.readList(this.selectedIndices, Integer.class.getClassLoader());
        this.cameraId = in.readInt();
    }

    public static final Parcelable.Creator<ScannerViewData> CREATOR = new Parcelable.Creator<ScannerViewData>() {
        @Override
        public ScannerViewData createFromParcel(Parcel source) {
            return new ScannerViewData(source);
        }

        @Override
        public ScannerViewData[] newArray(int size) {
            return new ScannerViewData[size];
        }
    };
}
