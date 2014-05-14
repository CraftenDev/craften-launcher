package de.craften.craftenlauncher.logic.auth;

import org.junit.Test;

import static org.junit.Assert.*;

public class MinecraftUserTest {

    @Test
    public void testEquals() throws Exception {
        MinecraftUser user1 = new MinecraftUser("email_one@test.de", "54bnb5br_profileid_berb4b3","one","b43v4bvbvt_accesstoken_bhbghtr","km787men56_clienttoken_45nbbntvb");
        MinecraftUser user2 = new MinecraftUser("email_one@test.de", "54bnb5br_profileid_berb4b3","one","b43v4bvbvt_accesstoken_bhbghtr","km787men56_clienttoken_45nbbntvb");

        assertEquals(user1,user2);
    }

    @Test
    public void testNotEquals() throws Exception {
        MinecraftUser user1 = new MinecraftUser("email_one@test.de", "54bnb5br_profileid_berb4b3","one","b43v4bvbvt_accesstoken_bhbghtr","km787men56_clienttoken_45nbbntvb");
        MinecraftUser user2 = new MinecraftUser("email_two@test.de", "7m67mnb_profileid_kmmmnu","two","wj5wzw5_accesstoken_sdnmenb5","7mnmn5nbbnt_clienttoken_ws5mnza");

        assertNotEquals(user1,user2);
    }
}