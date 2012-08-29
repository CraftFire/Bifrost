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
package com.craftfire.bifrost.handles;

import com.craftfire.bifrost.ScriptAPI;
import com.craftfire.bifrost.classes.Cache;
import com.craftfire.bifrost.classes.general.Ban;
import com.craftfire.bifrost.classes.general.Group;
import com.craftfire.bifrost.classes.general.PrivateMessage;
import com.craftfire.bifrost.classes.general.ScriptUser;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.enums.Scripts;
import com.craftfire.bifrost.exceptions.UnsupportedFunction;
import com.craftfire.bifrost.exceptions.UnsupportedScript;
import com.craftfire.bifrost.exceptions.UnsupportedVersion;
import com.craftfire.bifrost.script.Script;
import com.craftfire.commons.enums.Encryption;
import com.craftfire.commons.managers.DataManager;

import java.sql.SQLException;
import java.util.List;

/**
 * @see Script
 */
public class ScriptHandle {
    public Script script;
    public Scripts scriptName;
    public final String version;
    private final DataManager dataManager;
    private ScriptHandle instance;

    /**
     * @param script  The script using the enum list, for example: Scripts.SMF.
     * @param version The version that the user has set in his config.
     * @throws com.craftfire.bifrost.exceptions.UnsupportedVersion if the input version is not found in the list of supported versions.
     */
    public ScriptHandle(Scripts script, String version, DataManager dataManager) throws UnsupportedVersion {
        this.scriptName = script;
        this.version = version;
        this.dataManager = dataManager;
        this.script = ScriptAPI.setScript(this.scriptName, version, dataManager);
        if (!this.script.isSupportedVersion()) {
            throw new UnsupportedVersion();
        }
        this.instance = this;
    }

    public ScriptHandle(Script script) throws UnsupportedVersion {
        this.script = script;
        this.version = script.getVersion();
        this.dataManager = script.getDataManager();
        this.instance = this;
    }

    /**
     * @param script  The script in a string, for example: smf.
     * @param version The version that the user has set in his config.
     * @throws com.craftfire.bifrost.exceptions.UnsupportedScript if the input string is not found in the list of supported scripts.
     * @throws UnsupportedVersion if the input version is not found in the list of supported versions.
     */
    public ScriptHandle(String script, String version, DataManager dataManager) throws UnsupportedScript,
            UnsupportedVersion {
        this.scriptName = ScriptAPI.stringToScript(script);
        this.version = version;
        this.dataManager = dataManager;
        this.script = ScriptAPI.setScript(this.scriptName, version, dataManager);
        if (!this.script.isSupportedVersion()) {
            throw new UnsupportedVersion();
        }
        this.instance = this;
    }

    /**
     * @return The Script object.
     */
    public Script getScript() {
        return this.script;
    }

    public ScriptHandle getHandle() {
        return this.instance;
    }

    public DataManager getDataManager() {
        return this.dataManager;
    }

    public Cache getCache() {
        return this.script.getCache();
    }

    public PrivateMessage newPrivateMessage(ScriptUser sender, List<ScriptUser> recipients) {
        return new PrivateMessage(this.script, sender, recipients);
    }

    public ScriptUser newScriptUser(String username, String password) {
        return new ScriptUser(this.script, username, password);
    }

    public String getLatestVersion() {
        return this.script.getLatestVersion();
    }

    public boolean isSupportedVersion() {
        return this.script.isSupportedVersion();
    }

    public String getVersion() throws UnsupportedFunction {
        return this.script.getVersion();
    }

    public String[] getVersionRanges() {
        return this.script.getVersionRanges();
    }

    public Encryption getEncryption() throws UnsupportedFunction {
        return this.script.getEncryption();
    }

    public String getScriptName() throws UnsupportedFunction {
        return this.script.getScriptName();
    }

    public String getScriptShortname() throws UnsupportedFunction {
        return this.script.getScriptShortname();
    }

    public boolean authenticate(String username, String password) throws UnsupportedFunction {
        return this.script.authenticate(username, password);
    }

    public String hashPassword(String salt, String password) throws UnsupportedFunction {
        return this.script.hashPassword(salt, password);
    }

