package com.baima.objectivebook;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.baima.objectivebook.entities.Objective;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.litepal.LitePal;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LitePalTest {
    @Test
    public void findAll(){
        List<Objective> all = LitePal.findAll(Objective.class);
        for (Objective objective : all) {
            Log.i("baima", objective.toString());
        }
    }
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.baima.objectivebook", appContext.getPackageName());
    }
}
