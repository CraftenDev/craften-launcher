package de.craften.craftenlauncher.logic.json;

import de.craften.craftenlauncher.logic.auth.Profiles;
import de.craften.craftenlauncher.logic.auth.MinecraftUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class JSONWriterTest {
    Profiles prof1, prof2;
    MinecraftUser user1, user2, user3;

    @Before
    public void setUp() throws Exception {
        user1 = new MinecraftUser("email_one@test.de", "54bnb5br_profileid_berb4b3", "one", "b43v4bvbvt_accesstoken_bhbghtr", "km787men56_clienttoken_45nbbntvb");
        user2 = new MinecraftUser("email_two@test.de", "7m67mnb_profileid_kmmmnu", "two", "wj5wzw5_accesstoken_sdnmenb5", "7mnmn5nbbnt_clienttoken_ws5mnza");
        user3 = new MinecraftUser("email_three@test.de", "ymtnbb43_profileid_qhj454n4a", "three", "emm7m_accesstoken_wmnw6na", "aws4nbnb_clienttoken_aw4hbt");

        prof1 = new Profiles();
        prof1.clearAvailableUsers();
        prof1.addAvailableUser(user1);
        prof1.addAvailableUser(user2);
        prof1.addAvailableUser(user3);

        prof1.setSelectedUser(user1);

        prof2 = new Profiles();
        prof2.clearAvailableUsers();
        prof2.addAvailableUser(user1);
        prof2.addAvailableUser(user2);
        prof2.addAvailableUser(user3);

        prof2.setSelectedUser(user1);

        JSONWriter.saveProfiles(prof1);
    }

    @After
    public void deleteProfiles() {
        File login = new File(prof1.getPath() + "craftenlauncher_profiles.json");
        login.delete();
    }

    @Test
    public void testJsonCreated() throws Exception {
        assertTrue(new File(prof1.getPath() + "craftenlauncher_profiles.json").exists());
    }

    @Test
    @Ignore
    public void testEquals_onFile() throws Exception {
        Profiles dummy = JSONReader.readProfiles(prof1.getPath());
        assertEquals(dummy, prof1);
    }

    @Test
    public void testEquals_onObject() throws Exception {
        assertEquals(prof1, prof2);
    }

    @Test
    @Ignore
    public void testNotEquals_onFile() throws Exception {
        Profiles dummy = JSONReader.readProfiles(prof1.getPath());
        dummy.setSelectedUser(user3);
        assertNotEquals(dummy, prof2);
    }

    @Test
    public void testNotEquals_onObject() throws Exception {
        prof2.setSelectedUser(user3);
        assertNotEquals(prof1, prof2);
    }
}