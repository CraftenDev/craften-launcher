package de.craften.craftenlauncher.logic.json;

import de.craften.craftenlauncher.logic.auth.LastLogin;
import de.craften.craftenlauncher.logic.auth.MinecraftUser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class JSONWriterTest {
    LastLogin login;

    @Before
    public void setUp() throws Exception {
        MinecraftUser
                user1 = new MinecraftUser("email_one@test.de", "54bnb5br_profileid_berb4b3","one","b43v4bvbvt_accesstoken_bhbghtr","km787men56_clienttoken_45nbbntvb"),
                user2 = new MinecraftUser("email_two@test.de", "7m67mnb_profileid_kmmmnu","two","wj5wzw5_accesstoken_sdnmenb5","7mnmn5nbbnt_clienttoken_ws5mnza"),
                user3 = new MinecraftUser("email_three@test.de", "ymtnbb43_profileid_qhj454n4a","three","emm7m_accesstoken_wmnw6na","aws4nbnb_clienttoken_aw4hbt");

        login = new LastLogin();
        login.clearAvailableUsers();
        login.addAvailableUser(user1);
        login.addAvailableUser(user2);
        login.addAvailableUser(user3);

        login.setSelectedUser(user1);
    }

    @Test
    public void testSaveLastLogin() throws Exception {
       JSONWriter.saveLastLogin(login);
       assertTrue("LastLogin wurde geschrieben",new File(login.getPath() + "lastLogin.json").exists());
    }

    @Test
    public void testLastLogin() throws Exception {
        assertTrue("Vorherige geschriebene lastlogin ist aktuell gelesene",JSONReader.readLastLogin(login.getPath()).equals(login));
    }
}