
Feature('Sign Up');

Scenario('User should be able to sign up successfully', async ({ I }) => {
  I.amOnPage('/signup');

  I.fillField('Username', 'SampleU'); 
  I.fillField('Email Address', 'sampler@example.com'); 
  I.fillField('Address', '123 New Street'); 
  I.fillField('Password', 'admin123');

  I.click('Sign Up'); 
  I.waitForText('User registered', 5); 
  I.see('User registered', '.swal2-popup');

  I.seeInCurrentUrl('/home');
});

Scenario('Show validation errors when fields are invalid', async ({ I }) => {
  I.amOnPage('/signup'); 

  I.fillField('Username', 'username78'); // Fill with invalid username
  I.fillField('Email Address', 'invalidemail'); // Fill with invalid email
  I.fillField('Address', 'A very long address that exceeds the limit very long passordsb jjdsf fjhsfb jdfb'); // Fill with long address
  I.fillField('Password', 'short'); // Fill with invalid password

  I.click('Sign Up'); // Click the sign-up button

  // Assert that the validation errors appear
  I.waitForText('Username must be less than 8 characters.', 5);
  I.see('Username must be less than 8 characters.', '#username');

  I.waitForText('Email must be a valid email address.', 5);
  I.see('Email must be a valid email address.', '#email');

  I.waitForText('Address must be less than 30 characters.', 5);
  I.see('Address must be less than 30 characters.', '#address');

  I.waitForText('Password must be at least 8 characters long and include both letters and numbers.', 5);
  I.see('Password must be at least 8 characters long and include both letters and numbers.', '#password');
});

Scenario('Handle signup failure due to existing user', async ({ I }) => {
  I.amOnPage('/signup'); 

  I.mockRequest({
    method: 'POST',
    url: '/api/v1/auth/register',
    response: {
      statusCode: 400
    }
  });

  I.fillField('Username', 'abc');
  I.fillField('Email Address', 'abc@example.com'); 
  I.fillField('Address', '123 Existing Street'); 
  I.fillField('Password', 'admin123'); 

  I.click('Sign Up'); 

  // Assert that the error alert appears
  I.waitForText('User is already registered.', 5); // Wait up to 5 seconds for the text
  I.see('User is already registered.', '.swal2-popup');
});
