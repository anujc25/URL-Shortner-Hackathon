var mongoose = require('mongoose');


var trenddataSchema = new mongoose.Schema({
  shortenedURL: {
    type: String,
    required:true
  },
  browser: {
    type: String,
    required:true
  },
  country: {
    type: String,
    required:true
  },
  platform: {
    type: String,
    required:true
  },
  timestamp: {
    type: String,
    default:(new Date().getMonth()+1)+"-"+(new Date().getDate())+"-"+(new Date().getFullYear())
  },
  originalURL: {
    type: String,
    required:true
  }
},
{collection: 'trenddata'},
{versionKey: false})
;

module.exports = mongoose.model('trenddata', trenddataSchema);
