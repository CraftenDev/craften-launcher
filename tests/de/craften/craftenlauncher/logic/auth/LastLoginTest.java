package de.craften.craftenlauncher.logic.auth;

import de.craften.craftenlauncher.logic.json.JSONWriter;
import de.craften.craftenlauncher.logic.minecraft.MinecraftPathImpl;
import de.craften.util.OSHelper;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by redbeard on 04.05.14.
 */
public class LastLoginTest {

    private MinecraftUser expected;
    private LastLogin login;

    @Before
    public void setUpLastLogin() {
        MinecraftUser
                user1 = new MinecraftUser("email_one@test.de", "54bnb5br_profileid_berb4b3","one","b43v4bvbvt_accesstoken_bhbghtr","km787men56_clienttoken_45nbbntvb"),
                user2 = new MinecraftUser("email_two@test.de", "7m67mnb_profileid_kmmmnu","two","wj5wzw5_accesstoken_sdnmenb5","7mnmn5nbbnt_clienttoken_ws5mnza"),
                user3 = new MinecraftUser("email_three@test.de", "ymtnbb43_profileid_qhj454n4a","three","emm7m_accesstoken_wmnw6na","aws4nbnb_clienttoken_aw4hbt");

        LastLogin login = new LastLogin();
        login.clearAvailableUsers();
        login.addAvailableUser(user1);
        login.addAvailableUser(user2);
        login.addAvailableUser(user3);

        login.setSelectedUser(user1);

        JSONWriter.saveLastLogin(login);

        expected = user2;
    }

    @After
    public void deleteLastLogin() {
        File login = new File(OSHelper.getInstance().getMinecraftPath() + "lastLogin.json");

        login.delete();
    }

    @Test
    public void testChangeSelectedUser() throws Exception {
        givenLastLogin();
        whenChangeSelectedUserIsCalled();

        assertEquals(login.getSelectedUser(),expected);
    }

    private void givenLastLogin() {
        AuthenticationService service = new AuthenticationService();
        service.setMcPath(new MinecraftPathImpl());
        login = service.readLastLogin();
    }

    private void whenChangeSelectedUserIsCalled() {
        login.changeSelectedUser("7m67mnb_profileid_kmmmnu");
    }
}