var express = require('express');
var bodyParser = require('body-parser');
var app = express();
var db = require('./db');
var cookieParser = require('cookie-parser');
var session = require('express-session');
var path = require('path');
var AWS = require('aws-sdk');
// Set the region 
AWS.config.update({region: 'us-west-1'});

var morgan = require('morgan');

var UrlController = require('./url/UrlController');

app.set('view engine','html');
app.set('views',__dirname + '/views');

app.use(function(req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
  next();
});


app.use(bodyParser.json());
app.use(morgan('dev'));
app.use(cookieParser());

app.use('/', UrlController);

module.exports = app;