package de.craften.craftenlauncher.logic.auth;

import de.craften.craftenlauncher.logic.json.JSONWriter;

import java.util.ArrayList;

public class Profiles {
    private MinecraftUser mSelectedUser;
    private ArrayList<MinecraftUser> mAvailableUsers;
    private String mPath;

    public Profiles() {
        mAvailableUsers = new ArrayList<MinecraftUser>();
    }

    /**
     * @param selectedUser set the current selected user
     */
    public void setSelectedUser(MinecraftUser selectedUser) {
        this.mSelectedUser = selectedUser;
    }

    /**
     * @param path set the minecraft path
     */
    public void setPath(String path) {
        this.mPath = path;
    }

    /**
     * @return current minecraft path
     */
    public String getPath() {
        return mPath;
    }

    /**
     * @return current selected user
     */
    public MinecraftUser getSelectedUser() {
        return mSelectedUser;
    }

    /**
     * @return all saved users
     */
    public ArrayList<MinecraftUser> getAvailableUsers() {
        return mAvailableUsers;
    }

    /**
     * Returns a user with a given index
     *
     * @param i the index we want our user to be
     * @return a user at index i
     */
    public MinecraftUser getAvailableUser(int i) {
        return getAvailableUsers().get(i);
    }

    /**
     * Returns a user with a given id
     *
     * @param id the id from a user we want
     * @return a user with given id or null if not found
     */
    public MinecraftUser getAvailableUser(String id) {
        for (MinecraftUser minecraftUser : mAvailableUsers) {
            if (minecraftUser.getProfileId().equals(id)) {
                return minecraftUser;
            }
        }
        return null;
    }

    /**
     * Adds a new user to our saved list
     *
     * @param user the user we want to save
     */
    public void addAvailableUser(MinecraftUser user) {
        mAvailableUsers.add(user);
    }

    /**
     * Ändert den SelectedUser zum dem User mit der übergebenen profileID.
     *
     * @param profileID Eindeutiger Qualifier des Users
     */
    public void changeSelectedUser(String profileID) {
        for (MinecraftUser user : mAvailableUsers) {
            if (user.getProfileId().equals(profileID)) {
                mSelectedUser = user;
            }
        }
    }

    /**
     * Removes a user with a given index
     *
     * @param i the id from a user we want to remove
     */
    public MinecraftUser removeAvailableUser(int i) {
        return getAvailableUsers().remove(i);
    }

    /**
     * Removes a user with a given id
     *
     * @param id the id from a user we want to remove
     */
    public boolean removeAvailableUser(String id) {
        for (int i = 0; i < mAvailableUsers.size(); i++) {
            MinecraftUser minecraftUser = mAvailableUsers.get(i);

            if (minecraftUser.getProfileId().equals(id)) {
                mAvailableUsers.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes all users in the list
     */
    public void clearAvailableUsers() {
        mAvailableUsers.clear();
    }

    /**
     * Clears the selected user
     */
    public void clearSelectedUser() {
        mSelectedUser = new MinecraftUser("", "");
    }

    /**
     * Saves the stored users to disk
     */
    public void save() {
        JSONWriter.saveProfiles(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Profiles)) {
            return false;
        }

        Profiles a = (Profiles) obj;

        if (((a.getSelectedUser() == null && this.getSelectedUser() == null) || a.getSelectedUser().equals(this.getSelectedUser()))
                && a.getAvailableUsers().size() == getAvailableUsers().size()) {
            for (int i = 0; i < a.getAvailableUsers().size(); i++) {
                MinecraftUser minecraftUser = a.getAvailableUsers().get(i);
                if (!minecraftUser.equals(this.getAvailableUser(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getAvailableUsers().hashCode();
    }
}
