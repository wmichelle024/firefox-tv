const path = require('path');

module.exports = {
    entry: ['./src/index.js'],
    output: {
      path: __dirname + '/app/src/main/res/raw',
      filename: 'extract.js'
    },
    mode: 'development',
    module: {
             rules : [
                   // JavaScript/JSX Files
                   {
                     test: /\.js$/,
                     exclude: /node_modules/,
                     use: ['babel-loader'],
                   },
               ]
         },
};
