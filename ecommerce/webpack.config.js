const path = require('path');
const WebpackObfuscator = require('webpack-obfuscator');

module.exports = {
    entry: './src/index.js', // Entry point for your React app
    output: {
        path: path.resolve(__dirname, 'dist'),
        filename: 'bundle.js', // Output bundle
    },
    module: {
        rules: [
            {
                test: /\.jsx?$/, // Process JavaScript and JSX files
                exclude: /node_modules/,
                use: 'babel-loader', // Transpile modern JS/JSX
            },
        ],
    },
    module: {
        rules: [
          {
            test: /\.(woff|woff2|eot|ttf|otf)$/,
            use: [
              {
                loader: 'file-loader',
                options: {
                  name: 'assets/fonts/[name].[hash:8].[ext]'
                }
              }
            ]
          }
        ]
      },
    plugins: [
        new WebpackObfuscator({
            rotateStringArray: true, // Example option
        }, ['excluded-file.js']), // Exclude specific files if necessary
    ],
    resolve: {
        extensions: ['.js', '.jsx'], // File extensions to process
    },
};
