/*
 * This file is part of Bifrost.
 *
 * Copyright (c) 2011-2012, CraftFire <http://www.craftfire.com/>
 * Bifrost is licensed under the GNU Lesser General Public License.
 *
 * Bifrost is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Bifrost is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.craftfire.bifrost;

import com.craftfire.bifrost.classes.forum.ForumBoard;
import com.craftfire.bifrost.classes.forum.ForumUser;
import com.craftfire.bifrost.enums.Scripts;
import com.craftfire.commons.enums.DataType;
import com.craftfire.commons.managers.DataManager;
import com.craftfire.commons.managers.LoggingManager;

//TODO: Javadoc, analytics and logging.

/**
 * This is the main class for Bifrost.
 * <p>
 * This contains all the required methods to use the scripts.
 * To use Bifrost in your projects, you have to use {@link DataManager} and
 * {@link ScriptAPI}, which is provided with Bifrost.
 * <p>
 * Using Bifrost is simple, here's an example on how to initialize Bifrost and ScriptAPI:
 * <pre>
 *      Bifrost bifrost = new Bifrost();
 *      bifrost.getLoggingManager().setDebug(true);
 *      DataManager dataManager = new DataManager(DataType.MYSQL, "username", "password");
 *      dataManager.setDatabase("craftfire");
 *      dataManager.setKeepAlive(true);
 *      dataManager.setPort(3306);
 *      ScriptAPI scriptAPI = bifrost.getScriptAPI();
 *      scriptAPI.addHandle(Scripts.SMF, "2.0.2", dataManager);
 *      ForumUser forumUser = scriptAPI.getForumHandle(Scripts.SMF).getLastRegUser();
 * </pre>
 *
 * @see LoggingManager
 * @see DataManager
 * @see ScriptAPI
 */
public class Bifrost {
    private static Bifrost instance;
    private String version = "1.0.0";
    private final ScriptAPI scriptAPI;
    private final LoggingManager loggingManager = new LoggingManager("CraftFire.Bifrost", "[Bifrost]");

    /**
     * Default constructor for Bifrost.
     */
    public Bifrost() {
        instance = this;
        this.scriptAPI = new ScriptAPI();
        this.loggingManager.info("Initialized Bifrost version " + this.version); // TODO
    }

    /**
     * Returns the instance of Bifrost.
     * <p>
     * Only one instance of Bifrost should be run at all times, this method can be used to grab the instance of Bifrost.
     * @return Bifrost instance
     */
    public static Bifrost getInstance() {
        return instance;
    }

    /**
     * Returns the version of Bifrost.
     *
     * @return version of Bifrost
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Returns the ScriptAPI.
     * <p>
     * ScriptAPI contains all the needed methods to add new script handles, use script methods and such.
     *
     * @return ScriptAPI
     * @see ScriptAPI
     */
    public ScriptAPI getScriptAPI() {
        return this.scriptAPI;
    }

    /**
     * Returns the LoggingManager of Bifrost.
     *
     * @return LoggingManager
     * @see LoggingManager
     */
    public LoggingManager getLoggingManager() {
        return this.loggingManager;
    }
}