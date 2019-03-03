const path = require("path");
const VueLoaderPlugin = require("vue-loader/lib/plugin");
const UglifyJsPlugin = require("uglifyjs-webpack-plugin");
const webpack = require("webpack");

module.exports = env => {
	let clientPath = path.resolve(__dirname, "src/main/client");
	let outputPath = path.resolve(__dirname, env == "production" ? "src/main/webapp" : "out");
	return {
		mode: !env ? "development" : env,
		entry: {
			"assets/bundle/js/vendors": ["jquery", "vue", "axios", "vue-axios", "bootstrap", "vee-validate"],
			"assets/bundle/js/app/login": clientPath + "/js/app/login.js",
			"assets/bundle/js/app/code": clientPath + "/js/app/code.js",
			"assets/bundle/js/app/account": clientPath + "/js/app/account.js",
			"assets/bundle/js/app/category": clientPath + "/js/app/category.js",
			"assets/bundle/js/app/stat": clientPath + "/js/app/stat.js",
			"assets/bundle/js/app/settlement": clientPath + "/js/app/settlement.js",
			"assets/bundle/js/app/loginUserEdit": clientPath + "/js/app/loginUserEdit.js",
			"assets/bundle/js/app/grid": clientPath + "/js/app/grid.js",
			"assets/bundle/js/app/calendar": clientPath + "/js/app/calendar.js"
		},
		devtool: false,
		output: {
			path: outputPath,
			filename: "[name].js",
			pathinfo: true
		},
		optimization: {
			splitChunks: {
				chunks: "all",
				cacheGroups: {
					vendors: {
						test: /[\\/]node_modules[\\/]/,
						name: "assets/bundle/js/vendors"
					}
				}
			}
		},
		devServer: {
			contentBase: outputPath,
			publicPath: "/",
			host: "0.0.0.0",
			port: 8081,
			proxy: {
				"**": "http://127.0.0.1:8080"
			},
			inline: true,
			hot: false
		},
		module: {
			rules: [
				{
					test: /\.js$/,
					use: [
						{
							loader: "babel-loader",
							options: {
								presets: "env"
							}
						}
					]
				},
				{
					test: /\.vue$/,
					loader: "vue-loader"
				},
				{
					test: /\.css$/,
					use: ["style-loader", "css-loader"]
				}
			]
		},
		resolve: {
			alias: {
				vue$: "vue/dist/vue.esm.js"
			},
			extensions: [".js", ".css", ".vue"]
		},
		plugins: [
			new VueLoaderPlugin(),
			new webpack.SourceMapDevToolPlugin({
				filename: "[name].js.map"
			})
		]
	};
};
