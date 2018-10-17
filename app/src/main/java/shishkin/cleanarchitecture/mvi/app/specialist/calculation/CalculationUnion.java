package shishkin.cleanarchitecture.mvi.app.specialist.calculation;

import shishkin.cleanarchitecture.mvi.app.screen.fragment.calculation.CalcViewData;

public interface CalculationUnion {
    void execute(CalcViewData viewData);
}
