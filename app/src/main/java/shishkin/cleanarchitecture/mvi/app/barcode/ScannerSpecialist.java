package shishkin.cleanarchitecture.mvi.app.barcode;

import android.net.Uri;


import java.util.List;


import shishkin.cleanarchitecture.mvi.sl.Specialist;

public interface ScannerSpecialist extends Specialist {
    void scan();

    void scan(Uri uri);

    List<String> decode();

    List<String> decode(Uri uri);
}
