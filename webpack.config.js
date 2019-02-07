const path = require('path');
const VueLoaderPlugin = require('vue-loader/lib/plugin');
const UglifyJsPlugin = require('uglifyjs-webpack-plugin')

module.exports = (env) => {
	let clientPath = path.resolve(__dirname, 'src/main/client');
	let outputPath = path.resolve(__dirname, (env == 'production') ? 'src/main/webapp' : 'out')
	return {
		mode: !env ? 'development' : env,
		entry: {
			"assets/bundle/js/vendors": ['jquery', 'vue', 'axios', 'vue-axios', 'bootstrap'],
			"assets/bundle/js/login": clientPath + '/js/login.js',
		},
		devtool: 'source-map',
		output: {
			path: outputPath,
			filename: '[name].js',
			sourceMapFilename: "[name].js.map",
			pathinfo: true
		},
		optimization: {
			splitChunks: {
				chunks: 'all',
				cacheGroups: {
					vendors: {
						test: /[\\/]node_modules[\\/]/,
						name: 'assets/bundle/js/vendors'
					}
				}
			},
			minimizer: (env == 'production') ? [
				new UglifyJsPlugin(),
			] : []
		},
		devServer: {
			contentBase: outputPath,
			publicPath: '/',
			host: '0.0.0.0',
			port: 8081,
			proxy: {
				'**': 'http://127.0.0.1:8080'
			},
			inline: true,
			hot: false
		},
		module: {
			rules: [
				{
					test: /\.js$/,
					use: [{
						loader: 'babel-loader',
						options: {
							presets: 'env'
						}
					}]
				}, {
					test: /\.vue$/,
					loader: 'vue-loader'
				}
			]
		},
		resolve: {
			alias: {
				vue$: 'vue/dist/vue.esm.js'
			},
			extensions: ['.js', '.sass', '.scss', '.css', '.vue']
		},
		plugins: [
			new VueLoaderPlugin(),
		]
	}
}