    public String getUsername(int userid) throws UnsupportedFunction, SQLException {
        if (this.script.getCache().contains(CacheGroup.USER_USERNAME, userid)) {
            return (String) this.script.getCache().get(CacheGroup.USER_USERNAME, userid);
        }
        String username = this.script.getUsername(userid);
        this.script.getCache().put(CacheGroup.USER_USERNAME, username, userid);
        return username;
    }

    public int getUserID(String username) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.USER_ID, username)) {
            return (Integer) this.script.getCache().get(CacheGroup.USER_ID, username);
        }
        int userID = this.script.getUserID(username);
        this.script.getCache().put(CacheGroup.USER_ID, userID, username);
        return userID;
    }

    public ScriptUser getUser(String username) throws UnsupportedFunction, SQLException {
        int id = this.script.getUserID(username);
        return this.getUser(id);
    }

    public ScriptUser getUser(int userid) throws UnsupportedFunction, SQLException {
        if (ScriptUser.hasCache(this.getHandle(), userid)) {
            return ScriptUser.getCache(this, userid);
        }
        ScriptUser user = this.script.getUser(userid);
        ScriptUser.addCache(this, user);
        return user;
    }

    public ScriptUser getLastRegUser() throws UnsupportedFunction, SQLException {
        if (this.script.getCache().contains(CacheGroup.USER_LAST_REG)) {
            return (ScriptUser) this.script.getCache().get(CacheGroup.USER_LAST_REG);
        }
        ScriptUser user = this.script.getLastRegUser();
        this.script.getCache().put(CacheGroup.USER_LAST_REG, user);
        return user;
    }

    public void updateUser(ScriptUser user) throws SQLException, UnsupportedFunction {
        this.script.updateUser(user);
        ScriptUser.addCache(this, user);
    }

    public void createUser(ScriptUser user) throws SQLException, UnsupportedFunction {
        this.script.createUser(user);
        ScriptUser.addCache(this, user);
    }

    @SuppressWarnings("unchecked")
    public List<Group> getGroups(int limit) throws SQLException, UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.GROUP_LIST)) {
            return (List<Group>) this.script.getCache().get(CacheGroup.GROUP_LIST);
        }
        List<Group> groups = this.script.getGroups(limit);
        this.script.getCache().put(CacheGroup.GROUP_LIST, groups);
        return groups;
    }

    public int getGroupID(String group) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.GROUP_ID, group)) {
            return (Integer) this.script.getCache().get(CacheGroup.GROUP_ID, group);
        }
        int groupID = this.getGroupID(group);
        this.script.getCache().put(CacheGroup.GROUP_ID, groupID, group);
        return groupID;
    }

    public Group getGroup(int groupID) throws UnsupportedFunction {
        if (Group.hasCache(this, groupID)) {
            return Group.getCache(this, groupID);
        }
        Group group = this.script.getGroup(groupID);
        Group.addCache(this, group);
        return group;
    }

    public Group getGroup(String groupString) throws UnsupportedFunction {
        if (Group.hasCache(this, groupString)) {
            return Group.getCache(this, groupString);
        }
        Group group = this.script.getGroup(groupString);
        Group.addCache(this, group);
        return group;
    }

    @SuppressWarnings("unchecked")
    public List<Group> getUserGroups(String username) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.USER_GROUP, username)) {
            return (List<Group>) this.script.getCache().get(CacheGroup.USER_GROUP, username);
        }
        List<Group> groups = this.script.getUserGroups(username);
        this.script.getCache().put(CacheGroup.USER_GROUP, username, groups);
        return groups;
    }

    public void updateGroup(Group group) throws SQLException, UnsupportedFunction {
        this.script.updateGroup(group);
        Group.addCache(this, group);
    }

    public void createGroup(Group group) throws SQLException, UnsupportedFunction {
        this.script.createGroup(group);
        Group.addCache(this, group);
    }

    public PrivateMessage getPM(int pmid) throws UnsupportedFunction {
        if (PrivateMessage.hasCache(this, pmid)) {
            return PrivateMessage.getCache(this, pmid);
        }
        PrivateMessage pm = this.script.getPM(pmid);
        PrivateMessage.addCache(this, pm);
        return pm;
    }

    @SuppressWarnings("unchecked")
    public List<PrivateMessage> getPMsSent(String username, int limit) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.PM_SENT, username)) {
            return (List<PrivateMessage>) this.script.getCache().get(CacheGroup.PM_RECEIVED, username);
        }
        List<PrivateMessage> pms = this.script.getPMsSent(username, limit);
        this.script.getCache().put(CacheGroup.PM_SENT, username, pms);
        return pms;
    }

    @SuppressWarnings("unchecked")
    public List<PrivateMessage> getPMsReceived(String username, int limit) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.PM_RECEIVED, username)) {
            return (List<PrivateMessage>) this.script.getCache().get(CacheGroup.PM_RECEIVED, username);
        }
        List<PrivateMessage> pms = this.script.getPMsSent(username, limit);
        this.script.getCache().put(CacheGroup.PM_RECEIVED, username, pms);
        return pms;
    }

    public int getPMSentCount(String username) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.PM_SENT_COUNT, username)) {
            return (Integer) this.script.getCache().get(CacheGroup.PM_SENT_COUNT, username);
        }
        int count = this.script.getPMSentCount(username);
        this.script.getCache().put(CacheGroup.PM_SENT_COUNT, username, count);
        return count;
    }

    public int getPMReceivedCount(String username) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.PM_RECEIVED_COUNT, username)) {
            return (Integer) this.script.getCache().get(CacheGroup.PM_RECEIVED_COUNT, username);
        }
        int count = this.script.getPMReceivedCount(username);
        this.script.getCache().put(CacheGroup.PM_RECEIVED_COUNT, username, count);
        return count;
    }

    public void updatePrivateMessage(PrivateMessage privateMessage) throws SQLException, UnsupportedFunction {
        this.script.updatePrivateMessage(privateMessage);
        PrivateMessage.addCache(this, privateMessage);
    }

    public void createPrivateMessage(PrivateMessage privateMessage) throws SQLException, UnsupportedFunction {
        this.script.createPrivateMessage(privateMessage);
        PrivateMessage.addCache(this, privateMessage);
    }

    public int getUserCount() throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.USER_COUNT)) {
            return (Integer) this.script.getCache().get(CacheGroup.USER_COUNT);
        }
        int count = this.script.getUserCount();
        this.script.getCache().put(CacheGroup.USER_COUNT, count);
        return count;
    }

    public int getGroupCount() throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.GROUP_COUNT)) {
            return (Integer) this.script.getCache().get(CacheGroup.GROUP_COUNT);
        }
        int count = this.script.getGroupCount();
        this.script.getCache().put(CacheGroup.GROUP_COUNT, count);
        return count;
    }

    @SuppressWarnings("unchecked")
    public List<String> getIPs(String username) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.USER_IP, username)) {
            return (List<String>) this.script.getCache().get(CacheGroup.USER_IP, username);
        }
        List<String> ips = this.script.getIPs(username);
        this.script.getCache().put(CacheGroup.USER_IP, ips, username);
        return ips;
    }

    @SuppressWarnings("unchecked")
    public List<Ban> getBans(int limit) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.BAN_LIST)) {
            return (List<Ban>) this.script.getCache().get(CacheGroup.BAN_LIST);
        }
        List<Ban> bans = this.script.getBans(limit);
        this.script.getCache().put(CacheGroup.BAN_LIST, bans);
        return bans;
    }

    public void updateBan(Ban ban) throws SQLException, UnsupportedFunction {
        this.script.updateBan(ban);
        Ban.addCache(this, ban);
    }

    public void addBan(Ban ban) throws SQLException, UnsupportedFunction {
        this.script.addBan(ban);
        Ban.addCache(this, ban);
    }

    public int getBanCount() throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.BAN_COUNT)) {
            return (Integer) this.script.getCache().get(CacheGroup.BAN_COUNT);
        }
        int count = this.script.getBanCount();
        this.script.getCache().put(CacheGroup.BAN_COUNT, count);
        return count;
    }

    public boolean isBanned(String string) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.IS_BANNED, string)) {
            return (Boolean) this.script.getCache().get(CacheGroup.IS_BANNED, string);
        }
        boolean banned = this.script.isBanned(string);
        this.script.getCache().put(CacheGroup.IS_BANNED, banned, string);
        return banned;
    }

    public boolean isRegistered(String username) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.IS_REGISTERED, username)) {
            return (Boolean) this.script.getCache().get(CacheGroup.IS_REGISTERED, username);
        }
        boolean registered = this.script.isRegistered(username);
        this.script.getCache().put(CacheGroup.IS_REGISTERED, registered, username);
        return registered;
    }
}
