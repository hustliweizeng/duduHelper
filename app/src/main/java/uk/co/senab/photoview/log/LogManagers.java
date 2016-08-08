/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package uk.co.senab.photoview.log;

import android.util.Log;

/**
 * class that holds the {@link Loggers} for this library, defaults to {@link LoggerDefaults} to send logs to android {@link Log}
 */
public final class LogManagers {

    private static Loggers logger = new LoggerDefaults();

    public static void setLogger(Loggers newLogger) {
        logger = newLogger;
    }

    public static Loggers getLogger() {
        return logger;
    }

}
