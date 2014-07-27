/**
 * CraftenLauncher is an alternative Launcher for Minecraft developed by Mojang.
 * Copyright (C) 2013  Johannes "redbeard" Busch, Sascha "saschb2b" Becker
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Profiles class:
 *
 * @author saschb2b
 */
package de.craften.craftenlauncher.logic.auth;

import de.craften.craftenlauncher.logic.json.JSONWriter;

import java.util.ArrayList;

public class Profiles {
    private MinecraftUser selectedUser;
    private ArrayList<MinecraftUser> availableUsers;
    private String mPath;

    public Profiles() {
        availableUsers = new ArrayList<MinecraftUser>();
    }

    /**
     *
     * @param selectedUser set the current selected user
     */
    public void setSelectedUser(MinecraftUser selectedUser) {
        this.selectedUser = selectedUser;
    }

    /**
     *
     * @param path set the minecraft path
     */
    public void setPath(String path) {
        this.mPath = path;
    }

    /**
     *
     * @return current minecraft path
     */
    public String getPath() {
        return mPath;
    }

    /**
     *
     * @return current selected user
     */
    public MinecraftUser getSelectedUser() {
        return selectedUser;
    }

    /**
     *
     * @return all saved users
     */
    public ArrayList<MinecraftUser> getAvailableUsers() {
        return availableUsers;
    }

    /**
     * Returns a user with a given index
     * @param i the index we want our user to be
     * @return a user at index i
     */
    public MinecraftUser getAvailableUser(int i) {
        return getAvailableUsers().get(i);
    }

    /**
     * Returns a user with a given id
     * @param id the id from a user we want
     * @return a user with given id or null if not found
     */
    public MinecraftUser getAvailableUser(String id) {
        for (MinecraftUser minecraftUser : availableUsers) {
            if (minecraftUser.getProfileId().equals(id)) {
                return minecraftUser;
            }
        }
        return null;
    }

    /**
     * Adds a new user to our saved list
     * @param user the user we want to save
     */
    public void addAvailableUser(MinecraftUser user) {
        availableUsers.add(user);
    }

    /**
     * Ändert den SelectedUser zum dem User mit der übergebenen profileID.
     * @param profileID Eindeutiger Qualifier des Users
     */
    public void changeSelectedUser(String profileID) {
        for(MinecraftUser user : availableUsers) {
            if(user.getProfileId().equals(profileID)) {
                selectedUser = user;
            }
        }
    }

    /**
     * Removes a user with a given index
     * @param i the id from a user we want to remove
     */
    public MinecraftUser removeAvailableUser(int i) {
        return getAvailableUsers().remove(i);
    }

    /**
     * Removes a user with a given id
     * @param id the id from a user we want to remove
     */
    public boolean removeAvailableUser(String id) {
        for (int i = 0; i < availableUsers.size(); i++) {
            MinecraftUser minecraftUser = availableUsers.get(i);

            if (minecraftUser.getProfileId().equals(id)) {
                availableUsers.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes all users in the list
     */
    public void clearAvailableUsers() {
        availableUsers.clear();
    }

    /**
     * Clears the selected user
     */
    public void clearSelectedUser(){
        selectedUser = new MinecraftUser("","");
    }

    /**
     * Saves the stored users to disk
     */
    public void save() {
        JSONWriter.saveProfiles(this);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Profiles))
            return false;

        Profiles a = (Profiles) obj;
        Boolean flag = false;


        if ((a.getSelectedUser() == null && this.getSelectedUser() == null) || a.getSelectedUser().equals(this.getSelectedUser())) {
            flag = true;
        }

        if (a.getAvailableUsers().size() > 0) {
            for (int i = 0; i < a.getAvailableUsers().size(); i++) {
                MinecraftUser minecraftUser = a.getAvailableUsers().get(i);

                if (!minecraftUser.equals(this.getAvailableUser(i))) {
                    flag = false;
                    break;
                }
            }
        }

        if ((a.getAvailableUsers().size() == 0) && (this.getAvailableUsers().size() == 0)) {
            flag = true;
        }
        return flag;
    }
}
