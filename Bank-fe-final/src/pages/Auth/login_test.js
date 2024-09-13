Feature('Login');

Scenario('User should be able to login successfully', async ({ I }) => {
  // Mock the API response for a successful login
  I.mockRequest({
    method: 'POST',
    url: '/api/v1/auth/login',
    response: {
      statusCode: 200,
      body: {
        accountId: '123',
        role: 'USER',
        userId: '456',
        token: 'abcdef',
        username: 'testuser'
      }
    }
  });

  I.amOnPage('/login'); // Navigate to the login page

  I.fillField('Username', 'testuser');
  I.fillField('Password', 'password'); // Fill the password field
  I.click('Login'); 

  // Check if the success alert appears
  I.see('Login successful', '.swal2-popup'); 

  // Verify redirection
  I.seeInCurrentUrl('/dashboard');

  // Check if cookies are set correctly
  I.seeCookie('token', 'abcdef');
  I.seeCookie('userId', '456');
  I.seeCookie('accountId', '123');
  I.seeCookie('username', 'testuser');
});

Scenario('Show error when fields are empty', async ({ I }) => {
  I.amOnPage('/login'); // Navigate to the login page

  I.click('Login'); // Click the login button without filling fields

  // Assert that the error alert appears
  I.see('Please fill in all fields.', '.swal2-popup');
});

Scenario('Handle login failure', async ({ I }) => {
  // Mock the API request to fail
  I.mockRequest({
    method: 'POST',
    url: '/api/v1/auth/login',
    response: {
      statusCode: 401, // Use 401 for unauthorized
      body: {
        message: 'Incorrect username or password.'
      }
    }
  });

  I.amOnPage('/login'); // Navigate to the login page

  I.fillField('Username', 'testuser'); // Fill the username field
  I.fillField('Password', 'password'); // Fill the password field
  I.click('Login'); // Click the login button

  // Assert that the failure alert appears
  I.see('Login failed', '.swal2-popup');
});

Scenario('Check cookies are set correctly', async ({ I }) => {
  // Mock successful login response
  I.mockRequest({
    method: 'POST',
    url: '/api/v1/auth/login',
    response: {
      statusCode: 200,
      body: {
        accountId: '123',
        role: 'USER',
        userId: '456',
        token: 'abcdef',
        username: 'testuser'
      }
    }
  });

  I.amOnPage('/login'); // Navigate to the login page

  I.fillField('Username', 'testuser'); // Fill the username field
  I.fillField('Password', 'password'); // Fill the password field
  I.click('Login'); // Click the login button

  // Verify cookies are set correctly
  I.seeCookie('token', 'abcdef');
  I.seeCookie('userId', '456');
  I.seeCookie('accountId', '123');
  I.seeCookie('username', 'testuser');
});
