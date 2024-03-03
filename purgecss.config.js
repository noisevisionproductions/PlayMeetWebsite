// purgecss.config.js
module.exports = {
  content: ['src/main/resources/templates/**/*.html', 'src/main/resources/static/js/**/*.js'], // Adjust if your JS files are in a different directory
  css: ['src/main/resources/static/css/**/*.css'], // Adjust to where your CSS files are located
  output: 'src/main/resources/static/css/cleaned/', // The directory where you want the cleaned CSS to go
  // Any other options you want to set...
};
