Feature('Sign Up');

Scenario('User should be able to sign up successfully', async ({ I }) => {
  I.amOnPage('/signup');
  I.fillField('Username', 'testuser');
  I.fillField('Email Address', 'testuser@example.com');
  I.fillField('Address', '123 Test St');
  I.fillField('Password', 'TestPassword123');
  I.click('Sign Up');
  I.waitForText('User registered', 5);
  I.see('User registered');
});


