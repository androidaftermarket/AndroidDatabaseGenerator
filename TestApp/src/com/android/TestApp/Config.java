package com.android.TestApp;

import com.android.gda.dbgenerator.annotations.DbConfig;

/**
 * Created by gdaAquino on 5/27/14.
 */
@DbConfig(databaseName = "MyDatabaseTest",
          authority = "com.android.TestApp",
          useContentProvider = false,
          databaseVersion = 1)
public class Config {
}
