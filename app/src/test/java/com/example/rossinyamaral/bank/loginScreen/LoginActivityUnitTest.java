package com.example.rossinyamaral.bank.loginScreen;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class LoginActivityUnitTest {

    @Test
    public void LoginActivity_ShouldNOT_be_Null(){
        //Given
        LoginActivity activity = Robolectric.setupActivity(LoginActivity.class);

        //When

        // Then
        Assert.assertNotNull(activity);
    }

    @Test
    public void onCreate_shouldCall_fetchLoginMetaData(){
        //Given
        LoginActivityOutputSpy loginActivityOutputSpy = new LoginActivityOutputSpy();
        LoginActivity loginActivity = Robolectric.setupActivity(LoginActivity.class);

        // It must have called the onCreate earlier,
        // we are injecting the mock and calling the fetchMetaData to test our condition
        loginActivity.output = loginActivityOutputSpy;

        //When
        loginActivity.fetchUserData(null);

        //Then
        Assert.assertTrue(loginActivityOutputSpy.fetchLoginMetaDataIsCalled);
    }

    @Test
    public void onCreate_Calls_fetchLoginMetaData_withCorrectData(){
        //Given
        LoginActivityOutputSpy LoginActivityOutputSpy = new LoginActivityOutputSpy();
        LoginActivity loginActivity = Robolectric.setupActivity(LoginActivity.class);
        loginActivity.output = LoginActivityOutputSpy;

        //When
        LoginRequest request = new LoginRequest();
        request.user = "test";
        request.password = "test";
        loginActivity.fetchUserData(request);

        //Then
        Assert.assertNotNull(loginActivity);
        Assert.assertEquals(LoginActivityOutputSpy.loginRequestCopy, request);
    }

    /*@Test
    public void checkUppercaseLetter() {
        LoginActivity activity = Robolectric.setupActivity(LoginActivity.class);
        boolean result;
        result = activity.hasUppercaseLetter("Aaaa");
        Assert.assertTrue(result);
        result = activity.hasUppercaseLetter("aaa");
        Assert.assertFalse(result);
        result = activity.hasUppercaseLetter("asdf1234");
        Assert.assertFalse(result);
    }

    @Test
    public void checkAlphanumericCharacter() {
        LoginActivity activity = Robolectric.setupActivity(LoginActivity.class);
        boolean result;
        result = activity.hasAlphanumericCharacter("Aaaa");
        Assert.assertTrue(result);
        result = activity.hasAlphanumericCharacter("ASD@#");
        Assert.assertFalse(result);
        result = activity.hasAlphanumericCharacter("###");
        Assert.assertFalse(result);
    }

    @Test
    public void checkSpecialCharacter() {
        LoginActivity activity = Robolectric.setupActivity(LoginActivity.class);
        boolean result;
        result = activity.hasSpecialCharacter("Aa[aa");
        Assert.assertTrue(result);
        result = activity.hasSpecialCharacter("aaa");
        Assert.assertFalse(result);
        result = activity.hasSpecialCharacter("asdf1234");
        Assert.assertFalse(result);
    }

    @Test
    public void checkPassword() {
        LoginActivity activity = Robolectric.setupActivity(LoginActivity.class);
        boolean result;
        result = activity.checkPassword("Aa[aa");
        Assert.assertTrue(result);
        result = activity.checkPassword("aaa");
        Assert.assertFalse(result);
        result = activity.checkPassword("asdf1234");
        Assert.assertFalse(result);
    }*/

    private class LoginActivityOutputSpy implements LoginInteractorInput {

        boolean fetchLoginMetaDataIsCalled = false;
        LoginRequest loginRequestCopy;
        @Override
        public void fetchLoginData(LoginRequest request) {
            fetchLoginMetaDataIsCalled = true;
            loginRequestCopy = request;
        }
    }
}
