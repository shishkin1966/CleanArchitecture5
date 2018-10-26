package shishkin.cleanarchitecture.mvi.sl.task;

import android.os.AsyncTask;


import shishkin.cleanarchitecture.mvi.sl.data.Result;

public class AwaitTask extends AsyncTask<Void, Void, Result> {

    @Override
    protected Result doInBackground(Void... voids) {
        return new Result();
    }
}