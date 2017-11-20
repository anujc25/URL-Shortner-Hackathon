package cmpe281.hackathon.lrserver.dao;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import cmpe281.hackathon.lrserver.model.URL;

public class URLShortnerDAOImpl implements URLShortnerDAO {
	
	private MongoOperations mongoOps;
	
	public URLShortnerDAOImpl(MongoOperations mongoOps){
		this.mongoOps = mongoOps;
	}
	
	@Override
	public String getOriginalURLByShortenURL(String shortenURL){
		
		Query query = new Query();
		query.addCriteria(Criteria.where("slug").is(shortenURL));
		query.fields().include("destination");

	    URL originalURL = this.mongoOps.findOne(query, URL.class);
	    	    
	    return originalURL.getDestination();
	}
}
