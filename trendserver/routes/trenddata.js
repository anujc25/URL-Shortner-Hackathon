var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');
var TrendData = require('../model/trenddata.js');
var geoip = require('geoip-lite');


/* Display the trend data */
router.route('/')
    .get(function(req, res, next) {

        TrendData.find({}, function (err, result) {
              if (err) {
                  res.send('There was an error displaying the collection');
              } else if(result.length) {
                  res.send(result);
              }
              else {
                res.send('No documents available');
              }
        });
    })
/* Adding a trend data entry to the database */
    .post(function(req, res, next) {

        var shortenedURL = req.body.shortenedURL,
        originalURL = req.body.originalURL,
        browser = req.body.browser,
        ip = req.body.ip,
        country = req.body.country,
        platform = req.body.platform;

        var geo = geoip.lookup(ip);
        var country = geo.country;

        TrendData.create({
          shortenedURL : shortenedURL,
          browser : browser,
          country : country,
          platform : platform
        }, function(err,result){
          if(err) {
            res.send("There was a problem adding info to DB");
          } else {
            console.log("Creating new trend data entry" +result);
            res.json(result);
          }
        })

    });

    router.route('/:shortenedURL').get(function(req,res,next){
      TrendData.find(
    {shortenedURL:req.params.shortenedURL}
  , function (err, result) {
            if (err) {
                res.send('There was an error displaying the collection');
            } else if(result.length) {
                res.send(result);
            }
            else {
              res.send('No documents available');
            }
      });
    });

  //   router.route('/:shortenedURL/:type').get(function(req,res,next){
  //     console.log(req.params.type);
  //
  //     TrendData.find({$and:[
  //   {shortenedURL:req.params.shortenedURL},
  //   {country:req.params.type}
  // ]},{"country":1}
  //
  // , function (err, result) {
  //           if (err) {
  //               res.send('There was an error displaying the collection');
  //           } else if(result.length) {
  //               res.send(result);
  //           }
  //           else {
  //             res.send('No documents available');
  //           }
  //     });
  //   });

    router.route('/:shortenedURL/:type').get(function(req,res,next){

      if(req.params.type=='country'){
        TrendData.aggregate([
          { $match: {
              shortenedURL: req.params.shortenedURL
          }},
          { $group: {
              _id: "$country", count:{$sum:1}
          }},
          { $project: {
              _id: 0,
              country:"$_id",
              clicks: "$count"
          }}
      ], function (err, result) {
          if (err) {
              res.send('There was an error displaying the country records');
          }
          res.send(result);
      });
      }

      else if(req.params.type=='browser'){
        TrendData.aggregate([
          { $match: {
              shortenedURL: req.params.shortenedURL
          }},
          { $group: {
              _id: "$browser", count:{$sum:1}
          }},
          { $project: {
              _id: 0,
              browser:"$_id",
              clicks: "$count"
          }}
      ], function (err, result) {
          if (err) {
              res.send('There was an error displaying the country records');
          }
          res.send(result);
      });
      }

      else if(req.params.type=='platform'){
        TrendData.aggregate([
          { $match: {
              shortenedURL: req.params.shortenedURL
          }},
          { $group: {
              _id: "$platform", count:{$sum:1}
          }},
          { $project: {
              _id: 0,
              platform:"$_id",
              clicks: "$count"
          }}
      ], function (err, result) {
          if (err) {
              res.send('There was an error displaying the country records');
          }
          res.send(result);
      });
      }

      else if(req.params.type=='dailyvisits'){
        TrendData.aggregate([
          { $match: {
              shortenedURL: req.params.shortenedURL
          }},
          { $group: {
              _id: "$timestamp", count:{$sum:1}
          }},
          { $project: {
              _id: 0,
              date:"$_id",
              clicks: "$count"
          }}
      ], function (err, result) {
          if (err) {
              res.send('There was an error displaying the country records');
          }
          res.send(result);
      });
      }

      else if(req.params.type=='info'){
         TrendData.aggregate([
           { $match: {
               shortenedURL: req.params.shortenedURL
           }},
           { $group: {
               _id: "$originalURL", count:{$sum:1}
           }},
           { $project: {
               _id: 0,
               originalURL:"$_id",
               views: "$count"
           }}
       ], function (err, result) {
           if (err) {
               res.send('There was an error displaying the country records');
           }
           res.send(result);
       });
       }

    });


module.exports = router;
