package shishkin.cleanarchitecture.mvi.sl;

/**
 * Created by Shishkin on 05.03.2018.
 */

public interface SpecialistFactory {

    <T extends Specialist> T create(String name);

}
