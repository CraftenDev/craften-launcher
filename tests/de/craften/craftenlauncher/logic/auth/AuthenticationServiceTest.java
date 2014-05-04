package de.craften.craftenlauncher.logic.auth;

import de.craften.craftenlauncher.logic.json.JSONReader;
import de.craften.craftenlauncher.logic.json.JSONWriter;
import de.craften.craftenlauncher.logic.minecraft.MinecraftPath;
import de.craften.craftenlauncher.logic.minecraft.MinecraftPathImpl;
import de.craften.util.OSHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by redbeard on 04.05.14.
 */
public class AuthenticationServiceTest {

    private List<MinecraftUser> expected;
    private AuthenticationService service;


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

        expected = new ArrayList<MinecraftUser>();
        expected.add(user1);
        expected.add(user2);
        expected.add(user3);

    }

    @After
    public void deleteLastLogin() {
        File login = new File(OSHelper.getInstance().getMinecraftPath() + "lastLogin.json");

        login.delete();
    }

    @Test
    public void testGetAllUsers() {
        givenAuthServWithMCPath();

        List<MinecraftUser> actual = whenGetUsersIsCalled();

        assertThat(actual, is(expected));
    }

    private void givenAuthServWithMCPath() {
        MinecraftPath path = new MinecraftPathImpl();
        service = new AuthenticationService(path);
    }

    private List<MinecraftUser> whenGetUsersIsCalled() {
        return service.getUsers();
    }

    @Test
    @Ignore("Should be activated when mocking is available or user/pass is given!")
    public void testGetUsersAfterNewGetSessionID() {
        givenNewAuthService();

        List<MinecraftUser> actual = whenGetUsersIsCalled();

        // assertThat should be used when mocking is available.
        // assertThat(actual, is(expected));
        assertEquals(actual.size(),expected.size());
    }

    private void givenNewAuthService() {
        expected = new ArrayList<MinecraftUser>();
        expected.add(new MinecraftUser());

        service = new AuthenticationService();

        service.setMcPath(new MinecraftPathImpl());

        // Username and Password need to be given.
        service.getSessionID("","");
    }
}