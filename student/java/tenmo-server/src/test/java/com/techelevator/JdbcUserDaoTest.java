package com.techelevator;

import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JdbcUserDaoTest extends BaseDaoTests{

    private JdbcUserDao user;

    @Before
    public void setup(){
        user = new JdbcUserDao(dataSource);
        user.create("user", "password");
        user.create("userTwo", "password");
    }


    @Test
    public void findIdByUsername_returns_correct_user_id(){
        int userId = user.findIdByUsername("user");
        int expected = 1003;
        Assert.assertEquals(expected, userId);
    }

   /*@Test
    public void findIdByUsername_return_negative_1_when_user_not_found(){
        int userId = user.findIdByUsername("dne");
        int expected = -1;
        Assert.assertEquals(expected, userId);
    }*/
    @Test
    public void findByUsername_returns_correct_user_object_based_on_input(){
        User actual = user.findByUsername("user");
        User expected = new User((long)1001, "user", "password", "");
        expected.setAuthorities("USER");
        Assert.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void create_creates_new_user_in_database(){
        Assert.assertTrue(user.create("test", "password"));
    }

    @Test
    public void findAll_returns_list_of_all_users_in_database(){
        List<User> users = user.findAll();
        String actual = users.toString();
        List<User> expected = new ArrayList<>();
        expected.add(user.findByUsername("user"));
        expected.add(user.findByUsername("userTwo"));
        String expectedResult = expected.toString();
        Assert.assertEquals(expectedResult, actual);
    }
}
