package cpme281.hackathon.lrserver.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cmpe281.hackathon.lrserver.constant.AWSEndPointConstants;
import cmpe281.hackathon.lrserver.dao.URLShortnerDAO;
import cpme281.hackathon.lrserver.listener.TrendServerUpdater;
import redis.clients.jedis.Jedis;

@RestController
@CrossOrigin
public class LRServerController {
	
	 @Autowired
	 @Qualifier("urlShortnerDAO")
	 URLShortnerDAO urlShortnerDAO;
	
	 @RequestMapping(value = "/{id}", method = RequestMethod.GET)
	 public void performLinkRedirection(HttpServletRequest request, HttpServletResponse response, 
			 @RequestHeader(value="User-Agent") String userAgent, @PathVariable String id) throws IOException {
		 
		 id = AWSEndPointConstants.SHORT_URL_HOSTNAME + id;
		 
		 Jedis jedis = new Jedis(AWSEndPointConstants.CACHE_REDIS_INSTANCE_ENDPOINT, 6379);
		 String redirectURL = jedis.get(id);
		
		 if(redirectURL == null || redirectURL.isEmpty()){
			 System.out.println("Cache missed...");
			 redirectURL = urlShortnerDAO.getOriginalURLByShortenURL(id);
			 jedis.set(id, redirectURL);
		 }
		 
		 String ipAddress = request.getRemoteAddr();
		 
		 TrendServerUpdater trendServer = new TrendServerUpdater(id, redirectURL, ipAddress, userAgent);
		 trendServer.setDaemon(true);
		 trendServer.start();
		 
		 jedis.close();
		 
		 response.sendRedirect(redirectURL);
	 }
}
