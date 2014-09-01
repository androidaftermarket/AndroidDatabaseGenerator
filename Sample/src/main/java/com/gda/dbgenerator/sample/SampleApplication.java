package com.gda.dbgenerator.sample;

import android.app.Application;
import com.gda.dbgenerator.annotations.DbConfig;

/**
 * @author Gian Darren Azriel Aquino
 * @since 9/1/14
 */
@DbConfig(databaseName = "SampleDatabase", databaseVersion = 1)
public class SampleApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
  }
}
