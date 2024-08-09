Feature('Login');

Scenario('User should be able to login successfully', async ({ I }) => {
  I.amOnPage('/login'); // Navigate to the login page

  I.fillField('Username', 'abc');
  I.fillField('Password', 'admin123');
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

// Scenario('Show error when fields are empty', async ({ I }) => {
//   I.amOnPage('/login'); // Navigate to the login page

//   I.click('Login'); // Click the login button without filling fields

//   // Assert that the error alert appears
//   I.see('Please fill in all fields.', '.swal2-popup');
// });

Scenario('Handle login failure', async ({ I }) => {
  I.amOnPage('/login');

  I.fillField('Username', 'testu');
  I.fillField('Password', 'wrongp'); 
  I.click('Login'); 

  I.see('Login failed', '.swal2-popup');
});

Scenario('Check cookies are set correctly', async ({ I }) => {
  I.amOnPage('/login'); // Navigate to the login page

  I.fillField('Username', 'abc');
  I.fillField('Password', 'admin123');
  I.click('Login'); // Click the login button

  // Verify cookies are set correctly
  I.seeCookie('token');
  I.seeCookie('userId');
  I.seeCookie('accountId');
  I.seeCookie('username');
});
